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
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.CompartmentCode;
import com.ibm.fhir.model.type.code.GraphCompartmentRule;
import com.ibm.fhir.model.type.code.GraphCompartmentUse;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by 
 * following references. The Graph Definition resource defines a set and makes rules about the set.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "gdf-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/GraphDefinition"
)
@Constraint(
    id = "graphDefinition-1",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/GraphDefinition",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class GraphDefinition extends DomainResource {
    @Summary
    private final Uri url;
    @Summary
    private final String version;
    @Summary
    @Required
    private final String name;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.3.0-CIBUILD"
    )
    @Required
    private final PublicationStatus status;
    @Summary
    private final Boolean experimental;
    @Summary
    private final DateTime date;
    @Summary
    private final String publisher;
    @Summary
    private final List<ContactDetail> contact;
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
    private final Markdown purpose;
    @Summary
    @Binding(
        bindingName = "ResourceType",
        strength = BindingStrength.Value.REQUIRED,
        description = "One of the resource types defined as part of this version of FHIR.",
        valueSet = "http://hl7.org/fhir/ValueSet/resource-types|4.3.0-CIBUILD"
    )
    @Required
    private final ResourceTypeCode start;
    private final Canonical profile;
    private final List<Link> link;

    private GraphDefinition(Builder builder) {
        super(builder);
        url = builder.url;
        version = builder.version;
        name = builder.name;
        status = builder.status;
        experimental = builder.experimental;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        purpose = builder.purpose;
        start = builder.start;
        profile = builder.profile;
        link = Collections.unmodifiableList(builder.link);
    }

    /**
     * An absolute URI that is used to identify this graph definition when it is referenced in a specification, model, design 
     * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
     * at which at which an authoritative instance of this graph definition is (or will be) published. This URL can be the 
     * target of a canonical reference. It SHALL remain the same when the graph definition is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * The identifier that is used to identify this version of the graph definition when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the graph definition author and is not expected to be 
     * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
     * also no expectation that versions can be placed in a lexicographical sequence.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the graph definition. This name should be usable as an identifier for the module 
     * by machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that is non-null.
     */
    public String getName() {
        return name;
    }

    /**
     * The status of this graph definition. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this graph definition is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the graph definition was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the graph definition changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the graph definition.
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
     * A free text natural language description of the graph definition from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate graph definition instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the graph definition is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this graph definition is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * The type of FHIR resource at which instances of this graph start.
     * 
     * @return
     *     An immutable object of type {@link ResourceTypeCode} that is non-null.
     */
    public ResourceTypeCode getStart() {
        return start;
    }

    /**
     * The profile that describes the use of the base resource.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getProfile() {
        return profile;
    }

    /**
     * Links this graph makes rules about.
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
            (url != null) || 
            (version != null) || 
            (name != null) || 
            (status != null) || 
            (experimental != null) || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            (description != null) || 
            !useContext.isEmpty() || 
            !jurisdiction.isEmpty() || 
            (purpose != null) || 
            (start != null) || 
            (profile != null) || 
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
                accept(url, "url", visitor);
                accept(version, "version", visitor);
                accept(name, "name", visitor);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(purpose, "purpose", visitor);
                accept(start, "start", visitor);
                accept(profile, "profile", visitor);
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
        GraphDefinition other = (GraphDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(url, other.url) && 
            Objects.equals(version, other.version) && 
            Objects.equals(name, other.name) && 
            Objects.equals(status, other.status) && 
            Objects.equals(experimental, other.experimental) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(description, other.description) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(start, other.start) && 
            Objects.equals(profile, other.profile) && 
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
                url, 
                version, 
                name, 
                status, 
                experimental, 
                date, 
                publisher, 
                contact, 
                description, 
                useContext, 
                jurisdiction, 
                purpose, 
                start, 
                profile, 
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
        private Uri url;
        private String version;
        private String name;
        private PublicationStatus status;
        private Boolean experimental;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private ResourceTypeCode start;
        private Canonical profile;
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
         * An absolute URI that is used to identify this graph definition when it is referenced in a specification, model, design 
         * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
         * at which at which an authoritative instance of this graph definition is (or will be) published. This URL can be the 
         * target of a canonical reference. It SHALL remain the same when the graph definition is stored on different servers.
         * 
         * @param url
         *     Canonical identifier for this graph definition, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * Convenience method for setting {@code version}.
         * 
         * @param version
         *     Business version of the graph definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #version(com.ibm.fhir.model.type.String)
         */
        public Builder version(java.lang.String version) {
            this.version = (version == null) ? null : String.of(version);
            return this;
        }

        /**
         * The identifier that is used to identify this version of the graph definition when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the graph definition author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of the graph definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Convenience method for setting {@code name}.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     Name for this graph definition (computer friendly)
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
         * A natural language name identifying the graph definition. This name should be usable as an identifier for the module 
         * by machine processing applications such as code generation.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     Name for this graph definition (computer friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * The status of this graph definition. Enables tracking the life-cycle of the content.
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
         * Convenience method for setting {@code experimental}.
         * 
         * @param experimental
         *     For testing purposes, not real usage
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #experimental(com.ibm.fhir.model.type.Boolean)
         */
        public Builder experimental(java.lang.Boolean experimental) {
            this.experimental = (experimental == null) ? null : Boolean.of(experimental);
            return this;
        }

        /**
         * A Boolean value to indicate that this graph definition is authored for testing purposes (or 
         * education/evaluation/marketing) and is not intended to be used for genuine usage.
         * 
         * @param experimental
         *     For testing purposes, not real usage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder experimental(Boolean experimental) {
            this.experimental = experimental;
            return this;
        }

        /**
         * The date (and optionally time) when the graph definition was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the graph definition changes.
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
         * The name of the organization or individual that published the graph definition.
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
         * A free text natural language description of the graph definition from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the graph definition
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
         * may be used to assist with indexing and searching for appropriate graph definition instances.
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
         * may be used to assist with indexing and searching for appropriate graph definition instances.
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
         * A legal or geographic region in which the graph definition is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for graph definition (if applicable)
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
         * A legal or geographic region in which the graph definition is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for graph definition (if applicable)
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
         * Explanation of why this graph definition is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this graph definition is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * The type of FHIR resource at which instances of this graph start.
         * 
         * <p>This element is required.
         * 
         * @param start
         *     Type of resource at which the graph starts
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder start(ResourceTypeCode start) {
            this.start = start;
            return this;
        }

        /**
         * The profile that describes the use of the base resource.
         * 
         * @param profile
         *     Profile on base resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder profile(Canonical profile) {
            this.profile = profile;
            return this;
        }

        /**
         * Links this graph makes rules about.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param link
         *     Links this graph makes rules about
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
         * Links this graph makes rules about.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param link
         *     Links this graph makes rules about
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
         * Build the {@link GraphDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>name</li>
         * <li>status</li>
         * <li>start</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link GraphDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid GraphDefinition per the base specification
         */
        @Override
        public GraphDefinition build() {
            GraphDefinition graphDefinition = new GraphDefinition(this);
            if (validating) {
                validate(graphDefinition);
            }
            return graphDefinition;
        }

        protected void validate(GraphDefinition graphDefinition) {
            super.validate(graphDefinition);
            ValidationSupport.requireNonNull(graphDefinition.name, "name");
            ValidationSupport.requireNonNull(graphDefinition.status, "status");
            ValidationSupport.checkList(graphDefinition.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(graphDefinition.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(graphDefinition.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.requireNonNull(graphDefinition.start, "start");
            ValidationSupport.checkList(graphDefinition.link, "link", Link.class);
        }

        protected Builder from(GraphDefinition graphDefinition) {
            super.from(graphDefinition);
            url = graphDefinition.url;
            version = graphDefinition.version;
            name = graphDefinition.name;
            status = graphDefinition.status;
            experimental = graphDefinition.experimental;
            date = graphDefinition.date;
            publisher = graphDefinition.publisher;
            contact.addAll(graphDefinition.contact);
            description = graphDefinition.description;
            useContext.addAll(graphDefinition.useContext);
            jurisdiction.addAll(graphDefinition.jurisdiction);
            purpose = graphDefinition.purpose;
            start = graphDefinition.start;
            profile = graphDefinition.profile;
            link.addAll(graphDefinition.link);
            return this;
        }
    }

    /**
     * Links this graph makes rules about.
     */
    public static class Link extends BackboneElement {
        private final String path;
        private final String sliceName;
        private final Integer min;
        private final String max;
        private final String description;
        private final List<Target> target;

        private Link(Builder builder) {
            super(builder);
            path = builder.path;
            sliceName = builder.sliceName;
            min = builder.min;
            max = builder.max;
            description = builder.description;
            target = Collections.unmodifiableList(builder.target);
        }

        /**
         * A FHIR expression that identifies one of FHIR References to other resources.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getPath() {
            return path;
        }

        /**
         * Which slice (if profiled).
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getSliceName() {
            return sliceName;
        }

        /**
         * Minimum occurrences for this link.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getMin() {
            return min;
        }

        /**
         * Maximum occurrences for this link.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getMax() {
            return max;
        }

        /**
         * Information about why this link is of interest in this graph definition.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Potential target for the link.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Target} that may be empty.
         */
        public List<Target> getTarget() {
            return target;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (path != null) || 
                (sliceName != null) || 
                (min != null) || 
                (max != null) || 
                (description != null) || 
                !target.isEmpty();
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
                    accept(path, "path", visitor);
                    accept(sliceName, "sliceName", visitor);
                    accept(min, "min", visitor);
                    accept(max, "max", visitor);
                    accept(description, "description", visitor);
                    accept(target, "target", visitor, Target.class);
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
                Objects.equals(path, other.path) && 
                Objects.equals(sliceName, other.sliceName) && 
                Objects.equals(min, other.min) && 
                Objects.equals(max, other.max) && 
                Objects.equals(description, other.description) && 
                Objects.equals(target, other.target);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    path, 
                    sliceName, 
                    min, 
                    max, 
                    description, 
                    target);
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
            private String path;
            private String sliceName;
            private Integer min;
            private String max;
            private String description;
            private List<Target> target = new ArrayList<>();

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
             * Convenience method for setting {@code path}.
             * 
             * @param path
             *     Path in the resource that contains the link
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #path(com.ibm.fhir.model.type.String)
             */
            public Builder path(java.lang.String path) {
                this.path = (path == null) ? null : String.of(path);
                return this;
            }

            /**
             * A FHIR expression that identifies one of FHIR References to other resources.
             * 
             * @param path
             *     Path in the resource that contains the link
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder path(String path) {
                this.path = path;
                return this;
            }

            /**
             * Convenience method for setting {@code sliceName}.
             * 
             * @param sliceName
             *     Which slice (if profiled)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #sliceName(com.ibm.fhir.model.type.String)
             */
            public Builder sliceName(java.lang.String sliceName) {
                this.sliceName = (sliceName == null) ? null : String.of(sliceName);
                return this;
            }

            /**
             * Which slice (if profiled).
             * 
             * @param sliceName
             *     Which slice (if profiled)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sliceName(String sliceName) {
                this.sliceName = sliceName;
                return this;
            }

            /**
             * Convenience method for setting {@code min}.
             * 
             * @param min
             *     Minimum occurrences for this link
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #min(com.ibm.fhir.model.type.Integer)
             */
            public Builder min(java.lang.Integer min) {
                this.min = (min == null) ? null : Integer.of(min);
                return this;
            }

            /**
             * Minimum occurrences for this link.
             * 
             * @param min
             *     Minimum occurrences for this link
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder min(Integer min) {
                this.min = min;
                return this;
            }

            /**
             * Convenience method for setting {@code max}.
             * 
             * @param max
             *     Maximum occurrences for this link
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #max(com.ibm.fhir.model.type.String)
             */
            public Builder max(java.lang.String max) {
                this.max = (max == null) ? null : String.of(max);
                return this;
            }

            /**
             * Maximum occurrences for this link.
             * 
             * @param max
             *     Maximum occurrences for this link
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder max(String max) {
                this.max = max;
                return this;
            }

            /**
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Why this link is specified
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #description(com.ibm.fhir.model.type.String)
             */
            public Builder description(java.lang.String description) {
                this.description = (description == null) ? null : String.of(description);
                return this;
            }

            /**
             * Information about why this link is of interest in this graph definition.
             * 
             * @param description
             *     Why this link is specified
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Potential target for the link.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param target
             *     Potential target for the link
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder target(Target... target) {
                for (Target value : target) {
                    this.target.add(value);
                }
                return this;
            }

            /**
             * Potential target for the link.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param target
             *     Potential target for the link
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder target(Collection<Target> target) {
                this.target = new ArrayList<>(target);
                return this;
            }

            /**
             * Build the {@link Link}
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
                ValidationSupport.checkList(link.target, "target", Target.class);
                ValidationSupport.requireValueOrChildren(link);
            }

            protected Builder from(Link link) {
                super.from(link);
                path = link.path;
                sliceName = link.sliceName;
                min = link.min;
                max = link.max;
                description = link.description;
                target.addAll(link.target);
                return this;
            }
        }

        /**
         * Potential target for the link.
         */
        public static class Target extends BackboneElement {
            @Binding(
                bindingName = "ResourceType",
                strength = BindingStrength.Value.REQUIRED,
                description = "One of the resource types defined as part of this version of FHIR.",
                valueSet = "http://hl7.org/fhir/ValueSet/resource-types|4.3.0-CIBUILD"
            )
            @Required
            private final ResourceTypeCode type;
            private final String params;
            private final Canonical profile;
            private final List<Compartment> compartment;
            private final List<GraphDefinition.Link> link;

            private Target(Builder builder) {
                super(builder);
                type = builder.type;
                params = builder.params;
                profile = builder.profile;
                compartment = Collections.unmodifiableList(builder.compartment);
                link = Collections.unmodifiableList(builder.link);
            }

            /**
             * Type of resource this link refers to.
             * 
             * @return
             *     An immutable object of type {@link ResourceTypeCode} that is non-null.
             */
            public ResourceTypeCode getType() {
                return type;
            }

            /**
             * A set of parameters to look up.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getParams() {
                return params;
            }

            /**
             * Profile for the target resource.
             * 
             * @return
             *     An immutable object of type {@link Canonical} that may be null.
             */
            public Canonical getProfile() {
                return profile;
            }

            /**
             * Compartment Consistency Rules.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Compartment} that may be empty.
             */
            public List<Compartment> getCompartment() {
                return compartment;
            }

            /**
             * Additional links from target resource.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Link} that may be empty.
             */
            public List<GraphDefinition.Link> getLink() {
                return link;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (params != null) || 
                    (profile != null) || 
                    !compartment.isEmpty() || 
                    !link.isEmpty();
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
                        accept(params, "params", visitor);
                        accept(profile, "profile", visitor);
                        accept(compartment, "compartment", visitor, Compartment.class);
                        accept(link, "link", visitor, GraphDefinition.Link.class);
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
                Target other = (Target) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(params, other.params) && 
                    Objects.equals(profile, other.profile) && 
                    Objects.equals(compartment, other.compartment) && 
                    Objects.equals(link, other.link);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        params, 
                        profile, 
                        compartment, 
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

            public static class Builder extends BackboneElement.Builder {
                private ResourceTypeCode type;
                private String params;
                private Canonical profile;
                private List<Compartment> compartment = new ArrayList<>();
                private List<GraphDefinition.Link> link = new ArrayList<>();

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
                 * Type of resource this link refers to.
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     Type of resource this link refers to
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(ResourceTypeCode type) {
                    this.type = type;
                    return this;
                }

                /**
                 * Convenience method for setting {@code params}.
                 * 
                 * @param params
                 *     Criteria for reverse lookup
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #params(com.ibm.fhir.model.type.String)
                 */
                public Builder params(java.lang.String params) {
                    this.params = (params == null) ? null : String.of(params);
                    return this;
                }

                /**
                 * A set of parameters to look up.
                 * 
                 * @param params
                 *     Criteria for reverse lookup
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder params(String params) {
                    this.params = params;
                    return this;
                }

                /**
                 * Profile for the target resource.
                 * 
                 * @param profile
                 *     Profile for the target resource
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder profile(Canonical profile) {
                    this.profile = profile;
                    return this;
                }

                /**
                 * Compartment Consistency Rules.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param compartment
                 *     Compartment Consistency Rules
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder compartment(Compartment... compartment) {
                    for (Compartment value : compartment) {
                        this.compartment.add(value);
                    }
                    return this;
                }

                /**
                 * Compartment Consistency Rules.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param compartment
                 *     Compartment Consistency Rules
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder compartment(Collection<Compartment> compartment) {
                    this.compartment = new ArrayList<>(compartment);
                    return this;
                }

                /**
                 * Additional links from target resource.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param link
                 *     Additional links from target resource
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder link(GraphDefinition.Link... link) {
                    for (GraphDefinition.Link value : link) {
                        this.link.add(value);
                    }
                    return this;
                }

                /**
                 * Additional links from target resource.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param link
                 *     Additional links from target resource
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder link(Collection<GraphDefinition.Link> link) {
                    this.link = new ArrayList<>(link);
                    return this;
                }

                /**
                 * Build the {@link Target}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Target}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Target per the base specification
                 */
                @Override
                public Target build() {
                    Target target = new Target(this);
                    if (validating) {
                        validate(target);
                    }
                    return target;
                }

                protected void validate(Target target) {
                    super.validate(target);
                    ValidationSupport.requireNonNull(target.type, "type");
                    ValidationSupport.checkList(target.compartment, "compartment", Compartment.class);
                    ValidationSupport.checkList(target.link, "link", GraphDefinition.Link.class);
                    ValidationSupport.requireValueOrChildren(target);
                }

                protected Builder from(Target target) {
                    super.from(target);
                    type = target.type;
                    params = target.params;
                    profile = target.profile;
                    compartment.addAll(target.compartment);
                    link.addAll(target.link);
                    return this;
                }
            }

            /**
             * Compartment Consistency Rules.
             */
            public static class Compartment extends BackboneElement {
                @Binding(
                    bindingName = "GraphCompartmentUse",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "Defines how a compartment rule is used.",
                    valueSet = "http://hl7.org/fhir/ValueSet/graph-compartment-use|4.3.0-CIBUILD"
                )
                @Required
                private final GraphCompartmentUse use;
                @Binding(
                    bindingName = "CompartmentCode",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "Identifies a compartment.",
                    valueSet = "http://hl7.org/fhir/ValueSet/compartment-type|4.3.0-CIBUILD"
                )
                @Required
                private final CompartmentCode code;
                @Binding(
                    bindingName = "GraphCompartmentRule",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "How a compartment must be linked.",
                    valueSet = "http://hl7.org/fhir/ValueSet/graph-compartment-rule|4.3.0-CIBUILD"
                )
                @Required
                private final GraphCompartmentRule rule;
                private final String expression;
                private final String description;

                private Compartment(Builder builder) {
                    super(builder);
                    use = builder.use;
                    code = builder.code;
                    rule = builder.rule;
                    expression = builder.expression;
                    description = builder.description;
                }

                /**
                 * Defines how the compartment rule is used - whether it it is used to test whether resources are subject to the rule, or 
                 * whether it is a rule that must be followed.
                 * 
                 * @return
                 *     An immutable object of type {@link GraphCompartmentUse} that is non-null.
                 */
                public GraphCompartmentUse getUse() {
                    return use;
                }

                /**
                 * Identifies the compartment.
                 * 
                 * @return
                 *     An immutable object of type {@link CompartmentCode} that is non-null.
                 */
                public CompartmentCode getCode() {
                    return code;
                }

                /**
                 * identical | matching | different | no-rule | custom.
                 * 
                 * @return
                 *     An immutable object of type {@link GraphCompartmentRule} that is non-null.
                 */
                public GraphCompartmentRule getRule() {
                    return rule;
                }

                /**
                 * Custom rule, as a FHIRPath expression.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getExpression() {
                    return expression;
                }

                /**
                 * Documentation for FHIRPath expression.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getDescription() {
                    return description;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (use != null) || 
                        (code != null) || 
                        (rule != null) || 
                        (expression != null) || 
                        (description != null);
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
                            accept(use, "use", visitor);
                            accept(code, "code", visitor);
                            accept(rule, "rule", visitor);
                            accept(expression, "expression", visitor);
                            accept(description, "description", visitor);
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
                    Compartment other = (Compartment) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(use, other.use) && 
                        Objects.equals(code, other.code) && 
                        Objects.equals(rule, other.rule) && 
                        Objects.equals(expression, other.expression) && 
                        Objects.equals(description, other.description);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            use, 
                            code, 
                            rule, 
                            expression, 
                            description);
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
                    private GraphCompartmentUse use;
                    private CompartmentCode code;
                    private GraphCompartmentRule rule;
                    private String expression;
                    private String description;

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
                     * Defines how the compartment rule is used - whether it it is used to test whether resources are subject to the rule, or 
                     * whether it is a rule that must be followed.
                     * 
                     * <p>This element is required.
                     * 
                     * @param use
                     *     condition | requirement
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder use(GraphCompartmentUse use) {
                        this.use = use;
                        return this;
                    }

                    /**
                     * Identifies the compartment.
                     * 
                     * <p>This element is required.
                     * 
                     * @param code
                     *     Patient | Encounter | RelatedPerson | Practitioner | Device
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder code(CompartmentCode code) {
                        this.code = code;
                        return this;
                    }

                    /**
                     * identical | matching | different | no-rule | custom.
                     * 
                     * <p>This element is required.
                     * 
                     * @param rule
                     *     identical | matching | different | custom
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder rule(GraphCompartmentRule rule) {
                        this.rule = rule;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code expression}.
                     * 
                     * @param expression
                     *     Custom rule, as a FHIRPath expression
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #expression(com.ibm.fhir.model.type.String)
                     */
                    public Builder expression(java.lang.String expression) {
                        this.expression = (expression == null) ? null : String.of(expression);
                        return this;
                    }

                    /**
                     * Custom rule, as a FHIRPath expression.
                     * 
                     * @param expression
                     *     Custom rule, as a FHIRPath expression
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder expression(String expression) {
                        this.expression = expression;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code description}.
                     * 
                     * @param description
                     *     Documentation for FHIRPath expression
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #description(com.ibm.fhir.model.type.String)
                     */
                    public Builder description(java.lang.String description) {
                        this.description = (description == null) ? null : String.of(description);
                        return this;
                    }

                    /**
                     * Documentation for FHIRPath expression.
                     * 
                     * @param description
                     *     Documentation for FHIRPath expression
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder description(String description) {
                        this.description = description;
                        return this;
                    }

                    /**
                     * Build the {@link Compartment}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>use</li>
                     * <li>code</li>
                     * <li>rule</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Compartment}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Compartment per the base specification
                     */
                    @Override
                    public Compartment build() {
                        Compartment compartment = new Compartment(this);
                        if (validating) {
                            validate(compartment);
                        }
                        return compartment;
                    }

                    protected void validate(Compartment compartment) {
                        super.validate(compartment);
                        ValidationSupport.requireNonNull(compartment.use, "use");
                        ValidationSupport.requireNonNull(compartment.code, "code");
                        ValidationSupport.requireNonNull(compartment.rule, "rule");
                        ValidationSupport.requireValueOrChildren(compartment);
                    }

                    protected Builder from(Compartment compartment) {
                        super.from(compartment);
                        use = compartment.use;
                        code = compartment.code;
                        rule = compartment.rule;
                        expression = compartment.expression;
                        description = compartment.description;
                        return this;
                    }
                }
            }
        }
    }
}
