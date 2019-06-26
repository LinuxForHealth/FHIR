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
import com.ibm.watsonhealth.fhir.model.type.ChargeItemDefinitionPriceComponentType;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Money;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate 
 * costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a 
 * rough structure and requires profiling for each type of billing code system.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ChargeItemDefinition extends DomainResource {
    private final Uri url;
    private final List<Identifier> identifier;
    private final String version;
    private final String title;
    private final List<Uri> derivedFromUri;
    private final List<Canonical> partOf;
    private final List<Canonical> replaces;
    private final PublicationStatus status;
    private final Boolean experimental;
    private final DateTime date;
    private final String publisher;
    private final List<ContactDetail> contact;
    private final Markdown description;
    private final List<UsageContext> useContext;
    private final List<CodeableConcept> jurisdiction;
    private final Markdown copyright;
    private final Date approvalDate;
    private final Date lastReviewDate;
    private final Period effectivePeriod;
    private final CodeableConcept code;
    private final List<Reference> instance;
    private final List<Applicability> applicability;
    private final List<PropertyGroup> propertyGroup;

    private ChargeItemDefinition(Builder builder) {
        super(builder);
        this.url = ValidationSupport.requireNonNull(builder.url, "url");
        this.identifier = builder.identifier;
        this.version = builder.version;
        this.title = builder.title;
        this.derivedFromUri = builder.derivedFromUri;
        this.partOf = builder.partOf;
        this.replaces = builder.replaces;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.experimental = builder.experimental;
        this.date = builder.date;
        this.publisher = builder.publisher;
        this.contact = builder.contact;
        this.description = builder.description;
        this.useContext = builder.useContext;
        this.jurisdiction = builder.jurisdiction;
        this.copyright = builder.copyright;
        this.approvalDate = builder.approvalDate;
        this.lastReviewDate = builder.lastReviewDate;
        this.effectivePeriod = builder.effectivePeriod;
        this.code = builder.code;
        this.instance = builder.instance;
        this.applicability = builder.applicability;
        this.propertyGroup = builder.propertyGroup;
    }

    /**
     * <p>
     * An absolute URI that is used to identify this charge item definition when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this charge item definition is (or will be) published. This URL 
     * can be the target of a canonical reference. It SHALL remain the same when the charge item definition is stored on 
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
     * A formal identifier that is used to identify this charge item definition when it is represented in other formats, or 
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
     * The identifier that is used to identify this version of the charge item definition when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the charge item definition author and 
     * is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is 
     * not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
     * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
     * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
     * version is required for non-experimental active assets.
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
     * A short, descriptive, user-friendly title for the charge item definition.
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
     * The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this 
     * definition.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getDerivedFromUri() {
        return derivedFromUri;
    }

    /**
     * <p>
     * A larger definition of which this particular definition is a component or step.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getPartOf() {
        return partOf;
    }

    /**
     * <p>
     * As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new 
     * instance.
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
     * The current state of the ChargeItemDefinition.
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
     * A Boolean value to indicate that this charge item definition is authored for testing purposes (or 
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
     * The date (and optionally time) when the charge item definition was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the charge item definition changes.
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
     * The name of the organization or individual that published the charge item definition.
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
     * A free text natural language description of the charge item definition from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate charge item definition instances.
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
     * A legal or geographic region in which the charge item definition is intended to be used.
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
     * A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the charge item definition.
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
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is 
     * officially approved for usage.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Date}.
     */
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
     * <p>
     * The date on which the resource content was last reviewed. Review happens periodically after approval but does not 
     * change the original approval date.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Date}.
     */
    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    /**
     * <p>
     * The period during which the charge item definition content was or is planned to be in active use.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getEffectivePeriod() {
        return effectivePeriod;
    }

    /**
     * <p>
     * The defined billing details in this resource pertain to the given billing code.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * <p>
     * The defined billing details in this resource pertain to the given product instance(s).
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getInstance() {
        return instance;
    }

    /**
     * <p>
     * Expressions that describe applicability criteria for the billing code.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Applicability}.
     */
    public List<Applicability> getApplicability() {
        return applicability;
    }

    /**
     * <p>
     * Group of properties which are applicable under the same conditions. If no applicability rules are established for the 
     * group, then all properties always apply.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link PropertyGroup}.
     */
    public List<PropertyGroup> getPropertyGroup() {
        return propertyGroup;
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
                accept(title, "title", visitor);
                accept(derivedFromUri, "derivedFromUri", visitor, Uri.class);
                accept(partOf, "partOf", visitor, Canonical.class);
                accept(replaces, "replaces", visitor, Canonical.class);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(copyright, "copyright", visitor);
                accept(approvalDate, "approvalDate", visitor);
                accept(lastReviewDate, "lastReviewDate", visitor);
                accept(effectivePeriod, "effectivePeriod", visitor);
                accept(code, "code", visitor);
                accept(instance, "instance", visitor, Reference.class);
                accept(applicability, "applicability", visitor, Applicability.class);
                accept(propertyGroup, "propertyGroup", visitor, PropertyGroup.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(url, status);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.version = version;
        builder.title = title;
        builder.derivedFromUri.addAll(derivedFromUri);
        builder.partOf.addAll(partOf);
        builder.replaces.addAll(replaces);
        builder.experimental = experimental;
        builder.date = date;
        builder.publisher = publisher;
        builder.contact.addAll(contact);
        builder.description = description;
        builder.useContext.addAll(useContext);
        builder.jurisdiction.addAll(jurisdiction);
        builder.copyright = copyright;
        builder.approvalDate = approvalDate;
        builder.lastReviewDate = lastReviewDate;
        builder.effectivePeriod = effectivePeriod;
        builder.code = code;
        builder.instance.addAll(instance);
        builder.applicability.addAll(applicability);
        builder.propertyGroup.addAll(propertyGroup);
        return builder;
    }

    public static Builder builder(Uri url, PublicationStatus status) {
        return new Builder(url, status);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Uri url;
        private final PublicationStatus status;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private String version;
        private String title;
        private List<Uri> derivedFromUri = new ArrayList<>();
        private List<Canonical> partOf = new ArrayList<>();
        private List<Canonical> replaces = new ArrayList<>();
        private Boolean experimental;
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown copyright;
        private Date approvalDate;
        private Date lastReviewDate;
        private Period effectivePeriod;
        private CodeableConcept code;
        private List<Reference> instance = new ArrayList<>();
        private List<Applicability> applicability = new ArrayList<>();
        private List<PropertyGroup> propertyGroup = new ArrayList<>();

        private Builder(Uri url, PublicationStatus status) {
            super();
            this.url = url;
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
         * A formal identifier that is used to identify this charge item definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the charge item definition
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
         * A formal identifier that is used to identify this charge item definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the charge item definition
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
         * The identifier that is used to identify this version of the charge item definition when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the charge item definition author and 
         * is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is 
         * not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
         * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
         * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
         * version is required for non-experimental active assets.
         * </p>
         * 
         * @param version
         *     Business version of the charge item definition
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
         * A short, descriptive, user-friendly title for the charge item definition.
         * </p>
         * 
         * @param title
         *     Name for this charge item definition (human friendly)
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
         * The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this 
         * definition.
         * </p>
         * 
         * @param derivedFromUri
         *     Underlying externally-defined charge item definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder derivedFromUri(Uri... derivedFromUri) {
            for (Uri value : derivedFromUri) {
                this.derivedFromUri.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this 
         * definition.
         * </p>
         * 
         * @param derivedFromUri
         *     Underlying externally-defined charge item definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder derivedFromUri(Collection<Uri> derivedFromUri) {
            this.derivedFromUri.addAll(derivedFromUri);
            return this;
        }

        /**
         * <p>
         * A larger definition of which this particular definition is a component or step.
         * </p>
         * 
         * @param partOf
         *     A larger definition of which this particular definition is a component or step
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Canonical... partOf) {
            for (Canonical value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A larger definition of which this particular definition is a component or step.
         * </p>
         * 
         * @param partOf
         *     A larger definition of which this particular definition is a component or step
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Collection<Canonical> partOf) {
            this.partOf.addAll(partOf);
            return this;
        }

        /**
         * <p>
         * As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new 
         * instance.
         * </p>
         * 
         * @param replaces
         *     Completed or terminated request(s) whose function is taken by this new request
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
         * As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new 
         * instance.
         * </p>
         * 
         * @param replaces
         *     Completed or terminated request(s) whose function is taken by this new request
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
         * A Boolean value to indicate that this charge item definition is authored for testing purposes (or 
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
         * The date (and optionally time) when the charge item definition was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the charge item definition changes.
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
         * The name of the organization or individual that published the charge item definition.
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
         * A free text natural language description of the charge item definition from a consumer's perspective.
         * </p>
         * 
         * @param description
         *     Natural language description of the charge item definition
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
         * may be used to assist with indexing and searching for appropriate charge item definition instances.
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
         * may be used to assist with indexing and searching for appropriate charge item definition instances.
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
         * A legal or geographic region in which the charge item definition is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for charge item definition (if applicable)
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
         * A legal or geographic region in which the charge item definition is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for charge item definition (if applicable)
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
         * A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the charge item definition.
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
         * The date on which the resource content was approved by the publisher. Approval happens once when the content is 
         * officially approved for usage.
         * </p>
         * 
         * @param approvalDate
         *     When the charge item definition was approved by publisher
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder approvalDate(Date approvalDate) {
            this.approvalDate = approvalDate;
            return this;
        }

        /**
         * <p>
         * The date on which the resource content was last reviewed. Review happens periodically after approval but does not 
         * change the original approval date.
         * </p>
         * 
         * @param lastReviewDate
         *     When the charge item definition was last reviewed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder lastReviewDate(Date lastReviewDate) {
            this.lastReviewDate = lastReviewDate;
            return this;
        }

        /**
         * <p>
         * The period during which the charge item definition content was or is planned to be in active use.
         * </p>
         * 
         * @param effectivePeriod
         *     When the charge item definition is expected to be used
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder effectivePeriod(Period effectivePeriod) {
            this.effectivePeriod = effectivePeriod;
            return this;
        }

        /**
         * <p>
         * The defined billing details in this resource pertain to the given billing code.
         * </p>
         * 
         * @param code
         *     Billing codes or product types this definition applies to
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * <p>
         * The defined billing details in this resource pertain to the given product instance(s).
         * </p>
         * 
         * @param instance
         *     Instances this definition applies to
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instance(Reference... instance) {
            for (Reference value : instance) {
                this.instance.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The defined billing details in this resource pertain to the given product instance(s).
         * </p>
         * 
         * @param instance
         *     Instances this definition applies to
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instance(Collection<Reference> instance) {
            this.instance.addAll(instance);
            return this;
        }

        /**
         * <p>
         * Expressions that describe applicability criteria for the billing code.
         * </p>
         * 
         * @param applicability
         *     Whether or not the billing code is applicable
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder applicability(Applicability... applicability) {
            for (Applicability value : applicability) {
                this.applicability.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Expressions that describe applicability criteria for the billing code.
         * </p>
         * 
         * @param applicability
         *     Whether or not the billing code is applicable
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder applicability(Collection<Applicability> applicability) {
            this.applicability.addAll(applicability);
            return this;
        }

        /**
         * <p>
         * Group of properties which are applicable under the same conditions. If no applicability rules are established for the 
         * group, then all properties always apply.
         * </p>
         * 
         * @param propertyGroup
         *     Group of properties which are applicable under the same conditions
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder propertyGroup(PropertyGroup... propertyGroup) {
            for (PropertyGroup value : propertyGroup) {
                this.propertyGroup.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Group of properties which are applicable under the same conditions. If no applicability rules are established for the 
         * group, then all properties always apply.
         * </p>
         * 
         * @param propertyGroup
         *     Group of properties which are applicable under the same conditions
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder propertyGroup(Collection<PropertyGroup> propertyGroup) {
            this.propertyGroup.addAll(propertyGroup);
            return this;
        }

        @Override
        public ChargeItemDefinition build() {
            return new ChargeItemDefinition(this);
        }
    }

    /**
     * <p>
     * Expressions that describe applicability criteria for the billing code.
     * </p>
     */
    public static class Applicability extends BackboneElement {
        private final String description;
        private final String language;
        private final String expression;

        private Applicability(Builder builder) {
            super(builder);
            this.description = builder.description;
            this.language = builder.language;
            this.expression = builder.expression;
        }

        /**
         * <p>
         * A brief, natural language description of the condition that effectively communicates the intended semantics.
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
         * The media type of the language for the expression, e.g. "text/cql" for Clinical Query Language expressions or 
         * "text/fhirpath" for FHIRPath expressions.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getLanguage() {
            return language;
        }

        /**
         * <p>
         * An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath 
         * expressions, the %context environment variable must be replaced at runtime with the ChargeItem resource to which this 
         * definition is applied.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getExpression() {
            return expression;
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
                    accept(description, "description", visitor);
                    accept(language, "language", visitor);
                    accept(expression, "expression", visitor);
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
            private String description;
            private String language;
            private String expression;

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
             * A brief, natural language description of the condition that effectively communicates the intended semantics.
             * </p>
             * 
             * @param description
             *     Natural language description of the condition
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
             * The media type of the language for the expression, e.g. "text/cql" for Clinical Query Language expressions or 
             * "text/fhirpath" for FHIRPath expressions.
             * </p>
             * 
             * @param language
             *     Language of the expression
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder language(String language) {
                this.language = language;
                return this;
            }

            /**
             * <p>
             * An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath 
             * expressions, the %context environment variable must be replaced at runtime with the ChargeItem resource to which this 
             * definition is applied.
             * </p>
             * 
             * @param expression
             *     Boolean-valued expression
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder expression(String expression) {
                this.expression = expression;
                return this;
            }

            @Override
            public Applicability build() {
                return new Applicability(this);
            }

            private static Builder from(Applicability applicability) {
                Builder builder = new Builder();
                builder.id = applicability.id;
                builder.extension.addAll(applicability.extension);
                builder.modifierExtension.addAll(applicability.modifierExtension);
                builder.description = applicability.description;
                builder.language = applicability.language;
                builder.expression = applicability.expression;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Group of properties which are applicable under the same conditions. If no applicability rules are established for the 
     * group, then all properties always apply.
     * </p>
     */
    public static class PropertyGroup extends BackboneElement {
        private final List<ChargeItemDefinition.Applicability> applicability;
        private final List<PriceComponent> priceComponent;

        private PropertyGroup(Builder builder) {
            super(builder);
            this.applicability = builder.applicability;
            this.priceComponent = builder.priceComponent;
        }

        /**
         * <p>
         * Expressions that describe applicability criteria for the priceComponent.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Applicability}.
         */
        public List<ChargeItemDefinition.Applicability> getApplicability() {
            return applicability;
        }

        /**
         * <p>
         * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
         * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
         * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
         * the Invoice of how the prices have been calculated.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link PriceComponent}.
         */
        public List<PriceComponent> getPriceComponent() {
            return priceComponent;
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
                    accept(applicability, "applicability", visitor, ChargeItemDefinition.Applicability.class);
                    accept(priceComponent, "priceComponent", visitor, PriceComponent.class);
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
            private List<ChargeItemDefinition.Applicability> applicability = new ArrayList<>();
            private List<PriceComponent> priceComponent = new ArrayList<>();

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
             * Expressions that describe applicability criteria for the priceComponent.
             * </p>
             * 
             * @param applicability
             *     Conditions under which the priceComponent is applicable
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder applicability(ChargeItemDefinition.Applicability... applicability) {
                for (ChargeItemDefinition.Applicability value : applicability) {
                    this.applicability.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Expressions that describe applicability criteria for the priceComponent.
             * </p>
             * 
             * @param applicability
             *     Conditions under which the priceComponent is applicable
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder applicability(Collection<ChargeItemDefinition.Applicability> applicability) {
                this.applicability.addAll(applicability);
                return this;
            }

            /**
             * <p>
             * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
             * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
             * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
             * the Invoice of how the prices have been calculated.
             * </p>
             * 
             * @param priceComponent
             *     Components of total line item price
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder priceComponent(PriceComponent... priceComponent) {
                for (PriceComponent value : priceComponent) {
                    this.priceComponent.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
             * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
             * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
             * the Invoice of how the prices have been calculated.
             * </p>
             * 
             * @param priceComponent
             *     Components of total line item price
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder priceComponent(Collection<PriceComponent> priceComponent) {
                this.priceComponent.addAll(priceComponent);
                return this;
            }

            @Override
            public PropertyGroup build() {
                return new PropertyGroup(this);
            }

            private static Builder from(PropertyGroup propertyGroup) {
                Builder builder = new Builder();
                builder.id = propertyGroup.id;
                builder.extension.addAll(propertyGroup.extension);
                builder.modifierExtension.addAll(propertyGroup.modifierExtension);
                builder.applicability.addAll(propertyGroup.applicability);
                builder.priceComponent.addAll(propertyGroup.priceComponent);
                return builder;
            }
        }

        /**
         * <p>
         * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
         * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
         * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
         * the Invoice of how the prices have been calculated.
         * </p>
         */
        public static class PriceComponent extends BackboneElement {
            private final ChargeItemDefinitionPriceComponentType type;
            private final CodeableConcept code;
            private final Decimal factor;
            private final Money amount;

            private PriceComponent(Builder builder) {
                super(builder);
                this.type = ValidationSupport.requireNonNull(builder.type, "type");
                this.code = builder.code;
                this.factor = builder.factor;
                this.amount = builder.amount;
            }

            /**
             * <p>
             * This code identifies the type of the component.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link ChargeItemDefinitionPriceComponentType}.
             */
            public ChargeItemDefinitionPriceComponentType getType() {
                return type;
            }

            /**
             * <p>
             * A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts 
             * etc.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getCode() {
                return code;
            }

            /**
             * <p>
             * The factor that has been applied on the base price for calculating this component.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Decimal}.
             */
            public Decimal getFactor() {
                return factor;
            }

            /**
             * <p>
             * The amount calculated for this component.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Money}.
             */
            public Money getAmount() {
                return amount;
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
                        accept(code, "code", visitor);
                        accept(factor, "factor", visitor);
                        accept(amount, "amount", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return Builder.from(this);
            }

            public static Builder builder(ChargeItemDefinitionPriceComponentType type) {
                return new Builder(type);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final ChargeItemDefinitionPriceComponentType type;

                // optional
                private CodeableConcept code;
                private Decimal factor;
                private Money amount;

                private Builder(ChargeItemDefinitionPriceComponentType type) {
                    super();
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
                 * A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts 
                 * etc.
                 * </p>
                 * 
                 * @param code
                 *     Code identifying the specific component
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder code(CodeableConcept code) {
                    this.code = code;
                    return this;
                }

                /**
                 * <p>
                 * The factor that has been applied on the base price for calculating this component.
                 * </p>
                 * 
                 * @param factor
                 *     Factor used for calculating this component
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder factor(Decimal factor) {
                    this.factor = factor;
                    return this;
                }

                /**
                 * <p>
                 * The amount calculated for this component.
                 * </p>
                 * 
                 * @param amount
                 *     Monetary amount associated with this component
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder amount(Money amount) {
                    this.amount = amount;
                    return this;
                }

                @Override
                public PriceComponent build() {
                    return new PriceComponent(this);
                }

                private static Builder from(PriceComponent priceComponent) {
                    Builder builder = new Builder(priceComponent.type);
                    builder.id = priceComponent.id;
                    builder.extension.addAll(priceComponent.extension);
                    builder.modifierExtension.addAll(priceComponent.modifierExtension);
                    builder.code = priceComponent.code;
                    builder.factor = priceComponent.factor;
                    builder.amount = priceComponent.amount;
                    return builder;
                }
            }
        }
    }
}
