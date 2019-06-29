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
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.DeviceNameType;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.FHIRDeviceStatus;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.UDIEntryType;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A type of a manufactured item that is used in the provision of healthcare without being substantially changed through 
 * that activity. The device may be a medical or non-medical device.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Device extends DomainResource {
    private final List<Identifier> identifier;
    private final Reference definition;
    private final List<UdiCarrier> udiCarrier;
    private final FHIRDeviceStatus status;
    private final List<CodeableConcept> statusReason;
    private final String distinctIdentifier;
    private final String manufacturer;
    private final DateTime manufactureDate;
    private final DateTime expirationDate;
    private final String lotNumber;
    private final String serialNumber;
    private final List<DeviceName> deviceName;
    private final String modelNumber;
    private final String partNumber;
    private final CodeableConcept type;
    private final List<Specialization> specialization;
    private final List<Version> version;
    private final List<Property> property;
    private final Reference patient;
    private final Reference owner;
    private final List<ContactPoint> contact;
    private final Reference location;
    private final Uri url;
    private final List<Annotation> note;
    private final List<CodeableConcept> safety;
    private final Reference parent;

    private Device(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        definition = builder.definition;
        udiCarrier = Collections.unmodifiableList(builder.udiCarrier);
        status = builder.status;
        statusReason = Collections.unmodifiableList(builder.statusReason);
        distinctIdentifier = builder.distinctIdentifier;
        manufacturer = builder.manufacturer;
        manufactureDate = builder.manufactureDate;
        expirationDate = builder.expirationDate;
        lotNumber = builder.lotNumber;
        serialNumber = builder.serialNumber;
        deviceName = Collections.unmodifiableList(builder.deviceName);
        modelNumber = builder.modelNumber;
        partNumber = builder.partNumber;
        type = builder.type;
        specialization = Collections.unmodifiableList(builder.specialization);
        version = Collections.unmodifiableList(builder.version);
        property = Collections.unmodifiableList(builder.property);
        patient = builder.patient;
        owner = builder.owner;
        contact = Collections.unmodifiableList(builder.contact);
        location = builder.location;
        url = builder.url;
        note = Collections.unmodifiableList(builder.note);
        safety = Collections.unmodifiableList(builder.safety);
        parent = builder.parent;
    }

    /**
     * <p>
     * Unique instance identifiers assigned to a device by manufacturers other organizations or owners.
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
     * The reference to the definition for the device.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getDefinition() {
        return definition;
    }

    /**
     * <p>
     * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
     * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
     * jurisdictions it could have been sold.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link UdiCarrier}.
     */
    public List<UdiCarrier> getUdiCarrier() {
        return udiCarrier;
    }

    /**
     * <p>
     * Status of the Device availability.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link FHIRDeviceStatus}.
     */
    public FHIRDeviceStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Reason for the dtatus of the Device availability.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getStatusReason() {
        return statusReason;
    }

    /**
     * <p>
     * The distinct identification string as required by regulation for a human cell, tissue, or cellular and tissue-based 
     * product.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getDistinctIdentifier() {
        return distinctIdentifier;
    }

    /**
     * <p>
     * A name of the manufacturer.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * <p>
     * The date and time when the device was manufactured.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getManufactureDate() {
        return manufactureDate;
    }

    /**
     * <p>
     * The date and time beyond which this device is no longer valid or should not be used (if applicable).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     * <p>
     * Lot number assigned by the manufacturer.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getLotNumber() {
        return lotNumber;
    }

    /**
     * <p>
     * The serial number assigned by the organization when the device was manufactured.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * <p>
     * This represents the manufacturer's name of the device as provided by the device, from a UDI label, or by a person 
     * describing the Device. This typically would be used when a person provides the name(s) or when the device represents 
     * one of the names available from DeviceDefinition.
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
     * The part number of the device.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     * <p>
     * The kind or type of device.
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
     * The actual design of the device or software version running on the device.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Version}.
     */
    public List<Version> getVersion() {
        return version;
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
     * Patient information, If the device is affixed to a person.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPatient() {
        return patient;
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
     * The place where the device can be found.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getLocation() {
        return location;
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
     * Provides additional safety characteristics about a medical device. For example devices containing latex.
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
     * The parent device.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getParent() {
        return parent;
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
                accept(definition, "definition", visitor);
                accept(udiCarrier, "udiCarrier", visitor, UdiCarrier.class);
                accept(status, "status", visitor);
                accept(statusReason, "statusReason", visitor, CodeableConcept.class);
                accept(distinctIdentifier, "distinctIdentifier", visitor);
                accept(manufacturer, "manufacturer", visitor);
                accept(manufactureDate, "manufactureDate", visitor);
                accept(expirationDate, "expirationDate", visitor);
                accept(lotNumber, "lotNumber", visitor);
                accept(serialNumber, "serialNumber", visitor);
                accept(deviceName, "deviceName", visitor, DeviceName.class);
                accept(modelNumber, "modelNumber", visitor);
                accept(partNumber, "partNumber", visitor);
                accept(type, "type", visitor);
                accept(specialization, "specialization", visitor, Specialization.class);
                accept(version, "version", visitor, Version.class);
                accept(property, "property", visitor, Property.class);
                accept(patient, "patient", visitor);
                accept(owner, "owner", visitor);
                accept(contact, "contact", visitor, ContactPoint.class);
                accept(location, "location", visitor);
                accept(url, "url", visitor);
                accept(note, "note", visitor, Annotation.class);
                accept(safety, "safety", visitor, CodeableConcept.class);
                accept(parent, "parent", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Reference definition;
        private List<UdiCarrier> udiCarrier = new ArrayList<>();
        private FHIRDeviceStatus status;
        private List<CodeableConcept> statusReason = new ArrayList<>();
        private String distinctIdentifier;
        private String manufacturer;
        private DateTime manufactureDate;
        private DateTime expirationDate;
        private String lotNumber;
        private String serialNumber;
        private List<DeviceName> deviceName = new ArrayList<>();
        private String modelNumber;
        private String partNumber;
        private CodeableConcept type;
        private List<Specialization> specialization = new ArrayList<>();
        private List<Version> version = new ArrayList<>();
        private List<Property> property = new ArrayList<>();
        private Reference patient;
        private Reference owner;
        private List<ContactPoint> contact = new ArrayList<>();
        private Reference location;
        private Uri url;
        private List<Annotation> note = new ArrayList<>();
        private List<CodeableConcept> safety = new ArrayList<>();
        private Reference parent;

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
         * Unique instance identifiers assigned to a device by manufacturers other organizations or owners.
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
         * Unique instance identifiers assigned to a device by manufacturers other organizations or owners.
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
         * The reference to the definition for the device.
         * </p>
         * 
         * @param definition
         *     The reference to the definition for the device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder definition(Reference definition) {
            this.definition = definition;
            return this;
        }

        /**
         * <p>
         * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
         * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
         * jurisdictions it could have been sold.
         * </p>
         * 
         * @param udiCarrier
         *     Unique Device Identifier (UDI) Barcode string
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder udiCarrier(UdiCarrier... udiCarrier) {
            for (UdiCarrier value : udiCarrier) {
                this.udiCarrier.add(value);
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
         * @param udiCarrier
         *     Unique Device Identifier (UDI) Barcode string
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder udiCarrier(Collection<UdiCarrier> udiCarrier) {
            this.udiCarrier.addAll(udiCarrier);
            return this;
        }

        /**
         * <p>
         * Status of the Device availability.
         * </p>
         * 
         * @param status
         *     active | inactive | entered-in-error | unknown
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder status(FHIRDeviceStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * Reason for the dtatus of the Device availability.
         * </p>
         * 
         * @param statusReason
         *     online | paused | standby | offline | not-ready | transduc-discon | hw-discon | off
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder statusReason(CodeableConcept... statusReason) {
            for (CodeableConcept value : statusReason) {
                this.statusReason.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Reason for the dtatus of the Device availability.
         * </p>
         * 
         * @param statusReason
         *     online | paused | standby | offline | not-ready | transduc-discon | hw-discon | off
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder statusReason(Collection<CodeableConcept> statusReason) {
            this.statusReason.addAll(statusReason);
            return this;
        }

        /**
         * <p>
         * The distinct identification string as required by regulation for a human cell, tissue, or cellular and tissue-based 
         * product.
         * </p>
         * 
         * @param distinctIdentifier
         *     The distinct identification string
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder distinctIdentifier(String distinctIdentifier) {
            this.distinctIdentifier = distinctIdentifier;
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
        public Builder manufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        /**
         * <p>
         * The date and time when the device was manufactured.
         * </p>
         * 
         * @param manufactureDate
         *     Date when the device was made
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder manufactureDate(DateTime manufactureDate) {
            this.manufactureDate = manufactureDate;
            return this;
        }

        /**
         * <p>
         * The date and time beyond which this device is no longer valid or should not be used (if applicable).
         * </p>
         * 
         * @param expirationDate
         *     Date and time of expiry of this device (if applicable)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder expirationDate(DateTime expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        /**
         * <p>
         * Lot number assigned by the manufacturer.
         * </p>
         * 
         * @param lotNumber
         *     Lot number of manufacture
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder lotNumber(String lotNumber) {
            this.lotNumber = lotNumber;
            return this;
        }

        /**
         * <p>
         * The serial number assigned by the organization when the device was manufactured.
         * </p>
         * 
         * @param serialNumber
         *     Serial number assigned by the manufacturer
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder serialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        /**
         * <p>
         * This represents the manufacturer's name of the device as provided by the device, from a UDI label, or by a person 
         * describing the Device. This typically would be used when a person provides the name(s) or when the device represents 
         * one of the names available from DeviceDefinition.
         * </p>
         * 
         * @param deviceName
         *     The name of the device as given by the manufacturer
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
         * This represents the manufacturer's name of the device as provided by the device, from a UDI label, or by a person 
         * describing the Device. This typically would be used when a person provides the name(s) or when the device represents 
         * one of the names available from DeviceDefinition.
         * </p>
         * 
         * @param deviceName
         *     The name of the device as given by the manufacturer
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
         * The part number of the device.
         * </p>
         * 
         * @param partNumber
         *     The part number of the device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partNumber(String partNumber) {
            this.partNumber = partNumber;
            return this;
        }

        /**
         * <p>
         * The kind or type of device.
         * </p>
         * 
         * @param type
         *     The kind or type of device
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
         * The actual design of the device or software version running on the device.
         * </p>
         * 
         * @param version
         *     The actual design of the device or software version running on the device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder version(Version... version) {
            for (Version value : version) {
                this.version.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The actual design of the device or software version running on the device.
         * </p>
         * 
         * @param version
         *     The actual design of the device or software version running on the device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder version(Collection<Version> version) {
            this.version.addAll(version);
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
         * Patient information, If the device is affixed to a person.
         * </p>
         * 
         * @param patient
         *     Patient to whom Device is affixed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
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
         * The place where the device can be found.
         * </p>
         * 
         * @param location
         *     Where the device is found
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder location(Reference location) {
            this.location = location;
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
         * Provides additional safety characteristics about a medical device. For example devices containing latex.
         * </p>
         * 
         * @param safety
         *     Safety Characteristics of Device
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
         * Provides additional safety characteristics about a medical device. For example devices containing latex.
         * </p>
         * 
         * @param safety
         *     Safety Characteristics of Device
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
         * The parent device.
         * </p>
         * 
         * @param parent
         *     The parent device
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parent(Reference parent) {
            this.parent = parent;
            return this;
        }

        @Override
        public Device build() {
            return new Device(this);
        }

        private Builder from(Device device) {
            id = device.id;
            meta = device.meta;
            implicitRules = device.implicitRules;
            language = device.language;
            text = device.text;
            contained.addAll(device.contained);
            extension.addAll(device.extension);
            modifierExtension.addAll(device.modifierExtension);
            identifier.addAll(device.identifier);
            definition = device.definition;
            udiCarrier.addAll(device.udiCarrier);
            status = device.status;
            statusReason.addAll(device.statusReason);
            distinctIdentifier = device.distinctIdentifier;
            manufacturer = device.manufacturer;
            manufactureDate = device.manufactureDate;
            expirationDate = device.expirationDate;
            lotNumber = device.lotNumber;
            serialNumber = device.serialNumber;
            deviceName.addAll(device.deviceName);
            modelNumber = device.modelNumber;
            partNumber = device.partNumber;
            type = device.type;
            specialization.addAll(device.specialization);
            version.addAll(device.version);
            property.addAll(device.property);
            patient = device.patient;
            owner = device.owner;
            contact.addAll(device.contact);
            location = device.location;
            url = device.url;
            note.addAll(device.note);
            safety.addAll(device.safety);
            parent = device.parent;
            return this;
        }
    }

    /**
     * <p>
     * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
     * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
     * jurisdictions it could have been sold.
     * </p>
     */
    public static class UdiCarrier extends BackboneElement {
        private final String deviceIdentifier;
        private final Uri issuer;
        private final Uri jurisdiction;
        private final Base64Binary carrierAIDC;
        private final String carrierHRF;
        private final UDIEntryType entryType;

        private UdiCarrier(Builder builder) {
            super(builder);
            deviceIdentifier = builder.deviceIdentifier;
            issuer = builder.issuer;
            jurisdiction = builder.jurisdiction;
            carrierAIDC = builder.carrierAIDC;
            carrierHRF = builder.carrierHRF;
            entryType = builder.entryType;
        }

        /**
         * <p>
         * The device identifier (DI) is a mandatory, fixed portion of a UDI that identifies the labeler and the specific version 
         * or model of a device.
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
         * Organization that is charged with issuing UDIs for devices. For example, the US FDA issuers include :
         * 1) GS1: 
         * http://hl7.org/fhir/NamingSystem/gs1-di, 
         * 2) HIBCC:
         * http://hl7.org/fhir/NamingSystem/hibcc-dI, 
         * 3) ICCBBA for blood containers:
         * http://hl7.org/fhir/NamingSystem/iccbba-blood-di, 
         * 4) ICCBA for other devices:
         * http://hl7.org/fhir/NamingSystem/iccbba-other-di.
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
         * The identity of the authoritative source for UDI generation within a jurisdiction. All UDIs are globally unique within 
         * a single namespace with the appropriate repository uri as the system. For example, UDIs of devices managed in the U.S. 
         * by the FDA, the value is http://hl7.org/fhir/NamingSystem/fda-udi.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getJurisdiction() {
            return jurisdiction;
        }

        /**
         * <p>
         * The full UDI carrier of the Automatic Identification and Data Capture (AIDC) technology representation of the barcode 
         * string as printed on the packaging of the device - e.g., a barcode or RFID. Because of limitations on character sets 
         * in XML and the need to round-trip JSON data through XML, AIDC Formats *SHALL* be base64 encoded.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Base64Binary}.
         */
        public Base64Binary getCarrierAIDC() {
            return carrierAIDC;
        }

        /**
         * <p>
         * The full UDI carrier as the human readable form (HRF) representation of the barcode string as printed on the packaging 
         * of the device.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getCarrierHRF() {
            return carrierHRF;
        }

        /**
         * <p>
         * A coded entry to indicate how the data was entered.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link UDIEntryType}.
         */
        public UDIEntryType getEntryType() {
            return entryType;
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
                    accept(carrierAIDC, "carrierAIDC", visitor);
                    accept(carrierHRF, "carrierHRF", visitor);
                    accept(entryType, "entryType", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private String deviceIdentifier;
            private Uri issuer;
            private Uri jurisdiction;
            private Base64Binary carrierAIDC;
            private String carrierHRF;
            private UDIEntryType entryType;

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
             * The device identifier (DI) is a mandatory, fixed portion of a UDI that identifies the labeler and the specific version 
             * or model of a device.
             * </p>
             * 
             * @param deviceIdentifier
             *     Mandatory fixed portion of UDI
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder deviceIdentifier(String deviceIdentifier) {
                this.deviceIdentifier = deviceIdentifier;
                return this;
            }

            /**
             * <p>
             * Organization that is charged with issuing UDIs for devices. For example, the US FDA issuers include :
             * 1) GS1: 
             * http://hl7.org/fhir/NamingSystem/gs1-di, 
             * 2) HIBCC:
             * http://hl7.org/fhir/NamingSystem/hibcc-dI, 
             * 3) ICCBBA for blood containers:
             * http://hl7.org/fhir/NamingSystem/iccbba-blood-di, 
             * 4) ICCBA for other devices:
             * http://hl7.org/fhir/NamingSystem/iccbba-other-di.
             * </p>
             * 
             * @param issuer
             *     UDI Issuing Organization
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder issuer(Uri issuer) {
                this.issuer = issuer;
                return this;
            }

            /**
             * <p>
             * The identity of the authoritative source for UDI generation within a jurisdiction. All UDIs are globally unique within 
             * a single namespace with the appropriate repository uri as the system. For example, UDIs of devices managed in the U.S. 
             * by the FDA, the value is http://hl7.org/fhir/NamingSystem/fda-udi.
             * </p>
             * 
             * @param jurisdiction
             *     Regional UDI authority
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder jurisdiction(Uri jurisdiction) {
                this.jurisdiction = jurisdiction;
                return this;
            }

            /**
             * <p>
             * The full UDI carrier of the Automatic Identification and Data Capture (AIDC) technology representation of the barcode 
             * string as printed on the packaging of the device - e.g., a barcode or RFID. Because of limitations on character sets 
             * in XML and the need to round-trip JSON data through XML, AIDC Formats *SHALL* be base64 encoded.
             * </p>
             * 
             * @param carrierAIDC
             *     UDI Machine Readable Barcode String
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder carrierAIDC(Base64Binary carrierAIDC) {
                this.carrierAIDC = carrierAIDC;
                return this;
            }

            /**
             * <p>
             * The full UDI carrier as the human readable form (HRF) representation of the barcode string as printed on the packaging 
             * of the device.
             * </p>
             * 
             * @param carrierHRF
             *     UDI Human Readable Barcode String
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder carrierHRF(String carrierHRF) {
                this.carrierHRF = carrierHRF;
                return this;
            }

            /**
             * <p>
             * A coded entry to indicate how the data was entered.
             * </p>
             * 
             * @param entryType
             *     barcode | rfid | manual +
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder entryType(UDIEntryType entryType) {
                this.entryType = entryType;
                return this;
            }

            @Override
            public UdiCarrier build() {
                return new UdiCarrier(this);
            }

            private Builder from(UdiCarrier udiCarrier) {
                id = udiCarrier.id;
                extension.addAll(udiCarrier.extension);
                modifierExtension.addAll(udiCarrier.modifierExtension);
                deviceIdentifier = udiCarrier.deviceIdentifier;
                issuer = udiCarrier.issuer;
                jurisdiction = udiCarrier.jurisdiction;
                carrierAIDC = udiCarrier.carrierAIDC;
                carrierHRF = udiCarrier.carrierHRF;
                entryType = udiCarrier.entryType;
                return this;
            }
        }
    }

    /**
     * <p>
     * This represents the manufacturer's name of the device as provided by the device, from a UDI label, or by a person 
     * describing the Device. This typically would be used when a person provides the name(s) or when the device represents 
     * one of the names available from DeviceDefinition.
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
            return new Builder(name, type).from(this);
        }

        public Builder toBuilder(String name, DeviceNameType type) {
            return new Builder(name, type).from(this);
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

            private Builder from(DeviceName deviceName) {
                id = deviceName.id;
                extension.addAll(deviceName.extension);
                modifierExtension.addAll(deviceName.modifierExtension);
                return this;
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
        private final CodeableConcept systemType;
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
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getSystemType() {
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
            return new Builder(systemType).from(this);
        }

        public Builder toBuilder(CodeableConcept systemType) {
            return new Builder(systemType).from(this);
        }

        public static Builder builder(CodeableConcept systemType) {
            return new Builder(systemType);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept systemType;

            // optional
            private String version;

            private Builder(CodeableConcept systemType) {
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

            private Builder from(Specialization specialization) {
                id = specialization.id;
                extension.addAll(specialization.extension);
                modifierExtension.addAll(specialization.modifierExtension);
                version = specialization.version;
                return this;
            }
        }
    }

    /**
     * <p>
     * The actual design of the device or software version running on the device.
     * </p>
     */
    public static class Version extends BackboneElement {
        private final CodeableConcept type;
        private final Identifier component;
        private final String value;

        private Version(Builder builder) {
            super(builder);
            type = builder.type;
            component = builder.component;
            value = ValidationSupport.requireNonNull(builder.value, "value");
        }

        /**
         * <p>
         * The type of the device version.
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
         * A single component of the device version.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Identifier}.
         */
        public Identifier getComponent() {
            return component;
        }

        /**
         * <p>
         * The version text.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getValue() {
            return value;
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
                    accept(component, "component", visitor);
                    accept(value, "value", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(value).from(this);
        }

        public Builder toBuilder(String value) {
            return new Builder(value).from(this);
        }

        public static Builder builder(String value) {
            return new Builder(value);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String value;

            // optional
            private CodeableConcept type;
            private Identifier component;

            private Builder(String value) {
                super();
                this.value = value;
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
             * The type of the device version.
             * </p>
             * 
             * @param type
             *     The type of the device version
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
             * A single component of the device version.
             * </p>
             * 
             * @param component
             *     A single component of the device version
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder component(Identifier component) {
                this.component = component;
                return this;
            }

            @Override
            public Version build() {
                return new Version(this);
            }

            private Builder from(Version version) {
                id = version.id;
                extension.addAll(version.extension);
                modifierExtension.addAll(version.modifierExtension);
                type = version.type;
                component = version.component;
                return this;
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
            return new Builder(type).from(this);
        }

        public Builder toBuilder(CodeableConcept type) {
            return new Builder(type).from(this);
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

            private Builder from(Property property) {
                id = property.id;
                extension.addAll(property.extension);
                modifierExtension.addAll(property.modifierExtension);
                valueQuantity.addAll(property.valueQuantity);
                valueCode.addAll(property.valueCode);
                return this;
            }
        }
    }
}
