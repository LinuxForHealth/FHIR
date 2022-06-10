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
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.LinkType;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Demographics and other administrative information about an individual or animal receiving care or other health-related 
 * services.
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.Value.NORMATIVE
)
@Constraint(
    id = "pat-1",
    level = "Rule",
    location = "Patient.contact",
    description = "SHALL at least contain a contact's details or a reference to an organization",
    expression = "name.exists() or telecom.exists() or address.exists() or organization.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Patient"
)
@Constraint(
    id = "patient-2",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/marital-status",
    expression = "maritalStatus.exists() implies (maritalStatus.memberOf('http://hl7.org/fhir/ValueSet/marital-status', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/Patient",
    generated = true
)
@Constraint(
    id = "patient-3",
    level = "Warning",
    location = "contact.relationship",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/patient-contactrelationship",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/patient-contactrelationship', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Patient",
    generated = true
)
@Constraint(
    id = "patient-4",
    level = "Warning",
    location = "communication.language",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/languages",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/Patient",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Patient extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final Boolean active;
    @Summary
    private final List<HumanName> name;
    @Summary
    private final List<ContactPoint> telecom;
    @Summary
    @Binding(
        bindingName = "AdministrativeGender",
        strength = BindingStrength.Value.REQUIRED,
        description = "The gender of a person used for administrative purposes.",
        valueSet = "http://hl7.org/fhir/ValueSet/administrative-gender|4.3.0"
    )
    private final AdministrativeGender gender;
    @Summary
    private final Date birthDate;
    @Summary
    @Choice({ Boolean.class, DateTime.class })
    private final Element deceased;
    @Summary
    private final List<Address> address;
    @Binding(
        bindingName = "MaritalStatus",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "The domestic partnership status of a person.",
        valueSet = "http://hl7.org/fhir/ValueSet/marital-status"
    )
    private final CodeableConcept maritalStatus;
    @Choice({ Boolean.class, Integer.class })
    private final Element multipleBirth;
    private final List<Attachment> photo;
    private final List<Contact> contact;
    private final List<Communication> communication;
    @ReferenceTarget({ "Organization", "Practitioner", "PractitionerRole" })
    private final List<Reference> generalPractitioner;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference managingOrganization;
    @Summary
    private final List<Link> link;

    private Patient(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        active = builder.active;
        name = Collections.unmodifiableList(builder.name);
        telecom = Collections.unmodifiableList(builder.telecom);
        gender = builder.gender;
        birthDate = builder.birthDate;
        deceased = builder.deceased;
        address = Collections.unmodifiableList(builder.address);
        maritalStatus = builder.maritalStatus;
        multipleBirth = builder.multipleBirth;
        photo = Collections.unmodifiableList(builder.photo);
        contact = Collections.unmodifiableList(builder.contact);
        communication = Collections.unmodifiableList(builder.communication);
        generalPractitioner = Collections.unmodifiableList(builder.generalPractitioner);
        managingOrganization = builder.managingOrganization;
        link = Collections.unmodifiableList(builder.link);
    }

    /**
     * An identifier for this patient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Whether this patient record is in active use. 
     * Many systems use this property to mark as non-current patients, such as those that have not been seen for a period of 
     * time based on an organization's business rules.
     * 
     * <p>It is often used to filter patient lists to exclude inactive patients
     * 
     * <p>Deceased patients may also be marked as inactive for the same reasons, but may be active for some time after death.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * A name associated with the individual.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link HumanName} that may be empty.
     */
    public List<HumanName> getName() {
        return name;
    }

    /**
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactPoint} that may be empty.
     */
    public List<ContactPoint> getTelecom() {
        return telecom;
    }

    /**
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping 
     * purposes.
     * 
     * @return
     *     An immutable object of type {@link AdministrativeGender} that may be null.
     */
    public AdministrativeGender getGender() {
        return gender;
    }

    /**
     * The date of birth for the individual.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Indicates if the individual is deceased or not.
     * 
     * @return
     *     An immutable object of type {@link Boolean} or {@link DateTime} that may be null.
     */
    public Element getDeceased() {
        return deceased;
    }

    /**
     * An address for the individual.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Address} that may be empty.
     */
    public List<Address> getAddress() {
        return address;
    }

    /**
     * This field contains a patient's most recent marital (civil) status.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Indicates whether the patient is part of a multiple (boolean) or indicates the actual birth order (integer).
     * 
     * @return
     *     An immutable object of type {@link Boolean} or {@link Integer} that may be null.
     */
    public Element getMultipleBirth() {
        return multipleBirth;
    }

    /**
     * Image of the patient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Attachment} that may be empty.
     */
    public List<Attachment> getPhoto() {
        return photo;
    }

    /**
     * A contact party (e.g. guardian, partner, friend) for the patient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Contact} that may be empty.
     */
    public List<Contact> getContact() {
        return contact;
    }

    /**
     * A language which may be used to communicate with the patient about his or her health.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Communication} that may be empty.
     */
    public List<Communication> getCommunication() {
        return communication;
    }

    /**
     * Patient's nominated care provider.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getGeneralPractitioner() {
        return generalPractitioner;
    }

    /**
     * Organization that is the custodian of the patient record.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getManagingOrganization() {
        return managingOrganization;
    }

    /**
     * Link to another patient resource that concerns the same actual patient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Link} that may be empty.
     */
    public List<Link> getLink() {
        return link;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (active != null) || 
            !name.isEmpty() || 
            !telecom.isEmpty() || 
            (gender != null) || 
            (birthDate != null) || 
            (deceased != null) || 
            !address.isEmpty() || 
            (maritalStatus != null) || 
            (multipleBirth != null) || 
            !photo.isEmpty() || 
            !contact.isEmpty() || 
            !communication.isEmpty() || 
            !generalPractitioner.isEmpty() || 
            (managingOrganization != null) || 
            !link.isEmpty();
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
                accept(active, "active", visitor);
                accept(name, "name", visitor, HumanName.class);
                accept(telecom, "telecom", visitor, ContactPoint.class);
                accept(gender, "gender", visitor);
                accept(birthDate, "birthDate", visitor);
                accept(deceased, "deceased", visitor);
                accept(address, "address", visitor, Address.class);
                accept(maritalStatus, "maritalStatus", visitor);
                accept(multipleBirth, "multipleBirth", visitor);
                accept(photo, "photo", visitor, Attachment.class);
                accept(contact, "contact", visitor, Contact.class);
                accept(communication, "communication", visitor, Communication.class);
                accept(generalPractitioner, "generalPractitioner", visitor, Reference.class);
                accept(managingOrganization, "managingOrganization", visitor);
                accept(link, "link", visitor, Link.class);
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
        Patient other = (Patient) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(active, other.active) && 
            Objects.equals(name, other.name) && 
            Objects.equals(telecom, other.telecom) && 
            Objects.equals(gender, other.gender) && 
            Objects.equals(birthDate, other.birthDate) && 
            Objects.equals(deceased, other.deceased) && 
            Objects.equals(address, other.address) && 
            Objects.equals(maritalStatus, other.maritalStatus) && 
            Objects.equals(multipleBirth, other.multipleBirth) && 
            Objects.equals(photo, other.photo) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(communication, other.communication) && 
            Objects.equals(generalPractitioner, other.generalPractitioner) && 
            Objects.equals(managingOrganization, other.managingOrganization) && 
            Objects.equals(link, other.link);
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
                active, 
                name, 
                telecom, 
                gender, 
                birthDate, 
                deceased, 
                address, 
                maritalStatus, 
                multipleBirth, 
                photo, 
                contact, 
                communication, 
                generalPractitioner, 
                managingOrganization, 
                link);
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
        private Boolean active;
        private List<HumanName> name = new ArrayList<>();
        private List<ContactPoint> telecom = new ArrayList<>();
        private AdministrativeGender gender;
        private Date birthDate;
        private Element deceased;
        private List<Address> address = new ArrayList<>();
        private CodeableConcept maritalStatus;
        private Element multipleBirth;
        private List<Attachment> photo = new ArrayList<>();
        private List<Contact> contact = new ArrayList<>();
        private List<Communication> communication = new ArrayList<>();
        private List<Reference> generalPractitioner = new ArrayList<>();
        private Reference managingOrganization;
        private List<Link> link = new ArrayList<>();

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
         * An identifier for this patient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     An identifier for this patient
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
         * An identifier for this patient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     An identifier for this patient
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
         * Convenience method for setting {@code active}.
         * 
         * @param active
         *     Whether this patient's record is in active use
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #active(com.ibm.fhir.model.type.Boolean)
         */
        public Builder active(java.lang.Boolean active) {
            this.active = (active == null) ? null : Boolean.of(active);
            return this;
        }

        /**
         * Whether this patient record is in active use. 
         * Many systems use this property to mark as non-current patients, such as those that have not been seen for a period of 
         * time based on an organization's business rules.
         * 
         * <p>It is often used to filter patient lists to exclude inactive patients
         * 
         * <p>Deceased patients may also be marked as inactive for the same reasons, but may be active for some time after death.
         * 
         * @param active
         *     Whether this patient's record is in active use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        /**
         * A name associated with the individual.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param name
         *     A name associated with the patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(HumanName... name) {
            for (HumanName value : name) {
                this.name.add(value);
            }
            return this;
        }

        /**
         * A name associated with the individual.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param name
         *     A name associated with the patient
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder name(Collection<HumanName> name) {
            this.name = new ArrayList<>(name);
            return this;
        }

        /**
         * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param telecom
         *     A contact detail for the individual
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder telecom(ContactPoint... telecom) {
            for (ContactPoint value : telecom) {
                this.telecom.add(value);
            }
            return this;
        }

        /**
         * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param telecom
         *     A contact detail for the individual
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder telecom(Collection<ContactPoint> telecom) {
            this.telecom = new ArrayList<>(telecom);
            return this;
        }

        /**
         * Administrative Gender - the gender that the patient is considered to have for administration and record keeping 
         * purposes.
         * 
         * @param gender
         *     male | female | other | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder gender(AdministrativeGender gender) {
            this.gender = gender;
            return this;
        }

        /**
         * Convenience method for setting {@code birthDate}.
         * 
         * @param birthDate
         *     The date of birth for the individual
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #birthDate(com.ibm.fhir.model.type.Date)
         */
        public Builder birthDate(java.time.LocalDate birthDate) {
            this.birthDate = (birthDate == null) ? null : Date.of(birthDate);
            return this;
        }

        /**
         * The date of birth for the individual.
         * 
         * @param birthDate
         *     The date of birth for the individual
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        /**
         * Convenience method for setting {@code deceased} with choice type Boolean.
         * 
         * @param deceased
         *     Indicates if the individual is deceased or not
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #deceased(Element)
         */
        public Builder deceased(java.lang.Boolean deceased) {
            this.deceased = (deceased == null) ? null : Boolean.of(deceased);
            return this;
        }

        /**
         * Indicates if the individual is deceased or not.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Boolean}</li>
         * <li>{@link DateTime}</li>
         * </ul>
         * 
         * @param deceased
         *     Indicates if the individual is deceased or not
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder deceased(Element deceased) {
            this.deceased = deceased;
            return this;
        }

        /**
         * An address for the individual.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param address
         *     An address for the individual
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder address(Address... address) {
            for (Address value : address) {
                this.address.add(value);
            }
            return this;
        }

        /**
         * An address for the individual.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param address
         *     An address for the individual
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder address(Collection<Address> address) {
            this.address = new ArrayList<>(address);
            return this;
        }

        /**
         * This field contains a patient's most recent marital (civil) status.
         * 
         * @param maritalStatus
         *     Marital (civil) status of a patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder maritalStatus(CodeableConcept maritalStatus) {
            this.maritalStatus = maritalStatus;
            return this;
        }

        /**
         * Convenience method for setting {@code multipleBirth} with choice type Boolean.
         * 
         * @param multipleBirth
         *     Whether patient is part of a multiple birth
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #multipleBirth(Element)
         */
        public Builder multipleBirth(java.lang.Boolean multipleBirth) {
            this.multipleBirth = (multipleBirth == null) ? null : Boolean.of(multipleBirth);
            return this;
        }

        /**
         * Convenience method for setting {@code multipleBirth} with choice type Integer.
         * 
         * @param multipleBirth
         *     Whether patient is part of a multiple birth
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #multipleBirth(Element)
         */
        public Builder multipleBirth(java.lang.Integer multipleBirth) {
            this.multipleBirth = (multipleBirth == null) ? null : Integer.of(multipleBirth);
            return this;
        }

        /**
         * Indicates whether the patient is part of a multiple (boolean) or indicates the actual birth order (integer).
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Boolean}</li>
         * <li>{@link Integer}</li>
         * </ul>
         * 
         * @param multipleBirth
         *     Whether patient is part of a multiple birth
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder multipleBirth(Element multipleBirth) {
            this.multipleBirth = multipleBirth;
            return this;
        }

        /**
         * Image of the patient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param photo
         *     Image of the patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder photo(Attachment... photo) {
            for (Attachment value : photo) {
                this.photo.add(value);
            }
            return this;
        }

        /**
         * Image of the patient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param photo
         *     Image of the patient
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder photo(Collection<Attachment> photo) {
            this.photo = new ArrayList<>(photo);
            return this;
        }

        /**
         * A contact party (e.g. guardian, partner, friend) for the patient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     A contact party (e.g. guardian, partner, friend) for the patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(Contact... contact) {
            for (Contact value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * A contact party (e.g. guardian, partner, friend) for the patient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     A contact party (e.g. guardian, partner, friend) for the patient
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<Contact> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * A language which may be used to communicate with the patient about his or her health.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param communication
         *     A language which may be used to communicate with the patient about his or her health
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder communication(Communication... communication) {
            for (Communication value : communication) {
                this.communication.add(value);
            }
            return this;
        }

        /**
         * A language which may be used to communicate with the patient about his or her health.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param communication
         *     A language which may be used to communicate with the patient about his or her health
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder communication(Collection<Communication> communication) {
            this.communication = new ArrayList<>(communication);
            return this;
        }

        /**
         * Patient's nominated care provider.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param generalPractitioner
         *     Patient's nominated primary care provider
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder generalPractitioner(Reference... generalPractitioner) {
            for (Reference value : generalPractitioner) {
                this.generalPractitioner.add(value);
            }
            return this;
        }

        /**
         * Patient's nominated care provider.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Organization}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param generalPractitioner
         *     Patient's nominated primary care provider
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder generalPractitioner(Collection<Reference> generalPractitioner) {
            this.generalPractitioner = new ArrayList<>(generalPractitioner);
            return this;
        }

        /**
         * Organization that is the custodian of the patient record.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param managingOrganization
         *     Organization that is the custodian of the patient record
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder managingOrganization(Reference managingOrganization) {
            this.managingOrganization = managingOrganization;
            return this;
        }

        /**
         * Link to another patient resource that concerns the same actual patient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param link
         *     Link to another patient resource that concerns the same actual person
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder link(Link... link) {
            for (Link value : link) {
                this.link.add(value);
            }
            return this;
        }

        /**
         * Link to another patient resource that concerns the same actual patient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param link
         *     Link to another patient resource that concerns the same actual person
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder link(Collection<Link> link) {
            this.link = new ArrayList<>(link);
            return this;
        }

        /**
         * Build the {@link Patient}
         * 
         * @return
         *     An immutable object of type {@link Patient}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Patient per the base specification
         */
        @Override
        public Patient build() {
            Patient patient = new Patient(this);
            if (validating) {
                validate(patient);
            }
            return patient;
        }

        protected void validate(Patient patient) {
            super.validate(patient);
            ValidationSupport.checkList(patient.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(patient.name, "name", HumanName.class);
            ValidationSupport.checkList(patient.telecom, "telecom", ContactPoint.class);
            ValidationSupport.choiceElement(patient.deceased, "deceased", Boolean.class, DateTime.class);
            ValidationSupport.checkList(patient.address, "address", Address.class);
            ValidationSupport.choiceElement(patient.multipleBirth, "multipleBirth", Boolean.class, Integer.class);
            ValidationSupport.checkList(patient.photo, "photo", Attachment.class);
            ValidationSupport.checkList(patient.contact, "contact", Contact.class);
            ValidationSupport.checkList(patient.communication, "communication", Communication.class);
            ValidationSupport.checkList(patient.generalPractitioner, "generalPractitioner", Reference.class);
            ValidationSupport.checkList(patient.link, "link", Link.class);
            ValidationSupport.checkReferenceType(patient.generalPractitioner, "generalPractitioner", "Organization", "Practitioner", "PractitionerRole");
            ValidationSupport.checkReferenceType(patient.managingOrganization, "managingOrganization", "Organization");
        }

        protected Builder from(Patient patient) {
            super.from(patient);
            identifier.addAll(patient.identifier);
            active = patient.active;
            name.addAll(patient.name);
            telecom.addAll(patient.telecom);
            gender = patient.gender;
            birthDate = patient.birthDate;
            deceased = patient.deceased;
            address.addAll(patient.address);
            maritalStatus = patient.maritalStatus;
            multipleBirth = patient.multipleBirth;
            photo.addAll(patient.photo);
            contact.addAll(patient.contact);
            communication.addAll(patient.communication);
            generalPractitioner.addAll(patient.generalPractitioner);
            managingOrganization = patient.managingOrganization;
            link.addAll(patient.link);
            return this;
        }
    }

    /**
     * A contact party (e.g. guardian, partner, friend) for the patient.
     */
    public static class Contact extends BackboneElement {
        @Binding(
            bindingName = "ContactRelationship",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "The nature of the relationship between a patient and a contact person for that patient.",
            valueSet = "http://hl7.org/fhir/ValueSet/patient-contactrelationship"
        )
        private final List<CodeableConcept> relationship;
        private final HumanName name;
        private final List<ContactPoint> telecom;
        private final Address address;
        @Binding(
            bindingName = "AdministrativeGender",
            strength = BindingStrength.Value.REQUIRED,
            description = "The gender of a person used for administrative purposes.",
            valueSet = "http://hl7.org/fhir/ValueSet/administrative-gender|4.3.0"
        )
        private final AdministrativeGender gender;
        @ReferenceTarget({ "Organization" })
        private final Reference organization;
        private final Period period;

        private Contact(Builder builder) {
            super(builder);
            relationship = Collections.unmodifiableList(builder.relationship);
            name = builder.name;
            telecom = Collections.unmodifiableList(builder.telecom);
            address = builder.address;
            gender = builder.gender;
            organization = builder.organization;
            period = builder.period;
        }

        /**
         * The nature of the relationship between the patient and the contact person.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getRelationship() {
            return relationship;
        }

        /**
         * A name associated with the contact person.
         * 
         * @return
         *     An immutable object of type {@link HumanName} that may be null.
         */
        public HumanName getName() {
            return name;
        }

        /**
         * A contact detail for the person, e.g. a telephone number or an email address.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ContactPoint} that may be empty.
         */
        public List<ContactPoint> getTelecom() {
            return telecom;
        }

        /**
         * Address for the contact person.
         * 
         * @return
         *     An immutable object of type {@link Address} that may be null.
         */
        public Address getAddress() {
            return address;
        }

        /**
         * Administrative Gender - the gender that the contact person is considered to have for administration and record keeping 
         * purposes.
         * 
         * @return
         *     An immutable object of type {@link AdministrativeGender} that may be null.
         */
        public AdministrativeGender getGender() {
            return gender;
        }

        /**
         * Organization on behalf of which the contact is acting or for which the contact is working.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getOrganization() {
            return organization;
        }

        /**
         * The period during which this contact person or organization is valid to be contacted relating to this patient.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getPeriod() {
            return period;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !relationship.isEmpty() || 
                (name != null) || 
                !telecom.isEmpty() || 
                (address != null) || 
                (gender != null) || 
                (organization != null) || 
                (period != null);
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
                    accept(relationship, "relationship", visitor, CodeableConcept.class);
                    accept(name, "name", visitor);
                    accept(telecom, "telecom", visitor, ContactPoint.class);
                    accept(address, "address", visitor);
                    accept(gender, "gender", visitor);
                    accept(organization, "organization", visitor);
                    accept(period, "period", visitor);
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
            Contact other = (Contact) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(relationship, other.relationship) && 
                Objects.equals(name, other.name) && 
                Objects.equals(telecom, other.telecom) && 
                Objects.equals(address, other.address) && 
                Objects.equals(gender, other.gender) && 
                Objects.equals(organization, other.organization) && 
                Objects.equals(period, other.period);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    relationship, 
                    name, 
                    telecom, 
                    address, 
                    gender, 
                    organization, 
                    period);
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
            private List<CodeableConcept> relationship = new ArrayList<>();
            private HumanName name;
            private List<ContactPoint> telecom = new ArrayList<>();
            private Address address;
            private AdministrativeGender gender;
            private Reference organization;
            private Period period;

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
             * The nature of the relationship between the patient and the contact person.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param relationship
             *     The kind of relationship
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder relationship(CodeableConcept... relationship) {
                for (CodeableConcept value : relationship) {
                    this.relationship.add(value);
                }
                return this;
            }

            /**
             * The nature of the relationship between the patient and the contact person.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param relationship
             *     The kind of relationship
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder relationship(Collection<CodeableConcept> relationship) {
                this.relationship = new ArrayList<>(relationship);
                return this;
            }

            /**
             * A name associated with the contact person.
             * 
             * @param name
             *     A name associated with the contact person
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(HumanName name) {
                this.name = name;
                return this;
            }

            /**
             * A contact detail for the person, e.g. a telephone number or an email address.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param telecom
             *     A contact detail for the person
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder telecom(ContactPoint... telecom) {
                for (ContactPoint value : telecom) {
                    this.telecom.add(value);
                }
                return this;
            }

            /**
             * A contact detail for the person, e.g. a telephone number or an email address.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param telecom
             *     A contact detail for the person
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder telecom(Collection<ContactPoint> telecom) {
                this.telecom = new ArrayList<>(telecom);
                return this;
            }

            /**
             * Address for the contact person.
             * 
             * @param address
             *     Address for the contact person
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder address(Address address) {
                this.address = address;
                return this;
            }

            /**
             * Administrative Gender - the gender that the contact person is considered to have for administration and record keeping 
             * purposes.
             * 
             * @param gender
             *     male | female | other | unknown
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder gender(AdministrativeGender gender) {
                this.gender = gender;
                return this;
            }

            /**
             * Organization on behalf of which the contact is acting or for which the contact is working.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param organization
             *     Organization that is associated with the contact
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder organization(Reference organization) {
                this.organization = organization;
                return this;
            }

            /**
             * The period during which this contact person or organization is valid to be contacted relating to this patient.
             * 
             * @param period
             *     The period during which this contact person or organization is valid to be contacted relating to this patient
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * Build the {@link Contact}
             * 
             * @return
             *     An immutable object of type {@link Contact}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Contact per the base specification
             */
            @Override
            public Contact build() {
                Contact contact = new Contact(this);
                if (validating) {
                    validate(contact);
                }
                return contact;
            }

            protected void validate(Contact contact) {
                super.validate(contact);
                ValidationSupport.checkList(contact.relationship, "relationship", CodeableConcept.class);
                ValidationSupport.checkList(contact.telecom, "telecom", ContactPoint.class);
                ValidationSupport.checkReferenceType(contact.organization, "organization", "Organization");
                ValidationSupport.requireValueOrChildren(contact);
            }

            protected Builder from(Contact contact) {
                super.from(contact);
                relationship.addAll(contact.relationship);
                name = contact.name;
                telecom.addAll(contact.telecom);
                address = contact.address;
                gender = contact.gender;
                organization = contact.organization;
                period = contact.period;
                return this;
            }
        }
    }

    /**
     * A language which may be used to communicate with the patient about his or her health.
     */
    public static class Communication extends BackboneElement {
        @Binding(
            bindingName = "Language",
            strength = BindingStrength.Value.PREFERRED,
            description = "IETF language tag",
            valueSet = "http://hl7.org/fhir/ValueSet/languages",
            maxValueSet = "http://hl7.org/fhir/ValueSet/all-languages"
        )
        @Required
        private final CodeableConcept language;
        private final Boolean preferred;

        private Communication(Builder builder) {
            super(builder);
            language = builder.language;
            preferred = builder.preferred;
        }

        /**
         * The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 
         * code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England 
         * English.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getLanguage() {
            return language;
        }

        /**
         * Indicates whether or not the patient prefers this language (over other languages he masters up a certain level).
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getPreferred() {
            return preferred;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (language != null) || 
                (preferred != null);
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
                    accept(language, "language", visitor);
                    accept(preferred, "preferred", visitor);
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
            Communication other = (Communication) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(language, other.language) && 
                Objects.equals(preferred, other.preferred);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    language, 
                    preferred);
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
            private CodeableConcept language;
            private Boolean preferred;

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
             * The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 
             * code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England 
             * English.
             * 
             * <p>This element is required.
             * 
             * @param language
             *     The language which can be used to communicate with the patient about his or her health
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder language(CodeableConcept language) {
                this.language = language;
                return this;
            }

            /**
             * Convenience method for setting {@code preferred}.
             * 
             * @param preferred
             *     Language preference indicator
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #preferred(com.ibm.fhir.model.type.Boolean)
             */
            public Builder preferred(java.lang.Boolean preferred) {
                this.preferred = (preferred == null) ? null : Boolean.of(preferred);
                return this;
            }

            /**
             * Indicates whether or not the patient prefers this language (over other languages he masters up a certain level).
             * 
             * @param preferred
             *     Language preference indicator
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder preferred(Boolean preferred) {
                this.preferred = preferred;
                return this;
            }

            /**
             * Build the {@link Communication}
             * 
             * <p>Required elements:
             * <ul>
             * <li>language</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Communication}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Communication per the base specification
             */
            @Override
            public Communication build() {
                Communication communication = new Communication(this);
                if (validating) {
                    validate(communication);
                }
                return communication;
            }

            protected void validate(Communication communication) {
                super.validate(communication);
                ValidationSupport.requireNonNull(communication.language, "language");
                ValidationSupport.checkValueSetBinding(communication.language, "language", "http://hl7.org/fhir/ValueSet/all-languages", "urn:ietf:bcp:47");
                ValidationSupport.requireValueOrChildren(communication);
            }

            protected Builder from(Communication communication) {
                super.from(communication);
                language = communication.language;
                preferred = communication.preferred;
                return this;
            }
        }
    }

    /**
     * Link to another patient resource that concerns the same actual patient.
     */
    public static class Link extends BackboneElement {
        @Summary
        @ReferenceTarget({ "Patient", "RelatedPerson" })
        @Required
        private final Reference other;
        @Summary
        @Binding(
            bindingName = "LinkType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of link between this patient resource and another patient resource.",
            valueSet = "http://hl7.org/fhir/ValueSet/link-type|4.3.0"
        )
        @Required
        private final LinkType type;

        private Link(Builder builder) {
            super(builder);
            other = builder.other;
            type = builder.type;
        }

        /**
         * The other patient resource that the link refers to.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getOther() {
            return other;
        }

        /**
         * The type of link between this patient resource and another patient resource.
         * 
         * @return
         *     An immutable object of type {@link LinkType} that is non-null.
         */
        public LinkType getType() {
            return type;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (other != null) || 
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
                    accept(other, "other", visitor);
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
            Link other = (Link) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(this.other, other.other) && 
                Objects.equals(type, other.type);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    other, 
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
            private Reference other;
            private LinkType type;

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
             * The other patient resource that the link refers to.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Patient}</li>
             * <li>{@link RelatedPerson}</li>
             * </ul>
             * 
             * @param other
             *     The other patient or related person resource that the link refers to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder other(Reference other) {
                this.other = other;
                return this;
            }

            /**
             * The type of link between this patient resource and another patient resource.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     replaced-by | replaces | refer | seealso
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(LinkType type) {
                this.type = type;
                return this;
            }

            /**
             * Build the {@link Link}
             * 
             * <p>Required elements:
             * <ul>
             * <li>other</li>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Link}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Link per the base specification
             */
            @Override
            public Link build() {
                Link link = new Link(this);
                if (validating) {
                    validate(link);
                }
                return link;
            }

            protected void validate(Link link) {
                super.validate(link);
                ValidationSupport.requireNonNull(link.other, "other");
                ValidationSupport.requireNonNull(link.type, "type");
                ValidationSupport.checkReferenceType(link.other, "other", "Patient", "RelatedPerson");
                ValidationSupport.requireValueOrChildren(link);
            }

            protected Builder from(Link link) {
                super.from(link);
                other = link.other;
                type = link.type;
                return this;
            }
        }
    }
}
