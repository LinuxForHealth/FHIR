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
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.FamilyHistoryStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Significant health conditions for a person related to the patient relevant in the context of care for the patient.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "fhs-1",
    level = "Rule",
    location = "(base)",
    description = "Can have age[x] or born[x], but not both",
    expression = "age.empty() or born.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/FamilyMemberHistory"
)
@Constraint(
    id = "fhs-2",
    level = "Rule",
    location = "(base)",
    description = "Can only have estimatedAge if age[x] is present",
    expression = "age.exists() or estimatedAge.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/FamilyMemberHistory"
)
@Constraint(
    id = "familyMemberHistory-3",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/administrative-gender",
    expression = "sex.exists() implies (sex.memberOf('http://hl7.org/fhir/ValueSet/administrative-gender', 'extensible'))",
    source = "http://hl7.org/fhir/StructureDefinition/FamilyMemberHistory",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class FamilyMemberHistory extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final List<Canonical> instantiatesCanonical;
    @Summary
    private final List<Uri> instantiatesUri;
    @Summary
    @Binding(
        bindingName = "FamilyHistoryStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A code that identifies the status of the family history record.",
        valueSet = "http://hl7.org/fhir/ValueSet/history-status|4.3.0-CIBUILD"
    )
    @Required
    private final FamilyHistoryStatus status;
    @Summary
    @Binding(
        bindingName = "FamilyHistoryAbsentReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes describing the reason why a family member's history is not available.",
        valueSet = "http://hl7.org/fhir/ValueSet/history-absent-reason"
    )
    private final CodeableConcept dataAbsentReason;
    @Summary
    @ReferenceTarget({ "Patient" })
    @Required
    private final Reference patient;
    @Summary
    private final DateTime date;
    @Summary
    private final String name;
    @Summary
    @Binding(
        bindingName = "FamilialRelationship",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The nature of the relationship between the patient and the related person being described in the family member history.",
        valueSet = "http://terminology.hl7.org/ValueSet/v3-FamilyMember"
    )
    @Required
    private final CodeableConcept relationship;
    @Summary
    @Binding(
        bindingName = "Sex",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Codes describing the sex assigned at birth as documented on the birth registration.",
        valueSet = "http://hl7.org/fhir/ValueSet/administrative-gender"
    )
    private final CodeableConcept sex;
    @Choice({ Period.class, Date.class, String.class })
    private final Element born;
    @Summary
    @Choice({ Age.class, Range.class, String.class })
    private final Element age;
    @Summary
    private final Boolean estimatedAge;
    @Summary
    @Choice({ Boolean.class, Age.class, Range.class, Date.class, String.class })
    private final Element deceased;
    @Summary
    @Binding(
        bindingName = "FamilyHistoryReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes indicating why the family member history was done.",
        valueSet = "http://hl7.org/fhir/ValueSet/clinical-findings"
    )
    private final List<CodeableConcept> reasonCode;
    @Summary
    @ReferenceTarget({ "Condition", "Observation", "AllergyIntolerance", "QuestionnaireResponse", "DiagnosticReport", "DocumentReference" })
    private final List<Reference> reasonReference;
    private final List<Annotation> note;
    private final List<Condition> condition;

    private FamilyMemberHistory(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiatesCanonical = Collections.unmodifiableList(builder.instantiatesCanonical);
        instantiatesUri = Collections.unmodifiableList(builder.instantiatesUri);
        status = builder.status;
        dataAbsentReason = builder.dataAbsentReason;
        patient = builder.patient;
        date = builder.date;
        name = builder.name;
        relationship = builder.relationship;
        sex = builder.sex;
        born = builder.born;
        age = builder.age;
        estimatedAge = builder.estimatedAge;
        deceased = builder.deceased;
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        note = Collections.unmodifiableList(builder.note);
        condition = Collections.unmodifiableList(builder.condition);
    }

    /**
     * Business identifiers assigned to this family member history by the performer or other systems which remain constant as 
     * the resource is updated and propagates from server to server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
     * part by this FamilyMemberHistory.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
     * whole or in part by this FamilyMemberHistory.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri} that may be empty.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * A code specifying the status of the record of the family history of a specific family member.
     * 
     * @return
     *     An immutable object of type {@link FamilyHistoryStatus} that is non-null.
     */
    public FamilyHistoryStatus getStatus() {
        return status;
    }

    /**
     * Describes why the family member's history is not available.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getDataAbsentReason() {
        return dataAbsentReason;
    }

    /**
     * The person who this history concerns.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * The date (and possibly time) when the family member history was recorded or last updated.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * This will either be a name or a description; e.g. "Aunt Susan", "my cousin with the red hair".
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * The type of relationship this person has to the patient (father, mother, brother etc.).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getRelationship() {
        return relationship;
    }

    /**
     * The birth sex of the family member.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSex() {
        return sex;
    }

    /**
     * The actual or approximate date of birth of the relative.
     * 
     * @return
     *     An immutable object of type {@link Period}, {@link Date} or {@link String} that may be null.
     */
    public Element getBorn() {
        return born;
    }

    /**
     * The age of the relative at the time the family member history is recorded.
     * 
     * @return
     *     An immutable object of type {@link Age}, {@link Range} or {@link String} that may be null.
     */
    public Element getAge() {
        return age;
    }

    /**
     * If true, indicates that the age value specified is an estimated value.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getEstimatedAge() {
        return estimatedAge;
    }

    /**
     * Deceased flag or the actual or approximate age of the relative at the time of death for the family member history 
     * record.
     * 
     * @return
     *     An immutable object of type {@link Boolean}, {@link Age}, {@link Range}, {@link Date} or {@link String} that may be 
     *     null.
     */
    public Element getDeceased() {
        return deceased;
    }

    /**
     * Describes why the family member history occurred in coded or textual form.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Indicates a Condition, Observation, AllergyIntolerance, or QuestionnaireResponse that justifies this family member 
     * history event.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in 
     * the condition property, but this is not always possible.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to 
     * represent more than one condition per resource, though there is nothing stopping multiple resources - one per 
     * condition.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Condition} that may be empty.
     */
    public List<Condition> getCondition() {
        return condition;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !instantiatesCanonical.isEmpty() || 
            !instantiatesUri.isEmpty() || 
            (status != null) || 
            (dataAbsentReason != null) || 
            (patient != null) || 
            (date != null) || 
            (name != null) || 
            (relationship != null) || 
            (sex != null) || 
            (born != null) || 
            (age != null) || 
            (estimatedAge != null) || 
            (deceased != null) || 
            !reasonCode.isEmpty() || 
            !reasonReference.isEmpty() || 
            !note.isEmpty() || 
            !condition.isEmpty();
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
                accept(instantiatesCanonical, "instantiatesCanonical", visitor, Canonical.class);
                accept(instantiatesUri, "instantiatesUri", visitor, Uri.class);
                accept(status, "status", visitor);
                accept(dataAbsentReason, "dataAbsentReason", visitor);
                accept(patient, "patient", visitor);
                accept(date, "date", visitor);
                accept(name, "name", visitor);
                accept(relationship, "relationship", visitor);
                accept(sex, "sex", visitor);
                accept(born, "born", visitor);
                accept(age, "age", visitor);
                accept(estimatedAge, "estimatedAge", visitor);
                accept(deceased, "deceased", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(condition, "condition", visitor, Condition.class);
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
        FamilyMemberHistory other = (FamilyMemberHistory) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(instantiatesCanonical, other.instantiatesCanonical) && 
            Objects.equals(instantiatesUri, other.instantiatesUri) && 
            Objects.equals(status, other.status) && 
            Objects.equals(dataAbsentReason, other.dataAbsentReason) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(date, other.date) && 
            Objects.equals(name, other.name) && 
            Objects.equals(relationship, other.relationship) && 
            Objects.equals(sex, other.sex) && 
            Objects.equals(born, other.born) && 
            Objects.equals(age, other.age) && 
            Objects.equals(estimatedAge, other.estimatedAge) && 
            Objects.equals(deceased, other.deceased) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(note, other.note) && 
            Objects.equals(condition, other.condition);
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
                instantiatesCanonical, 
                instantiatesUri, 
                status, 
                dataAbsentReason, 
                patient, 
                date, 
                name, 
                relationship, 
                sex, 
                born, 
                age, 
                estimatedAge, 
                deceased, 
                reasonCode, 
                reasonReference, 
                note, 
                condition);
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
        private List<Identifier> identifier = new ArrayList<>();
        private List<Canonical> instantiatesCanonical = new ArrayList<>();
        private List<Uri> instantiatesUri = new ArrayList<>();
        private FamilyHistoryStatus status;
        private CodeableConcept dataAbsentReason;
        private Reference patient;
        private DateTime date;
        private String name;
        private CodeableConcept relationship;
        private CodeableConcept sex;
        private Element born;
        private Element age;
        private Boolean estimatedAge;
        private Element deceased;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Condition> condition = new ArrayList<>();

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
         * Business identifiers assigned to this family member history by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External Id(s) for this record
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
         * Business identifiers assigned to this family member history by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External Id(s) for this record
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
         * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
         * part by this FamilyMemberHistory.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesCanonical(Canonical... instantiatesCanonical) {
            for (Canonical value : instantiatesCanonical) {
                this.instantiatesCanonical.add(value);
            }
            return this;
        }

        /**
         * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
         * part by this FamilyMemberHistory.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
            this.instantiatesCanonical = new ArrayList<>(instantiatesCanonical);
            return this;
        }

        /**
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this FamilyMemberHistory.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesUri(Uri... instantiatesUri) {
            for (Uri value : instantiatesUri) {
                this.instantiatesUri.add(value);
            }
            return this;
        }

        /**
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this FamilyMemberHistory.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
            this.instantiatesUri = new ArrayList<>(instantiatesUri);
            return this;
        }

        /**
         * A code specifying the status of the record of the family history of a specific family member.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     partial | completed | entered-in-error | health-unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(FamilyHistoryStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Describes why the family member's history is not available.
         * 
         * @param dataAbsentReason
         *     subject-unknown | withheld | unable-to-obtain | deferred
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dataAbsentReason(CodeableConcept dataAbsentReason) {
            this.dataAbsentReason = dataAbsentReason;
            return this;
        }

        /**
         * The person who this history concerns.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     Patient history is about
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * The date (and possibly time) when the family member history was recorded or last updated.
         * 
         * @param date
         *     When history was recorded or last updated
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     The family member described
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
         * This will either be a name or a description; e.g. "Aunt Susan", "my cousin with the red hair".
         * 
         * @param name
         *     The family member described
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * The type of relationship this person has to the patient (father, mother, brother etc.).
         * 
         * <p>This element is required.
         * 
         * @param relationship
         *     Relationship to the subject
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relationship(CodeableConcept relationship) {
            this.relationship = relationship;
            return this;
        }

        /**
         * The birth sex of the family member.
         * 
         * @param sex
         *     male | female | other | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sex(CodeableConcept sex) {
            this.sex = sex;
            return this;
        }

        /**
         * Convenience method for setting {@code born} with choice type Date.
         * 
         * @param born
         *     (approximate) date of birth
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #born(Element)
         */
        public Builder born(java.time.LocalDate born) {
            this.born = (born == null) ? null : Date.of(born);
            return this;
        }

        /**
         * Convenience method for setting {@code born} with choice type String.
         * 
         * @param born
         *     (approximate) date of birth
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #born(Element)
         */
        public Builder born(java.lang.String born) {
            this.born = (born == null) ? null : String.of(born);
            return this;
        }

        /**
         * The actual or approximate date of birth of the relative.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Period}</li>
         * <li>{@link Date}</li>
         * <li>{@link String}</li>
         * </ul>
         * 
         * @param born
         *     (approximate) date of birth
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder born(Element born) {
            this.born = born;
            return this;
        }

        /**
         * Convenience method for setting {@code age} with choice type String.
         * 
         * @param age
         *     (approximate) age
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #age(Element)
         */
        public Builder age(java.lang.String age) {
            this.age = (age == null) ? null : String.of(age);
            return this;
        }

        /**
         * The age of the relative at the time the family member history is recorded.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Age}</li>
         * <li>{@link Range}</li>
         * <li>{@link String}</li>
         * </ul>
         * 
         * @param age
         *     (approximate) age
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder age(Element age) {
            this.age = age;
            return this;
        }

        /**
         * Convenience method for setting {@code estimatedAge}.
         * 
         * @param estimatedAge
         *     Age is estimated?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #estimatedAge(com.ibm.fhir.model.type.Boolean)
         */
        public Builder estimatedAge(java.lang.Boolean estimatedAge) {
            this.estimatedAge = (estimatedAge == null) ? null : Boolean.of(estimatedAge);
            return this;
        }

        /**
         * If true, indicates that the age value specified is an estimated value.
         * 
         * @param estimatedAge
         *     Age is estimated?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder estimatedAge(Boolean estimatedAge) {
            this.estimatedAge = estimatedAge;
            return this;
        }

        /**
         * Convenience method for setting {@code deceased} with choice type Boolean.
         * 
         * @param deceased
         *     Dead? How old/when?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #deceased(Element)
         */
        public Builder deceased(java.lang.Boolean deceased) {
            this.deceased = (deceased == null) ? null : Boolean.of(deceased);
            return this;
        }

        /**
         * Convenience method for setting {@code deceased} with choice type Date.
         * 
         * @param deceased
         *     Dead? How old/when?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #deceased(Element)
         */
        public Builder deceased(java.time.LocalDate deceased) {
            this.deceased = (deceased == null) ? null : Date.of(deceased);
            return this;
        }

        /**
         * Convenience method for setting {@code deceased} with choice type String.
         * 
         * @param deceased
         *     Dead? How old/when?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #deceased(Element)
         */
        public Builder deceased(java.lang.String deceased) {
            this.deceased = (deceased == null) ? null : String.of(deceased);
            return this;
        }

        /**
         * Deceased flag or the actual or approximate age of the relative at the time of death for the family member history 
         * record.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Boolean}</li>
         * <li>{@link Age}</li>
         * <li>{@link Range}</li>
         * <li>{@link Date}</li>
         * <li>{@link String}</li>
         * </ul>
         * 
         * @param deceased
         *     Dead? How old/when?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder deceased(Element deceased) {
            this.deceased = deceased;
            return this;
        }

        /**
         * Describes why the family member history occurred in coded or textual form.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Why was family member history performed?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonCode(CodeableConcept... reasonCode) {
            for (CodeableConcept value : reasonCode) {
                this.reasonCode.add(value);
            }
            return this;
        }

        /**
         * Describes why the family member history occurred in coded or textual form.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Why was family member history performed?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode = new ArrayList<>(reasonCode);
            return this;
        }

        /**
         * Indicates a Condition, Observation, AllergyIntolerance, or QuestionnaireResponse that justifies this family member 
         * history event.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link AllergyIntolerance}</li>
         * <li>{@link QuestionnaireResponse}</li>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Why was family member history performed?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonReference(Reference... reasonReference) {
            for (Reference value : reasonReference) {
                this.reasonReference.add(value);
            }
            return this;
        }

        /**
         * Indicates a Condition, Observation, AllergyIntolerance, or QuestionnaireResponse that justifies this family member 
         * history event.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link AllergyIntolerance}</li>
         * <li>{@link QuestionnaireResponse}</li>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Why was family member history performed?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference = new ArrayList<>(reasonReference);
            return this;
        }

        /**
         * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in 
         * the condition property, but this is not always possible.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     General note about related person
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
         * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in 
         * the condition property, but this is not always possible.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     General note about related person
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
         * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to 
         * represent more than one condition per resource, though there is nothing stopping multiple resources - one per 
         * condition.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param condition
         *     Condition that the related person had
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder condition(Condition... condition) {
            for (Condition value : condition) {
                this.condition.add(value);
            }
            return this;
        }

        /**
         * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to 
         * represent more than one condition per resource, though there is nothing stopping multiple resources - one per 
         * condition.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param condition
         *     Condition that the related person had
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder condition(Collection<Condition> condition) {
            this.condition = new ArrayList<>(condition);
            return this;
        }

        /**
         * Build the {@link FamilyMemberHistory}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>patient</li>
         * <li>relationship</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link FamilyMemberHistory}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid FamilyMemberHistory per the base specification
         */
        @Override
        public FamilyMemberHistory build() {
            FamilyMemberHistory familyMemberHistory = new FamilyMemberHistory(this);
            if (validating) {
                validate(familyMemberHistory);
            }
            return familyMemberHistory;
        }

        protected void validate(FamilyMemberHistory familyMemberHistory) {
            super.validate(familyMemberHistory);
            ValidationSupport.checkList(familyMemberHistory.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(familyMemberHistory.instantiatesCanonical, "instantiatesCanonical", Canonical.class);
            ValidationSupport.checkList(familyMemberHistory.instantiatesUri, "instantiatesUri", Uri.class);
            ValidationSupport.requireNonNull(familyMemberHistory.status, "status");
            ValidationSupport.requireNonNull(familyMemberHistory.patient, "patient");
            ValidationSupport.requireNonNull(familyMemberHistory.relationship, "relationship");
            ValidationSupport.choiceElement(familyMemberHistory.born, "born", Period.class, Date.class, String.class);
            ValidationSupport.choiceElement(familyMemberHistory.age, "age", Age.class, Range.class, String.class);
            ValidationSupport.choiceElement(familyMemberHistory.deceased, "deceased", Boolean.class, Age.class, Range.class, Date.class, String.class);
            ValidationSupport.checkList(familyMemberHistory.reasonCode, "reasonCode", CodeableConcept.class);
            ValidationSupport.checkList(familyMemberHistory.reasonReference, "reasonReference", Reference.class);
            ValidationSupport.checkList(familyMemberHistory.note, "note", Annotation.class);
            ValidationSupport.checkList(familyMemberHistory.condition, "condition", Condition.class);
            ValidationSupport.checkReferenceType(familyMemberHistory.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(familyMemberHistory.reasonReference, "reasonReference", "Condition", "Observation", "AllergyIntolerance", "QuestionnaireResponse", "DiagnosticReport", "DocumentReference");
        }

        protected Builder from(FamilyMemberHistory familyMemberHistory) {
            super.from(familyMemberHistory);
            identifier.addAll(familyMemberHistory.identifier);
            instantiatesCanonical.addAll(familyMemberHistory.instantiatesCanonical);
            instantiatesUri.addAll(familyMemberHistory.instantiatesUri);
            status = familyMemberHistory.status;
            dataAbsentReason = familyMemberHistory.dataAbsentReason;
            patient = familyMemberHistory.patient;
            date = familyMemberHistory.date;
            name = familyMemberHistory.name;
            relationship = familyMemberHistory.relationship;
            sex = familyMemberHistory.sex;
            born = familyMemberHistory.born;
            age = familyMemberHistory.age;
            estimatedAge = familyMemberHistory.estimatedAge;
            deceased = familyMemberHistory.deceased;
            reasonCode.addAll(familyMemberHistory.reasonCode);
            reasonReference.addAll(familyMemberHistory.reasonReference);
            note.addAll(familyMemberHistory.note);
            condition.addAll(familyMemberHistory.condition);
            return this;
        }
    }

    /**
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to 
     * represent more than one condition per resource, though there is nothing stopping multiple resources - one per 
     * condition.
     */
    public static class Condition extends BackboneElement {
        @Binding(
            bindingName = "ConditionCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Identification of the Condition or diagnosis.",
            valueSet = "http://hl7.org/fhir/ValueSet/condition-code"
        )
        @Required
        private final CodeableConcept code;
        @Binding(
            bindingName = "ConditionOutcome",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The result of the condition for the patient; e.g. death, permanent disability, temporary disability, etc.",
            valueSet = "http://hl7.org/fhir/ValueSet/condition-outcome"
        )
        private final CodeableConcept outcome;
        private final Boolean contributedToDeath;
        @Choice({ Age.class, Range.class, Period.class, String.class })
        private final Element onset;
        private final List<Annotation> note;

        private Condition(Builder builder) {
            super(builder);
            code = builder.code;
            outcome = builder.outcome;
            contributedToDeath = builder.contributedToDeath;
            onset = builder.onset;
            note = Collections.unmodifiableList(builder.note);
        }

        /**
         * The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 
         * 'cancer' depending on how much is known about the condition and the capabilities of the creating system.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * Indicates what happened following the condition. If the condition resulted in death, deceased date is captured on the 
         * relation.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getOutcome() {
            return outcome;
        }

        /**
         * This condition contributed to the cause of death of the related person. If contributedToDeath is not populated, then 
         * it is unknown.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getContributedToDeath() {
            return contributedToDeath;
        }

        /**
         * Either the age of onset, range of approximate age or descriptive string can be recorded. For conditions with multiple 
         * occurrences, this describes the first known occurrence.
         * 
         * @return
         *     An immutable object of type {@link Age}, {@link Range}, {@link Period} or {@link String} that may be null.
         */
        public Element getOnset() {
            return onset;
        }

        /**
         * An area where general notes can be placed about this specific condition.
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
                (code != null) || 
                (outcome != null) || 
                (contributedToDeath != null) || 
                (onset != null) || 
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
                    accept(code, "code", visitor);
                    accept(outcome, "outcome", visitor);
                    accept(contributedToDeath, "contributedToDeath", visitor);
                    accept(onset, "onset", visitor);
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
            Condition other = (Condition) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(outcome, other.outcome) && 
                Objects.equals(contributedToDeath, other.contributedToDeath) && 
                Objects.equals(onset, other.onset) && 
                Objects.equals(note, other.note);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    outcome, 
                    contributedToDeath, 
                    onset, 
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
            private CodeableConcept code;
            private CodeableConcept outcome;
            private Boolean contributedToDeath;
            private Element onset;
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
             * The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 
             * 'cancer' depending on how much is known about the condition and the capabilities of the creating system.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     Condition suffered by relation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Indicates what happened following the condition. If the condition resulted in death, deceased date is captured on the 
             * relation.
             * 
             * @param outcome
             *     deceased | permanent disability | etc.
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder outcome(CodeableConcept outcome) {
                this.outcome = outcome;
                return this;
            }

            /**
             * Convenience method for setting {@code contributedToDeath}.
             * 
             * @param contributedToDeath
             *     Whether the condition contributed to the cause of death
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #contributedToDeath(com.ibm.fhir.model.type.Boolean)
             */
            public Builder contributedToDeath(java.lang.Boolean contributedToDeath) {
                this.contributedToDeath = (contributedToDeath == null) ? null : Boolean.of(contributedToDeath);
                return this;
            }

            /**
             * This condition contributed to the cause of death of the related person. If contributedToDeath is not populated, then 
             * it is unknown.
             * 
             * @param contributedToDeath
             *     Whether the condition contributed to the cause of death
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder contributedToDeath(Boolean contributedToDeath) {
                this.contributedToDeath = contributedToDeath;
                return this;
            }

            /**
             * Convenience method for setting {@code onset} with choice type String.
             * 
             * @param onset
             *     When condition first manifested
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #onset(Element)
             */
            public Builder onset(java.lang.String onset) {
                this.onset = (onset == null) ? null : String.of(onset);
                return this;
            }

            /**
             * Either the age of onset, range of approximate age or descriptive string can be recorded. For conditions with multiple 
             * occurrences, this describes the first known occurrence.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Age}</li>
             * <li>{@link Range}</li>
             * <li>{@link Period}</li>
             * <li>{@link String}</li>
             * </ul>
             * 
             * @param onset
             *     When condition first manifested
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder onset(Element onset) {
                this.onset = onset;
                return this;
            }

            /**
             * An area where general notes can be placed about this specific condition.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Extra information about condition
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
             * An area where general notes can be placed about this specific condition.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Extra information about condition
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
             * Build the {@link Condition}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Condition}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Condition per the base specification
             */
            @Override
            public Condition build() {
                Condition condition = new Condition(this);
                if (validating) {
                    validate(condition);
                }
                return condition;
            }

            protected void validate(Condition condition) {
                super.validate(condition);
                ValidationSupport.requireNonNull(condition.code, "code");
                ValidationSupport.choiceElement(condition.onset, "onset", Age.class, Range.class, Period.class, String.class);
                ValidationSupport.checkList(condition.note, "note", Annotation.class);
                ValidationSupport.requireValueOrChildren(condition);
            }

            protected Builder from(Condition condition) {
                super.from(condition);
                code = condition.code;
                outcome = condition.outcome;
                contributedToDeath = condition.contributedToDeath;
                onset = condition.onset;
                note.addAll(condition.note);
                return this;
            }
        }
    }
}
