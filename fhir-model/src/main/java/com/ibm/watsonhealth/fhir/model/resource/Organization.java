/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    key = "org-1",
    severity = "error",
    human = "The organization SHALL at least have a name or an identifier, and possibly more than one",
    expression = "(identifier.count() + name.count()) > 0"
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

    private Organization(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.active = builder.active;
        this.type = builder.type;
        this.name = builder.name;
        this.alias = builder.alias;
        this.telecom = builder.telecom;
        this.address = builder.address;
        this.partOf = builder.partOf;
        this.contact = builder.contact;
        this.endpoint = builder.endpoint;
    }

    /**
     * <p>
     * Identifier for the organization that is used to identify the organization across multiple disparate systems.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Identifier}.
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
     *     A list containing immutable objects of type {@link CodeableConcept}.
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
     *     A list containing immutable objects of type {@link String}.
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
     *     A list containing immutable objects of type {@link ContactPoint}.
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
     *     A list containing immutable objects of type {@link Address}.
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
     *     A list containing immutable objects of type {@link Contact}.
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
     *     A list containing immutable objects of type {@link Reference}.
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
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.active = active;
        builder.type.addAll(type);
        builder.name = name;
        builder.alias.addAll(alias);
        builder.telecom.addAll(telecom);
        builder.address.addAll(address);
        builder.partOf = partOf;
        builder.contact.addAll(contact);
        builder.endpoint.addAll(endpoint);
        return builder;
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         *     A reference to this Builder instance.
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
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * Identifier for the organization that is used to identify the organization across multiple disparate systems.
         * </p>
         * 
         * @param identifier
         *     Identifies this organization across multiple systems
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param identifier
         *     Identifies this organization across multiple systems
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier.addAll(identifier);
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
         *     A reference to this Builder instance.
         */
        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        /**
         * <p>
         * The kind(s) of organization that this is.
         * </p>
         * 
         * @param type
         *     Kind of organization
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param type
         *     Kind of organization
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder type(Collection<CodeableConcept> type) {
            this.type.addAll(type);
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
         *     A reference to this Builder instance.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * <p>
         * A list of alternate names that the organization is known as, or was known as in the past.
         * </p>
         * 
         * @param alias
         *     A list of alternate names that the organization is known as, or was known as in the past
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param alias
         *     A list of alternate names that the organization is known as, or was known as in the past
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder alias(Collection<String> alias) {
            this.alias.addAll(alias);
            return this;
        }

        /**
         * <p>
         * A contact detail for the organization.
         * </p>
         * 
         * @param telecom
         *     A contact detail for the organization
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param telecom
         *     A contact detail for the organization
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder telecom(Collection<ContactPoint> telecom) {
            this.telecom.addAll(telecom);
            return this;
        }

        /**
         * <p>
         * An address for the organization.
         * </p>
         * 
         * @param address
         *     An address for the organization
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param address
         *     An address for the organization
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder address(Collection<Address> address) {
            this.address.addAll(address);
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
         *     A reference to this Builder instance.
         */
        public Builder partOf(Reference partOf) {
            this.partOf = partOf;
            return this;
        }

        /**
         * <p>
         * Contact for the organization for a certain purpose.
         * </p>
         * 
         * @param contact
         *     Contact for the organization for a certain purpose
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param contact
         *     Contact for the organization for a certain purpose
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(Collection<Contact> contact) {
            this.contact.addAll(contact);
            return this;
        }

        /**
         * <p>
         * Technical endpoints providing access to services operated for the organization.
         * </p>
         * 
         * @param endpoint
         *     Technical endpoints providing access to services operated for the organization
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param endpoint
         *     Technical endpoints providing access to services operated for the organization
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder endpoint(Collection<Reference> endpoint) {
            this.endpoint.addAll(endpoint);
            return this;
        }

        @Override
        public Organization build() {
            return new Organization(this);
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

        private Contact(Builder builder) {
            super(builder);
            this.purpose = builder.purpose;
            this.name = builder.name;
            this.telecom = builder.telecom;
            this.address = builder.address;
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
         *     A list containing immutable objects of type {@link ContactPoint}.
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
        public Builder toBuilder() {
            return Builder.from(this);
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
             *     A reference to this Builder instance.
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
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
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
             *     A reference to this Builder instance.
             */
            public Builder name(HumanName name) {
                this.name = name;
                return this;
            }

            /**
             * <p>
             * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
             * </p>
             * 
             * @param telecom
             *     Contact details (telephone, email, etc.) for a contact
             * 
             * @return
             *     A reference to this Builder instance.
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
             * 
             * @param telecom
             *     Contact details (telephone, email, etc.) for a contact
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder telecom(Collection<ContactPoint> telecom) {
                this.telecom.addAll(telecom);
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
             *     A reference to this Builder instance.
             */
            public Builder address(Address address) {
                this.address = address;
                return this;
            }

            @Override
            public Contact build() {
                return new Contact(this);
            }

            private static Builder from(Contact contact) {
                Builder builder = new Builder();
                builder.id = contact.id;
                builder.extension.addAll(contact.extension);
                builder.modifierExtension.addAll(contact.modifierExtension);
                builder.purpose = contact.purpose;
                builder.name = contact.name;
                builder.telecom.addAll(contact.telecom);
                builder.address = contact.address;
                return builder;
            }
        }
    }
}
