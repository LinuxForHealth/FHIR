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
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.QuestionnaireResponseStatus;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, 
 * corresponding to the structure of the grouping of the questionnaire being responded to.
 * </p>
 */
@Constraint(
    id = "qrs-1",
    level = "Rule",
    location = "QuestionnaireResponse.item",
    description = "Nested item can't be beneath both item and answer",
    expression = "(answer.exists() and item.exists()).not()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class QuestionnaireResponse extends DomainResource {
    private final Identifier identifier;
    private final List<Reference> basedOn;
    private final List<Reference> partOf;
    private final Canonical questionnaire;
    private final QuestionnaireResponseStatus status;
    private final Reference subject;
    private final Reference encounter;
    private final DateTime authored;
    private final Reference author;
    private final Reference source;
    private final List<Item> item;

    private volatile int hashCode;

    private QuestionnaireResponse(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        basedOn = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.basedOn, "basedOn"));
        partOf = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.partOf, "partOf"));
        questionnaire = builder.questionnaire;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        subject = builder.subject;
        encounter = builder.encounter;
        authored = builder.authored;
        author = builder.author;
        source = builder.source;
        item = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.item, "item"));
    }

    /**
     * <p>
     * A business identifier assigned to a particular completed (or partially completed) questionnaire.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The order, proposal or plan that is fulfilled in whole or in part by this QuestionnaireResponse. For example, a 
     * ServiceRequest seeking an intake assessment or a decision support recommendation to assess for post-partum depression.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * <p>
     * A procedure or observation that this questionnaire was performed as part of the execution of. For example, the surgery 
     * a checklist was executed as part of.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * <p>
     * The Questionnaire that defines and organizes the questions for which answers are being provided.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getQuestionnaire() {
        return questionnaire;
    }

    /**
     * <p>
     * The position of the questionnaire response within its overall lifecycle.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link QuestionnaireResponseStatus}.
     */
    public QuestionnaireResponseStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The subject of the questionnaire response. This could be a patient, organization, practitioner, device, etc. This is 
     * who/what the answers apply to, but is not necessarily the source of information.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * <p>
     * The Encounter during which this questionnaire response was created or to which the creation of this record is tightly 
     * associated.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * <p>
     * The date and/or time that this set of answers were last changed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getAuthored() {
        return authored;
    }

    /**
     * <p>
     * Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAuthor() {
        return author;
    }

    /**
     * <p>
     * The person who answered the questions about the subject.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSource() {
        return source;
    }

    /**
     * <p>
     * A group or question item from the original questionnaire for which answers are provided.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Item}.
     */
    public List<Item> getItem() {
        return item;
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
                accept(identifier, "identifier", visitor);
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(questionnaire, "questionnaire", visitor);
                accept(status, "status", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(authored, "authored", visitor);
                accept(author, "author", visitor);
                accept(source, "source", visitor);
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
        QuestionnaireResponse other = (QuestionnaireResponse) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(basedOn, other.basedOn) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(questionnaire, other.questionnaire) && 
            Objects.equals(status, other.status) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(authored, other.authored) && 
            Objects.equals(author, other.author) && 
            Objects.equals(source, other.source) && 
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
                identifier, 
                basedOn, 
                partOf, 
                questionnaire, 
                status, 
                subject, 
                encounter, 
                authored, 
                author, 
                source, 
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
        private Identifier identifier;
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private Canonical questionnaire;
        private QuestionnaireResponseStatus status;
        private Reference subject;
        private Reference encounter;
        private DateTime authored;
        private Reference author;
        private Reference source;
        private List<Item> item = new ArrayList<>();

        private Builder() {
            super();
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
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * A business identifier assigned to a particular completed (or partially completed) questionnaire.
         * </p>
         * 
         * @param identifier
         *     Unique id for this set of answers
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * <p>
         * The order, proposal or plan that is fulfilled in whole or in part by this QuestionnaireResponse. For example, a 
         * ServiceRequest seeking an intake assessment or a decision support recommendation to assess for post-partum depression.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param basedOn
         *     Request fulfilled by this QuestionnaireResponse
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Reference... basedOn) {
            for (Reference value : basedOn) {
                this.basedOn.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The order, proposal or plan that is fulfilled in whole or in part by this QuestionnaireResponse. For example, a 
         * ServiceRequest seeking an intake assessment or a decision support recommendation to assess for post-partum depression.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param basedOn
         *     Request fulfilled by this QuestionnaireResponse
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * <p>
         * A procedure or observation that this questionnaire was performed as part of the execution of. For example, the surgery 
         * a checklist was executed as part of.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param partOf
         *     Part of this action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A procedure or observation that this questionnaire was performed as part of the execution of. For example, the surgery 
         * a checklist was executed as part of.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param partOf
         *     Part of this action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * <p>
         * The Questionnaire that defines and organizes the questions for which answers are being provided.
         * </p>
         * 
         * @param questionnaire
         *     Form being answered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder questionnaire(Canonical questionnaire) {
            this.questionnaire = questionnaire;
            return this;
        }

        /**
         * <p>
         * The position of the questionnaire response within its overall lifecycle.
         * </p>
         * 
         * @param status
         *     in-progress | completed | amended | entered-in-error | stopped
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(QuestionnaireResponseStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * The subject of the questionnaire response. This could be a patient, organization, practitioner, device, etc. This is 
         * who/what the answers apply to, but is not necessarily the source of information.
         * </p>
         * 
         * @param subject
         *     The subject of the questions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * The Encounter during which this questionnaire response was created or to which the creation of this record is tightly 
         * associated.
         * </p>
         * 
         * @param encounter
         *     Encounter created as part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * <p>
         * The date and/or time that this set of answers were last changed.
         * </p>
         * 
         * @param authored
         *     Date the answers were gathered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder authored(DateTime authored) {
            this.authored = authored;
            return this;
        }

        /**
         * <p>
         * Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system.
         * </p>
         * 
         * @param author
         *     Person who received and recorded the answers
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Reference author) {
            this.author = author;
            return this;
        }

        /**
         * <p>
         * The person who answered the questions about the subject.
         * </p>
         * 
         * @param source
         *     The person who answered the questions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Reference source) {
            this.source = source;
            return this;
        }

        /**
         * <p>
         * A group or question item from the original questionnaire for which answers are provided.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param item
         *     Groups and questions
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
         * <p>
         * A group or question item from the original questionnaire for which answers are provided.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param item
         *     Groups and questions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder item(Collection<Item> item) {
            this.item = new ArrayList<>(item);
            return this;
        }

        @Override
        public QuestionnaireResponse build() {
            return new QuestionnaireResponse(this);
        }

        protected Builder from(QuestionnaireResponse questionnaireResponse) {
            super.from(questionnaireResponse);
            identifier = questionnaireResponse.identifier;
            basedOn.addAll(questionnaireResponse.basedOn);
            partOf.addAll(questionnaireResponse.partOf);
            questionnaire = questionnaireResponse.questionnaire;
            status = questionnaireResponse.status;
            subject = questionnaireResponse.subject;
            encounter = questionnaireResponse.encounter;
            authored = questionnaireResponse.authored;
            author = questionnaireResponse.author;
            source = questionnaireResponse.source;
            item.addAll(questionnaireResponse.item);
            return this;
        }
    }

    /**
     * <p>
     * A group or question item from the original questionnaire for which answers are provided.
     * </p>
     */
    public static class Item extends BackboneElement {
        private final String linkId;
        private final Uri definition;
        private final String text;
        private final List<Answer> answer;
        private final List<QuestionnaireResponse.Item> item;

        private volatile int hashCode;

        private Item(Builder builder) {
            super(builder);
            linkId = ValidationSupport.requireNonNull(builder.linkId, "linkId");
            definition = builder.definition;
            text = builder.text;
            answer = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.answer, "answer"));
            item = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.item, "item"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The item from the Questionnaire that corresponds to this item in the QuestionnaireResponse resource.
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
         * A reference to an [ElementDefinition](elementdefinition.html) that provides the details for the item.
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
         * Text that is displayed above the contents of the group or as the text of the question being answered.
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
         * The respondent's answer(s) to the question.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Answer}.
         */
        public List<Answer> getAnswer() {
            return answer;
        }

        /**
         * <p>
         * Questions or sub-groups nested beneath a question or group.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Item}.
         */
        public List<QuestionnaireResponse.Item> getItem() {
            return item;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (linkId != null) || 
                (definition != null) || 
                (text != null) || 
                !answer.isEmpty() || 
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
                    accept(text, "text", visitor);
                    accept(answer, "answer", visitor, Answer.class);
                    accept(item, "item", visitor, QuestionnaireResponse.Item.class);
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
                Objects.equals(text, other.text) && 
                Objects.equals(answer, other.answer) && 
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
                    text, 
                    answer, 
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
            private String text;
            private List<Answer> answer = new ArrayList<>();
            private List<QuestionnaireResponse.Item> item = new ArrayList<>();

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
             * Adds new element(s) to existing list
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
             * Adds new element(s) to existing list
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
             * The item from the Questionnaire that corresponds to this item in the QuestionnaireResponse resource.
             * </p>
             * 
             * @param linkId
             *     Pointer to specific item from Questionnaire
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder linkId(String linkId) {
                this.linkId = linkId;
                return this;
            }

            /**
             * <p>
             * A reference to an [ElementDefinition](elementdefinition.html) that provides the details for the item.
             * </p>
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
             * <p>
             * Text that is displayed above the contents of the group or as the text of the question being answered.
             * </p>
             * 
             * @param text
             *     Name for group or question text
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            /**
             * <p>
             * The respondent's answer(s) to the question.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param answer
             *     The response(s) to the question
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder answer(Answer... answer) {
                for (Answer value : answer) {
                    this.answer.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The respondent's answer(s) to the question.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param answer
             *     The response(s) to the question
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder answer(Collection<Answer> answer) {
                this.answer = new ArrayList<>(answer);
                return this;
            }

            /**
             * <p>
             * Questions or sub-groups nested beneath a question or group.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param item
             *     Nested questionnaire response items
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(QuestionnaireResponse.Item... item) {
                for (QuestionnaireResponse.Item value : item) {
                    this.item.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Questions or sub-groups nested beneath a question or group.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param item
             *     Nested questionnaire response items
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(Collection<QuestionnaireResponse.Item> item) {
                this.item = new ArrayList<>(item);
                return this;
            }

            @Override
            public Item build() {
                return new Item(this);
            }

            protected Builder from(Item item) {
                super.from(item);
                linkId = item.linkId;
                definition = item.definition;
                text = item.text;
                answer.addAll(item.answer);
                this.item.addAll(item.item);
                return this;
            }
        }

        /**
         * <p>
         * The respondent's answer(s) to the question.
         * </p>
         */
        public static class Answer extends BackboneElement {
            private final Element value;
            private final List<QuestionnaireResponse.Item> item;

            private volatile int hashCode;

            private Answer(Builder builder) {
                super(builder);
                value = ValidationSupport.choiceElement(builder.value, "value", Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Uri.class, Attachment.class, Coding.class, Quantity.class, Reference.class);
                item = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.item, "item"));
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * The answer (or one of the answers) provided by the respondent to the question.
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
             * Nested groups and/or questions found within this particular answer.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Item}.
             */
            public List<QuestionnaireResponse.Item> getItem() {
                return item;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (value != null) || 
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
                        accept(value, "value", visitor);
                        accept(item, "item", visitor, QuestionnaireResponse.Item.class);
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
                Answer other = (Answer) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(value, other.value) && 
                    Objects.equals(item, other.item);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        value, 
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
                private Element value;
                private List<QuestionnaireResponse.Item> item = new ArrayList<>();

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
                 * Adds new element(s) to existing list
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
                 * Adds new element(s) to existing list
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
                 * The answer (or one of the answers) provided by the respondent to the question.
                 * </p>
                 * 
                 * @param value
                 *     Single-valued answer to the question
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(Element value) {
                    this.value = value;
                    return this;
                }

                /**
                 * <p>
                 * Nested groups and/or questions found within this particular answer.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param item
                 *     Nested groups and questions
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder item(QuestionnaireResponse.Item... item) {
                    for (QuestionnaireResponse.Item value : item) {
                        this.item.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Nested groups and/or questions found within this particular answer.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param item
                 *     Nested groups and questions
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder item(Collection<QuestionnaireResponse.Item> item) {
                    this.item = new ArrayList<>(item);
                    return this;
                }

                @Override
                public Answer build() {
                    return new Answer(this);
                }

                protected Builder from(Answer answer) {
                    super.from(answer);
                    value = answer.value;
                    item.addAll(answer.item);
                    return this;
                }
            }
        }
    }
}
