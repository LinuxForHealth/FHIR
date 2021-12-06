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
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Annotation;
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
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AllergyIntoleranceCategory;
import com.ibm.fhir.model.type.code.AllergyIntoleranceCriticality;
import com.ibm.fhir.model.type.code.AllergyIntoleranceSeverity;
import com.ibm.fhir.model.type.code.AllergyIntoleranceType;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure 
 * to a substance.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "ait-1",
    level = "Rule",
    location = "(base)",
    description = "AllergyIntolerance.clinicalStatus SHALL be present if verificationStatus is not entered-in-error.",
    expression = "(verificationStatus.exists() and verificationStatus.coding.where(system='http://terminology.hl7.org/CodeSystem/allergyintolerance-verification' and code='entered-in-error').exists().not()) implies clinicalStatus.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/AllergyIntolerance"
)
@Constraint(
    id = "ait-2",
    level = "Rule",
    location = "(base)",
    description = "AllergyIntolerance.clinicalStatus SHALL NOT be present if verification Status is entered-in-error",
    expression = "(verificationStatus.coding.where(system='http://terminology.hl7.org/CodeSystem/allergyintolerance-verification' and code='entered-in-error').exists()) implies clinicalStatus.exists().not()",
    source = "http://hl7.org/fhir/StructureDefinition/AllergyIntolerance"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AllergyIntolerance extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "AllergyIntoleranceClinicalStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The clinical status of the allergy or intolerance.",
        valueSet = "http://hl7.org/fhir/ValueSet/allergyintolerance-clinical|4.1.0"
    )
    private final CodeableConcept clinicalStatus;
    @Summary
    @Binding(
        bindingName = "AllergyIntoleranceVerificationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Assertion about certainty associated with a propensity, or potential risk, of a reaction to the identified substance.",
        valueSet = "http://hl7.org/fhir/ValueSet/allergyintolerance-verification|4.1.0"
    )
    private final CodeableConcept verificationStatus;
    @Summary
    @Binding(
        bindingName = "AllergyIntoleranceType",
        strength = BindingStrength.Value.REQUIRED,
        description = "Identification of the underlying physiological mechanism for a Reaction Risk.",
        valueSet = "http://hl7.org/fhir/ValueSet/allergy-intolerance-type|4.1.0"
    )
    private final AllergyIntoleranceType type;
    @Summary
    @Binding(
        bindingName = "AllergyIntoleranceCategory",
        strength = BindingStrength.Value.REQUIRED,
        description = "Category of an identified substance associated with allergies or intolerances.",
        valueSet = "http://hl7.org/fhir/ValueSet/allergy-intolerance-category|4.1.0"
    )
    private final List<AllergyIntoleranceCategory> category;
    @Summary
    @Binding(
        bindingName = "AllergyIntoleranceCriticality",
        strength = BindingStrength.Value.REQUIRED,
        description = "Estimate of the potential clinical harm, or seriousness, of a reaction to an identified substance.",
        valueSet = "http://hl7.org/fhir/ValueSet/allergy-intolerance-criticality|4.1.0"
    )
    private final AllergyIntoleranceCriticality criticality;
    @Summary
    @Binding(
        bindingName = "AllergyIntoleranceCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Type of the substance/product, allergy or intolerance condition, or negation/exclusion codes for reporting no known allergies.",
        valueSet = "http://hl7.org/fhir/ValueSet/allergyintolerance-code"
    )
    private final CodeableConcept code;
    @Summary
    @ReferenceTarget({ "Patient" })
    @Required
    private final Reference patient;
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Choice({ DateTime.class, Age.class, Period.class, Range.class, String.class })
    private final Element onset;
    private final DateTime recordedDate;
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Patient", "RelatedPerson" })
    private final Reference recorder;
    @Summary
    @ReferenceTarget({ "Patient", "RelatedPerson", "Practitioner", "PractitionerRole" })
    private final Reference asserter;
    private final DateTime lastOccurrence;
    private final List<Annotation> note;
    private final List<Reaction> reaction;

    private AllergyIntolerance(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        clinicalStatus = builder.clinicalStatus;
        verificationStatus = builder.verificationStatus;
        type = builder.type;
        category = Collections.unmodifiableList(builder.category);
        criticality = builder.criticality;
        code = builder.code;
        patient = builder.patient;
        encounter = builder.encounter;
        onset = builder.onset;
        recordedDate = builder.recordedDate;
        recorder = builder.recorder;
        asserter = builder.asserter;
        lastOccurrence = builder.lastOccurrence;
        note = Collections.unmodifiableList(builder.note);
        reaction = Collections.unmodifiableList(builder.reaction);
    }

    /**
     * Business identifiers assigned to this AllergyIntolerance by the performer or other systems which remain constant as 
     * the resource is updated and propagates from server to server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The clinical status of the allergy or intolerance.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getClinicalStatus() {
        return clinicalStatus;
    }

    /**
     * Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance 
     * (including pharmaceutical product).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getVerificationStatus() {
        return verificationStatus;
    }

    /**
     * Identification of the underlying physiological mechanism for the reaction risk.
     * 
     * @return
     *     An immutable object of type {@link AllergyIntoleranceType} that may be null.
     */
    public AllergyIntoleranceType getType() {
        return type;
    }

    /**
     * Category of the identified substance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link AllergyIntoleranceCategory} that may be empty.
     */
    public List<AllergyIntoleranceCategory> getCategory() {
        return category;
    }

    /**
     * Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.
     * 
     * @return
     *     An immutable object of type {@link AllergyIntoleranceCriticality} that may be null.
     */
    public AllergyIntoleranceCriticality getCriticality() {
        return criticality;
    }

    /**
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
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * The patient who has the allergy or intolerance.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * The encounter when the allergy or intolerance was asserted.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * Estimated or actual date, date-time, or age when allergy or intolerance was identified.
     * 
     * @return
     *     An immutable object of type {@link DateTime}, {@link Age}, {@link Period}, {@link Range} or {@link String} that may be 
     *     null.
     */
    public Element getOnset() {
        return onset;
    }

    /**
     * The recordedDate represents when this particular AllergyIntolerance record was created in the system, which is often a 
     * system-generated date.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getRecordedDate() {
        return recordedDate;
    }

    /**
     * Individual who recorded the record and takes responsibility for its content.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRecorder() {
        return recorder;
    }

    /**
     * The source of the information about the allergy that is recorded.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAsserter() {
        return asserter;
    }

    /**
     * Represents the date and/or time of the last known occurrence of a reaction event.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getLastOccurrence() {
        return lastOccurrence;
    }

    /**
     * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Details about each adverse reaction event linked to exposure to the identified substance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reaction} that may be empty.
     */
    public List<Reaction> getReaction() {
        return reaction;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (clinicalStatus != null) || 
            (verificationStatus != null) || 
            (type != null) || 
            !category.isEmpty() || 
            (criticality != null) || 
            (code != null) || 
            (patient != null) || 
            (encounter != null) || 
            (onset != null) || 
            (recordedDate != null) || 
            (recorder != null) || 
            (asserter != null) || 
            (lastOccurrence != null) || 
            !note.isEmpty() || 
            !reaction.isEmpty();
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
         * Business identifiers assigned to this AllergyIntolerance by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Business identifiers assigned to this AllergyIntolerance by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External ids for this item
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
         * The clinical status of the allergy or intolerance.
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
         * Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance 
         * (including pharmaceutical product).
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
         * Identification of the underlying physiological mechanism for the reaction risk.
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
         * Category of the identified substance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Category of the identified substance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     food | medication | environment | biologic
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<AllergyIntoleranceCategory> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.
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
         * The patient who has the allergy or intolerance.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
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
         * The encounter when the allergy or intolerance was asserted.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
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
         * Convenience method for setting {@code onset} with choice type String.
         * 
         * @param onset
         *     When allergy or intolerance was identified
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
         * Estimated or actual date, date-time, or age when allergy or intolerance was identified.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link DateTime}</li>
         * <li>{@link Age}</li>
         * <li>{@link Period}</li>
         * <li>{@link Range}</li>
         * <li>{@link String}</li>
         * </ul>
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
         * The recordedDate represents when this particular AllergyIntolerance record was created in the system, which is often a 
         * system-generated date.
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
         * Individual who recorded the record and takes responsibility for its content.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
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
         * The source of the information about the allergy that is recorded.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
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
         * Represents the date and/or time of the last known occurrence of a reaction event.
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
         * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Additional text not captured in other fields
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
         * Details about each adverse reaction event linked to exposure to the identified substance.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Details about each adverse reaction event linked to exposure to the identified substance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reaction
         *     Adverse Reaction Events linked to exposure to substance
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reaction(Collection<Reaction> reaction) {
            this.reaction = new ArrayList<>(reaction);
            return this;
        }

        /**
         * Build the {@link AllergyIntolerance}
         * 
         * <p>Required elements:
         * <ul>
         * <li>patient</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link AllergyIntolerance}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid AllergyIntolerance per the base specification
         */
        @Override
        public AllergyIntolerance build() {
            AllergyIntolerance allergyIntolerance = new AllergyIntolerance(this);
            if (validating) {
                validate(allergyIntolerance);
            }
            return allergyIntolerance;
        }

        protected void validate(AllergyIntolerance allergyIntolerance) {
            super.validate(allergyIntolerance);
            ValidationSupport.checkList(allergyIntolerance.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(allergyIntolerance.category, "category", AllergyIntoleranceCategory.class);
            ValidationSupport.requireNonNull(allergyIntolerance.patient, "patient");
            ValidationSupport.choiceElement(allergyIntolerance.onset, "onset", DateTime.class, Age.class, Period.class, Range.class, String.class);
            ValidationSupport.checkList(allergyIntolerance.note, "note", Annotation.class);
            ValidationSupport.checkList(allergyIntolerance.reaction, "reaction", Reaction.class);
            ValidationSupport.checkValueSetBinding(allergyIntolerance.clinicalStatus, "clinicalStatus", "http://hl7.org/fhir/ValueSet/allergyintolerance-clinical", "http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "active", "inactive", "resolved");
            ValidationSupport.checkValueSetBinding(allergyIntolerance.verificationStatus, "verificationStatus", "http://hl7.org/fhir/ValueSet/allergyintolerance-verification", "http://terminology.hl7.org/CodeSystem/allergyintolerance-verification", "unconfirmed", "confirmed", "refuted", "entered-in-error");
            ValidationSupport.checkReferenceType(allergyIntolerance.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(allergyIntolerance.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(allergyIntolerance.recorder, "recorder", "Practitioner", "PractitionerRole", "Patient", "RelatedPerson");
            ValidationSupport.checkReferenceType(allergyIntolerance.asserter, "asserter", "Patient", "RelatedPerson", "Practitioner", "PractitionerRole");
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
     * Details about each adverse reaction event linked to exposure to the identified substance.
     */
    public static class Reaction extends BackboneElement {
        @Binding(
            bindingName = "SubstanceCode",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Codes defining the type of the substance (including pharmaceutical products).",
            valueSet = "http://hl7.org/fhir/ValueSet/substance-code"
        )
        private final CodeableConcept substance;
        @Binding(
            bindingName = "Manifestation",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Clinical symptoms and/or signs that are observed or associated with an Adverse Reaction Event.",
            valueSet = "http://hl7.org/fhir/ValueSet/clinical-findings"
        )
        @Required
        private final List<CodeableConcept> manifestation;
        private final String description;
        private final DateTime onset;
        @Binding(
            bindingName = "AllergyIntoleranceSeverity",
            strength = BindingStrength.Value.REQUIRED,
            description = "Clinical assessment of the severity of a reaction event as a whole, potentially considering multiple different manifestations.",
            valueSet = "http://hl7.org/fhir/ValueSet/reaction-event-severity|4.1.0"
        )
        private final AllergyIntoleranceSeverity severity;
        @Binding(
            bindingName = "RouteOfAdministration",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A coded concept describing the route or physiological path of administration of a therapeutic agent into or onto the body of a subject.",
            valueSet = "http://hl7.org/fhir/ValueSet/route-codes"
        )
        private final CodeableConcept exposureRoute;
        private final List<Annotation> note;

        private Reaction(Builder builder) {
            super(builder);
            substance = builder.substance;
            manifestation = Collections.unmodifiableList(builder.manifestation);
            description = builder.description;
            onset = builder.onset;
            severity = builder.severity;
            exposureRoute = builder.exposureRoute;
            note = Collections.unmodifiableList(builder.note);
        }

        /**
         * Identification of the specific substance (or pharmaceutical product) considered to be responsible for the Adverse 
         * Reaction event. Note: the substance for a specific reaction may be different from the substance identified as the 
         * cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a brand 
         * medication) or a composite product that includes the identified substance. It must be clinically safe to only process 
         * the 'code' and ignore the 'reaction.substance'. If a receiving system is unable to confirm that AllergyIntolerance.
         * reaction.substance falls within the semantic scope of AllergyIntolerance.code, then the receiving system should ignore 
         * AllergyIntolerance.reaction.substance.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getSubstance() {
            return substance;
        }

        /**
         * Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that is non-empty.
         */
        public List<CodeableConcept> getManifestation() {
            return manifestation;
        }

        /**
         * Text description about the reaction as a whole, including details of the manifestation if required.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Record of the date and/or time of the onset of the Reaction.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getOnset() {
            return onset;
        }

        /**
         * Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different 
         * manifestations.
         * 
         * @return
         *     An immutable object of type {@link AllergyIntoleranceSeverity} that may be null.
         */
        public AllergyIntoleranceSeverity getSeverity() {
            return severity;
        }

        /**
         * Identification of the route by which the subject was exposed to the substance.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getExposureRoute() {
            return exposureRoute;
        }

        /**
         * Additional text about the adverse reaction event not captured in other fields.
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
             * Identification of the specific substance (or pharmaceutical product) considered to be responsible for the Adverse 
             * Reaction event. Note: the substance for a specific reaction may be different from the substance identified as the 
             * cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a brand 
             * medication) or a composite product that includes the identified substance. It must be clinically safe to only process 
             * the 'code' and ignore the 'reaction.substance'. If a receiving system is unable to confirm that AllergyIntolerance.
             * reaction.substance falls within the semantic scope of AllergyIntolerance.code, then the receiving system should ignore 
             * AllergyIntolerance.reaction.substance.
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
             * Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
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
             * Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>This element is required.
             * 
             * @param manifestation
             *     Clinical symptoms/signs associated with the Event
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder manifestation(Collection<CodeableConcept> manifestation) {
                this.manifestation = new ArrayList<>(manifestation);
                return this;
            }

            /**
             * Convenience method for setting {@code description}.
             * 
             * @param description
             *     Description of the event as a whole
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
             * Text description about the reaction as a whole, including details of the manifestation if required.
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
             * Record of the date and/or time of the onset of the Reaction.
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
             * Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different 
             * manifestations.
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
             * Identification of the route by which the subject was exposed to the substance.
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
             * Additional text about the adverse reaction event not captured in other fields.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * Additional text about the adverse reaction event not captured in other fields.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param note
             *     Text about event not captured in other fields
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
             * Build the {@link Reaction}
             * 
             * <p>Required elements:
             * <ul>
             * <li>manifestation</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Reaction}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Reaction per the base specification
             */
            @Override
            public Reaction build() {
                Reaction reaction = new Reaction(this);
                if (validating) {
                    validate(reaction);
                }
                return reaction;
            }

            protected void validate(Reaction reaction) {
                super.validate(reaction);
                ValidationSupport.checkNonEmptyList(reaction.manifestation, "manifestation", CodeableConcept.class);
                ValidationSupport.checkList(reaction.note, "note", Annotation.class);
                ValidationSupport.requireValueOrChildren(reaction);
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
