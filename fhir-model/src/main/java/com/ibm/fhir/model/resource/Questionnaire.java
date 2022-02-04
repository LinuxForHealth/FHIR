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
import com.ibm.fhir.model.type.Attachment;
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
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.EnableWhenBehavior;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.QuestionnaireItemOperator;
import com.ibm.fhir.model.type.code.QuestionnaireItemType;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide 
 * detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "que-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-1",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Group items must have nested items, display items cannot have nested items",
    expression = "(type='group' implies item.empty().not()) and (type.trace('type')='display' implies item.trace('item').empty())",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-2",
    level = "Rule",
    location = "(base)",
    description = "The link ids for groups and questions must be unique within the questionnaire",
    expression = "descendants().linkId.isDistinct()",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-3",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Display items cannot have a \"code\" asserted",
    expression = "type!='display' or code.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-4",
    level = "Rule",
    location = "Questionnaire.item",
    description = "A question cannot have both answerOption and answerValueSet",
    expression = "answerOption.empty() or answerValueSet.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-5",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Only 'choice' and 'open-choice' items can have answerValueSet",
    expression = "(type ='choice' or type = 'open-choice' or type = 'decimal' or type = 'integer' or type = 'date' or type = 'dateTime' or type = 'time' or type = 'string' or type = 'quantity') or (answerValueSet.empty() and answerOption.empty())",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-6",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Required and repeat aren't permitted for display items",
    expression = "type!='display' or (required.empty() and repeats.empty())",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-7",
    level = "Rule",
    location = "Questionnaire.item.enableWhen",
    description = "If the operator is 'exists', the value must be a boolean",
    expression = "operator = 'exists' implies (answer is boolean)",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-8",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Initial values can't be specified for groups or display items",
    expression = "(type!='group' and type!='display') or initial.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-9",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Read-only can't be specified for \"display\" items",
    expression = "type!='display' or readOnly.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-10",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Maximum length can only be declared for simple question types",
    expression = "(type in ('boolean' | 'decimal' | 'integer' | 'string' | 'text' | 'url' | 'open-choice')) or maxLength.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-11",
    level = "Rule",
    location = "Questionnaire.item",
    description = "If one or more answerOption is present, initial[x] must be missing",
    expression = "answerOption.empty() or initial.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-12",
    level = "Rule",
    location = "Questionnaire.item",
    description = "If there are more than one enableWhen, enableBehavior must be specified",
    expression = "enableWhen.count() > 1 implies enableBehavior.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "que-13",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Can only have multiple initial values for repeating items",
    expression = "repeats=true or initial.count() <= 1",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire"
)
@Constraint(
    id = "questionnaire-14",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/jurisdiction",
    expression = "jurisdiction.exists() implies (jurisdiction.all(memberOf('http://hl7.org/fhir/ValueSet/jurisdiction', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/Questionnaire",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Questionnaire extends DomainResource {
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
    private final List<Canonical> derivedFrom;
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
    @Binding(
        bindingName = "ResourceType",
        strength = BindingStrength.Value.REQUIRED,
        description = "One of the resource types defined as part of this version of FHIR.",
        valueSet = "http://hl7.org/fhir/ValueSet/resource-types|4.3.0-CIBUILD"
    )
    private final List<ResourceTypeCode> subjectType;
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
    private final Date approvalDate;
    private final Date lastReviewDate;
    @Summary
    private final Period effectivePeriod;
    @Summary
    @Binding(
        bindingName = "QuestionnaireConcept",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes for questionnaires, groups and individual questions.",
        valueSet = "http://hl7.org/fhir/ValueSet/questionnaire-questions"
    )
    private final List<Coding> code;
    private final List<Item> item;

    private Questionnaire(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = builder.name;
        title = builder.title;
        derivedFrom = Collections.unmodifiableList(builder.derivedFrom);
        status = builder.status;
        experimental = builder.experimental;
        subjectType = Collections.unmodifiableList(builder.subjectType);
        date = builder.date;
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        description = builder.description;
        useContext = Collections.unmodifiableList(builder.useContext);
        jurisdiction = Collections.unmodifiableList(builder.jurisdiction);
        purpose = builder.purpose;
        copyright = builder.copyright;
        approvalDate = builder.approvalDate;
        lastReviewDate = builder.lastReviewDate;
        effectivePeriod = builder.effectivePeriod;
        code = Collections.unmodifiableList(builder.code);
        item = Collections.unmodifiableList(builder.item);
    }

    /**
     * An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or 
     * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this questionnaire is (or will be) published. This URL can be the target 
     * of a canonical reference. It SHALL remain the same when the questionnaire is stored on different servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced 
     * in a specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be 
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
     * A natural language name identifying the questionnaire. This name should be usable as an identifier for the module by 
     * machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the questionnaire.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The URL of a Questionnaire that this Questionnaire is based on.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * The status of this questionnaire. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * A Boolean value to indicate that this questionnaire is authored for testing purposes (or 
     * education/evaluation/marketing) and is not intended to be used for genuine usage.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getExperimental() {
        return experimental;
    }

    /**
     * The types of subjects that can be the subject of responses created for the questionnaire.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ResourceTypeCode} that may be empty.
     */
    public List<ResourceTypeCode> getSubjectType() {
        return subjectType;
    }

    /**
     * The date (and optionally time) when the questionnaire was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the questionnaire changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The name of the organization or individual that published the questionnaire.
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
     * A free text natural language description of the questionnaire from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate questionnaire instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * A legal or geographic region in which the questionnaire is intended to be used.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getJurisdiction() {
        return jurisdiction;
    }

    /**
     * Explanation of why this questionnaire is needed and why it has been designed as it has.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getPurpose() {
        return purpose;
    }

    /**
     * A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the questionnaire.
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
     * The period during which the questionnaire content was or is planned to be in active use.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getEffectivePeriod() {
        return effectivePeriod;
    }

    /**
     * An identifier for this question or group of questions in a particular terminology such as LOINC.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
     */
    public List<Coding> getCode() {
        return code;
    }

    /**
     * A particular question, question grouping or display text that is part of the questionnaire.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Item} that may be empty.
     */
    public List<Item> getItem() {
        return item;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            !identifier.isEmpty() || 
            (version != null) || 
            (name != null) || 
            (title != null) || 
            !derivedFrom.isEmpty() || 
            (status != null) || 
            (experimental != null) || 
            !subjectType.isEmpty() || 
            (date != null) || 
            (publisher != null) || 
            !contact.isEmpty() || 
            (description != null) || 
            !useContext.isEmpty() || 
            !jurisdiction.isEmpty() || 
            (purpose != null) || 
            (copyright != null) || 
            (approvalDate != null) || 
            (lastReviewDate != null) || 
            (effectivePeriod != null) || 
            !code.isEmpty() || 
            !item.isEmpty();
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
                accept(derivedFrom, "derivedFrom", visitor, Canonical.class);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(subjectType, "subjectType", visitor, ResourceTypeCode.class);
                accept(date, "date", visitor);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(description, "description", visitor);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                accept(purpose, "purpose", visitor);
                accept(copyright, "copyright", visitor);
                accept(approvalDate, "approvalDate", visitor);
                accept(lastReviewDate, "lastReviewDate", visitor);
                accept(effectivePeriod, "effectivePeriod", visitor);
                accept(code, "code", visitor, Coding.class);
                accept(item, "item", visitor, Item.class);
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
        Questionnaire other = (Questionnaire) obj;
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
            Objects.equals(derivedFrom, other.derivedFrom) && 
            Objects.equals(status, other.status) && 
            Objects.equals(experimental, other.experimental) && 
            Objects.equals(subjectType, other.subjectType) && 
            Objects.equals(date, other.date) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(description, other.description) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(jurisdiction, other.jurisdiction) && 
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(copyright, other.copyright) && 
            Objects.equals(approvalDate, other.approvalDate) && 
            Objects.equals(lastReviewDate, other.lastReviewDate) && 
            Objects.equals(effectivePeriod, other.effectivePeriod) && 
            Objects.equals(code, other.code) && 
            Objects.equals(item, other.item);
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
                derivedFrom, 
                status, 
                experimental, 
                subjectType, 
                date, 
                publisher, 
                contact, 
                description, 
                useContext, 
                jurisdiction, 
                purpose, 
                copyright, 
                approvalDate, 
                lastReviewDate, 
                effectivePeriod, 
                code, 
                item);
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
        private List<Canonical> derivedFrom = new ArrayList<>();
        private PublicationStatus status;
        private Boolean experimental;
        private List<ResourceTypeCode> subjectType = new ArrayList<>();
        private DateTime date;
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private Markdown description;
        private List<UsageContext> useContext = new ArrayList<>();
        private List<CodeableConcept> jurisdiction = new ArrayList<>();
        private Markdown purpose;
        private Markdown copyright;
        private Date approvalDate;
        private Date lastReviewDate;
        private Period effectivePeriod;
        private List<Coding> code = new ArrayList<>();
        private List<Item> item = new ArrayList<>();

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
         * An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or 
         * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
         * which at which an authoritative instance of this questionnaire is (or will be) published. This URL can be the target 
         * of a canonical reference. It SHALL remain the same when the questionnaire is stored on different servers.
         * 
         * @param url
         *     Canonical identifier for this questionnaire, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the questionnaire
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
         * A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the questionnaire
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
         *     Business version of the questionnaire
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
         * The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence.
         * 
         * @param version
         *     Business version of the questionnaire
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
         *     Name for this questionnaire (computer friendly)
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
         * A natural language name identifying the questionnaire. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
         * 
         * @param name
         *     Name for this questionnaire (computer friendly)
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
         *     Name for this questionnaire (human friendly)
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
         * A short, descriptive, user-friendly title for the questionnaire.
         * 
         * @param title
         *     Name for this questionnaire (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The URL of a Questionnaire that this Questionnaire is based on.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param derivedFrom
         *     Instantiates protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder derivedFrom(Canonical... derivedFrom) {
            for (Canonical value : derivedFrom) {
                this.derivedFrom.add(value);
            }
            return this;
        }

        /**
         * The URL of a Questionnaire that this Questionnaire is based on.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param derivedFrom
         *     Instantiates protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder derivedFrom(Collection<Canonical> derivedFrom) {
            this.derivedFrom = new ArrayList<>(derivedFrom);
            return this;
        }

        /**
         * The status of this questionnaire. Enables tracking the life-cycle of the content.
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
         * A Boolean value to indicate that this questionnaire is authored for testing purposes (or 
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
         * The types of subjects that can be the subject of responses created for the questionnaire.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param subjectType
         *     Resource that can be subject of QuestionnaireResponse
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subjectType(ResourceTypeCode... subjectType) {
            for (ResourceTypeCode value : subjectType) {
                this.subjectType.add(value);
            }
            return this;
        }

        /**
         * The types of subjects that can be the subject of responses created for the questionnaire.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param subjectType
         *     Resource that can be subject of QuestionnaireResponse
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder subjectType(Collection<ResourceTypeCode> subjectType) {
            this.subjectType = new ArrayList<>(subjectType);
            return this;
        }

        /**
         * The date (and optionally time) when the questionnaire was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the questionnaire changes.
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
         * The name of the organization or individual that published the questionnaire.
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
         * A free text natural language description of the questionnaire from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the questionnaire
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
         * may be used to assist with indexing and searching for appropriate questionnaire instances.
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
         * may be used to assist with indexing and searching for appropriate questionnaire instances.
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
         * A legal or geographic region in which the questionnaire is intended to be used.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for questionnaire (if applicable)
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
         * A legal or geographic region in which the questionnaire is intended to be used.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param jurisdiction
         *     Intended jurisdiction for questionnaire (if applicable)
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
         * Explanation of why this questionnaire is needed and why it has been designed as it has.
         * 
         * @param purpose
         *     Why this questionnaire is defined
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Markdown purpose) {
            this.purpose = purpose;
            return this;
        }

        /**
         * A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the questionnaire.
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
         *     When the questionnaire was approved by publisher
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #approvalDate(com.ibm.fhir.model.type.Date)
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
         *     When the questionnaire was approved by publisher
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
         *     When the questionnaire was last reviewed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #lastReviewDate(com.ibm.fhir.model.type.Date)
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
         *     When the questionnaire was last reviewed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastReviewDate(Date lastReviewDate) {
            this.lastReviewDate = lastReviewDate;
            return this;
        }

        /**
         * The period during which the questionnaire content was or is planned to be in active use.
         * 
         * @param effectivePeriod
         *     When the questionnaire is expected to be used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effectivePeriod(Period effectivePeriod) {
            this.effectivePeriod = effectivePeriod;
            return this;
        }

        /**
         * An identifier for this question or group of questions in a particular terminology such as LOINC.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param code
         *     Concept that represents the overall questionnaire
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Coding... code) {
            for (Coding value : code) {
                this.code.add(value);
            }
            return this;
        }

        /**
         * An identifier for this question or group of questions in a particular terminology such as LOINC.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param code
         *     Concept that represents the overall questionnaire
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder code(Collection<Coding> code) {
            this.code = new ArrayList<>(code);
            return this;
        }

        /**
         * A particular question, question grouping or display text that is part of the questionnaire.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param item
         *     Questions and sections within the Questionnaire
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder item(Item... item) {
            for (Item value : item) {
                this.item.add(value);
            }
            return this;
        }

        /**
         * A particular question, question grouping or display text that is part of the questionnaire.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param item
         *     Questions and sections within the Questionnaire
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder item(Collection<Item> item) {
            this.item = new ArrayList<>(item);
            return this;
        }

        /**
         * Build the {@link Questionnaire}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Questionnaire}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Questionnaire per the base specification
         */
        @Override
        public Questionnaire build() {
            Questionnaire questionnaire = new Questionnaire(this);
            if (validating) {
                validate(questionnaire);
            }
            return questionnaire;
        }

        protected void validate(Questionnaire questionnaire) {
            super.validate(questionnaire);
            ValidationSupport.checkList(questionnaire.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(questionnaire.derivedFrom, "derivedFrom", Canonical.class);
            ValidationSupport.requireNonNull(questionnaire.status, "status");
            ValidationSupport.checkList(questionnaire.subjectType, "subjectType", ResourceTypeCode.class);
            ValidationSupport.checkList(questionnaire.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(questionnaire.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(questionnaire.jurisdiction, "jurisdiction", CodeableConcept.class);
            ValidationSupport.checkList(questionnaire.code, "code", Coding.class);
            ValidationSupport.checkList(questionnaire.item, "item", Item.class);
        }

        protected Builder from(Questionnaire questionnaire) {
            super.from(questionnaire);
            url = questionnaire.url;
            identifier.addAll(questionnaire.identifier);
            version = questionnaire.version;
            name = questionnaire.name;
            title = questionnaire.title;
            derivedFrom.addAll(questionnaire.derivedFrom);
            status = questionnaire.status;
            experimental = questionnaire.experimental;
            subjectType.addAll(questionnaire.subjectType);
            date = questionnaire.date;
            publisher = questionnaire.publisher;
            contact.addAll(questionnaire.contact);
            description = questionnaire.description;
            useContext.addAll(questionnaire.useContext);
            jurisdiction.addAll(questionnaire.jurisdiction);
            purpose = questionnaire.purpose;
            copyright = questionnaire.copyright;
            approvalDate = questionnaire.approvalDate;
            lastReviewDate = questionnaire.lastReviewDate;
            effectivePeriod = questionnaire.effectivePeriod;
            code.addAll(questionnaire.code);
            item.addAll(questionnaire.item);
            return this;
        }
    }

    /**
     * A particular question, question grouping or display text that is part of the questionnaire.
     */
    public static class Item extends BackboneElement {
        @Required
        private final String linkId;
        private final Uri definition;
        @Binding(
            bindingName = "QuestionnaireConcept",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes for questionnaires, groups and individual questions.",
            valueSet = "http://hl7.org/fhir/ValueSet/questionnaire-questions"
        )
        private final List<Coding> code;
        private final String prefix;
        private final String text;
        @Binding(
            bindingName = "QuestionnaireItemType",
            strength = BindingStrength.Value.REQUIRED,
            description = "Distinguishes groups from questions and display text and indicates data type for questions.",
            valueSet = "http://hl7.org/fhir/ValueSet/item-type|4.3.0-CIBUILD"
        )
        @Required
        private final QuestionnaireItemType type;
        private final List<EnableWhen> enableWhen;
        @Binding(
            bindingName = "EnableWhenBehavior",
            strength = BindingStrength.Value.REQUIRED,
            description = "Controls how multiple enableWhen values are interpreted -  whether all or any must be true.",
            valueSet = "http://hl7.org/fhir/ValueSet/questionnaire-enable-behavior|4.3.0-CIBUILD"
        )
        private final EnableWhenBehavior enableBehavior;
        private final Boolean required;
        private final Boolean repeats;
        private final Boolean readOnly;
        private final Integer maxLength;
        private final Canonical answerValueSet;
        private final List<AnswerOption> answerOption;
        private final List<Initial> initial;
        private final List<Questionnaire.Item> item;

        private Item(Builder builder) {
            super(builder);
            linkId = builder.linkId;
            definition = builder.definition;
            code = Collections.unmodifiableList(builder.code);
            prefix = builder.prefix;
            text = builder.text;
            type = builder.type;
            enableWhen = Collections.unmodifiableList(builder.enableWhen);
            enableBehavior = builder.enableBehavior;
            required = builder.required;
            repeats = builder.repeats;
            readOnly = builder.readOnly;
            maxLength = builder.maxLength;
            answerValueSet = builder.answerValueSet;
            answerOption = Collections.unmodifiableList(builder.answerOption);
            initial = Collections.unmodifiableList(builder.initial);
            item = Collections.unmodifiableList(builder.item);
        }

        /**
         * An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a 
         * QuestionnaireResponse resource.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getLinkId() {
            return linkId;
        }

        /**
         * This element is a URI that refers to an [ElementDefinition](elementdefinition.html) that provides information about 
         * this item, including information that might otherwise be included in the instance of the Questionnaire resource. A 
         * detailed description of the construction of the URI is shown in Comments, below. If this element is present then the 
         * following element values MAY be derived from the Element Definition if the corresponding elements of this 
         * Questionnaire resource instance have no value:
         * 
         * <p>* code (ElementDefinition.code) 
         * <p>* type (ElementDefinition.type) 
         * <p>* required (ElementDefinition.min) 
         * <p>* repeats (ElementDefinition.max) 
         * <p>* maxLength (ElementDefinition.maxLength) 
         * <p>* answerValueSet (ElementDefinition.binding)
         * <p>* options (ElementDefinition.binding).
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getDefinition() {
            return definition;
        }

        /**
         * A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions 
         * and answers).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Coding} that may be empty.
         */
        public List<Coding> getCode() {
            return code;
        }

        /**
         * A short label for a particular group, question or set of display text within the questionnaire used for reference by 
         * the individual completing the questionnaire.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * The name of a section, the text of a question or text content for a display item.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getText() {
            return text;
        }

        /**
         * The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of 
         * data to be captured (string, integer, coded choice, etc.).
         * 
         * @return
         *     An immutable object of type {@link QuestionnaireItemType} that is non-null.
         */
        public QuestionnaireItemType getType() {
            return type;
        }

        /**
         * A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the 
         * specified condition is true.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link EnableWhen} that may be empty.
         */
        public List<EnableWhen> getEnableWhen() {
            return enableWhen;
        }

        /**
         * Controls how multiple enableWhen values are interpreted - whether all or any must be true.
         * 
         * @return
         *     An immutable object of type {@link EnableWhenBehavior} that may be null.
         */
        public EnableWhenBehavior getEnableBehavior() {
            return enableBehavior;
        }

        /**
         * An indication, if true, that the item must be present in a "completed" QuestionnaireResponse. If false, the item may 
         * be skipped when answering the questionnaire.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getRequired() {
            return required;
        }

        /**
         * An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for 
         * questions or multiple sets of answers for groups.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getRepeats() {
            return repeats;
        }

        /**
         * An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getReadOnly() {
            return readOnly;
        }

        /**
         * The maximum number of characters that are permitted in the answer to be considered a "valid" QuestionnaireResponse.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getMaxLength() {
            return maxLength;
        }

        /**
         * A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" 
         * question.
         * 
         * @return
         *     An immutable object of type {@link Canonical} that may be null.
         */
        public Canonical getAnswerValueSet() {
            return answerValueSet;
        }

        /**
         * One of the permitted answers for a "choice" or "open-choice" question.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link AnswerOption} that may be empty.
         */
        public List<AnswerOption> getAnswerOption() {
            return answerOption;
        }

        /**
         * One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user 
         * input.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Initial} that may be empty.
         */
        public List<Initial> getInitial() {
            return initial;
        }

        /**
         * Text, questions and other groups to be nested beneath a question or group.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Item} that may be empty.
         */
        public List<Questionnaire.Item> getItem() {
            return item;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (linkId != null) || 
                (definition != null) || 
                !code.isEmpty() || 
                (prefix != null) || 
                (text != null) || 
                (type != null) || 
                !enableWhen.isEmpty() || 
                (enableBehavior != null) || 
                (required != null) || 
                (repeats != null) || 
                (readOnly != null) || 
                (maxLength != null) || 
                (answerValueSet != null) || 
                !answerOption.isEmpty() || 
                !initial.isEmpty() || 
                !item.isEmpty();
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
                    accept(linkId, "linkId", visitor);
                    accept(definition, "definition", visitor);
                    accept(code, "code", visitor, Coding.class);
                    accept(prefix, "prefix", visitor);
                    accept(text, "text", visitor);
                    accept(type, "type", visitor);
                    accept(enableWhen, "enableWhen", visitor, EnableWhen.class);
                    accept(enableBehavior, "enableBehavior", visitor);
                    accept(required, "required", visitor);
                    accept(repeats, "repeats", visitor);
                    accept(readOnly, "readOnly", visitor);
                    accept(maxLength, "maxLength", visitor);
                    accept(answerValueSet, "answerValueSet", visitor);
                    accept(answerOption, "answerOption", visitor, AnswerOption.class);
                    accept(initial, "initial", visitor, Initial.class);
                    accept(item, "item", visitor, Questionnaire.Item.class);
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
            Item other = (Item) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(linkId, other.linkId) && 
                Objects.equals(definition, other.definition) && 
                Objects.equals(code, other.code) && 
                Objects.equals(prefix, other.prefix) && 
                Objects.equals(text, other.text) && 
                Objects.equals(type, other.type) && 
                Objects.equals(enableWhen, other.enableWhen) && 
                Objects.equals(enableBehavior, other.enableBehavior) && 
                Objects.equals(required, other.required) && 
                Objects.equals(repeats, other.repeats) && 
                Objects.equals(readOnly, other.readOnly) && 
                Objects.equals(maxLength, other.maxLength) && 
                Objects.equals(answerValueSet, other.answerValueSet) && 
                Objects.equals(answerOption, other.answerOption) && 
                Objects.equals(initial, other.initial) && 
                Objects.equals(item, other.item);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    linkId, 
                    definition, 
                    code, 
                    prefix, 
                    text, 
                    type, 
                    enableWhen, 
                    enableBehavior, 
                    required, 
                    repeats, 
                    readOnly, 
                    maxLength, 
                    answerValueSet, 
                    answerOption, 
                    initial, 
                    item);
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
            private String linkId;
            private Uri definition;
            private List<Coding> code = new ArrayList<>();
            private String prefix;
            private String text;
            private QuestionnaireItemType type;
            private List<EnableWhen> enableWhen = new ArrayList<>();
            private EnableWhenBehavior enableBehavior;
            private Boolean required;
            private Boolean repeats;
            private Boolean readOnly;
            private Integer maxLength;
            private Canonical answerValueSet;
            private List<AnswerOption> answerOption = new ArrayList<>();
            private List<Initial> initial = new ArrayList<>();
            private List<Questionnaire.Item> item = new ArrayList<>();

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
             * Convenience method for setting {@code linkId}.
             * 
             * <p>This element is required.
             * 
             * @param linkId
             *     Unique id for item in questionnaire
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #linkId(com.ibm.fhir.model.type.String)
             */
            public Builder linkId(java.lang.String linkId) {
                this.linkId = (linkId == null) ? null : String.of(linkId);
                return this;
            }

            /**
             * An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a 
             * QuestionnaireResponse resource.
             * 
             * <p>This element is required.
             * 
             * @param linkId
             *     Unique id for item in questionnaire
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder linkId(String linkId) {
                this.linkId = linkId;
                return this;
            }

            /**
             * This element is a URI that refers to an [ElementDefinition](elementdefinition.html) that provides information about 
             * this item, including information that might otherwise be included in the instance of the Questionnaire resource. A 
             * detailed description of the construction of the URI is shown in Comments, below. If this element is present then the 
             * following element values MAY be derived from the Element Definition if the corresponding elements of this 
             * Questionnaire resource instance have no value:
             * 
             * <p>* code (ElementDefinition.code) 
             * <p>* type (ElementDefinition.type) 
             * <p>* required (ElementDefinition.min) 
             * <p>* repeats (ElementDefinition.max) 
             * <p>* maxLength (ElementDefinition.maxLength) 
             * <p>* answerValueSet (ElementDefinition.binding)
             * <p>* options (ElementDefinition.binding).
             * 
             * @param definition
             *     ElementDefinition - details for the item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder definition(Uri definition) {
                this.definition = definition;
                return this;
            }

            /**
             * A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions 
             * and answers).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     Corresponding concept for this item in a terminology
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(Coding... code) {
                for (Coding value : code) {
                    this.code.add(value);
                }
                return this;
            }

            /**
             * A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions 
             * and answers).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     Corresponding concept for this item in a terminology
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder code(Collection<Coding> code) {
                this.code = new ArrayList<>(code);
                return this;
            }

            /**
             * Convenience method for setting {@code prefix}.
             * 
             * @param prefix
             *     E.g. "1(a)", "2.5.3"
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #prefix(com.ibm.fhir.model.type.String)
             */
            public Builder prefix(java.lang.String prefix) {
                this.prefix = (prefix == null) ? null : String.of(prefix);
                return this;
            }

            /**
             * A short label for a particular group, question or set of display text within the questionnaire used for reference by 
             * the individual completing the questionnaire.
             * 
             * @param prefix
             *     E.g. "1(a)", "2.5.3"
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder prefix(String prefix) {
                this.prefix = prefix;
                return this;
            }

            /**
             * Convenience method for setting {@code text}.
             * 
             * @param text
             *     Primary text for the item
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #text(com.ibm.fhir.model.type.String)
             */
            public Builder text(java.lang.String text) {
                this.text = (text == null) ? null : String.of(text);
                return this;
            }

            /**
             * The name of a section, the text of a question or text content for a display item.
             * 
             * @param text
             *     Primary text for the item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            /**
             * The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of 
             * data to be captured (string, integer, coded choice, etc.).
             * 
             * <p>This element is required.
             * 
             * @param type
             *     group | display | boolean | decimal | integer | date | dateTime +
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(QuestionnaireItemType type) {
                this.type = type;
                return this;
            }

            /**
             * A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the 
             * specified condition is true.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param enableWhen
             *     Only allow data when
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder enableWhen(EnableWhen... enableWhen) {
                for (EnableWhen value : enableWhen) {
                    this.enableWhen.add(value);
                }
                return this;
            }

            /**
             * A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the 
             * specified condition is true.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param enableWhen
             *     Only allow data when
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder enableWhen(Collection<EnableWhen> enableWhen) {
                this.enableWhen = new ArrayList<>(enableWhen);
                return this;
            }

            /**
             * Controls how multiple enableWhen values are interpreted - whether all or any must be true.
             * 
             * @param enableBehavior
             *     all | any
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder enableBehavior(EnableWhenBehavior enableBehavior) {
                this.enableBehavior = enableBehavior;
                return this;
            }

            /**
             * Convenience method for setting {@code required}.
             * 
             * @param required
             *     Whether the item must be included in data results
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #required(com.ibm.fhir.model.type.Boolean)
             */
            public Builder required(java.lang.Boolean required) {
                this.required = (required == null) ? null : Boolean.of(required);
                return this;
            }

            /**
             * An indication, if true, that the item must be present in a "completed" QuestionnaireResponse. If false, the item may 
             * be skipped when answering the questionnaire.
             * 
             * @param required
             *     Whether the item must be included in data results
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder required(Boolean required) {
                this.required = required;
                return this;
            }

            /**
             * Convenience method for setting {@code repeats}.
             * 
             * @param repeats
             *     Whether the item may repeat
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #repeats(com.ibm.fhir.model.type.Boolean)
             */
            public Builder repeats(java.lang.Boolean repeats) {
                this.repeats = (repeats == null) ? null : Boolean.of(repeats);
                return this;
            }

            /**
             * An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for 
             * questions or multiple sets of answers for groups.
             * 
             * @param repeats
             *     Whether the item may repeat
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder repeats(Boolean repeats) {
                this.repeats = repeats;
                return this;
            }

            /**
             * Convenience method for setting {@code readOnly}.
             * 
             * @param readOnly
             *     Don't allow human editing
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #readOnly(com.ibm.fhir.model.type.Boolean)
             */
            public Builder readOnly(java.lang.Boolean readOnly) {
                this.readOnly = (readOnly == null) ? null : Boolean.of(readOnly);
                return this;
            }

            /**
             * An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.
             * 
             * @param readOnly
             *     Don't allow human editing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder readOnly(Boolean readOnly) {
                this.readOnly = readOnly;
                return this;
            }

            /**
             * Convenience method for setting {@code maxLength}.
             * 
             * @param maxLength
             *     No more than this many characters
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #maxLength(com.ibm.fhir.model.type.Integer)
             */
            public Builder maxLength(java.lang.Integer maxLength) {
                this.maxLength = (maxLength == null) ? null : Integer.of(maxLength);
                return this;
            }

            /**
             * The maximum number of characters that are permitted in the answer to be considered a "valid" QuestionnaireResponse.
             * 
             * @param maxLength
             *     No more than this many characters
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder maxLength(Integer maxLength) {
                this.maxLength = maxLength;
                return this;
            }

            /**
             * A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" 
             * question.
             * 
             * @param answerValueSet
             *     Valueset containing permitted answers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder answerValueSet(Canonical answerValueSet) {
                this.answerValueSet = answerValueSet;
                return this;
            }

            /**
             * One of the permitted answers for a "choice" or "open-choice" question.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param answerOption
             *     Permitted answer
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder answerOption(AnswerOption... answerOption) {
                for (AnswerOption value : answerOption) {
                    this.answerOption.add(value);
                }
                return this;
            }

            /**
             * One of the permitted answers for a "choice" or "open-choice" question.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param answerOption
             *     Permitted answer
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder answerOption(Collection<AnswerOption> answerOption) {
                this.answerOption = new ArrayList<>(answerOption);
                return this;
            }

            /**
             * One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user 
             * input.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param initial
             *     Initial value(s) when item is first rendered
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder initial(Initial... initial) {
                for (Initial value : initial) {
                    this.initial.add(value);
                }
                return this;
            }

            /**
             * One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user 
             * input.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param initial
             *     Initial value(s) when item is first rendered
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder initial(Collection<Initial> initial) {
                this.initial = new ArrayList<>(initial);
                return this;
            }

            /**
             * Text, questions and other groups to be nested beneath a question or group.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param item
             *     Nested questionnaire items
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(Questionnaire.Item... item) {
                for (Questionnaire.Item value : item) {
                    this.item.add(value);
                }
                return this;
            }

            /**
             * Text, questions and other groups to be nested beneath a question or group.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param item
             *     Nested questionnaire items
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder item(Collection<Questionnaire.Item> item) {
                this.item = new ArrayList<>(item);
                return this;
            }

            /**
             * Build the {@link Item}
             * 
             * <p>Required elements:
             * <ul>
             * <li>linkId</li>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Item}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Item per the base specification
             */
            @Override
            public Item build() {
                Item item = new Item(this);
                if (validating) {
                    validate(item);
                }
                return item;
            }

            protected void validate(Item item) {
                super.validate(item);
                ValidationSupport.requireNonNull(item.linkId, "linkId");
                ValidationSupport.checkList(item.code, "code", Coding.class);
                ValidationSupport.requireNonNull(item.type, "type");
                ValidationSupport.checkList(item.enableWhen, "enableWhen", EnableWhen.class);
                ValidationSupport.checkList(item.answerOption, "answerOption", AnswerOption.class);
                ValidationSupport.checkList(item.initial, "initial", Initial.class);
                ValidationSupport.checkList(item.item, "item", Questionnaire.Item.class);
                ValidationSupport.requireValueOrChildren(item);
            }

            protected Builder from(Item item) {
                super.from(item);
                linkId = item.linkId;
                definition = item.definition;
                code.addAll(item.code);
                prefix = item.prefix;
                text = item.text;
                type = item.type;
                enableWhen.addAll(item.enableWhen);
                enableBehavior = item.enableBehavior;
                required = item.required;
                repeats = item.repeats;
                readOnly = item.readOnly;
                maxLength = item.maxLength;
                answerValueSet = item.answerValueSet;
                answerOption.addAll(item.answerOption);
                initial.addAll(item.initial);
                this.item.addAll(item.item);
                return this;
            }
        }

        /**
         * A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the 
         * specified condition is true.
         */
        public static class EnableWhen extends BackboneElement {
            @Required
            private final String question;
            @Binding(
                bindingName = "QuestionnaireItemOperator",
                strength = BindingStrength.Value.REQUIRED,
                description = "The criteria by which a question is enabled.",
                valueSet = "http://hl7.org/fhir/ValueSet/questionnaire-enable-operator|4.3.0-CIBUILD"
            )
            @Required
            private final QuestionnaireItemOperator operator;
            @Choice({ Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Coding.class, Quantity.class, Reference.class })
            @Binding(
                bindingName = "QuestionnaireQuestionOption3",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Allowed values to answer questions.",
                valueSet = "http://hl7.org/fhir/ValueSet/questionnaire-answers"
            )
            @Required
            private final Element answer;

            private EnableWhen(Builder builder) {
                super(builder);
                question = builder.question;
                operator = builder.operator;
                answer = builder.answer;
            }

            /**
             * The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getQuestion() {
                return question;
            }

            /**
             * Specifies the criteria by which the question is enabled.
             * 
             * @return
             *     An immutable object of type {@link QuestionnaireItemOperator} that is non-null.
             */
            public QuestionnaireItemOperator getOperator() {
                return operator;
            }

            /**
             * A value that the referenced question is tested using the specified operator in order for the item to be enabled.
             * 
             * @return
             *     An immutable object of type {@link Boolean}, {@link Decimal}, {@link Integer}, {@link Date}, {@link DateTime}, {@link 
             *     Time}, {@link String}, {@link Coding}, {@link Quantity} or {@link Reference} that is non-null.
             */
            public Element getAnswer() {
                return answer;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (question != null) || 
                    (operator != null) || 
                    (answer != null);
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
                        accept(question, "question", visitor);
                        accept(operator, "operator", visitor);
                        accept(answer, "answer", visitor);
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
                EnableWhen other = (EnableWhen) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(question, other.question) && 
                    Objects.equals(operator, other.operator) && 
                    Objects.equals(answer, other.answer);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        question, 
                        operator, 
                        answer);
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
                private String question;
                private QuestionnaireItemOperator operator;
                private Element answer;

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
                 * Convenience method for setting {@code question}.
                 * 
                 * <p>This element is required.
                 * 
                 * @param question
                 *     Question that determines whether item is enabled
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #question(com.ibm.fhir.model.type.String)
                 */
                public Builder question(java.lang.String question) {
                    this.question = (question == null) ? null : String.of(question);
                    return this;
                }

                /**
                 * The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.
                 * 
                 * <p>This element is required.
                 * 
                 * @param question
                 *     Question that determines whether item is enabled
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder question(String question) {
                    this.question = question;
                    return this;
                }

                /**
                 * Specifies the criteria by which the question is enabled.
                 * 
                 * <p>This element is required.
                 * 
                 * @param operator
                 *     exists | = | != | &gt; | &lt; | &gt;= | &lt;=
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder operator(QuestionnaireItemOperator operator) {
                    this.operator = operator;
                    return this;
                }

                /**
                 * Convenience method for setting {@code answer} with choice type Boolean.
                 * 
                 * <p>This element is required.
                 * 
                 * @param answer
                 *     Value for question comparison based on operator
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #answer(Element)
                 */
                public Builder answer(java.lang.Boolean answer) {
                    this.answer = (answer == null) ? null : Boolean.of(answer);
                    return this;
                }

                /**
                 * Convenience method for setting {@code answer} with choice type Integer.
                 * 
                 * <p>This element is required.
                 * 
                 * @param answer
                 *     Value for question comparison based on operator
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #answer(Element)
                 */
                public Builder answer(java.lang.Integer answer) {
                    this.answer = (answer == null) ? null : Integer.of(answer);
                    return this;
                }

                /**
                 * Convenience method for setting {@code answer} with choice type Date.
                 * 
                 * <p>This element is required.
                 * 
                 * @param answer
                 *     Value for question comparison based on operator
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #answer(Element)
                 */
                public Builder answer(java.time.LocalDate answer) {
                    this.answer = (answer == null) ? null : Date.of(answer);
                    return this;
                }

                /**
                 * Convenience method for setting {@code answer} with choice type Time.
                 * 
                 * <p>This element is required.
                 * 
                 * @param answer
                 *     Value for question comparison based on operator
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #answer(Element)
                 */
                public Builder answer(java.time.LocalTime answer) {
                    this.answer = (answer == null) ? null : Time.of(answer);
                    return this;
                }

                /**
                 * Convenience method for setting {@code answer} with choice type String.
                 * 
                 * <p>This element is required.
                 * 
                 * @param answer
                 *     Value for question comparison based on operator
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #answer(Element)
                 */
                public Builder answer(java.lang.String answer) {
                    this.answer = (answer == null) ? null : String.of(answer);
                    return this;
                }

                /**
                 * A value that the referenced question is tested using the specified operator in order for the item to be enabled.
                 * 
                 * <p>This element is required.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Boolean}</li>
                 * <li>{@link Decimal}</li>
                 * <li>{@link Integer}</li>
                 * <li>{@link Date}</li>
                 * <li>{@link DateTime}</li>
                 * <li>{@link Time}</li>
                 * <li>{@link String}</li>
                 * <li>{@link Coding}</li>
                 * <li>{@link Quantity}</li>
                 * <li>{@link Reference}</li>
                 * </ul>
                 * 
                 * @param answer
                 *     Value for question comparison based on operator
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder answer(Element answer) {
                    this.answer = answer;
                    return this;
                }

                /**
                 * Build the {@link EnableWhen}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>question</li>
                 * <li>operator</li>
                 * <li>answer</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link EnableWhen}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid EnableWhen per the base specification
                 */
                @Override
                public EnableWhen build() {
                    EnableWhen enableWhen = new EnableWhen(this);
                    if (validating) {
                        validate(enableWhen);
                    }
                    return enableWhen;
                }

                protected void validate(EnableWhen enableWhen) {
                    super.validate(enableWhen);
                    ValidationSupport.requireNonNull(enableWhen.question, "question");
                    ValidationSupport.requireNonNull(enableWhen.operator, "operator");
                    ValidationSupport.requireChoiceElement(enableWhen.answer, "answer", Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Coding.class, Quantity.class, Reference.class);
                    ValidationSupport.requireValueOrChildren(enableWhen);
                }

                protected Builder from(EnableWhen enableWhen) {
                    super.from(enableWhen);
                    question = enableWhen.question;
                    operator = enableWhen.operator;
                    answer = enableWhen.answer;
                    return this;
                }
            }
        }

        /**
         * One of the permitted answers for a "choice" or "open-choice" question.
         */
        public static class AnswerOption extends BackboneElement {
            @Choice({ Integer.class, Date.class, Time.class, String.class, Coding.class, Reference.class })
            @Binding(
                bindingName = "QuestionnaireQuestionOption",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Allowed values to answer questions.",
                valueSet = "http://hl7.org/fhir/ValueSet/questionnaire-answers"
            )
            @Required
            private final Element value;
            private final Boolean initialSelected;

            private AnswerOption(Builder builder) {
                super(builder);
                value = builder.value;
                initialSelected = builder.initialSelected;
            }

            /**
             * A potential answer that's allowed as the answer to this question.
             * 
             * @return
             *     An immutable object of type {@link Integer}, {@link Date}, {@link Time}, {@link String}, {@link Coding} or {@link 
             *     Reference} that is non-null.
             */
            public Element getValue() {
                return value;
            }

            /**
             * Indicates whether the answer value is selected when the list of possible answers is initially shown.
             * 
             * @return
             *     An immutable object of type {@link Boolean} that may be null.
             */
            public Boolean getInitialSelected() {
                return initialSelected;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (value != null) || 
                    (initialSelected != null);
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
                        accept(value, "value", visitor);
                        accept(initialSelected, "initialSelected", visitor);
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
                AnswerOption other = (AnswerOption) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(value, other.value) && 
                    Objects.equals(initialSelected, other.initialSelected);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        value, 
                        initialSelected);
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
                private Element value;
                private Boolean initialSelected;

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
                 * Convenience method for setting {@code value} with choice type Integer.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Answer value
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
                 * Convenience method for setting {@code value} with choice type Date.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Answer value
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #value(Element)
                 */
                public Builder value(java.time.LocalDate value) {
                    this.value = (value == null) ? null : Date.of(value);
                    return this;
                }

                /**
                 * Convenience method for setting {@code value} with choice type Time.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Answer value
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #value(Element)
                 */
                public Builder value(java.time.LocalTime value) {
                    this.value = (value == null) ? null : Time.of(value);
                    return this;
                }

                /**
                 * Convenience method for setting {@code value} with choice type String.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Answer value
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
                 * A potential answer that's allowed as the answer to this question.
                 * 
                 * <p>This element is required.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Integer}</li>
                 * <li>{@link Date}</li>
                 * <li>{@link Time}</li>
                 * <li>{@link String}</li>
                 * <li>{@link Coding}</li>
                 * <li>{@link Reference}</li>
                 * </ul>
                 * 
                 * @param value
                 *     Answer value
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(Element value) {
                    this.value = value;
                    return this;
                }

                /**
                 * Convenience method for setting {@code initialSelected}.
                 * 
                 * @param initialSelected
                 *     Whether option is selected by default
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #initialSelected(com.ibm.fhir.model.type.Boolean)
                 */
                public Builder initialSelected(java.lang.Boolean initialSelected) {
                    this.initialSelected = (initialSelected == null) ? null : Boolean.of(initialSelected);
                    return this;
                }

                /**
                 * Indicates whether the answer value is selected when the list of possible answers is initially shown.
                 * 
                 * @param initialSelected
                 *     Whether option is selected by default
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder initialSelected(Boolean initialSelected) {
                    this.initialSelected = initialSelected;
                    return this;
                }

                /**
                 * Build the {@link AnswerOption}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>value</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link AnswerOption}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid AnswerOption per the base specification
                 */
                @Override
                public AnswerOption build() {
                    AnswerOption answerOption = new AnswerOption(this);
                    if (validating) {
                        validate(answerOption);
                    }
                    return answerOption;
                }

                protected void validate(AnswerOption answerOption) {
                    super.validate(answerOption);
                    ValidationSupport.requireChoiceElement(answerOption.value, "value", Integer.class, Date.class, Time.class, String.class, Coding.class, Reference.class);
                    ValidationSupport.requireValueOrChildren(answerOption);
                }

                protected Builder from(AnswerOption answerOption) {
                    super.from(answerOption);
                    value = answerOption.value;
                    initialSelected = answerOption.initialSelected;
                    return this;
                }
            }
        }

        /**
         * One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user 
         * input.
         */
        public static class Initial extends BackboneElement {
            @Choice({ Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Uri.class, Attachment.class, Coding.class, Quantity.class, Reference.class })
            @Binding(
                bindingName = "QuestionnaireQuestionOption2",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Allowed values to answer questions.",
                valueSet = "http://hl7.org/fhir/ValueSet/questionnaire-answers"
            )
            @Required
            private final Element value;

            private Initial(Builder builder) {
                super(builder);
                value = builder.value;
            }

            /**
             * The actual value to for an initial answer.
             * 
             * @return
             *     An immutable object of type {@link Boolean}, {@link Decimal}, {@link Integer}, {@link Date}, {@link DateTime}, {@link 
             *     Time}, {@link String}, {@link Uri}, {@link Attachment}, {@link Coding}, {@link Quantity} or {@link Reference} that is 
             *     non-null.
             */
            public Element getValue() {
                return value;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
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
                Initial other = (Initial) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(value, other.value);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
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
                 * Convenience method for setting {@code value} with choice type Boolean.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Actual value for initializing the question
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
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Actual value for initializing the question
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
                 * Convenience method for setting {@code value} with choice type Date.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Actual value for initializing the question
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #value(Element)
                 */
                public Builder value(java.time.LocalDate value) {
                    this.value = (value == null) ? null : Date.of(value);
                    return this;
                }

                /**
                 * Convenience method for setting {@code value} with choice type Time.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Actual value for initializing the question
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #value(Element)
                 */
                public Builder value(java.time.LocalTime value) {
                    this.value = (value == null) ? null : Time.of(value);
                    return this;
                }

                /**
                 * Convenience method for setting {@code value} with choice type String.
                 * 
                 * <p>This element is required.
                 * 
                 * @param value
                 *     Actual value for initializing the question
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
                 * The actual value to for an initial answer.
                 * 
                 * <p>This element is required.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Boolean}</li>
                 * <li>{@link Decimal}</li>
                 * <li>{@link Integer}</li>
                 * <li>{@link Date}</li>
                 * <li>{@link DateTime}</li>
                 * <li>{@link Time}</li>
                 * <li>{@link String}</li>
                 * <li>{@link Uri}</li>
                 * <li>{@link Attachment}</li>
                 * <li>{@link Coding}</li>
                 * <li>{@link Quantity}</li>
                 * <li>{@link Reference}</li>
                 * </ul>
                 * 
                 * @param value
                 *     Actual value for initializing the question
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(Element value) {
                    this.value = value;
                    return this;
                }

                /**
                 * Build the {@link Initial}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>value</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Initial}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Initial per the base specification
                 */
                @Override
                public Initial build() {
                    Initial initial = new Initial(this);
                    if (validating) {
                        validate(initial);
                    }
                    return initial;
                }

                protected void validate(Initial initial) {
                    super.validate(initial);
                    ValidationSupport.requireChoiceElement(initial.value, "value", Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Uri.class, Attachment.class, Coding.class, Quantity.class, Reference.class);
                    ValidationSupport.requireValueOrChildren(initial);
                }

                protected Builder from(Initial initial) {
                    super.from(initial);
                    value = initial.value;
                    return this;
                }
            }
        }
    }
}
