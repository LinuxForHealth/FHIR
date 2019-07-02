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
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.CompositionAttestationMode;
import com.ibm.watsonhealth.fhir.model.type.CompositionStatus;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.DocumentConfidentiality;
import com.ibm.watsonhealth.fhir.model.type.DocumentRelationshipType;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.SectionMode;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A set of healthcare-related information that is assembled together into a single logical package that provides a 
 * single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who 
 * is making the statement. A Composition defines the structure and narrative content necessary for a document. However, 
 * a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where 
 * Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in 
 * the Bundle (for example Patient, Practitioner, Encounter, etc.).
 * </p>
 */
@Constraint(
    id = "cmp-1",
    level = "Rule",
    location = "Composition.section",
    description = "A section must contain at least one of text, entries, or sub-sections",
    expression = "text.exists() or entry.exists() or section.exists()"
)
@Constraint(
    id = "cmp-2",
    level = "Rule",
    location = "Composition.section",
    description = "A section can only have an emptyReason if it is empty",
    expression = "emptyReason.empty() or entry.empty()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Composition extends DomainResource {
    private final Identifier identifier;
    private final CompositionStatus status;
    private final CodeableConcept type;
    private final List<CodeableConcept> category;
    private final Reference subject;
    private final Reference encounter;
    private final DateTime date;
    private final List<Reference> author;
    private final String title;
    private final DocumentConfidentiality confidentiality;
    private final List<Attester> attester;
    private final Reference custodian;
    private final List<RelatesTo> relatesTo;
    private final List<Event> event;
    private final List<Section> section;

    private volatile int hashCode;

    private Composition(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        type = ValidationSupport.requireNonNull(builder.type, "type");
        category = Collections.unmodifiableList(builder.category);
        subject = builder.subject;
        encounter = builder.encounter;
        date = ValidationSupport.requireNonNull(builder.date, "date");
        author = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.author, "author"));
        title = ValidationSupport.requireNonNull(builder.title, "title");
        confidentiality = builder.confidentiality;
        attester = Collections.unmodifiableList(builder.attester);
        custodian = builder.custodian;
        relatesTo = Collections.unmodifiableList(builder.relatesTo);
        event = Collections.unmodifiableList(builder.event);
        section = Collections.unmodifiableList(builder.section);
    }

    /**
     * <p>
     * A version-independent identifier for the Composition. This identifier stays constant as the composition is changed 
     * over time.
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
     * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CompositionStatus}.
     */
    public CompositionStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This 
     * usually equates to the purpose of making the composition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * <p>
     * A categorization for the type of the composition - helps for indexing and searching. This may be implied by or derived 
     * from the code specified in the Composition Type.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * <p>
     * Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a 
     * device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of 
     * patients that share a common exposure).
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
     * Describes the clinical encounter or type of care this documentation is associated with.
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
     * The composition editing time, when the composition was last logically changed by the author.
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
     * Identifies who is responsible for the information in the composition, not necessarily who typed it in.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAuthor() {
        return author;
    }

    /**
     * <p>
     * Official human-readable label for the composition.
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
     * The code specifying the level of confidentiality of the Composition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DocumentConfidentiality}.
     */
    public DocumentConfidentiality getConfidentiality() {
        return confidentiality;
    }

    /**
     * <p>
     * A participant who has attested to the accuracy of the composition/document.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Attester}.
     */
    public List<Attester> getAttester() {
        return attester;
    }

    /**
     * <p>
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the 
     * composition/document information.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getCustodian() {
        return custodian;
    }

    /**
     * <p>
     * Relationships that this composition has with other compositions or documents that already exist.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link RelatesTo}.
     */
    public List<RelatesTo> getRelatesTo() {
        return relatesTo;
    }

    /**
     * <p>
     * The clinical service, such as a colonoscopy or an appendectomy, being documented.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Event}.
     */
    public List<Event> getEvent() {
        return event;
    }

    /**
     * <p>
     * The root of the sections that make up the composition.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Section}.
     */
    public List<Section> getSection() {
        return section;
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
                accept(identifier, "identifier", visitor);
                accept(status, "status", visitor);
                accept(type, "type", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(date, "date", visitor);
                accept(author, "author", visitor, Reference.class);
                accept(title, "title", visitor);
                accept(confidentiality, "confidentiality", visitor);
                accept(attester, "attester", visitor, Attester.class);
                accept(custodian, "custodian", visitor);
                accept(relatesTo, "relatesTo", visitor, RelatesTo.class);
                accept(event, "event", visitor, Event.class);
                accept(section, "section", visitor, Section.class);
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
        Composition other = (Composition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(type, other.type) && 
            Objects.equals(category, other.category) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(date, other.date) && 
            Objects.equals(author, other.author) && 
            Objects.equals(title, other.title) && 
            Objects.equals(confidentiality, other.confidentiality) && 
            Objects.equals(attester, other.attester) && 
            Objects.equals(custodian, other.custodian) && 
            Objects.equals(relatesTo, other.relatesTo) && 
            Objects.equals(event, other.event) && 
            Objects.equals(section, other.section);
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
                status, 
                type, 
                category, 
                subject, 
                encounter, 
                date, 
                author, 
                title, 
                confidentiality, 
                attester, 
                custodian, 
                relatesTo, 
                event, 
                section);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status, type, date, author, title).from(this);
    }

    public Builder toBuilder(CompositionStatus status, CodeableConcept type, DateTime date, List<Reference> author, String title) {
        return new Builder(status, type, date, author, title).from(this);
    }

    public static Builder builder(CompositionStatus status, CodeableConcept type, DateTime date, List<Reference> author, String title) {
        return new Builder(status, type, date, author, title);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final CompositionStatus status;
        private final CodeableConcept type;
        private final DateTime date;
        private final List<Reference> author;
        private final String title;

        // optional
        private Identifier identifier;
        private List<CodeableConcept> category = new ArrayList<>();
        private Reference subject;
        private Reference encounter;
        private DocumentConfidentiality confidentiality;
        private List<Attester> attester = new ArrayList<>();
        private Reference custodian;
        private List<RelatesTo> relatesTo = new ArrayList<>();
        private List<Event> event = new ArrayList<>();
        private List<Section> section = new ArrayList<>();

        private Builder(CompositionStatus status, CodeableConcept type, DateTime date, List<Reference> author, String title) {
            super();
            this.status = status;
            this.type = type;
            this.date = date;
            this.author = author;
            this.title = title;
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
         * A version-independent identifier for the Composition. This identifier stays constant as the composition is changed 
         * over time.
         * </p>
         * 
         * @param identifier
         *     Version-independent identifier for the Composition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * <p>
         * A categorization for the type of the composition - helps for indexing and searching. This may be implied by or derived 
         * from the code specified in the Composition Type.
         * </p>
         * 
         * @param category
         *     Categorization of Composition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A categorization for the type of the composition - helps for indexing and searching. This may be implied by or derived 
         * from the code specified in the Composition Type.
         * </p>
         * 
         * @param category
         *     Categorization of Composition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category.addAll(category);
            return this;
        }

        /**
         * <p>
         * Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a 
         * device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of 
         * patients that share a common exposure).
         * </p>
         * 
         * @param subject
         *     Who and/or what the composition is about
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * Describes the clinical encounter or type of care this documentation is associated with.
         * </p>
         * 
         * @param encounter
         *     Context of the Composition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * <p>
         * The code specifying the level of confidentiality of the Composition.
         * </p>
         * 
         * @param confidentiality
         *     As defined by affinity domain
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder confidentiality(DocumentConfidentiality confidentiality) {
            this.confidentiality = confidentiality;
            return this;
        }

        /**
         * <p>
         * A participant who has attested to the accuracy of the composition/document.
         * </p>
         * 
         * @param attester
         *     Attests to accuracy of composition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder attester(Attester... attester) {
            for (Attester value : attester) {
                this.attester.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A participant who has attested to the accuracy of the composition/document.
         * </p>
         * 
         * @param attester
         *     Attests to accuracy of composition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder attester(Collection<Attester> attester) {
            this.attester.addAll(attester);
            return this;
        }

        /**
         * <p>
         * Identifies the organization or group who is responsible for ongoing maintenance of and access to the 
         * composition/document information.
         * </p>
         * 
         * @param custodian
         *     Organization which maintains the composition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder custodian(Reference custodian) {
            this.custodian = custodian;
            return this;
        }

        /**
         * <p>
         * Relationships that this composition has with other compositions or documents that already exist.
         * </p>
         * 
         * @param relatesTo
         *     Relationships to other compositions/documents
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relatesTo(RelatesTo... relatesTo) {
            for (RelatesTo value : relatesTo) {
                this.relatesTo.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Relationships that this composition has with other compositions or documents that already exist.
         * </p>
         * 
         * @param relatesTo
         *     Relationships to other compositions/documents
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relatesTo(Collection<RelatesTo> relatesTo) {
            this.relatesTo.addAll(relatesTo);
            return this;
        }

        /**
         * <p>
         * The clinical service, such as a colonoscopy or an appendectomy, being documented.
         * </p>
         * 
         * @param event
         *     The clinical service(s) being documented
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder event(Event... event) {
            for (Event value : event) {
                this.event.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The clinical service, such as a colonoscopy or an appendectomy, being documented.
         * </p>
         * 
         * @param event
         *     The clinical service(s) being documented
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder event(Collection<Event> event) {
            this.event.addAll(event);
            return this;
        }

        /**
         * <p>
         * The root of the sections that make up the composition.
         * </p>
         * 
         * @param section
         *     Composition is broken into sections
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder section(Section... section) {
            for (Section value : section) {
                this.section.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The root of the sections that make up the composition.
         * </p>
         * 
         * @param section
         *     Composition is broken into sections
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder section(Collection<Section> section) {
            this.section.addAll(section);
            return this;
        }

        @Override
        public Composition build() {
            return new Composition(this);
        }

        private Builder from(Composition composition) {
            id = composition.id;
            meta = composition.meta;
            implicitRules = composition.implicitRules;
            language = composition.language;
            text = composition.text;
            contained.addAll(composition.contained);
            extension.addAll(composition.extension);
            modifierExtension.addAll(composition.modifierExtension);
            identifier = composition.identifier;
            category.addAll(composition.category);
            subject = composition.subject;
            encounter = composition.encounter;
            confidentiality = composition.confidentiality;
            attester.addAll(composition.attester);
            custodian = composition.custodian;
            relatesTo.addAll(composition.relatesTo);
            event.addAll(composition.event);
            section.addAll(composition.section);
            return this;
        }
    }

    /**
     * <p>
     * A participant who has attested to the accuracy of the composition/document.
     * </p>
     */
    public static class Attester extends BackboneElement {
        private final CompositionAttestationMode mode;
        private final DateTime time;
        private final Reference party;

        private volatile int hashCode;

        private Attester(Builder builder) {
            super(builder);
            mode = ValidationSupport.requireNonNull(builder.mode, "mode");
            time = builder.time;
            party = builder.party;
        }

        /**
         * <p>
         * The type of attestation the authenticator offers.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CompositionAttestationMode}.
         */
        public CompositionAttestationMode getMode() {
            return mode;
        }

        /**
         * <p>
         * When the composition was attested by the party.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getTime() {
            return time;
        }

        /**
         * <p>
         * Who attested the composition in the specified way.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getParty() {
            return party;
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
                    accept(mode, "mode", visitor);
                    accept(time, "time", visitor);
                    accept(party, "party", visitor);
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
            Attester other = (Attester) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(mode, other.mode) && 
                Objects.equals(time, other.time) && 
                Objects.equals(party, other.party);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    mode, 
                    time, 
                    party);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(mode).from(this);
        }

        public Builder toBuilder(CompositionAttestationMode mode) {
            return new Builder(mode).from(this);
        }

        public static Builder builder(CompositionAttestationMode mode) {
            return new Builder(mode);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CompositionAttestationMode mode;

            // optional
            private DateTime time;
            private Reference party;

            private Builder(CompositionAttestationMode mode) {
                super();
                this.mode = mode;
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
             * When the composition was attested by the party.
             * </p>
             * 
             * @param time
             *     When the composition was attested
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder time(DateTime time) {
                this.time = time;
                return this;
            }

            /**
             * <p>
             * Who attested the composition in the specified way.
             * </p>
             * 
             * @param party
             *     Who attested the composition
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder party(Reference party) {
                this.party = party;
                return this;
            }

            @Override
            public Attester build() {
                return new Attester(this);
            }

            private Builder from(Attester attester) {
                id = attester.id;
                extension.addAll(attester.extension);
                modifierExtension.addAll(attester.modifierExtension);
                time = attester.time;
                party = attester.party;
                return this;
            }
        }
    }

    /**
     * <p>
     * Relationships that this composition has with other compositions or documents that already exist.
     * </p>
     */
    public static class RelatesTo extends BackboneElement {
        private final DocumentRelationshipType code;
        private final Element target;

        private volatile int hashCode;

        private RelatesTo(Builder builder) {
            super(builder);
            code = ValidationSupport.requireNonNull(builder.code, "code");
            target = ValidationSupport.requireChoiceElement(builder.target, "target", Identifier.class, Reference.class);
        }

        /**
         * <p>
         * The type of relationship that this composition has with anther composition or document.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DocumentRelationshipType}.
         */
        public DocumentRelationshipType getCode() {
            return code;
        }

        /**
         * <p>
         * The target composition/document of this relationship.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getTarget() {
            return target;
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
                    accept(target, "target", visitor);
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
            RelatesTo other = (RelatesTo) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(target, other.target);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    target);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(code, target).from(this);
        }

        public Builder toBuilder(DocumentRelationshipType code, Element target) {
            return new Builder(code, target).from(this);
        }

        public static Builder builder(DocumentRelationshipType code, Element target) {
            return new Builder(code, target);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final DocumentRelationshipType code;
            private final Element target;

            private Builder(DocumentRelationshipType code, Element target) {
                super();
                this.code = code;
                this.target = target;
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
            public RelatesTo build() {
                return new RelatesTo(this);
            }

            private Builder from(RelatesTo relatesTo) {
                id = relatesTo.id;
                extension.addAll(relatesTo.extension);
                modifierExtension.addAll(relatesTo.modifierExtension);
                return this;
            }
        }
    }

    /**
     * <p>
     * The clinical service, such as a colonoscopy or an appendectomy, being documented.
     * </p>
     */
    public static class Event extends BackboneElement {
        private final List<CodeableConcept> code;
        private final Period period;
        private final List<Reference> detail;

        private volatile int hashCode;

        private Event(Builder builder) {
            super(builder);
            code = Collections.unmodifiableList(builder.code);
            period = builder.period;
            detail = Collections.unmodifiableList(builder.detail);
        }

        /**
         * <p>
         * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
         * some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure 
         * being documented is necessarily a "History and Physical" act.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getCode() {
            return code;
        }

        /**
         * <p>
         * The period of time covered by the documentation. There is no assertion that the documentation is a complete 
         * representation for this period, only that it documents events during this time.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * <p>
         * The description and/or reference of the event(s) being documented. For example, this could be used to document such a 
         * colonoscopy or an appendectomy.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getDetail() {
            return detail;
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
                    accept(code, "code", visitor, CodeableConcept.class);
                    accept(period, "period", visitor);
                    accept(detail, "detail", visitor, Reference.class);
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
            Event other = (Event) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(period, other.period) && 
                Objects.equals(detail, other.detail);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    period, 
                    detail);
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
            private List<CodeableConcept> code = new ArrayList<>();
            private Period period;
            private List<Reference> detail = new ArrayList<>();

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
             * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
             * some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure 
             * being documented is necessarily a "History and Physical" act.
             * </p>
             * 
             * @param code
             *     Code(s) that apply to the event being documented
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder code(CodeableConcept... code) {
                for (CodeableConcept value : code) {
                    this.code.add(value);
                }
                return this;
            }

            /**
             * <p>
             * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
             * some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure 
             * being documented is necessarily a "History and Physical" act.
             * </p>
             * 
             * @param code
             *     Code(s) that apply to the event being documented
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder code(Collection<CodeableConcept> code) {
                this.code.addAll(code);
                return this;
            }

            /**
             * <p>
             * The period of time covered by the documentation. There is no assertion that the documentation is a complete 
             * representation for this period, only that it documents events during this time.
             * </p>
             * 
             * @param period
             *     The period covered by the documentation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * <p>
             * The description and/or reference of the event(s) being documented. For example, this could be used to document such a 
             * colonoscopy or an appendectomy.
             * </p>
             * 
             * @param detail
             *     The event(s) being documented
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detail(Reference... detail) {
                for (Reference value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The description and/or reference of the event(s) being documented. For example, this could be used to document such a 
             * colonoscopy or an appendectomy.
             * </p>
             * 
             * @param detail
             *     The event(s) being documented
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder detail(Collection<Reference> detail) {
                this.detail.addAll(detail);
                return this;
            }

            @Override
            public Event build() {
                return new Event(this);
            }

            private Builder from(Event event) {
                id = event.id;
                extension.addAll(event.extension);
                modifierExtension.addAll(event.modifierExtension);
                code.addAll(event.code);
                period = event.period;
                detail.addAll(event.detail);
                return this;
            }
        }
    }

    /**
     * <p>
     * The root of the sections that make up the composition.
     * </p>
     */
    public static class Section extends BackboneElement {
        private final String title;
        private final CodeableConcept code;
        private final List<Reference> author;
        private final Reference focus;
        private final Narrative text;
        private final SectionMode mode;
        private final CodeableConcept orderedBy;
        private final List<Reference> entry;
        private final CodeableConcept emptyReason;
        private final List<Composition.Section> section;

        private volatile int hashCode;

        private Section(Builder builder) {
            super(builder);
            title = builder.title;
            code = builder.code;
            author = Collections.unmodifiableList(builder.author);
            focus = builder.focus;
            text = builder.text;
            mode = builder.mode;
            orderedBy = builder.orderedBy;
            entry = Collections.unmodifiableList(builder.entry);
            emptyReason = builder.emptyReason;
            section = Collections.unmodifiableList(builder.section);
        }

        /**
         * <p>
         * The label for this particular section. This will be part of the rendered content for the document, and is often used 
         * to build a table of contents.
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
         * A code identifying the kind of content contained within the section. This must be consistent with the section title.
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
         * Identifies who is responsible for the information in this section, not necessarily who typed it in.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getAuthor() {
            return author;
        }

        /**
         * <p>
         * The actual focus of the section when it is not the subject of the composition, but instead represents something or 
         * someone associated with the subject such as (for a patient subject) a spouse, parent, fetus, or donor. If not focus is 
         * specified, the focus is assumed to be focus of the parent section, or, for a section in the Composition itself, the 
         * subject of the composition. Sections with a focus SHALL only include resources where the logical subject (patient, 
         * subject, focus, etc.) matches the section focus, or the resources have no logical subject (few resources).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getFocus() {
            return focus;
        }

        /**
         * <p>
         * A human-readable narrative that contains the attested content of the section, used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Narrative}.
         */
        public Narrative getText() {
            return text;
        }

        /**
         * <p>
         * How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing 
         * basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where 
         * items may be marked as added, modified or deleted.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link SectionMode}.
         */
        public SectionMode getMode() {
            return mode;
        }

        /**
         * <p>
         * Specifies the order applied to the items in the section entries.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getOrderedBy() {
            return orderedBy;
        }

        /**
         * <p>
         * A reference to the actual resource from which the narrative in the section is derived.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getEntry() {
            return entry;
        }

        /**
         * <p>
         * If the section is empty, why the list is empty. An empty section typically has some text explaining the empty reason.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getEmptyReason() {
            return emptyReason;
        }

        /**
         * <p>
         * A nested sub-section within this section.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Section}.
         */
        public List<Composition.Section> getSection() {
            return section;
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
                    accept(code, "code", visitor);
                    accept(author, "author", visitor, Reference.class);
                    accept(focus, "focus", visitor);
                    accept(text, "text", visitor);
                    accept(mode, "mode", visitor);
                    accept(orderedBy, "orderedBy", visitor);
                    accept(entry, "entry", visitor, Reference.class);
                    accept(emptyReason, "emptyReason", visitor);
                    accept(section, "section", visitor, Composition.Section.class);
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
            Section other = (Section) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(title, other.title) && 
                Objects.equals(code, other.code) && 
                Objects.equals(author, other.author) && 
                Objects.equals(focus, other.focus) && 
                Objects.equals(text, other.text) && 
                Objects.equals(mode, other.mode) && 
                Objects.equals(orderedBy, other.orderedBy) && 
                Objects.equals(entry, other.entry) && 
                Objects.equals(emptyReason, other.emptyReason) && 
                Objects.equals(section, other.section);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    title, 
                    code, 
                    author, 
                    focus, 
                    text, 
                    mode, 
                    orderedBy, 
                    entry, 
                    emptyReason, 
                    section);
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
            private String title;
            private CodeableConcept code;
            private List<Reference> author = new ArrayList<>();
            private Reference focus;
            private Narrative text;
            private SectionMode mode;
            private CodeableConcept orderedBy;
            private List<Reference> entry = new ArrayList<>();
            private CodeableConcept emptyReason;
            private List<Composition.Section> section = new ArrayList<>();

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
             * The label for this particular section. This will be part of the rendered content for the document, and is often used 
             * to build a table of contents.
             * </p>
             * 
             * @param title
             *     Label for section (e.g. for ToC)
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
             * A code identifying the kind of content contained within the section. This must be consistent with the section title.
             * </p>
             * 
             * @param code
             *     Classification of section (recommended)
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
             * Identifies who is responsible for the information in this section, not necessarily who typed it in.
             * </p>
             * 
             * @param author
             *     Who and/or what authored the section
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder author(Reference... author) {
                for (Reference value : author) {
                    this.author.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Identifies who is responsible for the information in this section, not necessarily who typed it in.
             * </p>
             * 
             * @param author
             *     Who and/or what authored the section
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder author(Collection<Reference> author) {
                this.author.addAll(author);
                return this;
            }

            /**
             * <p>
             * The actual focus of the section when it is not the subject of the composition, but instead represents something or 
             * someone associated with the subject such as (for a patient subject) a spouse, parent, fetus, or donor. If not focus is 
             * specified, the focus is assumed to be focus of the parent section, or, for a section in the Composition itself, the 
             * subject of the composition. Sections with a focus SHALL only include resources where the logical subject (patient, 
             * subject, focus, etc.) matches the section focus, or the resources have no logical subject (few resources).
             * </p>
             * 
             * @param focus
             *     Who/what the section is about, when it is not about the subject of composition
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder focus(Reference focus) {
                this.focus = focus;
                return this;
            }

            /**
             * <p>
             * A human-readable narrative that contains the attested content of the section, used to represent the content of the 
             * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
             * detail to make it "clinically safe" for a human to just read the narrative.
             * </p>
             * 
             * @param text
             *     Text summary of the section, for human interpretation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder text(Narrative text) {
                this.text = text;
                return this;
            }

            /**
             * <p>
             * How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing 
             * basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where 
             * items may be marked as added, modified or deleted.
             * </p>
             * 
             * @param mode
             *     working | snapshot | changes
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder mode(SectionMode mode) {
                this.mode = mode;
                return this;
            }

            /**
             * <p>
             * Specifies the order applied to the items in the section entries.
             * </p>
             * 
             * @param orderedBy
             *     Order of section entries
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder orderedBy(CodeableConcept orderedBy) {
                this.orderedBy = orderedBy;
                return this;
            }

            /**
             * <p>
             * A reference to the actual resource from which the narrative in the section is derived.
             * </p>
             * 
             * @param entry
             *     A reference to data that supports this section
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder entry(Reference... entry) {
                for (Reference value : entry) {
                    this.entry.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A reference to the actual resource from which the narrative in the section is derived.
             * </p>
             * 
             * @param entry
             *     A reference to data that supports this section
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder entry(Collection<Reference> entry) {
                this.entry.addAll(entry);
                return this;
            }

            /**
             * <p>
             * If the section is empty, why the list is empty. An empty section typically has some text explaining the empty reason.
             * </p>
             * 
             * @param emptyReason
             *     Why the section is empty
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder emptyReason(CodeableConcept emptyReason) {
                this.emptyReason = emptyReason;
                return this;
            }

            /**
             * <p>
             * A nested sub-section within this section.
             * </p>
             * 
             * @param section
             *     Nested Section
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder section(Composition.Section... section) {
                for (Composition.Section value : section) {
                    this.section.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A nested sub-section within this section.
             * </p>
             * 
             * @param section
             *     Nested Section
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder section(Collection<Composition.Section> section) {
                this.section.addAll(section);
                return this;
            }

            @Override
            public Section build() {
                return new Section(this);
            }

            private Builder from(Section section) {
                id = section.id;
                extension.addAll(section.extension);
                modifierExtension.addAll(section.modifierExtension);
                title = section.title;
                code = section.code;
                author.addAll(section.author);
                focus = section.focus;
                text = section.text;
                mode = section.mode;
                orderedBy = section.orderedBy;
                entry.addAll(section.entry);
                emptyReason = section.emptyReason;
                this.section.addAll(section.section);
                return this;
            }
        }
    }
}
