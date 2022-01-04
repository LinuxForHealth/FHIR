/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ListMode;
import com.ibm.fhir.model.type.code.ListStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A list is a curated collection of resources.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "lst-1",
    level = "Rule",
    location = "(base)",
    description = "A list can only have an emptyReason if it is empty",
    expression = "emptyReason.empty() or entry.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/List"
)
@Constraint(
    id = "lst-2",
    level = "Rule",
    location = "(base)",
    description = "The deleted flag can only be used if the mode of the list is \"changes\"",
    expression = "mode = 'changes' or entry.deleted.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/List"
)
@Constraint(
    id = "lst-3",
    level = "Rule",
    location = "(base)",
    description = "An entry date can only be used if the mode of the list is \"working\"",
    expression = "mode = 'working' or entry.date.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/List"
)
@Constraint(
    id = "list-4",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/list-order",
    expression = "orderedBy.exists() implies (orderedBy.memberOf('http://hl7.org/fhir/ValueSet/list-order', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/List",
    generated = true
)
@Constraint(
    id = "list-5",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/list-empty-reason",
    expression = "emptyReason.exists() implies (emptyReason.memberOf('http://hl7.org/fhir/ValueSet/list-empty-reason', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/List",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class List extends DomainResource {
    private final java.util.List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "ListStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The current state of the list.",
        valueSet = "http://hl7.org/fhir/ValueSet/list-status|4.3.0-CIBUILD"
    )
    @Required
    private final ListStatus status;
    @Summary
    @Binding(
        bindingName = "ListMode",
        strength = BindingStrength.Value.REQUIRED,
        description = "The processing mode that applies to this list.",
        valueSet = "http://hl7.org/fhir/ValueSet/list-mode|4.3.0-CIBUILD"
    )
    @Required
    private final ListMode mode;
    @Summary
    private final String title;
    @Summary
    @Binding(
        bindingName = "ListPurpose",
        strength = BindingStrength.Value.EXAMPLE,
        description = "What the purpose of a list is.",
        valueSet = "http://hl7.org/fhir/ValueSet/list-example-codes"
    )
    private final CodeableConcept code;
    @Summary
    @ReferenceTarget({ "Patient", "Group", "Device", "Location" })
    private final Reference subject;
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Summary
    private final DateTime date;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Patient", "Device" })
    private final Reference source;
    @Binding(
        bindingName = "ListOrder",
        strength = BindingStrength.Value.PREFERRED,
        description = "What order applies to the items in a list.",
        valueSet = "http://hl7.org/fhir/ValueSet/list-order"
    )
    private final CodeableConcept orderedBy;
    private final java.util.List<Annotation> note;
    private final java.util.List<Entry> entry;
    @Binding(
        bindingName = "ListEmptyReason",
        strength = BindingStrength.Value.PREFERRED,
        description = "If a list is empty, why it is empty.",
        valueSet = "http://hl7.org/fhir/ValueSet/list-empty-reason"
    )
    private final CodeableConcept emptyReason;

    private List(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        mode = builder.mode;
        title = builder.title;
        code = builder.code;
        subject = builder.subject;
        encounter = builder.encounter;
        date = builder.date;
        source = builder.source;
        orderedBy = builder.orderedBy;
        note = Collections.unmodifiableList(builder.note);
        entry = Collections.unmodifiableList(builder.entry);
        emptyReason = builder.emptyReason;
    }

    /**
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public java.util.List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Indicates the current state of this list.
     * 
     * @return
     *     An immutable object of type {@link ListStatus} that is non-null.
     */
    public ListStatus getStatus() {
        return status;
    }

    /**
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, 
     * or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items 
     * may be marked as added, modified or deleted.
     * 
     * @return
     *     An immutable object of type {@link ListMode} that is non-null.
     */
    public ListMode getMode() {
        return mode;
    }

    /**
     * A label for the list assigned by the author.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * This code defines the purpose of the list - why it was created.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * The common subject (or patient) of the resources that are in the list if there is one.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The encounter that is the context in which this list was created.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * The date that the list was prepared.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is 
     * the same as the author of the list.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSource() {
        return source;
    }

    /**
     * What order applies to the items in the list.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getOrderedBy() {
        return orderedBy;
    }

    /**
     * Comments that apply to the overall list.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public java.util.List<Annotation> getNote() {
        return note;
    }

    /**
     * Entries in this list.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Entry} that may be empty.
     */
    public java.util.List<Entry> getEntry() {
        return entry;
    }

    /**
     * If the list is empty, why the list is empty.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getEmptyReason() {
        return emptyReason;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (mode != null) || 
            (title != null) || 
            (code != null) || 
            (subject != null) || 
            (encounter != null) || 
            (date != null) || 
            (source != null) || 
            (orderedBy != null) || 
            !note.isEmpty() || 
            !entry.isEmpty() || 
            (emptyReason != null);
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
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(status, "status", visitor);
                accept(mode, "mode", visitor);
                accept(title, "title", visitor);
                accept(code, "code", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(date, "date", visitor);
                accept(source, "source", visitor);
                accept(orderedBy, "orderedBy", visitor);
                accept(note, "note", visitor, Annotation.class);
                accept(entry, "entry", visitor, Entry.class);
                accept(emptyReason, "emptyReason", visitor);
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
        List other = (List) obj;
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
            Objects.equals(mode, other.mode) && 
            Objects.equals(title, other.title) && 
            Objects.equals(code, other.code) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(date, other.date) && 
            Objects.equals(source, other.source) && 
            Objects.equals(orderedBy, other.orderedBy) && 
            Objects.equals(note, other.note) && 
            Objects.equals(entry, other.entry) && 
            Objects.equals(emptyReason, other.emptyReason);
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
                mode, 
                title, 
                code, 
                subject, 
                encounter, 
                date, 
                source, 
                orderedBy, 
                note, 
                entry, 
                emptyReason);
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
        private java.util.List<Identifier> identifier = new ArrayList<>();
        private ListStatus status;
        private ListMode mode;
        private String title;
        private CodeableConcept code;
        private Reference subject;
        private Reference encounter;
        private DateTime date;
        private Reference source;
        private CodeableConcept orderedBy;
        private java.util.List<Annotation> note = new ArrayList<>();
        private java.util.List<Entry> entry = new ArrayList<>();
        private CodeableConcept emptyReason;

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
         * Identifier for the List assigned for business purposes outside the context of FHIR.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier
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
         * Identifier for the List assigned for business purposes outside the context of FHIR.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business identifier
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
         * Indicates the current state of this list.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     current | retired | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ListStatus status) {
            this.status = status;
            return this;
        }

        /**
         * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, 
         * or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items 
         * may be marked as added, modified or deleted.
         * 
         * <p>This element is required.
         * 
         * @param mode
         *     working | snapshot | changes
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder mode(ListMode mode) {
            this.mode = mode;
            return this;
        }

        /**
         * Convenience method for setting {@code title}.
         * 
         * @param title
         *     Descriptive name for the list
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
         * A label for the list assigned by the author.
         * 
         * @param title
         *     Descriptive name for the list
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * This code defines the purpose of the list - why it was created.
         * 
         * @param code
         *     What the purpose of this list is
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * The common subject (or patient) of the resources that are in the list if there is one.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * <li>{@link Device}</li>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param subject
         *     If all resources have the same subject
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The encounter that is the context in which this list was created.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
         * 
         * @param encounter
         *     Context in which list created
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * The date that the list was prepared.
         * 
         * @param date
         *     When the list was prepared
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is 
         * the same as the author of the list.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param source
         *     Who and/or what defined the list contents (aka Author)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Reference source) {
            this.source = source;
            return this;
        }

        /**
         * What order applies to the items in the list.
         * 
         * @param orderedBy
         *     What order the list has
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder orderedBy(CodeableConcept orderedBy) {
            this.orderedBy = orderedBy;
            return this;
        }

        /**
         * Comments that apply to the overall list.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments about the list
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
         * Comments that apply to the overall list.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments about the list
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
         * Entries in this list.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param entry
         *     Entries in the list
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder entry(Entry... entry) {
            for (Entry value : entry) {
                this.entry.add(value);
            }
            return this;
        }

        /**
         * Entries in this list.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param entry
         *     Entries in the list
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder entry(Collection<Entry> entry) {
            this.entry = new ArrayList<>(entry);
            return this;
        }

        /**
         * If the list is empty, why the list is empty.
         * 
         * @param emptyReason
         *     Why list is empty
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder emptyReason(CodeableConcept emptyReason) {
            this.emptyReason = emptyReason;
            return this;
        }

        /**
         * Build the {@link List}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>mode</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link List}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid List per the base specification
         */
        @Override
        public List build() {
            List list = new List(this);
            if (validating) {
                validate(list);
            }
            return list;
        }

        protected void validate(List list) {
            super.validate(list);
            ValidationSupport.checkList(list.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(list.status, "status");
            ValidationSupport.requireNonNull(list.mode, "mode");
            ValidationSupport.checkList(list.note, "note", Annotation.class);
            ValidationSupport.checkList(list.entry, "entry", Entry.class);
            ValidationSupport.checkReferenceType(list.subject, "subject", "Patient", "Group", "Device", "Location");
            ValidationSupport.checkReferenceType(list.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(list.source, "source", "Practitioner", "PractitionerRole", "Patient", "Device");
        }

        protected Builder from(List list) {
            super.from(list);
            identifier.addAll(list.identifier);
            status = list.status;
            mode = list.mode;
            title = list.title;
            code = list.code;
            subject = list.subject;
            encounter = list.encounter;
            date = list.date;
            source = list.source;
            orderedBy = list.orderedBy;
            note.addAll(list.note);
            entry.addAll(list.entry);
            emptyReason = list.emptyReason;
            return this;
        }
    }

    /**
     * Entries in this list.
     */
    public static class Entry extends BackboneElement {
        @Binding(
            bindingName = "ListItemFlag",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes that provide further information about the reason and meaning of the item in the list.",
            valueSet = "http://hl7.org/fhir/ValueSet/list-item-flag"
        )
        private final CodeableConcept flag;
        private final Boolean deleted;
        private final DateTime date;
        @Required
        private final Reference item;

        private Entry(Builder builder) {
            super(builder);
            flag = builder.flag;
            deleted = builder.deleted;
            date = builder.date;
            item = builder.item;
        }

        /**
         * The flag allows the system constructing the list to indicate the role and significance of the item in the list.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getFlag() {
            return flag;
        }

        /**
         * True if this item is marked as deleted in the list.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getDeleted() {
            return deleted;
        }

        /**
         * When this item was added to the list.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getDate() {
            return date;
        }

        /**
         * A reference to the actual resource from which data was derived.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getItem() {
            return item;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (flag != null) || 
                (deleted != null) || 
                (date != null) || 
                (item != null);
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
                    accept(flag, "flag", visitor);
                    accept(deleted, "deleted", visitor);
                    accept(date, "date", visitor);
                    accept(item, "item", visitor);
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
            Entry other = (Entry) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(flag, other.flag) && 
                Objects.equals(deleted, other.deleted) && 
                Objects.equals(date, other.date) && 
                Objects.equals(item, other.item);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    flag, 
                    deleted, 
                    date, 
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
            private CodeableConcept flag;
            private Boolean deleted;
            private DateTime date;
            private Reference item;

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
             * The flag allows the system constructing the list to indicate the role and significance of the item in the list.
             * 
             * @param flag
             *     Status/Workflow information about this item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder flag(CodeableConcept flag) {
                this.flag = flag;
                return this;
            }

            /**
             * Convenience method for setting {@code deleted}.
             * 
             * @param deleted
             *     If this item is actually marked as deleted
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #deleted(com.ibm.fhir.model.type.Boolean)
             */
            public Builder deleted(java.lang.Boolean deleted) {
                this.deleted = (deleted == null) ? null : Boolean.of(deleted);
                return this;
            }

            /**
             * True if this item is marked as deleted in the list.
             * 
             * @param deleted
             *     If this item is actually marked as deleted
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder deleted(Boolean deleted) {
                this.deleted = deleted;
                return this;
            }

            /**
             * When this item was added to the list.
             * 
             * @param date
             *     When item added to list
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder date(DateTime date) {
                this.date = date;
                return this;
            }

            /**
             * A reference to the actual resource from which data was derived.
             * 
             * <p>This element is required.
             * 
             * @param item
             *     Actual entry
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(Reference item) {
                this.item = item;
                return this;
            }

            /**
             * Build the {@link Entry}
             * 
             * <p>Required elements:
             * <ul>
             * <li>item</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Entry}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Entry per the base specification
             */
            @Override
            public Entry build() {
                Entry entry = new Entry(this);
                if (validating) {
                    validate(entry);
                }
                return entry;
            }

            protected void validate(Entry entry) {
                super.validate(entry);
                ValidationSupport.requireNonNull(entry.item, "item");
                ValidationSupport.requireValueOrChildren(entry);
            }

            protected Builder from(Entry entry) {
                super.from(entry);
                flag = entry.flag;
                deleted = entry.deleted;
                date = entry.date;
                item = entry.item;
                return this;
            }
        }
    }
}
