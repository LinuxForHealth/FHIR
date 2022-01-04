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
import com.ibm.fhir.model.annotation.Choice;
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
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.CodeSystemContentMode;
import com.ibm.fhir.model.type.code.CodeSystemHierarchyMeaning;
import com.ibm.fhir.model.type.code.FilterOperator;
import com.ibm.fhir.model.type.code.PropertyType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement and 
 * its key properties, and optionally define a part or all of its content.
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.Value.NORMATIVE
)
@Constraint(
    id = "csd-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/CodeSystem"
)
@Constraint(
    id = "csd-1",
    level = "Rule",
    location = "(base)",
    description = "Within a code system definition, all the codes SHALL be unique",
    expression = "concept.code.combine($this.descendants().concept.code).isDistinct()",
    source = "http://hl7.org/fhir/StructureDefinition/CodeSystem"
)
@Constraint(
    id = "codeSystem-2",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/CodeSystem",
    generated = true
)
@Constraint(
    id = "codeSystem-3",
    level = "Warning",
    location = "concept.designation.language",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/languages",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/CodeSystem",
    generated = true
)
@Constraint(
    id = "codeSystem-4",
    level = "Warning",
    location = "concept.designation.use",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/designation-use",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/designation-use', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/CodeSystem",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CodeSystem extends DomainResource {
    @Summary
    private final Uri url;
    @Summary
    private final List<Identifier> identifier;
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
    private final Boolean caseSensitive;
    @Summary
    private final Canonical valueSet;
    @Summary
    @Binding(
        bindingName = "CodeSystemHierarchyMeaning",
        strength = BindingStrength.Value.REQUIRED,
        description = "The meaning of the hierarchy of concepts in a code system.",
        valueSet = "http://hl7.org/fhir/ValueSet/codesystem-hierarchy-meaning|4.3.0-CIBUILD"
    )
    private final CodeSystemHierarchyMeaning hierarchyMeaning;
    @Summary
    private final Boolean compositional;
    @Summary
    private final Boolean versionNeeded;
    @Summary
    @Binding(
        bindingName = "CodeSystemContentMode",
        strength = BindingStrength.Value.REQUIRED,
        description = "The extent of the content of the code system (the concepts and codes it defines) are represented in a code system resource.",
        valueSet = "http://hl7.org/fhir/ValueSet/codesystem-content-mode|4.3.0-CIBUILD"
    )
    @Required
    private final CodeSystemContentMode content;
    @Summary
    private final Canonical supplements;
    @Summary
    private final UnsignedInt count;
    @Summary
    private final List<Filter> filter;
    @Summary
    private final List<Property> property;
    private final List<Concept> concept;

    private CodeSystem(Builder builder) {
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
        caseSensitive = builder.caseSensitive;
        valueSet = builder.valueSet;
        hierarchyMeaning = builder.hierarchyMeaning;
        compositional = builder.compositional;
        versionNeeded = builder.versionNeeded;
        content = builder.content;
        supplements = builder.supplements;
        count = builder.count;
        filter = Collections.unmodifiableList(builder.filter);
        property = Collections.unmodifiableList(builder.property);
        concept = Collections.unmodifiableList(builder.concept);
    }

    /**
     * An absolute URI that is used to identify this code system when it is referenced in a specification, model, design or 
     * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this code system is (or will be) published. This URL can be the target of 
     * a canonical reference. It SHALL remain the same when the code system is stored on different servers. This is used in 
     * [Coding](datatypes.html#Coding).system.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this code system when it is represented in other formats, or referenced 
     * in a specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the code system when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the code system author and is not expected to be 
     * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
     * also no expectation that versions can be placed in a lexicographical sequence. This is used in [Coding](datatypes.
     * html#Coding).version.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the code system. This name should be usable as an identifier for the module by 
     * machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the code system.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The date (and optionally time) when the code system resource was created or revised.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this code system is authored for testing purposes (or education/evaluation/marketing) 
     * and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the code system was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the code system changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the code system.
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
     * A free text natural language description of the code system from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate code system instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the code system is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this code system is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A copyright statement relating to the code system and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the code system.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * If code comparison is case sensitive when codes within this system are compared to each other.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getCaseSensitive() {
        return caseSensitive;
    }

    /**
     * Canonical reference to the value set that contains the entire code system.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getValueSet() {
        return valueSet;
    }

    /**
     * The meaning of the hierarchy of concepts as represented in this resource.
     * 
     * @return
     *     An immutable object of type {@link CodeSystemHierarchyMeaning} that may be null.
     */
    public CodeSystemHierarchyMeaning getHierarchyMeaning() {
        return hierarchyMeaning;
    }

    /**
     * The code system defines a compositional (post-coordination) grammar.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getCompositional() {
        return compositional;
    }

    /**
     * This flag is used to signify that the code system does not commit to concept permanence across versions. If true, a 
     * version must be specified when referencing this code system.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getVersionNeeded() {
        return versionNeeded;
    }

    /**
     * The extent of the content of the code system (the concepts and codes it defines) are represented in this resource 
     * instance.
     * 
     * @return
     *     An immutable object of type {@link CodeSystemContentMode} that is non-null.
     */
    public CodeSystemContentMode getContent() {
        return content;
    }

    /**
     * The canonical URL of the code system that this code system supplement is adding designations and properties to.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getSupplements() {
        return supplements;
    }

    /**
     * The total number of concepts defined by the code system. Where the code system has a compositional grammar, the basis 
     * of this count is defined by the system steward.
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt} that may be null.
     */
    public UnsignedInt getCount() {
        return count;
    }

    /**
     * A filter that can be used in a value set compose statement when selecting concepts using a filter.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Filter} that may be empty.
     */
    public List<Filter> getFilter() {
        return filter;
    }

    /**
     * A property defines an additional slot through which additional information can be provided about a concept.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Property} that may be empty.
     */
    public List<Property> getProperty() {
        return property;
    }

    /**
     * Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be 
     * consulted to determine what the meanings of the hierarchical relationships are.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Concept} that may be empty.
     */
    public List<Concept> getConcept() {
        return concept;
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
            (caseSensitive != null) || 
            (valueSet != null) || 
            (hierarchyMeaning != null) || 
            (compositional != null) || 
            (versionNeeded != null) || 
            (content != null) || 
            (supplements != null) || 
            (count != null) || 
            !filter.isEmpty() || 
            !property.isEmpty() || 
            !concept.isEmpty();
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
                accept(caseSensitive, "caseSensitive", visitor);
                accept(valueSet, "valueSet", visitor);
                accept(hierarchyMeaning, "hierarchyMeaning", visitor);
                accept(compositional, "compositional", visitor);
                accept(versionNeeded, "versionNeeded", visitor);
                accept(content, "content", visitor);
                accept(supplements, "supplements", visitor);
                accept(count, "count", visitor);
                accept(filter, "filter", visitor, Filter.class);
                accept(property, "property", visitor, Property.class);
                accept(concept, "concept", visitor, Concept.class);
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
            Objects.equals(caseSensitive, other.caseSensitive) && 
            Objects.equals(valueSet, other.valueSet) && 
            Objects.equals(hierarchyMeaning, other.hierarchyMeaning) && 
            Objects.equals(compositional, other.compositional) && 
            Objects.equals(versionNeeded, other.versionNeeded) && 
            Objects.equals(content, other.content) && 
            Objects.equals(supplements, other.supplements) && 
            Objects.equals(count, other.count) && 
            Objects.equals(filter, other.filter) && 
            Objects.equals(property, other.property) && 
            Objects.equals(concept, other.concept);
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
                caseSensitive, 
                valueSet, 
                hierarchyMeaning, 
                compositional, 
                versionNeeded, 
                content, 
                supplements, 
                count, 
                filter, 
                property, 
                concept);
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
        private Boolean caseSensitive;
        private Canonical valueSet;
        private CodeSystemHierarchyMeaning hierarchyMeaning;
        private Boolean compositional;
        private Boolean versionNeeded;
        private CodeSystemContentMode content;
        private Canonical supplements;
        private UnsignedInt count;
        private List<Filter> filter = new ArrayList<>();
        private List<Property> property = new ArrayList<>();
        private List<Concept> concept = new ArrayList<>();

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
         * An absolute URI that is used to identify this code system when it is referenced in a specification, model, design or 
         * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
         * which at which an authoritative instance of this code system is (or will be) published. This URL can be the target of 
         * a canonical reference. It SHALL remain the same when the code system is stored on different servers. This is used in 
         * [Coding](datatypes.html#Coding).system.
         * 
         * @param url
         *     Canonical identifier for this code system, represented as a URI (globally unique) (Coding.system)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this code system when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the code system (business identifier)
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
         * A formal identifier that is used to identify this code system when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the code system (business identifier)
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
         *     Business version of the code system (Coding.version)
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
         * The identifier that is used to identify this version of the code system when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the code system author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence. This is used in [Coding](datatypes.
         * html#Coding).version.
         * 
         * @param version
         *     Business version of the code system (Coding.version)
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
         *     Name for this code system (computer friendly)
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
         * A natural language name identifying the code system. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
         * 
         * @param name
         *     Name for this code system (computer friendly)
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
         *     Name for this code system (human friendly)
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
         * A short, descriptive, user-friendly title for the code system.
         * 
         * @param title
         *     Name for this code system (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The date (and optionally time) when the code system resource was created or revised.
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
         * A Boolean value to indicate that this code system is authored for testing purposes (or education/evaluation/marketing) 
         * and is not intended to be used for genuine usage.
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
         * The date (and optionally time) when the code system was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the code system changes.
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
         * The name of the organization or individual that published the code system.
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
         * A free text natural language description of the code system from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the code system
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
         * may be used to assist with indexing and searching for appropriate code system instances.
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
         * may be used to assist with indexing and searching for appropriate code system instances.
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
         * A legal or geographic region in which the code system is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for code system (if applicable)
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
         * A legal or geographic region in which the code system is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for code system (if applicable)
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
         * Explanation of why this code system is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this code system is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * A copyright statement relating to the code system and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the code system.
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
         * Convenience method for setting {@code caseSensitive}.
         * 
         * @param caseSensitive
         *     If code comparison is case sensitive
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #caseSensitive(com.ibm.fhir.model.type.Boolean)
         */
        public Builder caseSensitive(java.lang.Boolean caseSensitive) {
            this.caseSensitive = (caseSensitive == null) ? null : Boolean.of(caseSensitive);
            return this;
        }

        /**
         * If code comparison is case sensitive when codes within this system are compared to each other.
         * 
         * @param caseSensitive
         *     If code comparison is case sensitive
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder caseSensitive(Boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
            return this;
        }

        /**
         * Canonical reference to the value set that contains the entire code system.
         * 
         * @param valueSet
         *     Canonical reference to the value set with entire code system
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder valueSet(Canonical valueSet) {
            this.valueSet = valueSet;
            return this;
        }

        /**
         * The meaning of the hierarchy of concepts as represented in this resource.
         * 
         * @param hierarchyMeaning
         *     grouped-by | is-a | part-of | classified-with
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder hierarchyMeaning(CodeSystemHierarchyMeaning hierarchyMeaning) {
            this.hierarchyMeaning = hierarchyMeaning;
            return this;
        }

        /**
         * Convenience method for setting {@code compositional}.
         * 
         * @param compositional
         *     If code system defines a compositional grammar
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
         * The code system defines a compositional (post-coordination) grammar.
         * 
         * @param compositional
         *     If code system defines a compositional grammar
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder compositional(Boolean compositional) {
            this.compositional = compositional;
            return this;
        }

        /**
         * Convenience method for setting {@code versionNeeded}.
         * 
         * @param versionNeeded
         *     If definitions are not stable
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #versionNeeded(com.ibm.fhir.model.type.Boolean)
         */
        public Builder versionNeeded(java.lang.Boolean versionNeeded) {
            this.versionNeeded = (versionNeeded == null) ? null : Boolean.of(versionNeeded);
            return this;
        }

        /**
         * This flag is used to signify that the code system does not commit to concept permanence across versions. If true, a 
         * version must be specified when referencing this code system.
         * 
         * @param versionNeeded
         *     If definitions are not stable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder versionNeeded(Boolean versionNeeded) {
            this.versionNeeded = versionNeeded;
            return this;
        }

        /**
         * The extent of the content of the code system (the concepts and codes it defines) are represented in this resource 
         * instance.
         * 
         * <p>This element is required.
         * 
         * @param content
         *     not-present | example | fragment | complete | supplement
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder content(CodeSystemContentMode content) {
            this.content = content;
            return this;
        }

        /**
         * The canonical URL of the code system that this code system supplement is adding designations and properties to.
         * 
         * @param supplements
         *     Canonical URL of Code System this adds designations and properties to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supplements(Canonical supplements) {
            this.supplements = supplements;
            return this;
        }

        /**
         * The total number of concepts defined by the code system. Where the code system has a compositional grammar, the basis 
         * of this count is defined by the system steward.
         * 
         * @param count
         *     Total concepts in the code system
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder count(UnsignedInt count) {
            this.count = count;
            return this;
        }

        /**
         * A filter that can be used in a value set compose statement when selecting concepts using a filter.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param filter
         *     Filter that can be used in a value set
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
         * A filter that can be used in a value set compose statement when selecting concepts using a filter.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param filter
         *     Filter that can be used in a value set
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
         * A property defines an additional slot through which additional information can be provided about a concept.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param property
         *     Additional information supplied about each concept
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder property(Property... property) {
            for (Property value : property) {
                this.property.add(value);
            }
            return this;
        }

        /**
         * A property defines an additional slot through which additional information can be provided about a concept.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param property
         *     Additional information supplied about each concept
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder property(Collection<Property> property) {
            this.property = new ArrayList<>(property);
            return this;
        }

        /**
         * Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be 
         * consulted to determine what the meanings of the hierarchical relationships are.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param concept
         *     Concepts in the code system
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder concept(Concept... concept) {
            for (Concept value : concept) {
                this.concept.add(value);
            }
            return this;
        }

        /**
         * Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be 
         * consulted to determine what the meanings of the hierarchical relationships are.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param concept
         *     Concepts in the code system
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder concept(Collection<Concept> concept) {
            this.concept = new ArrayList<>(concept);
            return this;
        }

        /**
         * Build the {@link CodeSystem}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>content</li>
         * </ul>
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
            ValidationSupport.checkList(codeSystem.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(codeSystem.status, "status");
            ValidationSupport.checkList(codeSystem.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(codeSystem.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(codeSystem.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.requireNonNull(codeSystem.content, "content");
            ValidationSupport.checkList(codeSystem.filter, "filter", Filter.class);
            ValidationSupport.checkList(codeSystem.property, "property", Property.class);
            ValidationSupport.checkList(codeSystem.concept, "concept", Concept.class);
        }

        protected Builder from(CodeSystem codeSystem) {
            super.from(codeSystem);
            url = codeSystem.url;
            identifier.addAll(codeSystem.identifier);
            version = codeSystem.version;
            name = codeSystem.name;
            title = codeSystem.title;
            status = codeSystem.status;
            experimental = codeSystem.experimental;
            date = codeSystem.date;
            publisher = codeSystem.publisher;
            contact.addAll(codeSystem.contact);
            description = codeSystem.description;
            useContext.addAll(codeSystem.useContext);
            jurisdiction.addAll(codeSystem.jurisdiction);
            purpose = codeSystem.purpose;
            copyright = codeSystem.copyright;
            caseSensitive = codeSystem.caseSensitive;
            valueSet = codeSystem.valueSet;
            hierarchyMeaning = codeSystem.hierarchyMeaning;
            compositional = codeSystem.compositional;
            versionNeeded = codeSystem.versionNeeded;
            content = codeSystem.content;
            supplements = codeSystem.supplements;
            count = codeSystem.count;
            filter.addAll(codeSystem.filter);
            property.addAll(codeSystem.property);
            concept.addAll(codeSystem.concept);
            return this;
        }
    }

    /**
     * A filter that can be used in a value set compose statement when selecting concepts using a filter.
     */
    public static class Filter extends BackboneElement {
        @Summary
        @Required
        private final Code code;
        @Summary
        private final String description;
        @Summary
        @Binding(
            bindingName = "FilterOperator",
            strength = BindingStrength.Value.REQUIRED,
            description = "The kind of operation to perform as a part of a property based filter.",
            valueSet = "http://hl7.org/fhir/ValueSet/filter-operator|4.3.0-CIBUILD"
        )
        @Required
        private final List<FilterOperator> operator;
        @Summary
        @Required
        private final String value;

        private Filter(Builder builder) {
            super(builder);
            code = builder.code;
            description = builder.description;
            operator = Collections.unmodifiableList(builder.operator);
            value = builder.value;
        }

        /**
         * The code that identifies this filter when it is used as a filter in [ValueSet](valueset.html#).compose.include.filter.
         * 
         * @return
         *     An immutable object of type {@link Code} that is non-null.
         */
        public Code getCode() {
            return code;
        }

        /**
         * A description of how or why the filter is used.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * A list of operators that can be used with the filter.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link FilterOperator} that is non-empty.
         */
        public List<FilterOperator> getOperator() {
            return operator;
        }

        /**
         * A description of what the value for the filter should be.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getValue() {
            return value;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (description != null) || 
                !operator.isEmpty() || 
                (value != null);
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
                    accept(description, "description", visitor);
                    accept(operator, "operator", visitor, FilterOperator.class);
                    accept(value, "value", visitor);
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
                Objects.equals(description, other.description) && 
                Objects.equals(operator, other.operator) && 
                Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    description, 
                    operator, 
                    value);
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
            private String description;
            private List<FilterOperator> operator = new ArrayList<>();
            private String value;

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
             * The code that identifies this filter when it is used as a filter in [ValueSet](valueset.html#).compose.include.filter.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     Code that identifies the filter
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(Code code) {
                this.code = code;
                return this;
            }

            /**
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     How or why the filter is used
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
             * A description of how or why the filter is used.
             * 
             * @param description
             *     How or why the filter is used
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * A list of operators that can be used with the filter.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param operator
             *     = | is-a | descendent-of | is-not-a | regex | in | not-in | generalizes | exists
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder operator(FilterOperator... operator) {
                for (FilterOperator value : operator) {
                    this.operator.add(value);
                }
                return this;
            }

            /**
             * A list of operators that can be used with the filter.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param operator
             *     = | is-a | descendent-of | is-not-a | regex | in | not-in | generalizes | exists
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder operator(Collection<FilterOperator> operator) {
                this.operator = new ArrayList<>(operator);
                return this;
            }

            /**
             * Convenience method for setting {@code value}.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     What to use for the value
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
             * A description of what the value for the filter should be.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     What to use for the value
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(String value) {
                this.value = value;
                return this;
            }

            /**
             * Build the {@link Filter}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * <li>operator</li>
             * <li>value</li>
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
                ValidationSupport.checkNonEmptyList(filter.operator, "operator", FilterOperator.class);
                ValidationSupport.requireNonNull(filter.value, "value");
                ValidationSupport.requireValueOrChildren(filter);
            }

            protected Builder from(Filter filter) {
                super.from(filter);
                code = filter.code;
                description = filter.description;
                operator.addAll(filter.operator);
                value = filter.value;
                return this;
            }
        }
    }

    /**
     * A property defines an additional slot through which additional information can be provided about a concept.
     */
    public static class Property extends BackboneElement {
        @Summary
        @Required
        private final Code code;
        @Summary
        private final Uri uri;
        @Summary
        private final String description;
        @Summary
        @Binding(
            bindingName = "PropertyType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of a property value.",
            valueSet = "http://hl7.org/fhir/ValueSet/concept-property-type|4.3.0-CIBUILD"
        )
        @Required
        private final PropertyType type;

        private Property(Builder builder) {
            super(builder);
            code = builder.code;
            uri = builder.uri;
            description = builder.description;
            type = builder.type;
        }

        /**
         * A code that is used to identify the property. The code is used internally (in CodeSystem.concept.property.code) and 
         * also externally, such as in property filters.
         * 
         * @return
         *     An immutable object of type {@link Code} that is non-null.
         */
        public Code getCode() {
            return code;
        }

        /**
         * Reference to the formal meaning of the property. One possible source of meaning is the [Concept Properties](codesystem-
         * concept-properties.html) code system.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getUri() {
            return uri;
        }

        /**
         * A description of the property- why it is defined, and how its value might be used.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * The type of the property value. Properties of type "code" contain a code defined by the code system (e.g. a reference 
         * to another defined concept).
         * 
         * @return
         *     An immutable object of type {@link PropertyType} that is non-null.
         */
        public PropertyType getType() {
            return type;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (uri != null) || 
                (description != null) || 
                (type != null);
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
                    accept(uri, "uri", visitor);
                    accept(description, "description", visitor);
                    accept(type, "type", visitor);
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
            Property other = (Property) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(uri, other.uri) && 
                Objects.equals(description, other.description) && 
                Objects.equals(type, other.type);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    uri, 
                    description, 
                    type);
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
            private Uri uri;
            private String description;
            private PropertyType type;

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
             * A code that is used to identify the property. The code is used internally (in CodeSystem.concept.property.code) and 
             * also externally, such as in property filters.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     Identifies the property on the concepts, and when referred to in operations
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(Code code) {
                this.code = code;
                return this;
            }

            /**
             * Reference to the formal meaning of the property. One possible source of meaning is the [Concept Properties](codesystem-
             * concept-properties.html) code system.
             * 
             * @param uri
             *     Formal identifier for the property
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder uri(Uri uri) {
                this.uri = uri;
                return this;
            }

            /**
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Why the property is defined, and/or what it conveys
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
             * A description of the property- why it is defined, and how its value might be used.
             * 
             * @param description
             *     Why the property is defined, and/or what it conveys
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * The type of the property value. Properties of type "code" contain a code defined by the code system (e.g. a reference 
             * to another defined concept).
             * 
             * <p>This element is required.
             * 
             * @param type
             *     code | Coding | string | integer | boolean | dateTime | decimal
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(PropertyType type) {
                this.type = type;
                return this;
            }

            /**
             * Build the {@link Property}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Property}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Property per the base specification
             */
            @Override
            public Property build() {
                Property property = new Property(this);
                if (validating) {
                    validate(property);
                }
                return property;
            }

            protected void validate(Property property) {
                super.validate(property);
                ValidationSupport.requireNonNull(property.code, "code");
                ValidationSupport.requireNonNull(property.type, "type");
                ValidationSupport.requireValueOrChildren(property);
            }

            protected Builder from(Property property) {
                super.from(property);
                code = property.code;
                uri = property.uri;
                description = property.description;
                type = property.type;
                return this;
            }
        }
    }

    /**
     * Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be 
     * consulted to determine what the meanings of the hierarchical relationships are.
     */
    public static class Concept extends BackboneElement {
        @Required
        private final Code code;
        private final String display;
        private final String definition;
        private final List<Designation> designation;
        private final List<Property> property;
        private final List<CodeSystem.Concept> concept;

        private Concept(Builder builder) {
            super(builder);
            code = builder.code;
            display = builder.display;
            definition = builder.definition;
            designation = Collections.unmodifiableList(builder.designation);
            property = Collections.unmodifiableList(builder.property);
            concept = Collections.unmodifiableList(builder.concept);
        }

        /**
         * A code - a text symbol - that uniquely identifies the concept within the code system.
         * 
         * @return
         *     An immutable object of type {@link Code} that is non-null.
         */
        public Code getCode() {
            return code;
        }

        /**
         * A human readable string that is the recommended default way to present this concept to a user.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDisplay() {
            return display;
        }

        /**
         * The formal definition of the concept. The code system resource does not make formal definitions required, because of 
         * the prevalence of legacy systems. However, they are highly recommended, as without them there is no formal meaning 
         * associated with the concept.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDefinition() {
            return definition;
        }

        /**
         * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular 
         * purposes, etc.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Designation} that may be empty.
         */
        public List<Designation> getDesignation() {
            return designation;
        }

        /**
         * A property value for this concept.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Property} that may be empty.
         */
        public List<Property> getProperty() {
            return property;
        }

        /**
         * Defines children of a concept to produce a hierarchy of concepts. The nature of the relationships is variable (is-
         * a/contains/categorizes) - see hierarchyMeaning.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Concept} that may be empty.
         */
        public List<CodeSystem.Concept> getConcept() {
            return concept;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (display != null) || 
                (definition != null) || 
                !designation.isEmpty() || 
                !property.isEmpty() || 
                !concept.isEmpty();
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
                    accept(display, "display", visitor);
                    accept(definition, "definition", visitor);
                    accept(designation, "designation", visitor, Designation.class);
                    accept(property, "property", visitor, Property.class);
                    accept(concept, "concept", visitor, CodeSystem.Concept.class);
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
            Concept other = (Concept) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(display, other.display) && 
                Objects.equals(definition, other.definition) && 
                Objects.equals(designation, other.designation) && 
                Objects.equals(property, other.property) && 
                Objects.equals(concept, other.concept);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    display, 
                    definition, 
                    designation, 
                    property, 
                    concept);
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
            private String display;
            private String definition;
            private List<Designation> designation = new ArrayList<>();
            private List<Property> property = new ArrayList<>();
            private List<CodeSystem.Concept> concept = new ArrayList<>();

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
             * A code - a text symbol - that uniquely identifies the concept within the code system.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     Code that identifies concept
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(Code code) {
                this.code = code;
                return this;
            }

            /**
             * Convenience method for setting {@code display}.
             * 
             * @param display
             *     Text to display to the user
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #display(com.ibm.fhir.model.type.String)
             */
            public Builder display(java.lang.String display) {
                this.display = (display == null) ? null : String.of(display);
                return this;
            }

            /**
             * A human readable string that is the recommended default way to present this concept to a user.
             * 
             * @param display
             *     Text to display to the user
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder display(String display) {
                this.display = display;
                return this;
            }

            /**
             * Convenience method for setting {@code definition}.
             * 
             * @param definition
             *     Formal definition
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #definition(com.ibm.fhir.model.type.String)
             */
            public Builder definition(java.lang.String definition) {
                this.definition = (definition == null) ? null : String.of(definition);
                return this;
            }

            /**
             * The formal definition of the concept. The code system resource does not make formal definitions required, because of 
             * the prevalence of legacy systems. However, they are highly recommended, as without them there is no formal meaning 
             * associated with the concept.
             * 
             * @param definition
             *     Formal definition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder definition(String definition) {
                this.definition = definition;
                return this;
            }

            /**
             * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular 
             * purposes, etc.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param designation
             *     Additional representations for the concept
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder designation(Designation... designation) {
                for (Designation value : designation) {
                    this.designation.add(value);
                }
                return this;
            }

            /**
             * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular 
             * purposes, etc.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param designation
             *     Additional representations for the concept
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder designation(Collection<Designation> designation) {
                this.designation = new ArrayList<>(designation);
                return this;
            }

            /**
             * A property value for this concept.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param property
             *     Property value for the concept
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder property(Property... property) {
                for (Property value : property) {
                    this.property.add(value);
                }
                return this;
            }

            /**
             * A property value for this concept.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param property
             *     Property value for the concept
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder property(Collection<Property> property) {
                this.property = new ArrayList<>(property);
                return this;
            }

            /**
             * Defines children of a concept to produce a hierarchy of concepts. The nature of the relationships is variable (is-
             * a/contains/categorizes) - see hierarchyMeaning.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param concept
             *     Child Concepts (is-a/contains/categorizes)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder concept(CodeSystem.Concept... concept) {
                for (CodeSystem.Concept value : concept) {
                    this.concept.add(value);
                }
                return this;
            }

            /**
             * Defines children of a concept to produce a hierarchy of concepts. The nature of the relationships is variable (is-
             * a/contains/categorizes) - see hierarchyMeaning.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param concept
             *     Child Concepts (is-a/contains/categorizes)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder concept(Collection<CodeSystem.Concept> concept) {
                this.concept = new ArrayList<>(concept);
                return this;
            }

            /**
             * Build the {@link Concept}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Concept}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Concept per the base specification
             */
            @Override
            public Concept build() {
                Concept concept = new Concept(this);
                if (validating) {
                    validate(concept);
                }
                return concept;
            }

            protected void validate(Concept concept) {
                super.validate(concept);
                ValidationSupport.requireNonNull(concept.code, "code");
                ValidationSupport.checkList(concept.designation, "designation", Designation.class);
                ValidationSupport.checkList(concept.property, "property", Property.class);
                ValidationSupport.checkList(concept.concept, "concept", CodeSystem.Concept.class);
                ValidationSupport.requireValueOrChildren(concept);
            }

            protected Builder from(Concept concept) {
                super.from(concept);
                code = concept.code;
                display = concept.display;
                definition = concept.definition;
                designation.addAll(concept.designation);
                property.addAll(concept.property);
                this.concept.addAll(concept.concept);
                return this;
            }
        }

        /**
         * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular 
         * purposes, etc.
         */
        public static class Designation extends BackboneElement {
            @Binding(
                bindingName = "Language",
                strength = BindingStrength.Value.PREFERRED,
                description = "IETF language tag",
                valueSet = "http://hl7.org/fhir/ValueSet/languages",
                maxValueSet = "http://hl7.org/fhir/ValueSet/all-languages"
            )
            private final Code language;
            @Binding(
                bindingName = "ConceptDesignationUse",
                strength = BindingStrength.Value.EXTENSIBLE,
                description = "Details of how a designation would be used.",
                valueSet = "http://hl7.org/fhir/ValueSet/designation-use"
            )
            private final Coding use;
            @Required
            private final String value;

            private Designation(Builder builder) {
                super(builder);
                language = builder.language;
                use = builder.use;
                value = builder.value;
            }

            /**
             * The language this designation is defined for.
             * 
             * @return
             *     An immutable object of type {@link Code} that may be null.
             */
            public Code getLanguage() {
                return language;
            }

            /**
             * A code that details how this designation would be used.
             * 
             * @return
             *     An immutable object of type {@link Coding} that may be null.
             */
            public Coding getUse() {
                return use;
            }

            /**
             * The text value for this designation.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getValue() {
                return value;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (language != null) || 
                    (use != null) || 
                    (value != null);
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
                        accept(language, "language", visitor);
                        accept(use, "use", visitor);
                        accept(value, "value", visitor);
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
                Designation other = (Designation) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(language, other.language) && 
                    Objects.equals(use, other.use) && 
                    Objects.equals(value, other.value);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        language, 
                        use, 
                        value);
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
                private Code language;
                private Coding use;
                private String value;

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
                 * The language this designation is defined for.
                 * 
                 * @param language
                 *     Human language of the designation
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder language(Code language) {
                    this.language = language;
                    return this;
                }

                /**
                 * A code that details how this designation would be used.
                 * 
                 * @param use
                 *     Details how this designation would be used
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder use(Coding use) {
                    this.use = use;
                    return this;
                }

                /**
                 * Convenience method for setting {@code value}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     The text value for this designation
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
                 * The text value for this designation.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     The text value for this designation
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(String value) {
                    this.value = value;
                    return this;
                }

                /**
                 * Build the {@link Designation}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>value</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Designation}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Designation per the base specification
                 */
                @Override
                public Designation build() {
                    Designation designation = new Designation(this);
                    if (validating) {
                        validate(designation);
                    }
                    return designation;
                }

                protected void validate(Designation designation) {
                    super.validate(designation);
                    ValidationSupport.requireNonNull(designation.value, "value");
                    ValidationSupport.checkValueSetBinding(designation.language, "language", "http://hl7.org/fhir/ValueSet/all-languages", "urn:ietf:bcp:47");
                    ValidationSupport.requireValueOrChildren(designation);
                }

                protected Builder from(Designation designation) {
                    super.from(designation);
                    language = designation.language;
                    use = designation.use;
                    value = designation.value;
                    return this;
                }
            }
        }

        /**
         * A property value for this concept.
         */
        public static class Property extends BackboneElement {
            @Required
            private final Code code;
            @Choice({ Code.class, Coding.class, String.class, Integer.class, Boolean.class, DateTime.class, Decimal.class })
            @Required
            private final Element value;

            private Property(Builder builder) {
                super(builder);
                code = builder.code;
                value = builder.value;
            }

            /**
             * A code that is a reference to CodeSystem.property.code.
             * 
             * @return
             *     An immutable object of type {@link Code} that is non-null.
             */
            public Code getCode() {
                return code;
            }

            /**
             * The value of this property.
             * 
             * @return
             *     An immutable object of type {@link Code}, {@link Coding}, {@link String}, {@link Integer}, {@link Boolean}, {@link 
             *     DateTime} or {@link Decimal} that is non-null.
             */
            public Element getValue() {
                return value;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (code != null) || 
                    (value != null);
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
                        accept(value, "value", visitor);
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
                Property other = (Property) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(value, other.value);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        code, 
                        value);
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
                private Element value;

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
                 * A code that is a reference to CodeSystem.property.code.
                 * 
                 * <p>This element is required.
                 * 
                 * @param code
                 *     Reference to CodeSystem.property.code
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(Code code) {
                    this.code = code;
                    return this;
                }

                /**
                 * Convenience method for setting {@code value} with choice type String.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Value of the property for this concept
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #value(Element)
                 */
                public Builder value(java.lang.String value) {
                    this.value = (value == null) ? null : String.of(value);
                    return this;
                }

                /**
                 * Convenience method for setting {@code value} with choice type Integer.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Value of the property for this concept
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #value(Element)
                 */
                public Builder value(java.lang.Integer value) {
                    this.value = (value == null) ? null : Integer.of(value);
                    return this;
                }

                /**
                 * Convenience method for setting {@code value} with choice type Boolean.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Value of the property for this concept
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #value(Element)
                 */
                public Builder value(java.lang.Boolean value) {
                    this.value = (value == null) ? null : Boolean.of(value);
                    return this;
                }

                /**
                 * The value of this property.
                 * 
                 * <p>This element is required.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Code}</li>
                 * <li>{@link Coding}</li>
                 * <li>{@link String}</li>
                 * <li>{@link Integer}</li>
                 * <li>{@link Boolean}</li>
                 * <li>{@link DateTime}</li>
                 * <li>{@link Decimal}</li>
                 * </ul>
                 * 
                 * @param value
                 *     Value of the property for this concept
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(Element value) {
                    this.value = value;
                    return this;
                }

                /**
                 * Build the {@link Property}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>code</li>
                 * <li>value</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Property}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Property per the base specification
                 */
                @Override
                public Property build() {
                    Property property = new Property(this);
                    if (validating) {
                        validate(property);
                    }
                    return property;
                }

                protected void validate(Property property) {
                    super.validate(property);
                    ValidationSupport.requireNonNull(property.code, "code");
                    ValidationSupport.requireChoiceElement(property.value, "value", Code.class, Coding.class, String.class, Integer.class, Boolean.class, DateTime.class, Decimal.class);
                    ValidationSupport.requireValueOrChildren(property);
                }

                protected Builder from(Property property) {
                    super.from(property);
                    code = property.code;
                    value = property.value;
                    return this;
                }
            }
        }
    }
}
