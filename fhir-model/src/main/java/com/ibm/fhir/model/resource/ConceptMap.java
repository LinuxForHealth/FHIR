/*
 * (C) Copyright IBM Corp. 2019, 2022
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
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ConceptMapEquivalence;
import com.ibm.fhir.model.type.code.ConceptMapGroupUnmappedMode;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A statement of relationships from one set of concepts to one or more other concepts - either concepts in code systems, 
 * or data element/data element concepts, or classes in class models.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "cmd-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/ConceptMap"
)
@Constraint(
    id = "cmd-1",
    level = "Rule",
    location = "ConceptMap.group.element.target",
    description = "If the map is narrower or inexact, there SHALL be some comments",
    expression = "comment.exists() or equivalence.empty() or ((equivalence != 'narrower') and (equivalence != 'inexact'))",
    source = "http://hl7.org/fhir/StructureDefinition/ConceptMap"
)
@Constraint(
    id = "cmd-2",
    level = "Rule",
    location = "ConceptMap.group.unmapped",
    description = "If the mode is 'fixed', a code must be provided",
    expression = "(mode = 'fixed') implies code.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/ConceptMap"
)
@Constraint(
    id = "cmd-3",
    level = "Rule",
    location = "ConceptMap.group.unmapped",
    description = "If the mode is 'other-map', a url must be provided",
    expression = "(mode = 'other-map') implies url.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/ConceptMap"
)
@Constraint(
    id = "conceptMap-4",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/ConceptMap",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ConceptMap extends DomainResource {
    @Summary
    private final Uri url;
    @Summary
    private final Identifier identifier;
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
    @Choice({ Uri.class, Canonical.class })
    private final Element source;
    @Summary
    @Choice({ Uri.class, Canonical.class })
    private final Element target;
    private final List<Group> group;

    private ConceptMap(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = builder.identifier;
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
        source = builder.source;
        target = builder.target;
        group = Collections.unmodifiableList(builder.group);
    }

    /**
     * An absolute URI that is used to identify this concept map when it is referenced in a specification, model, design or 
     * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this concept map is (or will be) published. This URL can be the target of 
     * a canonical reference. It SHALL remain the same when the concept map is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this concept map when it is represented in other formats, or referenced 
     * in a specification, model, design or an instance.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the concept map when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the concept map author and is not expected to be 
     * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
     * also no expectation that versions can be placed in a lexicographical sequence.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the concept map. This name should be usable as an identifier for the module by 
     * machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the concept map.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The status of this concept map. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this concept map is authored for testing purposes (or education/evaluation/marketing) 
     * and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the concept map was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the concept map changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the concept map.
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
     * A free text natural language description of the concept map from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate concept map instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the concept map is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this concept map is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A copyright statement relating to the concept map and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the concept map.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * Identifier for the source value set that contains the concepts that are being mapped and provides context for the 
     * mappings.
     * 
     * @return
     *     An immutable object of type {@link Uri} or {@link Canonical} that may be null.
     */
    public Element getSource() {
        return source;
    }

    /**
     * The target value set provides context for the mappings. Note that the mapping is made between concepts, not between 
     * value sets, but the value set provides important context about how the concept mapping choices are made.
     * 
     * @return
     *     An immutable object of type {@link Uri} or {@link Canonical} that may be null.
     */
    public Element getTarget() {
        return target;
    }

    /**
     * A group of mappings that all have the same source and target system.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Group} that may be empty.
     */
    public List<Group> getGroup() {
        return group;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            (identifier != null) || 
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
            (source != null) || 
            (target != null) || 
            !group.isEmpty();
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
                accept(identifier, "identifier", visitor);
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
                accept(source, "source", visitor);
                accept(target, "target", visitor);
                accept(group, "group", visitor, Group.class);
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
        ConceptMap other = (ConceptMap) obj;
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
            Objects.equals(source, other.source) && 
            Objects.equals(target, other.target) && 
            Objects.equals(group, other.group);
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
                source, 
                target, 
                group);
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
        private Identifier identifier;
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
        private Element source;
        private Element target;
        private List<Group> group = new ArrayList<>();

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
         * An absolute URI that is used to identify this concept map when it is referenced in a specification, model, design or 
         * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
         * which at which an authoritative instance of this concept map is (or will be) published. This URL can be the target of 
         * a canonical reference. It SHALL remain the same when the concept map is stored on different servers.
         * 
         * @param url
         *     Canonical identifier for this concept map, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this concept map when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * 
         * @param identifier
         *     Additional identifier for the concept map
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * Convenience method for setting {@code version}.
         * 
         * @param version
         *     Business version of the concept map
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
         * The identifier that is used to identify this version of the concept map when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the concept map author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of the concept map
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
         *     Name for this concept map (computer friendly)
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
         * A natural language name identifying the concept map. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
         * 
         * @param name
         *     Name for this concept map (computer friendly)
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
         *     Name for this concept map (human friendly)
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
         * A short, descriptive, user-friendly title for the concept map.
         * 
         * @param title
         *     Name for this concept map (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The status of this concept map. Enables tracking the life-cycle of the content.
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
         * A Boolean value to indicate that this concept map is authored for testing purposes (or education/evaluation/marketing) 
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
         * The date (and optionally time) when the concept map was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the concept map changes.
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
         * The name of the organization or individual that published the concept map.
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
         * A free text natural language description of the concept map from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the concept map
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
         * may be used to assist with indexing and searching for appropriate concept map instances.
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
         * may be used to assist with indexing and searching for appropriate concept map instances.
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
         * A legal or geographic region in which the concept map is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for concept map (if applicable)
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
         * A legal or geographic region in which the concept map is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for concept map (if applicable)
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
         * Explanation of why this concept map is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this concept map is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * A copyright statement relating to the concept map and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the concept map.
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
         * Identifier for the source value set that contains the concepts that are being mapped and provides context for the 
         * mappings.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Uri}</li>
         * <li>{@link Canonical}</li>
         * </ul>
         * 
         * @param source
         *     The source value set that contains the concepts that are being mapped
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Element source) {
            this.source = source;
            return this;
        }

        /**
         * The target value set provides context for the mappings. Note that the mapping is made between concepts, not between 
         * value sets, but the value set provides important context about how the concept mapping choices are made.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Uri}</li>
         * <li>{@link Canonical}</li>
         * </ul>
         * 
         * @param target
         *     The target value set which provides context for the mappings
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder target(Element target) {
            this.target = target;
            return this;
        }

        /**
         * A group of mappings that all have the same source and target system.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param group
         *     Same source and target systems
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder group(Group... group) {
            for (Group value : group) {
                this.group.add(value);
            }
            return this;
        }

        /**
         * A group of mappings that all have the same source and target system.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param group
         *     Same source and target systems
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder group(Collection<Group> group) {
            this.group = new ArrayList<>(group);
            return this;
        }

        /**
         * Build the {@link ConceptMap}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ConceptMap}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ConceptMap per the base specification
         */
        @Override
        public ConceptMap build() {
            ConceptMap conceptMap = new ConceptMap(this);
            if (validating) {
                validate(conceptMap);
            }
            return conceptMap;
        }

        protected void validate(ConceptMap conceptMap) {
            super.validate(conceptMap);
            ValidationSupport.requireNonNull(conceptMap.status, "status");
            ValidationSupport.checkList(conceptMap.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(conceptMap.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(conceptMap.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.choiceElement(conceptMap.source, "source", Uri.class, Canonical.class);
            ValidationSupport.choiceElement(conceptMap.target, "target", Uri.class, Canonical.class);
            ValidationSupport.checkList(conceptMap.group, "group", Group.class);
        }

        protected Builder from(ConceptMap conceptMap) {
            super.from(conceptMap);
            url = conceptMap.url;
            identifier = conceptMap.identifier;
            version = conceptMap.version;
            name = conceptMap.name;
            title = conceptMap.title;
            status = conceptMap.status;
            experimental = conceptMap.experimental;
            date = conceptMap.date;
            publisher = conceptMap.publisher;
            contact.addAll(conceptMap.contact);
            description = conceptMap.description;
            useContext.addAll(conceptMap.useContext);
            jurisdiction.addAll(conceptMap.jurisdiction);
            purpose = conceptMap.purpose;
            copyright = conceptMap.copyright;
            source = conceptMap.source;
            target = conceptMap.target;
            group.addAll(conceptMap.group);
            return this;
        }
    }

    /**
     * A group of mappings that all have the same source and target system.
     */
    public static class Group extends BackboneElement {
        private final Uri source;
        private final String sourceVersion;
        private final Uri target;
        private final String targetVersion;
        @Required
        private final List<Element> element;
        private final Unmapped unmapped;

        private Group(Builder builder) {
            super(builder);
            source = builder.source;
            sourceVersion = builder.sourceVersion;
            target = builder.target;
            targetVersion = builder.targetVersion;
            element = Collections.unmodifiableList(builder.element);
            unmapped = builder.unmapped;
        }

        /**
         * An absolute URI that identifies the source system where the concepts to be mapped are defined.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getSource() {
            return source;
        }

        /**
         * The specific version of the code system, as determined by the code system authority.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getSourceVersion() {
            return sourceVersion;
        }

        /**
         * An absolute URI that identifies the target system that the concepts will be mapped to.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getTarget() {
            return target;
        }

        /**
         * The specific version of the code system, as determined by the code system authority.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getTargetVersion() {
            return targetVersion;
        }

        /**
         * Mappings for an individual concept in the source to one or more concepts in the target.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Element} that is non-empty.
         */
        public List<Element> getElement() {
            return element;
        }

        /**
         * What to do when there is no mapping for the source concept. "Unmapped" does not include codes that are unmatched, and 
         * the unmapped element is ignored in a code is specified to have equivalence = unmatched.
         * 
         * @return
         *     An immutable object of type {@link Unmapped} that may be null.
         */
        public Unmapped getUnmapped() {
            return unmapped;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (source != null) || 
                (sourceVersion != null) || 
                (target != null) || 
                (targetVersion != null) || 
                !element.isEmpty() || 
                (unmapped != null);
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
                    accept(sourceVersion, "sourceVersion", visitor);
                    accept(target, "target", visitor);
                    accept(targetVersion, "targetVersion", visitor);
                    accept(element, "element", visitor, Element.class);
                    accept(unmapped, "unmapped", visitor);
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
            Group other = (Group) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(source, other.source) && 
                Objects.equals(sourceVersion, other.sourceVersion) && 
                Objects.equals(target, other.target) && 
                Objects.equals(targetVersion, other.targetVersion) && 
                Objects.equals(element, other.element) && 
                Objects.equals(unmapped, other.unmapped);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    source, 
                    sourceVersion, 
                    target, 
                    targetVersion, 
                    element, 
                    unmapped);
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
            private Uri source;
            private String sourceVersion;
            private Uri target;
            private String targetVersion;
            private List<Element> element = new ArrayList<>();
            private Unmapped unmapped;

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
             * An absolute URI that identifies the source system where the concepts to be mapped are defined.
             * 
             * @param source
             *     Source system where concepts to be mapped are defined
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Uri source) {
                this.source = source;
                return this;
            }

            /**
             * Convenience method for setting {@code sourceVersion}.
             * 
             * @param sourceVersion
             *     Specific version of the code system
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #sourceVersion(com.ibm.fhir.model.type.String)
             */
            public Builder sourceVersion(java.lang.String sourceVersion) {
                this.sourceVersion = (sourceVersion == null) ? null : String.of(sourceVersion);
                return this;
            }

            /**
             * The specific version of the code system, as determined by the code system authority.
             * 
             * @param sourceVersion
             *     Specific version of the code system
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sourceVersion(String sourceVersion) {
                this.sourceVersion = sourceVersion;
                return this;
            }

            /**
             * An absolute URI that identifies the target system that the concepts will be mapped to.
             * 
             * @param target
             *     Target system that the concepts are to be mapped to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder target(Uri target) {
                this.target = target;
                return this;
            }

            /**
             * Convenience method for setting {@code targetVersion}.
             * 
             * @param targetVersion
             *     Specific version of the code system
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #targetVersion(com.ibm.fhir.model.type.String)
             */
            public Builder targetVersion(java.lang.String targetVersion) {
                this.targetVersion = (targetVersion == null) ? null : String.of(targetVersion);
                return this;
            }

            /**
             * The specific version of the code system, as determined by the code system authority.
             * 
             * @param targetVersion
             *     Specific version of the code system
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder targetVersion(String targetVersion) {
                this.targetVersion = targetVersion;
                return this;
            }

            /**
             * Mappings for an individual concept in the source to one or more concepts in the target.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param element
             *     Mappings for a concept from the source set
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder element(Element... element) {
                for (Element value : element) {
                    this.element.add(value);
                }
                return this;
            }

            /**
             * Mappings for an individual concept in the source to one or more concepts in the target.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param element
             *     Mappings for a concept from the source set
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder element(Collection<Element> element) {
                this.element = new ArrayList<>(element);
                return this;
            }

            /**
             * What to do when there is no mapping for the source concept. "Unmapped" does not include codes that are unmatched, and 
             * the unmapped element is ignored in a code is specified to have equivalence = unmatched.
             * 
             * @param unmapped
             *     What to do when there is no mapping for the source concept
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder unmapped(Unmapped unmapped) {
                this.unmapped = unmapped;
                return this;
            }

            /**
             * Build the {@link Group}
             * 
             * <p>Required elements:
             * <ul>
             * <li>element</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Group}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Group per the base specification
             */
            @Override
            public Group build() {
                Group group = new Group(this);
                if (validating) {
                    validate(group);
                }
                return group;
            }

            protected void validate(Group group) {
                super.validate(group);
                ValidationSupport.checkNonEmptyList(group.element, "element", Element.class);
                ValidationSupport.requireValueOrChildren(group);
            }

            protected Builder from(Group group) {
                super.from(group);
                source = group.source;
                sourceVersion = group.sourceVersion;
                target = group.target;
                targetVersion = group.targetVersion;
                element.addAll(group.element);
                unmapped = group.unmapped;
                return this;
            }
        }

        /**
         * Mappings for an individual concept in the source to one or more concepts in the target.
         */
        public static class Element extends BackboneElement {
            private final Code code;
            private final String display;
            private final List<Target> target;

            private Element(Builder builder) {
                super(builder);
                code = builder.code;
                display = builder.display;
                target = Collections.unmodifiableList(builder.target);
            }

            /**
             * Identity (code or path) or the element/item being mapped.
             * 
             * @return
             *     An immutable object of type {@link Code} that may be null.
             */
            public Code getCode() {
                return code;
            }

            /**
             * The display for the code. The display is only provided to help editors when editing the concept map.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDisplay() {
                return display;
            }

            /**
             * A concept from the target value set that this concept maps to.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Target} that may be empty.
             */
            public List<Target> getTarget() {
                return target;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (code != null) || 
                    (display != null) || 
                    !target.isEmpty();
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
                        accept(target, "target", visitor, Target.class);
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
                Element other = (Element) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(display, other.display) && 
                    Objects.equals(target, other.target);
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
                        target);
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
                private List<Target> target = new ArrayList<>();

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
                 * Identity (code or path) or the element/item being mapped.
                 * 
                 * @param code
                 *     Identifies element being mapped
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
                 *     Display for the code
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
                 * The display for the code. The display is only provided to help editors when editing the concept map.
                 * 
                 * @param display
                 *     Display for the code
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder display(String display) {
                    this.display = display;
                    return this;
                }

                /**
                 * A concept from the target value set that this concept maps to.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param target
                 *     Concept in target system for element
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder target(Target... target) {
                    for (Target value : target) {
                        this.target.add(value);
                    }
                    return this;
                }

                /**
                 * A concept from the target value set that this concept maps to.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param target
                 *     Concept in target system for element
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder target(Collection<Target> target) {
                    this.target = new ArrayList<>(target);
                    return this;
                }

                /**
                 * Build the {@link Element}
                 * 
                 * @return
                 *     An immutable object of type {@link Element}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Element per the base specification
                 */
                @Override
                public Element build() {
                    Element element = new Element(this);
                    if (validating) {
                        validate(element);
                    }
                    return element;
                }

                protected void validate(Element element) {
                    super.validate(element);
                    ValidationSupport.checkList(element.target, "target", Target.class);
                    ValidationSupport.requireValueOrChildren(element);
                }

                protected Builder from(Element element) {
                    super.from(element);
                    code = element.code;
                    display = element.display;
                    target.addAll(element.target);
                    return this;
                }
            }

            /**
             * A concept from the target value set that this concept maps to.
             */
            public static class Target extends BackboneElement {
                private final Code code;
                private final String display;
                @Binding(
                    bindingName = "ConceptMapEquivalence",
                    strength = BindingStrength.Value.REQUIRED,
                    description = "The degree of equivalence between concepts.",
                    valueSet = "http://hl7.org/fhir/ValueSet/concept-map-equivalence|4.3.0-CIBUILD"
                )
                @Required
                private final ConceptMapEquivalence equivalence;
                private final String comment;
                private final List<DependsOn> dependsOn;
                private final List<ConceptMap.Group.Element.Target.DependsOn> product;

                private Target(Builder builder) {
                    super(builder);
                    code = builder.code;
                    display = builder.display;
                    equivalence = builder.equivalence;
                    comment = builder.comment;
                    dependsOn = Collections.unmodifiableList(builder.dependsOn);
                    product = Collections.unmodifiableList(builder.product);
                }

                /**
                 * Identity (code or path) or the element/item that the map refers to.
                 * 
                 * @return
                 *     An immutable object of type {@link Code} that may be null.
                 */
                public Code getCode() {
                    return code;
                }

                /**
                 * The display for the code. The display is only provided to help editors when editing the concept map.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getDisplay() {
                    return display;
                }

                /**
                 * The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence 
                 * is read from target to source (e.g. the target is 'wider' than the source).
                 * 
                 * @return
                 *     An immutable object of type {@link ConceptMapEquivalence} that is non-null.
                 */
                public ConceptMapEquivalence getEquivalence() {
                    return equivalence;
                }

                /**
                 * A description of status/issues in mapping that conveys additional information not represented in the structured data.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getComment() {
                    return comment;
                }

                /**
                 * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element 
                 * can be resolved, and it has the specified value.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link DependsOn} that may be empty.
                 */
                public List<DependsOn> getDependsOn() {
                    return dependsOn;
                }

                /**
                 * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified 
                 * element must be mapped to some data element or source that is in context. The mapping may still be useful without a 
                 * place for the additional data elements, but the equivalence cannot be relied on.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link DependsOn} that may be empty.
                 */
                public List<ConceptMap.Group.Element.Target.DependsOn> getProduct() {
                    return product;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (code != null) || 
                        (display != null) || 
                        (equivalence != null) || 
                        (comment != null) || 
                        !dependsOn.isEmpty() || 
                        !product.isEmpty();
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
                            accept(equivalence, "equivalence", visitor);
                            accept(comment, "comment", visitor);
                            accept(dependsOn, "dependsOn", visitor, DependsOn.class);
                            accept(product, "product", visitor, ConceptMap.Group.Element.Target.DependsOn.class);
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
                    Target other = (Target) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(code, other.code) && 
                        Objects.equals(display, other.display) && 
                        Objects.equals(equivalence, other.equivalence) && 
                        Objects.equals(comment, other.comment) && 
                        Objects.equals(dependsOn, other.dependsOn) && 
                        Objects.equals(product, other.product);
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
                            equivalence, 
                            comment, 
                            dependsOn, 
                            product);
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
                    private ConceptMapEquivalence equivalence;
                    private String comment;
                    private List<DependsOn> dependsOn = new ArrayList<>();
                    private List<ConceptMap.Group.Element.Target.DependsOn> product = new ArrayList<>();

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
                     * Identity (code or path) or the element/item that the map refers to.
                     * 
                     * @param code
                     *     Code that identifies the target element
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
                     *     Display for the code
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
                     * The display for the code. The display is only provided to help editors when editing the concept map.
                     * 
                     * @param display
                     *     Display for the code
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder display(String display) {
                        this.display = display;
                        return this;
                    }

                    /**
                     * The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence 
                     * is read from target to source (e.g. the target is 'wider' than the source).
                     * 
                     * <p>This element is required.
                     * 
                     * @param equivalence
                     *     relatedto | equivalent | equal | wider | subsumes | narrower | specializes | inexact | unmatched | disjoint
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder equivalence(ConceptMapEquivalence equivalence) {
                        this.equivalence = equivalence;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code comment}.
                     * 
                     * @param comment
                     *     Description of status/issues in mapping
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
                     * A description of status/issues in mapping that conveys additional information not represented in the structured data.
                     * 
                     * @param comment
                     *     Description of status/issues in mapping
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder comment(String comment) {
                        this.comment = comment;
                        return this;
                    }

                    /**
                     * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element 
                     * can be resolved, and it has the specified value.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param dependsOn
                     *     Other elements required for this mapping (from context)
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder dependsOn(DependsOn... dependsOn) {
                        for (DependsOn value : dependsOn) {
                            this.dependsOn.add(value);
                        }
                        return this;
                    }

                    /**
                     * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element 
                     * can be resolved, and it has the specified value.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param dependsOn
                     *     Other elements required for this mapping (from context)
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder dependsOn(Collection<DependsOn> dependsOn) {
                        this.dependsOn = new ArrayList<>(dependsOn);
                        return this;
                    }

                    /**
                     * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified 
                     * element must be mapped to some data element or source that is in context. The mapping may still be useful without a 
                     * place for the additional data elements, but the equivalence cannot be relied on.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param product
                     *     Other concepts that this mapping also produces
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder product(ConceptMap.Group.Element.Target.DependsOn... product) {
                        for (ConceptMap.Group.Element.Target.DependsOn value : product) {
                            this.product.add(value);
                        }
                        return this;
                    }

                    /**
                     * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified 
                     * element must be mapped to some data element or source that is in context. The mapping may still be useful without a 
                     * place for the additional data elements, but the equivalence cannot be relied on.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param product
                     *     Other concepts that this mapping also produces
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder product(Collection<ConceptMap.Group.Element.Target.DependsOn> product) {
                        this.product = new ArrayList<>(product);
                        return this;
                    }

                    /**
                     * Build the {@link Target}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>equivalence</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Target}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Target per the base specification
                     */
                    @Override
                    public Target build() {
                        Target target = new Target(this);
                        if (validating) {
                            validate(target);
                        }
                        return target;
                    }

                    protected void validate(Target target) {
                        super.validate(target);
                        ValidationSupport.requireNonNull(target.equivalence, "equivalence");
                        ValidationSupport.checkList(target.dependsOn, "dependsOn", DependsOn.class);
                        ValidationSupport.checkList(target.product, "product", ConceptMap.Group.Element.Target.DependsOn.class);
                        ValidationSupport.requireValueOrChildren(target);
                    }

                    protected Builder from(Target target) {
                        super.from(target);
                        code = target.code;
                        display = target.display;
                        equivalence = target.equivalence;
                        comment = target.comment;
                        dependsOn.addAll(target.dependsOn);
                        product.addAll(target.product);
                        return this;
                    }
                }

                /**
                 * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element 
                 * can be resolved, and it has the specified value.
                 */
                public static class DependsOn extends BackboneElement {
                    @Required
                    private final Uri property;
                    private final Canonical system;
                    @Required
                    private final String value;
                    private final String display;

                    private DependsOn(Builder builder) {
                        super(builder);
                        property = builder.property;
                        system = builder.system;
                        value = builder.value;
                        display = builder.display;
                    }

                    /**
                     * A reference to an element that holds a coded value that corresponds to a code system property. The idea is that the 
                     * information model carries an element somewhere that is labeled to correspond with a code system property.
                     * 
                     * @return
                     *     An immutable object of type {@link Uri} that is non-null.
                     */
                    public Uri getProperty() {
                        return property;
                    }

                    /**
                     * An absolute URI that identifies the code system of the dependency code (if the source/dependency is a value set that 
                     * crosses code systems).
                     * 
                     * @return
                     *     An immutable object of type {@link Canonical} that may be null.
                     */
                    public Canonical getSystem() {
                        return system;
                    }

                    /**
                     * Identity (code or path) or the element/item/ValueSet/text that the map depends on / refers to.
                     * 
                     * @return
                     *     An immutable object of type {@link String} that is non-null.
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * The display for the code. The display is only provided to help editors when editing the concept map.
                     * 
                     * @return
                     *     An immutable object of type {@link String} that may be null.
                     */
                    public String getDisplay() {
                        return display;
                    }

                    @Override
                    public boolean hasChildren() {
                        return super.hasChildren() || 
                            (property != null) || 
                            (system != null) || 
                            (value != null) || 
                            (display != null);
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
                                accept(system, "system", visitor);
                                accept(value, "value", visitor);
                                accept(display, "display", visitor);
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
                        DependsOn other = (DependsOn) obj;
                        return Objects.equals(id, other.id) && 
                            Objects.equals(extension, other.extension) && 
                            Objects.equals(modifierExtension, other.modifierExtension) && 
                            Objects.equals(property, other.property) && 
                            Objects.equals(system, other.system) && 
                            Objects.equals(value, other.value) && 
                            Objects.equals(display, other.display);
                    }

                    @Override
                    public int hashCode() {
                        int result = hashCode;
                        if (result == 0) {
                            result = Objects.hash(id, 
                                extension, 
                                modifierExtension, 
                                property, 
                                system, 
                                value, 
                                display);
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
                        private Uri property;
                        private Canonical system;
                        private String value;
                        private String display;

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
                         * A reference to an element that holds a coded value that corresponds to a code system property. The idea is that the 
                         * information model carries an element somewhere that is labeled to correspond with a code system property.
                         * 
                         * <p>This element is required.
                         * 
                         * @param property
                         *     Reference to property mapping depends on
                         * 
                         * @return
                         *     A reference to this Builder instance
                         */
                        public Builder property(Uri property) {
                            this.property = property;
                            return this;
                        }

                        /**
                         * An absolute URI that identifies the code system of the dependency code (if the source/dependency is a value set that 
                         * crosses code systems).
                         * 
                         * @param system
                         *     Code System (if necessary)
                         * 
                         * @return
                         *     A reference to this Builder instance
                         */
                        public Builder system(Canonical system) {
                            this.system = system;
                            return this;
                        }

                        /**
                         * Convenience method for setting {@code value}.
                         * 
                         * <p>This element is required.
                         * 
                         * @param value
                         *     Value of the referenced element
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
                         * Identity (code or path) or the element/item/ValueSet/text that the map depends on / refers to.
                         * 
                         * <p>This element is required.
                         * 
                         * @param value
                         *     Value of the referenced element
                         * 
                         * @return
                         *     A reference to this Builder instance
                         */
                        public Builder value(String value) {
                            this.value = value;
                            return this;
                        }

                        /**
                         * Convenience method for setting {@code display}.
                         * 
                         * @param display
                         *     Display for the code (if value is a code)
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
                         * The display for the code. The display is only provided to help editors when editing the concept map.
                         * 
                         * @param display
                         *     Display for the code (if value is a code)
                         * 
                         * @return
                         *     A reference to this Builder instance
                         */
                        public Builder display(String display) {
                            this.display = display;
                            return this;
                        }

                        /**
                         * Build the {@link DependsOn}
                         * 
                         * <p>Required elements:
                         * <ul>
                         * <li>property</li>
                         * <li>value</li>
                         * </ul>
                         * 
                         * @return
                         *     An immutable object of type {@link DependsOn}
                         * @throws IllegalStateException
                         *     if the current state cannot be built into a valid DependsOn per the base specification
                         */
                        @Override
                        public DependsOn build() {
                            DependsOn dependsOn = new DependsOn(this);
                            if (validating) {
                                validate(dependsOn);
                            }
                            return dependsOn;
                        }

                        protected void validate(DependsOn dependsOn) {
                            super.validate(dependsOn);
                            ValidationSupport.requireNonNull(dependsOn.property, "property");
                            ValidationSupport.requireNonNull(dependsOn.value, "value");
                            ValidationSupport.requireValueOrChildren(dependsOn);
                        }

                        protected Builder from(DependsOn dependsOn) {
                            super.from(dependsOn);
                            property = dependsOn.property;
                            system = dependsOn.system;
                            value = dependsOn.value;
                            display = dependsOn.display;
                            return this;
                        }
                    }
                }
            }
        }

        /**
         * What to do when there is no mapping for the source concept. "Unmapped" does not include codes that are unmatched, and 
         * the unmapped element is ignored in a code is specified to have equivalence = unmatched.
         */
        public static class Unmapped extends BackboneElement {
            @Binding(
                bindingName = "ConceptMapGroupUnmappedMode",
                strength = BindingStrength.Value.REQUIRED,
                description = "Defines which action to take if there is no match in the group.",
                valueSet = "http://hl7.org/fhir/ValueSet/conceptmap-unmapped-mode|4.3.0-CIBUILD"
            )
            @Required
            private final ConceptMapGroupUnmappedMode mode;
            private final Code code;
            private final String display;
            private final Canonical url;

            private Unmapped(Builder builder) {
                super(builder);
                mode = builder.mode;
                code = builder.code;
                display = builder.display;
                url = builder.url;
            }

            /**
             * Defines which action to take if there is no match for the source concept in the target system designated for the 
             * group. One of 3 actions are possible: use the unmapped code (this is useful when doing a mapping between versions, and 
             * only a few codes have changed), use a fixed code (a default code), or alternatively, a reference to a different 
             * concept map can be provided (by canonical URL).
             * 
             * @return
             *     An immutable object of type {@link ConceptMapGroupUnmappedMode} that is non-null.
             */
            public ConceptMapGroupUnmappedMode getMode() {
                return mode;
            }

            /**
             * The fixed code to use when the mode = 'fixed' - all unmapped codes are mapped to a single fixed code.
             * 
             * @return
             *     An immutable object of type {@link Code} that may be null.
             */
            public Code getCode() {
                return code;
            }

            /**
             * The display for the code. The display is only provided to help editors when editing the concept map.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDisplay() {
                return display;
            }

            /**
             * The canonical reference to an additional ConceptMap resource instance to use for mapping if this ConceptMap resource 
             * contains no matching mapping for the source concept.
             * 
             * @return
             *     An immutable object of type {@link Canonical} that may be null.
             */
            public Canonical getUrl() {
                return url;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (mode != null) || 
                    (code != null) || 
                    (display != null) || 
                    (url != null);
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
                        accept(code, "code", visitor);
                        accept(display, "display", visitor);
                        accept(url, "url", visitor);
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
                Unmapped other = (Unmapped) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(mode, other.mode) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(display, other.display) && 
                    Objects.equals(url, other.url);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        mode, 
                        code, 
                        display, 
                        url);
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
                private ConceptMapGroupUnmappedMode mode;
                private Code code;
                private String display;
                private Canonical url;

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
                 * Defines which action to take if there is no match for the source concept in the target system designated for the 
                 * group. One of 3 actions are possible: use the unmapped code (this is useful when doing a mapping between versions, and 
                 * only a few codes have changed), use a fixed code (a default code), or alternatively, a reference to a different 
                 * concept map can be provided (by canonical URL).
                 * 
                 * <p>This element is required.
                 * 
                 * @param mode
                 *     provided | fixed | other-map
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder mode(ConceptMapGroupUnmappedMode mode) {
                    this.mode = mode;
                    return this;
                }

                /**
                 * The fixed code to use when the mode = 'fixed' - all unmapped codes are mapped to a single fixed code.
                 * 
                 * @param code
                 *     Fixed code when mode = fixed
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
                 *     Display for the code
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
                 * The display for the code. The display is only provided to help editors when editing the concept map.
                 * 
                 * @param display
                 *     Display for the code
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder display(String display) {
                    this.display = display;
                    return this;
                }

                /**
                 * The canonical reference to an additional ConceptMap resource instance to use for mapping if this ConceptMap resource 
                 * contains no matching mapping for the source concept.
                 * 
                 * @param url
                 *     canonical reference to an additional ConceptMap to use for mapping if the source concept is unmapped
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder url(Canonical url) {
                    this.url = url;
                    return this;
                }

                /**
                 * Build the {@link Unmapped}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>mode</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Unmapped}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Unmapped per the base specification
                 */
                @Override
                public Unmapped build() {
                    Unmapped unmapped = new Unmapped(this);
                    if (validating) {
                        validate(unmapped);
                    }
                    return unmapped;
                }

                protected void validate(Unmapped unmapped) {
                    super.validate(unmapped);
                    ValidationSupport.requireNonNull(unmapped.mode, "mode");
                    ValidationSupport.requireValueOrChildren(unmapped);
                }

                protected Builder from(Unmapped unmapped) {
                    super.from(unmapped);
                    mode = unmapped.mode;
                    code = unmapped.code;
                    display = unmapped.display;
                    url = unmapped.url;
                    return this;
                }
            }
        }
    }
}
