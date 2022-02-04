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
import com.ibm.fhir.model.annotation.ReferenceTarget;
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
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.FHIRVersion;
import com.ibm.fhir.model.type.code.GuidePageGeneration;
import com.ibm.fhir.model.type.code.GuideParameterCode;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.SPDXLicense;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A set of rules of how a particular interoperability or standards problem is solved - typically through the use of FHIR 
 * resources. This resource is used to gather all the parts of an implementation guide into a logical whole and to 
 * publish a computable definition of all the parts.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "ig-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/ImplementationGuide"
)
@Constraint(
    id = "ig-1",
    level = "Rule",
    location = "ImplementationGuide.definition",
    description = "If a resource has a groupingId, it must refer to a grouping defined in the Implementation Guide",
    expression = "resource.groupingId.all(%context.grouping.id contains $this)",
    source = "http://hl7.org/fhir/StructureDefinition/ImplementationGuide"
)
@Constraint(
    id = "ig-2",
    level = "Rule",
    location = "(base)",
    description = "If a resource has a fhirVersion, it must be oe of the versions defined for the Implementation Guide",
    expression = "definition.resource.fhirVersion.all(%context.fhirVersion contains $this)",
    source = "http://hl7.org/fhir/StructureDefinition/ImplementationGuide"
)
@Constraint(
    id = "implementationGuide-3",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/ImplementationGuide",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ImplementationGuide extends DomainResource {
    @Summary
    @Required
    private final Uri url;
    @Summary
    private final String version;
    @Summary
    @Required
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
    private final Markdown copyright;
    @Summary
    @Required
    private final Id packageId;
    @Summary
    @Binding(
        bindingName = "SPDXLicense",
        strength = BindingStrength.Value.REQUIRED,
        description = "The license that applies to an Implementation Guide (using an SPDX license Identifiers, or 'not-open-source'). The binding is required but new SPDX license Identifiers are allowed to be used (https://spdx.org/licenses/).",
        valueSet = "http://hl7.org/fhir/ValueSet/spdx-license|4.3.0-CIBUILD"
    )
    private final SPDXLicense license;
    @Summary
    @Binding(
        bindingName = "FHIRVersion",
        strength = BindingStrength.Value.REQUIRED,
        description = "All published FHIR Versions.",
        valueSet = "http://hl7.org/fhir/ValueSet/FHIR-version|4.3.0-CIBUILD"
    )
    @Required
    private final List<FHIRVersion> fhirVersion;
    @Summary
    private final List<DependsOn> dependsOn;
    @Summary
    private final List<Global> global;
    private final Definition definition;
    private final Manifest manifest;

    private ImplementationGuide(Builder builder) {
        super(builder);
        url = builder.url;
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
        copyright = builder.copyright;
        packageId = builder.packageId;
        license = builder.license;
        fhirVersion = Collections.unmodifiableList(builder.fhirVersion);
        dependsOn = Collections.unmodifiableList(builder.dependsOn);
        global = Collections.unmodifiableList(builder.global);
        definition = builder.definition;
        manifest = builder.manifest;
    }

    /**
     * An absolute URI that is used to identify this implementation guide when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this implementation guide is (or will be) published. This URL 
     * can be the target of a canonical reference. It SHALL remain the same when the implementation guide is stored on 
     * different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that is non-null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * The identifier that is used to identify this version of the implementation guide when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the implementation guide author and is 
     * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
     * available. There is also no expectation that versions can be placed in a lexicographical sequence.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the implementation guide. This name should be usable as an identifier for the 
     * module by machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that is non-null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the implementation guide.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The status of this implementation guide. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this implementation guide is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the implementation guide was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the implementation guide changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the implementation guide.
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
     * A free text natural language description of the implementation guide from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate implementation guide instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the implementation guide is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the implementation guide.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * The NPM package name for this Implementation Guide, used in the NPM package distribution, which is the primary 
     * mechanism by which FHIR based tooling manages IG dependencies. This value must be globally unique, and should be 
     * assigned with care.
     * 
     * @return
     *     An immutable object of type {@link Id} that is non-null.
     */
    public Id getPackageId() {
        return packageId;
    }

    /**
     * The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'.
     * 
     * @return
     *     An immutable object of type {@link SPDXLicense} that may be null.
     */
    public SPDXLicense getLicense() {
        return license;
    }

    /**
     * The version(s) of the FHIR specification that this ImplementationGuide targets - e.g. describes how to use. The value 
     * of this element is the formal version of the specification, without the revision number, e.g. [publication].[major].
     * [minor], which is 4.3.0-CIBUILD. for this version.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link FHIRVersion} that is non-empty.
     */
    public List<FHIRVersion> getFhirVersion() {
        return fhirVersion;
    }

    /**
     * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, 
     * profiles etc.defined in other implementation guides.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link DependsOn} that may be empty.
     */
    public List<DependsOn> getDependsOn() {
        return dependsOn;
    }

    /**
     * A set of profiles that all resources covered by this implementation guide must conform to.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Global} that may be empty.
     */
    public List<Global> getGlobal() {
        return global;
    }

    /**
     * The information needed by an IG publisher tool to publish the whole implementation guide.
     * 
     * @return
     *     An immutable object of type {@link Definition} that may be null.
     */
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Information about an assembled implementation guide, created by the publication tooling.
     * 
     * @return
     *     An immutable object of type {@link Manifest} that may be null.
     */
    public Manifest getManifest() {
        return manifest;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
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
            (copyright != null) || 
            (packageId != null) || 
            (license != null) || 
            !fhirVersion.isEmpty() || 
            !dependsOn.isEmpty() || 
            !global.isEmpty() || 
            (definition != null) || 
            (manifest != null);
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
                accept(contained, "contained", visitor, com.ibm.fhir.model.resource.Resource.class);
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
        ImplementationGuide other = (ImplementationGuide) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(url, other.url) && 
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
            Objects.equals(copyright, other.copyright) && 
            Objects.equals(packageId, other.packageId) && 
            Objects.equals(license, other.license) && 
            Objects.equals(fhirVersion, other.fhirVersion) && 
            Objects.equals(dependsOn, other.dependsOn) && 
            Objects.equals(global, other.global) && 
            Objects.equals(definition, other.definition) && 
            Objects.equals(manifest, other.manifest);
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
                copyright, 
                packageId, 
                license, 
                fhirVersion, 
                dependsOn, 
                global, 
                definition, 
                manifest);
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
        private Markdown copyright;
        private Id packageId;
        private SPDXLicense license;
        private List<FHIRVersion> fhirVersion = new ArrayList<>();
        private List<DependsOn> dependsOn = new ArrayList<>();
        private List<Global> global = new ArrayList<>();
        private Definition definition;
        private Manifest manifest;

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
        public Builder contained(com.ibm.fhir.model.resource.Resource... contained) {
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
        public Builder contained(Collection<com.ibm.fhir.model.resource.Resource> contained) {
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
         * An absolute URI that is used to identify this implementation guide when it is referenced in a specification, model, 
         * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
         * address at which at which an authoritative instance of this implementation guide is (or will be) published. This URL 
         * can be the target of a canonical reference. It SHALL remain the same when the implementation guide is stored on 
         * different servers.
         * 
         * <p>This element is required.
         * 
         * @param url
         *     Canonical identifier for this implementation guide, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * Convenience method for setting {@code version}.
         * 
         * @param version
         *     Business version of the implementation guide
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
         * The identifier that is used to identify this version of the implementation guide when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the implementation guide author and is 
         * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
         * available. There is also no expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of the implementation guide
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
         * <p>This element is required.
         * 
         * @param name
         *     Name for this implementation guide (computer friendly)
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
         * A natural language name identifying the implementation guide. This name should be usable as an identifier for the 
         * module by machine processing applications such as code generation.
         * 
         * <p>This element is required.
         * 
         * @param name
         *     Name for this implementation guide (computer friendly)
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
         *     Name for this implementation guide (human friendly)
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
         * A short, descriptive, user-friendly title for the implementation guide.
         * 
         * @param title
         *     Name for this implementation guide (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The status of this implementation guide. Enables tracking the life-cycle of the content.
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
         * A Boolean value to indicate that this implementation guide is authored for testing purposes (or 
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
         * The date (and optionally time) when the implementation guide was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the implementation guide changes.
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
         * The name of the organization or individual that published the implementation guide.
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
         * A free text natural language description of the implementation guide from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the implementation guide
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
         * may be used to assist with indexing and searching for appropriate implementation guide instances.
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
         * may be used to assist with indexing and searching for appropriate implementation guide instances.
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
         * A legal or geographic region in which the implementation guide is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for implementation guide (if applicable)
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
         * A legal or geographic region in which the implementation guide is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for implementation guide (if applicable)
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
         * A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the implementation guide.
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
         * The NPM package name for this Implementation Guide, used in the NPM package distribution, which is the primary 
         * mechanism by which FHIR based tooling manages IG dependencies. This value must be globally unique, and should be 
         * assigned with care.
         * 
         * <p>This element is required.
         * 
         * @param packageId
         *     NPM Package name for IG
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder packageId(Id packageId) {
            this.packageId = packageId;
            return this;
        }

        /**
         * The license that applies to this Implementation Guide, using an SPDX license code, or 'not-open-source'.
         * 
         * @param license
         *     SPDX license code for this IG (or not-open-source)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder license(SPDXLicense license) {
            this.license = license;
            return this;
        }

        /**
         * The version(s) of the FHIR specification that this ImplementationGuide targets - e.g. describes how to use. The value 
         * of this element is the formal version of the specification, without the revision number, e.g. [publication].[major].
         * [minor], which is 4.3.0-CIBUILD. for this version.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param fhirVersion
         *     FHIR Version(s) this Implementation Guide targets
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder fhirVersion(FHIRVersion... fhirVersion) {
            for (FHIRVersion value : fhirVersion) {
                this.fhirVersion.add(value);
            }
            return this;
        }

        /**
         * The version(s) of the FHIR specification that this ImplementationGuide targets - e.g. describes how to use. The value 
         * of this element is the formal version of the specification, without the revision number, e.g. [publication].[major].
         * [minor], which is 4.3.0-CIBUILD. for this version.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * @param fhirVersion
         *     FHIR Version(s) this Implementation Guide targets
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder fhirVersion(Collection<FHIRVersion> fhirVersion) {
            this.fhirVersion = new ArrayList<>(fhirVersion);
            return this;
        }

        /**
         * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, 
         * profiles etc.defined in other implementation guides.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param dependsOn
         *     Another Implementation guide this depends on
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
         * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, 
         * profiles etc.defined in other implementation guides.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param dependsOn
         *     Another Implementation guide this depends on
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
         * A set of profiles that all resources covered by this implementation guide must conform to.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param global
         *     Profiles that apply globally
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder global(Global... global) {
            for (Global value : global) {
                this.global.add(value);
            }
            return this;
        }

        /**
         * A set of profiles that all resources covered by this implementation guide must conform to.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param global
         *     Profiles that apply globally
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder global(Collection<Global> global) {
            this.global = new ArrayList<>(global);
            return this;
        }

        /**
         * The information needed by an IG publisher tool to publish the whole implementation guide.
         * 
         * @param definition
         *     Information needed to build the IG
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder definition(Definition definition) {
            this.definition = definition;
            return this;
        }

        /**
         * Information about an assembled implementation guide, created by the publication tooling.
         * 
         * @param manifest
         *     Information about an assembled IG
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manifest(Manifest manifest) {
            this.manifest = manifest;
            return this;
        }

        /**
         * Build the {@link ImplementationGuide}
         * 
         * <p>Required elements:
         * <ul>
         * <li>url</li>
         * <li>name</li>
         * <li>status</li>
         * <li>packageId</li>
         * <li>fhirVersion</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ImplementationGuide}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ImplementationGuide per the base specification
         */
        @Override
        public ImplementationGuide build() {
            ImplementationGuide implementationGuide = new ImplementationGuide(this);
            if (validating) {
                validate(implementationGuide);
            }
            return implementationGuide;
        }

        protected void validate(ImplementationGuide implementationGuide) {
            super.validate(implementationGuide);
            ValidationSupport.requireNonNull(implementationGuide.url, "url");
            ValidationSupport.requireNonNull(implementationGuide.name, "name");
            ValidationSupport.requireNonNull(implementationGuide.status, "status");
            ValidationSupport.checkList(implementationGuide.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(implementationGuide.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(implementationGuide.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.requireNonNull(implementationGuide.packageId, "packageId");
            ValidationSupport.checkNonEmptyList(implementationGuide.fhirVersion, "fhirVersion", FHIRVersion.class);
            ValidationSupport.checkList(implementationGuide.dependsOn, "dependsOn", DependsOn.class);
            ValidationSupport.checkList(implementationGuide.global, "global", Global.class);
        }

        protected Builder from(ImplementationGuide implementationGuide) {
            super.from(implementationGuide);
            url = implementationGuide.url;
            version = implementationGuide.version;
            name = implementationGuide.name;
            title = implementationGuide.title;
            status = implementationGuide.status;
            experimental = implementationGuide.experimental;
            date = implementationGuide.date;
            publisher = implementationGuide.publisher;
            contact.addAll(implementationGuide.contact);
            description = implementationGuide.description;
            useContext.addAll(implementationGuide.useContext);
            jurisdiction.addAll(implementationGuide.jurisdiction);
            copyright = implementationGuide.copyright;
            packageId = implementationGuide.packageId;
            license = implementationGuide.license;
            fhirVersion.addAll(implementationGuide.fhirVersion);
            dependsOn.addAll(implementationGuide.dependsOn);
            global.addAll(implementationGuide.global);
            definition = implementationGuide.definition;
            manifest = implementationGuide.manifest;
            return this;
        }
    }

    /**
     * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, 
     * profiles etc.defined in other implementation guides.
     */
    public static class DependsOn extends BackboneElement {
        @Summary
        @Required
        private final Canonical uri;
        @Summary
        private final Id packageId;
        @Summary
        private final String version;

        private DependsOn(Builder builder) {
            super(builder);
            uri = builder.uri;
            packageId = builder.packageId;
            version = builder.version;
        }

        /**
         * A canonical reference to the Implementation guide for the dependency.
         * 
         * @return
         *     An immutable object of type {@link Canonical} that is non-null.
         */
        public Canonical getUri() {
            return uri;
        }

        /**
         * The NPM package name for the Implementation Guide that this IG depends on.
         * 
         * @return
         *     An immutable object of type {@link Id} that may be null.
         */
        public Id getPackageId() {
            return packageId;
        }

        /**
         * The version of the IG that is depended on, when the correct version is required to understand the IG correctly.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getVersion() {
            return version;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (uri != null) || 
                (packageId != null) || 
                (version != null);
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
                    accept(uri, "uri", visitor);
                    accept(packageId, "packageId", visitor);
                    accept(version, "version", visitor);
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
                Objects.equals(uri, other.uri) && 
                Objects.equals(packageId, other.packageId) && 
                Objects.equals(version, other.version);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    uri, 
                    packageId, 
                    version);
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
            private Canonical uri;
            private Id packageId;
            private String version;

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
             * A canonical reference to the Implementation guide for the dependency.
             * 
             * <p>This element is required.
             * 
             * @param uri
             *     Identity of the IG that this depends on
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder uri(Canonical uri) {
                this.uri = uri;
                return this;
            }

            /**
             * The NPM package name for the Implementation Guide that this IG depends on.
             * 
             * @param packageId
             *     NPM Package name for IG this depends on
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder packageId(Id packageId) {
                this.packageId = packageId;
                return this;
            }

            /**
             * Convenience method for setting {@code version}.
             * 
             * @param version
             *     Version of the IG
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
             * The version of the IG that is depended on, when the correct version is required to understand the IG correctly.
             * 
             * @param version
             *     Version of the IG
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder version(String version) {
                this.version = version;
                return this;
            }

            /**
             * Build the {@link DependsOn}
             * 
             * <p>Required elements:
             * <ul>
             * <li>uri</li>
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
                ValidationSupport.requireNonNull(dependsOn.uri, "uri");
                ValidationSupport.requireValueOrChildren(dependsOn);
            }

            protected Builder from(DependsOn dependsOn) {
                super.from(dependsOn);
                uri = dependsOn.uri;
                packageId = dependsOn.packageId;
                version = dependsOn.version;
                return this;
            }
        }
    }

    /**
     * A set of profiles that all resources covered by this implementation guide must conform to.
     */
    public static class Global extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "ResourceType",
            strength = BindingStrength.Value.REQUIRED,
            description = "One of the resource types defined as part of this version of FHIR.",
            valueSet = "http://hl7.org/fhir/ValueSet/resource-types|4.3.0-CIBUILD"
        )
        @Required
        private final ResourceTypeCode type;
        @Summary
        @Required
        private final Canonical profile;

        private Global(Builder builder) {
            super(builder);
            type = builder.type;
            profile = builder.profile;
        }

        /**
         * The type of resource that all instances must conform to.
         * 
         * @return
         *     An immutable object of type {@link ResourceTypeCode} that is non-null.
         */
        public ResourceTypeCode getType() {
            return type;
        }

        /**
         * A reference to the profile that all instances must conform to.
         * 
         * @return
         *     An immutable object of type {@link Canonical} that is non-null.
         */
        public Canonical getProfile() {
            return profile;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (profile != null);
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
                    accept(type, "type", visitor);
                    accept(profile, "profile", visitor);
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
            Global other = (Global) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(profile, other.profile);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    profile);
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
            private ResourceTypeCode type;
            private Canonical profile;

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
             * The type of resource that all instances must conform to.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     Type this profile applies to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(ResourceTypeCode type) {
                this.type = type;
                return this;
            }

            /**
             * A reference to the profile that all instances must conform to.
             * 
             * <p>This element is required.
             * 
             * @param profile
             *     Profile that all resources must conform to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder profile(Canonical profile) {
                this.profile = profile;
                return this;
            }

            /**
             * Build the {@link Global}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * <li>profile</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Global}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Global per the base specification
             */
            @Override
            public Global build() {
                Global global = new Global(this);
                if (validating) {
                    validate(global);
                }
                return global;
            }

            protected void validate(Global global) {
                super.validate(global);
                ValidationSupport.requireNonNull(global.type, "type");
                ValidationSupport.requireNonNull(global.profile, "profile");
                ValidationSupport.requireValueOrChildren(global);
            }

            protected Builder from(Global global) {
                super.from(global);
                type = global.type;
                profile = global.profile;
                return this;
            }
        }
    }

    /**
     * The information needed by an IG publisher tool to publish the whole implementation guide.
     */
    public static class Definition extends BackboneElement {
        private final List<Grouping> grouping;
        @Required
        private final List<Resource> resource;
        private final Page page;
        private final List<Parameter> parameter;
        private final List<Template> template;

        private Definition(Builder builder) {
            super(builder);
            grouping = Collections.unmodifiableList(builder.grouping);
            resource = Collections.unmodifiableList(builder.resource);
            page = builder.page;
            parameter = Collections.unmodifiableList(builder.parameter);
            template = Collections.unmodifiableList(builder.template);
        }

        /**
         * A logical group of resources. Logical groups can be used when building pages.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Grouping} that may be empty.
         */
        public List<Grouping> getGrouping() {
            return grouping;
        }

        /**
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
         * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
         * example resource.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Resource} that is non-empty.
         */
        public List<Resource> getResource() {
            return resource;
        }

        /**
         * A page / section in the implementation guide. The root page is the implementation guide home page.
         * 
         * @return
         *     An immutable object of type {@link Page} that may be null.
         */
        public Page getPage() {
            return page;
        }

        /**
         * Defines how IG is built by tools.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Parameter} that may be empty.
         */
        public List<Parameter> getParameter() {
            return parameter;
        }

        /**
         * A template for building resources.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Template} that may be empty.
         */
        public List<Template> getTemplate() {
            return template;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !grouping.isEmpty() || 
                !resource.isEmpty() || 
                (page != null) || 
                !parameter.isEmpty() || 
                !template.isEmpty();
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
                    accept(grouping, "grouping", visitor, Grouping.class);
                    accept(resource, "resource", visitor, Resource.class);
                    accept(page, "page", visitor);
                    accept(parameter, "parameter", visitor, Parameter.class);
                    accept(template, "template", visitor, Template.class);
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
            Definition other = (Definition) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(grouping, other.grouping) && 
                Objects.equals(resource, other.resource) && 
                Objects.equals(page, other.page) && 
                Objects.equals(parameter, other.parameter) && 
                Objects.equals(template, other.template);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    grouping, 
                    resource, 
                    page, 
                    parameter, 
                    template);
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
            private List<Grouping> grouping = new ArrayList<>();
            private List<Resource> resource = new ArrayList<>();
            private Page page;
            private List<Parameter> parameter = new ArrayList<>();
            private List<Template> template = new ArrayList<>();

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
             * A logical group of resources. Logical groups can be used when building pages.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param grouping
             *     Grouping used to present related resources in the IG
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder grouping(Grouping... grouping) {
                for (Grouping value : grouping) {
                    this.grouping.add(value);
                }
                return this;
            }

            /**
             * A logical group of resources. Logical groups can be used when building pages.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param grouping
             *     Grouping used to present related resources in the IG
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder grouping(Collection<Grouping> grouping) {
                this.grouping = new ArrayList<>(grouping);
                return this;
            }

            /**
             * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
             * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
             * example resource.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param resource
             *     Resource in the implementation guide
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Resource... resource) {
                for (Resource value : resource) {
                    this.resource.add(value);
                }
                return this;
            }

            /**
             * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
             * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
             * example resource.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param resource
             *     Resource in the implementation guide
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder resource(Collection<Resource> resource) {
                this.resource = new ArrayList<>(resource);
                return this;
            }

            /**
             * A page / section in the implementation guide. The root page is the implementation guide home page.
             * 
             * @param page
             *     Page/Section in the Guide
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder page(Page page) {
                this.page = page;
                return this;
            }

            /**
             * Defines how IG is built by tools.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param parameter
             *     Defines how IG is built by tools
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
             * Defines how IG is built by tools.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param parameter
             *     Defines how IG is built by tools
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
             * A template for building resources.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param template
             *     A template for building resources
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder template(Template... template) {
                for (Template value : template) {
                    this.template.add(value);
                }
                return this;
            }

            /**
             * A template for building resources.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param template
             *     A template for building resources
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder template(Collection<Template> template) {
                this.template = new ArrayList<>(template);
                return this;
            }

            /**
             * Build the {@link Definition}
             * 
             * <p>Required elements:
             * <ul>
             * <li>resource</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Definition}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Definition per the base specification
             */
            @Override
            public Definition build() {
                Definition definition = new Definition(this);
                if (validating) {
                    validate(definition);
                }
                return definition;
            }

            protected void validate(Definition definition) {
                super.validate(definition);
                ValidationSupport.checkList(definition.grouping, "grouping", Grouping.class);
                ValidationSupport.checkNonEmptyList(definition.resource, "resource", Resource.class);
                ValidationSupport.checkList(definition.parameter, "parameter", Parameter.class);
                ValidationSupport.checkList(definition.template, "template", Template.class);
                ValidationSupport.requireValueOrChildren(definition);
            }

            protected Builder from(Definition definition) {
                super.from(definition);
                grouping.addAll(definition.grouping);
                resource.addAll(definition.resource);
                page = definition.page;
                parameter.addAll(definition.parameter);
                template.addAll(definition.template);
                return this;
            }
        }

        /**
         * A logical group of resources. Logical groups can be used when building pages.
         */
        public static class Grouping extends BackboneElement {
            @Required
            private final String name;
            private final String description;

            private Grouping(Builder builder) {
                super(builder);
                name = builder.name;
                description = builder.description;
            }

            /**
             * The human-readable title to display for the package of resources when rendering the implementation guide.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getName() {
                return name;
            }

            /**
             * Human readable text describing the package.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDescription() {
                return description;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
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
                Grouping other = (Grouping) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
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
                private String name;
                private String description;

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
                 *     Descriptive name for the package
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
                 * The human-readable title to display for the package of resources when rendering the implementation guide.
                 * 
                 * <p>This element is required.
                 * 
                 * @param name
                 *     Descriptive name for the package
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder name(String name) {
                    this.name = name;
                    return this;
                }

                /**
                 * Convenience method for setting {@code description}.
                 * 
                 * @param description
                 *     Human readable text describing the package
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #description(com.ibm.fhir.model.type.String)
                 */
                public Builder description(java.lang.String description) {
                    this.description = (description == null) ? null : String.of(description);
                    return this;
                }

                /**
                 * Human readable text describing the package.
                 * 
                 * @param description
                 *     Human readable text describing the package
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                /**
                 * Build the {@link Grouping}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>name</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Grouping}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Grouping per the base specification
                 */
                @Override
                public Grouping build() {
                    Grouping grouping = new Grouping(this);
                    if (validating) {
                        validate(grouping);
                    }
                    return grouping;
                }

                protected void validate(Grouping grouping) {
                    super.validate(grouping);
                    ValidationSupport.requireNonNull(grouping.name, "name");
                    ValidationSupport.requireValueOrChildren(grouping);
                }

                protected Builder from(Grouping grouping) {
                    super.from(grouping);
                    name = grouping.name;
                    description = grouping.description;
                    return this;
                }
            }
        }

        /**
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
         * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
         * example resource.
         */
        public static class Resource extends BackboneElement {
            @Required
            private final Reference reference;
            @Binding(
                bindingName = "FHIRVersion",
                strength = BindingStrength.Value.REQUIRED,
                description = "All published FHIR Versions.",
                valueSet = "http://hl7.org/fhir/ValueSet/FHIR-version|4.3.0-CIBUILD"
            )
            private final List<FHIRVersion> fhirVersion;
            private final String name;
            private final String description;
            @Choice({ Boolean.class, Canonical.class })
            private final Element example;
            private final Id groupingId;

            private Resource(Builder builder) {
                super(builder);
                reference = builder.reference;
                fhirVersion = Collections.unmodifiableList(builder.fhirVersion);
                name = builder.name;
                description = builder.description;
                example = builder.example;
                groupingId = builder.groupingId;
            }

            /**
             * Where this resource is found.
             * 
             * @return
             *     An immutable object of type {@link Reference} that is non-null.
             */
            public Reference getReference() {
                return reference;
            }

            /**
             * Indicates the FHIR Version(s) this artifact is intended to apply to. If no versions are specified, the resource is 
             * assumed to apply to all the versions stated in ImplementationGuide.fhirVersion.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link FHIRVersion} that may be empty.
             */
            public List<FHIRVersion> getFhirVersion() {
                return fhirVersion;
            }

            /**
             * A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the 
             * resource (e.g. ValueSet.name).
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getName() {
                return name;
            }

            /**
             * A description of the reason that a resource has been included in the implementation guide.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDescription() {
                return description;
            }

            /**
             * If true or a reference, indicates the resource is an example instance. If a reference is present, indicates that the 
             * example is an example of the specified profile.
             * 
             * @return
             *     An immutable object of type {@link Boolean} or {@link Canonical} that may be null.
             */
            public Element getExample() {
                return example;
            }

            /**
             * Reference to the id of the grouping this resource appears in.
             * 
             * @return
             *     An immutable object of type {@link Id} that may be null.
             */
            public Id getGroupingId() {
                return groupingId;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (reference != null) || 
                    !fhirVersion.isEmpty() || 
                    (name != null) || 
                    (description != null) || 
                    (example != null) || 
                    (groupingId != null);
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
                        accept(reference, "reference", visitor);
                        accept(fhirVersion, "fhirVersion", visitor, FHIRVersion.class);
                        accept(name, "name", visitor);
                        accept(description, "description", visitor);
                        accept(example, "example", visitor);
                        accept(groupingId, "groupingId", visitor);
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
                Resource other = (Resource) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(reference, other.reference) && 
                    Objects.equals(fhirVersion, other.fhirVersion) && 
                    Objects.equals(name, other.name) && 
                    Objects.equals(description, other.description) && 
                    Objects.equals(example, other.example) && 
                    Objects.equals(groupingId, other.groupingId);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        reference, 
                        fhirVersion, 
                        name, 
                        description, 
                        example, 
                        groupingId);
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
                private Reference reference;
                private List<FHIRVersion> fhirVersion = new ArrayList<>();
                private String name;
                private String description;
                private Element example;
                private Id groupingId;

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
                 * Where this resource is found.
                 * 
                 * <p>This element is required.
                 * 
                 * @param reference
                 *     Location of the resource
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reference(Reference reference) {
                    this.reference = reference;
                    return this;
                }

                /**
                 * Indicates the FHIR Version(s) this artifact is intended to apply to. If no versions are specified, the resource is 
                 * assumed to apply to all the versions stated in ImplementationGuide.fhirVersion.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param fhirVersion
                 *     Versions this applies to (if different to IG)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder fhirVersion(FHIRVersion... fhirVersion) {
                    for (FHIRVersion value : fhirVersion) {
                        this.fhirVersion.add(value);
                    }
                    return this;
                }

                /**
                 * Indicates the FHIR Version(s) this artifact is intended to apply to. If no versions are specified, the resource is 
                 * assumed to apply to all the versions stated in ImplementationGuide.fhirVersion.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param fhirVersion
                 *     Versions this applies to (if different to IG)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder fhirVersion(Collection<FHIRVersion> fhirVersion) {
                    this.fhirVersion = new ArrayList<>(fhirVersion);
                    return this;
                }

                /**
                 * Convenience method for setting {@code name}.
                 * 
                 * @param name
                 *     Human Name for the resource
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
                 * A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the 
                 * resource (e.g. ValueSet.name).
                 * 
                 * @param name
                 *     Human Name for the resource
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder name(String name) {
                    this.name = name;
                    return this;
                }

                /**
                 * Convenience method for setting {@code description}.
                 * 
                 * @param description
                 *     Reason why included in guide
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #description(com.ibm.fhir.model.type.String)
                 */
                public Builder description(java.lang.String description) {
                    this.description = (description == null) ? null : String.of(description);
                    return this;
                }

                /**
                 * A description of the reason that a resource has been included in the implementation guide.
                 * 
                 * @param description
                 *     Reason why included in guide
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                /**
                 * Convenience method for setting {@code example} with choice type Boolean.
                 * 
                 * @param example
                 *     Is an example/What is this an example of?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #example(Element)
                 */
                public Builder example(java.lang.Boolean example) {
                    this.example = (example == null) ? null : Boolean.of(example);
                    return this;
                }

                /**
                 * If true or a reference, indicates the resource is an example instance. If a reference is present, indicates that the 
                 * example is an example of the specified profile.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Boolean}</li>
                 * <li>{@link Canonical}</li>
                 * </ul>
                 * 
                 * @param example
                 *     Is an example/What is this an example of?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder example(Element example) {
                    this.example = example;
                    return this;
                }

                /**
                 * Reference to the id of the grouping this resource appears in.
                 * 
                 * @param groupingId
                 *     Grouping this is part of
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder groupingId(Id groupingId) {
                    this.groupingId = groupingId;
                    return this;
                }

                /**
                 * Build the {@link Resource}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>reference</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Resource}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Resource per the base specification
                 */
                @Override
                public Resource build() {
                    Resource resource = new Resource(this);
                    if (validating) {
                        validate(resource);
                    }
                    return resource;
                }

                protected void validate(Resource resource) {
                    super.validate(resource);
                    ValidationSupport.requireNonNull(resource.reference, "reference");
                    ValidationSupport.checkList(resource.fhirVersion, "fhirVersion", FHIRVersion.class);
                    ValidationSupport.choiceElement(resource.example, "example", Boolean.class, Canonical.class);
                    ValidationSupport.requireValueOrChildren(resource);
                }

                protected Builder from(Resource resource) {
                    super.from(resource);
                    reference = resource.reference;
                    fhirVersion.addAll(resource.fhirVersion);
                    name = resource.name;
                    description = resource.description;
                    example = resource.example;
                    groupingId = resource.groupingId;
                    return this;
                }
            }
        }

        /**
         * A page / section in the implementation guide. The root page is the implementation guide home page.
         */
        public static class Page extends BackboneElement {
            @ReferenceTarget({ "Binary" })
            @Choice({ Url.class, Reference.class })
            @Required
            private final Element name;
            @Required
            private final String title;
            @Binding(
                bindingName = "GuidePageGeneration",
                strength = BindingStrength.Value.REQUIRED,
                description = "A code that indicates how the page is generated.",
                valueSet = "http://hl7.org/fhir/ValueSet/guide-page-generation|4.3.0-CIBUILD"
            )
            @Required
            private final GuidePageGeneration generation;
            private final List<ImplementationGuide.Definition.Page> page;

            private Page(Builder builder) {
                super(builder);
                name = builder.name;
                title = builder.title;
                generation = builder.generation;
                page = Collections.unmodifiableList(builder.page);
            }

            /**
             * The source address for the page.
             * 
             * @return
             *     An immutable object of type {@link Url} or {@link Reference} that is non-null.
             */
            public Element getName() {
                return name;
            }

            /**
             * A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getTitle() {
                return title;
            }

            /**
             * A code that indicates how the page is generated.
             * 
             * @return
             *     An immutable object of type {@link GuidePageGeneration} that is non-null.
             */
            public GuidePageGeneration getGeneration() {
                return generation;
            }

            /**
             * Nested Pages/Sections under this page.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Page} that may be empty.
             */
            public List<ImplementationGuide.Definition.Page> getPage() {
                return page;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (name != null) || 
                    (title != null) || 
                    (generation != null) || 
                    !page.isEmpty();
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
                        accept(title, "title", visitor);
                        accept(generation, "generation", visitor);
                        accept(page, "page", visitor, ImplementationGuide.Definition.Page.class);
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
                Page other = (Page) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(name, other.name) && 
                    Objects.equals(title, other.title) && 
                    Objects.equals(generation, other.generation) && 
                    Objects.equals(page, other.page);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        name, 
                        title, 
                        generation, 
                        page);
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
                private Element name;
                private String title;
                private GuidePageGeneration generation;
                private List<ImplementationGuide.Definition.Page> page = new ArrayList<>();

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
                 * The source address for the page.
                 * 
                 * <p>This element is required.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Url}</li>
                 * <li>{@link Reference}</li>
                 * </ul>
                 * 
                 * When of type {@link Reference}, the allowed resource types for this reference are:
                 * <ul>
                 * <li>{@link Binary}</li>
                 * </ul>
                 * 
                 * @param name
                 *     Where to find that page
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder name(Element name) {
                    this.name = name;
                    return this;
                }

                /**
                 * Convenience method for setting {@code title}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param title
                 *     Short title shown for navigational assistance
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
                 * A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
                 * 
                 * <p>This element is required.
                 * 
                 * @param title
                 *     Short title shown for navigational assistance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder title(String title) {
                    this.title = title;
                    return this;
                }

                /**
                 * A code that indicates how the page is generated.
                 * 
                 * <p>This element is required.
                 * 
                 * @param generation
                 *     html | markdown | xml | generated
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder generation(GuidePageGeneration generation) {
                    this.generation = generation;
                    return this;
                }

                /**
                 * Nested Pages/Sections under this page.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param page
                 *     Nested Pages / Sections
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder page(ImplementationGuide.Definition.Page... page) {
                    for (ImplementationGuide.Definition.Page value : page) {
                        this.page.add(value);
                    }
                    return this;
                }

                /**
                 * Nested Pages/Sections under this page.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param page
                 *     Nested Pages / Sections
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder page(Collection<ImplementationGuide.Definition.Page> page) {
                    this.page = new ArrayList<>(page);
                    return this;
                }

                /**
                 * Build the {@link Page}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>name</li>
                 * <li>title</li>
                 * <li>generation</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Page}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Page per the base specification
                 */
                @Override
                public Page build() {
                    Page page = new Page(this);
                    if (validating) {
                        validate(page);
                    }
                    return page;
                }

                protected void validate(Page page) {
                    super.validate(page);
                    ValidationSupport.requireChoiceElement(page.name, "name", Url.class, Reference.class);
                    ValidationSupport.requireNonNull(page.title, "title");
                    ValidationSupport.requireNonNull(page.generation, "generation");
                    ValidationSupport.checkList(page.page, "page", ImplementationGuide.Definition.Page.class);
                    ValidationSupport.checkReferenceType(page.name, "name", "Binary");
                    ValidationSupport.requireValueOrChildren(page);
                }

                protected Builder from(Page page) {
                    super.from(page);
                    name = page.name;
                    title = page.title;
                    generation = page.generation;
                    this.page.addAll(page.page);
                    return this;
                }
            }
        }

        /**
         * Defines how IG is built by tools.
         */
        public static class Parameter extends BackboneElement {
            @Binding(
                bindingName = "GuideParameterCode",
                strength = BindingStrength.Value.REQUIRED,
                description = "Code of parameter that is input to the guide.",
                valueSet = "http://hl7.org/fhir/ValueSet/guide-parameter-code|4.3.0-CIBUILD"
            )
            @Required
            private final GuideParameterCode code;
            @Required
            private final String value;

            private Parameter(Builder builder) {
                super(builder);
                code = builder.code;
                value = builder.value;
            }

            /**
             * apply | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-
             * json | generate-turtle | html-template.
             * 
             * @return
             *     An immutable object of type {@link GuideParameterCode} that is non-null.
             */
            public GuideParameterCode getCode() {
                return code;
            }

            /**
             * Value for named type.
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
                    (code != null) || 
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
                        accept(code, "code", visitor);
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
                    Objects.equals(code, other.code) && 
                    Objects.equals(value, other.value);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        code, 
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
                private GuideParameterCode code;
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
                 * apply | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-
                 * json | generate-turtle | html-template.
                 * 
                 * <p>This element is required.
                 * 
                 * @param code
                 *     apply | path-resource | path-pages | path-tx-cache | expansion-parameter | rule-broken-links | generate-xml | generate-
                 *     json | generate-turtle | html-template
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(GuideParameterCode code) {
                    this.code = code;
                    return this;
                }

                /**
                 * Convenience method for setting {@code value}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Value for named type
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
                 * Value for named type.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Value for named type
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(String value) {
                    this.value = value;
                    return this;
                }

                /**
                 * Build the {@link Parameter}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>code</li>
                 * <li>value</li>
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
                    ValidationSupport.requireNonNull(parameter.code, "code");
                    ValidationSupport.requireNonNull(parameter.value, "value");
                    ValidationSupport.requireValueOrChildren(parameter);
                }

                protected Builder from(Parameter parameter) {
                    super.from(parameter);
                    code = parameter.code;
                    value = parameter.value;
                    return this;
                }
            }
        }

        /**
         * A template for building resources.
         */
        public static class Template extends BackboneElement {
            @Required
            private final Code code;
            @Required
            private final String source;
            private final String scope;

            private Template(Builder builder) {
                super(builder);
                code = builder.code;
                source = builder.source;
                scope = builder.scope;
            }

            /**
             * Type of template specified.
             * 
             * @return
             *     An immutable object of type {@link Code} that is non-null.
             */
            public Code getCode() {
                return code;
            }

            /**
             * The source location for the template.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getSource() {
                return source;
            }

            /**
             * The scope in which the template applies.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getScope() {
                return scope;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (code != null) || 
                    (source != null) || 
                    (scope != null);
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
                        accept(source, "source", visitor);
                        accept(scope, "scope", visitor);
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
                Template other = (Template) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(source, other.source) && 
                    Objects.equals(scope, other.scope);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        code, 
                        source, 
                        scope);
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
                private String source;
                private String scope;

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
                 * Type of template specified.
                 * 
                 * <p>This element is required.
                 * 
                 * @param code
                 *     Type of template specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(Code code) {
                    this.code = code;
                    return this;
                }

                /**
                 * Convenience method for setting {@code source}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param source
                 *     The source location for the template
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #source(com.ibm.fhir.model.type.String)
                 */
                public Builder source(java.lang.String source) {
                    this.source = (source == null) ? null : String.of(source);
                    return this;
                }

                /**
                 * The source location for the template.
                 * 
                 * <p>This element is required.
                 * 
                 * @param source
                 *     The source location for the template
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder source(String source) {
                    this.source = source;
                    return this;
                }

                /**
                 * Convenience method for setting {@code scope}.
                 * 
                 * @param scope
                 *     The scope in which the template applies
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #scope(com.ibm.fhir.model.type.String)
                 */
                public Builder scope(java.lang.String scope) {
                    this.scope = (scope == null) ? null : String.of(scope);
                    return this;
                }

                /**
                 * The scope in which the template applies.
                 * 
                 * @param scope
                 *     The scope in which the template applies
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder scope(String scope) {
                    this.scope = scope;
                    return this;
                }

                /**
                 * Build the {@link Template}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>code</li>
                 * <li>source</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Template}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Template per the base specification
                 */
                @Override
                public Template build() {
                    Template template = new Template(this);
                    if (validating) {
                        validate(template);
                    }
                    return template;
                }

                protected void validate(Template template) {
                    super.validate(template);
                    ValidationSupport.requireNonNull(template.code, "code");
                    ValidationSupport.requireNonNull(template.source, "source");
                    ValidationSupport.requireValueOrChildren(template);
                }

                protected Builder from(Template template) {
                    super.from(template);
                    code = template.code;
                    source = template.source;
                    scope = template.scope;
                    return this;
                }
            }
        }
    }

    /**
     * Information about an assembled implementation guide, created by the publication tooling.
     */
    public static class Manifest extends BackboneElement {
        @Summary
        private final Url rendering;
        @Summary
        @Required
        private final List<Resource> resource;
        private final List<Page> page;
        private final List<String> image;
        private final List<String> other;

        private Manifest(Builder builder) {
            super(builder);
            rendering = builder.rendering;
            resource = Collections.unmodifiableList(builder.resource);
            page = Collections.unmodifiableList(builder.page);
            image = Collections.unmodifiableList(builder.image);
            other = Collections.unmodifiableList(builder.other);
        }

        /**
         * A pointer to official web page, PDF or other rendering of the implementation guide.
         * 
         * @return
         *     An immutable object of type {@link Url} that may be null.
         */
        public Url getRendering() {
            return rendering;
        }

        /**
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
         * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
         * example resource.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Resource} that is non-empty.
         */
        public List<Resource> getResource() {
            return resource;
        }

        /**
         * Information about a page within the IG.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Page} that may be empty.
         */
        public List<Page> getPage() {
            return page;
        }

        /**
         * Indicates a relative path to an image that exists within the IG.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
         */
        public List<String> getImage() {
            return image;
        }

        /**
         * Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and 
         * similar files that could be the target of a hyperlink in a derived IG.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
         */
        public List<String> getOther() {
            return other;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (rendering != null) || 
                !resource.isEmpty() || 
                !page.isEmpty() || 
                !image.isEmpty() || 
                !other.isEmpty();
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
                    accept(rendering, "rendering", visitor);
                    accept(resource, "resource", visitor, Resource.class);
                    accept(page, "page", visitor, Page.class);
                    accept(image, "image", visitor, String.class);
                    accept(other, "other", visitor, String.class);
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
            Manifest other = (Manifest) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(rendering, other.rendering) && 
                Objects.equals(resource, other.resource) && 
                Objects.equals(page, other.page) && 
                Objects.equals(image, other.image) && 
                Objects.equals(this.other, other.other);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    rendering, 
                    resource, 
                    page, 
                    image, 
                    other);
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
            private Url rendering;
            private List<Resource> resource = new ArrayList<>();
            private List<Page> page = new ArrayList<>();
            private List<String> image = new ArrayList<>();
            private List<String> other = new ArrayList<>();

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
             * A pointer to official web page, PDF or other rendering of the implementation guide.
             * 
             * @param rendering
             *     Location of rendered implementation guide
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder rendering(Url rendering) {
                this.rendering = rendering;
                return this;
            }

            /**
             * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
             * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
             * example resource.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param resource
             *     Resource in the implementation guide
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Resource... resource) {
                for (Resource value : resource) {
                    this.resource.add(value);
                }
                return this;
            }

            /**
             * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
             * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
             * example resource.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param resource
             *     Resource in the implementation guide
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder resource(Collection<Resource> resource) {
                this.resource = new ArrayList<>(resource);
                return this;
            }

            /**
             * Information about a page within the IG.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param page
             *     HTML page within the parent IG
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder page(Page... page) {
                for (Page value : page) {
                    this.page.add(value);
                }
                return this;
            }

            /**
             * Information about a page within the IG.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param page
             *     HTML page within the parent IG
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder page(Collection<Page> page) {
                this.page = new ArrayList<>(page);
                return this;
            }

            /**
             * Convenience method for setting {@code image}.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param image
             *     Image within the IG
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #image(com.ibm.fhir.model.type.String)
             */
            public Builder image(java.lang.String... image) {
                for (java.lang.String value : image) {
                    this.image.add((value == null) ? null : String.of(value));
                }
                return this;
            }

            /**
             * Indicates a relative path to an image that exists within the IG.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param image
             *     Image within the IG
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder image(String... image) {
                for (String value : image) {
                    this.image.add(value);
                }
                return this;
            }

            /**
             * Indicates a relative path to an image that exists within the IG.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param image
             *     Image within the IG
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder image(Collection<String> image) {
                this.image = new ArrayList<>(image);
                return this;
            }

            /**
             * Convenience method for setting {@code other}.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param other
             *     Additional linkable file in IG
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #other(com.ibm.fhir.model.type.String)
             */
            public Builder other(java.lang.String... other) {
                for (java.lang.String value : other) {
                    this.other.add((value == null) ? null : String.of(value));
                }
                return this;
            }

            /**
             * Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and 
             * similar files that could be the target of a hyperlink in a derived IG.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param other
             *     Additional linkable file in IG
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder other(String... other) {
                for (String value : other) {
                    this.other.add(value);
                }
                return this;
            }

            /**
             * Indicates the relative path of an additional non-page, non-image file that is part of the IG - e.g. zip, jar and 
             * similar files that could be the target of a hyperlink in a derived IG.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param other
             *     Additional linkable file in IG
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder other(Collection<String> other) {
                this.other = new ArrayList<>(other);
                return this;
            }

            /**
             * Build the {@link Manifest}
             * 
             * <p>Required elements:
             * <ul>
             * <li>resource</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Manifest}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Manifest per the base specification
             */
            @Override
            public Manifest build() {
                Manifest manifest = new Manifest(this);
                if (validating) {
                    validate(manifest);
                }
                return manifest;
            }

            protected void validate(Manifest manifest) {
                super.validate(manifest);
                ValidationSupport.checkNonEmptyList(manifest.resource, "resource", Resource.class);
                ValidationSupport.checkList(manifest.page, "page", Page.class);
                ValidationSupport.checkList(manifest.image, "image", String.class);
                ValidationSupport.checkList(manifest.other, "other", String.class);
                ValidationSupport.requireValueOrChildren(manifest);
            }

            protected Builder from(Manifest manifest) {
                super.from(manifest);
                rendering = manifest.rendering;
                resource.addAll(manifest.resource);
                page.addAll(manifest.page);
                image.addAll(manifest.image);
                other.addAll(manifest.other);
                return this;
            }
        }

        /**
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, 
         * capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an 
         * example resource.
         */
        public static class Resource extends BackboneElement {
            @Summary
            @Required
            private final Reference reference;
            @Choice({ Boolean.class, Canonical.class })
            private final Element example;
            private final Url relativePath;

            private Resource(Builder builder) {
                super(builder);
                reference = builder.reference;
                example = builder.example;
                relativePath = builder.relativePath;
            }

            /**
             * Where this resource is found.
             * 
             * @return
             *     An immutable object of type {@link Reference} that is non-null.
             */
            public Reference getReference() {
                return reference;
            }

            /**
             * If true or a reference, indicates the resource is an example instance. If a reference is present, indicates that the 
             * example is an example of the specified profile.
             * 
             * @return
             *     An immutable object of type {@link Boolean} or {@link Canonical} that may be null.
             */
            public Element getExample() {
                return example;
            }

            /**
             * The relative path for primary page for this resource within the IG.
             * 
             * @return
             *     An immutable object of type {@link Url} that may be null.
             */
            public Url getRelativePath() {
                return relativePath;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (reference != null) || 
                    (example != null) || 
                    (relativePath != null);
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
                        accept(reference, "reference", visitor);
                        accept(example, "example", visitor);
                        accept(relativePath, "relativePath", visitor);
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
                Resource other = (Resource) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(reference, other.reference) && 
                    Objects.equals(example, other.example) && 
                    Objects.equals(relativePath, other.relativePath);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        reference, 
                        example, 
                        relativePath);
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
                private Reference reference;
                private Element example;
                private Url relativePath;

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
                 * Where this resource is found.
                 * 
                 * <p>This element is required.
                 * 
                 * @param reference
                 *     Location of the resource
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder reference(Reference reference) {
                    this.reference = reference;
                    return this;
                }

                /**
                 * Convenience method for setting {@code example} with choice type Boolean.
                 * 
                 * @param example
                 *     Is an example/What is this an example of?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #example(Element)
                 */
                public Builder example(java.lang.Boolean example) {
                    this.example = (example == null) ? null : Boolean.of(example);
                    return this;
                }

                /**
                 * If true or a reference, indicates the resource is an example instance. If a reference is present, indicates that the 
                 * example is an example of the specified profile.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Boolean}</li>
                 * <li>{@link Canonical}</li>
                 * </ul>
                 * 
                 * @param example
                 *     Is an example/What is this an example of?
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder example(Element example) {
                    this.example = example;
                    return this;
                }

                /**
                 * The relative path for primary page for this resource within the IG.
                 * 
                 * @param relativePath
                 *     Relative path for page in IG
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder relativePath(Url relativePath) {
                    this.relativePath = relativePath;
                    return this;
                }

                /**
                 * Build the {@link Resource}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>reference</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Resource}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Resource per the base specification
                 */
                @Override
                public Resource build() {
                    Resource resource = new Resource(this);
                    if (validating) {
                        validate(resource);
                    }
                    return resource;
                }

                protected void validate(Resource resource) {
                    super.validate(resource);
                    ValidationSupport.requireNonNull(resource.reference, "reference");
                    ValidationSupport.choiceElement(resource.example, "example", Boolean.class, Canonical.class);
                    ValidationSupport.requireValueOrChildren(resource);
                }

                protected Builder from(Resource resource) {
                    super.from(resource);
                    reference = resource.reference;
                    example = resource.example;
                    relativePath = resource.relativePath;
                    return this;
                }
            }
        }

        /**
         * Information about a page within the IG.
         */
        public static class Page extends BackboneElement {
            @Required
            private final String name;
            private final String title;
            private final List<String> anchor;

            private Page(Builder builder) {
                super(builder);
                name = builder.name;
                title = builder.title;
                anchor = Collections.unmodifiableList(builder.anchor);
            }

            /**
             * Relative path to the page.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getName() {
                return name;
            }

            /**
             * Label for the page intended for human display.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getTitle() {
                return title;
            }

            /**
             * The name of an anchor available on the page.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
             */
            public List<String> getAnchor() {
                return anchor;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (name != null) || 
                    (title != null) || 
                    !anchor.isEmpty();
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
                        accept(title, "title", visitor);
                        accept(anchor, "anchor", visitor, String.class);
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
                Page other = (Page) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(name, other.name) && 
                    Objects.equals(title, other.title) && 
                    Objects.equals(anchor, other.anchor);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        name, 
                        title, 
                        anchor);
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
                private String title;
                private List<String> anchor = new ArrayList<>();

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
                 *     HTML page name
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
                 * Relative path to the page.
                 * 
                 * <p>This element is required.
                 * 
                 * @param name
                 *     HTML page name
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
                 *     Title of the page, for references
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
                 * Label for the page intended for human display.
                 * 
                 * @param title
                 *     Title of the page, for references
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder title(String title) {
                    this.title = title;
                    return this;
                }

                /**
                 * Convenience method for setting {@code anchor}.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param anchor
                 *     Anchor available on the page
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #anchor(com.ibm.fhir.model.type.String)
                 */
                public Builder anchor(java.lang.String... anchor) {
                    for (java.lang.String value : anchor) {
                        this.anchor.add((value == null) ? null : String.of(value));
                    }
                    return this;
                }

                /**
                 * The name of an anchor available on the page.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param anchor
                 *     Anchor available on the page
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder anchor(String... anchor) {
                    for (String value : anchor) {
                        this.anchor.add(value);
                    }
                    return this;
                }

                /**
                 * The name of an anchor available on the page.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param anchor
                 *     Anchor available on the page
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder anchor(Collection<String> anchor) {
                    this.anchor = new ArrayList<>(anchor);
                    return this;
                }

                /**
                 * Build the {@link Page}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>name</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Page}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Page per the base specification
                 */
                @Override
                public Page build() {
                    Page page = new Page(this);
                    if (validating) {
                        validate(page);
                    }
                    return page;
                }

                protected void validate(Page page) {
                    super.validate(page);
                    ValidationSupport.requireNonNull(page.name, "name");
                    ValidationSupport.checkList(page.anchor, "anchor", String.class);
                    ValidationSupport.requireValueOrChildren(page);
                }

                protected Builder from(Page page) {
                    super.from(page);
                    name = page.name;
                    title = page.title;
                    anchor.addAll(page.anchor);
                    return this;
                }
            }
        }
    }
}
