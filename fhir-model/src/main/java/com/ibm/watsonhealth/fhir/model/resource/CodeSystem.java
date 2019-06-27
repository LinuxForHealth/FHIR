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
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeSystemContentMode;
import com.ibm.watsonhealth.fhir.model.type.CodeSystemHierarchyMeaning;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.FilterOperator;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.PropertyType;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement and 
 * its key properties, and optionally define a part or all of its content.
 * </p>
 */
@Constraint(
    key = "csd-1",
    severity = "error",
    human = "Within a code system definition, all the codes SHALL be unique",
    expression = "concept.code.combine($this.descendants().concept.code).isDistinct()"
)
@Constraint(
    key = "csd-0",
    severity = "warning",
    human = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class CodeSystem extends DomainResource {
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
    private final Boolean caseSensitive;
    private final Canonical valueSet;
    private final CodeSystemHierarchyMeaning hierarchyMeaning;
    private final Boolean compositional;
    private final Boolean versionNeeded;
    private final CodeSystemContentMode content;
    private final Canonical supplements;
    private final UnsignedInt count;
    private final List<Filter> filter;
    private final List<Property> property;
    private final List<Concept> concept;

    private CodeSystem(Builder builder) {
        super(builder);
        this.url = builder.url;
        this.identifier = builder.identifier;
        this.version = builder.version;
        this.name = builder.name;
        this.title = builder.title;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.experimental = builder.experimental;
        this.date = builder.date;
        this.publisher = builder.publisher;
        this.contact = builder.contact;
        this.description = builder.description;
        this.useContext = builder.useContext;
        this.jurisdiction = builder.jurisdiction;
        this.purpose = builder.purpose;
        this.copyright = builder.copyright;
        this.caseSensitive = builder.caseSensitive;
        this.valueSet = builder.valueSet;
        this.hierarchyMeaning = builder.hierarchyMeaning;
        this.compositional = builder.compositional;
        this.versionNeeded = builder.versionNeeded;
        this.content = ValidationSupport.requireNonNull(builder.content, "content");
        this.supplements = builder.supplements;
        this.count = builder.count;
        this.filter = builder.filter;
        this.property = builder.property;
        this.concept = builder.concept;
    }

    /**
     * <p>
     * An absolute URI that is used to identify this code system when it is referenced in a specification, model, design or 
     * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this code system is (or will be) published. This URL can be the target of 
     * a canonical reference. It SHALL remain the same when the code system is stored on different servers. This is used in 
     * [Coding](datatypes.html#Coding).system.
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
     * A formal identifier that is used to identify this code system when it is represented in other formats, or referenced 
     * in a specification, model, design or an instance.
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
     * The identifier that is used to identify this version of the code system when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the code system author and is not expected to be 
     * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
     * also no expectation that versions can be placed in a lexicographical sequence. This is used in [Coding](datatypes.
     * html#Coding).version.
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
     * A natural language name identifying the code system. This name should be usable as an identifier for the module by 
     * machine processing applications such as code generation.
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
     * A short, descriptive, user-friendly title for the code system.
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
     * The date (and optionally time) when the code system resource was created or revised.
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
     * A Boolean value to indicate that this code system is authored for testing purposes (or education/evaluation/marketing) 
     * and is not intended to be used for genuine usage.
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
     * The date (and optionally time) when the code system was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the code system changes.
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
     * The name of the organization or individual that published the code system.
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
     * A free text natural language description of the code system from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate code system instances.
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
     * A legal or geographic region in which the code system is intended to be used.
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
     * Explanation of why this code system is needed and why it has been designed as it has.
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
     * A copyright statement relating to the code system and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the code system.
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
     * If code comparison is case sensitive when codes within this system are compared to each other.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getCaseSensitive() {
        return caseSensitive;
    }

    /**
     * <p>
     * Canonical reference to the value set that contains the entire code system.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getValueSet() {
        return valueSet;
    }

    /**
     * <p>
     * The meaning of the hierarchy of concepts as represented in this resource.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeSystemHierarchyMeaning}.
     */
    public CodeSystemHierarchyMeaning getHierarchyMeaning() {
        return hierarchyMeaning;
    }

    /**
     * <p>
     * The code system defines a compositional (post-coordination) grammar.
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
     * This flag is used to signify that the code system does not commit to concept permanence across versions. If true, a 
     * version must be specified when referencing this code system.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getVersionNeeded() {
        return versionNeeded;
    }

    /**
     * <p>
     * The extent of the content of the code system (the concepts and codes it defines) are represented in this resource 
     * instance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeSystemContentMode}.
     */
    public CodeSystemContentMode getContent() {
        return content;
    }

    /**
     * <p>
     * The canonical URL of the code system that this code system supplement is adding designations and properties to.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getSupplements() {
        return supplements;
    }

    /**
     * <p>
     * The total number of concepts defined by the code system. Where the code system has a compositional grammar, the basis 
     * of this count is defined by the system steward.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt}.
     */
    public UnsignedInt getCount() {
        return count;
    }

    /**
     * <p>
     * A filter that can be used in a value set compose statement when selecting concepts using a filter.
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
     * A property defines an additional slot through which additional information can be provided about a concept.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Property}.
     */
    public List<Property> getProperty() {
        return property;
    }

    /**
     * <p>
     * Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be 
     * consulted to determine what the meanings of the hierarchical relationships are.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Concept}.
     */
    public List<Concept> getConcept() {
        return concept;
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, content);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.url = url;
        builder.identifier.addAll(identifier);
        builder.version = version;
        builder.name = name;
        builder.title = title;
        builder.experimental = experimental;
        builder.date = date;
        builder.publisher = publisher;
        builder.contact.addAll(contact);
        builder.description = description;
        builder.useContext.addAll(useContext);
        builder.jurisdiction.addAll(jurisdiction);
        builder.purpose = purpose;
        builder.copyright = copyright;
        builder.caseSensitive = caseSensitive;
        builder.valueSet = valueSet;
        builder.hierarchyMeaning = hierarchyMeaning;
        builder.compositional = compositional;
        builder.versionNeeded = versionNeeded;
        builder.supplements = supplements;
        builder.count = count;
        builder.filter.addAll(filter);
        builder.property.addAll(property);
        builder.concept.addAll(concept);
        return builder;
    }

    public static Builder builder(PublicationStatus status, CodeSystemContentMode content) {
        return new Builder(status, content);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final PublicationStatus status;
        private final CodeSystemContentMode content;

        // optional
        private Uri url;
        private List<Identifier> identifier = new ArrayList<>();
        private String version;
        private String name;
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
        private Boolean caseSensitive;
        private Canonical valueSet;
        private CodeSystemHierarchyMeaning hierarchyMeaning;
        private Boolean compositional;
        private Boolean versionNeeded;
        private Canonical supplements;
        private UnsignedInt count;
        private List<Filter> filter = new ArrayList<>();
        private List<Property> property = new ArrayList<>();
        private List<Concept> concept = new ArrayList<>();

        private Builder(PublicationStatus status, CodeSystemContentMode content) {
            super();
            this.status = status;
            this.content = content;
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
         * An absolute URI that is used to identify this code system when it is referenced in a specification, model, design or 
         * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
         * which at which an authoritative instance of this code system is (or will be) published. This URL can be the target of 
         * a canonical reference. It SHALL remain the same when the code system is stored on different servers. This is used in 
         * [Coding](datatypes.html#Coding).system.
         * </p>
         * 
         * @param url
         *     Canonical identifier for this code system, represented as a URI (globally unique) (Coding.system)
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
         * A formal identifier that is used to identify this code system when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the code system (business identifier)
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
         * A formal identifier that is used to identify this code system when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the code system (business identifier)
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
         * The identifier that is used to identify this version of the code system when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the code system author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence. This is used in [Coding](datatypes.
         * html#Coding).version.
         * </p>
         * 
         * @param version
         *     Business version of the code system (Coding.version)
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
         * A natural language name identifying the code system. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
         * </p>
         * 
         * @param name
         *     Name for this code system (computer friendly)
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
         * A short, descriptive, user-friendly title for the code system.
         * </p>
         * 
         * @param title
         *     Name for this code system (human friendly)
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
         * A Boolean value to indicate that this code system is authored for testing purposes (or education/evaluation/marketing) 
         * and is not intended to be used for genuine usage.
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
         * The date (and optionally time) when the code system was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the code system changes.
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
         * The name of the organization or individual that published the code system.
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
         * A free text natural language description of the code system from a consumer's perspective.
         * </p>
         * 
         * @param description
         *     Natural language description of the code system
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
         * may be used to assist with indexing and searching for appropriate code system instances.
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
         * may be used to assist with indexing and searching for appropriate code system instances.
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
         * A legal or geographic region in which the code system is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for code system (if applicable)
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
         * A legal or geographic region in which the code system is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for code system (if applicable)
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
         * Explanation of why this code system is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this code system is defined
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
         * A copyright statement relating to the code system and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the code system.
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
         * If code comparison is case sensitive when codes within this system are compared to each other.
         * </p>
         * 
         * @param caseSensitive
         *     If code comparison is case sensitive
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder caseSensitive(Boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
            return this;
        }

        /**
         * <p>
         * Canonical reference to the value set that contains the entire code system.
         * </p>
         * 
         * @param valueSet
         *     Canonical reference to the value set with entire code system
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder valueSet(Canonical valueSet) {
            this.valueSet = valueSet;
            return this;
        }

        /**
         * <p>
         * The meaning of the hierarchy of concepts as represented in this resource.
         * </p>
         * 
         * @param hierarchyMeaning
         *     grouped-by | is-a | part-of | classified-with
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder hierarchyMeaning(CodeSystemHierarchyMeaning hierarchyMeaning) {
            this.hierarchyMeaning = hierarchyMeaning;
            return this;
        }

        /**
         * <p>
         * The code system defines a compositional (post-coordination) grammar.
         * </p>
         * 
         * @param compositional
         *     If code system defines a compositional grammar
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
         * This flag is used to signify that the code system does not commit to concept permanence across versions. If true, a 
         * version must be specified when referencing this code system.
         * </p>
         * 
         * @param versionNeeded
         *     If definitions are not stable
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder versionNeeded(Boolean versionNeeded) {
            this.versionNeeded = versionNeeded;
            return this;
        }

        /**
         * <p>
         * The canonical URL of the code system that this code system supplement is adding designations and properties to.
         * </p>
         * 
         * @param supplements
         *     Canonical URL of Code System this adds designations and properties to
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supplements(Canonical supplements) {
            this.supplements = supplements;
            return this;
        }

        /**
         * <p>
         * The total number of concepts defined by the code system. Where the code system has a compositional grammar, the basis 
         * of this count is defined by the system steward.
         * </p>
         * 
         * @param count
         *     Total concepts in the code system
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder count(UnsignedInt count) {
            this.count = count;
            return this;
        }

        /**
         * <p>
         * A filter that can be used in a value set compose statement when selecting concepts using a filter.
         * </p>
         * 
         * @param filter
         *     Filter that can be used in a value set
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
         * A filter that can be used in a value set compose statement when selecting concepts using a filter.
         * </p>
         * 
         * @param filter
         *     Filter that can be used in a value set
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
         * A property defines an additional slot through which additional information can be provided about a concept.
         * </p>
         * 
         * @param property
         *     Additional information supplied about each concept
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder property(Property... property) {
            for (Property value : property) {
                this.property.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A property defines an additional slot through which additional information can be provided about a concept.
         * </p>
         * 
         * @param property
         *     Additional information supplied about each concept
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder property(Collection<Property> property) {
            this.property.addAll(property);
            return this;
        }

        /**
         * <p>
         * Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be 
         * consulted to determine what the meanings of the hierarchical relationships are.
         * </p>
         * 
         * @param concept
         *     Concepts in the code system
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder concept(Concept... concept) {
            for (Concept value : concept) {
                this.concept.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be 
         * consulted to determine what the meanings of the hierarchical relationships are.
         * </p>
         * 
         * @param concept
         *     Concepts in the code system
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder concept(Collection<Concept> concept) {
            this.concept.addAll(concept);
            return this;
        }

        @Override
        public CodeSystem build() {
            return new CodeSystem(this);
        }
    }

    /**
     * <p>
     * A filter that can be used in a value set compose statement when selecting concepts using a filter.
     * </p>
     */
    public static class Filter extends BackboneElement {
        private final Code code;
        private final String description;
        private final List<FilterOperator> operator;
        private final String value;

        private Filter(Builder builder) {
            super(builder);
            this.code = ValidationSupport.requireNonNull(builder.code, "code");
            this.description = builder.description;
            this.operator = ValidationSupport.requireNonEmpty(builder.operator, "operator");
            this.value = ValidationSupport.requireNonNull(builder.value, "value");
        }

        /**
         * <p>
         * The code that identifies this filter when it is used as a filter in [ValueSet](valueset.html#).compose.include.filter.
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
         * A description of how or why the filter is used.
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
         * A list of operators that can be used with the filter.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link FilterOperator}.
         */
        public List<FilterOperator> getOperator() {
            return operator;
        }

        /**
         * <p>
         * A description of what the value for the filter should be.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getValue() {
            return value;
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
                    accept(description, "description", visitor);
                    accept(operator, "operator", visitor, FilterOperator.class);
                    accept(value, "value", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Code code, List<FilterOperator> operator, String value) {
            return new Builder(code, operator, value);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Code code;
            private final List<FilterOperator> operator;
            private final String value;

            // optional
            private String description;

            private Builder(Code code, List<FilterOperator> operator, String value) {
                super();
                this.code = code;
                this.operator = operator;
                this.value = value;
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
             * A description of how or why the filter is used.
             * </p>
             * 
             * @param description
             *     How or why the filter is used
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            @Override
            public Filter build() {
                return new Filter(this);
            }

            private static Builder from(Filter filter) {
                Builder builder = new Builder(filter.code, filter.operator, filter.value);
                builder.id = filter.id;
                builder.extension.addAll(filter.extension);
                builder.modifierExtension.addAll(filter.modifierExtension);
                builder.description = filter.description;
                return builder;
            }
        }
    }

    /**
     * <p>
     * A property defines an additional slot through which additional information can be provided about a concept.
     * </p>
     */
    public static class Property extends BackboneElement {
        private final Code code;
        private final Uri uri;
        private final String description;
        private final PropertyType type;

        private Property(Builder builder) {
            super(builder);
            this.code = ValidationSupport.requireNonNull(builder.code, "code");
            this.uri = builder.uri;
            this.description = builder.description;
            this.type = ValidationSupport.requireNonNull(builder.type, "type");
        }

        /**
         * <p>
         * A code that is used to identify the property. The code is used internally (in CodeSystem.concept.property.code) and 
         * also externally, such as in property filters.
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
         * Reference to the formal meaning of the property. One possible source of meaning is the [Concept Properties](codesystem-
         * concept-properties.html) code system.
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
         * A description of the property- why it is defined, and how its value might be used.
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
         * The type of the property value. Properties of type "code" contain a code defined by the code system (e.g. a reference 
         * to another defined concept).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PropertyType}.
         */
        public PropertyType getType() {
            return type;
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
                    accept(uri, "uri", visitor);
                    accept(description, "description", visitor);
                    accept(type, "type", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Code code, PropertyType type) {
            return new Builder(code, type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Code code;
            private final PropertyType type;

            // optional
            private Uri uri;
            private String description;

            private Builder(Code code, PropertyType type) {
                super();
                this.code = code;
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
             * Reference to the formal meaning of the property. One possible source of meaning is the [Concept Properties](codesystem-
             * concept-properties.html) code system.
             * </p>
             * 
             * @param uri
             *     Formal identifier for the property
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
             * A description of the property- why it is defined, and how its value might be used.
             * </p>
             * 
             * @param description
             *     Why the property is defined, and/or what it conveys
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            @Override
            public Property build() {
                return new Property(this);
            }

            private static Builder from(Property property) {
                Builder builder = new Builder(property.code, property.type);
                builder.id = property.id;
                builder.extension.addAll(property.extension);
                builder.modifierExtension.addAll(property.modifierExtension);
                builder.uri = property.uri;
                builder.description = property.description;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be 
     * consulted to determine what the meanings of the hierarchical relationships are.
     * </p>
     */
    public static class Concept extends BackboneElement {
        private final Code code;
        private final String display;
        private final String definition;
        private final List<Designation> designation;
        private final List<Property> property;
        private final List<CodeSystem.Concept> concept;

        private Concept(Builder builder) {
            super(builder);
            this.code = ValidationSupport.requireNonNull(builder.code, "code");
            this.display = builder.display;
            this.definition = builder.definition;
            this.designation = builder.designation;
            this.property = builder.property;
            this.concept = builder.concept;
        }

        /**
         * <p>
         * A code - a text symbol - that uniquely identifies the concept within the code system.
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
         * A human readable string that is the recommended default way to present this concept to a user.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDisplay() {
            return display;
        }

        /**
         * <p>
         * The formal definition of the concept. The code system resource does not make formal definitions required, because of 
         * the prevalence of legacy systems. However, they are highly recommended, as without them there is no formal meaning 
         * associated with the concept.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDefinition() {
            return definition;
        }

        /**
         * <p>
         * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular 
         * purposes, etc.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Designation}.
         */
        public List<Designation> getDesignation() {
            return designation;
        }

        /**
         * <p>
         * A property value for this concept.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Property}.
         */
        public List<Property> getProperty() {
            return property;
        }

        /**
         * <p>
         * Defines children of a concept to produce a hierarchy of concepts. The nature of the relationships is variable (is-
         * a/contains/categorizes) - see hierarchyMeaning.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Concept}.
         */
        public List<CodeSystem.Concept> getConcept() {
            return concept;
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
                    accept(display, "display", visitor);
                    accept(definition, "definition", visitor);
                    accept(designation, "designation", visitor, Designation.class);
                    accept(property, "property", visitor, Property.class);
                    accept(concept, "concept", visitor, CodeSystem.Concept.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Code code) {
            return new Builder(code);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Code code;

            // optional
            private String display;
            private String definition;
            private List<Designation> designation = new ArrayList<>();
            private List<Property> property = new ArrayList<>();
            private List<CodeSystem.Concept> concept = new ArrayList<>();

            private Builder(Code code) {
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
             * A human readable string that is the recommended default way to present this concept to a user.
             * </p>
             * 
             * @param display
             *     Text to display to the user
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder display(String display) {
                this.display = display;
                return this;
            }

            /**
             * <p>
             * The formal definition of the concept. The code system resource does not make formal definitions required, because of 
             * the prevalence of legacy systems. However, they are highly recommended, as without them there is no formal meaning 
             * associated with the concept.
             * </p>
             * 
             * @param definition
             *     Formal definition
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder definition(String definition) {
                this.definition = definition;
                return this;
            }

            /**
             * <p>
             * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular 
             * purposes, etc.
             * </p>
             * 
             * @param designation
             *     Additional representations for the concept
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder designation(Designation... designation) {
                for (Designation value : designation) {
                    this.designation.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular 
             * purposes, etc.
             * </p>
             * 
             * @param designation
             *     Additional representations for the concept
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder designation(Collection<Designation> designation) {
                this.designation.addAll(designation);
                return this;
            }

            /**
             * <p>
             * A property value for this concept.
             * </p>
             * 
             * @param property
             *     Property value for the concept
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder property(Property... property) {
                for (Property value : property) {
                    this.property.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A property value for this concept.
             * </p>
             * 
             * @param property
             *     Property value for the concept
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder property(Collection<Property> property) {
                this.property.addAll(property);
                return this;
            }

            /**
             * <p>
             * Defines children of a concept to produce a hierarchy of concepts. The nature of the relationships is variable (is-
             * a/contains/categorizes) - see hierarchyMeaning.
             * </p>
             * 
             * @param concept
             *     Child Concepts (is-a/contains/categorizes)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder concept(CodeSystem.Concept... concept) {
                for (CodeSystem.Concept value : concept) {
                    this.concept.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Defines children of a concept to produce a hierarchy of concepts. The nature of the relationships is variable (is-
             * a/contains/categorizes) - see hierarchyMeaning.
             * </p>
             * 
             * @param concept
             *     Child Concepts (is-a/contains/categorizes)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder concept(Collection<CodeSystem.Concept> concept) {
                this.concept.addAll(concept);
                return this;
            }

            @Override
            public Concept build() {
                return new Concept(this);
            }

            private static Builder from(Concept concept) {
                Builder builder = new Builder(concept.code);
                builder.id = concept.id;
                builder.extension.addAll(concept.extension);
                builder.modifierExtension.addAll(concept.modifierExtension);
                builder.display = concept.display;
                builder.definition = concept.definition;
                builder.designation.addAll(concept.designation);
                builder.property.addAll(concept.property);
                builder.concept.addAll(concept.concept);
                return builder;
            }
        }

        /**
         * <p>
         * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular 
         * purposes, etc.
         * </p>
         */
        public static class Designation extends BackboneElement {
            private final Code language;
            private final Coding use;
            private final String value;

            private Designation(Builder builder) {
                super(builder);
                this.language = builder.language;
                this.use = builder.use;
                this.value = ValidationSupport.requireNonNull(builder.value, "value");
            }

            /**
             * <p>
             * The language this designation is defined for.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Code}.
             */
            public Code getLanguage() {
                return language;
            }

            /**
             * <p>
             * A code that details how this designation would be used.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Coding}.
             */
            public Coding getUse() {
                return use;
            }

            /**
             * <p>
             * The text value for this designation.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getValue() {
                return value;
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
                        accept(language, "language", visitor);
                        accept(use, "use", visitor);
                        accept(value, "value", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(String value) {
                return new Builder(value);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final String value;

                // optional
                private Code language;
                private Coding use;

                private Builder(String value) {
                    super();
                    this.value = value;
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
                 * The language this designation is defined for.
                 * </p>
                 * 
                 * @param language
                 *     Human language of the designation
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder language(Code language) {
                    this.language = language;
                    return this;
                }

                /**
                 * <p>
                 * A code that details how this designation would be used.
                 * </p>
                 * 
                 * @param use
                 *     Details how this designation would be used
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder use(Coding use) {
                    this.use = use;
                    return this;
                }

                @Override
                public Designation build() {
                    return new Designation(this);
                }

                private static Builder from(Designation designation) {
                    Builder builder = new Builder(designation.value);
                    builder.id = designation.id;
                    builder.extension.addAll(designation.extension);
                    builder.modifierExtension.addAll(designation.modifierExtension);
                    builder.language = designation.language;
                    builder.use = designation.use;
                    return builder;
                }
            }
        }

        /**
         * <p>
         * A property value for this concept.
         * </p>
         */
        public static class Property extends BackboneElement {
            private final Code code;
            private final Element value;

            private Property(Builder builder) {
                super(builder);
                this.code = ValidationSupport.requireNonNull(builder.code, "code");
                this.value = ValidationSupport.requireChoiceElement(builder.value, "value", Code.class, Coding.class, String.class, Integer.class, Boolean.class, DateTime.class, Decimal.class);
            }

            /**
             * <p>
             * A code that is a reference to CodeSystem.property.code.
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
             * The value of this property.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getValue() {
                return value;
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
                        accept(value, "value", visitor, true);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(Code code, Element value) {
                return new Builder(code, value);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Code code;
                private final Element value;

                private Builder(Code code, Element value) {
                    super();
                    this.code = code;
                    this.value = value;
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
                public Property build() {
                    return new Property(this);
                }

                private static Builder from(Property property) {
                    Builder builder = new Builder(property.code, property.value);
                    builder.id = property.id;
                    builder.extension.addAll(property.extension);
                    builder.modifierExtension.addAll(property.modifierExtension);
                    return builder;
                }
            }
        }
    }
}
