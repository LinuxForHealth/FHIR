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

import com.ibm.watsonhealth.fhir.model.type.Address;
import com.ibm.watsonhealth.fhir.model.type.AdministrativeGender;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A person who is directly or indirectly involved in the provisioning of healthcare.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Practitioner extends DomainResource {
    private final List<Identifier> identifier;
    private final Boolean active;
    private final List<HumanName> name;
    private final List<ContactPoint> telecom;
    private final List<Address> address;
    private final AdministrativeGender gender;
    private final Date birthDate;
    private final List<Attachment> photo;
    private final List<Qualification> qualification;
    private final List<CodeableConcept> communication;

    private volatile int hashCode;

    private Practitioner(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        active = builder.active;
        name = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.name, "name"));
        telecom = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.telecom, "telecom"));
        address = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.address, "address"));
        gender = builder.gender;
        birthDate = builder.birthDate;
        photo = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.photo, "photo"));
        qualification = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.qualification, "qualification"));
        communication = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.communication, "communication"));
    }

    /**
     * <p>
     * An identifier that applies to this person in this role.
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
     * Whether this practitioner's record is in active use.
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
     * The name(s) associated with the practitioner.
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
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
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
     * Address(es) of the practitioner that are not role specific (typically home address). 
Work addresses are not typically 
     * entered in this property as they are usually role dependent.
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
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping 
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
     * The date of birth for the practitioner.
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
     * Image of the person.
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
     * The official certifications, training, and licenses that authorize or otherwise pertain to the provision of care by 
     * the practitioner. For example, a medical license issued by a medical board authorizing the practitioner to practice 
     * medicine within a certian locality.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Qualification}.
     */
    public List<Qualification> getQualification() {
        return qualification;
    }

    /**
     * <p>
     * A language the practitioner can use in patient communication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCommunication() {
        return communication;
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
                accept(address, "address", visitor, Address.class);
                accept(gender, "gender", visitor);
                accept(birthDate, "birthDate", visitor);
                accept(photo, "photo", visitor, Attachment.class);
                accept(qualification, "qualification", visitor, Qualification.class);
                accept(communication, "communication", visitor, CodeableConcept.class);
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
        Practitioner other = (Practitioner) obj;
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
            Objects.equals(address, other.address) && 
            Objects.equals(gender, other.gender) && 
            Objects.equals(birthDate, other.birthDate) && 
            Objects.equals(photo, other.photo) && 
            Objects.equals(qualification, other.qualification) && 
            Objects.equals(communication, other.communication);
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
                address, 
                gender, 
                birthDate, 
                photo, 
                qualification, 
                communication);
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
        private List<Address> address = new ArrayList<>();
        private AdministrativeGender gender;
        private Date birthDate;
        private List<Attachment> photo = new ArrayList<>();
        private List<Qualification> qualification = new ArrayList<>();
        private List<CodeableConcept> communication = new ArrayList<>();

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
         * An identifier that applies to this person in this role.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     An identifier for the person as this agent
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
         * An identifier that applies to this person in this role.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     An identifier for the person as this agent
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
         * Whether this practitioner's record is in active use.
         * </p>
         * 
         * @param active
         *     Whether this practitioner's record is in active use
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
         * The name(s) associated with the practitioner.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param name
         *     The name(s) associated with the practitioner
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
         * The name(s) associated with the practitioner.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param name
         *     The name(s) associated with the practitioner
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
         * A contact detail for the practitioner, e.g. a telephone number or an email address.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param telecom
         *     A contact detail for the practitioner (that apply to all roles)
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
         * A contact detail for the practitioner, e.g. a telephone number or an email address.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param telecom
         *     A contact detail for the practitioner (that apply to all roles)
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
         * Address(es) of the practitioner that are not role specific (typically home address). 
Work addresses are not typically 
         * entered in this property as they are usually role dependent.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param address
         *     Address(es) of the practitioner that are not role specific (typically home address)
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
         * Address(es) of the practitioner that are not role specific (typically home address). 
Work addresses are not typically 
         * entered in this property as they are usually role dependent.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param address
         *     Address(es) of the practitioner that are not role specific (typically home address)
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
         * Administrative Gender - the gender that the person is considered to have for administration and record keeping 
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
         * The date of birth for the practitioner.
         * </p>
         * 
         * @param birthDate
         *     The date on which the practitioner was born
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
         * Image of the person.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param photo
         *     Image of the person
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
         * Image of the person.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param photo
         *     Image of the person
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
         * The official certifications, training, and licenses that authorize or otherwise pertain to the provision of care by 
         * the practitioner. For example, a medical license issued by a medical board authorizing the practitioner to practice 
         * medicine within a certian locality.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param qualification
         *     Certification, licenses, or training pertaining to the provision of care
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder qualification(Qualification... qualification) {
            for (Qualification value : qualification) {
                this.qualification.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The official certifications, training, and licenses that authorize or otherwise pertain to the provision of care by 
         * the practitioner. For example, a medical license issued by a medical board authorizing the practitioner to practice 
         * medicine within a certian locality.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param qualification
         *     Certification, licenses, or training pertaining to the provision of care
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder qualification(Collection<Qualification> qualification) {
            this.qualification = new ArrayList<>(qualification);
            return this;
        }

        /**
         * <p>
         * A language the practitioner can use in patient communication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param communication
         *     A language the practitioner can use in patient communication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder communication(CodeableConcept... communication) {
            for (CodeableConcept value : communication) {
                this.communication.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A language the practitioner can use in patient communication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param communication
         *     A language the practitioner can use in patient communication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder communication(Collection<CodeableConcept> communication) {
            this.communication = new ArrayList<>(communication);
            return this;
        }

        @Override
        public Practitioner build() {
            return new Practitioner(this);
        }

        private Builder from(Practitioner practitioner) {
            id = practitioner.id;
            meta = practitioner.meta;
            implicitRules = practitioner.implicitRules;
            language = practitioner.language;
            text = practitioner.text;
            contained.addAll(practitioner.contained);
            extension.addAll(practitioner.extension);
            modifierExtension.addAll(practitioner.modifierExtension);
            identifier.addAll(practitioner.identifier);
            active = practitioner.active;
            name.addAll(practitioner.name);
            telecom.addAll(practitioner.telecom);
            address.addAll(practitioner.address);
            gender = practitioner.gender;
            birthDate = practitioner.birthDate;
            photo.addAll(practitioner.photo);
            qualification.addAll(practitioner.qualification);
            communication.addAll(practitioner.communication);
            return this;
        }
    }

    /**
     * <p>
     * The official certifications, training, and licenses that authorize or otherwise pertain to the provision of care by 
     * the practitioner. For example, a medical license issued by a medical board authorizing the practitioner to practice 
     * medicine within a certian locality.
     * </p>
     */
    public static class Qualification extends BackboneElement {
        private final List<Identifier> identifier;
        private final CodeableConcept code;
        private final Period period;
        private final Reference issuer;

        private volatile int hashCode;

        private Qualification(Builder builder) {
            super(builder);
            identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
            code = ValidationSupport.requireNonNull(builder.code, "code");
            period = builder.period;
            issuer = builder.issuer;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * An identifier that applies to this person's qualification in this role.
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
         * Coded representation of the qualification.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * <p>
         * Period during which the qualification is valid.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * <p>
         * Organization that regulates and issues the qualification.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getIssuer() {
            return issuer;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !identifier.isEmpty() || 
                (code != null) || 
                (period != null) || 
                (issuer != null);
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
                    accept(code, "code", visitor);
                    accept(period, "period", visitor);
                    accept(issuer, "issuer", visitor);
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
            Qualification other = (Qualification) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(code, other.code) && 
                Objects.equals(period, other.period) && 
                Objects.equals(issuer, other.issuer);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identifier, 
                    code, 
                    period, 
                    issuer);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(code).from(this);
        }

        public Builder toBuilder(CodeableConcept code) {
            return new Builder(code).from(this);
        }

        public static Builder builder(CodeableConcept code) {
            return new Builder(code);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept code;

            // optional
            private List<Identifier> identifier = new ArrayList<>();
            private Period period;
            private Reference issuer;

            private Builder(CodeableConcept code) {
                super();
                this.code = code;
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
             * An identifier that applies to this person's qualification in this role.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param identifier
             *     An identifier for this qualification for the practitioner
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
             * An identifier that applies to this person's qualification in this role.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param identifier
             *     An identifier for this qualification for the practitioner
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
             * Period during which the qualification is valid.
             * </p>
             * 
             * @param period
             *     Period during which the qualification is valid
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * <p>
             * Organization that regulates and issues the qualification.
             * </p>
             * 
             * @param issuer
             *     Organization that regulates and issues the qualification
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder issuer(Reference issuer) {
                this.issuer = issuer;
                return this;
            }

            @Override
            public Qualification build() {
                return new Qualification(this);
            }

            private Builder from(Qualification qualification) {
                id = qualification.id;
                extension.addAll(qualification.extension);
                modifierExtension.addAll(qualification.modifierExtension);
                identifier.addAll(qualification.identifier);
                period = qualification.period;
                issuer = qualification.issuer;
                return this;
            }
        }
    }
}
