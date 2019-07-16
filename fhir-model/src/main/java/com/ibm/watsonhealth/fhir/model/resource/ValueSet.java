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
import java.util.Objects;

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

    private volatile int hashCode;

    private ValueSet(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        version = builder.version;
        name = builder.name;
        title = builder.title;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        experimental = builder.experimental;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.contact, "contact"));
        description = builder.description;
        useContext = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.useContext, "useContext"));
        jurisdiction = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.jurisdiction, "jurisdiction"));
        immutable = builder.immutable;
        purpose = builder.purpose;
        copyright = builder.copyright;
        compose = builder.compose;
        expansion = builder.expansion;
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
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
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
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail}.
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
     *     An unmodifiable list containing immutable objects of type {@link UsageContext}.
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
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
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
        ValueSet other = (ValueSet) obj;
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
            Objects.equals(immutable, other.immutable) && 
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(copyright, other.copyright) && 
            Objects.equals(compose, other.compose) && 
            Objects.equals(expansion, other.expansion);
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
                immutable, 
                purpose, 
                copyright, 
                compose, 
                expansion);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status).from(this);
    }

    public Builder toBuilder(PublicationStatus status) {
        return new Builder(status).from(this);
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the value set (business identifier)
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
         * <p>
         * A formal identifier that is used to identify this value set when it is represented in other formats, or referenced in 
         * a specification, model, design or an instance.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the value set (business identifier)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
         */
        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        /**
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Contact details to assist a user in finding and communicating with the publisher.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contact
         *     Contact details for the publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact = new ArrayList<>(contact);
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
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate value set instances.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param useContext
         *     The context that the content is intended to support
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder useContext(Collection<UsageContext> useContext) {
            this.useContext = new ArrayList<>(useContext);
            return this;
        }

        /**
         * <p>
         * A legal or geographic region in which the value set is intended to be used.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for value set (if applicable)
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
         * <p>
         * A legal or geographic region in which the value set is intended to be used.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for value set (if applicable)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
            this.jurisdiction = new ArrayList<>(jurisdiction);
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
         */
        public Builder expansion(Expansion expansion) {
            this.expansion = expansion;
            return this;
        }

        @Override
        public ValueSet build() {
            return new ValueSet(this);
        }

        private Builder from(ValueSet valueSet) {
            id = valueSet.id;
            meta = valueSet.meta;
            implicitRules = valueSet.implicitRules;
            language = valueSet.language;
            text = valueSet.text;
            contained.addAll(valueSet.contained);
            extension.addAll(valueSet.extension);
            modifierExtension.addAll(valueSet.modifierExtension);
            url = valueSet.url;
            identifier.addAll(valueSet.identifier);
            version = valueSet.version;
            name = valueSet.name;
            title = valueSet.title;
            experimental = valueSet.experimental;
            date = valueSet.date;
            publisher = valueSet.publisher;
            contact.addAll(valueSet.contact);
            description = valueSet.description;
            useContext.addAll(valueSet.useContext);
            jurisdiction.addAll(valueSet.jurisdiction);
            immutable = valueSet.immutable;
            purpose = valueSet.purpose;
            copyright = valueSet.copyright;
            compose = valueSet.compose;
            expansion = valueSet.expansion;
            return this;
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

        private volatile int hashCode;

        private Compose(Builder builder) {
            super(builder);
            lockedDate = builder.lockedDate;
            inactive = builder.inactive;
            include = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.include, "include"));
            exclude = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.exclude, "exclude"));
            ValidationSupport.requireValueOrChildren(this);
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
         *     An unmodifiable list containing immutable objects of type {@link Include}.
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
         *     An unmodifiable list containing immutable objects of type {@link Include}.
         */
        public List<ValueSet.Compose.Include> getExclude() {
            return exclude;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (lockedDate != null) || 
                (inactive != null) || 
                !include.isEmpty() || 
                !exclude.isEmpty();
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
            Compose other = (Compose) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(lockedDate, other.lockedDate) && 
                Objects.equals(inactive, other.inactive) && 
                Objects.equals(include, other.include) && 
                Objects.equals(exclude, other.exclude);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    lockedDate, 
                    inactive, 
                    include, 
                    exclude);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(include).from(this);
        }

        public Builder toBuilder(Collection<Include> include) {
            return new Builder(include).from(this);
        }

        public static Builder builder(Collection<Include> include) {
            return new Builder(include);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final List<Include> include;

            // optional
            private Date lockedDate;
            private Boolean inactive;
            private List<ValueSet.Compose.Include> exclude = new ArrayList<>();

            private Builder(Collection<Include> include) {
                super();
                this.include = new ArrayList<>(include);
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
             *     A reference to this Builder instance
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
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
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
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
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
             *     A reference to this Builder instance
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
             *     A reference to this Builder instance
             */
            public Builder inactive(Boolean inactive) {
                this.inactive = inactive;
                return this;
            }

            /**
             * <p>
             * Exclude one or more codes from the value set based on code system filters and/or other value sets.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param exclude
             *     Explicitly exclude codes from a code system or other value sets
             * 
             * @return
             *     A reference to this Builder instance
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param exclude
             *     Explicitly exclude codes from a code system or other value sets
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder exclude(Collection<ValueSet.Compose.Include> exclude) {
                this.exclude = new ArrayList<>(exclude);
                return this;
            }

            @Override
            public Compose build() {
                return new Compose(this);
            }

            private Builder from(Compose compose) {
                id = compose.id;
                extension.addAll(compose.extension);
                modifierExtension.addAll(compose.modifierExtension);
                lockedDate = compose.lockedDate;
                inactive = compose.inactive;
                exclude.addAll(compose.exclude);
                return this;
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

            private volatile int hashCode;

            private Include(Builder builder) {
                super(builder);
                system = builder.system;
                version = builder.version;
                concept = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.concept, "concept"));
                filter = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.filter, "filter"));
                valueSet = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.valueSet, "valueSet"));
                ValidationSupport.requireValueOrChildren(this);
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
             *     An unmodifiable list containing immutable objects of type {@link Concept}.
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
             *     An unmodifiable list containing immutable objects of type {@link Filter}.
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
             *     An unmodifiable list containing immutable objects of type {@link Canonical}.
             */
            public List<Canonical> getValueSet() {
                return valueSet;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (system != null) || 
                    (version != null) || 
                    !concept.isEmpty() || 
                    !filter.isEmpty() || 
                    !valueSet.isEmpty();
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
                Include other = (Include) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(system, other.system) && 
                    Objects.equals(version, other.version) && 
                    Objects.equals(concept, other.concept) && 
                    Objects.equals(filter, other.filter) && 
                    Objects.equals(valueSet, other.valueSet);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        system, 
                        version, 
                        concept, 
                        filter, 
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
                 *     A reference to this Builder instance
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
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
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
                 * <p>
                 * May be used to represent additional information that is not part of the basic definition of the element. To make the 
                 * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
                 * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
                 * of the definition of the extension.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param extension
                 *     Additional content defined by implementations
                 * 
                 * @return
                 *     A reference to this Builder instance
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
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
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
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
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
                 *     A reference to this Builder instance
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
                 *     A reference to this Builder instance
                 */
                public Builder version(String version) {
                    this.version = version;
                    return this;
                }

                /**
                 * <p>
                 * Specifies a concept to be included or excluded.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param concept
                 *     A concept defined in the system
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
                 * <p>
                 * Specifies a concept to be included or excluded.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param concept
                 *     A concept defined in the system
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder concept(Collection<Concept> concept) {
                    this.concept = new ArrayList<>(concept);
                    return this;
                }

                /**
                 * <p>
                 * Select concepts by specify a matching criterion based on the properties (including relationships) defined by the 
                 * system, or on filters defined by the system. If multiple filters are specified, they SHALL all be true.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param filter
                 *     Select codes/concepts by their properties (including relationships)
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
                 * <p>
                 * Select concepts by specify a matching criterion based on the properties (including relationships) defined by the 
                 * system, or on filters defined by the system. If multiple filters are specified, they SHALL all be true.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param filter
                 *     Select codes/concepts by their properties (including relationships)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder filter(Collection<Filter> filter) {
                    this.filter = new ArrayList<>(filter);
                    return this;
                }

                /**
                 * <p>
                 * Selects the concepts found in this value set (based on its value set definition). This is an absolute URI that is a 
                 * reference to ValueSet.url. If multiple value sets are specified this includes the union of the contents of all of the 
                 * referenced value sets.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param valueSet
                 *     Select the contents included in this value set
                 * 
                 * @return
                 *     A reference to this Builder instance
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
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param valueSet
                 *     Select the contents included in this value set
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder valueSet(Collection<Canonical> valueSet) {
                    this.valueSet = new ArrayList<>(valueSet);
                    return this;
                }

                @Override
                public Include build() {
                    return new Include(this);
                }

                private Builder from(Include include) {
                    id = include.id;
                    extension.addAll(include.extension);
                    modifierExtension.addAll(include.modifierExtension);
                    system = include.system;
                    version = include.version;
                    concept.addAll(include.concept);
                    filter.addAll(include.filter);
                    valueSet.addAll(include.valueSet);
                    return this;
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

                private volatile int hashCode;

                private Concept(Builder builder) {
                    super(builder);
                    code = ValidationSupport.requireNonNull(builder.code, "code");
                    display = builder.display;
                    designation = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.designation, "designation"));
                    ValidationSupport.requireValueOrChildren(this);
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
                 *     An unmodifiable list containing immutable objects of type {@link Designation}.
                 */
                public List<Designation> getDesignation() {
                    return designation;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (code != null) || 
                        (display != null) || 
                        !designation.isEmpty();
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
                        Objects.equals(designation, other.designation);
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
                            designation);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(code).from(this);
                }

                public Builder toBuilder(Code code) {
                    return new Builder(code).from(this);
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
                     *     A reference to this Builder instance
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
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
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
                     * <p>
                     * May be used to represent additional information that is not part of the basic definition of the element. To make the 
                     * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
                     * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
                     * of the definition of the extension.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param extension
                     *     Additional content defined by implementations
                     * 
                     * @return
                     *     A reference to this Builder instance
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
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
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
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param modifierExtension
                     *     Extensions that cannot be ignored even if unrecognized
                     * 
                     * @return
                     *     A reference to this Builder instance
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
                     *     A reference to this Builder instance
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
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
                     * 
                     * @param designation
                     *     Additional representations for this concept
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
                     * <p>
                     * Additional representations for this concept when used in this value set - other languages, aliases, specialized 
                     * purposes, used for particular purposes, etc.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param designation
                     *     Additional representations for this concept
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder designation(Collection<Designation> designation) {
                        this.designation = new ArrayList<>(designation);
                        return this;
                    }

                    @Override
                    public Concept build() {
                        return new Concept(this);
                    }

                    private Builder from(Concept concept) {
                        id = concept.id;
                        extension.addAll(concept.extension);
                        modifierExtension.addAll(concept.modifierExtension);
                        display = concept.display;
                        designation.addAll(concept.designation);
                        return this;
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

                    private volatile int hashCode;

                    private Designation(Builder builder) {
                        super(builder);
                        language = builder.language;
                        use = builder.use;
                        value = ValidationSupport.requireNonNull(builder.value, "value");
                        ValidationSupport.requireValueOrChildren(this);
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
                    public boolean hasChildren() {
                        return super.hasChildren() || 
                            (language != null) || 
                            (use != null) || 
                            (value != null);
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
                        return new Builder(value).from(this);
                    }

                    public Builder toBuilder(String value) {
                        return new Builder(value).from(this);
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
                         *     A reference to this Builder instance
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
                         * <p>
                         * Adds new element(s) to existing list
                         * </p>
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
                         * <p>
                         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
                         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
                         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
                         * of the definition of the extension.
                         * </p>
                         * <p>
                         * Replaces existing list with a new one containing elements from the Collection
                         * </p>
                         * 
                         * @param extension
                         *     Additional content defined by implementations
                         * 
                         * @return
                         *     A reference to this Builder instance
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
                         * <p>
                         * Adds new element(s) to existing list
                         * </p>
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
                         * <p>
                         * Replaces existing list with a new one containing elements from the Collection
                         * </p>
                         * 
                         * @param modifierExtension
                         *     Extensions that cannot be ignored even if unrecognized
                         * 
                         * @return
                         *     A reference to this Builder instance
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
                         *     A reference to this Builder instance
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
                         *     A reference to this Builder instance
                         */
                        public Builder use(Coding use) {
                            this.use = use;
                            return this;
                        }

                        @Override
                        public Designation build() {
                            return new Designation(this);
                        }

                        private Builder from(Designation designation) {
                            id = designation.id;
                            extension.addAll(designation.extension);
                            modifierExtension.addAll(designation.modifierExtension);
                            language = designation.language;
                            use = designation.use;
                            return this;
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

                private volatile int hashCode;

                private Filter(Builder builder) {
                    super(builder);
                    property = ValidationSupport.requireNonNull(builder.property, "property");
                    op = ValidationSupport.requireNonNull(builder.op, "op");
                    value = ValidationSupport.requireNonNull(builder.value, "value");
                    ValidationSupport.requireValueOrChildren(this);
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
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (property != null) || 
                        (op != null) || 
                        (value != null);
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
                        Objects.equals(property, other.property) && 
                        Objects.equals(op, other.op) && 
                        Objects.equals(value, other.value);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            property, 
                            op, 
                            value);
                        hashCode = result;
                    }
                    return result;
                }

                @Override
                public Builder toBuilder() {
                    return new Builder(property, op, value).from(this);
                }

                public Builder toBuilder(Code property, FilterOperator op, String value) {
                    return new Builder(property, op, value).from(this);
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
                     *     A reference to this Builder instance
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
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
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
                     * <p>
                     * May be used to represent additional information that is not part of the basic definition of the element. To make the 
                     * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
                     * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
                     * of the definition of the extension.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param extension
                     *     Additional content defined by implementations
                     * 
                     * @return
                     *     A reference to this Builder instance
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
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
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
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param modifierExtension
                     *     Extensions that cannot be ignored even if unrecognized
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    @Override
                    public Builder modifierExtension(Collection<Extension> modifierExtension) {
                        return (Builder) super.modifierExtension(modifierExtension);
                    }

                    @Override
                    public Filter build() {
                        return new Filter(this);
                    }

                    private Builder from(Filter filter) {
                        id = filter.id;
                        extension.addAll(filter.extension);
                        modifierExtension.addAll(filter.modifierExtension);
                        return this;
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

        private volatile int hashCode;

        private Expansion(Builder builder) {
            super(builder);
            identifier = builder.identifier;
            timestamp = ValidationSupport.requireNonNull(builder.timestamp, "timestamp");
            total = builder.total;
            offset = builder.offset;
            parameter = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.parameter, "parameter"));
            contains = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.contains, "contains"));
            ValidationSupport.requireValueOrChildren(this);
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
         *     An unmodifiable list containing immutable objects of type {@link Parameter}.
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
         *     An unmodifiable list containing immutable objects of type {@link Contains}.
         */
        public List<Contains> getContains() {
            return contains;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (identifier != null) || 
                (timestamp != null) || 
                (total != null) || 
                (offset != null) || 
                !parameter.isEmpty() || 
                !contains.isEmpty();
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
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(timestamp, other.timestamp) && 
                Objects.equals(total, other.total) && 
                Objects.equals(offset, other.offset) && 
                Objects.equals(parameter, other.parameter) && 
                Objects.equals(contains, other.contains);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identifier, 
                    timestamp, 
                    total, 
                    offset, 
                    parameter, 
                    contains);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(timestamp).from(this);
        }

        public Builder toBuilder(DateTime timestamp) {
            return new Builder(timestamp).from(this);
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
             *     A reference to this Builder instance
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
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
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
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
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
             *     A reference to this Builder instance
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
             *     A reference to this Builder instance
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
             *     A reference to this Builder instance
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
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param parameter
             *     Parameter that controlled the expansion process
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
             * <p>
             * A parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to 
             * check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param parameter
             *     Parameter that controlled the expansion process
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder parameter(Collection<Parameter> parameter) {
                this.parameter = new ArrayList<>(parameter);
                return this;
            }

            /**
             * <p>
             * The codes that are contained in the value set expansion.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param contains
             *     Codes in the value set
             * 
             * @return
             *     A reference to this Builder instance
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param contains
             *     Codes in the value set
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder contains(Collection<Contains> contains) {
                this.contains = new ArrayList<>(contains);
                return this;
            }

            @Override
            public Expansion build() {
                return new Expansion(this);
            }

            private Builder from(Expansion expansion) {
                id = expansion.id;
                extension.addAll(expansion.extension);
                modifierExtension.addAll(expansion.modifierExtension);
                identifier = expansion.identifier;
                total = expansion.total;
                offset = expansion.offset;
                parameter.addAll(expansion.parameter);
                contains.addAll(expansion.contains);
                return this;
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

            private volatile int hashCode;

            private Parameter(Builder builder) {
                super(builder);
                name = ValidationSupport.requireNonNull(builder.name, "name");
                value = ValidationSupport.choiceElement(builder.value, "value", String.class, Boolean.class, Integer.class, Decimal.class, Uri.class, Code.class, DateTime.class);
                ValidationSupport.requireValueOrChildren(this);
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
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (name != null) || 
                    (value != null);
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
                        accept(value, "value", visitor);
                    }
                    visitor.visitEnd(elementName, this);
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
                    Objects.equals(value, other.value);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        name, 
                        value);
                    hashCode = result;
                }
                return result;
            }

            @Override
            public Builder toBuilder() {
                return new Builder(name).from(this);
            }

            public Builder toBuilder(String name) {
                return new Builder(name).from(this);
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
                 *     A reference to this Builder instance
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
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
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
                 * <p>
                 * May be used to represent additional information that is not part of the basic definition of the element. To make the 
                 * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
                 * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
                 * of the definition of the extension.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param extension
                 *     Additional content defined by implementations
                 * 
                 * @return
                 *     A reference to this Builder instance
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
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
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
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
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
                 *     A reference to this Builder instance
                 */
                public Builder value(Element value) {
                    this.value = value;
                    return this;
                }

                @Override
                public Parameter build() {
                    return new Parameter(this);
                }

                private Builder from(Parameter parameter) {
                    id = parameter.id;
                    extension.addAll(parameter.extension);
                    modifierExtension.addAll(parameter.modifierExtension);
                    value = parameter.value;
                    return this;
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

            private volatile int hashCode;

            private Contains(Builder builder) {
                super(builder);
                system = builder.system;
                _abstract = builder._abstract;
                inactive = builder.inactive;
                version = builder.version;
                code = builder.code;
                display = builder.display;
                designation = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.designation, "designation"));
                contains = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.contains, "contains"));
                ValidationSupport.requireValueOrChildren(this);
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
             *     An unmodifiable list containing immutable objects of type {@link Designation}.
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
             *     An unmodifiable list containing immutable objects of type {@link Contains}.
             */
            public List<ValueSet.Expansion.Contains> getContains() {
                return contains;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (system != null) || 
                    (_abstract != null) || 
                    (inactive != null) || 
                    (version != null) || 
                    (code != null) || 
                    (display != null) || 
                    !designation.isEmpty() || 
                    !contains.isEmpty();
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
                Contains other = (Contains) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(system, other.system) && 
                    Objects.equals(_abstract, other._abstract) && 
                    Objects.equals(inactive, other.inactive) && 
                    Objects.equals(version, other.version) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(display, other.display) && 
                    Objects.equals(designation, other.designation) && 
                    Objects.equals(contains, other.contains);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        system, 
                        _abstract, 
                        inactive, 
                        version, 
                        code, 
                        display, 
                        designation, 
                        contains);
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
                 *     A reference to this Builder instance
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
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
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
                 * <p>
                 * May be used to represent additional information that is not part of the basic definition of the element. To make the 
                 * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
                 * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
                 * of the definition of the extension.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param extension
                 *     Additional content defined by implementations
                 * 
                 * @return
                 *     A reference to this Builder instance
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
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
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
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
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
                 *     A reference to this Builder instance
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
                 *     A reference to this Builder instance
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
                 *     A reference to this Builder instance
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
                 *     A reference to this Builder instance
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
                 *     A reference to this Builder instance
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
                 *     A reference to this Builder instance
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
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param designation
                 *     Additional representations for this item
                 * 
                 * @return
                 *     A reference to this Builder instance
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
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param designation
                 *     Additional representations for this item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder designation(Collection<ValueSet.Compose.Include.Concept.Designation> designation) {
                    this.designation = new ArrayList<>(designation);
                    return this;
                }

                /**
                 * <p>
                 * Other codes and entries contained under this entry in the hierarchy.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param contains
                 *     Codes contained under this entry
                 * 
                 * @return
                 *     A reference to this Builder instance
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
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param contains
                 *     Codes contained under this entry
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder contains(Collection<ValueSet.Expansion.Contains> contains) {
                    this.contains = new ArrayList<>(contains);
                    return this;
                }

                @Override
                public Contains build() {
                    return new Contains(this);
                }

                private Builder from(Contains contains) {
                    id = contains.id;
                    extension.addAll(contains.extension);
                    modifierExtension.addAll(contains.modifierExtension);
                    system = contains.system;
                    _abstract = contains._abstract;
                    inactive = contains.inactive;
                    version = contains.version;
                    code = contains.code;
                    display = contains.display;
                    designation.addAll(contains.designation);
                    this.contains.addAll(contains.contains);
                    return this;
                }
            }
        }
    }
}
