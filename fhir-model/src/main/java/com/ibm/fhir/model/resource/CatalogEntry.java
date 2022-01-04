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

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.CatalogEntryRelationType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Catalog entries are wrappers that contextualize items included in a catalog.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CatalogEntry extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    private final CodeableConcept type;
    @Summary
    @Required
    private final Boolean orderable;
    @Summary
    @ReferenceTarget({ "Medication", "Device", "Organization", "Practitioner", "PractitionerRole", "HealthcareService", "ActivityDefinition", "PlanDefinition", "SpecimenDefinition", "ObservationDefinition", "Binary" })
    @Required
    private final Reference referencedItem;
    private final List<Identifier> additionalIdentifier;
    private final List<CodeableConcept> classification;
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.3.0-CIBUILD"
    )
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
        orderable = builder.orderable;
        referencedItem = builder.referencedItem;
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
     * Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The type of item - medication, device, service, protocol or other.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * Whether the entry represents an orderable item.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that is non-null.
     */
    public Boolean getOrderable() {
        return orderable;
    }

    /**
     * The item in a catalog or definition.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getReferencedItem() {
        return referencedItem;
    }

    /**
     * Used in supporting related concepts, e.g. NDC to RxNorm.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getAdditionalIdentifier() {
        return additionalIdentifier;
    }

    /**
     * Classes of devices, or ATC for medication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getClassification() {
        return classification;
    }

    /**
     * Used to support catalog exchange even for unsupported products, e.g. getting list of medications even if not 
     * prescribable.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that may be null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * The time period in which this catalog entry is expected to be active.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * The date until which this catalog entry is expected to be active.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getValidTo() {
        return validTo;
    }

    /**
     * Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last 
     * updated.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Used for examplefor Out of Formulary, or any specifics.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getAdditionalCharacteristic() {
        return additionalCharacteristic;
    }

    /**
     * User for example for ATC classification, or.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getAdditionalClassification() {
        return additionalClassification;
    }

    /**
     * Used for example, to point to a substance, or to a device used to administer a medication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatedEntry} that may be empty.
     */
    public List<RelatedEntry> getRelatedEntry() {
        return relatedEntry;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (type != null) || 
            (orderable != null) || 
            (referencedItem != null) || 
            !additionalIdentifier.isEmpty() || 
            !classification.isEmpty() || 
            (status != null) || 
            (validityPeriod != null) || 
            (validTo != null) || 
            (lastUpdated != null) || 
            !additionalCharacteristic.isEmpty() || 
            !additionalClassification.isEmpty() || 
            !relatedEntry.isEmpty();
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
        CatalogEntry other = (CatalogEntry) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(type, other.type) && 
            Objects.equals(orderable, other.orderable) && 
            Objects.equals(referencedItem, other.referencedItem) && 
            Objects.equals(additionalIdentifier, other.additionalIdentifier) && 
            Objects.equals(classification, other.classification) && 
            Objects.equals(status, other.status) && 
            Objects.equals(validityPeriod, other.validityPeriod) && 
            Objects.equals(validTo, other.validTo) && 
            Objects.equals(lastUpdated, other.lastUpdated) && 
            Objects.equals(additionalCharacteristic, other.additionalCharacteristic) && 
            Objects.equals(additionalClassification, other.additionalClassification) && 
            Objects.equals(relatedEntry, other.relatedEntry);
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
                type, 
                orderable, 
                referencedItem, 
                additionalIdentifier, 
                classification, 
                status, 
                validityPeriod, 
                validTo, 
                lastUpdated, 
                additionalCharacteristic, 
                additionalClassification, 
                relatedEntry);
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
        private CodeableConcept type;
        private Boolean orderable;
        private Reference referencedItem;
        private List<Identifier> additionalIdentifier = new ArrayList<>();
        private List<CodeableConcept> classification = new ArrayList<>();
        private PublicationStatus status;
        private Period validityPeriod;
        private DateTime validTo;
        private DateTime lastUpdated;
        private List<CodeableConcept> additionalCharacteristic = new ArrayList<>();
        private List<CodeableConcept> additionalClassification = new ArrayList<>();
        private List<RelatedEntry> relatedEntry = new ArrayList<>();

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
         * Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique identifier of the catalog item
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
         * Used in supporting different identifiers for the same product, e.g. manufacturer code and retailer code.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique identifier of the catalog item
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
         * The type of item - medication, device, service, protocol or other.
         * 
         * @param type
         *     The type of item - medication, device, service, protocol or other
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * Convenience method for setting {@code orderable}.
         * 
         * <p>This element is required.
         * 
         * @param orderable
         *     Whether the entry represents an orderable item
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #orderable(com.ibm.fhir.model.type.Boolean)
         */
        public Builder orderable(java.lang.Boolean orderable) {
            this.orderable = (orderable == null) ? null : Boolean.of(orderable);
            return this;
        }

        /**
         * Whether the entry represents an orderable item.
         * 
         * <p>This element is required.
         * 
         * @param orderable
         *     Whether the entry represents an orderable item
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder orderable(Boolean orderable) {
            this.orderable = orderable;
            return this;
        }

        /**
         * The item in a catalog or definition.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Medication}</li>
         * <li>{@link Device}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link HealthcareService}</li>
         * <li>{@link ActivityDefinition}</li>
         * <li>{@link PlanDefinition}</li>
         * <li>{@link SpecimenDefinition}</li>
         * <li>{@link ObservationDefinition}</li>
         * <li>{@link Binary}</li>
         * </ul>
         * 
         * @param referencedItem
         *     The item that is being defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder referencedItem(Reference referencedItem) {
            this.referencedItem = referencedItem;
            return this;
        }

        /**
         * Used in supporting related concepts, e.g. NDC to RxNorm.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param additionalIdentifier
         *     Any additional identifier(s) for the catalog item, in the same granularity or concept
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder additionalIdentifier(Identifier... additionalIdentifier) {
            for (Identifier value : additionalIdentifier) {
                this.additionalIdentifier.add(value);
            }
            return this;
        }

        /**
         * Used in supporting related concepts, e.g. NDC to RxNorm.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param additionalIdentifier
         *     Any additional identifier(s) for the catalog item, in the same granularity or concept
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder additionalIdentifier(Collection<Identifier> additionalIdentifier) {
            this.additionalIdentifier = new ArrayList<>(additionalIdentifier);
            return this;
        }

        /**
         * Classes of devices, or ATC for medication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param classification
         *     Classification (category or class) of the item entry
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder classification(CodeableConcept... classification) {
            for (CodeableConcept value : classification) {
                this.classification.add(value);
            }
            return this;
        }

        /**
         * Classes of devices, or ATC for medication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param classification
         *     Classification (category or class) of the item entry
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder classification(Collection<CodeableConcept> classification) {
            this.classification = new ArrayList<>(classification);
            return this;
        }

        /**
         * Used to support catalog exchange even for unsupported products, e.g. getting list of medications even if not 
         * prescribable.
         * 
         * @param status
         *     draft | active | retired | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(PublicationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The time period in which this catalog entry is expected to be active.
         * 
         * @param validityPeriod
         *     The time period in which this catalog entry is expected to be active
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder validityPeriod(Period validityPeriod) {
            this.validityPeriod = validityPeriod;
            return this;
        }

        /**
         * The date until which this catalog entry is expected to be active.
         * 
         * @param validTo
         *     The date until which this catalog entry is expected to be active
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder validTo(DateTime validTo) {
            this.validTo = validTo;
            return this;
        }

        /**
         * Typically date of issue is different from the beginning of the validity. This can be used to see when an item was last 
         * updated.
         * 
         * @param lastUpdated
         *     When was this catalog last updated
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastUpdated(DateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        /**
         * Used for examplefor Out of Formulary, or any specifics.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param additionalCharacteristic
         *     Additional characteristics of the catalog entry
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder additionalCharacteristic(CodeableConcept... additionalCharacteristic) {
            for (CodeableConcept value : additionalCharacteristic) {
                this.additionalCharacteristic.add(value);
            }
            return this;
        }

        /**
         * Used for examplefor Out of Formulary, or any specifics.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param additionalCharacteristic
         *     Additional characteristics of the catalog entry
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder additionalCharacteristic(Collection<CodeableConcept> additionalCharacteristic) {
            this.additionalCharacteristic = new ArrayList<>(additionalCharacteristic);
            return this;
        }

        /**
         * User for example for ATC classification, or.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param additionalClassification
         *     Additional classification of the catalog entry
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder additionalClassification(CodeableConcept... additionalClassification) {
            for (CodeableConcept value : additionalClassification) {
                this.additionalClassification.add(value);
            }
            return this;
        }

        /**
         * User for example for ATC classification, or.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param additionalClassification
         *     Additional classification of the catalog entry
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder additionalClassification(Collection<CodeableConcept> additionalClassification) {
            this.additionalClassification = new ArrayList<>(additionalClassification);
            return this;
        }

        /**
         * Used for example, to point to a substance, or to a device used to administer a medication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedEntry
         *     An item that this catalog entry is related to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relatedEntry(RelatedEntry... relatedEntry) {
            for (RelatedEntry value : relatedEntry) {
                this.relatedEntry.add(value);
            }
            return this;
        }

        /**
         * Used for example, to point to a substance, or to a device used to administer a medication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedEntry
         *     An item that this catalog entry is related to
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder relatedEntry(Collection<RelatedEntry> relatedEntry) {
            this.relatedEntry = new ArrayList<>(relatedEntry);
            return this;
        }

        /**
         * Build the {@link CatalogEntry}
         * 
         * <p>Required elements:
         * <ul>
         * <li>orderable</li>
         * <li>referencedItem</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link CatalogEntry}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid CatalogEntry per the base specification
         */
        @Override
        public CatalogEntry build() {
            CatalogEntry catalogEntry = new CatalogEntry(this);
            if (validating) {
                validate(catalogEntry);
            }
            return catalogEntry;
        }

        protected void validate(CatalogEntry catalogEntry) {
            super.validate(catalogEntry);
            ValidationSupport.checkList(catalogEntry.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(catalogEntry.orderable, "orderable");
            ValidationSupport.requireNonNull(catalogEntry.referencedItem, "referencedItem");
            ValidationSupport.checkList(catalogEntry.additionalIdentifier, "additionalIdentifier", Identifier.class);
            ValidationSupport.checkList(catalogEntry.classification, "classification", CodeableConcept.class);
            ValidationSupport.checkList(catalogEntry.additionalCharacteristic, "additionalCharacteristic", CodeableConcept.class);
            ValidationSupport.checkList(catalogEntry.additionalClassification, "additionalClassification", CodeableConcept.class);
            ValidationSupport.checkList(catalogEntry.relatedEntry, "relatedEntry", RelatedEntry.class);
            ValidationSupport.checkReferenceType(catalogEntry.referencedItem, "referencedItem", "Medication", "Device", "Organization", "Practitioner", "PractitionerRole", "HealthcareService", "ActivityDefinition", "PlanDefinition", "SpecimenDefinition", "ObservationDefinition", "Binary");
        }

        protected Builder from(CatalogEntry catalogEntry) {
            super.from(catalogEntry);
            identifier.addAll(catalogEntry.identifier);
            type = catalogEntry.type;
            orderable = catalogEntry.orderable;
            referencedItem = catalogEntry.referencedItem;
            additionalIdentifier.addAll(catalogEntry.additionalIdentifier);
            classification.addAll(catalogEntry.classification);
            status = catalogEntry.status;
            validityPeriod = catalogEntry.validityPeriod;
            validTo = catalogEntry.validTo;
            lastUpdated = catalogEntry.lastUpdated;
            additionalCharacteristic.addAll(catalogEntry.additionalCharacteristic);
            additionalClassification.addAll(catalogEntry.additionalClassification);
            relatedEntry.addAll(catalogEntry.relatedEntry);
            return this;
        }
    }

    /**
     * Used for example, to point to a substance, or to a device used to administer a medication.
     */
    public static class RelatedEntry extends BackboneElement {
        @Binding(
            bindingName = "CatalogEntryRelationType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of relations between entries.",
            valueSet = "http://hl7.org/fhir/ValueSet/relation-type|4.3.0-CIBUILD"
        )
        @Required
        private final CatalogEntryRelationType relationtype;
        @ReferenceTarget({ "CatalogEntry" })
        @Required
        private final Reference item;

        private RelatedEntry(Builder builder) {
            super(builder);
            relationtype = builder.relationtype;
            item = builder.item;
        }

        /**
         * The type of relation to the related item: child, parent, packageContent, containerPackage, usedIn, uses, requires, etc.
         * 
         * @return
         *     An immutable object of type {@link CatalogEntryRelationType} that is non-null.
         */
        public CatalogEntryRelationType getRelationtype() {
            return relationtype;
        }

        /**
         * The reference to the related item.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getItem() {
            return item;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (relationtype != null) || 
                (item != null);
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
                    accept(relationtype, "relationtype", visitor);
                    accept(item, "item", visitor);
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
            RelatedEntry other = (RelatedEntry) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(relationtype, other.relationtype) && 
                Objects.equals(item, other.item);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    relationtype, 
                    item);
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
            private CatalogEntryRelationType relationtype;
            private Reference item;

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
             * The type of relation to the related item: child, parent, packageContent, containerPackage, usedIn, uses, requires, etc.
             * 
             * <p>This element is required.
             * 
             * @param relationtype
             *     triggers | is-replaced-by
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder relationtype(CatalogEntryRelationType relationtype) {
                this.relationtype = relationtype;
                return this;
            }

            /**
             * The reference to the related item.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link CatalogEntry}</li>
             * </ul>
             * 
             * @param item
             *     The reference to the related item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(Reference item) {
                this.item = item;
                return this;
            }

            /**
             * Build the {@link RelatedEntry}
             * 
             * <p>Required elements:
             * <ul>
             * <li>relationtype</li>
             * <li>item</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link RelatedEntry}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid RelatedEntry per the base specification
             */
            @Override
            public RelatedEntry build() {
                RelatedEntry relatedEntry = new RelatedEntry(this);
                if (validating) {
                    validate(relatedEntry);
                }
                return relatedEntry;
            }

            protected void validate(RelatedEntry relatedEntry) {
                super.validate(relatedEntry);
                ValidationSupport.requireNonNull(relatedEntry.relationtype, "relationtype");
                ValidationSupport.requireNonNull(relatedEntry.item, "item");
                ValidationSupport.checkReferenceType(relatedEntry.item, "item", "CatalogEntry");
                ValidationSupport.requireValueOrChildren(relatedEntry);
            }

            protected Builder from(RelatedEntry relatedEntry) {
                super.from(relatedEntry);
                relationtype = relatedEntry.relationtype;
                item = relatedEntry.item;
                return this;
            }
        }
    }
}
