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
 * Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor 
 * has a formal responsibility in the care process.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class RelatedPerson extends DomainResource {
    private final List<Identifier> identifier;
    private final Boolean active;
    private final Reference patient;
    private final List<CodeableConcept> relationship;
    private final List<HumanName> name;
    private final List<ContactPoint> telecom;
    private final AdministrativeGender gender;
    private final Date birthDate;
    private final List<Address> address;
    private final List<Attachment> photo;
    private final Period period;
    private final List<Communication> communication;

    private volatile int hashCode;

    private RelatedPerson(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        active = builder.active;
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
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
     * <p>
     * Identifier for a person within a particular scope.
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
     * Whether this related person record is in active use.
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
     * The patient this person is related to.
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
     * The nature of the relationship between a patient and the related person.
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
     * A name associated with the person.
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
     * The date on which the related person was born.
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
     * Address where the related person can be contacted or visited.
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
     * The period of time during which this relationship is or was active. If there are no dates defined, then the interval 
     * is unknown.
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
     * A language which may be used to communicate with about the patient's health.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Communication}.
     */
    public List<Communication> getCommunication() {
        return communication;
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
        return new Builder(patient).from(this);
    }

    public Builder toBuilder(Reference patient) {
        return new Builder(patient).from(this);
    }

    public static Builder builder(Reference patient) {
        return new Builder(patient);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Reference patient;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Boolean active;
        private List<CodeableConcept> relationship = new ArrayList<>();
        private List<HumanName> name = new ArrayList<>();
        private List<ContactPoint> telecom = new ArrayList<>();
        private AdministrativeGender gender;
        private Date birthDate;
        private List<Address> address = new ArrayList<>();
        private List<Attachment> photo = new ArrayList<>();
        private Period period;
        private List<Communication> communication = new ArrayList<>();

        private Builder(Reference patient) {
            super();
            this.patient = patient;
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
         * Identifier for a person within a particular scope.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Identifier for a person within a particular scope.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     A human identifier for this person
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
         * Whether this related person record is in active use.
         * </p>
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
         * <p>
         * The nature of the relationship between a patient and the related person.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The nature of the relationship between a patient and the related person.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param relationship
         *     The nature of the relationship
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
         * A name associated with the person.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A name associated with the person.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param name
         *     A name associated with the person
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
         * The date on which the related person was born.
         * </p>
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
         * <p>
         * Address where the related person can be contacted or visited.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Address where the related person can be contacted or visited.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param address
         *     Address where the related person can be contacted or visited
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
         * Image of the person.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
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
         * The period of time during which this relationship is or was active. If there are no dates defined, then the interval 
         * is unknown.
         * </p>
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
         * <p>
         * A language which may be used to communicate with about the patient's health.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A language which may be used to communicate with about the patient's health.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param communication
         *     A language which may be used to communicate with about the patient's health
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder communication(Collection<Communication> communication) {
            this.communication = new ArrayList<>(communication);
            return this;
        }

        @Override
        public RelatedPerson build() {
            return new RelatedPerson(this);
        }

        private Builder from(RelatedPerson relatedPerson) {
            id = relatedPerson.id;
            meta = relatedPerson.meta;
            implicitRules = relatedPerson.implicitRules;
            language = relatedPerson.language;
            text = relatedPerson.text;
            contained.addAll(relatedPerson.contained);
            extension.addAll(relatedPerson.extension);
            modifierExtension.addAll(relatedPerson.modifierExtension);
            identifier.addAll(relatedPerson.identifier);
            active = relatedPerson.active;
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
     * <p>
     * A language which may be used to communicate with about the patient's health.
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
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
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
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (language != null) || 
                (preferred != null);
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
}
