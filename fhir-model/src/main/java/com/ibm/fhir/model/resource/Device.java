/*
 * (C) Copyright IBM Corp. 2019, 2022
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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.DeviceNameType;
import com.ibm.fhir.model.type.code.FHIRDeviceStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.UDIEntryType;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A type of a manufactured item that is used in the provision of healthcare without being substantially changed through 
 * that activity. The device may be a medical or non-medical device.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "device-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/device-status-reason",
    expression = "statusReason.exists() implies (statusReason.all(memberOf('http://hl7.org/fhir/ValueSet/device-status-reason', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/Device",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Device extends DomainResource {
    private final List<Identifier> identifier;
    @ReferenceTarget({ "DeviceDefinition" })
    private final Reference definition;
    @Summary
    private final List<UdiCarrier> udiCarrier;
    @Summary
    @Binding(
        bindingName = "FHIRDeviceStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The availability status of the device.",
        valueSet = "http://hl7.org/fhir/ValueSet/device-status|4.3.0"
    )
    private final FHIRDeviceStatus status;
    @Binding(
        bindingName = "FHIRDeviceStatusReason",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The availability status reason of the device.",
        valueSet = "http://hl7.org/fhir/ValueSet/device-status-reason"
    )
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
    @Binding(
        bindingName = "DeviceType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes to identify medical devices.",
        valueSet = "http://hl7.org/fhir/ValueSet/device-type"
    )
    private final CodeableConcept type;
    private final List<Specialization> specialization;
    private final List<Version> version;
    private final List<Property> property;
    @ReferenceTarget({ "Patient" })
    private final Reference patient;
    @ReferenceTarget({ "Organization" })
    private final Reference owner;
    private final List<ContactPoint> contact;
    @ReferenceTarget({ "Location" })
    private final Reference location;
    private final Uri url;
    private final List<Annotation> note;
    @Summary
    private final List<CodeableConcept> safety;
    @ReferenceTarget({ "Device" })
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
     * Unique instance identifiers assigned to a device by manufacturers other organizations or owners.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The reference to the definition for the device.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getDefinition() {
        return definition;
    }

    /**
     * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
     * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
     * jurisdictions it could have been sold.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UdiCarrier} that may be empty.
     */
    public List<UdiCarrier> getUdiCarrier() {
        return udiCarrier;
    }

    /**
     * Status of the Device availability.
     * 
     * @return
     *     An immutable object of type {@link FHIRDeviceStatus} that may be null.
     */
    public FHIRDeviceStatus getStatus() {
        return status;
    }

    /**
     * Reason for the dtatus of the Device availability.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getStatusReason() {
        return statusReason;
    }

    /**
     * The distinct identification string as required by regulation for a human cell, tissue, or cellular and tissue-based 
     * product.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDistinctIdentifier() {
        return distinctIdentifier;
    }

    /**
     * A name of the manufacturer.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * The date and time when the device was manufactured.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getManufactureDate() {
        return manufactureDate;
    }

    /**
     * The date and time beyond which this device is no longer valid or should not be used (if applicable).
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     * Lot number assigned by the manufacturer.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getLotNumber() {
        return lotNumber;
    }

    /**
     * The serial number assigned by the organization when the device was manufactured.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * This represents the manufacturer's name of the device as provided by the device, from a UDI label, or by a person 
     * describing the Device. This typically would be used when a person provides the name(s) or when the device represents 
     * one of the names available from DeviceDefinition.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link DeviceName} that may be empty.
     */
    public List<DeviceName> getDeviceName() {
        return deviceName;
    }

    /**
     * The manufacturer's model number for the device.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getModelNumber() {
        return modelNumber;
    }

    /**
     * The part number or catalog number of the device.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     * The kind or type of device.
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
     * The actual design of the device or software version running on the device.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Version} that may be empty.
     */
    public List<Version> getVersion() {
        return version;
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
     * Patient information, If the device is affixed to a person.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPatient() {
        return patient;
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
     * The place where the device can be found.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getLocation() {
        return location;
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
     * Descriptive information, usage information or implantation information that is not captured in an existing element.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Provides additional safety characteristics about a medical device. For example devices containing latex.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getSafety() {
        return safety;
    }

    /**
     * The device that this device is attached to or is part of.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getParent() {
        return parent;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (definition != null) || 
            !udiCarrier.isEmpty() || 
            (status != null) || 
            !statusReason.isEmpty() || 
            (distinctIdentifier != null) || 
            (manufacturer != null) || 
            (manufactureDate != null) || 
            (expirationDate != null) || 
            (lotNumber != null) || 
            (serialNumber != null) || 
            !deviceName.isEmpty() || 
            (modelNumber != null) || 
            (partNumber != null) || 
            (type != null) || 
            !specialization.isEmpty() || 
            !version.isEmpty() || 
            !property.isEmpty() || 
            (patient != null) || 
            (owner != null) || 
            !contact.isEmpty() || 
            (location != null) || 
            (url != null) || 
            !note.isEmpty() || 
            !safety.isEmpty() || 
            (parent != null);
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
        Device other = (Device) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(definition, other.definition) && 
            Objects.equals(udiCarrier, other.udiCarrier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusReason, other.statusReason) && 
            Objects.equals(distinctIdentifier, other.distinctIdentifier) && 
            Objects.equals(manufacturer, other.manufacturer) && 
            Objects.equals(manufactureDate, other.manufactureDate) && 
            Objects.equals(expirationDate, other.expirationDate) && 
            Objects.equals(lotNumber, other.lotNumber) && 
            Objects.equals(serialNumber, other.serialNumber) && 
            Objects.equals(deviceName, other.deviceName) && 
            Objects.equals(modelNumber, other.modelNumber) && 
            Objects.equals(partNumber, other.partNumber) && 
            Objects.equals(type, other.type) && 
            Objects.equals(specialization, other.specialization) && 
            Objects.equals(version, other.version) && 
            Objects.equals(property, other.property) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(owner, other.owner) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(location, other.location) && 
            Objects.equals(url, other.url) && 
            Objects.equals(note, other.note) && 
            Objects.equals(safety, other.safety) && 
            Objects.equals(parent, other.parent);
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
                definition, 
                udiCarrier, 
                status, 
                statusReason, 
                distinctIdentifier, 
                manufacturer, 
                manufactureDate, 
                expirationDate, 
                lotNumber, 
                serialNumber, 
                deviceName, 
                modelNumber, 
                partNumber, 
                type, 
                specialization, 
                version, 
                property, 
                patient, 
                owner, 
                contact, 
                location, 
                url, 
                note, 
                safety, 
                parent);
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
         * Unique instance identifiers assigned to a device by manufacturers other organizations or owners.
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
         * Unique instance identifiers assigned to a device by manufacturers other organizations or owners.
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
         * The reference to the definition for the device.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link DeviceDefinition}</li>
         * </ul>
         * 
         * @param definition
         *     The reference to the definition for the device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder definition(Reference definition) {
            this.definition = definition;
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
         * @param udiCarrier
         *     Unique Device Identifier (UDI) Barcode string
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder udiCarrier(UdiCarrier... udiCarrier) {
            for (UdiCarrier value : udiCarrier) {
                this.udiCarrier.add(value);
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
         * @param udiCarrier
         *     Unique Device Identifier (UDI) Barcode string
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder udiCarrier(Collection<UdiCarrier> udiCarrier) {
            this.udiCarrier = new ArrayList<>(udiCarrier);
            return this;
        }

        /**
         * Status of the Device availability.
         * 
         * @param status
         *     active | inactive | entered-in-error | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(FHIRDeviceStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Reason for the dtatus of the Device availability.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param statusReason
         *     online | paused | standby | offline | not-ready | transduc-discon | hw-discon | off
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusReason(CodeableConcept... statusReason) {
            for (CodeableConcept value : statusReason) {
                this.statusReason.add(value);
            }
            return this;
        }

        /**
         * Reason for the dtatus of the Device availability.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param statusReason
         *     online | paused | standby | offline | not-ready | transduc-discon | hw-discon | off
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder statusReason(Collection<CodeableConcept> statusReason) {
            this.statusReason = new ArrayList<>(statusReason);
            return this;
        }

        /**
         * Convenience method for setting {@code distinctIdentifier}.
         * 
         * @param distinctIdentifier
         *     The distinct identification string
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #distinctIdentifier(com.ibm.fhir.model.type.String)
         */
        public Builder distinctIdentifier(java.lang.String distinctIdentifier) {
            this.distinctIdentifier = (distinctIdentifier == null) ? null : String.of(distinctIdentifier);
            return this;
        }

        /**
         * The distinct identification string as required by regulation for a human cell, tissue, or cellular and tissue-based 
         * product.
         * 
         * @param distinctIdentifier
         *     The distinct identification string
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder distinctIdentifier(String distinctIdentifier) {
            this.distinctIdentifier = distinctIdentifier;
            return this;
        }

        /**
         * Convenience method for setting {@code manufacturer}.
         * 
         * @param manufacturer
         *     Name of device manufacturer
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #manufacturer(com.ibm.fhir.model.type.String)
         */
        public Builder manufacturer(java.lang.String manufacturer) {
            this.manufacturer = (manufacturer == null) ? null : String.of(manufacturer);
            return this;
        }

        /**
         * A name of the manufacturer.
         * 
         * @param manufacturer
         *     Name of device manufacturer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        /**
         * The date and time when the device was manufactured.
         * 
         * @param manufactureDate
         *     Date when the device was made
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manufactureDate(DateTime manufactureDate) {
            this.manufactureDate = manufactureDate;
            return this;
        }

        /**
         * The date and time beyond which this device is no longer valid or should not be used (if applicable).
         * 
         * @param expirationDate
         *     Date and time of expiry of this device (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder expirationDate(DateTime expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        /**
         * Convenience method for setting {@code lotNumber}.
         * 
         * @param lotNumber
         *     Lot number of manufacture
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #lotNumber(com.ibm.fhir.model.type.String)
         */
        public Builder lotNumber(java.lang.String lotNumber) {
            this.lotNumber = (lotNumber == null) ? null : String.of(lotNumber);
            return this;
        }

        /**
         * Lot number assigned by the manufacturer.
         * 
         * @param lotNumber
         *     Lot number of manufacture
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lotNumber(String lotNumber) {
            this.lotNumber = lotNumber;
            return this;
        }

        /**
         * Convenience method for setting {@code serialNumber}.
         * 
         * @param serialNumber
         *     Serial number assigned by the manufacturer
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #serialNumber(com.ibm.fhir.model.type.String)
         */
        public Builder serialNumber(java.lang.String serialNumber) {
            this.serialNumber = (serialNumber == null) ? null : String.of(serialNumber);
            return this;
        }

        /**
         * The serial number assigned by the organization when the device was manufactured.
         * 
         * @param serialNumber
         *     Serial number assigned by the manufacturer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        /**
         * This represents the manufacturer's name of the device as provided by the device, from a UDI label, or by a person 
         * describing the Device. This typically would be used when a person provides the name(s) or when the device represents 
         * one of the names available from DeviceDefinition.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param deviceName
         *     The name of the device as given by the manufacturer
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
         * This represents the manufacturer's name of the device as provided by the device, from a UDI label, or by a person 
         * describing the Device. This typically would be used when a person provides the name(s) or when the device represents 
         * one of the names available from DeviceDefinition.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param deviceName
         *     The name of the device as given by the manufacturer
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
         *     The manufacturer's model number for the device
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
         * The manufacturer's model number for the device.
         * 
         * @param modelNumber
         *     The manufacturer's model number for the device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder modelNumber(String modelNumber) {
            this.modelNumber = modelNumber;
            return this;
        }

        /**
         * Convenience method for setting {@code partNumber}.
         * 
         * @param partNumber
         *     The part number or catalog number of the device
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #partNumber(com.ibm.fhir.model.type.String)
         */
        public Builder partNumber(java.lang.String partNumber) {
            this.partNumber = (partNumber == null) ? null : String.of(partNumber);
            return this;
        }

        /**
         * The part number or catalog number of the device.
         * 
         * @param partNumber
         *     The part number or catalog number of the device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partNumber(String partNumber) {
            this.partNumber = partNumber;
            return this;
        }

        /**
         * The kind or type of device.
         * 
         * @param type
         *     The kind or type of device
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
         * The actual design of the device or software version running on the device.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param version
         *     The actual design of the device or software version running on the device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(Version... version) {
            for (Version value : version) {
                this.version.add(value);
            }
            return this;
        }

        /**
         * The actual design of the device or software version running on the device.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param version
         *     The actual design of the device or software version running on the device
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder version(Collection<Version> version) {
            this.version = new ArrayList<>(version);
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
         * Patient information, If the device is affixed to a person.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     Patient to whom Device is affixed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
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
         * The place where the device can be found.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param location
         *     Where the device is found
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(Reference location) {
            this.location = location;
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
         * Provides additional safety characteristics about a medical device. For example devices containing latex.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param safety
         *     Safety Characteristics of Device
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
         * Provides additional safety characteristics about a medical device. For example devices containing latex.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param safety
         *     Safety Characteristics of Device
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
         * The device that this device is attached to or is part of.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param parent
         *     The device that this device is attached to or is part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder parent(Reference parent) {
            this.parent = parent;
            return this;
        }

        /**
         * Build the {@link Device}
         * 
         * @return
         *     An immutable object of type {@link Device}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Device per the base specification
         */
        @Override
        public Device build() {
            Device device = new Device(this);
            if (validating) {
                validate(device);
            }
            return device;
        }

        protected void validate(Device device) {
            super.validate(device);
            ValidationSupport.checkList(device.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(device.udiCarrier, "udiCarrier", UdiCarrier.class);
            ValidationSupport.checkList(device.statusReason, "statusReason", CodeableConcept.class);
            ValidationSupport.checkList(device.deviceName, "deviceName", DeviceName.class);
            ValidationSupport.checkList(device.specialization, "specialization", Specialization.class);
            ValidationSupport.checkList(device.version, "version", Version.class);
            ValidationSupport.checkList(device.property, "property", Property.class);
            ValidationSupport.checkList(device.contact, "contact", ContactPoint.class);
            ValidationSupport.checkList(device.note, "note", Annotation.class);
            ValidationSupport.checkList(device.safety, "safety", CodeableConcept.class);
            ValidationSupport.checkReferenceType(device.definition, "definition", "DeviceDefinition");
            ValidationSupport.checkReferenceType(device.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(device.owner, "owner", "Organization");
            ValidationSupport.checkReferenceType(device.location, "location", "Location");
            ValidationSupport.checkReferenceType(device.parent, "parent", "Device");
        }

        protected Builder from(Device device) {
            super.from(device);
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
     * Unique device identifier (UDI) assigned to device label or package. Note that the Device may include multiple 
     * udiCarriers as it either may include just the udiCarrier for the jurisdiction it is sold, or for multiple 
     * jurisdictions it could have been sold.
     */
    public static class UdiCarrier extends BackboneElement {
        @Summary
        private final String deviceIdentifier;
        private final Uri issuer;
        private final Uri jurisdiction;
        @Summary
        private final Base64Binary carrierAIDC;
        @Summary
        private final String carrierHRF;
        @Binding(
            bindingName = "UDIEntryType",
            strength = BindingStrength.Value.REQUIRED,
            description = "Codes to identify how UDI data was entered.",
            valueSet = "http://hl7.org/fhir/ValueSet/udi-entry-type|4.3.0"
        )
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
         * The device identifier (DI) is a mandatory, fixed portion of a UDI that identifies the labeler and the specific version 
         * or model of a device.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDeviceIdentifier() {
            return deviceIdentifier;
        }

        /**
         * Organization that is charged with issuing UDIs for devices. For example, the US FDA issuers include :
         * 1) GS1: 
         * http://hl7.org/fhir/NamingSystem/gs1-di, 
         * 2) HIBCC:
         * http://hl7.org/fhir/NamingSystem/hibcc-dI, 
         * 3) ICCBBA for blood containers:
         * http://hl7.org/fhir/NamingSystem/iccbba-blood-di, 
         * 4) ICCBA for other devices:
         * http://hl7.org/fhir/NamingSystem/iccbba-other-di.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getIssuer() {
            return issuer;
        }

        /**
         * The identity of the authoritative source for UDI generation within a jurisdiction. All UDIs are globally unique within 
         * a single namespace with the appropriate repository uri as the system. For example, UDIs of devices managed in the U.S. 
         * by the FDA, the value is http://hl7.org/fhir/NamingSystem/fda-udi.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getJurisdiction() {
            return jurisdiction;
        }

        /**
         * The full UDI carrier of the Automatic Identification and Data Capture (AIDC) technology representation of the barcode 
         * string as printed on the packaging of the device - e.g., a barcode or RFID. Because of limitations on character sets 
         * in XML and the need to round-trip JSON data through XML, AIDC Formats *SHALL* be base64 encoded.
         * 
         * @return
         *     An immutable object of type {@link Base64Binary} that may be null.
         */
        public Base64Binary getCarrierAIDC() {
            return carrierAIDC;
        }

        /**
         * The full UDI carrier as the human readable form (HRF) representation of the barcode string as printed on the packaging 
         * of the device.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getCarrierHRF() {
            return carrierHRF;
        }

        /**
         * A coded entry to indicate how the data was entered.
         * 
         * @return
         *     An immutable object of type {@link UDIEntryType} that may be null.
         */
        public UDIEntryType getEntryType() {
            return entryType;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (deviceIdentifier != null) || 
                (issuer != null) || 
                (jurisdiction != null) || 
                (carrierAIDC != null) || 
                (carrierHRF != null) || 
                (entryType != null);
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
                    accept(carrierAIDC, "carrierAIDC", visitor);
                    accept(carrierHRF, "carrierHRF", visitor);
                    accept(entryType, "entryType", visitor);
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
            UdiCarrier other = (UdiCarrier) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(deviceIdentifier, other.deviceIdentifier) && 
                Objects.equals(issuer, other.issuer) && 
                Objects.equals(jurisdiction, other.jurisdiction) && 
                Objects.equals(carrierAIDC, other.carrierAIDC) && 
                Objects.equals(carrierHRF, other.carrierHRF) && 
                Objects.equals(entryType, other.entryType);
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
                    jurisdiction, 
                    carrierAIDC, 
                    carrierHRF, 
                    entryType);
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
            private Base64Binary carrierAIDC;
            private String carrierHRF;
            private UDIEntryType entryType;

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
             * @param deviceIdentifier
             *     Mandatory fixed portion of UDI
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
             * The device identifier (DI) is a mandatory, fixed portion of a UDI that identifies the labeler and the specific version 
             * or model of a device.
             * 
             * @param deviceIdentifier
             *     Mandatory fixed portion of UDI
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder deviceIdentifier(String deviceIdentifier) {
                this.deviceIdentifier = deviceIdentifier;
                return this;
            }

            /**
             * Organization that is charged with issuing UDIs for devices. For example, the US FDA issuers include :
             * 1) GS1: 
             * http://hl7.org/fhir/NamingSystem/gs1-di, 
             * 2) HIBCC:
             * http://hl7.org/fhir/NamingSystem/hibcc-dI, 
             * 3) ICCBBA for blood containers:
             * http://hl7.org/fhir/NamingSystem/iccbba-blood-di, 
             * 4) ICCBA for other devices:
             * http://hl7.org/fhir/NamingSystem/iccbba-other-di.
             * 
             * @param issuer
             *     UDI Issuing Organization
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder issuer(Uri issuer) {
                this.issuer = issuer;
                return this;
            }

            /**
             * The identity of the authoritative source for UDI generation within a jurisdiction. All UDIs are globally unique within 
             * a single namespace with the appropriate repository uri as the system. For example, UDIs of devices managed in the U.S. 
             * by the FDA, the value is http://hl7.org/fhir/NamingSystem/fda-udi.
             * 
             * @param jurisdiction
             *     Regional UDI authority
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder jurisdiction(Uri jurisdiction) {
                this.jurisdiction = jurisdiction;
                return this;
            }

            /**
             * The full UDI carrier of the Automatic Identification and Data Capture (AIDC) technology representation of the barcode 
             * string as printed on the packaging of the device - e.g., a barcode or RFID. Because of limitations on character sets 
             * in XML and the need to round-trip JSON data through XML, AIDC Formats *SHALL* be base64 encoded.
             * 
             * @param carrierAIDC
             *     UDI Machine Readable Barcode String
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder carrierAIDC(Base64Binary carrierAIDC) {
                this.carrierAIDC = carrierAIDC;
                return this;
            }

            /**
             * Convenience method for setting {@code carrierHRF}.
             * 
             * @param carrierHRF
             *     UDI Human Readable Barcode String
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #carrierHRF(com.ibm.fhir.model.type.String)
             */
            public Builder carrierHRF(java.lang.String carrierHRF) {
                this.carrierHRF = (carrierHRF == null) ? null : String.of(carrierHRF);
                return this;
            }

            /**
             * The full UDI carrier as the human readable form (HRF) representation of the barcode string as printed on the packaging 
             * of the device.
             * 
             * @param carrierHRF
             *     UDI Human Readable Barcode String
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder carrierHRF(String carrierHRF) {
                this.carrierHRF = carrierHRF;
                return this;
            }

            /**
             * A coded entry to indicate how the data was entered.
             * 
             * @param entryType
             *     barcode | rfid | manual +
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder entryType(UDIEntryType entryType) {
                this.entryType = entryType;
                return this;
            }

            /**
             * Build the {@link UdiCarrier}
             * 
             * @return
             *     An immutable object of type {@link UdiCarrier}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid UdiCarrier per the base specification
             */
            @Override
            public UdiCarrier build() {
                UdiCarrier udiCarrier = new UdiCarrier(this);
                if (validating) {
                    validate(udiCarrier);
                }
                return udiCarrier;
            }

            protected void validate(UdiCarrier udiCarrier) {
                super.validate(udiCarrier);
                ValidationSupport.requireValueOrChildren(udiCarrier);
            }

            protected Builder from(UdiCarrier udiCarrier) {
                super.from(udiCarrier);
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
     * This represents the manufacturer's name of the device as provided by the device, from a UDI label, or by a person 
     * describing the Device. This typically would be used when a person provides the name(s) or when the device represents 
     * one of the names available from DeviceDefinition.
     */
    public static class DeviceName extends BackboneElement {
        @Required
        private final String name;
        @Binding(
            bindingName = "DeviceNameType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of name the device is referred by.",
            valueSet = "http://hl7.org/fhir/ValueSet/device-nametype|4.3.0"
        )
        @Required
        private final DeviceNameType type;

        private DeviceName(Builder builder) {
            super(builder);
            name = builder.name;
            type = builder.type;
        }

        /**
         * The name that identifies the device.
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
             *     The name that identifies the device
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
             * The name that identifies the device.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     The name that identifies the device
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
        private final CodeableConcept systemType;
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
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getSystemType() {
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
            private CodeableConcept systemType;
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
            public Builder systemType(CodeableConcept systemType) {
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
     * The actual design of the device or software version running on the device.
     */
    public static class Version extends BackboneElement {
        private final CodeableConcept type;
        private final Identifier component;
        @Required
        private final String value;

        private Version(Builder builder) {
            super(builder);
            type = builder.type;
            component = builder.component;
            value = builder.value;
        }

        /**
         * The type of the device version, e.g. manufacturer, approved, internal.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * A single component of the device version.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getComponent() {
            return component;
        }

        /**
         * The version text.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getValue() {
            return value;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (component != null) || 
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
                    accept(component, "component", visitor);
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
            Version other = (Version) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(component, other.component) && 
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
                    component, 
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
            private Identifier component;
            private String value;

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
             * The type of the device version, e.g. manufacturer, approved, internal.
             * 
             * @param type
             *     The type of the device version, e.g. manufacturer, approved, internal
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * A single component of the device version.
             * 
             * @param component
             *     A single component of the device version
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder component(Identifier component) {
                this.component = component;
                return this;
            }

            /**
             * Convenience method for setting {@code value}.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     The version text
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(com.ibm.fhir.model.type.String)
             */
            public Builder value(java.lang.String value) {
                this.value = (value == null) ? null : String.of(value);
                return this;
            }

            /**
             * The version text.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     The version text
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(String value) {
                this.value = value;
                return this;
            }

            /**
             * Build the {@link Version}
             * 
             * <p>Required elements:
             * <ul>
             * <li>value</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Version}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Version per the base specification
             */
            @Override
            public Version build() {
                Version version = new Version(this);
                if (validating) {
                    validate(version);
                }
                return version;
            }

            protected void validate(Version version) {
                super.validate(version);
                ValidationSupport.requireNonNull(version.value, "value");
                ValidationSupport.requireValueOrChildren(version);
            }

            protected Builder from(Version version) {
                super.from(version);
                type = version.type;
                component = version.component;
                value = version.value;
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
}
