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
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ExtensionContextType;
import com.ibm.fhir.model.type.code.FHIRVersion;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.StructureDefinitionKind;
import com.ibm.fhir.model.type.code.TypeDerivationRule;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in 
 * FHIR, and also for describing extensions and constraints on resources and data types.
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.Value.NORMATIVE
)
@Constraint(
    id = "sdf-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-1",
    level = "Rule",
    location = "(base)",
    description = "Element paths must be unique unless the structure is a constraint",
    expression = "derivation = 'constraint' or snapshot.element.select(path).isDistinct()",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-2",
    level = "Rule",
    location = "StructureDefinition.mapping",
    description = "Must have at least a name or a uri (or both)",
    expression = "name.exists() or uri.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-3",
    level = "Rule",
    location = "StructureDefinition.snapshot",
    description = "Each element definition in a snapshot must have a formal definition and cardinalities",
    expression = "element.all(definition.exists() and min.exists() and max.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-4",
    level = "Rule",
    location = "(base)",
    description = "If the structure is not abstract, then there SHALL be a baseDefinition",
    expression = "abstract = true or baseDefinition.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-5",
    level = "Rule",
    location = "(base)",
    description = "If the structure defines an extension then the structure must have context information",
    expression = "type != 'Extension' or derivation = 'specialization' or (context.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-6",
    level = "Rule",
    location = "(base)",
    description = "A structure must have either a differential, or a snapshot (or both)",
    expression = "snapshot.exists() or differential.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-8",
    level = "Rule",
    location = "StructureDefinition.snapshot",
    description = "All snapshot elements must start with the StructureDefinition's specified type for non-logical models, or with the same type name for logical models",
    expression = "(%resource.kind = 'logical' or element.first().path = %resource.type) and element.tail().all(path.startsWith(%resource.snapshot.element.first().path&'.'))",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-8a",
    level = "Rule",
    location = "StructureDefinition.differential",
    description = "In any differential, all the elements must start with the StructureDefinition's specified type for non-logical models, or with the same type name for logical models",
    expression = "(%resource.kind = 'logical' or element.first().path.startsWith(%resource.type)) and (element.tail().empty() or element.tail().all(path.startsWith(%resource.differential.element.first().path.replaceMatches('\\..*','')&'.')))",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-8b",
    level = "Rule",
    location = "StructureDefinition.snapshot",
    description = "All snapshot elements must have a base definition",
    expression = "element.all(base.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-9",
    level = "Rule",
    location = "(base)",
    description = "In any snapshot or differential, no label, code or requirements on an element without a \".\" in the path (e.g. the first element)",
    expression = "children().element.where(path.contains('.').not()).label.empty() and children().element.where(path.contains('.').not()).code.empty() and children().element.where(path.contains('.').not()).requirements.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-10",
    level = "Rule",
    location = "StructureDefinition.snapshot.element",
    description = "provide either a binding reference or a description (or both)",
    expression = "binding.empty() or binding.valueSet.exists() or binding.description.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-11",
    level = "Rule",
    location = "(base)",
    description = "If there's a type, its content must match the path name in the first element of a snapshot",
    expression = "kind != 'logical' implies snapshot.empty() or snapshot.element.first().path = type",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-14",
    level = "Rule",
    location = "(base)",
    description = "All element definitions must have an id",
    expression = "snapshot.element.all(id.exists()) and differential.element.all(id.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-15",
    level = "Rule",
    location = "(base)",
    description = "The first element in a snapshot has no type unless model is a logical model.",
    expression = "kind!='logical' implies snapshot.element.first().type.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-15a",
    level = "Rule",
    location = "(base)",
    description = "If the first element in a differential has no \".\" in the path and it's not a logical model, it has no type",
    expression = "(kind!='logical'  and differential.element.first().path.contains('.').not()) implies differential.element.first().type.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-16",
    level = "Rule",
    location = "(base)",
    description = "All element definitions must have unique ids (snapshot)",
    expression = "snapshot.element.all(id.exists()) and snapshot.element.id.trace('ids').isDistinct()",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-17",
    level = "Rule",
    location = "(base)",
    description = "All element definitions must have unique ids (diff)",
    expression = "differential.element.all(id.exists()) and differential.element.id.trace('ids').isDistinct()",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-18",
    level = "Rule",
    location = "(base)",
    description = "Context Invariants can only be used for extensions",
    expression = "contextInvariant.exists() implies type = 'Extension'",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-19",
    level = "Rule",
    location = "(base)",
    description = "FHIR Specification models only use FHIR defined types",
    expression = "url.startsWith('http://hl7.org/fhir/StructureDefinition') implies (differential.element.type.code.all(matches('^[a-zA-Z0-9]+$') or matches('^http:\\/\\/hl7\\.org\\/fhirpath\\/System\\.[A-Z][A-Za-z]+$')) and snapshot.element.type.code.all(matches('^[a-zA-Z0-9\\.]+$') or matches('^http:\\/\\/hl7\\.org\\/fhirpath\\/System\\.[A-Z][A-Za-z]+$')))",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-20",
    level = "Rule",
    location = "StructureDefinition.differential",
    description = "No slicing on the root element",
    expression = "element.where(path.contains('.').not()).slicing.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-21",
    level = "Rule",
    location = "(base)",
    description = "Default values can only be specified on specializations",
    expression = "differential.element.defaultValue.exists() implies (derivation = 'specialization')",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-22",
    level = "Rule",
    location = "(base)",
    description = "FHIR Specification models never have default values",
    expression = "url.startsWith('http://hl7.org/fhir/StructureDefinition') implies (snapshot.element.defaultValue.empty() and differential.element.defaultValue.empty())",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "sdf-23",
    level = "Rule",
    location = "(base)",
    description = "No slice name on root",
    expression = "(snapshot | differential).element.all(path.contains('.').not() implies sliceName.empty())",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition"
)
@Constraint(
    id = "structureDefinition-24",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition",
    generated = true
)
@Constraint(
    id = "structureDefinition-25",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/definition-use",
    expression = "keyword.exists() implies (keyword.all(memberOf('http://hl7.org/fhir/ValueSet/definition-use', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition",
    generated = true
)
@Constraint(
    id = "structureDefinition-26",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/defined-types",
    expression = "type.exists() and type.memberOf('http://hl7.org/fhir/ValueSet/defined-types', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/StructureDefinition",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class StructureDefinition extends DomainResource {
    @Summary
    @Required
    private final Uri url;
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final String version;
    @Summary
    @Required
    private final String name;
    @Summary
    private final String title;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.1.0"
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
    private final Markdown copyright;
    @Summary
    @Binding(
        bindingName = "StructureDefinitionKeyword",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Codes for the meaning of the defined structure (SNOMED CT and LOINC codes, as an example).",
        valueSet = "http://hl7.org/fhir/ValueSet/definition-use"
    )
    private final List<Coding> keyword;
    @Summary
    @Binding(
        bindingName = "FHIRVersion",
        strength = BindingStrength.Value.REQUIRED,
        description = "All published FHIR Versions.",
        valueSet = "http://hl7.org/fhir/ValueSet/FHIR-version|4.1.0"
    )
    private final FHIRVersion fhirVersion;
    private final List<Mapping> mapping;
    @Summary
    @Binding(
        bindingName = "StructureDefinitionKind",
        strength = BindingStrength.Value.REQUIRED,
        description = "Defines the type of structure that a definition is describing.",
        valueSet = "http://hl7.org/fhir/ValueSet/structure-definition-kind|4.1.0"
    )
    @Required
    private final StructureDefinitionKind kind;
    @Summary
    @Required
    private final Boolean _abstract;
    @Summary
    private final List<Context> context;
    @Summary
    private final List<String> contextInvariant;
    @Summary
    @Binding(
        bindingName = "FHIRDefinedTypeExt",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Either a resource or a data type, including logical model types.",
        valueSet = "http://hl7.org/fhir/ValueSet/defined-types"
    )
    @Required
    private final Uri type;
    @Summary
    private final Canonical baseDefinition;
    @Summary
    @Binding(
        bindingName = "TypeDerivationRule",
        strength = BindingStrength.Value.REQUIRED,
        description = "How a type relates to its baseDefinition.",
        valueSet = "http://hl7.org/fhir/ValueSet/type-derivation-rule|4.1.0"
    )
    private final TypeDerivationRule derivation;
    private final Snapshot snapshot;
    private final Differential differential;

    private StructureDefinition(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
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
        keyword = Collections.unmodifiableList(builder.keyword);
        fhirVersion = builder.fhirVersion;
        mapping = Collections.unmodifiableList(builder.mapping);
        kind = builder.kind;
        _abstract = builder._abstract;
        context = Collections.unmodifiableList(builder.context);
        contextInvariant = Collections.unmodifiableList(builder.contextInvariant);
        type = builder.type;
        baseDefinition = builder.baseDefinition;
        derivation = builder.derivation;
        snapshot = builder.snapshot;
        differential = builder.differential;
    }

    /**
     * An absolute URI that is used to identify this structure definition when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this structure definition is (or will be) published. This URL 
     * can be the target of a canonical reference. It SHALL remain the same when the structure definition is stored on 
     * different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that is non-null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this structure definition when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the structure definition when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the structure definition author and is 
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
     * A natural language name identifying the structure definition. This name should be usable as an identifier for the 
     * module by machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that is non-null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the structure definition.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The status of this structure definition. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this structure definition is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the structure definition was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the structure definition changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the structure definition.
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
     * A free text natural language description of the structure definition from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate structure definition instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the structure definition is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this structure definition is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the structure definition.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * A set of key words or terms from external terminologies that may be used to assist with indexing and searching of 
     * templates nby describing the use of this structure definition, or the content it describes.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
     */
    public List<Coding> getKeyword() {
        return keyword;
    }

    /**
     * The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the 
     * specification, without the revision number, e.g. [publication].[major].[minor], which is 4.1.0 for this version.
     * 
     * @return
     *     An immutable object of type {@link FHIRVersion} that may be null.
     */
    public FHIRVersion getFhirVersion() {
        return fhirVersion;
    }

    /**
     * An external specification that the content is mapped to.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Mapping} that may be empty.
     */
    public List<Mapping> getMapping() {
        return mapping;
    }

    /**
     * Defines the kind of structure that this definition is describing.
     * 
     * @return
     *     An immutable object of type {@link StructureDefinitionKind} that is non-null.
     */
    public StructureDefinitionKind getKind() {
        return kind;
    }

    /**
     * Whether structure this definition describes is abstract or not - that is, whether the structure is not intended to be 
     * instantiated. For Resources and Data types, abstract types will never be exchanged between systems.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that is non-null.
     */
    public Boolean getAbstract() {
        return _abstract;
    }

    /**
     * Identifies the types of resource or data type elements to which the extension can be applied.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Context} that may be empty.
     */
    public List<Context> getContext() {
        return context;
    }

    /**
     * A set of rules as FHIRPath Invariants about when the extension can be used (e.g. co-occurrence variants for the 
     * extension). All the rules must be true.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getContextInvariant() {
        return contextInvariant;
    }

    /**
     * The type this structure describes. If the derivation kind is 'specialization' then this is the master definition for a 
     * type, and there is always one of these (a data type, an extension, a resource, including abstract ones). Otherwise the 
     * structure definition is a constraint on the stated type (and in this case, the type cannot be an abstract type). 
     * References are URLs that are relative to http://hl7.org/fhir/StructureDefinition e.g. "string" is a reference to http:
     * //hl7.org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.
     * 
     * @return
     *     An immutable object of type {@link Uri} that is non-null.
     */
    public Uri getType() {
        return type;
    }

    /**
     * An absolute URI that is the base structure from which this type is derived, either by specialization or constraint.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getBaseDefinition() {
        return baseDefinition;
    }

    /**
     * How the type relates to the baseDefinition.
     * 
     * @return
     *     An immutable object of type {@link TypeDerivationRule} that may be null.
     */
    public TypeDerivationRule getDerivation() {
        return derivation;
    }

    /**
     * A snapshot view is expressed in a standalone form that can be used and interpreted without considering the base 
     * StructureDefinition.
     * 
     * @return
     *     An immutable object of type {@link Snapshot} that may be null.
     */
    public Snapshot getSnapshot() {
        return snapshot;
    }

    /**
     * A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies.
     * 
     * @return
     *     An immutable object of type {@link Differential} that may be null.
     */
    public Differential getDifferential() {
        return differential;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            !identifier.isEmpty() || 
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
            !keyword.isEmpty() || 
            (fhirVersion != null) || 
            !mapping.isEmpty() || 
            (kind != null) || 
            (_abstract != null) || 
            !context.isEmpty() || 
            !contextInvariant.isEmpty() || 
            (type != null) || 
            (baseDefinition != null) || 
            (derivation != null) || 
            (snapshot != null) || 
            (differential != null);
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
        StructureDefinition other = (StructureDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(url, other.url) && 
            Objects.equals(identifier, other.identifier) && 
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
            Objects.equals(keyword, other.keyword) && 
            Objects.equals(fhirVersion, other.fhirVersion) && 
            Objects.equals(mapping, other.mapping) && 
            Objects.equals(kind, other.kind) && 
            Objects.equals(_abstract, other._abstract) && 
            Objects.equals(context, other.context) && 
            Objects.equals(contextInvariant, other.contextInvariant) && 
            Objects.equals(type, other.type) && 
            Objects.equals(baseDefinition, other.baseDefinition) && 
            Objects.equals(derivation, other.derivation) && 
            Objects.equals(snapshot, other.snapshot) && 
            Objects.equals(differential, other.differential);
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
                identifier, 
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
                keyword, 
                fhirVersion, 
                mapping, 
                kind, 
                _abstract, 
                context, 
                contextInvariant, 
                type, 
                baseDefinition, 
                derivation, 
                snapshot, 
                differential);
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
        private List<Identifier> identifier = new ArrayList<>();
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
        private List<Coding> keyword = new ArrayList<>();
        private FHIRVersion fhirVersion;
        private List<Mapping> mapping = new ArrayList<>();
        private StructureDefinitionKind kind;
        private Boolean _abstract;
        private List<Context> context = new ArrayList<>();
        private List<String> contextInvariant = new ArrayList<>();
        private Uri type;
        private Canonical baseDefinition;
        private TypeDerivationRule derivation;
        private Snapshot snapshot;
        private Differential differential;

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
         * An absolute URI that is used to identify this structure definition when it is referenced in a specification, model, 
         * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
         * address at which at which an authoritative instance of this structure definition is (or will be) published. This URL 
         * can be the target of a canonical reference. It SHALL remain the same when the structure definition is stored on 
         * different servers.
         * 
         * <p>This element is required.
         * 
         * @param url
         *     Canonical identifier for this structure definition, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this structure definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the structure definition
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
         * A formal identifier that is used to identify this structure definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the structure definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * Convenience method for setting {@code version}.
         * 
         * @param version
         *     Business version of the structure definition
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
         * The identifier that is used to identify this version of the structure definition when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the structure definition author and is 
         * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
         * available. There is also no expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of the structure definition
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
         *     Name for this structure definition (computer friendly)
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
         * A natural language name identifying the structure definition. This name should be usable as an identifier for the 
         * module by machine processing applications such as code generation.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     Name for this structure definition (computer friendly)
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
         *     Name for this structure definition (human friendly)
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
         * A short, descriptive, user-friendly title for the structure definition.
         * 
         * @param title
         *     Name for this structure definition (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The status of this structure definition. Enables tracking the life-cycle of the content.
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
         * A Boolean value to indicate that this structure definition is authored for testing purposes (or 
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
         * The date (and optionally time) when the structure definition was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the structure definition changes.
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
         * The name of the organization or individual that published the structure definition.
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
         * A free text natural language description of the structure definition from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the structure definition
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
         * may be used to assist with indexing and searching for appropriate structure definition instances.
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
         * may be used to assist with indexing and searching for appropriate structure definition instances.
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
         * A legal or geographic region in which the structure definition is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for structure definition (if applicable)
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
         * A legal or geographic region in which the structure definition is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for structure definition (if applicable)
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
         * Explanation of why this structure definition is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this structure definition is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the structure definition.
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
         * A set of key words or terms from external terminologies that may be used to assist with indexing and searching of 
         * templates nby describing the use of this structure definition, or the content it describes.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param keyword
         *     Assist with indexing and finding
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder keyword(Coding... keyword) {
            for (Coding value : keyword) {
                this.keyword.add(value);
            }
            return this;
        }

        /**
         * A set of key words or terms from external terminologies that may be used to assist with indexing and searching of 
         * templates nby describing the use of this structure definition, or the content it describes.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param keyword
         *     Assist with indexing and finding
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder keyword(Collection<Coding> keyword) {
            this.keyword = new ArrayList<>(keyword);
            return this;
        }

        /**
         * The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the 
         * specification, without the revision number, e.g. [publication].[major].[minor], which is 4.1.0 for this version.
         * 
         * @param fhirVersion
         *     FHIR Version this StructureDefinition targets
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder fhirVersion(FHIRVersion fhirVersion) {
            this.fhirVersion = fhirVersion;
            return this;
        }

        /**
         * An external specification that the content is mapped to.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param mapping
         *     External specification that the content is mapped to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder mapping(Mapping... mapping) {
            for (Mapping value : mapping) {
                this.mapping.add(value);
            }
            return this;
        }

        /**
         * An external specification that the content is mapped to.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param mapping
         *     External specification that the content is mapped to
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder mapping(Collection<Mapping> mapping) {
            this.mapping = new ArrayList<>(mapping);
            return this;
        }

        /**
         * Defines the kind of structure that this definition is describing.
         * 
         * <p>This element is required.
         * 
         * @param kind
         *     primitive-type | complex-type | resource | logical
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder kind(StructureDefinitionKind kind) {
            this.kind = kind;
            return this;
        }

        /**
         * Convenience method for setting {@code _abstract}.
         * 
         * <p>This element is required.
         * 
         * @param _abstract
         *     Whether the structure is abstract
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #_abstract(com.ibm.fhir.model.type.Boolean)
         */
        public Builder _abstract(java.lang.Boolean _abstract) {
            this._abstract = (_abstract == null) ? null : Boolean.of(_abstract);
            return this;
        }

        /**
         * Whether structure this definition describes is abstract or not - that is, whether the structure is not intended to be 
         * instantiated. For Resources and Data types, abstract types will never be exchanged between systems.
         * 
         * <p>This element is required.
         * 
         * @param _abstract
         *     Whether the structure is abstract
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder _abstract(Boolean _abstract) {
            this._abstract = _abstract;
            return this;
        }

        /**
         * Identifies the types of resource or data type elements to which the extension can be applied.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param context
         *     If an extension, where it can be used in instances
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder context(Context... context) {
            for (Context value : context) {
                this.context.add(value);
            }
            return this;
        }

        /**
         * Identifies the types of resource or data type elements to which the extension can be applied.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param context
         *     If an extension, where it can be used in instances
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder context(Collection<Context> context) {
            this.context = new ArrayList<>(context);
            return this;
        }

        /**
         * Convenience method for setting {@code contextInvariant}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contextInvariant
         *     FHIRPath invariants - when the extension can be used
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #contextInvariant(com.ibm.fhir.model.type.String)
         */
        public Builder contextInvariant(java.lang.String... contextInvariant) {
            for (java.lang.String value : contextInvariant) {
                this.contextInvariant.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * A set of rules as FHIRPath Invariants about when the extension can be used (e.g. co-occurrence variants for the 
         * extension). All the rules must be true.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contextInvariant
         *     FHIRPath invariants - when the extension can be used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contextInvariant(String... contextInvariant) {
            for (String value : contextInvariant) {
                this.contextInvariant.add(value);
            }
            return this;
        }

        /**
         * A set of rules as FHIRPath Invariants about when the extension can be used (e.g. co-occurrence variants for the 
         * extension). All the rules must be true.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contextInvariant
         *     FHIRPath invariants - when the extension can be used
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contextInvariant(Collection<String> contextInvariant) {
            this.contextInvariant = new ArrayList<>(contextInvariant);
            return this;
        }

        /**
         * The type this structure describes. If the derivation kind is 'specialization' then this is the master definition for a 
         * type, and there is always one of these (a data type, an extension, a resource, including abstract ones). Otherwise the 
         * structure definition is a constraint on the stated type (and in this case, the type cannot be an abstract type). 
         * References are URLs that are relative to http://hl7.org/fhir/StructureDefinition e.g. "string" is a reference to http:
         * //hl7.org/fhir/StructureDefinition/string. Absolute URLs are only allowed in logical models.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     Type defined or constrained by this structure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(Uri type) {
            this.type = type;
            return this;
        }

        /**
         * An absolute URI that is the base structure from which this type is derived, either by specialization or constraint.
         * 
         * @param baseDefinition
         *     Definition that this type is constrained/specialized from
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder baseDefinition(Canonical baseDefinition) {
            this.baseDefinition = baseDefinition;
            return this;
        }

        /**
         * How the type relates to the baseDefinition.
         * 
         * @param derivation
         *     specialization | constraint - How relates to base definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder derivation(TypeDerivationRule derivation) {
            this.derivation = derivation;
            return this;
        }

        /**
         * A snapshot view is expressed in a standalone form that can be used and interpreted without considering the base 
         * StructureDefinition.
         * 
         * @param snapshot
         *     Snapshot view of the structure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder snapshot(Snapshot snapshot) {
            this.snapshot = snapshot;
            return this;
        }

        /**
         * A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies.
         * 
         * @param differential
         *     Differential view of the structure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder differential(Differential differential) {
            this.differential = differential;
            return this;
        }

        /**
         * Build the {@link StructureDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>url</li>
         * <li>name</li>
         * <li>status</li>
         * <li>kind</li>
         * <li>abstract</li>
         * <li>type</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link StructureDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid StructureDefinition per the base specification
         */
        @Override
        public StructureDefinition build() {
            StructureDefinition structureDefinition = new StructureDefinition(this);
            if (validating) {
                validate(structureDefinition);
            }
            return structureDefinition;
        }

        protected void validate(StructureDefinition structureDefinition) {
            super.validate(structureDefinition);
            ValidationSupport.requireNonNull(structureDefinition.url, "url");
            ValidationSupport.checkList(structureDefinition.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(structureDefinition.name, "name");
            ValidationSupport.requireNonNull(structureDefinition.status, "status");
            ValidationSupport.checkList(structureDefinition.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(structureDefinition.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(structureDefinition.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkList(structureDefinition.keyword, "keyword", Coding.class);
            ValidationSupport.checkList(structureDefinition.mapping, "mapping", Mapping.class);
            ValidationSupport.requireNonNull(structureDefinition.kind, "kind");
            ValidationSupport.requireNonNull(structureDefinition._abstract, "abstract");
            ValidationSupport.checkList(structureDefinition.context, "context", Context.class);
            ValidationSupport.checkList(structureDefinition.contextInvariant, "contextInvariant", String.class);
            ValidationSupport.requireNonNull(structureDefinition.type, "type");
        }

        protected Builder from(StructureDefinition structureDefinition) {
            super.from(structureDefinition);
            url = structureDefinition.url;
            identifier.addAll(structureDefinition.identifier);
            version = structureDefinition.version;
            name = structureDefinition.name;
            title = structureDefinition.title;
            status = structureDefinition.status;
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
            kind = structureDefinition.kind;
            _abstract = structureDefinition._abstract;
            context.addAll(structureDefinition.context);
            contextInvariant.addAll(structureDefinition.contextInvariant);
            type = structureDefinition.type;
            baseDefinition = structureDefinition.baseDefinition;
            derivation = structureDefinition.derivation;
            snapshot = structureDefinition.snapshot;
            differential = structureDefinition.differential;
            return this;
        }
    }

    /**
     * An external specification that the content is mapped to.
     */
    public static class Mapping extends BackboneElement {
        @Required
        private final Id identity;
        private final Uri uri;
        private final String name;
        private final String comment;

        private Mapping(Builder builder) {
            super(builder);
            identity = builder.identity;
            uri = builder.uri;
            name = builder.name;
            comment = builder.comment;
        }

        /**
         * An Internal id that is used to identify this mapping set when specific mappings are made.
         * 
         * @return
         *     An immutable object of type {@link Id} that is non-null.
         */
        public Id getIdentity() {
            return identity;
        }

        /**
         * An absolute URI that identifies the specification that this mapping is expressed to.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getUri() {
            return uri;
        }

        /**
         * A name for the specification that is being mapped to.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getComment() {
            return comment;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (identity != null) || 
                (uri != null) || 
                (name != null) || 
                (comment != null);
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
                    accept(identity, "identity", visitor);
                    accept(uri, "uri", visitor);
                    accept(name, "name", visitor);
                    accept(comment, "comment", visitor);
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
            Mapping other = (Mapping) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identity, other.identity) && 
                Objects.equals(uri, other.uri) && 
                Objects.equals(name, other.name) && 
                Objects.equals(comment, other.comment);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identity, 
                    uri, 
                    name, 
                    comment);
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
            private Id identity;
            private Uri uri;
            private String name;
            private String comment;

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
             * An Internal id that is used to identify this mapping set when specific mappings are made.
             * 
             * <p>This element is required.
             * 
             * @param identity
             *     Internal id when this mapping is used
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identity(Id identity) {
                this.identity = identity;
                return this;
            }

            /**
             * An absolute URI that identifies the specification that this mapping is expressed to.
             * 
             * @param uri
             *     Identifies what this mapping refers to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder uri(Uri uri) {
                this.uri = uri;
                return this;
            }

            /**
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     Names what this mapping refers to
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
             * A name for the specification that is being mapped to.
             * 
             * @param name
             *     Names what this mapping refers to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Convenience method for setting {@code comment}.
             * 
             * @param comment
             *     Versions, Issues, Scope limitations etc.
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
             * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
             * 
             * @param comment
             *     Versions, Issues, Scope limitations etc.
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder comment(String comment) {
                this.comment = comment;
                return this;
            }

            /**
             * Build the {@link Mapping}
             * 
             * <p>Required elements:
             * <ul>
             * <li>identity</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Mapping}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Mapping per the base specification
             */
            @Override
            public Mapping build() {
                Mapping mapping = new Mapping(this);
                if (validating) {
                    validate(mapping);
                }
                return mapping;
            }

            protected void validate(Mapping mapping) {
                super.validate(mapping);
                ValidationSupport.requireNonNull(mapping.identity, "identity");
                ValidationSupport.requireValueOrChildren(mapping);
            }

            protected Builder from(Mapping mapping) {
                super.from(mapping);
                identity = mapping.identity;
                uri = mapping.uri;
                name = mapping.name;
                comment = mapping.comment;
                return this;
            }
        }
    }

    /**
     * Identifies the types of resource or data type elements to which the extension can be applied.
     */
    public static class Context extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "ExtensionContextType",
            strength = BindingStrength.Value.REQUIRED,
            description = "How an extension context is interpreted.",
            valueSet = "http://hl7.org/fhir/ValueSet/extension-context-type|4.1.0"
        )
        @Required
        private final ExtensionContextType type;
        @Summary
        @Required
        private final String expression;

        private Context(Builder builder) {
            super(builder);
            type = builder.type;
            expression = builder.expression;
        }

        /**
         * Defines how to interpret the expression that defines what the context of the extension is.
         * 
         * @return
         *     An immutable object of type {@link ExtensionContextType} that is non-null.
         */
        public ExtensionContextType getType() {
            return type;
        }

        /**
         * An expression that defines where an extension can be used in resources.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getExpression() {
            return expression;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (expression != null);
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
                    accept(expression, "expression", visitor);
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
            Context other = (Context) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(expression, other.expression);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    expression);
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
            private ExtensionContextType type;
            private String expression;

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
             * Defines how to interpret the expression that defines what the context of the extension is.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     fhirpath | element | extension
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(ExtensionContextType type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code expression}.
             * 
             * <p>This element is required.
             * 
             * @param expression
             *     Where the extension can be used in instances
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
             * An expression that defines where an extension can be used in resources.
             * 
             * <p>This element is required.
             * 
             * @param expression
             *     Where the extension can be used in instances
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder expression(String expression) {
                this.expression = expression;
                return this;
            }

            /**
             * Build the {@link Context}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>expression</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Context}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Context per the base specification
             */
            @Override
            public Context build() {
                Context context = new Context(this);
                if (validating) {
                    validate(context);
                }
                return context;
            }

            protected void validate(Context context) {
                super.validate(context);
                ValidationSupport.requireNonNull(context.type, "type");
                ValidationSupport.requireNonNull(context.expression, "expression");
                ValidationSupport.requireValueOrChildren(context);
            }

            protected Builder from(Context context) {
                super.from(context);
                type = context.type;
                expression = context.expression;
                return this;
            }
        }
    }

    /**
     * A snapshot view is expressed in a standalone form that can be used and interpreted without considering the base 
     * StructureDefinition.
     */
    public static class Snapshot extends BackboneElement {
        @Required
        private final List<ElementDefinition> element;

        private Snapshot(Builder builder) {
            super(builder);
            element = Collections.unmodifiableList(builder.element);
        }

        /**
         * Captures constraints on each element within the resource.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ElementDefinition} that is non-empty.
         */
        public List<ElementDefinition> getElement() {
            return element;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !element.isEmpty();
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
                    accept(element, "element", visitor, ElementDefinition.class);
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
            Snapshot other = (Snapshot) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(element, other.element);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    element);
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
            private List<ElementDefinition> element = new ArrayList<>();

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
             * Captures constraints on each element within the resource.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param element
             *     Definition of elements in the resource (if no StructureDefinition)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder element(ElementDefinition... element) {
                for (ElementDefinition value : element) {
                    this.element.add(value);
                }
                return this;
            }

            /**
             * Captures constraints on each element within the resource.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param element
             *     Definition of elements in the resource (if no StructureDefinition)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder element(Collection<ElementDefinition> element) {
                this.element = new ArrayList<>(element);
                return this;
            }

            /**
             * Build the {@link Snapshot}
             * 
             * <p>Required elements:
             * <ul>
             * <li>element</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Snapshot}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Snapshot per the base specification
             */
            @Override
            public Snapshot build() {
                Snapshot snapshot = new Snapshot(this);
                if (validating) {
                    validate(snapshot);
                }
                return snapshot;
            }

            protected void validate(Snapshot snapshot) {
                super.validate(snapshot);
                ValidationSupport.checkNonEmptyList(snapshot.element, "element", ElementDefinition.class);
                ValidationSupport.requireValueOrChildren(snapshot);
            }

            protected Builder from(Snapshot snapshot) {
                super.from(snapshot);
                element.addAll(snapshot.element);
                return this;
            }
        }
    }

    /**
     * A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies.
     */
    public static class Differential extends BackboneElement {
        @Required
        private final List<ElementDefinition> element;

        private Differential(Builder builder) {
            super(builder);
            element = Collections.unmodifiableList(builder.element);
        }

        /**
         * Captures constraints on each element within the resource.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ElementDefinition} that is non-empty.
         */
        public List<ElementDefinition> getElement() {
            return element;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !element.isEmpty();
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
                    accept(element, "element", visitor, ElementDefinition.class);
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
            Differential other = (Differential) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(element, other.element);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    element);
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
            private List<ElementDefinition> element = new ArrayList<>();

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
             * Captures constraints on each element within the resource.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param element
             *     Definition of elements in the resource (if no StructureDefinition)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder element(ElementDefinition... element) {
                for (ElementDefinition value : element) {
                    this.element.add(value);
                }
                return this;
            }

            /**
             * Captures constraints on each element within the resource.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param element
             *     Definition of elements in the resource (if no StructureDefinition)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder element(Collection<ElementDefinition> element) {
                this.element = new ArrayList<>(element);
                return this;
            }

            /**
             * Build the {@link Differential}
             * 
             * <p>Required elements:
             * <ul>
             * <li>element</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Differential}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Differential per the base specification
             */
            @Override
            public Differential build() {
                Differential differential = new Differential(this);
                if (validating) {
                    validate(differential);
                }
                return differential;
            }

            protected void validate(Differential differential) {
                super.validate(differential);
                ValidationSupport.checkNonEmptyList(differential.element, "element", ElementDefinition.class);
                ValidationSupport.requireValueOrChildren(differential);
            }

            protected Builder from(Differential differential) {
                super.from(differential);
                element.addAll(differential.element);
                return this;
            }
        }
    }
}
