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
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A person who is directly or indirectly involved in the provisioning of healthcare.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "practitioner-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/languages",
    expression = "communication.exists() implies (communication.all(memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/Practitioner",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Practitioner extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final Boolean active;
    @Summary
    private final List<HumanName> name;
    @Summary
    private final List<ContactPoint> telecom;
    @Summary
    private final List<Address> address;
    @Summary
    @Binding(
        bindingName = "AdministrativeGender",
        strength = BindingStrength.Value.REQUIRED,
        description = "The gender of a person used for administrative purposes.",
        valueSet = "http://hl7.org/fhir/ValueSet/administrative-gender|4.3.0-CIBUILD"
    )
    private final AdministrativeGender gender;
    @Summary
    private final Date birthDate;
    private final List<Attachment> photo;
    private final List<Qualification> qualification;
    @Binding(
        bindingName = "Language",
        strength = BindingStrength.Value.PREFERRED,
        description = "IETF language tag",
        valueSet = "http://hl7.org/fhir/ValueSet/languages",
        maxValueSet = "http://hl7.org/fhir/ValueSet/all-languages"
    )
    private final List<CodeableConcept> communication;

    private Practitioner(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        active = builder.active;
        name = Collections.unmodifiableList(builder.name);
        telecom = Collections.unmodifiableList(builder.telecom);
        address = Collections.unmodifiableList(builder.address);
        gender = builder.gender;
        birthDate = builder.birthDate;
        photo = Collections.unmodifiableList(builder.photo);
        qualification = Collections.unmodifiableList(builder.qualification);
        communication = Collections.unmodifiableList(builder.communication);
    }

    /**
     * An identifier that applies to this person in this role.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Whether this practitioner's record is in active use.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * The name(s) associated with the practitioner.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link HumanName} that may be empty.
     */
    public List<HumanName> getName() {
        return name;
    }

    /**
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactPoint} that may be empty.
     */
    public List<ContactPoint> getTelecom() {
        return telecom;
    }

    /**
     * Address(es) of the practitioner that are not role specific (typically home address). Work addresses are not typically 
     * entered in this property as they are usually role dependent.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Address} that may be empty.
     */
    public List<Address> getAddress() {
        return address;
    }

    /**
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping 
     * purposes.
     * 
     * @return
     *     An immutable object of type {@link AdministrativeGender} that may be null.
     */
    public AdministrativeGender getGender() {
        return gender;
    }

    /**
     * The date of birth for the practitioner.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Image of the person.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Attachment} that may be empty.
     */
    public List<Attachment> getPhoto() {
        return photo;
    }

    /**
     * The official certifications, training, and licenses that authorize or otherwise pertain to the provision of care by 
     * the practitioner. For example, a medical license issued by a medical board authorizing the practitioner to practice 
     * medicine within a certian locality.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Qualification} that may be empty.
     */
    public List<Qualification> getQualification() {
        return qualification;
    }

    /**
     * A language the practitioner can use in patient communication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCommunication() {
        return communication;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (active != null) || 
            !name.isEmpty() || 
            !telecom.isEmpty() || 
            !address.isEmpty() || 
            (gender != null) || 
            (birthDate != null) || 
            !photo.isEmpty() || 
            !qualification.isEmpty() || 
            !communication.isEmpty();
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
         * An identifier that applies to this person in this role.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * An identifier that applies to this person in this role.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     An identifier for the person as this agent
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
         *     Whether this practitioner's record is in active use
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
         * Whether this practitioner's record is in active use.
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
         * The name(s) associated with the practitioner.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The name(s) associated with the practitioner.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param name
         *     The name(s) associated with the practitioner
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
         * A contact detail for the practitioner, e.g. a telephone number or an email address.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A contact detail for the practitioner, e.g. a telephone number or an email address.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param telecom
         *     A contact detail for the practitioner (that apply to all roles)
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
         * Address(es) of the practitioner that are not role specific (typically home address). Work addresses are not typically 
         * entered in this property as they are usually role dependent.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Address(es) of the practitioner that are not role specific (typically home address). Work addresses are not typically 
         * entered in this property as they are usually role dependent.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param address
         *     Address(es) of the practitioner that are not role specific (typically home address)
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
         * Administrative Gender - the gender that the person is considered to have for administration and record keeping 
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
         *     The date on which the practitioner was born
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
         * The date of birth for the practitioner.
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
         * Image of the person.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Image of the person.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param photo
         *     Image of the person
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
         * The official certifications, training, and licenses that authorize or otherwise pertain to the provision of care by 
         * the practitioner. For example, a medical license issued by a medical board authorizing the practitioner to practice 
         * medicine within a certian locality.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The official certifications, training, and licenses that authorize or otherwise pertain to the provision of care by 
         * the practitioner. For example, a medical license issued by a medical board authorizing the practitioner to practice 
         * medicine within a certian locality.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param qualification
         *     Certification, licenses, or training pertaining to the provision of care
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder qualification(Collection<Qualification> qualification) {
            this.qualification = new ArrayList<>(qualification);
            return this;
        }

        /**
         * A language the practitioner can use in patient communication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A language the practitioner can use in patient communication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param communication
         *     A language the practitioner can use in patient communication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder communication(Collection<CodeableConcept> communication) {
            this.communication = new ArrayList<>(communication);
            return this;
        }

        /**
         * Build the {@link Practitioner}
         * 
         * @return
         *     An immutable object of type {@link Practitioner}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Practitioner per the base specification
         */
        @Override
        public Practitioner build() {
            Practitioner practitioner = new Practitioner(this);
            if (validating) {
                validate(practitioner);
            }
            return practitioner;
        }

        protected void validate(Practitioner practitioner) {
            super.validate(practitioner);
            ValidationSupport.checkList(practitioner.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(practitioner.name, "name", HumanName.class);
            ValidationSupport.checkList(practitioner.telecom, "telecom", ContactPoint.class);
            ValidationSupport.checkList(practitioner.address, "address", Address.class);
            ValidationSupport.checkList(practitioner.photo, "photo", Attachment.class);
            ValidationSupport.checkList(practitioner.qualification, "qualification", Qualification.class);
            ValidationSupport.checkList(practitioner.communication, "communication", CodeableConcept.class);
            ValidationSupport.checkValueSetBinding(practitioner.communication, "communication", "http://hl7.org/fhir/ValueSet/all-languages", "urn:ietf:bcp:47");
        }

        protected Builder from(Practitioner practitioner) {
            super.from(practitioner);
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
     * The official certifications, training, and licenses that authorize or otherwise pertain to the provision of care by 
     * the practitioner. For example, a medical license issued by a medical board authorizing the practitioner to practice 
     * medicine within a certian locality.
     */
    public static class Qualification extends BackboneElement {
        private final List<Identifier> identifier;
        @Binding(
            bindingName = "Qualification",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Specific qualification the practitioner has to provide a service.",
            valueSet = "http://terminology.hl7.org/ValueSet/v2-2.7-0360"
        )
        @Required
        private final CodeableConcept code;
        private final Period period;
        @ReferenceTarget({ "Organization" })
        private final Reference issuer;

        private Qualification(Builder builder) {
            super(builder);
            identifier = Collections.unmodifiableList(builder.identifier);
            code = builder.code;
            period = builder.period;
            issuer = builder.issuer;
        }

        /**
         * An identifier that applies to this person's qualification in this role.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
         */
        public List<Identifier> getIdentifier() {
            return identifier;
        }

        /**
         * Coded representation of the qualification.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * Period during which the qualification is valid.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * Organization that regulates and issues the qualification.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private List<Identifier> identifier = new ArrayList<>();
            private CodeableConcept code;
            private Period period;
            private Reference issuer;

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
             * An identifier that applies to this person's qualification in this role.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * An identifier that applies to this person's qualification in this role.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param identifier
             *     An identifier for this qualification for the practitioner
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
             * Coded representation of the qualification.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     Coded representation of the qualification
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Period during which the qualification is valid.
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
             * Organization that regulates and issues the qualification.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
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

            /**
             * Build the {@link Qualification}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Qualification}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Qualification per the base specification
             */
            @Override
            public Qualification build() {
                Qualification qualification = new Qualification(this);
                if (validating) {
                    validate(qualification);
                }
                return qualification;
            }

            protected void validate(Qualification qualification) {
                super.validate(qualification);
                ValidationSupport.checkList(qualification.identifier, "identifier", Identifier.class);
                ValidationSupport.requireNonNull(qualification.code, "code");
                ValidationSupport.checkReferenceType(qualification.issuer, "issuer", "Organization");
                ValidationSupport.requireValueOrChildren(qualification);
            }

            protected Builder from(Qualification qualification) {
                super.from(qualification);
                identifier.addAll(qualification.identifier);
                code = qualification.code;
                period = qualification.period;
                issuer = qualification.issuer;
                return this;
            }
        }
    }
}
