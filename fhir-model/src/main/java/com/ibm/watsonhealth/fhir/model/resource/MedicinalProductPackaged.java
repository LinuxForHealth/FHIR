/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.MarketingStatus;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.ProdCharacteristic;
import com.ibm.watsonhealth.fhir.model.type.ProductShelfLife;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A medicinal product in a container or package.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MedicinalProductPackaged extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Reference> subject;
    private final String description;
    private final CodeableConcept legalStatusOfSupply;
    private final List<MarketingStatus> marketingStatus;
    private final Reference marketingAuthorization;
    private final List<Reference> manufacturer;
    private final List<BatchIdentifier> batchIdentifier;
    private final List<PackageItem> packageItem;

    private volatile int hashCode;

    private MedicinalProductPackaged(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        subject = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.subject, "subject"));
        description = builder.description;
        legalStatusOfSupply = builder.legalStatusOfSupply;
        marketingStatus = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.marketingStatus, "marketingStatus"));
        marketingAuthorization = builder.marketingAuthorization;
        manufacturer = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.manufacturer, "manufacturer"));
        batchIdentifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.batchIdentifier, "batchIdentifier"));
        packageItem = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.packageItem, "packageItem"));
    }

    /**
     * <p>
     * Unique identifier.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The product with this is a pack for.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSubject() {
        return subject;
    }

    /**
     * <p>
     * Textual description.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>
     * The legal status of supply of the medicinal product as classified by the regulator.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getLegalStatusOfSupply() {
        return legalStatusOfSupply;
    }

    /**
     * <p>
     * Marketing information.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MarketingStatus}.
     */
    public List<MarketingStatus> getMarketingStatus() {
        return marketingStatus;
    }

    /**
     * <p>
     * Manufacturer of this Package Item.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getMarketingAuthorization() {
        return marketingAuthorization;
    }

    /**
     * <p>
     * Manufacturer of this Package Item.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getManufacturer() {
        return manufacturer;
    }

    /**
     * <p>
     * Batch numbering.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link BatchIdentifier}.
     */
    public List<BatchIdentifier> getBatchIdentifier() {
        return batchIdentifier;
    }

    /**
     * <p>
     * A packaging item, as a contained for medicine, possibly with other packaging items within.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link PackageItem}.
     */
    public List<PackageItem> getPackageItem() {
        return packageItem;
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
        return new Builder(packageItem).from(this);
    }

    public Builder toBuilder(Collection<PackageItem> packageItem) {
        return new Builder(packageItem).from(this);
    }

    public static Builder builder(Collection<PackageItem> packageItem) {
        return new Builder(packageItem);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final List<PackageItem> packageItem;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> subject = new ArrayList<>();
        private String description;
        private CodeableConcept legalStatusOfSupply;
        private List<MarketingStatus> marketingStatus = new ArrayList<>();
        private Reference marketingAuthorization;
        private List<Reference> manufacturer = new ArrayList<>();
        private List<BatchIdentifier> batchIdentifier = new ArrayList<>();

        private Builder(Collection<PackageItem> packageItem) {
            super();
            this.packageItem = new ArrayList<>(packageItem);
        }

        /**
         * <p>
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * </p>
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(Id id) {
            return (Builder) super.id(id);
        }

        /**
         * <p>
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * </p>
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
         * <p>
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * </p>
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
         * <p>
         * The base language in which the resource is written.
         * </p>
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
         * <p>
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * </p>
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
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * <p>
         * Adds new element(s) to existing list
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
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * Unique identifier.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Unique identifier.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Unique identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * <p>
         * The product with this is a pack for.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The product with this is a pack for.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param subject
         *     The product with this is a pack for
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Collection<Reference> subject) {
            this.subject = new ArrayList<>(subject);
            return this;
        }

        /**
         * <p>
         * Textual description.
         * </p>
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
         * <p>
         * The legal status of supply of the medicinal product as classified by the regulator.
         * </p>
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
         * <p>
         * Marketing information.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Marketing information.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param marketingStatus
         *     Marketing information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder marketingStatus(Collection<MarketingStatus> marketingStatus) {
            this.marketingStatus = new ArrayList<>(marketingStatus);
            return this;
        }

        /**
         * <p>
         * Manufacturer of this Package Item.
         * </p>
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
         * <p>
         * Manufacturer of this Package Item.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Manufacturer of this Package Item.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param manufacturer
         *     Manufacturer of this Package Item
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manufacturer(Collection<Reference> manufacturer) {
            this.manufacturer = new ArrayList<>(manufacturer);
            return this;
        }

        /**
         * <p>
         * Batch numbering.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Batch numbering.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param batchIdentifier
         *     Batch numbering
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder batchIdentifier(Collection<BatchIdentifier> batchIdentifier) {
            this.batchIdentifier = new ArrayList<>(batchIdentifier);
            return this;
        }

        @Override
        public MedicinalProductPackaged build() {
            return new MedicinalProductPackaged(this);
        }

        private Builder from(MedicinalProductPackaged medicinalProductPackaged) {
            id = medicinalProductPackaged.id;
            meta = medicinalProductPackaged.meta;
            implicitRules = medicinalProductPackaged.implicitRules;
            language = medicinalProductPackaged.language;
            text = medicinalProductPackaged.text;
            contained.addAll(medicinalProductPackaged.contained);
            extension.addAll(medicinalProductPackaged.extension);
            modifierExtension.addAll(medicinalProductPackaged.modifierExtension);
            identifier.addAll(medicinalProductPackaged.identifier);
            subject.addAll(medicinalProductPackaged.subject);
            description = medicinalProductPackaged.description;
            legalStatusOfSupply = medicinalProductPackaged.legalStatusOfSupply;
            marketingStatus.addAll(medicinalProductPackaged.marketingStatus);
            marketingAuthorization = medicinalProductPackaged.marketingAuthorization;
            manufacturer.addAll(medicinalProductPackaged.manufacturer);
            batchIdentifier.addAll(medicinalProductPackaged.batchIdentifier);
            return this;
        }
    }

    /**
     * <p>
     * Batch numbering.
     * </p>
     */
    public static class BatchIdentifier extends BackboneElement {
        private final Identifier outerPackaging;
        private final Identifier immediatePackaging;

        private volatile int hashCode;

        private BatchIdentifier(Builder builder) {
            super(builder);
            outerPackaging = ValidationSupport.requireNonNull(builder.outerPackaging, "outerPackaging");
            immediatePackaging = builder.immediatePackaging;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A number appearing on the outer packaging of a specific batch.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getOuterPackaging() {
            return outerPackaging;
        }

        /**
         * <p>
         * A number appearing on the immediate packaging (and not the outer packaging).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
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
            return new Builder(outerPackaging).from(this);
        }

        public Builder toBuilder(Identifier outerPackaging) {
            return new Builder(outerPackaging).from(this);
        }

        public static Builder builder(Identifier outerPackaging) {
            return new Builder(outerPackaging);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Identifier outerPackaging;

            // optional
            private Identifier immediatePackaging;

            private Builder(Identifier outerPackaging) {
                super();
                this.outerPackaging = outerPackaging;
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
             * Adds new element(s) to existing list
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * A number appearing on the immediate packaging (and not the outer packaging).
             * </p>
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

            @Override
            public BatchIdentifier build() {
                return new BatchIdentifier(this);
            }

            private Builder from(BatchIdentifier batchIdentifier) {
                id = batchIdentifier.id;
                extension.addAll(batchIdentifier.extension);
                modifierExtension.addAll(batchIdentifier.modifierExtension);
                immediatePackaging = batchIdentifier.immediatePackaging;
                return this;
            }
        }
    }

    /**
     * <p>
     * A packaging item, as a contained for medicine, possibly with other packaging items within.
     * </p>
     */
    public static class PackageItem extends BackboneElement {
        private final List<Identifier> identifier;
        private final CodeableConcept type;
        private final Quantity quantity;
        private final List<CodeableConcept> material;
        private final List<CodeableConcept> alternateMaterial;
        private final List<Reference> device;
        private final List<Reference> manufacturedItem;
        private final List<MedicinalProductPackaged.PackageItem> packageItem;
        private final ProdCharacteristic physicalCharacteristics;
        private final List<CodeableConcept> otherCharacteristics;
        private final List<ProductShelfLife> shelfLifeStorage;
        private final List<Reference> manufacturer;

        private volatile int hashCode;

        private PackageItem(Builder builder) {
            super(builder);
            identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
            type = ValidationSupport.requireNonNull(builder.type, "type");
            quantity = ValidationSupport.requireNonNull(builder.quantity, "quantity");
            material = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.material, "material"));
            alternateMaterial = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.alternateMaterial, "alternateMaterial"));
            device = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.device, "device"));
            manufacturedItem = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.manufacturedItem, "manufacturedItem"));
            packageItem = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.packageItem, "packageItem"));
            physicalCharacteristics = builder.physicalCharacteristics;
            otherCharacteristics = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.otherCharacteristics, "otherCharacteristics"));
            shelfLifeStorage = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.shelfLifeStorage, "shelfLifeStorage"));
            manufacturer = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.manufacturer, "manufacturer"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Including possibly Data Carrier Identifier.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Identifier}.
         */
        public List<Identifier> getIdentifier() {
            return identifier;
        }

        /**
         * <p>
         * The physical type of the container of the medicine.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * <p>
         * The quantity of this package in the medicinal product, at the current level of packaging. The outermost is always 1.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getQuantity() {
            return quantity;
        }

        /**
         * <p>
         * Material type of the package item.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getMaterial() {
            return material;
        }

        /**
         * <p>
         * A possible alternate material for the packaging.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getAlternateMaterial() {
            return alternateMaterial;
        }

        /**
         * <p>
         * A device accompanying a medicinal product.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getDevice() {
            return device;
        }

        /**
         * <p>
         * The manufactured item as contained in the packaged medicinal product.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getManufacturedItem() {
            return manufacturedItem;
        }

        /**
         * <p>
         * Allows containers within containers.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PackageItem}.
         */
        public List<MedicinalProductPackaged.PackageItem> getPackageItem() {
            return packageItem;
        }

        /**
         * <p>
         * Dimensions, color etc.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ProdCharacteristic}.
         */
        public ProdCharacteristic getPhysicalCharacteristics() {
            return physicalCharacteristics;
        }

        /**
         * <p>
         * Other codeable characteristics.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getOtherCharacteristics() {
            return otherCharacteristics;
        }

        /**
         * <p>
         * Shelf Life and storage information.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ProductShelfLife}.
         */
        public List<ProductShelfLife> getShelfLifeStorage() {
            return shelfLifeStorage;
        }

        /**
         * <p>
         * Manufacturer of this Package Item.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
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
            return new Builder(type, quantity).from(this);
        }

        public Builder toBuilder(CodeableConcept type, Quantity quantity) {
            return new Builder(type, quantity).from(this);
        }

        public static Builder builder(CodeableConcept type, Quantity quantity) {
            return new Builder(type, quantity);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;
            private final Quantity quantity;

            // optional
            private List<Identifier> identifier = new ArrayList<>();
            private List<CodeableConcept> material = new ArrayList<>();
            private List<CodeableConcept> alternateMaterial = new ArrayList<>();
            private List<Reference> device = new ArrayList<>();
            private List<Reference> manufacturedItem = new ArrayList<>();
            private List<MedicinalProductPackaged.PackageItem> packageItem = new ArrayList<>();
            private ProdCharacteristic physicalCharacteristics;
            private List<CodeableConcept> otherCharacteristics = new ArrayList<>();
            private List<ProductShelfLife> shelfLifeStorage = new ArrayList<>();
            private List<Reference> manufacturer = new ArrayList<>();

            private Builder(CodeableConcept type, Quantity quantity) {
                super();
                this.type = type;
                this.quantity = quantity;
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
             * Adds new element(s) to existing list
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * Including possibly Data Carrier Identifier.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Including possibly Data Carrier Identifier.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param identifier
             *     Including possibly Data Carrier Identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Collection<Identifier> identifier) {
                this.identifier = new ArrayList<>(identifier);
                return this;
            }

            /**
             * <p>
             * Material type of the package item.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Material type of the package item.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param material
             *     Material type of the package item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder material(Collection<CodeableConcept> material) {
                this.material = new ArrayList<>(material);
                return this;
            }

            /**
             * <p>
             * A possible alternate material for the packaging.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * A possible alternate material for the packaging.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param alternateMaterial
             *     A possible alternate material for the packaging
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder alternateMaterial(Collection<CodeableConcept> alternateMaterial) {
                this.alternateMaterial = new ArrayList<>(alternateMaterial);
                return this;
            }

            /**
             * <p>
             * A device accompanying a medicinal product.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * A device accompanying a medicinal product.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param device
             *     A device accompanying a medicinal product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder device(Collection<Reference> device) {
                this.device = new ArrayList<>(device);
                return this;
            }

            /**
             * <p>
             * The manufactured item as contained in the packaged medicinal product.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * The manufactured item as contained in the packaged medicinal product.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param manufacturedItem
             *     The manufactured item as contained in the packaged medicinal product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder manufacturedItem(Collection<Reference> manufacturedItem) {
                this.manufacturedItem = new ArrayList<>(manufacturedItem);
                return this;
            }

            /**
             * <p>
             * Allows containers within containers.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Allows containers within containers.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param packageItem
             *     Allows containers within containers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder packageItem(Collection<MedicinalProductPackaged.PackageItem> packageItem) {
                this.packageItem = new ArrayList<>(packageItem);
                return this;
            }

            /**
             * <p>
             * Dimensions, color etc.
             * </p>
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
             * <p>
             * Other codeable characteristics.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Other codeable characteristics.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param otherCharacteristics
             *     Other codeable characteristics
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder otherCharacteristics(Collection<CodeableConcept> otherCharacteristics) {
                this.otherCharacteristics = new ArrayList<>(otherCharacteristics);
                return this;
            }

            /**
             * <p>
             * Shelf Life and storage information.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Shelf Life and storage information.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param shelfLifeStorage
             *     Shelf Life and storage information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder shelfLifeStorage(Collection<ProductShelfLife> shelfLifeStorage) {
                this.shelfLifeStorage = new ArrayList<>(shelfLifeStorage);
                return this;
            }

            /**
             * <p>
             * Manufacturer of this Package Item.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Manufacturer of this Package Item.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param manufacturer
             *     Manufacturer of this Package Item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder manufacturer(Collection<Reference> manufacturer) {
                this.manufacturer = new ArrayList<>(manufacturer);
                return this;
            }

            @Override
            public PackageItem build() {
                return new PackageItem(this);
            }

            private Builder from(PackageItem packageItem) {
                id = packageItem.id;
                extension.addAll(packageItem.extension);
                modifierExtension.addAll(packageItem.modifierExtension);
                identifier.addAll(packageItem.identifier);
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
