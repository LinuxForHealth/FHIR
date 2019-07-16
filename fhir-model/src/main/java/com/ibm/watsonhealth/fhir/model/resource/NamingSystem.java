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
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.NamingSystemIdentifierType;
import com.ibm.watsonhealth.fhir.model.type.NamingSystemType;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, 
 * devices, etc. Represents a "System" used within the Identifier and Coding data types.
 * </p>
 */
@Constraint(
    id = "nsd-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Constraint(
    id = "nsd-1",
    level = "Rule",
    location = "(base)",
    description = "Root systems cannot have uuid identifiers",
    expression = "kind != 'root' or uniqueId.all(type != 'uuid')"
)
@Constraint(
    id = "nsd-2",
    level = "Rule",
    location = "(base)",
    description = "Can't have more than one preferred identifier for a type",
    expression = "uniqueId.where(preferred = true).select(type).isDistinct()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class NamingSystem extends DomainResource {
    private final String name;
    private final PublicationStatus status;
    private final NamingSystemType kind;
    private final DateTime date;
    private final String publisher;
    private final List<ContactDetail> contact;
    private final String responsible;
    private final CodeableConcept type;
    private final Markdown description;
    private final List<UsageContext> useContext;
    private final List<CodeableConcept> jurisdiction;
    private final String usage;
    private final List<UniqueId> uniqueId;

    private volatile int hashCode;

    private NamingSystem(Builder builder) {
        super(builder);
        name = ValidationSupport.requireNonNull(builder.name, "name");
        status = ValidationSupport.requireNonNull(builder.status, "status");
        kind = ValidationSupport.requireNonNull(builder.kind, "kind");
        date = ValidationSupport.requireNonNull(builder.date, "date");
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        responsible = builder.responsible;
        type = builder.type;
        description = builder.description;
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        usage = builder.usage;
        uniqueId = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.uniqueId, "uniqueId"));
    }

    /**
     * <p>
     * A natural language name identifying the naming system. This name should be usable as an identifier for the module by 
     * machine processing applications such as code generation.
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
     * The status of this naming system. Enables tracking the life-cycle of the content.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus}.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Indicates the purpose for the naming system - what kinds of things does it make unique?
     * </p>
     * 
     * @return
     *     An immutable object of type {@link NamingSystemType}.
     */
    public NamingSystemType getKind() {
        return kind;
    }

    /**
     * <p>
     * The date (and optionally time) when the naming system was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the naming system changes.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * <p>
     * The name of the organization or individual that published the naming system.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * <p>
     * Contact details to assist a user in finding and communicating with the publisher.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * <p>
     * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring 
     * their non-collision.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getResponsible() {
        return responsible;
    }

    /**
     * <p>
     * Categorizes a naming system for easier search by grouping related naming systems.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * <p>
     * A free text natural language description of the naming system from a consumer's perspective. Details about what the 
     * namespace identifies including scope, granularity, version labeling, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * <p>
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate naming system instances.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext}.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * <p>
     * A legal or geographic region in which the naming system is intended to be used.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * <p>
     * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower 
     * case, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * <p>
     * Indicates how the system may be identified when referenced in electronic exchange.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UniqueId}.
     */
    public List<UniqueId> getUniqueId() {
        return uniqueId;
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
                accept(name, "name", visitor);
                accept(status, "status", visitor);
                accept(kind, "kind", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(responsible, "responsible", visitor);
                accept(type, "type", visitor);
                accept(description, "description", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(usage, "usage", visitor);
                accept(uniqueId, "uniqueId", visitor, UniqueId.class);
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
        NamingSystem other = (NamingSystem) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(name, other.name) && 
            Objects.equals(status, other.status) && 
            Objects.equals(kind, other.kind) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(responsible, other.responsible) && 
            Objects.equals(type, other.type) && 
            Objects.equals(description, other.description) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(usage, other.usage) && 
            Objects.equals(uniqueId, other.uniqueId);
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
                name, 
                status, 
                kind, 
                date, 
                publisher, 
                contact, 
                responsible, 
                type, 
                description, 
                useContext, 
                jurisdiction, 
                usage, 
                uniqueId);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(name, status, kind, date, uniqueId).from(this);
    }

    public Builder toBuilder(String name, PublicationStatus status, NamingSystemType kind, DateTime date, Collection<UniqueId> uniqueId) {
        return new Builder(name, status, kind, date, uniqueId).from(this);
    }

    public static Builder builder(String name, PublicationStatus status, NamingSystemType kind, DateTime date, Collection<UniqueId> uniqueId) {
        return new Builder(name, status, kind, date, uniqueId);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final String name;
        private final PublicationStatus status;
        private final NamingSystemType kind;
        private final DateTime date;
        private final List<UniqueId> uniqueId;

        // optional
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private String responsible;
        private CodeableConcept type;
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private String usage;

        private Builder(String name, PublicationStatus status, NamingSystemType kind, DateTime date, Collection<UniqueId> uniqueId) {
            super();
            this.name = name;
            this.status = status;
            this.kind = kind;
            this.date = date;
            this.uniqueId = new ArrayList<>(uniqueId);
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
         * The name of the organization or individual that published the naming system.
         * </p>
         * 
         * @param publisher
         *     Name of the publisher (organization or individual)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        /**
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(ContactDetail... contact) {
            for (ContactDetail value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * <p>
         * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring 
         * their non-collision.
         * </p>
         * 
         * @param responsible
         *     Who maintains system namespace?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder responsible(String responsible) {
            this.responsible = responsible;
            return this;
        }

        /**
         * <p>
         * Categorizes a naming system for easier search by grouping related naming systems.
         * </p>
         * 
         * @param type
         *     e.g. driver, provider, patient, bank etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * <p>
         * A free text natural language description of the naming system from a consumer's perspective. Details about what the 
         * namespace identifies including scope, granularity, version labeling, etc.
         * </p>
         * 
         * @param description
         *     Natural language description of the naming system
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate naming system instances.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder useContext(UsageContext... useContext) {
            for (UsageContext value : useContext) {
                this.useContext.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate naming system instances.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder useContext(Collection<UsageContext> useContext) {
            this.useContext = new ArrayList<>(useContext);
            return this;
        }

        /**
         * <p>
         * A legal or geographic region in which the naming system is intended to be used.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for naming system (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder jurisdiction(CodeableConcept... jurisdiction) {
            for (CodeableConcept value : jurisdiction) {
                this.jurisdiction.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A legal or geographic region in which the naming system is intended to be used.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for naming system (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction = new ArrayList<>(jurisdiction);
            return this;
        }

        /**
         * <p>
         * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower 
         * case, etc.
         * </p>
         * 
         * @param usage
         *     How/where is it used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder usage(String usage) {
            this.usage = usage;
            return this;
        }

        @Override
        public NamingSystem build() {
            return new NamingSystem(this);
        }

        private Builder from(NamingSystem namingSystem) {
            id = namingSystem.id;
            meta = namingSystem.meta;
            implicitRules = namingSystem.implicitRules;
            language = namingSystem.language;
            text = namingSystem.text;
            contained.addAll(namingSystem.contained);
            extension.addAll(namingSystem.extension);
            modifierExtension.addAll(namingSystem.modifierExtension);
            publisher = namingSystem.publisher;
            contact.addAll(namingSystem.contact);
            responsible = namingSystem.responsible;
            type = namingSystem.type;
            description = namingSystem.description;
            useContext.addAll(namingSystem.useContext);
            jurisdiction.addAll(namingSystem.jurisdiction);
            usage = namingSystem.usage;
            return this;
        }
    }

    /**
     * <p>
     * Indicates how the system may be identified when referenced in electronic exchange.
     * </p>
     */
    public static class UniqueId extends BackboneElement {
        private final NamingSystemIdentifierType type;
        private final String value;
        private final Boolean preferred;
        private final String comment;
        private final Period period;

        private volatile int hashCode;

        private UniqueId(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            value = ValidationSupport.requireNonNull(builder.value, "value");
            preferred = builder.preferred;
            comment = builder.comment;
            period = builder.period;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Identifies the unique identifier scheme used for this particular identifier.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link NamingSystemIdentifierType}.
         */
        public NamingSystemIdentifierType getType() {
            return type;
        }

        /**
         * <p>
         * The string that should be sent over the wire to identify the code system or identifier system.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getValue() {
            return value;
        }

        /**
         * <p>
         * Indicates whether this identifier is the "preferred" identifier of this type.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getPreferred() {
            return preferred;
        }

        /**
         * <p>
         * Notes about the past or intended usage of this identifier.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getComment() {
            return comment;
        }

        /**
         * <p>
         * Identifies the period of time over which this identifier is considered appropriate to refer to the naming system. 
         * Outside of this window, the identifier might be non-deterministic.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getPeriod() {
            return period;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (value != null) || 
                (preferred != null) || 
                (comment != null) || 
                (period != null);
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
                    accept(type, "type", visitor);
                    accept(value, "value", visitor);
                    accept(preferred, "preferred", visitor);
                    accept(comment, "comment", visitor);
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
            UniqueId other = (UniqueId) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(value, other.value) && 
                Objects.equals(preferred, other.preferred) && 
                Objects.equals(comment, other.comment) && 
                Objects.equals(period, other.period);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    value, 
                    preferred, 
                    comment, 
                    period);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(type, value).from(this);
        }

        public Builder toBuilder(NamingSystemIdentifierType type, String value) {
            return new Builder(type, value).from(this);
        }

        public static Builder builder(NamingSystemIdentifierType type, String value) {
            return new Builder(type, value);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final NamingSystemIdentifierType type;
            private final String value;

            // optional
            private Boolean preferred;
            private String comment;
            private Period period;

            private Builder(NamingSystemIdentifierType type, String value) {
                super();
                this.type = type;
                this.value = value;
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
             * Indicates whether this identifier is the "preferred" identifier of this type.
             * </p>
             * 
             * @param preferred
             *     Is this the id that should be used for this type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder preferred(Boolean preferred) {
                this.preferred = preferred;
                return this;
            }

            /**
             * <p>
             * Notes about the past or intended usage of this identifier.
             * </p>
             * 
             * @param comment
             *     Notes about identifier usage
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder comment(String comment) {
                this.comment = comment;
                return this;
            }

            /**
             * <p>
             * Identifies the period of time over which this identifier is considered appropriate to refer to the naming system. 
             * Outside of this window, the identifier might be non-deterministic.
             * </p>
             * 
             * @param period
             *     When is identifier valid?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            @Override
            public UniqueId build() {
                return new UniqueId(this);
            }

            private Builder from(UniqueId uniqueId) {
                id = uniqueId.id;
                extension.addAll(uniqueId.extension);
                modifierExtension.addAll(uniqueId.modifierExtension);
                preferred = uniqueId.preferred;
                comment = uniqueId.comment;
                period = uniqueId.period;
                return this;
            }
        }
    }
}
