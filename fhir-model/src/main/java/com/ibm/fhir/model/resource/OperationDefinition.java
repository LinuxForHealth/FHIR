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
import com.ibm.fhir.model.type.code.FHIRAllTypes;
import com.ibm.fhir.model.type.code.OperationKind;
import com.ibm.fhir.model.type.code.OperationParameterUse;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A formal computable definition of an operation (on the RESTful interface) or a named query (using the search 
 * interaction).
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.Value.NORMATIVE
)
@Constraint(
    id = "opd-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/OperationDefinition"
)
@Constraint(
    id = "opd-1",
    level = "Rule",
    location = "OperationDefinition.parameter",
    description = "Either a type must be provided, or parts",
    expression = "type.exists() or part.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/OperationDefinition"
)
@Constraint(
    id = "opd-2",
    level = "Rule",
    location = "OperationDefinition.parameter",
    description = "A search type can only be specified for parameters of type string",
    expression = "searchType.exists() implies type = 'string'",
    source = "http://hl7.org/fhir/StructureDefinition/OperationDefinition"
)
@Constraint(
    id = "opd-3",
    level = "Rule",
    location = "OperationDefinition.parameter",
    description = "A targetProfile can only be specified for parameters of type Reference or Canonical",
    expression = "targetProfile.exists() implies (type = 'Reference' or type = 'canonical')",
    source = "http://hl7.org/fhir/StructureDefinition/OperationDefinition"
)
@Constraint(
    id = "operationDefinition-4",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/OperationDefinition",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class OperationDefinition extends DomainResource {
    @Summary
    private final Uri url;
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
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.3.0-CIBUILD"
    )
    @Required
    private final PublicationStatus status;
    @Summary
    @Binding(
        bindingName = "OperationKind",
        strength = BindingStrength.Value.REQUIRED,
        description = "Whether an operation is a normal operation or a query.",
        valueSet = "http://hl7.org/fhir/ValueSet/operation-kind|4.3.0-CIBUILD"
    )
    @Required
    private final OperationKind kind;
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
    private final Boolean affectsState;
    @Summary
    @Required
    private final Code code;
    private final Markdown comment;
    @Summary
    private final Canonical base;
    @Summary
    @Binding(
        bindingName = "ResourceType",
        strength = BindingStrength.Value.REQUIRED,
        description = "One of the resource types defined as part of this version of FHIR.",
        valueSet = "http://hl7.org/fhir/ValueSet/resource-types|4.3.0-CIBUILD"
    )
    private final List<ResourceTypeCode> resource;
    @Summary
    @Required
    private final Boolean system;
    @Summary
    @Required
    private final Boolean type;
    @Summary
    @Required
    private final Boolean instance;
    private final Canonical inputProfile;
    private final Canonical outputProfile;
    private final List<Parameter> parameter;
    private final List<Overload> overload;

    private OperationDefinition(Builder builder) {
        super(builder);
        url = builder.url;
        version = builder.version;
        name = builder.name;
        title = builder.title;
        status = builder.status;
        kind = builder.kind;
        experimental = builder.experimental;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        purpose = builder.purpose;
        affectsState = builder.affectsState;
        code = builder.code;
        comment = builder.comment;
        base = builder.base;
        resource = Collections.unmodifiableList(builder.resource);
        system = builder.system;
        type = builder.type;
        instance = builder.instance;
        inputProfile = builder.inputProfile;
        outputProfile = builder.outputProfile;
        parameter = Collections.unmodifiableList(builder.parameter);
        overload = Collections.unmodifiableList(builder.overload);
    }

    /**
     * An absolute URI that is used to identify this operation definition when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this operation definition is (or will be) published. This URL 
     * can be the target of a canonical reference. It SHALL remain the same when the operation definition is stored on 
     * different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * The identifier that is used to identify this version of the operation definition when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the operation definition author and is 
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
     * A natural language name identifying the operation definition. This name should be usable as an identifier for the 
     * module by machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that is non-null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the operation definition.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The status of this operation definition. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * Whether this is an operation or a named query.
     * 
     * @return
     *     An immutable object of type {@link OperationKind} that is non-null.
     */
    public OperationKind getKind() {
        return kind;
    }

    /**
     * A Boolean value to indicate that this operation definition is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the operation definition was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the operation definition changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the operation definition.
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
     * A free text natural language description of the operation definition from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate operation definition instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the operation definition is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this operation definition is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * Whether the operation affects state. Side effects such as producing audit trail entries do not count as 'affecting 
     * state'.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getAffectsState() {
        return affectsState;
    }

    /**
     * The name used to invoke the operation.
     * 
     * @return
     *     An immutable object of type {@link Code} that is non-null.
     */
    public Code getCode() {
        return code;
    }

    /**
     * Additional information about how to use this operation or named query.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getComment() {
        return comment;
    }

    /**
     * Indicates that this operation definition is a constraining profile on the base.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getBase() {
        return base;
    }

    /**
     * The types on which this operation can be executed.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ResourceTypeCode} that may be empty.
     */
    public List<ResourceTypeCode> getResource() {
        return resource;
    }

    /**
     * Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a 
     * resource type for the context).
     * 
     * @return
     *     An immutable object of type {@link Boolean} that is non-null.
     */
    public Boolean getSystem() {
        return system;
    }

    /**
     * Indicates whether this operation or named query can be invoked at the resource type level for any given resource type 
     * level (e.g. without needing to choose a specific resource id for the context).
     * 
     * @return
     *     An immutable object of type {@link Boolean} that is non-null.
     */
    public Boolean getType() {
        return type;
    }

    /**
     * Indicates whether this operation can be invoked on a particular instance of one of the given types.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that is non-null.
     */
    public Boolean getInstance() {
        return instance;
    }

    /**
     * Additional validation information for the in parameters - a single profile that covers all the parameters. The profile 
     * is a constraint on the parameters resource as a whole.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getInputProfile() {
        return inputProfile;
    }

    /**
     * Additional validation information for the out parameters - a single profile that covers all the parameters. The 
     * profile is a constraint on the parameters resource.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getOutputProfile() {
        return outputProfile;
    }

    /**
     * The parameters for the operation/query.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Parameter} that may be empty.
     */
    public List<Parameter> getParameter() {
        return parameter;
    }

    /**
     * Defines an appropriate combination of parameters to use when invoking this operation, to help code generators when 
     * generating overloaded parameter sets for this operation.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Overload} that may be empty.
     */
    public List<Overload> getOverload() {
        return overload;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            (version != null) || 
            (name != null) || 
            (title != null) || 
            (status != null) || 
            (kind != null) || 
            (experimental != null) || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            (description != null) || 
            !useContext.isEmpty() || 
            !jurisdiction.isEmpty() || 
            (purpose != null) || 
            (affectsState != null) || 
            (code != null) || 
            (comment != null) || 
            (base != null) || 
            !resource.isEmpty() || 
            (system != null) || 
            (type != null) || 
            (instance != null) || 
            (inputProfile != null) || 
            (outputProfile != null) || 
            !parameter.isEmpty() || 
            !overload.isEmpty();
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
                accept(kind, "kind", visitor);
                accept(experimental, "experimental", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(purpose, "purpose", visitor);
                accept(affectsState, "affectsState", visitor);
                accept(code, "code", visitor);
                accept(comment, "comment", visitor);
                accept(base, "base", visitor);
                accept(resource, "resource", visitor, ResourceTypeCode.class);
                accept(system, "system", visitor);
                accept(type, "type", visitor);
                accept(instance, "instance", visitor);
                accept(inputProfile, "inputProfile", visitor);
                accept(outputProfile, "outputProfile", visitor);
                accept(parameter, "parameter", visitor, Parameter.class);
                accept(overload, "overload", visitor, Overload.class);
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
        OperationDefinition other = (OperationDefinition) obj;
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
            Objects.equals(kind, other.kind) && 
            Objects.equals(experimental, other.experimental) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(description, other.description) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(affectsState, other.affectsState) && 
            Objects.equals(code, other.code) && 
            Objects.equals(comment, other.comment) && 
            Objects.equals(base, other.base) && 
            Objects.equals(resource, other.resource) && 
            Objects.equals(system, other.system) && 
            Objects.equals(type, other.type) && 
            Objects.equals(instance, other.instance) && 
            Objects.equals(inputProfile, other.inputProfile) && 
            Objects.equals(outputProfile, other.outputProfile) && 
            Objects.equals(parameter, other.parameter) && 
            Objects.equals(overload, other.overload);
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
                kind, 
                experimental, 
                date, 
                publisher, 
                contact, 
                description, 
                useContext, 
                jurisdiction, 
                purpose, 
                affectsState, 
                code, 
                comment, 
                base, 
                resource, 
                system, 
                type, 
                instance, 
                inputProfile, 
                outputProfile, 
                parameter, 
                overload);
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
        private OperationKind kind;
        private Boolean experimental;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private Boolean affectsState;
        private Code code;
        private Markdown comment;
        private Canonical base;
        private List<ResourceTypeCode> resource = new ArrayList<>();
        private Boolean system;
        private Boolean type;
        private Boolean instance;
        private Canonical inputProfile;
        private Canonical outputProfile;
        private List<Parameter> parameter = new ArrayList<>();
        private List<Overload> overload = new ArrayList<>();

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
         * An absolute URI that is used to identify this operation definition when it is referenced in a specification, model, 
         * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
         * address at which at which an authoritative instance of this operation definition is (or will be) published. This URL 
         * can be the target of a canonical reference. It SHALL remain the same when the operation definition is stored on 
         * different servers.
         * 
         * @param url
         *     Canonical identifier for this operation definition, represented as a URI (globally unique)
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
         *     Business version of the operation definition
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
         * The identifier that is used to identify this version of the operation definition when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the operation definition author and is 
         * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
         * available. There is also no expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of the operation definition
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
         *     Name for this operation definition (computer friendly)
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
         * A natural language name identifying the operation definition. This name should be usable as an identifier for the 
         * module by machine processing applications such as code generation.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     Name for this operation definition (computer friendly)
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
         *     Name for this operation definition (human friendly)
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
         * A short, descriptive, user-friendly title for the operation definition.
         * 
         * @param title
         *     Name for this operation definition (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The status of this operation definition. Enables tracking the life-cycle of the content.
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
         * Whether this is an operation or a named query.
         * 
         * <p>This element is required.
         * 
         * @param kind
         *     operation | query
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder kind(OperationKind kind) {
            this.kind = kind;
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
         * A Boolean value to indicate that this operation definition is authored for testing purposes (or 
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
         * The date (and optionally time) when the operation definition was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the operation definition changes.
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
         * The name of the organization or individual that published the operation definition.
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
         * A free text natural language description of the operation definition from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the operation definition
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
         * may be used to assist with indexing and searching for appropriate operation definition instances.
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
         * may be used to assist with indexing and searching for appropriate operation definition instances.
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
         * A legal or geographic region in which the operation definition is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for operation definition (if applicable)
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
         * A legal or geographic region in which the operation definition is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for operation definition (if applicable)
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
         * Explanation of why this operation definition is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this operation definition is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * Convenience method for setting {@code affectsState}.
         * 
         * @param affectsState
         *     Whether content is changed by the operation
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #affectsState(com.ibm.fhir.model.type.Boolean)
         */
        public Builder affectsState(java.lang.Boolean affectsState) {
            this.affectsState = (affectsState == null) ? null : Boolean.of(affectsState);
            return this;
        }

        /**
         * Whether the operation affects state. Side effects such as producing audit trail entries do not count as 'affecting 
         * state'.
         * 
         * @param affectsState
         *     Whether content is changed by the operation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder affectsState(Boolean affectsState) {
            this.affectsState = affectsState;
            return this;
        }

        /**
         * The name used to invoke the operation.
         * 
         * <p>This element is required.
         * 
         * @param code
         *     Name used to invoke the operation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Code code) {
            this.code = code;
            return this;
        }

        /**
         * Additional information about how to use this operation or named query.
         * 
         * @param comment
         *     Additional information about use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder comment(Markdown comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Indicates that this operation definition is a constraining profile on the base.
         * 
         * @param base
         *     Marks this as a profile of the base
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder base(Canonical base) {
            this.base = base;
            return this;
        }

        /**
         * The types on which this operation can be executed.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param resource
         *     Types this operation applies to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder resource(ResourceTypeCode... resource) {
            for (ResourceTypeCode value : resource) {
                this.resource.add(value);
            }
            return this;
        }

        /**
         * The types on which this operation can be executed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param resource
         *     Types this operation applies to
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder resource(Collection<ResourceTypeCode> resource) {
            this.resource = new ArrayList<>(resource);
            return this;
        }

        /**
         * Convenience method for setting {@code system}.
         * 
         * <p>This element is required.
         * 
         * @param system
         *     Invoke at the system level?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #system(com.ibm.fhir.model.type.Boolean)
         */
        public Builder system(java.lang.Boolean system) {
            this.system = (system == null) ? null : Boolean.of(system);
            return this;
        }

        /**
         * Indicates whether this operation or named query can be invoked at the system level (e.g. without needing to choose a 
         * resource type for the context).
         * 
         * <p>This element is required.
         * 
         * @param system
         *     Invoke at the system level?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder system(Boolean system) {
            this.system = system;
            return this;
        }

        /**
         * Convenience method for setting {@code type}.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     Invoke at the type level?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #type(com.ibm.fhir.model.type.Boolean)
         */
        public Builder type(java.lang.Boolean type) {
            this.type = (type == null) ? null : Boolean.of(type);
            return this;
        }

        /**
         * Indicates whether this operation or named query can be invoked at the resource type level for any given resource type 
         * level (e.g. without needing to choose a specific resource id for the context).
         * 
         * <p>This element is required.
         * 
         * @param type
         *     Invoke at the type level?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(Boolean type) {
            this.type = type;
            return this;
        }

        /**
         * Convenience method for setting {@code instance}.
         * 
         * <p>This element is required.
         * 
         * @param instance
         *     Invoke on an instance?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #instance(com.ibm.fhir.model.type.Boolean)
         */
        public Builder instance(java.lang.Boolean instance) {
            this.instance = (instance == null) ? null : Boolean.of(instance);
            return this;
        }

        /**
         * Indicates whether this operation can be invoked on a particular instance of one of the given types.
         * 
         * <p>This element is required.
         * 
         * @param instance
         *     Invoke on an instance?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instance(Boolean instance) {
            this.instance = instance;
            return this;
        }

        /**
         * Additional validation information for the in parameters - a single profile that covers all the parameters. The profile 
         * is a constraint on the parameters resource as a whole.
         * 
         * @param inputProfile
         *     Validation information for in parameters
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder inputProfile(Canonical inputProfile) {
            this.inputProfile = inputProfile;
            return this;
        }

        /**
         * Additional validation information for the out parameters - a single profile that covers all the parameters. The 
         * profile is a constraint on the parameters resource.
         * 
         * @param outputProfile
         *     Validation information for out parameters
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder outputProfile(Canonical outputProfile) {
            this.outputProfile = outputProfile;
            return this;
        }

        /**
         * The parameters for the operation/query.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param parameter
         *     Parameters for the operation/query
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
         * The parameters for the operation/query.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param parameter
         *     Parameters for the operation/query
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
         * Defines an appropriate combination of parameters to use when invoking this operation, to help code generators when 
         * generating overloaded parameter sets for this operation.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param overload
         *     Define overloaded variants for when generating code
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder overload(Overload... overload) {
            for (Overload value : overload) {
                this.overload.add(value);
            }
            return this;
        }

        /**
         * Defines an appropriate combination of parameters to use when invoking this operation, to help code generators when 
         * generating overloaded parameter sets for this operation.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param overload
         *     Define overloaded variants for when generating code
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder overload(Collection<Overload> overload) {
            this.overload = new ArrayList<>(overload);
            return this;
        }

        /**
         * Build the {@link OperationDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>name</li>
         * <li>status</li>
         * <li>kind</li>
         * <li>code</li>
         * <li>system</li>
         * <li>type</li>
         * <li>instance</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link OperationDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid OperationDefinition per the base specification
         */
        @Override
        public OperationDefinition build() {
            OperationDefinition operationDefinition = new OperationDefinition(this);
            if (validating) {
                validate(operationDefinition);
            }
            return operationDefinition;
        }

        protected void validate(OperationDefinition operationDefinition) {
            super.validate(operationDefinition);
            ValidationSupport.requireNonNull(operationDefinition.name, "name");
            ValidationSupport.requireNonNull(operationDefinition.status, "status");
            ValidationSupport.requireNonNull(operationDefinition.kind, "kind");
            ValidationSupport.checkList(operationDefinition.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(operationDefinition.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(operationDefinition.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.requireNonNull(operationDefinition.code, "code");
            ValidationSupport.checkList(operationDefinition.resource, "resource", ResourceTypeCode.class);
            ValidationSupport.requireNonNull(operationDefinition.system, "system");
            ValidationSupport.requireNonNull(operationDefinition.type, "type");
            ValidationSupport.requireNonNull(operationDefinition.instance, "instance");
            ValidationSupport.checkList(operationDefinition.parameter, "parameter", Parameter.class);
            ValidationSupport.checkList(operationDefinition.overload, "overload", Overload.class);
        }

        protected Builder from(OperationDefinition operationDefinition) {
            super.from(operationDefinition);
            url = operationDefinition.url;
            version = operationDefinition.version;
            name = operationDefinition.name;
            title = operationDefinition.title;
            status = operationDefinition.status;
            kind = operationDefinition.kind;
            experimental = operationDefinition.experimental;
            date = operationDefinition.date;
            publisher = operationDefinition.publisher;
            contact.addAll(operationDefinition.contact);
            description = operationDefinition.description;
            useContext.addAll(operationDefinition.useContext);
            jurisdiction.addAll(operationDefinition.jurisdiction);
            purpose = operationDefinition.purpose;
            affectsState = operationDefinition.affectsState;
            code = operationDefinition.code;
            comment = operationDefinition.comment;
            base = operationDefinition.base;
            resource.addAll(operationDefinition.resource);
            system = operationDefinition.system;
            type = operationDefinition.type;
            instance = operationDefinition.instance;
            inputProfile = operationDefinition.inputProfile;
            outputProfile = operationDefinition.outputProfile;
            parameter.addAll(operationDefinition.parameter);
            overload.addAll(operationDefinition.overload);
            return this;
        }
    }

    /**
     * The parameters for the operation/query.
     */
    public static class Parameter extends BackboneElement {
        @Required
        private final Code name;
        @com.ibm.fhir.model.annotation.Binding(
            bindingName = "OperationParameterUse",
            strength = BindingStrength.Value.REQUIRED,
            description = "Whether an operation parameter is an input or an output parameter.",
            valueSet = "http://hl7.org/fhir/ValueSet/operation-parameter-use|4.3.0-CIBUILD"
        )
        @Required
        private final OperationParameterUse use;
        @Required
        private final Integer min;
        @Required
        private final String max;
        private final String documentation;
        @com.ibm.fhir.model.annotation.Binding(
            bindingName = "FHIRAllTypes",
            strength = BindingStrength.Value.REQUIRED,
            description = "A list of all the concrete types defined in this version of the FHIR specification - Abstract Types, Data Types and Resource Types.",
            valueSet = "http://hl7.org/fhir/ValueSet/all-types|4.3.0-CIBUILD"
        )
        private final FHIRAllTypes type;
        private final List<Canonical> targetProfile;
        @com.ibm.fhir.model.annotation.Binding(
            bindingName = "SearchParamType",
            strength = BindingStrength.Value.REQUIRED,
            description = "Data types allowed to be used for search parameters.",
            valueSet = "http://hl7.org/fhir/ValueSet/search-param-type|4.3.0-CIBUILD"
        )
        private final SearchParamType searchType;
        private final Binding binding;
        private final List<ReferencedFrom> referencedFrom;
        private final List<OperationDefinition.Parameter> part;

        private Parameter(Builder builder) {
            super(builder);
            name = builder.name;
            use = builder.use;
            min = builder.min;
            max = builder.max;
            documentation = builder.documentation;
            type = builder.type;
            targetProfile = Collections.unmodifiableList(builder.targetProfile);
            searchType = builder.searchType;
            binding = builder.binding;
            referencedFrom = Collections.unmodifiableList(builder.referencedFrom);
            part = Collections.unmodifiableList(builder.part);
        }

        /**
         * The name of used to identify the parameter.
         * 
         * @return
         *     An immutable object of type {@link Code} that is non-null.
         */
        public Code getName() {
            return name;
        }

        /**
         * Whether this is an input or an output parameter.
         * 
         * @return
         *     An immutable object of type {@link OperationParameterUse} that is non-null.
         */
        public OperationParameterUse getUse() {
            return use;
        }

        /**
         * The minimum number of times this parameter SHALL appear in the request or response.
         * 
         * @return
         *     An immutable object of type {@link Integer} that is non-null.
         */
        public Integer getMin() {
            return min;
        }

        /**
         * The maximum number of times this element is permitted to appear in the request or response.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getMax() {
            return max;
        }

        /**
         * Describes the meaning or use of this parameter.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDocumentation() {
            return documentation;
        }

        /**
         * The type for this parameter.
         * 
         * @return
         *     An immutable object of type {@link FHIRAllTypes} that may be null.
         */
        public FHIRAllTypes getType() {
            return type;
        }

        /**
         * Used when the type is "Reference" or "canonical", and identifies a profile structure or implementation Guide that 
         * applies to the target of the reference this parameter refers to. If any profiles are specified, then the content must 
         * conform to at least one of them. The URL can be a local reference - to a contained StructureDefinition, or a reference 
         * to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, 
         * the target resource SHALL conform to at least one profile defined in the implementation guide.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
         */
        public List<Canonical> getTargetProfile() {
            return targetProfile;
        }

        /**
         * How the parameter is understood as a search parameter. This is only used if the parameter type is 'string'.
         * 
         * @return
         *     An immutable object of type {@link SearchParamType} that may be null.
         */
        public SearchParamType getSearchType() {
            return searchType;
        }

        /**
         * Binds to a value set if this parameter is coded (code, Coding, CodeableConcept).
         * 
         * @return
         *     An immutable object of type {@link Binding} that may be null.
         */
        public Binding getBinding() {
            return binding;
        }

        /**
         * Identifies other resource parameters within the operation invocation that are expected to resolve to this resource.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ReferencedFrom} that may be empty.
         */
        public List<ReferencedFrom> getReferencedFrom() {
            return referencedFrom;
        }

        /**
         * The parts of a nested Parameter.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Parameter} that may be empty.
         */
        public List<OperationDefinition.Parameter> getPart() {
            return part;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (use != null) || 
                (min != null) || 
                (max != null) || 
                (documentation != null) || 
                (type != null) || 
                !targetProfile.isEmpty() || 
                (searchType != null) || 
                (binding != null) || 
                !referencedFrom.isEmpty() || 
                !part.isEmpty();
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
                    accept(use, "use", visitor);
                    accept(min, "min", visitor);
                    accept(max, "max", visitor);
                    accept(documentation, "documentation", visitor);
                    accept(type, "type", visitor);
                    accept(targetProfile, "targetProfile", visitor, Canonical.class);
                    accept(searchType, "searchType", visitor);
                    accept(binding, "binding", visitor);
                    accept(referencedFrom, "referencedFrom", visitor, ReferencedFrom.class);
                    accept(part, "part", visitor, OperationDefinition.Parameter.class);
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
                Objects.equals(use, other.use) && 
                Objects.equals(min, other.min) && 
                Objects.equals(max, other.max) && 
                Objects.equals(documentation, other.documentation) && 
                Objects.equals(type, other.type) && 
                Objects.equals(targetProfile, other.targetProfile) && 
                Objects.equals(searchType, other.searchType) && 
                Objects.equals(binding, other.binding) && 
                Objects.equals(referencedFrom, other.referencedFrom) && 
                Objects.equals(part, other.part);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    use, 
                    min, 
                    max, 
                    documentation, 
                    type, 
                    targetProfile, 
                    searchType, 
                    binding, 
                    referencedFrom, 
                    part);
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
            private OperationParameterUse use;
            private Integer min;
            private String max;
            private String documentation;
            private FHIRAllTypes type;
            private List<Canonical> targetProfile = new ArrayList<>();
            private SearchParamType searchType;
            private Binding binding;
            private List<ReferencedFrom> referencedFrom = new ArrayList<>();
            private List<OperationDefinition.Parameter> part = new ArrayList<>();

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
             * The name of used to identify the parameter.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     Name in Parameters.parameter.name or in URL
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(Code name) {
                this.name = name;
                return this;
            }

            /**
             * Whether this is an input or an output parameter.
             * 
             * <p>This element is required.
             * 
             * @param use
             *     in | out
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder use(OperationParameterUse use) {
                this.use = use;
                return this;
            }

            /**
             * Convenience method for setting {@code min}.
             * 
             * <p>This element is required.
             * 
             * @param min
             *     Minimum Cardinality
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
             * The minimum number of times this parameter SHALL appear in the request or response.
             * 
             * <p>This element is required.
             * 
             * @param min
             *     Minimum Cardinality
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
             * <p>This element is required.
             * 
             * @param max
             *     Maximum Cardinality (a number or *)
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
             * The maximum number of times this element is permitted to appear in the request or response.
             * 
             * <p>This element is required.
             * 
             * @param max
             *     Maximum Cardinality (a number or *)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder max(String max) {
                this.max = max;
                return this;
            }

            /**
             * Convenience method for setting {@code documentation}.
             * 
             * @param documentation
             *     Description of meaning/use
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
             * Describes the meaning or use of this parameter.
             * 
             * @param documentation
             *     Description of meaning/use
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder documentation(String documentation) {
                this.documentation = documentation;
                return this;
            }

            /**
             * The type for this parameter.
             * 
             * @param type
             *     What type this parameter has
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(FHIRAllTypes type) {
                this.type = type;
                return this;
            }

            /**
             * Used when the type is "Reference" or "canonical", and identifies a profile structure or implementation Guide that 
             * applies to the target of the reference this parameter refers to. If any profiles are specified, then the content must 
             * conform to at least one of them. The URL can be a local reference - to a contained StructureDefinition, or a reference 
             * to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, 
             * the target resource SHALL conform to at least one profile defined in the implementation guide.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param targetProfile
             *     If type is Reference | canonical, allowed targets
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder targetProfile(Canonical... targetProfile) {
                for (Canonical value : targetProfile) {
                    this.targetProfile.add(value);
                }
                return this;
            }

            /**
             * Used when the type is "Reference" or "canonical", and identifies a profile structure or implementation Guide that 
             * applies to the target of the reference this parameter refers to. If any profiles are specified, then the content must 
             * conform to at least one of them. The URL can be a local reference - to a contained StructureDefinition, or a reference 
             * to another StructureDefinition or Implementation Guide by a canonical URL. When an implementation guide is specified, 
             * the target resource SHALL conform to at least one profile defined in the implementation guide.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param targetProfile
             *     If type is Reference | canonical, allowed targets
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder targetProfile(Collection<Canonical> targetProfile) {
                this.targetProfile = new ArrayList<>(targetProfile);
                return this;
            }

            /**
             * How the parameter is understood as a search parameter. This is only used if the parameter type is 'string'.
             * 
             * @param searchType
             *     number | date | string | token | reference | composite | quantity | uri | special
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder searchType(SearchParamType searchType) {
                this.searchType = searchType;
                return this;
            }

            /**
             * Binds to a value set if this parameter is coded (code, Coding, CodeableConcept).
             * 
             * @param binding
             *     ValueSet details if this is coded
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder binding(Binding binding) {
                this.binding = binding;
                return this;
            }

            /**
             * Identifies other resource parameters within the operation invocation that are expected to resolve to this resource.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param referencedFrom
             *     References to this parameter
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder referencedFrom(ReferencedFrom... referencedFrom) {
                for (ReferencedFrom value : referencedFrom) {
                    this.referencedFrom.add(value);
                }
                return this;
            }

            /**
             * Identifies other resource parameters within the operation invocation that are expected to resolve to this resource.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param referencedFrom
             *     References to this parameter
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder referencedFrom(Collection<ReferencedFrom> referencedFrom) {
                this.referencedFrom = new ArrayList<>(referencedFrom);
                return this;
            }

            /**
             * The parts of a nested Parameter.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param part
             *     Parts of a nested Parameter
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder part(OperationDefinition.Parameter... part) {
                for (OperationDefinition.Parameter value : part) {
                    this.part.add(value);
                }
                return this;
            }

            /**
             * The parts of a nested Parameter.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param part
             *     Parts of a nested Parameter
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder part(Collection<OperationDefinition.Parameter> part) {
                this.part = new ArrayList<>(part);
                return this;
            }

            /**
             * Build the {@link Parameter}
             * 
             * <p>Required elements:
             * <ul>
             * <li>name</li>
             * <li>use</li>
             * <li>min</li>
             * <li>max</li>
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
                ValidationSupport.requireNonNull(parameter.use, "use");
                ValidationSupport.requireNonNull(parameter.min, "min");
                ValidationSupport.requireNonNull(parameter.max, "max");
                ValidationSupport.checkList(parameter.targetProfile, "targetProfile", Canonical.class);
                ValidationSupport.checkList(parameter.referencedFrom, "referencedFrom", ReferencedFrom.class);
                ValidationSupport.checkList(parameter.part, "part", OperationDefinition.Parameter.class);
                ValidationSupport.requireValueOrChildren(parameter);
            }

            protected Builder from(Parameter parameter) {
                super.from(parameter);
                name = parameter.name;
                use = parameter.use;
                min = parameter.min;
                max = parameter.max;
                documentation = parameter.documentation;
                type = parameter.type;
                targetProfile.addAll(parameter.targetProfile);
                searchType = parameter.searchType;
                binding = parameter.binding;
                referencedFrom.addAll(parameter.referencedFrom);
                part.addAll(parameter.part);
                return this;
            }
        }

        /**
         * Binds to a value set if this parameter is coded (code, Coding, CodeableConcept).
         */
        public static class Binding extends BackboneElement {
            @com.ibm.fhir.model.annotation.Binding(
                bindingName = "BindingStrength",
                strength = BindingStrength.Value.REQUIRED,
                description = "Indication of the degree of conformance expectations associated with a binding.",
                valueSet = "http://hl7.org/fhir/ValueSet/binding-strength|4.3.0-CIBUILD"
            )
            @Required
            private final BindingStrength strength;
            @Required
            private final Canonical valueSet;

            private Binding(Builder builder) {
                super(builder);
                strength = builder.strength;
                valueSet = builder.valueSet;
            }

            /**
             * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the 
             * provided value set must be adhered to in the instances.
             * 
             * @return
             *     An immutable object of type {@link BindingStrength} that is non-null.
             */
            public BindingStrength getStrength() {
                return strength;
            }

            /**
             * Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.
             * 
             * @return
             *     An immutable object of type {@link Canonical} that is non-null.
             */
            public Canonical getValueSet() {
                return valueSet;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (strength != null) || 
                    (valueSet != null);
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
                        accept(strength, "strength", visitor);
                        accept(valueSet, "valueSet", visitor);
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
                Binding other = (Binding) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(strength, other.strength) && 
                    Objects.equals(valueSet, other.valueSet);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        strength, 
                        valueSet);
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
                private BindingStrength strength;
                private Canonical valueSet;

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
                 * Indicates the degree of conformance expectations associated with this binding - that is, the degree to which the 
                 * provided value set must be adhered to in the instances.
                 * 
                 * <p>This element is required.
                 * 
                 * @param strength
                 *     required | extensible | preferred | example
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder strength(BindingStrength strength) {
                    this.strength = strength;
                    return this;
                }

                /**
                 * Points to the value set or external definition (e.g. implicit value set) that identifies the set of codes to be used.
                 * 
                 * <p>This element is required.
                 * 
                 * @param valueSet
                 *     Source of value set
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder valueSet(Canonical valueSet) {
                    this.valueSet = valueSet;
                    return this;
                }

                /**
                 * Build the {@link Binding}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>strength</li>
                 * <li>valueSet</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Binding}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Binding per the base specification
                 */
                @Override
                public Binding build() {
                    Binding binding = new Binding(this);
                    if (validating) {
                        validate(binding);
                    }
                    return binding;
                }

                protected void validate(Binding binding) {
                    super.validate(binding);
                    ValidationSupport.requireNonNull(binding.strength, "strength");
                    ValidationSupport.requireNonNull(binding.valueSet, "valueSet");
                    ValidationSupport.requireValueOrChildren(binding);
                }

                protected Builder from(Binding binding) {
                    super.from(binding);
                    strength = binding.strength;
                    valueSet = binding.valueSet;
                    return this;
                }
            }
        }

        /**
         * Identifies other resource parameters within the operation invocation that are expected to resolve to this resource.
         */
        public static class ReferencedFrom extends BackboneElement {
            @Required
            private final String source;
            private final String sourceId;

            private ReferencedFrom(Builder builder) {
                super(builder);
                source = builder.source;
                sourceId = builder.sourceId;
            }

            /**
             * The name of the parameter or dot-separated path of parameter names pointing to the resource parameter that is expected 
             * to contain a reference to this resource.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getSource() {
                return source;
            }

            /**
             * The id of the element in the referencing resource that is expected to resolve to this resource.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getSourceId() {
                return sourceId;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (source != null) || 
                    (sourceId != null);
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
                        accept(source, "source", visitor);
                        accept(sourceId, "sourceId", visitor);
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
                ReferencedFrom other = (ReferencedFrom) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(source, other.source) && 
                    Objects.equals(sourceId, other.sourceId);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        source, 
                        sourceId);
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
                private String source;
                private String sourceId;

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
                 * Convenience method for setting {@code source}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param source
                 *     Referencing parameter
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #source(com.ibm.fhir.model.type.String)
                 */
                public Builder source(java.lang.String source) {
                    this.source = (source == null) ? null : String.of(source);
                    return this;
                }

                /**
                 * The name of the parameter or dot-separated path of parameter names pointing to the resource parameter that is expected 
                 * to contain a reference to this resource.
                 * 
                 * <p>This element is required.
                 * 
                 * @param source
                 *     Referencing parameter
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder source(String source) {
                    this.source = source;
                    return this;
                }

                /**
                 * Convenience method for setting {@code sourceId}.
                 * 
                 * @param sourceId
                 *     Element id of reference
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #sourceId(com.ibm.fhir.model.type.String)
                 */
                public Builder sourceId(java.lang.String sourceId) {
                    this.sourceId = (sourceId == null) ? null : String.of(sourceId);
                    return this;
                }

                /**
                 * The id of the element in the referencing resource that is expected to resolve to this resource.
                 * 
                 * @param sourceId
                 *     Element id of reference
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder sourceId(String sourceId) {
                    this.sourceId = sourceId;
                    return this;
                }

                /**
                 * Build the {@link ReferencedFrom}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>source</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link ReferencedFrom}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid ReferencedFrom per the base specification
                 */
                @Override
                public ReferencedFrom build() {
                    ReferencedFrom referencedFrom = new ReferencedFrom(this);
                    if (validating) {
                        validate(referencedFrom);
                    }
                    return referencedFrom;
                }

                protected void validate(ReferencedFrom referencedFrom) {
                    super.validate(referencedFrom);
                    ValidationSupport.requireNonNull(referencedFrom.source, "source");
                    ValidationSupport.requireValueOrChildren(referencedFrom);
                }

                protected Builder from(ReferencedFrom referencedFrom) {
                    super.from(referencedFrom);
                    source = referencedFrom.source;
                    sourceId = referencedFrom.sourceId;
                    return this;
                }
            }
        }
    }

    /**
     * Defines an appropriate combination of parameters to use when invoking this operation, to help code generators when 
     * generating overloaded parameter sets for this operation.
     */
    public static class Overload extends BackboneElement {
        private final List<String> parameterName;
        private final String comment;

        private Overload(Builder builder) {
            super(builder);
            parameterName = Collections.unmodifiableList(builder.parameterName);
            comment = builder.comment;
        }

        /**
         * Name of parameter to include in overload.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
         */
        public List<String> getParameterName() {
            return parameterName;
        }

        /**
         * Comments to go on overload.
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
                !parameterName.isEmpty() || 
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
                    accept(parameterName, "parameterName", visitor, String.class);
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
            Overload other = (Overload) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(parameterName, other.parameterName) && 
                Objects.equals(comment, other.comment);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    parameterName, 
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
            private List<String> parameterName = new ArrayList<>();
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
             * Convenience method for setting {@code parameterName}.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param parameterName
             *     Name of parameter to include in overload
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #parameterName(com.ibm.fhir.model.type.String)
             */
            public Builder parameterName(java.lang.String... parameterName) {
                for (java.lang.String value : parameterName) {
                    this.parameterName.add((value == null) ? null : String.of(value));
                }
                return this;
            }

            /**
             * Name of parameter to include in overload.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param parameterName
             *     Name of parameter to include in overload
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder parameterName(String... parameterName) {
                for (String value : parameterName) {
                    this.parameterName.add(value);
                }
                return this;
            }

            /**
             * Name of parameter to include in overload.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param parameterName
             *     Name of parameter to include in overload
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder parameterName(Collection<String> parameterName) {
                this.parameterName = new ArrayList<>(parameterName);
                return this;
            }

            /**
             * Convenience method for setting {@code comment}.
             * 
             * @param comment
             *     Comments to go on overload
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
             * Comments to go on overload.
             * 
             * @param comment
             *     Comments to go on overload
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder comment(String comment) {
                this.comment = comment;
                return this;
            }

            /**
             * Build the {@link Overload}
             * 
             * @return
             *     An immutable object of type {@link Overload}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Overload per the base specification
             */
            @Override
            public Overload build() {
                Overload overload = new Overload(this);
                if (validating) {
                    validate(overload);
                }
                return overload;
            }

            protected void validate(Overload overload) {
                super.validate(overload);
                ValidationSupport.checkList(overload.parameterName, "parameterName", String.class);
                ValidationSupport.requireValueOrChildren(overload);
            }

            protected Builder from(Overload overload) {
                super.from(overload);
                parameterName.addAll(overload.parameterName);
                comment = overload.comment;
                return this;
            }
        }
    }
}
