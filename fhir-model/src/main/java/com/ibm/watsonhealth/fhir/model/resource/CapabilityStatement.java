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
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.CapabilityStatementKind;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ConditionalDeleteStatus;
import com.ibm.watsonhealth.fhir.model.type.ConditionalReadStatus;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.DocumentMode;
import com.ibm.watsonhealth.fhir.model.type.EventCapabilityMode;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.FHIRVersion;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.ReferenceHandlingPolicy;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.model.type.ResourceVersionPolicy;
import com.ibm.watsonhealth.fhir.model.type.RestfulCapabilityMode;
import com.ibm.watsonhealth.fhir.model.type.SearchParamType;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.SystemRestfulInteraction;
import com.ibm.watsonhealth.fhir.model.type.TypeRestfulInteraction;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Url;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server for a particular version of FHIR 
 * that may be used as a statement of actual server functionality or a statement of required or desired server 
 * implementation.
 * </p>
 */
@Constraint(
    id = "cpb-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Constraint(
    id = "cpb-1",
    level = "Rule",
    location = "(base)",
    description = "A Capability Statement SHALL have at least one of REST, messaging or document element.",
    expression = "rest.exists() or messaging.exists() or document.exists()"
)
@Constraint(
    id = "cpb-2",
    level = "Rule",
    location = "(base)",
    description = "A Capability Statement SHALL have at least one of description, software, or implementation element.",
    expression = "(description.count() + software.count() + implementation.count()) > 0"
)
@Constraint(
    id = "cpb-3",
    level = "Rule",
    location = "(base)",
    description = "Messaging end-point is required (and is only permitted) when a statement is for an implementation.",
    expression = "messaging.endpoint.empty() or kind = 'instance'"
)
@Constraint(
    id = "cpb-7",
    level = "Rule",
    location = "(base)",
    description = "The set of documents must be unique by the combination of profile and mode.",
    expression = "document.select(profile&mode).isDistinct()"
)
@Constraint(
    id = "cpb-9",
    level = "Rule",
    location = "CapabilityStatement.rest",
    description = "A given resource can only be described once per RESTful mode.",
    expression = "resource.select(type).isDistinct()"
)
@Constraint(
    id = "cpb-12",
    level = "Rule",
    location = "CapabilityStatement.rest.resource",
    description = "Search parameter names must be unique in the context of a resource.",
    expression = "searchParam.select(name).isDistinct()"
)
@Constraint(
    id = "cpb-14",
    level = "Rule",
    location = "(base)",
    description = "If kind = instance, implementation must be present and software may be present",
    expression = "(kind != 'instance') or implementation.exists()"
)
@Constraint(
    id = "cpb-15",
    level = "Rule",
    location = "(base)",
    description = "If kind = capability, implementation must be absent, software must be present",
    expression = "(kind != 'capability') or (implementation.exists().not() and software.exists())"
)
@Constraint(
    id = "cpb-16",
    level = "Rule",
    location = "(base)",
    description = "If kind = requirements, implementation and software must be absent",
    expression = "(kind!='requirements') or (implementation.exists().not() and software.exists().not())"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class CapabilityStatement extends DomainResource {
    private final Uri url;
    private final String version;
    private final String name;
    private final String title;
    private final PublicationStatus status;
    private final Boolean experimental;
    private final DateTime date;
    private final String publisher;
    private final List<ContactDetail> contact;
    private final Markdown description;
    private final List<UsageContext> useContext;
    private final List<CodeableConcept> jurisdiction;
    private final Markdown purpose;
    private final Markdown copyright;
    private final CapabilityStatementKind kind;
    private final List<Canonical> instantiates;
    private final List<Canonical> imports;
    private final Software software;
    private final Implementation implementation;
    private final FHIRVersion fhirVersion;
    private final List<Code> format;
    private final List<Code> patchFormat;
    private final List<Canonical> implementationGuide;
    private final List<Rest> rest;
    private final List<Messaging> messaging;
    private final List<Document> document;

    private CapabilityStatement(Builder builder) {
        super(builder);
        this.url = builder.url;
        this.version = builder.version;
        this.name = builder.name;
        this.title = builder.title;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.experimental = builder.experimental;
        this.date = ValidationSupport.requireNonNull(builder.date, "date");
        this.publisher = builder.publisher;
        this.contact = builder.contact;
        this.description = builder.description;
        this.useContext = builder.useContext;
        this.jurisdiction = builder.jurisdiction;
        this.purpose = builder.purpose;
        this.copyright = builder.copyright;
        this.kind = ValidationSupport.requireNonNull(builder.kind, "kind");
        this.instantiates = builder.instantiates;
        this.imports = builder.imports;
        this.software = builder.software;
        this.implementation = builder.implementation;
        this.fhirVersion = ValidationSupport.requireNonNull(builder.fhirVersion, "fhirVersion");
        this.format = ValidationSupport.requireNonEmpty(builder.format, "format");
        this.patchFormat = builder.patchFormat;
        this.implementationGuide = builder.implementationGuide;
        this.rest = builder.rest;
        this.messaging = builder.messaging;
        this.document = builder.document;
    }

    /**
     * <p>
     * An absolute URI that is used to identify this capability statement when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this capability statement is (or will be) published. This URL 
     * can be the target of a canonical reference. It SHALL remain the same when the capability statement is stored on 
     * different servers.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * <p>
     * The identifier that is used to identify this version of the capability statement when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the capability statement author and is 
     * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
     * available. There is also no expectation that versions can be placed in a lexicographical sequence.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getVersion() {
        return version;
    }

    /**
     * <p>
     * A natural language name identifying the capability statement. This name should be usable as an identifier for the 
     * module by machine processing applications such as code generation.
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
     * A short, descriptive, user-friendly title for the capability statement.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>
     * The status of this capability statement. Enables tracking the life-cycle of the content.
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
     * A Boolean value to indicate that this capability statement is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * <p>
     * The date (and optionally time) when the capability statement was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the capability statement changes.
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
     * The name of the organization or individual that published the capability statement.
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
     *     A list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * <p>
     * A free text natural language description of the capability statement from a consumer's perspective. Typically, this is 
     * used when the capability statement describes a desired rather than an actual solution, for example as a formal 
     * expression of requirements as part of an RFP.
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
     * may be used to assist with indexing and searching for appropriate capability statement instances.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link UsageContext}.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * <p>
     * A legal or geographic region in which the capability statement is intended to be used.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * <p>
     * Explanation of why this capability statement is needed and why it has been designed as it has.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * <p>
     * A copyright statement relating to the capability statement and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the capability statement.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * <p>
     * The way that this statement is intended to be used, to describe an actual running instance of software, a particular 
     * product (kind, not instance of software) or a class of implementation (e.g. a desired purchase).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CapabilityStatementKind}.
     */
    public CapabilityStatementKind getKind() {
        return kind;
    }

    /**
     * <p>
     * Reference to a canonical URL of another CapabilityStatement that this software implements. This capability statement 
     * is a published API description that corresponds to a business service. The server may actually implement a subset of 
     * the capability statement it claims to implement, so the capability statement must specify the full capability details.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getInstantiates() {
        return instantiates;
    }

    /**
     * <p>
     * Reference to a canonical URL of another CapabilityStatement that this software adds to. The capability statement 
     * automatically includes everything in the other statement, and it is not duplicated, though the server may repeat the 
     * same resources, interactions and operations to add additional details to them.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getImports() {
        return imports;
    }

    /**
     * <p>
     * Software that is covered by this capability statement. It is used when the capability statement describes the 
     * capabilities of a particular software version, independent of an installation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Software}.
     */
    public Software getSoftware() {
        return software;
    }

    /**
     * <p>
     * Identifies a specific implementation instance that is described by the capability statement - i.e. a particular 
     * installation, rather than the capabilities of a software program.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Implementation}.
     */
    public Implementation getImplementation() {
        return implementation;
    }

    /**
     * <p>
     * The version of the FHIR specification that this CapabilityStatement describes (which SHALL be the same as the FHIR 
     * version of the CapabilityStatement itself). There is no default value.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link FHIRVersion}.
     */
    public FHIRVersion getFhirVersion() {
        return fhirVersion;
    }

    /**
     * <p>
     * A list of the formats supported by this implementation using their content types.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Code}.
     */
    public List<Code> getFormat() {
        return format;
    }

    /**
     * <p>
     * A list of the patch formats supported by this implementation using their content types.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Code}.
     */
    public List<Code> getPatchFormat() {
        return patchFormat;
    }

    /**
     * <p>
     * A list of implementation guides that the server does (or should) support in their entirety.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getImplementationGuide() {
        return implementationGuide;
    }

    /**
     * <p>
     * A definition of the restful capabilities of the solution, if any.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Rest}.
     */
    public List<Rest> getRest() {
        return rest;
    }

    /**
     * <p>
     * A description of the messaging capabilities of the solution.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Messaging}.
     */
    public List<Messaging> getMessaging() {
        return messaging;
    }

    /**
     * <p>
     * A document definition.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Document}.
     */
    public List<Document> getDocument() {
        return document;
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
                accept(contained, "contained", visitor, com.ibm.watsonhealth.fhir.model.resource.Resource.class);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(url, "url", visitor);
                accept(version, "version", visitor);
                accept(name, "name", visitor);
                accept(title, "title", visitor);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(purpose, "purpose", visitor);
                accept(copyright, "copyright", visitor);
                accept(kind, "kind", visitor);
                accept(instantiates, "instantiates", visitor, Canonical.class);
                accept(imports, "imports", visitor, Canonical.class);
                accept(software, "software", visitor);
                accept(implementation, "implementation", visitor);
                accept(fhirVersion, "fhirVersion", visitor);
                accept(format, "format", visitor, Code.class);
                accept(patchFormat, "patchFormat", visitor, Code.class);
                accept(implementationGuide, "implementationGuide", visitor, Canonical.class);
                accept(rest, "rest", visitor, Rest.class);
                accept(messaging, "messaging", visitor, Messaging.class);
                accept(document, "document", visitor, Document.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, date, kind, fhirVersion, format);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.url = url;
        builder.version = version;
        builder.name = name;
        builder.title = title;
        builder.experimental = experimental;
        builder.publisher = publisher;
        builder.contact.addAll(contact);
        builder.description = description;
        builder.useContext.addAll(useContext);
        builder.jurisdiction.addAll(jurisdiction);
        builder.purpose = purpose;
        builder.copyright = copyright;
        builder.instantiates.addAll(instantiates);
        builder.imports.addAll(imports);
        builder.software = software;
        builder.implementation = implementation;
        builder.patchFormat.addAll(patchFormat);
        builder.implementationGuide.addAll(implementationGuide);
        builder.rest.addAll(rest);
        builder.messaging.addAll(messaging);
        builder.document.addAll(document);
        return builder;
    }

    public static Builder builder(PublicationStatus status, DateTime date, CapabilityStatementKind kind, FHIRVersion fhirVersion, List<Code> format) {
        return new Builder(status, date, kind, fhirVersion, format);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final PublicationStatus status;
        private final DateTime date;
        private final CapabilityStatementKind kind;
        private final FHIRVersion fhirVersion;
        private final List<Code> format;

        // optional
        private Uri url;
        private String version;
        private String name;
        private String title;
        private Boolean experimental;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private Markdown copyright;
        private List<Canonical> instantiates = new ArrayList<>();
        private List<Canonical> imports = new ArrayList<>();
        private Software software;
        private Implementation implementation;
        private List<Code> patchFormat = new ArrayList<>();
        private List<Canonical> implementationGuide = new ArrayList<>();
        private List<Rest> rest = new ArrayList<>();
        private List<Messaging> messaging = new ArrayList<>();
        private List<Document> document = new ArrayList<>();

        private Builder(PublicationStatus status, DateTime date, CapabilityStatementKind kind, FHIRVersion fhirVersion, List<Code> format) {
            super();
            this.status = status;
            this.date = date;
            this.kind = kind;
            this.fhirVersion = fhirVersion;
            this.format = format;
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
        public Builder contained(com.ibm.watsonhealth.fhir.model.resource.Resource... contained) {
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
        public Builder contained(Collection<com.ibm.watsonhealth.fhir.model.resource.Resource> contained) {
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
         * An absolute URI that is used to identify this capability statement when it is referenced in a specification, model, 
         * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
         * address at which at which an authoritative instance of this capability statement is (or will be) published. This URL 
         * can be the target of a canonical reference. It SHALL remain the same when the capability statement is stored on 
         * different servers.
         * </p>
         * 
         * @param url
         *     Canonical identifier for this capability statement, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * <p>
         * The identifier that is used to identify this version of the capability statement when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the capability statement author and is 
         * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
         * available. There is also no expectation that versions can be placed in a lexicographical sequence.
         * </p>
         * 
         * @param version
         *     Business version of the capability statement
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * <p>
         * A natural language name identifying the capability statement. This name should be usable as an identifier for the 
         * module by machine processing applications such as code generation.
         * </p>
         * 
         * @param name
         *     Name for this capability statement (computer friendly)
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
         * A short, descriptive, user-friendly title for the capability statement.
         * </p>
         * 
         * @param title
         *     Name for this capability statement (human friendly)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * <p>
         * A Boolean value to indicate that this capability statement is authored for testing purposes (or 
         * education/evaluation/marketing) and is not intended to be used for genuine usage.
         * </p>
         * 
         * @param experimental
         *     For testing purposes, not real usage
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder experimental(Boolean experimental) {
            this.experimental = experimental;
            return this;
        }

        /**
         * <p>
         * The name of the organization or individual that published the capability statement.
         * </p>
         * 
         * @param publisher
         *     Name of the publisher (organization or individual)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        /**
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance.
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
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact.addAll(contact);
            return this;
        }

        /**
         * <p>
         * A free text natural language description of the capability statement from a consumer's perspective. Typically, this is 
         * used when the capability statement describes a desired rather than an actual solution, for example as a formal 
         * expression of requirements as part of an RFP.
         * </p>
         * 
         * @param description
         *     Natural language description of the capability statement
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate capability statement instances.
         * </p>
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance.
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
         * may be used to assist with indexing and searching for appropriate capability statement instances.
         * </p>
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder useContext(Collection<UsageContext> useContext) {
            this.useContext.addAll(useContext);
            return this;
        }

        /**
         * <p>
         * A legal or geographic region in which the capability statement is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for capability statement (if applicable)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder jurisdiction(CodeableConcept... jurisdiction) {
            for (CodeableConcept value : jurisdiction) {
                this.jurisdiction.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A legal or geographic region in which the capability statement is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for capability statement (if applicable)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction.addAll(jurisdiction);
            return this;
        }

        /**
         * <p>
         * Explanation of why this capability statement is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this capability statement is defined
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * <p>
         * A copyright statement relating to the capability statement and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the capability statement.
         * </p>
         * 
         * @param copyright
         *     Use and/or publishing restrictions
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder copyright(Markdown copyright) {
            this.copyright = copyright;
            return this;
        }

        /**
         * <p>
         * Reference to a canonical URL of another CapabilityStatement that this software implements. This capability statement 
         * is a published API description that corresponds to a business service. The server may actually implement a subset of 
         * the capability statement it claims to implement, so the capability statement must specify the full capability details.
         * </p>
         * 
         * @param instantiates
         *     Canonical URL of another capability statement this implements
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiates(Canonical... instantiates) {
            for (Canonical value : instantiates) {
                this.instantiates.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Reference to a canonical URL of another CapabilityStatement that this software implements. This capability statement 
         * is a published API description that corresponds to a business service. The server may actually implement a subset of 
         * the capability statement it claims to implement, so the capability statement must specify the full capability details.
         * </p>
         * 
         * @param instantiates
         *     Canonical URL of another capability statement this implements
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiates(Collection<Canonical> instantiates) {
            this.instantiates.addAll(instantiates);
            return this;
        }

        /**
         * <p>
         * Reference to a canonical URL of another CapabilityStatement that this software adds to. The capability statement 
         * automatically includes everything in the other statement, and it is not duplicated, though the server may repeat the 
         * same resources, interactions and operations to add additional details to them.
         * </p>
         * 
         * @param imports
         *     Canonical URL of another capability statement this adds to
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder imports(Canonical... imports) {
            for (Canonical value : imports) {
                this.imports.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Reference to a canonical URL of another CapabilityStatement that this software adds to. The capability statement 
         * automatically includes everything in the other statement, and it is not duplicated, though the server may repeat the 
         * same resources, interactions and operations to add additional details to them.
         * </p>
         * 
         * @param imports
         *     Canonical URL of another capability statement this adds to
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder imports(Collection<Canonical> imports) {
            this.imports.addAll(imports);
            return this;
        }

        /**
         * <p>
         * Software that is covered by this capability statement. It is used when the capability statement describes the 
         * capabilities of a particular software version, independent of an installation.
         * </p>
         * 
         * @param software
         *     Software that is covered by this capability statement
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder software(Software software) {
            this.software = software;
            return this;
        }

        /**
         * <p>
         * Identifies a specific implementation instance that is described by the capability statement - i.e. a particular 
         * installation, rather than the capabilities of a software program.
         * </p>
         * 
         * @param implementation
         *     If this describes a specific instance
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder implementation(Implementation implementation) {
            this.implementation = implementation;
            return this;
        }

        /**
         * <p>
         * A list of the patch formats supported by this implementation using their content types.
         * </p>
         * 
         * @param patchFormat
         *     Patch formats supported
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder patchFormat(Code... patchFormat) {
            for (Code value : patchFormat) {
                this.patchFormat.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A list of the patch formats supported by this implementation using their content types.
         * </p>
         * 
         * @param patchFormat
         *     Patch formats supported
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder patchFormat(Collection<Code> patchFormat) {
            this.patchFormat.addAll(patchFormat);
            return this;
        }

        /**
         * <p>
         * A list of implementation guides that the server does (or should) support in their entirety.
         * </p>
         * 
         * @param implementationGuide
         *     Implementation guides supported
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder implementationGuide(Canonical... implementationGuide) {
            for (Canonical value : implementationGuide) {
                this.implementationGuide.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A list of implementation guides that the server does (or should) support in their entirety.
         * </p>
         * 
         * @param implementationGuide
         *     Implementation guides supported
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder implementationGuide(Collection<Canonical> implementationGuide) {
            this.implementationGuide.addAll(implementationGuide);
            return this;
        }

        /**
         * <p>
         * A definition of the restful capabilities of the solution, if any.
         * </p>
         * 
         * @param rest
         *     If the endpoint is a RESTful one
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder rest(Rest... rest) {
            for (Rest value : rest) {
                this.rest.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A definition of the restful capabilities of the solution, if any.
         * </p>
         * 
         * @param rest
         *     If the endpoint is a RESTful one
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder rest(Collection<Rest> rest) {
            this.rest.addAll(rest);
            return this;
        }

        /**
         * <p>
         * A description of the messaging capabilities of the solution.
         * </p>
         * 
         * @param messaging
         *     If messaging is supported
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder messaging(Messaging... messaging) {
            for (Messaging value : messaging) {
                this.messaging.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A description of the messaging capabilities of the solution.
         * </p>
         * 
         * @param messaging
         *     If messaging is supported
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder messaging(Collection<Messaging> messaging) {
            this.messaging.addAll(messaging);
            return this;
        }

        /**
         * <p>
         * A document definition.
         * </p>
         * 
         * @param document
         *     Document definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder document(Document... document) {
            for (Document value : document) {
                this.document.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A document definition.
         * </p>
         * 
         * @param document
         *     Document definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder document(Collection<Document> document) {
            this.document.addAll(document);
            return this;
        }

        @Override
        public CapabilityStatement build() {
            return new CapabilityStatement(this);
        }
    }

    /**
     * <p>
     * Software that is covered by this capability statement. It is used when the capability statement describes the 
     * capabilities of a particular software version, independent of an installation.
     * </p>
     */
    public static class Software extends BackboneElement {
        private final String name;
        private final String version;
        private final DateTime releaseDate;

        private Software(Builder builder) {
            super(builder);
            this.name = ValidationSupport.requireNonNull(builder.name, "name");
            this.version = builder.version;
            this.releaseDate = builder.releaseDate;
        }

        /**
         * <p>
         * Name the software is known by.
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
         * The version identifier for the software covered by this statement.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getVersion() {
            return version;
        }

        /**
         * <p>
         * Date this version of the software was released.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getReleaseDate() {
            return releaseDate;
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
                    accept(name, "name", visitor);
                    accept(version, "version", visitor);
                    accept(releaseDate, "releaseDate", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(String name) {
            return new Builder(name);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String name;

            // optional
            private String version;
            private DateTime releaseDate;

            private Builder(String name) {
                super();
                this.name = name;
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
             * The version identifier for the software covered by this statement.
             * </p>
             * 
             * @param version
             *     Version covered by this statement
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder version(String version) {
                this.version = version;
                return this;
            }

            /**
             * <p>
             * Date this version of the software was released.
             * </p>
             * 
             * @param releaseDate
             *     Date this version was released
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder releaseDate(DateTime releaseDate) {
                this.releaseDate = releaseDate;
                return this;
            }

            @Override
            public Software build() {
                return new Software(this);
            }

            private static Builder from(Software software) {
                Builder builder = new Builder(software.name);
                builder.id = software.id;
                builder.extension.addAll(software.extension);
                builder.modifierExtension.addAll(software.modifierExtension);
                builder.version = software.version;
                builder.releaseDate = software.releaseDate;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Identifies a specific implementation instance that is described by the capability statement - i.e. a particular 
     * installation, rather than the capabilities of a software program.
     * </p>
     */
    public static class Implementation extends BackboneElement {
        private final String description;
        private final Url url;
        private final Reference custodian;

        private Implementation(Builder builder) {
            super(builder);
            this.description = ValidationSupport.requireNonNull(builder.description, "description");
            this.url = builder.url;
            this.custodian = builder.custodian;
        }

        /**
         * <p>
         * Information about the specific installation that this capability statement relates to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDescription() {
            return description;
        }

        /**
         * <p>
         * An absolute base URL for the implementation. This forms the base for REST interfaces as well as the mailbox and 
         * document interfaces.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Url}.
         */
        public Url getUrl() {
            return url;
        }

        /**
         * <p>
         * The organization responsible for the management of the instance and oversight of the data on the server at the 
         * specified URL.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getCustodian() {
            return custodian;
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
                    accept(description, "description", visitor);
                    accept(url, "url", visitor);
                    accept(custodian, "custodian", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(String description) {
            return new Builder(description);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String description;

            // optional
            private Url url;
            private Reference custodian;

            private Builder(String description) {
                super();
                this.description = description;
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
             * An absolute base URL for the implementation. This forms the base for REST interfaces as well as the mailbox and 
             * document interfaces.
             * </p>
             * 
             * @param url
             *     Base URL for the installation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder url(Url url) {
                this.url = url;
                return this;
            }

            /**
             * <p>
             * The organization responsible for the management of the instance and oversight of the data on the server at the 
             * specified URL.
             * </p>
             * 
             * @param custodian
             *     Organization that manages the data
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder custodian(Reference custodian) {
                this.custodian = custodian;
                return this;
            }

            @Override
            public Implementation build() {
                return new Implementation(this);
            }

            private static Builder from(Implementation implementation) {
                Builder builder = new Builder(implementation.description);
                builder.id = implementation.id;
                builder.extension.addAll(implementation.extension);
                builder.modifierExtension.addAll(implementation.modifierExtension);
                builder.url = implementation.url;
                builder.custodian = implementation.custodian;
                return builder;
            }
        }
    }

    /**
     * <p>
     * A definition of the restful capabilities of the solution, if any.
     * </p>
     */
    public static class Rest extends BackboneElement {
        private final RestfulCapabilityMode mode;
        private final Markdown documentation;
        private final Security security;
        private final List<Resource> resource;
        private final List<Interaction> interaction;
        private final List<CapabilityStatement.Rest.Resource.SearchParam> searchParam;
        private final List<CapabilityStatement.Rest.Resource.Operation> operation;
        private final List<Canonical> compartment;

        private Rest(Builder builder) {
            super(builder);
            this.mode = ValidationSupport.requireNonNull(builder.mode, "mode");
            this.documentation = builder.documentation;
            this.security = builder.security;
            this.resource = builder.resource;
            this.interaction = builder.interaction;
            this.searchParam = builder.searchParam;
            this.operation = builder.operation;
            this.compartment = builder.compartment;
        }

        /**
         * <p>
         * Identifies whether this portion of the statement is describing the ability to initiate or receive restful operations.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link RestfulCapabilityMode}.
         */
        public RestfulCapabilityMode getMode() {
            return mode;
        }

        /**
         * <p>
         * Information about the system's restful capabilities that apply across all applications, such as security.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Markdown}.
         */
        public Markdown getDocumentation() {
            return documentation;
        }

        /**
         * <p>
         * Information about security implementation from an interface perspective - what a client needs to know.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Security}.
         */
        public Security getSecurity() {
            return security;
        }

        /**
         * <p>
         * A specification of the restful capabilities of the solution for a specific resource type.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Resource}.
         */
        public List<Resource> getResource() {
            return resource;
        }

        /**
         * <p>
         * A specification of restful operations supported by the system.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Interaction}.
         */
        public List<Interaction> getInteraction() {
            return interaction;
        }

        /**
         * <p>
         * Search parameters that are supported for searching all resources for implementations to support and/or make use of - 
         * either references to ones defined in the specification, or additional ones defined for/by the implementation.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link SearchParam}.
         */
        public List<CapabilityStatement.Rest.Resource.SearchParam> getSearchParam() {
            return searchParam;
        }

        /**
         * <p>
         * Definition of an operation or a named query together with its parameters and their meaning and type.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Operation}.
         */
        public List<CapabilityStatement.Rest.Resource.Operation> getOperation() {
            return operation;
        }

        /**
         * <p>
         * An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to 
         * a CompartmentDefinition resource by its canonical URL .
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Canonical}.
         */
        public List<Canonical> getCompartment() {
            return compartment;
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
                    accept(mode, "mode", visitor);
                    accept(documentation, "documentation", visitor);
                    accept(security, "security", visitor);
                    accept(resource, "resource", visitor, Resource.class);
                    accept(interaction, "interaction", visitor, Interaction.class);
                    accept(searchParam, "searchParam", visitor, CapabilityStatement.Rest.Resource.SearchParam.class);
                    accept(operation, "operation", visitor, CapabilityStatement.Rest.Resource.Operation.class);
                    accept(compartment, "compartment", visitor, Canonical.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(RestfulCapabilityMode mode) {
            return new Builder(mode);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final RestfulCapabilityMode mode;

            // optional
            private Markdown documentation;
            private Security security;
            private List<Resource> resource = new ArrayList<>();
            private List<Interaction> interaction = new ArrayList<>();
            private List<CapabilityStatement.Rest.Resource.SearchParam> searchParam = new ArrayList<>();
            private List<CapabilityStatement.Rest.Resource.Operation> operation = new ArrayList<>();
            private List<Canonical> compartment = new ArrayList<>();

            private Builder(RestfulCapabilityMode mode) {
                super();
                this.mode = mode;
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
             * Information about the system's restful capabilities that apply across all applications, such as security.
             * </p>
             * 
             * @param documentation
             *     General description of implementation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder documentation(Markdown documentation) {
                this.documentation = documentation;
                return this;
            }

            /**
             * <p>
             * Information about security implementation from an interface perspective - what a client needs to know.
             * </p>
             * 
             * @param security
             *     Information about security of implementation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder security(Security security) {
                this.security = security;
                return this;
            }

            /**
             * <p>
             * A specification of the restful capabilities of the solution for a specific resource type.
             * </p>
             * 
             * @param resource
             *     Resource served on the REST interface
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder resource(Resource... resource) {
                for (Resource value : resource) {
                    this.resource.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A specification of the restful capabilities of the solution for a specific resource type.
             * </p>
             * 
             * @param resource
             *     Resource served on the REST interface
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder resource(Collection<Resource> resource) {
                this.resource.addAll(resource);
                return this;
            }

            /**
             * <p>
             * A specification of restful operations supported by the system.
             * </p>
             * 
             * @param interaction
             *     What operations are supported?
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder interaction(Interaction... interaction) {
                for (Interaction value : interaction) {
                    this.interaction.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A specification of restful operations supported by the system.
             * </p>
             * 
             * @param interaction
             *     What operations are supported?
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder interaction(Collection<Interaction> interaction) {
                this.interaction.addAll(interaction);
                return this;
            }

            /**
             * <p>
             * Search parameters that are supported for searching all resources for implementations to support and/or make use of - 
             * either references to ones defined in the specification, or additional ones defined for/by the implementation.
             * </p>
             * 
             * @param searchParam
             *     Search parameters for searching all resources
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder searchParam(CapabilityStatement.Rest.Resource.SearchParam... searchParam) {
                for (CapabilityStatement.Rest.Resource.SearchParam value : searchParam) {
                    this.searchParam.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Search parameters that are supported for searching all resources for implementations to support and/or make use of - 
             * either references to ones defined in the specification, or additional ones defined for/by the implementation.
             * </p>
             * 
             * @param searchParam
             *     Search parameters for searching all resources
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder searchParam(Collection<CapabilityStatement.Rest.Resource.SearchParam> searchParam) {
                this.searchParam.addAll(searchParam);
                return this;
            }

            /**
             * <p>
             * Definition of an operation or a named query together with its parameters and their meaning and type.
             * </p>
             * 
             * @param operation
             *     Definition of a system level operation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder operation(CapabilityStatement.Rest.Resource.Operation... operation) {
                for (CapabilityStatement.Rest.Resource.Operation value : operation) {
                    this.operation.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Definition of an operation or a named query together with its parameters and their meaning and type.
             * </p>
             * 
             * @param operation
             *     Definition of a system level operation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder operation(Collection<CapabilityStatement.Rest.Resource.Operation> operation) {
                this.operation.addAll(operation);
                return this;
            }

            /**
             * <p>
             * An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to 
             * a CompartmentDefinition resource by its canonical URL .
             * </p>
             * 
             * @param compartment
             *     Compartments served/used by system
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder compartment(Canonical... compartment) {
                for (Canonical value : compartment) {
                    this.compartment.add(value);
                }
                return this;
            }

            /**
             * <p>
             * An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to 
             * a CompartmentDefinition resource by its canonical URL .
             * </p>
             * 
             * @param compartment
             *     Compartments served/used by system
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder compartment(Collection<Canonical> compartment) {
                this.compartment.addAll(compartment);
                return this;
            }

            @Override
            public Rest build() {
                return new Rest(this);
            }

            private static Builder from(Rest rest) {
                Builder builder = new Builder(rest.mode);
                builder.id = rest.id;
                builder.extension.addAll(rest.extension);
                builder.modifierExtension.addAll(rest.modifierExtension);
                builder.documentation = rest.documentation;
                builder.security = rest.security;
                builder.resource.addAll(rest.resource);
                builder.interaction.addAll(rest.interaction);
                builder.searchParam.addAll(rest.searchParam);
                builder.operation.addAll(rest.operation);
                builder.compartment.addAll(rest.compartment);
                return builder;
            }
        }

        /**
         * <p>
         * Information about security implementation from an interface perspective - what a client needs to know.
         * </p>
         */
        public static class Security extends BackboneElement {
            private final Boolean cors;
            private final List<CodeableConcept> service;
            private final Markdown description;

            private Security(Builder builder) {
                super(builder);
                this.cors = builder.cors;
                this.service = builder.service;
                this.description = builder.description;
            }

            /**
             * <p>
             * Server adds CORS headers when responding to requests - this enables Javascript applications to use the server.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getCors() {
                return cors;
            }

            /**
             * <p>
             * Types of security services that are supported/required by the system.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link CodeableConcept}.
             */
            public List<CodeableConcept> getService() {
                return service;
            }

            /**
             * <p>
             * General description of how security works.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Markdown}.
             */
            public Markdown getDescription() {
                return description;
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
                        accept(cors, "cors", visitor);
                        accept(service, "service", visitor, CodeableConcept.class);
                        accept(description, "description", visitor);
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
                private Boolean cors;
                private List<CodeableConcept> service = new ArrayList<>();
                private Markdown description;

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
                 * Server adds CORS headers when responding to requests - this enables Javascript applications to use the server.
                 * </p>
                 * 
                 * @param cors
                 *     Adds CORS Headers (http://enable-cors.org/)
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder cors(Boolean cors) {
                    this.cors = cors;
                    return this;
                }

                /**
                 * <p>
                 * Types of security services that are supported/required by the system.
                 * </p>
                 * 
                 * @param service
                 *     OAuth | SMART-on-FHIR | NTLM | Basic | Kerberos | Certificates
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder service(CodeableConcept... service) {
                    for (CodeableConcept value : service) {
                        this.service.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Types of security services that are supported/required by the system.
                 * </p>
                 * 
                 * @param service
                 *     OAuth | SMART-on-FHIR | NTLM | Basic | Kerberos | Certificates
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder service(Collection<CodeableConcept> service) {
                    this.service.addAll(service);
                    return this;
                }

                /**
                 * <p>
                 * General description of how security works.
                 * </p>
                 * 
                 * @param description
                 *     General description of how security works
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder description(Markdown description) {
                    this.description = description;
                    return this;
                }

                @Override
                public Security build() {
                    return new Security(this);
                }

                private static Builder from(Security security) {
                    Builder builder = new Builder();
                    builder.id = security.id;
                    builder.extension.addAll(security.extension);
                    builder.modifierExtension.addAll(security.modifierExtension);
                    builder.cors = security.cors;
                    builder.service.addAll(security.service);
                    builder.description = security.description;
                    return builder;
                }
            }
        }

        /**
         * <p>
         * A specification of the restful capabilities of the solution for a specific resource type.
         * </p>
         */
        public static class Resource extends BackboneElement {
            private final ResourceType type;
            private final Canonical profile;
            private final List<Canonical> supportedProfile;
            private final Markdown documentation;
            private final List<Interaction> interaction;
            private final ResourceVersionPolicy versioning;
            private final Boolean readHistory;
            private final Boolean updateCreate;
            private final Boolean conditionalCreate;
            private final ConditionalReadStatus conditionalRead;
            private final Boolean conditionalUpdate;
            private final ConditionalDeleteStatus conditionalDelete;
            private final List<ReferenceHandlingPolicy> referencePolicy;
            private final List<String> searchInclude;
            private final List<String> searchRevInclude;
            private final List<SearchParam> searchParam;
            private final List<Operation> operation;

            private Resource(Builder builder) {
                super(builder);
                this.type = ValidationSupport.requireNonNull(builder.type, "type");
                this.profile = builder.profile;
                this.supportedProfile = builder.supportedProfile;
                this.documentation = builder.documentation;
                this.interaction = builder.interaction;
                this.versioning = builder.versioning;
                this.readHistory = builder.readHistory;
                this.updateCreate = builder.updateCreate;
                this.conditionalCreate = builder.conditionalCreate;
                this.conditionalRead = builder.conditionalRead;
                this.conditionalUpdate = builder.conditionalUpdate;
                this.conditionalDelete = builder.conditionalDelete;
                this.referencePolicy = builder.referencePolicy;
                this.searchInclude = builder.searchInclude;
                this.searchRevInclude = builder.searchRevInclude;
                this.searchParam = builder.searchParam;
                this.operation = builder.operation;
            }

            /**
             * <p>
             * A type of resource exposed via the restful interface.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ResourceType}.
             */
            public ResourceType getType() {
                return type;
            }

            /**
             * <p>
             * A specification of the profile that describes the solution's overall support for the resource, including any 
             * constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles]
             * (profiling.html#profile-uses).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Canonical}.
             */
            public Canonical getProfile() {
                return profile;
            }

            /**
             * <p>
             * A list of profiles that represent different use cases supported by the system. For a server, "supported by the system" 
             * means the system hosts/produces a set of resources that are conformant to a particular profile, and allows clients 
             * that use its services to search using this profile and to find appropriate data. For a client, it means the system 
             * will search by this profile and process data according to the guidance implicit in the profile. See further discussion 
             * in [Using Profiles](profiling.html#profile-uses).
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Canonical}.
             */
            public List<Canonical> getSupportedProfile() {
                return supportedProfile;
            }

            /**
             * <p>
             * Additional information about the resource type used by the system.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Markdown}.
             */
            public Markdown getDocumentation() {
                return documentation;
            }

            /**
             * <p>
             * Identifies a restful operation supported by the solution.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Interaction}.
             */
            public List<Interaction> getInteraction() {
                return interaction;
            }

            /**
             * <p>
             * This field is set to no-version to specify that the system does not support (server) or use (client) versioning for 
             * this resource type. If this has some other value, the server must at least correctly track and populate the versionId 
             * meta-property on resources. If the value is 'versioned-update', then the server supports all the versioning features, 
             * including using e-tags for version integrity in the API.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ResourceVersionPolicy}.
             */
            public ResourceVersionPolicy getVersioning() {
                return versioning;
            }

            /**
             * <p>
             * A flag for whether the server is able to return past versions as part of the vRead operation.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getReadHistory() {
                return readHistory;
            }

            /**
             * <p>
             * A flag to indicate that the server allows or needs to allow the client to create new identities on the server (that 
             * is, the client PUTs to a location where there is no existing resource). Allowing this operation means that the server 
             * allows the client to create new identities on the server.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getUpdateCreate() {
                return updateCreate;
            }

            /**
             * <p>
             * A flag that indicates that the server supports conditional create.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getConditionalCreate() {
                return conditionalCreate;
            }

            /**
             * <p>
             * A code that indicates how the server supports conditional read.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ConditionalReadStatus}.
             */
            public ConditionalReadStatus getConditionalRead() {
                return conditionalRead;
            }

            /**
             * <p>
             * A flag that indicates that the server supports conditional update.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getConditionalUpdate() {
                return conditionalUpdate;
            }

            /**
             * <p>
             * A code that indicates how the server supports conditional delete.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ConditionalDeleteStatus}.
             */
            public ConditionalDeleteStatus getConditionalDelete() {
                return conditionalDelete;
            }

            /**
             * <p>
             * A set of flags that defines how references are supported.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link ReferenceHandlingPolicy}.
             */
            public List<ReferenceHandlingPolicy> getReferencePolicy() {
                return referencePolicy;
            }

            /**
             * <p>
             * A list of _include values supported by the server.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link String}.
             */
            public List<String> getSearchInclude() {
                return searchInclude;
            }

            /**
             * <p>
             * A list of _revinclude (reverse include) values supported by the server.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link String}.
             */
            public List<String> getSearchRevInclude() {
                return searchRevInclude;
            }

            /**
             * <p>
             * Search parameters for implementations to support and/or make use of - either references to ones defined in the 
             * specification, or additional ones defined for/by the implementation.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link SearchParam}.
             */
            public List<SearchParam> getSearchParam() {
                return searchParam;
            }

            /**
             * <p>
             * Definition of an operation or a named query together with its parameters and their meaning and type. Consult the 
             * definition of the operation for details about how to invoke the operation, and the parameters.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Operation}.
             */
            public List<Operation> getOperation() {
                return operation;
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
                        accept(profile, "profile", visitor);
                        accept(supportedProfile, "supportedProfile", visitor, Canonical.class);
                        accept(documentation, "documentation", visitor);
                        accept(interaction, "interaction", visitor, Interaction.class);
                        accept(versioning, "versioning", visitor);
                        accept(readHistory, "readHistory", visitor);
                        accept(updateCreate, "updateCreate", visitor);
                        accept(conditionalCreate, "conditionalCreate", visitor);
                        accept(conditionalRead, "conditionalRead", visitor);
                        accept(conditionalUpdate, "conditionalUpdate", visitor);
                        accept(conditionalDelete, "conditionalDelete", visitor);
                        accept(referencePolicy, "referencePolicy", visitor, ReferenceHandlingPolicy.class);
                        accept(searchInclude, "searchInclude", visitor, String.class);
                        accept(searchRevInclude, "searchRevInclude", visitor, String.class);
                        accept(searchParam, "searchParam", visitor, SearchParam.class);
                        accept(operation, "operation", visitor, Operation.class);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(ResourceType type) {
                return new Builder(type);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final ResourceType type;

                // optional
                private Canonical profile;
                private List<Canonical> supportedProfile = new ArrayList<>();
                private Markdown documentation;
                private List<Interaction> interaction = new ArrayList<>();
                private ResourceVersionPolicy versioning;
                private Boolean readHistory;
                private Boolean updateCreate;
                private Boolean conditionalCreate;
                private ConditionalReadStatus conditionalRead;
                private Boolean conditionalUpdate;
                private ConditionalDeleteStatus conditionalDelete;
                private List<ReferenceHandlingPolicy> referencePolicy = new ArrayList<>();
                private List<String> searchInclude = new ArrayList<>();
                private List<String> searchRevInclude = new ArrayList<>();
                private List<SearchParam> searchParam = new ArrayList<>();
                private List<Operation> operation = new ArrayList<>();

                private Builder(ResourceType type) {
                    super();
                    this.type = type;
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
                 * A specification of the profile that describes the solution's overall support for the resource, including any 
                 * constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles]
                 * (profiling.html#profile-uses).
                 * </p>
                 * 
                 * @param profile
                 *     Base System profile for all uses of resource
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder profile(Canonical profile) {
                    this.profile = profile;
                    return this;
                }

                /**
                 * <p>
                 * A list of profiles that represent different use cases supported by the system. For a server, "supported by the system" 
                 * means the system hosts/produces a set of resources that are conformant to a particular profile, and allows clients 
                 * that use its services to search using this profile and to find appropriate data. For a client, it means the system 
                 * will search by this profile and process data according to the guidance implicit in the profile. See further discussion 
                 * in [Using Profiles](profiling.html#profile-uses).
                 * </p>
                 * 
                 * @param supportedProfile
                 *     Profiles for use cases supported
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder supportedProfile(Canonical... supportedProfile) {
                    for (Canonical value : supportedProfile) {
                        this.supportedProfile.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * A list of profiles that represent different use cases supported by the system. For a server, "supported by the system" 
                 * means the system hosts/produces a set of resources that are conformant to a particular profile, and allows clients 
                 * that use its services to search using this profile and to find appropriate data. For a client, it means the system 
                 * will search by this profile and process data according to the guidance implicit in the profile. See further discussion 
                 * in [Using Profiles](profiling.html#profile-uses).
                 * </p>
                 * 
                 * @param supportedProfile
                 *     Profiles for use cases supported
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder supportedProfile(Collection<Canonical> supportedProfile) {
                    this.supportedProfile.addAll(supportedProfile);
                    return this;
                }

                /**
                 * <p>
                 * Additional information about the resource type used by the system.
                 * </p>
                 * 
                 * @param documentation
                 *     Additional information about the use of the resource type
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder documentation(Markdown documentation) {
                    this.documentation = documentation;
                    return this;
                }

                /**
                 * <p>
                 * Identifies a restful operation supported by the solution.
                 * </p>
                 * 
                 * @param interaction
                 *     What operations are supported?
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder interaction(Interaction... interaction) {
                    for (Interaction value : interaction) {
                        this.interaction.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Identifies a restful operation supported by the solution.
                 * </p>
                 * 
                 * @param interaction
                 *     What operations are supported?
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder interaction(Collection<Interaction> interaction) {
                    this.interaction.addAll(interaction);
                    return this;
                }

                /**
                 * <p>
                 * This field is set to no-version to specify that the system does not support (server) or use (client) versioning for 
                 * this resource type. If this has some other value, the server must at least correctly track and populate the versionId 
                 * meta-property on resources. If the value is 'versioned-update', then the server supports all the versioning features, 
                 * including using e-tags for version integrity in the API.
                 * </p>
                 * 
                 * @param versioning
                 *     no-version | versioned | versioned-update
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder versioning(ResourceVersionPolicy versioning) {
                    this.versioning = versioning;
                    return this;
                }

                /**
                 * <p>
                 * A flag for whether the server is able to return past versions as part of the vRead operation.
                 * </p>
                 * 
                 * @param readHistory
                 *     Whether vRead can return past versions
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder readHistory(Boolean readHistory) {
                    this.readHistory = readHistory;
                    return this;
                }

                /**
                 * <p>
                 * A flag to indicate that the server allows or needs to allow the client to create new identities on the server (that 
                 * is, the client PUTs to a location where there is no existing resource). Allowing this operation means that the server 
                 * allows the client to create new identities on the server.
                 * </p>
                 * 
                 * @param updateCreate
                 *     If update can commit to a new identity
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder updateCreate(Boolean updateCreate) {
                    this.updateCreate = updateCreate;
                    return this;
                }

                /**
                 * <p>
                 * A flag that indicates that the server supports conditional create.
                 * </p>
                 * 
                 * @param conditionalCreate
                 *     If allows/uses conditional create
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder conditionalCreate(Boolean conditionalCreate) {
                    this.conditionalCreate = conditionalCreate;
                    return this;
                }

                /**
                 * <p>
                 * A code that indicates how the server supports conditional read.
                 * </p>
                 * 
                 * @param conditionalRead
                 *     not-supported | modified-since | not-match | full-support
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder conditionalRead(ConditionalReadStatus conditionalRead) {
                    this.conditionalRead = conditionalRead;
                    return this;
                }

                /**
                 * <p>
                 * A flag that indicates that the server supports conditional update.
                 * </p>
                 * 
                 * @param conditionalUpdate
                 *     If allows/uses conditional update
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder conditionalUpdate(Boolean conditionalUpdate) {
                    this.conditionalUpdate = conditionalUpdate;
                    return this;
                }

                /**
                 * <p>
                 * A code that indicates how the server supports conditional delete.
                 * </p>
                 * 
                 * @param conditionalDelete
                 *     not-supported | single | multiple - how conditional delete is supported
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder conditionalDelete(ConditionalDeleteStatus conditionalDelete) {
                    this.conditionalDelete = conditionalDelete;
                    return this;
                }

                /**
                 * <p>
                 * A set of flags that defines how references are supported.
                 * </p>
                 * 
                 * @param referencePolicy
                 *     literal | logical | resolves | enforced | local
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder referencePolicy(ReferenceHandlingPolicy... referencePolicy) {
                    for (ReferenceHandlingPolicy value : referencePolicy) {
                        this.referencePolicy.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * A set of flags that defines how references are supported.
                 * </p>
                 * 
                 * @param referencePolicy
                 *     literal | logical | resolves | enforced | local
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder referencePolicy(Collection<ReferenceHandlingPolicy> referencePolicy) {
                    this.referencePolicy.addAll(referencePolicy);
                    return this;
                }

                /**
                 * <p>
                 * A list of _include values supported by the server.
                 * </p>
                 * 
                 * @param searchInclude
                 *     _include values supported by the server
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder searchInclude(String... searchInclude) {
                    for (String value : searchInclude) {
                        this.searchInclude.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * A list of _include values supported by the server.
                 * </p>
                 * 
                 * @param searchInclude
                 *     _include values supported by the server
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder searchInclude(Collection<String> searchInclude) {
                    this.searchInclude.addAll(searchInclude);
                    return this;
                }

                /**
                 * <p>
                 * A list of _revinclude (reverse include) values supported by the server.
                 * </p>
                 * 
                 * @param searchRevInclude
                 *     _revinclude values supported by the server
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder searchRevInclude(String... searchRevInclude) {
                    for (String value : searchRevInclude) {
                        this.searchRevInclude.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * A list of _revinclude (reverse include) values supported by the server.
                 * </p>
                 * 
                 * @param searchRevInclude
                 *     _revinclude values supported by the server
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder searchRevInclude(Collection<String> searchRevInclude) {
                    this.searchRevInclude.addAll(searchRevInclude);
                    return this;
                }

                /**
                 * <p>
                 * Search parameters for implementations to support and/or make use of - either references to ones defined in the 
                 * specification, or additional ones defined for/by the implementation.
                 * </p>
                 * 
                 * @param searchParam
                 *     Search parameters supported by implementation
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder searchParam(SearchParam... searchParam) {
                    for (SearchParam value : searchParam) {
                        this.searchParam.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Search parameters for implementations to support and/or make use of - either references to ones defined in the 
                 * specification, or additional ones defined for/by the implementation.
                 * </p>
                 * 
                 * @param searchParam
                 *     Search parameters supported by implementation
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder searchParam(Collection<SearchParam> searchParam) {
                    this.searchParam.addAll(searchParam);
                    return this;
                }

                /**
                 * <p>
                 * Definition of an operation or a named query together with its parameters and their meaning and type. Consult the 
                 * definition of the operation for details about how to invoke the operation, and the parameters.
                 * </p>
                 * 
                 * @param operation
                 *     Definition of a resource operation
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder operation(Operation... operation) {
                    for (Operation value : operation) {
                        this.operation.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Definition of an operation or a named query together with its parameters and their meaning and type. Consult the 
                 * definition of the operation for details about how to invoke the operation, and the parameters.
                 * </p>
                 * 
                 * @param operation
                 *     Definition of a resource operation
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder operation(Collection<Operation> operation) {
                    this.operation.addAll(operation);
                    return this;
                }

                @Override
                public Resource build() {
                    return new Resource(this);
                }

                private static Builder from(Resource resource) {
                    Builder builder = new Builder(resource.type);
                    builder.id = resource.id;
                    builder.extension.addAll(resource.extension);
                    builder.modifierExtension.addAll(resource.modifierExtension);
                    builder.profile = resource.profile;
                    builder.supportedProfile.addAll(resource.supportedProfile);
                    builder.documentation = resource.documentation;
                    builder.interaction.addAll(resource.interaction);
                    builder.versioning = resource.versioning;
                    builder.readHistory = resource.readHistory;
                    builder.updateCreate = resource.updateCreate;
                    builder.conditionalCreate = resource.conditionalCreate;
                    builder.conditionalRead = resource.conditionalRead;
                    builder.conditionalUpdate = resource.conditionalUpdate;
                    builder.conditionalDelete = resource.conditionalDelete;
                    builder.referencePolicy.addAll(resource.referencePolicy);
                    builder.searchInclude.addAll(resource.searchInclude);
                    builder.searchRevInclude.addAll(resource.searchRevInclude);
                    builder.searchParam.addAll(resource.searchParam);
                    builder.operation.addAll(resource.operation);
                    return builder;
                }
            }

            /**
             * <p>
             * Identifies a restful operation supported by the solution.
             * </p>
             */
            public static class Interaction extends BackboneElement {
                private final TypeRestfulInteraction code;
                private final Markdown documentation;

                private Interaction(Builder builder) {
                    super(builder);
                    this.code = ValidationSupport.requireNonNull(builder.code, "code");
                    this.documentation = builder.documentation;
                }

                /**
                 * <p>
                 * Coded identifier of the operation, supported by the system resource.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link TypeRestfulInteraction}.
                 */
                public TypeRestfulInteraction getCode() {
                    return code;
                }

                /**
                 * <p>
                 * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only 
                 * allowed with version id' or 'creates permitted from pre-authorized certificates only'.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown}.
                 */
                public Markdown getDocumentation() {
                    return documentation;
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
                            accept(code, "code", visitor);
                            accept(documentation, "documentation", visitor);
                        }
                        visitor.visitEnd(elementName, this);
                        visitor.postVisit(this);
                    }
                }

                @Override
                public Builder toBuilder() {
                    return Builder.from(this);
                }

                public static Builder builder(TypeRestfulInteraction code) {
                    return new Builder(code);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final TypeRestfulInteraction code;

                    // optional
                    private Markdown documentation;

                    private Builder(TypeRestfulInteraction code) {
                        super();
                        this.code = code;
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
                     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only 
                     * allowed with version id' or 'creates permitted from pre-authorized certificates only'.
                     * </p>
                     * 
                     * @param documentation
                     *     Anything special about operation behavior
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder documentation(Markdown documentation) {
                        this.documentation = documentation;
                        return this;
                    }

                    @Override
                    public Interaction build() {
                        return new Interaction(this);
                    }

                    private static Builder from(Interaction interaction) {
                        Builder builder = new Builder(interaction.code);
                        builder.id = interaction.id;
                        builder.extension.addAll(interaction.extension);
                        builder.modifierExtension.addAll(interaction.modifierExtension);
                        builder.documentation = interaction.documentation;
                        return builder;
                    }
                }
            }

            /**
             * <p>
             * Search parameters for implementations to support and/or make use of - either references to ones defined in the 
             * specification, or additional ones defined for/by the implementation.
             * </p>
             */
            public static class SearchParam extends BackboneElement {
                private final String name;
                private final Canonical definition;
                private final SearchParamType type;
                private final Markdown documentation;

                private SearchParam(Builder builder) {
                    super(builder);
                    this.name = ValidationSupport.requireNonNull(builder.name, "name");
                    this.definition = builder.definition;
                    this.type = ValidationSupport.requireNonNull(builder.type, "type");
                    this.documentation = builder.documentation;
                }

                /**
                 * <p>
                 * The name of the search parameter used in the interface.
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
                 * An absolute URI that is a formal reference to where this parameter was first defined, so that a client can be 
                 * confident of the meaning of the search parameter (a reference to [SearchParameter.url](searchparameter-definitions.
                 * html#SearchParameter.url)). This element SHALL be populated if the search parameter refers to a SearchParameter 
                 * defined by the FHIR core specification or externally defined IGs.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Canonical}.
                 */
                public Canonical getDefinition() {
                    return definition;
                }

                /**
                 * <p>
                 * The type of value a search parameter refers to, and how the content is interpreted.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link SearchParamType}.
                 */
                public SearchParamType getType() {
                    return type;
                }

                /**
                 * <p>
                 * This allows documentation of any distinct behaviors about how the search parameter is used. For example, text matching 
                 * algorithms.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown}.
                 */
                public Markdown getDocumentation() {
                    return documentation;
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
                            accept(name, "name", visitor);
                            accept(definition, "definition", visitor);
                            accept(type, "type", visitor);
                            accept(documentation, "documentation", visitor);
                        }
                        visitor.visitEnd(elementName, this);
                        visitor.postVisit(this);
                    }
                }

                @Override
                public Builder toBuilder() {
                    return Builder.from(this);
                }

                public static Builder builder(String name, SearchParamType type) {
                    return new Builder(name, type);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final String name;
                    private final SearchParamType type;

                    // optional
                    private Canonical definition;
                    private Markdown documentation;

                    private Builder(String name, SearchParamType type) {
                        super();
                        this.name = name;
                        this.type = type;
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
                     * An absolute URI that is a formal reference to where this parameter was first defined, so that a client can be 
                     * confident of the meaning of the search parameter (a reference to [SearchParameter.url](searchparameter-definitions.
                     * html#SearchParameter.url)). This element SHALL be populated if the search parameter refers to a SearchParameter 
                     * defined by the FHIR core specification or externally defined IGs.
                     * </p>
                     * 
                     * @param definition
                     *     Source of definition for parameter
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder definition(Canonical definition) {
                        this.definition = definition;
                        return this;
                    }

                    /**
                     * <p>
                     * This allows documentation of any distinct behaviors about how the search parameter is used. For example, text matching 
                     * algorithms.
                     * </p>
                     * 
                     * @param documentation
                     *     Server-specific usage
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder documentation(Markdown documentation) {
                        this.documentation = documentation;
                        return this;
                    }

                    @Override
                    public SearchParam build() {
                        return new SearchParam(this);
                    }

                    private static Builder from(SearchParam searchParam) {
                        Builder builder = new Builder(searchParam.name, searchParam.type);
                        builder.id = searchParam.id;
                        builder.extension.addAll(searchParam.extension);
                        builder.modifierExtension.addAll(searchParam.modifierExtension);
                        builder.definition = searchParam.definition;
                        builder.documentation = searchParam.documentation;
                        return builder;
                    }
                }
            }

            /**
             * <p>
             * Definition of an operation or a named query together with its parameters and their meaning and type. Consult the 
             * definition of the operation for details about how to invoke the operation, and the parameters.
             * </p>
             */
            public static class Operation extends BackboneElement {
                private final String name;
                private final Canonical definition;
                private final Markdown documentation;

                private Operation(Builder builder) {
                    super(builder);
                    this.name = ValidationSupport.requireNonNull(builder.name, "name");
                    this.definition = ValidationSupport.requireNonNull(builder.definition, "definition");
                    this.documentation = builder.documentation;
                }

                /**
                 * <p>
                 * The name of the operation or query. For an operation, this is the name prefixed with $ and used in the URL. For a 
                 * query, this is the name used in the _query parameter when the query is called.
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
                 * Where the formal definition can be found. If a server references the base definition of an Operation (i.e. from the 
                 * specification itself such as ```http://hl7.org/fhir/OperationDefinition/ValueSet-expand```), that means it supports 
                 * the full capabilities of the operation - e.g. both GET and POST invocation. If it only supports a subset, it must 
                 * define its own custom [OperationDefinition](operationdefinition.html#) with a 'base' of the original 
                 * OperationDefinition. The custom definition would describe the specific subset of functionality supported.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Canonical}.
                 */
                public Canonical getDefinition() {
                    return definition;
                }

                /**
                 * <p>
                 * Documentation that describes anything special about the operation behavior, possibly detailing different behavior for 
                 * system, type and instance-level invocation of the operation.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown}.
                 */
                public Markdown getDocumentation() {
                    return documentation;
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
                            accept(name, "name", visitor);
                            accept(definition, "definition", visitor);
                            accept(documentation, "documentation", visitor);
                        }
                        visitor.visitEnd(elementName, this);
                        visitor.postVisit(this);
                    }
                }

                @Override
                public Builder toBuilder() {
                    return Builder.from(this);
                }

                public static Builder builder(String name, Canonical definition) {
                    return new Builder(name, definition);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final String name;
                    private final Canonical definition;

                    // optional
                    private Markdown documentation;

                    private Builder(String name, Canonical definition) {
                        super();
                        this.name = name;
                        this.definition = definition;
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
                     * Documentation that describes anything special about the operation behavior, possibly detailing different behavior for 
                     * system, type and instance-level invocation of the operation.
                     * </p>
                     * 
                     * @param documentation
                     *     Specific details about operation behavior
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder documentation(Markdown documentation) {
                        this.documentation = documentation;
                        return this;
                    }

                    @Override
                    public Operation build() {
                        return new Operation(this);
                    }

                    private static Builder from(Operation operation) {
                        Builder builder = new Builder(operation.name, operation.definition);
                        builder.id = operation.id;
                        builder.extension.addAll(operation.extension);
                        builder.modifierExtension.addAll(operation.modifierExtension);
                        builder.documentation = operation.documentation;
                        return builder;
                    }
                }
            }
        }

        /**
         * <p>
         * A specification of restful operations supported by the system.
         * </p>
         */
        public static class Interaction extends BackboneElement {
            private final SystemRestfulInteraction code;
            private final Markdown documentation;

            private Interaction(Builder builder) {
                super(builder);
                this.code = ValidationSupport.requireNonNull(builder.code, "code");
                this.documentation = builder.documentation;
            }

            /**
             * <p>
             * A coded identifier of the operation, supported by the system.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link SystemRestfulInteraction}.
             */
            public SystemRestfulInteraction getCode() {
                return code;
            }

            /**
             * <p>
             * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or 
             * information about system wide search is implemented.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Markdown}.
             */
            public Markdown getDocumentation() {
                return documentation;
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
                        accept(code, "code", visitor);
                        accept(documentation, "documentation", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(SystemRestfulInteraction code) {
                return new Builder(code);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final SystemRestfulInteraction code;

                // optional
                private Markdown documentation;

                private Builder(SystemRestfulInteraction code) {
                    super();
                    this.code = code;
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
                 * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or 
                 * information about system wide search is implemented.
                 * </p>
                 * 
                 * @param documentation
                 *     Anything special about operation behavior
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder documentation(Markdown documentation) {
                    this.documentation = documentation;
                    return this;
                }

                @Override
                public Interaction build() {
                    return new Interaction(this);
                }

                private static Builder from(Interaction interaction) {
                    Builder builder = new Builder(interaction.code);
                    builder.id = interaction.id;
                    builder.extension.addAll(interaction.extension);
                    builder.modifierExtension.addAll(interaction.modifierExtension);
                    builder.documentation = interaction.documentation;
                    return builder;
                }
            }
        }
    }

    /**
     * <p>
     * A description of the messaging capabilities of the solution.
     * </p>
     */
    public static class Messaging extends BackboneElement {
        private final List<Endpoint> endpoint;
        private final UnsignedInt reliableCache;
        private final Markdown documentation;
        private final List<SupportedMessage> supportedMessage;

        private Messaging(Builder builder) {
            super(builder);
            this.endpoint = builder.endpoint;
            this.reliableCache = builder.reliableCache;
            this.documentation = builder.documentation;
            this.supportedMessage = builder.supportedMessage;
        }

        /**
         * <p>
         * An endpoint (network accessible address) to which messages and/or replies are to be sent.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Endpoint}.
         */
        public List<Endpoint> getEndpoint() {
            return endpoint;
        }

        /**
         * <p>
         * Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the 
         * receiver should be (if a sender).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link UnsignedInt}.
         */
        public UnsignedInt getReliableCache() {
            return reliableCache;
        }

        /**
         * <p>
         * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the capability 
         * statement. For example, the process for becoming an authorized messaging exchange partner.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Markdown}.
         */
        public Markdown getDocumentation() {
            return documentation;
        }

        /**
         * <p>
         * References to message definitions for messages this system can send or receive.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link SupportedMessage}.
         */
        public List<SupportedMessage> getSupportedMessage() {
            return supportedMessage;
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
                    accept(endpoint, "endpoint", visitor, Endpoint.class);
                    accept(reliableCache, "reliableCache", visitor);
                    accept(documentation, "documentation", visitor);
                    accept(supportedMessage, "supportedMessage", visitor, SupportedMessage.class);
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
            private List<Endpoint> endpoint = new ArrayList<>();
            private UnsignedInt reliableCache;
            private Markdown documentation;
            private List<SupportedMessage> supportedMessage = new ArrayList<>();

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
             * An endpoint (network accessible address) to which messages and/or replies are to be sent.
             * </p>
             * 
             * @param endpoint
             *     Where messages should be sent
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder endpoint(Endpoint... endpoint) {
                for (Endpoint value : endpoint) {
                    this.endpoint.add(value);
                }
                return this;
            }

            /**
             * <p>
             * An endpoint (network accessible address) to which messages and/or replies are to be sent.
             * </p>
             * 
             * @param endpoint
             *     Where messages should be sent
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder endpoint(Collection<Endpoint> endpoint) {
                this.endpoint.addAll(endpoint);
                return this;
            }

            /**
             * <p>
             * Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the 
             * receiver should be (if a sender).
             * </p>
             * 
             * @param reliableCache
             *     Reliable Message Cache Length (min)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder reliableCache(UnsignedInt reliableCache) {
                this.reliableCache = reliableCache;
                return this;
            }

            /**
             * <p>
             * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the capability 
             * statement. For example, the process for becoming an authorized messaging exchange partner.
             * </p>
             * 
             * @param documentation
             *     Messaging interface behavior details
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder documentation(Markdown documentation) {
                this.documentation = documentation;
                return this;
            }

            /**
             * <p>
             * References to message definitions for messages this system can send or receive.
             * </p>
             * 
             * @param supportedMessage
             *     Messages supported by this system
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder supportedMessage(SupportedMessage... supportedMessage) {
                for (SupportedMessage value : supportedMessage) {
                    this.supportedMessage.add(value);
                }
                return this;
            }

            /**
             * <p>
             * References to message definitions for messages this system can send or receive.
             * </p>
             * 
             * @param supportedMessage
             *     Messages supported by this system
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder supportedMessage(Collection<SupportedMessage> supportedMessage) {
                this.supportedMessage.addAll(supportedMessage);
                return this;
            }

            @Override
            public Messaging build() {
                return new Messaging(this);
            }

            private static Builder from(Messaging messaging) {
                Builder builder = new Builder();
                builder.id = messaging.id;
                builder.extension.addAll(messaging.extension);
                builder.modifierExtension.addAll(messaging.modifierExtension);
                builder.endpoint.addAll(messaging.endpoint);
                builder.reliableCache = messaging.reliableCache;
                builder.documentation = messaging.documentation;
                builder.supportedMessage.addAll(messaging.supportedMessage);
                return builder;
            }
        }

        /**
         * <p>
         * An endpoint (network accessible address) to which messages and/or replies are to be sent.
         * </p>
         */
        public static class Endpoint extends BackboneElement {
            private final Coding protocol;
            private final Url address;

            private Endpoint(Builder builder) {
                super(builder);
                this.protocol = ValidationSupport.requireNonNull(builder.protocol, "protocol");
                this.address = ValidationSupport.requireNonNull(builder.address, "address");
            }

            /**
             * <p>
             * A list of the messaging transport protocol(s) identifiers, supported by this endpoint.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Coding}.
             */
            public Coding getProtocol() {
                return protocol;
            }

            /**
             * <p>
             * The network address of the endpoint. For solutions that do not use network addresses for routing, it can be just an 
             * identifier.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Url}.
             */
            public Url getAddress() {
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
                        accept(protocol, "protocol", visitor);
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

            public static Builder builder(Coding protocol, Url address) {
                return new Builder(protocol, address);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Coding protocol;
                private final Url address;

                private Builder(Coding protocol, Url address) {
                    super();
                    this.protocol = protocol;
                    this.address = address;
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

                @Override
                public Endpoint build() {
                    return new Endpoint(this);
                }

                private static Builder from(Endpoint endpoint) {
                    Builder builder = new Builder(endpoint.protocol, endpoint.address);
                    builder.id = endpoint.id;
                    builder.extension.addAll(endpoint.extension);
                    builder.modifierExtension.addAll(endpoint.modifierExtension);
                    return builder;
                }
            }
        }

        /**
         * <p>
         * References to message definitions for messages this system can send or receive.
         * </p>
         */
        public static class SupportedMessage extends BackboneElement {
            private final EventCapabilityMode mode;
            private final Canonical definition;

            private SupportedMessage(Builder builder) {
                super(builder);
                this.mode = ValidationSupport.requireNonNull(builder.mode, "mode");
                this.definition = ValidationSupport.requireNonNull(builder.definition, "definition");
            }

            /**
             * <p>
             * The mode of this event declaration - whether application is sender or receiver.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link EventCapabilityMode}.
             */
            public EventCapabilityMode getMode() {
                return mode;
            }

            /**
             * <p>
             * Points to a message definition that identifies the messaging event, message structure, allowed responses, etc.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Canonical}.
             */
            public Canonical getDefinition() {
                return definition;
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
                        accept(mode, "mode", visitor);
                        accept(definition, "definition", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(EventCapabilityMode mode, Canonical definition) {
                return new Builder(mode, definition);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final EventCapabilityMode mode;
                private final Canonical definition;

                private Builder(EventCapabilityMode mode, Canonical definition) {
                    super();
                    this.mode = mode;
                    this.definition = definition;
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

                @Override
                public SupportedMessage build() {
                    return new SupportedMessage(this);
                }

                private static Builder from(SupportedMessage supportedMessage) {
                    Builder builder = new Builder(supportedMessage.mode, supportedMessage.definition);
                    builder.id = supportedMessage.id;
                    builder.extension.addAll(supportedMessage.extension);
                    builder.modifierExtension.addAll(supportedMessage.modifierExtension);
                    return builder;
                }
            }
        }
    }

    /**
     * <p>
     * A document definition.
     * </p>
     */
    public static class Document extends BackboneElement {
        private final DocumentMode mode;
        private final Markdown documentation;
        private final Canonical profile;

        private Document(Builder builder) {
            super(builder);
            this.mode = ValidationSupport.requireNonNull(builder.mode, "mode");
            this.documentation = builder.documentation;
            this.profile = ValidationSupport.requireNonNull(builder.profile, "profile");
        }

        /**
         * <p>
         * Mode of this document declaration - whether an application is a producer or consumer.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DocumentMode}.
         */
        public DocumentMode getMode() {
            return mode;
        }

        /**
         * <p>
         * A description of how the application supports or uses the specified document profile. For example, when documents are 
         * created, what action is taken with consumed documents, etc.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Markdown}.
         */
        public Markdown getDocumentation() {
            return documentation;
        }

        /**
         * <p>
         * A profile on the document Bundle that constrains which resources are present, and their contents.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getProfile() {
            return profile;
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
                    accept(mode, "mode", visitor);
                    accept(documentation, "documentation", visitor);
                    accept(profile, "profile", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(DocumentMode mode, Canonical profile) {
            return new Builder(mode, profile);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final DocumentMode mode;
            private final Canonical profile;

            // optional
            private Markdown documentation;

            private Builder(DocumentMode mode, Canonical profile) {
                super();
                this.mode = mode;
                this.profile = profile;
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
             * A description of how the application supports or uses the specified document profile. For example, when documents are 
             * created, what action is taken with consumed documents, etc.
             * </p>
             * 
             * @param documentation
             *     Description of document support
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder documentation(Markdown documentation) {
                this.documentation = documentation;
                return this;
            }

            @Override
            public Document build() {
                return new Document(this);
            }

            private static Builder from(Document document) {
                Builder builder = new Builder(document.mode, document.profile);
                builder.id = document.id;
                builder.extension.addAll(document.extension);
                builder.modifierExtension.addAll(document.modifierExtension);
                builder.documentation = document.documentation;
                return builder;
            }
        }
    }
}
