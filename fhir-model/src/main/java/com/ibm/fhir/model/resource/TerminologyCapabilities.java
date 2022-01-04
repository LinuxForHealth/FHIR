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
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.CapabilityStatementKind;
import com.ibm.fhir.model.type.code.CodeSearchSupport;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A TerminologyCapabilities resource documents a set of capabilities (behaviors) of a FHIR Terminology Server that may 
 * be used as a statement of actual server functionality or a statement of required or desired server implementation.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "tcp-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/TerminologyCapabilities"
)
@Constraint(
    id = "tcp-1",
    level = "Rule",
    location = "TerminologyCapabilities.codeSystem",
    description = "If there is more than one version, a version code must be defined",
    expression = "version.count() > 1 implies version.all(code.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/TerminologyCapabilities"
)
@Constraint(
    id = "tcp-2",
    level = "Rule",
    location = "(base)",
    description = "A Capability Statement SHALL have at least one of description, software, or implementation element.",
    expression = "(description.count() + software.count() + implementation.count()) > 0",
    source = "http://hl7.org/fhir/StructureDefinition/TerminologyCapabilities"
)
@Constraint(
    id = "tcp-3",
    level = "Rule",
    location = "(base)",
    description = "If kind = instance, implementation must be present and software may be present",
    expression = "(kind != 'instance') or implementation.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/TerminologyCapabilities"
)
@Constraint(
    id = "tcp-4",
    level = "Rule",
    location = "(base)",
    description = "If kind = capability, implementation must be absent, software must be present",
    expression = "(kind != 'capability') or (implementation.exists().not() and software.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/TerminologyCapabilities"
)
@Constraint(
    id = "tcp-5",
    level = "Rule",
    location = "(base)",
    description = "If kind = requirements, implementation and software must be absent",
    expression = "(kind!='requirements') or (implementation.exists().not() and software.exists().not())",
    source = "http://hl7.org/fhir/StructureDefinition/TerminologyCapabilities"
)
@Constraint(
    id = "terminologyCapabilities-6",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/TerminologyCapabilities",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class TerminologyCapabilities extends DomainResource {
    @Summary
    private final Uri url;
    @Summary
    private final String version;
    @Summary
    private final String name;
    @Summary
    private final String title;
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
    @Required
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
    private final Markdown copyright;
    @Summary
    @Binding(
        bindingName = "CapabilityStatementKind",
        strength = BindingStrength.Value.REQUIRED,
        description = "How a capability statement is intended to be used.",
        valueSet = "http://hl7.org/fhir/ValueSet/capability-statement-kind|4.3.0-CIBUILD"
    )
    @Required
    private final CapabilityStatementKind kind;
    @Summary
    private final Software software;
    @Summary
    private final Implementation implementation;
    @Summary
    private final Boolean lockedDate;
    private final List<CodeSystem> codeSystem;
    private final Expansion expansion;
    @Binding(
        bindingName = "CodeSearchSupport",
        strength = BindingStrength.Value.REQUIRED,
        description = "The degree to which the server supports the code search parameter on ValueSet, if it is supported.",
        valueSet = "http://hl7.org/fhir/ValueSet/code-search-support|4.3.0-CIBUILD"
    )
    private final CodeSearchSupport codeSearch;
    private final ValidateCode validateCode;
    private final Translation translation;
    private final Closure closure;

    private TerminologyCapabilities(Builder builder) {
        super(builder);
        url = builder.url;
        version = builder.version;
        name = builder.name;
        title = builder.title;
        status = builder.status;
        experimental = builder.experimental;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        purpose = builder.purpose;
        copyright = builder.copyright;
        kind = builder.kind;
        software = builder.software;
        implementation = builder.implementation;
        lockedDate = builder.lockedDate;
        codeSystem = Collections.unmodifiableList(builder.codeSystem);
        expansion = builder.expansion;
        codeSearch = builder.codeSearch;
        validateCode = builder.validateCode;
        translation = builder.translation;
        closure = builder.closure;
    }

    /**
     * An absolute URI that is used to identify this terminology capabilities when it is referenced in a specification, 
     * model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a 
     * literal address at which at which an authoritative instance of this terminology capabilities is (or will be) 
     * published. This URL can be the target of a canonical reference. It SHALL remain the same when the terminology 
     * capabilities is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * The identifier that is used to identify this version of the terminology capabilities when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the terminology capabilities author 
     * and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version 
     * is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the terminology capabilities. This name should be usable as an identifier for the 
     * module by machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the terminology capabilities.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The status of this terminology capabilities. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this terminology capabilities is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the terminology capabilities was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the terminology capabilities changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the terminology capabilities.
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
     * A free text natural language description of the terminology capabilities from a consumer's perspective. Typically, 
     * this is used when the capability statement describes a desired rather than an actual solution, for example as a formal 
     * expression of requirements as part of an RFP.
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
     * may be used to assist with indexing and searching for appropriate terminology capabilities instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the terminology capabilities is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this terminology capabilities is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A copyright statement relating to the terminology capabilities and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the terminology capabilities.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * The way that this statement is intended to be used, to describe an actual running instance of software, a particular 
     * product (kind, not instance of software) or a class of implementation (e.g. a desired purchase).
     * 
     * @return
     *     An immutable object of type {@link CapabilityStatementKind} that is non-null.
     */
    public CapabilityStatementKind getKind() {
        return kind;
    }

    /**
     * Software that is covered by this terminology capability statement. It is used when the statement describes the 
     * capabilities of a particular software version, independent of an installation.
     * 
     * @return
     *     An immutable object of type {@link Software} that may be null.
     */
    public Software getSoftware() {
        return software;
    }

    /**
     * Identifies a specific implementation instance that is described by the terminology capability statement - i.e. a 
     * particular installation, rather than the capabilities of a software program.
     * 
     * @return
     *     An immutable object of type {@link Implementation} that may be null.
     */
    public Implementation getImplementation() {
        return implementation;
    }

    /**
     * Whether the server supports lockedDate.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getLockedDate() {
        return lockedDate;
    }

    /**
     * Identifies a code system that is supported by the server. If there is a no code system URL, then this declares the 
     * general assumptions a client can make about support for any CodeSystem resource.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeSystem} that may be empty.
     */
    public List<CodeSystem> getCodeSystem() {
        return codeSystem;
    }

    /**
     * Information about the [ValueSet/$expand](valueset-operation-expand.html) operation.
     * 
     * @return
     *     An immutable object of type {@link Expansion} that may be null.
     */
    public Expansion getExpansion() {
        return expansion;
    }

    /**
     * The degree to which the server supports the code search parameter on ValueSet, if it is supported.
     * 
     * @return
     *     An immutable object of type {@link CodeSearchSupport} that may be null.
     */
    public CodeSearchSupport getCodeSearch() {
        return codeSearch;
    }

    /**
     * Information about the [ValueSet/$validate-code](valueset-operation-validate-code.html) operation.
     * 
     * @return
     *     An immutable object of type {@link ValidateCode} that may be null.
     */
    public ValidateCode getValidateCode() {
        return validateCode;
    }

    /**
     * Information about the [ConceptMap/$translate](conceptmap-operation-translate.html) operation.
     * 
     * @return
     *     An immutable object of type {@link Translation} that may be null.
     */
    public Translation getTranslation() {
        return translation;
    }

    /**
     * Whether the $closure operation is supported.
     * 
     * @return
     *     An immutable object of type {@link Closure} that may be null.
     */
    public Closure getClosure() {
        return closure;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            (version != null) || 
            (name != null) || 
            (title != null) || 
            (status != null) || 
            (experimental != null) || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            (description != null) || 
            !useContext.isEmpty() || 
            !jurisdiction.isEmpty() || 
            (purpose != null) || 
            (copyright != null) || 
            (kind != null) || 
            (software != null) || 
            (implementation != null) || 
            (lockedDate != null) || 
            !codeSystem.isEmpty() || 
            (expansion != null) || 
            (codeSearch != null) || 
            (validateCode != null) || 
            (translation != null) || 
            (closure != null);
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
        TerminologyCapabilities other = (TerminologyCapabilities) obj;
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
            Objects.equals(title, other.title) && 
            Objects.equals(status, other.status) && 
            Objects.equals(experimental, other.experimental) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(description, other.description) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(copyright, other.copyright) && 
            Objects.equals(kind, other.kind) && 
            Objects.equals(software, other.software) && 
            Objects.equals(implementation, other.implementation) && 
            Objects.equals(lockedDate, other.lockedDate) && 
            Objects.equals(codeSystem, other.codeSystem) && 
            Objects.equals(expansion, other.expansion) && 
            Objects.equals(codeSearch, other.codeSearch) && 
            Objects.equals(validateCode, other.validateCode) && 
            Objects.equals(translation, other.translation) && 
            Objects.equals(closure, other.closure);
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
                title, 
                status, 
                experimental, 
                date, 
                publisher, 
                contact, 
                description, 
                useContext, 
                jurisdiction, 
                purpose, 
                copyright, 
                kind, 
                software, 
                implementation, 
                lockedDate, 
                codeSystem, 
                expansion, 
                codeSearch, 
                validateCode, 
                translation, 
                closure);
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
        private String title;
        private PublicationStatus status;
        private Boolean experimental;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private Markdown copyright;
        private CapabilityStatementKind kind;
        private Software software;
        private Implementation implementation;
        private Boolean lockedDate;
        private List<CodeSystem> codeSystem = new ArrayList<>();
        private Expansion expansion;
        private CodeSearchSupport codeSearch;
        private ValidateCode validateCode;
        private Translation translation;
        private Closure closure;

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
         * An absolute URI that is used to identify this terminology capabilities when it is referenced in a specification, 
         * model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a 
         * literal address at which at which an authoritative instance of this terminology capabilities is (or will be) 
         * published. This URL can be the target of a canonical reference. It SHALL remain the same when the terminology 
         * capabilities is stored on different servers.
         * 
         * @param url
         *     Canonical identifier for this terminology capabilities, represented as a URI (globally unique)
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
         *     Business version of the terminology capabilities
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
         * The identifier that is used to identify this version of the terminology capabilities when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the terminology capabilities author 
         * and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version 
         * is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of the terminology capabilities
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
         * @param name
         *     Name for this terminology capabilities (computer friendly)
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
         * A natural language name identifying the terminology capabilities. This name should be usable as an identifier for the 
         * module by machine processing applications such as code generation.
         * 
         * @param name
         *     Name for this terminology capabilities (computer friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Convenience method for setting {@code title}.
         * 
         * @param title
         *     Name for this terminology capabilities (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #title(com.ibm.fhir.model.type.String)
         */
        public Builder title(java.lang.String title) {
            this.title = (title == null) ? null : String.of(title);
            return this;
        }

        /**
         * A short, descriptive, user-friendly title for the terminology capabilities.
         * 
         * @param title
         *     Name for this terminology capabilities (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The status of this terminology capabilities. Enables tracking the life-cycle of the content.
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
         * A Boolean value to indicate that this terminology capabilities is authored for testing purposes (or 
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
         * The date (and optionally time) when the terminology capabilities was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the terminology capabilities changes.
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
         * The name of the organization or individual that published the terminology capabilities.
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
         * A free text natural language description of the terminology capabilities from a consumer's perspective. Typically, 
         * this is used when the capability statement describes a desired rather than an actual solution, for example as a formal 
         * expression of requirements as part of an RFP.
         * 
         * @param description
         *     Natural language description of the terminology capabilities
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
         * may be used to assist with indexing and searching for appropriate terminology capabilities instances.
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
         * may be used to assist with indexing and searching for appropriate terminology capabilities instances.
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
         * A legal or geographic region in which the terminology capabilities is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for terminology capabilities (if applicable)
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
         * A legal or geographic region in which the terminology capabilities is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for terminology capabilities (if applicable)
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
         * Explanation of why this terminology capabilities is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this terminology capabilities is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * A copyright statement relating to the terminology capabilities and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the terminology capabilities.
         * 
         * @param copyright
         *     Use and/or publishing restrictions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder copyright(Markdown copyright) {
            this.copyright = copyright;
            return this;
        }

        /**
         * The way that this statement is intended to be used, to describe an actual running instance of software, a particular 
         * product (kind, not instance of software) or a class of implementation (e.g. a desired purchase).
         * 
         * <p>This element is required.
         * 
         * @param kind
         *     instance | capability | requirements
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder kind(CapabilityStatementKind kind) {
            this.kind = kind;
            return this;
        }

        /**
         * Software that is covered by this terminology capability statement. It is used when the statement describes the 
         * capabilities of a particular software version, independent of an installation.
         * 
         * @param software
         *     Software that is covered by this terminology capability statement
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder software(Software software) {
            this.software = software;
            return this;
        }

        /**
         * Identifies a specific implementation instance that is described by the terminology capability statement - i.e. a 
         * particular installation, rather than the capabilities of a software program.
         * 
         * @param implementation
         *     If this describes a specific instance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder implementation(Implementation implementation) {
            this.implementation = implementation;
            return this;
        }

        /**
         * Convenience method for setting {@code lockedDate}.
         * 
         * @param lockedDate
         *     Whether lockedDate is supported
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #lockedDate(com.ibm.fhir.model.type.Boolean)
         */
        public Builder lockedDate(java.lang.Boolean lockedDate) {
            this.lockedDate = (lockedDate == null) ? null : Boolean.of(lockedDate);
            return this;
        }

        /**
         * Whether the server supports lockedDate.
         * 
         * @param lockedDate
         *     Whether lockedDate is supported
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lockedDate(Boolean lockedDate) {
            this.lockedDate = lockedDate;
            return this;
        }

        /**
         * Identifies a code system that is supported by the server. If there is a no code system URL, then this declares the 
         * general assumptions a client can make about support for any CodeSystem resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param codeSystem
         *     A code system supported by the server
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder codeSystem(CodeSystem... codeSystem) {
            for (CodeSystem value : codeSystem) {
                this.codeSystem.add(value);
            }
            return this;
        }

        /**
         * Identifies a code system that is supported by the server. If there is a no code system URL, then this declares the 
         * general assumptions a client can make about support for any CodeSystem resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param codeSystem
         *     A code system supported by the server
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder codeSystem(Collection<CodeSystem> codeSystem) {
            this.codeSystem = new ArrayList<>(codeSystem);
            return this;
        }

        /**
         * Information about the [ValueSet/$expand](valueset-operation-expand.html) operation.
         * 
         * @param expansion
         *     Information about the [ValueSet/$expand](valueset-operation-expand.html) operation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder expansion(Expansion expansion) {
            this.expansion = expansion;
            return this;
        }

        /**
         * The degree to which the server supports the code search parameter on ValueSet, if it is supported.
         * 
         * @param codeSearch
         *     explicit | all
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder codeSearch(CodeSearchSupport codeSearch) {
            this.codeSearch = codeSearch;
            return this;
        }

        /**
         * Information about the [ValueSet/$validate-code](valueset-operation-validate-code.html) operation.
         * 
         * @param validateCode
         *     Information about the [ValueSet/$validate-code](valueset-operation-validate-code.html) operation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder validateCode(ValidateCode validateCode) {
            this.validateCode = validateCode;
            return this;
        }

        /**
         * Information about the [ConceptMap/$translate](conceptmap-operation-translate.html) operation.
         * 
         * @param translation
         *     Information about the [ConceptMap/$translate](conceptmap-operation-translate.html) operation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder translation(Translation translation) {
            this.translation = translation;
            return this;
        }

        /**
         * Whether the $closure operation is supported.
         * 
         * @param closure
         *     Information about the [ConceptMap/$closure](conceptmap-operation-closure.html) operation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder closure(Closure closure) {
            this.closure = closure;
            return this;
        }

        /**
         * Build the {@link TerminologyCapabilities}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>date</li>
         * <li>kind</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link TerminologyCapabilities}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid TerminologyCapabilities per the base specification
         */
        @Override
        public TerminologyCapabilities build() {
            TerminologyCapabilities terminologyCapabilities = new TerminologyCapabilities(this);
            if (validating) {
                validate(terminologyCapabilities);
            }
            return terminologyCapabilities;
        }

        protected void validate(TerminologyCapabilities terminologyCapabilities) {
            super.validate(terminologyCapabilities);
            ValidationSupport.requireNonNull(terminologyCapabilities.status, "status");
            ValidationSupport.requireNonNull(terminologyCapabilities.date, "date");
            ValidationSupport.checkList(terminologyCapabilities.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(terminologyCapabilities.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(terminologyCapabilities.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.requireNonNull(terminologyCapabilities.kind, "kind");
            ValidationSupport.checkList(terminologyCapabilities.codeSystem, "codeSystem", CodeSystem.class);
        }

        protected Builder from(TerminologyCapabilities terminologyCapabilities) {
            super.from(terminologyCapabilities);
            url = terminologyCapabilities.url;
            version = terminologyCapabilities.version;
            name = terminologyCapabilities.name;
            title = terminologyCapabilities.title;
            status = terminologyCapabilities.status;
            experimental = terminologyCapabilities.experimental;
            date = terminologyCapabilities.date;
            publisher = terminologyCapabilities.publisher;
            contact.addAll(terminologyCapabilities.contact);
            description = terminologyCapabilities.description;
            useContext.addAll(terminologyCapabilities.useContext);
            jurisdiction.addAll(terminologyCapabilities.jurisdiction);
            purpose = terminologyCapabilities.purpose;
            copyright = terminologyCapabilities.copyright;
            kind = terminologyCapabilities.kind;
            software = terminologyCapabilities.software;
            implementation = terminologyCapabilities.implementation;
            lockedDate = terminologyCapabilities.lockedDate;
            codeSystem.addAll(terminologyCapabilities.codeSystem);
            expansion = terminologyCapabilities.expansion;
            codeSearch = terminologyCapabilities.codeSearch;
            validateCode = terminologyCapabilities.validateCode;
            translation = terminologyCapabilities.translation;
            closure = terminologyCapabilities.closure;
            return this;
        }
    }

    /**
     * Software that is covered by this terminology capability statement. It is used when the statement describes the 
     * capabilities of a particular software version, independent of an installation.
     */
    public static class Software extends BackboneElement {
        @Summary
        @Required
        private final String name;
        @Summary
        private final String version;

        private Software(Builder builder) {
            super(builder);
            name = builder.name;
            version = builder.version;
        }

        /**
         * Name the software is known by.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getName() {
            return name;
        }

        /**
         * The version identifier for the software covered by this statement.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getVersion() {
            return version;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (version != null);
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
                    accept(name, "name", visitor);
                    accept(version, "version", visitor);
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
            Software other = (Software) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(version, other.version);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    version);
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
            private String name;
            private String version;

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
             * Convenience method for setting {@code name}.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     A name the software is known by
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
             * Name the software is known by.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     A name the software is known by
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Convenience method for setting {@code version}.
             * 
             * @param version
             *     Version covered by this statement
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
             * The version identifier for the software covered by this statement.
             * 
             * @param version
             *     Version covered by this statement
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder version(String version) {
                this.version = version;
                return this;
            }

            /**
             * Build the {@link Software}
             * 
             * <p>Required elements:
             * <ul>
             * <li>name</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Software}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Software per the base specification
             */
            @Override
            public Software build() {
                Software software = new Software(this);
                if (validating) {
                    validate(software);
                }
                return software;
            }

            protected void validate(Software software) {
                super.validate(software);
                ValidationSupport.requireNonNull(software.name, "name");
                ValidationSupport.requireValueOrChildren(software);
            }

            protected Builder from(Software software) {
                super.from(software);
                name = software.name;
                version = software.version;
                return this;
            }
        }
    }

    /**
     * Identifies a specific implementation instance that is described by the terminology capability statement - i.e. a 
     * particular installation, rather than the capabilities of a software program.
     */
    public static class Implementation extends BackboneElement {
        @Summary
        @Required
        private final String description;
        @Summary
        private final Url url;

        private Implementation(Builder builder) {
            super(builder);
            description = builder.description;
            url = builder.url;
        }

        /**
         * Information about the specific installation that this terminology capability statement relates to.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * An absolute base URL for the implementation.
         * 
         * @return
         *     An immutable object of type {@link Url} that may be null.
         */
        public Url getUrl() {
            return url;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (url != null);
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
                    accept(description, "description", visitor);
                    accept(url, "url", visitor);
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
            Implementation other = (Implementation) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(url, other.url);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    url);
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
            private String description;
            private Url url;

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
             * Convenience method for setting {@code description}.
             * 
             * <p>This element is required.
             * 
             * @param description
             *     Describes this specific instance
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
             * Information about the specific installation that this terminology capability statement relates to.
             * 
             * <p>This element is required.
             * 
             * @param description
             *     Describes this specific instance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * An absolute base URL for the implementation.
             * 
             * @param url
             *     Base URL for the implementation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder url(Url url) {
                this.url = url;
                return this;
            }

            /**
             * Build the {@link Implementation}
             * 
             * <p>Required elements:
             * <ul>
             * <li>description</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Implementation}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Implementation per the base specification
             */
            @Override
            public Implementation build() {
                Implementation implementation = new Implementation(this);
                if (validating) {
                    validate(implementation);
                }
                return implementation;
            }

            protected void validate(Implementation implementation) {
                super.validate(implementation);
                ValidationSupport.requireNonNull(implementation.description, "description");
                ValidationSupport.requireValueOrChildren(implementation);
            }

            protected Builder from(Implementation implementation) {
                super.from(implementation);
                description = implementation.description;
                url = implementation.url;
                return this;
            }
        }
    }

    /**
     * Identifies a code system that is supported by the server. If there is a no code system URL, then this declares the 
     * general assumptions a client can make about support for any CodeSystem resource.
     */
    public static class CodeSystem extends BackboneElement {
        private final Canonical uri;
        private final List<Version> version;
        private final Boolean subsumption;

        private CodeSystem(Builder builder) {
            super(builder);
            uri = builder.uri;
            version = Collections.unmodifiableList(builder.version);
            subsumption = builder.subsumption;
        }

        /**
         * URI for the Code System.
         * 
         * @return
         *     An immutable object of type {@link Canonical} that may be null.
         */
        public Canonical getUri() {
            return uri;
        }

        /**
         * For the code system, a list of versions that are supported by the server.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Version} that may be empty.
         */
        public List<Version> getVersion() {
            return version;
        }

        /**
         * True if subsumption is supported for this version of the code system.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getSubsumption() {
            return subsumption;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (uri != null) || 
                !version.isEmpty() || 
                (subsumption != null);
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
                    accept(uri, "uri", visitor);
                    accept(version, "version", visitor, Version.class);
                    accept(subsumption, "subsumption", visitor);
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
            CodeSystem other = (CodeSystem) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(uri, other.uri) && 
                Objects.equals(version, other.version) && 
                Objects.equals(subsumption, other.subsumption);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    uri, 
                    version, 
                    subsumption);
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
            private Canonical uri;
            private List<Version> version = new ArrayList<>();
            private Boolean subsumption;

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
             * URI for the Code System.
             * 
             * @param uri
             *     URI for the Code System
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder uri(Canonical uri) {
                this.uri = uri;
                return this;
            }

            /**
             * For the code system, a list of versions that are supported by the server.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param version
             *     Version of Code System supported
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder version(Version... version) {
                for (Version value : version) {
                    this.version.add(value);
                }
                return this;
            }

            /**
             * For the code system, a list of versions that are supported by the server.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param version
             *     Version of Code System supported
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder version(Collection<Version> version) {
                this.version = new ArrayList<>(version);
                return this;
            }

            /**
             * Convenience method for setting {@code subsumption}.
             * 
             * @param subsumption
             *     Whether subsumption is supported
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #subsumption(com.ibm.fhir.model.type.Boolean)
             */
            public Builder subsumption(java.lang.Boolean subsumption) {
                this.subsumption = (subsumption == null) ? null : Boolean.of(subsumption);
                return this;
            }

            /**
             * True if subsumption is supported for this version of the code system.
             * 
             * @param subsumption
             *     Whether subsumption is supported
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subsumption(Boolean subsumption) {
                this.subsumption = subsumption;
                return this;
            }

            /**
             * Build the {@link CodeSystem}
             * 
             * @return
             *     An immutable object of type {@link CodeSystem}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid CodeSystem per the base specification
             */
            @Override
            public CodeSystem build() {
                CodeSystem codeSystem = new CodeSystem(this);
                if (validating) {
                    validate(codeSystem);
                }
                return codeSystem;
            }

            protected void validate(CodeSystem codeSystem) {
                super.validate(codeSystem);
                ValidationSupport.checkList(codeSystem.version, "version", Version.class);
                ValidationSupport.requireValueOrChildren(codeSystem);
            }

            protected Builder from(CodeSystem codeSystem) {
                super.from(codeSystem);
                uri = codeSystem.uri;
                version.addAll(codeSystem.version);
                subsumption = codeSystem.subsumption;
                return this;
            }
        }

        /**
         * For the code system, a list of versions that are supported by the server.
         */
        public static class Version extends BackboneElement {
            @Summary
            private final String code;
            @Summary
            private final Boolean isDefault;
            private final Boolean compositional;
            private final List<Code> language;
            private final List<Filter> filter;
            private final List<Code> property;

            private Version(Builder builder) {
                super(builder);
                code = builder.code;
                isDefault = builder.isDefault;
                compositional = builder.compositional;
                language = Collections.unmodifiableList(builder.language);
                filter = Collections.unmodifiableList(builder.filter);
                property = Collections.unmodifiableList(builder.property);
            }

            /**
             * For version-less code systems, there should be a single version with no identifier.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getCode() {
                return code;
            }

            /**
             * If this is the default version for this code system.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getIsDefault() {
                return isDefault;
            }

            /**
             * If the compositional grammar defined by the code system is supported.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getCompositional() {
                return compositional;
            }

            /**
             * Language Displays supported.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Code} that may be empty.
             */
            public List<Code> getLanguage() {
                return language;
            }

            /**
             * Filter Properties supported.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Filter} that may be empty.
             */
            public List<Filter> getFilter() {
                return filter;
            }

            /**
             * Properties supported for $lookup.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Code} that may be empty.
             */
            public List<Code> getProperty() {
                return property;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (code != null) || 
                    (isDefault != null) || 
                    (compositional != null) || 
                    !language.isEmpty() || 
                    !filter.isEmpty() || 
                    !property.isEmpty();
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
                        accept(code, "code", visitor);
                        accept(isDefault, "isDefault", visitor);
                        accept(compositional, "compositional", visitor);
                        accept(language, "language", visitor, Code.class);
                        accept(filter, "filter", visitor, Filter.class);
                        accept(property, "property", visitor, Code.class);
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
                Version other = (Version) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(isDefault, other.isDefault) && 
                    Objects.equals(compositional, other.compositional) && 
                    Objects.equals(language, other.language) && 
                    Objects.equals(filter, other.filter) && 
                    Objects.equals(property, other.property);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        code, 
                        isDefault, 
                        compositional, 
                        language, 
                        filter, 
                        property);
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
                 * Convenience method for setting {@code code}.
                 * 
                 * @param code
                 *     Version identifier for this version
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #code(com.ibm.fhir.model.type.String)
                 */
                public Builder code(java.lang.String code) {
                    this.code = (code == null) ? null : String.of(code);
                    return this;
                }

                /**
                 * For version-less code systems, there should be a single version with no identifier.
                 * 
                 * @param code
                 *     Version identifier for this version
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(String code) {
                    this.code = code;
                    return this;
                }

                /**
                 * Convenience method for setting {@code isDefault}.
                 * 
                 * @param isDefault
                 *     If this is the default version for this code system
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #isDefault(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder isDefault(java.lang.Boolean isDefault) {
                    this.isDefault = (isDefault == null) ? null : Boolean.of(isDefault);
                    return this;
                }

                /**
                 * If this is the default version for this code system.
                 * 
                 * @param isDefault
                 *     If this is the default version for this code system
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder isDefault(Boolean isDefault) {
                    this.isDefault = isDefault;
                    return this;
                }

                /**
                 * Convenience method for setting {@code compositional}.
                 * 
                 * @param compositional
                 *     If compositional grammar is supported
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #compositional(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder compositional(java.lang.Boolean compositional) {
                    this.compositional = (compositional == null) ? null : Boolean.of(compositional);
                    return this;
                }

                /**
                 * If the compositional grammar defined by the code system is supported.
                 * 
                 * @param compositional
                 *     If compositional grammar is supported
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder compositional(Boolean compositional) {
                    this.compositional = compositional;
                    return this;
                }

                /**
                 * Language Displays supported.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param language
                 *     Language Displays supported
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder language(Code... language) {
                    for (Code value : language) {
                        this.language.add(value);
                    }
                    return this;
                }

                /**
                 * Language Displays supported.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param language
                 *     Language Displays supported
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder language(Collection<Code> language) {
                    this.language = new ArrayList<>(language);
                    return this;
                }

                /**
                 * Filter Properties supported.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param filter
                 *     Filter Properties supported
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder filter(Filter... filter) {
                    for (Filter value : filter) {
                        this.filter.add(value);
                    }
                    return this;
                }

                /**
                 * Filter Properties supported.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param filter
                 *     Filter Properties supported
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder filter(Collection<Filter> filter) {
                    this.filter = new ArrayList<>(filter);
                    return this;
                }

                /**
                 * Properties supported for $lookup.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param property
                 *     Properties supported for $lookup
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder property(Code... property) {
                    for (Code value : property) {
                        this.property.add(value);
                    }
                    return this;
                }

                /**
                 * Properties supported for $lookup.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param property
                 *     Properties supported for $lookup
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder property(Collection<Code> property) {
                    this.property = new ArrayList<>(property);
                    return this;
                }

                /**
                 * Build the {@link Version}
                 * 
                 * @return
                 *     An immutable object of type {@link Version}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Version per the base specification
                 */
                @Override
                public Version build() {
                    Version version = new Version(this);
                    if (validating) {
                        validate(version);
                    }
                    return version;
                }

                protected void validate(Version version) {
                    super.validate(version);
                    ValidationSupport.checkList(version.language, "language", Code.class);
                    ValidationSupport.checkList(version.filter, "filter", Filter.class);
                    ValidationSupport.checkList(version.property, "property", Code.class);
                    ValidationSupport.requireValueOrChildren(version);
                }

                protected Builder from(Version version) {
                    super.from(version);
                    code = version.code;
                    isDefault = version.isDefault;
                    compositional = version.compositional;
                    language.addAll(version.language);
                    filter.addAll(version.filter);
                    property.addAll(version.property);
                    return this;
                }
            }

            /**
             * Filter Properties supported.
             */
            public static class Filter extends BackboneElement {
                @Required
                private final Code code;
                @Required
                private final List<Code> op;

                private Filter(Builder builder) {
                    super(builder);
                    code = builder.code;
                    op = Collections.unmodifiableList(builder.op);
                }

                /**
                 * Code of the property supported.
                 * 
                 * @return
                 *     An immutable object of type {@link Code} that is non-null.
                 */
                public Code getCode() {
                    return code;
                }

                /**
                 * Operations supported for the property.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Code} that is non-empty.
                 */
                public List<Code> getOp() {
                    return op;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (code != null) || 
                        !op.isEmpty();
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
                            accept(code, "code", visitor);
                            accept(op, "op", visitor, Code.class);
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
                    Filter other = (Filter) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(code, other.code) && 
                        Objects.equals(op, other.op);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            code, 
                            op);
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
                    private Code code;
                    private List<Code> op = new ArrayList<>();

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
                     * Code of the property supported.
                     * 
                     * <p>This element is required.
                     * 
                     * @param code
                     *     Code of the property supported
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder code(Code code) {
                        this.code = code;
                        return this;
                    }

                    /**
                     * Operations supported for the property.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * <p>This element is required.
                     * 
                     * @param op
                     *     Operations supported for the property
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder op(Code... op) {
                        for (Code value : op) {
                            this.op.add(value);
                        }
                        return this;
                    }

                    /**
                     * Operations supported for the property.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * <p>This element is required.
                     * 
                     * @param op
                     *     Operations supported for the property
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder op(Collection<Code> op) {
                        this.op = new ArrayList<>(op);
                        return this;
                    }

                    /**
                     * Build the {@link Filter}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>code</li>
                     * <li>op</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Filter}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Filter per the base specification
                     */
                    @Override
                    public Filter build() {
                        Filter filter = new Filter(this);
                        if (validating) {
                            validate(filter);
                        }
                        return filter;
                    }

                    protected void validate(Filter filter) {
                        super.validate(filter);
                        ValidationSupport.requireNonNull(filter.code, "code");
                        ValidationSupport.checkNonEmptyList(filter.op, "op", Code.class);
                        ValidationSupport.requireValueOrChildren(filter);
                    }

                    protected Builder from(Filter filter) {
                        super.from(filter);
                        code = filter.code;
                        op.addAll(filter.op);
                        return this;
                    }
                }
            }
        }
    }

    /**
     * Information about the [ValueSet/$expand](valueset-operation-expand.html) operation.
     */
    public static class Expansion extends BackboneElement {
        private final Boolean hierarchical;
        private final Boolean paging;
        private final Boolean incomplete;
        private final List<Parameter> parameter;
        private final Markdown textFilter;

        private Expansion(Builder builder) {
            super(builder);
            hierarchical = builder.hierarchical;
            paging = builder.paging;
            incomplete = builder.incomplete;
            parameter = Collections.unmodifiableList(builder.parameter);
            textFilter = builder.textFilter;
        }

        /**
         * Whether the server can return nested value sets.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getHierarchical() {
            return hierarchical;
        }

        /**
         * Whether the server supports paging on expansion.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getPaging() {
            return paging;
        }

        /**
         * Allow request for incomplete expansions?
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getIncomplete() {
            return incomplete;
        }

        /**
         * Supported expansion parameter.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Parameter} that may be empty.
         */
        public List<Parameter> getParameter() {
            return parameter;
        }

        /**
         * Documentation about text searching works.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getTextFilter() {
            return textFilter;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (hierarchical != null) || 
                (paging != null) || 
                (incomplete != null) || 
                !parameter.isEmpty() || 
                (textFilter != null);
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
                    accept(hierarchical, "hierarchical", visitor);
                    accept(paging, "paging", visitor);
                    accept(incomplete, "incomplete", visitor);
                    accept(parameter, "parameter", visitor, Parameter.class);
                    accept(textFilter, "textFilter", visitor);
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
            Expansion other = (Expansion) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(hierarchical, other.hierarchical) && 
                Objects.equals(paging, other.paging) && 
                Objects.equals(incomplete, other.incomplete) && 
                Objects.equals(parameter, other.parameter) && 
                Objects.equals(textFilter, other.textFilter);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    hierarchical, 
                    paging, 
                    incomplete, 
                    parameter, 
                    textFilter);
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
            private Boolean hierarchical;
            private Boolean paging;
            private Boolean incomplete;
            private List<Parameter> parameter = new ArrayList<>();
            private Markdown textFilter;

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
             * Convenience method for setting {@code hierarchical}.
             * 
             * @param hierarchical
             *     Whether the server can return nested value sets
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #hierarchical(com.ibm.fhir.model.type.Boolean)
             */
            public Builder hierarchical(java.lang.Boolean hierarchical) {
                this.hierarchical = (hierarchical == null) ? null : Boolean.of(hierarchical);
                return this;
            }

            /**
             * Whether the server can return nested value sets.
             * 
             * @param hierarchical
             *     Whether the server can return nested value sets
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder hierarchical(Boolean hierarchical) {
                this.hierarchical = hierarchical;
                return this;
            }

            /**
             * Convenience method for setting {@code paging}.
             * 
             * @param paging
             *     Whether the server supports paging on expansion
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #paging(com.ibm.fhir.model.type.Boolean)
             */
            public Builder paging(java.lang.Boolean paging) {
                this.paging = (paging == null) ? null : Boolean.of(paging);
                return this;
            }

            /**
             * Whether the server supports paging on expansion.
             * 
             * @param paging
             *     Whether the server supports paging on expansion
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder paging(Boolean paging) {
                this.paging = paging;
                return this;
            }

            /**
             * Convenience method for setting {@code incomplete}.
             * 
             * @param incomplete
             *     Allow request for incomplete expansions?
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #incomplete(com.ibm.fhir.model.type.Boolean)
             */
            public Builder incomplete(java.lang.Boolean incomplete) {
                this.incomplete = (incomplete == null) ? null : Boolean.of(incomplete);
                return this;
            }

            /**
             * Allow request for incomplete expansions?
             * 
             * @param incomplete
             *     Allow request for incomplete expansions?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder incomplete(Boolean incomplete) {
                this.incomplete = incomplete;
                return this;
            }

            /**
             * Supported expansion parameter.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param parameter
             *     Supported expansion parameter
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder parameter(Parameter... parameter) {
                for (Parameter value : parameter) {
                    this.parameter.add(value);
                }
                return this;
            }

            /**
             * Supported expansion parameter.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param parameter
             *     Supported expansion parameter
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder parameter(Collection<Parameter> parameter) {
                this.parameter = new ArrayList<>(parameter);
                return this;
            }

            /**
             * Documentation about text searching works.
             * 
             * @param textFilter
             *     Documentation about text searching works
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder textFilter(Markdown textFilter) {
                this.textFilter = textFilter;
                return this;
            }

            /**
             * Build the {@link Expansion}
             * 
             * @return
             *     An immutable object of type {@link Expansion}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Expansion per the base specification
             */
            @Override
            public Expansion build() {
                Expansion expansion = new Expansion(this);
                if (validating) {
                    validate(expansion);
                }
                return expansion;
            }

            protected void validate(Expansion expansion) {
                super.validate(expansion);
                ValidationSupport.checkList(expansion.parameter, "parameter", Parameter.class);
                ValidationSupport.requireValueOrChildren(expansion);
            }

            protected Builder from(Expansion expansion) {
                super.from(expansion);
                hierarchical = expansion.hierarchical;
                paging = expansion.paging;
                incomplete = expansion.incomplete;
                parameter.addAll(expansion.parameter);
                textFilter = expansion.textFilter;
                return this;
            }
        }

        /**
         * Supported expansion parameter.
         */
        public static class Parameter extends BackboneElement {
            @Required
            private final Code name;
            private final String documentation;

            private Parameter(Builder builder) {
                super(builder);
                name = builder.name;
                documentation = builder.documentation;
            }

            /**
             * Expansion Parameter name.
             * 
             * @return
             *     An immutable object of type {@link Code} that is non-null.
             */
            public Code getName() {
                return name;
            }

            /**
             * Description of support for parameter.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDocumentation() {
                return documentation;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (name != null) || 
                    (documentation != null);
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
                        accept(name, "name", visitor);
                        accept(documentation, "documentation", visitor);
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
                Parameter other = (Parameter) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(name, other.name) && 
                    Objects.equals(documentation, other.documentation);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        name, 
                        documentation);
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
                private Code name;
                private String documentation;

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
                 * Expansion Parameter name.
                 * 
                 * <p>This element is required.
                 * 
                 * @param name
                 *     Expansion Parameter name
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder name(Code name) {
                    this.name = name;
                    return this;
                }

                /**
                 * Convenience method for setting {@code documentation}.
                 * 
                 * @param documentation
                 *     Description of support for parameter
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #documentation(com.ibm.fhir.model.type.String)
                 */
                public Builder documentation(java.lang.String documentation) {
                    this.documentation = (documentation == null) ? null : String.of(documentation);
                    return this;
                }

                /**
                 * Description of support for parameter.
                 * 
                 * @param documentation
                 *     Description of support for parameter
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder documentation(String documentation) {
                    this.documentation = documentation;
                    return this;
                }

                /**
                 * Build the {@link Parameter}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>name</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Parameter}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Parameter per the base specification
                 */
                @Override
                public Parameter build() {
                    Parameter parameter = new Parameter(this);
                    if (validating) {
                        validate(parameter);
                    }
                    return parameter;
                }

                protected void validate(Parameter parameter) {
                    super.validate(parameter);
                    ValidationSupport.requireNonNull(parameter.name, "name");
                    ValidationSupport.requireValueOrChildren(parameter);
                }

                protected Builder from(Parameter parameter) {
                    super.from(parameter);
                    name = parameter.name;
                    documentation = parameter.documentation;
                    return this;
                }
            }
        }
    }

    /**
     * Information about the [ValueSet/$validate-code](valueset-operation-validate-code.html) operation.
     */
    public static class ValidateCode extends BackboneElement {
        @Required
        private final Boolean translations;

        private ValidateCode(Builder builder) {
            super(builder);
            translations = builder.translations;
        }

        /**
         * Whether translations are validated.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that is non-null.
         */
        public Boolean getTranslations() {
            return translations;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (translations != null);
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
                    accept(translations, "translations", visitor);
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
            ValidateCode other = (ValidateCode) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(translations, other.translations);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    translations);
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
            private Boolean translations;

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
             * Convenience method for setting {@code translations}.
             * 
             * <p>This element is required.
             * 
             * @param translations
             *     Whether translations are validated
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #translations(com.ibm.fhir.model.type.Boolean)
             */
            public Builder translations(java.lang.Boolean translations) {
                this.translations = (translations == null) ? null : Boolean.of(translations);
                return this;
            }

            /**
             * Whether translations are validated.
             * 
             * <p>This element is required.
             * 
             * @param translations
             *     Whether translations are validated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder translations(Boolean translations) {
                this.translations = translations;
                return this;
            }

            /**
             * Build the {@link ValidateCode}
             * 
             * <p>Required elements:
             * <ul>
             * <li>translations</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link ValidateCode}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ValidateCode per the base specification
             */
            @Override
            public ValidateCode build() {
                ValidateCode validateCode = new ValidateCode(this);
                if (validating) {
                    validate(validateCode);
                }
                return validateCode;
            }

            protected void validate(ValidateCode validateCode) {
                super.validate(validateCode);
                ValidationSupport.requireNonNull(validateCode.translations, "translations");
                ValidationSupport.requireValueOrChildren(validateCode);
            }

            protected Builder from(ValidateCode validateCode) {
                super.from(validateCode);
                translations = validateCode.translations;
                return this;
            }
        }
    }

    /**
     * Information about the [ConceptMap/$translate](conceptmap-operation-translate.html) operation.
     */
    public static class Translation extends BackboneElement {
        @Required
        private final Boolean needsMap;

        private Translation(Builder builder) {
            super(builder);
            needsMap = builder.needsMap;
        }

        /**
         * Whether the client must identify the map.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that is non-null.
         */
        public Boolean getNeedsMap() {
            return needsMap;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (needsMap != null);
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
                    accept(needsMap, "needsMap", visitor);
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
            Translation other = (Translation) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(needsMap, other.needsMap);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    needsMap);
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
            private Boolean needsMap;

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
             * Convenience method for setting {@code needsMap}.
             * 
             * <p>This element is required.
             * 
             * @param needsMap
             *     Whether the client must identify the map
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #needsMap(com.ibm.fhir.model.type.Boolean)
             */
            public Builder needsMap(java.lang.Boolean needsMap) {
                this.needsMap = (needsMap == null) ? null : Boolean.of(needsMap);
                return this;
            }

            /**
             * Whether the client must identify the map.
             * 
             * <p>This element is required.
             * 
             * @param needsMap
             *     Whether the client must identify the map
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder needsMap(Boolean needsMap) {
                this.needsMap = needsMap;
                return this;
            }

            /**
             * Build the {@link Translation}
             * 
             * <p>Required elements:
             * <ul>
             * <li>needsMap</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Translation}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Translation per the base specification
             */
            @Override
            public Translation build() {
                Translation translation = new Translation(this);
                if (validating) {
                    validate(translation);
                }
                return translation;
            }

            protected void validate(Translation translation) {
                super.validate(translation);
                ValidationSupport.requireNonNull(translation.needsMap, "needsMap");
                ValidationSupport.requireValueOrChildren(translation);
            }

            protected Builder from(Translation translation) {
                super.from(translation);
                needsMap = translation.needsMap;
                return this;
            }
        }
    }

    /**
     * Whether the $closure operation is supported.
     */
    public static class Closure extends BackboneElement {
        private final Boolean translation;

        private Closure(Builder builder) {
            super(builder);
            translation = builder.translation;
        }

        /**
         * If cross-system closure is supported.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getTranslation() {
            return translation;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (translation != null);
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
                    accept(translation, "translation", visitor);
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
            Closure other = (Closure) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(translation, other.translation);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    translation);
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
            private Boolean translation;

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
             * Convenience method for setting {@code translation}.
             * 
             * @param translation
             *     If cross-system closure is supported
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #translation(com.ibm.fhir.model.type.Boolean)
             */
            public Builder translation(java.lang.Boolean translation) {
                this.translation = (translation == null) ? null : Boolean.of(translation);
                return this;
            }

            /**
             * If cross-system closure is supported.
             * 
             * @param translation
             *     If cross-system closure is supported
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder translation(Boolean translation) {
                this.translation = translation;
                return this;
            }

            /**
             * Build the {@link Closure}
             * 
             * @return
             *     An immutable object of type {@link Closure}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Closure per the base specification
             */
            @Override
            public Closure build() {
                Closure closure = new Closure(this);
                if (validating) {
                    validate(closure);
                }
                return closure;
            }

            protected void validate(Closure closure) {
                super.validate(closure);
                ValidationSupport.requireValueOrChildren(closure);
            }

            protected Builder from(Closure closure) {
                super.from(closure);
                translation = closure.translation;
                return this;
            }
        }
    }
}
