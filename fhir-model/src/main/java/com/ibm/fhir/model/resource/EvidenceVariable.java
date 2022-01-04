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
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Expression;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.CharacteristicCombination;
import com.ibm.fhir.model.type.code.EvidenceVariableHandling;
import com.ibm.fhir.model.type.code.GroupMeasure;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The EvidenceVariable resource describes an element that knowledge (Evidence) is about.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "cnl-0",
    level = "Warning",
    location = "(base)",
    description = "Name should be usable as an identifier for the module by machine processing applications such as code generation",
    expression = "name.matches('[A-Z]([A-Za-z0-9_]){0,254}')",
    source = "http://hl7.org/fhir/StructureDefinition/EvidenceVariable"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class EvidenceVariable extends DomainResource {
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
    @Summary
    private final String shortTitle;
    private final String subtitle;
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
    private final DateTime date;
    @Summary
    private final Markdown description;
    private final List<Annotation> note;
    @Summary
    private final List<UsageContext> useContext;
    @Summary
    private final String publisher;
    @Summary
    private final List<ContactDetail> contact;
    private final List<ContactDetail> author;
    private final List<ContactDetail> editor;
    private final List<ContactDetail> reviewer;
    private final List<ContactDetail> endorser;
    private final List<RelatedArtifact> relatedArtifact;
    private final Boolean actual;
    @Binding(
        bindingName = "CharacteristicCombination",
        strength = BindingStrength.Value.REQUIRED,
        description = "Logical grouping of characteristics.",
        valueSet = "http://hl7.org/fhir/ValueSet/characteristic-combination|4.3.0-CIBUILD"
    )
    private final CharacteristicCombination characteristicCombination;
    @Summary
    private final List<Characteristic> characteristic;
    @Summary
    @Binding(
        bindingName = "EvidenceVariableHandling",
        strength = BindingStrength.Value.REQUIRED,
        description = "The handling of the variable in statistical analysis for exposures or outcomes (E.g. Dichotomous, Continuous, Descriptive).",
        valueSet = "http://hl7.org/fhir/ValueSet/variable-handling|4.3.0-CIBUILD"
    )
    private final EvidenceVariableHandling handling;
    private final List<Category> category;

    private EvidenceVariable(Builder builder) {
        super(builder);
        url = builder.url;
        identifier = Collections.unmodifiableList(builder.identifier);
        version = builder.version;
        name = builder.name;
        title = builder.title;
        shortTitle = builder.shortTitle;
        subtitle = builder.subtitle;
        status = builder.status;
        date = builder.date;
        description = builder.description;
        note = Collections.unmodifiableList(builder.note);
        useContext = Collections.unmodifiableList(builder.useContext);
        publisher = builder.publisher;
        contact = Collections.unmodifiableList(builder.contact);
        author = Collections.unmodifiableList(builder.author);
        editor = Collections.unmodifiableList(builder.editor);
        reviewer = Collections.unmodifiableList(builder.reviewer);
        endorser = Collections.unmodifiableList(builder.endorser);
        relatedArtifact = Collections.unmodifiableList(builder.relatedArtifact);
        actual = builder.actual;
        characteristicCombination = builder.characteristicCombination;
        characteristic = Collections.unmodifiableList(builder.characteristic);
        handling = builder.handling;
        category = Collections.unmodifiableList(builder.category);
    }

    /**
     * An absolute URI that is used to identify this evidence variable when it is referenced in a specification, model, 
     * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
     * address at which at which an authoritative instance of this evidence variable is (or will be) published. This URL can 
     * be the target of a canonical reference. It SHALL remain the same when the evidence variable is stored on different 
     * servers.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * A formal identifier that is used to identify this evidence variable when it is represented in other formats, or 
     * referenced in a specification, model, design or an instance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The identifier that is used to identify this version of the evidence variable when it is referenced in a 
     * specification, model, design or instance. This is an arbitrary value managed by the evidence variable author and is 
     * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
     * available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
     * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
     * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
     * version is required for non-experimental active artifacts.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A natural language name identifying the evidence variable. This name should be usable as an identifier for the module 
     * by machine processing applications such as code generation.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A short, descriptive, user-friendly title for the evidence variable.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is 
     * not necessary.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * An explanatory or alternate title for the EvidenceVariable giving additional information about its content.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * The status of this evidence variable. Enables tracking the life-cycle of the content.
     * 
     * @return
     *     An immutable object of type {@link PublicationStatus} that is non-null.
     */
    public PublicationStatus getStatus() {
        return status;
    }

    /**
     * The date (and optionally time) when the evidence variable was published. The date must change when the business 
     * version changes and it must change if the status code changes. In addition, it should change when the substantive 
     * content of the evidence variable changes.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * A free text natural language description of the evidence variable from a consumer's perspective.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that may be null.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * A human-readable string to clarify or explain concepts about the resource.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
     * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
     * may be used to assist with indexing and searching for appropriate evidence variable instances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link UsageContext} that may be empty.
     */
    public List<UsageContext> getUseContext() {
        return useContext;
    }

    /**
     * The name of the organization or individual that published the evidence variable.
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
     * An individiual or organization primarily involved in the creation and maintenance of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getAuthor() {
        return author;
    }

    /**
     * An individual or organization primarily responsible for internal coherence of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getEditor() {
        return editor;
    }

    /**
     * An individual or organization primarily responsible for review of some aspect of the content.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getReviewer() {
        return reviewer;
    }

    /**
     * An individual or organization responsible for officially endorsing the content for use in some setting.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactDetail} that may be empty.
     */
    public List<ContactDetail> getEndorser() {
        return endorser;
    }

    /**
     * Related artifacts such as additional documentation, justification, or bibliographic references.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatedArtifact} that may be empty.
     */
    public List<RelatedArtifact> getRelatedArtifact() {
        return relatedArtifact;
    }

    /**
     * True if the actual variable measured, false if a conceptual representation of the intended variable.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getActual() {
        return actual;
    }

    /**
     * Used to specify if two or more characteristics are combined with OR or AND.
     * 
     * @return
     *     An immutable object of type {@link CharacteristicCombination} that may be null.
     */
    public CharacteristicCombination getCharacteristicCombination() {
        return characteristicCombination;
    }

    /**
     * A characteristic that defines the members of the evidence element. Multiple characteristics are applied with "and" 
     * semantics.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Characteristic} that may be empty.
     */
    public List<Characteristic> getCharacteristic() {
        return characteristic;
    }

    /**
     * Used for an outcome to classify.
     * 
     * @return
     *     An immutable object of type {@link EvidenceVariableHandling} that may be null.
     */
    public EvidenceVariableHandling getHandling() {
        return handling;
    }

    /**
     * A grouping (or set of values) described along with other groupings to specify the set of groupings allowed for the 
     * variable.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Category} that may be empty.
     */
    public List<Category> getCategory() {
        return category;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (url != null) || 
            !identifier.isEmpty() || 
            (version != null) || 
            (name != null) || 
            (title != null) || 
            (shortTitle != null) || 
            (subtitle != null) || 
            (status != null) || 
            (date != null) || 
            (description != null) || 
            !note.isEmpty() || 
            !useContext.isEmpty() || 
            (publisher != null) || 
            !contact.isEmpty() || 
            !author.isEmpty() || 
            !editor.isEmpty() || 
            !reviewer.isEmpty() || 
            !endorser.isEmpty() || 
            !relatedArtifact.isEmpty() || 
            (actual != null) || 
            (characteristicCombination != null) || 
            !characteristic.isEmpty() || 
            (handling != null) || 
            !category.isEmpty();
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
                accept(shortTitle, "shortTitle", visitor);
                accept(subtitle, "subtitle", visitor);
                accept(status, "status", visitor);
                accept(date, "date", visitor);
                accept(description, "description", visitor);
                accept(note, "note", visitor, Annotation.class);
                accept(useContext, "useContext", visitor, UsageContext.class);
                accept(publisher, "publisher", visitor);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(author, "author", visitor, ContactDetail.class);
                accept(editor, "editor", visitor, ContactDetail.class);
                accept(reviewer, "reviewer", visitor, ContactDetail.class);
                accept(endorser, "endorser", visitor, ContactDetail.class);
                accept(relatedArtifact, "relatedArtifact", visitor, RelatedArtifact.class);
                accept(actual, "actual", visitor);
                accept(characteristicCombination, "characteristicCombination", visitor);
                accept(characteristic, "characteristic", visitor, Characteristic.class);
                accept(handling, "handling", visitor);
                accept(category, "category", visitor, Category.class);
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
        EvidenceVariable other = (EvidenceVariable) obj;
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
            Objects.equals(shortTitle, other.shortTitle) && 
            Objects.equals(subtitle, other.subtitle) && 
            Objects.equals(status, other.status) && 
            Objects.equals(date, other.date) && 
            Objects.equals(description, other.description) && 
            Objects.equals(note, other.note) && 
            Objects.equals(useContext, other.useContext) && 
            Objects.equals(publisher, other.publisher) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(author, other.author) && 
            Objects.equals(editor, other.editor) && 
            Objects.equals(reviewer, other.reviewer) && 
            Objects.equals(endorser, other.endorser) && 
            Objects.equals(relatedArtifact, other.relatedArtifact) && 
            Objects.equals(actual, other.actual) && 
            Objects.equals(characteristicCombination, other.characteristicCombination) && 
            Objects.equals(characteristic, other.characteristic) && 
            Objects.equals(handling, other.handling) && 
            Objects.equals(category, other.category);
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
                shortTitle, 
                subtitle, 
                status, 
                date, 
                description, 
                note, 
                useContext, 
                publisher, 
                contact, 
                author, 
                editor, 
                reviewer, 
                endorser, 
                relatedArtifact, 
                actual, 
                characteristicCombination, 
                characteristic, 
                handling, 
                category);
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
        private String shortTitle;
        private String subtitle;
        private PublicationStatus status;
        private DateTime date;
        private Markdown description;
        private List<Annotation> note = new ArrayList<>();
        private List<UsageContext> useContext = new ArrayList<>();
        private String publisher;
        private List<ContactDetail> contact = new ArrayList<>();
        private List<ContactDetail> author = new ArrayList<>();
        private List<ContactDetail> editor = new ArrayList<>();
        private List<ContactDetail> reviewer = new ArrayList<>();
        private List<ContactDetail> endorser = new ArrayList<>();
        private List<RelatedArtifact> relatedArtifact = new ArrayList<>();
        private Boolean actual;
        private CharacteristicCombination characteristicCombination;
        private List<Characteristic> characteristic = new ArrayList<>();
        private EvidenceVariableHandling handling;
        private List<Category> category = new ArrayList<>();

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
         * An absolute URI that is used to identify this evidence variable when it is referenced in a specification, model, 
         * design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal 
         * address at which at which an authoritative instance of this evidence variable is (or will be) published. This URL can 
         * be the target of a canonical reference. It SHALL remain the same when the evidence variable is stored on different 
         * servers.
         * 
         * @param url
         *     Canonical identifier for this evidence variable, represented as a URI (globally unique)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder url(Uri url) {
            this.url = url;
            return this;
        }

        /**
         * A formal identifier that is used to identify this evidence variable when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the evidence variable
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
         * A formal identifier that is used to identify this evidence variable when it is represented in other formats, or 
         * referenced in a specification, model, design or an instance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Additional identifier for the evidence variable
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
         *     Business version of the evidence variable
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
         * The identifier that is used to identify this version of the evidence variable when it is referenced in a 
         * specification, model, design or instance. This is an arbitrary value managed by the evidence variable author and is 
         * not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not 
         * available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a 
         * version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). 
         * For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a 
         * version is required for non-experimental active artifacts.
         * 
         * @param version
         *     Business version of the evidence variable
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
         *     Name for this evidence variable (computer friendly)
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
         * A natural language name identifying the evidence variable. This name should be usable as an identifier for the module 
         * by machine processing applications such as code generation.
         * 
         * @param name
         *     Name for this evidence variable (computer friendly)
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
         *     Name for this evidence variable (human friendly)
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
         * A short, descriptive, user-friendly title for the evidence variable.
         * 
         * @param title
         *     Name for this evidence variable (human friendly)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Convenience method for setting {@code shortTitle}.
         * 
         * @param shortTitle
         *     Title for use in informal contexts
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #shortTitle(com.ibm.fhir.model.type.String)
         */
        public Builder shortTitle(java.lang.String shortTitle) {
            this.shortTitle = (shortTitle == null) ? null : String.of(shortTitle);
            return this;
        }

        /**
         * The short title provides an alternate title for use in informal descriptive contexts where the full, formal title is 
         * not necessary.
         * 
         * @param shortTitle
         *     Title for use in informal contexts
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder shortTitle(String shortTitle) {
            this.shortTitle = shortTitle;
            return this;
        }

        /**
         * Convenience method for setting {@code subtitle}.
         * 
         * @param subtitle
         *     Subordinate title of the EvidenceVariable
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #subtitle(com.ibm.fhir.model.type.String)
         */
        public Builder subtitle(java.lang.String subtitle) {
            this.subtitle = (subtitle == null) ? null : String.of(subtitle);
            return this;
        }

        /**
         * An explanatory or alternate title for the EvidenceVariable giving additional information about its content.
         * 
         * @param subtitle
         *     Subordinate title of the EvidenceVariable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        /**
         * The status of this evidence variable. Enables tracking the life-cycle of the content.
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
         * The date (and optionally time) when the evidence variable was published. The date must change when the business 
         * version changes and it must change if the status code changes. In addition, it should change when the substantive 
         * content of the evidence variable changes.
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
         * A free text natural language description of the evidence variable from a consumer's perspective.
         * 
         * @param description
         *     Natural language description of the evidence variable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * A human-readable string to clarify or explain concepts about the resource.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Used for footnotes or explanatory notes
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * A human-readable string to clarify or explain concepts about the resource.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Used for footnotes or explanatory notes
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be 
         * general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and 
         * may be used to assist with indexing and searching for appropriate evidence variable instances.
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
         * may be used to assist with indexing and searching for appropriate evidence variable instances.
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
         * The name of the organization or individual that published the evidence variable.
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
         * An individiual or organization primarily involved in the creation and maintenance of the content.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param author
         *     Who authored the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(ContactDetail... author) {
            for (ContactDetail value : author) {
                this.author.add(value);
            }
            return this;
        }

        /**
         * An individiual or organization primarily involved in the creation and maintenance of the content.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param author
         *     Who authored the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder author(Collection<ContactDetail> author) {
            this.author = new ArrayList<>(author);
            return this;
        }

        /**
         * An individual or organization primarily responsible for internal coherence of the content.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param editor
         *     Who edited the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder editor(ContactDetail... editor) {
            for (ContactDetail value : editor) {
                this.editor.add(value);
            }
            return this;
        }

        /**
         * An individual or organization primarily responsible for internal coherence of the content.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param editor
         *     Who edited the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder editor(Collection<ContactDetail> editor) {
            this.editor = new ArrayList<>(editor);
            return this;
        }

        /**
         * An individual or organization primarily responsible for review of some aspect of the content.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reviewer
         *     Who reviewed the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reviewer(ContactDetail... reviewer) {
            for (ContactDetail value : reviewer) {
                this.reviewer.add(value);
            }
            return this;
        }

        /**
         * An individual or organization primarily responsible for review of some aspect of the content.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reviewer
         *     Who reviewed the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reviewer(Collection<ContactDetail> reviewer) {
            this.reviewer = new ArrayList<>(reviewer);
            return this;
        }

        /**
         * An individual or organization responsible for officially endorsing the content for use in some setting.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param endorser
         *     Who endorsed the content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder endorser(ContactDetail... endorser) {
            for (ContactDetail value : endorser) {
                this.endorser.add(value);
            }
            return this;
        }

        /**
         * An individual or organization responsible for officially endorsing the content for use in some setting.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param endorser
         *     Who endorsed the content
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder endorser(Collection<ContactDetail> endorser) {
            this.endorser = new ArrayList<>(endorser);
            return this;
        }

        /**
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedArtifact
         *     Additional documentation, citations, etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relatedArtifact(RelatedArtifact... relatedArtifact) {
            for (RelatedArtifact value : relatedArtifact) {
                this.relatedArtifact.add(value);
            }
            return this;
        }

        /**
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatedArtifact
         *     Additional documentation, citations, etc.
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder relatedArtifact(Collection<RelatedArtifact> relatedArtifact) {
            this.relatedArtifact = new ArrayList<>(relatedArtifact);
            return this;
        }

        /**
         * Convenience method for setting {@code actual}.
         * 
         * @param actual
         *     Actual or conceptual
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #actual(com.ibm.fhir.model.type.Boolean)
         */
        public Builder actual(java.lang.Boolean actual) {
            this.actual = (actual == null) ? null : Boolean.of(actual);
            return this;
        }

        /**
         * True if the actual variable measured, false if a conceptual representation of the intended variable.
         * 
         * @param actual
         *     Actual or conceptual
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder actual(Boolean actual) {
            this.actual = actual;
            return this;
        }

        /**
         * Used to specify if two or more characteristics are combined with OR or AND.
         * 
         * @param characteristicCombination
         *     intersection | union
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder characteristicCombination(CharacteristicCombination characteristicCombination) {
            this.characteristicCombination = characteristicCombination;
            return this;
        }

        /**
         * A characteristic that defines the members of the evidence element. Multiple characteristics are applied with "and" 
         * semantics.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param characteristic
         *     What defines the members of the evidence element
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder characteristic(Characteristic... characteristic) {
            for (Characteristic value : characteristic) {
                this.characteristic.add(value);
            }
            return this;
        }

        /**
         * A characteristic that defines the members of the evidence element. Multiple characteristics are applied with "and" 
         * semantics.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param characteristic
         *     What defines the members of the evidence element
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder characteristic(Collection<Characteristic> characteristic) {
            this.characteristic = new ArrayList<>(characteristic);
            return this;
        }

        /**
         * Used for an outcome to classify.
         * 
         * @param handling
         *     continuous | dichotomous | ordinal | polychotomous
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder handling(EvidenceVariableHandling handling) {
            this.handling = handling;
            return this;
        }

        /**
         * A grouping (or set of values) described along with other groupings to specify the set of groupings allowed for the 
         * variable.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     A grouping for ordinal or polychotomous variables
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(Category... category) {
            for (Category value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * A grouping (or set of values) described along with other groupings to specify the set of groupings allowed for the 
         * variable.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     A grouping for ordinal or polychotomous variables
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<Category> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * Build the {@link EvidenceVariable}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link EvidenceVariable}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid EvidenceVariable per the base specification
         */
        @Override
        public EvidenceVariable build() {
            EvidenceVariable evidenceVariable = new EvidenceVariable(this);
            if (validating) {
                validate(evidenceVariable);
            }
            return evidenceVariable;
        }

        protected void validate(EvidenceVariable evidenceVariable) {
            super.validate(evidenceVariable);
            ValidationSupport.checkList(evidenceVariable.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(evidenceVariable.status, "status");
            ValidationSupport.checkList(evidenceVariable.note, "note", Annotation.class);
            ValidationSupport.checkList(evidenceVariable.useContext, "useContext", UsageContext.class);
            ValidationSupport.checkList(evidenceVariable.contact, "contact", ContactDetail.class);
            ValidationSupport.checkList(evidenceVariable.author, "author", ContactDetail.class);
            ValidationSupport.checkList(evidenceVariable.editor, "editor", ContactDetail.class);
            ValidationSupport.checkList(evidenceVariable.reviewer, "reviewer", ContactDetail.class);
            ValidationSupport.checkList(evidenceVariable.endorser, "endorser", ContactDetail.class);
            ValidationSupport.checkList(evidenceVariable.relatedArtifact, "relatedArtifact", RelatedArtifact.class);
            ValidationSupport.checkList(evidenceVariable.characteristic, "characteristic", Characteristic.class);
            ValidationSupport.checkList(evidenceVariable.category, "category", Category.class);
        }

        protected Builder from(EvidenceVariable evidenceVariable) {
            super.from(evidenceVariable);
            url = evidenceVariable.url;
            identifier.addAll(evidenceVariable.identifier);
            version = evidenceVariable.version;
            name = evidenceVariable.name;
            title = evidenceVariable.title;
            shortTitle = evidenceVariable.shortTitle;
            subtitle = evidenceVariable.subtitle;
            status = evidenceVariable.status;
            date = evidenceVariable.date;
            description = evidenceVariable.description;
            note.addAll(evidenceVariable.note);
            useContext.addAll(evidenceVariable.useContext);
            publisher = evidenceVariable.publisher;
            contact.addAll(evidenceVariable.contact);
            author.addAll(evidenceVariable.author);
            editor.addAll(evidenceVariable.editor);
            reviewer.addAll(evidenceVariable.reviewer);
            endorser.addAll(evidenceVariable.endorser);
            relatedArtifact.addAll(evidenceVariable.relatedArtifact);
            actual = evidenceVariable.actual;
            characteristicCombination = evidenceVariable.characteristicCombination;
            characteristic.addAll(evidenceVariable.characteristic);
            handling = evidenceVariable.handling;
            category.addAll(evidenceVariable.category);
            return this;
        }
    }

    /**
     * A characteristic that defines the members of the evidence element. Multiple characteristics are applied with "and" 
     * semantics.
     */
    public static class Characteristic extends BackboneElement {
        private final String description;
        @Summary
        @ReferenceTarget({ "Group", "EvidenceVariable" })
        @Choice({ Reference.class, Canonical.class, CodeableConcept.class, Expression.class })
        @Required
        private final Element definition;
        @Binding(
            bindingName = "CharacteristicMethod",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The method used to determine the characteristic(s) of the variable.",
            valueSet = "http://hl7.org/fhir/ValueSet/characteristic-method"
        )
        private final CodeableConcept method;
        @ReferenceTarget({ "Device", "DeviceMetric" })
        private final Reference device;
        private final Boolean exclude;
        private final TimeFromStart timeFromStart;
        @Binding(
            bindingName = "GroupMeasure",
            strength = BindingStrength.Value.REQUIRED,
            description = "Possible group measure aggregates (E.g. Mean, Median).",
            valueSet = "http://hl7.org/fhir/ValueSet/group-measure|4.3.0-CIBUILD"
        )
        private final GroupMeasure groupMeasure;

        private Characteristic(Builder builder) {
            super(builder);
            description = builder.description;
            definition = builder.definition;
            method = builder.method;
            device = builder.device;
            exclude = builder.exclude;
            timeFromStart = builder.timeFromStart;
            groupMeasure = builder.groupMeasure;
        }

        /**
         * A short, natural language description of the characteristic that could be used to communicate the criteria to an end-
         * user.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( 
         * using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the 
         * last year).
         * 
         * @return
         *     An immutable object of type {@link Reference}, {@link Canonical}, {@link CodeableConcept} or {@link Expression} that 
         *     is non-null.
         */
        public Element getDefinition() {
            return definition;
        }

        /**
         * Method used for describing characteristic.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getMethod() {
            return method;
        }

        /**
         * Device used for determining characteristic.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getDevice() {
            return device;
        }

        /**
         * When true, members with this characteristic are excluded from the element.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getExclude() {
            return exclude;
        }

        /**
         * Indicates duration, period, or point of observation from the participant's study entry.
         * 
         * @return
         *     An immutable object of type {@link TimeFromStart} that may be null.
         */
        public TimeFromStart getTimeFromStart() {
            return timeFromStart;
        }

        /**
         * Indicates how elements are aggregated within the study effective period.
         * 
         * @return
         *     An immutable object of type {@link GroupMeasure} that may be null.
         */
        public GroupMeasure getGroupMeasure() {
            return groupMeasure;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (definition != null) || 
                (method != null) || 
                (device != null) || 
                (exclude != null) || 
                (timeFromStart != null) || 
                (groupMeasure != null);
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
                    accept(definition, "definition", visitor);
                    accept(method, "method", visitor);
                    accept(device, "device", visitor);
                    accept(exclude, "exclude", visitor);
                    accept(timeFromStart, "timeFromStart", visitor);
                    accept(groupMeasure, "groupMeasure", visitor);
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
            Characteristic other = (Characteristic) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(definition, other.definition) && 
                Objects.equals(method, other.method) && 
                Objects.equals(device, other.device) && 
                Objects.equals(exclude, other.exclude) && 
                Objects.equals(timeFromStart, other.timeFromStart) && 
                Objects.equals(groupMeasure, other.groupMeasure);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    definition, 
                    method, 
                    device, 
                    exclude, 
                    timeFromStart, 
                    groupMeasure);
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
            private Element definition;
            private CodeableConcept method;
            private Reference device;
            private Boolean exclude;
            private TimeFromStart timeFromStart;
            private GroupMeasure groupMeasure;

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
             *     Natural language description of the characteristic
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
             * A short, natural language description of the characteristic that could be used to communicate the criteria to an end-
             * user.
             * 
             * @param description
             *     Natural language description of the characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Define members of the evidence element using Codes (such as condition, medication, or observation), Expressions ( 
             * using an expression language such as FHIRPath or CQL) or DataRequirements (such as Diabetes diagnosis onset in the 
             * last year).
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Reference}</li>
             * <li>{@link Canonical}</li>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Expression}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link Group}</li>
             * <li>{@link EvidenceVariable}</li>
             * </ul>
             * 
             * @param definition
             *     What code or expression defines members?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder definition(Element definition) {
                this.definition = definition;
                return this;
            }

            /**
             * Method used for describing characteristic.
             * 
             * @param method
             *     Method used for describing characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder method(CodeableConcept method) {
                this.method = method;
                return this;
            }

            /**
             * Device used for determining characteristic.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Device}</li>
             * <li>{@link DeviceMetric}</li>
             * </ul>
             * 
             * @param device
             *     Device used for determining characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder device(Reference device) {
                this.device = device;
                return this;
            }

            /**
             * Convenience method for setting {@code exclude}.
             * 
             * @param exclude
             *     Whether the characteristic includes or excludes members
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #exclude(com.ibm.fhir.model.type.Boolean)
             */
            public Builder exclude(java.lang.Boolean exclude) {
                this.exclude = (exclude == null) ? null : Boolean.of(exclude);
                return this;
            }

            /**
             * When true, members with this characteristic are excluded from the element.
             * 
             * @param exclude
             *     Whether the characteristic includes or excludes members
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder exclude(Boolean exclude) {
                this.exclude = exclude;
                return this;
            }

            /**
             * Indicates duration, period, or point of observation from the participant's study entry.
             * 
             * @param timeFromStart
             *     Observation time from study start
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder timeFromStart(TimeFromStart timeFromStart) {
                this.timeFromStart = timeFromStart;
                return this;
            }

            /**
             * Indicates how elements are aggregated within the study effective period.
             * 
             * @param groupMeasure
             *     mean | median | mean-of-mean | mean-of-median | median-of-mean | median-of-median
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder groupMeasure(GroupMeasure groupMeasure) {
                this.groupMeasure = groupMeasure;
                return this;
            }

            /**
             * Build the {@link Characteristic}
             * 
             * <p>Required elements:
             * <ul>
             * <li>definition</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Characteristic}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Characteristic per the base specification
             */
            @Override
            public Characteristic build() {
                Characteristic characteristic = new Characteristic(this);
                if (validating) {
                    validate(characteristic);
                }
                return characteristic;
            }

            protected void validate(Characteristic characteristic) {
                super.validate(characteristic);
                ValidationSupport.requireChoiceElement(characteristic.definition, "definition", Reference.class, Canonical.class, CodeableConcept.class, Expression.class);
                ValidationSupport.checkReferenceType(characteristic.definition, "definition", "Group", "EvidenceVariable");
                ValidationSupport.checkReferenceType(characteristic.device, "device", "Device", "DeviceMetric");
                ValidationSupport.requireValueOrChildren(characteristic);
            }

            protected Builder from(Characteristic characteristic) {
                super.from(characteristic);
                description = characteristic.description;
                definition = characteristic.definition;
                method = characteristic.method;
                device = characteristic.device;
                exclude = characteristic.exclude;
                timeFromStart = characteristic.timeFromStart;
                groupMeasure = characteristic.groupMeasure;
                return this;
            }
        }

        /**
         * Indicates duration, period, or point of observation from the participant's study entry.
         */
        public static class TimeFromStart extends BackboneElement {
            private final String description;
            private final Quantity quantity;
            private final Range range;
            private final List<Annotation> note;

            private TimeFromStart(Builder builder) {
                super(builder);
                description = builder.description;
                quantity = builder.quantity;
                range = builder.range;
                note = Collections.unmodifiableList(builder.note);
            }

            /**
             * A short, natural language description.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDescription() {
                return description;
            }

            /**
             * Used to express the observation at a defined amount of time after the study start.
             * 
             * @return
             *     An immutable object of type {@link Quantity} that may be null.
             */
            public Quantity getQuantity() {
                return quantity;
            }

            /**
             * Used to express the observation within a period after the study start.
             * 
             * @return
             *     An immutable object of type {@link Range} that may be null.
             */
            public Range getRange() {
                return range;
            }

            /**
             * A human-readable string to clarify or explain concepts about the resource.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
             */
            public List<Annotation> getNote() {
                return note;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (description != null) || 
                    (quantity != null) || 
                    (range != null) || 
                    !note.isEmpty();
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
                        accept(quantity, "quantity", visitor);
                        accept(range, "range", visitor);
                        accept(note, "note", visitor, Annotation.class);
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
                TimeFromStart other = (TimeFromStart) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(description, other.description) && 
                    Objects.equals(quantity, other.quantity) && 
                    Objects.equals(range, other.range) && 
                    Objects.equals(note, other.note);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        description, 
                        quantity, 
                        range, 
                        note);
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
                private Quantity quantity;
                private Range range;
                private List<Annotation> note = new ArrayList<>();

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
                 *     Human readable description
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
                 * A short, natural language description.
                 * 
                 * @param description
                 *     Human readable description
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                /**
                 * Used to express the observation at a defined amount of time after the study start.
                 * 
                 * @param quantity
                 *     Used to express the observation at a defined amount of time after the study start
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder quantity(Quantity quantity) {
                    this.quantity = quantity;
                    return this;
                }

                /**
                 * Used to express the observation within a period after the study start.
                 * 
                 * @param range
                 *     Used to express the observation within a period after the study start
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder range(Range range) {
                    this.range = range;
                    return this;
                }

                /**
                 * A human-readable string to clarify or explain concepts about the resource.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param note
                 *     Used for footnotes or explanatory notes
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder note(Annotation... note) {
                    for (Annotation value : note) {
                        this.note.add(value);
                    }
                    return this;
                }

                /**
                 * A human-readable string to clarify or explain concepts about the resource.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param note
                 *     Used for footnotes or explanatory notes
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder note(Collection<Annotation> note) {
                    this.note = new ArrayList<>(note);
                    return this;
                }

                /**
                 * Build the {@link TimeFromStart}
                 * 
                 * @return
                 *     An immutable object of type {@link TimeFromStart}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid TimeFromStart per the base specification
                 */
                @Override
                public TimeFromStart build() {
                    TimeFromStart timeFromStart = new TimeFromStart(this);
                    if (validating) {
                        validate(timeFromStart);
                    }
                    return timeFromStart;
                }

                protected void validate(TimeFromStart timeFromStart) {
                    super.validate(timeFromStart);
                    ValidationSupport.checkList(timeFromStart.note, "note", Annotation.class);
                    ValidationSupport.requireValueOrChildren(timeFromStart);
                }

                protected Builder from(TimeFromStart timeFromStart) {
                    super.from(timeFromStart);
                    description = timeFromStart.description;
                    quantity = timeFromStart.quantity;
                    range = timeFromStart.range;
                    note.addAll(timeFromStart.note);
                    return this;
                }
            }
        }
    }

    /**
     * A grouping (or set of values) described along with other groupings to specify the set of groupings allowed for the 
     * variable.
     */
    public static class Category extends BackboneElement {
        private final String name;
        @Choice({ CodeableConcept.class, Quantity.class, Range.class })
        private final Element value;

        private Category(Builder builder) {
            super(builder);
            name = builder.name;
            value = builder.value;
        }

        /**
         * A human-readable title or representation of the grouping.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * Value or set of values that define the grouping.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}, {@link Quantity} or {@link Range} that may be null.
         */
        public Element getValue() {
            return value;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
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
                    accept(name, "name", visitor);
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
            Category other = (Category) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
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
            private String name;
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
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     Description of the grouping
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
             * A human-readable title or representation of the grouping.
             * 
             * @param name
             *     Description of the grouping
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Value or set of values that define the grouping.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Quantity}</li>
             * <li>{@link Range}</li>
             * </ul>
             * 
             * @param value
             *     Definition of the grouping
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * Build the {@link Category}
             * 
             * @return
             *     An immutable object of type {@link Category}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Category per the base specification
             */
            @Override
            public Category build() {
                Category category = new Category(this);
                if (validating) {
                    validate(category);
                }
                return category;
            }

            protected void validate(Category category) {
                super.validate(category);
                ValidationSupport.choiceElement(category.value, "value", CodeableConcept.class, Quantity.class, Range.class);
                ValidationSupport.requireValueOrChildren(category);
            }

            protected Builder from(Category category) {
                super.from(category);
                name = category.name;
                value = category.value;
                return this;
            }
        }
    }
}
