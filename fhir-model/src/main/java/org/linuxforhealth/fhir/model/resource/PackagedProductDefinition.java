/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.annotation.Binding;
import org.linuxforhealth.fhir.model.annotation.Choice;
import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.annotation.Maturity;
import org.linuxforhealth.fhir.model.annotation.ReferenceTarget;
import org.linuxforhealth.fhir.model.annotation.Required;
import org.linuxforhealth.fhir.model.annotation.Summary;
import org.linuxforhealth.fhir.model.type.Attachment;
import org.linuxforhealth.fhir.model.type.BackboneElement;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.CodeableReference;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Duration;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Integer;
import org.linuxforhealth.fhir.model.type.Markdown;
import org.linuxforhealth.fhir.model.type.MarketingStatus;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.String;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.BindingStrength;
import org.linuxforhealth.fhir.model.type.code.StandardsStatus;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * A medically related item or items, in a container or package.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "packagedProductDefinition-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/publication-status",
    expression = "status.exists() implies (status.memberOf('http://hl7.org/fhir/ValueSet/publication-status', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/PackagedProductDefinition",
    generated = true
)
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class PackagedProductDefinition extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final String name;
    @Summary
    @Binding(
        bindingName = "PackageType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A high level categorisation of a package.",
        valueSet = "http://hl7.org/fhir/ValueSet/package-type"
    )
    private final CodeableConcept type;
    @Summary
    @ReferenceTarget({ "MedicinalProductDefinition" })
    private final List<Reference> packageFor;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.PREFERRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status"
    )
    private final CodeableConcept status;
    @Summary
    private final DateTime statusDate;
    @Summary
    private final List<Quantity> containedItemQuantity;
    @Summary
    private final Markdown description;
    @Summary
    private final List<LegalStatusOfSupply> legalStatusOfSupply;
    @Summary
    private final List<MarketingStatus> marketingStatus;
    @Summary
    @Binding(
        bindingName = "PackageCharacteristic",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A characteristic of a package.",
        valueSet = "http://hl7.org/fhir/ValueSet/package-characteristic"
    )
    private final List<CodeableConcept> characteristic;
    @Summary
    private final Boolean copackagedIndicator;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final List<Reference> manufacturer;
    @Summary
    private final Package _package;

    private PackagedProductDefinition(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        name = builder.name;
        type = builder.type;
        packageFor = Collections.unmodifiableList(builder.packageFor);
        status = builder.status;
        statusDate = builder.statusDate;
        containedItemQuantity = Collections.unmodifiableList(builder.containedItemQuantity);
        description = builder.description;
        legalStatusOfSupply = Collections.unmodifiableList(builder.legalStatusOfSupply);
        marketingStatus = Collections.unmodifiableList(builder.marketingStatus);
        characteristic = Collections.unmodifiableList(builder.characteristic);
        copackagedIndicator = builder.copackagedIndicator;
        manufacturer = Collections.unmodifiableList(builder.manufacturer);
        _package = builder._package;
    }

    /**
     * A unique identifier for this package as whole. Unique instance identifiers assigned to a package by manufacturers, 
     * regulators, drug catalogue custodians or other organizations.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A name for this package. Typically what it would be listed as in a drug formulary or catalogue, inventory etc.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A high level category e.g. medicinal product, raw material, shipping/transport container, etc.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * The product that this is a pack for.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPackageFor() {
        return packageFor;
    }

    /**
     * The status within the lifecycle of this item. A high level status, this is not intended to duplicate details carried 
     * elsewhere such as legal status, or authorization or marketing status.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatus() {
        return status;
    }

    /**
     * The date at which the given status became applicable.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getStatusDate() {
        return statusDate;
    }

    /**
     * A total of the complete count of contained items of a particular type/form, independent of sub-packaging or 
     * organization. This can be considered as the pack size. This attribute differs from containedItem.amount in that it can 
     * give a single aggregated count of all tablet types in a pack, even when these are different manufactured items. For 
     * example a pill pack of 21 tablets plus 7 sugar tablets, can be denoted here as '28 tablets'. This attribute is 
     * repeatable so that the different item types in one pack type can be counted (e.g. a count of vials and count of 
     * syringes). Each repeat must have different units, so that it is clear what the different sets of counted items are, 
     * and it is not intended to allow different counts of similar items (e.g. not '2 tubes and 3 tubes'). Repeats are not to 
     * be used to represent different pack sizes (e.g. 20 pack vs. 50 pack) - which would be different instances of this 
     * resource.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Quantity} that may be empty.
     */
    public List<Quantity> getContainedItemQuantity() {
        return containedItemQuantity;
    }

    /**
     * Textual description. Note that this is not the name of the package or product.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * The legal status of supply of the packaged item as classified by the regulator.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link LegalStatusOfSupply} that may be empty.
     */
    public List<LegalStatusOfSupply> getLegalStatusOfSupply() {
        return legalStatusOfSupply;
    }

    /**
     * Allows specifying that an item is on the market for sale, or that it is not available, and the dates and locations 
     * associated.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MarketingStatus} that may be empty.
     */
    public List<MarketingStatus> getMarketingStatus() {
        return marketingStatus;
    }

    /**
     * Allows the key features to be recorded, such as "hospital pack", "nurse prescribable", "calendar pack".
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCharacteristic() {
        return characteristic;
    }

    /**
     * States whether a drug product is supplied with another item such as a diluent or adjuvant.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getCopackagedIndicator() {
        return copackagedIndicator;
    }

    /**
     * Manufacturer of this package type. When there are multiple it means these are all possible manufacturers.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getManufacturer() {
        return manufacturer;
    }

    /**
     * A packaging item, as a container for medically related items, possibly with other packaging items within, or a 
     * packaging component, such as bottle cap (which is not a device or a medication manufactured item).
     * 
     * @return
     *     An immutable object of type {@link Package} that may be null.
     */
    public Package getPackage() {
        return _package;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (name != null) || 
            (type != null) || 
            !packageFor.isEmpty() || 
            (status != null) || 
            (statusDate != null) || 
            !containedItemQuantity.isEmpty() || 
            (description != null) || 
            !legalStatusOfSupply.isEmpty() || 
            !marketingStatus.isEmpty() || 
            !characteristic.isEmpty() || 
            (copackagedIndicator != null) || 
            !manufacturer.isEmpty() || 
            (_package != null);
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
                accept(name, "name", visitor);
                accept(type, "type", visitor);
                accept(packageFor, "packageFor", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(statusDate, "statusDate", visitor);
                accept(containedItemQuantity, "containedItemQuantity", visitor, Quantity.class);
                accept(description, "description", visitor);
                accept(legalStatusOfSupply, "legalStatusOfSupply", visitor, LegalStatusOfSupply.class);
                accept(marketingStatus, "marketingStatus", visitor, MarketingStatus.class);
                accept(characteristic, "characteristic", visitor, CodeableConcept.class);
                accept(copackagedIndicator, "copackagedIndicator", visitor);
                accept(manufacturer, "manufacturer", visitor, Reference.class);
                accept(_package, "package", visitor);
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
        PackagedProductDefinition other = (PackagedProductDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(name, other.name) && 
            Objects.equals(type, other.type) && 
            Objects.equals(packageFor, other.packageFor) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusDate, other.statusDate) && 
            Objects.equals(containedItemQuantity, other.containedItemQuantity) && 
            Objects.equals(description, other.description) && 
            Objects.equals(legalStatusOfSupply, other.legalStatusOfSupply) && 
            Objects.equals(marketingStatus, other.marketingStatus) && 
            Objects.equals(characteristic, other.characteristic) && 
            Objects.equals(copackagedIndicator, other.copackagedIndicator) && 
            Objects.equals(manufacturer, other.manufacturer) && 
            Objects.equals(_package, other._package);
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
                name, 
                type, 
                packageFor, 
                status, 
                statusDate, 
                containedItemQuantity, 
                description, 
                legalStatusOfSupply, 
                marketingStatus, 
                characteristic, 
                copackagedIndicator, 
                manufacturer, 
                _package);
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
        private String name;
        private CodeableConcept type;
        private List<Reference> packageFor = new ArrayList<>();
        private CodeableConcept status;
        private DateTime statusDate;
        private List<Quantity> containedItemQuantity = new ArrayList<>();
        private Markdown description;
        private List<LegalStatusOfSupply> legalStatusOfSupply = new ArrayList<>();
        private List<MarketingStatus> marketingStatus = new ArrayList<>();
        private List<CodeableConcept> characteristic = new ArrayList<>();
        private Boolean copackagedIndicator;
        private List<Reference> manufacturer = new ArrayList<>();
        private Package _package;

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
         * A unique identifier for this package as whole. Unique instance identifiers assigned to a package by manufacturers, 
         * regulators, drug catalogue custodians or other organizations.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     A unique identifier for this package as whole
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
         * A unique identifier for this package as whole. Unique instance identifiers assigned to a package by manufacturers, 
         * regulators, drug catalogue custodians or other organizations.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     A unique identifier for this package as whole
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
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     A name for this package. Typically as listed in a drug formulary, catalogue, inventory etc
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #name(org.linuxforhealth.fhir.model.type.String)
         */
        public Builder name(java.lang.String name) {
            this.name = (name == null) ? null : String.of(name);
            return this;
        }

        /**
         * A name for this package. Typically what it would be listed as in a drug formulary or catalogue, inventory etc.
         * 
         * @param name
         *     A name for this package. Typically as listed in a drug formulary, catalogue, inventory etc
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * A high level category e.g. medicinal product, raw material, shipping/transport container, etc.
         * 
         * @param type
         *     A high level category e.g. medicinal product, raw material, shipping container etc
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * The product that this is a pack for.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductDefinition}</li>
         * </ul>
         * 
         * @param packageFor
         *     The product that this is a pack for
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder packageFor(Reference... packageFor) {
            for (Reference value : packageFor) {
                this.packageFor.add(value);
            }
            return this;
        }

        /**
         * The product that this is a pack for.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductDefinition}</li>
         * </ul>
         * 
         * @param packageFor
         *     The product that this is a pack for
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder packageFor(Collection<Reference> packageFor) {
            this.packageFor = new ArrayList<>(packageFor);
            return this;
        }

        /**
         * The status within the lifecycle of this item. A high level status, this is not intended to duplicate details carried 
         * elsewhere such as legal status, or authorization or marketing status.
         * 
         * @param status
         *     The status within the lifecycle of this item. High level - not intended to duplicate details elsewhere e.g. legal 
         *     status, or authorization/marketing status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CodeableConcept status) {
            this.status = status;
            return this;
        }

        /**
         * The date at which the given status became applicable.
         * 
         * @param statusDate
         *     The date at which the given status became applicable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusDate(DateTime statusDate) {
            this.statusDate = statusDate;
            return this;
        }

        /**
         * A total of the complete count of contained items of a particular type/form, independent of sub-packaging or 
         * organization. This can be considered as the pack size. This attribute differs from containedItem.amount in that it can 
         * give a single aggregated count of all tablet types in a pack, even when these are different manufactured items. For 
         * example a pill pack of 21 tablets plus 7 sugar tablets, can be denoted here as '28 tablets'. This attribute is 
         * repeatable so that the different item types in one pack type can be counted (e.g. a count of vials and count of 
         * syringes). Each repeat must have different units, so that it is clear what the different sets of counted items are, 
         * and it is not intended to allow different counts of similar items (e.g. not '2 tubes and 3 tubes'). Repeats are not to 
         * be used to represent different pack sizes (e.g. 20 pack vs. 50 pack) - which would be different instances of this 
         * resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param containedItemQuantity
         *     A total of the complete count of contained items of a particular type/form, independent of sub-packaging or 
         *     organization. This can be considered as the pack size
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder containedItemQuantity(Quantity... containedItemQuantity) {
            for (Quantity value : containedItemQuantity) {
                this.containedItemQuantity.add(value);
            }
            return this;
        }

        /**
         * A total of the complete count of contained items of a particular type/form, independent of sub-packaging or 
         * organization. This can be considered as the pack size. This attribute differs from containedItem.amount in that it can 
         * give a single aggregated count of all tablet types in a pack, even when these are different manufactured items. For 
         * example a pill pack of 21 tablets plus 7 sugar tablets, can be denoted here as '28 tablets'. This attribute is 
         * repeatable so that the different item types in one pack type can be counted (e.g. a count of vials and count of 
         * syringes). Each repeat must have different units, so that it is clear what the different sets of counted items are, 
         * and it is not intended to allow different counts of similar items (e.g. not '2 tubes and 3 tubes'). Repeats are not to 
         * be used to represent different pack sizes (e.g. 20 pack vs. 50 pack) - which would be different instances of this 
         * resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param containedItemQuantity
         *     A total of the complete count of contained items of a particular type/form, independent of sub-packaging or 
         *     organization. This can be considered as the pack size
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder containedItemQuantity(Collection<Quantity> containedItemQuantity) {
            this.containedItemQuantity = new ArrayList<>(containedItemQuantity);
            return this;
        }

        /**
         * Textual description. Note that this is not the name of the package or product.
         * 
         * @param description
         *     Textual description. Note that this is not the name of the package or product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * The legal status of supply of the packaged item as classified by the regulator.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param legalStatusOfSupply
         *     The legal status of supply of the packaged item as classified by the regulator
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder legalStatusOfSupply(LegalStatusOfSupply... legalStatusOfSupply) {
            for (LegalStatusOfSupply value : legalStatusOfSupply) {
                this.legalStatusOfSupply.add(value);
            }
            return this;
        }

        /**
         * The legal status of supply of the packaged item as classified by the regulator.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param legalStatusOfSupply
         *     The legal status of supply of the packaged item as classified by the regulator
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder legalStatusOfSupply(Collection<LegalStatusOfSupply> legalStatusOfSupply) {
            this.legalStatusOfSupply = new ArrayList<>(legalStatusOfSupply);
            return this;
        }

        /**
         * Allows specifying that an item is on the market for sale, or that it is not available, and the dates and locations 
         * associated.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param marketingStatus
         *     Allows specifying that an item is on the market for sale, or that it is not available, and the dates and locations 
         *     associated
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
         * Allows specifying that an item is on the market for sale, or that it is not available, and the dates and locations 
         * associated.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param marketingStatus
         *     Allows specifying that an item is on the market for sale, or that it is not available, and the dates and locations 
         *     associated
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
         * Allows the key features to be recorded, such as "hospital pack", "nurse prescribable", "calendar pack".
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param characteristic
         *     Allows the key features to be recorded, such as "hospital pack", "nurse prescribable"
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder characteristic(CodeableConcept... characteristic) {
            for (CodeableConcept value : characteristic) {
                this.characteristic.add(value);
            }
            return this;
        }

        /**
         * Allows the key features to be recorded, such as "hospital pack", "nurse prescribable", "calendar pack".
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param characteristic
         *     Allows the key features to be recorded, such as "hospital pack", "nurse prescribable"
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder characteristic(Collection<CodeableConcept> characteristic) {
            this.characteristic = new ArrayList<>(characteristic);
            return this;
        }

        /**
         * Convenience method for setting {@code copackagedIndicator}.
         * 
         * @param copackagedIndicator
         *     If the drug product is supplied with another item such as a diluent or adjuvant
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #copackagedIndicator(org.linuxforhealth.fhir.model.type.Boolean)
         */
        public Builder copackagedIndicator(java.lang.Boolean copackagedIndicator) {
            this.copackagedIndicator = (copackagedIndicator == null) ? null : Boolean.of(copackagedIndicator);
            return this;
        }

        /**
         * States whether a drug product is supplied with another item such as a diluent or adjuvant.
         * 
         * @param copackagedIndicator
         *     If the drug product is supplied with another item such as a diluent or adjuvant
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder copackagedIndicator(Boolean copackagedIndicator) {
            this.copackagedIndicator = copackagedIndicator;
            return this;
        }

        /**
         * Manufacturer of this package type. When there are multiple it means these are all possible manufacturers.
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
         *     Manufacturer of this package type (multiple means these are all possible manufacturers)
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
         * Manufacturer of this package type. When there are multiple it means these are all possible manufacturers.
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
         *     Manufacturer of this package type (multiple means these are all possible manufacturers)
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
         * A packaging item, as a container for medically related items, possibly with other packaging items within, or a 
         * packaging component, such as bottle cap (which is not a device or a medication manufactured item).
         * 
         * @param _package
         *     A packaging item, as a container for medically related items, possibly with other packaging items within, or a 
         *     packaging component, such as bottle cap
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder _package(Package _package) {
            this._package = _package;
            return this;
        }

        /**
         * Build the {@link PackagedProductDefinition}
         * 
         * @return
         *     An immutable object of type {@link PackagedProductDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid PackagedProductDefinition per the base specification
         */
        @Override
        public PackagedProductDefinition build() {
            PackagedProductDefinition packagedProductDefinition = new PackagedProductDefinition(this);
            if (validating) {
                validate(packagedProductDefinition);
            }
            return packagedProductDefinition;
        }

        protected void validate(PackagedProductDefinition packagedProductDefinition) {
            super.validate(packagedProductDefinition);
            ValidationSupport.checkList(packagedProductDefinition.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(packagedProductDefinition.packageFor, "packageFor", Reference.class);
            ValidationSupport.checkList(packagedProductDefinition.containedItemQuantity, "containedItemQuantity", Quantity.class);
            ValidationSupport.checkList(packagedProductDefinition.legalStatusOfSupply, "legalStatusOfSupply", LegalStatusOfSupply.class);
            ValidationSupport.checkList(packagedProductDefinition.marketingStatus, "marketingStatus", MarketingStatus.class);
            ValidationSupport.checkList(packagedProductDefinition.characteristic, "characteristic", CodeableConcept.class);
            ValidationSupport.checkList(packagedProductDefinition.manufacturer, "manufacturer", Reference.class);
            ValidationSupport.checkReferenceType(packagedProductDefinition.packageFor, "packageFor", "MedicinalProductDefinition");
            ValidationSupport.checkReferenceType(packagedProductDefinition.manufacturer, "manufacturer", "Organization");
        }

        protected Builder from(PackagedProductDefinition packagedProductDefinition) {
            super.from(packagedProductDefinition);
            identifier.addAll(packagedProductDefinition.identifier);
            name = packagedProductDefinition.name;
            type = packagedProductDefinition.type;
            packageFor.addAll(packagedProductDefinition.packageFor);
            status = packagedProductDefinition.status;
            statusDate = packagedProductDefinition.statusDate;
            containedItemQuantity.addAll(packagedProductDefinition.containedItemQuantity);
            description = packagedProductDefinition.description;
            legalStatusOfSupply.addAll(packagedProductDefinition.legalStatusOfSupply);
            marketingStatus.addAll(packagedProductDefinition.marketingStatus);
            characteristic.addAll(packagedProductDefinition.characteristic);
            copackagedIndicator = packagedProductDefinition.copackagedIndicator;
            manufacturer.addAll(packagedProductDefinition.manufacturer);
            _package = packagedProductDefinition._package;
            return this;
        }
    }

    /**
     * The legal status of supply of the packaged item as classified by the regulator.
     */
    public static class LegalStatusOfSupply extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "LegalStatusOfSupply",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The prescription supply types appropriate to a medicinal product",
            valueSet = "http://hl7.org/fhir/ValueSet/legal-status-of-supply"
        )
        private final CodeableConcept code;
        @Summary
        @Binding(
            bindingName = "Jurisdiction",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Jurisdiction codes",
            valueSet = "http://hl7.org/fhir/ValueSet/jurisdiction"
        )
        private final CodeableConcept jurisdiction;

        private LegalStatusOfSupply(Builder builder) {
            super(builder);
            code = builder.code;
            jurisdiction = builder.jurisdiction;
        }

        /**
         * The actual status of supply. Conveys in what situation this package type may be supplied for use.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * The place where the legal status of supply applies. When not specified, this indicates it is unknown in this context.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getJurisdiction() {
            return jurisdiction;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (jurisdiction != null);
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
                    accept(code, "code", visitor);
                    accept(jurisdiction, "jurisdiction", visitor);
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
            LegalStatusOfSupply other = (LegalStatusOfSupply) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(jurisdiction, other.jurisdiction);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    jurisdiction);
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
            private CodeableConcept code;
            private CodeableConcept jurisdiction;

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
             * The actual status of supply. Conveys in what situation this package type may be supplied for use.
             * 
             * @param code
             *     The actual status of supply. In what situation this package type may be supplied for use
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * The place where the legal status of supply applies. When not specified, this indicates it is unknown in this context.
             * 
             * @param jurisdiction
             *     The place where the legal status of supply applies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder jurisdiction(CodeableConcept jurisdiction) {
                this.jurisdiction = jurisdiction;
                return this;
            }

            /**
             * Build the {@link LegalStatusOfSupply}
             * 
             * @return
             *     An immutable object of type {@link LegalStatusOfSupply}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid LegalStatusOfSupply per the base specification
             */
            @Override
            public LegalStatusOfSupply build() {
                LegalStatusOfSupply legalStatusOfSupply = new LegalStatusOfSupply(this);
                if (validating) {
                    validate(legalStatusOfSupply);
                }
                return legalStatusOfSupply;
            }

            protected void validate(LegalStatusOfSupply legalStatusOfSupply) {
                super.validate(legalStatusOfSupply);
                ValidationSupport.requireValueOrChildren(legalStatusOfSupply);
            }

            protected Builder from(LegalStatusOfSupply legalStatusOfSupply) {
                super.from(legalStatusOfSupply);
                code = legalStatusOfSupply.code;
                jurisdiction = legalStatusOfSupply.jurisdiction;
                return this;
            }
        }
    }

    /**
     * A packaging item, as a container for medically related items, possibly with other packaging items within, or a 
     * packaging component, such as bottle cap (which is not a device or a medication manufactured item).
     */
    public static class Package extends BackboneElement {
        @Summary
        private final List<Identifier> identifier;
        @Summary
        @Binding(
            bindingName = "PackagingType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A high level categorisation of a package.",
            valueSet = "http://hl7.org/fhir/ValueSet/packaging-type"
        )
        private final CodeableConcept type;
        @Summary
        private final Integer quantity;
        @Summary
        @Binding(
            bindingName = "PackageMaterial",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A material used in the construction of packages and their components.",
            valueSet = "http://hl7.org/fhir/ValueSet/package-material"
        )
        private final List<CodeableConcept> material;
        @Summary
        @Binding(
            bindingName = "PackageMaterial",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A material used in the construction of packages and their components.",
            valueSet = "http://hl7.org/fhir/ValueSet/package-material"
        )
        private final List<CodeableConcept> alternateMaterial;
        @Summary
        private final List<ShelfLifeStorage> shelfLifeStorage;
        @Summary
        @ReferenceTarget({ "Organization" })
        private final List<Reference> manufacturer;
        @Summary
        private final List<Property> property;
        @Summary
        private final List<ContainedItem> containedItem;
        @Summary
        private final List<PackagedProductDefinition.Package> _package;

        private Package(Builder builder) {
            super(builder);
            identifier = Collections.unmodifiableList(builder.identifier);
            type = builder.type;
            quantity = builder.quantity;
            material = Collections.unmodifiableList(builder.material);
            alternateMaterial = Collections.unmodifiableList(builder.alternateMaterial);
            shelfLifeStorage = Collections.unmodifiableList(builder.shelfLifeStorage);
            manufacturer = Collections.unmodifiableList(builder.manufacturer);
            property = Collections.unmodifiableList(builder.property);
            containedItem = Collections.unmodifiableList(builder.containedItem);
            _package = Collections.unmodifiableList(builder._package);
        }

        /**
         * An identifier that is specific to this particular part of the packaging. Including possibly Data Carrier Identifier (a 
         * GS1 barcode).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
         */
        public List<Identifier> getIdentifier() {
            return identifier;
        }

        /**
         * The physical type of the container of the items.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The quantity of this level of packaging in the package that contains it. If specified, the outermost level is always 1.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getQuantity() {
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
         * A possible alternate material for this part of the packaging, that is allowed to be used instead of the usual material 
         * (e.g. different types of plastic for a blister sleeve).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getAlternateMaterial() {
            return alternateMaterial;
        }

        /**
         * Shelf Life and storage information.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ShelfLifeStorage} that may be empty.
         */
        public List<ShelfLifeStorage> getShelfLifeStorage() {
            return shelfLifeStorage;
        }

        /**
         * Manufacturer of this package Item. When there are multiple it means these are all possible manufacturers.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getManufacturer() {
            return manufacturer;
        }

        /**
         * General characteristics of this item.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Property} that may be empty.
         */
        public List<Property> getProperty() {
            return property;
        }

        /**
         * The item(s) within the packaging.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ContainedItem} that may be empty.
         */
        public List<ContainedItem> getContainedItem() {
            return containedItem;
        }

        /**
         * Allows containers (and parts of containers) parwithin containers, still a single packaged product. See also 
         * PackagedProductDefinition.package.containedItem.item(PackagedProductDefinition).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Package} that may be empty.
         */
        public List<PackagedProductDefinition.Package> getPackage() {
            return _package;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !identifier.isEmpty() || 
                (type != null) || 
                (quantity != null) || 
                !material.isEmpty() || 
                !alternateMaterial.isEmpty() || 
                !shelfLifeStorage.isEmpty() || 
                !manufacturer.isEmpty() || 
                !property.isEmpty() || 
                !containedItem.isEmpty() || 
                !_package.isEmpty();
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
                    accept(shelfLifeStorage, "shelfLifeStorage", visitor, ShelfLifeStorage.class);
                    accept(manufacturer, "manufacturer", visitor, Reference.class);
                    accept(property, "property", visitor, Property.class);
                    accept(containedItem, "containedItem", visitor, ContainedItem.class);
                    accept(_package, "package", visitor, PackagedProductDefinition.Package.class);
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
            Package other = (Package) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(type, other.type) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(material, other.material) && 
                Objects.equals(alternateMaterial, other.alternateMaterial) && 
                Objects.equals(shelfLifeStorage, other.shelfLifeStorage) && 
                Objects.equals(manufacturer, other.manufacturer) && 
                Objects.equals(property, other.property) && 
                Objects.equals(containedItem, other.containedItem) && 
                Objects.equals(_package, other._package);
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
                    shelfLifeStorage, 
                    manufacturer, 
                    property, 
                    containedItem, 
                    _package);
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
            private Integer quantity;
            private List<CodeableConcept> material = new ArrayList<>();
            private List<CodeableConcept> alternateMaterial = new ArrayList<>();
            private List<ShelfLifeStorage> shelfLifeStorage = new ArrayList<>();
            private List<Reference> manufacturer = new ArrayList<>();
            private List<Property> property = new ArrayList<>();
            private List<ContainedItem> containedItem = new ArrayList<>();
            private List<PackagedProductDefinition.Package> _package = new ArrayList<>();

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
             * An identifier that is specific to this particular part of the packaging. Including possibly Data Carrier Identifier (a 
             * GS1 barcode).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     An identifier that is specific to this particular part of the packaging. Including possibly a Data Carrier Identifier
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
             * An identifier that is specific to this particular part of the packaging. Including possibly Data Carrier Identifier (a 
             * GS1 barcode).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     An identifier that is specific to this particular part of the packaging. Including possibly a Data Carrier Identifier
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
             * The physical type of the container of the items.
             * 
             * @param type
             *     The physical type of the container of the items
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code quantity}.
             * 
             * @param quantity
             *     The quantity of this level of packaging in the package that contains it (with the outermost level being 1)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #quantity(org.linuxforhealth.fhir.model.type.Integer)
             */
            public Builder quantity(java.lang.Integer quantity) {
                this.quantity = (quantity == null) ? null : Integer.of(quantity);
                return this;
            }

            /**
             * The quantity of this level of packaging in the package that contains it. If specified, the outermost level is always 1.
             * 
             * @param quantity
             *     The quantity of this level of packaging in the package that contains it (with the outermost level being 1)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(Integer quantity) {
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
             * A possible alternate material for this part of the packaging, that is allowed to be used instead of the usual material 
             * (e.g. different types of plastic for a blister sleeve).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param alternateMaterial
             *     A possible alternate material for this part of the packaging, that is allowed to be used instead of the usual material
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
             * A possible alternate material for this part of the packaging, that is allowed to be used instead of the usual material 
             * (e.g. different types of plastic for a blister sleeve).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param alternateMaterial
             *     A possible alternate material for this part of the packaging, that is allowed to be used instead of the usual material
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
            public Builder shelfLifeStorage(ShelfLifeStorage... shelfLifeStorage) {
                for (ShelfLifeStorage value : shelfLifeStorage) {
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
            public Builder shelfLifeStorage(Collection<ShelfLifeStorage> shelfLifeStorage) {
                this.shelfLifeStorage = new ArrayList<>(shelfLifeStorage);
                return this;
            }

            /**
             * Manufacturer of this package Item. When there are multiple it means these are all possible manufacturers.
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
             *     Manufacturer of this package Item (multiple means these are all possible manufacturers)
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
             * Manufacturer of this package Item. When there are multiple it means these are all possible manufacturers.
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
             *     Manufacturer of this package Item (multiple means these are all possible manufacturers)
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
             * General characteristics of this item.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param property
             *     General characteristics of this item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder property(Property... property) {
                for (Property value : property) {
                    this.property.add(value);
                }
                return this;
            }

            /**
             * General characteristics of this item.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param property
             *     General characteristics of this item
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder property(Collection<Property> property) {
                this.property = new ArrayList<>(property);
                return this;
            }

            /**
             * The item(s) within the packaging.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param containedItem
             *     The item(s) within the packaging
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder containedItem(ContainedItem... containedItem) {
                for (ContainedItem value : containedItem) {
                    this.containedItem.add(value);
                }
                return this;
            }

            /**
             * The item(s) within the packaging.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param containedItem
             *     The item(s) within the packaging
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder containedItem(Collection<ContainedItem> containedItem) {
                this.containedItem = new ArrayList<>(containedItem);
                return this;
            }

            /**
             * Allows containers (and parts of containers) parwithin containers, still a single packaged product. See also 
             * PackagedProductDefinition.package.containedItem.item(PackagedProductDefinition).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param _package
             *     Allows containers (and parts of containers) within containers, still a single packaged product
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder _package(PackagedProductDefinition.Package... _package) {
                for (PackagedProductDefinition.Package value : _package) {
                    this._package.add(value);
                }
                return this;
            }

            /**
             * Allows containers (and parts of containers) parwithin containers, still a single packaged product. See also 
             * PackagedProductDefinition.package.containedItem.item(PackagedProductDefinition).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param _package
             *     Allows containers (and parts of containers) within containers, still a single packaged product
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder _package(Collection<PackagedProductDefinition.Package> _package) {
                this._package = new ArrayList<>(_package);
                return this;
            }

            /**
             * Build the {@link Package}
             * 
             * @return
             *     An immutable object of type {@link Package}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Package per the base specification
             */
            @Override
            public Package build() {
                Package _package = new Package(this);
                if (validating) {
                    validate(_package);
                }
                return _package;
            }

            protected void validate(Package _package) {
                super.validate(_package);
                ValidationSupport.checkList(_package.identifier, "identifier", Identifier.class);
                ValidationSupport.checkList(_package.material, "material", CodeableConcept.class);
                ValidationSupport.checkList(_package.alternateMaterial, "alternateMaterial", CodeableConcept.class);
                ValidationSupport.checkList(_package.shelfLifeStorage, "shelfLifeStorage", ShelfLifeStorage.class);
                ValidationSupport.checkList(_package.manufacturer, "manufacturer", Reference.class);
                ValidationSupport.checkList(_package.property, "property", Property.class);
                ValidationSupport.checkList(_package.containedItem, "containedItem", ContainedItem.class);
                ValidationSupport.checkList(_package._package, "package", PackagedProductDefinition.Package.class);
                ValidationSupport.checkReferenceType(_package.manufacturer, "manufacturer", "Organization");
                ValidationSupport.requireValueOrChildren(_package);
            }

            protected Builder from(Package _package) {
                super.from(_package);
                identifier.addAll(_package.identifier);
                type = _package.type;
                quantity = _package.quantity;
                material.addAll(_package.material);
                alternateMaterial.addAll(_package.alternateMaterial);
                shelfLifeStorage.addAll(_package.shelfLifeStorage);
                manufacturer.addAll(_package.manufacturer);
                property.addAll(_package.property);
                containedItem.addAll(_package.containedItem);
                this._package.addAll(_package._package);
                return this;
            }
        }

        /**
         * Shelf Life and storage information.
         */
        public static class ShelfLifeStorage extends BackboneElement {
            @Summary
            private final CodeableConcept type;
            @Summary
            @Choice({ Duration.class, String.class })
            private final Element period;
            @Summary
            private final List<CodeableConcept> specialPrecautionsForStorage;

            private ShelfLifeStorage(Builder builder) {
                super(builder);
                type = builder.type;
                period = builder.period;
                specialPrecautionsForStorage = Collections.unmodifiableList(builder.specialPrecautionsForStorage);
            }

            /**
             * This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal 
             * Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, 
             * etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the 
             * controlled term identifier shall be specified.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * The shelf life time period can be specified using a numerical value for the period of time and its unit of time 
             * measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The 
             * symbol and the symbol identifier shall be used.
             * 
             * @return
             *     An immutable object of type {@link Duration} or {@link String} that may be null.
             */
            public Element getPeriod() {
                return period;
            }

            /**
             * Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary. The controlled 
             * term and the controlled term identifier shall be specified.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getSpecialPrecautionsForStorage() {
                return specialPrecautionsForStorage;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (period != null) || 
                    !specialPrecautionsForStorage.isEmpty();
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
                        accept(type, "type", visitor);
                        accept(period, "period", visitor);
                        accept(specialPrecautionsForStorage, "specialPrecautionsForStorage", visitor, CodeableConcept.class);
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
                ShelfLifeStorage other = (ShelfLifeStorage) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(period, other.period) && 
                    Objects.equals(specialPrecautionsForStorage, other.specialPrecautionsForStorage);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        period, 
                        specialPrecautionsForStorage);
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
                private CodeableConcept type;
                private Element period;
                private List<CodeableConcept> specialPrecautionsForStorage = new ArrayList<>();

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
                 * This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal 
                 * Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, 
                 * etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the 
                 * controlled term identifier shall be specified.
                 * 
                 * @param type
                 *     This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal 
                 *     Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, 
                 *     etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the 
                 *     controlled term identifier shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * Convenience method for setting {@code period} with choice type String.
                 * 
                 * @param period
                 *     The shelf life time period can be specified using a numerical value for the period of time and its unit of time 
                 *     measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The 
                 *     symbol and the symbol identifier shall be used
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #period(Element)
                 */
                public Builder period(java.lang.String period) {
                    this.period = (period == null) ? null : String.of(period);
                    return this;
                }

                /**
                 * The shelf life time period can be specified using a numerical value for the period of time and its unit of time 
                 * measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The 
                 * symbol and the symbol identifier shall be used.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Duration}</li>
                 * <li>{@link String}</li>
                 * </ul>
                 * 
                 * @param period
                 *     The shelf life time period can be specified using a numerical value for the period of time and its unit of time 
                 *     measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The 
                 *     symbol and the symbol identifier shall be used
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder period(Element period) {
                    this.period = period;
                    return this;
                }

                /**
                 * Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary. The controlled 
                 * term and the controlled term identifier shall be specified.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param specialPrecautionsForStorage
                 *     Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary. The controlled 
                 *     term and the controlled term identifier shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder specialPrecautionsForStorage(CodeableConcept... specialPrecautionsForStorage) {
                    for (CodeableConcept value : specialPrecautionsForStorage) {
                        this.specialPrecautionsForStorage.add(value);
                    }
                    return this;
                }

                /**
                 * Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary. The controlled 
                 * term and the controlled term identifier shall be specified.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param specialPrecautionsForStorage
                 *     Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary. The controlled 
                 *     term and the controlled term identifier shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder specialPrecautionsForStorage(Collection<CodeableConcept> specialPrecautionsForStorage) {
                    this.specialPrecautionsForStorage = new ArrayList<>(specialPrecautionsForStorage);
                    return this;
                }

                /**
                 * Build the {@link ShelfLifeStorage}
                 * 
                 * @return
                 *     An immutable object of type {@link ShelfLifeStorage}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid ShelfLifeStorage per the base specification
                 */
                @Override
                public ShelfLifeStorage build() {
                    ShelfLifeStorage shelfLifeStorage = new ShelfLifeStorage(this);
                    if (validating) {
                        validate(shelfLifeStorage);
                    }
                    return shelfLifeStorage;
                }

                protected void validate(ShelfLifeStorage shelfLifeStorage) {
                    super.validate(shelfLifeStorage);
                    ValidationSupport.choiceElement(shelfLifeStorage.period, "period", Duration.class, String.class);
                    ValidationSupport.checkList(shelfLifeStorage.specialPrecautionsForStorage, "specialPrecautionsForStorage", CodeableConcept.class);
                    ValidationSupport.requireValueOrChildren(shelfLifeStorage);
                }

                protected Builder from(ShelfLifeStorage shelfLifeStorage) {
                    super.from(shelfLifeStorage);
                    type = shelfLifeStorage.type;
                    period = shelfLifeStorage.period;
                    specialPrecautionsForStorage.addAll(shelfLifeStorage.specialPrecautionsForStorage);
                    return this;
                }
            }
        }

        /**
         * General characteristics of this item.
         */
        public static class Property extends BackboneElement {
            @Summary
            @Binding(
                bindingName = "ProductCharacteristic",
                strength = BindingStrength.Value.EXAMPLE,
                description = "This value set includes all observable entity codes from SNOMED CT - provided as an exemplar value set.",
                valueSet = "http://hl7.org/fhir/ValueSet/product-characteristic-codes"
            )
            @Required
            private final CodeableConcept type;
            @Summary
            @Choice({ CodeableConcept.class, Quantity.class, Date.class, Boolean.class, Attachment.class })
            private final Element value;

            private Property(Builder builder) {
                super(builder);
                type = builder.type;
                value = builder.value;
            }

            /**
             * A code expressing the type of characteristic.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that is non-null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * A value for the characteristic.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}, {@link Quantity}, {@link Date}, {@link Boolean} or {@link 
             *     Attachment} that may be null.
             */
            public Element getValue() {
                return value;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (value != null);
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
                        accept(type, "type", visitor);
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
                Property other = (Property) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(value, other.value);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
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

            public static class Builder extends BackboneElement.Builder {
                private CodeableConcept type;
                private Element value;

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
                 * A code expressing the type of characteristic.
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     A code expressing the type of characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * Convenience method for setting {@code value} with choice type Date.
                 * 
                 * @param value
                 *     A value for the characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #value(Element)
                 */
                public Builder value(java.time.LocalDate value) {
                    this.value = (value == null) ? null : Date.of(value);
                    return this;
                }

                /**
                 * Convenience method for setting {@code value} with choice type Boolean.
                 * 
                 * @param value
                 *     A value for the characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #value(Element)
                 */
                public Builder value(java.lang.Boolean value) {
                    this.value = (value == null) ? null : Boolean.of(value);
                    return this;
                }

                /**
                 * A value for the characteristic.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link CodeableConcept}</li>
                 * <li>{@link Quantity}</li>
                 * <li>{@link Date}</li>
                 * <li>{@link Boolean}</li>
                 * <li>{@link Attachment}</li>
                 * </ul>
                 * 
                 * @param value
                 *     A value for the characteristic
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(Element value) {
                    this.value = value;
                    return this;
                }

                /**
                 * Build the {@link Property}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Property}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Property per the base specification
                 */
                @Override
                public Property build() {
                    Property property = new Property(this);
                    if (validating) {
                        validate(property);
                    }
                    return property;
                }

                protected void validate(Property property) {
                    super.validate(property);
                    ValidationSupport.requireNonNull(property.type, "type");
                    ValidationSupport.choiceElement(property.value, "value", CodeableConcept.class, Quantity.class, Date.class, Boolean.class, Attachment.class);
                    ValidationSupport.requireValueOrChildren(property);
                }

                protected Builder from(Property property) {
                    super.from(property);
                    type = property.type;
                    value = property.value;
                    return this;
                }
            }
        }

        /**
         * The item(s) within the packaging.
         */
        public static class ContainedItem extends BackboneElement {
            @Summary
            @Required
            private final CodeableReference item;
            @Summary
            private final Quantity amount;

            private ContainedItem(Builder builder) {
                super(builder);
                item = builder.item;
                amount = builder.amount;
            }

            /**
             * The actual item(s) of medication, as manufactured, or a device (typically, but not necessarily, a co-packaged one), or 
             * other medically related item (such as food, biologicals, raw materials, medical fluids, gases etc.), as contained in 
             * the package. This also allows another whole packaged product to be included, which is solely for the case where a 
             * package of other entire packages is wanted - such as a wholesale or distribution pack (for layers within one package, 
             * use PackagedProductDefinition.package.package).
             * 
             * @return
             *     An immutable object of type {@link CodeableReference} that is non-null.
             */
            public CodeableReference getItem() {
                return item;
            }

            /**
             * The number of this type of item within this packaging.
             * 
             * @return
             *     An immutable object of type {@link Quantity} that may be null.
             */
            public Quantity getAmount() {
                return amount;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (item != null) || 
                    (amount != null);
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
                        accept(item, "item", visitor);
                        accept(amount, "amount", visitor);
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
                ContainedItem other = (ContainedItem) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(item, other.item) && 
                    Objects.equals(amount, other.amount);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        item, 
                        amount);
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
                private CodeableReference item;
                private Quantity amount;

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
                 * The actual item(s) of medication, as manufactured, or a device (typically, but not necessarily, a co-packaged one), or 
                 * other medically related item (such as food, biologicals, raw materials, medical fluids, gases etc.), as contained in 
                 * the package. This also allows another whole packaged product to be included, which is solely for the case where a 
                 * package of other entire packages is wanted - such as a wholesale or distribution pack (for layers within one package, 
                 * use PackagedProductDefinition.package.package).
                 * 
                 * <p>This element is required.
                 * 
                 * @param item
                 *     The actual item(s) of medication, as manufactured, or a device, or other medically related item (food, biologicals, 
                 *     raw materials, medical fluids, gases etc.), as contained in the package
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder item(CodeableReference item) {
                    this.item = item;
                    return this;
                }

                /**
                 * The number of this type of item within this packaging.
                 * 
                 * @param amount
                 *     The number of this type of item within this packaging
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder amount(Quantity amount) {
                    this.amount = amount;
                    return this;
                }

                /**
                 * Build the {@link ContainedItem}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>item</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link ContainedItem}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid ContainedItem per the base specification
                 */
                @Override
                public ContainedItem build() {
                    ContainedItem containedItem = new ContainedItem(this);
                    if (validating) {
                        validate(containedItem);
                    }
                    return containedItem;
                }

                protected void validate(ContainedItem containedItem) {
                    super.validate(containedItem);
                    ValidationSupport.requireNonNull(containedItem.item, "item");
                    ValidationSupport.requireValueOrChildren(containedItem);
                }

                protected Builder from(ContainedItem containedItem) {
                    super.from(containedItem);
                    item = containedItem.item;
                    amount = containedItem.amount;
                    return this;
                }
            }
        }
    }
}
