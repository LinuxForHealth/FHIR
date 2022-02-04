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
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.CapabilityStatementKind;
import com.ibm.fhir.model.type.code.ConditionalDeleteStatus;
import com.ibm.fhir.model.type.code.ConditionalReadStatus;
import com.ibm.fhir.model.type.code.DocumentMode;
import com.ibm.fhir.model.type.code.EventCapabilityMode;
import com.ibm.fhir.model.type.code.FHIRVersion;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ReferenceHandlingPolicy;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.ResourceVersionPolicy;
import com.ibm.fhir.model.type.code.RestfulCapabilityMode;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.SystemRestfulInteraction;
import com.ibm.fhir.model.type.code.TypeRestfulInteraction;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server for a particular version of FHIR 
 * that may be used as a statement of actual server functionality or a statement of required or desired server 
 * implementation.
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.Value.NORMATIVE
)
@Constraint(
    id = "cpb-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement"
)
@Constraint(
    id = "cpb-1",
    level = "Rule",
    location = "(base)",
    description = "A Capability Statement SHALL have at least one of REST, messaging or document element.",
    expression = "rest.exists() or messaging.exists() or document.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement"
)
@Constraint(
    id = "cpb-2",
    level = "Rule",
    location = "(base)",
    description = "A Capability Statement SHALL have at least one of description, software, or implementation element.",
    expression = "(description.count() + software.count() + implementation.count()) > 0",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement"
)
@Constraint(
    id = "cpb-3",
    level = "Rule",
    location = "(base)",
    description = "Messaging end-point is required (and is only permitted) when a statement is for an implementation.",
    expression = "messaging.endpoint.empty() or kind = 'instance'",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement"
)
@Constraint(
    id = "cpb-7",
    level = "Rule",
    location = "(base)",
    description = "The set of documents must be unique by the combination of profile and mode.",
    expression = "document.select(profile&mode).isDistinct()",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement"
)
@Constraint(
    id = "cpb-9",
    level = "Rule",
    location = "CapabilityStatement.rest",
    description = "A given resource can only be described once per RESTful mode.",
    expression = "resource.select(type).isDistinct()",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement"
)
@Constraint(
    id = "cpb-12",
    level = "Rule",
    location = "CapabilityStatement.rest.resource",
    description = "Search parameter names must be unique in the context of a resource.",
    expression = "searchParam.select(name).isDistinct()",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement"
)
@Constraint(
    id = "cpb-14",
    level = "Rule",
    location = "(base)",
    description = "If kind = instance, implementation must be present and software may be present",
    expression = "(kind != 'instance') or implementation.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement"
)
@Constraint(
    id = "cpb-15",
    level = "Rule",
    location = "(base)",
    description = "If kind = capability, implementation must be absent, software must be present",
    expression = "(kind != 'capability') or (implementation.exists().not() and software.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement"
)
@Constraint(
    id = "cpb-16",
    level = "Rule",
    location = "(base)",
    description = "If kind = requirements, implementation and software must be absent",
    expression = "(kind!='requirements') or (implementation.exists().not() and software.exists().not())",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement"
)
@Constraint(
    id = "capabilityStatement-17",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement",
    generated = true
)
@Constraint(
    id = "capabilityStatement-18",
    level = "Warning",
    location = "rest.security.service",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/restful-security-service",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/restful-security-service', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement",
    generated = true
)
@Constraint(
    id = "capabilityStatement-19",
    level = "Warning",
    location = "messaging.endpoint.protocol",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/message-transport",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/message-transport', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/CapabilityStatement",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CapabilityStatement extends DomainResource {
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
    private final List<Canonical> instantiates;
    @Summary
    private final List<Canonical> imports;
    @Summary
    private final Software software;
    @Summary
    private final Implementation implementation;
    @Summary
    @Binding(
        bindingName = "FHIRVersion",
        strength = BindingStrength.Value.REQUIRED,
        description = "All published FHIR Versions.",
        valueSet = "http://hl7.org/fhir/ValueSet/FHIR-version|4.3.0-CIBUILD"
    )
    @Required
    private final FHIRVersion fhirVersion;
    @Summary
    @Binding(
        bindingName = "MimeType",
        strength = BindingStrength.Value.REQUIRED,
        description = "BCP 13 (RFCs 2045, 2046, 2047, 4288, 4289 and 2049)",
        valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.3.0-CIBUILD"
    )
    @Required
    private final List<Code> format;
    @Summary
    @Binding(
        bindingName = "MimeType",
        strength = BindingStrength.Value.REQUIRED,
        description = "BCP 13 (RFCs 2045, 2046, 2047, 4288, 4289 and 2049)",
        valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.3.0-CIBUILD"
    )
    private final List<Code> patchFormat;
    @Summary
    private final List<Canonical> implementationGuide;
    @Summary
    private final List<Rest> rest;
    @Summary
    private final List<Messaging> messaging;
    @Summary
    private final List<Document> document;

    private CapabilityStatement(Builder builder) {
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
        instantiates = Collections.unmodifiableList(builder.instantiates);
        imports = Collections.unmodifiableList(builder.imports);
        software = builder.software;
        implementation = builder.implementation;
        fhirVersion = builder.fhirVersion;
        format = Collections.unmodifiableList(builder.format);
        patchFormat = Collections.unmodifiableList(builder.patchFormat);
        implementationGuide = Collections.unmodifiableList(builder.implementationGuide);
        rest = Collections.unmodifiableList(builder.rest);
        messaging = Collections.unmodifiableList(builder.messaging);
        document = Collections.unmodifiableList(builder.document);
    }

    /**
     * An absolute URI that is used to identify this capability statement when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this capability statement is (or will be) published. This URL 
     * can be the target of a canonical reference. It SHALL remain the same when the capability statement is stored on 
     * different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * The identifier that is used to identify this version of the capability statement when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the capability statement author and is 
     * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
     * available. There is also no expectation that versions can be placed in a lexicographical sequence.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the capability statement. This name should be usable as an identifier for the 
     * module by machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the capability statement.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The status of this capability statement. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this capability statement is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the capability statement was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the capability statement changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the capability statement.
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
     * A free text natural language description of the capability statement from a consumer's perspective. Typically, this is 
     * used when the capability statement describes a desired rather than an actual solution, for example as a formal 
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
     * may be used to assist with indexing and searching for appropriate capability statement instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the capability statement is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this capability statement is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A copyright statement relating to the capability statement and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the capability statement.
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
     * Reference to a canonical URL of another CapabilityStatement that this software implements. This capability statement 
     * is a published API description that corresponds to a business service. The server may actually implement a subset of 
     * the capability statement it claims to implement, so the capability statement must specify the full capability details.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getInstantiates() {
        return instantiates;
    }

    /**
     * Reference to a canonical URL of another CapabilityStatement that this software adds to. The capability statement 
     * automatically includes everything in the other statement, and it is not duplicated, though the server may repeat the 
     * same resources, interactions and operations to add additional details to them.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getImports() {
        return imports;
    }

    /**
     * Software that is covered by this capability statement. It is used when the capability statement describes the 
     * capabilities of a particular software version, independent of an installation.
     * 
     * @return
     *     An immutable object of type {@link Software} that may be null.
     */
    public Software getSoftware() {
        return software;
    }

    /**
     * Identifies a specific implementation instance that is described by the capability statement - i.e. a particular 
     * installation, rather than the capabilities of a software program.
     * 
     * @return
     *     An immutable object of type {@link Implementation} that may be null.
     */
    public Implementation getImplementation() {
        return implementation;
    }

    /**
     * The version of the FHIR specification that this CapabilityStatement describes (which SHALL be the same as the FHIR 
     * version of the CapabilityStatement itself). There is no default value.
     * 
     * @return
     *     An immutable object of type {@link FHIRVersion} that is non-null.
     */
    public FHIRVersion getFhirVersion() {
        return fhirVersion;
    }

    /**
     * A list of the formats supported by this implementation using their content types.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Code} that is non-empty.
     */
    public List<Code> getFormat() {
        return format;
    }

    /**
     * A list of the patch formats supported by this implementation using their content types.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Code} that may be empty.
     */
    public List<Code> getPatchFormat() {
        return patchFormat;
    }

    /**
     * A list of implementation guides that the server does (or should) support in their entirety.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getImplementationGuide() {
        return implementationGuide;
    }

    /**
     * A definition of the restful capabilities of the solution, if any.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Rest} that may be empty.
     */
    public List<Rest> getRest() {
        return rest;
    }

    /**
     * A description of the messaging capabilities of the solution.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Messaging} that may be empty.
     */
    public List<Messaging> getMessaging() {
        return messaging;
    }

    /**
     * A document definition.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Document} that may be empty.
     */
    public List<Document> getDocument() {
        return document;
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
            !instantiates.isEmpty() || 
            !imports.isEmpty() || 
            (software != null) || 
            (implementation != null) || 
            (fhirVersion != null) || 
            !format.isEmpty() || 
            !patchFormat.isEmpty() || 
            !implementationGuide.isEmpty() || 
            !rest.isEmpty() || 
            !messaging.isEmpty() || 
            !document.isEmpty();
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
                accept(contained, "contained", visitor, com.ibm.fhir.model.resource.Resource.class);
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
        CapabilityStatement other = (CapabilityStatement) obj;
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
            Objects.equals(instantiates, other.instantiates) && 
            Objects.equals(imports, other.imports) && 
            Objects.equals(software, other.software) && 
            Objects.equals(implementation, other.implementation) && 
            Objects.equals(fhirVersion, other.fhirVersion) && 
            Objects.equals(format, other.format) && 
            Objects.equals(patchFormat, other.patchFormat) && 
            Objects.equals(implementationGuide, other.implementationGuide) && 
            Objects.equals(rest, other.rest) && 
            Objects.equals(messaging, other.messaging) && 
            Objects.equals(document, other.document);
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
                instantiates, 
                imports, 
                software, 
                implementation, 
                fhirVersion, 
                format, 
                patchFormat, 
                implementationGuide, 
                rest, 
                messaging, 
                document);
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
        private List<Canonical> instantiates = new ArrayList<>();
        private List<Canonical> imports = new ArrayList<>();
        private Software software;
        private Implementation implementation;
        private FHIRVersion fhirVersion;
        private List<Code> format = new ArrayList<>();
        private List<Code> patchFormat = new ArrayList<>();
        private List<Canonical> implementationGuide = new ArrayList<>();
        private List<Rest> rest = new ArrayList<>();
        private List<Messaging> messaging = new ArrayList<>();
        private List<Document> document = new ArrayList<>();

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
        public Builder contained(com.ibm.fhir.model.resource.Resource... contained) {
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
        public Builder contained(Collection<com.ibm.fhir.model.resource.Resource> contained) {
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
         * An absolute URI that is used to identify this capability statement when it is referenced in a specification, model, 
         * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
         * address at which at which an authoritative instance of this capability statement is (or will be) published. This URL 
         * can be the target of a canonical reference. It SHALL remain the same when the capability statement is stored on 
         * different servers.
         * 
         * @param url
         *     Canonical identifier for this capability statement, represented as a URI (globally unique)
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
         *     Business version of the capability statement
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
         * The identifier that is used to identify this version of the capability statement when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the capability statement author and is 
         * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
         * available. There is also no expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of the capability statement
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
         *     Name for this capability statement (computer friendly)
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
         * A natural language name identifying the capability statement. This name should be usable as an identifier for the 
         * module by machine processing applications such as code generation.
         * 
         * @param name
         *     Name for this capability statement (computer friendly)
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
         *     Name for this capability statement (human friendly)
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
         * A short, descriptive, user-friendly title for the capability statement.
         * 
         * @param title
         *     Name for this capability statement (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The status of this capability statement. Enables tracking the life-cycle of the content.
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
         * A Boolean value to indicate that this capability statement is authored for testing purposes (or 
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
         * The date (and optionally time) when the capability statement was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the capability statement changes.
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
         * The name of the organization or individual that published the capability statement.
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
         * A free text natural language description of the capability statement from a consumer's perspective. Typically, this is 
         * used when the capability statement describes a desired rather than an actual solution, for example as a formal 
         * expression of requirements as part of an RFP.
         * 
         * @param description
         *     Natural language description of the capability statement
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
         * may be used to assist with indexing and searching for appropriate capability statement instances.
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
         * may be used to assist with indexing and searching for appropriate capability statement instances.
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
         * A legal or geographic region in which the capability statement is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for capability statement (if applicable)
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
         * A legal or geographic region in which the capability statement is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for capability statement (if applicable)
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
         * Explanation of why this capability statement is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this capability statement is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * A copyright statement relating to the capability statement and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the capability statement.
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
         * Reference to a canonical URL of another CapabilityStatement that this software implements. This capability statement 
         * is a published API description that corresponds to a business service. The server may actually implement a subset of 
         * the capability statement it claims to implement, so the capability statement must specify the full capability details.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiates
         *     Canonical URL of another capability statement this implements
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiates(Canonical... instantiates) {
            for (Canonical value : instantiates) {
                this.instantiates.add(value);
            }
            return this;
        }

        /**
         * Reference to a canonical URL of another CapabilityStatement that this software implements. This capability statement 
         * is a published API description that corresponds to a business service. The server may actually implement a subset of 
         * the capability statement it claims to implement, so the capability statement must specify the full capability details.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiates
         *     Canonical URL of another capability statement this implements
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instantiates(Collection<Canonical> instantiates) {
            this.instantiates = new ArrayList<>(instantiates);
            return this;
        }

        /**
         * Reference to a canonical URL of another CapabilityStatement that this software adds to. The capability statement 
         * automatically includes everything in the other statement, and it is not duplicated, though the server may repeat the 
         * same resources, interactions and operations to add additional details to them.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param imports
         *     Canonical URL of another capability statement this adds to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder imports(Canonical... imports) {
            for (Canonical value : imports) {
                this.imports.add(value);
            }
            return this;
        }

        /**
         * Reference to a canonical URL of another CapabilityStatement that this software adds to. The capability statement 
         * automatically includes everything in the other statement, and it is not duplicated, though the server may repeat the 
         * same resources, interactions and operations to add additional details to them.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param imports
         *     Canonical URL of another capability statement this adds to
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder imports(Collection<Canonical> imports) {
            this.imports = new ArrayList<>(imports);
            return this;
        }

        /**
         * Software that is covered by this capability statement. It is used when the capability statement describes the 
         * capabilities of a particular software version, independent of an installation.
         * 
         * @param software
         *     Software that is covered by this capability statement
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder software(Software software) {
            this.software = software;
            return this;
        }

        /**
         * Identifies a specific implementation instance that is described by the capability statement - i.e. a particular 
         * installation, rather than the capabilities of a software program.
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
         * The version of the FHIR specification that this CapabilityStatement describes (which SHALL be the same as the FHIR 
         * version of the CapabilityStatement itself). There is no default value.
         * 
         * <p>This element is required.
         * 
         * @param fhirVersion
         *     FHIR Version the system supports
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder fhirVersion(FHIRVersion fhirVersion) {
            this.fhirVersion = fhirVersion;
            return this;
        }

        /**
         * A list of the formats supported by this implementation using their content types.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param format
         *     formats supported (xml | json | ttl | mime type)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder format(Code... format) {
            for (Code value : format) {
                this.format.add(value);
            }
            return this;
        }

        /**
         * A list of the formats supported by this implementation using their content types.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param format
         *     formats supported (xml | json | ttl | mime type)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder format(Collection<Code> format) {
            this.format = new ArrayList<>(format);
            return this;
        }

        /**
         * A list of the patch formats supported by this implementation using their content types.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param patchFormat
         *     Patch formats supported
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patchFormat(Code... patchFormat) {
            for (Code value : patchFormat) {
                this.patchFormat.add(value);
            }
            return this;
        }

        /**
         * A list of the patch formats supported by this implementation using their content types.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param patchFormat
         *     Patch formats supported
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder patchFormat(Collection<Code> patchFormat) {
            this.patchFormat = new ArrayList<>(patchFormat);
            return this;
        }

        /**
         * A list of implementation guides that the server does (or should) support in their entirety.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param implementationGuide
         *     Implementation guides supported
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder implementationGuide(Canonical... implementationGuide) {
            for (Canonical value : implementationGuide) {
                this.implementationGuide.add(value);
            }
            return this;
        }

        /**
         * A list of implementation guides that the server does (or should) support in their entirety.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param implementationGuide
         *     Implementation guides supported
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder implementationGuide(Collection<Canonical> implementationGuide) {
            this.implementationGuide = new ArrayList<>(implementationGuide);
            return this;
        }

        /**
         * A definition of the restful capabilities of the solution, if any.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param rest
         *     If the endpoint is a RESTful one
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder rest(Rest... rest) {
            for (Rest value : rest) {
                this.rest.add(value);
            }
            return this;
        }

        /**
         * A definition of the restful capabilities of the solution, if any.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param rest
         *     If the endpoint is a RESTful one
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder rest(Collection<Rest> rest) {
            this.rest = new ArrayList<>(rest);
            return this;
        }

        /**
         * A description of the messaging capabilities of the solution.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param messaging
         *     If messaging is supported
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder messaging(Messaging... messaging) {
            for (Messaging value : messaging) {
                this.messaging.add(value);
            }
            return this;
        }

        /**
         * A description of the messaging capabilities of the solution.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param messaging
         *     If messaging is supported
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder messaging(Collection<Messaging> messaging) {
            this.messaging = new ArrayList<>(messaging);
            return this;
        }

        /**
         * A document definition.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param document
         *     Document definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder document(Document... document) {
            for (Document value : document) {
                this.document.add(value);
            }
            return this;
        }

        /**
         * A document definition.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param document
         *     Document definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder document(Collection<Document> document) {
            this.document = new ArrayList<>(document);
            return this;
        }

        /**
         * Build the {@link CapabilityStatement}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>date</li>
         * <li>kind</li>
         * <li>fhirVersion</li>
         * <li>format</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link CapabilityStatement}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid CapabilityStatement per the base specification
         */
        @Override
        public CapabilityStatement build() {
            CapabilityStatement capabilityStatement = new CapabilityStatement(this);
            if (validating) {
                validate(capabilityStatement);
            }
            return capabilityStatement;
        }

        protected void validate(CapabilityStatement capabilityStatement) {
            super.validate(capabilityStatement);
            ValidationSupport.requireNonNull(capabilityStatement.status, "status");
            ValidationSupport.requireNonNull(capabilityStatement.date, "date");
            ValidationSupport.checkList(capabilityStatement.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(capabilityStatement.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(capabilityStatement.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.requireNonNull(capabilityStatement.kind, "kind");
            ValidationSupport.checkList(capabilityStatement.instantiates, "instantiates", Canonical.class);
            ValidationSupport.checkList(capabilityStatement.imports, "imports", Canonical.class);
            ValidationSupport.requireNonNull(capabilityStatement.fhirVersion, "fhirVersion");
            ValidationSupport.checkNonEmptyList(capabilityStatement.format, "format", Code.class);
            ValidationSupport.checkList(capabilityStatement.patchFormat, "patchFormat", Code.class);
            ValidationSupport.checkList(capabilityStatement.implementationGuide, "implementationGuide", Canonical.class);
            ValidationSupport.checkList(capabilityStatement.rest, "rest", Rest.class);
            ValidationSupport.checkList(capabilityStatement.messaging, "messaging", Messaging.class);
            ValidationSupport.checkList(capabilityStatement.document, "document", Document.class);
        }

        protected Builder from(CapabilityStatement capabilityStatement) {
            super.from(capabilityStatement);
            url = capabilityStatement.url;
            version = capabilityStatement.version;
            name = capabilityStatement.name;
            title = capabilityStatement.title;
            status = capabilityStatement.status;
            experimental = capabilityStatement.experimental;
            date = capabilityStatement.date;
            publisher = capabilityStatement.publisher;
            contact.addAll(capabilityStatement.contact);
            description = capabilityStatement.description;
            useContext.addAll(capabilityStatement.useContext);
            jurisdiction.addAll(capabilityStatement.jurisdiction);
            purpose = capabilityStatement.purpose;
            copyright = capabilityStatement.copyright;
            kind = capabilityStatement.kind;
            instantiates.addAll(capabilityStatement.instantiates);
            imports.addAll(capabilityStatement.imports);
            software = capabilityStatement.software;
            implementation = capabilityStatement.implementation;
            fhirVersion = capabilityStatement.fhirVersion;
            format.addAll(capabilityStatement.format);
            patchFormat.addAll(capabilityStatement.patchFormat);
            implementationGuide.addAll(capabilityStatement.implementationGuide);
            rest.addAll(capabilityStatement.rest);
            messaging.addAll(capabilityStatement.messaging);
            document.addAll(capabilityStatement.document);
            return this;
        }
    }

    /**
     * Software that is covered by this capability statement. It is used when the capability statement describes the 
     * capabilities of a particular software version, independent of an installation.
     */
    public static class Software extends BackboneElement {
        @Summary
        @Required
        private final String name;
        @Summary
        private final String version;
        @Summary
        private final DateTime releaseDate;

        private Software(Builder builder) {
            super(builder);
            name = builder.name;
            version = builder.version;
            releaseDate = builder.releaseDate;
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

        /**
         * Date this version of the software was released.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getReleaseDate() {
            return releaseDate;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (version != null) || 
                (releaseDate != null);
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
                    accept(releaseDate, "releaseDate", visitor);
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
                Objects.equals(version, other.version) && 
                Objects.equals(releaseDate, other.releaseDate);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    version, 
                    releaseDate);
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
            private DateTime releaseDate;

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
             * Date this version of the software was released.
             * 
             * @param releaseDate
             *     Date this version was released
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder releaseDate(DateTime releaseDate) {
                this.releaseDate = releaseDate;
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
                releaseDate = software.releaseDate;
                return this;
            }
        }
    }

    /**
     * Identifies a specific implementation instance that is described by the capability statement - i.e. a particular 
     * installation, rather than the capabilities of a software program.
     */
    public static class Implementation extends BackboneElement {
        @Summary
        @Required
        private final String description;
        @Summary
        private final Url url;
        @Summary
        @ReferenceTarget({ "Organization" })
        private final Reference custodian;

        private Implementation(Builder builder) {
            super(builder);
            description = builder.description;
            url = builder.url;
            custodian = builder.custodian;
        }

        /**
         * Information about the specific installation that this capability statement relates to.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * An absolute base URL for the implementation. This forms the base for REST interfaces as well as the mailbox and 
         * document interfaces.
         * 
         * @return
         *     An immutable object of type {@link Url} that may be null.
         */
        public Url getUrl() {
            return url;
        }

        /**
         * The organization responsible for the management of the instance and oversight of the data on the server at the 
         * specified URL.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getCustodian() {
            return custodian;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (url != null) || 
                (custodian != null);
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
                    accept(custodian, "custodian", visitor);
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
                Objects.equals(url, other.url) && 
                Objects.equals(custodian, other.custodian);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    url, 
                    custodian);
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
            private Reference custodian;

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
             * Information about the specific installation that this capability statement relates to.
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
             * An absolute base URL for the implementation. This forms the base for REST interfaces as well as the mailbox and 
             * document interfaces.
             * 
             * @param url
             *     Base URL for the installation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder url(Url url) {
                this.url = url;
                return this;
            }

            /**
             * The organization responsible for the management of the instance and oversight of the data on the server at the 
             * specified URL.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param custodian
             *     Organization that manages the data
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder custodian(Reference custodian) {
                this.custodian = custodian;
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
                ValidationSupport.checkReferenceType(implementation.custodian, "custodian", "Organization");
                ValidationSupport.requireValueOrChildren(implementation);
            }

            protected Builder from(Implementation implementation) {
                super.from(implementation);
                description = implementation.description;
                url = implementation.url;
                custodian = implementation.custodian;
                return this;
            }
        }
    }

    /**
     * A definition of the restful capabilities of the solution, if any.
     */
    public static class Rest extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "RestfulCapabilityMode",
            strength = BindingStrength.Value.REQUIRED,
            description = "The mode of a RESTful capability statement.",
            valueSet = "http://hl7.org/fhir/ValueSet/restful-capability-mode|4.3.0-CIBUILD"
        )
        @Required
        private final RestfulCapabilityMode mode;
        private final Markdown documentation;
        @Summary
        private final Security security;
        @Summary
        private final List<Resource> resource;
        private final List<Interaction> interaction;
        private final List<CapabilityStatement.Rest.Resource.SearchParam> searchParam;
        @Summary
        private final List<CapabilityStatement.Rest.Resource.Operation> operation;
        private final List<Canonical> compartment;

        private Rest(Builder builder) {
            super(builder);
            mode = builder.mode;
            documentation = builder.documentation;
            security = builder.security;
            resource = Collections.unmodifiableList(builder.resource);
            interaction = Collections.unmodifiableList(builder.interaction);
            searchParam = Collections.unmodifiableList(builder.searchParam);
            operation = Collections.unmodifiableList(builder.operation);
            compartment = Collections.unmodifiableList(builder.compartment);
        }

        /**
         * Identifies whether this portion of the statement is describing the ability to initiate or receive restful operations.
         * 
         * @return
         *     An immutable object of type {@link RestfulCapabilityMode} that is non-null.
         */
        public RestfulCapabilityMode getMode() {
            return mode;
        }

        /**
         * Information about the system's restful capabilities that apply across all applications, such as security.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getDocumentation() {
            return documentation;
        }

        /**
         * Information about security implementation from an interface perspective - what a client needs to know.
         * 
         * @return
         *     An immutable object of type {@link Security} that may be null.
         */
        public Security getSecurity() {
            return security;
        }

        /**
         * A specification of the restful capabilities of the solution for a specific resource type.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Resource} that may be empty.
         */
        public List<Resource> getResource() {
            return resource;
        }

        /**
         * A specification of restful operations supported by the system.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Interaction} that may be empty.
         */
        public List<Interaction> getInteraction() {
            return interaction;
        }

        /**
         * Search parameters that are supported for searching all resources for implementations to support and/or make use of - 
         * either references to ones defined in the specification, or additional ones defined for/by the implementation.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link SearchParam} that may be empty.
         */
        public List<CapabilityStatement.Rest.Resource.SearchParam> getSearchParam() {
            return searchParam;
        }

        /**
         * Definition of an operation or a named query together with its parameters and their meaning and type.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Operation} that may be empty.
         */
        public List<CapabilityStatement.Rest.Resource.Operation> getOperation() {
            return operation;
        }

        /**
         * An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to 
         * a CompartmentDefinition resource by its canonical URL .
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
         */
        public List<Canonical> getCompartment() {
            return compartment;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (mode != null) || 
                (documentation != null) || 
                (security != null) || 
                !resource.isEmpty() || 
                !interaction.isEmpty() || 
                !searchParam.isEmpty() || 
                !operation.isEmpty() || 
                !compartment.isEmpty();
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
                    accept(mode, "mode", visitor);
                    accept(documentation, "documentation", visitor);
                    accept(security, "security", visitor);
                    accept(resource, "resource", visitor, Resource.class);
                    accept(interaction, "interaction", visitor, Interaction.class);
                    accept(searchParam, "searchParam", visitor, CapabilityStatement.Rest.Resource.SearchParam.class);
                    accept(operation, "operation", visitor, CapabilityStatement.Rest.Resource.Operation.class);
                    accept(compartment, "compartment", visitor, Canonical.class);
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
            Rest other = (Rest) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(mode, other.mode) && 
                Objects.equals(documentation, other.documentation) && 
                Objects.equals(security, other.security) && 
                Objects.equals(resource, other.resource) && 
                Objects.equals(interaction, other.interaction) && 
                Objects.equals(searchParam, other.searchParam) && 
                Objects.equals(operation, other.operation) && 
                Objects.equals(compartment, other.compartment);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    mode, 
                    documentation, 
                    security, 
                    resource, 
                    interaction, 
                    searchParam, 
                    operation, 
                    compartment);
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
            private RestfulCapabilityMode mode;
            private Markdown documentation;
            private Security security;
            private List<Resource> resource = new ArrayList<>();
            private List<Interaction> interaction = new ArrayList<>();
            private List<CapabilityStatement.Rest.Resource.SearchParam> searchParam = new ArrayList<>();
            private List<CapabilityStatement.Rest.Resource.Operation> operation = new ArrayList<>();
            private List<Canonical> compartment = new ArrayList<>();

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
             * Identifies whether this portion of the statement is describing the ability to initiate or receive restful operations.
             * 
             * <p>This element is required.
             * 
             * @param mode
             *     client | server
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder mode(RestfulCapabilityMode mode) {
                this.mode = mode;
                return this;
            }

            /**
             * Information about the system's restful capabilities that apply across all applications, such as security.
             * 
             * @param documentation
             *     General description of implementation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(Markdown documentation) {
                this.documentation = documentation;
                return this;
            }

            /**
             * Information about security implementation from an interface perspective - what a client needs to know.
             * 
             * @param security
             *     Information about security of implementation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder security(Security security) {
                this.security = security;
                return this;
            }

            /**
             * A specification of the restful capabilities of the solution for a specific resource type.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param resource
             *     Resource served on the REST interface
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Resource... resource) {
                for (Resource value : resource) {
                    this.resource.add(value);
                }
                return this;
            }

            /**
             * A specification of the restful capabilities of the solution for a specific resource type.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param resource
             *     Resource served on the REST interface
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder resource(Collection<Resource> resource) {
                this.resource = new ArrayList<>(resource);
                return this;
            }

            /**
             * A specification of restful operations supported by the system.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param interaction
             *     What operations are supported?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder interaction(Interaction... interaction) {
                for (Interaction value : interaction) {
                    this.interaction.add(value);
                }
                return this;
            }

            /**
             * A specification of restful operations supported by the system.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param interaction
             *     What operations are supported?
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder interaction(Collection<Interaction> interaction) {
                this.interaction = new ArrayList<>(interaction);
                return this;
            }

            /**
             * Search parameters that are supported for searching all resources for implementations to support and/or make use of - 
             * either references to ones defined in the specification, or additional ones defined for/by the implementation.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param searchParam
             *     Search parameters for searching all resources
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder searchParam(CapabilityStatement.Rest.Resource.SearchParam... searchParam) {
                for (CapabilityStatement.Rest.Resource.SearchParam value : searchParam) {
                    this.searchParam.add(value);
                }
                return this;
            }

            /**
             * Search parameters that are supported for searching all resources for implementations to support and/or make use of - 
             * either references to ones defined in the specification, or additional ones defined for/by the implementation.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param searchParam
             *     Search parameters for searching all resources
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder searchParam(Collection<CapabilityStatement.Rest.Resource.SearchParam> searchParam) {
                this.searchParam = new ArrayList<>(searchParam);
                return this;
            }

            /**
             * Definition of an operation or a named query together with its parameters and their meaning and type.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param operation
             *     Definition of a system level operation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder operation(CapabilityStatement.Rest.Resource.Operation... operation) {
                for (CapabilityStatement.Rest.Resource.Operation value : operation) {
                    this.operation.add(value);
                }
                return this;
            }

            /**
             * Definition of an operation or a named query together with its parameters and their meaning and type.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param operation
             *     Definition of a system level operation
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder operation(Collection<CapabilityStatement.Rest.Resource.Operation> operation) {
                this.operation = new ArrayList<>(operation);
                return this;
            }

            /**
             * An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to 
             * a CompartmentDefinition resource by its canonical URL .
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param compartment
             *     Compartments served/used by system
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder compartment(Canonical... compartment) {
                for (Canonical value : compartment) {
                    this.compartment.add(value);
                }
                return this;
            }

            /**
             * An absolute URI which is a reference to the definition of a compartment that the system supports. The reference is to 
             * a CompartmentDefinition resource by its canonical URL .
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param compartment
             *     Compartments served/used by system
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder compartment(Collection<Canonical> compartment) {
                this.compartment = new ArrayList<>(compartment);
                return this;
            }

            /**
             * Build the {@link Rest}
             * 
             * <p>Required elements:
             * <ul>
             * <li>mode</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Rest}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Rest per the base specification
             */
            @Override
            public Rest build() {
                Rest rest = new Rest(this);
                if (validating) {
                    validate(rest);
                }
                return rest;
            }

            protected void validate(Rest rest) {
                super.validate(rest);
                ValidationSupport.requireNonNull(rest.mode, "mode");
                ValidationSupport.checkList(rest.resource, "resource", Resource.class);
                ValidationSupport.checkList(rest.interaction, "interaction", Interaction.class);
                ValidationSupport.checkList(rest.searchParam, "searchParam", CapabilityStatement.Rest.Resource.SearchParam.class);
                ValidationSupport.checkList(rest.operation, "operation", CapabilityStatement.Rest.Resource.Operation.class);
                ValidationSupport.checkList(rest.compartment, "compartment", Canonical.class);
                ValidationSupport.requireValueOrChildren(rest);
            }

            protected Builder from(Rest rest) {
                super.from(rest);
                mode = rest.mode;
                documentation = rest.documentation;
                security = rest.security;
                resource.addAll(rest.resource);
                interaction.addAll(rest.interaction);
                searchParam.addAll(rest.searchParam);
                operation.addAll(rest.operation);
                compartment.addAll(rest.compartment);
                return this;
            }
        }

        /**
         * Information about security implementation from an interface perspective - what a client needs to know.
         */
        public static class Security extends BackboneElement {
            @Summary
            private final Boolean cors;
            @Summary
            @Binding(
                bindingName = "RestfulSecurityService",
                strength = BindingStrength.Value.EXTENSIBLE,
                description = "Types of security services used with FHIR.",
                valueSet = "http://hl7.org/fhir/ValueSet/restful-security-service"
            )
            private final List<CodeableConcept> service;
            private final Markdown description;

            private Security(Builder builder) {
                super(builder);
                cors = builder.cors;
                service = Collections.unmodifiableList(builder.service);
                description = builder.description;
            }

            /**
             * Server adds CORS headers when responding to requests - this enables Javascript applications to use the server.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getCors() {
                return cors;
            }

            /**
             * Types of security services that are supported/required by the system.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
             */
            public List<CodeableConcept> getService() {
                return service;
            }

            /**
             * General description of how security works.
             * 
             * @return
             *     An immutable object of type {@link Markdown} that may be null.
             */
            public Markdown getDescription() {
                return description;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (cors != null) || 
                    !service.isEmpty() || 
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
                        accept(cors, "cors", visitor);
                        accept(service, "service", visitor, CodeableConcept.class);
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
                Security other = (Security) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(cors, other.cors) && 
                    Objects.equals(service, other.service) && 
                    Objects.equals(description, other.description);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        cors, 
                        service, 
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
                private Boolean cors;
                private List<CodeableConcept> service = new ArrayList<>();
                private Markdown description;

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
                 * Convenience method for setting {@code cors}.
                 * 
                 * @param cors
                 *     Adds CORS Headers (http://enable-cors.org/)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #cors(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder cors(java.lang.Boolean cors) {
                    this.cors = (cors == null) ? null : Boolean.of(cors);
                    return this;
                }

                /**
                 * Server adds CORS headers when responding to requests - this enables Javascript applications to use the server.
                 * 
                 * @param cors
                 *     Adds CORS Headers (http://enable-cors.org/)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder cors(Boolean cors) {
                    this.cors = cors;
                    return this;
                }

                /**
                 * Types of security services that are supported/required by the system.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param service
                 *     OAuth | SMART-on-FHIR | NTLM | Basic | Kerberos | Certificates
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder service(CodeableConcept... service) {
                    for (CodeableConcept value : service) {
                        this.service.add(value);
                    }
                    return this;
                }

                /**
                 * Types of security services that are supported/required by the system.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param service
                 *     OAuth | SMART-on-FHIR | NTLM | Basic | Kerberos | Certificates
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder service(Collection<CodeableConcept> service) {
                    this.service = new ArrayList<>(service);
                    return this;
                }

                /**
                 * General description of how security works.
                 * 
                 * @param description
                 *     General description of how security works
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(Markdown description) {
                    this.description = description;
                    return this;
                }

                /**
                 * Build the {@link Security}
                 * 
                 * @return
                 *     An immutable object of type {@link Security}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Security per the base specification
                 */
                @Override
                public Security build() {
                    Security security = new Security(this);
                    if (validating) {
                        validate(security);
                    }
                    return security;
                }

                protected void validate(Security security) {
                    super.validate(security);
                    ValidationSupport.checkList(security.service, "service", CodeableConcept.class);
                    ValidationSupport.requireValueOrChildren(security);
                }

                protected Builder from(Security security) {
                    super.from(security);
                    cors = security.cors;
                    service.addAll(security.service);
                    description = security.description;
                    return this;
                }
            }
        }

        /**
         * A specification of the restful capabilities of the solution for a specific resource type.
         */
        public static class Resource extends BackboneElement {
            @Summary
            @Binding(
                bindingName = "ResourceType",
                strength = BindingStrength.Value.REQUIRED,
                description = "One of the resource types defined as part of this version of FHIR.",
                valueSet = "http://hl7.org/fhir/ValueSet/resource-types|4.3.0-CIBUILD"
            )
            @Required
            private final ResourceTypeCode type;
            @Summary
            private final Canonical profile;
            @Summary
            private final List<Canonical> supportedProfile;
            private final Markdown documentation;
            private final List<Interaction> interaction;
            @Binding(
                bindingName = "ResourceVersionPolicy",
                strength = BindingStrength.Value.REQUIRED,
                description = "How the system supports versioning for a resource.",
                valueSet = "http://hl7.org/fhir/ValueSet/versioning-policy|4.3.0-CIBUILD"
            )
            private final ResourceVersionPolicy versioning;
            private final Boolean readHistory;
            private final Boolean updateCreate;
            private final Boolean conditionalCreate;
            @Binding(
                bindingName = "ConditionalReadStatus",
                strength = BindingStrength.Value.REQUIRED,
                description = "A code that indicates how the server supports conditional read.",
                valueSet = "http://hl7.org/fhir/ValueSet/conditional-read-status|4.3.0-CIBUILD"
            )
            private final ConditionalReadStatus conditionalRead;
            private final Boolean conditionalUpdate;
            @Binding(
                bindingName = "ConditionalDeleteStatus",
                strength = BindingStrength.Value.REQUIRED,
                description = "A code that indicates how the server supports conditional delete.",
                valueSet = "http://hl7.org/fhir/ValueSet/conditional-delete-status|4.3.0-CIBUILD"
            )
            private final ConditionalDeleteStatus conditionalDelete;
            @Binding(
                bindingName = "ReferenceHandlingPolicy",
                strength = BindingStrength.Value.REQUIRED,
                description = "A set of flags that defines how references are supported.",
                valueSet = "http://hl7.org/fhir/ValueSet/reference-handling-policy|4.3.0-CIBUILD"
            )
            private final List<ReferenceHandlingPolicy> referencePolicy;
            private final List<String> searchInclude;
            private final List<String> searchRevInclude;
            private final List<SearchParam> searchParam;
            @Summary
            private final List<Operation> operation;

            private Resource(Builder builder) {
                super(builder);
                type = builder.type;
                profile = builder.profile;
                supportedProfile = Collections.unmodifiableList(builder.supportedProfile);
                documentation = builder.documentation;
                interaction = Collections.unmodifiableList(builder.interaction);
                versioning = builder.versioning;
                readHistory = builder.readHistory;
                updateCreate = builder.updateCreate;
                conditionalCreate = builder.conditionalCreate;
                conditionalRead = builder.conditionalRead;
                conditionalUpdate = builder.conditionalUpdate;
                conditionalDelete = builder.conditionalDelete;
                referencePolicy = Collections.unmodifiableList(builder.referencePolicy);
                searchInclude = Collections.unmodifiableList(builder.searchInclude);
                searchRevInclude = Collections.unmodifiableList(builder.searchRevInclude);
                searchParam = Collections.unmodifiableList(builder.searchParam);
                operation = Collections.unmodifiableList(builder.operation);
            }

            /**
             * A type of resource exposed via the restful interface.
             * 
             * @return
             *     An immutable object of type {@link ResourceTypeCode} that is non-null.
             */
            public ResourceTypeCode getType() {
                return type;
            }

            /**
             * A specification of the profile that describes the solution's overall support for the resource, including any 
             * constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles]
             * (profiling.html#profile-uses).
             * 
             * @return
             *     An immutable object of type {@link Canonical} that may be null.
             */
            public Canonical getProfile() {
                return profile;
            }

            /**
             * A list of profiles that represent different use cases supported by the system. For a server, "supported by the system" 
             * means the system hosts/produces a set of resources that are conformant to a particular profile, and allows clients 
             * that use its services to search using this profile and to find appropriate data. For a client, it means the system 
             * will search by this profile and process data according to the guidance implicit in the profile. See further discussion 
             * in [Using Profiles](profiling.html#profile-uses).
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
             */
            public List<Canonical> getSupportedProfile() {
                return supportedProfile;
            }

            /**
             * Additional information about the resource type used by the system.
             * 
             * @return
             *     An immutable object of type {@link Markdown} that may be null.
             */
            public Markdown getDocumentation() {
                return documentation;
            }

            /**
             * Identifies a restful operation supported by the solution.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Interaction} that may be empty.
             */
            public List<Interaction> getInteraction() {
                return interaction;
            }

            /**
             * This field is set to no-version to specify that the system does not support (server) or use (client) versioning for 
             * this resource type. If this has some other value, the server must at least correctly track and populate the versionId 
             * meta-property on resources. If the value is 'versioned-update', then the server supports all the versioning features, 
             * including using e-tags for version integrity in the API.
             * 
             * @return
             *     An immutable object of type {@link ResourceVersionPolicy} that may be null.
             */
            public ResourceVersionPolicy getVersioning() {
                return versioning;
            }

            /**
             * A flag for whether the server is able to return past versions as part of the vRead operation.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getReadHistory() {
                return readHistory;
            }

            /**
             * A flag to indicate that the server allows or needs to allow the client to create new identities on the server (that 
             * is, the client PUTs to a location where there is no existing resource). Allowing this operation means that the server 
             * allows the client to create new identities on the server.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getUpdateCreate() {
                return updateCreate;
            }

            /**
             * A flag that indicates that the server supports conditional create.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getConditionalCreate() {
                return conditionalCreate;
            }

            /**
             * A code that indicates how the server supports conditional read.
             * 
             * @return
             *     An immutable object of type {@link ConditionalReadStatus} that may be null.
             */
            public ConditionalReadStatus getConditionalRead() {
                return conditionalRead;
            }

            /**
             * A flag that indicates that the server supports conditional update.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getConditionalUpdate() {
                return conditionalUpdate;
            }

            /**
             * A code that indicates how the server supports conditional delete.
             * 
             * @return
             *     An immutable object of type {@link ConditionalDeleteStatus} that may be null.
             */
            public ConditionalDeleteStatus getConditionalDelete() {
                return conditionalDelete;
            }

            /**
             * A set of flags that defines how references are supported.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link ReferenceHandlingPolicy} that may be empty.
             */
            public List<ReferenceHandlingPolicy> getReferencePolicy() {
                return referencePolicy;
            }

            /**
             * A list of _include values supported by the server.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
             */
            public List<String> getSearchInclude() {
                return searchInclude;
            }

            /**
             * A list of _revinclude (reverse include) values supported by the server.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
             */
            public List<String> getSearchRevInclude() {
                return searchRevInclude;
            }

            /**
             * Search parameters for implementations to support and/or make use of - either references to ones defined in the 
             * specification, or additional ones defined for/by the implementation.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link SearchParam} that may be empty.
             */
            public List<SearchParam> getSearchParam() {
                return searchParam;
            }

            /**
             * Definition of an operation or a named query together with its parameters and their meaning and type. Consult the 
             * definition of the operation for details about how to invoke the operation, and the parameters.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Operation} that may be empty.
             */
            public List<Operation> getOperation() {
                return operation;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (profile != null) || 
                    !supportedProfile.isEmpty() || 
                    (documentation != null) || 
                    !interaction.isEmpty() || 
                    (versioning != null) || 
                    (readHistory != null) || 
                    (updateCreate != null) || 
                    (conditionalCreate != null) || 
                    (conditionalRead != null) || 
                    (conditionalUpdate != null) || 
                    (conditionalDelete != null) || 
                    !referencePolicy.isEmpty() || 
                    !searchInclude.isEmpty() || 
                    !searchRevInclude.isEmpty() || 
                    !searchParam.isEmpty() || 
                    !operation.isEmpty();
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
                Resource other = (Resource) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(profile, other.profile) && 
                    Objects.equals(supportedProfile, other.supportedProfile) && 
                    Objects.equals(documentation, other.documentation) && 
                    Objects.equals(interaction, other.interaction) && 
                    Objects.equals(versioning, other.versioning) && 
                    Objects.equals(readHistory, other.readHistory) && 
                    Objects.equals(updateCreate, other.updateCreate) && 
                    Objects.equals(conditionalCreate, other.conditionalCreate) && 
                    Objects.equals(conditionalRead, other.conditionalRead) && 
                    Objects.equals(conditionalUpdate, other.conditionalUpdate) && 
                    Objects.equals(conditionalDelete, other.conditionalDelete) && 
                    Objects.equals(referencePolicy, other.referencePolicy) && 
                    Objects.equals(searchInclude, other.searchInclude) && 
                    Objects.equals(searchRevInclude, other.searchRevInclude) && 
                    Objects.equals(searchParam, other.searchParam) && 
                    Objects.equals(operation, other.operation);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        profile, 
                        supportedProfile, 
                        documentation, 
                        interaction, 
                        versioning, 
                        readHistory, 
                        updateCreate, 
                        conditionalCreate, 
                        conditionalRead, 
                        conditionalUpdate, 
                        conditionalDelete, 
                        referencePolicy, 
                        searchInclude, 
                        searchRevInclude, 
                        searchParam, 
                        operation);
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
                 * A type of resource exposed via the restful interface.
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     A resource type that is supported
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(ResourceTypeCode type) {
                    this.type = type;
                    return this;
                }

                /**
                 * A specification of the profile that describes the solution's overall support for the resource, including any 
                 * constraints on cardinality, bindings, lengths or other limitations. See further discussion in [Using Profiles]
                 * (profiling.html#profile-uses).
                 * 
                 * @param profile
                 *     Base System profile for all uses of resource
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder profile(Canonical profile) {
                    this.profile = profile;
                    return this;
                }

                /**
                 * A list of profiles that represent different use cases supported by the system. For a server, "supported by the system" 
                 * means the system hosts/produces a set of resources that are conformant to a particular profile, and allows clients 
                 * that use its services to search using this profile and to find appropriate data. For a client, it means the system 
                 * will search by this profile and process data according to the guidance implicit in the profile. See further discussion 
                 * in [Using Profiles](profiling.html#profile-uses).
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param supportedProfile
                 *     Profiles for use cases supported
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder supportedProfile(Canonical... supportedProfile) {
                    for (Canonical value : supportedProfile) {
                        this.supportedProfile.add(value);
                    }
                    return this;
                }

                /**
                 * A list of profiles that represent different use cases supported by the system. For a server, "supported by the system" 
                 * means the system hosts/produces a set of resources that are conformant to a particular profile, and allows clients 
                 * that use its services to search using this profile and to find appropriate data. For a client, it means the system 
                 * will search by this profile and process data according to the guidance implicit in the profile. See further discussion 
                 * in [Using Profiles](profiling.html#profile-uses).
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param supportedProfile
                 *     Profiles for use cases supported
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder supportedProfile(Collection<Canonical> supportedProfile) {
                    this.supportedProfile = new ArrayList<>(supportedProfile);
                    return this;
                }

                /**
                 * Additional information about the resource type used by the system.
                 * 
                 * @param documentation
                 *     Additional information about the use of the resource type
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder documentation(Markdown documentation) {
                    this.documentation = documentation;
                    return this;
                }

                /**
                 * Identifies a restful operation supported by the solution.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param interaction
                 *     What operations are supported?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder interaction(Interaction... interaction) {
                    for (Interaction value : interaction) {
                        this.interaction.add(value);
                    }
                    return this;
                }

                /**
                 * Identifies a restful operation supported by the solution.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param interaction
                 *     What operations are supported?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder interaction(Collection<Interaction> interaction) {
                    this.interaction = new ArrayList<>(interaction);
                    return this;
                }

                /**
                 * This field is set to no-version to specify that the system does not support (server) or use (client) versioning for 
                 * this resource type. If this has some other value, the server must at least correctly track and populate the versionId 
                 * meta-property on resources. If the value is 'versioned-update', then the server supports all the versioning features, 
                 * including using e-tags for version integrity in the API.
                 * 
                 * @param versioning
                 *     no-version | versioned | versioned-update
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder versioning(ResourceVersionPolicy versioning) {
                    this.versioning = versioning;
                    return this;
                }

                /**
                 * Convenience method for setting {@code readHistory}.
                 * 
                 * @param readHistory
                 *     Whether vRead can return past versions
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #readHistory(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder readHistory(java.lang.Boolean readHistory) {
                    this.readHistory = (readHistory == null) ? null : Boolean.of(readHistory);
                    return this;
                }

                /**
                 * A flag for whether the server is able to return past versions as part of the vRead operation.
                 * 
                 * @param readHistory
                 *     Whether vRead can return past versions
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder readHistory(Boolean readHistory) {
                    this.readHistory = readHistory;
                    return this;
                }

                /**
                 * Convenience method for setting {@code updateCreate}.
                 * 
                 * @param updateCreate
                 *     If update can commit to a new identity
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #updateCreate(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder updateCreate(java.lang.Boolean updateCreate) {
                    this.updateCreate = (updateCreate == null) ? null : Boolean.of(updateCreate);
                    return this;
                }

                /**
                 * A flag to indicate that the server allows or needs to allow the client to create new identities on the server (that 
                 * is, the client PUTs to a location where there is no existing resource). Allowing this operation means that the server 
                 * allows the client to create new identities on the server.
                 * 
                 * @param updateCreate
                 *     If update can commit to a new identity
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder updateCreate(Boolean updateCreate) {
                    this.updateCreate = updateCreate;
                    return this;
                }

                /**
                 * Convenience method for setting {@code conditionalCreate}.
                 * 
                 * @param conditionalCreate
                 *     If allows/uses conditional create
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #conditionalCreate(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder conditionalCreate(java.lang.Boolean conditionalCreate) {
                    this.conditionalCreate = (conditionalCreate == null) ? null : Boolean.of(conditionalCreate);
                    return this;
                }

                /**
                 * A flag that indicates that the server supports conditional create.
                 * 
                 * @param conditionalCreate
                 *     If allows/uses conditional create
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder conditionalCreate(Boolean conditionalCreate) {
                    this.conditionalCreate = conditionalCreate;
                    return this;
                }

                /**
                 * A code that indicates how the server supports conditional read.
                 * 
                 * @param conditionalRead
                 *     not-supported | modified-since | not-match | full-support
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder conditionalRead(ConditionalReadStatus conditionalRead) {
                    this.conditionalRead = conditionalRead;
                    return this;
                }

                /**
                 * Convenience method for setting {@code conditionalUpdate}.
                 * 
                 * @param conditionalUpdate
                 *     If allows/uses conditional update
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #conditionalUpdate(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder conditionalUpdate(java.lang.Boolean conditionalUpdate) {
                    this.conditionalUpdate = (conditionalUpdate == null) ? null : Boolean.of(conditionalUpdate);
                    return this;
                }

                /**
                 * A flag that indicates that the server supports conditional update.
                 * 
                 * @param conditionalUpdate
                 *     If allows/uses conditional update
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder conditionalUpdate(Boolean conditionalUpdate) {
                    this.conditionalUpdate = conditionalUpdate;
                    return this;
                }

                /**
                 * A code that indicates how the server supports conditional delete.
                 * 
                 * @param conditionalDelete
                 *     not-supported | single | multiple - how conditional delete is supported
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder conditionalDelete(ConditionalDeleteStatus conditionalDelete) {
                    this.conditionalDelete = conditionalDelete;
                    return this;
                }

                /**
                 * A set of flags that defines how references are supported.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param referencePolicy
                 *     literal | logical | resolves | enforced | local
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder referencePolicy(ReferenceHandlingPolicy... referencePolicy) {
                    for (ReferenceHandlingPolicy value : referencePolicy) {
                        this.referencePolicy.add(value);
                    }
                    return this;
                }

                /**
                 * A set of flags that defines how references are supported.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param referencePolicy
                 *     literal | logical | resolves | enforced | local
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder referencePolicy(Collection<ReferenceHandlingPolicy> referencePolicy) {
                    this.referencePolicy = new ArrayList<>(referencePolicy);
                    return this;
                }

                /**
                 * Convenience method for setting {@code searchInclude}.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param searchInclude
                 *     _include values supported by the server
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #searchInclude(com.ibm.fhir.model.type.String)
                 */
                public Builder searchInclude(java.lang.String... searchInclude) {
                    for (java.lang.String value : searchInclude) {
                        this.searchInclude.add((value == null) ? null : String.of(value));
                    }
                    return this;
                }

                /**
                 * A list of _include values supported by the server.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param searchInclude
                 *     _include values supported by the server
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder searchInclude(String... searchInclude) {
                    for (String value : searchInclude) {
                        this.searchInclude.add(value);
                    }
                    return this;
                }

                /**
                 * A list of _include values supported by the server.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param searchInclude
                 *     _include values supported by the server
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder searchInclude(Collection<String> searchInclude) {
                    this.searchInclude = new ArrayList<>(searchInclude);
                    return this;
                }

                /**
                 * Convenience method for setting {@code searchRevInclude}.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param searchRevInclude
                 *     _revinclude values supported by the server
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #searchRevInclude(com.ibm.fhir.model.type.String)
                 */
                public Builder searchRevInclude(java.lang.String... searchRevInclude) {
                    for (java.lang.String value : searchRevInclude) {
                        this.searchRevInclude.add((value == null) ? null : String.of(value));
                    }
                    return this;
                }

                /**
                 * A list of _revinclude (reverse include) values supported by the server.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param searchRevInclude
                 *     _revinclude values supported by the server
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder searchRevInclude(String... searchRevInclude) {
                    for (String value : searchRevInclude) {
                        this.searchRevInclude.add(value);
                    }
                    return this;
                }

                /**
                 * A list of _revinclude (reverse include) values supported by the server.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param searchRevInclude
                 *     _revinclude values supported by the server
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder searchRevInclude(Collection<String> searchRevInclude) {
                    this.searchRevInclude = new ArrayList<>(searchRevInclude);
                    return this;
                }

                /**
                 * Search parameters for implementations to support and/or make use of - either references to ones defined in the 
                 * specification, or additional ones defined for/by the implementation.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param searchParam
                 *     Search parameters supported by implementation
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder searchParam(SearchParam... searchParam) {
                    for (SearchParam value : searchParam) {
                        this.searchParam.add(value);
                    }
                    return this;
                }

                /**
                 * Search parameters for implementations to support and/or make use of - either references to ones defined in the 
                 * specification, or additional ones defined for/by the implementation.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param searchParam
                 *     Search parameters supported by implementation
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder searchParam(Collection<SearchParam> searchParam) {
                    this.searchParam = new ArrayList<>(searchParam);
                    return this;
                }

                /**
                 * Definition of an operation or a named query together with its parameters and their meaning and type. Consult the 
                 * definition of the operation for details about how to invoke the operation, and the parameters.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param operation
                 *     Definition of a resource operation
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder operation(Operation... operation) {
                    for (Operation value : operation) {
                        this.operation.add(value);
                    }
                    return this;
                }

                /**
                 * Definition of an operation or a named query together with its parameters and their meaning and type. Consult the 
                 * definition of the operation for details about how to invoke the operation, and the parameters.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param operation
                 *     Definition of a resource operation
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder operation(Collection<Operation> operation) {
                    this.operation = new ArrayList<>(operation);
                    return this;
                }

                /**
                 * Build the {@link Resource}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Resource}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Resource per the base specification
                 */
                @Override
                public Resource build() {
                    Resource resource = new Resource(this);
                    if (validating) {
                        validate(resource);
                    }
                    return resource;
                }

                protected void validate(Resource resource) {
                    super.validate(resource);
                    ValidationSupport.requireNonNull(resource.type, "type");
                    ValidationSupport.checkList(resource.supportedProfile, "supportedProfile", Canonical.class);
                    ValidationSupport.checkList(resource.interaction, "interaction", Interaction.class);
                    ValidationSupport.checkList(resource.referencePolicy, "referencePolicy", ReferenceHandlingPolicy.class);
                    ValidationSupport.checkList(resource.searchInclude, "searchInclude", String.class);
                    ValidationSupport.checkList(resource.searchRevInclude, "searchRevInclude", String.class);
                    ValidationSupport.checkList(resource.searchParam, "searchParam", SearchParam.class);
                    ValidationSupport.checkList(resource.operation, "operation", Operation.class);
                    ValidationSupport.requireValueOrChildren(resource);
                }

                protected Builder from(Resource resource) {
                    super.from(resource);
                    type = resource.type;
                    profile = resource.profile;
                    supportedProfile.addAll(resource.supportedProfile);
                    documentation = resource.documentation;
                    interaction.addAll(resource.interaction);
                    versioning = resource.versioning;
                    readHistory = resource.readHistory;
                    updateCreate = resource.updateCreate;
                    conditionalCreate = resource.conditionalCreate;
                    conditionalRead = resource.conditionalRead;
                    conditionalUpdate = resource.conditionalUpdate;
                    conditionalDelete = resource.conditionalDelete;
                    referencePolicy.addAll(resource.referencePolicy);
                    searchInclude.addAll(resource.searchInclude);
                    searchRevInclude.addAll(resource.searchRevInclude);
                    searchParam.addAll(resource.searchParam);
                    operation.addAll(resource.operation);
                    return this;
                }
            }

            /**
             * Identifies a restful operation supported by the solution.
             */
            public static class Interaction extends BackboneElement {
                @Binding(
                    bindingName = "TypeRestfulInteraction",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "Operations supported by REST at the type or instance level.",
                    valueSet = "http://hl7.org/fhir/ValueSet/type-restful-interaction|4.3.0-CIBUILD"
                )
                @Required
                private final TypeRestfulInteraction code;
                private final Markdown documentation;

                private Interaction(Builder builder) {
                    super(builder);
                    code = builder.code;
                    documentation = builder.documentation;
                }

                /**
                 * Coded identifier of the operation, supported by the system resource.
                 * 
                 * @return
                 *     An immutable object of type {@link TypeRestfulInteraction} that is non-null.
                 */
                public TypeRestfulInteraction getCode() {
                    return code;
                }

                /**
                 * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only 
                 * allowed with version id' or 'creates permitted from pre-authorized certificates only'.
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown} that may be null.
                 */
                public Markdown getDocumentation() {
                    return documentation;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (code != null) || 
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
                            accept(code, "code", visitor);
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
                    Interaction other = (Interaction) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(code, other.code) && 
                        Objects.equals(documentation, other.documentation);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            code, 
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
                    private TypeRestfulInteraction code;
                    private Markdown documentation;

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
                     * Coded identifier of the operation, supported by the system resource.
                     * 
                     * <p>This element is required.
                     * 
                     * @param code
                     *     read | vread | update | patch | delete | history-instance | history-type | create | search-type
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder code(TypeRestfulInteraction code) {
                        this.code = code;
                        return this;
                    }

                    /**
                     * Guidance specific to the implementation of this operation, such as 'delete is a logical delete' or 'updates are only 
                     * allowed with version id' or 'creates permitted from pre-authorized certificates only'.
                     * 
                     * @param documentation
                     *     Anything special about operation behavior
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder documentation(Markdown documentation) {
                        this.documentation = documentation;
                        return this;
                    }

                    /**
                     * Build the {@link Interaction}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>code</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Interaction}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Interaction per the base specification
                     */
                    @Override
                    public Interaction build() {
                        Interaction interaction = new Interaction(this);
                        if (validating) {
                            validate(interaction);
                        }
                        return interaction;
                    }

                    protected void validate(Interaction interaction) {
                        super.validate(interaction);
                        ValidationSupport.requireNonNull(interaction.code, "code");
                        ValidationSupport.requireValueOrChildren(interaction);
                    }

                    protected Builder from(Interaction interaction) {
                        super.from(interaction);
                        code = interaction.code;
                        documentation = interaction.documentation;
                        return this;
                    }
                }
            }

            /**
             * Search parameters for implementations to support and/or make use of - either references to ones defined in the 
             * specification, or additional ones defined for/by the implementation.
             */
            public static class SearchParam extends BackboneElement {
                @Required
                private final String name;
                private final Canonical definition;
                @Binding(
                    bindingName = "SearchParamType",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "Data types allowed to be used for search parameters.",
                    valueSet = "http://hl7.org/fhir/ValueSet/search-param-type|4.3.0-CIBUILD"
                )
                @Required
                private final SearchParamType type;
                private final Markdown documentation;

                private SearchParam(Builder builder) {
                    super(builder);
                    name = builder.name;
                    definition = builder.definition;
                    type = builder.type;
                    documentation = builder.documentation;
                }

                /**
                 * The name of the search parameter used in the interface.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that is non-null.
                 */
                public String getName() {
                    return name;
                }

                /**
                 * An absolute URI that is a formal reference to where this parameter was first defined, so that a client can be 
                 * confident of the meaning of the search parameter (a reference to [SearchParameter.url](searchparameter-definitions.
                 * html#SearchParameter.url)). This element SHALL be populated if the search parameter refers to a SearchParameter 
                 * defined by the FHIR core specification or externally defined IGs.
                 * 
                 * @return
                 *     An immutable object of type {@link Canonical} that may be null.
                 */
                public Canonical getDefinition() {
                    return definition;
                }

                /**
                 * The type of value a search parameter refers to, and how the content is interpreted.
                 * 
                 * @return
                 *     An immutable object of type {@link SearchParamType} that is non-null.
                 */
                public SearchParamType getType() {
                    return type;
                }

                /**
                 * This allows documentation of any distinct behaviors about how the search parameter is used. For example, text matching 
                 * algorithms.
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown} that may be null.
                 */
                public Markdown getDocumentation() {
                    return documentation;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (name != null) || 
                        (definition != null) || 
                        (type != null) || 
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
                            accept(definition, "definition", visitor);
                            accept(type, "type", visitor);
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
                    SearchParam other = (SearchParam) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(name, other.name) && 
                        Objects.equals(definition, other.definition) && 
                        Objects.equals(type, other.type) && 
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
                            definition, 
                            type, 
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
                    private String name;
                    private Canonical definition;
                    private SearchParamType type;
                    private Markdown documentation;

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
                     *     Name of search parameter
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
                     * The name of the search parameter used in the interface.
                     * 
                     * <p>This element is required.
                     * 
                     * @param name
                     *     Name of search parameter
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder name(String name) {
                        this.name = name;
                        return this;
                    }

                    /**
                     * An absolute URI that is a formal reference to where this parameter was first defined, so that a client can be 
                     * confident of the meaning of the search parameter (a reference to [SearchParameter.url](searchparameter-definitions.
                     * html#SearchParameter.url)). This element SHALL be populated if the search parameter refers to a SearchParameter 
                     * defined by the FHIR core specification or externally defined IGs.
                     * 
                     * @param definition
                     *     Source of definition for parameter
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder definition(Canonical definition) {
                        this.definition = definition;
                        return this;
                    }

                    /**
                     * The type of value a search parameter refers to, and how the content is interpreted.
                     * 
                     * <p>This element is required.
                     * 
                     * @param type
                     *     number | date | string | token | reference | composite | quantity | uri | special
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder type(SearchParamType type) {
                        this.type = type;
                        return this;
                    }

                    /**
                     * This allows documentation of any distinct behaviors about how the search parameter is used. For example, text matching 
                     * algorithms.
                     * 
                     * @param documentation
                     *     Server-specific usage
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder documentation(Markdown documentation) {
                        this.documentation = documentation;
                        return this;
                    }

                    /**
                     * Build the {@link SearchParam}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>name</li>
                     * <li>type</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link SearchParam}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid SearchParam per the base specification
                     */
                    @Override
                    public SearchParam build() {
                        SearchParam searchParam = new SearchParam(this);
                        if (validating) {
                            validate(searchParam);
                        }
                        return searchParam;
                    }

                    protected void validate(SearchParam searchParam) {
                        super.validate(searchParam);
                        ValidationSupport.requireNonNull(searchParam.name, "name");
                        ValidationSupport.requireNonNull(searchParam.type, "type");
                        ValidationSupport.requireValueOrChildren(searchParam);
                    }

                    protected Builder from(SearchParam searchParam) {
                        super.from(searchParam);
                        name = searchParam.name;
                        definition = searchParam.definition;
                        type = searchParam.type;
                        documentation = searchParam.documentation;
                        return this;
                    }
                }
            }

            /**
             * Definition of an operation or a named query together with its parameters and their meaning and type. Consult the 
             * definition of the operation for details about how to invoke the operation, and the parameters.
             */
            public static class Operation extends BackboneElement {
                @Summary
                @Required
                private final String name;
                @Summary
                @Required
                private final Canonical definition;
                private final Markdown documentation;

                private Operation(Builder builder) {
                    super(builder);
                    name = builder.name;
                    definition = builder.definition;
                    documentation = builder.documentation;
                }

                /**
                 * The name of the operation or query. For an operation, this is the name prefixed with $ and used in the URL. For a 
                 * query, this is the name used in the _query parameter when the query is called.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that is non-null.
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Where the formal definition can be found. If a server references the base definition of an Operation (i.e. from the 
                 * specification itself such as ```http://hl7.org/fhir/OperationDefinition/ValueSet-expand```), that means it supports 
                 * the full capabilities of the operation - e.g. both GET and POST invocation. If it only supports a subset, it must 
                 * define its own custom [OperationDefinition](operationdefinition.html#) with a 'base' of the original 
                 * OperationDefinition. The custom definition would describe the specific subset of functionality supported.
                 * 
                 * @return
                 *     An immutable object of type {@link Canonical} that is non-null.
                 */
                public Canonical getDefinition() {
                    return definition;
                }

                /**
                 * Documentation that describes anything special about the operation behavior, possibly detailing different behavior for 
                 * system, type and instance-level invocation of the operation.
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown} that may be null.
                 */
                public Markdown getDocumentation() {
                    return documentation;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (name != null) || 
                        (definition != null) || 
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
                            accept(definition, "definition", visitor);
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
                    Operation other = (Operation) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(name, other.name) && 
                        Objects.equals(definition, other.definition) && 
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
                            definition, 
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
                    private String name;
                    private Canonical definition;
                    private Markdown documentation;

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
                     *     Name by which the operation/query is invoked
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
                     * The name of the operation or query. For an operation, this is the name prefixed with $ and used in the URL. For a 
                     * query, this is the name used in the _query parameter when the query is called.
                     * 
                     * <p>This element is required.
                     * 
                     * @param name
                     *     Name by which the operation/query is invoked
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder name(String name) {
                        this.name = name;
                        return this;
                    }

                    /**
                     * Where the formal definition can be found. If a server references the base definition of an Operation (i.e. from the 
                     * specification itself such as ```http://hl7.org/fhir/OperationDefinition/ValueSet-expand```), that means it supports 
                     * the full capabilities of the operation - e.g. both GET and POST invocation. If it only supports a subset, it must 
                     * define its own custom [OperationDefinition](operationdefinition.html#) with a 'base' of the original 
                     * OperationDefinition. The custom definition would describe the specific subset of functionality supported.
                     * 
                     * <p>This element is required.
                     * 
                     * @param definition
                     *     The defined operation/query
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder definition(Canonical definition) {
                        this.definition = definition;
                        return this;
                    }

                    /**
                     * Documentation that describes anything special about the operation behavior, possibly detailing different behavior for 
                     * system, type and instance-level invocation of the operation.
                     * 
                     * @param documentation
                     *     Specific details about operation behavior
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder documentation(Markdown documentation) {
                        this.documentation = documentation;
                        return this;
                    }

                    /**
                     * Build the {@link Operation}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>name</li>
                     * <li>definition</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Operation}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Operation per the base specification
                     */
                    @Override
                    public Operation build() {
                        Operation operation = new Operation(this);
                        if (validating) {
                            validate(operation);
                        }
                        return operation;
                    }

                    protected void validate(Operation operation) {
                        super.validate(operation);
                        ValidationSupport.requireNonNull(operation.name, "name");
                        ValidationSupport.requireNonNull(operation.definition, "definition");
                        ValidationSupport.requireValueOrChildren(operation);
                    }

                    protected Builder from(Operation operation) {
                        super.from(operation);
                        name = operation.name;
                        definition = operation.definition;
                        documentation = operation.documentation;
                        return this;
                    }
                }
            }
        }

        /**
         * A specification of restful operations supported by the system.
         */
        public static class Interaction extends BackboneElement {
            @Binding(
                bindingName = "SystemRestfulInteraction",
                strength = BindingStrength.Value.REQUIRED,
                description = "Operations supported by REST at the system level.",
                valueSet = "http://hl7.org/fhir/ValueSet/system-restful-interaction|4.3.0-CIBUILD"
            )
            @Required
            private final SystemRestfulInteraction code;
            private final Markdown documentation;

            private Interaction(Builder builder) {
                super(builder);
                code = builder.code;
                documentation = builder.documentation;
            }

            /**
             * A coded identifier of the operation, supported by the system.
             * 
             * @return
             *     An immutable object of type {@link SystemRestfulInteraction} that is non-null.
             */
            public SystemRestfulInteraction getCode() {
                return code;
            }

            /**
             * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or 
             * information about system wide search is implemented.
             * 
             * @return
             *     An immutable object of type {@link Markdown} that may be null.
             */
            public Markdown getDocumentation() {
                return documentation;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (code != null) || 
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
                        accept(code, "code", visitor);
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
                Interaction other = (Interaction) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(documentation, other.documentation);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        code, 
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
                private SystemRestfulInteraction code;
                private Markdown documentation;

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
                 * A coded identifier of the operation, supported by the system.
                 * 
                 * <p>This element is required.
                 * 
                 * @param code
                 *     transaction | batch | search-system | history-system
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(SystemRestfulInteraction code) {
                    this.code = code;
                    return this;
                }

                /**
                 * Guidance specific to the implementation of this operation, such as limitations on the kind of transactions allowed, or 
                 * information about system wide search is implemented.
                 * 
                 * @param documentation
                 *     Anything special about operation behavior
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder documentation(Markdown documentation) {
                    this.documentation = documentation;
                    return this;
                }

                /**
                 * Build the {@link Interaction}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>code</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Interaction}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Interaction per the base specification
                 */
                @Override
                public Interaction build() {
                    Interaction interaction = new Interaction(this);
                    if (validating) {
                        validate(interaction);
                    }
                    return interaction;
                }

                protected void validate(Interaction interaction) {
                    super.validate(interaction);
                    ValidationSupport.requireNonNull(interaction.code, "code");
                    ValidationSupport.requireValueOrChildren(interaction);
                }

                protected Builder from(Interaction interaction) {
                    super.from(interaction);
                    code = interaction.code;
                    documentation = interaction.documentation;
                    return this;
                }
            }
        }
    }

    /**
     * A description of the messaging capabilities of the solution.
     */
    public static class Messaging extends BackboneElement {
        private final List<Endpoint> endpoint;
        private final UnsignedInt reliableCache;
        private final Markdown documentation;
        @Summary
        private final List<SupportedMessage> supportedMessage;

        private Messaging(Builder builder) {
            super(builder);
            endpoint = Collections.unmodifiableList(builder.endpoint);
            reliableCache = builder.reliableCache;
            documentation = builder.documentation;
            supportedMessage = Collections.unmodifiableList(builder.supportedMessage);
        }

        /**
         * An endpoint (network accessible address) to which messages and/or replies are to be sent.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Endpoint} that may be empty.
         */
        public List<Endpoint> getEndpoint() {
            return endpoint;
        }

        /**
         * Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the 
         * receiver should be (if a sender).
         * 
         * @return
         *     An immutable object of type {@link UnsignedInt} that may be null.
         */
        public UnsignedInt getReliableCache() {
            return reliableCache;
        }

        /**
         * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the capability 
         * statement. For example, the process for becoming an authorized messaging exchange partner.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getDocumentation() {
            return documentation;
        }

        /**
         * References to message definitions for messages this system can send or receive.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link SupportedMessage} that may be empty.
         */
        public List<SupportedMessage> getSupportedMessage() {
            return supportedMessage;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !endpoint.isEmpty() || 
                (reliableCache != null) || 
                (documentation != null) || 
                !supportedMessage.isEmpty();
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
                    accept(endpoint, "endpoint", visitor, Endpoint.class);
                    accept(reliableCache, "reliableCache", visitor);
                    accept(documentation, "documentation", visitor);
                    accept(supportedMessage, "supportedMessage", visitor, SupportedMessage.class);
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
            Messaging other = (Messaging) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(endpoint, other.endpoint) && 
                Objects.equals(reliableCache, other.reliableCache) && 
                Objects.equals(documentation, other.documentation) && 
                Objects.equals(supportedMessage, other.supportedMessage);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    endpoint, 
                    reliableCache, 
                    documentation, 
                    supportedMessage);
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
            private List<Endpoint> endpoint = new ArrayList<>();
            private UnsignedInt reliableCache;
            private Markdown documentation;
            private List<SupportedMessage> supportedMessage = new ArrayList<>();

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
             * An endpoint (network accessible address) to which messages and/or replies are to be sent.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param endpoint
             *     Where messages should be sent
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder endpoint(Endpoint... endpoint) {
                for (Endpoint value : endpoint) {
                    this.endpoint.add(value);
                }
                return this;
            }

            /**
             * An endpoint (network accessible address) to which messages and/or replies are to be sent.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param endpoint
             *     Where messages should be sent
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder endpoint(Collection<Endpoint> endpoint) {
                this.endpoint = new ArrayList<>(endpoint);
                return this;
            }

            /**
             * Length if the receiver's reliable messaging cache in minutes (if a receiver) or how long the cache length on the 
             * receiver should be (if a sender).
             * 
             * @param reliableCache
             *     Reliable Message Cache Length (min)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reliableCache(UnsignedInt reliableCache) {
                this.reliableCache = reliableCache;
                return this;
            }

            /**
             * Documentation about the system's messaging capabilities for this endpoint not otherwise documented by the capability 
             * statement. For example, the process for becoming an authorized messaging exchange partner.
             * 
             * @param documentation
             *     Messaging interface behavior details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(Markdown documentation) {
                this.documentation = documentation;
                return this;
            }

            /**
             * References to message definitions for messages this system can send or receive.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param supportedMessage
             *     Messages supported by this system
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder supportedMessage(SupportedMessage... supportedMessage) {
                for (SupportedMessage value : supportedMessage) {
                    this.supportedMessage.add(value);
                }
                return this;
            }

            /**
             * References to message definitions for messages this system can send or receive.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param supportedMessage
             *     Messages supported by this system
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder supportedMessage(Collection<SupportedMessage> supportedMessage) {
                this.supportedMessage = new ArrayList<>(supportedMessage);
                return this;
            }

            /**
             * Build the {@link Messaging}
             * 
             * @return
             *     An immutable object of type {@link Messaging}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Messaging per the base specification
             */
            @Override
            public Messaging build() {
                Messaging messaging = new Messaging(this);
                if (validating) {
                    validate(messaging);
                }
                return messaging;
            }

            protected void validate(Messaging messaging) {
                super.validate(messaging);
                ValidationSupport.checkList(messaging.endpoint, "endpoint", Endpoint.class);
                ValidationSupport.checkList(messaging.supportedMessage, "supportedMessage", SupportedMessage.class);
                ValidationSupport.requireValueOrChildren(messaging);
            }

            protected Builder from(Messaging messaging) {
                super.from(messaging);
                endpoint.addAll(messaging.endpoint);
                reliableCache = messaging.reliableCache;
                documentation = messaging.documentation;
                supportedMessage.addAll(messaging.supportedMessage);
                return this;
            }
        }

        /**
         * An endpoint (network accessible address) to which messages and/or replies are to be sent.
         */
        public static class Endpoint extends BackboneElement {
            @Binding(
                bindingName = "MessageTransport",
                strength = BindingStrength.Value.EXTENSIBLE,
                description = "The protocol used for message transport.",
                valueSet = "http://hl7.org/fhir/ValueSet/message-transport"
            )
            @Required
            private final Coding protocol;
            @Required
            private final Url address;

            private Endpoint(Builder builder) {
                super(builder);
                protocol = builder.protocol;
                address = builder.address;
            }

            /**
             * A list of the messaging transport protocol(s) identifiers, supported by this endpoint.
             * 
             * @return
             *     An immutable object of type {@link Coding} that is non-null.
             */
            public Coding getProtocol() {
                return protocol;
            }

            /**
             * The network address of the endpoint. For solutions that do not use network addresses for routing, it can be just an 
             * identifier.
             * 
             * @return
             *     An immutable object of type {@link Url} that is non-null.
             */
            public Url getAddress() {
                return address;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (protocol != null) || 
                    (address != null);
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
                        accept(protocol, "protocol", visitor);
                        accept(address, "address", visitor);
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
                Endpoint other = (Endpoint) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(protocol, other.protocol) && 
                    Objects.equals(address, other.address);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        protocol, 
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
                private Coding protocol;
                private Url address;

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
                 * A list of the messaging transport protocol(s) identifiers, supported by this endpoint.
                 * 
                 * <p>This element is required.
                 * 
                 * @param protocol
                 *     http | ftp | mllp +
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder protocol(Coding protocol) {
                    this.protocol = protocol;
                    return this;
                }

                /**
                 * The network address of the endpoint. For solutions that do not use network addresses for routing, it can be just an 
                 * identifier.
                 * 
                 * <p>This element is required.
                 * 
                 * @param address
                 *     Network address or identifier of the end-point
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder address(Url address) {
                    this.address = address;
                    return this;
                }

                /**
                 * Build the {@link Endpoint}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>protocol</li>
                 * <li>address</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Endpoint}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Endpoint per the base specification
                 */
                @Override
                public Endpoint build() {
                    Endpoint endpoint = new Endpoint(this);
                    if (validating) {
                        validate(endpoint);
                    }
                    return endpoint;
                }

                protected void validate(Endpoint endpoint) {
                    super.validate(endpoint);
                    ValidationSupport.requireNonNull(endpoint.protocol, "protocol");
                    ValidationSupport.requireNonNull(endpoint.address, "address");
                    ValidationSupport.requireValueOrChildren(endpoint);
                }

                protected Builder from(Endpoint endpoint) {
                    super.from(endpoint);
                    protocol = endpoint.protocol;
                    address = endpoint.address;
                    return this;
                }
            }
        }

        /**
         * References to message definitions for messages this system can send or receive.
         */
        public static class SupportedMessage extends BackboneElement {
            @Summary
            @Binding(
                bindingName = "EventCapabilityMode",
                strength = BindingStrength.Value.REQUIRED,
                description = "The mode of a message capability statement.",
                valueSet = "http://hl7.org/fhir/ValueSet/event-capability-mode|4.3.0-CIBUILD"
            )
            @Required
            private final EventCapabilityMode mode;
            @Summary
            @Required
            private final Canonical definition;

            private SupportedMessage(Builder builder) {
                super(builder);
                mode = builder.mode;
                definition = builder.definition;
            }

            /**
             * The mode of this event declaration - whether application is sender or receiver.
             * 
             * @return
             *     An immutable object of type {@link EventCapabilityMode} that is non-null.
             */
            public EventCapabilityMode getMode() {
                return mode;
            }

            /**
             * Points to a message definition that identifies the messaging event, message structure, allowed responses, etc.
             * 
             * @return
             *     An immutable object of type {@link Canonical} that is non-null.
             */
            public Canonical getDefinition() {
                return definition;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (mode != null) || 
                    (definition != null);
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
                        accept(mode, "mode", visitor);
                        accept(definition, "definition", visitor);
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
                SupportedMessage other = (SupportedMessage) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(mode, other.mode) && 
                    Objects.equals(definition, other.definition);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        mode, 
                        definition);
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
                private EventCapabilityMode mode;
                private Canonical definition;

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
                 * The mode of this event declaration - whether application is sender or receiver.
                 * 
                 * <p>This element is required.
                 * 
                 * @param mode
                 *     sender | receiver
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder mode(EventCapabilityMode mode) {
                    this.mode = mode;
                    return this;
                }

                /**
                 * Points to a message definition that identifies the messaging event, message structure, allowed responses, etc.
                 * 
                 * <p>This element is required.
                 * 
                 * @param definition
                 *     Message supported by this system
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder definition(Canonical definition) {
                    this.definition = definition;
                    return this;
                }

                /**
                 * Build the {@link SupportedMessage}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>mode</li>
                 * <li>definition</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link SupportedMessage}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid SupportedMessage per the base specification
                 */
                @Override
                public SupportedMessage build() {
                    SupportedMessage supportedMessage = new SupportedMessage(this);
                    if (validating) {
                        validate(supportedMessage);
                    }
                    return supportedMessage;
                }

                protected void validate(SupportedMessage supportedMessage) {
                    super.validate(supportedMessage);
                    ValidationSupport.requireNonNull(supportedMessage.mode, "mode");
                    ValidationSupport.requireNonNull(supportedMessage.definition, "definition");
                    ValidationSupport.requireValueOrChildren(supportedMessage);
                }

                protected Builder from(SupportedMessage supportedMessage) {
                    super.from(supportedMessage);
                    mode = supportedMessage.mode;
                    definition = supportedMessage.definition;
                    return this;
                }
            }
        }
    }

    /**
     * A document definition.
     */
    public static class Document extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "DocumentMode",
            strength = BindingStrength.Value.REQUIRED,
            description = "Whether the application produces or consumes documents.",
            valueSet = "http://hl7.org/fhir/ValueSet/document-mode|4.3.0-CIBUILD"
        )
        @Required
        private final DocumentMode mode;
        private final Markdown documentation;
        @Summary
        @Required
        private final Canonical profile;

        private Document(Builder builder) {
            super(builder);
            mode = builder.mode;
            documentation = builder.documentation;
            profile = builder.profile;
        }

        /**
         * Mode of this document declaration - whether an application is a producer or consumer.
         * 
         * @return
         *     An immutable object of type {@link DocumentMode} that is non-null.
         */
        public DocumentMode getMode() {
            return mode;
        }

        /**
         * A description of how the application supports or uses the specified document profile. For example, when documents are 
         * created, what action is taken with consumed documents, etc.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getDocumentation() {
            return documentation;
        }

        /**
         * A profile on the document Bundle that constrains which resources are present, and their contents.
         * 
         * @return
         *     An immutable object of type {@link Canonical} that is non-null.
         */
        public Canonical getProfile() {
            return profile;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (mode != null) || 
                (documentation != null) || 
                (profile != null);
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
                    accept(mode, "mode", visitor);
                    accept(documentation, "documentation", visitor);
                    accept(profile, "profile", visitor);
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
            Document other = (Document) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(mode, other.mode) && 
                Objects.equals(documentation, other.documentation) && 
                Objects.equals(profile, other.profile);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    mode, 
                    documentation, 
                    profile);
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
            private DocumentMode mode;
            private Markdown documentation;
            private Canonical profile;

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
             * Mode of this document declaration - whether an application is a producer or consumer.
             * 
             * <p>This element is required.
             * 
             * @param mode
             *     producer | consumer
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder mode(DocumentMode mode) {
                this.mode = mode;
                return this;
            }

            /**
             * A description of how the application supports or uses the specified document profile. For example, when documents are 
             * created, what action is taken with consumed documents, etc.
             * 
             * @param documentation
             *     Description of document support
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(Markdown documentation) {
                this.documentation = documentation;
                return this;
            }

            /**
             * A profile on the document Bundle that constrains which resources are present, and their contents.
             * 
             * <p>This element is required.
             * 
             * @param profile
             *     Constraint on the resources used in the document
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder profile(Canonical profile) {
                this.profile = profile;
                return this;
            }

            /**
             * Build the {@link Document}
             * 
             * <p>Required elements:
             * <ul>
             * <li>mode</li>
             * <li>profile</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Document}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Document per the base specification
             */
            @Override
            public Document build() {
                Document document = new Document(this);
                if (validating) {
                    validate(document);
                }
                return document;
            }

            protected void validate(Document document) {
                super.validate(document);
                ValidationSupport.requireNonNull(document.mode, "mode");
                ValidationSupport.requireNonNull(document.profile, "profile");
                ValidationSupport.requireValueOrChildren(document);
            }

            protected Builder from(Document document) {
                super.from(document);
                mode = document.mode;
                documentation = document.documentation;
                profile = document.profile;
                return this;
            }
        }
    }
}
