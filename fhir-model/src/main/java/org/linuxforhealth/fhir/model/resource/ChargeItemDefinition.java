/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.annotation.Binding;
import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.annotation.Maturity;
import org.linuxforhealth.fhir.model.annotation.ReferenceTarget;
import org.linuxforhealth.fhir.model.annotation.Required;
import org.linuxforhealth.fhir.model.annotation.Summary;
import org.linuxforhealth.fhir.model.type.BackboneElement;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.ContactDetail;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Decimal;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Markdown;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Money;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Period;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.String;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.UsageContext;
import org.linuxforhealth.fhir.model.type.code.BindingStrength;
import org.linuxforhealth.fhir.model.type.code.ChargeItemDefinitionPriceComponentType;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;
import org.linuxforhealth.fhir.model.type.code.StandardsStatus;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate 
 * costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a 
 * rough structure and requires profiling for each type of billing code system.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "cid-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.exists() implies name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/ChargeItemDefinition"
)
@Constraint(
    id = "chargeItemDefinition-1",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/ChargeItemDefinition",
    generated = true
)
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ChargeItemDefinition extends DomainResource {
    @Summary
    @Required
    private final Uri url;
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final String version;
    @Summary
    private final String title;
    @Summary
    private final List<Uri> derivedFromUri;
    @Summary
    private final List<Canonical> partOf;
    @Summary
    private final List<Canonical> replaces;
    @Summary
    @Binding(
        bindingName = "PublicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The lifecycle status of an artifact.",
        valueSet = "http://hl7.org/fhir/ValueSet/publication-status|4.3.0"
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
    private final Date approvalDate;
    private final Date lastReviewDate;
    @Summary
    private final Period effectivePeriod;
    @Summary
    @Binding(
        bindingName = "ChargeItemDefinitionCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Billing Code defined by this ChargeItemDefinition.",
        valueSet = "http://hl7.org/fhir/ValueSet/chargeitem-billingcodes"
    )
    private final CodeableConcept code;
    @ReferenceTarget({ "Medication", "Substance", "Device" })
    private final List<Reference> instance;
    private final List<Applicability> applicability;
    private final List<PropertyGroup> propertyGroup;

    private ChargeItemDefinition(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        title = builder.title;
        derivedFromUri = Collections.unmodifiableList(builder.derivedFromUri);
        partOf = Collections.unmodifiableList(builder.partOf);
        replaces = Collections.unmodifiableList(builder.replaces);
        status = builder.status;
        experimental = builder.experimental;
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        copyright = builder.copyright;
        approvalDate = builder.approvalDate;
        lastReviewDate = builder.lastReviewDate;
        effectivePeriod = builder.effectivePeriod;
        code = builder.code;
        instance = Collections.unmodifiableList(builder.instance);
        applicability = Collections.unmodifiableList(builder.applicability);
        propertyGroup = Collections.unmodifiableList(builder.propertyGroup);
    }

    /**
     * An absolute URI that is used to identify this charge item definition when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this charge item definition is (or will be) published. This URL 
     * can be the target of a canonical reference. It SHALL remain the same when the charge item definition is stored on 
     * different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that is non-null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this charge item definition when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the charge item definition when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the charge item definition author and 
     * is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is 
     * not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
     * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
     * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
     * version is required for non-experimental active assets.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A short, descriptive, user-friendly title for the charge item definition.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this 
     * definition.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri} that may be empty.
     */
    public List<Uri> getDerivedFromUri() {
        return derivedFromUri;
    }

    /**
     * A larger definition of which this particular definition is a component or step.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getPartOf() {
        return partOf;
    }

    /**
     * As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new 
     * instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getReplaces() {
        return replaces;
    }

    /**
     * The current state of the ChargeItemDefinition.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this charge item definition is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The date (and optionally time) when the charge item definition was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the charge item definition changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the charge item definition.
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
     * A free text natural language description of the charge item definition from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate charge item definition instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the charge item definition is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally 
     * legal restrictions on the use and publishing of the charge item definition.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getCopyright() {
        return copyright;
    }

    /**
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is 
     * officially approved for usage.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
     * The date on which the resource content was last reviewed. Review happens periodically after approval but does not 
     * change the original approval date.
     * 
     * @return
     *     An immutable object of type {@link Date} that may be null.
     */
    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    /**
     * The period during which the charge item definition content was or is planned to be in active use.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getEffectivePeriod() {
        return effectivePeriod;
    }

    /**
     * The defined billing details in this resource pertain to the given billing code.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * The defined billing details in this resource pertain to the given product instance(s).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getInstance() {
        return instance;
    }

    /**
     * Expressions that describe applicability criteria for the billing code.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Applicability} that may be empty.
     */
    public List<Applicability> getApplicability() {
        return applicability;
    }

    /**
     * Group of properties which are applicable under the same conditions. If no applicability rules are established for the 
     * group, then all properties always apply.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link PropertyGroup} that may be empty.
     */
    public List<PropertyGroup> getPropertyGroup() {
        return propertyGroup;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            !identifier.isEmpty() || 
            (version != null) || 
            (title != null) || 
            !derivedFromUri.isEmpty() || 
            !partOf.isEmpty() || 
            !replaces.isEmpty() || 
            (status != null) || 
            (experimental != null) || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            (description != null) || 
            !useContext.isEmpty() || 
            !jurisdiction.isEmpty() || 
            (copyright != null) || 
            (approvalDate != null) || 
            (lastReviewDate != null) || 
            (effectivePeriod != null) || 
            (code != null) || 
            !instance.isEmpty() || 
            !applicability.isEmpty() || 
            !propertyGroup.isEmpty();
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
        ChargeItemDefinition other = (ChargeItemDefinition) obj;
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
            Objects.equals(title, other.title) && 
            Objects.equals(derivedFromUri, other.derivedFromUri) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(replaces, other.replaces) && 
            Objects.equals(status, other.status) && 
            Objects.equals(experimental, other.experimental) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(description, other.description) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(copyright, other.copyright) && 
            Objects.equals(approvalDate, other.approvalDate) && 
            Objects.equals(lastReviewDate, other.lastReviewDate) && 
            Objects.equals(effectivePeriod, other.effectivePeriod) && 
            Objects.equals(code, other.code) && 
            Objects.equals(instance, other.instance) && 
            Objects.equals(applicability, other.applicability) && 
            Objects.equals(propertyGroup, other.propertyGroup);
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
                title, 
                derivedFromUri, 
                partOf, 
                replaces, 
                status, 
                experimental, 
                date, 
                publisher, 
                contact, 
                description, 
                useContext, 
                jurisdiction, 
                copyright, 
                approvalDate, 
                lastReviewDate, 
                effectivePeriod, 
                code, 
                instance, 
                applicability, 
                propertyGroup);
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
        private String title;
        private List<Uri> derivedFromUri = new ArrayList<>();
        private List<Canonical> partOf = new ArrayList<>();
        private List<Canonical> replaces = new ArrayList<>();
        private PublicationStatus status;
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
         * An absolute URI that is used to identify this charge item definition when it is referenced in a specification, model, 
         * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
         * address at which at which an authoritative instance of this charge item definition is (or will be) published. This URL 
         * can be the target of a canonical reference. It SHALL remain the same when the charge item definition is stored on 
         * different servers.
         * 
         * <p>This element is required.
         * 
         * @param url
         *     Canonical identifier for this charge item definition, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this charge item definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the charge item definition
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
         * A formal identifier that is used to identify this charge item definition when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the charge item definition
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
         *     Business version of the charge item definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #version(org.linuxforhealth.fhir.model.type.String)
         */
        public Builder version(java.lang.String version) {
            this.version = (version == null) ? null : String.of(version);
            return this;
        }

        /**
         * The identifier that is used to identify this version of the charge item definition when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the charge item definition author and 
         * is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is 
         * not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
         * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
         * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
         * version is required for non-experimental active assets.
         * 
         * @param version
         *     Business version of the charge item definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Convenience method for setting {@code title}.
         * 
         * @param title
         *     Name for this charge item definition (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #title(org.linuxforhealth.fhir.model.type.String)
         */
        public Builder title(java.lang.String title) {
            this.title = (title == null) ? null : String.of(title);
            return this;
        }

        /**
         * A short, descriptive, user-friendly title for the charge item definition.
         * 
         * @param title
         *     Name for this charge item definition (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this 
         * definition.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param derivedFromUri
         *     Underlying externally-defined charge item definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder derivedFromUri(Uri... derivedFromUri) {
            for (Uri value : derivedFromUri) {
                this.derivedFromUri.add(value);
            }
            return this;
        }

        /**
         * The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this 
         * definition.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param derivedFromUri
         *     Underlying externally-defined charge item definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder derivedFromUri(Collection<Uri> derivedFromUri) {
            this.derivedFromUri = new ArrayList<>(derivedFromUri);
            return this;
        }

        /**
         * A larger definition of which this particular definition is a component or step.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param partOf
         *     A larger definition of which this particular definition is a component or step
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Canonical... partOf) {
            for (Canonical value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * A larger definition of which this particular definition is a component or step.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param partOf
         *     A larger definition of which this particular definition is a component or step
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder partOf(Collection<Canonical> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new 
         * instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param replaces
         *     Completed or terminated request(s) whose function is taken by this new request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder replaces(Canonical... replaces) {
            for (Canonical value : replaces) {
                this.replaces.add(value);
            }
            return this;
        }

        /**
         * As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new 
         * instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param replaces
         *     Completed or terminated request(s) whose function is taken by this new request
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder replaces(Collection<Canonical> replaces) {
            this.replaces = new ArrayList<>(replaces);
            return this;
        }

        /**
         * The current state of the ChargeItemDefinition.
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
         * @see #experimental(org.linuxforhealth.fhir.model.type.Boolean)
         */
        public Builder experimental(java.lang.Boolean experimental) {
            this.experimental = (experimental == null) ? null : Boolean.of(experimental);
            return this;
        }

        /**
         * A Boolean value to indicate that this charge item definition is authored for testing purposes (or 
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
         * The date (and optionally time) when the charge item definition was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the charge item definition changes.
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
         * @see #publisher(org.linuxforhealth.fhir.model.type.String)
         */
        public Builder publisher(java.lang.String publisher) {
            this.publisher = (publisher == null) ? null : String.of(publisher);
            return this;
        }

        /**
         * The name of the organization or individual that published the charge item definition.
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
         * A free text natural language description of the charge item definition from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the charge item definition
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
         * may be used to assist with indexing and searching for appropriate charge item definition instances.
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
         * may be used to assist with indexing and searching for appropriate charge item definition instances.
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
         * A legal or geographic region in which the charge item definition is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for charge item definition (if applicable)
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
         * A legal or geographic region in which the charge item definition is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for charge item definition (if applicable)
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
         * A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally 
         * legal restrictions on the use and publishing of the charge item definition.
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
         * Convenience method for setting {@code approvalDate}.
         * 
         * @param approvalDate
         *     When the charge item definition was approved by publisher
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #approvalDate(org.linuxforhealth.fhir.model.type.Date)
         */
        public Builder approvalDate(java.time.LocalDate approvalDate) {
            this.approvalDate = (approvalDate == null) ? null : Date.of(approvalDate);
            return this;
        }

        /**
         * The date on which the resource content was approved by the publisher. Approval happens once when the content is 
         * officially approved for usage.
         * 
         * @param approvalDate
         *     When the charge item definition was approved by publisher
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder approvalDate(Date approvalDate) {
            this.approvalDate = approvalDate;
            return this;
        }

        /**
         * Convenience method for setting {@code lastReviewDate}.
         * 
         * @param lastReviewDate
         *     When the charge item definition was last reviewed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #lastReviewDate(org.linuxforhealth.fhir.model.type.Date)
         */
        public Builder lastReviewDate(java.time.LocalDate lastReviewDate) {
            this.lastReviewDate = (lastReviewDate == null) ? null : Date.of(lastReviewDate);
            return this;
        }

        /**
         * The date on which the resource content was last reviewed. Review happens periodically after approval but does not 
         * change the original approval date.
         * 
         * @param lastReviewDate
         *     When the charge item definition was last reviewed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastReviewDate(Date lastReviewDate) {
            this.lastReviewDate = lastReviewDate;
            return this;
        }

        /**
         * The period during which the charge item definition content was or is planned to be in active use.
         * 
         * @param effectivePeriod
         *     When the charge item definition is expected to be used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effectivePeriod(Period effectivePeriod) {
            this.effectivePeriod = effectivePeriod;
            return this;
        }

        /**
         * The defined billing details in this resource pertain to the given billing code.
         * 
         * @param code
         *     Billing codes or product types this definition applies to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * The defined billing details in this resource pertain to the given product instance(s).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Medication}</li>
         * <li>{@link Substance}</li>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param instance
         *     Instances this definition applies to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instance(Reference... instance) {
            for (Reference value : instance) {
                this.instance.add(value);
            }
            return this;
        }

        /**
         * The defined billing details in this resource pertain to the given product instance(s).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Medication}</li>
         * <li>{@link Substance}</li>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param instance
         *     Instances this definition applies to
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instance(Collection<Reference> instance) {
            this.instance = new ArrayList<>(instance);
            return this;
        }

        /**
         * Expressions that describe applicability criteria for the billing code.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param applicability
         *     Whether or not the billing code is applicable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder applicability(Applicability... applicability) {
            for (Applicability value : applicability) {
                this.applicability.add(value);
            }
            return this;
        }

        /**
         * Expressions that describe applicability criteria for the billing code.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param applicability
         *     Whether or not the billing code is applicable
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder applicability(Collection<Applicability> applicability) {
            this.applicability = new ArrayList<>(applicability);
            return this;
        }

        /**
         * Group of properties which are applicable under the same conditions. If no applicability rules are established for the 
         * group, then all properties always apply.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param propertyGroup
         *     Group of properties which are applicable under the same conditions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder propertyGroup(PropertyGroup... propertyGroup) {
            for (PropertyGroup value : propertyGroup) {
                this.propertyGroup.add(value);
            }
            return this;
        }

        /**
         * Group of properties which are applicable under the same conditions. If no applicability rules are established for the 
         * group, then all properties always apply.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param propertyGroup
         *     Group of properties which are applicable under the same conditions
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder propertyGroup(Collection<PropertyGroup> propertyGroup) {
            this.propertyGroup = new ArrayList<>(propertyGroup);
            return this;
        }

        /**
         * Build the {@link ChargeItemDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>url</li>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ChargeItemDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ChargeItemDefinition per the base specification
         */
        @Override
        public ChargeItemDefinition build() {
            ChargeItemDefinition chargeItemDefinition = new ChargeItemDefinition(this);
            if (validating) {
                validate(chargeItemDefinition);
            }
            return chargeItemDefinition;
        }

        protected void validate(ChargeItemDefinition chargeItemDefinition) {
            super.validate(chargeItemDefinition);
            ValidationSupport.requireNonNull(chargeItemDefinition.url, "url");
            ValidationSupport.checkList(chargeItemDefinition.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(chargeItemDefinition.derivedFromUri, "derivedFromUri", Uri.class);
            ValidationSupport.checkList(chargeItemDefinition.partOf, "partOf", Canonical.class);
            ValidationSupport.checkList(chargeItemDefinition.replaces, "replaces", Canonical.class);
            ValidationSupport.requireNonNull(chargeItemDefinition.status, "status");
            ValidationSupport.checkList(chargeItemDefinition.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(chargeItemDefinition.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(chargeItemDefinition.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkList(chargeItemDefinition.instance, "instance", Reference.class);
            ValidationSupport.checkList(chargeItemDefinition.applicability, "applicability", Applicability.class);
            ValidationSupport.checkList(chargeItemDefinition.propertyGroup, "propertyGroup", PropertyGroup.class);
            ValidationSupport.checkReferenceType(chargeItemDefinition.instance, "instance", "Medication", "Substance", "Device");
        }

        protected Builder from(ChargeItemDefinition chargeItemDefinition) {
            super.from(chargeItemDefinition);
            url = chargeItemDefinition.url;
            identifier.addAll(chargeItemDefinition.identifier);
            version = chargeItemDefinition.version;
            title = chargeItemDefinition.title;
            derivedFromUri.addAll(chargeItemDefinition.derivedFromUri);
            partOf.addAll(chargeItemDefinition.partOf);
            replaces.addAll(chargeItemDefinition.replaces);
            status = chargeItemDefinition.status;
            experimental = chargeItemDefinition.experimental;
            date = chargeItemDefinition.date;
            publisher = chargeItemDefinition.publisher;
            contact.addAll(chargeItemDefinition.contact);
            description = chargeItemDefinition.description;
            useContext.addAll(chargeItemDefinition.useContext);
            jurisdiction.addAll(chargeItemDefinition.jurisdiction);
            copyright = chargeItemDefinition.copyright;
            approvalDate = chargeItemDefinition.approvalDate;
            lastReviewDate = chargeItemDefinition.lastReviewDate;
            effectivePeriod = chargeItemDefinition.effectivePeriod;
            code = chargeItemDefinition.code;
            instance.addAll(chargeItemDefinition.instance);
            applicability.addAll(chargeItemDefinition.applicability);
            propertyGroup.addAll(chargeItemDefinition.propertyGroup);
            return this;
        }
    }

    /**
     * Expressions that describe applicability criteria for the billing code.
     */
    public static class Applicability extends BackboneElement {
        private final String description;
        private final String language;
        private final String expression;

        private Applicability(Builder builder) {
            super(builder);
            description = builder.description;
            language = builder.language;
            expression = builder.expression;
        }

        /**
         * A brief, natural language description of the condition that effectively communicates the intended semantics.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * The media type of the language for the expression, e.g. "text/cql" for Clinical Query Language expressions or 
         * "text/fhirpath" for FHIRPath expressions.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getLanguage() {
            return language;
        }

        /**
         * An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath 
         * expressions, the %context environment variable must be replaced at runtime with the ChargeItem resource to which this 
         * definition is applied.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getExpression() {
            return expression;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (language != null) || 
                (expression != null);
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
                    accept(description, "description", visitor);
                    accept(language, "language", visitor);
                    accept(expression, "expression", visitor);
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
            Applicability other = (Applicability) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(language, other.language) && 
                Objects.equals(expression, other.expression);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    language, 
                    expression);
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
            private String description;
            private String language;
            private String expression;

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
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Natural language description of the condition
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #description(org.linuxforhealth.fhir.model.type.String)
             */
            public Builder description(java.lang.String description) {
                this.description = (description == null) ? null : String.of(description);
                return this;
            }

            /**
             * A brief, natural language description of the condition that effectively communicates the intended semantics.
             * 
             * @param description
             *     Natural language description of the condition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Convenience method for setting {@code language}.
             * 
             * @param language
             *     Language of the expression
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #language(org.linuxforhealth.fhir.model.type.String)
             */
            public Builder language(java.lang.String language) {
                this.language = (language == null) ? null : String.of(language);
                return this;
            }

            /**
             * The media type of the language for the expression, e.g. "text/cql" for Clinical Query Language expressions or 
             * "text/fhirpath" for FHIRPath expressions.
             * 
             * @param language
             *     Language of the expression
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder language(String language) {
                this.language = language;
                return this;
            }

            /**
             * Convenience method for setting {@code expression}.
             * 
             * @param expression
             *     Boolean-valued expression
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #expression(org.linuxforhealth.fhir.model.type.String)
             */
            public Builder expression(java.lang.String expression) {
                this.expression = (expression == null) ? null : String.of(expression);
                return this;
            }

            /**
             * An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath 
             * expressions, the %context environment variable must be replaced at runtime with the ChargeItem resource to which this 
             * definition is applied.
             * 
             * @param expression
             *     Boolean-valued expression
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder expression(String expression) {
                this.expression = expression;
                return this;
            }

            /**
             * Build the {@link Applicability}
             * 
             * @return
             *     An immutable object of type {@link Applicability}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Applicability per the base specification
             */
            @Override
            public Applicability build() {
                Applicability applicability = new Applicability(this);
                if (validating) {
                    validate(applicability);
                }
                return applicability;
            }

            protected void validate(Applicability applicability) {
                super.validate(applicability);
                ValidationSupport.requireValueOrChildren(applicability);
            }

            protected Builder from(Applicability applicability) {
                super.from(applicability);
                description = applicability.description;
                language = applicability.language;
                expression = applicability.expression;
                return this;
            }
        }
    }

    /**
     * Group of properties which are applicable under the same conditions. If no applicability rules are established for the 
     * group, then all properties always apply.
     */
    public static class PropertyGroup extends BackboneElement {
        private final List<ChargeItemDefinition.Applicability> applicability;
        private final List<PriceComponent> priceComponent;

        private PropertyGroup(Builder builder) {
            super(builder);
            applicability = Collections.unmodifiableList(builder.applicability);
            priceComponent = Collections.unmodifiableList(builder.priceComponent);
        }

        /**
         * Expressions that describe applicability criteria for the priceComponent.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Applicability} that may be empty.
         */
        public List<ChargeItemDefinition.Applicability> getApplicability() {
            return applicability;
        }

        /**
         * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
         * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
         * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
         * the Invoice of how the prices have been calculated.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PriceComponent} that may be empty.
         */
        public List<PriceComponent> getPriceComponent() {
            return priceComponent;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !applicability.isEmpty() || 
                !priceComponent.isEmpty();
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
                    accept(applicability, "applicability", visitor, ChargeItemDefinition.Applicability.class);
                    accept(priceComponent, "priceComponent", visitor, PriceComponent.class);
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
            PropertyGroup other = (PropertyGroup) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(applicability, other.applicability) && 
                Objects.equals(priceComponent, other.priceComponent);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    applicability, 
                    priceComponent);
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
            private List<ChargeItemDefinition.Applicability> applicability = new ArrayList<>();
            private List<PriceComponent> priceComponent = new ArrayList<>();

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
             * Expressions that describe applicability criteria for the priceComponent.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param applicability
             *     Conditions under which the priceComponent is applicable
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder applicability(ChargeItemDefinition.Applicability... applicability) {
                for (ChargeItemDefinition.Applicability value : applicability) {
                    this.applicability.add(value);
                }
                return this;
            }

            /**
             * Expressions that describe applicability criteria for the priceComponent.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param applicability
             *     Conditions under which the priceComponent is applicable
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder applicability(Collection<ChargeItemDefinition.Applicability> applicability) {
                this.applicability = new ArrayList<>(applicability);
                return this;
            }

            /**
             * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
             * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
             * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
             * the Invoice of how the prices have been calculated.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param priceComponent
             *     Components of total line item price
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder priceComponent(PriceComponent... priceComponent) {
                for (PriceComponent value : priceComponent) {
                    this.priceComponent.add(value);
                }
                return this;
            }

            /**
             * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
             * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
             * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
             * the Invoice of how the prices have been calculated.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param priceComponent
             *     Components of total line item price
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder priceComponent(Collection<PriceComponent> priceComponent) {
                this.priceComponent = new ArrayList<>(priceComponent);
                return this;
            }

            /**
             * Build the {@link PropertyGroup}
             * 
             * @return
             *     An immutable object of type {@link PropertyGroup}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid PropertyGroup per the base specification
             */
            @Override
            public PropertyGroup build() {
                PropertyGroup propertyGroup = new PropertyGroup(this);
                if (validating) {
                    validate(propertyGroup);
                }
                return propertyGroup;
            }

            protected void validate(PropertyGroup propertyGroup) {
                super.validate(propertyGroup);
                ValidationSupport.checkList(propertyGroup.applicability, "applicability", ChargeItemDefinition.Applicability.class);
                ValidationSupport.checkList(propertyGroup.priceComponent, "priceComponent", PriceComponent.class);
                ValidationSupport.requireValueOrChildren(propertyGroup);
            }

            protected Builder from(PropertyGroup propertyGroup) {
                super.from(propertyGroup);
                applicability.addAll(propertyGroup.applicability);
                priceComponent.addAll(propertyGroup.priceComponent);
                return this;
            }
        }

        /**
         * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain 
         * conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing 
         * code is currently under development. The priceComponent element can be used to offer transparency to the recipient of 
         * the Invoice of how the prices have been calculated.
         */
        public static class PriceComponent extends BackboneElement {
            @Binding(
                bindingName = "ChargeItemDefinitionPriceComponentType",
                strength = BindingStrength.Value.REQUIRED,
                description = "Codes indicating the kind of the price component.",
                valueSet = "http://hl7.org/fhir/ValueSet/invoice-priceComponentType|4.3.0"
            )
            @Required
            private final ChargeItemDefinitionPriceComponentType type;
            private final CodeableConcept code;
            private final Decimal factor;
            private final Money amount;

            private PriceComponent(Builder builder) {
                super(builder);
                type = builder.type;
                code = builder.code;
                factor = builder.factor;
                amount = builder.amount;
            }

            /**
             * This code identifies the type of the component.
             * 
             * @return
             *     An immutable object of type {@link ChargeItemDefinitionPriceComponentType} that is non-null.
             */
            public ChargeItemDefinitionPriceComponentType getType() {
                return type;
            }

            /**
             * A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts 
             * etc.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getCode() {
                return code;
            }

            /**
             * The factor that has been applied on the base price for calculating this component.
             * 
             * @return
             *     An immutable object of type {@link Decimal} that may be null.
             */
            public Decimal getFactor() {
                return factor;
            }

            /**
             * The amount calculated for this component.
             * 
             * @return
             *     An immutable object of type {@link Money} that may be null.
             */
            public Money getAmount() {
                return amount;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (code != null) || 
                    (factor != null) || 
                    (amount != null);
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
                        accept(code, "code", visitor);
                        accept(factor, "factor", visitor);
                        accept(amount, "amount", visitor);
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
                PriceComponent other = (PriceComponent) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(code, other.code) && 
                    Objects.equals(factor, other.factor) && 
                    Objects.equals(amount, other.amount);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        code, 
                        factor, 
                        amount);
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
                private ChargeItemDefinitionPriceComponentType type;
                private CodeableConcept code;
                private Decimal factor;
                private Money amount;

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
                 * This code identifies the type of the component.
                 * 
                 * <p>This element is required.
                 * 
                 * @param type
                 *     base | surcharge | deduction | discount | tax | informational
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(ChargeItemDefinitionPriceComponentType type) {
                    this.type = type;
                    return this;
                }

                /**
                 * A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts 
                 * etc.
                 * 
                 * @param code
                 *     Code identifying the specific component
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder code(CodeableConcept code) {
                    this.code = code;
                    return this;
                }

                /**
                 * The factor that has been applied on the base price for calculating this component.
                 * 
                 * @param factor
                 *     Factor used for calculating this component
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder factor(Decimal factor) {
                    this.factor = factor;
                    return this;
                }

                /**
                 * The amount calculated for this component.
                 * 
                 * @param amount
                 *     Monetary amount associated with this component
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder amount(Money amount) {
                    this.amount = amount;
                    return this;
                }

                /**
                 * Build the {@link PriceComponent}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>type</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link PriceComponent}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid PriceComponent per the base specification
                 */
                @Override
                public PriceComponent build() {
                    PriceComponent priceComponent = new PriceComponent(this);
                    if (validating) {
                        validate(priceComponent);
                    }
                    return priceComponent;
                }

                protected void validate(PriceComponent priceComponent) {
                    super.validate(priceComponent);
                    ValidationSupport.requireNonNull(priceComponent.type, "type");
                    ValidationSupport.requireValueOrChildren(priceComponent);
                }

                protected Builder from(PriceComponent priceComponent) {
                    super.from(priceComponent);
                    type = priceComponent.type;
                    code = priceComponent.code;
                    factor = priceComponent.factor;
                    amount = priceComponent.amount;
                    return this;
                }
            }
        }
    }
}
