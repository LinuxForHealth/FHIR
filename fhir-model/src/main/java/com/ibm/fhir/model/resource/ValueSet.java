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
import com.ibm.fhir.model.type.Date;
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
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.FilterOperator;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A ValueSet resource instance specifies a set of codes drawn from one or more code systems, intended for use in a 
 * particular context. Value sets link between [CodeSystem](codesystem.html) definitions and their use in [coded elements]
 * (terminologies.html).
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.Value.NORMATIVE
)
@Constraint(
    id = "vsd-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/ValueSet"
)
@Constraint(
    id = "vsd-1",
    level = "Rule",
    location = "ValueSet.compose.include",
    description = "A value set include/exclude SHALL have a value set or a system",
    expression = "valueSet.exists() or system.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/ValueSet"
)
@Constraint(
    id = "vsd-2",
    level = "Rule",
    location = "ValueSet.compose.include",
    description = "A value set with concepts or filters SHALL include a system",
    expression = "(concept.exists() or filter.exists()) implies system.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/ValueSet"
)
@Constraint(
    id = "vsd-3",
    level = "Rule",
    location = "ValueSet.compose.include",
    description = "Cannot have both concept and filter",
    expression = "concept.empty() or filter.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/ValueSet"
)
@Constraint(
    id = "vsd-6",
    level = "Rule",
    location = "ValueSet.expansion.contains",
    description = "SHALL have a code or a display",
    expression = "code.exists() or display.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/ValueSet"
)
@Constraint(
    id = "vsd-9",
    level = "Rule",
    location = "ValueSet.expansion.contains",
    description = "Must have a code if not abstract",
    expression = "code.exists() or abstract = true",
    source = "http://hl7.org/fhir/StructureDefinition/ValueSet"
)
@Constraint(
    id = "vsd-10",
    level = "Rule",
    location = "ValueSet.expansion.contains",
    description = "Must have a system if a code is present",
    expression = "code.empty() or system.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/ValueSet"
)
@Constraint(
    id = "valueSet-11",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/ValueSet",
    generated = true
)
@Constraint(
    id = "valueSet-12",
    level = "Warning",
    location = "compose.include.concept.designation.language",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/languages",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/languages', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/ValueSet",
    generated = true
)
@Constraint(
    id = "valueSet-13",
    level = "Warning",
    location = "compose.include.concept.designation.use",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/designation-use",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/designation-use', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/ValueSet",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ValueSet extends DomainResource {
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
    @Summary
    private final Boolean immutable;
    private final Markdown purpose;
    private final Markdown copyright;
    private final Compose compose;
    private final Expansion expansion;

    private ValueSet(Builder builder) {
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
        immutable = builder.immutable;
        purpose = builder.purpose;
        copyright = builder.copyright;
        compose = builder.compose;
        expansion = builder.expansion;
    }

    /**
     * An absolute URI that is used to identify this value set when it is referenced in a specification, model, design or an 
     * instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this value set is (or will be) published. This URL can be the target of a 
     * canonical reference. It SHALL remain the same when the value set is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this value set when it is represented in other formats, or referenced in 
     * a specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, 
     * design or instance. This is an arbitrary value managed by the value set author and is not expected to be globally 
     * unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no 
     * expectation that versions can be placed in a lexicographical sequence.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the value set. This name should be usable as an identifier for the module by 
     * machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the value set.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The status of this value set. Enables tracking the life-cycle of the content. The status of the value set applies to 
     * the value set definition (ValueSet.compose) and the associated ValueSet metadata. Expansions do not have a state.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this value set is authored for testing purposes (or education/evaluation/marketing) 
     * and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the value set was created or revised (e.g. the 'content logical definition').
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the value set.
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
     * A free text natural language description of the value set from a consumer's perspective. The textual description 
     * specifies the span of meanings for concepts to be included within the Value Set Expansion, and also may specify the 
     * intended use and limitations of the Value Set.
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
     * may be used to assist with indexing and searching for appropriate value set instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the value set is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * If this is set to 'true', then no new versions of the content logical definition can be created. Note: Other metadata 
     * might still change.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getImmutable() {
        return immutable;
    }

    /**
     * Explanation of why this value set is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the value set.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * A set of criteria that define the contents of the value set by including or excluding codes selected from the 
     * specified code system(s) that the value set draws from. This is also known as the Content Logical Definition (CLD).
     * 
     * @return
     *     An immutable object of type {@link Compose} that may be null.
     */
    public Compose getCompose() {
        return compose;
    }

    /**
     * A value set can also be "expanded", where the value set is turned into a simple collection of enumerated codes. This 
     * element holds the expansion, if it has been performed.
     * 
     * @return
     *     An immutable object of type {@link Expansion} that may be null.
     */
    public Expansion getExpansion() {
        return expansion;
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
            (immutable != null) || 
            (purpose != null) || 
            (copyright != null) || 
            (compose != null) || 
            (expansion != null);
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
                accept(immutable, "immutable", visitor);
                accept(purpose, "purpose", visitor);
                accept(copyright, "copyright", visitor);
                accept(compose, "compose", visitor);
                accept(expansion, "expansion", visitor);
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
        private Boolean immutable;
        private Markdown purpose;
        private Markdown copyright;
        private Compose compose;
        private Expansion expansion;

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
         * An absolute URI that is used to identify this value set when it is referenced in a specification, model, design or an 
         * instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
         * which at which an authoritative instance of this value set is (or will be) published. This URL can be the target of a 
         * canonical reference. It SHALL remain the same when the value set is stored on different servers.
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
         * A formal identifier that is used to identify this value set when it is represented in other formats, or referenced in 
         * a specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A formal identifier that is used to identify this value set when it is represented in other formats, or referenced in 
         * a specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the value set (business identifier)
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
         *     Business version of the value set
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
         * The identifier that is used to identify this version of the value set when it is referenced in a specification, model, 
         * design or instance. This is an arbitrary value managed by the value set author and is not expected to be globally 
         * unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no 
         * expectation that versions can be placed in a lexicographical sequence.
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
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     Name for this value set (computer friendly)
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
         * A natural language name identifying the value set. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
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
         * Convenience method for setting {@code title}.
         * 
         * @param title
         *     Name for this value set (human friendly)
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
         * A short, descriptive, user-friendly title for the value set.
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
         * The status of this value set. Enables tracking the life-cycle of the content. The status of the value set applies to 
         * the value set definition (ValueSet.compose) and the associated ValueSet metadata. Expansions do not have a state.
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
         * A Boolean value to indicate that this value set is authored for testing purposes (or education/evaluation/marketing) 
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
         * The date (and optionally time) when the value set was created or revised (e.g. the 'content logical definition').
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
         * The name of the organization or individual that published the value set.
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
         * A free text natural language description of the value set from a consumer's perspective. The textual description 
         * specifies the span of meanings for concepts to be included within the Value Set Expansion, and also may specify the 
         * intended use and limitations of the Value Set.
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate value set instances.
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
         * may be used to assist with indexing and searching for appropriate value set instances.
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
         * A legal or geographic region in which the value set is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A legal or geographic region in which the value set is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for value set (if applicable)
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
         * Convenience method for setting {@code immutable}.
         * 
         * @param immutable
         *     Indicates whether or not any change to the content logical definition may occur
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #immutable(com.ibm.fhir.model.type.Boolean)
         */
        public Builder immutable(java.lang.Boolean immutable) {
            this.immutable = (immutable == null) ? null : Boolean.of(immutable);
            return this;
        }

        /**
         * If this is set to 'true', then no new versions of the content logical definition can be created. Note: Other metadata 
         * might still change.
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
         * Explanation of why this value set is needed and why it has been designed as it has.
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
         * A copyright statement relating to the value set and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the value set.
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
         * A set of criteria that define the contents of the value set by including or excluding codes selected from the 
         * specified code system(s) that the value set draws from. This is also known as the Content Logical Definition (CLD).
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
         * A value set can also be "expanded", where the value set is turned into a simple collection of enumerated codes. This 
         * element holds the expansion, if it has been performed.
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

        /**
         * Build the {@link ValueSet}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ValueSet}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ValueSet per the base specification
         */
        @Override
        public ValueSet build() {
            ValueSet valueSet = new ValueSet(this);
            if (validating) {
                validate(valueSet);
            }
            return valueSet;
        }

        protected void validate(ValueSet valueSet) {
            super.validate(valueSet);
            ValidationSupport.checkList(valueSet.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(valueSet.status, "status");
            ValidationSupport.checkList(valueSet.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(valueSet.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(valueSet.jurisdiction, "jurisdiction", CodeableConcept.class);
        }

        protected Builder from(ValueSet valueSet) {
            super.from(valueSet);
            url = valueSet.url;
            identifier.addAll(valueSet.identifier);
            version = valueSet.version;
            name = valueSet.name;
            title = valueSet.title;
            status = valueSet.status;
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
     * A set of criteria that define the contents of the value set by including or excluding codes selected from the 
     * specified code system(s) that the value set draws from. This is also known as the Content Logical Definition (CLD).
     */
    public static class Compose extends BackboneElement {
        @Summary
        private final Date lockedDate;
        @Summary
        private final Boolean inactive;
        @Summary
        @Required
        private final List<Include> include;
        private final List<ValueSet.Compose.Include> exclude;

        private Compose(Builder builder) {
            super(builder);
            lockedDate = builder.lockedDate;
            inactive = builder.inactive;
            include = Collections.unmodifiableList(builder.include);
            exclude = Collections.unmodifiableList(builder.exclude);
        }

        /**
         * The Locked Date is the effective date that is used to determine the version of all referenced Code Systems and Value 
         * Set Definitions included in the compose that are not already tied to a specific version.
         * 
         * @return
         *     An immutable object of type {@link Date} that may be null.
         */
        public Date getLockedDate() {
            return lockedDate;
        }

        /**
         * Whether inactive codes - codes that are not approved for current use - are in the value set. If inactive = true, 
         * inactive codes are to be included in the expansion, if inactive = false, the inactive codes will not be included in 
         * the expansion. If absent, the behavior is determined by the implementation, or by the applicable $expand parameters 
         * (but generally, inactive codes would be expected to be included).
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getInactive() {
            return inactive;
        }

        /**
         * Include one or more codes from a code system or other value set(s).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Include} that is non-empty.
         */
        public List<Include> getInclude() {
            return include;
        }

        /**
         * Exclude one or more codes from the value set based on code system filters and/or other value sets.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Include} that may be empty.
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
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(lockedDate, "lockedDate", visitor);
                    accept(inactive, "inactive", visitor);
                    accept(include, "include", visitor, Include.class);
                    accept(exclude, "exclude", visitor, ValueSet.Compose.Include.class);
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private Date lockedDate;
            private Boolean inactive;
            private List<Include> include = new ArrayList<>();
            private List<ValueSet.Compose.Include> exclude = new ArrayList<>();

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
             * Convenience method for setting {@code lockedDate}.
             * 
             * @param lockedDate
             *     Fixed date for references with no specified version (transitive)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #lockedDate(com.ibm.fhir.model.type.Date)
             */
            public Builder lockedDate(java.time.LocalDate lockedDate) {
                this.lockedDate = (lockedDate == null) ? null : Date.of(lockedDate);
                return this;
            }

            /**
             * The Locked Date is the effective date that is used to determine the version of all referenced Code Systems and Value 
             * Set Definitions included in the compose that are not already tied to a specific version.
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
             * Convenience method for setting {@code inactive}.
             * 
             * @param inactive
             *     Whether inactive codes are in the value set
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #inactive(com.ibm.fhir.model.type.Boolean)
             */
            public Builder inactive(java.lang.Boolean inactive) {
                this.inactive = (inactive == null) ? null : Boolean.of(inactive);
                return this;
            }

            /**
             * Whether inactive codes - codes that are not approved for current use - are in the value set. If inactive = true, 
             * inactive codes are to be included in the expansion, if inactive = false, the inactive codes will not be included in 
             * the expansion. If absent, the behavior is determined by the implementation, or by the applicable $expand parameters 
             * (but generally, inactive codes would be expected to be included).
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
             * Include one or more codes from a code system or other value set(s).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param include
             *     Include one or more codes from a code system or other value set(s)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder include(Include... include) {
                for (Include value : include) {
                    this.include.add(value);
                }
                return this;
            }

            /**
             * Include one or more codes from a code system or other value set(s).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param include
             *     Include one or more codes from a code system or other value set(s)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder include(Collection<Include> include) {
                this.include = new ArrayList<>(include);
                return this;
            }

            /**
             * Exclude one or more codes from the value set based on code system filters and/or other value sets.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Exclude one or more codes from the value set based on code system filters and/or other value sets.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param exclude
             *     Explicitly exclude codes from a code system or other value sets
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder exclude(Collection<ValueSet.Compose.Include> exclude) {
                this.exclude = new ArrayList<>(exclude);
                return this;
            }

            /**
             * Build the {@link Compose}
             * 
             * <p>Required elements:
             * <ul>
             * <li>include</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Compose}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Compose per the base specification
             */
            @Override
            public Compose build() {
                Compose compose = new Compose(this);
                if (validating) {
                    validate(compose);
                }
                return compose;
            }

            protected void validate(Compose compose) {
                super.validate(compose);
                ValidationSupport.checkNonEmptyList(compose.include, "include", Include.class);
                ValidationSupport.checkList(compose.exclude, "exclude", ValueSet.Compose.Include.class);
                ValidationSupport.requireValueOrChildren(compose);
            }

            protected Builder from(Compose compose) {
                super.from(compose);
                lockedDate = compose.lockedDate;
                inactive = compose.inactive;
                include.addAll(compose.include);
                exclude.addAll(compose.exclude);
                return this;
            }
        }

        /**
         * Include one or more codes from a code system or other value set(s).
         */
        public static class Include extends BackboneElement {
            @Summary
            private final Uri system;
            @Summary
            private final String version;
            private final List<Concept> concept;
            @Summary
            private final List<Filter> filter;
            @Summary
            private final List<Canonical> valueSet;

            private Include(Builder builder) {
                super(builder);
                system = builder.system;
                version = builder.version;
                concept = Collections.unmodifiableList(builder.concept);
                filter = Collections.unmodifiableList(builder.filter);
                valueSet = Collections.unmodifiableList(builder.valueSet);
            }

            /**
             * An absolute URI which is the code system from which the selected codes come from.
             * 
             * @return
             *     An immutable object of type {@link Uri} that may be null.
             */
            public Uri getSystem() {
                return system;
            }

            /**
             * The version of the code system that the codes are selected from, or the special version '*' for all versions.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getVersion() {
                return version;
            }

            /**
             * Specifies a concept to be included or excluded.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Concept} that may be empty.
             */
            public List<Concept> getConcept() {
                return concept;
            }

            /**
             * Select concepts by specify a matching criterion based on the properties (including relationships) defined by the 
             * system, or on filters defined by the system. If multiple filters are specified, they SHALL all be true.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Filter} that may be empty.
             */
            public List<Filter> getFilter() {
                return filter;
            }

            /**
             * Selects the concepts found in this value set (based on its value set definition). This is an absolute URI that is a 
             * reference to ValueSet.url. If multiple value sets are specified this includes the union of the contents of all of the 
             * referenced value sets.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
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
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
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
                private Uri system;
                private String version;
                private List<Concept> concept = new ArrayList<>();
                private List<Filter> filter = new ArrayList<>();
                private List<Canonical> valueSet = new ArrayList<>();

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
                 * An absolute URI which is the code system from which the selected codes come from.
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
                 * Convenience method for setting {@code version}.
                 * 
                 * @param version
                 *     Specific version of the code system referred to
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
                 * The version of the code system that the codes are selected from, or the special version '*' for all versions.
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
                 * Specifies a concept to be included or excluded.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * Specifies a concept to be included or excluded.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param concept
                 *     A concept defined in the system
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
                 * Select concepts by specify a matching criterion based on the properties (including relationships) defined by the 
                 * system, or on filters defined by the system. If multiple filters are specified, they SHALL all be true.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * Select concepts by specify a matching criterion based on the properties (including relationships) defined by the 
                 * system, or on filters defined by the system. If multiple filters are specified, they SHALL all be true.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param filter
                 *     Select codes/concepts by their properties (including relationships)
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
                 * Selects the concepts found in this value set (based on its value set definition). This is an absolute URI that is a 
                 * reference to ValueSet.url. If multiple value sets are specified this includes the union of the contents of all of the 
                 * referenced value sets.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * Selects the concepts found in this value set (based on its value set definition). This is an absolute URI that is a 
                 * reference to ValueSet.url. If multiple value sets are specified this includes the union of the contents of all of the 
                 * referenced value sets.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param valueSet
                 *     Select the contents included in this value set
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder valueSet(Collection<Canonical> valueSet) {
                    this.valueSet = new ArrayList<>(valueSet);
                    return this;
                }

                /**
                 * Build the {@link Include}
                 * 
                 * @return
                 *     An immutable object of type {@link Include}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Include per the base specification
                 */
                @Override
                public Include build() {
                    Include include = new Include(this);
                    if (validating) {
                        validate(include);
                    }
                    return include;
                }

                protected void validate(Include include) {
                    super.validate(include);
                    ValidationSupport.checkList(include.concept, "concept", Concept.class);
                    ValidationSupport.checkList(include.filter, "filter", Filter.class);
                    ValidationSupport.checkList(include.valueSet, "valueSet", Canonical.class);
                    ValidationSupport.requireValueOrChildren(include);
                }

                protected Builder from(Include include) {
                    super.from(include);
                    system = include.system;
                    version = include.version;
                    concept.addAll(include.concept);
                    filter.addAll(include.filter);
                    valueSet.addAll(include.valueSet);
                    return this;
                }
            }

            /**
             * Specifies a concept to be included or excluded.
             */
            public static class Concept extends BackboneElement {
                @Required
                private final Code code;
                private final String display;
                private final List<Designation> designation;

                private Concept(Builder builder) {
                    super(builder);
                    code = builder.code;
                    display = builder.display;
                    designation = Collections.unmodifiableList(builder.designation);
                }

                /**
                 * Specifies a code for the concept to be included or excluded.
                 * 
                 * @return
                 *     An immutable object of type {@link Code} that is non-null.
                 */
                public Code getCode() {
                    return code;
                }

                /**
                 * The text to display to the user for this concept in the context of this valueset. If no display is provided, then 
                 * applications using the value set use the display specified for the code by the system.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getDisplay() {
                    return display;
                }

                /**
                 * Additional representations for this concept when used in this value set - other languages, aliases, specialized 
                 * purposes, used for particular purposes, etc.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Designation} that may be empty.
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
                            accept(designation, "designation", visitor, Designation.class);
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
                    return new Builder().from(this);
                }

                public static Builder builder() {
                    return new Builder();
                }

                public static class Builder extends BackboneElement.Builder {
                    private Code code;
                    private String display;
                    private List<Designation> designation = new ArrayList<>();

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
                     * Specifies a code for the concept to be included or excluded.
                     * 
                     * <p>This element is required.
                     * 
                     * @param code
                     *     Code or expression from system
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
                     *     Text to display for this code for this value set in this valueset
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
                     * The text to display to the user for this concept in the context of this valueset. If no display is provided, then 
                     * applications using the value set use the display specified for the code by the system.
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
                     * Additional representations for this concept when used in this value set - other languages, aliases, specialized 
                     * purposes, used for particular purposes, etc.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
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
                     * Additional representations for this concept when used in this value set - other languages, aliases, specialized 
                     * purposes, used for particular purposes, etc.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param designation
                     *     Additional representations for this concept
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
                        ValidationSupport.requireValueOrChildren(concept);
                    }

                    protected Builder from(Concept concept) {
                        super.from(concept);
                        code = concept.code;
                        display = concept.display;
                        designation.addAll(concept.designation);
                        return this;
                    }
                }

                /**
                 * Additional representations for this concept when used in this value set - other languages, aliases, specialized 
                 * purposes, used for particular purposes, etc.
                 */
                public static class Designation extends BackboneElement {
                    @Binding(
                        bindingName = "Language",
                        strength = BindingStrength.Value.PREFERRED,
                        description = "A human language.",
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
                     * A code that represents types of uses of designations.
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
                         * A code that represents types of uses of designations.
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
            }

            /**
             * Select concepts by specify a matching criterion based on the properties (including relationships) defined by the 
             * system, or on filters defined by the system. If multiple filters are specified, they SHALL all be true.
             */
            public static class Filter extends BackboneElement {
                @Summary
                @Required
                private final Code property;
                @Summary
                @Binding(
                    bindingName = "FilterOperator",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The kind of operation to perform as a part of a property based filter.",
                    valueSet = "http://hl7.org/fhir/ValueSet/filter-operator|4.1.0"
                )
                @Required
                private final FilterOperator op;
                @Summary
                @Required
                private final String value;

                private Filter(Builder builder) {
                    super(builder);
                    property = builder.property;
                    op = builder.op;
                    value = builder.value;
                }

                /**
                 * A code that identifies a property or a filter defined in the code system.
                 * 
                 * @return
                 *     An immutable object of type {@link Code} that is non-null.
                 */
                public Code getProperty() {
                    return property;
                }

                /**
                 * The kind of operation to perform as a part of the filter criteria.
                 * 
                 * @return
                 *     An immutable object of type {@link FilterOperator} that is non-null.
                 */
                public FilterOperator getOp() {
                    return op;
                }

                /**
                 * The match value may be either a code defined by the system, or a string value, which is a regex match on the literal 
                 * string of the property value (if the filter represents a property defined in CodeSystem) or of the system filter value 
                 * (if the filter represents a filter defined in CodeSystem) when the operation is 'regex', or one of the values (true 
                 * and false), when the operation is 'exists'.
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
                        (property != null) || 
                        (op != null) || 
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
                            accept(property, "property", visitor);
                            accept(op, "op", visitor);
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
                    return new Builder().from(this);
                }

                public static Builder builder() {
                    return new Builder();
                }

                public static class Builder extends BackboneElement.Builder {
                    private Code property;
                    private FilterOperator op;
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
                     * A code that identifies a property or a filter defined in the code system.
                     * 
                     * <p>This element is required.
                     * 
                     * @param property
                     *     A property/filter defined by the code system
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder property(Code property) {
                        this.property = property;
                        return this;
                    }

                    /**
                     * The kind of operation to perform as a part of the filter criteria.
                     * 
                     * <p>This element is required.
                     * 
                     * @param op
                     *     = | is-a | descendent-of | is-not-a | regex | in | not-in | generalizes | exists
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder op(FilterOperator op) {
                        this.op = op;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code value}.
                     * 
                     * <p>This element is required.
                     * 
                     * @param value
                     *     Code from the system, or regex criteria, or boolean value for exists
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
                     * The match value may be either a code defined by the system, or a string value, which is a regex match on the literal 
                     * string of the property value (if the filter represents a property defined in CodeSystem) or of the system filter value 
                     * (if the filter represents a filter defined in CodeSystem) when the operation is 'regex', or one of the values (true 
                     * and false), when the operation is 'exists'.
                     * 
                     * <p>This element is required.
                     * 
                     * @param value
                     *     Code from the system, or regex criteria, or boolean value for exists
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
                     * <li>property</li>
                     * <li>op</li>
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
                        ValidationSupport.requireNonNull(filter.property, "property");
                        ValidationSupport.requireNonNull(filter.op, "op");
                        ValidationSupport.requireNonNull(filter.value, "value");
                        ValidationSupport.requireValueOrChildren(filter);
                    }

                    protected Builder from(Filter filter) {
                        super.from(filter);
                        property = filter.property;
                        op = filter.op;
                        value = filter.value;
                        return this;
                    }
                }
            }
        }
    }

    /**
     * A value set can also be "expanded", where the value set is turned into a simple collection of enumerated codes. This 
     * element holds the expansion, if it has been performed.
     */
    public static class Expansion extends BackboneElement {
        private final Uri identifier;
        @Required
        private final DateTime timestamp;
        private final Integer total;
        private final Integer offset;
        private final List<Parameter> parameter;
        private final List<Contains> contains;

        private Expansion(Builder builder) {
            super(builder);
            identifier = builder.identifier;
            timestamp = builder.timestamp;
            total = builder.total;
            offset = builder.offset;
            parameter = Collections.unmodifiableList(builder.parameter);
            contains = Collections.unmodifiableList(builder.contains);
        }

        /**
         * An identifier that uniquely identifies this expansion of the valueset, based on a unique combination of the provided 
         * parameters, the system default parameters, and the underlying system code system versions etc. Systems may re-use the 
         * same identifier as long as those factors remain the same, and the expansion is the same, but are not required to do 
         * so. This is a business identifier.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getIdentifier() {
            return identifier;
        }

        /**
         * The time at which the expansion was produced by the expanding system.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that is non-null.
         */
        public DateTime getTimestamp() {
            return timestamp;
        }

        /**
         * The total number of concepts in the expansion. If the number of concept nodes in this resource is less than the stated 
         * number, then the server can return more using the offset parameter.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getTotal() {
            return total;
        }

        /**
         * If paging is being used, the offset at which this resource starts. I.e. this resource is a partial view into the 
         * expansion. If paging is not being used, this element SHALL NOT be present.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getOffset() {
            return offset;
        }

        /**
         * A parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to 
         * check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Parameter} that may be empty.
         */
        public List<Parameter> getParameter() {
            return parameter;
        }

        /**
         * The codes that are contained in the value set expansion.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Contains} that may be empty.
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
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private Uri identifier;
            private DateTime timestamp;
            private Integer total;
            private Integer offset;
            private List<Parameter> parameter = new ArrayList<>();
            private List<Contains> contains = new ArrayList<>();

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
             * An identifier that uniquely identifies this expansion of the valueset, based on a unique combination of the provided 
             * parameters, the system default parameters, and the underlying system code system versions etc. Systems may re-use the 
             * same identifier as long as those factors remain the same, and the expansion is the same, but are not required to do 
             * so. This is a business identifier.
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
             * The time at which the expansion was produced by the expanding system.
             * 
             * <p>This element is required.
             * 
             * @param timestamp
             *     Time ValueSet expansion happened
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder timestamp(DateTime timestamp) {
                this.timestamp = timestamp;
                return this;
            }

            /**
             * Convenience method for setting {@code total}.
             * 
             * @param total
             *     Total number of codes in the expansion
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #total(com.ibm.fhir.model.type.Integer)
             */
            public Builder total(java.lang.Integer total) {
                this.total = (total == null) ? null : Integer.of(total);
                return this;
            }

            /**
             * The total number of concepts in the expansion. If the number of concept nodes in this resource is less than the stated 
             * number, then the server can return more using the offset parameter.
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
             * Convenience method for setting {@code offset}.
             * 
             * @param offset
             *     Offset at which this resource starts
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #offset(com.ibm.fhir.model.type.Integer)
             */
            public Builder offset(java.lang.Integer offset) {
                this.offset = (offset == null) ? null : Integer.of(offset);
                return this;
            }

            /**
             * If paging is being used, the offset at which this resource starts. I.e. this resource is a partial view into the 
             * expansion. If paging is not being used, this element SHALL NOT be present.
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
             * A parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to 
             * check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * A parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to 
             * check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param parameter
             *     Parameter that controlled the expansion process
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
             * The codes that are contained in the value set expansion.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * The codes that are contained in the value set expansion.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param contains
             *     Codes in the value set
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder contains(Collection<Contains> contains) {
                this.contains = new ArrayList<>(contains);
                return this;
            }

            /**
             * Build the {@link Expansion}
             * 
             * <p>Required elements:
             * <ul>
             * <li>timestamp</li>
             * </ul>
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
                ValidationSupport.requireNonNull(expansion.timestamp, "timestamp");
                ValidationSupport.checkList(expansion.parameter, "parameter", Parameter.class);
                ValidationSupport.checkList(expansion.contains, "contains", Contains.class);
                ValidationSupport.requireValueOrChildren(expansion);
            }

            protected Builder from(Expansion expansion) {
                super.from(expansion);
                identifier = expansion.identifier;
                timestamp = expansion.timestamp;
                total = expansion.total;
                offset = expansion.offset;
                parameter.addAll(expansion.parameter);
                contains.addAll(expansion.contains);
                return this;
            }
        }

        /**
         * A parameter that controlled the expansion process. These parameters may be used by users of expanded value sets to 
         * check whether the expansion is suitable for a particular purpose, or to pick the correct expansion.
         */
        public static class Parameter extends BackboneElement {
            @Required
            private final String name;
            @Choice({ String.class, Boolean.class, Integer.class, Decimal.class, Uri.class, Code.class, DateTime.class })
            private final Element value;

            private Parameter(Builder builder) {
                super(builder);
                name = builder.name;
                value = builder.value;
            }

            /**
             * Name of the input parameter to the $expand operation; may be a server-assigned name for additional default or other 
             * server-supplied parameters used to control the expansion process.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getName() {
                return name;
            }

            /**
             * The value of the parameter.
             * 
             * @return
             *     An immutable object of type {@link String}, {@link Boolean}, {@link Integer}, {@link Decimal}, {@link Uri}, {@link 
             *     Code} or {@link DateTime} that may be null.
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
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(name, "name", visitor);
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
                return new Builder().from(this);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder extends BackboneElement.Builder {
                private String name;
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
                 * Convenience method for setting {@code name}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param name
                 *     Name as assigned by the client or server
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
                 * Name of the input parameter to the $expand operation; may be a server-assigned name for additional default or other 
                 * server-supplied parameters used to control the expansion process.
                 * 
                 * <p>This element is required.
                 * 
                 * @param name
                 *     Name as assigned by the client or server
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder name(String name) {
                    this.name = name;
                    return this;
                }

                /**
                 * Convenience method for setting {@code value} with choice type String.
                 * 
                 * @param value
                 *     Value of the named parameter
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
                 * Convenience method for setting {@code value} with choice type Boolean.
                 * 
                 * @param value
                 *     Value of the named parameter
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
                 * Convenience method for setting {@code value} with choice type Integer.
                 * 
                 * @param value
                 *     Value of the named parameter
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
                 * The value of the parameter.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link String}</li>
                 * <li>{@link Boolean}</li>
                 * <li>{@link Integer}</li>
                 * <li>{@link Decimal}</li>
                 * <li>{@link Uri}</li>
                 * <li>{@link Code}</li>
                 * <li>{@link DateTime}</li>
                 * </ul>
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
                    ValidationSupport.choiceElement(parameter.value, "value", String.class, Boolean.class, Integer.class, Decimal.class, Uri.class, Code.class, DateTime.class);
                    ValidationSupport.requireValueOrChildren(parameter);
                }

                protected Builder from(Parameter parameter) {
                    super.from(parameter);
                    name = parameter.name;
                    value = parameter.value;
                    return this;
                }
            }
        }

        /**
         * The codes that are contained in the value set expansion.
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
                system = builder.system;
                _abstract = builder._abstract;
                inactive = builder.inactive;
                version = builder.version;
                code = builder.code;
                display = builder.display;
                designation = Collections.unmodifiableList(builder.designation);
                contains = Collections.unmodifiableList(builder.contains);
            }

            /**
             * An absolute URI which is the code system in which the code for this item in the expansion is defined.
             * 
             * @return
             *     An immutable object of type {@link Uri} that may be null.
             */
            public Uri getSystem() {
                return system;
            }

            /**
             * If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code 
             * directly as a proper value.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getAbstract() {
                return _abstract;
            }

            /**
             * If the concept is inactive in the code system that defines it. Inactive codes are those that are no longer to be used, 
             * but are maintained by the code system for understanding legacy data. It might not be known or specified whether an 
             * concept is inactive (and it may depend on the context of use).
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getInactive() {
                return inactive;
            }

            /**
             * The version of the code system from this code was taken. Note that a well-maintained code system does not need the 
             * version reported, because the meaning of codes is consistent across versions. However this cannot consistently be 
             * assured, and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getVersion() {
                return version;
            }

            /**
             * The code for this item in the expansion hierarchy. If this code is missing the entry in the hierarchy is a place 
             * holder (abstract) and does not represent a valid code in the value set.
             * 
             * @return
             *     An immutable object of type {@link Code} that may be null.
             */
            public Code getCode() {
                return code;
            }

            /**
             * The recommended display for this item in the expansion.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDisplay() {
                return display;
            }

            /**
             * Additional representations for this item - other languages, aliases, specialized purposes, used for particular 
             * purposes, etc. These are relevant when the conditions of the expansion do not fix to a single correct representation.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Designation} that may be empty.
             */
            public List<ValueSet.Compose.Include.Concept.Designation> getDesignation() {
                return designation;
            }

            /**
             * Other codes and entries contained under this entry in the hierarchy.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Contains} that may be empty.
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
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
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
                 * An absolute URI which is the code system in which the code for this item in the expansion is defined.
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
                 * Convenience method for setting {@code _abstract}.
                 * 
                 * @param _abstract
                 *     If user cannot select this entry
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
                 * If true, this entry is included in the expansion for navigational purposes, and the user cannot select the code 
                 * directly as a proper value.
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
                 * Convenience method for setting {@code inactive}.
                 * 
                 * @param inactive
                 *     If concept is inactive in the code system
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #inactive(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder inactive(java.lang.Boolean inactive) {
                    this.inactive = (inactive == null) ? null : Boolean.of(inactive);
                    return this;
                }

                /**
                 * If the concept is inactive in the code system that defines it. Inactive codes are those that are no longer to be used, 
                 * but are maintained by the code system for understanding legacy data. It might not be known or specified whether an 
                 * concept is inactive (and it may depend on the context of use).
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
                 * Convenience method for setting {@code version}.
                 * 
                 * @param version
                 *     Version in which this code/display is defined
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
                 * The version of the code system from this code was taken. Note that a well-maintained code system does not need the 
                 * version reported, because the meaning of codes is consistent across versions. However this cannot consistently be 
                 * assured, and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.
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
                 * The code for this item in the expansion hierarchy. If this code is missing the entry in the hierarchy is a place 
                 * holder (abstract) and does not represent a valid code in the value set.
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
                 * Convenience method for setting {@code display}.
                 * 
                 * @param display
                 *     User display for the concept
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
                 * The recommended display for this item in the expansion.
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
                 * Additional representations for this item - other languages, aliases, specialized purposes, used for particular 
                 * purposes, etc. These are relevant when the conditions of the expansion do not fix to a single correct representation.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * Additional representations for this item - other languages, aliases, specialized purposes, used for particular 
                 * purposes, etc. These are relevant when the conditions of the expansion do not fix to a single correct representation.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param designation
                 *     Additional representations for this item
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder designation(Collection<ValueSet.Compose.Include.Concept.Designation> designation) {
                    this.designation = new ArrayList<>(designation);
                    return this;
                }

                /**
                 * Other codes and entries contained under this entry in the hierarchy.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * Other codes and entries contained under this entry in the hierarchy.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param contains
                 *     Codes contained under this entry
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder contains(Collection<ValueSet.Expansion.Contains> contains) {
                    this.contains = new ArrayList<>(contains);
                    return this;
                }

                /**
                 * Build the {@link Contains}
                 * 
                 * @return
                 *     An immutable object of type {@link Contains}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Contains per the base specification
                 */
                @Override
                public Contains build() {
                    Contains contains = new Contains(this);
                    if (validating) {
                        validate(contains);
                    }
                    return contains;
                }

                protected void validate(Contains contains) {
                    super.validate(contains);
                    ValidationSupport.checkList(contains.designation, "designation", ValueSet.Compose.Include.Concept.Designation.class);
                    ValidationSupport.checkList(contains.contains, "contains", ValueSet.Expansion.Contains.class);
                    ValidationSupport.requireValueOrChildren(contains);
                }

                protected Builder from(Contains contains) {
                    super.from(contains);
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
