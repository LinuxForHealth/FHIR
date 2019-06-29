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

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.EnableWhenBehavior;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.QuestionnaireItemOperator;
import com.ibm.watsonhealth.fhir.model.type.QuestionnaireItemType;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.UsageContext;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide 
 * detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
 * </p>
 */
@Constraint(
    id = "que-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')"
)
@Constraint(
    id = "que-1",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Group items must have nested items, display items cannot have nested items",
    expression = "(type='group' implies item.empty().not()) and (type.trace('type')='display' implies item.trace('item').empty())"
)
@Constraint(
    id = "que-2",
    level = "Rule",
    location = "(base)",
    description = "The link ids for groups and questions must be unique within the questionnaire",
    expression = "descendants().linkId.isDistinct()"
)
@Constraint(
    id = "que-3",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Display items cannot have a \"code\" asserted",
    expression = "type!='display' or code.empty()"
)
@Constraint(
    id = "que-4",
    level = "Rule",
    location = "Questionnaire.item",
    description = "A question cannot have both answerOption and answerValueSet",
    expression = "answerOption.empty() or answerValueSet.empty()"
)
@Constraint(
    id = "que-5",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Only 'choice' and 'open-choice' items can have answerValueSet",
    expression = "(type ='choice' or type = 'open-choice' or type = 'decimal' or type = 'integer' or type = 'date' or type = 'dateTime' or type = 'time' or type = 'string' or type = 'quantity') or (answerValueSet.empty() and answerOption.empty())"
)
@Constraint(
    id = "que-6",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Required and repeat aren't permitted for display items",
    expression = "type!='display' or (required.empty() and repeats.empty())"
)
@Constraint(
    id = "que-7",
    level = "Rule",
    location = "Questionnaire.item.enableWhen",
    description = "If the operator is 'exists', the value must be a boolean",
    expression = "operator = 'exists' implies (answer is Boolean)"
)
@Constraint(
    id = "que-8",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Initial values can't be specified for groups or display items",
    expression = "(type!='group' and type!='display') or initial.empty()"
)
@Constraint(
    id = "que-9",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Read-only can't be specified for \"display\" items",
    expression = "type!='display' or readOnly.empty()"
)
@Constraint(
    id = "que-10",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Maximum length can only be declared for simple question types",
    expression = "(type in ('boolean' | 'decimal' | 'integer' | 'string' | 'text' | 'url' | 'open-choice')) or maxLength.empty()"
)
@Constraint(
    id = "que-11",
    level = "Rule",
    location = "Questionnaire.item",
    description = "If one or more answerOption is present, initial[x] must be missing",
    expression = "answerOption.empty() or initial.empty()"
)
@Constraint(
    id = "que-12",
    level = "Rule",
    location = "Questionnaire.item",
    description = "If there are more than one enableWhen, enableBehavior must be specified",
    expression = "enableWhen.count() > 2 implies enableBehavior.exists()"
)
@Constraint(
    id = "que-13",
    level = "Rule",
    location = "Questionnaire.item",
    description = "Can only have multiple initial values for repeating items",
    expression = "repeats=true or initial.count() <= 1"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Questionnaire extends DomainResource {
    private final Uri url;
    private final List<Identifier> identifier;
    private final String version;
    private final String name;
    private final String title;
    private final List<Canonical> derivedFrom;
    private final PublicationStatus status;
    private final Boolean experimental;
    private final List<ResourceType> subjectType;
    private final DateTime date;
    private final String publisher;
    private final List<ContactDetail> contact;
    private final Markdown description;
    private final List<UsageContext> useContext;
    private final List<CodeableConcept> jurisdiction;
    private final Markdown purpose;
    private final Markdown copyright;
    private final Date approvalDate;
    private final Date lastReviewDate;
    private final Period effectivePeriod;
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
        status = ValidationSupport.requireNonNull(builder.status, "status");
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
     * <p>
     * An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or 
     * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
     * which at which an authoritative instance of this questionnaire is (or will be) published. This URL can be the target 
     * of a canonical reference. It SHALL remain the same when the questionnaire is stored on different servers.
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
     * A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced 
     * in a specification, model, design or an instance.
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
     * The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, 
     * model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be 
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
     * A natural language name identifying the questionnaire. This name should be usable as an identifier for the module by 
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
     * A short, descriptive, user-friendly title for the questionnaire.
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
     * The URL of a Questionnaire that this Questionnaire is based on.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * <p>
     * The status of this questionnaire. Enables tracking the life-cycle of the content.
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
     * A Boolean value to indicate that this questionnaire is authored for testing purposes (or 
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
     * The types of subjects that can be the subject of responses created for the questionnaire.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ResourceType}.
     */
    public List<ResourceType> getSubjectType() {
        return subjectType;
    }

    /**
     * <p>
     * The date (and optionally time) when the questionnaire was published. The date must change when the business version 
     * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
     * the questionnaire changes.
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
     * The name of the organization or individual that published the questionnaire.
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
     * A free text natural language description of the questionnaire from a consumer's perspective.
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
     * may be used to assist with indexing and searching for appropriate questionnaire instances.
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
     * A legal or geographic region in which the questionnaire is intended to be used.
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
     * Explanation of why this questionnaire is needed and why it has been designed as it has.
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
     * A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal 
     * restrictions on the use and publishing of the questionnaire.
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
     * The period during which the questionnaire content was or is planned to be in active use.
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
     * An identifier for this question or group of questions in a particular terminology such as LOINC.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Coding}.
     */
    public List<Coding> getCode() {
        return code;
    }

    /**
     * <p>
     * A particular question, question grouping or display text that is part of the questionnaire.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Item}.
     */
    public List<Item> getItem() {
        return item;
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
                accept(derivedFrom, "derivedFrom", visitor, Canonical.class);
                accept(status, "status", visitor);
                accept(experimental, "experimental", visitor);
                accept(subjectType, "subjectType", visitor, ResourceType.class);
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
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
        private String title;
        private List<Canonical> derivedFrom = new ArrayList<>();
        private Boolean experimental;
        private List<ResourceType> subjectType = new ArrayList<>();
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
         * An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or 
         * an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at 
         * which at which an authoritative instance of this questionnaire is (or will be) published. This URL can be the target 
         * of a canonical reference. It SHALL remain the same when the questionnaire is stored on different servers.
         * </p>
         * 
         * @param url
         *     Canonical identifier for this questionnaire, represented as a URI (globally unique)
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
         * A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the questionnaire
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
         * A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced 
         * in a specification, model, design or an instance.
         * </p>
         * 
         * @param identifier
         *     Additional identifier for the questionnaire
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
         * The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, 
         * model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be 
         * globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is 
         * also no expectation that versions can be placed in a lexicographical sequence.
         * </p>
         * 
         * @param version
         *     Business version of the questionnaire
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
         * A natural language name identifying the questionnaire. This name should be usable as an identifier for the module by 
         * machine processing applications such as code generation.
         * </p>
         * 
         * @param name
         *     Name for this questionnaire (computer friendly)
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
         * A short, descriptive, user-friendly title for the questionnaire.
         * </p>
         * 
         * @param title
         *     Name for this questionnaire (human friendly)
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
         * The URL of a Questionnaire that this Questionnaire is based on.
         * </p>
         * 
         * @param derivedFrom
         *     Instantiates protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder derivedFrom(Canonical... derivedFrom) {
            for (Canonical value : derivedFrom) {
                this.derivedFrom.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The URL of a Questionnaire that this Questionnaire is based on.
         * </p>
         * 
         * @param derivedFrom
         *     Instantiates protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder derivedFrom(Collection<Canonical> derivedFrom) {
            this.derivedFrom.addAll(derivedFrom);
            return this;
        }

        /**
         * <p>
         * A Boolean value to indicate that this questionnaire is authored for testing purposes (or 
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
         * The types of subjects that can be the subject of responses created for the questionnaire.
         * </p>
         * 
         * @param subjectType
         *     Resource that can be subject of QuestionnaireResponse
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subjectType(ResourceType... subjectType) {
            for (ResourceType value : subjectType) {
                this.subjectType.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The types of subjects that can be the subject of responses created for the questionnaire.
         * </p>
         * 
         * @param subjectType
         *     Resource that can be subject of QuestionnaireResponse
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subjectType(Collection<ResourceType> subjectType) {
            this.subjectType.addAll(subjectType);
            return this;
        }

        /**
         * <p>
         * The date (and optionally time) when the questionnaire was published. The date must change when the business version 
         * changes and it must change if the status code changes. In addition, it should change when the substantive content of 
         * the questionnaire changes.
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
         * The name of the organization or individual that published the questionnaire.
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
         * A free text natural language description of the questionnaire from a consumer's perspective.
         * </p>
         * 
         * @param description
         *     Natural language description of the questionnaire
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
         * may be used to assist with indexing and searching for appropriate questionnaire instances.
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
         * may be used to assist with indexing and searching for appropriate questionnaire instances.
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
         * A legal or geographic region in which the questionnaire is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for questionnaire (if applicable)
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
         * A legal or geographic region in which the questionnaire is intended to be used.
         * </p>
         * 
         * @param jurisdiction
         *     Intended jurisdiction for questionnaire (if applicable)
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
         * Explanation of why this questionnaire is needed and why it has been designed as it has.
         * </p>
         * 
         * @param purpose
         *     Why this questionnaire is defined
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
         * A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal 
         * restrictions on the use and publishing of the questionnaire.
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
         *     When the questionnaire was approved by publisher
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
         *     When the questionnaire was last reviewed
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
         * The period during which the questionnaire content was or is planned to be in active use.
         * </p>
         * 
         * @param effectivePeriod
         *     When the questionnaire is expected to be used
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
         * An identifier for this question or group of questions in a particular terminology such as LOINC.
         * </p>
         * 
         * @param code
         *     Concept that represents the overall questionnaire
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder code(Coding... code) {
            for (Coding value : code) {
                this.code.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An identifier for this question or group of questions in a particular terminology such as LOINC.
         * </p>
         * 
         * @param code
         *     Concept that represents the overall questionnaire
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder code(Collection<Coding> code) {
            this.code.addAll(code);
            return this;
        }

        /**
         * <p>
         * A particular question, question grouping or display text that is part of the questionnaire.
         * </p>
         * 
         * @param item
         *     Questions and sections within the Questionnaire
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder item(Item... item) {
            for (Item value : item) {
                this.item.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A particular question, question grouping or display text that is part of the questionnaire.
         * </p>
         * 
         * @param item
         *     Questions and sections within the Questionnaire
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder item(Collection<Item> item) {
            this.item.addAll(item);
            return this;
        }

        @Override
        public Questionnaire build() {
            return new Questionnaire(this);
        }

        private Builder from(Questionnaire questionnaire) {
            id = questionnaire.id;
            meta = questionnaire.meta;
            implicitRules = questionnaire.implicitRules;
            language = questionnaire.language;
            text = questionnaire.text;
            contained.addAll(questionnaire.contained);
            extension.addAll(questionnaire.extension);
            modifierExtension.addAll(questionnaire.modifierExtension);
            url = questionnaire.url;
            identifier.addAll(questionnaire.identifier);
            version = questionnaire.version;
            name = questionnaire.name;
            title = questionnaire.title;
            derivedFrom.addAll(questionnaire.derivedFrom);
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
     * <p>
     * A particular question, question grouping or display text that is part of the questionnaire.
     * </p>
     */
    public static class Item extends BackboneElement {
        private final String linkId;
        private final Uri definition;
        private final List<Coding> code;
        private final String prefix;
        private final String text;
        private final QuestionnaireItemType type;
        private final List<EnableWhen> enableWhen;
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
            linkId = ValidationSupport.requireNonNull(builder.linkId, "linkId");
            definition = builder.definition;
            code = Collections.unmodifiableList(builder.code);
            prefix = builder.prefix;
            text = builder.text;
            type = ValidationSupport.requireNonNull(builder.type, "type");
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
         * <p>
         * An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a 
         * QuestionnaireResponse resource.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getLinkId() {
            return linkId;
        }

        /**
         * <p>
         * This element is a URI that refers to an [ElementDefinition](elementdefinition.html) that provides information about 
         * this item, including information that might otherwise be included in the instance of the Questionnaire resource. A 
         * detailed description of the construction of the URI is shown in Comments, below. If this element is present then the 
         * following element values MAY be derived from the Element Definition if the corresponding elements of this 
         * Questionnaire resource instance have no value:
         * </p>
         * <p>
         * * code (ElementDefinition.code) 
         * * type (ElementDefinition.type) 
         * * required (ElementDefinition.min) 
         * * repeats (ElementDefinition.max) 
         * * maxLength (ElementDefinition.maxLength) 
         * * answerValueSet (ElementDefinition.binding)
         * * options (ElementDefinition.binding).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getDefinition() {
            return definition;
        }

        /**
         * <p>
         * A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions 
         * and answers).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Coding}.
         */
        public List<Coding> getCode() {
            return code;
        }

        /**
         * <p>
         * A short label for a particular group, question or set of display text within the questionnaire used for reference by 
         * the individual completing the questionnaire.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * <p>
         * The name of a section, the text of a question or text content for a display item.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getText() {
            return text;
        }

        /**
         * <p>
         * The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of 
         * data to be captured (string, integer, coded choice, etc.).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link QuestionnaireItemType}.
         */
        public QuestionnaireItemType getType() {
            return type;
        }

        /**
         * <p>
         * A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the 
         * specified condition is true.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link EnableWhen}.
         */
        public List<EnableWhen> getEnableWhen() {
            return enableWhen;
        }

        /**
         * <p>
         * Controls how multiple enableWhen values are interpreted - whether all or any must be true.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link EnableWhenBehavior}.
         */
        public EnableWhenBehavior getEnableBehavior() {
            return enableBehavior;
        }

        /**
         * <p>
         * An indication, if true, that the item must be present in a "completed" QuestionnaireResponse. If false, the item may 
         * be skipped when answering the questionnaire.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getRequired() {
            return required;
        }

        /**
         * <p>
         * An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for 
         * questions or multiple sets of answers for groups.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getRepeats() {
            return repeats;
        }

        /**
         * <p>
         * An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getReadOnly() {
            return readOnly;
        }

        /**
         * <p>
         * The maximum number of characters that are permitted in the answer to be considered a "valid" QuestionnaireResponse.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getMaxLength() {
            return maxLength;
        }

        /**
         * <p>
         * A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" 
         * question.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Canonical}.
         */
        public Canonical getAnswerValueSet() {
            return answerValueSet;
        }

        /**
         * <p>
         * One of the permitted answers for a "choice" or "open-choice" question.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link AnswerOption}.
         */
        public List<AnswerOption> getAnswerOption() {
            return answerOption;
        }

        /**
         * <p>
         * One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user 
         * input.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Initial}.
         */
        public List<Initial> getInitial() {
            return initial;
        }

        /**
         * <p>
         * Text, questions and other groups to be nested beneath a question or group.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Item}.
         */
        public List<Questionnaire.Item> getItem() {
            return item;
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
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(linkId, type).from(this);
        }

        public Builder toBuilder(String linkId, QuestionnaireItemType type) {
            return new Builder(linkId, type).from(this);
        }

        public static Builder builder(String linkId, QuestionnaireItemType type) {
            return new Builder(linkId, type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String linkId;
            private final QuestionnaireItemType type;

            // optional
            private Uri definition;
            private List<Coding> code = new ArrayList<>();
            private String prefix;
            private String text;
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

            private Builder(String linkId, QuestionnaireItemType type) {
                super();
                this.linkId = linkId;
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
             * This element is a URI that refers to an [ElementDefinition](elementdefinition.html) that provides information about 
             * this item, including information that might otherwise be included in the instance of the Questionnaire resource. A 
             * detailed description of the construction of the URI is shown in Comments, below. If this element is present then the 
             * following element values MAY be derived from the Element Definition if the corresponding elements of this 
             * Questionnaire resource instance have no value:
             * </p>
             * <p>
             * * code (ElementDefinition.code) 
             * * type (ElementDefinition.type) 
             * * required (ElementDefinition.min) 
             * * repeats (ElementDefinition.max) 
             * * maxLength (ElementDefinition.maxLength) 
             * * answerValueSet (ElementDefinition.binding)
             * * options (ElementDefinition.binding).
             * </p>
             * 
             * @param definition
             *     ElementDefinition - details for the item
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder definition(Uri definition) {
                this.definition = definition;
                return this;
            }

            /**
             * <p>
             * A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions 
             * and answers).
             * </p>
             * 
             * @param code
             *     Corresponding concept for this item in a terminology
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder code(Coding... code) {
                for (Coding value : code) {
                    this.code.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions 
             * and answers).
             * </p>
             * 
             * @param code
             *     Corresponding concept for this item in a terminology
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder code(Collection<Coding> code) {
                this.code.addAll(code);
                return this;
            }

            /**
             * <p>
             * A short label for a particular group, question or set of display text within the questionnaire used for reference by 
             * the individual completing the questionnaire.
             * </p>
             * 
             * @param prefix
             *     E.g. "1(a)", "2.5.3"
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder prefix(String prefix) {
                this.prefix = prefix;
                return this;
            }

            /**
             * <p>
             * The name of a section, the text of a question or text content for a display item.
             * </p>
             * 
             * @param text
             *     Primary text for the item
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            /**
             * <p>
             * A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the 
             * specified condition is true.
             * </p>
             * 
             * @param enableWhen
             *     Only allow data when
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder enableWhen(EnableWhen... enableWhen) {
                for (EnableWhen value : enableWhen) {
                    this.enableWhen.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the 
             * specified condition is true.
             * </p>
             * 
             * @param enableWhen
             *     Only allow data when
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder enableWhen(Collection<EnableWhen> enableWhen) {
                this.enableWhen.addAll(enableWhen);
                return this;
            }

            /**
             * <p>
             * Controls how multiple enableWhen values are interpreted - whether all or any must be true.
             * </p>
             * 
             * @param enableBehavior
             *     all | any
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder enableBehavior(EnableWhenBehavior enableBehavior) {
                this.enableBehavior = enableBehavior;
                return this;
            }

            /**
             * <p>
             * An indication, if true, that the item must be present in a "completed" QuestionnaireResponse. If false, the item may 
             * be skipped when answering the questionnaire.
             * </p>
             * 
             * @param required
             *     Whether the item must be included in data results
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder required(Boolean required) {
                this.required = required;
                return this;
            }

            /**
             * <p>
             * An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for 
             * questions or multiple sets of answers for groups.
             * </p>
             * 
             * @param repeats
             *     Whether the item may repeat
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder repeats(Boolean repeats) {
                this.repeats = repeats;
                return this;
            }

            /**
             * <p>
             * An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.
             * </p>
             * 
             * @param readOnly
             *     Don't allow human editing
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder readOnly(Boolean readOnly) {
                this.readOnly = readOnly;
                return this;
            }

            /**
             * <p>
             * The maximum number of characters that are permitted in the answer to be considered a "valid" QuestionnaireResponse.
             * </p>
             * 
             * @param maxLength
             *     No more than this many characters
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder maxLength(Integer maxLength) {
                this.maxLength = maxLength;
                return this;
            }

            /**
             * <p>
             * A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" 
             * question.
             * </p>
             * 
             * @param answerValueSet
             *     Valueset containing permitted answers
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder answerValueSet(Canonical answerValueSet) {
                this.answerValueSet = answerValueSet;
                return this;
            }

            /**
             * <p>
             * One of the permitted answers for a "choice" or "open-choice" question.
             * </p>
             * 
             * @param answerOption
             *     Permitted answer
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder answerOption(AnswerOption... answerOption) {
                for (AnswerOption value : answerOption) {
                    this.answerOption.add(value);
                }
                return this;
            }

            /**
             * <p>
             * One of the permitted answers for a "choice" or "open-choice" question.
             * </p>
             * 
             * @param answerOption
             *     Permitted answer
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder answerOption(Collection<AnswerOption> answerOption) {
                this.answerOption.addAll(answerOption);
                return this;
            }

            /**
             * <p>
             * One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user 
             * input.
             * </p>
             * 
             * @param initial
             *     Initial value(s) when item is first rendered
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder initial(Initial... initial) {
                for (Initial value : initial) {
                    this.initial.add(value);
                }
                return this;
            }

            /**
             * <p>
             * One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user 
             * input.
             * </p>
             * 
             * @param initial
             *     Initial value(s) when item is first rendered
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder initial(Collection<Initial> initial) {
                this.initial.addAll(initial);
                return this;
            }

            /**
             * <p>
             * Text, questions and other groups to be nested beneath a question or group.
             * </p>
             * 
             * @param item
             *     Nested questionnaire items
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder item(Questionnaire.Item... item) {
                for (Questionnaire.Item value : item) {
                    this.item.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Text, questions and other groups to be nested beneath a question or group.
             * </p>
             * 
             * @param item
             *     Nested questionnaire items
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder item(Collection<Questionnaire.Item> item) {
                this.item.addAll(item);
                return this;
            }

            @Override
            public Item build() {
                return new Item(this);
            }

            private Builder from(Item item) {
                id = item.id;
                extension.addAll(item.extension);
                modifierExtension.addAll(item.modifierExtension);
                definition = item.definition;
                code.addAll(item.code);
                prefix = item.prefix;
                text = item.text;
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
         * <p>
         * A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the 
         * specified condition is true.
         * </p>
         */
        public static class EnableWhen extends BackboneElement {
            private final String question;
            private final QuestionnaireItemOperator operator;
            private final Element answer;

            private EnableWhen(Builder builder) {
                super(builder);
                question = ValidationSupport.requireNonNull(builder.question, "question");
                operator = ValidationSupport.requireNonNull(builder.operator, "operator");
                answer = ValidationSupport.requireChoiceElement(builder.answer, "answer", Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Coding.class, Quantity.class, Reference.class);
            }

            /**
             * <p>
             * The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getQuestion() {
                return question;
            }

            /**
             * <p>
             * Specifies the criteria by which the question is enabled.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link QuestionnaireItemOperator}.
             */
            public QuestionnaireItemOperator getOperator() {
                return operator;
            }

            /**
             * <p>
             * A value that the referenced question is tested using the specified operator in order for the item to be enabled.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getAnswer() {
                return answer;
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
                        accept(question, "question", visitor);
                        accept(operator, "operator", visitor);
                        accept(answer, "answer", visitor, true);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return new Builder(question, operator, answer).from(this);
            }

            public Builder toBuilder(String question, QuestionnaireItemOperator operator, Element answer) {
                return new Builder(question, operator, answer).from(this);
            }

            public static Builder builder(String question, QuestionnaireItemOperator operator, Element answer) {
                return new Builder(question, operator, answer);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final String question;
                private final QuestionnaireItemOperator operator;
                private final Element answer;

                private Builder(String question, QuestionnaireItemOperator operator, Element answer) {
                    super();
                    this.question = question;
                    this.operator = operator;
                    this.answer = answer;
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
                public EnableWhen build() {
                    return new EnableWhen(this);
                }

                private Builder from(EnableWhen enableWhen) {
                    id = enableWhen.id;
                    extension.addAll(enableWhen.extension);
                    modifierExtension.addAll(enableWhen.modifierExtension);
                    return this;
                }
            }
        }

        /**
         * <p>
         * One of the permitted answers for a "choice" or "open-choice" question.
         * </p>
         */
        public static class AnswerOption extends BackboneElement {
            private final Element value;
            private final Boolean initialSelected;

            private AnswerOption(Builder builder) {
                super(builder);
                value = ValidationSupport.requireChoiceElement(builder.value, "value", Integer.class, Date.class, Time.class, String.class, Coding.class, Reference.class);
                initialSelected = builder.initialSelected;
            }

            /**
             * <p>
             * A potential answer that's allowed as the answer to this question.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getValue() {
                return value;
            }

            /**
             * <p>
             * Indicates whether the answer value is selected when the list of possible answers is initially shown.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Boolean}.
             */
            public Boolean getInitialSelected() {
                return initialSelected;
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
                        accept(value, "value", visitor, true);
                        accept(initialSelected, "initialSelected", visitor);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return new Builder(value).from(this);
            }

            public Builder toBuilder(Element value) {
                return new Builder(value).from(this);
            }

            public static Builder builder(Element value) {
                return new Builder(value);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Element value;

                // optional
                private Boolean initialSelected;

                private Builder(Element value) {
                    super();
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

                /**
                 * <p>
                 * Indicates whether the answer value is selected when the list of possible answers is initially shown.
                 * </p>
                 * 
                 * @param initialSelected
                 *     Whether option is selected by default
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder initialSelected(Boolean initialSelected) {
                    this.initialSelected = initialSelected;
                    return this;
                }

                @Override
                public AnswerOption build() {
                    return new AnswerOption(this);
                }

                private Builder from(AnswerOption answerOption) {
                    id = answerOption.id;
                    extension.addAll(answerOption.extension);
                    modifierExtension.addAll(answerOption.modifierExtension);
                    initialSelected = answerOption.initialSelected;
                    return this;
                }
            }
        }

        /**
         * <p>
         * One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user 
         * input.
         * </p>
         */
        public static class Initial extends BackboneElement {
            private final Element value;

            private Initial(Builder builder) {
                super(builder);
                value = ValidationSupport.requireChoiceElement(builder.value, "value", Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Uri.class, Attachment.class, Coding.class, Quantity.class, Reference.class);
            }

            /**
             * <p>
             * The actual value to for an initial answer.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getValue() {
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
                        accept(value, "value", visitor, true);
                    }
                    visitor.visitEnd(elementName, this);
                    visitor.postVisit(this);
                }
            }

            @Override
            public Builder toBuilder() {
                return new Builder(value).from(this);
            }

            public Builder toBuilder(Element value) {
                return new Builder(value).from(this);
            }

            public static Builder builder(Element value) {
                return new Builder(value);
            }

            public static class Builder extends BackboneElement.Builder {
                // required
                private final Element value;

                private Builder(Element value) {
                    super();
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
                public Initial build() {
                    return new Initial(this);
                }

                private Builder from(Initial initial) {
                    id = initial.id;
                    extension.addAll(initial.extension);
                    modifierExtension.addAll(initial.modifierExtension);
                    return this;
                }
            }
        }
    }
}
