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
import com.ibm.watsonhealth.fhir.model.type.Age;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.FamilyHistoryStatus;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Significant health conditions for a person related to the patient relevant in the context of care for the patient.
 * </p>
 */
@Constraint(
    id = "fhs-1",
    level = "Rule",
    location = "(base)",
    description = "Can have age[x] or born[x], but not both",
    expression = "age.empty() or born.empty()"
)
@Constraint(
    id = "fhs-2",
    level = "Rule",
    location = "(base)",
    description = "Can only have estimatedAge if age[x] is present",
    expression = "age.exists() or estimatedAge.empty()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class FamilyMemberHistory extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Canonical> instantiatesCanonical;
    private final List<Uri> instantiatesUri;
    private final FamilyHistoryStatus status;
    private final CodeableConcept dataAbsentReason;
    private final Reference patient;
    private final DateTime date;
    private final String name;
    private final CodeableConcept relationship;
    private final CodeableConcept sex;
    private final Element born;
    private final Element age;
    private final Boolean estimatedAge;
    private final Element deceased;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final List<Annotation> note;
    private final List<Condition> condition;

    private FamilyMemberHistory(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiatesCanonical = Collections.unmodifiableList(builder.instantiatesCanonical);
        instantiatesUri = Collections.unmodifiableList(builder.instantiatesUri);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        dataAbsentReason = builder.dataAbsentReason;
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        date = builder.date;
        name = builder.name;
        relationship = ValidationSupport.requireNonNull(builder.relationship, "relationship");
        sex = builder.sex;
        born = ValidationSupport.choiceElement(builder.born, "born", Period.class, Date.class, String.class);
        age = ValidationSupport.choiceElement(builder.age, "age", Age.class, Range.class, String.class);
        estimatedAge = builder.estimatedAge;
        deceased = ValidationSupport.choiceElement(builder.deceased, "deceased", Boolean.class, Age.class, Range.class, Date.class, String.class);
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        note = Collections.unmodifiableList(builder.note);
        condition = Collections.unmodifiableList(builder.condition);
    }

    /**
     * <p>
     * Business identifiers assigned to this family member history by the performer or other systems which remain constant as 
     * the resource is updated and propagates from server to server.
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
     * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
     * part by this FamilyMemberHistory.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * <p>
     * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
     * whole or in part by this FamilyMemberHistory.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * <p>
     * A code specifying the status of the record of the family history of a specific family member.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link FamilyHistoryStatus}.
     */
    public FamilyHistoryStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Describes why the family member's history is not available.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getDataAbsentReason() {
        return dataAbsentReason;
    }

    /**
     * <p>
     * The person who this history concerns.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * <p>
     * The date (and possibly time) when the family member history was recorded or last updated.
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
     * This will either be a name or a description; e.g. "Aunt Susan", "my cousin with the red hair".
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
     * The type of relationship this person has to the patient (father, mother, brother etc.).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getRelationship() {
        return relationship;
    }

    /**
     * <p>
     * The birth sex of the family member.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getSex() {
        return sex;
    }

    /**
     * <p>
     * The actual or approximate date of birth of the relative.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getBorn() {
        return born;
    }

    /**
     * <p>
     * The age of the relative at the time the family member history is recorded.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getAge() {
        return age;
    }

    /**
     * <p>
     * If true, indicates that the age value specified is an estimated value.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getEstimatedAge() {
        return estimatedAge;
    }

    /**
     * <p>
     * Deceased flag or the actual or approximate age of the relative at the time of death for the family member history 
     * record.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getDeceased() {
        return deceased;
    }

    /**
     * <p>
     * Describes why the family member history occurred in coded or textual form.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * <p>
     * Indicates a Condition, Observation, AllergyIntolerance, or QuestionnaireResponse that justifies this family member 
     * history event.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * <p>
     * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in 
     * the condition property, but this is not always possible.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * <p>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to 
     * represent more than one condition per resource, though there is nothing stopping multiple resources - one per 
     * condition.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Condition}.
     */
    public List<Condition> getCondition() {
        return condition;
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
                accept(instantiatesCanonical, "instantiatesCanonical", visitor, Canonical.class);
                accept(instantiatesUri, "instantiatesUri", visitor, Uri.class);
                accept(status, "status", visitor);
                accept(dataAbsentReason, "dataAbsentReason", visitor);
                accept(patient, "patient", visitor);
                accept(date, "date", visitor);
                accept(name, "name", visitor);
                accept(relationship, "relationship", visitor);
                accept(sex, "sex", visitor);
                accept(born, "born", visitor, true);
                accept(age, "age", visitor, true);
                accept(estimatedAge, "estimatedAge", visitor);
                accept(deceased, "deceased", visitor, true);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(condition, "condition", visitor, Condition.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status, patient, relationship).from(this);
    }

    public Builder toBuilder(FamilyHistoryStatus status, Reference patient, CodeableConcept relationship) {
        return new Builder(status, patient, relationship).from(this);
    }

    public static Builder builder(FamilyHistoryStatus status, Reference patient, CodeableConcept relationship) {
        return new Builder(status, patient, relationship);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final FamilyHistoryStatus status;
        private final Reference patient;
        private final CodeableConcept relationship;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Canonical> instantiatesCanonical = new ArrayList<>();
        private List<Uri> instantiatesUri = new ArrayList<>();
        private CodeableConcept dataAbsentReason;
        private DateTime date;
        private String name;
        private CodeableConcept sex;
        private Element born;
        private Element age;
        private Boolean estimatedAge;
        private Element deceased;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Condition> condition = new ArrayList<>();

        private Builder(FamilyHistoryStatus status, Reference patient, CodeableConcept relationship) {
            super();
            this.status = status;
            this.patient = patient;
            this.relationship = relationship;
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
         * Business identifiers assigned to this family member history by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     External Id(s) for this record
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
         * Business identifiers assigned to this family member history by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     External Id(s) for this record
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
         * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
         * part by this FamilyMemberHistory.
         * </p>
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesCanonical(Canonical... instantiatesCanonical) {
            for (Canonical value : instantiatesCanonical) {
                this.instantiatesCanonical.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
         * part by this FamilyMemberHistory.
         * </p>
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
            this.instantiatesCanonical.addAll(instantiatesCanonical);
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this FamilyMemberHistory.
         * </p>
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesUri(Uri... instantiatesUri) {
            for (Uri value : instantiatesUri) {
                this.instantiatesUri.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this FamilyMemberHistory.
         * </p>
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
            this.instantiatesUri.addAll(instantiatesUri);
            return this;
        }

        /**
         * <p>
         * Describes why the family member's history is not available.
         * </p>
         * 
         * @param dataAbsentReason
         *     subject-unknown | withheld | unable-to-obtain | deferred
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dataAbsentReason(CodeableConcept dataAbsentReason) {
            this.dataAbsentReason = dataAbsentReason;
            return this;
        }

        /**
         * <p>
         * The date (and possibly time) when the family member history was recorded or last updated.
         * </p>
         * 
         * @param date
         *     When history was recorded or last updated
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
         * This will either be a name or a description; e.g. "Aunt Susan", "my cousin with the red hair".
         * </p>
         * 
         * @param name
         *     The family member described
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
         * The birth sex of the family member.
         * </p>
         * 
         * @param sex
         *     male | female | other | unknown
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder sex(CodeableConcept sex) {
            this.sex = sex;
            return this;
        }

        /**
         * <p>
         * The actual or approximate date of birth of the relative.
         * </p>
         * 
         * @param born
         *     (approximate) date of birth
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder born(Element born) {
            this.born = born;
            return this;
        }

        /**
         * <p>
         * The age of the relative at the time the family member history is recorded.
         * </p>
         * 
         * @param age
         *     (approximate) age
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder age(Element age) {
            this.age = age;
            return this;
        }

        /**
         * <p>
         * If true, indicates that the age value specified is an estimated value.
         * </p>
         * 
         * @param estimatedAge
         *     Age is estimated?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder estimatedAge(Boolean estimatedAge) {
            this.estimatedAge = estimatedAge;
            return this;
        }

        /**
         * <p>
         * Deceased flag or the actual or approximate age of the relative at the time of death for the family member history 
         * record.
         * </p>
         * 
         * @param deceased
         *     Dead? How old/when?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder deceased(Element deceased) {
            this.deceased = deceased;
            return this;
        }

        /**
         * <p>
         * Describes why the family member history occurred in coded or textual form.
         * </p>
         * 
         * @param reasonCode
         *     Why was family member history performed?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonCode(CodeableConcept... reasonCode) {
            for (CodeableConcept value : reasonCode) {
                this.reasonCode.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Describes why the family member history occurred in coded or textual form.
         * </p>
         * 
         * @param reasonCode
         *     Why was family member history performed?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode.addAll(reasonCode);
            return this;
        }

        /**
         * <p>
         * Indicates a Condition, Observation, AllergyIntolerance, or QuestionnaireResponse that justifies this family member 
         * history event.
         * </p>
         * 
         * @param reasonReference
         *     Why was family member history performed?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonReference(Reference... reasonReference) {
            for (Reference value : reasonReference) {
                this.reasonReference.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates a Condition, Observation, AllergyIntolerance, or QuestionnaireResponse that justifies this family member 
         * history event.
         * </p>
         * 
         * @param reasonReference
         *     Why was family member history performed?
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference.addAll(reasonReference);
            return this;
        }

        /**
         * <p>
         * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in 
         * the condition property, but this is not always possible.
         * </p>
         * 
         * @param note
         *     General note about related person
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * <p>
         * This property allows a non condition-specific note to the made about the related person. Ideally, the note would be in 
         * the condition property, but this is not always possible.
         * </p>
         * 
         * @param note
         *     General note about related person
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        /**
         * <p>
         * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to 
         * represent more than one condition per resource, though there is nothing stopping multiple resources - one per 
         * condition.
         * </p>
         * 
         * @param condition
         *     Condition that the related person had
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder condition(Condition... condition) {
            for (Condition value : condition) {
                this.condition.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to 
         * represent more than one condition per resource, though there is nothing stopping multiple resources - one per 
         * condition.
         * </p>
         * 
         * @param condition
         *     Condition that the related person had
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder condition(Collection<Condition> condition) {
            this.condition.addAll(condition);
            return this;
        }

        @Override
        public FamilyMemberHistory build() {
            return new FamilyMemberHistory(this);
        }

        private Builder from(FamilyMemberHistory familyMemberHistory) {
            id = familyMemberHistory.id;
            meta = familyMemberHistory.meta;
            implicitRules = familyMemberHistory.implicitRules;
            language = familyMemberHistory.language;
            text = familyMemberHistory.text;
            contained.addAll(familyMemberHistory.contained);
            extension.addAll(familyMemberHistory.extension);
            modifierExtension.addAll(familyMemberHistory.modifierExtension);
            identifier.addAll(familyMemberHistory.identifier);
            instantiatesCanonical.addAll(familyMemberHistory.instantiatesCanonical);
            instantiatesUri.addAll(familyMemberHistory.instantiatesUri);
            dataAbsentReason = familyMemberHistory.dataAbsentReason;
            date = familyMemberHistory.date;
            name = familyMemberHistory.name;
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
     * <p>
     * The significant Conditions (or condition) that the family member had. This is a repeating section to allow a system to 
     * represent more than one condition per resource, though there is nothing stopping multiple resources - one per 
     * condition.
     * </p>
     */
    public static class Condition extends BackboneElement {
        private final CodeableConcept code;
        private final CodeableConcept outcome;
        private final Boolean contributedToDeath;
        private final Element onset;
        private final List<Annotation> note;

        private Condition(Builder builder) {
            super(builder);
            code = ValidationSupport.requireNonNull(builder.code, "code");
            outcome = builder.outcome;
            contributedToDeath = builder.contributedToDeath;
            onset = ValidationSupport.choiceElement(builder.onset, "onset", Age.class, Range.class, Period.class, String.class);
            note = Collections.unmodifiableList(builder.note);
        }

        /**
         * <p>
         * The actual condition specified. Could be a coded condition (like MI or Diabetes) or a less specific string like 
         * 'cancer' depending on how much is known about the condition and the capabilities of the creating system.
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
         * Indicates what happened following the condition. If the condition resulted in death, deceased date is captured on the 
         * relation.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getOutcome() {
            return outcome;
        }

        /**
         * <p>
         * This condition contributed to the cause of death of the related person. If contributedToDeath is not populated, then 
         * it is unknown.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getContributedToDeath() {
            return contributedToDeath;
        }

        /**
         * <p>
         * Either the age of onset, range of approximate age or descriptive string can be recorded. For conditions with multiple 
         * occurrences, this describes the first known occurrence.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getOnset() {
            return onset;
        }

        /**
         * <p>
         * An area where general notes can be placed about this specific condition.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Annotation}.
         */
        public List<Annotation> getNote() {
            return note;
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
                    accept(outcome, "outcome", visitor);
                    accept(contributedToDeath, "contributedToDeath", visitor);
                    accept(onset, "onset", visitor, true);
                    accept(note, "note", visitor, Annotation.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return new Builder(code).from(this);
        }

        public Builder toBuilder(CodeableConcept code) {
            return new Builder(code).from(this);
        }

        public static Builder builder(CodeableConcept code) {
            return new Builder(code);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept code;

            // optional
            private CodeableConcept outcome;
            private Boolean contributedToDeath;
            private Element onset;
            private List<Annotation> note = new ArrayList<>();

            private Builder(CodeableConcept code) {
                super();
                this.code = code;
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
             * Indicates what happened following the condition. If the condition resulted in death, deceased date is captured on the 
             * relation.
             * </p>
             * 
             * @param outcome
             *     deceased | permanent disability | etc.
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder outcome(CodeableConcept outcome) {
                this.outcome = outcome;
                return this;
            }

            /**
             * <p>
             * This condition contributed to the cause of death of the related person. If contributedToDeath is not populated, then 
             * it is unknown.
             * </p>
             * 
             * @param contributedToDeath
             *     Whether the condition contributed to the cause of death
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder contributedToDeath(Boolean contributedToDeath) {
                this.contributedToDeath = contributedToDeath;
                return this;
            }

            /**
             * <p>
             * Either the age of onset, range of approximate age or descriptive string can be recorded. For conditions with multiple 
             * occurrences, this describes the first known occurrence.
             * </p>
             * 
             * @param onset
             *     When condition first manifested
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder onset(Element onset) {
                this.onset = onset;
                return this;
            }

            /**
             * <p>
             * An area where general notes can be placed about this specific condition.
             * </p>
             * 
             * @param note
             *     Extra information about condition
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder note(Annotation... note) {
                for (Annotation value : note) {
                    this.note.add(value);
                }
                return this;
            }

            /**
             * <p>
             * An area where general notes can be placed about this specific condition.
             * </p>
             * 
             * @param note
             *     Extra information about condition
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder note(Collection<Annotation> note) {
                this.note.addAll(note);
                return this;
            }

            @Override
            public Condition build() {
                return new Condition(this);
            }

            private Builder from(Condition condition) {
                id = condition.id;
                extension.addAll(condition.extension);
                modifierExtension.addAll(condition.modifierExtension);
                outcome = condition.outcome;
                contributedToDeath = condition.contributedToDeath;
                onset = condition.onset;
                note.addAll(condition.note);
                return this;
            }
        }
    }
}
