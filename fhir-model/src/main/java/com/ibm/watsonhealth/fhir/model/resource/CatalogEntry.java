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

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.CatalogEntryRelationType;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Catalog entries are wrappers that contextualize items included in a catalog.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class CatalogEntry extends DomainResource {
    private final List<Identifier> identifier;
    private final CodeableConcept type;
    private final Boolean orderable;
    private final Reference referencedItem;
    private final List<Identifier> additionalIdentifier;
    private final List<CodeableConcept> classification;
    private final PublicationStatus status;
    private final Period validityPeriod;
    private final DateTime validTo;
    private final DateTime lastUpdated;
    private final List<CodeableConcept> additionalCharacteristic;
    private final List<CodeableConcept> additionalClassification;
    private final List<RelatedEntry> relatedEntry;

    private CatalogEntry(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        type = builder.type;
        orderable = ValidationSupport.requireNonNull(builder.orderable, "orderable");
        referencedItem = ValidationSupport.requireNonNull(builder.referencedItem, "referencedItem");
        additionalIdentifier = Collections.unmodifiableList(builder.additionalIdentifier);
        classification = Collections.unmodifiableList(builder.classification);
        status = builder.status;
        validityPeriod = builder.validityPeriod;
        validTo = builder.validTo;
        lastUpdated = builder.lastUpdated;
        additionalCharacteristic = Collections.unmodifiableList(builder.additionalCharacteristic);
        additionalClassification = Collections.unmodifiableList(builder.additionalClassification);
        relatedEntry = Collections.unmodifiableList(builder.relatedEntry);
    }

    /**
     * <p>
     * Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The type of item - medication, device, service, protocol or other.
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
     * Whether the entry represents an orderable item.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getOrderable() {
        return orderable;
    }

    /**
     * <p>
     * The item in a catalog or definition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getReferencedItem() {
        return referencedItem;
    }

    /**
     * <p>
     * Used in supporting related concepts, e.g. NDC to RxNorm.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getAdditionalIdentifier() {
        return additionalIdentifier;
    }

    /**
     * <p>
     * Classes of devices, or ATC for medication.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getClassification() {
        return classification;
    }

    /**
     * <p>
     * Used to support catalog exchange even for unsupported products, e.g. getting list of medications even if not 
     * prescribable.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus}.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The time period in which this catalog entry is expected to be active.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * <p>
     * The date until which this catalog entry is expected to be active.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getValidTo() {
        return validTo;
    }

    /**
     * <p>
     * Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last 
     * updated.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * <p>
     * Used for examplefor Out of Formulary, or any specifics.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getAdditionalCharacteristic() {
        return additionalCharacteristic;
    }

    /**
     * <p>
     * User for example for ATC classification, or.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getAdditionalClassification() {
        return additionalClassification;
    }

    /**
     * <p>
     * Used for example, to point to a substance, or to a device used to administer a medication.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link RelatedEntry}.
     */
    public List<RelatedEntry> getRelatedEntry() {
        return relatedEntry;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
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
                accept(type, "type", visitor);
                accept(orderable, "orderable", visitor);
                accept(referencedItem, "referencedItem", visitor);
                accept(additionalIdentifier, "additionalIdentifier", visitor, Identifier.class);
                accept(classification, "classification", visitor, CodeableConcept.class);
                accept(status, "status", visitor);
                accept(validityPeriod, "validityPeriod", visitor);
                accept(validTo, "validTo", visitor);
                accept(lastUpdated, "lastUpdated", visitor);
                accept(additionalCharacteristic, "additionalCharacteristic", visitor, CodeableConcept.class);
                accept(additionalClassification, "additionalClassification", visitor, CodeableConcept.class);
                accept(relatedEntry, "relatedEntry", visitor, RelatedEntry.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(orderable, referencedItem);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.type = type;
        builder.additionalIdentifier.addAll(additionalIdentifier);
        builder.classification.addAll(classification);
        builder.status = status;
        builder.validityPeriod = validityPeriod;
        builder.validTo = validTo;
        builder.lastUpdated = lastUpdated;
        builder.additionalCharacteristic.addAll(additionalCharacteristic);
        builder.additionalClassification.addAll(additionalClassification);
        builder.relatedEntry.addAll(relatedEntry);
        return builder;
    }

    public static Builder builder(Boolean orderable, Reference referencedItem) {
        return new Builder(orderable, referencedItem);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Boolean orderable;
        private final Reference referencedItem;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept type;
        private List<Identifier> additionalIdentifier = new ArrayList<>();
        private List<CodeableConcept> classification = new ArrayList<>();
        private PublicationStatus status;
        private Period validityPeriod;
        private DateTime validTo;
        private DateTime lastUpdated;
        private List<CodeableConcept> additionalCharacteristic = new ArrayList<>();
        private List<CodeableConcept> additionalClassification = new ArrayList<>();
        private List<RelatedEntry> relatedEntry = new ArrayList<>();

        private Builder(Boolean orderable, Reference referencedItem) {
            super();
            this.orderable = orderable;
            this.referencedItem = referencedItem;
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code.
         * </p>
         * 
         * @param identifier
         *     Unique identifier of the catalog item
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code.
         * </p>
         * 
         * @param identifier
         *     Unique identifier of the catalog item
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier.addAll(identifier);
            return this;
        }

        /**
         * <p>
         * The type of item - medication, device, service, protocol or other.
         * </p>
         * 
         * @param type
         *     The type of item - medication, device, service, protocol or other
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * <p>
         * Used in supporting related concepts, e.g. NDC to RxNorm.
         * </p>
         * 
         * @param additionalIdentifier
         *     Any additional identifier(s) for the catalog item, in the same granularity or concept
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder additionalIdentifier(Identifier... additionalIdentifier) {
            for (Identifier value : additionalIdentifier) {
                this.additionalIdentifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Used in supporting related concepts, e.g. NDC to RxNorm.
         * </p>
         * 
         * @param additionalIdentifier
         *     Any additional identifier(s) for the catalog item, in the same granularity or concept
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder additionalIdentifier(Collection<Identifier> additionalIdentifier) {
            this.additionalIdentifier.addAll(additionalIdentifier);
            return this;
        }

        /**
         * <p>
         * Classes of devices, or ATC for medication.
         * </p>
         * 
         * @param classification
         *     Classification (category or class) of the item entry
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder classification(CodeableConcept... classification) {
            for (CodeableConcept value : classification) {
                this.classification.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Classes of devices, or ATC for medication.
         * </p>
         * 
         * @param classification
         *     Classification (category or class) of the item entry
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder classification(Collection<CodeableConcept> classification) {
            this.classification.addAll(classification);
            return this;
        }

        /**
         * <p>
         * Used to support catalog exchange even for unsupported products, e.g. getting list of medications even if not 
         * prescribable.
         * </p>
         * 
         * @param status
         *     draft | active | retired | unknown
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder status(PublicationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * The time period in which this catalog entry is expected to be active.
         * </p>
         * 
         * @param validityPeriod
         *     The time period in which this catalog entry is expected to be active
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder validityPeriod(Period validityPeriod) {
            this.validityPeriod = validityPeriod;
            return this;
        }

        /**
         * <p>
         * The date until which this catalog entry is expected to be active.
         * </p>
         * 
         * @param validTo
         *     The date until which this catalog entry is expected to be active
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder validTo(DateTime validTo) {
            this.validTo = validTo;
            return this;
        }

        /**
         * <p>
         * Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last 
         * updated.
         * </p>
         * 
         * @param lastUpdated
         *     When was this catalog last updated
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder lastUpdated(DateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        /**
         * <p>
         * Used for examplefor Out of Formulary, or any specifics.
         * </p>
         * 
         * @param additionalCharacteristic
         *     Additional characteristics of the catalog entry
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder additionalCharacteristic(CodeableConcept... additionalCharacteristic) {
            for (CodeableConcept value : additionalCharacteristic) {
                this.additionalCharacteristic.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Used for examplefor Out of Formulary, or any specifics.
         * </p>
         * 
         * @param additionalCharacteristic
         *     Additional characteristics of the catalog entry
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder additionalCharacteristic(Collection<CodeableConcept> additionalCharacteristic) {
            this.additionalCharacteristic.addAll(additionalCharacteristic);
            return this;
        }

        /**
         * <p>
         * User for example for ATC classification, or.
         * </p>
         * 
         * @param additionalClassification
         *     Additional classification of the catalog entry
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder additionalClassification(CodeableConcept... additionalClassification) {
            for (CodeableConcept value : additionalClassification) {
                this.additionalClassification.add(value);
            }
            return this;
        }

        /**
         * <p>
         * User for example for ATC classification, or.
         * </p>
         * 
         * @param additionalClassification
         *     Additional classification of the catalog entry
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder additionalClassification(Collection<CodeableConcept> additionalClassification) {
            this.additionalClassification.addAll(additionalClassification);
            return this;
        }

        /**
         * <p>
         * Used for example, to point to a substance, or to a device used to administer a medication.
         * </p>
         * 
         * @param relatedEntry
         *     An item that this catalog entry is related to
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relatedEntry(RelatedEntry... relatedEntry) {
            for (RelatedEntry value : relatedEntry) {
                this.relatedEntry.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Used for example, to point to a substance, or to a device used to administer a medication.
         * </p>
         * 
         * @param relatedEntry
         *     An item that this catalog entry is related to
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relatedEntry(Collection<RelatedEntry> relatedEntry) {
            this.relatedEntry.addAll(relatedEntry);
            return this;
        }

        @Override
        public CatalogEntry build() {
            return new CatalogEntry(this);
        }
    }

    /**
     * <p>
     * Used for example, to point to a substance, or to a device used to administer a medication.
     * </p>
     */
    public static class RelatedEntry extends BackboneElement {
        private final CatalogEntryRelationType relationtype;
        private final Reference item;

        private RelatedEntry(Builder builder) {
            super(builder);
            relationtype = ValidationSupport.requireNonNull(builder.relationtype, "relationtype");
            item = ValidationSupport.requireNonNull(builder.item, "item");
        }

        /**
         * <p>
         * The type of relation to the related item: child, parent, packageContent, containerPackage, usedIn, uses, requires, etc.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CatalogEntryRelationType}.
         */
        public CatalogEntryRelationType getRelationtype() {
            return relationtype;
        }

        /**
         * <p>
         * The reference to the related item.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getItem() {
            return item;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(relationtype, "relationtype", visitor);
                    accept(item, "item", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CatalogEntryRelationType relationtype, Reference item) {
            return new Builder(relationtype, item);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CatalogEntryRelationType relationtype;
            private final Reference item;

            private Builder(CatalogEntryRelationType relationtype, Reference item) {
                super();
                this.relationtype = relationtype;
                this.item = item;
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
             *     A reference to this Builder instance.
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            @Override
            public RelatedEntry build() {
                return new RelatedEntry(this);
            }

            private static Builder from(RelatedEntry relatedEntry) {
                Builder builder = new Builder(relatedEntry.relationtype, relatedEntry.item);
                return builder;
            }
        }
    }
}
