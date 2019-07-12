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
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.ExampleScenarioActorType;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.FHIRResourceType;
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
 * Example of workflow instance.
 * </p>
 */
@Constraint(
    id = "esc-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ExampleScenario extends DomainResource {
    private final Uri url;
    private final List<Identifier> identifier;
    private final String version;
    private final String name;
    private final PublicationStatus status;
    private final Boolean experimental;
    private final DateTime date;
    private final String publisher;
    private final List<ContactDetail> contact;
    private final List<UsageContext> useContext;
    private final List<CodeableConcept> jurisdiction;
    private final Markdown copyright;
    private final Markdown purpose;
    private final List<Actor> actor;
    private final List<Instance> instance;
    private final List<Process> process;
    private final List<Canonical> workflow;

    private volatile int hashCode;

    private ExampleScenario(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = builder.name;
        status = ValidationSupport.requireNonNull(builder.status, "status");
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
     * <p>
     * An absolute URI that is used to identify this example scenario when it is referenced in a specification, model, design 
     * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
     * at which at which an authoritative instance of this example scenario is (or will be) published. This URL can be the 
     * target of a canonical reference. It SHALL remain the same when the example scenario is stored on different servers.
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
     * A formal identifier that is used to identify this example scenario when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
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
     * The identifier that is used to identify this version of the example scenario when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the example scenario author and is not expected to be 
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
     * A natural language name identifying the example scenario. This name should be usable as an identifier for the module 
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
     * The status of this example scenario. Enables tracking the life-cycle of the content.
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
     * A Boolean value to indicate that this example scenario is authored for testing purposes (or 
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
     * The date (and optionally time) when the example scenario was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the example scenario changes. (e.g. the 'content logical definition').
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
     * The name of the organization or individual that published the example scenario.
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
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate example scenario instances.
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
     * A legal or geographic region in which the example scenario is intended to be used.
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
     * A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the example scenario.
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
     * What the example scenario resource is created for. This should not be used to show the business purpose of the 
     * scenario itself, but the purpose of documenting a scenario.
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
     * Actor participating in the resource.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Actor}.
     */
    public List<Actor> getActor() {
        return actor;
    }

    /**
     * <p>
     * Each resource and each version that is present in the workflow.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Instance}.
     */
    public List<Instance> getInstance() {
        return instance;
    }

    /**
     * <p>
     * Each major process - a group of operations.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Process}.
     */
    public List<Process> getProcess() {
        return process;
    }

    /**
     * <p>
     * Another nested workflow.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getWorkflow() {
        return workflow;
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * An absolute URI that is used to identify this example scenario when it is referenced in a specification, model, design 
         * or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address 
         * at which at which an authoritative instance of this example scenario is (or will be) published. This URL can be the 
         * target of a canonical reference. It SHALL remain the same when the example scenario is stored on different servers.
         * </p>
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
         * <p>
         * A formal identifier that is used to identify this example scenario when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A formal identifier that is used to identify this example scenario when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the example scenario
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
         * The identifier that is used to identify this version of the example scenario when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the example scenario author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence.
         * </p>
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
         * <p>
         * A natural language name identifying the example scenario. This name should be usable as an identifier for the module 
         * by machine processing applications such as code generation.
         * </p>
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
         * <p>
         * A Boolean value to indicate that this example scenario is authored for testing purposes (or 
         * education/evaluation/marketing) and is not intended to be used for genuine usage.
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
         * The date (and optionally time) when the example scenario was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the example scenario changes. (e.g. the 'content logical definition').
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
         * The name of the organization or individual that published the example scenario.
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
         * Adds new element(s) to the existing list
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
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate example scenario instances.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
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
         * may be used to assist with indexing and searching for appropriate example scenario instances.
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
         * A legal or geographic region in which the example scenario is intended to be used.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * A legal or geographic region in which the example scenario is intended to be used.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for example scenario (if applicable)
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
         * A copyright statement relating to the example scenario and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the example scenario.
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
         * What the example scenario resource is created for. This should not be used to show the business purpose of the 
         * scenario itself, but the purpose of documenting a scenario.
         * </p>
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
         * <p>
         * Actor participating in the resource.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Actor participating in the resource.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param actor
         *     Actor participating in the resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder actor(Collection<Actor> actor) {
            this.actor = new ArrayList<>(actor);
            return this;
        }

        /**
         * <p>
         * Each resource and each version that is present in the workflow.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Each resource and each version that is present in the workflow.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param instance
         *     Each resource and each version that is present in the workflow
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instance(Collection<Instance> instance) {
            this.instance = new ArrayList<>(instance);
            return this;
        }

        /**
         * <p>
         * Each major process - a group of operations.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Each major process - a group of operations.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param process
         *     Each major process - a group of operations
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder process(Collection<Process> process) {
            this.process = new ArrayList<>(process);
            return this;
        }

        /**
         * <p>
         * Another nested workflow.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Another nested workflow.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param workflow
         *     Another nested workflow
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder workflow(Collection<Canonical> workflow) {
            this.workflow = new ArrayList<>(workflow);
            return this;
        }

        @Override
        public ExampleScenario build() {
            return new ExampleScenario(this);
        }

        private Builder from(ExampleScenario exampleScenario) {
            id = exampleScenario.id;
            meta = exampleScenario.meta;
            implicitRules = exampleScenario.implicitRules;
            language = exampleScenario.language;
            text = exampleScenario.text;
            contained.addAll(exampleScenario.contained);
            extension.addAll(exampleScenario.extension);
            modifierExtension.addAll(exampleScenario.modifierExtension);
            url = exampleScenario.url;
            identifier.addAll(exampleScenario.identifier);
            version = exampleScenario.version;
            name = exampleScenario.name;
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
     * <p>
     * Actor participating in the resource.
     * </p>
     */
    public static class Actor extends BackboneElement {
        private final String actorId;
        private final ExampleScenarioActorType type;
        private final String name;
        private final Markdown description;

        private volatile int hashCode;

        private Actor(Builder builder) {
            super(builder);
            actorId = ValidationSupport.requireNonNull(builder.actorId, "actorId");
            type = ValidationSupport.requireNonNull(builder.type, "type");
            name = builder.name;
            description = builder.description;
        }

        /**
         * <p>
         * ID or acronym of actor.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getActorId() {
            return actorId;
        }

        /**
         * <p>
         * The type of actor - person or system.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ExampleScenarioActorType}.
         */
        public ExampleScenarioActorType getType() {
            return type;
        }

        /**
         * <p>
         * The name of the actor as shown in the page.
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
         * The description of the actor.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Markdown}.
         */
        public Markdown getDescription() {
            return description;
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
                    accept(actorId, "actorId", visitor);
                    accept(type, "type", visitor);
                    accept(name, "name", visitor);
                    accept(description, "description", visitor);
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
            return new Builder(actorId, type).from(this);
        }

        public Builder toBuilder(String actorId, ExampleScenarioActorType type) {
            return new Builder(actorId, type).from(this);
        }

        public static Builder builder(String actorId, ExampleScenarioActorType type) {
            return new Builder(actorId, type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String actorId;
            private final ExampleScenarioActorType type;

            // optional
            private String name;
            private Markdown description;

            private Builder(String actorId, ExampleScenarioActorType type) {
                super();
                this.actorId = actorId;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * The name of the actor as shown in the page.
             * </p>
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
             * <p>
             * The description of the actor.
             * </p>
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

            @Override
            public Actor build() {
                return new Actor(this);
            }

            private Builder from(Actor actor) {
                id = actor.id;
                extension.addAll(actor.extension);
                modifierExtension.addAll(actor.modifierExtension);
                name = actor.name;
                description = actor.description;
                return this;
            }
        }
    }

    /**
     * <p>
     * Each resource and each version that is present in the workflow.
     * </p>
     */
    public static class Instance extends BackboneElement {
        private final String resourceId;
        private final FHIRResourceType resourceType;
        private final String name;
        private final Markdown description;
        private final List<Version> version;
        private final List<ContainedInstance> containedInstance;

        private volatile int hashCode;

        private Instance(Builder builder) {
            super(builder);
            resourceId = ValidationSupport.requireNonNull(builder.resourceId, "resourceId");
            resourceType = ValidationSupport.requireNonNull(builder.resourceType, "resourceType");
            name = builder.name;
            description = builder.description;
            version = Collections.unmodifiableList(builder.version);
            containedInstance = Collections.unmodifiableList(builder.containedInstance);
        }

        /**
         * <p>
         * The id of the resource for referencing.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getResourceId() {
            return resourceId;
        }

        /**
         * <p>
         * The type of the resource.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link FHIRResourceType}.
         */
        public FHIRResourceType getResourceType() {
            return resourceType;
        }

        /**
         * <p>
         * A short name for the resource instance.
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
         * Human-friendly description of the resource instance.
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
         * A specific version of the resource.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Version}.
         */
        public List<Version> getVersion() {
            return version;
        }

        /**
         * <p>
         * Resources contained in the instance (e.g. the observations contained in a bundle).
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link ContainedInstance}.
         */
        public List<ContainedInstance> getContainedInstance() {
            return containedInstance;
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
                    accept(resourceId, "resourceId", visitor);
                    accept(resourceType, "resourceType", visitor);
                    accept(name, "name", visitor);
                    accept(description, "description", visitor);
                    accept(version, "version", visitor, Version.class);
                    accept(containedInstance, "containedInstance", visitor, ContainedInstance.class);
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
            return new Builder(resourceId, resourceType).from(this);
        }

        public Builder toBuilder(String resourceId, FHIRResourceType resourceType) {
            return new Builder(resourceId, resourceType).from(this);
        }

        public static Builder builder(String resourceId, FHIRResourceType resourceType) {
            return new Builder(resourceId, resourceType);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String resourceId;
            private final FHIRResourceType resourceType;

            // optional
            private String name;
            private Markdown description;
            private List<Version> version = new ArrayList<>();
            private List<ContainedInstance> containedInstance = new ArrayList<>();

            private Builder(String resourceId, FHIRResourceType resourceType) {
                super();
                this.resourceId = resourceId;
                this.resourceType = resourceType;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * A short name for the resource instance.
             * </p>
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
             * <p>
             * Human-friendly description of the resource instance.
             * </p>
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
             * <p>
             * A specific version of the resource.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * A specific version of the resource.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param version
             *     A specific version of the resource
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder version(Collection<Version> version) {
                this.version = new ArrayList<>(version);
                return this;
            }

            /**
             * <p>
             * Resources contained in the instance (e.g. the observations contained in a bundle).
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Resources contained in the instance (e.g. the observations contained in a bundle).
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param containedInstance
             *     Resources contained in the instance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder containedInstance(Collection<ContainedInstance> containedInstance) {
                this.containedInstance = new ArrayList<>(containedInstance);
                return this;
            }

            @Override
            public Instance build() {
                return new Instance(this);
            }

            private Builder from(Instance instance) {
                id = instance.id;
                extension.addAll(instance.extension);
                modifierExtension.addAll(instance.modifierExtension);
                name = instance.name;
                description = instance.description;
                version.addAll(instance.version);
                containedInstance.addAll(instance.containedInstance);
                return this;
            }
        }

        /**
         * <p>
         * A specific version of the resource.
         * </p>
         */
        public static class Version extends BackboneElement {
            private final String versionId;
            private final Markdown description;

            private volatile int hashCode;

            private Version(Builder builder) {
                super(builder);
                versionId = ValidationSupport.requireNonNull(builder.versionId, "versionId");
                description = ValidationSupport.requireNonNull(builder.description, "description");
            }

            /**
             * <p>
             * The identifier of a specific version of a resource.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getVersionId() {
                return versionId;
            }

            /**
             * <p>
             * The description of the resource version.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Markdown}.
             */
            public Markdown getDescription() {
                return description;
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
                        accept(versionId, "versionId", visitor);
                        accept(description, "description", visitor);
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
                return new Builder(versionId, description).from(this);
            }

            public Builder toBuilder(String versionId, Markdown description) {
                return new Builder(versionId, description).from(this);
            }

            public static Builder builder(String versionId, Markdown description) {
                return new Builder(versionId, description);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final String versionId;
                private final Markdown description;

                private Builder(String versionId, Markdown description) {
                    super();
                    this.versionId = versionId;
                    this.description = description;
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
                 * Adds new element(s) to the existing list
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
                 * Adds new element(s) to the existing list
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
                public Version build() {
                    return new Version(this);
                }

                private Builder from(Version version) {
                    id = version.id;
                    extension.addAll(version.extension);
                    modifierExtension.addAll(version.modifierExtension);
                    return this;
                }
            }
        }

        /**
         * <p>
         * Resources contained in the instance (e.g. the observations contained in a bundle).
         * </p>
         */
        public static class ContainedInstance extends BackboneElement {
            private final String resourceId;
            private final String versionId;

            private volatile int hashCode;

            private ContainedInstance(Builder builder) {
                super(builder);
                resourceId = ValidationSupport.requireNonNull(builder.resourceId, "resourceId");
                versionId = builder.versionId;
            }

            /**
             * <p>
             * Each resource contained in the instance.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getResourceId() {
                return resourceId;
            }

            /**
             * <p>
             * A specific version of a resource contained in the instance.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getVersionId() {
                return versionId;
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
                        accept(resourceId, "resourceId", visitor);
                        accept(versionId, "versionId", visitor);
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
                return new Builder(resourceId).from(this);
            }

            public Builder toBuilder(String resourceId) {
                return new Builder(resourceId).from(this);
            }

            public static Builder builder(String resourceId) {
                return new Builder(resourceId);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final String resourceId;

                // optional
                private String versionId;

                private Builder(String resourceId) {
                    super();
                    this.resourceId = resourceId;
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
                 * Adds new element(s) to the existing list
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
                 * Adds new element(s) to the existing list
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
                 * A specific version of a resource contained in the instance.
                 * </p>
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

                @Override
                public ContainedInstance build() {
                    return new ContainedInstance(this);
                }

                private Builder from(ContainedInstance containedInstance) {
                    id = containedInstance.id;
                    extension.addAll(containedInstance.extension);
                    modifierExtension.addAll(containedInstance.modifierExtension);
                    versionId = containedInstance.versionId;
                    return this;
                }
            }
        }
    }

    /**
     * <p>
     * Each major process - a group of operations.
     * </p>
     */
    public static class Process extends BackboneElement {
        private final String title;
        private final Markdown description;
        private final Markdown preConditions;
        private final Markdown postConditions;
        private final List<Step> step;

        private volatile int hashCode;

        private Process(Builder builder) {
            super(builder);
            title = ValidationSupport.requireNonNull(builder.title, "title");
            description = builder.description;
            preConditions = builder.preConditions;
            postConditions = builder.postConditions;
            step = Collections.unmodifiableList(builder.step);
        }

        /**
         * <p>
         * The diagram title of the group of operations.
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
         * A longer description of the group of operations.
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
         * Description of initial status before the process starts.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Markdown}.
         */
        public Markdown getPreConditions() {
            return preConditions;
        }

        /**
         * <p>
         * Description of final status after the process ends.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Markdown}.
         */
        public Markdown getPostConditions() {
            return postConditions;
        }

        /**
         * <p>
         * Each step of the process.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Step}.
         */
        public List<Step> getStep() {
            return step;
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
                    accept(title, "title", visitor);
                    accept(description, "description", visitor);
                    accept(preConditions, "preConditions", visitor);
                    accept(postConditions, "postConditions", visitor);
                    accept(step, "step", visitor, Step.class);
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
            return new Builder(title).from(this);
        }

        public Builder toBuilder(String title) {
            return new Builder(title).from(this);
        }

        public static Builder builder(String title) {
            return new Builder(title);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String title;

            // optional
            private Markdown description;
            private Markdown preConditions;
            private Markdown postConditions;
            private List<Step> step = new ArrayList<>();

            private Builder(String title) {
                super();
                this.title = title;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * A longer description of the group of operations.
             * </p>
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
             * <p>
             * Description of initial status before the process starts.
             * </p>
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
             * <p>
             * Description of final status after the process ends.
             * </p>
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
             * <p>
             * Each step of the process.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Each step of the process.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param step
             *     Each step of the process
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder step(Collection<Step> step) {
                this.step = new ArrayList<>(step);
                return this;
            }

            @Override
            public Process build() {
                return new Process(this);
            }

            private Builder from(Process process) {
                id = process.id;
                extension.addAll(process.extension);
                modifierExtension.addAll(process.modifierExtension);
                description = process.description;
                preConditions = process.preConditions;
                postConditions = process.postConditions;
                step.addAll(process.step);
                return this;
            }
        }

        /**
         * <p>
         * Each step of the process.
         * </p>
         */
        public static class Step extends BackboneElement {
            private final List<ExampleScenario.Process> process;
            private final Boolean pause;
            private final Operation operation;
            private final List<Alternative> alternative;

            private volatile int hashCode;

            private Step(Builder builder) {
                super(builder);
                process = Collections.unmodifiableList(builder.process);
                pause = builder.pause;
                operation = builder.operation;
                alternative = Collections.unmodifiableList(builder.alternative);
            }

            /**
             * <p>
             * Nested process.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Process}.
             */
            public List<ExampleScenario.Process> getProcess() {
                return process;
            }

            /**
             * <p>
             * If there is a pause in the flow.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getPause() {
                return pause;
            }

            /**
             * <p>
             * Each interaction or action.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Operation}.
             */
            public Operation getOperation() {
                return operation;
            }

            /**
             * <p>
             * Indicates an alternative step that can be taken instead of the operations on the base step in exceptional/atypical 
             * circumstances.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Alternative}.
             */
            public List<Alternative> getAlternative() {
                return alternative;
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
                        accept(process, "process", visitor, ExampleScenario.Process.class);
                        accept(pause, "pause", visitor);
                        accept(operation, "operation", visitor);
                        accept(alternative, "alternative", visitor, Alternative.class);
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
                // optional
                private List<ExampleScenario.Process> process = new ArrayList<>();
                private Boolean pause;
                private Operation operation;
                private List<Alternative> alternative = new ArrayList<>();

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
                 * Adds new element(s) to the existing list
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
                 * Adds new element(s) to the existing list
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
                 * Nested process.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
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
                 * <p>
                 * Nested process.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param process
                 *     Nested process
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder process(Collection<ExampleScenario.Process> process) {
                    this.process = new ArrayList<>(process);
                    return this;
                }

                /**
                 * <p>
                 * If there is a pause in the flow.
                 * </p>
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
                 * <p>
                 * Each interaction or action.
                 * </p>
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
                 * <p>
                 * Indicates an alternative step that can be taken instead of the operations on the base step in exceptional/atypical 
                 * circumstances.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
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
                 * <p>
                 * Indicates an alternative step that can be taken instead of the operations on the base step in exceptional/atypical 
                 * circumstances.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param alternative
                 *     Alternate non-typical step action
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder alternative(Collection<Alternative> alternative) {
                    this.alternative = new ArrayList<>(alternative);
                    return this;
                }

                @Override
                public Step build() {
                    return new Step(this);
                }

                private Builder from(Step step) {
                    id = step.id;
                    extension.addAll(step.extension);
                    modifierExtension.addAll(step.modifierExtension);
                    process.addAll(step.process);
                    pause = step.pause;
                    operation = step.operation;
                    alternative.addAll(step.alternative);
                    return this;
                }
            }

            /**
             * <p>
             * Each interaction or action.
             * </p>
             */
            public static class Operation extends BackboneElement {
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

                private volatile int hashCode;

                private Operation(Builder builder) {
                    super(builder);
                    number = ValidationSupport.requireNonNull(builder.number, "number");
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
                 * <p>
                 * The sequential number of the interaction, e.g. 1.2.5.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getNumber() {
                    return number;
                }

                /**
                 * <p>
                 * The type of operation - CRUD.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getType() {
                    return type;
                }

                /**
                 * <p>
                 * The human-friendly name of the interaction.
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
                 * Who starts the transaction.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getInitiator() {
                    return initiator;
                }

                /**
                 * <p>
                 * Who receives the transaction.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link String}.
                 */
                public String getReceiver() {
                    return receiver;
                }

                /**
                 * <p>
                 * A comment to be inserted in the diagram.
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
                 * Whether the initiator is deactivated right after the transaction.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Boolean}.
                 */
                public Boolean getInitiatorActive() {
                    return initiatorActive;
                }

                /**
                 * <p>
                 * Whether the receiver is deactivated right after the transaction.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Boolean}.
                 */
                public Boolean getReceiverActive() {
                    return receiverActive;
                }

                /**
                 * <p>
                 * Each resource instance used by the initiator.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link ContainedInstance}.
                 */
                public ExampleScenario.Instance.ContainedInstance getRequest() {
                    return request;
                }

                /**
                 * <p>
                 * Each resource instance used by the responder.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link ContainedInstance}.
                 */
                public ExampleScenario.Instance.ContainedInstance getResponse() {
                    return response;
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
                    return new Builder(number).from(this);
                }

                public Builder toBuilder(String number) {
                    return new Builder(number).from(this);
                }

                public static Builder builder(String number) {
                    return new Builder(number);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final String number;

                    // optional
                    private String type;
                    private String name;
                    private String initiator;
                    private String receiver;
                    private Markdown description;
                    private Boolean initiatorActive;
                    private Boolean receiverActive;
                    private ExampleScenario.Instance.ContainedInstance request;
                    private ExampleScenario.Instance.ContainedInstance response;

                    private Builder(String number) {
                        super();
                        this.number = number;
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
                     * Adds new element(s) to the existing list
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
                     * Adds new element(s) to the existing list
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
                     * The type of operation - CRUD.
                     * </p>
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
                     * <p>
                     * The human-friendly name of the interaction.
                     * </p>
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
                     * <p>
                     * Who starts the transaction.
                     * </p>
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
                     * <p>
                     * Who receives the transaction.
                     * </p>
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
                     * <p>
                     * A comment to be inserted in the diagram.
                     * </p>
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
                     * <p>
                     * Whether the initiator is deactivated right after the transaction.
                     * </p>
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
                     * <p>
                     * Whether the receiver is deactivated right after the transaction.
                     * </p>
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
                     * <p>
                     * Each resource instance used by the initiator.
                     * </p>
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
                     * <p>
                     * Each resource instance used by the responder.
                     * </p>
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

                    @Override
                    public Operation build() {
                        return new Operation(this);
                    }

                    private Builder from(Operation operation) {
                        id = operation.id;
                        extension.addAll(operation.extension);
                        modifierExtension.addAll(operation.modifierExtension);
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
             * <p>
             * Indicates an alternative step that can be taken instead of the operations on the base step in exceptional/atypical 
             * circumstances.
             * </p>
             */
            public static class Alternative extends BackboneElement {
                private final String title;
                private final Markdown description;
                private final List<ExampleScenario.Process.Step> step;

                private volatile int hashCode;

                private Alternative(Builder builder) {
                    super(builder);
                    title = ValidationSupport.requireNonNull(builder.title, "title");
                    description = builder.description;
                    step = Collections.unmodifiableList(builder.step);
                }

                /**
                 * <p>
                 * The label to display for the alternative that gives a sense of the circumstance in which the alternative should be 
                 * invoked.
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
                 * A human-readable description of the alternative explaining when the alternative should occur rather than the base step.
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
                 * What happens in each alternative option.
                 * </p>
                 * 
                 * @return
                 *     An unmodifiable list containing immutable objects of type {@link Step}.
                 */
                public List<ExampleScenario.Process.Step> getStep() {
                    return step;
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
                            accept(title, "title", visitor);
                            accept(description, "description", visitor);
                            accept(step, "step", visitor, ExampleScenario.Process.Step.class);
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
                    return new Builder(title).from(this);
                }

                public Builder toBuilder(String title) {
                    return new Builder(title).from(this);
                }

                public static Builder builder(String title) {
                    return new Builder(title);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final String title;

                    // optional
                    private Markdown description;
                    private List<ExampleScenario.Process.Step> step = new ArrayList<>();

                    private Builder(String title) {
                        super();
                        this.title = title;
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
                     * Adds new element(s) to the existing list
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
                     * Adds new element(s) to the existing list
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
                     * A human-readable description of the alternative explaining when the alternative should occur rather than the base step.
                     * </p>
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
                     * <p>
                     * What happens in each alternative option.
                     * </p>
                     * <p>
                     * Adds new element(s) to the existing list
                     * </p>
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
                     * <p>
                     * What happens in each alternative option.
                     * </p>
                     * <p>
                     * Replaces existing list with a new one containing elements from the Collection
                     * </p>
                     * 
                     * @param step
                     *     What happens in each alternative option
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder step(Collection<ExampleScenario.Process.Step> step) {
                        this.step = new ArrayList<>(step);
                        return this;
                    }

                    @Override
                    public Alternative build() {
                        return new Alternative(this);
                    }

                    private Builder from(Alternative alternative) {
                        id = alternative.id;
                        extension.addAll(alternative.extension);
                        modifierExtension.addAll(alternative.modifierExtension);
                        description = alternative.description;
                        step.addAll(alternative.step);
                        return this;
                    }
                }
            }
        }
    }
}
