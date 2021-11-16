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
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.NamingSystemIdentifierType;
import com.ibm.fhir.model.type.code.NamingSystemType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, 
 * devices, etc. Represents a "System" used within the Identifier and Coding data types.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "nsd-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/NamingSystem"
)
@Constraint(
    id = "nsd-1",
    level = "Rule",
    location = "(base)",
    description = "Root systems cannot have uuid identifiers",
    expression = "kind != 'root' or uniqueId.all(type != 'uuid')",
    source = "http://hl7.org/fhir/StructureDefinition/NamingSystem"
)
@Constraint(
    id = "nsd-2",
    level = "Rule",
    location = "(base)",
    description = "Can't have more than one preferred identifier for a type",
    expression = "uniqueId.where(preferred = true).select(type).isDistinct()",
    source = "http://hl7.org/fhir/StructureDefinition/NamingSystem"
)
@Constraint(
    id = "namingSystem-3",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/identifier-type",
    expression = "type.exists() implies (type.memberOf('http://hl7.org/fhir/ValueSet/identifier-type', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/NamingSystem",
    generated = true
)
@Constraint(
    id = "namingSystem-4",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/NamingSystem",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class NamingSystem extends DomainResource {
    @Summary
    @Required
    private final String name;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.0.1"
    )
    @Required
    private final PublicationStatus status;
    @Summary
    @Binding(
        bindingName = "NamingSystemType",
        strength = BindingStrength.Value.REQUIRED,
        description = "Identifies the purpose of the naming system.",
        valueSet = "http://hl7.org/fhir/ValueSet/namingsystem-type|4.0.1"
    )
    @Required
    private final NamingSystemType kind;
    @Summary
    @Required
    private final DateTime date;
    @Summary
    private final String publisher;
    @Summary
    private final List<ContactDetail> contact;
    private final String responsible;
    @Binding(
        bindingName = "IdentifierType",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "A coded type for an identifier that can be used to determine which identifier to use for a specific purpose.",
        valueSet = "http://hl7.org/fhir/ValueSet/identifier-type"
    )
    private final CodeableConcept type;
    private final Markdown description;
    @Summary
    private final List<UsageContext> useContext;
    @Summary
    @Binding(
        bindingName = "Jurisdiction",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Countries and regions within which this artifact is targeted for use.",
        valueSet = "http://hl7.org/fhir/ValueSet/jurisdiction"
    )
    private final List<CodeableConcept> jurisdiction;
    private final String usage;
    @Summary
    @Required
    private final List<UniqueId> uniqueId;

    private NamingSystem(Builder builder) {
        super(builder);
        name = builder.name;
        status = builder.status;
        kind = builder.kind;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        responsible = builder.responsible;
        type = builder.type;
        description = builder.description;
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        usage = builder.usage;
        uniqueId = Collections.unmodifiableList(builder.uniqueId);
    }

    /**
     * A natural language name identifying the naming system. This name should be usable as an identifier for the module by 
     * machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that is non-null.
     */
    public String getName() {
        return name;
    }

    /**
     * The status of this naming system. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * Indicates the purpose for the naming system - what kinds of things does it make unique?
     * 
     * @return
     *     An immutable object of type {@link NamingSystemType} that is non-null.
     */
    public NamingSystemType getKind() {
        return kind;
    }

    /**
     * The date (and optionally time) when the naming system was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the naming system changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the naming system.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring 
     * their non-collision.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getResponsible() {
        return responsible;
    }

    /**
     * Categorizes a naming system for easier search by grouping related naming systems.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * A free text natural language description of the naming system from a consumer's perspective. Details about what the 
     * namespace identifies including scope, granularity, version labeling, etc.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate naming system instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the naming system is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower 
     * case, etc.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Indicates how the system may be identified when referenced in electronic exchange.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UniqueId} that is non-empty.
     */
    public List<UniqueId> getUniqueId() {
        return uniqueId;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (name != null) || 
            (status != null) || 
            (kind != null) || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            (responsible != null) || 
            (type != null) || 
            (description != null) || 
            !useContext.isEmpty() || 
            !jurisdiction.isEmpty() || 
            (usage != null) || 
            !uniqueId.isEmpty();
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
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private String name;
        private PublicationStatus status;
        private NamingSystemType kind;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private String responsible;
        private CodeableConcept type;
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private String usage;
        private List<UniqueId> uniqueId = new ArrayList<>();

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
         * Convenience method for setting {@code name}.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     Name for this naming system (computer friendly)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #name(com.ibm.fhir.model.type.String)
         */
        public Builder name(java.lang.String name) {
            this.name = (name == null) ? null : String.of(name);
            return this;
        }

        /**
         * A natural language name identifying the naming system. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     Name for this naming system (computer friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * The status of this naming system. Enables tracking the life-cycle of the content.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     draft | active | retired | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(PublicationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Indicates the purpose for the naming system - what kinds of things does it make unique?
         * 
         * <p>This element is required.
         * 
         * @param kind
         *     codesystem | identifier | root
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder kind(NamingSystemType kind) {
            this.kind = kind;
            return this;
        }

        /**
         * The date (and optionally time) when the naming system was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the naming system changes.
         * 
         * <p>This element is required.
         * 
         * @param date
         *     Date last changed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * Convenience method for setting {@code publisher}.
         * 
         * @param publisher
         *     Name of the publisher (organization or individual)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #publisher(com.ibm.fhir.model.type.String)
         */
        public Builder publisher(java.lang.String publisher) {
            this.publisher = (publisher == null) ? null : String.of(publisher);
            return this;
        }

        /**
         * The name of the organization or individual that published the naming system.
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
         * Contact details to assist a user in finding and communicating with the publisher.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Contact details to assist a user in finding and communicating with the publisher.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * Convenience method for setting {@code responsible}.
         * 
         * @param responsible
         *     Who maintains system namespace?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #responsible(com.ibm.fhir.model.type.String)
         */
        public Builder responsible(java.lang.String responsible) {
            this.responsible = (responsible == null) ? null : String.of(responsible);
            return this;
        }

        /**
         * The name of the organization that is responsible for issuing identifiers or codes for this namespace and ensuring 
         * their non-collision.
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
         * Categorizes a naming system for easier search by grouping related naming systems.
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
         * A free text natural language description of the naming system from a consumer's perspective. Details about what the 
         * namespace identifies including scope, granularity, version labeling, etc.
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate naming system instances.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate naming system instances.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder useContext(Collection<UsageContext> useContext) {
            this.useContext = new ArrayList<>(useContext);
            return this;
        }

        /**
         * A legal or geographic region in which the naming system is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A legal or geographic region in which the naming system is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for naming system (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction = new ArrayList<>(jurisdiction);
            return this;
        }

        /**
         * Convenience method for setting {@code usage}.
         * 
         * @param usage
         *     How/where is it used
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #usage(com.ibm.fhir.model.type.String)
         */
        public Builder usage(java.lang.String usage) {
            this.usage = (usage == null) ? null : String.of(usage);
            return this;
        }

        /**
         * Provides guidance on the use of the namespace, including the handling of formatting characters, use of upper vs. lower 
         * case, etc.
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

        /**
         * Indicates how the system may be identified when referenced in electronic exchange.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param uniqueId
         *     Unique identifiers used for system
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder uniqueId(UniqueId... uniqueId) {
            for (UniqueId value : uniqueId) {
                this.uniqueId.add(value);
            }
            return this;
        }

        /**
         * Indicates how the system may be identified when referenced in electronic exchange.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param uniqueId
         *     Unique identifiers used for system
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder uniqueId(Collection<UniqueId> uniqueId) {
            this.uniqueId = new ArrayList<>(uniqueId);
            return this;
        }

        /**
         * Build the {@link NamingSystem}
         * 
         * <p>Required elements:
         * <ul>
         * <li>name</li>
         * <li>status</li>
         * <li>kind</li>
         * <li>date</li>
         * <li>uniqueId</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link NamingSystem}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid NamingSystem per the base specification
         */
        @Override
        public NamingSystem build() {
            NamingSystem namingSystem = new NamingSystem(this);
            if (validating) {
                validate(namingSystem);
            }
            return namingSystem;
        }

        protected void validate(NamingSystem namingSystem) {
            super.validate(namingSystem);
            ValidationSupport.requireNonNull(namingSystem.name, "name");
            ValidationSupport.requireNonNull(namingSystem.status, "status");
            ValidationSupport.requireNonNull(namingSystem.kind, "kind");
            ValidationSupport.requireNonNull(namingSystem.date, "date");
            ValidationSupport.checkList(namingSystem.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(namingSystem.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(namingSystem.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkNonEmptyList(namingSystem.uniqueId, "uniqueId", UniqueId.class);
        }

        protected Builder from(NamingSystem namingSystem) {
            super.from(namingSystem);
            name = namingSystem.name;
            status = namingSystem.status;
            kind = namingSystem.kind;
            date = namingSystem.date;
            publisher = namingSystem.publisher;
            contact.addAll(namingSystem.contact);
            responsible = namingSystem.responsible;
            type = namingSystem.type;
            description = namingSystem.description;
            useContext.addAll(namingSystem.useContext);
            jurisdiction.addAll(namingSystem.jurisdiction);
            usage = namingSystem.usage;
            uniqueId.addAll(namingSystem.uniqueId);
            return this;
        }
    }

    /**
     * Indicates how the system may be identified when referenced in electronic exchange.
     */
    public static class UniqueId extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "NamingSystemIdentifierType",
            strength = BindingStrength.Value.REQUIRED,
            description = "Identifies the style of unique identifier used to identify a namespace.",
            valueSet = "http://hl7.org/fhir/ValueSet/namingsystem-identifier-type|4.0.1"
        )
        @Required
        private final NamingSystemIdentifierType type;
        @Summary
        @Required
        private final String value;
        private final Boolean preferred;
        private final String comment;
        private final Period period;

        private UniqueId(Builder builder) {
            super(builder);
            type = builder.type;
            value = builder.value;
            preferred = builder.preferred;
            comment = builder.comment;
            period = builder.period;
        }

        /**
         * Identifies the unique identifier scheme used for this particular identifier.
         * 
         * @return
         *     An immutable object of type {@link NamingSystemIdentifierType} that is non-null.
         */
        public NamingSystemIdentifierType getType() {
            return type;
        }

        /**
         * The string that should be sent over the wire to identify the code system or identifier system.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getValue() {
            return value;
        }

        /**
         * Indicates whether this identifier is the "preferred" identifier of this type.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getPreferred() {
            return preferred;
        }

        /**
         * Notes about the past or intended usage of this identifier.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getComment() {
            return comment;
        }

        /**
         * Identifies the period of time over which this identifier is considered appropriate to refer to the naming system. 
         * Outside of this window, the identifier might be non-deterministic.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
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
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private NamingSystemIdentifierType type;
            private String value;
            private Boolean preferred;
            private String comment;
            private Period period;

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
             * Identifies the unique identifier scheme used for this particular identifier.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     oid | uuid | uri | other
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(NamingSystemIdentifierType type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code value}.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     The unique identifier
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(com.ibm.fhir.model.type.String)
             */
            public Builder value(java.lang.String value) {
                this.value = (value == null) ? null : String.of(value);
                return this;
            }

            /**
             * The string that should be sent over the wire to identify the code system or identifier system.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     The unique identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(String value) {
                this.value = value;
                return this;
            }

            /**
             * Convenience method for setting {@code preferred}.
             * 
             * @param preferred
             *     Is this the id that should be used for this type
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
             * Indicates whether this identifier is the "preferred" identifier of this type.
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
             * Convenience method for setting {@code comment}.
             * 
             * @param comment
             *     Notes about identifier usage
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #comment(com.ibm.fhir.model.type.String)
             */
            public Builder comment(java.lang.String comment) {
                this.comment = (comment == null) ? null : String.of(comment);
                return this;
            }

            /**
             * Notes about the past or intended usage of this identifier.
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
             * Identifies the period of time over which this identifier is considered appropriate to refer to the naming system. 
             * Outside of this window, the identifier might be non-deterministic.
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

            /**
             * Build the {@link UniqueId}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>value</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link UniqueId}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid UniqueId per the base specification
             */
            @Override
            public UniqueId build() {
                UniqueId uniqueId = new UniqueId(this);
                if (validating) {
                    validate(uniqueId);
                }
                return uniqueId;
            }

            protected void validate(UniqueId uniqueId) {
                super.validate(uniqueId);
                ValidationSupport.requireNonNull(uniqueId.type, "type");
                ValidationSupport.requireNonNull(uniqueId.value, "value");
                ValidationSupport.requireValueOrChildren(uniqueId);
            }

            protected Builder from(UniqueId uniqueId) {
                super.from(uniqueId);
                type = uniqueId.type;
                value = uniqueId.value;
                preferred = uniqueId.preferred;
                comment = uniqueId.comment;
                period = uniqueId.period;
                return this;
            }
        }
    }
}
