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
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.ProdCharacteristic;
import com.ibm.fhir.model.type.ProductShelfLife;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.DeviceNameType;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The characteristics, operational status and capabilities of a medical-related component of a medical device.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DeviceDefinition extends DomainResource {
    private final List<Identifier> identifier;
    private final List<UdiDeviceIdentifier> udiDeviceIdentifier;
    @ReferenceTarget({ "Organization" })
    @Choice({ String.class, Reference.class })
    private final Element manufacturer;
    private final List<DeviceName> deviceName;
    private final String modelNumber;
    @Binding(
        bindingName = "DeviceKind",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Type of device e.g. according to official classification.",
        valueSet = "http://hl7.org/fhir/ValueSet/device-kind"
    )
    private final CodeableConcept type;
    private final List<Specialization> specialization;
    private final List<String> version;
    @Summary
    @Binding(
        bindingName = "Safety",
        strength = BindingStrength.Value.EXAMPLE,
        valueSet = "http://hl7.org/fhir/ValueSet/device-safety"
    )
    private final List<CodeableConcept> safety;
    private final List<ProductShelfLife> shelfLifeStorage;
    private final ProdCharacteristic physicalCharacteristics;
    private final List<CodeableConcept> languageCode;
    private final List<Capability> capability;
    private final List<Property> property;
    @ReferenceTarget({ "Organization" })
    private final Reference owner;
    private final List<ContactPoint> contact;
    private final Uri url;
    private final Uri onlineInformation;
    private final List<Annotation> note;
    private final Quantity quantity;
    @Summary
    @ReferenceTarget({ "DeviceDefinition" })
    private final Reference parentDevice;
    private final List<Material> material;

    private DeviceDefinition(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        udiDeviceIdentifier = Collections.unmodifiableList(builder.udiDeviceIdentifier);
        manufacturer = builder.manufacturer;
        deviceName = Collections.unmodifiableList(builder.deviceName);
        modelNumber = builder.modelNumber;
        type = builder.type;
        specialization = Collections.unmodifiableList(builder.specialization);
        version = Collections.unmodifiableList(builder.version);
        safety = Collections.unmodifiableList(builder.safety);
        shelfLifeStorage = Collections.unmodifiableList(builder.shelfLifeStorage);
        physicalCharacteristics = builder.physicalCharacteristics;
        languageCode = Collections.unmodifiableList(builder.languageCode);
        capability = Collections.unmodifiableList(builder.capability);
        property = Collections.unmodifiableList(builder.property);
        owner = builder.owner;
        contact = Collections.unmodifiableList(builder.contact);
        url = builder.url;
        onlineInformation = builder.onlineInformation;
        note = Collections.unmodifiableList(builder.note);
        quantity = builder.quantity;
        parentDevice = builder.parentDevice;
        material = Collections.unmodifiableList(builder.material);
    }

    /**
     * Unique instance identifiers assigned to a device by the software, manufacturers, other organizations or owners. For 
     * example: handle ID.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
     * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
     * jurisdictions it could have been sold.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UdiDeviceIdentifier} that may be empty.
     */
    public List<UdiDeviceIdentifier> getUdiDeviceIdentifier() {
        return udiDeviceIdentifier;
    }

    /**
     * A name of the manufacturer.
     * 
     * @return
     *     An immutable object of type {@link String} or {@link Reference} that may be null.
     */
    public Element getManufacturer() {
        return manufacturer;
    }

    /**
     * A name given to the device to identify it.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link DeviceName} that may be empty.
     */
    public List<DeviceName> getDeviceName() {
        return deviceName;
    }

    /**
     * The model number for the device.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getModelNumber() {
        return modelNumber;
    }

    /**
     * What kind of device or device system this is.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
     * for the communication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Specialization} that may be empty.
     */
    public List<Specialization> getSpecialization() {
        return specialization;
    }

    /**
     * The available versions of the device, e.g., software versions.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getVersion() {
        return version;
    }

    /**
     * Safety characteristics of the device.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getSafety() {
        return safety;
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
     * Dimensions, color etc.
     * 
     * @return
     *     An immutable object of type {@link ProdCharacteristic} that may be null.
     */
    public ProdCharacteristic getPhysicalCharacteristics() {
        return physicalCharacteristics;
    }

    /**
     * Language code for the human-readable text strings produced by the device (all supported).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getLanguageCode() {
        return languageCode;
    }

    /**
     * Device capabilities.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Capability} that may be empty.
     */
    public List<Capability> getCapability() {
        return capability;
    }

    /**
     * The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Property} that may be empty.
     */
    public List<Property> getProperty() {
        return property;
    }

    /**
     * An organization that is responsible for the provision and ongoing maintenance of the device.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getOwner() {
        return owner;
    }

    /**
     * Contact details for an organization or a particular human that is responsible for the device.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactPoint} that may be empty.
     */
    public List<ContactPoint> getContact() {
        return contact;
    }

    /**
     * A network address on which the device may be contacted directly.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * Access to on-line information about the device.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getOnlineInformation() {
        return onlineInformation;
    }

    /**
     * Descriptive information, usage information or implantation information that is not captured in an existing element.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of 
     * devices in the same package of the medicinal product).
     * 
     * @return
     *     An immutable object of type {@link Quantity} that may be null.
     */
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * The parent device it can be part of.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getParentDevice() {
        return parentDevice;
    }

    /**
     * A substance used to create the material(s) of which the device is made.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Material} that may be empty.
     */
    public List<Material> getMaterial() {
        return material;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !udiDeviceIdentifier.isEmpty() || 
            (manufacturer != null) || 
            !deviceName.isEmpty() || 
            (modelNumber != null) || 
            (type != null) || 
            !specialization.isEmpty() || 
            !version.isEmpty() || 
            !safety.isEmpty() || 
            !shelfLifeStorage.isEmpty() || 
            (physicalCharacteristics != null) || 
            !languageCode.isEmpty() || 
            !capability.isEmpty() || 
            !property.isEmpty() || 
            (owner != null) || 
            !contact.isEmpty() || 
            (url != null) || 
            (onlineInformation != null) || 
            !note.isEmpty() || 
            (quantity != null) || 
            (parentDevice != null) || 
            !material.isEmpty();
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
                accept(udiDeviceIdentifier, "udiDeviceIdentifier", visitor, UdiDeviceIdentifier.class);
                accept(manufacturer, "manufacturer", visitor);
                accept(deviceName, "deviceName", visitor, DeviceName.class);
                accept(modelNumber, "modelNumber", visitor);
                accept(type, "type", visitor);
                accept(specialization, "specialization", visitor, Specialization.class);
                accept(version, "version", visitor, String.class);
                accept(safety, "safety", visitor, CodeableConcept.class);
                accept(shelfLifeStorage, "shelfLifeStorage", visitor, ProductShelfLife.class);
                accept(physicalCharacteristics, "physicalCharacteristics", visitor);
                accept(languageCode, "languageCode", visitor, CodeableConcept.class);
                accept(capability, "capability", visitor, Capability.class);
                accept(property, "property", visitor, Property.class);
                accept(owner, "owner", visitor);
                accept(contact, "contact", visitor, ContactPoint.class);
                accept(url, "url", visitor);
                accept(onlineInformation, "onlineInformation", visitor);
                accept(note, "note", visitor, Annotation.class);
                accept(quantity, "quantity", visitor);
                accept(parentDevice, "parentDevice", visitor);
                accept(material, "material", visitor, Material.class);
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
        DeviceDefinition other = (DeviceDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(udiDeviceIdentifier, other.udiDeviceIdentifier) && 
            Objects.equals(manufacturer, other.manufacturer) && 
            Objects.equals(deviceName, other.deviceName) && 
            Objects.equals(modelNumber, other.modelNumber) && 
            Objects.equals(type, other.type) && 
            Objects.equals(specialization, other.specialization) && 
            Objects.equals(version, other.version) && 
            Objects.equals(safety, other.safety) && 
            Objects.equals(shelfLifeStorage, other.shelfLifeStorage) && 
            Objects.equals(physicalCharacteristics, other.physicalCharacteristics) && 
            Objects.equals(languageCode, other.languageCode) && 
            Objects.equals(capability, other.capability) && 
            Objects.equals(property, other.property) && 
            Objects.equals(owner, other.owner) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(url, other.url) && 
            Objects.equals(onlineInformation, other.onlineInformation) && 
            Objects.equals(note, other.note) && 
            Objects.equals(quantity, other.quantity) && 
            Objects.equals(parentDevice, other.parentDevice) && 
            Objects.equals(material, other.material);
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
                udiDeviceIdentifier, 
                manufacturer, 
                deviceName, 
                modelNumber, 
                type, 
                specialization, 
                version, 
                safety, 
                shelfLifeStorage, 
                physicalCharacteristics, 
                languageCode, 
                capability, 
                property, 
                owner, 
                contact, 
                url, 
                onlineInformation, 
                note, 
                quantity, 
                parentDevice, 
                material);
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
        private List<UdiDeviceIdentifier> udiDeviceIdentifier = new ArrayList<>();
        private Element manufacturer;
        private List<DeviceName> deviceName = new ArrayList<>();
        private String modelNumber;
        private CodeableConcept type;
        private List<Specialization> specialization = new ArrayList<>();
        private List<String> version = new ArrayList<>();
        private List<CodeableConcept> safety = new ArrayList<>();
        private List<ProductShelfLife> shelfLifeStorage = new ArrayList<>();
        private ProdCharacteristic physicalCharacteristics;
        private List<CodeableConcept> languageCode = new ArrayList<>();
        private List<Capability> capability = new ArrayList<>();
        private List<Property> property = new ArrayList<>();
        private Reference owner;
        private List<ContactPoint> contact = new ArrayList<>();
        private Uri url;
        private Uri onlineInformation;
        private List<Annotation> note = new ArrayList<>();
        private Quantity quantity;
        private Reference parentDevice;
        private List<Material> material = new ArrayList<>();

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
         * Unique instance identifiers assigned to a device by the software, manufacturers, other organizations or owners. For 
         * example: handle ID.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Instance identifier
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
         * Unique instance identifiers assigned to a device by the software, manufacturers, other organizations or owners. For 
         * example: handle ID.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Instance identifier
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
         * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
         * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
         * jurisdictions it could have been sold.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param udiDeviceIdentifier
         *     Unique Device Identifier (UDI) Barcode string
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder udiDeviceIdentifier(UdiDeviceIdentifier... udiDeviceIdentifier) {
            for (UdiDeviceIdentifier value : udiDeviceIdentifier) {
                this.udiDeviceIdentifier.add(value);
            }
            return this;
        }

        /**
         * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
         * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
         * jurisdictions it could have been sold.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param udiDeviceIdentifier
         *     Unique Device Identifier (UDI) Barcode string
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder udiDeviceIdentifier(Collection<UdiDeviceIdentifier> udiDeviceIdentifier) {
            this.udiDeviceIdentifier = new ArrayList<>(udiDeviceIdentifier);
            return this;
        }

        /**
         * Convenience method for setting {@code manufacturer} with choice type String.
         * 
         * @param manufacturer
         *     Name of device manufacturer
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #manufacturer(Element)
         */
        public Builder manufacturer(java.lang.String manufacturer) {
            this.manufacturer = (manufacturer == null) ? null : String.of(manufacturer);
            return this;
        }

        /**
         * A name of the manufacturer.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link String}</li>
         * <li>{@link Reference}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param manufacturer
         *     Name of device manufacturer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manufacturer(Element manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        /**
         * A name given to the device to identify it.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param deviceName
         *     A name given to the device to identify it
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder deviceName(DeviceName... deviceName) {
            for (DeviceName value : deviceName) {
                this.deviceName.add(value);
            }
            return this;
        }

        /**
         * A name given to the device to identify it.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param deviceName
         *     A name given to the device to identify it
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder deviceName(Collection<DeviceName> deviceName) {
            this.deviceName = new ArrayList<>(deviceName);
            return this;
        }

        /**
         * Convenience method for setting {@code modelNumber}.
         * 
         * @param modelNumber
         *     The model number for the device
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #modelNumber(com.ibm.fhir.model.type.String)
         */
        public Builder modelNumber(java.lang.String modelNumber) {
            this.modelNumber = (modelNumber == null) ? null : String.of(modelNumber);
            return this;
        }

        /**
         * The model number for the device.
         * 
         * @param modelNumber
         *     The model number for the device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder modelNumber(String modelNumber) {
            this.modelNumber = modelNumber;
            return this;
        }

        /**
         * What kind of device or device system this is.
         * 
         * @param type
         *     What kind of device or device system this is
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
         * for the communication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialization
         *     The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
         *     for the communication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specialization(Specialization... specialization) {
            for (Specialization value : specialization) {
                this.specialization.add(value);
            }
            return this;
        }

        /**
         * The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
         * for the communication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param specialization
         *     The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
         *     for the communication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder specialization(Collection<Specialization> specialization) {
            this.specialization = new ArrayList<>(specialization);
            return this;
        }

        /**
         * Convenience method for setting {@code version}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param version
         *     Available versions
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #version(com.ibm.fhir.model.type.String)
         */
        public Builder version(java.lang.String... version) {
            for (java.lang.String value : version) {
                this.version.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * The available versions of the device, e.g., software versions.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param version
         *     Available versions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String... version) {
            for (String value : version) {
                this.version.add(value);
            }
            return this;
        }

        /**
         * The available versions of the device, e.g., software versions.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param version
         *     Available versions
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder version(Collection<String> version) {
            this.version = new ArrayList<>(version);
            return this;
        }

        /**
         * Safety characteristics of the device.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param safety
         *     Safety characteristics of the device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder safety(CodeableConcept... safety) {
            for (CodeableConcept value : safety) {
                this.safety.add(value);
            }
            return this;
        }

        /**
         * Safety characteristics of the device.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param safety
         *     Safety characteristics of the device
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder safety(Collection<CodeableConcept> safety) {
            this.safety = new ArrayList<>(safety);
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
         * Language code for the human-readable text strings produced by the device (all supported).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param languageCode
         *     Language code for the human-readable text strings produced by the device (all supported)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder languageCode(CodeableConcept... languageCode) {
            for (CodeableConcept value : languageCode) {
                this.languageCode.add(value);
            }
            return this;
        }

        /**
         * Language code for the human-readable text strings produced by the device (all supported).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param languageCode
         *     Language code for the human-readable text strings produced by the device (all supported)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder languageCode(Collection<CodeableConcept> languageCode) {
            this.languageCode = new ArrayList<>(languageCode);
            return this;
        }

        /**
         * Device capabilities.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param capability
         *     Device capabilities
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder capability(Capability... capability) {
            for (Capability value : capability) {
                this.capability.add(value);
            }
            return this;
        }

        /**
         * Device capabilities.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param capability
         *     Device capabilities
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder capability(Collection<Capability> capability) {
            this.capability = new ArrayList<>(capability);
            return this;
        }

        /**
         * The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param property
         *     The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties
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
         * The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param property
         *     The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties
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
         * An organization that is responsible for the provision and ongoing maintenance of the device.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param owner
         *     Organization responsible for device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder owner(Reference owner) {
            this.owner = owner;
            return this;
        }

        /**
         * Contact details for an organization or a particular human that is responsible for the device.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Details for human/organization for support
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(ContactPoint... contact) {
            for (ContactPoint value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * Contact details for an organization or a particular human that is responsible for the device.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Details for human/organization for support
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<ContactPoint> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * A network address on which the device may be contacted directly.
         * 
         * @param url
         *     Network address to contact device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * Access to on-line information about the device.
         * 
         * @param onlineInformation
         *     Access to on-line information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder onlineInformation(Uri onlineInformation) {
            this.onlineInformation = onlineInformation;
            return this;
        }

        /**
         * Descriptive information, usage information or implantation information that is not captured in an existing element.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Device notes and comments
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * Descriptive information, usage information or implantation information that is not captured in an existing element.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Device notes and comments
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of 
         * devices in the same package of the medicinal product).
         * 
         * @param quantity
         *     The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of 
         *     devices in the same package of the medicinal product)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder quantity(Quantity quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * The parent device it can be part of.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link DeviceDefinition}</li>
         * </ul>
         * 
         * @param parentDevice
         *     The parent device it can be part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder parentDevice(Reference parentDevice) {
            this.parentDevice = parentDevice;
            return this;
        }

        /**
         * A substance used to create the material(s) of which the device is made.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param material
         *     A substance used to create the material(s) of which the device is made
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder material(Material... material) {
            for (Material value : material) {
                this.material.add(value);
            }
            return this;
        }

        /**
         * A substance used to create the material(s) of which the device is made.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param material
         *     A substance used to create the material(s) of which the device is made
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder material(Collection<Material> material) {
            this.material = new ArrayList<>(material);
            return this;
        }

        /**
         * Build the {@link DeviceDefinition}
         * 
         * @return
         *     An immutable object of type {@link DeviceDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid DeviceDefinition per the base specification
         */
        @Override
        public DeviceDefinition build() {
            DeviceDefinition deviceDefinition = new DeviceDefinition(this);
            if (validating) {
                validate(deviceDefinition);
            }
            return deviceDefinition;
        }

        protected void validate(DeviceDefinition deviceDefinition) {
            super.validate(deviceDefinition);
            ValidationSupport.checkList(deviceDefinition.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(deviceDefinition.udiDeviceIdentifier, "udiDeviceIdentifier", UdiDeviceIdentifier.class);
            ValidationSupport.choiceElement(deviceDefinition.manufacturer, "manufacturer", String.class, Reference.class);
            ValidationSupport.checkList(deviceDefinition.deviceName, "deviceName", DeviceName.class);
            ValidationSupport.checkList(deviceDefinition.specialization, "specialization", Specialization.class);
            ValidationSupport.checkList(deviceDefinition.version, "version", String.class);
            ValidationSupport.checkList(deviceDefinition.safety, "safety", CodeableConcept.class);
            ValidationSupport.checkList(deviceDefinition.shelfLifeStorage, "shelfLifeStorage", ProductShelfLife.class);
            ValidationSupport.checkList(deviceDefinition.languageCode, "languageCode", CodeableConcept.class);
            ValidationSupport.checkList(deviceDefinition.capability, "capability", Capability.class);
            ValidationSupport.checkList(deviceDefinition.property, "property", Property.class);
            ValidationSupport.checkList(deviceDefinition.contact, "contact", ContactPoint.class);
            ValidationSupport.checkList(deviceDefinition.note, "note", Annotation.class);
            ValidationSupport.checkList(deviceDefinition.material, "material", Material.class);
            ValidationSupport.checkReferenceType(deviceDefinition.manufacturer, "manufacturer", "Organization");
            ValidationSupport.checkReferenceType(deviceDefinition.owner, "owner", "Organization");
            ValidationSupport.checkReferenceType(deviceDefinition.parentDevice, "parentDevice", "DeviceDefinition");
        }

        protected Builder from(DeviceDefinition deviceDefinition) {
            super.from(deviceDefinition);
            identifier.addAll(deviceDefinition.identifier);
            udiDeviceIdentifier.addAll(deviceDefinition.udiDeviceIdentifier);
            manufacturer = deviceDefinition.manufacturer;
            deviceName.addAll(deviceDefinition.deviceName);
            modelNumber = deviceDefinition.modelNumber;
            type = deviceDefinition.type;
            specialization.addAll(deviceDefinition.specialization);
            version.addAll(deviceDefinition.version);
            safety.addAll(deviceDefinition.safety);
            shelfLifeStorage.addAll(deviceDefinition.shelfLifeStorage);
            physicalCharacteristics = deviceDefinition.physicalCharacteristics;
            languageCode.addAll(deviceDefinition.languageCode);
            capability.addAll(deviceDefinition.capability);
            property.addAll(deviceDefinition.property);
            owner = deviceDefinition.owner;
            contact.addAll(deviceDefinition.contact);
            url = deviceDefinition.url;
            onlineInformation = deviceDefinition.onlineInformation;
            note.addAll(deviceDefinition.note);
            quantity = deviceDefinition.quantity;
            parentDevice = deviceDefinition.parentDevice;
            material.addAll(deviceDefinition.material);
            return this;
        }
    }

    /**
     * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
     * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
     * jurisdictions it could have been sold.
     */
    public static class UdiDeviceIdentifier extends BackboneElement {
        @Required
        private final String deviceIdentifier;
        @Required
        private final Uri issuer;
        @Required
        private final Uri jurisdiction;

        private UdiDeviceIdentifier(Builder builder) {
            super(builder);
            deviceIdentifier = builder.deviceIdentifier;
            issuer = builder.issuer;
            jurisdiction = builder.jurisdiction;
        }

        /**
         * The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and 
         * jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getDeviceIdentifier() {
            return deviceIdentifier;
        }

        /**
         * The organization that assigns the identifier algorithm.
         * 
         * @return
         *     An immutable object of type {@link Uri} that is non-null.
         */
        public Uri getIssuer() {
            return issuer;
        }

        /**
         * The jurisdiction to which the deviceIdentifier applies.
         * 
         * @return
         *     An immutable object of type {@link Uri} that is non-null.
         */
        public Uri getJurisdiction() {
            return jurisdiction;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (deviceIdentifier != null) || 
                (issuer != null) || 
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
                    accept(deviceIdentifier, "deviceIdentifier", visitor);
                    accept(issuer, "issuer", visitor);
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
            UdiDeviceIdentifier other = (UdiDeviceIdentifier) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(deviceIdentifier, other.deviceIdentifier) && 
                Objects.equals(issuer, other.issuer) && 
                Objects.equals(jurisdiction, other.jurisdiction);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    deviceIdentifier, 
                    issuer, 
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
            private String deviceIdentifier;
            private Uri issuer;
            private Uri jurisdiction;

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
             * Convenience method for setting {@code deviceIdentifier}.
             * 
             * <p>This element is required.
             * 
             * @param deviceIdentifier
             *     The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and 
             *     jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #deviceIdentifier(com.ibm.fhir.model.type.String)
             */
            public Builder deviceIdentifier(java.lang.String deviceIdentifier) {
                this.deviceIdentifier = (deviceIdentifier == null) ? null : String.of(deviceIdentifier);
                return this;
            }

            /**
             * The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and 
             * jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier.
             * 
             * <p>This element is required.
             * 
             * @param deviceIdentifier
             *     The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and 
             *     jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder deviceIdentifier(String deviceIdentifier) {
                this.deviceIdentifier = deviceIdentifier;
                return this;
            }

            /**
             * The organization that assigns the identifier algorithm.
             * 
             * <p>This element is required.
             * 
             * @param issuer
             *     The organization that assigns the identifier algorithm
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder issuer(Uri issuer) {
                this.issuer = issuer;
                return this;
            }

            /**
             * The jurisdiction to which the deviceIdentifier applies.
             * 
             * <p>This element is required.
             * 
             * @param jurisdiction
             *     The jurisdiction to which the deviceIdentifier applies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder jurisdiction(Uri jurisdiction) {
                this.jurisdiction = jurisdiction;
                return this;
            }

            /**
             * Build the {@link UdiDeviceIdentifier}
             * 
             * <p>Required elements:
             * <ul>
             * <li>deviceIdentifier</li>
             * <li>issuer</li>
             * <li>jurisdiction</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link UdiDeviceIdentifier}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid UdiDeviceIdentifier per the base specification
             */
            @Override
            public UdiDeviceIdentifier build() {
                UdiDeviceIdentifier udiDeviceIdentifier = new UdiDeviceIdentifier(this);
                if (validating) {
                    validate(udiDeviceIdentifier);
                }
                return udiDeviceIdentifier;
            }

            protected void validate(UdiDeviceIdentifier udiDeviceIdentifier) {
                super.validate(udiDeviceIdentifier);
                ValidationSupport.requireNonNull(udiDeviceIdentifier.deviceIdentifier, "deviceIdentifier");
                ValidationSupport.requireNonNull(udiDeviceIdentifier.issuer, "issuer");
                ValidationSupport.requireNonNull(udiDeviceIdentifier.jurisdiction, "jurisdiction");
                ValidationSupport.requireValueOrChildren(udiDeviceIdentifier);
            }

            protected Builder from(UdiDeviceIdentifier udiDeviceIdentifier) {
                super.from(udiDeviceIdentifier);
                deviceIdentifier = udiDeviceIdentifier.deviceIdentifier;
                issuer = udiDeviceIdentifier.issuer;
                jurisdiction = udiDeviceIdentifier.jurisdiction;
                return this;
            }
        }
    }

    /**
     * A name given to the device to identify it.
     */
    public static class DeviceName extends BackboneElement {
        @Required
        private final String name;
        @Binding(
            bindingName = "DeviceNameType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of name the device is referred by.",
            valueSet = "http://hl7.org/fhir/ValueSet/device-nametype|4.0.1"
        )
        @Required
        private final DeviceNameType type;

        private DeviceName(Builder builder) {
            super(builder);
            name = builder.name;
            type = builder.type;
        }

        /**
         * The name of the device.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getName() {
            return name;
        }

        /**
         * The type of deviceName.
         * UDILabelName | UserFriendlyName | PatientReportedName | ManufactureDeviceName | ModelName.
         * 
         * @return
         *     An immutable object of type {@link DeviceNameType} that is non-null.
         */
        public DeviceNameType getType() {
            return type;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (type != null);
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
                    accept(name, "name", visitor);
                    accept(type, "type", visitor);
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
            DeviceName other = (DeviceName) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(type, other.type);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    type);
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
            private String name;
            private DeviceNameType type;

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
             * Convenience method for setting {@code name}.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     The name of the device
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #name(com.ibm.fhir.model.type.String)
             */
            public Builder name(java.lang.String name) {
                this.name = (name == null) ? null : String.of(name);
                return this;
            }

            /**
             * The name of the device.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     The name of the device
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * The type of deviceName.
             * UDILabelName | UserFriendlyName | PatientReportedName | ManufactureDeviceName | ModelName.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     udi-label-name | user-friendly-name | patient-reported-name | manufacturer-name | model-name | other
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(DeviceNameType type) {
                this.type = type;
                return this;
            }

            /**
             * Build the {@link DeviceName}
             * 
             * <p>Required elements:
             * <ul>
             * <li>name</li>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link DeviceName}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid DeviceName per the base specification
             */
            @Override
            public DeviceName build() {
                DeviceName deviceName = new DeviceName(this);
                if (validating) {
                    validate(deviceName);
                }
                return deviceName;
            }

            protected void validate(DeviceName deviceName) {
                super.validate(deviceName);
                ValidationSupport.requireNonNull(deviceName.name, "name");
                ValidationSupport.requireNonNull(deviceName.type, "type");
                ValidationSupport.requireValueOrChildren(deviceName);
            }

            protected Builder from(DeviceName deviceName) {
                super.from(deviceName);
                name = deviceName.name;
                type = deviceName.type;
                return this;
            }
        }
    }

    /**
     * The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
     * for the communication.
     */
    public static class Specialization extends BackboneElement {
        @Required
        private final String systemType;
        private final String version;

        private Specialization(Builder builder) {
            super(builder);
            systemType = builder.systemType;
            version = builder.version;
        }

        /**
         * The standard that is used to operate and communicate.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getSystemType() {
            return systemType;
        }

        /**
         * The version of the standard that is used to operate and communicate.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getVersion() {
            return version;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (systemType != null) || 
                (version != null);
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
                    accept(systemType, "systemType", visitor);
                    accept(version, "version", visitor);
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
            Specialization other = (Specialization) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(systemType, other.systemType) && 
                Objects.equals(version, other.version);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    systemType, 
                    version);
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
            private String systemType;
            private String version;

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
             * Convenience method for setting {@code systemType}.
             * 
             * <p>This element is required.
             * 
             * @param systemType
             *     The standard that is used to operate and communicate
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #systemType(com.ibm.fhir.model.type.String)
             */
            public Builder systemType(java.lang.String systemType) {
                this.systemType = (systemType == null) ? null : String.of(systemType);
                return this;
            }

            /**
             * The standard that is used to operate and communicate.
             * 
             * <p>This element is required.
             * 
             * @param systemType
             *     The standard that is used to operate and communicate
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder systemType(String systemType) {
                this.systemType = systemType;
                return this;
            }

            /**
             * Convenience method for setting {@code version}.
             * 
             * @param version
             *     The version of the standard that is used to operate and communicate
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #version(com.ibm.fhir.model.type.String)
             */
            public Builder version(java.lang.String version) {
                this.version = (version == null) ? null : String.of(version);
                return this;
            }

            /**
             * The version of the standard that is used to operate and communicate.
             * 
             * @param version
             *     The version of the standard that is used to operate and communicate
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder version(String version) {
                this.version = version;
                return this;
            }

            /**
             * Build the {@link Specialization}
             * 
             * <p>Required elements:
             * <ul>
             * <li>systemType</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Specialization}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Specialization per the base specification
             */
            @Override
            public Specialization build() {
                Specialization specialization = new Specialization(this);
                if (validating) {
                    validate(specialization);
                }
                return specialization;
            }

            protected void validate(Specialization specialization) {
                super.validate(specialization);
                ValidationSupport.requireNonNull(specialization.systemType, "systemType");
                ValidationSupport.requireValueOrChildren(specialization);
            }

            protected Builder from(Specialization specialization) {
                super.from(specialization);
                systemType = specialization.systemType;
                version = specialization.version;
                return this;
            }
        }
    }

    /**
     * Device capabilities.
     */
    public static class Capability extends BackboneElement {
        @Required
        private final CodeableConcept type;
        private final List<CodeableConcept> description;

        private Capability(Builder builder) {
            super(builder);
            type = builder.type;
            description = Collections.unmodifiableList(builder.description);
        }

        /**
         * Type of capability.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Description of capability.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getDescription() {
            return description;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                !description.isEmpty();
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
                    accept(description, "description", visitor, CodeableConcept.class);
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
            Capability other = (Capability) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(description, other.description);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    description);
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
            private List<CodeableConcept> description = new ArrayList<>();

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
             * Type of capability.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     Type of capability
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Description of capability.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param description
             *     Description of capability
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(CodeableConcept... description) {
                for (CodeableConcept value : description) {
                    this.description.add(value);
                }
                return this;
            }

            /**
             * Description of capability.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param description
             *     Description of capability
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder description(Collection<CodeableConcept> description) {
                this.description = new ArrayList<>(description);
                return this;
            }

            /**
             * Build the {@link Capability}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Capability}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Capability per the base specification
             */
            @Override
            public Capability build() {
                Capability capability = new Capability(this);
                if (validating) {
                    validate(capability);
                }
                return capability;
            }

            protected void validate(Capability capability) {
                super.validate(capability);
                ValidationSupport.requireNonNull(capability.type, "type");
                ValidationSupport.checkList(capability.description, "description", CodeableConcept.class);
                ValidationSupport.requireValueOrChildren(capability);
            }

            protected Builder from(Capability capability) {
                super.from(capability);
                type = capability.type;
                description.addAll(capability.description);
                return this;
            }
        }
    }

    /**
     * The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.
     */
    public static class Property extends BackboneElement {
        @Required
        private final CodeableConcept type;
        private final List<Quantity> valueQuantity;
        private final List<CodeableConcept> valueCode;

        private Property(Builder builder) {
            super(builder);
            type = builder.type;
            valueQuantity = Collections.unmodifiableList(builder.valueQuantity);
            valueCode = Collections.unmodifiableList(builder.valueCode);
        }

        /**
         * Code that specifies the property DeviceDefinitionPropetyCode (Extensible).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Property value as a quantity.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Quantity} that may be empty.
         */
        public List<Quantity> getValueQuantity() {
            return valueQuantity;
        }

        /**
         * Property value as a code, e.g., NTP4 (synced to NTP).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getValueCode() {
            return valueCode;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                !valueQuantity.isEmpty() || 
                !valueCode.isEmpty();
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
                    accept(valueQuantity, "valueQuantity", visitor, Quantity.class);
                    accept(valueCode, "valueCode", visitor, CodeableConcept.class);
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
                Objects.equals(valueQuantity, other.valueQuantity) && 
                Objects.equals(valueCode, other.valueCode);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    valueQuantity, 
                    valueCode);
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
            private List<Quantity> valueQuantity = new ArrayList<>();
            private List<CodeableConcept> valueCode = new ArrayList<>();

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
             * Code that specifies the property DeviceDefinitionPropetyCode (Extensible).
             * 
             * <p>This element is required.
             * 
             * @param type
             *     Code that specifies the property DeviceDefinitionPropetyCode (Extensible)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Property value as a quantity.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param valueQuantity
             *     Property value as a quantity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder valueQuantity(Quantity... valueQuantity) {
                for (Quantity value : valueQuantity) {
                    this.valueQuantity.add(value);
                }
                return this;
            }

            /**
             * Property value as a quantity.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param valueQuantity
             *     Property value as a quantity
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder valueQuantity(Collection<Quantity> valueQuantity) {
                this.valueQuantity = new ArrayList<>(valueQuantity);
                return this;
            }

            /**
             * Property value as a code, e.g., NTP4 (synced to NTP).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param valueCode
             *     Property value as a code, e.g., NTP4 (synced to NTP)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder valueCode(CodeableConcept... valueCode) {
                for (CodeableConcept value : valueCode) {
                    this.valueCode.add(value);
                }
                return this;
            }

            /**
             * Property value as a code, e.g., NTP4 (synced to NTP).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param valueCode
             *     Property value as a code, e.g., NTP4 (synced to NTP)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder valueCode(Collection<CodeableConcept> valueCode) {
                this.valueCode = new ArrayList<>(valueCode);
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
                ValidationSupport.checkList(property.valueQuantity, "valueQuantity", Quantity.class);
                ValidationSupport.checkList(property.valueCode, "valueCode", CodeableConcept.class);
                ValidationSupport.requireValueOrChildren(property);
            }

            protected Builder from(Property property) {
                super.from(property);
                type = property.type;
                valueQuantity.addAll(property.valueQuantity);
                valueCode.addAll(property.valueCode);
                return this;
            }
        }
    }

    /**
     * A substance used to create the material(s) of which the device is made.
     */
    public static class Material extends BackboneElement {
        @Required
        private final CodeableConcept substance;
        private final Boolean alternate;
        private final Boolean allergenicIndicator;

        private Material(Builder builder) {
            super(builder);
            substance = builder.substance;
            alternate = builder.alternate;
            allergenicIndicator = builder.allergenicIndicator;
        }

        /**
         * The substance.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getSubstance() {
            return substance;
        }

        /**
         * Indicates an alternative material of the device.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getAlternate() {
            return alternate;
        }

        /**
         * Whether the substance is a known or suspected allergen.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getAllergenicIndicator() {
            return allergenicIndicator;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (substance != null) || 
                (alternate != null) || 
                (allergenicIndicator != null);
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
                    accept(substance, "substance", visitor);
                    accept(alternate, "alternate", visitor);
                    accept(allergenicIndicator, "allergenicIndicator", visitor);
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
            Material other = (Material) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(substance, other.substance) && 
                Objects.equals(alternate, other.alternate) && 
                Objects.equals(allergenicIndicator, other.allergenicIndicator);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    substance, 
                    alternate, 
                    allergenicIndicator);
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
            private CodeableConcept substance;
            private Boolean alternate;
            private Boolean allergenicIndicator;

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
             * The substance.
             * 
             * <p>This element is required.
             * 
             * @param substance
             *     The substance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder substance(CodeableConcept substance) {
                this.substance = substance;
                return this;
            }

            /**
             * Convenience method for setting {@code alternate}.
             * 
             * @param alternate
             *     Indicates an alternative material of the device
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #alternate(com.ibm.fhir.model.type.Boolean)
             */
            public Builder alternate(java.lang.Boolean alternate) {
                this.alternate = (alternate == null) ? null : Boolean.of(alternate);
                return this;
            }

            /**
             * Indicates an alternative material of the device.
             * 
             * @param alternate
             *     Indicates an alternative material of the device
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder alternate(Boolean alternate) {
                this.alternate = alternate;
                return this;
            }

            /**
             * Convenience method for setting {@code allergenicIndicator}.
             * 
             * @param allergenicIndicator
             *     Whether the substance is a known or suspected allergen
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #allergenicIndicator(com.ibm.fhir.model.type.Boolean)
             */
            public Builder allergenicIndicator(java.lang.Boolean allergenicIndicator) {
                this.allergenicIndicator = (allergenicIndicator == null) ? null : Boolean.of(allergenicIndicator);
                return this;
            }

            /**
             * Whether the substance is a known or suspected allergen.
             * 
             * @param allergenicIndicator
             *     Whether the substance is a known or suspected allergen
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder allergenicIndicator(Boolean allergenicIndicator) {
                this.allergenicIndicator = allergenicIndicator;
                return this;
            }

            /**
             * Build the {@link Material}
             * 
             * <p>Required elements:
             * <ul>
             * <li>substance</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Material}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Material per the base specification
             */
            @Override
            public Material build() {
                Material material = new Material(this);
                if (validating) {
                    validate(material);
                }
                return material;
            }

            protected void validate(Material material) {
                super.validate(material);
                ValidationSupport.requireNonNull(material.substance, "substance");
                ValidationSupport.requireValueOrChildren(material);
            }

            protected Builder from(Material material) {
                super.from(material);
                substance = material.substance;
                alternate = material.alternate;
                allergenicIndicator = material.allergenicIndicator;
                return this;
            }
        }
    }
}
