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

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.FHIRVersion;
import com.ibm.watsonhealth.fhir.model.type.GuidePageGeneration;
import com.ibm.watsonhealth.fhir.model.type.GuideParameterCode;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.model.type.SPDXLicense;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Url;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A set of rules of how a particular interoperability or standards problem is solved - typically through the use of FHIR 
 * resources. This resource is used to gather all the parts of an implementation guide into a logical whole and to 
 * publish a computable definition of all the parts.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ImplementationGuide extends DomainResource {
    private final Uri url;
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
    private final Markdown copyright;
    private final Id packageId;
    private final SPDXLicense license;
    private final List<FHIRVersion> fhirVersion;
    private final List<DependsOn> dependsOn;
    private final List<Global> global;
    private final Definition definition;
    private final Manifest manifest;

    private ImplementationGuide(Builder builder) {
        super(builder);
        this.url = ValidationSupport.requireNonNull(builder.url, "url");
        this.version = builder.version;
        this.name = ValidationSupport.requireNonNull(builder.name, "name");
        this.title = builder.title;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.experimental = builder.experimental;
        this.date = builder.date;
        this.publisher = builder.publisher;
        this.contact = builder.contact;
        this.description = builder.description;
        this.useContext = builder.useContext;
        this.jurisdiction = builder.jurisdiction;
        this.copyright = builder.copyright;
        this.packageId = ValidationSupport.requireNonNull(builder.packageId, "packageId");
        this.license = builder.license;
        this.fhirVersion = ValidationSupport.requireNonEmpty(builder.fhirVersion, "fhirVersion");
        this.dependsOn = builder.dependsOn;
        this.global = builder.global;
        this.definition = builder.definition;
        this.manifest = builder.manifest;
    }

    /**
     * <p>
     * An absolute URI that is used to identify this implementation guide when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this implementation guide is (or will be) published. This URL 
     * can be the target of a canonical reference. It SHALL remain the same when the implementation guide is stored on 
     * different servers.
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
     * The identifier that is used to identify this version of the implementation guide when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the implementation guide author and is 
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
     * A natural language name identifying the implementation guide. This name should be usable as an identifier for the 
     * module by machine processing applications such as code generation.
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
     * A short, descriptive, user-friendly title for the implementation guide.
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
     * The status of this implementation guide. Enables tracking the life-cycle of the content.
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
     * A Boolean value to indicate that this implementation guide is authored for testing purposes (or 
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
     * The date (and optionally time) when the implementation guide was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the implementation guide changes.
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
     * The name of the organization or individual that published the implementation guide.
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
     * A free text natural language description of the implementation guide from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate implementation guide instances.
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
     * A legal or geographic region in which the implementation guide is intended to be used.
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
     * A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the implementation guide.
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
     * The NPM package name for this Implementation Guide, used in the NPM package distribution, which is the primary 
     * mechanism by which FHIR based tooling manages IG dependencies. This value must be globally unique, and should be 
     * assigned with care.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Id}.
     */
    public Id getPackageId() {
        return packageId;
    }

    /**
     * <p>
     * The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link SPDXLicense}.
     */
    public SPDXLicense getLicense() {
        return license;
    }

    /**
     * <p>
     * The version(s) of the FHIR specification that this ImplementationGuide targets - e.g. describes how to use. The value 
     * of this element is the formal version of the specification, without the revision number, e.g. [publication].[major].
     * [minor], which is 4.0.0. for this version.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link FHIRVersion}.
     */
    public List<FHIRVersion> getFhirVersion() {
        return fhirVersion;
    }

    /**
     * <p>
     * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, 
     * profiles etc.defined in other implementation guides.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link DependsOn}.
     */
    public List<DependsOn> getDependsOn() {
        return dependsOn;
    }

    /**
     * <p>
     * A set of profiles that all resources covered by this implementation guide must conform to.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Global}.
     */
    public List<Global> getGlobal() {
        return global;
    }

    /**
     * <p>
     * The information needed by an IG publisher tool to publish the whole implementation guide.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Definition}.
     */
    public Definition getDefinition() {
        return definition;
    }

    /**
     * <p>
     * Information about an assembled implementation guide, created by the publication tooling.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Manifest}.
     */
    public Manifest getManifest() {
        return manifest;
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
                accept(contained, "contained", visitor, com.ibm.watsonhealth.fhir.model.resource.Resource.class);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(url, "url", visitor);
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
                accept(copyright, "copyright", visitor);
                accept(packageId, "packageId", visitor);
                accept(license, "license", visitor);
                accept(fhirVersion, "fhirVersion", visitor, FHIRVersion.class);
                accept(dependsOn, "dependsOn", visitor, DependsOn.class);
                accept(global, "global", visitor, Global.class);
                accept(definition, "definition", visitor);
                accept(manifest, "manifest", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(url, name, status, packageId, fhirVersion);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.version = version;
        builder.title = title;
        builder.experimental = experimental;
        builder.date = date;
        builder.publisher = publisher;
        builder.contact.addAll(contact);
        builder.description = description;
        builder.useContext.addAll(useContext);
        builder.jurisdiction.addAll(jurisdiction);
        builder.copyright = copyright;
        builder.license = license;
        builder.dependsOn.addAll(dependsOn);
        builder.global.addAll(global);
        builder.definition = definition;
        builder.manifest = manifest;
        return builder;
    }

    public static Builder builder(Uri url, String name, PublicationStatus status, Id packageId, List<FHIRVersion> fhirVersion) {
        return new Builder(url, name, status, packageId, fhirVersion);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Uri url;
        private final String name;
        private final PublicationStatus status;
        private final Id packageId;
        private final List<FHIRVersion> fhirVersion;

        // optional
        private String version;
        private String title;
        private Boolean experimental;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown copyright;
        private SPDXLicense license;
        private List<DependsOn> dependsOn = new ArrayList<>();
        private List<Global> global = new ArrayList<>();
        private Definition definition;
        private Manifest manifest;

        private Builder(Uri url, String name, PublicationStatus status, Id packageId, List<FHIRVersion> fhirVersion) {
            super();
            this.url = url;
            this.name = name;
            this.status = status;
            this.packageId = packageId;
            this.fhirVersion = fhirVersion;
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
        public Builder contained(com.ibm.watsonhealth.fhir.model.resource.Resource... contained) {
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
        public Builder contained(Collection<com.ibm.watsonhealth.fhir.model.resource.Resource> contained) {
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
         * The identifier that is used to identify this version of the implementation guide when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the implementation guide author and is 
         * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
         * available. There is also no expectation that versions can be placed in a lexicographical sequence.
         * </p>
         * 
         * @param version
         *     Business version of the implementation guide
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
         * A short, descriptive, user-friendly title for the implementation guide.
         * </p>
         * 
         * @param title
         *     Name for this implementation guide (human friendly)
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
         * A Boolean value to indicate that this implementation guide is authored for testing purposes (or 
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
         * The date (and optionally time) when the implementation guide was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the implementation guide changes.
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
         * The name of the organization or individual that published the implementation guide.
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
         * A free text natural language description of the implementation guide from a consumer's perspective.
         * </p>
         * 
         * @param description
         *     Natural language description of the implementation guide
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
         * may be used to assist with indexing and searching for appropriate implementation guide instances.
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
         * may be used to assist with indexing and searching for appropriate implementation guide instances.
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
         * A legal or geographic region in which the implementation guide is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for implementation guide (if applicable)
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
         * A legal or geographic region in which the implementation guide is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for implementation guide (if applicable)
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
         * A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the implementation guide.
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
         * The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'.
         * </p>
         * 
         * @param license
         *     SPDX license code for this IG (or not-open-source)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder license(SPDXLicense license) {
            this.license = license;
            return this;
        }

        /**
         * <p>
         * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, 
         * profiles etc.defined in other implementation guides.
         * </p>
         * 
         * @param dependsOn
         *     Another Implementation guide this depends on
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dependsOn(DependsOn... dependsOn) {
            for (DependsOn value : dependsOn) {
                this.dependsOn.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, 
         * profiles etc.defined in other implementation guides.
         * </p>
         * 
         * @param dependsOn
         *     Another Implementation guide this depends on
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dependsOn(Collection<DependsOn> dependsOn) {
            this.dependsOn.addAll(dependsOn);
            return this;
        }

        /**
         * <p>
         * A set of profiles that all resources covered by this implementation guide must conform to.
         * </p>
         * 
         * @param global
         *     Profiles that apply globally
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder global(Global... global) {
            for (Global value : global) {
                this.global.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A set of profiles that all resources covered by this implementation guide must conform to.
         * </p>
         * 
         * @param global
         *     Profiles that apply globally
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder global(Collection<Global> global) {
            this.global.addAll(global);
            return this;
        }

        /**
         * <p>
         * The information needed by an IG publisher tool to publish the whole implementation guide.
         * </p>
         * 
         * @param definition
         *     Information needed to build the IG
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder definition(Definition definition) {
            this.definition = definition;
            return this;
        }

        /**
         * <p>
         * Information about an assembled implementation guide, created by the publication tooling.
         * </p>
         * 
         * @param manifest
         *     Information about an assembled IG
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder manifest(Manifest manifest) {
            this.manifest = manifest;
            return this;
        }

        @Override
        public ImplementationGuide build() {
            return new ImplementationGuide(this);
        }
    }

    /**
     * <p>
     * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, 
     * profiles etc.defined in other implementation guides.
     * </p>
     */
    public static class DependsOn extends BackboneElement {
        private final Canonical uri;
        private final Id packageId;
        private final String version;

        private DependsOn(Builder builder) {
            super(builder);
            this.uri = ValidationSupport.requireNonNull(builder.uri, "uri");
            this.packageId = builder.packageId;
            this.version = builder.version;
        }

        /**
         * <p>
         * A canonical reference to the Implementation guide for the dependency.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getUri() {
            return uri;
        }

        /**
         * <p>
         * The NPM package name for the Implementation Guide that this IG depends on.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Id}.
         */
        public Id getPackageId() {
            return packageId;
        }

        /**
         * <p>
         * The version of the IG that is depended on, when the correct version is required to understand the IG correctly.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getVersion() {
            return version;
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
                    accept(uri, "uri", visitor);
                    accept(packageId, "packageId", visitor);
                    accept(version, "version", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Canonical uri) {
            return new Builder(uri);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Canonical uri;

            // optional
            private Id packageId;
            private String version;

            private Builder(Canonical uri) {
                super();
                this.uri = uri;
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
             * The NPM package name for the Implementation Guide that this IG depends on.
             * </p>
             * 
             * @param packageId
             *     NPM Package name for IG this depends on
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder packageId(Id packageId) {
                this.packageId = packageId;
                return this;
            }

            /**
             * <p>
             * The version of the IG that is depended on, when the correct version is required to understand the IG correctly.
             * </p>
             * 
             * @param version
             *     Version of the IG
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder version(String version) {
                this.version = version;
                return this;
            }

            @Override
            public DependsOn build() {
                return new DependsOn(this);
            }

            private static Builder from(DependsOn dependsOn) {
                Builder builder = new Builder(dependsOn.uri);
                builder.id = dependsOn.id;
                builder.extension.addAll(dependsOn.extension);
                builder.modifierExtension.addAll(dependsOn.modifierExtension);
                builder.packageId = dependsOn.packageId;
                builder.version = dependsOn.version;
                return builder;
            }
        }
    }

    /**
     * <p>
     * A set of profiles that all resources covered by this implementation guide must conform to.
     * </p>
     */
    public static class Global extends BackboneElement {
        private final ResourceType type;
        private final Canonical profile;

        private Global(Builder builder) {
            super(builder);
            this.type = ValidationSupport.requireNonNull(builder.type, "type");
            this.profile = ValidationSupport.requireNonNull(builder.profile, "profile");
        }

        /**
         * <p>
         * The type of resource that all instances must conform to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ResourceType}.
         */
        public ResourceType getType() {
            return type;
        }

        /**
         * <p>
         * A reference to the profile that all instances must conform to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getProfile() {
            return profile;
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
                    accept(type, "type", visitor);
                    accept(profile, "profile", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(ResourceType type, Canonical profile) {
            return new Builder(type, profile);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final ResourceType type;
            private final Canonical profile;

            private Builder(ResourceType type, Canonical profile) {
                super();
                this.type = type;
                this.profile = profile;
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
            public Global build() {
                return new Global(this);
            }

            private static Builder from(Global global) {
                Builder builder = new Builder(global.type, global.profile);
                builder.id = global.id;
                builder.extension.addAll(global.extension);
                builder.modifierExtension.addAll(global.modifierExtension);
                return builder;
            }
        }
    }

    /**
     * <p>
     * The information needed by an IG publisher tool to publish the whole implementation guide.
     * </p>
     */
    public static class Definition extends BackboneElement {
        private final List<Grouping> grouping;
        private final List<Resource> resource;
        private final Page page;
        private final List<Parameter> parameter;
        private final List<Template> template;

        private Definition(Builder builder) {
            super(builder);
            this.grouping = builder.grouping;
            this.resource = ValidationSupport.requireNonEmpty(builder.resource, "resource");
            this.page = builder.page;
            this.parameter = builder.parameter;
            this.template = builder.template;
        }

        /**
         * <p>
         * A logical group of resources. Logical groups can be used when building pages.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Grouping}.
         */
        public List<Grouping> getGrouping() {
            return grouping;
        }

        /**
         * <p>
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
         * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
         * example resource.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Resource}.
         */
        public List<Resource> getResource() {
            return resource;
        }

        /**
         * <p>
         * A page / section in the implementation guide. The root page is the implementation guide home page.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Page}.
         */
        public Page getPage() {
            return page;
        }

        /**
         * <p>
         * Defines how IG is built by tools.
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
         * A template for building resources.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Template}.
         */
        public List<Template> getTemplate() {
            return template;
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
                    accept(grouping, "grouping", visitor, Grouping.class);
                    accept(resource, "resource", visitor, Resource.class);
                    accept(page, "page", visitor);
                    accept(parameter, "parameter", visitor, Parameter.class);
                    accept(template, "template", visitor, Template.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(List<Resource> resource) {
            return new Builder(resource);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final List<Resource> resource;

            // optional
            private List<Grouping> grouping = new ArrayList<>();
            private Page page;
            private List<Parameter> parameter = new ArrayList<>();
            private List<Template> template = new ArrayList<>();

            private Builder(List<Resource> resource) {
                super();
                this.resource = resource;
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
             * A logical group of resources. Logical groups can be used when building pages.
             * </p>
             * 
             * @param grouping
             *     Grouping used to present related resources in the IG
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder grouping(Grouping... grouping) {
                for (Grouping value : grouping) {
                    this.grouping.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A logical group of resources. Logical groups can be used when building pages.
             * </p>
             * 
             * @param grouping
             *     Grouping used to present related resources in the IG
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder grouping(Collection<Grouping> grouping) {
                this.grouping.addAll(grouping);
                return this;
            }

            /**
             * <p>
             * A page / section in the implementation guide. The root page is the implementation guide home page.
             * </p>
             * 
             * @param page
             *     Page/Section in the Guide
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder page(Page page) {
                this.page = page;
                return this;
            }

            /**
             * <p>
             * Defines how IG is built by tools.
             * </p>
             * 
             * @param parameter
             *     Defines how IG is built by tools
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
             * Defines how IG is built by tools.
             * </p>
             * 
             * @param parameter
             *     Defines how IG is built by tools
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
             * A template for building resources.
             * </p>
             * 
             * @param template
             *     A template for building resources
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder template(Template... template) {
                for (Template value : template) {
                    this.template.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A template for building resources.
             * </p>
             * 
             * @param template
             *     A template for building resources
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder template(Collection<Template> template) {
                this.template.addAll(template);
                return this;
            }

            @Override
            public Definition build() {
                return new Definition(this);
            }

            private static Builder from(Definition definition) {
                Builder builder = new Builder(definition.resource);
                builder.id = definition.id;
                builder.extension.addAll(definition.extension);
                builder.modifierExtension.addAll(definition.modifierExtension);
                builder.grouping.addAll(definition.grouping);
                builder.page = definition.page;
                builder.parameter.addAll(definition.parameter);
                builder.template.addAll(definition.template);
                return builder;
            }
        }

        /**
         * <p>
         * A logical group of resources. Logical groups can be used when building pages.
         * </p>
         */
        public static class Grouping extends BackboneElement {
            private final String name;
            private final String description;

            private Grouping(Builder builder) {
                super(builder);
                this.name = ValidationSupport.requireNonNull(builder.name, "name");
                this.description = builder.description;
            }

            /**
             * <p>
             * The human-readable title to display for the package of resources when rendering the implementation guide.
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
             * Human readable text describing the package.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getDescription() {
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
                        accept(name, "name", visitor);
                        accept(description, "description", visitor);
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
                private String description;

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
                 * Human readable text describing the package.
                 * </p>
                 * 
                 * @param description
                 *     Human readable text describing the package
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                @Override
                public Grouping build() {
                    return new Grouping(this);
                }

                private static Builder from(Grouping grouping) {
                    Builder builder = new Builder(grouping.name);
                    builder.id = grouping.id;
                    builder.extension.addAll(grouping.extension);
                    builder.modifierExtension.addAll(grouping.modifierExtension);
                    builder.description = grouping.description;
                    return builder;
                }
            }
        }

        /**
         * <p>
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
         * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
         * example resource.
         * </p>
         */
        public static class Resource extends BackboneElement {
            private final Reference reference;
            private final List<FHIRVersion> fhirVersion;
            private final String name;
            private final String description;
            private final Element example;
            private final Id groupingId;

            private Resource(Builder builder) {
                super(builder);
                this.reference = ValidationSupport.requireNonNull(builder.reference, "reference");
                this.fhirVersion = builder.fhirVersion;
                this.name = builder.name;
                this.description = builder.description;
                this.example = ValidationSupport.choiceElement(builder.example, "example", Boolean.class, Canonical.class);
                this.groupingId = builder.groupingId;
            }

            /**
             * <p>
             * Where this resource is found.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Reference}.
             */
            public Reference getReference() {
                return reference;
            }

            /**
             * <p>
             * Indicates the FHIR Version(s) this artifact is intended to apply to. If no versions are specified, the resource is 
             * assumed to apply to all the versions stated in ImplementationGuide.fhirVersion.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link FHIRVersion}.
             */
            public List<FHIRVersion> getFhirVersion() {
                return fhirVersion;
            }

            /**
             * <p>
             * A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the 
             * resource (e.g. ValueSet.name).
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
             * A description of the reason that a resource has been included in the implementation guide.
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
             * If true or a reference, indicates the resource is an example instance. If a reference is present, indicates that the 
             * example is an example of the specified profile.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getExample() {
                return example;
            }

            /**
             * <p>
             * Reference to the id of the grouping this resource appears in.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Id}.
             */
            public Id getGroupingId() {
                return groupingId;
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
                        accept(reference, "reference", visitor);
                        accept(fhirVersion, "fhirVersion", visitor, FHIRVersion.class);
                        accept(name, "name", visitor);
                        accept(description, "description", visitor);
                        accept(example, "example", visitor, true);
                        accept(groupingId, "groupingId", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(Reference reference) {
                return new Builder(reference);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Reference reference;

                // optional
                private List<FHIRVersion> fhirVersion = new ArrayList<>();
                private String name;
                private String description;
                private Element example;
                private Id groupingId;

                private Builder(Reference reference) {
                    super();
                    this.reference = reference;
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
                 * Indicates the FHIR Version(s) this artifact is intended to apply to. If no versions are specified, the resource is 
                 * assumed to apply to all the versions stated in ImplementationGuide.fhirVersion.
                 * </p>
                 * 
                 * @param fhirVersion
                 *     Versions this applies to (if different to IG)
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder fhirVersion(FHIRVersion... fhirVersion) {
                    for (FHIRVersion value : fhirVersion) {
                        this.fhirVersion.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Indicates the FHIR Version(s) this artifact is intended to apply to. If no versions are specified, the resource is 
                 * assumed to apply to all the versions stated in ImplementationGuide.fhirVersion.
                 * </p>
                 * 
                 * @param fhirVersion
                 *     Versions this applies to (if different to IG)
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder fhirVersion(Collection<FHIRVersion> fhirVersion) {
                    this.fhirVersion.addAll(fhirVersion);
                    return this;
                }

                /**
                 * <p>
                 * A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the 
                 * resource (e.g. ValueSet.name).
                 * </p>
                 * 
                 * @param name
                 *     Human Name for the resource
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
                 * A description of the reason that a resource has been included in the implementation guide.
                 * </p>
                 * 
                 * @param description
                 *     Reason why included in guide
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                /**
                 * <p>
                 * If true or a reference, indicates the resource is an example instance. If a reference is present, indicates that the 
                 * example is an example of the specified profile.
                 * </p>
                 * 
                 * @param example
                 *     Is an example/What is this an example of?
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder example(Element example) {
                    this.example = example;
                    return this;
                }

                /**
                 * <p>
                 * Reference to the id of the grouping this resource appears in.
                 * </p>
                 * 
                 * @param groupingId
                 *     Grouping this is part of
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder groupingId(Id groupingId) {
                    this.groupingId = groupingId;
                    return this;
                }

                @Override
                public Resource build() {
                    return new Resource(this);
                }

                private static Builder from(Resource resource) {
                    Builder builder = new Builder(resource.reference);
                    builder.id = resource.id;
                    builder.extension.addAll(resource.extension);
                    builder.modifierExtension.addAll(resource.modifierExtension);
                    builder.fhirVersion.addAll(resource.fhirVersion);
                    builder.name = resource.name;
                    builder.description = resource.description;
                    builder.example = resource.example;
                    builder.groupingId = resource.groupingId;
                    return builder;
                }
            }
        }

        /**
         * <p>
         * A page / section in the implementation guide. The root page is the implementation guide home page.
         * </p>
         */
        public static class Page extends BackboneElement {
            private final Element name;
            private final String title;
            private final GuidePageGeneration generation;
            private final List<ImplementationGuide.Definition.Page> page;

            private Page(Builder builder) {
                super(builder);
                this.name = ValidationSupport.requireChoiceElement(builder.name, "name", Url.class, Reference.class);
                this.title = ValidationSupport.requireNonNull(builder.title, "title");
                this.generation = ValidationSupport.requireNonNull(builder.generation, "generation");
                this.page = builder.page;
            }

            /**
             * <p>
             * The source address for the page.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getName() {
                return name;
            }

            /**
             * <p>
             * A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
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
             * A code that indicates how the page is generated.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link GuidePageGeneration}.
             */
            public GuidePageGeneration getGeneration() {
                return generation;
            }

            /**
             * <p>
             * Nested Pages/Sections under this page.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link Page}.
             */
            public List<ImplementationGuide.Definition.Page> getPage() {
                return page;
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
                        accept(name, "name", visitor, true);
                        accept(title, "title", visitor);
                        accept(generation, "generation", visitor);
                        accept(page, "page", visitor, ImplementationGuide.Definition.Page.class);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(Element name, String title, GuidePageGeneration generation) {
                return new Builder(name, title, generation);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Element name;
                private final String title;
                private final GuidePageGeneration generation;

                // optional
                private List<ImplementationGuide.Definition.Page> page = new ArrayList<>();

                private Builder(Element name, String title, GuidePageGeneration generation) {
                    super();
                    this.name = name;
                    this.title = title;
                    this.generation = generation;
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
                 * Nested Pages/Sections under this page.
                 * </p>
                 * 
                 * @param page
                 *     Nested Pages / Sections
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder page(ImplementationGuide.Definition.Page... page) {
                    for (ImplementationGuide.Definition.Page value : page) {
                        this.page.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Nested Pages/Sections under this page.
                 * </p>
                 * 
                 * @param page
                 *     Nested Pages / Sections
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder page(Collection<ImplementationGuide.Definition.Page> page) {
                    this.page.addAll(page);
                    return this;
                }

                @Override
                public Page build() {
                    return new Page(this);
                }

                private static Builder from(Page page) {
                    Builder builder = new Builder(page.name, page.title, page.generation);
                    builder.id = page.id;
                    builder.extension.addAll(page.extension);
                    builder.modifierExtension.addAll(page.modifierExtension);
                    builder.page.addAll(page.page);
                    return builder;
                }
            }
        }

        /**
         * <p>
         * Defines how IG is built by tools.
         * </p>
         */
        public static class Parameter extends BackboneElement {
            private final GuideParameterCode code;
            private final String value;

            private Parameter(Builder builder) {
                super(builder);
                this.code = ValidationSupport.requireNonNull(builder.code, "code");
                this.value = ValidationSupport.requireNonNull(builder.value, "value");
            }

            /**
             * <p>
             * apply | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-
             * json | generate-turtle | html-template.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link GuideParameterCode}.
             */
            public GuideParameterCode getCode() {
                return code;
            }

            /**
             * <p>
             * Value for named type.
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

            public static Builder builder(GuideParameterCode code, String value) {
                return new Builder(code, value);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final GuideParameterCode code;
                private final String value;

                private Builder(GuideParameterCode code, String value) {
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
                public Parameter build() {
                    return new Parameter(this);
                }

                private static Builder from(Parameter parameter) {
                    Builder builder = new Builder(parameter.code, parameter.value);
                    builder.id = parameter.id;
                    builder.extension.addAll(parameter.extension);
                    builder.modifierExtension.addAll(parameter.modifierExtension);
                    return builder;
                }
            }
        }

        /**
         * <p>
         * A template for building resources.
         * </p>
         */
        public static class Template extends BackboneElement {
            private final Code code;
            private final String source;
            private final String scope;

            private Template(Builder builder) {
                super(builder);
                this.code = ValidationSupport.requireNonNull(builder.code, "code");
                this.source = ValidationSupport.requireNonNull(builder.source, "source");
                this.scope = builder.scope;
            }

            /**
             * <p>
             * Type of template specified.
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
             * The source location for the template.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getSource() {
                return source;
            }

            /**
             * <p>
             * The scope in which the template applies.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getScope() {
                return scope;
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
                        accept(source, "source", visitor);
                        accept(scope, "scope", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(Code code, String source) {
                return new Builder(code, source);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Code code;
                private final String source;

                // optional
                private String scope;

                private Builder(Code code, String source) {
                    super();
                    this.code = code;
                    this.source = source;
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
                 * The scope in which the template applies.
                 * </p>
                 * 
                 * @param scope
                 *     The scope in which the template applies
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder scope(String scope) {
                    this.scope = scope;
                    return this;
                }

                @Override
                public Template build() {
                    return new Template(this);
                }

                private static Builder from(Template template) {
                    Builder builder = new Builder(template.code, template.source);
                    builder.id = template.id;
                    builder.extension.addAll(template.extension);
                    builder.modifierExtension.addAll(template.modifierExtension);
                    builder.scope = template.scope;
                    return builder;
                }
            }
        }
    }

    /**
     * <p>
     * Information about an assembled implementation guide, created by the publication tooling.
     * </p>
     */
    public static class Manifest extends BackboneElement {
        private final Url rendering;
        private final List<Resource> resource;
        private final List<Page> page;
        private final List<String> image;
        private final List<String> other;

        private Manifest(Builder builder) {
            super(builder);
            this.rendering = builder.rendering;
            this.resource = ValidationSupport.requireNonEmpty(builder.resource, "resource");
            this.page = builder.page;
            this.image = builder.image;
            this.other = builder.other;
        }

        /**
         * <p>
         * A pointer to official web page, PDF or other rendering of the implementation guide.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Url}.
         */
        public Url getRendering() {
            return rendering;
        }

        /**
         * <p>
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
         * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
         * example resource.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Resource}.
         */
        public List<Resource> getResource() {
            return resource;
        }

        /**
         * <p>
         * Information about a page within the IG.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Page}.
         */
        public List<Page> getPage() {
            return page;
        }

        /**
         * <p>
         * Indicates a relative path to an image that exists within the IG.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link String}.
         */
        public List<String> getImage() {
            return image;
        }

        /**
         * <p>
         * Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and 
         * similar files that could be the target of a hyperlink in a derived IG.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link String}.
         */
        public List<String> getOther() {
            return other;
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
                    accept(rendering, "rendering", visitor);
                    accept(resource, "resource", visitor, Resource.class);
                    accept(page, "page", visitor, Page.class);
                    accept(image, "image", visitor, String.class);
                    accept(other, "other", visitor, String.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(List<Resource> resource) {
            return new Builder(resource);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final List<Resource> resource;

            // optional
            private Url rendering;
            private List<Page> page = new ArrayList<>();
            private List<String> image = new ArrayList<>();
            private List<String> other = new ArrayList<>();

            private Builder(List<Resource> resource) {
                super();
                this.resource = resource;
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
             * A pointer to official web page, PDF or other rendering of the implementation guide.
             * </p>
             * 
             * @param rendering
             *     Location of rendered implementation guide
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder rendering(Url rendering) {
                this.rendering = rendering;
                return this;
            }

            /**
             * <p>
             * Information about a page within the IG.
             * </p>
             * 
             * @param page
             *     HTML page within the parent IG
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder page(Page... page) {
                for (Page value : page) {
                    this.page.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Information about a page within the IG.
             * </p>
             * 
             * @param page
             *     HTML page within the parent IG
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder page(Collection<Page> page) {
                this.page.addAll(page);
                return this;
            }

            /**
             * <p>
             * Indicates a relative path to an image that exists within the IG.
             * </p>
             * 
             * @param image
             *     Image within the IG
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder image(String... image) {
                for (String value : image) {
                    this.image.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Indicates a relative path to an image that exists within the IG.
             * </p>
             * 
             * @param image
             *     Image within the IG
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder image(Collection<String> image) {
                this.image.addAll(image);
                return this;
            }

            /**
             * <p>
             * Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and 
             * similar files that could be the target of a hyperlink in a derived IG.
             * </p>
             * 
             * @param other
             *     Additional linkable file in IG
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder other(String... other) {
                for (String value : other) {
                    this.other.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and 
             * similar files that could be the target of a hyperlink in a derived IG.
             * </p>
             * 
             * @param other
             *     Additional linkable file in IG
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder other(Collection<String> other) {
                this.other.addAll(other);
                return this;
            }

            @Override
            public Manifest build() {
                return new Manifest(this);
            }

            private static Builder from(Manifest manifest) {
                Builder builder = new Builder(manifest.resource);
                builder.id = manifest.id;
                builder.extension.addAll(manifest.extension);
                builder.modifierExtension.addAll(manifest.modifierExtension);
                builder.rendering = manifest.rendering;
                builder.page.addAll(manifest.page);
                builder.image.addAll(manifest.image);
                builder.other.addAll(manifest.other);
                return builder;
            }
        }

        /**
         * <p>
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
         * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
         * example resource.
         * </p>
         */
        public static class Resource extends BackboneElement {
            private final Reference reference;
            private final Element example;
            private final Url relativePath;

            private Resource(Builder builder) {
                super(builder);
                this.reference = ValidationSupport.requireNonNull(builder.reference, "reference");
                this.example = ValidationSupport.choiceElement(builder.example, "example", Boolean.class, Canonical.class);
                this.relativePath = builder.relativePath;
            }

            /**
             * <p>
             * Where this resource is found.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Reference}.
             */
            public Reference getReference() {
                return reference;
            }

            /**
             * <p>
             * If true or a reference, indicates the resource is an example instance. If a reference is present, indicates that the 
             * example is an example of the specified profile.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getExample() {
                return example;
            }

            /**
             * <p>
             * The relative path for primary page for this resource within the IG.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Url}.
             */
            public Url getRelativePath() {
                return relativePath;
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
                        accept(reference, "reference", visitor);
                        accept(example, "example", visitor, true);
                        accept(relativePath, "relativePath", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(Reference reference) {
                return new Builder(reference);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Reference reference;

                // optional
                private Element example;
                private Url relativePath;

                private Builder(Reference reference) {
                    super();
                    this.reference = reference;
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
                 * If true or a reference, indicates the resource is an example instance. If a reference is present, indicates that the 
                 * example is an example of the specified profile.
                 * </p>
                 * 
                 * @param example
                 *     Is an example/What is this an example of?
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder example(Element example) {
                    this.example = example;
                    return this;
                }

                /**
                 * <p>
                 * The relative path for primary page for this resource within the IG.
                 * </p>
                 * 
                 * @param relativePath
                 *     Relative path for page in IG
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder relativePath(Url relativePath) {
                    this.relativePath = relativePath;
                    return this;
                }

                @Override
                public Resource build() {
                    return new Resource(this);
                }

                private static Builder from(Resource resource) {
                    Builder builder = new Builder(resource.reference);
                    builder.id = resource.id;
                    builder.extension.addAll(resource.extension);
                    builder.modifierExtension.addAll(resource.modifierExtension);
                    builder.example = resource.example;
                    builder.relativePath = resource.relativePath;
                    return builder;
                }
            }
        }

        /**
         * <p>
         * Information about a page within the IG.
         * </p>
         */
        public static class Page extends BackboneElement {
            private final String name;
            private final String title;
            private final List<String> anchor;

            private Page(Builder builder) {
                super(builder);
                this.name = ValidationSupport.requireNonNull(builder.name, "name");
                this.title = builder.title;
                this.anchor = builder.anchor;
            }

            /**
             * <p>
             * Relative path to the page.
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
             * Label for the page intended for human display.
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
             * The name of an anchor available on the page.
             * </p>
             * 
             * @return
             *     A list containing immutable objects of type {@link String}.
             */
            public List<String> getAnchor() {
                return anchor;
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
                        accept(title, "title", visitor);
                        accept(anchor, "anchor", visitor, String.class);
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
                private String title;
                private List<String> anchor = new ArrayList<>();

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
                 * Label for the page intended for human display.
                 * </p>
                 * 
                 * @param title
                 *     Title of the page, for references
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
                 * The name of an anchor available on the page.
                 * </p>
                 * 
                 * @param anchor
                 *     Anchor available on the page
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder anchor(String... anchor) {
                    for (String value : anchor) {
                        this.anchor.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The name of an anchor available on the page.
                 * </p>
                 * 
                 * @param anchor
                 *     Anchor available on the page
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder anchor(Collection<String> anchor) {
                    this.anchor.addAll(anchor);
                    return this;
                }

                @Override
                public Page build() {
                    return new Page(this);
                }

                private static Builder from(Page page) {
                    Builder builder = new Builder(page.name);
                    builder.id = page.id;
                    builder.extension.addAll(page.extension);
                    builder.modifierExtension.addAll(page.modifierExtension);
                    builder.title = page.title;
                    builder.anchor.addAll(page.anchor);
                    return builder;
                }
            }
        }
    }
}
