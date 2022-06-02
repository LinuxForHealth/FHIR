/*
 * (C) Copyright IBM Corp. 2019, 2022
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
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.CompositionAttestationMode;
import com.ibm.fhir.model.type.code.CompositionStatus;
import com.ibm.fhir.model.type.code.DocumentConfidentiality;
import com.ibm.fhir.model.type.code.DocumentRelationshipType;
import com.ibm.fhir.model.type.code.SectionMode;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A set of healthcare-related information that is assembled together into a single logical package that provides a 
 * single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who 
 * is making the statement. A Composition defines the structure and narrative content necessary for a document. However, 
 * a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where 
 * Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in 
 * the Bundle (for example Patient, Practitioner, Encounter, etc.).
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "cmp-1",
    level = "Rule",
    location = "Composition.section",
    description = "A section must contain at least one of text, entries, or sub-sections",
    expression = "text.exists() or entry.exists() or section.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Composition"
)
@Constraint(
    id = "cmp-2",
    level = "Rule",
    location = "Composition.section",
    description = "A section can only have an emptyReason if it is empty",
    expression = "emptyReason.empty() or entry.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/Composition"
)
@Constraint(
    id = "composition-3",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/doc-typecodes",
    expression = "type.exists() and type.memberOf('http://hl7.org/fhir/ValueSet/doc-typecodes', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/Composition",
    generated = true
)
@Constraint(
    id = "composition-4",
    level = "Warning",
    location = "section.orderedBy",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/list-order",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/list-order', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/Composition",
    generated = true
)
@Constraint(
    id = "composition-5",
    level = "Warning",
    location = "section.emptyReason",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/list-empty-reason",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/list-empty-reason', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/Composition",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Composition extends DomainResource {
    @Summary
    private final Identifier identifier;
    @Summary
    @Binding(
        bindingName = "CompositionStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The workflow/clinical status of the composition.",
        valueSet = "http://hl7.org/fhir/ValueSet/composition-status|4.3.0-cibuild"
    )
    @Required
    private final CompositionStatus status;
    @Summary
    @Binding(
        bindingName = "DocumentType",
        strength = BindingStrength.Value.PREFERRED,
        description = "Type of a composition.",
        valueSet = "http://hl7.org/fhir/ValueSet/doc-typecodes"
    )
    @Required
    private final CodeableConcept type;
    @Summary
    @Binding(
        bindingName = "DocumentCategory",
        strength = BindingStrength.Value.EXAMPLE,
        description = "High-level kind of a clinical document at a macro level.",
        valueSet = "http://terminology.hl7.org/ValueSet/v3-LoincDocumentOntologyInternational"
    )
    private final List<CodeableConcept> category;
    @Summary
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Summary
    @Required
    private final DateTime date;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Device", "Patient", "RelatedPerson", "Organization" })
    @Required
    private final List<Reference> author;
    @Summary
    @Required
    private final String title;
    @Summary
    @Binding(
        bindingName = "DocumentConfidentiality",
        strength = BindingStrength.Value.REQUIRED,
        description = "Codes specifying the level of confidentiality of the composition.",
        valueSet = "http://terminology.hl7.org/ValueSet/v3-Confidentiality|2.0.0|2.0.0"
    )
    private final DocumentConfidentiality confidentiality;
    private final List<Attester> attester;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference custodian;
    private final List<RelatesTo> relatesTo;
    @Summary
    private final List<Event> event;
    private final List<Section> section;

    private Composition(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        status = builder.status;
        type = builder.type;
        category = Collections.unmodifiableList(builder.category);
        subject = builder.subject;
        encounter = builder.encounter;
        date = builder.date;
        author = Collections.unmodifiableList(builder.author);
        title = builder.title;
        confidentiality = builder.confidentiality;
        attester = Collections.unmodifiableList(builder.attester);
        custodian = builder.custodian;
        relatesTo = Collections.unmodifiableList(builder.relatesTo);
        event = Collections.unmodifiableList(builder.event);
        section = Collections.unmodifiableList(builder.section);
    }

    /**
     * A version-independent identifier for the Composition. This identifier stays constant as the composition is changed 
     * over time.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.
     * 
     * @return
     *     An immutable object of type {@link CompositionStatus} that is non-null.
     */
    public CompositionStatus getStatus() {
        return status;
    }

    /**
     * Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This 
     * usually equates to the purpose of making the composition.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * A categorization for the type of the composition - helps for indexing and searching. This may be implied by or derived 
     * from the code specified in the Composition Type.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a 
     * device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of 
     * patients that share a common exposure).
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * Describes the clinical encounter or type of care this documentation is associated with.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * The composition editing time, when the composition was last logically changed by the author.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * Identifies who is responsible for the information in the composition, not necessarily who typed it in.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that is non-empty.
     */
    public List<Reference> getAuthor() {
        return author;
    }

    /**
     * Official human-readable label for the composition.
     * 
     * @return
     *     An immutable object of type {@link String} that is non-null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The code specifying the level of confidentiality of the Composition.
     * 
     * @return
     *     An immutable object of type {@link DocumentConfidentiality} that may be null.
     */
    public DocumentConfidentiality getConfidentiality() {
        return confidentiality;
    }

    /**
     * A participant who has attested to the accuracy of the composition/document.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Attester} that may be empty.
     */
    public List<Attester> getAttester() {
        return attester;
    }

    /**
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the 
     * composition/document information.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getCustodian() {
        return custodian;
    }

    /**
     * Relationships that this composition has with other compositions or documents that already exist.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link RelatesTo} that may be empty.
     */
    public List<RelatesTo> getRelatesTo() {
        return relatesTo;
    }

    /**
     * The clinical service, such as a colonoscopy or an appendectomy, being documented.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Event} that may be empty.
     */
    public List<Event> getEvent() {
        return event;
    }

    /**
     * The root of the sections that make up the composition.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Section} that may be empty.
     */
    public List<Section> getSection() {
        return section;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (identifier != null) || 
            (status != null) || 
            (type != null) || 
            !category.isEmpty() || 
            (subject != null) || 
            (encounter != null) || 
            (date != null) || 
            !author.isEmpty() || 
            (title != null) || 
            (confidentiality != null) || 
            !attester.isEmpty() || 
            (custodian != null) || 
            !relatesTo.isEmpty() || 
            !event.isEmpty() || 
            !section.isEmpty();
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
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private Identifier identifier;
        private CompositionStatus status;
        private CodeableConcept type;
        private List<CodeableConcept> category = new ArrayList<>();
        private Reference subject;
        private Reference encounter;
        private DateTime date;
        private List<Reference> author = new ArrayList<>();
        private String title;
        private DocumentConfidentiality confidentiality;
        private List<Attester> attester = new ArrayList<>();
        private Reference custodian;
        private List<RelatesTo> relatesTo = new ArrayList<>();
        private List<Event> event = new ArrayList<>();
        private List<Section> section = new ArrayList<>();

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
         * A version-independent identifier for the Composition. This identifier stays constant as the composition is changed 
         * over time.
         * 
         * @param identifier
         *     Version-independent identifier for the Composition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     preliminary | final | amended | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CompositionStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This 
         * usually equates to the purpose of making the composition.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     Kind of composition (LOINC if possible)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * A categorization for the type of the composition - helps for indexing and searching. This may be implied by or derived 
         * from the code specified in the Composition Type.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Categorization of Composition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * A categorization for the type of the composition - helps for indexing and searching. This may be implied by or derived 
         * from the code specified in the Composition Type.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Categorization of Composition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a 
         * device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of 
         * patients that share a common exposure).
         * 
         * @param subject
         *     Who and/or what the composition is about
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Describes the clinical encounter or type of care this documentation is associated with.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
         * 
         * @param encounter
         *     Context of the Composition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * The composition editing time, when the composition was last logically changed by the author.
         * 
         * <p>This element is required.
         * 
         * @param date
         *     Composition editing time
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * Identifies who is responsible for the information in the composition, not necessarily who typed it in.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Device}</li>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param author
         *     Who and/or what authored the composition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Reference... author) {
            for (Reference value : author) {
                this.author.add(value);
            }
            return this;
        }

        /**
         * Identifies who is responsible for the information in the composition, not necessarily who typed it in.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Device}</li>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param author
         *     Who and/or what authored the composition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder author(Collection<Reference> author) {
            this.author = new ArrayList<>(author);
            return this;
        }

        /**
         * Convenience method for setting {@code title}.
         * 
         * <p>This element is required.
         * 
         * @param title
         *     Human Readable name/title
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
         * Official human-readable label for the composition.
         * 
         * <p>This element is required.
         * 
         * @param title
         *     Human Readable name/title
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * The code specifying the level of confidentiality of the Composition.
         * 
         * @param confidentiality
         *     As defined by affinity domain
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder confidentiality(DocumentConfidentiality confidentiality) {
            this.confidentiality = confidentiality;
            return this;
        }

        /**
         * A participant who has attested to the accuracy of the composition/document.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param attester
         *     Attests to accuracy of composition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder attester(Attester... attester) {
            for (Attester value : attester) {
                this.attester.add(value);
            }
            return this;
        }

        /**
         * A participant who has attested to the accuracy of the composition/document.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param attester
         *     Attests to accuracy of composition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder attester(Collection<Attester> attester) {
            this.attester = new ArrayList<>(attester);
            return this;
        }

        /**
         * Identifies the organization or group who is responsible for ongoing maintenance of and access to the 
         * composition/document information.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param custodian
         *     Organization which maintains the composition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder custodian(Reference custodian) {
            this.custodian = custodian;
            return this;
        }

        /**
         * Relationships that this composition has with other compositions or documents that already exist.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatesTo
         *     Relationships to other compositions/documents
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relatesTo(RelatesTo... relatesTo) {
            for (RelatesTo value : relatesTo) {
                this.relatesTo.add(value);
            }
            return this;
        }

        /**
         * Relationships that this composition has with other compositions or documents that already exist.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param relatesTo
         *     Relationships to other compositions/documents
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder relatesTo(Collection<RelatesTo> relatesTo) {
            this.relatesTo = new ArrayList<>(relatesTo);
            return this;
        }

        /**
         * The clinical service, such as a colonoscopy or an appendectomy, being documented.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param event
         *     The clinical service(s) being documented
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder event(Event... event) {
            for (Event value : event) {
                this.event.add(value);
            }
            return this;
        }

        /**
         * The clinical service, such as a colonoscopy or an appendectomy, being documented.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param event
         *     The clinical service(s) being documented
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder event(Collection<Event> event) {
            this.event = new ArrayList<>(event);
            return this;
        }

        /**
         * The root of the sections that make up the composition.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param section
         *     Composition is broken into sections
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder section(Section... section) {
            for (Section value : section) {
                this.section.add(value);
            }
            return this;
        }

        /**
         * The root of the sections that make up the composition.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param section
         *     Composition is broken into sections
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder section(Collection<Section> section) {
            this.section = new ArrayList<>(section);
            return this;
        }

        /**
         * Build the {@link Composition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>type</li>
         * <li>date</li>
         * <li>author</li>
         * <li>title</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Composition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Composition per the base specification
         */
        @Override
        public Composition build() {
            Composition composition = new Composition(this);
            if (validating) {
                validate(composition);
            }
            return composition;
        }

        protected void validate(Composition composition) {
            super.validate(composition);
            ValidationSupport.requireNonNull(composition.status, "status");
            ValidationSupport.requireNonNull(composition.type, "type");
            ValidationSupport.checkList(composition.category, "category", CodeableConcept.class);
            ValidationSupport.requireNonNull(composition.date, "date");
            ValidationSupport.checkNonEmptyList(composition.author, "author", Reference.class);
            ValidationSupport.requireNonNull(composition.title, "title");
            ValidationSupport.checkList(composition.attester, "attester", Attester.class);
            ValidationSupport.checkList(composition.relatesTo, "relatesTo", RelatesTo.class);
            ValidationSupport.checkList(composition.event, "event", Event.class);
            ValidationSupport.checkList(composition.section, "section", Section.class);
            ValidationSupport.checkReferenceType(composition.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(composition.author, "author", "Practitioner", "PractitionerRole", "Device", "Patient", "RelatedPerson", "Organization");
            ValidationSupport.checkReferenceType(composition.custodian, "custodian", "Organization");
        }

        protected Builder from(Composition composition) {
            super.from(composition);
            identifier = composition.identifier;
            status = composition.status;
            type = composition.type;
            category.addAll(composition.category);
            subject = composition.subject;
            encounter = composition.encounter;
            date = composition.date;
            author.addAll(composition.author);
            title = composition.title;
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
     * A participant who has attested to the accuracy of the composition/document.
     */
    public static class Attester extends BackboneElement {
        @Binding(
            bindingName = "CompositionAttestationMode",
            strength = BindingStrength.Value.REQUIRED,
            description = "The way in which a person authenticated a composition.",
            valueSet = "http://hl7.org/fhir/ValueSet/composition-attestation-mode|4.3.0-cibuild"
        )
        @Required
        private final CompositionAttestationMode mode;
        private final DateTime time;
        @ReferenceTarget({ "Patient", "RelatedPerson", "Practitioner", "PractitionerRole", "Organization" })
        private final Reference party;

        private Attester(Builder builder) {
            super(builder);
            mode = builder.mode;
            time = builder.time;
            party = builder.party;
        }

        /**
         * The type of attestation the authenticator offers.
         * 
         * @return
         *     An immutable object of type {@link CompositionAttestationMode} that is non-null.
         */
        public CompositionAttestationMode getMode() {
            return mode;
        }

        /**
         * When the composition was attested by the party.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getTime() {
            return time;
        }

        /**
         * Who attested the composition in the specified way.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getParty() {
            return party;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (mode != null) || 
                (time != null) || 
                (party != null);
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
                    accept(mode, "mode", visitor);
                    accept(time, "time", visitor);
                    accept(party, "party", visitor);
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private CompositionAttestationMode mode;
            private DateTime time;
            private Reference party;

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
             * The type of attestation the authenticator offers.
             * 
             * <p>This element is required.
             * 
             * @param mode
             *     personal | professional | legal | official
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder mode(CompositionAttestationMode mode) {
                this.mode = mode;
                return this;
            }

            /**
             * When the composition was attested by the party.
             * 
             * @param time
             *     When the composition was attested
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder time(DateTime time) {
                this.time = time;
                return this;
            }

            /**
             * Who attested the composition in the specified way.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Patient}</li>
             * <li>{@link RelatedPerson}</li>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param party
             *     Who attested the composition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder party(Reference party) {
                this.party = party;
                return this;
            }

            /**
             * Build the {@link Attester}
             * 
             * <p>Required elements:
             * <ul>
             * <li>mode</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Attester}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Attester per the base specification
             */
            @Override
            public Attester build() {
                Attester attester = new Attester(this);
                if (validating) {
                    validate(attester);
                }
                return attester;
            }

            protected void validate(Attester attester) {
                super.validate(attester);
                ValidationSupport.requireNonNull(attester.mode, "mode");
                ValidationSupport.checkReferenceType(attester.party, "party", "Patient", "RelatedPerson", "Practitioner", "PractitionerRole", "Organization");
                ValidationSupport.requireValueOrChildren(attester);
            }

            protected Builder from(Attester attester) {
                super.from(attester);
                mode = attester.mode;
                time = attester.time;
                party = attester.party;
                return this;
            }
        }
    }

    /**
     * Relationships that this composition has with other compositions or documents that already exist.
     */
    public static class RelatesTo extends BackboneElement {
        @Binding(
            bindingName = "DocumentRelationshipType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of relationship between documents.",
            valueSet = "http://hl7.org/fhir/ValueSet/document-relationship-type|4.3.0-cibuild"
        )
        @Required
        private final DocumentRelationshipType code;
        @ReferenceTarget({ "Composition" })
        @Choice({ Identifier.class, Reference.class })
        @Required
        private final Element target;

        private RelatesTo(Builder builder) {
            super(builder);
            code = builder.code;
            target = builder.target;
        }

        /**
         * The type of relationship that this composition has with anther composition or document.
         * 
         * @return
         *     An immutable object of type {@link DocumentRelationshipType} that is non-null.
         */
        public DocumentRelationshipType getCode() {
            return code;
        }

        /**
         * The target composition/document of this relationship.
         * 
         * @return
         *     An immutable object of type {@link Identifier} or {@link Reference} that is non-null.
         */
        public Element getTarget() {
            return target;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (target != null);
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
                    accept(target, "target", visitor);
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private DocumentRelationshipType code;
            private Element target;

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
             * The type of relationship that this composition has with anther composition or document.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     replaces | transforms | signs | appends
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(DocumentRelationshipType code) {
                this.code = code;
                return this;
            }

            /**
             * The target composition/document of this relationship.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Identifier}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link Composition}</li>
             * </ul>
             * 
             * @param target
             *     Target of the relationship
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder target(Element target) {
                this.target = target;
                return this;
            }

            /**
             * Build the {@link RelatesTo}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * <li>target</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link RelatesTo}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid RelatesTo per the base specification
             */
            @Override
            public RelatesTo build() {
                RelatesTo relatesTo = new RelatesTo(this);
                if (validating) {
                    validate(relatesTo);
                }
                return relatesTo;
            }

            protected void validate(RelatesTo relatesTo) {
                super.validate(relatesTo);
                ValidationSupport.requireNonNull(relatesTo.code, "code");
                ValidationSupport.requireChoiceElement(relatesTo.target, "target", Identifier.class, Reference.class);
                ValidationSupport.checkReferenceType(relatesTo.target, "target", "Composition");
                ValidationSupport.requireValueOrChildren(relatesTo);
            }

            protected Builder from(RelatesTo relatesTo) {
                super.from(relatesTo);
                code = relatesTo.code;
                target = relatesTo.target;
                return this;
            }
        }
    }

    /**
     * The clinical service, such as a colonoscopy or an appendectomy, being documented.
     */
    public static class Event extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "DocumentEventType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "This list of codes represents the main clinical acts being documented.",
            valueSet = "http://terminology.hl7.org/ValueSet/v3-ActCode"
        )
        private final List<CodeableConcept> code;
        @Summary
        private final Period period;
        @Summary
        private final List<Reference> detail;

        private Event(Builder builder) {
            super(builder);
            code = Collections.unmodifiableList(builder.code);
            period = builder.period;
            detail = Collections.unmodifiableList(builder.detail);
        }

        /**
         * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
         * some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure 
         * being documented is necessarily a "History and Physical" act.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getCode() {
            return code;
        }

        /**
         * The period of time covered by the documentation. There is no assertion that the documentation is a complete 
         * representation for this period, only that it documents events during this time.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * The description and/or reference of the event(s) being documented. For example, this could be used to document such a 
         * colonoscopy or an appendectomy.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getDetail() {
            return detail;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !code.isEmpty() || 
                (period != null) || 
                !detail.isEmpty();
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
                    accept(code, "code", visitor, CodeableConcept.class);
                    accept(period, "period", visitor);
                    accept(detail, "detail", visitor, Reference.class);
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
            private List<CodeableConcept> code = new ArrayList<>();
            private Period period;
            private List<Reference> detail = new ArrayList<>();

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
             * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
             * some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure 
             * being documented is necessarily a "History and Physical" act.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     Code(s) that apply to the event being documented
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept... code) {
                for (CodeableConcept value : code) {
                    this.code.add(value);
                }
                return this;
            }

            /**
             * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In 
             * some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure 
             * being documented is necessarily a "History and Physical" act.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param code
             *     Code(s) that apply to the event being documented
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder code(Collection<CodeableConcept> code) {
                this.code = new ArrayList<>(code);
                return this;
            }

            /**
             * The period of time covered by the documentation. There is no assertion that the documentation is a complete 
             * representation for this period, only that it documents events during this time.
             * 
             * @param period
             *     The period covered by the documentation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * The description and/or reference of the event(s) being documented. For example, this could be used to document such a 
             * colonoscopy or an appendectomy.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param detail
             *     The event(s) being documented
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Reference... detail) {
                for (Reference value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * The description and/or reference of the event(s) being documented. For example, this could be used to document such a 
             * colonoscopy or an appendectomy.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param detail
             *     The event(s) being documented
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder detail(Collection<Reference> detail) {
                this.detail = new ArrayList<>(detail);
                return this;
            }

            /**
             * Build the {@link Event}
             * 
             * @return
             *     An immutable object of type {@link Event}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Event per the base specification
             */
            @Override
            public Event build() {
                Event event = new Event(this);
                if (validating) {
                    validate(event);
                }
                return event;
            }

            protected void validate(Event event) {
                super.validate(event);
                ValidationSupport.checkList(event.code, "code", CodeableConcept.class);
                ValidationSupport.checkList(event.detail, "detail", Reference.class);
                ValidationSupport.requireValueOrChildren(event);
            }

            protected Builder from(Event event) {
                super.from(event);
                code.addAll(event.code);
                period = event.period;
                detail.addAll(event.detail);
                return this;
            }
        }
    }

    /**
     * The root of the sections that make up the composition.
     */
    public static class Section extends BackboneElement {
        private final String title;
        @Binding(
            bindingName = "CompositionSectionType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Classification of a section of a composition/document.",
            valueSet = "http://hl7.org/fhir/ValueSet/doc-section-codes"
        )
        private final CodeableConcept code;
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Device", "Patient", "RelatedPerson", "Organization" })
        private final List<Reference> author;
        private final Reference focus;
        private final Narrative text;
        @Binding(
            bindingName = "SectionMode",
            strength = BindingStrength.Value.REQUIRED,
            description = "The processing mode that applies to this section.",
            valueSet = "http://hl7.org/fhir/ValueSet/list-mode|4.3.0-cibuild"
        )
        private final SectionMode mode;
        @Binding(
            bindingName = "SectionEntryOrder",
            strength = BindingStrength.Value.PREFERRED,
            description = "What order applies to the items in the entry.",
            valueSet = "http://hl7.org/fhir/ValueSet/list-order"
        )
        private final CodeableConcept orderedBy;
        private final List<Reference> entry;
        @Binding(
            bindingName = "SectionEmptyReason",
            strength = BindingStrength.Value.PREFERRED,
            description = "If a section is empty, why it is empty.",
            valueSet = "http://hl7.org/fhir/ValueSet/list-empty-reason"
        )
        private final CodeableConcept emptyReason;
        private final List<Composition.Section> section;

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
         * The label for this particular section. This will be part of the rendered content for the document, and is often used 
         * to build a table of contents.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getTitle() {
            return title;
        }

        /**
         * A code identifying the kind of content contained within the section. This must be consistent with the section title.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * Identifies who is responsible for the information in this section, not necessarily who typed it in.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getAuthor() {
            return author;
        }

        /**
         * The actual focus of the section when it is not the subject of the composition, but instead represents something or 
         * someone associated with the subject such as (for a patient subject) a spouse, parent, fetus, or donor. If not focus is 
         * specified, the focus is assumed to be focus of the parent section, or, for a section in the Composition itself, the 
         * subject of the composition. Sections with a focus SHALL only include resources where the logical subject (patient, 
         * subject, focus, etc.) matches the section focus, or the resources have no logical subject (few resources).
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getFocus() {
            return focus;
        }

        /**
         * A human-readable narrative that contains the attested content of the section, used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative.
         * 
         * @return
         *     An immutable object of type {@link Narrative} that may be null.
         */
        public Narrative getText() {
            return text;
        }

        /**
         * How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing 
         * basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where 
         * items may be marked as added, modified or deleted.
         * 
         * @return
         *     An immutable object of type {@link SectionMode} that may be null.
         */
        public SectionMode getMode() {
            return mode;
        }

        /**
         * Specifies the order applied to the items in the section entries.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getOrderedBy() {
            return orderedBy;
        }

        /**
         * A reference to the actual resource from which the narrative in the section is derived.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getEntry() {
            return entry;
        }

        /**
         * If the section is empty, why the list is empty. An empty section typically has some text explaining the empty reason.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getEmptyReason() {
            return emptyReason;
        }

        /**
         * A nested sub-section within this section.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Section} that may be empty.
         */
        public List<Composition.Section> getSection() {
            return section;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (title != null) || 
                (code != null) || 
                !author.isEmpty() || 
                (focus != null) || 
                (text != null) || 
                (mode != null) || 
                (orderedBy != null) || 
                !entry.isEmpty() || 
                (emptyReason != null) || 
                !section.isEmpty();
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
             * Convenience method for setting {@code title}.
             * 
             * @param title
             *     Label for section (e.g. for ToC)
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
             * The label for this particular section. This will be part of the rendered content for the document, and is often used 
             * to build a table of contents.
             * 
             * @param title
             *     Label for section (e.g. for ToC)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder title(String title) {
                this.title = title;
                return this;
            }

            /**
             * A code identifying the kind of content contained within the section. This must be consistent with the section title.
             * 
             * @param code
             *     Classification of section (recommended)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Identifies who is responsible for the information in this section, not necessarily who typed it in.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Device}</li>
             * <li>{@link Patient}</li>
             * <li>{@link RelatedPerson}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param author
             *     Who and/or what authored the section
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder author(Reference... author) {
                for (Reference value : author) {
                    this.author.add(value);
                }
                return this;
            }

            /**
             * Identifies who is responsible for the information in this section, not necessarily who typed it in.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Device}</li>
             * <li>{@link Patient}</li>
             * <li>{@link RelatedPerson}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param author
             *     Who and/or what authored the section
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder author(Collection<Reference> author) {
                this.author = new ArrayList<>(author);
                return this;
            }

            /**
             * The actual focus of the section when it is not the subject of the composition, but instead represents something or 
             * someone associated with the subject such as (for a patient subject) a spouse, parent, fetus, or donor. If not focus is 
             * specified, the focus is assumed to be focus of the parent section, or, for a section in the Composition itself, the 
             * subject of the composition. Sections with a focus SHALL only include resources where the logical subject (patient, 
             * subject, focus, etc.) matches the section focus, or the resources have no logical subject (few resources).
             * 
             * @param focus
             *     Who/what the section is about, when it is not about the subject of composition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder focus(Reference focus) {
                this.focus = focus;
                return this;
            }

            /**
             * A human-readable narrative that contains the attested content of the section, used to represent the content of the 
             * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
             * detail to make it "clinically safe" for a human to just read the narrative.
             * 
             * @param text
             *     Text summary of the section, for human interpretation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder text(Narrative text) {
                this.text = text;
                return this;
            }

            /**
             * How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing 
             * basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where 
             * items may be marked as added, modified or deleted.
             * 
             * @param mode
             *     working | snapshot | changes
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder mode(SectionMode mode) {
                this.mode = mode;
                return this;
            }

            /**
             * Specifies the order applied to the items in the section entries.
             * 
             * @param orderedBy
             *     Order of section entries
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder orderedBy(CodeableConcept orderedBy) {
                this.orderedBy = orderedBy;
                return this;
            }

            /**
             * A reference to the actual resource from which the narrative in the section is derived.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param entry
             *     A reference to data that supports this section
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder entry(Reference... entry) {
                for (Reference value : entry) {
                    this.entry.add(value);
                }
                return this;
            }

            /**
             * A reference to the actual resource from which the narrative in the section is derived.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param entry
             *     A reference to data that supports this section
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder entry(Collection<Reference> entry) {
                this.entry = new ArrayList<>(entry);
                return this;
            }

            /**
             * If the section is empty, why the list is empty. An empty section typically has some text explaining the empty reason.
             * 
             * @param emptyReason
             *     Why the section is empty
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder emptyReason(CodeableConcept emptyReason) {
                this.emptyReason = emptyReason;
                return this;
            }

            /**
             * A nested sub-section within this section.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param section
             *     Nested Section
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder section(Composition.Section... section) {
                for (Composition.Section value : section) {
                    this.section.add(value);
                }
                return this;
            }

            /**
             * A nested sub-section within this section.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param section
             *     Nested Section
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder section(Collection<Composition.Section> section) {
                this.section = new ArrayList<>(section);
                return this;
            }

            /**
             * Build the {@link Section}
             * 
             * @return
             *     An immutable object of type {@link Section}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Section per the base specification
             */
            @Override
            public Section build() {
                Section section = new Section(this);
                if (validating) {
                    validate(section);
                }
                return section;
            }

            protected void validate(Section section) {
                super.validate(section);
                ValidationSupport.checkList(section.author, "author", Reference.class);
                ValidationSupport.checkList(section.entry, "entry", Reference.class);
                ValidationSupport.checkList(section.section, "section", Composition.Section.class);
                ValidationSupport.checkReferenceType(section.author, "author", "Practitioner", "PractitionerRole", "Device", "Patient", "RelatedPerson", "Organization");
                ValidationSupport.requireValueOrChildren(section);
            }

            protected Builder from(Section section) {
                super.from(section);
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
