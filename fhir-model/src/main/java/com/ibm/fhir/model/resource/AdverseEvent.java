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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
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
import com.ibm.fhir.model.type.code.AdverseEventActuality;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Actual or potential/avoided event causing unintended physical injury resulting from or contributed to by medical care, 
 * a research study or other healthcare setting factors that requires additional monitoring, treatment, or 
 * hospitalization, or that results in death.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "adverseEvent-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/adverse-event-category",
    expression = "category.exists() implies (category.all(memberOf('http://hl7.org/fhir/ValueSet/adverse-event-category', 'extensible')))",
    source = "http://hl7.org/fhir/StructureDefinition/AdverseEvent",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AdverseEvent extends DomainResource {
    @Summary
    private final Identifier identifier;
    @Summary
    @Binding(
        bindingName = "AdverseEventActuality",
        strength = BindingStrength.Value.REQUIRED,
        description = "Overall nature of the adverse event, e.g. real or potential.",
        valueSet = "http://hl7.org/fhir/ValueSet/adverse-event-actuality|4.3.0-cibuild"
    )
    @Required
    private final AdverseEventActuality actuality;
    @Summary
    @Binding(
        bindingName = "AdverseEventCategory",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Overall categorization of the event, e.g. product-related or situational.",
        valueSet = "http://hl7.org/fhir/ValueSet/adverse-event-category"
    )
    private final List<CodeableConcept> category;
    @Summary
    @Binding(
        bindingName = "AdverseEventType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Detailed type of event.",
        valueSet = "http://hl7.org/fhir/ValueSet/adverse-event-type"
    )
    private final CodeableConcept event;
    @Summary
    @ReferenceTarget({ "Patient", "Group", "Practitioner", "RelatedPerson" })
    @Required
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Summary
    private final DateTime date;
    @Summary
    private final DateTime detected;
    @Summary
    private final DateTime recordedDate;
    @Summary
    @ReferenceTarget({ "Condition" })
    private final List<Reference> resultingCondition;
    @Summary
    @ReferenceTarget({ "Location" })
    private final Reference location;
    @Summary
    @Binding(
        bindingName = "AdverseEventSeriousness",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Overall seriousness of this event for the patient.",
        valueSet = "http://hl7.org/fhir/ValueSet/adverse-event-seriousness"
    )
    private final CodeableConcept seriousness;
    @Summary
    @Binding(
        bindingName = "AdverseEventSeverity",
        strength = BindingStrength.Value.REQUIRED,
        description = "The severity of the adverse event itself, in direct relation to the subject.",
        valueSet = "http://hl7.org/fhir/ValueSet/adverse-event-severity|4.3.0-cibuild"
    )
    private final CodeableConcept severity;
    @Summary
    @Binding(
        bindingName = "AdverseEventOutcome",
        strength = BindingStrength.Value.REQUIRED,
        description = "TODO (and should this be required?).",
        valueSet = "http://hl7.org/fhir/ValueSet/adverse-event-outcome|4.3.0-cibuild"
    )
    private final CodeableConcept outcome;
    @Summary
    @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "RelatedPerson" })
    private final Reference recorder;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Device" })
    private final List<Reference> contributor;
    @Summary
    private final List<SuspectEntity> suspectEntity;
    @Summary
    @ReferenceTarget({ "Condition", "Observation", "AllergyIntolerance", "FamilyMemberHistory", "Immunization", "Procedure", "Media", "DocumentReference" })
    private final List<Reference> subjectMedicalHistory;
    @Summary
    @ReferenceTarget({ "DocumentReference" })
    private final List<Reference> referenceDocument;
    @Summary
    @ReferenceTarget({ "ResearchStudy" })
    private final List<Reference> study;

    private AdverseEvent(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        actuality = builder.actuality;
        category = Collections.unmodifiableList(builder.category);
        event = builder.event;
        subject = builder.subject;
        encounter = builder.encounter;
        date = builder.date;
        detected = builder.detected;
        recordedDate = builder.recordedDate;
        resultingCondition = Collections.unmodifiableList(builder.resultingCondition);
        location = builder.location;
        seriousness = builder.seriousness;
        severity = builder.severity;
        outcome = builder.outcome;
        recorder = builder.recorder;
        contributor = Collections.unmodifiableList(builder.contributor);
        suspectEntity = Collections.unmodifiableList(builder.suspectEntity);
        subjectMedicalHistory = Collections.unmodifiableList(builder.subjectMedicalHistory);
        referenceDocument = Collections.unmodifiableList(builder.referenceDocument);
        study = Collections.unmodifiableList(builder.study);
    }

    /**
     * Business identifiers assigned to this adverse event by the performer or other systems which remain constant as the 
     * resource is updated and propagates from server to server.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * Whether the event actually happened, or just had the potential to. Note that this is independent of whether anyone was 
     * affected or harmed or how severely.
     * 
     * @return
     *     An immutable object of type {@link AdverseEventActuality} that is non-null.
     */
    public AdverseEventActuality getActuality() {
        return actuality;
    }

    /**
     * The overall type of event, intended for search and filtering purposes.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * This element defines the specific type of event that occurred or that was prevented from occurring.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getEvent() {
        return event;
    }

    /**
     * This subject or group impacted by the event.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The Encounter during which AdverseEvent was created or to which the creation of this record is tightly associated.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * The date (and perhaps time) when the adverse event occurred.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * Estimated or actual date the AdverseEvent began, in the opinion of the reporter.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDetected() {
        return detected;
    }

    /**
     * The date on which the existence of the AdverseEvent was first recorded.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getRecordedDate() {
        return recordedDate;
    }

    /**
     * Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a 
     * chemical).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getResultingCondition() {
        return resultingCondition;
    }

    /**
     * The information about where the adverse event occurred.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getLocation() {
        return location;
    }

    /**
     * Assessment whether this event was of real importance.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSeriousness() {
        return seriousness;
    }

    /**
     * Describes the severity of the adverse event, in relation to the subject. Contrast to AdverseEvent.seriousness - a 
     * severe rash might not be serious, but a mild heart problem is.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSeverity() {
        return severity;
    }

    /**
     * Describes the type of outcome from the adverse event.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getOutcome() {
        return outcome;
    }

    /**
     * Information on who recorded the adverse event. May be the patient or a practitioner.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRecorder() {
        return recorder;
    }

    /**
     * Parties that may or should contribute or have contributed information to the adverse event, which can consist of one 
     * or more activities. Such information includes information leading to the decision to perform the activity and how to 
     * perform the activity (e.g. consultant), information that the activity itself seeks to reveal (e.g. informant of 
     * clinical history), or information about what activity was performed (e.g. informant witness).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getContributor() {
        return contributor;
    }

    /**
     * Describes the entity that is suspected to have caused the adverse event.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link SuspectEntity} that may be empty.
     */
    public List<SuspectEntity> getSuspectEntity() {
        return suspectEntity;
    }

    /**
     * AdverseEvent.subjectMedicalHistory.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSubjectMedicalHistory() {
        return subjectMedicalHistory;
    }

    /**
     * AdverseEvent.referenceDocument.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReferenceDocument() {
        return referenceDocument;
    }

    /**
     * AdverseEvent.study.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getStudy() {
        return study;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (identifier != null) || 
            (actuality != null) || 
            !category.isEmpty() || 
            (event != null) || 
            (subject != null) || 
            (encounter != null) || 
            (date != null) || 
            (detected != null) || 
            (recordedDate != null) || 
            !resultingCondition.isEmpty() || 
            (location != null) || 
            (seriousness != null) || 
            (severity != null) || 
            (outcome != null) || 
            (recorder != null) || 
            !contributor.isEmpty() || 
            !suspectEntity.isEmpty() || 
            !subjectMedicalHistory.isEmpty() || 
            !referenceDocument.isEmpty() || 
            !study.isEmpty();
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
                accept(actuality, "actuality", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(event, "event", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(date, "date", visitor);
                accept(detected, "detected", visitor);
                accept(recordedDate, "recordedDate", visitor);
                accept(resultingCondition, "resultingCondition", visitor, Reference.class);
                accept(location, "location", visitor);
                accept(seriousness, "seriousness", visitor);
                accept(severity, "severity", visitor);
                accept(outcome, "outcome", visitor);
                accept(recorder, "recorder", visitor);
                accept(contributor, "contributor", visitor, Reference.class);
                accept(suspectEntity, "suspectEntity", visitor, SuspectEntity.class);
                accept(subjectMedicalHistory, "subjectMedicalHistory", visitor, Reference.class);
                accept(referenceDocument, "referenceDocument", visitor, Reference.class);
                accept(study, "study", visitor, Reference.class);
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
        AdverseEvent other = (AdverseEvent) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(actuality, other.actuality) && 
            Objects.equals(category, other.category) && 
            Objects.equals(event, other.event) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(date, other.date) && 
            Objects.equals(detected, other.detected) && 
            Objects.equals(recordedDate, other.recordedDate) && 
            Objects.equals(resultingCondition, other.resultingCondition) && 
            Objects.equals(location, other.location) && 
            Objects.equals(seriousness, other.seriousness) && 
            Objects.equals(severity, other.severity) && 
            Objects.equals(outcome, other.outcome) && 
            Objects.equals(recorder, other.recorder) && 
            Objects.equals(contributor, other.contributor) && 
            Objects.equals(suspectEntity, other.suspectEntity) && 
            Objects.equals(subjectMedicalHistory, other.subjectMedicalHistory) && 
            Objects.equals(referenceDocument, other.referenceDocument) && 
            Objects.equals(study, other.study);
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
                actuality, 
                category, 
                event, 
                subject, 
                encounter, 
                date, 
                detected, 
                recordedDate, 
                resultingCondition, 
                location, 
                seriousness, 
                severity, 
                outcome, 
                recorder, 
                contributor, 
                suspectEntity, 
                subjectMedicalHistory, 
                referenceDocument, 
                study);
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
        private AdverseEventActuality actuality;
        private List<CodeableConcept> category = new ArrayList<>();
        private CodeableConcept event;
        private Reference subject;
        private Reference encounter;
        private DateTime date;
        private DateTime detected;
        private DateTime recordedDate;
        private List<Reference> resultingCondition = new ArrayList<>();
        private Reference location;
        private CodeableConcept seriousness;
        private CodeableConcept severity;
        private CodeableConcept outcome;
        private Reference recorder;
        private List<Reference> contributor = new ArrayList<>();
        private List<SuspectEntity> suspectEntity = new ArrayList<>();
        private List<Reference> subjectMedicalHistory = new ArrayList<>();
        private List<Reference> referenceDocument = new ArrayList<>();
        private List<Reference> study = new ArrayList<>();

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
         * Business identifiers assigned to this adverse event by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * 
         * @param identifier
         *     Business identifier for the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * Whether the event actually happened, or just had the potential to. Note that this is independent of whether anyone was 
         * affected or harmed or how severely.
         * 
         * <p>This element is required.
         * 
         * @param actuality
         *     actual | potential
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder actuality(AdverseEventActuality actuality) {
            this.actuality = actuality;
            return this;
        }

        /**
         * The overall type of event, intended for search and filtering purposes.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     product-problem | product-quality | product-use-error | wrong-dose | incorrect-prescribing-information | wrong-
         *     technique | wrong-route-of-administration | wrong-rate | wrong-duration | wrong-time | expired-drug | medical-device-
         *     use-error | problem-different-manufacturer | unsafe-physical-environment
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
         * The overall type of event, intended for search and filtering purposes.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     product-problem | product-quality | product-use-error | wrong-dose | incorrect-prescribing-information | wrong-
         *     technique | wrong-route-of-administration | wrong-rate | wrong-duration | wrong-time | expired-drug | medical-device-
         *     use-error | problem-different-manufacturer | unsafe-physical-environment
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
         * This element defines the specific type of event that occurred or that was prevented from occurring.
         * 
         * @param event
         *     Type of the event itself in relation to the subject
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder event(CodeableConcept event) {
            this.event = event;
            return this;
        }

        /**
         * This subject or group impacted by the event.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param subject
         *     Subject impacted by event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The Encounter during which AdverseEvent was created or to which the creation of this record is tightly associated.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
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
         * The date (and perhaps time) when the adverse event occurred.
         * 
         * @param date
         *     When the event occurred
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * Estimated or actual date the AdverseEvent began, in the opinion of the reporter.
         * 
         * @param detected
         *     When the event was detected
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder detected(DateTime detected) {
            this.detected = detected;
            return this;
        }

        /**
         * The date on which the existence of the AdverseEvent was first recorded.
         * 
         * @param recordedDate
         *     When the event was recorded
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recordedDate(DateTime recordedDate) {
            this.recordedDate = recordedDate;
            return this;
        }

        /**
         * Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a 
         * chemical).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * </ul>
         * 
         * @param resultingCondition
         *     Effect on the subject due to this event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder resultingCondition(Reference... resultingCondition) {
            for (Reference value : resultingCondition) {
                this.resultingCondition.add(value);
            }
            return this;
        }

        /**
         * Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a 
         * chemical).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * </ul>
         * 
         * @param resultingCondition
         *     Effect on the subject due to this event
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder resultingCondition(Collection<Reference> resultingCondition) {
            this.resultingCondition = new ArrayList<>(resultingCondition);
            return this;
        }

        /**
         * The information about where the adverse event occurred.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param location
         *     Location where adverse event occurred
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(Reference location) {
            this.location = location;
            return this;
        }

        /**
         * Assessment whether this event was of real importance.
         * 
         * @param seriousness
         *     Seriousness of the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder seriousness(CodeableConcept seriousness) {
            this.seriousness = seriousness;
            return this;
        }

        /**
         * Describes the severity of the adverse event, in relation to the subject. Contrast to AdverseEvent.seriousness - a 
         * severe rash might not be serious, but a mild heart problem is.
         * 
         * @param severity
         *     mild | moderate | severe
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder severity(CodeableConcept severity) {
            this.severity = severity;
            return this;
        }

        /**
         * Describes the type of outcome from the adverse event.
         * 
         * @param outcome
         *     resolved | recovering | ongoing | resolvedWithSequelae | fatal | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder outcome(CodeableConcept outcome) {
            this.outcome = outcome;
            return this;
        }

        /**
         * Information on who recorded the adverse event. May be the patient or a practitioner.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param recorder
         *     Who recorded the adverse event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recorder(Reference recorder) {
            this.recorder = recorder;
            return this;
        }

        /**
         * Parties that may or should contribute or have contributed information to the adverse event, which can consist of one 
         * or more activities. Such information includes information leading to the decision to perform the activity and how to 
         * perform the activity (e.g. consultant), information that the activity itself seeks to reveal (e.g. informant of 
         * clinical history), or information about what activity was performed (e.g. informant witness).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param contributor
         *     Who was involved in the adverse event or the potential adverse event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contributor(Reference... contributor) {
            for (Reference value : contributor) {
                this.contributor.add(value);
            }
            return this;
        }

        /**
         * Parties that may or should contribute or have contributed information to the adverse event, which can consist of one 
         * or more activities. Such information includes information leading to the decision to perform the activity and how to 
         * perform the activity (e.g. consultant), information that the activity itself seeks to reveal (e.g. informant of 
         * clinical history), or information about what activity was performed (e.g. informant witness).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param contributor
         *     Who was involved in the adverse event or the potential adverse event
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contributor(Collection<Reference> contributor) {
            this.contributor = new ArrayList<>(contributor);
            return this;
        }

        /**
         * Describes the entity that is suspected to have caused the adverse event.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param suspectEntity
         *     The suspected agent causing the adverse event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder suspectEntity(SuspectEntity... suspectEntity) {
            for (SuspectEntity value : suspectEntity) {
                this.suspectEntity.add(value);
            }
            return this;
        }

        /**
         * Describes the entity that is suspected to have caused the adverse event.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param suspectEntity
         *     The suspected agent causing the adverse event
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder suspectEntity(Collection<SuspectEntity> suspectEntity) {
            this.suspectEntity = new ArrayList<>(suspectEntity);
            return this;
        }

        /**
         * AdverseEvent.subjectMedicalHistory.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link AllergyIntolerance}</li>
         * <li>{@link FamilyMemberHistory}</li>
         * <li>{@link Immunization}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Media}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param subjectMedicalHistory
         *     AdverseEvent.subjectMedicalHistory
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subjectMedicalHistory(Reference... subjectMedicalHistory) {
            for (Reference value : subjectMedicalHistory) {
                this.subjectMedicalHistory.add(value);
            }
            return this;
        }

        /**
         * AdverseEvent.subjectMedicalHistory.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link AllergyIntolerance}</li>
         * <li>{@link FamilyMemberHistory}</li>
         * <li>{@link Immunization}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Media}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param subjectMedicalHistory
         *     AdverseEvent.subjectMedicalHistory
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder subjectMedicalHistory(Collection<Reference> subjectMedicalHistory) {
            this.subjectMedicalHistory = new ArrayList<>(subjectMedicalHistory);
            return this;
        }

        /**
         * AdverseEvent.referenceDocument.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param referenceDocument
         *     AdverseEvent.referenceDocument
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder referenceDocument(Reference... referenceDocument) {
            for (Reference value : referenceDocument) {
                this.referenceDocument.add(value);
            }
            return this;
        }

        /**
         * AdverseEvent.referenceDocument.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param referenceDocument
         *     AdverseEvent.referenceDocument
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder referenceDocument(Collection<Reference> referenceDocument) {
            this.referenceDocument = new ArrayList<>(referenceDocument);
            return this;
        }

        /**
         * AdverseEvent.study.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ResearchStudy}</li>
         * </ul>
         * 
         * @param study
         *     AdverseEvent.study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder study(Reference... study) {
            for (Reference value : study) {
                this.study.add(value);
            }
            return this;
        }

        /**
         * AdverseEvent.study.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ResearchStudy}</li>
         * </ul>
         * 
         * @param study
         *     AdverseEvent.study
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder study(Collection<Reference> study) {
            this.study = new ArrayList<>(study);
            return this;
        }

        /**
         * Build the {@link AdverseEvent}
         * 
         * <p>Required elements:
         * <ul>
         * <li>actuality</li>
         * <li>subject</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link AdverseEvent}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid AdverseEvent per the base specification
         */
        @Override
        public AdverseEvent build() {
            AdverseEvent adverseEvent = new AdverseEvent(this);
            if (validating) {
                validate(adverseEvent);
            }
            return adverseEvent;
        }

        protected void validate(AdverseEvent adverseEvent) {
            super.validate(adverseEvent);
            ValidationSupport.requireNonNull(adverseEvent.actuality, "actuality");
            ValidationSupport.checkList(adverseEvent.category, "category", CodeableConcept.class);
            ValidationSupport.requireNonNull(adverseEvent.subject, "subject");
            ValidationSupport.checkList(adverseEvent.resultingCondition, "resultingCondition", Reference.class);
            ValidationSupport.checkList(adverseEvent.contributor, "contributor", Reference.class);
            ValidationSupport.checkList(adverseEvent.suspectEntity, "suspectEntity", SuspectEntity.class);
            ValidationSupport.checkList(adverseEvent.subjectMedicalHistory, "subjectMedicalHistory", Reference.class);
            ValidationSupport.checkList(adverseEvent.referenceDocument, "referenceDocument", Reference.class);
            ValidationSupport.checkList(adverseEvent.study, "study", Reference.class);
            ValidationSupport.checkValueSetBinding(adverseEvent.severity, "severity", "http://hl7.org/fhir/ValueSet/adverse-event-severity", "http://terminology.hl7.org/CodeSystem/adverse-event-severity", "mild", "moderate", "severe");
            ValidationSupport.checkValueSetBinding(adverseEvent.outcome, "outcome", "http://hl7.org/fhir/ValueSet/adverse-event-outcome", "http://terminology.hl7.org/CodeSystem/adverse-event-outcome", "resolved", "recovering", "ongoing", "resolvedWithSequelae", "fatal", "unknown");
            ValidationSupport.checkReferenceType(adverseEvent.subject, "subject", "Patient", "Group", "Practitioner", "RelatedPerson");
            ValidationSupport.checkReferenceType(adverseEvent.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(adverseEvent.resultingCondition, "resultingCondition", "Condition");
            ValidationSupport.checkReferenceType(adverseEvent.location, "location", "Location");
            ValidationSupport.checkReferenceType(adverseEvent.recorder, "recorder", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson");
            ValidationSupport.checkReferenceType(adverseEvent.contributor, "contributor", "Practitioner", "PractitionerRole", "Device");
            ValidationSupport.checkReferenceType(adverseEvent.subjectMedicalHistory, "subjectMedicalHistory", "Condition", "Observation", "AllergyIntolerance", "FamilyMemberHistory", "Immunization", "Procedure", "Media", "DocumentReference");
            ValidationSupport.checkReferenceType(adverseEvent.referenceDocument, "referenceDocument", "DocumentReference");
            ValidationSupport.checkReferenceType(adverseEvent.study, "study", "ResearchStudy");
        }

        protected Builder from(AdverseEvent adverseEvent) {
            super.from(adverseEvent);
            identifier = adverseEvent.identifier;
            actuality = adverseEvent.actuality;
            category.addAll(adverseEvent.category);
            event = adverseEvent.event;
            subject = adverseEvent.subject;
            encounter = adverseEvent.encounter;
            date = adverseEvent.date;
            detected = adverseEvent.detected;
            recordedDate = adverseEvent.recordedDate;
            resultingCondition.addAll(adverseEvent.resultingCondition);
            location = adverseEvent.location;
            seriousness = adverseEvent.seriousness;
            severity = adverseEvent.severity;
            outcome = adverseEvent.outcome;
            recorder = adverseEvent.recorder;
            contributor.addAll(adverseEvent.contributor);
            suspectEntity.addAll(adverseEvent.suspectEntity);
            subjectMedicalHistory.addAll(adverseEvent.subjectMedicalHistory);
            referenceDocument.addAll(adverseEvent.referenceDocument);
            study.addAll(adverseEvent.study);
            return this;
        }
    }

    /**
     * Describes the entity that is suspected to have caused the adverse event.
     */
    public static class SuspectEntity extends BackboneElement {
        @Summary
        @ReferenceTarget({ "Immunization", "Procedure", "Substance", "Medication", "MedicationAdministration", "MedicationStatement", "Device" })
        @Required
        private final Reference instance;
        @Summary
        private final List<Causality> causality;

        private SuspectEntity(Builder builder) {
            super(builder);
            instance = builder.instance;
            causality = Collections.unmodifiableList(builder.causality);
        }

        /**
         * Identifies the actual instance of what caused the adverse event. May be a substance, medication, medication 
         * administration, medication statement or a device.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getInstance() {
            return instance;
        }

        /**
         * Information on the possible cause of the event.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Causality} that may be empty.
         */
        public List<Causality> getCausality() {
            return causality;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (instance != null) || 
                !causality.isEmpty();
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
                    accept(instance, "instance", visitor);
                    accept(causality, "causality", visitor, Causality.class);
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
            SuspectEntity other = (SuspectEntity) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(instance, other.instance) && 
                Objects.equals(causality, other.causality);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    instance, 
                    causality);
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
            private Reference instance;
            private List<Causality> causality = new ArrayList<>();

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
             * Identifies the actual instance of what caused the adverse event. May be a substance, medication, medication 
             * administration, medication statement or a device.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Immunization}</li>
             * <li>{@link Procedure}</li>
             * <li>{@link Substance}</li>
             * <li>{@link Medication}</li>
             * <li>{@link MedicationAdministration}</li>
             * <li>{@link MedicationStatement}</li>
             * <li>{@link Device}</li>
             * </ul>
             * 
             * @param instance
             *     Refers to the specific entity that caused the adverse event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder instance(Reference instance) {
                this.instance = instance;
                return this;
            }

            /**
             * Information on the possible cause of the event.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param causality
             *     Information on the possible cause of the event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder causality(Causality... causality) {
                for (Causality value : causality) {
                    this.causality.add(value);
                }
                return this;
            }

            /**
             * Information on the possible cause of the event.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param causality
             *     Information on the possible cause of the event
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder causality(Collection<Causality> causality) {
                this.causality = new ArrayList<>(causality);
                return this;
            }

            /**
             * Build the {@link SuspectEntity}
             * 
             * <p>Required elements:
             * <ul>
             * <li>instance</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link SuspectEntity}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid SuspectEntity per the base specification
             */
            @Override
            public SuspectEntity build() {
                SuspectEntity suspectEntity = new SuspectEntity(this);
                if (validating) {
                    validate(suspectEntity);
                }
                return suspectEntity;
            }

            protected void validate(SuspectEntity suspectEntity) {
                super.validate(suspectEntity);
                ValidationSupport.requireNonNull(suspectEntity.instance, "instance");
                ValidationSupport.checkList(suspectEntity.causality, "causality", Causality.class);
                ValidationSupport.checkReferenceType(suspectEntity.instance, "instance", "Immunization", "Procedure", "Substance", "Medication", "MedicationAdministration", "MedicationStatement", "Device");
                ValidationSupport.requireValueOrChildren(suspectEntity);
            }

            protected Builder from(SuspectEntity suspectEntity) {
                super.from(suspectEntity);
                instance = suspectEntity.instance;
                causality.addAll(suspectEntity.causality);
                return this;
            }
        }

        /**
         * Information on the possible cause of the event.
         */
        public static class Causality extends BackboneElement {
            @Summary
            @Binding(
                bindingName = "AdverseEventCausalityAssessment",
                strength = BindingStrength.Value.EXAMPLE,
                description = "Codes for the assessment of whether the entity caused the event.",
                valueSet = "http://hl7.org/fhir/ValueSet/adverse-event-causality-assess"
            )
            private final CodeableConcept assessment;
            @Summary
            private final String productRelatedness;
            @Summary
            @ReferenceTarget({ "Practitioner", "PractitionerRole" })
            private final Reference author;
            @Summary
            @Binding(
                bindingName = "AdverseEventCausalityMethod",
                strength = BindingStrength.Value.EXAMPLE,
                description = "TODO.",
                valueSet = "http://hl7.org/fhir/ValueSet/adverse-event-causality-method"
            )
            private final CodeableConcept method;

            private Causality(Builder builder) {
                super(builder);
                assessment = builder.assessment;
                productRelatedness = builder.productRelatedness;
                author = builder.author;
                method = builder.method;
            }

            /**
             * Assessment of if the entity caused the event.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getAssessment() {
                return assessment;
            }

            /**
             * AdverseEvent.suspectEntity.causalityProductRelatedness.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getProductRelatedness() {
                return productRelatedness;
            }

            /**
             * AdverseEvent.suspectEntity.causalityAuthor.
             * 
             * @return
             *     An immutable object of type {@link Reference} that may be null.
             */
            public Reference getAuthor() {
                return author;
            }

            /**
             * ProbabilityScale | Bayesian | Checklist.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getMethod() {
                return method;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (assessment != null) || 
                    (productRelatedness != null) || 
                    (author != null) || 
                    (method != null);
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
                        accept(assessment, "assessment", visitor);
                        accept(productRelatedness, "productRelatedness", visitor);
                        accept(author, "author", visitor);
                        accept(method, "method", visitor);
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
                Causality other = (Causality) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(assessment, other.assessment) && 
                    Objects.equals(productRelatedness, other.productRelatedness) && 
                    Objects.equals(author, other.author) && 
                    Objects.equals(method, other.method);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        assessment, 
                        productRelatedness, 
                        author, 
                        method);
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
                private CodeableConcept assessment;
                private String productRelatedness;
                private Reference author;
                private CodeableConcept method;

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
                 * Assessment of if the entity caused the event.
                 * 
                 * @param assessment
                 *     Assessment of if the entity caused the event
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder assessment(CodeableConcept assessment) {
                    this.assessment = assessment;
                    return this;
                }

                /**
                 * Convenience method for setting {@code productRelatedness}.
                 * 
                 * @param productRelatedness
                 *     AdverseEvent.suspectEntity.causalityProductRelatedness
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #productRelatedness(com.ibm.fhir.model.type.String)
                 */
                public Builder productRelatedness(java.lang.String productRelatedness) {
                    this.productRelatedness = (productRelatedness == null) ? null : String.of(productRelatedness);
                    return this;
                }

                /**
                 * AdverseEvent.suspectEntity.causalityProductRelatedness.
                 * 
                 * @param productRelatedness
                 *     AdverseEvent.suspectEntity.causalityProductRelatedness
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder productRelatedness(String productRelatedness) {
                    this.productRelatedness = productRelatedness;
                    return this;
                }

                /**
                 * AdverseEvent.suspectEntity.causalityAuthor.
                 * 
                 * <p>Allowed resource types for this reference:
                 * <ul>
                 * <li>{@link Practitioner}</li>
                 * <li>{@link PractitionerRole}</li>
                 * </ul>
                 * 
                 * @param author
                 *     AdverseEvent.suspectEntity.causalityAuthor
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder author(Reference author) {
                    this.author = author;
                    return this;
                }

                /**
                 * ProbabilityScale | Bayesian | Checklist.
                 * 
                 * @param method
                 *     ProbabilityScale | Bayesian | Checklist
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder method(CodeableConcept method) {
                    this.method = method;
                    return this;
                }

                /**
                 * Build the {@link Causality}
                 * 
                 * @return
                 *     An immutable object of type {@link Causality}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Causality per the base specification
                 */
                @Override
                public Causality build() {
                    Causality causality = new Causality(this);
                    if (validating) {
                        validate(causality);
                    }
                    return causality;
                }

                protected void validate(Causality causality) {
                    super.validate(causality);
                    ValidationSupport.checkReferenceType(causality.author, "author", "Practitioner", "PractitionerRole");
                    ValidationSupport.requireValueOrChildren(causality);
                }

                protected Builder from(Causality causality) {
                    super.from(causality);
                    assessment = causality.assessment;
                    productRelatedness = causality.productRelatedness;
                    author = causality.author;
                    method = causality.method;
                    return this;
                }
            }
        }
    }
}
