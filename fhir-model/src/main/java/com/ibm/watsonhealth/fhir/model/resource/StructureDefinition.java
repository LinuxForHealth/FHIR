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

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.ElementDefinition;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.ExtensionContextType;
import com.ibm.watsonhealth.fhir.model.type.FHIRVersion;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.StructureDefinitionKind;
import com.ibm.watsonhealth.fhir.model.type.TypeDerivationRule;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in 
 * FHIR, and also for describing extensions and constraints on resources and data types.
 * </p>
 */
@Constraint(
    id = "sdf-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Constraint(
    id = "sdf-1",
    level = "Rule",
    location = "(base)",
    description = "Element paths must be unique unless the structure is a constraint",
    expression = "derivation = 'constraint' or snapshot.element.select(path).isDistinct()"
)
@Constraint(
    id = "sdf-2",
    level = "Rule",
    location = "StructureDefinition.mapping",
    description = "Must have at least a name or a uri (or both)",
    expression = "name.exists() or uri.exists()"
)
@Constraint(
    id = "sdf-3",
    level = "Rule",
    location = "StructureDefinition.snapshot",
    description = "Each element definition in a snapshot must have a formal definition and cardinalities",
    expression = "element.all(definition.exists() and min.exists() and max.exists())"
)
@Constraint(
    id = "sdf-4",
    level = "Rule",
    location = "(base)",
    description = "If the structure is not abstract, then there SHALL be a baseDefinition",
    expression = "abstract = true or baseDefinition.exists()"
)
@Constraint(
    id = "sdf-5",
    level = "Rule",
    location = "(base)",
    description = "If the structure defines an extension then the structure must have context information",
    expression = "type != 'Extension' or derivation = 'specialization' or (context.exists())"
)
@Constraint(
    id = "sdf-6",
    level = "Rule",
    location = "(base)",
    description = "A structure must have either a differential, or a snapshot (or both)",
    expression = "snapshot.exists() or differential.exists()"
)
@Constraint(
    id = "sdf-8",
    level = "Rule",
    location = "StructureDefinition.snapshot",
    description = "All snapshot elements must start with the StructureDefinition's specified type for non-logical models, or with the same type name for logical models",
    expression = "(%resource.kind = 'logical' or element.first().path = %resource.type) and element.tail().all(path.startsWith(%resource.snapshot.element.first().path&'.'))"
)
@Constraint(
    id = "sdf-8a",
    level = "Rule",
    location = "StructureDefinition.differential",
    description = "In any differential, all the elements must start with the StructureDefinition's specified type for non-logical models, or with the same type name for logical models",
    expression = "(%resource.kind = 'logical' or element.first().path.startsWith(%resource.type)) and (element.tail().not() or  element.tail().all(path.startsWith(%resource.differential.element.first().path.replaceMatches('\\..*','')&'.')))"
)
@Constraint(
    id = "sdf-8b",
    level = "Rule",
    location = "StructureDefinition.snapshot",
    description = "All snapshot elements must have a base definition",
    expression = "element.all(base.exists())"
)
@Constraint(
    id = "sdf-9",
    level = "Rule",
    location = "(base)",
    description = "In any snapshot or differential, no label, code or requirements on an element without a \".\" in the path (e.g. the first element)",
    expression = "children().element.where(path.contains('.').not()).label.empty() and children().element.where(path.contains('.').not()).code.empty() and children().element.where(path.contains('.').not()).requirements.empty()"
)
@Constraint(
    id = "sdf-10",
    level = "Rule",
    location = "StructureDefinition.snapshot.element",
    description = "provide either a binding reference or a description (or both)",
    expression = "binding.empty() or binding.valueSet.exists() or binding.description.exists()"
)
@Constraint(
    id = "sdf-11",
    level = "Rule",
    location = "(base)",
    description = "If there's a type, its content must match the path name in the first element of a snapshot",
    expression = "kind != 'logical' implies snapshot.empty() or snapshot.element.first().path = type"
)
@Constraint(
    id = "sdf-14",
    level = "Rule",
    location = "(base)",
    description = "All element definitions must have an id",
    expression = "snapshot.element.all(id.exists()) and differential.element.all(id.exists())"
)
@Constraint(
    id = "sdf-15",
    level = "Rule",
    location = "(base)",
    description = "The first element in a snapshot has no type unless model is a logical model.",
    expression = "kind!='logical'  implies snapshot.element.first().type.empty()"
)
@Constraint(
    id = "sdf-15a",
    level = "Rule",
    location = "(base)",
    description = "If the first element in a differential has no \".\" in the path and it's not a logical model, it has no type",
    expression = "(kind!='logical'  and differential.element.first().path.contains('.').not()) implies differential.element.first().type.empty()"
)
@Constraint(
    id = "sdf-16",
    level = "Rule",
    location = "(base)",
    description = "All element definitions must have unique ids (snapshot)",
    expression = "snapshot.element.all(id) and snapshot.element.id.trace('ids').isDistinct()"
)
@Constraint(
    id = "sdf-17",
    level = "Rule",
    location = "(base)",
    description = "All element definitions must have unique ids (diff)",
    expression = "differential.element.all(id) and differential.element.id.trace('ids').isDistinct()"
)
@Constraint(
    id = "sdf-18",
    level = "Rule",
    location = "(base)",
    description = "Context Invariants can only be used for extensions",
    expression = "contextInvariant.exists() implies type = 'Extension'"
)
@Constraint(
    id = "sdf-19",
    level = "Rule",
    location = "(base)",
    description = "FHIR Specification models only use FHIR defined types",
    expression = "url.startsWith('http://hl7.org/fhir/StructureDefinition') implies (differential.element.type.code.all(hasValue() implies matches('^[a-zA-Z0-9]+$')) and snapshot.element.type.code.all(hasValue() implies matches('^[a-zA-Z0-9]+$')))"
)
@Constraint(
    id = "sdf-20",
    level = "Rule",
    location = "StructureDefinition.differential",
    description = "No slicing on the root element",
    expression = "element.where(path.contains('.').not()).slicing.empty()"
)
@Constraint(
    id = "sdf-21",
    level = "Rule",
    location = "(base)",
    description = "Default values can only be specified on specializations",
    expression = "differential.element.defaultValue.exists() implies (derivation = 'specialization')"
)
@Constraint(
    id = "sdf-22",
    level = "Rule",
    location = "(base)",
    description = "FHIR Specification models never have default values",
    expression = "url.startsWith('http://hl7.org/fhir/StructureDefinition') implies (snapshot.element.defaultValue.empty() and differential.element.defaultValue.empty())"
)
@Constraint(
    id = "sdf-23",
    level = "Rule",
    location = "(base)",
    description = "No slice name on root",
    expression = "(snapshot | differential).element.all(path.contains('.').not() implies sliceName.empty())"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class StructureDefinition extends DomainResource {
    private final Uri url;
    private final List<Identifier> identifier;
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
    private final List<Coding> keyword;
    private final FHIRVersion fhirVersion;
    private final List<Mapping> mapping;
    private final StructureDefinitionKind kind;
    private final Boolean _abstract;
    private final List<Context> context;
    private final List<String> contextInvariant;
    private final Uri type;
    private final Canonical baseDefinition;
    private final TypeDerivationRule derivation;
    private final Snapshot snapshot;
    private final Differential differential;

    private StructureDefinition(Builder builder) {
        super(builder);
        url = ValidationSupport.requireNonNull(builder.url, "url");
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = ValidationSupport.requireNonNull(builder.name, "name");
        title = builder.title;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        experimental = builder.experimental;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        purpose = builder.purpose;
        copyright = builder.copyright;
        keyword = Collections.unmodifiableList(builder.keyword);
        fhirVersion = builder.fhirVersion;
        mapping = Collections.unmodifiableList(builder.mapping);
        kind = ValidationSupport.requireNonNull(builder.kind, "kind");
        _abstract = ValidationSupport.requireNonNull(builder._abstract, "abstract");
        context = Collections.unmodifiableList(builder.context);
        contextInvariant = Collections.unmodifiableList(builder.contextInvariant);
        type = ValidationSupport.requireNonNull(builder.type, "type");
        baseDefinition = builder.baseDefinition;
        derivation = builder.derivation;
        snapshot = builder.snapshot;
        differential = builder.differential;
    }

    /**
     * <p>
     * An absolute URI that is used to identify this structure definition when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this structure definition is (or will be) published. This URL 
     * can be the target of a canonical reference. It SHALL remain the same when the structure definition is stored on 
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
     * A formal identifier that is used to identify this structure definition when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
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
     * The identifier that is used to identify this version of the structure definition when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the structure definition author and is 
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
     * A natural language name identifying the structure definition. This name should be usable as an identifier for the 
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
     * A short, descriptive, user-friendly title for the structure definition.
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
     * The status of this structure definition. Enables tracking the life-cycle of the content.
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
     * A Boolean value to indicate that this structure definition is authored for testing purposes (or 
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
     * The date (and optionally time) when the structure definition was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the structure definition changes.
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
     * The name of the organization or individual that published the structure definition.
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
     * A free text natural language description of the structure definition from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate structure definition instances.
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
     * A legal or geographic region in which the structure definition is intended to be used.
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
     * Explanation of why this structure definition is needed and why it has been designed as it has.
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
     * A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the structure definition.
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
     * A set of key words or terms from external terminologies that may be used to assist with indexing and searching of 
     * templates nby describing the use of this structure definition, or the content it describes.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Coding}.
     */
    public List<Coding> getKeyword() {
        return keyword;
    }

    /**
     * <p>
     * The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the 
     * specification, without the revision number, e.g. [publication].[major].[minor], which is 4.0.0. for this version.
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
     * An external specification that the content is mapped to.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Mapping}.
     */
    public List<Mapping> getMapping() {
        return mapping;
    }

    /**
     * <p>
     * Defines the kind of structure that this definition is describing.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link StructureDefinitionKind}.
     */
    public StructureDefinitionKind getKind() {
        return kind;
    }

    /**
     * <p>
     * Whether structure this definition describes is abstract or not - that is, whether the structure is not intended to be 
     * instantiated. For Resources and Data types, abstract types will never be exchanged between systems.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getabstract() {
        return _abstract;
    }

    /**
     * <p>
     * Identifies the types of resource or data type elements to which the extension can be applied.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Context}.
     */
    public List<Context> getContext() {
        return context;
    }

    /**
     * <p>
     * A set of rules as FHIRPath Invariants about when the extension can be used (e.g. co-occurrence variants for the 
     * extension). All the rules must be true.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link String}.
     */
    public List<String> getContextInvariant() {
        return contextInvariant;
    }

    /**
     * <p>
     * The type this structure describes. If the derivation kind is 'specialization' then this is the master definition for a 
     * type, and there is always one of these (a data type, an extension, a resource, including abstract ones). Otherwise the 
     * structure definition is a constraint on the stated type (and in this case, the type cannot be an abstract type). 
     * References are URLs that are relative to http://hl7.org/fhir/StructureDefinition e.g. "string" is a reference to http:
     * //hl7.org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Uri}.
     */
    public Uri getType() {
        return type;
    }

    /**
     * <p>
     * An absolute URI that is the base structure from which this type is derived, either by specialization or constraint.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getBaseDefinition() {
        return baseDefinition;
    }

    /**
     * <p>
     * How the type relates to the baseDefinition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link TypeDerivationRule}.
     */
    public TypeDerivationRule getDerivation() {
        return derivation;
    }

    /**
     * <p>
     * A snapshot view is expressed in a standalone form that can be used and interpreted without considering the base 
     * StructureDefinition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Snapshot}.
     */
    public Snapshot getSnapshot() {
        return snapshot;
    }

    /**
     * <p>
     * A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Differential}.
     */
    public Differential getDifferential() {
        return differential;
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
                accept(identifier, "identifier", visitor, Identifier.class);
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
                accept(keyword, "keyword", visitor, Coding.class);
                accept(fhirVersion, "fhirVersion", visitor);
                accept(mapping, "mapping", visitor, Mapping.class);
                accept(kind, "kind", visitor);
                accept(_abstract, "abstract", visitor);
                accept(context, "context", visitor, Context.class);
                accept(contextInvariant, "contextInvariant", visitor, String.class);
                accept(type, "type", visitor);
                accept(baseDefinition, "baseDefinition", visitor);
                accept(derivation, "derivation", visitor);
                accept(snapshot, "snapshot", visitor);
                accept(differential, "differential", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        return new Builder(url, name, status, kind, _abstract, type).from(this);
    }

    public Builder toBuilder(Uri url, String name, PublicationStatus status, StructureDefinitionKind kind, Boolean _abstract, Uri type) {
        return new Builder(url, name, status, kind, _abstract, type).from(this);
    }

    public static Builder builder(Uri url, String name, PublicationStatus status, StructureDefinitionKind kind, Boolean _abstract, Uri type) {
        return new Builder(url, name, status, kind, _abstract, type);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Uri url;
        private final String name;
        private final PublicationStatus status;
        private final StructureDefinitionKind kind;
        private final Boolean _abstract;
        private final Uri type;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private String version;
        private String title;
        private Boolean experimental;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private Markdown copyright;
        private List<Coding> keyword = new ArrayList<>();
        private FHIRVersion fhirVersion;
        private List<Mapping> mapping = new ArrayList<>();
        private List<Context> context = new ArrayList<>();
        private List<String> contextInvariant = new ArrayList<>();
        private Canonical baseDefinition;
        private TypeDerivationRule derivation;
        private Snapshot snapshot;
        private Differential differential;

        private Builder(Uri url, String name, PublicationStatus status, StructureDefinitionKind kind, Boolean _abstract, Uri type) {
            super();
            this.url = url;
            this.name = name;
            this.status = status;
            this.kind = kind;
            this._abstract = _abstract;
            this.type = type;
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
         * A formal identifier that is used to identify this structure definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the structure definition
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
         * A formal identifier that is used to identify this structure definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the structure definition
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
         * The identifier that is used to identify this version of the structure definition when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the structure definition author and is 
         * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
         * available. There is also no expectation that versions can be placed in a lexicographical sequence.
         * </p>
         * 
         * @param version
         *     Business version of the structure definition
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
         * A short, descriptive, user-friendly title for the structure definition.
         * </p>
         * 
         * @param title
         *     Name for this structure definition (human friendly)
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
         * A Boolean value to indicate that this structure definition is authored for testing purposes (or 
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
         * The date (and optionally time) when the structure definition was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the structure definition changes.
         * </p>
         * 
         * @param date
         *     Date last changed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * <p>
         * The name of the organization or individual that published the structure definition.
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
         * A free text natural language description of the structure definition from a consumer's perspective.
         * </p>
         * 
         * @param description
         *     Natural language description of the structure definition
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
         * may be used to assist with indexing and searching for appropriate structure definition instances.
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
         * may be used to assist with indexing and searching for appropriate structure definition instances.
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
         * A legal or geographic region in which the structure definition is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for structure definition (if applicable)
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
         * A legal or geographic region in which the structure definition is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for structure definition (if applicable)
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
         * Explanation of why this structure definition is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this structure definition is defined
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
         * A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the structure definition.
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
         * A set of key words or terms from external terminologies that may be used to assist with indexing and searching of 
         * templates nby describing the use of this structure definition, or the content it describes.
         * </p>
         * 
         * @param keyword
         *     Assist with indexing and finding
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder keyword(Coding... keyword) {
            for (Coding value : keyword) {
                this.keyword.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A set of key words or terms from external terminologies that may be used to assist with indexing and searching of 
         * templates nby describing the use of this structure definition, or the content it describes.
         * </p>
         * 
         * @param keyword
         *     Assist with indexing and finding
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder keyword(Collection<Coding> keyword) {
            this.keyword.addAll(keyword);
            return this;
        }

        /**
         * <p>
         * The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the 
         * specification, without the revision number, e.g. [publication].[major].[minor], which is 4.0.0. for this version.
         * </p>
         * 
         * @param fhirVersion
         *     FHIR Version this StructureDefinition targets
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder fhirVersion(FHIRVersion fhirVersion) {
            this.fhirVersion = fhirVersion;
            return this;
        }

        /**
         * <p>
         * An external specification that the content is mapped to.
         * </p>
         * 
         * @param mapping
         *     External specification that the content is mapped to
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder mapping(Mapping... mapping) {
            for (Mapping value : mapping) {
                this.mapping.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An external specification that the content is mapped to.
         * </p>
         * 
         * @param mapping
         *     External specification that the content is mapped to
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder mapping(Collection<Mapping> mapping) {
            this.mapping.addAll(mapping);
            return this;
        }

        /**
         * <p>
         * Identifies the types of resource or data type elements to which the extension can be applied.
         * </p>
         * 
         * @param context
         *     If an extension, where it can be used in instances
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder context(Context... context) {
            for (Context value : context) {
                this.context.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies the types of resource or data type elements to which the extension can be applied.
         * </p>
         * 
         * @param context
         *     If an extension, where it can be used in instances
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder context(Collection<Context> context) {
            this.context.addAll(context);
            return this;
        }

        /**
         * <p>
         * A set of rules as FHIRPath Invariants about when the extension can be used (e.g. co-occurrence variants for the 
         * extension). All the rules must be true.
         * </p>
         * 
         * @param contextInvariant
         *     FHIRPath invariants - when the extension can be used
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contextInvariant(String... contextInvariant) {
            for (String value : contextInvariant) {
                this.contextInvariant.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A set of rules as FHIRPath Invariants about when the extension can be used (e.g. co-occurrence variants for the 
         * extension). All the rules must be true.
         * </p>
         * 
         * @param contextInvariant
         *     FHIRPath invariants - when the extension can be used
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contextInvariant(Collection<String> contextInvariant) {
            this.contextInvariant.addAll(contextInvariant);
            return this;
        }

        /**
         * <p>
         * An absolute URI that is the base structure from which this type is derived, either by specialization or constraint.
         * </p>
         * 
         * @param baseDefinition
         *     Definition that this type is constrained/specialized from
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder baseDefinition(Canonical baseDefinition) {
            this.baseDefinition = baseDefinition;
            return this;
        }

        /**
         * <p>
         * How the type relates to the baseDefinition.
         * </p>
         * 
         * @param derivation
         *     specialization | constraint - How relates to base definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder derivation(TypeDerivationRule derivation) {
            this.derivation = derivation;
            return this;
        }

        /**
         * <p>
         * A snapshot view is expressed in a standalone form that can be used and interpreted without considering the base 
         * StructureDefinition.
         * </p>
         * 
         * @param snapshot
         *     Snapshot view of the structure
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder snapshot(Snapshot snapshot) {
            this.snapshot = snapshot;
            return this;
        }

        /**
         * <p>
         * A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies.
         * </p>
         * 
         * @param differential
         *     Differential view of the structure
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder differential(Differential differential) {
            this.differential = differential;
            return this;
        }

        @Override
        public StructureDefinition build() {
            return new StructureDefinition(this);
        }

        private Builder from(StructureDefinition structureDefinition) {
            id = structureDefinition.id;
            meta = structureDefinition.meta;
            implicitRules = structureDefinition.implicitRules;
            language = structureDefinition.language;
            text = structureDefinition.text;
            contained.addAll(structureDefinition.contained);
            extension.addAll(structureDefinition.extension);
            modifierExtension.addAll(structureDefinition.modifierExtension);
            identifier.addAll(structureDefinition.identifier);
            version = structureDefinition.version;
            title = structureDefinition.title;
            experimental = structureDefinition.experimental;
            date = structureDefinition.date;
            publisher = structureDefinition.publisher;
            contact.addAll(structureDefinition.contact);
            description = structureDefinition.description;
            useContext.addAll(structureDefinition.useContext);
            jurisdiction.addAll(structureDefinition.jurisdiction);
            purpose = structureDefinition.purpose;
            copyright = structureDefinition.copyright;
            keyword.addAll(structureDefinition.keyword);
            fhirVersion = structureDefinition.fhirVersion;
            mapping.addAll(structureDefinition.mapping);
            context.addAll(structureDefinition.context);
            contextInvariant.addAll(structureDefinition.contextInvariant);
            baseDefinition = structureDefinition.baseDefinition;
            derivation = structureDefinition.derivation;
            snapshot = structureDefinition.snapshot;
            differential = structureDefinition.differential;
            return this;
        }
    }

    /**
     * <p>
     * An external specification that the content is mapped to.
     * </p>
     */
    public static class Mapping extends BackboneElement {
        private final Id identity;
        private final Uri uri;
        private final String name;
        private final String comment;

        private Mapping(Builder builder) {
            super(builder);
            identity = ValidationSupport.requireNonNull(builder.identity, "identity");
            uri = builder.uri;
            name = builder.name;
            comment = builder.comment;
        }

        /**
         * <p>
         * An Internal id that is used to identify this mapping set when specific mappings are made.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Id}.
         */
        public Id getIdentity() {
            return identity;
        }

        /**
         * <p>
         * An absolute URI that identifies the specification that this mapping is expressed to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getUri() {
            return uri;
        }

        /**
         * <p>
         * A name for the specification that is being mapped to.
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
         * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getComment() {
            return comment;
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
                    accept(identity, "identity", visitor);
                    accept(uri, "uri", visitor);
                    accept(name, "name", visitor);
                    accept(comment, "comment", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(identity).from(this);
        }

        public Builder toBuilder(Id identity) {
            return new Builder(identity).from(this);
        }

        public static Builder builder(Id identity) {
            return new Builder(identity);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Id identity;

            // optional
            private Uri uri;
            private String name;
            private String comment;

            private Builder(Id identity) {
                super();
                this.identity = identity;
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
             * An absolute URI that identifies the specification that this mapping is expressed to.
             * </p>
             * 
             * @param uri
             *     Identifies what this mapping refers to
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder uri(Uri uri) {
                this.uri = uri;
                return this;
            }

            /**
             * <p>
             * A name for the specification that is being mapped to.
             * </p>
             * 
             * @param name
             *     Names what this mapping refers to
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
             * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
             * </p>
             * 
             * @param comment
             *     Versions, Issues, Scope limitations etc.
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder comment(String comment) {
                this.comment = comment;
                return this;
            }

            @Override
            public Mapping build() {
                return new Mapping(this);
            }

            private Builder from(Mapping mapping) {
                id = mapping.id;
                extension.addAll(mapping.extension);
                modifierExtension.addAll(mapping.modifierExtension);
                uri = mapping.uri;
                name = mapping.name;
                comment = mapping.comment;
                return this;
            }
        }
    }

    /**
     * <p>
     * Identifies the types of resource or data type elements to which the extension can be applied.
     * </p>
     */
    public static class Context extends BackboneElement {
        private final ExtensionContextType type;
        private final String expression;

        private Context(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            expression = ValidationSupport.requireNonNull(builder.expression, "expression");
        }

        /**
         * <p>
         * Defines how to interpret the expression that defines what the context of the extension is.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ExtensionContextType}.
         */
        public ExtensionContextType getType() {
            return type;
        }

        /**
         * <p>
         * An expression that defines where an extension can be used in resources.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getExpression() {
            return expression;
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
                    accept(expression, "expression", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(type, expression).from(this);
        }

        public Builder toBuilder(ExtensionContextType type, String expression) {
            return new Builder(type, expression).from(this);
        }

        public static Builder builder(ExtensionContextType type, String expression) {
            return new Builder(type, expression);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final ExtensionContextType type;
            private final String expression;

            private Builder(ExtensionContextType type, String expression) {
                super();
                this.type = type;
                this.expression = expression;
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
            public Context build() {
                return new Context(this);
            }

            private Builder from(Context context) {
                id = context.id;
                extension.addAll(context.extension);
                modifierExtension.addAll(context.modifierExtension);
                return this;
            }
        }
    }

    /**
     * <p>
     * A snapshot view is expressed in a standalone form that can be used and interpreted without considering the base 
     * StructureDefinition.
     * </p>
     */
    public static class Snapshot extends BackboneElement {
        private final List<ElementDefinition> element;

        private Snapshot(Builder builder) {
            super(builder);
            element = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.element, "element"));
        }

        /**
         * <p>
         * Captures constraints on each element within the resource.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link ElementDefinition}.
         */
        public List<ElementDefinition> getElement() {
            return element;
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
                    accept(element, "element", visitor, ElementDefinition.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(element).from(this);
        }

        public Builder toBuilder(List<ElementDefinition> element) {
            return new Builder(element).from(this);
        }

        public static Builder builder(List<ElementDefinition> element) {
            return new Builder(element);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final List<ElementDefinition> element;

            private Builder(List<ElementDefinition> element) {
                super();
                this.element = element;
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
            public Snapshot build() {
                return new Snapshot(this);
            }

            private Builder from(Snapshot snapshot) {
                id = snapshot.id;
                extension.addAll(snapshot.extension);
                modifierExtension.addAll(snapshot.modifierExtension);
                return this;
            }
        }
    }

    /**
     * <p>
     * A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies.
     * </p>
     */
    public static class Differential extends BackboneElement {
        private final List<ElementDefinition> element;

        private Differential(Builder builder) {
            super(builder);
            element = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.element, "element"));
        }

        /**
         * <p>
         * Captures constraints on each element within the resource.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link ElementDefinition}.
         */
        public List<ElementDefinition> getElement() {
            return element;
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
                    accept(element, "element", visitor, ElementDefinition.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(element).from(this);
        }

        public Builder toBuilder(List<ElementDefinition> element) {
            return new Builder(element).from(this);
        }

        public static Builder builder(List<ElementDefinition> element) {
            return new Builder(element);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final List<ElementDefinition> element;

            private Builder(List<ElementDefinition> element) {
                super();
                this.element = element;
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
            public Differential build() {
                return new Differential(this);
            }

            private Builder from(Differential differential) {
                id = differential.id;
                extension.addAll(differential.extension);
                modifierExtension.addAll(differential.modifierExtension);
                return this;
            }
        }
    }
}
