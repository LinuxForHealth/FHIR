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
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.IdentityAssuranceLevel;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Demographics and administrative information about a person independent of a specific health-related context.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Person extends DomainResource {
    private final List<Identifier> identifier;
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
    private final List<Address> address;
    private final Attachment photo;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference managingOrganization;
    @Summary
    private final Boolean active;
    private final List<Link> link;

    private Person(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        name = Collections.unmodifiableList(builder.name);
        telecom = Collections.unmodifiableList(builder.telecom);
        gender = builder.gender;
        birthDate = builder.birthDate;
        address = Collections.unmodifiableList(builder.address);
        photo = builder.photo;
        managingOrganization = builder.managingOrganization;
        active = builder.active;
        link = Collections.unmodifiableList(builder.link);
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
     * Administrative Gender.
     * 
     * @return
     *     An immutable object of type {@link AdministrativeGender} that may be null.
     */
    public AdministrativeGender getGender() {
        return gender;
    }

    /**
     * The birth date for the person.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * One or more addresses for the person.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Address} that may be empty.
     */
    public List<Address> getAddress() {
        return address;
    }

    /**
     * An image that can be displayed as a thumbnail of the person to enhance the identification of the individual.
     * 
     * @return
     *     An immutable object of type {@link Attachment} that may be null.
     */
    public Attachment getPhoto() {
        return photo;
    }

    /**
     * The organization that is the custodian of the person record.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getManagingOrganization() {
        return managingOrganization;
    }

    /**
     * Whether this person's record is in active use.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Link to a resource that concerns the same actual person.
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
            !name.isEmpty() || 
            !telecom.isEmpty() || 
            (gender != null) || 
            (birthDate != null) || 
            !address.isEmpty() || 
            (photo != null) || 
            (managingOrganization != null) || 
            (active != null) || 
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
                accept(name, "name", visitor, HumanName.class);
                accept(telecom, "telecom", visitor, ContactPoint.class);
                accept(gender, "gender", visitor);
                accept(birthDate, "birthDate", visitor);
                accept(address, "address", visitor, Address.class);
                accept(photo, "photo", visitor);
                accept(managingOrganization, "managingOrganization", visitor);
                accept(active, "active", visitor);
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
        Person other = (Person) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(name, other.name) && 
            Objects.equals(telecom, other.telecom) && 
            Objects.equals(gender, other.gender) && 
            Objects.equals(birthDate, other.birthDate) && 
            Objects.equals(address, other.address) && 
            Objects.equals(photo, other.photo) && 
            Objects.equals(managingOrganization, other.managingOrganization) && 
            Objects.equals(active, other.active) && 
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
                name, 
                telecom, 
                gender, 
                birthDate, 
                address, 
                photo, 
                managingOrganization, 
                active, 
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
        private List<HumanName> name = new ArrayList<>();
        private List<ContactPoint> telecom = new ArrayList<>();
        private AdministrativeGender gender;
        private Date birthDate;
        private List<Address> address = new ArrayList<>();
        private Attachment photo;
        private Reference managingOrganization;
        private Boolean active;
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
         * Administrative Gender.
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
         *     The date on which the person was born
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
         * The birth date for the person.
         * 
         * @param birthDate
         *     The date on which the person was born
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder birthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        /**
         * One or more addresses for the person.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param address
         *     One or more addresses for the person
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
         * One or more addresses for the person.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param address
         *     One or more addresses for the person
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
         * An image that can be displayed as a thumbnail of the person to enhance the identification of the individual.
         * 
         * @param photo
         *     Image of the person
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder photo(Attachment photo) {
            this.photo = photo;
            return this;
        }

        /**
         * The organization that is the custodian of the person record.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param managingOrganization
         *     The organization that is the custodian of the person record
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder managingOrganization(Reference managingOrganization) {
            this.managingOrganization = managingOrganization;
            return this;
        }

        /**
         * Convenience method for setting {@code active}.
         * 
         * @param active
         *     This person's record is in active use
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
         * Whether this person's record is in active use.
         * 
         * @param active
         *     This person's record is in active use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        /**
         * Link to a resource that concerns the same actual person.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param link
         *     Link to a resource that concerns the same actual person
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
         * Link to a resource that concerns the same actual person.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param link
         *     Link to a resource that concerns the same actual person
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
         * Build the {@link Person}
         * 
         * @return
         *     An immutable object of type {@link Person}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Person per the base specification
         */
        @Override
        public Person build() {
            Person person = new Person(this);
            if (validating) {
                validate(person);
            }
            return person;
        }

        protected void validate(Person person) {
            super.validate(person);
            ValidationSupport.checkList(person.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(person.name, "name", HumanName.class);
            ValidationSupport.checkList(person.telecom, "telecom", ContactPoint.class);
            ValidationSupport.checkList(person.address, "address", Address.class);
            ValidationSupport.checkList(person.link, "link", Link.class);
            ValidationSupport.checkReferenceType(person.managingOrganization, "managingOrganization", "Organization");
        }

        protected Builder from(Person person) {
            super.from(person);
            identifier.addAll(person.identifier);
            name.addAll(person.name);
            telecom.addAll(person.telecom);
            gender = person.gender;
            birthDate = person.birthDate;
            address.addAll(person.address);
            photo = person.photo;
            managingOrganization = person.managingOrganization;
            active = person.active;
            link.addAll(person.link);
            return this;
        }
    }

    /**
     * Link to a resource that concerns the same actual person.
     */
    public static class Link extends BackboneElement {
        @ReferenceTarget({ "Patient", "Practitioner", "RelatedPerson", "Person" })
        @Required
        private final Reference target;
        @Binding(
            bindingName = "IdentityAssuranceLevel",
            strength = BindingStrength.Value.REQUIRED,
            description = "The level of confidence that this link represents the same actual person, based on NIST Authentication Levels.",
            valueSet = "http://hl7.org/fhir/ValueSet/identity-assuranceLevel|4.3.0-CIBUILD"
        )
        private final IdentityAssuranceLevel assurance;

        private Link(Builder builder) {
            super(builder);
            target = builder.target;
            assurance = builder.assurance;
        }

        /**
         * The resource to which this actual person is associated.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getTarget() {
            return target;
        }

        /**
         * Level of assurance that this link is associated with the target resource.
         * 
         * @return
         *     An immutable object of type {@link IdentityAssuranceLevel} that may be null.
         */
        public IdentityAssuranceLevel getAssurance() {
            return assurance;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (target != null) || 
                (assurance != null);
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
                    accept(target, "target", visitor);
                    accept(assurance, "assurance", visitor);
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
                Objects.equals(target, other.target) && 
                Objects.equals(assurance, other.assurance);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    target, 
                    assurance);
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
            private Reference target;
            private IdentityAssuranceLevel assurance;

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
             * The resource to which this actual person is associated.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Patient}</li>
             * <li>{@link Practitioner}</li>
             * <li>{@link RelatedPerson}</li>
             * <li>{@link Person}</li>
             * </ul>
             * 
             * @param target
             *     The resource to which this actual person is associated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder target(Reference target) {
                this.target = target;
                return this;
            }

            /**
             * Level of assurance that this link is associated with the target resource.
             * 
             * @param assurance
             *     level1 | level2 | level3 | level4
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder assurance(IdentityAssuranceLevel assurance) {
                this.assurance = assurance;
                return this;
            }

            /**
             * Build the {@link Link}
             * 
             * <p>Required elements:
             * <ul>
             * <li>target</li>
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
                ValidationSupport.requireNonNull(link.target, "target");
                ValidationSupport.checkReferenceType(link.target, "target", "Patient", "Practitioner", "RelatedPerson", "Person");
                ValidationSupport.requireValueOrChildren(link);
            }

            protected Builder from(Link link) {
                super.from(link);
                target = link.target;
                assurance = link.assurance;
                return this;
            }
        }
    }
}
