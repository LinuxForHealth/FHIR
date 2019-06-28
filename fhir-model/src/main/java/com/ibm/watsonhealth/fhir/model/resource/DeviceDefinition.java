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

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.DeviceNameType;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
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
 * The characteristics, operational status and capabilities of a medical-related component of a medical device.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class DeviceDefinition extends DomainResource {
    private final List<Identifier> identifier;
    private final List<UdiDeviceIdentifier> udiDeviceIdentifier;
    private final Element manufacturer;
    private final List<DeviceName> deviceName;
    private final String modelNumber;
    private final CodeableConcept type;
    private final List<Specialization> specialization;
    private final List<String> version;
    private final List<CodeableConcept> safety;
    private final List<ProductShelfLife> shelfLifeStorage;
    private final ProdCharacteristic physicalCharacteristics;
    private final List<CodeableConcept> languageCode;
    private final List<Capability> capability;
    private final List<Property> property;
    private final Reference owner;
    private final List<ContactPoint> contact;
    private final Uri url;
    private final Uri onlineInformation;
    private final List<Annotation> note;
    private final Quantity quantity;
    private final Reference parentDevice;
    private final List<Material> material;

    private DeviceDefinition(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        udiDeviceIdentifier = Collections.unmodifiableList(builder.udiDeviceIdentifier);
        manufacturer = ValidationSupport.choiceElement(builder.manufacturer, "manufacturer", String.class, Reference.class);
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
     * <p>
     * Unique instance identifiers assigned to a device by the software, manufacturers, other organizations or owners. For 
     * example: handle ID.
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
     * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
     * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
     * jurisdictions it could have been sold.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link UdiDeviceIdentifier}.
     */
    public List<UdiDeviceIdentifier> getUdiDeviceIdentifier() {
        return udiDeviceIdentifier;
    }

    /**
     * <p>
     * A name of the manufacturer.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getManufacturer() {
        return manufacturer;
    }

    /**
     * <p>
     * A name given to the device to identify it.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link DeviceName}.
     */
    public List<DeviceName> getDeviceName() {
        return deviceName;
    }

    /**
     * <p>
     * The model number for the device.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getModelNumber() {
        return modelNumber;
    }

    /**
     * <p>
     * What kind of device or device system this is.
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
     * The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
     * for the communication.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Specialization}.
     */
    public List<Specialization> getSpecialization() {
        return specialization;
    }

    /**
     * <p>
     * The available versions of the device, e.g., software versions.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link String}.
     */
    public List<String> getVersion() {
        return version;
    }

    /**
     * <p>
     * Safety characteristics of the device.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getSafety() {
        return safety;
    }

    /**
     * <p>
     * Shelf Life and storage information.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ProductShelfLife}.
     */
    public List<ProductShelfLife> getShelfLifeStorage() {
        return shelfLifeStorage;
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
     * Language code for the human-readable text strings produced by the device (all supported).
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getLanguageCode() {
        return languageCode;
    }

    /**
     * <p>
     * Device capabilities.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Capability}.
     */
    public List<Capability> getCapability() {
        return capability;
    }

    /**
     * <p>
     * The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Property}.
     */
    public List<Property> getProperty() {
        return property;
    }

    /**
     * <p>
     * An organization that is responsible for the provision and ongoing maintenance of the device.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getOwner() {
        return owner;
    }

    /**
     * <p>
     * Contact details for an organization or a particular human that is responsible for the device.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ContactPoint}.
     */
    public List<ContactPoint> getContact() {
        return contact;
    }

    /**
     * <p>
     * A network address on which the device may be contacted directly.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * <p>
     * Access to on-line information about the device.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getOnlineInformation() {
        return onlineInformation;
    }

    /**
     * <p>
     * Descriptive information, usage information or implantation information that is not captured in an existing element.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * <p>
     * The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of 
     * devices in the same package of the medicinal product).
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
     * The parent device it can be part of.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getParentDevice() {
        return parentDevice;
    }

    /**
     * <p>
     * A substance used to create the material(s) of which the device is made.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Material}.
     */
    public List<Material> getMaterial() {
        return material;
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
                accept(udiDeviceIdentifier, "udiDeviceIdentifier", visitor, UdiDeviceIdentifier.class);
                accept(manufacturer, "manufacturer", visitor, true);
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.udiDeviceIdentifier.addAll(udiDeviceIdentifier);
        builder.manufacturer = manufacturer;
        builder.deviceName.addAll(deviceName);
        builder.modelNumber = modelNumber;
        builder.type = type;
        builder.specialization.addAll(specialization);
        builder.version.addAll(version);
        builder.safety.addAll(safety);
        builder.shelfLifeStorage.addAll(shelfLifeStorage);
        builder.physicalCharacteristics = physicalCharacteristics;
        builder.languageCode.addAll(languageCode);
        builder.capability.addAll(capability);
        builder.property.addAll(property);
        builder.owner = owner;
        builder.contact.addAll(contact);
        builder.url = url;
        builder.onlineInformation = onlineInformation;
        builder.note.addAll(note);
        builder.quantity = quantity;
        builder.parentDevice = parentDevice;
        builder.material.addAll(material);
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        // optional
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
         * Unique instance identifiers assigned to a device by the software, manufacturers, other organizations or owners. For 
         * example: handle ID.
         * </p>
         * 
         * @param identifier
         *     Instance identifier
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
         * Unique instance identifiers assigned to a device by the software, manufacturers, other organizations or owners. For 
         * example: handle ID.
         * </p>
         * 
         * @param identifier
         *     Instance identifier
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
         * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
         * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
         * jurisdictions it could have been sold.
         * </p>
         * 
         * @param udiDeviceIdentifier
         *     Unique Device Identifier (UDI) Barcode string
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder udiDeviceIdentifier(UdiDeviceIdentifier... udiDeviceIdentifier) {
            for (UdiDeviceIdentifier value : udiDeviceIdentifier) {
                this.udiDeviceIdentifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
         * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
         * jurisdictions it could have been sold.
         * </p>
         * 
         * @param udiDeviceIdentifier
         *     Unique Device Identifier (UDI) Barcode string
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder udiDeviceIdentifier(Collection<UdiDeviceIdentifier> udiDeviceIdentifier) {
            this.udiDeviceIdentifier.addAll(udiDeviceIdentifier);
            return this;
        }

        /**
         * <p>
         * A name of the manufacturer.
         * </p>
         * 
         * @param manufacturer
         *     Name of device manufacturer
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder manufacturer(Element manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        /**
         * <p>
         * A name given to the device to identify it.
         * </p>
         * 
         * @param deviceName
         *     A name given to the device to identify it
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder deviceName(DeviceName... deviceName) {
            for (DeviceName value : deviceName) {
                this.deviceName.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A name given to the device to identify it.
         * </p>
         * 
         * @param deviceName
         *     A name given to the device to identify it
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder deviceName(Collection<DeviceName> deviceName) {
            this.deviceName.addAll(deviceName);
            return this;
        }

        /**
         * <p>
         * The model number for the device.
         * </p>
         * 
         * @param modelNumber
         *     The model number for the device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder modelNumber(String modelNumber) {
            this.modelNumber = modelNumber;
            return this;
        }

        /**
         * <p>
         * What kind of device or device system this is.
         * </p>
         * 
         * @param type
         *     What kind of device or device system this is
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
         * The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
         * for the communication.
         * </p>
         * 
         * @param specialization
         *     The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
         *     for the communication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder specialization(Specialization... specialization) {
            for (Specialization value : specialization) {
                this.specialization.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
         * for the communication.
         * </p>
         * 
         * @param specialization
         *     The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
         *     for the communication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder specialization(Collection<Specialization> specialization) {
            this.specialization.addAll(specialization);
            return this;
        }

        /**
         * <p>
         * The available versions of the device, e.g., software versions.
         * </p>
         * 
         * @param version
         *     Available versions
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder version(String... version) {
            for (String value : version) {
                this.version.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The available versions of the device, e.g., software versions.
         * </p>
         * 
         * @param version
         *     Available versions
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder version(Collection<String> version) {
            this.version.addAll(version);
            return this;
        }

        /**
         * <p>
         * Safety characteristics of the device.
         * </p>
         * 
         * @param safety
         *     Safety characteristics of the device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder safety(CodeableConcept... safety) {
            for (CodeableConcept value : safety) {
                this.safety.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Safety characteristics of the device.
         * </p>
         * 
         * @param safety
         *     Safety characteristics of the device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder safety(Collection<CodeableConcept> safety) {
            this.safety.addAll(safety);
            return this;
        }

        /**
         * <p>
         * Shelf Life and storage information.
         * </p>
         * 
         * @param shelfLifeStorage
         *     Shelf Life and storage information
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param shelfLifeStorage
         *     Shelf Life and storage information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder shelfLifeStorage(Collection<ProductShelfLife> shelfLifeStorage) {
            this.shelfLifeStorage.addAll(shelfLifeStorage);
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
         *     A reference to this Builder instance.
         */
        public Builder physicalCharacteristics(ProdCharacteristic physicalCharacteristics) {
            this.physicalCharacteristics = physicalCharacteristics;
            return this;
        }

        /**
         * <p>
         * Language code for the human-readable text strings produced by the device (all supported).
         * </p>
         * 
         * @param languageCode
         *     Language code for the human-readable text strings produced by the device (all supported)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder languageCode(CodeableConcept... languageCode) {
            for (CodeableConcept value : languageCode) {
                this.languageCode.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Language code for the human-readable text strings produced by the device (all supported).
         * </p>
         * 
         * @param languageCode
         *     Language code for the human-readable text strings produced by the device (all supported)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder languageCode(Collection<CodeableConcept> languageCode) {
            this.languageCode.addAll(languageCode);
            return this;
        }

        /**
         * <p>
         * Device capabilities.
         * </p>
         * 
         * @param capability
         *     Device capabilities
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder capability(Capability... capability) {
            for (Capability value : capability) {
                this.capability.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Device capabilities.
         * </p>
         * 
         * @param capability
         *     Device capabilities
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder capability(Collection<Capability> capability) {
            this.capability.addAll(capability);
            return this;
        }

        /**
         * <p>
         * The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.
         * </p>
         * 
         * @param property
         *     The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder property(Property... property) {
            for (Property value : property) {
                this.property.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.
         * </p>
         * 
         * @param property
         *     The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder property(Collection<Property> property) {
            this.property.addAll(property);
            return this;
        }

        /**
         * <p>
         * An organization that is responsible for the provision and ongoing maintenance of the device.
         * </p>
         * 
         * @param owner
         *     Organization responsible for device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder owner(Reference owner) {
            this.owner = owner;
            return this;
        }

        /**
         * <p>
         * Contact details for an organization or a particular human that is responsible for the device.
         * </p>
         * 
         * @param contact
         *     Details for human/organization for support
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(ContactPoint... contact) {
            for (ContactPoint value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Contact details for an organization or a particular human that is responsible for the device.
         * </p>
         * 
         * @param contact
         *     Details for human/organization for support
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(Collection<ContactPoint> contact) {
            this.contact.addAll(contact);
            return this;
        }

        /**
         * <p>
         * A network address on which the device may be contacted directly.
         * </p>
         * 
         * @param url
         *     Network address to contact device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * <p>
         * Access to on-line information about the device.
         * </p>
         * 
         * @param onlineInformation
         *     Access to on-line information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder onlineInformation(Uri onlineInformation) {
            this.onlineInformation = onlineInformation;
            return this;
        }

        /**
         * <p>
         * Descriptive information, usage information or implantation information that is not captured in an existing element.
         * </p>
         * 
         * @param note
         *     Device notes and comments
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Descriptive information, usage information or implantation information that is not captured in an existing element.
         * </p>
         * 
         * @param note
         *     Device notes and comments
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        /**
         * <p>
         * The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of 
         * devices in the same package of the medicinal product).
         * </p>
         * 
         * @param quantity
         *     The quantity of the device present in the packaging (e.g. the number of devices present in a pack, or the number of 
         *     devices in the same package of the medicinal product)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder quantity(Quantity quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * <p>
         * The parent device it can be part of.
         * </p>
         * 
         * @param parentDevice
         *     The parent device it can be part of
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parentDevice(Reference parentDevice) {
            this.parentDevice = parentDevice;
            return this;
        }

        /**
         * <p>
         * A substance used to create the material(s) of which the device is made.
         * </p>
         * 
         * @param material
         *     A substance used to create the material(s) of which the device is made
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder material(Material... material) {
            for (Material value : material) {
                this.material.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A substance used to create the material(s) of which the device is made.
         * </p>
         * 
         * @param material
         *     A substance used to create the material(s) of which the device is made
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder material(Collection<Material> material) {
            this.material.addAll(material);
            return this;
        }

        @Override
        public DeviceDefinition build() {
            return new DeviceDefinition(this);
        }
    }

    /**
     * <p>
     * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
     * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
     * jurisdictions it could have been sold.
     * </p>
     */
    public static class UdiDeviceIdentifier extends BackboneElement {
        private final String deviceIdentifier;
        private final Uri issuer;
        private final Uri jurisdiction;

        private UdiDeviceIdentifier(Builder builder) {
            super(builder);
            deviceIdentifier = ValidationSupport.requireNonNull(builder.deviceIdentifier, "deviceIdentifier");
            issuer = ValidationSupport.requireNonNull(builder.issuer, "issuer");
            jurisdiction = ValidationSupport.requireNonNull(builder.jurisdiction, "jurisdiction");
        }

        /**
         * <p>
         * The identifier that is to be associated with every Device that references this DeviceDefintiion for the issuer and 
         * jurisdication porvided in the DeviceDefinition.udiDeviceIdentifier.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDeviceIdentifier() {
            return deviceIdentifier;
        }

        /**
         * <p>
         * The organization that assigns the identifier algorithm.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getIssuer() {
            return issuer;
        }

        /**
         * <p>
         * The jurisdiction to which the deviceIdentifier applies.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getJurisdiction() {
            return jurisdiction;
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
                    accept(deviceIdentifier, "deviceIdentifier", visitor);
                    accept(issuer, "issuer", visitor);
                    accept(jurisdiction, "jurisdiction", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(String deviceIdentifier, Uri issuer, Uri jurisdiction) {
            return new Builder(deviceIdentifier, issuer, jurisdiction);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String deviceIdentifier;
            private final Uri issuer;
            private final Uri jurisdiction;

            private Builder(String deviceIdentifier, Uri issuer, Uri jurisdiction) {
                super();
                this.deviceIdentifier = deviceIdentifier;
                this.issuer = issuer;
                this.jurisdiction = jurisdiction;
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
            public UdiDeviceIdentifier build() {
                return new UdiDeviceIdentifier(this);
            }

            private static Builder from(UdiDeviceIdentifier udiDeviceIdentifier) {
                Builder builder = new Builder(udiDeviceIdentifier.deviceIdentifier, udiDeviceIdentifier.issuer, udiDeviceIdentifier.jurisdiction);
                return builder;
            }
        }
    }

    /**
     * <p>
     * A name given to the device to identify it.
     * </p>
     */
    public static class DeviceName extends BackboneElement {
        private final String name;
        private final DeviceNameType type;

        private DeviceName(Builder builder) {
            super(builder);
            name = ValidationSupport.requireNonNull(builder.name, "name");
            type = ValidationSupport.requireNonNull(builder.type, "type");
        }

        /**
         * <p>
         * The name of the device.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getName() {
            return name;
        }

        /**
         * <p>
         * The type of deviceName.
         * UDILabelName | UserFriendlyName | PatientReportedName | ManufactureDeviceName | ModelName.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DeviceNameType}.
         */
        public DeviceNameType getType() {
            return type;
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
                    accept(name, "name", visitor);
                    accept(type, "type", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(String name, DeviceNameType type) {
            return new Builder(name, type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String name;
            private final DeviceNameType type;

            private Builder(String name, DeviceNameType type) {
                super();
                this.name = name;
                this.type = type;
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
            public DeviceName build() {
                return new DeviceName(this);
            }

            private static Builder from(DeviceName deviceName) {
                Builder builder = new Builder(deviceName.name, deviceName.type);
                return builder;
            }
        }
    }

    /**
     * <p>
     * The capabilities supported on a device, the standards to which the device conforms for a particular purpose, and used 
     * for the communication.
     * </p>
     */
    public static class Specialization extends BackboneElement {
        private final String systemType;
        private final String version;

        private Specialization(Builder builder) {
            super(builder);
            systemType = ValidationSupport.requireNonNull(builder.systemType, "systemType");
            version = builder.version;
        }

        /**
         * <p>
         * The standard that is used to operate and communicate.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getSystemType() {
            return systemType;
        }

        /**
         * <p>
         * The version of the standard that is used to operate and communicate.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getVersion() {
            return version;
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
                    accept(systemType, "systemType", visitor);
                    accept(version, "version", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(String systemType) {
            return new Builder(systemType);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String systemType;

            // optional
            private String version;

            private Builder(String systemType) {
                super();
                this.systemType = systemType;
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

            /**
             * <p>
             * The version of the standard that is used to operate and communicate.
             * </p>
             * 
             * @param version
             *     The version of the standard that is used to operate and communicate
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder version(String version) {
                this.version = version;
                return this;
            }

            @Override
            public Specialization build() {
                return new Specialization(this);
            }

            private static Builder from(Specialization specialization) {
                Builder builder = new Builder(specialization.systemType);
                builder.version = specialization.version;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Device capabilities.
     * </p>
     */
    public static class Capability extends BackboneElement {
        private final CodeableConcept type;
        private final List<CodeableConcept> description;

        private Capability(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            description = Collections.unmodifiableList(builder.description);
        }

        /**
         * <p>
         * Type of capability.
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
         * Description of capability.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getDescription() {
            return description;
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
                    accept(type, "type", visitor);
                    accept(description, "description", visitor, CodeableConcept.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept type) {
            return new Builder(type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;

            // optional
            private List<CodeableConcept> description = new ArrayList<>();

            private Builder(CodeableConcept type) {
                super();
                this.type = type;
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

            /**
             * <p>
             * Description of capability.
             * </p>
             * 
             * @param description
             *     Description of capability
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder description(CodeableConcept... description) {
                for (CodeableConcept value : description) {
                    this.description.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Description of capability.
             * </p>
             * 
             * @param description
             *     Description of capability
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder description(Collection<CodeableConcept> description) {
                this.description.addAll(description);
                return this;
            }

            @Override
            public Capability build() {
                return new Capability(this);
            }

            private static Builder from(Capability capability) {
                Builder builder = new Builder(capability.type);
                builder.description.addAll(capability.description);
                return builder;
            }
        }
    }

    /**
     * <p>
     * The actual configuration settings of a device as it actually operates, e.g., regulation status, time properties.
     * </p>
     */
    public static class Property extends BackboneElement {
        private final CodeableConcept type;
        private final List<Quantity> valueQuantity;
        private final List<CodeableConcept> valueCode;

        private Property(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            valueQuantity = Collections.unmodifiableList(builder.valueQuantity);
            valueCode = Collections.unmodifiableList(builder.valueCode);
        }

        /**
         * <p>
         * Code that specifies the property DeviceDefinitionPropetyCode (Extensible).
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
         * Property value as a quantity.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Quantity}.
         */
        public List<Quantity> getValueQuantity() {
            return valueQuantity;
        }

        /**
         * <p>
         * Property value as a code, e.g., NTP4 (synced to NTP).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getValueCode() {
            return valueCode;
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
                    accept(type, "type", visitor);
                    accept(valueQuantity, "valueQuantity", visitor, Quantity.class);
                    accept(valueCode, "valueCode", visitor, CodeableConcept.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept type) {
            return new Builder(type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept type;

            // optional
            private List<Quantity> valueQuantity = new ArrayList<>();
            private List<CodeableConcept> valueCode = new ArrayList<>();

            private Builder(CodeableConcept type) {
                super();
                this.type = type;
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

            /**
             * <p>
             * Property value as a quantity.
             * </p>
             * 
             * @param valueQuantity
             *     Property value as a quantity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder valueQuantity(Quantity... valueQuantity) {
                for (Quantity value : valueQuantity) {
                    this.valueQuantity.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Property value as a quantity.
             * </p>
             * 
             * @param valueQuantity
             *     Property value as a quantity
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder valueQuantity(Collection<Quantity> valueQuantity) {
                this.valueQuantity.addAll(valueQuantity);
                return this;
            }

            /**
             * <p>
             * Property value as a code, e.g., NTP4 (synced to NTP).
             * </p>
             * 
             * @param valueCode
             *     Property value as a code, e.g., NTP4 (synced to NTP)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder valueCode(CodeableConcept... valueCode) {
                for (CodeableConcept value : valueCode) {
                    this.valueCode.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Property value as a code, e.g., NTP4 (synced to NTP).
             * </p>
             * 
             * @param valueCode
             *     Property value as a code, e.g., NTP4 (synced to NTP)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder valueCode(Collection<CodeableConcept> valueCode) {
                this.valueCode.addAll(valueCode);
                return this;
            }

            @Override
            public Property build() {
                return new Property(this);
            }

            private static Builder from(Property property) {
                Builder builder = new Builder(property.type);
                builder.valueQuantity.addAll(property.valueQuantity);
                builder.valueCode.addAll(property.valueCode);
                return builder;
            }
        }
    }

    /**
     * <p>
     * A substance used to create the material(s) of which the device is made.
     * </p>
     */
    public static class Material extends BackboneElement {
        private final CodeableConcept substance;
        private final Boolean alternate;
        private final Boolean allergenicIndicator;

        private Material(Builder builder) {
            super(builder);
            substance = ValidationSupport.requireNonNull(builder.substance, "substance");
            alternate = builder.alternate;
            allergenicIndicator = builder.allergenicIndicator;
        }

        /**
         * <p>
         * The substance.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getSubstance() {
            return substance;
        }

        /**
         * <p>
         * Indicates an alternative material of the device.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getAlternate() {
            return alternate;
        }

        /**
         * <p>
         * Whether the substance is a known or suspected allergen.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getAllergenicIndicator() {
            return allergenicIndicator;
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
                    accept(substance, "substance", visitor);
                    accept(alternate, "alternate", visitor);
                    accept(allergenicIndicator, "allergenicIndicator", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept substance) {
            return new Builder(substance);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept substance;

            // optional
            private Boolean alternate;
            private Boolean allergenicIndicator;

            private Builder(CodeableConcept substance) {
                super();
                this.substance = substance;
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

            /**
             * <p>
             * Indicates an alternative material of the device.
             * </p>
             * 
             * @param alternate
             *     Indicates an alternative material of the device
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder alternate(Boolean alternate) {
                this.alternate = alternate;
                return this;
            }

            /**
             * <p>
             * Whether the substance is a known or suspected allergen.
             * </p>
             * 
             * @param allergenicIndicator
             *     Whether the substance is a known or suspected allergen
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder allergenicIndicator(Boolean allergenicIndicator) {
                this.allergenicIndicator = allergenicIndicator;
                return this;
            }

            @Override
            public Material build() {
                return new Material(this);
            }

            private static Builder from(Material material) {
                Builder builder = new Builder(material.substance);
                builder.alternate = material.alternate;
                builder.allergenicIndicator = material.allergenicIndicator;
                return builder;
            }
        }
    }
}
