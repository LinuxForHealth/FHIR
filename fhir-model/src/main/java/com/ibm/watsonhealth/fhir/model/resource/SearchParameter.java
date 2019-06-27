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
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.model.type.SearchComparator;
import com.ibm.watsonhealth.fhir.model.type.SearchModifierCode;
import com.ibm.watsonhealth.fhir.model.type.SearchParamType;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.type.XPathUsageType;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A search parameter that defines a named search item that can be used to search/filter on a resource.
 * </p>
 */
@Constraint(
    key = "spd-0",
    severity = "warning",
    human = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Constraint(
    key = "spd-1",
    severity = "error",
    human = "If an xpath is present, there SHALL be an xpathUsage",
    expression = "xpath.empty() or xpathUsage.exists()"
)
@Constraint(
    key = "spd-2",
    severity = "error",
    human = "Search parameters can only have chain names when the search parameter type is 'reference'",
    expression = "chain.empty() or type = 'reference'"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class SearchParameter extends DomainResource {
    private final Uri url;
    private final String version;
    private final String name;
    private final Canonical derivedFrom;
    private final PublicationStatus status;
    private final Boolean experimental;
    private final DateTime date;
    private final String publisher;
    private final List<ContactDetail> contact;
    private final Markdown description;
    private final List<UsageContext> useContext;
    private final List<CodeableConcept> jurisdiction;
    private final Markdown purpose;
    private final Code code;
    private final List<ResourceType> base;
    private final SearchParamType type;
    private final String expression;
    private final String xpath;
    private final XPathUsageType xpathUsage;
    private final List<ResourceType> target;
    private final Boolean multipleOr;
    private final Boolean multipleAnd;
    private final List<SearchComparator> comparator;
    private final List<SearchModifierCode> modifier;
    private final List<String> chain;
    private final List<Component> component;

    private SearchParameter(Builder builder) {
        super(builder);
        this.url = ValidationSupport.requireNonNull(builder.url, "url");
        this.version = builder.version;
        this.name = ValidationSupport.requireNonNull(builder.name, "name");
        this.derivedFrom = builder.derivedFrom;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.experimental = builder.experimental;
        this.date = builder.date;
        this.publisher = builder.publisher;
        this.contact = builder.contact;
        this.description = ValidationSupport.requireNonNull(builder.description, "description");
        this.useContext = builder.useContext;
        this.jurisdiction = builder.jurisdiction;
        this.purpose = builder.purpose;
        this.code = ValidationSupport.requireNonNull(builder.code, "code");
        this.base = ValidationSupport.requireNonEmpty(builder.base, "base");
        this.type = ValidationSupport.requireNonNull(builder.type, "type");
        this.expression = builder.expression;
        this.xpath = builder.xpath;
        this.xpathUsage = builder.xpathUsage;
        this.target = builder.target;
        this.multipleOr = builder.multipleOr;
        this.multipleAnd = builder.multipleAnd;
        this.comparator = builder.comparator;
        this.modifier = builder.modifier;
        this.chain = builder.chain;
        this.component = builder.component;
    }

    /**
     * <p>
     * An absolute URI that is used to identify this search parameter when it is referenced in a specification, model, design 
     * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
     * at which at which an authoritative instance of this search parameter is (or will be) published. This URL can be the 
     * target of a canonical reference. It SHALL remain the same when the search parameter is stored on different servers.
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
     * The identifier that is used to identify this version of the search parameter when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the search parameter author and is not expected to be 
     * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
     * also no expectation that versions can be placed in a lexicographical sequence.
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
     * A natural language name identifying the search parameter. This name should be usable as an identifier for the module 
     * by machine processing applications such as code generation.
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
     * Where this search parameter is originally defined. If a derivedFrom is provided, then the details in the search 
     * parameter must be consistent with the definition from which it is defined. i.e. the parameter should have the same 
     * meaning, and (usually) the functionality should be a proper subset of the underlying search parameter.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * <p>
     * The status of this search parameter. Enables tracking the life-cycle of the content.
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
     * A Boolean value to indicate that this search parameter is authored for testing purposes (or 
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
     * The date (and optionally time) when the search parameter was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the search parameter changes.
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
     * The name of the organization or individual that published the search parameter.
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
     * And how it used.
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
     * may be used to assist with indexing and searching for appropriate search parameter instances.
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
     * A legal or geographic region in which the search parameter is intended to be used.
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
     * Explanation of why this search parameter is needed and why it has been designed as it has.
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
     * The code used in the URL or the parameter name in a parameters resource for this search parameter.
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
     * The base resource type(s) that this search parameter can be used against.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ResourceType}.
     */
    public List<ResourceType> getBase() {
        return base;
    }

    /**
     * <p>
     * The type of value that a search parameter may contain, and how the content is interpreted.
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
     * A FHIRPath expression that returns a set of elements for the search parameter.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getExpression() {
        return expression;
    }

    /**
     * <p>
     * An XPath expression that returns a set of elements for the search parameter.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getXpath() {
        return xpath;
    }

    /**
     * <p>
     * How the search parameter relates to the set of elements returned by evaluating the xpath query.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link XPathUsageType}.
     */
    public XPathUsageType getXpathUsage() {
        return xpathUsage;
    }

    /**
     * <p>
     * Types of resource (if a resource is referenced).
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ResourceType}.
     */
    public List<ResourceType> getTarget() {
        return target;
    }

    /**
     * <p>
     * Whether multiple values are allowed for each time the parameter exists. Values are separated by commas, and the 
     * parameter matches if any of the values match.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getMultipleOr() {
        return multipleOr;
    }

    /**
     * <p>
     * Whether multiple parameters are allowed - e.g. more than one parameter with the same name. The search matches if all 
     * the parameters match.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getMultipleAnd() {
        return multipleAnd;
    }

    /**
     * <p>
     * Comparators supported for the search parameter.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link SearchComparator}.
     */
    public List<SearchComparator> getComparator() {
        return comparator;
    }

    /**
     * <p>
     * A modifier supported for the search parameter.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link SearchModifierCode}.
     */
    public List<SearchModifierCode> getModifier() {
        return modifier;
    }

    /**
     * <p>
     * Contains the names of any search parameters which may be chained to the containing search parameter. Chained 
     * parameters may be added to search parameters of type reference and specify that resources will only be returned if 
     * they contain a reference to a resource which matches the chained parameter value. Values for this field should be 
     * drawn from SearchParameter.code for a parameter on the target resource type.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link String}.
     */
    public List<String> getChain() {
        return chain;
    }

    /**
     * <p>
     * Used to define the parts of a composite search parameter.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Component}.
     */
    public List<Component> getComponent() {
        return component;
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
                accept(derivedFrom, "derivedFrom", visitor);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(purpose, "purpose", visitor);
                accept(code, "code", visitor);
                accept(base, "base", visitor, ResourceType.class);
                accept(type, "type", visitor);
                accept(expression, "expression", visitor);
                accept(xpath, "xpath", visitor);
                accept(xpathUsage, "xpathUsage", visitor);
                accept(target, "target", visitor, ResourceType.class);
                accept(multipleOr, "multipleOr", visitor);
                accept(multipleAnd, "multipleAnd", visitor);
                accept(comparator, "comparator", visitor, SearchComparator.class);
                accept(modifier, "modifier", visitor, SearchModifierCode.class);
                accept(chain, "chain", visitor, String.class);
                accept(component, "component", visitor, Component.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(url, name, status, description, code, base, type);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.version = version;
        builder.derivedFrom = derivedFrom;
        builder.experimental = experimental;
        builder.date = date;
        builder.publisher = publisher;
        builder.contact.addAll(contact);
        builder.useContext.addAll(useContext);
        builder.jurisdiction.addAll(jurisdiction);
        builder.purpose = purpose;
        builder.expression = expression;
        builder.xpath = xpath;
        builder.xpathUsage = xpathUsage;
        builder.target.addAll(target);
        builder.multipleOr = multipleOr;
        builder.multipleAnd = multipleAnd;
        builder.comparator.addAll(comparator);
        builder.modifier.addAll(modifier);
        builder.chain.addAll(chain);
        builder.component.addAll(component);
        return builder;
    }

    public static Builder builder(Uri url, String name, PublicationStatus status, Markdown description, Code code, List<ResourceType> base, SearchParamType type) {
        return new Builder(url, name, status, description, code, base, type);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Uri url;
        private final String name;
        private final PublicationStatus status;
        private final Markdown description;
        private final Code code;
        private final List<ResourceType> base;
        private final SearchParamType type;

        // optional
        private String version;
        private Canonical derivedFrom;
        private Boolean experimental;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private String expression;
        private String xpath;
        private XPathUsageType xpathUsage;
        private List<ResourceType> target = new ArrayList<>();
        private Boolean multipleOr;
        private Boolean multipleAnd;
        private List<SearchComparator> comparator = new ArrayList<>();
        private List<SearchModifierCode> modifier = new ArrayList<>();
        private List<String> chain = new ArrayList<>();
        private List<Component> component = new ArrayList<>();

        private Builder(Uri url, String name, PublicationStatus status, Markdown description, Code code, List<ResourceType> base, SearchParamType type) {
            super();
            this.url = url;
            this.name = name;
            this.status = status;
            this.description = description;
            this.code = code;
            this.base = base;
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
         * The identifier that is used to identify this version of the search parameter when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the search parameter author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence.
         * </p>
         * 
         * @param version
         *     Business version of the search parameter
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
         * Where this search parameter is originally defined. If a derivedFrom is provided, then the details in the search 
         * parameter must be consistent with the definition from which it is defined. i.e. the parameter should have the same 
         * meaning, and (usually) the functionality should be a proper subset of the underlying search parameter.
         * </p>
         * 
         * @param derivedFrom
         *     Original definition for the search parameter
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder derivedFrom(Canonical derivedFrom) {
            this.derivedFrom = derivedFrom;
            return this;
        }

        /**
         * <p>
         * A Boolean value to indicate that this search parameter is authored for testing purposes (or 
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
         * The date (and optionally time) when the search parameter was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the search parameter changes.
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
         * The name of the organization or individual that published the search parameter.
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate search parameter instances.
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
         * may be used to assist with indexing and searching for appropriate search parameter instances.
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
         * A legal or geographic region in which the search parameter is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for search parameter (if applicable)
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
         * A legal or geographic region in which the search parameter is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for search parameter (if applicable)
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
         * Explanation of why this search parameter is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this search parameter is defined
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
         * A FHIRPath expression that returns a set of elements for the search parameter.
         * </p>
         * 
         * @param expression
         *     FHIRPath expression that extracts the values
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder expression(String expression) {
            this.expression = expression;
            return this;
        }

        /**
         * <p>
         * An XPath expression that returns a set of elements for the search parameter.
         * </p>
         * 
         * @param xpath
         *     XPath that extracts the values
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder xpath(String xpath) {
            this.xpath = xpath;
            return this;
        }

        /**
         * <p>
         * How the search parameter relates to the set of elements returned by evaluating the xpath query.
         * </p>
         * 
         * @param xpathUsage
         *     normal | phonetic | nearby | distance | other
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder xpathUsage(XPathUsageType xpathUsage) {
            this.xpathUsage = xpathUsage;
            return this;
        }

        /**
         * <p>
         * Types of resource (if a resource is referenced).
         * </p>
         * 
         * @param target
         *     Types of resource (if a resource reference)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder target(ResourceType... target) {
            for (ResourceType value : target) {
                this.target.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Types of resource (if a resource is referenced).
         * </p>
         * 
         * @param target
         *     Types of resource (if a resource reference)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder target(Collection<ResourceType> target) {
            this.target.addAll(target);
            return this;
        }

        /**
         * <p>
         * Whether multiple values are allowed for each time the parameter exists. Values are separated by commas, and the 
         * parameter matches if any of the values match.
         * </p>
         * 
         * @param multipleOr
         *     Allow multiple values per parameter (or)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder multipleOr(Boolean multipleOr) {
            this.multipleOr = multipleOr;
            return this;
        }

        /**
         * <p>
         * Whether multiple parameters are allowed - e.g. more than one parameter with the same name. The search matches if all 
         * the parameters match.
         * </p>
         * 
         * @param multipleAnd
         *     Allow multiple parameters (and)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder multipleAnd(Boolean multipleAnd) {
            this.multipleAnd = multipleAnd;
            return this;
        }

        /**
         * <p>
         * Comparators supported for the search parameter.
         * </p>
         * 
         * @param comparator
         *     eq | ne | gt | lt | ge | le | sa | eb | ap
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder comparator(SearchComparator... comparator) {
            for (SearchComparator value : comparator) {
                this.comparator.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Comparators supported for the search parameter.
         * </p>
         * 
         * @param comparator
         *     eq | ne | gt | lt | ge | le | sa | eb | ap
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder comparator(Collection<SearchComparator> comparator) {
            this.comparator.addAll(comparator);
            return this;
        }

        /**
         * <p>
         * A modifier supported for the search parameter.
         * </p>
         * 
         * @param modifier
         *     missing | exact | contains | not | text | in | not-in | below | above | type | identifier | ofType
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder modifier(SearchModifierCode... modifier) {
            for (SearchModifierCode value : modifier) {
                this.modifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A modifier supported for the search parameter.
         * </p>
         * 
         * @param modifier
         *     missing | exact | contains | not | text | in | not-in | below | above | type | identifier | ofType
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder modifier(Collection<SearchModifierCode> modifier) {
            this.modifier.addAll(modifier);
            return this;
        }

        /**
         * <p>
         * Contains the names of any search parameters which may be chained to the containing search parameter. Chained 
         * parameters may be added to search parameters of type reference and specify that resources will only be returned if 
         * they contain a reference to a resource which matches the chained parameter value. Values for this field should be 
         * drawn from SearchParameter.code for a parameter on the target resource type.
         * </p>
         * 
         * @param chain
         *     Chained names supported
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder chain(String... chain) {
            for (String value : chain) {
                this.chain.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Contains the names of any search parameters which may be chained to the containing search parameter. Chained 
         * parameters may be added to search parameters of type reference and specify that resources will only be returned if 
         * they contain a reference to a resource which matches the chained parameter value. Values for this field should be 
         * drawn from SearchParameter.code for a parameter on the target resource type.
         * </p>
         * 
         * @param chain
         *     Chained names supported
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder chain(Collection<String> chain) {
            this.chain.addAll(chain);
            return this;
        }

        /**
         * <p>
         * Used to define the parts of a composite search parameter.
         * </p>
         * 
         * @param component
         *     For Composite resources to define the parts
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder component(Component... component) {
            for (Component value : component) {
                this.component.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Used to define the parts of a composite search parameter.
         * </p>
         * 
         * @param component
         *     For Composite resources to define the parts
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder component(Collection<Component> component) {
            this.component.addAll(component);
            return this;
        }

        @Override
        public SearchParameter build() {
            return new SearchParameter(this);
        }
    }

    /**
     * <p>
     * Used to define the parts of a composite search parameter.
     * </p>
     */
    public static class Component extends BackboneElement {
        private final Canonical definition;
        private final String expression;

        private Component(Builder builder) {
            super(builder);
            this.definition = ValidationSupport.requireNonNull(builder.definition, "definition");
            this.expression = ValidationSupport.requireNonNull(builder.expression, "expression");
        }

        /**
         * <p>
         * The definition of the search parameter that describes this part.
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
         * A sub-expression that defines how to extract values for this component from the output of the main SearchParameter.
         * expression.
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
                    accept(definition, "definition", visitor);
                    accept(expression, "expression", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Canonical definition, String expression) {
            return new Builder(definition, expression);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Canonical definition;
            private final String expression;

            private Builder(Canonical definition, String expression) {
                super();
                this.definition = definition;
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
            public Component build() {
                return new Component(this);
            }

            private static Builder from(Component component) {
                Builder builder = new Builder(component.definition, component.expression);
                builder.id = component.id;
                builder.extension.addAll(component.extension);
                builder.modifierExtension.addAll(component.modifierExtension);
                return builder;
            }
        }
    }
}
