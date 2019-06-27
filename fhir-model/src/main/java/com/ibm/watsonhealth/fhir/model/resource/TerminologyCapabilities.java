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
import com.ibm.watsonhealth.fhir.model.type.CodeSearchSupport;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Url;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A TerminologyCapabilities resource documents a set of capabilities (behaviors) of a FHIR Terminology Server that may 
 * be used as a statement of actual server functionality or a statement of required or desired server implementation.
 * </p>
 */
@Constraint(
    key = "tcp-0",
    severity = "warning",
    human = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Constraint(
    key = "tcp-3",
    severity = "error",
    human = "If kind = instance, implementation must be present and software may be present",
    expression = "(kind != 'instance') or implementation.exists()"
)
@Constraint(
    key = "tcp-2",
    severity = "error",
    human = "A Capability Statement SHALL have at least one of description, software, or implementation element.",
    expression = "(description.count() + software.count() + implementation.count()) > 0"
)
@Constraint(
    key = "tcp-5",
    severity = "error",
    human = "If kind = requirements, implementation and software must be absent",
    expression = "(kind!='requirements') or (implementation.exists().not() and software.exists().not())"
)
@Constraint(
    key = "tcp-4",
    severity = "error",
    human = "If kind = capability, implementation must be absent, software must be present",
    expression = "(kind != 'capability') or (implementation.exists().not() and software.exists())"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class TerminologyCapabilities extends DomainResource {
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
    private final Software software;
    private final Implementation implementation;
    private final Boolean lockedDate;
    private final List<CodeSystem> codeSystem;
    private final Expansion expansion;
    private final CodeSearchSupport codeSearch;
    private final ValidateCode validateCode;
    private final Translation translation;
    private final Closure closure;

    private TerminologyCapabilities(Builder builder) {
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
        this.software = builder.software;
        this.implementation = builder.implementation;
        this.lockedDate = builder.lockedDate;
        this.codeSystem = builder.codeSystem;
        this.expansion = builder.expansion;
        this.codeSearch = builder.codeSearch;
        this.validateCode = builder.validateCode;
        this.translation = builder.translation;
        this.closure = builder.closure;
    }

    /**
     * <p>
     * An absolute URI that is used to identify this terminology capabilities when it is referenced in a specification, 
     * model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a 
     * literal address at which at which an authoritative instance of this terminology capabilities is (or will be) 
     * published. This URL can be the target of a canonical reference. It SHALL remain the same when the terminology 
     * capabilities is stored on different servers.
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
     * The identifier that is used to identify this version of the terminology capabilities when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the terminology capabilities author 
     * and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version 
     * is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
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
     * A natural language name identifying the terminology capabilities. This name should be usable as an identifier for the 
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
     * A short, descriptive, user-friendly title for the terminology capabilities.
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
     * The status of this terminology capabilities. Enables tracking the life-cycle of the content.
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
     * A Boolean value to indicate that this terminology capabilities is authored for testing purposes (or 
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
     * The date (and optionally time) when the terminology capabilities was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the terminology capabilities changes.
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
     * The name of the organization or individual that published the terminology capabilities.
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
     * A free text natural language description of the terminology capabilities from a consumer's perspective. Typically, 
     * this is used when the capability statement describes a desired rather than an actual solution, for example as a formal 
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
     * may be used to assist with indexing and searching for appropriate terminology capabilities instances.
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
     * A legal or geographic region in which the terminology capabilities is intended to be used.
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
     * Explanation of why this terminology capabilities is needed and why it has been designed as it has.
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
     * A copyright statement relating to the terminology capabilities and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the terminology capabilities.
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
     * Software that is covered by this terminology capability statement. It is used when the statement describes the 
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
     * Identifies a specific implementation instance that is described by the terminology capability statement - i.e. a 
     * particular installation, rather than the capabilities of a software program.
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
     * Whether the server supports lockedDate.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getLockedDate() {
        return lockedDate;
    }

    /**
     * <p>
     * Identifies a code system that is supported by the server. If there is a no code system URL, then this declares the 
     * general assumptions a client can make about support for any CodeSystem resource.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeSystem}.
     */
    public List<CodeSystem> getCodeSystem() {
        return codeSystem;
    }

    /**
     * <p>
     * Information about the [ValueSet/$expand](valueset-operation-expand.html) operation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Expansion}.
     */
    public Expansion getExpansion() {
        return expansion;
    }

    /**
     * <p>
     * The degree to which the server supports the code search parameter on ValueSet, if it is supported.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeSearchSupport}.
     */
    public CodeSearchSupport getCodeSearch() {
        return codeSearch;
    }

    /**
     * <p>
     * Information about the [ValueSet/$validate-code](valueset-operation-validate-code.html) operation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ValidateCode}.
     */
    public ValidateCode getValidateCode() {
        return validateCode;
    }

    /**
     * <p>
     * Information about the [ConceptMap/$translate](conceptmap-operation-translate.html) operation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Translation}.
     */
    public Translation getTranslation() {
        return translation;
    }

    /**
     * <p>
     * Whether the $closure operation is supported.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Closure}.
     */
    public Closure getClosure() {
        return closure;
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
                accept(software, "software", visitor);
                accept(implementation, "implementation", visitor);
                accept(lockedDate, "lockedDate", visitor);
                accept(codeSystem, "codeSystem", visitor, CodeSystem.class);
                accept(expansion, "expansion", visitor);
                accept(codeSearch, "codeSearch", visitor);
                accept(validateCode, "validateCode", visitor);
                accept(translation, "translation", visitor);
                accept(closure, "closure", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, date, kind);
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
        builder.software = software;
        builder.implementation = implementation;
        builder.lockedDate = lockedDate;
        builder.codeSystem.addAll(codeSystem);
        builder.expansion = expansion;
        builder.codeSearch = codeSearch;
        builder.validateCode = validateCode;
        builder.translation = translation;
        builder.closure = closure;
        return builder;
    }

    public static Builder builder(PublicationStatus status, DateTime date, CapabilityStatementKind kind) {
        return new Builder(status, date, kind);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final PublicationStatus status;
        private final DateTime date;
        private final CapabilityStatementKind kind;

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
        private Software software;
        private Implementation implementation;
        private Boolean lockedDate;
        private List<CodeSystem> codeSystem = new ArrayList<>();
        private Expansion expansion;
        private CodeSearchSupport codeSearch;
        private ValidateCode validateCode;
        private Translation translation;
        private Closure closure;

        private Builder(PublicationStatus status, DateTime date, CapabilityStatementKind kind) {
            super();
            this.status = status;
            this.date = date;
            this.kind = kind;
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
         * An absolute URI that is used to identify this terminology capabilities when it is referenced in a specification, 
         * model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a 
         * literal address at which at which an authoritative instance of this terminology capabilities is (or will be) 
         * published. This URL can be the target of a canonical reference. It SHALL remain the same when the terminology 
         * capabilities is stored on different servers.
         * </p>
         * 
         * @param url
         *     Canonical identifier for this terminology capabilities, represented as a URI (globally unique)
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
         * The identifier that is used to identify this version of the terminology capabilities when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the terminology capabilities author 
         * and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version 
         * is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
         * </p>
         * 
         * @param version
         *     Business version of the terminology capabilities
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
         * A natural language name identifying the terminology capabilities. This name should be usable as an identifier for the 
         * module by machine processing applications such as code generation.
         * </p>
         * 
         * @param name
         *     Name for this terminology capabilities (computer friendly)
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
         * A short, descriptive, user-friendly title for the terminology capabilities.
         * </p>
         * 
         * @param title
         *     Name for this terminology capabilities (human friendly)
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
         * A Boolean value to indicate that this terminology capabilities is authored for testing purposes (or 
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
         * The name of the organization or individual that published the terminology capabilities.
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
         * A free text natural language description of the terminology capabilities from a consumer's perspective. Typically, 
         * this is used when the capability statement describes a desired rather than an actual solution, for example as a formal 
         * expression of requirements as part of an RFP.
         * </p>
         * 
         * @param description
         *     Natural language description of the terminology capabilities
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
         * may be used to assist with indexing and searching for appropriate terminology capabilities instances.
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
         * may be used to assist with indexing and searching for appropriate terminology capabilities instances.
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
         * A legal or geographic region in which the terminology capabilities is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for terminology capabilities (if applicable)
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
         * A legal or geographic region in which the terminology capabilities is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for terminology capabilities (if applicable)
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
         * Explanation of why this terminology capabilities is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this terminology capabilities is defined
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
         * A copyright statement relating to the terminology capabilities and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the terminology capabilities.
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
         * Software that is covered by this terminology capability statement. It is used when the statement describes the 
         * capabilities of a particular software version, independent of an installation.
         * </p>
         * 
         * @param software
         *     Software that is covered by this terminology capability statement
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
         * Identifies a specific implementation instance that is described by the terminology capability statement - i.e. a 
         * particular installation, rather than the capabilities of a software program.
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
         * Whether the server supports lockedDate.
         * </p>
         * 
         * @param lockedDate
         *     Whether lockedDate is supported
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder lockedDate(Boolean lockedDate) {
            this.lockedDate = lockedDate;
            return this;
        }

        /**
         * <p>
         * Identifies a code system that is supported by the server. If there is a no code system URL, then this declares the 
         * general assumptions a client can make about support for any CodeSystem resource.
         * </p>
         * 
         * @param codeSystem
         *     A code system supported by the server
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder codeSystem(CodeSystem... codeSystem) {
            for (CodeSystem value : codeSystem) {
                this.codeSystem.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies a code system that is supported by the server. If there is a no code system URL, then this declares the 
         * general assumptions a client can make about support for any CodeSystem resource.
         * </p>
         * 
         * @param codeSystem
         *     A code system supported by the server
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder codeSystem(Collection<CodeSystem> codeSystem) {
            this.codeSystem.addAll(codeSystem);
            return this;
        }

        /**
         * <p>
         * Information about the [ValueSet/$expand](valueset-operation-expand.html) operation.
         * </p>
         * 
         * @param expansion
         *     Information about the [ValueSet/$expand](valueset-operation-expand.html) operation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder expansion(Expansion expansion) {
            this.expansion = expansion;
            return this;
        }

        /**
         * <p>
         * The degree to which the server supports the code search parameter on ValueSet, if it is supported.
         * </p>
         * 
         * @param codeSearch
         *     explicit | all
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder codeSearch(CodeSearchSupport codeSearch) {
            this.codeSearch = codeSearch;
            return this;
        }

        /**
         * <p>
         * Information about the [ValueSet/$validate-code](valueset-operation-validate-code.html) operation.
         * </p>
         * 
         * @param validateCode
         *     Information about the [ValueSet/$validate-code](valueset-operation-validate-code.html) operation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder validateCode(ValidateCode validateCode) {
            this.validateCode = validateCode;
            return this;
        }

        /**
         * <p>
         * Information about the [ConceptMap/$translate](conceptmap-operation-translate.html) operation.
         * </p>
         * 
         * @param translation
         *     Information about the [ConceptMap/$translate](conceptmap-operation-translate.html) operation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder translation(Translation translation) {
            this.translation = translation;
            return this;
        }

        /**
         * <p>
         * Whether the $closure operation is supported.
         * </p>
         * 
         * @param closure
         *     Information about the [ConceptMap/$closure](conceptmap-operation-closure.html) operation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder closure(Closure closure) {
            this.closure = closure;
            return this;
        }

        @Override
        public TerminologyCapabilities build() {
            return new TerminologyCapabilities(this);
        }
    }

    /**
     * <p>
     * Software that is covered by this terminology capability statement. It is used when the statement describes the 
     * capabilities of a particular software version, independent of an installation.
     * </p>
     */
    public static class Software extends BackboneElement {
        private final String name;
        private final String version;

        private Software(Builder builder) {
            super(builder);
            this.name = ValidationSupport.requireNonNull(builder.name, "name");
            this.version = builder.version;
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
                return builder;
            }
        }
    }

    /**
     * <p>
     * Identifies a specific implementation instance that is described by the terminology capability statement - i.e. a 
     * particular installation, rather than the capabilities of a software program.
     * </p>
     */
    public static class Implementation extends BackboneElement {
        private final String description;
        private final Url url;

        private Implementation(Builder builder) {
            super(builder);
            this.description = ValidationSupport.requireNonNull(builder.description, "description");
            this.url = builder.url;
        }

        /**
         * <p>
         * Information about the specific installation that this terminology capability statement relates to.
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
         * An absolute base URL for the implementation.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Url}.
         */
        public Url getUrl() {
            return url;
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
             * An absolute base URL for the implementation.
             * </p>
             * 
             * @param url
             *     Base URL for the implementation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder url(Url url) {
                this.url = url;
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
                return builder;
            }
        }
    }

    /**
     * <p>
     * Identifies a code system that is supported by the server. If there is a no code system URL, then this declares the 
     * general assumptions a client can make about support for any CodeSystem resource.
     * </p>
     */
    public static class CodeSystem extends BackboneElement {
        private final Canonical uri;
        private final List<Version> version;
        private final Boolean subsumption;

        private CodeSystem(Builder builder) {
            super(builder);
            this.uri = builder.uri;
            this.version = builder.version;
            this.subsumption = builder.subsumption;
        }

        /**
         * <p>
         * URI for the Code System.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getUri() {
            return uri;
        }

        /**
         * <p>
         * For the code system, a list of versions that are supported by the server.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Version}.
         */
        public List<Version> getVersion() {
            return version;
        }

        /**
         * <p>
         * True if subsumption is supported for this version of the code system.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getSubsumption() {
            return subsumption;
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
                    accept(uri, "uri", visitor);
                    accept(version, "version", visitor, Version.class);
                    accept(subsumption, "subsumption", visitor);
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
            private Canonical uri;
            private List<Version> version = new ArrayList<>();
            private Boolean subsumption;

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
             * URI for the Code System.
             * </p>
             * 
             * @param uri
             *     URI for the Code System
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder uri(Canonical uri) {
                this.uri = uri;
                return this;
            }

            /**
             * <p>
             * For the code system, a list of versions that are supported by the server.
             * </p>
             * 
             * @param version
             *     Version of Code System supported
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder version(Version... version) {
                for (Version value : version) {
                    this.version.add(value);
                }
                return this;
            }

            /**
             * <p>
             * For the code system, a list of versions that are supported by the server.
             * </p>
             * 
             * @param version
             *     Version of Code System supported
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder version(Collection<Version> version) {
                this.version.addAll(version);
                return this;
            }

            /**
             * <p>
             * True if subsumption is supported for this version of the code system.
             * </p>
             * 
             * @param subsumption
             *     Whether subsumption is supported
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder subsumption(Boolean subsumption) {
                this.subsumption = subsumption;
                return this;
            }

            @Override
            public CodeSystem build() {
                return new CodeSystem(this);
            }

            private static Builder from(CodeSystem codeSystem) {
                Builder builder = new Builder();
                builder.id = codeSystem.id;
                builder.extension.addAll(codeSystem.extension);
                builder.modifierExtension.addAll(codeSystem.modifierExtension);
                builder.uri = codeSystem.uri;
                builder.version.addAll(codeSystem.version);
                builder.subsumption = codeSystem.subsumption;
                return builder;
            }
        }

        /**
         * <p>
         * For the code system, a list of versions that are supported by the server.
         * </p>
         */
        public static class Version extends BackboneElement {
            private final String code;
            private final Boolean isDefault;
            private final Boolean compositional;
            private final List<Code> language;
            private final List<Filter> filter;
            private final List<Code> property;

            private Version(Builder builder) {
                super(builder);
                this.code = builder.code;
                this.isDefault = builder.isDefault;
                this.compositional = builder.compositional;
                this.language = builder.language;
                this.filter = builder.filter;
                this.property = builder.property;
            }

            /**
             * <p>
             * For version-less code systems, there should be a single version with no identifier.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getCode() {
                return code;
            }

            /**
             * <p>
             * If this is the default version for this code system.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getIsDefault() {
                return isDefault;
            }

            /**
             * <p>
             * If the compositional grammar defined by the code system is supported.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getCompositional() {
                return compositional;
            }

            /**
             * <p>
             * Language Displays supported.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Code}.
             */
            public List<Code> getLanguage() {
                return language;
            }

            /**
             * <p>
             * Filter Properties supported.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Filter}.
             */
            public List<Filter> getFilter() {
                return filter;
            }

            /**
             * <p>
             * Properties supported for $lookup.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Code}.
             */
            public List<Code> getProperty() {
                return property;
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
                        accept(isDefault, "isDefault", visitor);
                        accept(compositional, "compositional", visitor);
                        accept(language, "language", visitor, Code.class);
                        accept(filter, "filter", visitor, Filter.class);
                        accept(property, "property", visitor, Code.class);
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
                private String code;
                private Boolean isDefault;
                private Boolean compositional;
                private List<Code> language = new ArrayList<>();
                private List<Filter> filter = new ArrayList<>();
                private List<Code> property = new ArrayList<>();

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
                 * For version-less code systems, there should be a single version with no identifier.
                 * </p>
                 * 
                 * @param code
                 *     Version identifier for this version
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder code(String code) {
                    this.code = code;
                    return this;
                }

                /**
                 * <p>
                 * If this is the default version for this code system.
                 * </p>
                 * 
                 * @param isDefault
                 *     If this is the default version for this code system
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder isDefault(Boolean isDefault) {
                    this.isDefault = isDefault;
                    return this;
                }

                /**
                 * <p>
                 * If the compositional grammar defined by the code system is supported.
                 * </p>
                 * 
                 * @param compositional
                 *     If compositional grammar is supported
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder compositional(Boolean compositional) {
                    this.compositional = compositional;
                    return this;
                }

                /**
                 * <p>
                 * Language Displays supported.
                 * </p>
                 * 
                 * @param language
                 *     Language Displays supported
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder language(Code... language) {
                    for (Code value : language) {
                        this.language.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Language Displays supported.
                 * </p>
                 * 
                 * @param language
                 *     Language Displays supported
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder language(Collection<Code> language) {
                    this.language.addAll(language);
                    return this;
                }

                /**
                 * <p>
                 * Filter Properties supported.
                 * </p>
                 * 
                 * @param filter
                 *     Filter Properties supported
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder filter(Filter... filter) {
                    for (Filter value : filter) {
                        this.filter.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Filter Properties supported.
                 * </p>
                 * 
                 * @param filter
                 *     Filter Properties supported
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder filter(Collection<Filter> filter) {
                    this.filter.addAll(filter);
                    return this;
                }

                /**
                 * <p>
                 * Properties supported for $lookup.
                 * </p>
                 * 
                 * @param property
                 *     Properties supported for $lookup
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder property(Code... property) {
                    for (Code value : property) {
                        this.property.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Properties supported for $lookup.
                 * </p>
                 * 
                 * @param property
                 *     Properties supported for $lookup
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder property(Collection<Code> property) {
                    this.property.addAll(property);
                    return this;
                }

                @Override
                public Version build() {
                    return new Version(this);
                }

                private static Builder from(Version version) {
                    Builder builder = new Builder();
                    builder.id = version.id;
                    builder.extension.addAll(version.extension);
                    builder.modifierExtension.addAll(version.modifierExtension);
                    builder.code = version.code;
                    builder.isDefault = version.isDefault;
                    builder.compositional = version.compositional;
                    builder.language.addAll(version.language);
                    builder.filter.addAll(version.filter);
                    builder.property.addAll(version.property);
                    return builder;
                }
            }

            /**
             * <p>
             * Filter Properties supported.
             * </p>
             */
            public static class Filter extends BackboneElement {
                private final Code code;
                private final List<Code> op;

                private Filter(Builder builder) {
                    super(builder);
                    this.code = ValidationSupport.requireNonNull(builder.code, "code");
                    this.op = ValidationSupport.requireNonEmpty(builder.op, "op");
                }

                /**
                 * <p>
                 * Code of the property supported.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Code}.
                 */
                public Code getCode() {
                    return code;
                }

                /**
                 * <p>
                 * Operations supported for the property.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link Code}.
                 */
                public List<Code> getOp() {
                    return op;
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
                            accept(op, "op", visitor, Code.class);
                        }
                        visitor.visitEnd(elementName, this);
                        visitor.postVisit(this);
                    }
                }

                @Override
                public Builder toBuilder() {
                    return Builder.from(this);
                }

                public static Builder builder(Code code, List<Code> op) {
                    return new Builder(code, op);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final Code code;
                    private final List<Code> op;

                    private Builder(Code code, List<Code> op) {
                        super();
                        this.code = code;
                        this.op = op;
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
                    public Filter build() {
                        return new Filter(this);
                    }

                    private static Builder from(Filter filter) {
                        Builder builder = new Builder(filter.code, filter.op);
                        builder.id = filter.id;
                        builder.extension.addAll(filter.extension);
                        builder.modifierExtension.addAll(filter.modifierExtension);
                        return builder;
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Information about the [ValueSet/$expand](valueset-operation-expand.html) operation.
     * </p>
     */
    public static class Expansion extends BackboneElement {
        private final Boolean hierarchical;
        private final Boolean paging;
        private final Boolean incomplete;
        private final List<Parameter> parameter;
        private final Markdown textFilter;

        private Expansion(Builder builder) {
            super(builder);
            this.hierarchical = builder.hierarchical;
            this.paging = builder.paging;
            this.incomplete = builder.incomplete;
            this.parameter = builder.parameter;
            this.textFilter = builder.textFilter;
        }

        /**
         * <p>
         * Whether the server can return nested value sets.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getHierarchical() {
            return hierarchical;
        }

        /**
         * <p>
         * Whether the server supports paging on expansion.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getPaging() {
            return paging;
        }

        /**
         * <p>
         * Allow request for incomplete expansions?
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getIncomplete() {
            return incomplete;
        }

        /**
         * <p>
         * Supported expansion parameter.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Parameter}.
         */
        public List<Parameter> getParameter() {
            return parameter;
        }

        /**
         * <p>
         * Documentation about text searching works.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Markdown}.
         */
        public Markdown getTextFilter() {
            return textFilter;
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
                    accept(hierarchical, "hierarchical", visitor);
                    accept(paging, "paging", visitor);
                    accept(incomplete, "incomplete", visitor);
                    accept(parameter, "parameter", visitor, Parameter.class);
                    accept(textFilter, "textFilter", visitor);
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
            private Boolean hierarchical;
            private Boolean paging;
            private Boolean incomplete;
            private List<Parameter> parameter = new ArrayList<>();
            private Markdown textFilter;

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
             * Whether the server can return nested value sets.
             * </p>
             * 
             * @param hierarchical
             *     Whether the server can return nested value sets
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder hierarchical(Boolean hierarchical) {
                this.hierarchical = hierarchical;
                return this;
            }

            /**
             * <p>
             * Whether the server supports paging on expansion.
             * </p>
             * 
             * @param paging
             *     Whether the server supports paging on expansion
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder paging(Boolean paging) {
                this.paging = paging;
                return this;
            }

            /**
             * <p>
             * Allow request for incomplete expansions?
             * </p>
             * 
             * @param incomplete
             *     Allow request for incomplete expansions?
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder incomplete(Boolean incomplete) {
                this.incomplete = incomplete;
                return this;
            }

            /**
             * <p>
             * Supported expansion parameter.
             * </p>
             * 
             * @param parameter
             *     Supported expansion parameter
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder parameter(Parameter... parameter) {
                for (Parameter value : parameter) {
                    this.parameter.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Supported expansion parameter.
             * </p>
             * 
             * @param parameter
             *     Supported expansion parameter
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder parameter(Collection<Parameter> parameter) {
                this.parameter.addAll(parameter);
                return this;
            }

            /**
             * <p>
             * Documentation about text searching works.
             * </p>
             * 
             * @param textFilter
             *     Documentation about text searching works
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder textFilter(Markdown textFilter) {
                this.textFilter = textFilter;
                return this;
            }

            @Override
            public Expansion build() {
                return new Expansion(this);
            }

            private static Builder from(Expansion expansion) {
                Builder builder = new Builder();
                builder.id = expansion.id;
                builder.extension.addAll(expansion.extension);
                builder.modifierExtension.addAll(expansion.modifierExtension);
                builder.hierarchical = expansion.hierarchical;
                builder.paging = expansion.paging;
                builder.incomplete = expansion.incomplete;
                builder.parameter.addAll(expansion.parameter);
                builder.textFilter = expansion.textFilter;
                return builder;
            }
        }

        /**
         * <p>
         * Supported expansion parameter.
         * </p>
         */
        public static class Parameter extends BackboneElement {
            private final Code name;
            private final String documentation;

            private Parameter(Builder builder) {
                super(builder);
                this.name = ValidationSupport.requireNonNull(builder.name, "name");
                this.documentation = builder.documentation;
            }

            /**
             * <p>
             * Expansion Parameter name.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Code}.
             */
            public Code getName() {
                return name;
            }

            /**
             * <p>
             * Description of support for parameter.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getDocumentation() {
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

            public static Builder builder(Code name) {
                return new Builder(name);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Code name;

                // optional
                private String documentation;

                private Builder(Code name) {
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
                 * Description of support for parameter.
                 * </p>
                 * 
                 * @param documentation
                 *     Description of support for parameter
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder documentation(String documentation) {
                    this.documentation = documentation;
                    return this;
                }

                @Override
                public Parameter build() {
                    return new Parameter(this);
                }

                private static Builder from(Parameter parameter) {
                    Builder builder = new Builder(parameter.name);
                    builder.id = parameter.id;
                    builder.extension.addAll(parameter.extension);
                    builder.modifierExtension.addAll(parameter.modifierExtension);
                    builder.documentation = parameter.documentation;
                    return builder;
                }
            }
        }
    }

    /**
     * <p>
     * Information about the [ValueSet/$validate-code](valueset-operation-validate-code.html) operation.
     * </p>
     */
    public static class ValidateCode extends BackboneElement {
        private final Boolean translations;

        private ValidateCode(Builder builder) {
            super(builder);
            this.translations = ValidationSupport.requireNonNull(builder.translations, "translations");
        }

        /**
         * <p>
         * Whether translations are validated.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getTranslations() {
            return translations;
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
                    accept(translations, "translations", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Boolean translations) {
            return new Builder(translations);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Boolean translations;

            private Builder(Boolean translations) {
                super();
                this.translations = translations;
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
            public ValidateCode build() {
                return new ValidateCode(this);
            }

            private static Builder from(ValidateCode validateCode) {
                Builder builder = new Builder(validateCode.translations);
                builder.id = validateCode.id;
                builder.extension.addAll(validateCode.extension);
                builder.modifierExtension.addAll(validateCode.modifierExtension);
                return builder;
            }
        }
    }

    /**
     * <p>
     * Information about the [ConceptMap/$translate](conceptmap-operation-translate.html) operation.
     * </p>
     */
    public static class Translation extends BackboneElement {
        private final Boolean needsMap;

        private Translation(Builder builder) {
            super(builder);
            this.needsMap = ValidationSupport.requireNonNull(builder.needsMap, "needsMap");
        }

        /**
         * <p>
         * Whether the client must identify the map.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getNeedsMap() {
            return needsMap;
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
                    accept(needsMap, "needsMap", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Boolean needsMap) {
            return new Builder(needsMap);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Boolean needsMap;

            private Builder(Boolean needsMap) {
                super();
                this.needsMap = needsMap;
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
            public Translation build() {
                return new Translation(this);
            }

            private static Builder from(Translation translation) {
                Builder builder = new Builder(translation.needsMap);
                builder.id = translation.id;
                builder.extension.addAll(translation.extension);
                builder.modifierExtension.addAll(translation.modifierExtension);
                return builder;
            }
        }
    }

    /**
     * <p>
     * Whether the $closure operation is supported.
     * </p>
     */
    public static class Closure extends BackboneElement {
        private final Boolean translation;

        private Closure(Builder builder) {
            super(builder);
            this.translation = builder.translation;
        }

        /**
         * <p>
         * If cross-system closure is supported.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getTranslation() {
            return translation;
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
                    accept(translation, "translation", visitor);
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
            private Boolean translation;

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
             * If cross-system closure is supported.
             * </p>
             * 
             * @param translation
             *     If cross-system closure is supported
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder translation(Boolean translation) {
                this.translation = translation;
                return this;
            }

            @Override
            public Closure build() {
                return new Closure(this);
            }

            private static Builder from(Closure closure) {
                Builder builder = new Builder();
                builder.id = closure.id;
                builder.extension.addAll(closure.extension);
                builder.modifierExtension.addAll(closure.modifierExtension);
                builder.translation = closure.translation;
                return builder;
            }
        }
    }
}
