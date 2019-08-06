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
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.IdentityAssuranceLevel;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Demographics and administrative information about a person independent of a specific health-related context.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Person extends DomainResource {
    private final List<Identifier> identifier;
    private final List<HumanName> name;
    private final List<ContactPoint> telecom;
    private final AdministrativeGender gender;
    private final Date birthDate;
    private final List<Address> address;
    private final Attachment photo;
    private final Reference managingOrganization;
    private final Boolean active;
    private final List<Link> link;

    private volatile int hashCode;

    private Person(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        name = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.name, "name"));
        telecom = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.telecom, "telecom"));
        gender = builder.gender;
        birthDate = builder.birthDate;
        address = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.address, "address"));
        photo = builder.photo;
        managingOrganization = builder.managingOrganization;
        active = builder.active;
        link = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.link, "link"));
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
     * Administrative Gender.
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
     * The birth date for the person.
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
     * One or more addresses for the person.
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
     * An image that can be displayed as a thumbnail of the person to enhance the identification of the individual.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Attachment}.
     */
    public Attachment getPhoto() {
        return photo;
    }

    /**
     * <p>
     * The organization that is the custodian of the person record.
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
     * Whether this person's record is in active use.
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
     * Link to a resource that concerns the same actual person.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Link}.
     */
    public List<Link> getLink() {
        return link;
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
         * Identifier for a person within a particular scope.
         * </p>
         * <p>
         * Adds new element(s) to existing list
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
         * A name associated with the person.
         * </p>
         * <p>
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * Administrative Gender.
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
         * The birth date for the person.
         * </p>
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
         * <p>
         * One or more addresses for the person.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * One or more addresses for the person.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param address
         *     One or more addresses for the person
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
         * An image that can be displayed as a thumbnail of the person to enhance the identification of the individual.
         * </p>
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
         * <p>
         * The organization that is the custodian of the person record.
         * </p>
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
         * <p>
         * Whether this person's record is in active use.
         * </p>
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
         * <p>
         * Link to a resource that concerns the same actual person.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Link to a resource that concerns the same actual person.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param link
         *     Link to a resource that concerns the same actual person
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder link(Collection<Link> link) {
            this.link = new ArrayList<>(link);
            return this;
        }

        @Override
        public Person build() {
            return new Person(this);
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
     * <p>
     * Link to a resource that concerns the same actual person.
     * </p>
     */
    public static class Link extends BackboneElement {
        private final Reference target;
        private final IdentityAssuranceLevel assurance;

        private volatile int hashCode;

        private Link(Builder builder) {
            super(builder);
            target = ValidationSupport.requireNonNull(builder.target, "target");
            assurance = builder.assurance;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The resource to which this actual person is associated.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getTarget() {
            return target;
        }

        /**
         * <p>
         * Level of assurance that this link is associated with the target resource.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link IdentityAssuranceLevel}.
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
             * The resource to which this actual person is associated.
             * </p>
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
             * <p>
             * Level of assurance that this link is associated with the target resource.
             * </p>
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

            @Override
            public Link build() {
                return new Link(this);
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
