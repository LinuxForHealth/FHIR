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
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.MessageHeaderResponseRequest;
import com.ibm.watsonhealth.fhir.model.type.MessageSignificanceCategory;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Defines the characteristics of a message that can be shared between systems, including the type of event that 
 * initiates the message, the content to be transmitted and what response(s), if any, are permitted.
 * </p>
 */
@Constraint(
    key = "msd-0",
    severity = "warning",
    human = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MessageDefinition extends DomainResource {
    private final Uri url;
    private final List<Identifier> identifier;
    private final String version;
    private final String name;
    private final String title;
    private final List<Canonical> replaces;
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
    private final Canonical base;
    private final List<Canonical> parent;
    private final Element event;
    private final MessageSignificanceCategory category;
    private final List<Focus> focus;
    private final MessageHeaderResponseRequest responseRequired;
    private final List<AllowedResponse> allowedResponse;
    private final List<Canonical> graph;

    private MessageDefinition(Builder builder) {
        super(builder);
        this.url = builder.url;
        this.identifier = builder.identifier;
        this.version = builder.version;
        this.name = builder.name;
        this.title = builder.title;
        this.replaces = builder.replaces;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.experimental = builder.experimental;
        this.date = ValidationSupport.requireNonNull(builder.date, "date");
        this.publisher = builder.publisher;
        this.contact = builder.contact;
        this.description = builder.description;
        this.useContext = builder.useContext;
        this.jurisdiction = builder.jurisdiction;
        this.purpose = builder.purpose;
        this.copyright = builder.copyright;
        this.base = builder.base;
        this.parent = builder.parent;
        this.event = ValidationSupport.requireChoiceElement(builder.event, "event", Coding.class, Uri.class);
        this.category = builder.category;
        this.focus = builder.focus;
        this.responseRequired = builder.responseRequired;
        this.allowedResponse = builder.allowedResponse;
        this.graph = builder.graph;
    }

    /**
     * <p>
     * The business identifier that is used to reference the MessageDefinition and *is* expected to be consistent from server 
     * to server.
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
     * A formal identifier that is used to identify this message definition when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
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
     * The identifier that is used to identify this version of the message definition when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the message definition author and is 
     * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
     * available. There is also no expectation that versions can be placed in a lexicographical sequence.
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
     * A natural language name identifying the message definition. This name should be usable as an identifier for the module 
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
     * A short, descriptive, user-friendly title for the message definition.
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
     * A MessageDefinition that is superseded by this definition.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getReplaces() {
        return replaces;
    }

    /**
     * <p>
     * The status of this message definition. Enables tracking the life-cycle of the content.
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
     * A Boolean value to indicate that this message definition is authored for testing purposes (or 
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
     * The date (and optionally time) when the message definition was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the message definition changes.
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
     * The name of the organization or individual that published the message definition.
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
     * A free text natural language description of the message definition from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate message definition instances.
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
     * A legal or geographic region in which the message definition is intended to be used.
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
     * Explanation of why this message definition is needed and why it has been designed as it has.
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
     * A copyright statement relating to the message definition and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the message definition.
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
     * The MessageDefinition that is the basis for the contents of this resource.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getBase() {
        return base;
    }

    /**
     * <p>
     * Identifies a protocol or workflow that this MessageDefinition represents a step in.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getParent() {
        return parent;
    }

    /**
     * <p>
     * Event code or link to the EventDefinition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getEvent() {
        return event;
    }

    /**
     * <p>
     * The impact of the content of the message.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MessageSignificanceCategory}.
     */
    public MessageSignificanceCategory getCategory() {
        return category;
    }

    /**
     * <p>
     * Identifies the resource (or resources) that are being addressed by the event. For example, the Encounter for an admit 
     * message or two Account records for a merge.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Focus}.
     */
    public List<Focus> getFocus() {
        return focus;
    }

    /**
     * <p>
     * Declare at a message definition level whether a response is required or only upon error or success, or never.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MessageHeaderResponseRequest}.
     */
    public MessageHeaderResponseRequest getResponseRequired() {
        return responseRequired;
    }

    /**
     * <p>
     * Indicates what types of messages may be sent as an application-level response to this message.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link AllowedResponse}.
     */
    public List<AllowedResponse> getAllowedResponse() {
        return allowedResponse;
    }

    /**
     * <p>
     * Canonical reference to a GraphDefinition. If a URL is provided, it is the canonical reference to a [GraphDefinition]
     * (graphdefinition.html) that it controls what resources are to be added to the bundle when building the document. The 
     * GraphDefinition can also specify profiles that apply to the various resources.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getGraph() {
        return graph;
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
                accept(replaces, "replaces", visitor, Canonical.class);
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
                accept(base, "base", visitor);
                accept(parent, "parent", visitor, Canonical.class);
                accept(event, "event", visitor, true);
                accept(category, "category", visitor);
                accept(focus, "focus", visitor, Focus.class);
                accept(responseRequired, "responseRequired", visitor);
                accept(allowedResponse, "allowedResponse", visitor, AllowedResponse.class);
                accept(graph, "graph", visitor, Canonical.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, date, event);
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
        builder.replaces.addAll(replaces);
        builder.experimental = experimental;
        builder.publisher = publisher;
        builder.contact.addAll(contact);
        builder.description = description;
        builder.useContext.addAll(useContext);
        builder.jurisdiction.addAll(jurisdiction);
        builder.purpose = purpose;
        builder.copyright = copyright;
        builder.base = base;
        builder.parent.addAll(parent);
        builder.category = category;
        builder.focus.addAll(focus);
        builder.responseRequired = responseRequired;
        builder.allowedResponse.addAll(allowedResponse);
        builder.graph.addAll(graph);
        return builder;
    }

    public static Builder builder(PublicationStatus status, DateTime date, Element event) {
        return new Builder(status, date, event);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final PublicationStatus status;
        private final DateTime date;
        private final Element event;

        // optional
        private Uri url;
        private List<Identifier> identifier = new ArrayList<>();
        private String version;
        private String name;
        private String title;
        private List<Canonical> replaces = new ArrayList<>();
        private Boolean experimental;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private Markdown copyright;
        private Canonical base;
        private List<Canonical> parent = new ArrayList<>();
        private MessageSignificanceCategory category;
        private List<Focus> focus = new ArrayList<>();
        private MessageHeaderResponseRequest responseRequired;
        private List<AllowedResponse> allowedResponse = new ArrayList<>();
        private List<Canonical> graph = new ArrayList<>();

        private Builder(PublicationStatus status, DateTime date, Element event) {
            super();
            this.status = status;
            this.date = date;
            this.event = event;
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
         * The business identifier that is used to reference the MessageDefinition and *is* expected to be consistent from server 
         * to server.
         * </p>
         * 
         * @param url
         *     Business Identifier for a given MessageDefinition
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
         * A formal identifier that is used to identify this message definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Primary key for the message definition on a given server
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
         * A formal identifier that is used to identify this message definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Primary key for the message definition on a given server
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
         * The identifier that is used to identify this version of the message definition when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the message definition author and is 
         * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
         * available. There is also no expectation that versions can be placed in a lexicographical sequence.
         * </p>
         * 
         * @param version
         *     Business version of the message definition
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
         * A natural language name identifying the message definition. This name should be usable as an identifier for the module 
         * by machine processing applications such as code generation.
         * </p>
         * 
         * @param name
         *     Name for this message definition (computer friendly)
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
         * A short, descriptive, user-friendly title for the message definition.
         * </p>
         * 
         * @param title
         *     Name for this message definition (human friendly)
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
         * A MessageDefinition that is superseded by this definition.
         * </p>
         * 
         * @param replaces
         *     Takes the place of
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder replaces(Canonical... replaces) {
            for (Canonical value : replaces) {
                this.replaces.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A MessageDefinition that is superseded by this definition.
         * </p>
         * 
         * @param replaces
         *     Takes the place of
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder replaces(Collection<Canonical> replaces) {
            this.replaces.addAll(replaces);
            return this;
        }

        /**
         * <p>
         * A Boolean value to indicate that this message definition is authored for testing purposes (or 
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
         * The name of the organization or individual that published the message definition.
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
         * A free text natural language description of the message definition from a consumer's perspective.
         * </p>
         * 
         * @param description
         *     Natural language description of the message definition
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
         * may be used to assist with indexing and searching for appropriate message definition instances.
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
         * may be used to assist with indexing and searching for appropriate message definition instances.
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
         * A legal or geographic region in which the message definition is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for message definition (if applicable)
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
         * A legal or geographic region in which the message definition is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for message definition (if applicable)
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
         * Explanation of why this message definition is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this message definition is defined
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
         * A copyright statement relating to the message definition and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the message definition.
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
         * The MessageDefinition that is the basis for the contents of this resource.
         * </p>
         * 
         * @param base
         *     Definition this one is based on
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder base(Canonical base) {
            this.base = base;
            return this;
        }

        /**
         * <p>
         * Identifies a protocol or workflow that this MessageDefinition represents a step in.
         * </p>
         * 
         * @param parent
         *     Protocol/workflow this is part of
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parent(Canonical... parent) {
            for (Canonical value : parent) {
                this.parent.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies a protocol or workflow that this MessageDefinition represents a step in.
         * </p>
         * 
         * @param parent
         *     Protocol/workflow this is part of
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parent(Collection<Canonical> parent) {
            this.parent.addAll(parent);
            return this;
        }

        /**
         * <p>
         * The impact of the content of the message.
         * </p>
         * 
         * @param category
         *     consequence | currency | notification
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(MessageSignificanceCategory category) {
            this.category = category;
            return this;
        }

        /**
         * <p>
         * Identifies the resource (or resources) that are being addressed by the event. For example, the Encounter for an admit 
         * message or two Account records for a merge.
         * </p>
         * 
         * @param focus
         *     Resource(s) that are the subject of the event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder focus(Focus... focus) {
            for (Focus value : focus) {
                this.focus.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies the resource (or resources) that are being addressed by the event. For example, the Encounter for an admit 
         * message or two Account records for a merge.
         * </p>
         * 
         * @param focus
         *     Resource(s) that are the subject of the event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder focus(Collection<Focus> focus) {
            this.focus.addAll(focus);
            return this;
        }

        /**
         * <p>
         * Declare at a message definition level whether a response is required or only upon error or success, or never.
         * </p>
         * 
         * @param responseRequired
         *     always | on-error | never | on-success
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder responseRequired(MessageHeaderResponseRequest responseRequired) {
            this.responseRequired = responseRequired;
            return this;
        }

        /**
         * <p>
         * Indicates what types of messages may be sent as an application-level response to this message.
         * </p>
         * 
         * @param allowedResponse
         *     Responses to this message
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder allowedResponse(AllowedResponse... allowedResponse) {
            for (AllowedResponse value : allowedResponse) {
                this.allowedResponse.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates what types of messages may be sent as an application-level response to this message.
         * </p>
         * 
         * @param allowedResponse
         *     Responses to this message
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder allowedResponse(Collection<AllowedResponse> allowedResponse) {
            this.allowedResponse.addAll(allowedResponse);
            return this;
        }

        /**
         * <p>
         * Canonical reference to a GraphDefinition. If a URL is provided, it is the canonical reference to a [GraphDefinition]
         * (graphdefinition.html) that it controls what resources are to be added to the bundle when building the document. The 
         * GraphDefinition can also specify profiles that apply to the various resources.
         * </p>
         * 
         * @param graph
         *     Canonical reference to a GraphDefinition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder graph(Canonical... graph) {
            for (Canonical value : graph) {
                this.graph.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Canonical reference to a GraphDefinition. If a URL is provided, it is the canonical reference to a [GraphDefinition]
         * (graphdefinition.html) that it controls what resources are to be added to the bundle when building the document. The 
         * GraphDefinition can also specify profiles that apply to the various resources.
         * </p>
         * 
         * @param graph
         *     Canonical reference to a GraphDefinition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder graph(Collection<Canonical> graph) {
            this.graph.addAll(graph);
            return this;
        }

        @Override
        public MessageDefinition build() {
            return new MessageDefinition(this);
        }
    }

    /**
     * <p>
     * Identifies the resource (or resources) that are being addressed by the event. For example, the Encounter for an admit 
     * message or two Account records for a merge.
     * </p>
     */
    public static class Focus extends BackboneElement {
        private final ResourceType code;
        private final Canonical profile;
        private final UnsignedInt min;
        private final String max;

        private Focus(Builder builder) {
            super(builder);
            this.code = ValidationSupport.requireNonNull(builder.code, "code");
            this.profile = builder.profile;
            this.min = ValidationSupport.requireNonNull(builder.min, "min");
            this.max = builder.max;
        }

        /**
         * <p>
         * The kind of resource that must be the focus for this message.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ResourceType}.
         */
        public ResourceType getCode() {
            return code;
        }

        /**
         * <p>
         * A profile that reflects constraints for the focal resource (and potentially for related resources).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getProfile() {
            return profile;
        }

        /**
         * <p>
         * Identifies the minimum number of resources of this type that must be pointed to by a message in order for it to be 
         * valid against this MessageDefinition.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link UnsignedInt}.
         */
        public UnsignedInt getMin() {
            return min;
        }

        /**
         * <p>
         * Identifies the maximum number of resources of this type that must be pointed to by a message in order for it to be 
         * valid against this MessageDefinition.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getMax() {
            return max;
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
                    accept(profile, "profile", visitor);
                    accept(min, "min", visitor);
                    accept(max, "max", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(ResourceType code, UnsignedInt min) {
            return new Builder(code, min);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final ResourceType code;
            private final UnsignedInt min;

            // optional
            private Canonical profile;
            private String max;

            private Builder(ResourceType code, UnsignedInt min) {
                super();
                this.code = code;
                this.min = min;
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
             * A profile that reflects constraints for the focal resource (and potentially for related resources).
             * </p>
             * 
             * @param profile
             *     Profile that must be adhered to by focus
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder profile(Canonical profile) {
                this.profile = profile;
                return this;
            }

            /**
             * <p>
             * Identifies the maximum number of resources of this type that must be pointed to by a message in order for it to be 
             * valid against this MessageDefinition.
             * </p>
             * 
             * @param max
             *     Maximum number of focuses of this type
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder max(String max) {
                this.max = max;
                return this;
            }

            @Override
            public Focus build() {
                return new Focus(this);
            }

            private static Builder from(Focus focus) {
                Builder builder = new Builder(focus.code, focus.min);
                builder.id = focus.id;
                builder.extension.addAll(focus.extension);
                builder.modifierExtension.addAll(focus.modifierExtension);
                builder.profile = focus.profile;
                builder.max = focus.max;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Indicates what types of messages may be sent as an application-level response to this message.
     * </p>
     */
    public static class AllowedResponse extends BackboneElement {
        private final Canonical message;
        private final Markdown situation;

        private AllowedResponse(Builder builder) {
            super(builder);
            this.message = ValidationSupport.requireNonNull(builder.message, "message");
            this.situation = builder.situation;
        }

        /**
         * <p>
         * A reference to the message definition that must be adhered to by this supported response.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getMessage() {
            return message;
        }

        /**
         * <p>
         * Provides a description of the circumstances in which this response should be used (as opposed to one of the 
         * alternative responses).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Markdown}.
         */
        public Markdown getSituation() {
            return situation;
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
                    accept(message, "message", visitor);
                    accept(situation, "situation", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Canonical message) {
            return new Builder(message);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Canonical message;

            // optional
            private Markdown situation;

            private Builder(Canonical message) {
                super();
                this.message = message;
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
             * Provides a description of the circumstances in which this response should be used (as opposed to one of the 
             * alternative responses).
             * </p>
             * 
             * @param situation
             *     When should this response be used
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder situation(Markdown situation) {
                this.situation = situation;
                return this;
            }

            @Override
            public AllowedResponse build() {
                return new AllowedResponse(this);
            }

            private static Builder from(AllowedResponse allowedResponse) {
                Builder builder = new Builder(allowedResponse.message);
                builder.id = allowedResponse.id;
                builder.extension.addAll(allowedResponse.extension);
                builder.modifierExtension.addAll(allowedResponse.modifierExtension);
                builder.situation = allowedResponse.situation;
                return builder;
            }
        }
    }
}
