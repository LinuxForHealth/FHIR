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
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.Date;
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
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A ValueSet resource instance specifies a set of codes drawn from one or more code systems, intended for use in a 
 * particular context. Value sets link between [CodeSystem](codesystem.html) definitions and their use in [coded elements]
 * (terminologies.html).
 * </p>
 */
@Constraint(
    id = "vsd-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Constraint(
    id = "vsd-1",
    level = "Rule",
    location = "ValueSet.compose.include",
    description = "A value set include/exclude SHALL have a value set or a system",
    expression = "valueSet.exists() or system.exists()"
)
@Constraint(
    id = "vsd-2",
    level = "Rule",
    location = "ValueSet.compose.include",
    description = "A value set with concepts or filters SHALL include a system",
    expression = "(concept.exists() or filter.exists()) implies system.exists()"
)
@Constraint(
    id = "vsd-3",
    level = "Rule",
    location = "ValueSet.compose.include",
    description = "Cannot have both concept and filter",
    expression = "concept.empty() or filter.empty()"
)
@Constraint(
    id = "vsd-6",
    level = "Rule",
    location = "ValueSet.expansion.contains",
    description = "SHALL have a code or a display",
    expression = "code.exists() or display.exists()"
)
@Constraint(
    id = "vsd-9",
    level = "Rule",
    location = "ValueSet.expansion.contains",
    description = "Must have a code if not abstract",
    expression = "code.exists() or abstract = true"
)
@Constraint(
    id = "vsd-10",
    level = "Rule",
    location = "ValueSet.expansion.contains",
    description = "Must have a system if a code is present",
    expression = "code.empty() or system.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ValueSet extends DomainResource {
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
    private final Boolean immutable;
    private final Markdown purpose;
    private final Markdown copyright;
    private final Compose compose;
    private final Expansion expansion;

    private ValueSet(Builder builder) {
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
        this.immutable = builder.immutable;
        this.purpose = builder.purpose;
        this.copyright = builder.copyright;
        this.compose = builder.compose;
        this.expansion = builder.expansion;
    }

    /**
     * <p>
     * An absolute URI that is used to identify this value set when it is referenced in a specification, model, design or an 
     * instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this value set is (or will be) published. This URL can be the target of a 
     * canonical reference. It SHALL remain the same when the value set is stored on different servers.
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
     * A formal identifier that is used to identify this value set when it is represented in other formats, or referenced in 
     * a specification, model, design or an instance.
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
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, 
     * design or instance. This is an arbitrary value managed by the value set author and is not expected to be globally 
     * unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no 
     * expectation that versions can be placed in a lexicographical sequence.
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
     * A natural language name identifying the value set. This name should be usable as an identifier for the module by 
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
     * A short, descriptive, user-friendly title for the value set.
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
     * The status of this value set. Enables tracking the life-cycle of the content. The status of the value set applies to 
     * the value set definition (ValueSet.compose) and the associated ValueSet metadata. Expansions do not have a state.
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
     * A Boolean value to indicate that this value set is authored for testing purposes (or education/evaluation/marketing) 
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
     * The date (and optionally time) when the value set was created or revised (e.g. the 'content logical definition').
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
     * The name of the organization or individual that published the value set.
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
     * A free text natural language description of the value set from a consumer's perspective. The textual description 
     * specifies the span of meanings for concepts to be included within the Value Set Expansion, and also may specify the 
     * intended use and limitations of the Value Set.
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
     * may be used to assist with indexing and searching for appropriate value set instances.
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
     * A legal or geographic region in which the value set is intended to be used.
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
     * If this is set to 'true', then no new versions of the content logical definition can be created. Note: Other metadata 
     * might still change.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getImmutable() {
        return immutable;
    }

    /**
     * <p>
     * Explanation of why this value set is needed and why it has been designed as it has.
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
     * A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the value set.
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
     * A set of criteria that define the contents of the value set by including or excluding codes selected from the 
     * specified code system(s) that the value set draws from. This is also known as the Content Logical Definition (CLD).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Compose}.
     */
    public Compose getCompose() {
        return compose;
    }

    /**
     * <p>
     * A value set can also be "expanded", where the value set is turned into a simple collection of enumerated codes. This 
     * element holds the expansion, if it has been performed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Expansion}.
     */
    public Expansion getExpansion() {
        return expansion;
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
                accept(immutable, "immutable", visitor);
                accept(purpose, "purpose", visitor);
                accept(copyright, "copyright", visitor);
                accept(compose, "compose", visitor);
                accept(expansion, "expansion", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status);
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
        builder.immutable = immutable;
        builder.purpose = purpose;
        builder.copyright = copyright;
        builder.compose = compose;
        builder.expansion = expansion;
        return builder;
    }

    public static Builder builder(PublicationStatus status) {
        return new Builder(status);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final PublicationStatus status;

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
        private Boolean immutable;
        private Markdown purpose;
        private Markdown copyright;
        private Compose compose;
        private Expansion expansion;

        private Builder(PublicationStatus status) {
            super();
            this.status = status;
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
         * An absolute URI that is used to identify this value set when it is referenced in a specification, model, design or an 
         * instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
         * which at which an authoritative instance of this value set is (or will be) published. This URL can be the target of a 
         * canonical reference. It SHALL remain the same when the value set is stored on different servers.
         * </p>
         * 
         * @param url
         *     Canonical identifier for this value set, represented as a URI (globally unique)
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
         * A formal identifier that is used to identify this value set when it is represented in other formats, or referenced in 
         * a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the value set (business identifier)
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
         * A formal identifier that is used to identify this value set when it is represented in other formats, or referenced in 
         * a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the value set (business identifier)
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
         * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, 
         * design or instance. This is an arbitrary value managed by the value set author and is not expected to be globally 
         * unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no 
         * expectation that versions can be placed in a lexicographical sequence.
         * </p>
         * 
         * @param version
         *     Business version of the value set
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
         * A natural language name identifying the value set. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
         * </p>
         * 
         * @param name
         *     Name for this value set (computer friendly)
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
         * A short, descriptive, user-friendly title for the value set.
         * </p>
         * 
         * @param title
         *     Name for this value set (human friendly)
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
         * A Boolean value to indicate that this value set is authored for testing purposes (or education/evaluation/marketing) 
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
         * The date (and optionally time) when the value set was created or revised (e.g. the 'content logical definition').
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
         * The name of the organization or individual that published the value set.
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
         * A free text natural language description of the value set from a consumer's perspective. The textual description 
         * specifies the span of meanings for concepts to be included within the Value Set Expansion, and also may specify the 
         * intended use and limitations of the Value Set.
         * </p>
         * 
         * @param description
         *     Natural language description of the value set
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
         * may be used to assist with indexing and searching for appropriate value set instances.
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
         * may be used to assist with indexing and searching for appropriate value set instances.
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
         * A legal or geographic region in which the value set is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for value set (if applicable)
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
         * A legal or geographic region in which the value set is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for value set (if applicable)
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
         * If this is set to 'true', then no new versions of the content logical definition can be created. Note: Other metadata 
         * might still change.
         * </p>
         * 
         * @param immutable
         *     Indicates whether or not any change to the content logical definition may occur
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder immutable(Boolean immutable) {
            this.immutable = immutable;
            return this;
        }

        /**
         * <p>
         * Explanation of why this value set is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this value set is defined
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
         * A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the value set.
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
         * A set of criteria that define the contents of the value set by including or excluding codes selected from the 
         * specified code system(s) that the value set draws from. This is also known as the Content Logical Definition (CLD).
         * </p>
         * 
         * @param compose
         *     Content logical definition of the value set (CLD)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder compose(Compose compose) {
            this.compose = compose;
            return this;
        }

        /**
         * <p>
         * A value set can also be "expanded", where the value set is turned into a simple collection of enumerated codes. This 
         * element holds the expansion, if it has been performed.
         * </p>
         * 
         * @param expansion
         *     Used when the value set is "expanded"
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder expansion(Expansion expansion) {
            this.expansion = expansion;
            return this;
        }

        @Override
        public ValueSet build() {
            return new ValueSet(this);
        }
    }

    /**
     * <p>
     * A set of criteria that define the contents of the value set by including or excluding codes selected from the 
     * specified code system(s) that the value set draws from. This is also known as the Content Logical Definition (CLD).
     * </p>
     */
    public static class Compose extends BackboneElement {
        private final Date lockedDate;
        private final Boolean inactive;
        private final List<Include> include;
        private final List<ValueSet.Compose.Include> exclude;

        private Compose(Builder builder) {
            super(builder);
            this.lockedDate = builder.lockedDate;
            this.inactive = builder.inactive;
            this.include = ValidationSupport.requireNonEmpty(builder.include, "include");
            this.exclude = builder.exclude;
        }

        /**
         * <p>
         * The Locked Date is the effective date that is used to determine the version of all referenced Code Systems and Value 
         * Set Definitions included in the compose that are not already tied to a specific version.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Date}.
         */
        public Date getLockedDate() {
            return lockedDate;
        }

        /**
         * <p>
         * Whether inactive codes - codes that are not approved for current use - are in the value set. If inactive = true, 
         * inactive codes are to be included in the expansion, if inactive = false, the inactive codes will not be included in 
         * the expansion. If absent, the behavior is determined by the implementation, or by the applicable $expand parameters 
         * (but generally, inactive codes would be expected to be included).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getInactive() {
            return inactive;
        }

        /**
         * <p>
         * Include one or more codes from a code system or other value set(s).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Include}.
         */
        public List<Include> getInclude() {
            return include;
        }

        /**
         * <p>
         * Exclude one or more codes from the value set based on code system filters and/or other value sets.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Include}.
         */
        public List<ValueSet.Compose.Include> getExclude() {
            return exclude;
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
                    accept(lockedDate, "lockedDate", visitor);
                    accept(inactive, "inactive", visitor);
                    accept(include, "include", visitor, Include.class);
                    accept(exclude, "exclude", visitor, ValueSet.Compose.Include.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(List<Include> include) {
            return new Builder(include);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final List<Include> include;

            // optional
            private Date lockedDate;
            private Boolean inactive;
            private List<ValueSet.Compose.Include> exclude = new ArrayList<>();

            private Builder(List<Include> include) {
                super();
                this.include = include;
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
             * The Locked Date is the effective date that is used to determine the version of all referenced Code Systems and Value 
             * Set Definitions included in the compose that are not already tied to a specific version.
             * </p>
             * 
             * @param lockedDate
             *     Fixed date for references with no specified version (transitive)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder lockedDate(Date lockedDate) {
                this.lockedDate = lockedDate;
                return this;
            }

            /**
             * <p>
             * Whether inactive codes - codes that are not approved for current use - are in the value set. If inactive = true, 
             * inactive codes are to be included in the expansion, if inactive = false, the inactive codes will not be included in 
             * the expansion. If absent, the behavior is determined by the implementation, or by the applicable $expand parameters 
             * (but generally, inactive codes would be expected to be included).
             * </p>
             * 
             * @param inactive
             *     Whether inactive codes are in the value set
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder inactive(Boolean inactive) {
                this.inactive = inactive;
                return this;
            }

            /**
             * <p>
             * Exclude one or more codes from the value set based on code system filters and/or other value sets.
             * </p>
             * 
             * @param exclude
             *     Explicitly exclude codes from a code system or other value sets
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder exclude(ValueSet.Compose.Include... exclude) {
                for (ValueSet.Compose.Include value : exclude) {
                    this.exclude.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Exclude one or more codes from the value set based on code system filters and/or other value sets.
             * </p>
             * 
             * @param exclude
             *     Explicitly exclude codes from a code system or other value sets
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder exclude(Collection<ValueSet.Compose.Include> exclude) {
                this.exclude.addAll(exclude);
                return this;
            }

            @Override
            public Compose build() {
                return new Compose(this);
            }

            private static Builder from(Compose compose) {
                Builder builder = new Builder(compose.include);
                builder.id = compose.id;
                builder.extension.addAll(compose.extension);
                builder.modifierExtension.addAll(compose.modifierExtension);
                builder.lockedDate = compose.lockedDate;
                builder.inactive = compose.inactive;
                builder.exclude.addAll(compose.exclude);
                return builder;
            }
        }

        /**
         * <p>
         * Include one or more codes from a code system or other value set(s).
         * </p>
         */
        public static class Include extends BackboneElement {
            private final Uri system;
            private final String version;
            private final List<Concept> concept;
            private final List<Filter> filter;
            private final List<Canonical> valueSet;

            private Include(Builder builder) {
                super(builder);
                this.system = builder.system;
                this.version = builder.version;
                this.concept = builder.concept;
                this.filter = builder.filter;
                this.valueSet = builder.valueSet;
            }

            /**
             * <p>
             * An absolute URI which is the code system from which the selected codes come from.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Uri}.
             */
            public Uri getSystem() {
                return system;
            }

            /**
             * <p>
             * The version of the code system that the codes are selected from, or the special version '*' for all versions.
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
             * Specifies a concept to be included or excluded.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Concept}.
             */
            public List<Concept> getConcept() {
                return concept;
            }

            /**
             * <p>
             * Select concepts by specify a matching criterion based on the properties (including relationships) defined by the 
             * system, or on filters defined by the system. If multiple filters are specified, they SHALL all be true.
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
             * Selects the concepts found in this value set (based on its value set definition). This is an absolute URI that is a 
             * reference to ValueSet.url. If multiple value sets are specified this includes the union of the contents of all of the 
             * referenced value sets.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Canonical}.
             */
            public List<Canonical> getValueSet() {
                return valueSet;
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
                        accept(system, "system", visitor);
                        accept(version, "version", visitor);
                        accept(concept, "concept", visitor, Concept.class);
                        accept(filter, "filter", visitor, Filter.class);
                        accept(valueSet, "valueSet", visitor, Canonical.class);
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
                private Uri system;
                private String version;
                private List<Concept> concept = new ArrayList<>();
                private List<Filter> filter = new ArrayList<>();
                private List<Canonical> valueSet = new ArrayList<>();

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
                 * An absolute URI which is the code system from which the selected codes come from.
                 * </p>
                 * 
                 * @param system
                 *     The system the codes come from
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder system(Uri system) {
                    this.system = system;
                    return this;
                }

                /**
                 * <p>
                 * The version of the code system that the codes are selected from, or the special version '*' for all versions.
                 * </p>
                 * 
                 * @param version
                 *     Specific version of the code system referred to
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
                 * Specifies a concept to be included or excluded.
                 * </p>
                 * 
                 * @param concept
                 *     A concept defined in the system
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
                 * Specifies a concept to be included or excluded.
                 * </p>
                 * 
                 * @param concept
                 *     A concept defined in the system
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder concept(Collection<Concept> concept) {
                    this.concept.addAll(concept);
                    return this;
                }

                /**
                 * <p>
                 * Select concepts by specify a matching criterion based on the properties (including relationships) defined by the 
                 * system, or on filters defined by the system. If multiple filters are specified, they SHALL all be true.
                 * </p>
                 * 
                 * @param filter
                 *     Select codes/concepts by their properties (including relationships)
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
                 * Select concepts by specify a matching criterion based on the properties (including relationships) defined by the 
                 * system, or on filters defined by the system. If multiple filters are specified, they SHALL all be true.
                 * </p>
                 * 
                 * @param filter
                 *     Select codes/concepts by their properties (including relationships)
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
                 * Selects the concepts found in this value set (based on its value set definition). This is an absolute URI that is a 
                 * reference to ValueSet.url. If multiple value sets are specified this includes the union of the contents of all of the 
                 * referenced value sets.
                 * </p>
                 * 
                 * @param valueSet
                 *     Select the contents included in this value set
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder valueSet(Canonical... valueSet) {
                    for (Canonical value : valueSet) {
                        this.valueSet.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Selects the concepts found in this value set (based on its value set definition). This is an absolute URI that is a 
                 * reference to ValueSet.url. If multiple value sets are specified this includes the union of the contents of all of the 
                 * referenced value sets.
                 * </p>
                 * 
                 * @param valueSet
                 *     Select the contents included in this value set
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder valueSet(Collection<Canonical> valueSet) {
                    this.valueSet.addAll(valueSet);
                    return this;
                }

                @Override
                public Include build() {
                    return new Include(this);
                }

                private static Builder from(Include include) {
                    Builder builder = new Builder();
                    builder.id = include.id;
                    builder.extension.addAll(include.extension);
                    builder.modifierExtension.addAll(include.modifierExtension);
                    builder.system = include.system;
                    builder.version = include.version;
                    builder.concept.addAll(include.concept);
                    builder.filter.addAll(include.filter);
                    builder.valueSet.addAll(include.valueSet);
                    return builder;
                }
            }

            /**
             * <p>
             * Specifies a concept to be included or excluded.
             * </p>
             */
            public static class Concept extends BackboneElement {
                private final Code code;
                private final String display;
                private final List<Designation> designation;

                private Concept(Builder builder) {
                    super(builder);
                    this.code = ValidationSupport.requireNonNull(builder.code, "code");
                    this.display = builder.display;
                    this.designation = builder.designation;
                }

                /**
                 * <p>
                 * Specifies a code for the concept to be included or excluded.
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
                 * The text to display to the user for this concept in the context of this valueset. If no display is provided, then 
                 * applications using the value set use the display specified for the code by the system.
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
                 * Additional representations for this concept when used in this value set - other languages, aliases, specialized 
                 * purposes, used for particular purposes, etc.
                 * </p>
                 * 
                 * @return
                 *     A list containing immutable objects of type {@link Designation}.
                 */
                public List<Designation> getDesignation() {
                    return designation;
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
                            accept(designation, "designation", visitor, Designation.class);
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
                    private List<Designation> designation = new ArrayList<>();

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
                     * The text to display to the user for this concept in the context of this valueset. If no display is provided, then 
                     * applications using the value set use the display specified for the code by the system.
                     * </p>
                     * 
                     * @param display
                     *     Text to display for this code for this value set in this valueset
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
                     * Additional representations for this concept when used in this value set - other languages, aliases, specialized 
                     * purposes, used for particular purposes, etc.
                     * </p>
                     * 
                     * @param designation
                     *     Additional representations for this concept
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
                     * Additional representations for this concept when used in this value set - other languages, aliases, specialized 
                     * purposes, used for particular purposes, etc.
                     * </p>
                     * 
                     * @param designation
                     *     Additional representations for this concept
                     * 
                     * @return
                     *     A reference to this Builder instance.
                     */
                    public Builder designation(Collection<Designation> designation) {
                        this.designation.addAll(designation);
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
                        builder.designation.addAll(concept.designation);
                        return builder;
                    }
                }

                /**
                 * <p>
                 * Additional representations for this concept when used in this value set - other languages, aliases, specialized 
                 * purposes, used for particular purposes, etc.
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
                     * A code that represents types of uses of designations.
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
                         * A code that represents types of uses of designations.
                         * </p>
                         * 
                         * @param use
                         *     Types of uses of designations
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
            }

            /**
             * <p>
             * Select concepts by specify a matching criterion based on the properties (including relationships) defined by the 
             * system, or on filters defined by the system. If multiple filters are specified, they SHALL all be true.
             * </p>
             */
            public static class Filter extends BackboneElement {
                private final Code property;
                private final FilterOperator op;
                private final String value;

                private Filter(Builder builder) {
                    super(builder);
                    this.property = ValidationSupport.requireNonNull(builder.property, "property");
                    this.op = ValidationSupport.requireNonNull(builder.op, "op");
                    this.value = ValidationSupport.requireNonNull(builder.value, "value");
                }

                /**
                 * <p>
                 * A code that identifies a property or a filter defined in the code system.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Code}.
                 */
                public Code getProperty() {
                    return property;
                }

                /**
                 * <p>
                 * The kind of operation to perform as a part of the filter criteria.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link FilterOperator}.
                 */
                public FilterOperator getOp() {
                    return op;
                }

                /**
                 * <p>
                 * The match value may be either a code defined by the system, or a string value, which is a regex match on the literal 
                 * string of the property value (if the filter represents a property defined in CodeSystem) or of the system filter value 
                 * (if the filter represents a filter defined in CodeSystem) when the operation is 'regex', or one of the values (true 
                 * and false), when the operation is 'exists'.
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
                            accept(property, "property", visitor);
                            accept(op, "op", visitor);
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

                public static Builder builder(Code property, FilterOperator op, String value) {
                    return new Builder(property, op, value);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final Code property;
                    private final FilterOperator op;
                    private final String value;

                    private Builder(Code property, FilterOperator op, String value) {
                        super();
                        this.property = property;
                        this.op = op;
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
                    public Filter build() {
                        return new Filter(this);
                    }

                    private static Builder from(Filter filter) {
                        Builder builder = new Builder(filter.property, filter.op, filter.value);
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
     * A value set can also be "expanded", where the value set is turned into a simple collection of enumerated codes. This 
     * element holds the expansion, if it has been performed.
     * </p>
     */
    public static class Expansion extends BackboneElement {
        private final Uri identifier;
        private final DateTime timestamp;
        private final Integer total;
        private final Integer offset;
        private final List<Parameter> parameter;
        private final List<Contains> contains;

        private Expansion(Builder builder) {
            super(builder);
            this.identifier = builder.identifier;
            this.timestamp = ValidationSupport.requireNonNull(builder.timestamp, "timestamp");
            this.total = builder.total;
            this.offset = builder.offset;
            this.parameter = builder.parameter;
            this.contains = builder.contains;
        }

        /**
         * <p>
         * An identifier that uniquely identifies this expansion of the valueset, based on a unique combination of the provided 
         * parameters, the system default parameters, and the underlying system code system versions etc. Systems may re-use the 
         * same identifier as long as those factors remain the same, and the expansion is the same, but are not required to do 
         * so. This is a business identifier.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getIdentifier() {
            return identifier;
        }

        /**
         * <p>
         * The time at which the expansion was produced by the expanding system.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getTimestamp() {
            return timestamp;
        }

        /**
         * <p>
         * The total number of concepts in the expansion. If the number of concept nodes in this resource is less than the stated 
         * number, then the server can return more using the offset parameter.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getTotal() {
            return total;
        }

        /**
         * <p>
         * If paging is being used, the offset at which this resource starts. I.e. this resource is a partial view into the 
         * expansion. If paging is not being used, this element SHALL NOT be present.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getOffset() {
            return offset;
        }

        /**
         * <p>
         * A parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to 
         * check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.
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
         * The codes that are contained in the value set expansion.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Contains}.
         */
        public List<Contains> getContains() {
            return contains;
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
                    accept(identifier, "identifier", visitor);
                    accept(timestamp, "timestamp", visitor);
                    accept(total, "total", visitor);
                    accept(offset, "offset", visitor);
                    accept(parameter, "parameter", visitor, Parameter.class);
                    accept(contains, "contains", visitor, Contains.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(DateTime timestamp) {
            return new Builder(timestamp);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final DateTime timestamp;

            // optional
            private Uri identifier;
            private Integer total;
            private Integer offset;
            private List<Parameter> parameter = new ArrayList<>();
            private List<Contains> contains = new ArrayList<>();

            private Builder(DateTime timestamp) {
                super();
                this.timestamp = timestamp;
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
             * An identifier that uniquely identifies this expansion of the valueset, based on a unique combination of the provided 
             * parameters, the system default parameters, and the underlying system code system versions etc. Systems may re-use the 
             * same identifier as long as those factors remain the same, and the expansion is the same, but are not required to do 
             * so. This is a business identifier.
             * </p>
             * 
             * @param identifier
             *     Identifies the value set expansion (business identifier)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder identifier(Uri identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * <p>
             * The total number of concepts in the expansion. If the number of concept nodes in this resource is less than the stated 
             * number, then the server can return more using the offset parameter.
             * </p>
             * 
             * @param total
             *     Total number of codes in the expansion
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder total(Integer total) {
                this.total = total;
                return this;
            }

            /**
             * <p>
             * If paging is being used, the offset at which this resource starts. I.e. this resource is a partial view into the 
             * expansion. If paging is not being used, this element SHALL NOT be present.
             * </p>
             * 
             * @param offset
             *     Offset at which this resource starts
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder offset(Integer offset) {
                this.offset = offset;
                return this;
            }

            /**
             * <p>
             * A parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to 
             * check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.
             * </p>
             * 
             * @param parameter
             *     Parameter that controlled the expansion process
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
             * A parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to 
             * check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.
             * </p>
             * 
             * @param parameter
             *     Parameter that controlled the expansion process
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
             * The codes that are contained in the value set expansion.
             * </p>
             * 
             * @param contains
             *     Codes in the value set
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder contains(Contains... contains) {
                for (Contains value : contains) {
                    this.contains.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The codes that are contained in the value set expansion.
             * </p>
             * 
             * @param contains
             *     Codes in the value set
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder contains(Collection<Contains> contains) {
                this.contains.addAll(contains);
                return this;
            }

            @Override
            public Expansion build() {
                return new Expansion(this);
            }

            private static Builder from(Expansion expansion) {
                Builder builder = new Builder(expansion.timestamp);
                builder.id = expansion.id;
                builder.extension.addAll(expansion.extension);
                builder.modifierExtension.addAll(expansion.modifierExtension);
                builder.identifier = expansion.identifier;
                builder.total = expansion.total;
                builder.offset = expansion.offset;
                builder.parameter.addAll(expansion.parameter);
                builder.contains.addAll(expansion.contains);
                return builder;
            }
        }

        /**
         * <p>
         * A parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to 
         * check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.
         * </p>
         */
        public static class Parameter extends BackboneElement {
            private final String name;
            private final Element value;

            private Parameter(Builder builder) {
                super(builder);
                this.name = ValidationSupport.requireNonNull(builder.name, "name");
                this.value = ValidationSupport.choiceElement(builder.value, "value", String.class, Boolean.class, Integer.class, Decimal.class, Uri.class, Code.class, DateTime.class);
            }

            /**
             * <p>
             * Name of the input parameter to the $expand operation; may be a server-assigned name for additional default or other 
             * server-supplied parameters used to control the expansion process.
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
             * The value of the parameter.
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
                        accept(name, "name", visitor);
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

            public static Builder builder(String name) {
                return new Builder(name);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final String name;

                // optional
                private Element value;

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
                 * The value of the parameter.
                 * </p>
                 * 
                 * @param value
                 *     Value of the named parameter
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder value(Element value) {
                    this.value = value;
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
                    builder.value = parameter.value;
                    return builder;
                }
            }
        }

        /**
         * <p>
         * The codes that are contained in the value set expansion.
         * </p>
         */
        public static class Contains extends BackboneElement {
            private final Uri system;
            private final Boolean _abstract;
            private final Boolean inactive;
            private final String version;
            private final Code code;
            private final String display;
            private final List<ValueSet.Compose.Include.Concept.Designation> designation;
            private final List<ValueSet.Expansion.Contains> contains;

            private Contains(Builder builder) {
                super(builder);
                this.system = builder.system;
                this._abstract = builder._abstract;
                this.inactive = builder.inactive;
                this.version = builder.version;
                this.code = builder.code;
                this.display = builder.display;
                this.designation = builder.designation;
                this.contains = builder.contains;
            }

            /**
             * <p>
             * An absolute URI which is the code system in which the code for this item in the expansion is defined.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Uri}.
             */
            public Uri getSystem() {
                return system;
            }

            /**
             * <p>
             * If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code 
             * directly as a proper value.
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
             * If the concept is inactive in the code system that defines it. Inactive codes are those that are no longer to be used, 
             * but are maintained by the code system for understanding legacy data. It might not be known or specified whether an 
             * concept is inactive (and it may depend on the context of use).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getInactive() {
                return inactive;
            }

            /**
             * <p>
             * The version of the code system from this code was taken. Note that a well-maintained code system does not need the 
             * version reported, because the meaning of codes is consistent across versions. However this cannot consistently be 
             * assured, and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.
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
             * The code for this item in the expansion hierarchy. If this code is missing the entry in the hierarchy is a place 
             * holder (abstract) and does not represent a valid code in the value set.
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
             * The recommended display for this item in the expansion.
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
             * Additional representations for this item - other languages, aliases, specialized purposes, used for particular 
             * purposes, etc. These are relevant when the conditions of the expansion do not fix to a single correct representation.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Designation}.
             */
            public List<ValueSet.Compose.Include.Concept.Designation> getDesignation() {
                return designation;
            }

            /**
             * <p>
             * Other codes and entries contained under this entry in the hierarchy.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Contains}.
             */
            public List<ValueSet.Expansion.Contains> getContains() {
                return contains;
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
                        accept(system, "system", visitor);
                        accept(_abstract, "abstract", visitor);
                        accept(inactive, "inactive", visitor);
                        accept(version, "version", visitor);
                        accept(code, "code", visitor);
                        accept(display, "display", visitor);
                        accept(designation, "designation", visitor, ValueSet.Compose.Include.Concept.Designation.class);
                        accept(contains, "contains", visitor, ValueSet.Expansion.Contains.class);
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
                private Uri system;
                private Boolean _abstract;
                private Boolean inactive;
                private String version;
                private Code code;
                private String display;
                private List<ValueSet.Compose.Include.Concept.Designation> designation = new ArrayList<>();
                private List<ValueSet.Expansion.Contains> contains = new ArrayList<>();

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
                 * An absolute URI which is the code system in which the code for this item in the expansion is defined.
                 * </p>
                 * 
                 * @param system
                 *     System value for the code
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder system(Uri system) {
                    this.system = system;
                    return this;
                }

                /**
                 * <p>
                 * If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code 
                 * directly as a proper value.
                 * </p>
                 * 
                 * @param _abstract
                 *     If user cannot select this entry
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder _abstract(Boolean _abstract) {
                    this._abstract = _abstract;
                    return this;
                }

                /**
                 * <p>
                 * If the concept is inactive in the code system that defines it. Inactive codes are those that are no longer to be used, 
                 * but are maintained by the code system for understanding legacy data. It might not be known or specified whether an 
                 * concept is inactive (and it may depend on the context of use).
                 * </p>
                 * 
                 * @param inactive
                 *     If concept is inactive in the code system
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder inactive(Boolean inactive) {
                    this.inactive = inactive;
                    return this;
                }

                /**
                 * <p>
                 * The version of the code system from this code was taken. Note that a well-maintained code system does not need the 
                 * version reported, because the meaning of codes is consistent across versions. However this cannot consistently be 
                 * assured, and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.
                 * </p>
                 * 
                 * @param version
                 *     Version in which this code/display is defined
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
                 * The code for this item in the expansion hierarchy. If this code is missing the entry in the hierarchy is a place 
                 * holder (abstract) and does not represent a valid code in the value set.
                 * </p>
                 * 
                 * @param code
                 *     Code - if blank, this is not a selectable code
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder code(Code code) {
                    this.code = code;
                    return this;
                }

                /**
                 * <p>
                 * The recommended display for this item in the expansion.
                 * </p>
                 * 
                 * @param display
                 *     User display for the concept
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
                 * Additional representations for this item - other languages, aliases, specialized purposes, used for particular 
                 * purposes, etc. These are relevant when the conditions of the expansion do not fix to a single correct representation.
                 * </p>
                 * 
                 * @param designation
                 *     Additional representations for this item
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder designation(ValueSet.Compose.Include.Concept.Designation... designation) {
                    for (ValueSet.Compose.Include.Concept.Designation value : designation) {
                        this.designation.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Additional representations for this item - other languages, aliases, specialized purposes, used for particular 
                 * purposes, etc. These are relevant when the conditions of the expansion do not fix to a single correct representation.
                 * </p>
                 * 
                 * @param designation
                 *     Additional representations for this item
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder designation(Collection<ValueSet.Compose.Include.Concept.Designation> designation) {
                    this.designation.addAll(designation);
                    return this;
                }

                /**
                 * <p>
                 * Other codes and entries contained under this entry in the hierarchy.
                 * </p>
                 * 
                 * @param contains
                 *     Codes contained under this entry
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder contains(ValueSet.Expansion.Contains... contains) {
                    for (ValueSet.Expansion.Contains value : contains) {
                        this.contains.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Other codes and entries contained under this entry in the hierarchy.
                 * </p>
                 * 
                 * @param contains
                 *     Codes contained under this entry
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder contains(Collection<ValueSet.Expansion.Contains> contains) {
                    this.contains.addAll(contains);
                    return this;
                }

                @Override
                public Contains build() {
                    return new Contains(this);
                }

                private static Builder from(Contains contains) {
                    Builder builder = new Builder();
                    builder.id = contains.id;
                    builder.extension.addAll(contains.extension);
                    builder.modifierExtension.addAll(contains.modifierExtension);
                    builder.system = contains.system;
                    builder._abstract = contains._abstract;
                    builder.inactive = contains.inactive;
                    builder.version = contains.version;
                    builder.code = contains.code;
                    builder.display = contains.display;
                    builder.designation.addAll(contains.designation);
                    builder.contains.addAll(contains.contains);
                    return builder;
                }
            }
        }
    }
}
