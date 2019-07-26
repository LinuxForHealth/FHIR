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
import com.ibm.watsonhealth.fhir.model.type.ConceptMapEquivalence;
import com.ibm.watsonhealth.fhir.model.type.ConceptMapGroupUnmappedMode;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
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
 * A statement of relationships from one set of concepts to one or more other concepts - either concepts in code systems, 
 * or data element/data element concepts, or classes in class models.
 * </p>
 */
@Constraint(
    id = "cmd-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Constraint(
    id = "cmd-1",
    level = "Rule",
    location = "ConceptMap.group.element.target",
    description = "If the map is narrower or inexact, there SHALL be some comments",
    expression = "comment.exists() or equivalence.empty() or ((equivalence != 'narrower') and (equivalence != 'inexact'))"
)
@Constraint(
    id = "cmd-2",
    level = "Rule",
    location = "ConceptMap.group.unmapped",
    description = "If the mode is 'fixed', a code must be provided",
    expression = "(mode = 'fixed') implies code.exists()"
)
@Constraint(
    id = "cmd-3",
    level = "Rule",
    location = "ConceptMap.group.unmapped",
    description = "If the mode is 'other-map', a url must be provided",
    expression = "(mode = 'other-map') implies url.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ConceptMap extends DomainResource {
    private final Uri url;
    private final Identifier identifier;
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
    private final Element source;
    private final Element target;
    private final List<Group> group;

    private volatile int hashCode;

    private ConceptMap(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = builder.identifier;
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
        purpose = builder.purpose;
        copyright = builder.copyright;
        source = ValidationSupport.choiceElement(builder.source, "source", Uri.class, Canonical.class);
        target = ValidationSupport.choiceElement(builder.target, "target", Uri.class, Canonical.class);
        group = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.group, "group"));
    }

    /**
     * <p>
     * An absolute URI that is used to identify this concept map when it is referenced in a specification, model, design or 
     * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this concept map is (or will be) published. This URL can be the target of 
     * a canonical reference. It SHALL remain the same when the concept map is stored on different servers.
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
     * A formal identifier that is used to identify this concept map when it is represented in other formats, or referenced 
     * in a specification, model, design or an instance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The identifier that is used to identify this version of the concept map when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the concept map author and is not expected to be 
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
     * A natural language name identifying the concept map. This name should be usable as an identifier for the module by 
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
     * A short, descriptive, user-friendly title for the concept map.
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
     * The status of this concept map. Enables tracking the life-cycle of the content.
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
     * A Boolean value to indicate that this concept map is authored for testing purposes (or education/evaluation/marketing) 
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
     * The date (and optionally time) when the concept map was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the concept map changes.
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
     * The name of the organization or individual that published the concept map.
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
     * A free text natural language description of the concept map from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate concept map instances.
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
     * A legal or geographic region in which the concept map is intended to be used.
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
     * Explanation of why this concept map is needed and why it has been designed as it has.
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
     * A copyright statement relating to the concept map and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the concept map.
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
     * Identifier for the source value set that contains the concepts that are being mapped and provides context for the 
     * mappings.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getSource() {
        return source;
    }

    /**
     * <p>
     * The target value set provides context for the mappings. Note that the mapping is made between concepts, not between 
     * value sets, but the value set provides important context about how the concept mapping choices are made.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getTarget() {
        return target;
    }

    /**
     * <p>
     * A group of mappings that all have the same source and target system.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Group}.
     */
    public List<Group> getGroup() {
        return group;
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

    public static Builder builder(PublicationStatus status) {
        Builder builder = new Builder();
        builder.status(status);
        return builder;
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
         * An absolute URI that is used to identify this concept map when it is referenced in a specification, model, design or 
         * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
         * which at which an authoritative instance of this concept map is (or will be) published. This URL can be the target of 
         * a canonical reference. It SHALL remain the same when the concept map is stored on different servers.
         * </p>
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
         * <p>
         * A formal identifier that is used to identify this concept map when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * </p>
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
         * <p>
         * The identifier that is used to identify this version of the concept map when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the concept map author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence.
         * </p>
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
         * <p>
         * A natural language name identifying the concept map. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
         * </p>
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
         * <p>
         * A short, descriptive, user-friendly title for the concept map.
         * </p>
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
         * <p>
         * The status of this concept map. Enables tracking the life-cycle of the content.
         * </p>
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
         * <p>
         * A Boolean value to indicate that this concept map is authored for testing purposes (or education/evaluation/marketing) 
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
         * The date (and optionally time) when the concept map was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the concept map changes.
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
         * The name of the organization or individual that published the concept map.
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
         * A free text natural language description of the concept map from a consumer's perspective.
         * </p>
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
         * <p>
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate concept map instances.
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
         * may be used to assist with indexing and searching for appropriate concept map instances.
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
         * A legal or geographic region in which the concept map is intended to be used.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * A legal or geographic region in which the concept map is intended to be used.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for concept map (if applicable)
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
         * Explanation of why this concept map is needed and why it has been designed as it has.
         * </p>
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
         * <p>
         * A copyright statement relating to the concept map and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the concept map.
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
         * Identifier for the source value set that contains the concepts that are being mapped and provides context for the 
         * mappings.
         * </p>
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
         * <p>
         * The target value set provides context for the mappings. Note that the mapping is made between concepts, not between 
         * value sets, but the value set provides important context about how the concept mapping choices are made.
         * </p>
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
         * <p>
         * A group of mappings that all have the same source and target system.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * A group of mappings that all have the same source and target system.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param group
         *     Same source and target systems
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder group(Collection<Group> group) {
            this.group = new ArrayList<>(group);
            return this;
        }

        @Override
        public ConceptMap build() {
            return new ConceptMap(this);
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
     * <p>
     * A group of mappings that all have the same source and target system.
     * </p>
     */
    public static class Group extends BackboneElement {
        private final Uri source;
        private final String sourceVersion;
        private final Uri target;
        private final String targetVersion;
        private final List<Element> element;
        private final Unmapped unmapped;

        private volatile int hashCode;

        private Group(Builder builder) {
            super(builder);
            source = builder.source;
            sourceVersion = builder.sourceVersion;
            target = builder.target;
            targetVersion = builder.targetVersion;
            element = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.element, "element"));
            unmapped = builder.unmapped;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * An absolute URI that identifies the source system where the concepts to be mapped are defined.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getSource() {
            return source;
        }

        /**
         * <p>
         * The specific version of the code system, as determined by the code system authority.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getSourceVersion() {
            return sourceVersion;
        }

        /**
         * <p>
         * An absolute URI that identifies the target system that the concepts will be mapped to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getTarget() {
            return target;
        }

        /**
         * <p>
         * The specific version of the code system, as determined by the code system authority.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getTargetVersion() {
            return targetVersion;
        }

        /**
         * <p>
         * Mappings for an individual concept in the source to one or more concepts in the target.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Element}.
         */
        public List<Element> getElement() {
            return element;
        }

        /**
         * <p>
         * What to do when there is no mapping for the source concept. "Unmapped" does not include codes that are unmatched, and 
         * the unmapped element is ignored in a code is specified to have equivalence = unmatched.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Unmapped}.
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

        public static Builder builder(Collection<Element> element) {
            Builder builder = new Builder();
            builder.element(element);
            return builder;
        }

        public static class Builder extends BackboneElement.Builder {
            private Uri source;
            private String sourceVersion;
            private Uri target;
            private String targetVersion;
            private List<Element> element = new ArrayList<>();
            private Unmapped unmapped;

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
             * An absolute URI that identifies the source system where the concepts to be mapped are defined.
             * </p>
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
             * <p>
             * The specific version of the code system, as determined by the code system authority.
             * </p>
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
             * <p>
             * An absolute URI that identifies the target system that the concepts will be mapped to.
             * </p>
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
             * <p>
             * The specific version of the code system, as determined by the code system authority.
             * </p>
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
             * <p>
             * Mappings for an individual concept in the source to one or more concepts in the target.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
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
             * <p>
             * Mappings for an individual concept in the source to one or more concepts in the target.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param element
             *     Mappings for a concept from the source set
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder element(Collection<Element> element) {
                this.element = new ArrayList<>(element);
                return this;
            }

            /**
             * <p>
             * What to do when there is no mapping for the source concept. "Unmapped" does not include codes that are unmatched, and 
             * the unmapped element is ignored in a code is specified to have equivalence = unmatched.
             * </p>
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

            @Override
            public Group build() {
                return new Group(this);
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
         * <p>
         * Mappings for an individual concept in the source to one or more concepts in the target.
         * </p>
         */
        public static class Element extends BackboneElement {
            private final Code code;
            private final String display;
            private final List<Target> target;

            private volatile int hashCode;

            private Element(Builder builder) {
                super(builder);
                code = builder.code;
                display = builder.display;
                target = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.target, "target"));
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Identity (code or path) or the element/item being mapped.
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
             * The display for the code. The display is only provided to help editors when editing the concept map.
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
             * A concept from the target value set that this concept maps to.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Target}.
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
                Builder builder = new Builder();
                return builder;
            }

            public static class Builder extends BackboneElement.Builder {
                private Code code;
                private String display;
                private List<Target> target = new ArrayList<>();

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
                 * Identity (code or path) or the element/item being mapped.
                 * </p>
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
                 * <p>
                 * The display for the code. The display is only provided to help editors when editing the concept map.
                 * </p>
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
                 * <p>
                 * A concept from the target value set that this concept maps to.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
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
                 * <p>
                 * A concept from the target value set that this concept maps to.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param target
                 *     Concept in target system for element
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder target(Collection<Target> target) {
                    this.target = new ArrayList<>(target);
                    return this;
                }

                @Override
                public Element build() {
                    return new Element(this);
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
             * <p>
             * A concept from the target value set that this concept maps to.
             * </p>
             */
            public static class Target extends BackboneElement {
                private final Code code;
                private final String display;
                private final ConceptMapEquivalence equivalence;
                private final String comment;
                private final List<DependsOn> dependsOn;
                private final List<ConceptMap.Group.Element.Target.DependsOn> product;

                private volatile int hashCode;

                private Target(Builder builder) {
                    super(builder);
                    code = builder.code;
                    display = builder.display;
                    equivalence = ValidationSupport.requireNonNull(builder.equivalence, "equivalence");
                    comment = builder.comment;
                    dependsOn = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.dependsOn, "dependsOn"));
                    product = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.product, "product"));
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * <p>
                 * Identity (code or path) or the element/item that the map refers to.
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
                 * The display for the code. The display is only provided to help editors when editing the concept map.
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
                 * The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence 
                 * is read from target to source (e.g. the target is 'wider' than the source).
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link ConceptMapEquivalence}.
                 */
                public ConceptMapEquivalence getEquivalence() {
                    return equivalence;
                }

                /**
                 * <p>
                 * A description of status/issues in mapping that conveys additional information not represented in the structured data.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getComment() {
                    return comment;
                }

                /**
                 * <p>
                 * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element 
                 * can be resolved, and it has the specified value.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link DependsOn}.
                 */
                public List<DependsOn> getDependsOn() {
                    return dependsOn;
                }

                /**
                 * <p>
                 * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified 
                 * element must be mapped to some data element or source that is in context. The mapping may still be useful without a 
                 * place for the additional data elements, but the equivalence cannot be relied on.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link DependsOn}.
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

                public static Builder builder(ConceptMapEquivalence equivalence) {
                    Builder builder = new Builder();
                    builder.equivalence(equivalence);
                    return builder;
                }

                public static class Builder extends BackboneElement.Builder {
                    private Code code;
                    private String display;
                    private ConceptMapEquivalence equivalence;
                    private String comment;
                    private List<DependsOn> dependsOn = new ArrayList<>();
                    private List<ConceptMap.Group.Element.Target.DependsOn> product = new ArrayList<>();

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
                     * Identity (code or path) or the element/item that the map refers to.
                     * </p>
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
                     * <p>
                     * The display for the code. The display is only provided to help editors when editing the concept map.
                     * </p>
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
                     * <p>
                     * The equivalence between the source and target concepts (counting for the dependencies and products). The equivalence 
                     * is read from target to source (e.g. the target is 'wider' than the source).
                     * </p>
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
                     * <p>
                     * A description of status/issues in mapping that conveys additional information not represented in the structured data.
                     * </p>
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
                     * <p>
                     * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element 
                     * can be resolved, and it has the specified value.
                     * </p>
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
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
                     * <p>
                     * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element 
                     * can be resolved, and it has the specified value.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param dependsOn
                     *     Other elements required for this mapping (from context)
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder dependsOn(Collection<DependsOn> dependsOn) {
                        this.dependsOn = new ArrayList<>(dependsOn);
                        return this;
                    }

                    /**
                     * <p>
                     * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified 
                     * element must be mapped to some data element or source that is in context. The mapping may still be useful without a 
                     * place for the additional data elements, but the equivalence cannot be relied on.
                     * </p>
                     * <p>
                     * Adds new element(s) to existing list
                     * </p>
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
                     * <p>
                     * A set of additional outcomes from this mapping to other elements. To properly execute this mapping, the specified 
                     * element must be mapped to some data element or source that is in context. The mapping may still be useful without a 
                     * place for the additional data elements, but the equivalence cannot be relied on.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param product
                     *     Other concepts that this mapping also produces
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder product(Collection<ConceptMap.Group.Element.Target.DependsOn> product) {
                        this.product = new ArrayList<>(product);
                        return this;
                    }

                    @Override
                    public Target build() {
                        return new Target(this);
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
                 * <p>
                 * A set of additional dependencies for this mapping to hold. This mapping is only applicable if the specified element 
                 * can be resolved, and it has the specified value.
                 * </p>
                 */
                public static class DependsOn extends BackboneElement {
                    private final Uri property;
                    private final Canonical system;
                    private final String value;
                    private final String display;

                    private volatile int hashCode;

                    private DependsOn(Builder builder) {
                        super(builder);
                        property = ValidationSupport.requireNonNull(builder.property, "property");
                        system = builder.system;
                        value = ValidationSupport.requireNonNull(builder.value, "value");
                        display = builder.display;
                        ValidationSupport.requireValueOrChildren(this);
                    }

                    /**
                     * <p>
                     * A reference to an element that holds a coded value that corresponds to a code system property. The idea is that the 
                     * information model carries an element somewhere that is labeled to correspond with a code system property.
                     * </p>
                     * 
                     * @return
                     *     An immutable object of type {@link Uri}.
                     */
                    public Uri getProperty() {
                        return property;
                    }

                    /**
                     * <p>
                     * An absolute URI that identifies the code system of the dependency code (if the source/dependency is a value set that 
                     * crosses code systems).
                     * </p>
                     * 
                     * @return
                     *     An immutable object of type {@link Canonical}.
                     */
                    public Canonical getSystem() {
                        return system;
                    }

                    /**
                     * <p>
                     * Identity (code or path) or the element/item/ValueSet/text that the map depends on / refers to.
                     * </p>
                     * 
                     * @return
                     *     An immutable object of type {@link String}.
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * <p>
                     * The display for the code. The display is only provided to help editors when editing the concept map.
                     * </p>
                     * 
                     * @return
                     *     An immutable object of type {@link String}.
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

                    public static Builder builder(Uri property, String value) {
                        Builder builder = new Builder();
                        builder.property(property);
                        builder.value(value);
                        return builder;
                    }

                    public static class Builder extends BackboneElement.Builder {
                        private Uri property;
                        private Canonical system;
                        private String value;
                        private String display;

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
                         * A reference to an element that holds a coded value that corresponds to a code system property. The idea is that the 
                         * information model carries an element somewhere that is labeled to correspond with a code system property.
                         * </p>
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
                         * <p>
                         * An absolute URI that identifies the code system of the dependency code (if the source/dependency is a value set that 
                         * crosses code systems).
                         * </p>
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
                         * <p>
                         * Identity (code or path) or the element/item/ValueSet/text that the map depends on / refers to.
                         * </p>
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
                         * <p>
                         * The display for the code. The display is only provided to help editors when editing the concept map.
                         * </p>
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

                        @Override
                        public DependsOn build() {
                            return new DependsOn(this);
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
         * <p>
         * What to do when there is no mapping for the source concept. "Unmapped" does not include codes that are unmatched, and 
         * the unmapped element is ignored in a code is specified to have equivalence = unmatched.
         * </p>
         */
        public static class Unmapped extends BackboneElement {
            private final ConceptMapGroupUnmappedMode mode;
            private final Code code;
            private final String display;
            private final Canonical url;

            private volatile int hashCode;

            private Unmapped(Builder builder) {
                super(builder);
                mode = ValidationSupport.requireNonNull(builder.mode, "mode");
                code = builder.code;
                display = builder.display;
                url = builder.url;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Defines which action to take if there is no match for the source concept in the target system designated for the 
             * group. One of 3 actions are possible: use the unmapped code (this is useful when doing a mapping between versions, and 
             * only a few codes have changed), use a fixed code (a default code), or alternatively, a reference to a different 
             * concept map can be provided (by canonical URL).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ConceptMapGroupUnmappedMode}.
             */
            public ConceptMapGroupUnmappedMode getMode() {
                return mode;
            }

            /**
             * <p>
             * The fixed code to use when the mode = 'fixed' - all unmapped codes are mapped to a single fixed code.
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
             * The display for the code. The display is only provided to help editors when editing the concept map.
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
             * The canonical reference to an additional ConceptMap resource instance to use for mapping if this ConceptMap resource 
             * contains no matching mapping for the source concept.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Canonical}.
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

            public static Builder builder(ConceptMapGroupUnmappedMode mode) {
                Builder builder = new Builder();
                builder.mode(mode);
                return builder;
            }

            public static class Builder extends BackboneElement.Builder {
                private ConceptMapGroupUnmappedMode mode;
                private Code code;
                private String display;
                private Canonical url;

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
                 * Defines which action to take if there is no match for the source concept in the target system designated for the 
                 * group. One of 3 actions are possible: use the unmapped code (this is useful when doing a mapping between versions, and 
                 * only a few codes have changed), use a fixed code (a default code), or alternatively, a reference to a different 
                 * concept map can be provided (by canonical URL).
                 * </p>
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
                 * <p>
                 * The fixed code to use when the mode = 'fixed' - all unmapped codes are mapped to a single fixed code.
                 * </p>
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
                 * <p>
                 * The display for the code. The display is only provided to help editors when editing the concept map.
                 * </p>
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
                 * <p>
                 * The canonical reference to an additional ConceptMap resource instance to use for mapping if this ConceptMap resource 
                 * contains no matching mapping for the source concept.
                 * </p>
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

                @Override
                public Unmapped build() {
                    return new Unmapped(this);
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
