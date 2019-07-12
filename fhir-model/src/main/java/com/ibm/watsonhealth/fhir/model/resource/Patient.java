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

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.Address;
import com.ibm.watsonhealth.fhir.model.type.AdministrativeGender;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.LinkType;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Demographics and other administrative information about an individual or animal receiving care or other health-related 
 * services.
 * </p>
 */
@Constraint(
    id = "pat-1",
    level = "Rule",
    location = "Patient.contact",
    description = "SHALL at least contain a contact's details or a reference to an organization",
    expression = "name.exists() or telecom.exists() or address.exists() or organization.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Patient extends DomainResource {
    private final List<Identifier> identifier;
    private final Boolean active;
    private final List<HumanName> name;
    private final List<ContactPoint> telecom;
    private final AdministrativeGender gender;
    private final Date birthDate;
    private final Element deceased;
    private final List<Address> address;
    private final CodeableConcept maritalStatus;
    private final Element multipleBirth;
    private final List<Attachment> photo;
    private final List<Contact> contact;
    private final List<Communication> communication;
    private final List<Reference> generalPractitioner;
    private final Reference managingOrganization;
    private final List<Link> link;

    private volatile int hashCode;

    private Patient(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        active = builder.active;
        name = Collections.unmodifiableList(builder.name);
        telecom = Collections.unmodifiableList(builder.telecom);
        gender = builder.gender;
        birthDate = builder.birthDate;
        deceased = ValidationSupport.choiceElement(builder.deceased, "deceased", Boolean.class, DateTime.class);
        address = Collections.unmodifiableList(builder.address);
        maritalStatus = builder.maritalStatus;
        multipleBirth = ValidationSupport.choiceElement(builder.multipleBirth, "multipleBirth", Boolean.class, Integer.class);
        photo = Collections.unmodifiableList(builder.photo);
        contact = Collections.unmodifiableList(builder.contact);
        communication = Collections.unmodifiableList(builder.communication);
        generalPractitioner = Collections.unmodifiableList(builder.generalPractitioner);
        managingOrganization = builder.managingOrganization;
        link = Collections.unmodifiableList(builder.link);
    }

    /**
     * <p>
     * An identifier for this patient.
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
     * Whether this patient record is in active use. 
     * Many systems use this property to mark as non-current patients, such as those that have not been seen for a period of 
     * time based on an organization's business rules.
     * </p>
     * <p>
     * It is often used to filter patient lists to exclude inactive patients
     * </p>
     * <p>
     * Deceased patients may also be marked as inactive for the same reasons, but may be active for some time after death.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * <p>
     * A name associated with the individual.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link HumanName}.
     */
    public List<HumanName> getName() {
        return name;
    }

    /**
     * <p>
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactPoint}.
     */
    public List<ContactPoint> getTelecom() {
        return telecom;
    }

    /**
     * <p>
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping 
     * purposes.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link AdministrativeGender}.
     */
    public AdministrativeGender getGender() {
        return gender;
    }

    /**
     * <p>
     * The date of birth for the individual.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Date}.
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * <p>
     * Indicates if the individual is deceased or not.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getDeceased() {
        return deceased;
    }

    /**
     * <p>
     * An address for the individual.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Address}.
     */
    public List<Address> getAddress() {
        return address;
    }

    /**
     * <p>
     * This field contains a patient's most recent marital (civil) status.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * <p>
     * Indicates whether the patient is part of a multiple (boolean) or indicates the actual birth order (integer).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getMultipleBirth() {
        return multipleBirth;
    }

    /**
     * <p>
     * Image of the patient.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Attachment}.
     */
    public List<Attachment> getPhoto() {
        return photo;
    }

    /**
     * <p>
     * A contact party (e.g. guardian, partner, friend) for the patient.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Contact}.
     */
    public List<Contact> getContact() {
        return contact;
    }

    /**
     * <p>
     * A language which may be used to communicate with the patient about his or her health.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Communication}.
     */
    public List<Communication> getCommunication() {
        return communication;
    }

    /**
     * <p>
     * Patient's nominated care provider.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getGeneralPractitioner() {
        return generalPractitioner;
    }

    /**
     * <p>
     * Organization that is the custodian of the patient record.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getManagingOrganization() {
        return managingOrganization;
    }

    /**
     * <p>
     * Link to another patient resource that concerns the same actual patient.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Link}.
     */
    public List<Link> getLink() {
        return link;
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
            visitor.visitEnd(elementName, this);
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
        // optional
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * An identifier for this patient.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * An identifier for this patient.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     An identifier for this patient
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
         * Whether this patient record is in active use. 
         * Many systems use this property to mark as non-current patients, such as those that have not been seen for a period of 
         * time based on an organization's business rules.
         * </p>
         * <p>
         * It is often used to filter patient lists to exclude inactive patients
         * </p>
         * <p>
         * Deceased patients may also be marked as inactive for the same reasons, but may be active for some time after death.
         * </p>
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
         * <p>
         * A name associated with the individual.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A name associated with the individual.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param name
         *     A name associated with the patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(Collection<HumanName> name) {
            this.name = new ArrayList<>(name);
            return this;
        }

        /**
         * <p>
         * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param telecom
         *     A contact detail for the individual
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder telecom(Collection<ContactPoint> telecom) {
            this.telecom = new ArrayList<>(telecom);
            return this;
        }

        /**
         * <p>
         * Administrative Gender - the gender that the patient is considered to have for administration and record keeping 
         * purposes.
         * </p>
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
         * <p>
         * The date of birth for the individual.
         * </p>
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
         * <p>
         * Indicates if the individual is deceased or not.
         * </p>
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
         * <p>
         * An address for the individual.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * An address for the individual.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param address
         *     An address for the individual
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder address(Collection<Address> address) {
            this.address = new ArrayList<>(address);
            return this;
        }

        /**
         * <p>
         * This field contains a patient's most recent marital (civil) status.
         * </p>
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
         * <p>
         * Indicates whether the patient is part of a multiple (boolean) or indicates the actual birth order (integer).
         * </p>
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
         * <p>
         * Image of the patient.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Image of the patient.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param photo
         *     Image of the patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder photo(Collection<Attachment> photo) {
            this.photo = new ArrayList<>(photo);
            return this;
        }

        /**
         * <p>
         * A contact party (e.g. guardian, partner, friend) for the patient.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A contact party (e.g. guardian, partner, friend) for the patient.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contact
         *     A contact party (e.g. guardian, partner, friend) for the patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(Collection<Contact> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * <p>
         * A language which may be used to communicate with the patient about his or her health.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A language which may be used to communicate with the patient about his or her health.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param communication
         *     A language which may be used to communicate with the patient about his or her health
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder communication(Collection<Communication> communication) {
            this.communication = new ArrayList<>(communication);
            return this;
        }

        /**
         * <p>
         * Patient's nominated care provider.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Patient's nominated care provider.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param generalPractitioner
         *     Patient's nominated primary care provider
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder generalPractitioner(Collection<Reference> generalPractitioner) {
            this.generalPractitioner = new ArrayList<>(generalPractitioner);
            return this;
        }

        /**
         * <p>
         * Organization that is the custodian of the patient record.
         * </p>
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
         * <p>
         * Link to another patient resource that concerns the same actual patient.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Link to another patient resource that concerns the same actual patient.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param link
         *     Link to another patient resource that concerns the same actual person
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder link(Collection<Link> link) {
            this.link = new ArrayList<>(link);
            return this;
        }

        @Override
        public Patient build() {
            return new Patient(this);
        }

        private Builder from(Patient patient) {
            id = patient.id;
            meta = patient.meta;
            implicitRules = patient.implicitRules;
            language = patient.language;
            text = patient.text;
            contained.addAll(patient.contained);
            extension.addAll(patient.extension);
            modifierExtension.addAll(patient.modifierExtension);
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
     * <p>
     * A contact party (e.g. guardian, partner, friend) for the patient.
     * </p>
     */
    public static class Contact extends BackboneElement {
        private final List<CodeableConcept> relationship;
        private final HumanName name;
        private final List<ContactPoint> telecom;
        private final Address address;
        private final AdministrativeGender gender;
        private final Reference organization;
        private final Period period;

        private volatile int hashCode;

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
         * <p>
         * The nature of the relationship between the patient and the contact person.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getRelationship() {
            return relationship;
        }

        /**
         * <p>
         * A name associated with the contact person.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link HumanName}.
         */
        public HumanName getName() {
            return name;
        }

        /**
         * <p>
         * A contact detail for the person, e.g. a telephone number or an email address.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ContactPoint}.
         */
        public List<ContactPoint> getTelecom() {
            return telecom;
        }

        /**
         * <p>
         * Address for the contact person.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Address}.
         */
        public Address getAddress() {
            return address;
        }

        /**
         * <p>
         * Administrative Gender - the gender that the contact person is considered to have for administration and record keeping 
         * purposes.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link AdministrativeGender}.
         */
        public AdministrativeGender getGender() {
            return gender;
        }

        /**
         * <p>
         * Organization on behalf of which the contact is acting or for which the contact is working.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getOrganization() {
            return organization;
        }

        /**
         * <p>
         * The period during which this contact person or organization is valid to be contacted relating to this patient.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getPeriod() {
            return period;
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
                    accept(relationship, "relationship", visitor, CodeableConcept.class);
                    accept(name, "name", visitor);
                    accept(telecom, "telecom", visitor, ContactPoint.class);
                    accept(address, "address", visitor);
                    accept(gender, "gender", visitor);
                    accept(organization, "organization", visitor);
                    accept(period, "period", visitor);
                }
                visitor.visitEnd(elementName, this);
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
            // optional
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * The nature of the relationship between the patient and the contact person.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * The nature of the relationship between the patient and the contact person.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param relationship
             *     The kind of relationship
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder relationship(Collection<CodeableConcept> relationship) {
                this.relationship = new ArrayList<>(relationship);
                return this;
            }

            /**
             * <p>
             * A name associated with the contact person.
             * </p>
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
             * <p>
             * A contact detail for the person, e.g. a telephone number or an email address.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * A contact detail for the person, e.g. a telephone number or an email address.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param telecom
             *     A contact detail for the person
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder telecom(Collection<ContactPoint> telecom) {
                this.telecom = new ArrayList<>(telecom);
                return this;
            }

            /**
             * <p>
             * Address for the contact person.
             * </p>
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
             * <p>
             * Administrative Gender - the gender that the contact person is considered to have for administration and record keeping 
             * purposes.
             * </p>
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
             * <p>
             * Organization on behalf of which the contact is acting or for which the contact is working.
             * </p>
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
             * <p>
             * The period during which this contact person or organization is valid to be contacted relating to this patient.
             * </p>
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

            @Override
            public Contact build() {
                return new Contact(this);
            }

            private Builder from(Contact contact) {
                id = contact.id;
                extension.addAll(contact.extension);
                modifierExtension.addAll(contact.modifierExtension);
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
     * <p>
     * A language which may be used to communicate with the patient about his or her health.
     * </p>
     */
    public static class Communication extends BackboneElement {
        private final CodeableConcept language;
        private final Boolean preferred;

        private volatile int hashCode;

        private Communication(Builder builder) {
            super(builder);
            language = ValidationSupport.requireNonNull(builder.language, "language");
            preferred = builder.preferred;
        }

        /**
         * <p>
         * The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 
         * code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England 
         * English.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getLanguage() {
            return language;
        }

        /**
         * <p>
         * Indicates whether or not the patient prefers this language (over other languages he masters up a certain level).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getPreferred() {
            return preferred;
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
                    accept(language, "language", visitor);
                    accept(preferred, "preferred", visitor);
                }
                visitor.visitEnd(elementName, this);
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
            return new Builder(language).from(this);
        }

        public Builder toBuilder(CodeableConcept language) {
            return new Builder(language).from(this);
        }

        public static Builder builder(CodeableConcept language) {
            return new Builder(language);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept language;

            // optional
            private Boolean preferred;

            private Builder(CodeableConcept language) {
                super();
                this.language = language;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * Indicates whether or not the patient prefers this language (over other languages he masters up a certain level).
             * </p>
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

            @Override
            public Communication build() {
                return new Communication(this);
            }

            private Builder from(Communication communication) {
                id = communication.id;
                extension.addAll(communication.extension);
                modifierExtension.addAll(communication.modifierExtension);
                preferred = communication.preferred;
                return this;
            }
        }
    }

    /**
     * <p>
     * Link to another patient resource that concerns the same actual patient.
     * </p>
     */
    public static class Link extends BackboneElement {
        private final Reference other;
        private final LinkType type;

        private volatile int hashCode;

        private Link(Builder builder) {
            super(builder);
            other = ValidationSupport.requireNonNull(builder.other, "other");
            type = ValidationSupport.requireNonNull(builder.type, "type");
        }

        /**
         * <p>
         * The other patient resource that the link refers to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getOther() {
            return other;
        }

        /**
         * <p>
         * The type of link between this patient resource and another patient resource.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link LinkType}.
         */
        public LinkType getType() {
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
                    accept(other, "other", visitor);
                    accept(type, "type", visitor);
                }
                visitor.visitEnd(elementName, this);
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
            return new Builder(other, type).from(this);
        }

        public Builder toBuilder(Reference other, LinkType type) {
            return new Builder(other, type).from(this);
        }

        public static Builder builder(Reference other, LinkType type) {
            return new Builder(other, type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference other;
            private final LinkType type;

            private Builder(Reference other, LinkType type) {
                super();
                this.other = other;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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

            @Override
            public Link build() {
                return new Link(this);
            }

            private Builder from(Link link) {
                id = link.id;
                extension.addAll(link.extension);
                modifierExtension.addAll(link.modifierExtension);
                return this;
            }
        }
    }
}
