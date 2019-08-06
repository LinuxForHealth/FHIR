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
import com.ibm.watsonhealth.fhir.model.type.Age;
import com.ibm.watsonhealth.fhir.model.type.AllergyIntoleranceCategory;
import com.ibm.watsonhealth.fhir.model.type.AllergyIntoleranceCriticality;
import com.ibm.watsonhealth.fhir.model.type.AllergyIntoleranceSeverity;
import com.ibm.watsonhealth.fhir.model.type.AllergyIntoleranceType;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
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
 * Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure 
 * to a substance.
 * </p>
 */
@Constraint(
    id = "ait-1",
    level = "Rule",
    location = "(base)",
    description = "AllergyIntolerance.clinicalStatus SHALL be present if verificationStatus is not entered-in-error.",
    expression = "verificationStatus='entered-in-error' or clinicalStatus.exists()"
)
@Constraint(
    id = "ait-2",
    level = "Rule",
    location = "(base)",
    description = "AllergyIntolerance.clinicalStatus SHALL NOT be present if verification Status is entered-in-error",
    expression = "verificationStatus!='entered-in-error' or clinicalStatus.empty()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class AllergyIntolerance extends DomainResource {
    private final List<Identifier> identifier;
    private final CodeableConcept clinicalStatus;
    private final CodeableConcept verificationStatus;
    private final AllergyIntoleranceType type;
    private final List<AllergyIntoleranceCategory> category;
    private final AllergyIntoleranceCriticality criticality;
    private final CodeableConcept code;
    private final Reference patient;
    private final Reference encounter;
    private final Element onset;
    private final DateTime recordedDate;
    private final Reference recorder;
    private final Reference asserter;
    private final DateTime lastOccurrence;
    private final List<Annotation> note;
    private final List<Reaction> reaction;

    private volatile int hashCode;

    private AllergyIntolerance(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        clinicalStatus = builder.clinicalStatus;
        verificationStatus = builder.verificationStatus;
        type = builder.type;
        category = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.category, "category"));
        criticality = builder.criticality;
        code = builder.code;
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        encounter = builder.encounter;
        onset = ValidationSupport.choiceElement(builder.onset, "onset", DateTime.class, Age.class, Period.class, Range.class, String.class);
        recordedDate = builder.recordedDate;
        recorder = builder.recorder;
        asserter = builder.asserter;
        lastOccurrence = builder.lastOccurrence;
        note = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.note, "note"));
        reaction = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reaction, "reaction"));
    }

    /**
     * <p>
     * Business identifiers assigned to this AllergyIntolerance by the performer or other systems which remain constant as 
     * the resource is updated and propagates from server to server.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The clinical status of the allergy or intolerance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getClinicalStatus() {
        return clinicalStatus;
    }

    /**
     * <p>
     * Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance 
     * (including pharmaceutical product).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getVerificationStatus() {
        return verificationStatus;
    }

    /**
     * <p>
     * Identification of the underlying physiological mechanism for the reaction risk.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link AllergyIntoleranceType}.
     */
    public AllergyIntoleranceType getType() {
        return type;
    }

    /**
     * <p>
     * Category of the identified substance.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link AllergyIntoleranceCategory}.
     */
    public List<AllergyIntoleranceCategory> getCategory() {
        return category;
    }

    /**
     * <p>
     * Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link AllergyIntoleranceCriticality}.
     */
    public AllergyIntoleranceCriticality getCriticality() {
        return criticality;
    }

    /**
     * <p>
     * Code for an allergy or intolerance statement (either a positive or a negated/excluded statement). This may be a code 
     * for a substance or pharmaceutical product that is considered to be responsible for the adverse reaction risk (e.g., 
     * "Latex"), an allergy or intolerance condition (e.g., "Latex allergy"), or a negated/excluded code for a specific 
     * substance or class (e.g., "No latex allergy") or a general or categorical negated statement (e.g., "No known allergy", 
     * "No known drug allergies"). Note: the substance for a specific reaction may be different from the substance identified 
     * as the cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a 
     * brand medication) or a composite product that includes the identified substance. It must be clinically safe to only 
     * process the 'code' and ignore the 'reaction.substance'. If a receiving system is unable to confirm that 
     * AllergyIntolerance.reaction.substance falls within the semantic scope of AllergyIntolerance.code, then the receiving 
     * system should ignore AllergyIntolerance.reaction.substance.
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
     * The patient who has the allergy or intolerance.
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
     * The encounter when the allergy or intolerance was asserted.
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
     * Estimated or actual date, date-time, or age when allergy or intolerance was identified.
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
     * The recordedDate represents when this particular AllergyIntolerance record was created in the system, which is often a 
     * system-generated date.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getRecordedDate() {
        return recordedDate;
    }

    /**
     * <p>
     * Individual who recorded the record and takes responsibility for its content.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRecorder() {
        return recorder;
    }

    /**
     * <p>
     * The source of the information about the allergy that is recorded.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAsserter() {
        return asserter;
    }

    /**
     * <p>
     * Represents the date and/or time of the last known occurrence of a reaction event.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getLastOccurrence() {
        return lastOccurrence;
    }

    /**
     * <p>
     * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * <p>
     * Details about each adverse reaction event linked to exposure to the identified substance.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reaction}.
     */
    public List<Reaction> getReaction() {
        return reaction;
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
                accept(clinicalStatus, "clinicalStatus", visitor);
                accept(verificationStatus, "verificationStatus", visitor);
                accept(type, "type", visitor);
                accept(category, "category", visitor, AllergyIntoleranceCategory.class);
                accept(criticality, "criticality", visitor);
                accept(code, "code", visitor);
                accept(patient, "patient", visitor);
                accept(encounter, "encounter", visitor);
                accept(onset, "onset", visitor);
                accept(recordedDate, "recordedDate", visitor);
                accept(recorder, "recorder", visitor);
                accept(asserter, "asserter", visitor);
                accept(lastOccurrence, "lastOccurrence", visitor);
                accept(note, "note", visitor, Annotation.class);
                accept(reaction, "reaction", visitor, Reaction.class);
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
        AllergyIntolerance other = (AllergyIntolerance) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(clinicalStatus, other.clinicalStatus) && 
            Objects.equals(verificationStatus, other.verificationStatus) && 
            Objects.equals(type, other.type) && 
            Objects.equals(category, other.category) && 
            Objects.equals(criticality, other.criticality) && 
            Objects.equals(code, other.code) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(onset, other.onset) && 
            Objects.equals(recordedDate, other.recordedDate) && 
            Objects.equals(recorder, other.recorder) && 
            Objects.equals(asserter, other.asserter) && 
            Objects.equals(lastOccurrence, other.lastOccurrence) && 
            Objects.equals(note, other.note) && 
            Objects.equals(reaction, other.reaction);
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
                clinicalStatus, 
                verificationStatus, 
                type, 
                category, 
                criticality, 
                code, 
                patient, 
                encounter, 
                onset, 
                recordedDate, 
                recorder, 
                asserter, 
                lastOccurrence, 
                note, 
                reaction);
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
        private CodeableConcept clinicalStatus;
        private CodeableConcept verificationStatus;
        private AllergyIntoleranceType type;
        private List<AllergyIntoleranceCategory> category = new ArrayList<>();
        private AllergyIntoleranceCriticality criticality;
        private CodeableConcept code;
        private Reference patient;
        private Reference encounter;
        private Element onset;
        private DateTime recordedDate;
        private Reference recorder;
        private Reference asserter;
        private DateTime lastOccurrence;
        private List<Annotation> note = new ArrayList<>();
        private List<Reaction> reaction = new ArrayList<>();

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
         * Business identifiers assigned to this AllergyIntolerance by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     External ids for this item
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
         * Business identifiers assigned to this AllergyIntolerance by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     External ids for this item
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
         * The clinical status of the allergy or intolerance.
         * </p>
         * 
         * @param clinicalStatus
         *     active | inactive | resolved
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder clinicalStatus(CodeableConcept clinicalStatus) {
            this.clinicalStatus = clinicalStatus;
            return this;
        }

        /**
         * <p>
         * Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance 
         * (including pharmaceutical product).
         * </p>
         * 
         * @param verificationStatus
         *     unconfirmed | confirmed | refuted | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder verificationStatus(CodeableConcept verificationStatus) {
            this.verificationStatus = verificationStatus;
            return this;
        }

        /**
         * <p>
         * Identification of the underlying physiological mechanism for the reaction risk.
         * </p>
         * 
         * @param type
         *     allergy | intolerance - Underlying mechanism (if known)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(AllergyIntoleranceType type) {
            this.type = type;
            return this;
        }

        /**
         * <p>
         * Category of the identified substance.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param category
         *     food | medication | environment | biologic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(AllergyIntoleranceCategory... category) {
            for (AllergyIntoleranceCategory value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Category of the identified substance.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param category
         *     food | medication | environment | biologic
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(Collection<AllergyIntoleranceCategory> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * <p>
         * Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.
         * </p>
         * 
         * @param criticality
         *     low | high | unable-to-assess
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder criticality(AllergyIntoleranceCriticality criticality) {
            this.criticality = criticality;
            return this;
        }

        /**
         * <p>
         * Code for an allergy or intolerance statement (either a positive or a negated/excluded statement). This may be a code 
         * for a substance or pharmaceutical product that is considered to be responsible for the adverse reaction risk (e.g., 
         * "Latex"), an allergy or intolerance condition (e.g., "Latex allergy"), or a negated/excluded code for a specific 
         * substance or class (e.g., "No latex allergy") or a general or categorical negated statement (e.g., "No known allergy", 
         * "No known drug allergies"). Note: the substance for a specific reaction may be different from the substance identified 
         * as the cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a 
         * brand medication) or a composite product that includes the identified substance. It must be clinically safe to only 
         * process the 'code' and ignore the 'reaction.substance'. If a receiving system is unable to confirm that 
         * AllergyIntolerance.reaction.substance falls within the semantic scope of AllergyIntolerance.code, then the receiving 
         * system should ignore AllergyIntolerance.reaction.substance.
         * </p>
         * 
         * @param code
         *     Code that identifies the allergy or intolerance
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
         * The patient who has the allergy or intolerance.
         * </p>
         * 
         * @param patient
         *     Who the sensitivity is for
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * <p>
         * The encounter when the allergy or intolerance was asserted.
         * </p>
         * 
         * @param encounter
         *     Encounter when the allergy or intolerance was asserted
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
         * Estimated or actual date, date-time, or age when allergy or intolerance was identified.
         * </p>
         * 
         * @param onset
         *     When allergy or intolerance was identified
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder onset(Element onset) {
            this.onset = onset;
            return this;
        }

        /**
         * <p>
         * The recordedDate represents when this particular AllergyIntolerance record was created in the system, which is often a 
         * system-generated date.
         * </p>
         * 
         * @param recordedDate
         *     Date first version of the resource instance was recorded
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recordedDate(DateTime recordedDate) {
            this.recordedDate = recordedDate;
            return this;
        }

        /**
         * <p>
         * Individual who recorded the record and takes responsibility for its content.
         * </p>
         * 
         * @param recorder
         *     Who recorded the sensitivity
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recorder(Reference recorder) {
            this.recorder = recorder;
            return this;
        }

        /**
         * <p>
         * The source of the information about the allergy that is recorded.
         * </p>
         * 
         * @param asserter
         *     Source of the information about the allergy
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder asserter(Reference asserter) {
            this.asserter = asserter;
            return this;
        }

        /**
         * <p>
         * Represents the date and/or time of the last known occurrence of a reaction event.
         * </p>
         * 
         * @param lastOccurrence
         *     Date(/time) of last known occurrence of a reaction
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lastOccurrence(DateTime lastOccurrence) {
            this.lastOccurrence = lastOccurrence;
            return this;
        }

        /**
         * <p>
         * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param note
         *     Additional text not captured in other fields
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
         * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param note
         *     Additional text not captured in other fields
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
         * Details about each adverse reaction event linked to exposure to the identified substance.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param reaction
         *     Adverse Reaction Events linked to exposure to substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reaction(Reaction... reaction) {
            for (Reaction value : reaction) {
                this.reaction.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Details about each adverse reaction event linked to exposure to the identified substance.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reaction
         *     Adverse Reaction Events linked to exposure to substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reaction(Collection<Reaction> reaction) {
            this.reaction = new ArrayList<>(reaction);
            return this;
        }

        @Override
        public AllergyIntolerance build() {
            return new AllergyIntolerance(this);
        }

        protected Builder from(AllergyIntolerance allergyIntolerance) {
            super.from(allergyIntolerance);
            identifier.addAll(allergyIntolerance.identifier);
            clinicalStatus = allergyIntolerance.clinicalStatus;
            verificationStatus = allergyIntolerance.verificationStatus;
            type = allergyIntolerance.type;
            category.addAll(allergyIntolerance.category);
            criticality = allergyIntolerance.criticality;
            code = allergyIntolerance.code;
            patient = allergyIntolerance.patient;
            encounter = allergyIntolerance.encounter;
            onset = allergyIntolerance.onset;
            recordedDate = allergyIntolerance.recordedDate;
            recorder = allergyIntolerance.recorder;
            asserter = allergyIntolerance.asserter;
            lastOccurrence = allergyIntolerance.lastOccurrence;
            note.addAll(allergyIntolerance.note);
            reaction.addAll(allergyIntolerance.reaction);
            return this;
        }
    }

    /**
     * <p>
     * Details about each adverse reaction event linked to exposure to the identified substance.
     * </p>
     */
    public static class Reaction extends BackboneElement {
        private final CodeableConcept substance;
        private final List<CodeableConcept> manifestation;
        private final String description;
        private final DateTime onset;
        private final AllergyIntoleranceSeverity severity;
        private final CodeableConcept exposureRoute;
        private final List<Annotation> note;

        private volatile int hashCode;

        private Reaction(Builder builder) {
            super(builder);
            substance = builder.substance;
            manifestation = Collections.unmodifiableList(ValidationSupport.requireNonEmpty(builder.manifestation, "manifestation"));
            description = builder.description;
            onset = builder.onset;
            severity = builder.severity;
            exposureRoute = builder.exposureRoute;
            note = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.note, "note"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Identification of the specific substance (or pharmaceutical product) considered to be responsible for the Adverse 
         * Reaction event. Note: the substance for a specific reaction may be different from the substance identified as the 
         * cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a brand 
         * medication) or a composite product that includes the identified substance. It must be clinically safe to only process 
         * the 'code' and ignore the 'reaction.substance'. If a receiving system is unable to confirm that AllergyIntolerance.
         * reaction.substance falls within the semantic scope of AllergyIntolerance.code, then the receiving system should ignore 
         * AllergyIntolerance.reaction.substance.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getSubstance() {
            return substance;
        }

        /**
         * <p>
         * Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getManifestation() {
            return manifestation;
        }

        /**
         * <p>
         * Text description about the reaction as a whole, including details of the manifestation if required.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDescription() {
            return description;
        }

        /**
         * <p>
         * Record of the date and/or time of the onset of the Reaction.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link DateTime}.
         */
        public DateTime getOnset() {
            return onset;
        }

        /**
         * <p>
         * Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different 
         * manifestations.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link AllergyIntoleranceSeverity}.
         */
        public AllergyIntoleranceSeverity getSeverity() {
            return severity;
        }

        /**
         * <p>
         * Identification of the route by which the subject was exposed to the substance.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getExposureRoute() {
            return exposureRoute;
        }

        /**
         * <p>
         * Additional text about the adverse reaction event not captured in other fields.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Annotation}.
         */
        public List<Annotation> getNote() {
            return note;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (substance != null) || 
                !manifestation.isEmpty() || 
                (description != null) || 
                (onset != null) || 
                (severity != null) || 
                (exposureRoute != null) || 
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
                    accept(substance, "substance", visitor);
                    accept(manifestation, "manifestation", visitor, CodeableConcept.class);
                    accept(description, "description", visitor);
                    accept(onset, "onset", visitor);
                    accept(severity, "severity", visitor);
                    accept(exposureRoute, "exposureRoute", visitor);
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
            Reaction other = (Reaction) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(substance, other.substance) && 
                Objects.equals(manifestation, other.manifestation) && 
                Objects.equals(description, other.description) && 
                Objects.equals(onset, other.onset) && 
                Objects.equals(severity, other.severity) && 
                Objects.equals(exposureRoute, other.exposureRoute) && 
                Objects.equals(note, other.note);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    substance, 
                    manifestation, 
                    description, 
                    onset, 
                    severity, 
                    exposureRoute, 
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
            private CodeableConcept substance;
            private List<CodeableConcept> manifestation = new ArrayList<>();
            private String description;
            private DateTime onset;
            private AllergyIntoleranceSeverity severity;
            private CodeableConcept exposureRoute;
            private List<Annotation> note = new ArrayList<>();

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
             * Identification of the specific substance (or pharmaceutical product) considered to be responsible for the Adverse 
             * Reaction event. Note: the substance for a specific reaction may be different from the substance identified as the 
             * cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a brand 
             * medication) or a composite product that includes the identified substance. It must be clinically safe to only process 
             * the 'code' and ignore the 'reaction.substance'. If a receiving system is unable to confirm that AllergyIntolerance.
             * reaction.substance falls within the semantic scope of AllergyIntolerance.code, then the receiving system should ignore 
             * AllergyIntolerance.reaction.substance.
             * </p>
             * 
             * @param substance
             *     Specific substance or pharmaceutical product considered to be responsible for event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder substance(CodeableConcept substance) {
                this.substance = substance;
                return this;
            }

            /**
             * <p>
             * Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param manifestation
             *     Clinical symptoms/signs associated with the Event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder manifestation(CodeableConcept... manifestation) {
                for (CodeableConcept value : manifestation) {
                    this.manifestation.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param manifestation
             *     Clinical symptoms/signs associated with the Event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder manifestation(Collection<CodeableConcept> manifestation) {
                this.manifestation = new ArrayList<>(manifestation);
                return this;
            }

            /**
             * <p>
             * Text description about the reaction as a whole, including details of the manifestation if required.
             * </p>
             * 
             * @param description
             *     Description of the event as a whole
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * <p>
             * Record of the date and/or time of the onset of the Reaction.
             * </p>
             * 
             * @param onset
             *     Date(/time) when manifestations showed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder onset(DateTime onset) {
                this.onset = onset;
                return this;
            }

            /**
             * <p>
             * Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different 
             * manifestations.
             * </p>
             * 
             * @param severity
             *     mild | moderate | severe (of event as a whole)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder severity(AllergyIntoleranceSeverity severity) {
                this.severity = severity;
                return this;
            }

            /**
             * <p>
             * Identification of the route by which the subject was exposed to the substance.
             * </p>
             * 
             * @param exposureRoute
             *     How the subject was exposed to the substance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder exposureRoute(CodeableConcept exposureRoute) {
                this.exposureRoute = exposureRoute;
                return this;
            }

            /**
             * <p>
             * Additional text about the adverse reaction event not captured in other fields.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param note
             *     Text about event not captured in other fields
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
             * Additional text about the adverse reaction event not captured in other fields.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param note
             *     Text about event not captured in other fields
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder note(Collection<Annotation> note) {
                this.note = new ArrayList<>(note);
                return this;
            }

            @Override
            public Reaction build() {
                return new Reaction(this);
            }

            protected Builder from(Reaction reaction) {
                super.from(reaction);
                substance = reaction.substance;
                manifestation.addAll(reaction.manifestation);
                description = reaction.description;
                onset = reaction.onset;
                severity = reaction.severity;
                exposureRoute = reaction.exposureRoute;
                note.addAll(reaction.note);
                return this;
            }
        }
    }
}
