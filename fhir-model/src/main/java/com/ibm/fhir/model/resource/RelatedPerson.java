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
 * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor 
 * has a formal responsibility in the care process.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "relatedPerson-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/relatedperson-relationshiptype",
    expression = "relationship.exists() implies (relationship.all(memberOf('http://hl7.org/fhir/ValueSet/relatedperson-relationshiptype', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/RelatedPerson",
    generated = true
)
@Constraint(
    id = "relatedPerson-1",
    level = "Warning",
    location = "communication.language",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/languages",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/RelatedPerson",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class RelatedPerson extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final Boolean active;
    @Summary
    @ReferenceTarget({ "Patient" })
    @Required
    private final Reference patient;
    @Summary
    @Binding(
        bindingName = "PatientRelationshipType",
        strength = BindingStrength.Value.PREFERRED,
        description = "The nature of the relationship between a patient and the related person.",
        valueSet = "http://hl7.org/fhir/ValueSet/relatedperson-relationshiptype"
    )
    private final List<CodeableConcept> relationship;
    @Summary
    private final List<HumanName> name;
    @Summary
    private final List<ContactPoint> telecom;
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
    @Summary
    private final List<Address> address;
    private final List<Attachment> photo;
    private final Period period;
    private final List<Communication> communication;

    private RelatedPerson(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        active = builder.active;
        patient = builder.patient;
        relationship = Collections.unmodifiableList(builder.relationship);
        name = Collections.unmodifiableList(builder.name);
        telecom = Collections.unmodifiableList(builder.telecom);
        gender = builder.gender;
        birthDate = builder.birthDate;
        address = Collections.unmodifiableList(builder.address);
        photo = Collections.unmodifiableList(builder.photo);
        period = builder.period;
        communication = Collections.unmodifiableList(builder.communication);
    }

    /**
     * Identifier for a person within a particular scope.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Whether this related person record is in active use.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * The patient this person is related to.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * The nature of the relationship between a patient and the related person.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getRelationship() {
        return relationship;
    }

    /**
     * A name associated with the person.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link HumanName} that may be empty.
     */
    public List<HumanName> getName() {
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
     * The date on which the related person was born.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Address where the related person can be contacted or visited.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Address} that may be empty.
     */
    public List<Address> getAddress() {
        return address;
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
     * The period of time during which this relationship is or was active. If there are no dates defined, then the interval 
     * is unknown.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * A language which may be used to communicate with about the patient's health.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Communication} that may be empty.
     */
    public List<Communication> getCommunication() {
        return communication;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (active != null) || 
            (patient != null) || 
            !relationship.isEmpty() || 
            !name.isEmpty() || 
            !telecom.isEmpty() || 
            (gender != null) || 
            (birthDate != null) || 
            !address.isEmpty() || 
            !photo.isEmpty() || 
            (period != null) || 
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
                accept(patient, "patient", visitor);
                accept(relationship, "relationship", visitor, CodeableConcept.class);
                accept(name, "name", visitor, HumanName.class);
                accept(telecom, "telecom", visitor, ContactPoint.class);
                accept(gender, "gender", visitor);
                accept(birthDate, "birthDate", visitor);
                accept(address, "address", visitor, Address.class);
                accept(photo, "photo", visitor, Attachment.class);
                accept(period, "period", visitor);
                accept(communication, "communication", visitor, Communication.class);
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
        RelatedPerson other = (RelatedPerson) obj;
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
            Objects.equals(patient, other.patient) && 
            Objects.equals(relationship, other.relationship) && 
            Objects.equals(name, other.name) && 
            Objects.equals(telecom, other.telecom) && 
            Objects.equals(gender, other.gender) && 
            Objects.equals(birthDate, other.birthDate) && 
            Objects.equals(address, other.address) && 
            Objects.equals(photo, other.photo) && 
            Objects.equals(period, other.period) && 
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
                patient, 
                relationship, 
                name, 
                telecom, 
                gender, 
                birthDate, 
                address, 
                photo, 
                period, 
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
        private Reference patient;
        private List<CodeableConcept> relationship = new ArrayList<>();
        private List<HumanName> name = new ArrayList<>();
        private List<ContactPoint> telecom = new ArrayList<>();
        private AdministrativeGender gender;
        private Date birthDate;
        private List<Address> address = new ArrayList<>();
        private List<Attachment> photo = new ArrayList<>();
        private Period period;
        private List<Communication> communication = new ArrayList<>();

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
         * Identifier for a person within a particular scope.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     A human identifier for this person
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
         * Identifier for a person within a particular scope.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     A human identifier for this person
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
         *     Whether this related person's record is in active use
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
         * Whether this related person record is in active use.
         * 
         * @param active
         *     Whether this related person's record is in active use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        /**
         * The patient this person is related to.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     The patient this person is related to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * The nature of the relationship between a patient and the related person.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relationship
         *     The nature of the relationship
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
         * The nature of the relationship between a patient and the related person.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relationship
         *     The nature of the relationship
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
         * A name associated with the person.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param name
         *     A name associated with the person
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
         * A name associated with the person.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param name
         *     A name associated with the person
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
         *     The date on which the related person was born
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
         * The date on which the related person was born.
         * 
         * @param birthDate
         *     The date on which the related person was born
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        /**
         * Address where the related person can be contacted or visited.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param address
         *     Address where the related person can be contacted or visited
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
         * Address where the related person can be contacted or visited.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param address
         *     Address where the related person can be contacted or visited
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
         * The period of time during which this relationship is or was active. If there are no dates defined, then the interval 
         * is unknown.
         * 
         * @param period
         *     Period of time that this relationship is considered valid
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * A language which may be used to communicate with about the patient's health.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param communication
         *     A language which may be used to communicate with about the patient's health
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
         * A language which may be used to communicate with about the patient's health.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param communication
         *     A language which may be used to communicate with about the patient's health
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
         * Build the {@link RelatedPerson}
         * 
         * <p>Required elements:
         * <ul>
         * <li>patient</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link RelatedPerson}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid RelatedPerson per the base specification
         */
        @Override
        public RelatedPerson build() {
            RelatedPerson relatedPerson = new RelatedPerson(this);
            if (validating) {
                validate(relatedPerson);
            }
            return relatedPerson;
        }

        protected void validate(RelatedPerson relatedPerson) {
            super.validate(relatedPerson);
            ValidationSupport.checkList(relatedPerson.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(relatedPerson.patient, "patient");
            ValidationSupport.checkList(relatedPerson.relationship, "relationship", CodeableConcept.class);
            ValidationSupport.checkList(relatedPerson.name, "name", HumanName.class);
            ValidationSupport.checkList(relatedPerson.telecom, "telecom", ContactPoint.class);
            ValidationSupport.checkList(relatedPerson.address, "address", Address.class);
            ValidationSupport.checkList(relatedPerson.photo, "photo", Attachment.class);
            ValidationSupport.checkList(relatedPerson.communication, "communication", Communication.class);
            ValidationSupport.checkReferenceType(relatedPerson.patient, "patient", "Patient");
        }

        protected Builder from(RelatedPerson relatedPerson) {
            super.from(relatedPerson);
            identifier.addAll(relatedPerson.identifier);
            active = relatedPerson.active;
            patient = relatedPerson.patient;
            relationship.addAll(relatedPerson.relationship);
            name.addAll(relatedPerson.name);
            telecom.addAll(relatedPerson.telecom);
            gender = relatedPerson.gender;
            birthDate = relatedPerson.birthDate;
            address.addAll(relatedPerson.address);
            photo.addAll(relatedPerson.photo);
            period = relatedPerson.period;
            communication.addAll(relatedPerson.communication);
            return this;
        }
    }

    /**
     * A language which may be used to communicate with about the patient's health.
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
}
