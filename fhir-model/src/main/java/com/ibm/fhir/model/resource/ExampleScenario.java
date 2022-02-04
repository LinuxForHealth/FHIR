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
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ExampleScenarioActorType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Example of workflow instance.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "esc-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/ExampleScenario"
)
@Constraint(
    id = "exampleScenario-1",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/ExampleScenario",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ExampleScenario extends DomainResource {
    @Summary
    private final Uri url;
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final String version;
    @Summary
    private final String name;
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
    private final Markdown copyright;
    private final Markdown purpose;
    private final List<Actor> actor;
    private final List<Instance> instance;
    private final List<Process> process;
    private final List<Canonical> workflow;

    private ExampleScenario(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = builder.name;
        status = builder.status;
        experimental = builder.experimental;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        copyright = builder.copyright;
        purpose = builder.purpose;
        actor = Collections.unmodifiableList(builder.actor);
        instance = Collections.unmodifiableList(builder.instance);
        process = Collections.unmodifiableList(builder.process);
        workflow = Collections.unmodifiableList(builder.workflow);
    }

    /**
     * An absolute URI that is used to identify this example scenario when it is referenced in a specification, model, design 
     * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
     * at which at which an authoritative instance of this example scenario is (or will be) published. This URL can be the 
     * target of a canonical reference. It SHALL remain the same when the example scenario is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this example scenario when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the example scenario when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the example scenario author and is not expected to be 
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
     * A natural language name identifying the example scenario. This name should be usable as an identifier for the module 
     * by machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * The status of this example scenario. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this example scenario is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the example scenario was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the example scenario changes. (e.g. the 'content logical definition').
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the example scenario.
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
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate example scenario instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the example scenario is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the example scenario.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * What the example scenario resource is created for. This should not be used to show the business purpose of the 
     * scenario itself, but the purpose of documenting a scenario.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * Actor participating in the resource.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Actor} that may be empty.
     */
    public List<Actor> getActor() {
        return actor;
    }

    /**
     * Each resource and each version that is present in the workflow.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Instance} that may be empty.
     */
    public List<Instance> getInstance() {
        return instance;
    }

    /**
     * Each major process - a group of operations.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Process} that may be empty.
     */
    public List<Process> getProcess() {
        return process;
    }

    /**
     * Another nested workflow.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getWorkflow() {
        return workflow;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            !identifier.isEmpty() || 
            (version != null) || 
            (name != null) || 
            (status != null) || 
            (experimental != null) || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            !useContext.isEmpty() || 
            !jurisdiction.isEmpty() || 
            (copyright != null) || 
            (purpose != null) || 
            !actor.isEmpty() || 
            !instance.isEmpty() || 
            !process.isEmpty() || 
            !workflow.isEmpty();
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
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(copyright, "copyright", visitor);
                accept(purpose, "purpose", visitor);
                accept(actor, "actor", visitor, Actor.class);
                accept(instance, "instance", visitor, Instance.class);
                accept(process, "process", visitor, Process.class);
                accept(workflow, "workflow", visitor, Canonical.class);
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
        ExampleScenario other = (ExampleScenario) obj;
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
            Objects.equals(status, other.status) && 
            Objects.equals(experimental, other.experimental) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(copyright, other.copyright) && 
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(actor, other.actor) && 
            Objects.equals(instance, other.instance) && 
            Objects.equals(process, other.process) && 
            Objects.equals(workflow, other.workflow);
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
                status, 
                experimental, 
                date, 
                publisher, 
                contact, 
                useContext, 
                jurisdiction, 
                copyright, 
                purpose, 
                actor, 
                instance, 
                process, 
                workflow);
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
        private PublicationStatus status;
        private Boolean experimental;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown copyright;
        private Markdown purpose;
        private List<Actor> actor = new ArrayList<>();
        private List<Instance> instance = new ArrayList<>();
        private List<Process> process = new ArrayList<>();
        private List<Canonical> workflow = new ArrayList<>();

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
         * An absolute URI that is used to identify this example scenario when it is referenced in a specification, model, design 
         * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
         * at which at which an authoritative instance of this example scenario is (or will be) published. This URL can be the 
         * target of a canonical reference. It SHALL remain the same when the example scenario is stored on different servers.
         * 
         * @param url
         *     Canonical identifier for this example scenario, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this example scenario when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the example scenario
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
         * A formal identifier that is used to identify this example scenario when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the example scenario
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
         *     Business version of the example scenario
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
         * The identifier that is used to identify this version of the example scenario when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the example scenario author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of the example scenario
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
         *     Name for this example scenario (computer friendly)
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
         * A natural language name identifying the example scenario. This name should be usable as an identifier for the module 
         * by machine processing applications such as code generation.
         * 
         * @param name
         *     Name for this example scenario (computer friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * The status of this example scenario. Enables tracking the life-cycle of the content.
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
         * A Boolean value to indicate that this example scenario is authored for testing purposes (or 
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
         * The date (and optionally time) when the example scenario was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the example scenario changes. (e.g. the 'content logical definition').
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
         * The name of the organization or individual that published the example scenario.
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate example scenario instances.
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
         * may be used to assist with indexing and searching for appropriate example scenario instances.
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
         * A legal or geographic region in which the example scenario is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for example scenario (if applicable)
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
         * A legal or geographic region in which the example scenario is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for example scenario (if applicable)
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
         * A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the example scenario.
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
         * What the example scenario resource is created for. This should not be used to show the business purpose of the 
         * scenario itself, but the purpose of documenting a scenario.
         * 
         * @param purpose
         *     The purpose of the example, e.g. to illustrate a scenario
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * Actor participating in the resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param actor
         *     Actor participating in the resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder actor(Actor... actor) {
            for (Actor value : actor) {
                this.actor.add(value);
            }
            return this;
        }

        /**
         * Actor participating in the resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param actor
         *     Actor participating in the resource
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder actor(Collection<Actor> actor) {
            this.actor = new ArrayList<>(actor);
            return this;
        }

        /**
         * Each resource and each version that is present in the workflow.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instance
         *     Each resource and each version that is present in the workflow
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instance(Instance... instance) {
            for (Instance value : instance) {
                this.instance.add(value);
            }
            return this;
        }

        /**
         * Each resource and each version that is present in the workflow.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instance
         *     Each resource and each version that is present in the workflow
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instance(Collection<Instance> instance) {
            this.instance = new ArrayList<>(instance);
            return this;
        }

        /**
         * Each major process - a group of operations.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param process
         *     Each major process - a group of operations
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder process(Process... process) {
            for (Process value : process) {
                this.process.add(value);
            }
            return this;
        }

        /**
         * Each major process - a group of operations.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param process
         *     Each major process - a group of operations
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder process(Collection<Process> process) {
            this.process = new ArrayList<>(process);
            return this;
        }

        /**
         * Another nested workflow.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param workflow
         *     Another nested workflow
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder workflow(Canonical... workflow) {
            for (Canonical value : workflow) {
                this.workflow.add(value);
            }
            return this;
        }

        /**
         * Another nested workflow.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param workflow
         *     Another nested workflow
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder workflow(Collection<Canonical> workflow) {
            this.workflow = new ArrayList<>(workflow);
            return this;
        }

        /**
         * Build the {@link ExampleScenario}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ExampleScenario}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ExampleScenario per the base specification
         */
        @Override
        public ExampleScenario build() {
            ExampleScenario exampleScenario = new ExampleScenario(this);
            if (validating) {
                validate(exampleScenario);
            }
            return exampleScenario;
        }

        protected void validate(ExampleScenario exampleScenario) {
            super.validate(exampleScenario);
            ValidationSupport.checkList(exampleScenario.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(exampleScenario.status, "status");
            ValidationSupport.checkList(exampleScenario.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(exampleScenario.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(exampleScenario.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkList(exampleScenario.actor, "actor", Actor.class);
            ValidationSupport.checkList(exampleScenario.instance, "instance", Instance.class);
            ValidationSupport.checkList(exampleScenario.process, "process", Process.class);
            ValidationSupport.checkList(exampleScenario.workflow, "workflow", Canonical.class);
        }

        protected Builder from(ExampleScenario exampleScenario) {
            super.from(exampleScenario);
            url = exampleScenario.url;
            identifier.addAll(exampleScenario.identifier);
            version = exampleScenario.version;
            name = exampleScenario.name;
            status = exampleScenario.status;
            experimental = exampleScenario.experimental;
            date = exampleScenario.date;
            publisher = exampleScenario.publisher;
            contact.addAll(exampleScenario.contact);
            useContext.addAll(exampleScenario.useContext);
            jurisdiction.addAll(exampleScenario.jurisdiction);
            copyright = exampleScenario.copyright;
            purpose = exampleScenario.purpose;
            actor.addAll(exampleScenario.actor);
            instance.addAll(exampleScenario.instance);
            process.addAll(exampleScenario.process);
            workflow.addAll(exampleScenario.workflow);
            return this;
        }
    }

    /**
     * Actor participating in the resource.
     */
    public static class Actor extends BackboneElement {
        @Required
        private final String actorId;
        @Binding(
            bindingName = "ExampleScenarioActorType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of actor - system or human.",
            valueSet = "http://hl7.org/fhir/ValueSet/examplescenario-actor-type|4.3.0-CIBUILD"
        )
        @Required
        private final ExampleScenarioActorType type;
        private final String name;
        private final Markdown description;

        private Actor(Builder builder) {
            super(builder);
            actorId = builder.actorId;
            type = builder.type;
            name = builder.name;
            description = builder.description;
        }

        /**
         * ID or acronym of actor.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getActorId() {
            return actorId;
        }

        /**
         * The type of actor - person or system.
         * 
         * @return
         *     An immutable object of type {@link ExampleScenarioActorType} that is non-null.
         */
        public ExampleScenarioActorType getType() {
            return type;
        }

        /**
         * The name of the actor as shown in the page.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * The description of the actor.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getDescription() {
            return description;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (actorId != null) || 
                (type != null) || 
                (name != null) || 
                (description != null);
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
                    accept(actorId, "actorId", visitor);
                    accept(type, "type", visitor);
                    accept(name, "name", visitor);
                    accept(description, "description", visitor);
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
            Actor other = (Actor) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(actorId, other.actorId) && 
                Objects.equals(type, other.type) && 
                Objects.equals(name, other.name) && 
                Objects.equals(description, other.description);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    actorId, 
                    type, 
                    name, 
                    description);
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
            private String actorId;
            private ExampleScenarioActorType type;
            private String name;
            private Markdown description;

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
             * Convenience method for setting {@code actorId}.
             * 
             * <p>This element is required.
             * 
             * @param actorId
             *     ID or acronym of the actor
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #actorId(com.ibm.fhir.model.type.String)
             */
            public Builder actorId(java.lang.String actorId) {
                this.actorId = (actorId == null) ? null : String.of(actorId);
                return this;
            }

            /**
             * ID or acronym of actor.
             * 
             * <p>This element is required.
             * 
             * @param actorId
             *     ID or acronym of the actor
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder actorId(String actorId) {
                this.actorId = actorId;
                return this;
            }

            /**
             * The type of actor - person or system.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     person | entity
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(ExampleScenarioActorType type) {
                this.type = type;
                return this;
            }

            /**
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     The name of the actor as shown in the page
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
             * The name of the actor as shown in the page.
             * 
             * @param name
             *     The name of the actor as shown in the page
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * The description of the actor.
             * 
             * @param description
             *     The description of the actor
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(Markdown description) {
                this.description = description;
                return this;
            }

            /**
             * Build the {@link Actor}
             * 
             * <p>Required elements:
             * <ul>
             * <li>actorId</li>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Actor}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Actor per the base specification
             */
            @Override
            public Actor build() {
                Actor actor = new Actor(this);
                if (validating) {
                    validate(actor);
                }
                return actor;
            }

            protected void validate(Actor actor) {
                super.validate(actor);
                ValidationSupport.requireNonNull(actor.actorId, "actorId");
                ValidationSupport.requireNonNull(actor.type, "type");
                ValidationSupport.requireValueOrChildren(actor);
            }

            protected Builder from(Actor actor) {
                super.from(actor);
                actorId = actor.actorId;
                type = actor.type;
                name = actor.name;
                description = actor.description;
                return this;
            }
        }
    }

    /**
     * Each resource and each version that is present in the workflow.
     */
    public static class Instance extends BackboneElement {
        @Required
        private final String resourceId;
        @Binding(
            bindingName = "FHIRResourceType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of resource.",
            valueSet = "http://hl7.org/fhir/ValueSet/resource-types|4.3.0-CIBUILD"
        )
        @Required
        private final ResourceTypeCode resourceType;
        private final String name;
        private final Markdown description;
        private final List<Version> version;
        private final List<ContainedInstance> containedInstance;

        private Instance(Builder builder) {
            super(builder);
            resourceId = builder.resourceId;
            resourceType = builder.resourceType;
            name = builder.name;
            description = builder.description;
            version = Collections.unmodifiableList(builder.version);
            containedInstance = Collections.unmodifiableList(builder.containedInstance);
        }

        /**
         * The id of the resource for referencing.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getResourceId() {
            return resourceId;
        }

        /**
         * The type of the resource.
         * 
         * @return
         *     An immutable object of type {@link ResourceTypeCode} that is non-null.
         */
        public ResourceTypeCode getResourceType() {
            return resourceType;
        }

        /**
         * A short name for the resource instance.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * Human-friendly description of the resource instance.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getDescription() {
            return description;
        }

        /**
         * A specific version of the resource.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Version} that may be empty.
         */
        public List<Version> getVersion() {
            return version;
        }

        /**
         * Resources contained in the instance (e.g. the observations contained in a bundle).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ContainedInstance} that may be empty.
         */
        public List<ContainedInstance> getContainedInstance() {
            return containedInstance;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (resourceId != null) || 
                (resourceType != null) || 
                (name != null) || 
                (description != null) || 
                !version.isEmpty() || 
                !containedInstance.isEmpty();
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
                    accept(resourceId, "resourceId", visitor);
                    accept(resourceType, "resourceType", visitor);
                    accept(name, "name", visitor);
                    accept(description, "description", visitor);
                    accept(version, "version", visitor, Version.class);
                    accept(containedInstance, "containedInstance", visitor, ContainedInstance.class);
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
            Instance other = (Instance) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(resourceId, other.resourceId) && 
                Objects.equals(resourceType, other.resourceType) && 
                Objects.equals(name, other.name) && 
                Objects.equals(description, other.description) && 
                Objects.equals(version, other.version) && 
                Objects.equals(containedInstance, other.containedInstance);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    resourceId, 
                    resourceType, 
                    name, 
                    description, 
                    version, 
                    containedInstance);
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
            private String resourceId;
            private ResourceTypeCode resourceType;
            private String name;
            private Markdown description;
            private List<Version> version = new ArrayList<>();
            private List<ContainedInstance> containedInstance = new ArrayList<>();

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
             * Convenience method for setting {@code resourceId}.
             * 
             * <p>This element is required.
             * 
             * @param resourceId
             *     The id of the resource for referencing
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #resourceId(com.ibm.fhir.model.type.String)
             */
            public Builder resourceId(java.lang.String resourceId) {
                this.resourceId = (resourceId == null) ? null : String.of(resourceId);
                return this;
            }

            /**
             * The id of the resource for referencing.
             * 
             * <p>This element is required.
             * 
             * @param resourceId
             *     The id of the resource for referencing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resourceId(String resourceId) {
                this.resourceId = resourceId;
                return this;
            }

            /**
             * The type of the resource.
             * 
             * <p>This element is required.
             * 
             * @param resourceType
             *     The type of the resource
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resourceType(ResourceTypeCode resourceType) {
                this.resourceType = resourceType;
                return this;
            }

            /**
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     A short name for the resource instance
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
             * A short name for the resource instance.
             * 
             * @param name
             *     A short name for the resource instance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Human-friendly description of the resource instance.
             * 
             * @param description
             *     Human-friendly description of the resource instance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(Markdown description) {
                this.description = description;
                return this;
            }

            /**
             * A specific version of the resource.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param version
             *     A specific version of the resource
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder version(Version... version) {
                for (Version value : version) {
                    this.version.add(value);
                }
                return this;
            }

            /**
             * A specific version of the resource.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param version
             *     A specific version of the resource
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder version(Collection<Version> version) {
                this.version = new ArrayList<>(version);
                return this;
            }

            /**
             * Resources contained in the instance (e.g. the observations contained in a bundle).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param containedInstance
             *     Resources contained in the instance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder containedInstance(ContainedInstance... containedInstance) {
                for (ContainedInstance value : containedInstance) {
                    this.containedInstance.add(value);
                }
                return this;
            }

            /**
             * Resources contained in the instance (e.g. the observations contained in a bundle).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param containedInstance
             *     Resources contained in the instance
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder containedInstance(Collection<ContainedInstance> containedInstance) {
                this.containedInstance = new ArrayList<>(containedInstance);
                return this;
            }

            /**
             * Build the {@link Instance}
             * 
             * <p>Required elements:
             * <ul>
             * <li>resourceId</li>
             * <li>resourceType</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Instance}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Instance per the base specification
             */
            @Override
            public Instance build() {
                Instance instance = new Instance(this);
                if (validating) {
                    validate(instance);
                }
                return instance;
            }

            protected void validate(Instance instance) {
                super.validate(instance);
                ValidationSupport.requireNonNull(instance.resourceId, "resourceId");
                ValidationSupport.requireNonNull(instance.resourceType, "resourceType");
                ValidationSupport.checkList(instance.version, "version", Version.class);
                ValidationSupport.checkList(instance.containedInstance, "containedInstance", ContainedInstance.class);
                ValidationSupport.requireValueOrChildren(instance);
            }

            protected Builder from(Instance instance) {
                super.from(instance);
                resourceId = instance.resourceId;
                resourceType = instance.resourceType;
                name = instance.name;
                description = instance.description;
                version.addAll(instance.version);
                containedInstance.addAll(instance.containedInstance);
                return this;
            }
        }

        /**
         * A specific version of the resource.
         */
        public static class Version extends BackboneElement {
            @Required
            private final String versionId;
            @Required
            private final Markdown description;

            private Version(Builder builder) {
                super(builder);
                versionId = builder.versionId;
                description = builder.description;
            }

            /**
             * The identifier of a specific version of a resource.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getVersionId() {
                return versionId;
            }

            /**
             * The description of the resource version.
             * 
             * @return
             *     An immutable object of type {@link Markdown} that is non-null.
             */
            public Markdown getDescription() {
                return description;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (versionId != null) || 
                    (description != null);
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
                        accept(versionId, "versionId", visitor);
                        accept(description, "description", visitor);
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
                Version other = (Version) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(versionId, other.versionId) && 
                    Objects.equals(description, other.description);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        versionId, 
                        description);
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
                private String versionId;
                private Markdown description;

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
                 * Convenience method for setting {@code versionId}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param versionId
                 *     The identifier of a specific version of a resource
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #versionId(com.ibm.fhir.model.type.String)
                 */
                public Builder versionId(java.lang.String versionId) {
                    this.versionId = (versionId == null) ? null : String.of(versionId);
                    return this;
                }

                /**
                 * The identifier of a specific version of a resource.
                 * 
                 * <p>This element is required.
                 * 
                 * @param versionId
                 *     The identifier of a specific version of a resource
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder versionId(String versionId) {
                    this.versionId = versionId;
                    return this;
                }

                /**
                 * The description of the resource version.
                 * 
                 * <p>This element is required.
                 * 
                 * @param description
                 *     The description of the resource version
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(Markdown description) {
                    this.description = description;
                    return this;
                }

                /**
                 * Build the {@link Version}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>versionId</li>
                 * <li>description</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Version}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Version per the base specification
                 */
                @Override
                public Version build() {
                    Version version = new Version(this);
                    if (validating) {
                        validate(version);
                    }
                    return version;
                }

                protected void validate(Version version) {
                    super.validate(version);
                    ValidationSupport.requireNonNull(version.versionId, "versionId");
                    ValidationSupport.requireNonNull(version.description, "description");
                    ValidationSupport.requireValueOrChildren(version);
                }

                protected Builder from(Version version) {
                    super.from(version);
                    versionId = version.versionId;
                    description = version.description;
                    return this;
                }
            }
        }

        /**
         * Resources contained in the instance (e.g. the observations contained in a bundle).
         */
        public static class ContainedInstance extends BackboneElement {
            @Required
            private final String resourceId;
            private final String versionId;

            private ContainedInstance(Builder builder) {
                super(builder);
                resourceId = builder.resourceId;
                versionId = builder.versionId;
            }

            /**
             * Each resource contained in the instance.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getResourceId() {
                return resourceId;
            }

            /**
             * A specific version of a resource contained in the instance.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getVersionId() {
                return versionId;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (resourceId != null) || 
                    (versionId != null);
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
                        accept(resourceId, "resourceId", visitor);
                        accept(versionId, "versionId", visitor);
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
                ContainedInstance other = (ContainedInstance) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(resourceId, other.resourceId) && 
                    Objects.equals(versionId, other.versionId);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        resourceId, 
                        versionId);
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
                private String resourceId;
                private String versionId;

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
                 * Convenience method for setting {@code resourceId}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param resourceId
                 *     Each resource contained in the instance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #resourceId(com.ibm.fhir.model.type.String)
                 */
                public Builder resourceId(java.lang.String resourceId) {
                    this.resourceId = (resourceId == null) ? null : String.of(resourceId);
                    return this;
                }

                /**
                 * Each resource contained in the instance.
                 * 
                 * <p>This element is required.
                 * 
                 * @param resourceId
                 *     Each resource contained in the instance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder resourceId(String resourceId) {
                    this.resourceId = resourceId;
                    return this;
                }

                /**
                 * Convenience method for setting {@code versionId}.
                 * 
                 * @param versionId
                 *     A specific version of a resource contained in the instance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #versionId(com.ibm.fhir.model.type.String)
                 */
                public Builder versionId(java.lang.String versionId) {
                    this.versionId = (versionId == null) ? null : String.of(versionId);
                    return this;
                }

                /**
                 * A specific version of a resource contained in the instance.
                 * 
                 * @param versionId
                 *     A specific version of a resource contained in the instance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder versionId(String versionId) {
                    this.versionId = versionId;
                    return this;
                }

                /**
                 * Build the {@link ContainedInstance}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>resourceId</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link ContainedInstance}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid ContainedInstance per the base specification
                 */
                @Override
                public ContainedInstance build() {
                    ContainedInstance containedInstance = new ContainedInstance(this);
                    if (validating) {
                        validate(containedInstance);
                    }
                    return containedInstance;
                }

                protected void validate(ContainedInstance containedInstance) {
                    super.validate(containedInstance);
                    ValidationSupport.requireNonNull(containedInstance.resourceId, "resourceId");
                    ValidationSupport.requireValueOrChildren(containedInstance);
                }

                protected Builder from(ContainedInstance containedInstance) {
                    super.from(containedInstance);
                    resourceId = containedInstance.resourceId;
                    versionId = containedInstance.versionId;
                    return this;
                }
            }
        }
    }

    /**
     * Each major process - a group of operations.
     */
    public static class Process extends BackboneElement {
        @Summary
        @Required
        private final String title;
        private final Markdown description;
        private final Markdown preConditions;
        private final Markdown postConditions;
        private final List<Step> step;

        private Process(Builder builder) {
            super(builder);
            title = builder.title;
            description = builder.description;
            preConditions = builder.preConditions;
            postConditions = builder.postConditions;
            step = Collections.unmodifiableList(builder.step);
        }

        /**
         * The diagram title of the group of operations.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getTitle() {
            return title;
        }

        /**
         * A longer description of the group of operations.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getDescription() {
            return description;
        }

        /**
         * Description of initial status before the process starts.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getPreConditions() {
            return preConditions;
        }

        /**
         * Description of final status after the process ends.
         * 
         * @return
         *     An immutable object of type {@link Markdown} that may be null.
         */
        public Markdown getPostConditions() {
            return postConditions;
        }

        /**
         * Each step of the process.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Step} that may be empty.
         */
        public List<Step> getStep() {
            return step;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (title != null) || 
                (description != null) || 
                (preConditions != null) || 
                (postConditions != null) || 
                !step.isEmpty();
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
                    accept(title, "title", visitor);
                    accept(description, "description", visitor);
                    accept(preConditions, "preConditions", visitor);
                    accept(postConditions, "postConditions", visitor);
                    accept(step, "step", visitor, Step.class);
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
            Process other = (Process) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(title, other.title) && 
                Objects.equals(description, other.description) && 
                Objects.equals(preConditions, other.preConditions) && 
                Objects.equals(postConditions, other.postConditions) && 
                Objects.equals(step, other.step);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    title, 
                    description, 
                    preConditions, 
                    postConditions, 
                    step);
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
            private String title;
            private Markdown description;
            private Markdown preConditions;
            private Markdown postConditions;
            private List<Step> step = new ArrayList<>();

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
             * Convenience method for setting {@code title}.
             * 
             * <p>This element is required.
             * 
             * @param title
             *     The diagram title of the group of operations
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
             * The diagram title of the group of operations.
             * 
             * <p>This element is required.
             * 
             * @param title
             *     The diagram title of the group of operations
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder title(String title) {
                this.title = title;
                return this;
            }

            /**
             * A longer description of the group of operations.
             * 
             * @param description
             *     A longer description of the group of operations
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(Markdown description) {
                this.description = description;
                return this;
            }

            /**
             * Description of initial status before the process starts.
             * 
             * @param preConditions
             *     Description of initial status before the process starts
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder preConditions(Markdown preConditions) {
                this.preConditions = preConditions;
                return this;
            }

            /**
             * Description of final status after the process ends.
             * 
             * @param postConditions
             *     Description of final status after the process ends
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder postConditions(Markdown postConditions) {
                this.postConditions = postConditions;
                return this;
            }

            /**
             * Each step of the process.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param step
             *     Each step of the process
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder step(Step... step) {
                for (Step value : step) {
                    this.step.add(value);
                }
                return this;
            }

            /**
             * Each step of the process.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param step
             *     Each step of the process
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder step(Collection<Step> step) {
                this.step = new ArrayList<>(step);
                return this;
            }

            /**
             * Build the {@link Process}
             * 
             * <p>Required elements:
             * <ul>
             * <li>title</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Process}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Process per the base specification
             */
            @Override
            public Process build() {
                Process process = new Process(this);
                if (validating) {
                    validate(process);
                }
                return process;
            }

            protected void validate(Process process) {
                super.validate(process);
                ValidationSupport.requireNonNull(process.title, "title");
                ValidationSupport.checkList(process.step, "step", Step.class);
                ValidationSupport.requireValueOrChildren(process);
            }

            protected Builder from(Process process) {
                super.from(process);
                title = process.title;
                description = process.description;
                preConditions = process.preConditions;
                postConditions = process.postConditions;
                step.addAll(process.step);
                return this;
            }
        }

        /**
         * Each step of the process.
         */
        public static class Step extends BackboneElement {
            private final List<ExampleScenario.Process> process;
            private final Boolean pause;
            private final Operation operation;
            private final List<Alternative> alternative;

            private Step(Builder builder) {
                super(builder);
                process = Collections.unmodifiableList(builder.process);
                pause = builder.pause;
                operation = builder.operation;
                alternative = Collections.unmodifiableList(builder.alternative);
            }

            /**
             * Nested process.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Process} that may be empty.
             */
            public List<ExampleScenario.Process> getProcess() {
                return process;
            }

            /**
             * If there is a pause in the flow.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getPause() {
                return pause;
            }

            /**
             * Each interaction or action.
             * 
             * @return
             *     An immutable object of type {@link Operation} that may be null.
             */
            public Operation getOperation() {
                return operation;
            }

            /**
             * Indicates an alternative step that can be taken instead of the operations on the base step in exceptional/atypical 
             * circumstances.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Alternative} that may be empty.
             */
            public List<Alternative> getAlternative() {
                return alternative;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    !process.isEmpty() || 
                    (pause != null) || 
                    (operation != null) || 
                    !alternative.isEmpty();
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
                        accept(process, "process", visitor, ExampleScenario.Process.class);
                        accept(pause, "pause", visitor);
                        accept(operation, "operation", visitor);
                        accept(alternative, "alternative", visitor, Alternative.class);
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
                Step other = (Step) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(process, other.process) && 
                    Objects.equals(pause, other.pause) && 
                    Objects.equals(operation, other.operation) && 
                    Objects.equals(alternative, other.alternative);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        process, 
                        pause, 
                        operation, 
                        alternative);
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
                private List<ExampleScenario.Process> process = new ArrayList<>();
                private Boolean pause;
                private Operation operation;
                private List<Alternative> alternative = new ArrayList<>();

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
                 * Nested process.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param process
                 *     Nested process
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder process(ExampleScenario.Process... process) {
                    for (ExampleScenario.Process value : process) {
                        this.process.add(value);
                    }
                    return this;
                }

                /**
                 * Nested process.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param process
                 *     Nested process
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder process(Collection<ExampleScenario.Process> process) {
                    this.process = new ArrayList<>(process);
                    return this;
                }

                /**
                 * Convenience method for setting {@code pause}.
                 * 
                 * @param pause
                 *     If there is a pause in the flow
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #pause(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder pause(java.lang.Boolean pause) {
                    this.pause = (pause == null) ? null : Boolean.of(pause);
                    return this;
                }

                /**
                 * If there is a pause in the flow.
                 * 
                 * @param pause
                 *     If there is a pause in the flow
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder pause(Boolean pause) {
                    this.pause = pause;
                    return this;
                }

                /**
                 * Each interaction or action.
                 * 
                 * @param operation
                 *     Each interaction or action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder operation(Operation operation) {
                    this.operation = operation;
                    return this;
                }

                /**
                 * Indicates an alternative step that can be taken instead of the operations on the base step in exceptional/atypical 
                 * circumstances.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param alternative
                 *     Alternate non-typical step action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder alternative(Alternative... alternative) {
                    for (Alternative value : alternative) {
                        this.alternative.add(value);
                    }
                    return this;
                }

                /**
                 * Indicates an alternative step that can be taken instead of the operations on the base step in exceptional/atypical 
                 * circumstances.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param alternative
                 *     Alternate non-typical step action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder alternative(Collection<Alternative> alternative) {
                    this.alternative = new ArrayList<>(alternative);
                    return this;
                }

                /**
                 * Build the {@link Step}
                 * 
                 * @return
                 *     An immutable object of type {@link Step}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Step per the base specification
                 */
                @Override
                public Step build() {
                    Step step = new Step(this);
                    if (validating) {
                        validate(step);
                    }
                    return step;
                }

                protected void validate(Step step) {
                    super.validate(step);
                    ValidationSupport.checkList(step.process, "process", ExampleScenario.Process.class);
                    ValidationSupport.checkList(step.alternative, "alternative", Alternative.class);
                    ValidationSupport.requireValueOrChildren(step);
                }

                protected Builder from(Step step) {
                    super.from(step);
                    process.addAll(step.process);
                    pause = step.pause;
                    operation = step.operation;
                    alternative.addAll(step.alternative);
                    return this;
                }
            }

            /**
             * Each interaction or action.
             */
            public static class Operation extends BackboneElement {
                @Required
                private final String number;
                private final String type;
                private final String name;
                private final String initiator;
                private final String receiver;
                private final Markdown description;
                private final Boolean initiatorActive;
                private final Boolean receiverActive;
                private final ExampleScenario.Instance.ContainedInstance request;
                private final ExampleScenario.Instance.ContainedInstance response;

                private Operation(Builder builder) {
                    super(builder);
                    number = builder.number;
                    type = builder.type;
                    name = builder.name;
                    initiator = builder.initiator;
                    receiver = builder.receiver;
                    description = builder.description;
                    initiatorActive = builder.initiatorActive;
                    receiverActive = builder.receiverActive;
                    request = builder.request;
                    response = builder.response;
                }

                /**
                 * The sequential number of the interaction, e.g. 1.2.5.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that is non-null.
                 */
                public String getNumber() {
                    return number;
                }

                /**
                 * The type of operation - CRUD.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getType() {
                    return type;
                }

                /**
                 * The human-friendly name of the interaction.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getName() {
                    return name;
                }

                /**
                 * Who starts the transaction.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getInitiator() {
                    return initiator;
                }

                /**
                 * Who receives the transaction.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that may be null.
                 */
                public String getReceiver() {
                    return receiver;
                }

                /**
                 * A comment to be inserted in the diagram.
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown} that may be null.
                 */
                public Markdown getDescription() {
                    return description;
                }

                /**
                 * Whether the initiator is deactivated right after the transaction.
                 * 
                 * @return
                 *     An immutable object of type {@link Boolean} that may be null.
                 */
                public Boolean getInitiatorActive() {
                    return initiatorActive;
                }

                /**
                 * Whether the receiver is deactivated right after the transaction.
                 * 
                 * @return
                 *     An immutable object of type {@link Boolean} that may be null.
                 */
                public Boolean getReceiverActive() {
                    return receiverActive;
                }

                /**
                 * Each resource instance used by the initiator.
                 * 
                 * @return
                 *     An immutable object of type {@link ExampleScenario.Instance.ContainedInstance} that may be null.
                 */
                public ExampleScenario.Instance.ContainedInstance getRequest() {
                    return request;
                }

                /**
                 * Each resource instance used by the responder.
                 * 
                 * @return
                 *     An immutable object of type {@link ExampleScenario.Instance.ContainedInstance} that may be null.
                 */
                public ExampleScenario.Instance.ContainedInstance getResponse() {
                    return response;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (number != null) || 
                        (type != null) || 
                        (name != null) || 
                        (initiator != null) || 
                        (receiver != null) || 
                        (description != null) || 
                        (initiatorActive != null) || 
                        (receiverActive != null) || 
                        (request != null) || 
                        (response != null);
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
                            accept(number, "number", visitor);
                            accept(type, "type", visitor);
                            accept(name, "name", visitor);
                            accept(initiator, "initiator", visitor);
                            accept(receiver, "receiver", visitor);
                            accept(description, "description", visitor);
                            accept(initiatorActive, "initiatorActive", visitor);
                            accept(receiverActive, "receiverActive", visitor);
                            accept(request, "request", visitor);
                            accept(response, "response", visitor);
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
                    Operation other = (Operation) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(number, other.number) && 
                        Objects.equals(type, other.type) && 
                        Objects.equals(name, other.name) && 
                        Objects.equals(initiator, other.initiator) && 
                        Objects.equals(receiver, other.receiver) && 
                        Objects.equals(description, other.description) && 
                        Objects.equals(initiatorActive, other.initiatorActive) && 
                        Objects.equals(receiverActive, other.receiverActive) && 
                        Objects.equals(request, other.request) && 
                        Objects.equals(response, other.response);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            number, 
                            type, 
                            name, 
                            initiator, 
                            receiver, 
                            description, 
                            initiatorActive, 
                            receiverActive, 
                            request, 
                            response);
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
                    private String number;
                    private String type;
                    private String name;
                    private String initiator;
                    private String receiver;
                    private Markdown description;
                    private Boolean initiatorActive;
                    private Boolean receiverActive;
                    private ExampleScenario.Instance.ContainedInstance request;
                    private ExampleScenario.Instance.ContainedInstance response;

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
                     * Convenience method for setting {@code number}.
                     * 
                     * <p>This element is required.
                     * 
                     * @param number
                     *     The sequential number of the interaction
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #number(com.ibm.fhir.model.type.String)
                     */
                    public Builder number(java.lang.String number) {
                        this.number = (number == null) ? null : String.of(number);
                        return this;
                    }

                    /**
                     * The sequential number of the interaction, e.g. 1.2.5.
                     * 
                     * <p>This element is required.
                     * 
                     * @param number
                     *     The sequential number of the interaction
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder number(String number) {
                        this.number = number;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code type}.
                     * 
                     * @param type
                     *     The type of operation - CRUD
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #type(com.ibm.fhir.model.type.String)
                     */
                    public Builder type(java.lang.String type) {
                        this.type = (type == null) ? null : String.of(type);
                        return this;
                    }

                    /**
                     * The type of operation - CRUD.
                     * 
                     * @param type
                     *     The type of operation - CRUD
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder type(String type) {
                        this.type = type;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code name}.
                     * 
                     * @param name
                     *     The human-friendly name of the interaction
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
                     * The human-friendly name of the interaction.
                     * 
                     * @param name
                     *     The human-friendly name of the interaction
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder name(String name) {
                        this.name = name;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code initiator}.
                     * 
                     * @param initiator
                     *     Who starts the transaction
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #initiator(com.ibm.fhir.model.type.String)
                     */
                    public Builder initiator(java.lang.String initiator) {
                        this.initiator = (initiator == null) ? null : String.of(initiator);
                        return this;
                    }

                    /**
                     * Who starts the transaction.
                     * 
                     * @param initiator
                     *     Who starts the transaction
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder initiator(String initiator) {
                        this.initiator = initiator;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code receiver}.
                     * 
                     * @param receiver
                     *     Who receives the transaction
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #receiver(com.ibm.fhir.model.type.String)
                     */
                    public Builder receiver(java.lang.String receiver) {
                        this.receiver = (receiver == null) ? null : String.of(receiver);
                        return this;
                    }

                    /**
                     * Who receives the transaction.
                     * 
                     * @param receiver
                     *     Who receives the transaction
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder receiver(String receiver) {
                        this.receiver = receiver;
                        return this;
                    }

                    /**
                     * A comment to be inserted in the diagram.
                     * 
                     * @param description
                     *     A comment to be inserted in the diagram
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder description(Markdown description) {
                        this.description = description;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code initiatorActive}.
                     * 
                     * @param initiatorActive
                     *     Whether the initiator is deactivated right after the transaction
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #initiatorActive(com.ibm.fhir.model.type.Boolean)
                     */
                    public Builder initiatorActive(java.lang.Boolean initiatorActive) {
                        this.initiatorActive = (initiatorActive == null) ? null : Boolean.of(initiatorActive);
                        return this;
                    }

                    /**
                     * Whether the initiator is deactivated right after the transaction.
                     * 
                     * @param initiatorActive
                     *     Whether the initiator is deactivated right after the transaction
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder initiatorActive(Boolean initiatorActive) {
                        this.initiatorActive = initiatorActive;
                        return this;
                    }

                    /**
                     * Convenience method for setting {@code receiverActive}.
                     * 
                     * @param receiverActive
                     *     Whether the receiver is deactivated right after the transaction
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @see #receiverActive(com.ibm.fhir.model.type.Boolean)
                     */
                    public Builder receiverActive(java.lang.Boolean receiverActive) {
                        this.receiverActive = (receiverActive == null) ? null : Boolean.of(receiverActive);
                        return this;
                    }

                    /**
                     * Whether the receiver is deactivated right after the transaction.
                     * 
                     * @param receiverActive
                     *     Whether the receiver is deactivated right after the transaction
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder receiverActive(Boolean receiverActive) {
                        this.receiverActive = receiverActive;
                        return this;
                    }

                    /**
                     * Each resource instance used by the initiator.
                     * 
                     * @param request
                     *     Each resource instance used by the initiator
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder request(ExampleScenario.Instance.ContainedInstance request) {
                        this.request = request;
                        return this;
                    }

                    /**
                     * Each resource instance used by the responder.
                     * 
                     * @param response
                     *     Each resource instance used by the responder
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder response(ExampleScenario.Instance.ContainedInstance response) {
                        this.response = response;
                        return this;
                    }

                    /**
                     * Build the {@link Operation}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>number</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Operation}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Operation per the base specification
                     */
                    @Override
                    public Operation build() {
                        Operation operation = new Operation(this);
                        if (validating) {
                            validate(operation);
                        }
                        return operation;
                    }

                    protected void validate(Operation operation) {
                        super.validate(operation);
                        ValidationSupport.requireNonNull(operation.number, "number");
                        ValidationSupport.requireValueOrChildren(operation);
                    }

                    protected Builder from(Operation operation) {
                        super.from(operation);
                        number = operation.number;
                        type = operation.type;
                        name = operation.name;
                        initiator = operation.initiator;
                        receiver = operation.receiver;
                        description = operation.description;
                        initiatorActive = operation.initiatorActive;
                        receiverActive = operation.receiverActive;
                        request = operation.request;
                        response = operation.response;
                        return this;
                    }
                }
            }

            /**
             * Indicates an alternative step that can be taken instead of the operations on the base step in exceptional/atypical 
             * circumstances.
             */
            public static class Alternative extends BackboneElement {
                @Required
                private final String title;
                private final Markdown description;
                private final List<ExampleScenario.Process.Step> step;

                private Alternative(Builder builder) {
                    super(builder);
                    title = builder.title;
                    description = builder.description;
                    step = Collections.unmodifiableList(builder.step);
                }

                /**
                 * The label to display for the alternative that gives a sense of the circumstance in which the alternative should be 
                 * invoked.
                 * 
                 * @return
                 *     An immutable object of type {@link String} that is non-null.
                 */
                public String getTitle() {
                    return title;
                }

                /**
                 * A human-readable description of the alternative explaining when the alternative should occur rather than the base step.
                 * 
                 * @return
                 *     An immutable object of type {@link Markdown} that may be null.
                 */
                public Markdown getDescription() {
                    return description;
                }

                /**
                 * What happens in each alternative option.
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Step} that may be empty.
                 */
                public List<ExampleScenario.Process.Step> getStep() {
                    return step;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (title != null) || 
                        (description != null) || 
                        !step.isEmpty();
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
                            accept(title, "title", visitor);
                            accept(description, "description", visitor);
                            accept(step, "step", visitor, ExampleScenario.Process.Step.class);
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
                    Alternative other = (Alternative) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(title, other.title) && 
                        Objects.equals(description, other.description) && 
                        Objects.equals(step, other.step);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            title, 
                            description, 
                            step);
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
                    private String title;
                    private Markdown description;
                    private List<ExampleScenario.Process.Step> step = new ArrayList<>();

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
                     * Convenience method for setting {@code title}.
                     * 
                     * <p>This element is required.
                     * 
                     * @param title
                     *     Label for alternative
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
                     * The label to display for the alternative that gives a sense of the circumstance in which the alternative should be 
                     * invoked.
                     * 
                     * <p>This element is required.
                     * 
                     * @param title
                     *     Label for alternative
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder title(String title) {
                        this.title = title;
                        return this;
                    }

                    /**
                     * A human-readable description of the alternative explaining when the alternative should occur rather than the base step.
                     * 
                     * @param description
                     *     A human-readable description of each option
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder description(Markdown description) {
                        this.description = description;
                        return this;
                    }

                    /**
                     * What happens in each alternative option.
                     * 
                     * <p>Adds new element(s) to the existing list.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param step
                     *     What happens in each alternative option
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder step(ExampleScenario.Process.Step... step) {
                        for (ExampleScenario.Process.Step value : step) {
                            this.step.add(value);
                        }
                        return this;
                    }

                    /**
                     * What happens in each alternative option.
                     * 
                     * <p>Replaces the existing list with a new one containing elements from the Collection.
                     * If any of the elements are null, calling {@link #build()} will fail.
                     * 
                     * @param step
                     *     What happens in each alternative option
                     * 
                     * @return
                     *     A reference to this Builder instance
                     * 
                     * @throws NullPointerException
                     *     If the passed collection is null
                     */
                    public Builder step(Collection<ExampleScenario.Process.Step> step) {
                        this.step = new ArrayList<>(step);
                        return this;
                    }

                    /**
                     * Build the {@link Alternative}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>title</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Alternative}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Alternative per the base specification
                     */
                    @Override
                    public Alternative build() {
                        Alternative alternative = new Alternative(this);
                        if (validating) {
                            validate(alternative);
                        }
                        return alternative;
                    }

                    protected void validate(Alternative alternative) {
                        super.validate(alternative);
                        ValidationSupport.requireNonNull(alternative.title, "title");
                        ValidationSupport.checkList(alternative.step, "step", ExampleScenario.Process.Step.class);
                        ValidationSupport.requireValueOrChildren(alternative);
                    }

                    protected Builder from(Alternative alternative) {
                        super.from(alternative);
                        title = alternative.title;
                        description = alternative.description;
                        step.addAll(alternative.step);
                        return this;
                    }
                }
            }
        }
    }
}
