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
 * A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a 
 * level of concern.
 * </p>
 */
@Constraint(
    id = "con-1",
    level = "Rule",
    location = "Condition.stage",
    description = "Stage SHALL have summary or assessment",
    expression = "summary.exists() or assessment.exists()"
)
@Constraint(
    id = "con-2",
    level = "Rule",
    location = "Condition.evidence",
    description = "evidence SHALL have code or details",
    expression = "code.exists() or detail.exists()"
)
@Constraint(
    id = "con-3",
    level = "Warning",
    location = "(base)",
    description = "Condition.clinicalStatus SHALL be present if verificationStatus is not entered-in-error and category is problem-list-item",
    expression = "clinicalStatus.exists() or verificationStatus='entered-in-error' or category.select($this='problem-list-item').empty()"
)
@Constraint(
    id = "con-4",
    level = "Rule",
    location = "(base)",
    description = "If condition is abated, then clinicalStatus must be either inactive, resolved, or remission",
    expression = "abatement.empty() or clinicalStatus.coding.where(system='http://terminology.hl7.org/CodeSystem/condition-clinical' and (code='resolved' or code='remission' or code='inactive')).exists()"
)
@Constraint(
    id = "con-5",
    level = "Rule",
    location = "(base)",
    description = "Condition.clinicalStatus SHALL NOT be present if verification Status is entered-in-error",
    expression = "verificationStatus.coding.where(system='http://terminology.hl7.org/CodeSystem/condition-ver-status' and code='entered-in-error').empty() or clinicalStatus.empty()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Condition extends DomainResource {
    private final List<Identifier> identifier;
    private final CodeableConcept clinicalStatus;
    private final CodeableConcept verificationStatus;
    private final List<CodeableConcept> category;
    private final CodeableConcept severity;
    private final CodeableConcept code;
    private final List<CodeableConcept> bodySite;
    private final Reference subject;
    private final Reference encounter;
    private final Element onset;
    private final Element abatement;
    private final DateTime recordedDate;
    private final Reference recorder;
    private final Reference asserter;
    private final List<Stage> stage;
    private final List<Evidence> evidence;
    private final List<Annotation> note;

    private volatile int hashCode;

    private Condition(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        clinicalStatus = builder.clinicalStatus;
        verificationStatus = builder.verificationStatus;
        category = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.category, "category"));
        severity = builder.severity;
        code = builder.code;
        bodySite = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.bodySite, "bodySite"));
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        encounter = builder.encounter;
        onset = ValidationSupport.choiceElement(builder.onset, "onset", DateTime.class, Age.class, Period.class, Range.class, String.class);
        abatement = ValidationSupport.choiceElement(builder.abatement, "abatement", DateTime.class, Age.class, Period.class, Range.class, String.class);
        recordedDate = builder.recordedDate;
        recorder = builder.recorder;
        asserter = builder.asserter;
        stage = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.stage, "stage"));
        evidence = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.evidence, "evidence"));
        note = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.note, "note"));
    }

    /**
     * <p>
     * Business identifiers assigned to this condition by the performer or other systems which remain constant as the 
     * resource is updated and propagates from server to server.
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
     * The clinical status of the condition.
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
     * The verification status to support the clinical status of the condition.
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
     * A category assigned to the condition.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * <p>
     * A subjective assessment of the severity of the condition as evaluated by the clinician.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getSeverity() {
        return severity;
    }

    /**
     * <p>
     * Identification of the condition, problem or diagnosis.
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
     * The anatomical location where this condition manifests itself.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getBodySite() {
        return bodySite;
    }

    /**
     * <p>
     * Indicates the patient or group who the condition record is associated with.
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
     * The Encounter during which this Condition was created or to which the creation of this record is tightly associated.
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
     * Estimated or actual date or date-time the condition began, in the opinion of the clinician.
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
     * The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of 
     * the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, 
     * but they can abate.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getAbatement() {
        return abatement;
    }

    /**
     * <p>
     * The recordedDate represents when this particular Condition record was created in the system, which is often a system-
     * generated date.
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
     * Individual who is making the condition statement.
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
     * Clinical stage or grade of a condition. May include formal severity assessments.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Stage}.
     */
    public List<Stage> getStage() {
        return stage;
    }

    /**
     * <p>
     * Supporting evidence / manifestations that are the basis of the Condition's verification status, such as evidence that 
     * confirmed or refuted the condition.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Evidence}.
     */
    public List<Evidence> getEvidence() {
        return evidence;
    }

    /**
     * <p>
     * Additional information about the Condition. This is a general notes/comments entry for description of the Condition, 
     * its diagnosis and prognosis.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation}.
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
                accept(category, "category", visitor, CodeableConcept.class);
                accept(severity, "severity", visitor);
                accept(code, "code", visitor);
                accept(bodySite, "bodySite", visitor, CodeableConcept.class);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(onset, "onset", visitor);
                accept(abatement, "abatement", visitor);
                accept(recordedDate, "recordedDate", visitor);
                accept(recorder, "recorder", visitor);
                accept(asserter, "asserter", visitor);
                accept(stage, "stage", visitor, Stage.class);
                accept(evidence, "evidence", visitor, Evidence.class);
                accept(note, "note", visitor, Annotation.class);
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
        Condition other = (Condition) obj;
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
            Objects.equals(category, other.category) && 
            Objects.equals(severity, other.severity) && 
            Objects.equals(code, other.code) && 
            Objects.equals(bodySite, other.bodySite) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(onset, other.onset) && 
            Objects.equals(abatement, other.abatement) && 
            Objects.equals(recordedDate, other.recordedDate) && 
            Objects.equals(recorder, other.recorder) && 
            Objects.equals(asserter, other.asserter) && 
            Objects.equals(stage, other.stage) && 
            Objects.equals(evidence, other.evidence) && 
            Objects.equals(note, other.note);
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
                category, 
                severity, 
                code, 
                bodySite, 
                subject, 
                encounter, 
                onset, 
                abatement, 
                recordedDate, 
                recorder, 
                asserter, 
                stage, 
                evidence, 
                note);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(subject).from(this);
    }

    public Builder toBuilder(Reference subject) {
        return new Builder(subject).from(this);
    }

    public static Builder builder(Reference subject) {
        return new Builder(subject);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Reference subject;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept clinicalStatus;
        private CodeableConcept verificationStatus;
        private List<CodeableConcept> category = new ArrayList<>();
        private CodeableConcept severity;
        private CodeableConcept code;
        private List<CodeableConcept> bodySite = new ArrayList<>();
        private Reference encounter;
        private Element onset;
        private Element abatement;
        private DateTime recordedDate;
        private Reference recorder;
        private Reference asserter;
        private List<Stage> stage = new ArrayList<>();
        private List<Evidence> evidence = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();

        private Builder(Reference subject) {
            super();
            this.subject = subject;
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
         * Business identifiers assigned to this condition by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     External Ids for this condition
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
         * Business identifiers assigned to this condition by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     External Ids for this condition
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
         * The clinical status of the condition.
         * </p>
         * 
         * @param clinicalStatus
         *     active | recurrence | relapse | inactive | remission | resolved
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
         * The verification status to support the clinical status of the condition.
         * </p>
         * 
         * @param verificationStatus
         *     unconfirmed | provisional | differential | confirmed | refuted | entered-in-error
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
         * A category assigned to the condition.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param category
         *     problem-list-item | encounter-diagnosis
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
         * <p>
         * A category assigned to the condition.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param category
         *     problem-list-item | encounter-diagnosis
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * <p>
         * A subjective assessment of the severity of the condition as evaluated by the clinician.
         * </p>
         * 
         * @param severity
         *     Subjective severity of condition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder severity(CodeableConcept severity) {
            this.severity = severity;
            return this;
        }

        /**
         * <p>
         * Identification of the condition, problem or diagnosis.
         * </p>
         * 
         * @param code
         *     Identification of the condition, problem or diagnosis
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
         * The anatomical location where this condition manifests itself.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param bodySite
         *     Anatomical location, if relevant
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder bodySite(CodeableConcept... bodySite) {
            for (CodeableConcept value : bodySite) {
                this.bodySite.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The anatomical location where this condition manifests itself.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param bodySite
         *     Anatomical location, if relevant
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder bodySite(Collection<CodeableConcept> bodySite) {
            this.bodySite = new ArrayList<>(bodySite);
            return this;
        }

        /**
         * <p>
         * The Encounter during which this Condition was created or to which the creation of this record is tightly associated.
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
         * Estimated or actual date or date-time the condition began, in the opinion of the clinician.
         * </p>
         * 
         * @param onset
         *     Estimated or actual date, date-time, or age
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
         * The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of 
         * the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, 
         * but they can abate.
         * </p>
         * 
         * @param abatement
         *     When in resolution/remission
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder abatement(Element abatement) {
            this.abatement = abatement;
            return this;
        }

        /**
         * <p>
         * The recordedDate represents when this particular Condition record was created in the system, which is often a system-
         * generated date.
         * </p>
         * 
         * @param recordedDate
         *     Date record was first recorded
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
         *     Who recorded the condition
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
         * Individual who is making the condition statement.
         * </p>
         * 
         * @param asserter
         *     Person who asserts this condition
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
         * Clinical stage or grade of a condition. May include formal severity assessments.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param stage
         *     Stage/grade, usually assessed formally
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder stage(Stage... stage) {
            for (Stage value : stage) {
                this.stage.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Clinical stage or grade of a condition. May include formal severity assessments.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param stage
         *     Stage/grade, usually assessed formally
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder stage(Collection<Stage> stage) {
            this.stage = new ArrayList<>(stage);
            return this;
        }

        /**
         * <p>
         * Supporting evidence / manifestations that are the basis of the Condition's verification status, such as evidence that 
         * confirmed or refuted the condition.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param evidence
         *     Supporting evidence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder evidence(Evidence... evidence) {
            for (Evidence value : evidence) {
                this.evidence.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Supporting evidence / manifestations that are the basis of the Condition's verification status, such as evidence that 
         * confirmed or refuted the condition.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param evidence
         *     Supporting evidence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder evidence(Collection<Evidence> evidence) {
            this.evidence = new ArrayList<>(evidence);
            return this;
        }

        /**
         * <p>
         * Additional information about the Condition. This is a general notes/comments entry for description of the Condition, 
         * its diagnosis and prognosis.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param note
         *     Additional information about the Condition
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
         * Additional information about the Condition. This is a general notes/comments entry for description of the Condition, 
         * its diagnosis and prognosis.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param note
         *     Additional information about the Condition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        @Override
        public Condition build() {
            return new Condition(this);
        }

        private Builder from(Condition condition) {
            id = condition.id;
            meta = condition.meta;
            implicitRules = condition.implicitRules;
            language = condition.language;
            text = condition.text;
            contained.addAll(condition.contained);
            extension.addAll(condition.extension);
            modifierExtension.addAll(condition.modifierExtension);
            identifier.addAll(condition.identifier);
            clinicalStatus = condition.clinicalStatus;
            verificationStatus = condition.verificationStatus;
            category.addAll(condition.category);
            severity = condition.severity;
            code = condition.code;
            bodySite.addAll(condition.bodySite);
            encounter = condition.encounter;
            onset = condition.onset;
            abatement = condition.abatement;
            recordedDate = condition.recordedDate;
            recorder = condition.recorder;
            asserter = condition.asserter;
            stage.addAll(condition.stage);
            evidence.addAll(condition.evidence);
            note.addAll(condition.note);
            return this;
        }
    }

    /**
     * <p>
     * Clinical stage or grade of a condition. May include formal severity assessments.
     * </p>
     */
    public static class Stage extends BackboneElement {
        private final CodeableConcept summary;
        private final List<Reference> assessment;
        private final CodeableConcept type;

        private volatile int hashCode;

        private Stage(Builder builder) {
            super(builder);
            summary = builder.summary;
            assessment = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.assessment, "assessment"));
            type = builder.type;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A simple summary of the stage such as "Stage 3". The determination of the stage is disease-specific.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getSummary() {
            return summary;
        }

        /**
         * <p>
         * Reference to a formal record of the evidence on which the staging assessment is based.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getAssessment() {
            return assessment;
        }

        /**
         * <p>
         * The kind of staging, such as pathological or clinical staging.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getType() {
            return type;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (summary != null) || 
                !assessment.isEmpty() || 
                (type != null);
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
                    accept(summary, "summary", visitor);
                    accept(assessment, "assessment", visitor, Reference.class);
                    accept(type, "type", visitor);
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
            Stage other = (Stage) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(summary, other.summary) && 
                Objects.equals(assessment, other.assessment) && 
                Objects.equals(type, other.type);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    summary, 
                    assessment, 
                    type);
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
            private CodeableConcept summary;
            private List<Reference> assessment = new ArrayList<>();
            private CodeableConcept type;

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
             * A simple summary of the stage such as "Stage 3". The determination of the stage is disease-specific.
             * </p>
             * 
             * @param summary
             *     Simple summary (disease specific)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder summary(CodeableConcept summary) {
                this.summary = summary;
                return this;
            }

            /**
             * <p>
             * Reference to a formal record of the evidence on which the staging assessment is based.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param assessment
             *     Formal record of assessment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder assessment(Reference... assessment) {
                for (Reference value : assessment) {
                    this.assessment.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Reference to a formal record of the evidence on which the staging assessment is based.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param assessment
             *     Formal record of assessment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder assessment(Collection<Reference> assessment) {
                this.assessment = new ArrayList<>(assessment);
                return this;
            }

            /**
             * <p>
             * The kind of staging, such as pathological or clinical staging.
             * </p>
             * 
             * @param type
             *     Kind of staging
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            @Override
            public Stage build() {
                return new Stage(this);
            }

            private Builder from(Stage stage) {
                id = stage.id;
                extension.addAll(stage.extension);
                modifierExtension.addAll(stage.modifierExtension);
                summary = stage.summary;
                assessment.addAll(stage.assessment);
                type = stage.type;
                return this;
            }
        }
    }

    /**
     * <p>
     * Supporting evidence / manifestations that are the basis of the Condition's verification status, such as evidence that 
     * confirmed or refuted the condition.
     * </p>
     */
    public static class Evidence extends BackboneElement {
        private final List<CodeableConcept> code;
        private final List<Reference> detail;

        private volatile int hashCode;

        private Evidence(Builder builder) {
            super(builder);
            code = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.code, "code"));
            detail = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.detail, "detail"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A manifestation or symptom that led to the recording of this condition.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getCode() {
            return code;
        }

        /**
         * <p>
         * Links to other relevant information, including pathology reports.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getDetail() {
            return detail;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !code.isEmpty() || 
                !detail.isEmpty();
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
            Evidence other = (Evidence) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
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
             * A manifestation or symptom that led to the recording of this condition.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param code
             *     Manifestation/symptom
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
             * <p>
             * A manifestation or symptom that led to the recording of this condition.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param code
             *     Manifestation/symptom
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(Collection<CodeableConcept> code) {
                this.code = new ArrayList<>(code);
                return this;
            }

            /**
             * <p>
             * Links to other relevant information, including pathology reports.
             * </p>
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param detail
             *     Supporting information found elsewhere
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
             * <p>
             * Links to other relevant information, including pathology reports.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param detail
             *     Supporting information found elsewhere
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Collection<Reference> detail) {
                this.detail = new ArrayList<>(detail);
                return this;
            }

            @Override
            public Evidence build() {
                return new Evidence(this);
            }

            private Builder from(Evidence evidence) {
                id = evidence.id;
                extension.addAll(evidence.extension);
                modifierExtension.addAll(evidence.modifierExtension);
                code.addAll(evidence.code);
                detail.addAll(evidence.detail);
                return this;
            }
        }
    }
}
