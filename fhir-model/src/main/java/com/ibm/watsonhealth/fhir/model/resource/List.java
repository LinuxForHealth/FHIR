/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.ListMode;
import com.ibm.watsonhealth.fhir.model.type.ListStatus;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A list is a curated collection of resources.
 * </p>
 */
@Constraint(
    id = "lst-1",
    level = "Rule",
    location = "(base)",
    description = "A list can only have an emptyReason if it is empty",
    expression = "emptyReason.empty() or entry.empty()"
)
@Constraint(
    id = "lst-2",
    level = "Rule",
    location = "(base)",
    description = "The deleted flag can only be used if the mode of the list is \"changes\"",
    expression = "mode = 'changes' or entry.deleted.empty()"
)
@Constraint(
    id = "lst-3",
    level = "Rule",
    location = "(base)",
    description = "An entry date can only be used if the mode of the list is \"working\"",
    expression = "mode = 'working' or entry.date.empty()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class List extends DomainResource {
    private final java.util.List<Identifier> identifier;
    private final ListStatus status;
    private final ListMode mode;
    private final String title;
    private final CodeableConcept code;
    private final Reference subject;
    private final Reference encounter;
    private final DateTime date;
    private final Reference source;
    private final CodeableConcept orderedBy;
    private final java.util.List<Annotation> note;
    private final java.util.List<Entry> entry;
    private final CodeableConcept emptyReason;

    private volatile int hashCode;

    private List(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        mode = ValidationSupport.requireNonNull(builder.mode, "mode");
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
     * <p>
     * Identifier for the List assigned for business purposes outside the context of FHIR.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
     */
    public java.util.List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * Indicates the current state of this list.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ListStatus}.
     */
    public ListStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * How this list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, 
     * or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items 
     * may be marked as added, modified or deleted.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ListMode}.
     */
    public ListMode getMode() {
        return mode;
    }

    /**
     * <p>
     * A label for the list assigned by the author.
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
     * This code defines the purpose of the list - why it was created.
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
     * The common subject (or patient) of the resources that are in the list if there is one.
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
     * The encounter that is the context in which this list was created.
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
     * The date that the list was prepared.
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
     * The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is 
     * the same as the author of the list.
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
     * What order applies to the items in the list.
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
     * Comments that apply to the overall list.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation}.
     */
    public java.util.List<Annotation> getNote() {
        return note;
    }

    /**
     * <p>
     * Entries in this list.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Entry}.
     */
    public java.util.List<Entry> getEntry() {
        return entry;
    }

    /**
     * <p>
     * If the list is empty, why the list is empty.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getEmptyReason() {
        return emptyReason;
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
        return new Builder(status, mode).from(this);
    }

    public Builder toBuilder(ListStatus status, ListMode mode) {
        return new Builder(status, mode).from(this);
    }

    public static Builder builder(ListStatus status, ListMode mode) {
        return new Builder(status, mode);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ListStatus status;
        private final ListMode mode;

        // optional
        private java.util.List<Identifier> identifier = new ArrayList<>();
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

        private Builder(ListStatus status, ListMode mode) {
            super();
            this.status = status;
            this.mode = mode;
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Identifier for the List assigned for business purposes outside the context of FHIR.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Identifier for the List assigned for business purposes outside the context of FHIR.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Business identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * <p>
         * A label for the list assigned by the author.
         * </p>
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
         * <p>
         * This code defines the purpose of the list - why it was created.
         * </p>
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
         * <p>
         * The common subject (or patient) of the resources that are in the list if there is one.
         * </p>
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
         * <p>
         * The encounter that is the context in which this list was created.
         * </p>
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
         * <p>
         * The date that the list was prepared.
         * </p>
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
         * <p>
         * The entity responsible for deciding what the contents of the list were. Where the list was created by a human, this is 
         * the same as the author of the list.
         * </p>
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
         * <p>
         * What order applies to the items in the list.
         * </p>
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
         * <p>
         * Comments that apply to the overall list.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Comments that apply to the overall list.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param note
         *     Comments about the list
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * <p>
         * Entries in this list.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Entries in this list.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param entry
         *     Entries in the list
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder entry(Collection<Entry> entry) {
            this.entry = new ArrayList<>(entry);
            return this;
        }

        /**
         * <p>
         * If the list is empty, why the list is empty.
         * </p>
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

        @Override
        public List build() {
            return new List(this);
        }

        private Builder from(List list) {
            id = list.id;
            meta = list.meta;
            implicitRules = list.implicitRules;
            language = list.language;
            text = list.text;
            contained.addAll(list.contained);
            extension.addAll(list.extension);
            modifierExtension.addAll(list.modifierExtension);
            identifier.addAll(list.identifier);
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
     * <p>
     * Entries in this list.
     * </p>
     */
    public static class Entry extends BackboneElement {
        private final CodeableConcept flag;
        private final Boolean deleted;
        private final DateTime date;
        private final Reference item;

        private volatile int hashCode;

        private Entry(Builder builder) {
            super(builder);
            flag = builder.flag;
            deleted = builder.deleted;
            date = builder.date;
            item = ValidationSupport.requireNonNull(builder.item, "item");
        }

        /**
         * <p>
         * The flag allows the system constructing the list to indicate the role and significance of the item in the list.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getFlag() {
            return flag;
        }

        /**
         * <p>
         * True if this item is marked as deleted in the list.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getDeleted() {
            return deleted;
        }

        /**
         * <p>
         * When this item was added to the list.
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
         * A reference to the actual resource from which data was derived.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getItem() {
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
                    accept(flag, "flag", visitor);
                    accept(deleted, "deleted", visitor);
                    accept(date, "date", visitor);
                    accept(item, "item", visitor);
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
            return new Builder(item).from(this);
        }

        public Builder toBuilder(Reference item) {
            return new Builder(item).from(this);
        }

        public static Builder builder(Reference item) {
            return new Builder(item);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference item;

            // optional
            private CodeableConcept flag;
            private Boolean deleted;
            private DateTime date;

            private Builder(Reference item) {
                super();
                this.item = item;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * The flag allows the system constructing the list to indicate the role and significance of the item in the list.
             * </p>
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
             * <p>
             * True if this item is marked as deleted in the list.
             * </p>
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
             * <p>
             * When this item was added to the list.
             * </p>
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

            @Override
            public Entry build() {
                return new Entry(this);
            }

            private Builder from(Entry entry) {
                id = entry.id;
                extension.addAll(entry.extension);
                modifierExtension.addAll(entry.modifierExtension);
                flag = entry.flag;
                deleted = entry.deleted;
                date = entry.date;
                return this;
            }
        }
    }
}
