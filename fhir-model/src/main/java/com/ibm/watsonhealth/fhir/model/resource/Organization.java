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
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form 
 * of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare 
 * practice groups, payer/insurer, etc.
 * </p>
 */
@Constraint(
    id = "org-1",
    level = "Rule",
    location = "(base)",
    description = "The organization SHALL at least have a name or an identifier, and possibly more than one",
    expression = "(identifier.count() + name.count()) > 0"
)
@Constraint(
    id = "org-2",
    level = "Rule",
    location = "Organization.address",
    description = "An address of an organization can never be of use 'home'",
    expression = "where(use = 'home').empty()"
)
@Constraint(
    id = "org-3",
    level = "Rule",
    location = "Organization.telecom",
    description = "The telecom of an organization can never be of use 'home'",
    expression = "where(use = 'home').empty()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Organization extends DomainResource {
    private final List<Identifier> identifier;
    private final Boolean active;
    private final List<CodeableConcept> type;
    private final String name;
    private final List<String> alias;
    private final List<ContactPoint> telecom;
    private final List<Address> address;
    private final Reference partOf;
    private final List<Contact> contact;
    private final List<Reference> endpoint;

    private volatile int hashCode;

    private Organization(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        active = builder.active;
        type = Collections.unmodifiableList(builder.type);
        name = builder.name;
        alias = Collections.unmodifiableList(builder.alias);
        telecom = Collections.unmodifiableList(builder.telecom);
        address = Collections.unmodifiableList(builder.address);
        partOf = builder.partOf;
        contact = Collections.unmodifiableList(builder.contact);
        endpoint = Collections.unmodifiableList(builder.endpoint);
    }

    /**
     * <p>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems.
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
     * Whether the organization's record is still in active use.
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
     * The kind(s) of organization that this is.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getType() {
        return type;
    }

    /**
     * <p>
     * A name associated with the organization.
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
     * A list of alternate names that the organization is known as, or was known as in the past.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String}.
     */
    public List<String> getAlias() {
        return alias;
    }

    /**
     * <p>
     * A contact detail for the organization.
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
     * An address for the organization.
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
     * The organization of which this organization forms a part.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPartOf() {
        return partOf;
    }

    /**
     * <p>
     * Contact for the organization for a certain purpose.
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
     * Technical endpoints providing access to services operated for the organization.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getEndpoint() {
        return endpoint;
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
                accept(type, "type", visitor, CodeableConcept.class);
                accept(name, "name", visitor);
                accept(alias, "alias", visitor, String.class);
                accept(telecom, "telecom", visitor, ContactPoint.class);
                accept(address, "address", visitor, Address.class);
                accept(partOf, "partOf", visitor);
                accept(contact, "contact", visitor, Contact.class);
                accept(endpoint, "endpoint", visitor, Reference.class);
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
        Organization other = (Organization) obj;
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
            Objects.equals(type, other.type) && 
            Objects.equals(name, other.name) && 
            Objects.equals(alias, other.alias) && 
            Objects.equals(telecom, other.telecom) && 
            Objects.equals(address, other.address) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(endpoint, other.endpoint);
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
                type, 
                name, 
                alias, 
                telecom, 
                address, 
                partOf, 
                contact, 
                endpoint);
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
        private List<CodeableConcept> type = new ArrayList<>();
        private String name;
        private List<String> alias = new ArrayList<>();
        private List<ContactPoint> telecom = new ArrayList<>();
        private List<Address> address = new ArrayList<>();
        private Reference partOf;
        private List<Contact> contact = new ArrayList<>();
        private List<Reference> endpoint = new ArrayList<>();

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
         * Identifier for the organization that is used to identify the organization across multiple disparate systems.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param identifier
         *     Identifies this organization across multiple systems
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
         * Identifier for the organization that is used to identify the organization across multiple disparate systems.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Identifies this organization across multiple systems
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
         * Whether the organization's record is still in active use.
         * </p>
         * 
         * @param active
         *     Whether the organization's record is still in active use
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
         * The kind(s) of organization that this is.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param type
         *     Kind of organization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept... type) {
            for (CodeableConcept value : type) {
                this.type.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The kind(s) of organization that this is.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param type
         *     Kind of organization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(Collection<CodeableConcept> type) {
            this.type = new ArrayList<>(type);
            return this;
        }

        /**
         * <p>
         * A name associated with the organization.
         * </p>
         * 
         * @param name
         *     Name used for the organization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * <p>
         * A list of alternate names that the organization is known as, or was known as in the past.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param alias
         *     A list of alternate names that the organization is known as, or was known as in the past
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder alias(String... alias) {
            for (String value : alias) {
                this.alias.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A list of alternate names that the organization is known as, or was known as in the past.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param alias
         *     A list of alternate names that the organization is known as, or was known as in the past
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder alias(Collection<String> alias) {
            this.alias = new ArrayList<>(alias);
            return this;
        }

        /**
         * <p>
         * A contact detail for the organization.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param telecom
         *     A contact detail for the organization
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
         * A contact detail for the organization.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param telecom
         *     A contact detail for the organization
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
         * An address for the organization.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param address
         *     An address for the organization
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
         * An address for the organization.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param address
         *     An address for the organization
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
         * The organization of which this organization forms a part.
         * </p>
         * 
         * @param partOf
         *     The organization of which this organization forms a part
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Reference partOf) {
            this.partOf = partOf;
            return this;
        }

        /**
         * <p>
         * Contact for the organization for a certain purpose.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param contact
         *     Contact for the organization for a certain purpose
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
         * Contact for the organization for a certain purpose.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contact
         *     Contact for the organization for a certain purpose
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
         * Technical endpoints providing access to services operated for the organization.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param endpoint
         *     Technical endpoints providing access to services operated for the organization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder endpoint(Reference... endpoint) {
            for (Reference value : endpoint) {
                this.endpoint.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Technical endpoints providing access to services operated for the organization.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param endpoint
         *     Technical endpoints providing access to services operated for the organization
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder endpoint(Collection<Reference> endpoint) {
            this.endpoint = new ArrayList<>(endpoint);
            return this;
        }

        @Override
        public Organization build() {
            return new Organization(this);
        }

        private Builder from(Organization organization) {
            id = organization.id;
            meta = organization.meta;
            implicitRules = organization.implicitRules;
            language = organization.language;
            text = organization.text;
            contained.addAll(organization.contained);
            extension.addAll(organization.extension);
            modifierExtension.addAll(organization.modifierExtension);
            identifier.addAll(organization.identifier);
            active = organization.active;
            type.addAll(organization.type);
            name = organization.name;
            alias.addAll(organization.alias);
            telecom.addAll(organization.telecom);
            address.addAll(organization.address);
            partOf = organization.partOf;
            contact.addAll(organization.contact);
            endpoint.addAll(organization.endpoint);
            return this;
        }
    }

    /**
     * <p>
     * Contact for the organization for a certain purpose.
     * </p>
     */
    public static class Contact extends BackboneElement {
        private final CodeableConcept purpose;
        private final HumanName name;
        private final List<ContactPoint> telecom;
        private final Address address;

        private volatile int hashCode;

        private Contact(Builder builder) {
            super(builder);
            purpose = builder.purpose;
            name = builder.name;
            telecom = Collections.unmodifiableList(builder.telecom);
            address = builder.address;
            if (!hasChildren()) {
                throw new IllegalStateException("ele-1: All FHIR elements must have a @value or children");
            }
        }

        /**
         * <p>
         * Indicates a purpose for which the contact can be reached.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getPurpose() {
            return purpose;
        }

        /**
         * <p>
         * A name associated with the contact.
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
         * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
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
         * Visiting or postal addresses for the contact.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Address}.
         */
        public Address getAddress() {
            return address;
        }

        @Override
        protected boolean hasChildren() {
            return super.hasChildren() || 
                (purpose != null) || 
                (name != null) || 
                !telecom.isEmpty() || 
                (address != null);
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
                    accept(purpose, "purpose", visitor);
                    accept(name, "name", visitor);
                    accept(telecom, "telecom", visitor, ContactPoint.class);
                    accept(address, "address", visitor);
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
                Objects.equals(purpose, other.purpose) && 
                Objects.equals(name, other.name) && 
                Objects.equals(telecom, other.telecom) && 
                Objects.equals(address, other.address);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    purpose, 
                    name, 
                    telecom, 
                    address);
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
            private CodeableConcept purpose;
            private HumanName name;
            private List<ContactPoint> telecom = new ArrayList<>();
            private Address address;

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
             * Indicates a purpose for which the contact can be reached.
             * </p>
             * 
             * @param purpose
             *     The type of contact
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder purpose(CodeableConcept purpose) {
                this.purpose = purpose;
                return this;
            }

            /**
             * <p>
             * A name associated with the contact.
             * </p>
             * 
             * @param name
             *     A name associated with the contact
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
             * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
             * 
             * @param telecom
             *     Contact details (telephone, email, etc.) for a contact
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
             * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param telecom
             *     Contact details (telephone, email, etc.) for a contact
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
             * Visiting or postal addresses for the contact.
             * </p>
             * 
             * @param address
             *     Visiting or postal addresses for the contact
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder address(Address address) {
                this.address = address;
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
                purpose = contact.purpose;
                name = contact.name;
                telecom.addAll(contact.telecom);
                address = contact.address;
                return this;
            }
        }
    }
}
