/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.annotation.Binding;
import org.linuxforhealth.fhir.model.annotation.Choice;
import org.linuxforhealth.fhir.model.annotation.Maturity;
import org.linuxforhealth.fhir.model.annotation.ReferenceTarget;
import org.linuxforhealth.fhir.model.annotation.Required;
import org.linuxforhealth.fhir.model.annotation.Summary;
import org.linuxforhealth.fhir.model.type.Annotation;
import org.linuxforhealth.fhir.model.type.BackboneElement;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Period;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.String;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.BindingStrength;
import org.linuxforhealth.fhir.model.type.code.ClinicalImpressionStatus;
import org.linuxforhealth.fhir.model.type.code.StandardsStatus;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning 
 * the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with 
 * a clinical consultation / encounter, but this varies greatly depending on the clinical workflow. This resource is 
 * called "ClinicalImpression" rather than "ClinicalAssessment" to avoid confusion with the recording of assessment tools 
 * such as Apgar score.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ClinicalImpression extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "ClinicalImpressionStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The workflow state of a clinical impression.",
        valueSet = "http://hl7.org/fhir/ValueSet/clinicalimpression-status|4.3.0"
    )
    @Required
    private final ClinicalImpressionStatus status;
    @Binding(
        bindingName = "ClinicalImpressionStatusReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes identifying the reason for the current state of a clinical impression."
    )
    private final CodeableConcept statusReason;
    @Summary
    @Binding(
        bindingName = "ClinicalImpressionCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Identifies categories of clinical impressions.  This is a place-holder only.  It may be removed."
    )
    private final CodeableConcept code;
    @Summary
    private final String description;
    @Summary
    @ReferenceTarget({ "Patient", "Group" })
    @Required
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Summary
    @Choice({ DateTime.class, Period.class })
    private final Element effective;
    @Summary
    private final DateTime date;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole" })
    private final Reference assessor;
    @ReferenceTarget({ "ClinicalImpression" })
    private final Reference previous;
    @Summary
    @ReferenceTarget({ "Condition", "AllergyIntolerance" })
    private final List<Reference> problem;
    private final List<Investigation> investigation;
    private final List<Uri> protocol;
    private final String summary;
    private final List<Finding> finding;
    @Binding(
        bindingName = "ClinicalImpressionPrognosis",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Prognosis or outlook findings.",
        valueSet = "http://hl7.org/fhir/ValueSet/clinicalimpression-prognosis"
    )
    private final List<CodeableConcept> prognosisCodeableConcept;
    @ReferenceTarget({ "RiskAssessment" })
    private final List<Reference> prognosisReference;
    private final List<Reference> supportingInfo;
    private final List<Annotation> note;

    private ClinicalImpression(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        statusReason = builder.statusReason;
        code = builder.code;
        description = builder.description;
        subject = builder.subject;
        encounter = builder.encounter;
        effective = builder.effective;
        date = builder.date;
        assessor = builder.assessor;
        previous = builder.previous;
        problem = Collections.unmodifiableList(builder.problem);
        investigation = Collections.unmodifiableList(builder.investigation);
        protocol = Collections.unmodifiableList(builder.protocol);
        summary = builder.summary;
        finding = Collections.unmodifiableList(builder.finding);
        prognosisCodeableConcept = Collections.unmodifiableList(builder.prognosisCodeableConcept);
        prognosisReference = Collections.unmodifiableList(builder.prognosisReference);
        supportingInfo = Collections.unmodifiableList(builder.supportingInfo);
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * Business identifiers assigned to this clinical impression by the performer or other systems which remain constant as 
     * the resource is updated and propagates from server to server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Identifies the workflow status of the assessment.
     * 
     * @return
     *     An immutable object of type {@link ClinicalImpressionStatus} that is non-null.
     */
    public ClinicalImpressionStatus getStatus() {
        return status;
    }

    /**
     * Captures the reason for the current state of the ClinicalImpression.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatusReason() {
        return statusReason;
    }

    /**
     * Categorizes the type of clinical assessment performed.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * A summary of the context and/or cause of the assessment - why / where it was performed, and what patient events/status 
     * prompted it.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * The patient or group of individuals assessed as part of this record.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The Encounter during which this ClinicalImpression was created or to which the creation of this record is tightly 
     * associated.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * The point in time or period over which the subject was assessed.
     * 
     * @return
     *     An immutable object of type {@link DateTime} or {@link Period} that may be null.
     */
    public Element getEffective() {
        return effective;
    }

    /**
     * Indicates when the documentation of the assessment was complete.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDate() {
        return date;
    }

    /**
     * The clinician performing the assessment.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAssessor() {
        return assessor;
    }

    /**
     * A reference to the last assessment that was conducted on this patient. Assessments are often/usually ongoing in 
     * nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the 
     * patient's conditions changes.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPrevious() {
        return previous;
    }

    /**
     * A list of the relevant problems/conditions for a patient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getProblem() {
        return problem;
    }

    /**
     * One or more sets of investigations (signs, symptoms, etc.). The actual grouping of investigations varies greatly 
     * depending on the type and context of the assessment. These investigations may include data generated during the 
     * assessment process, or data previously generated and recorded that is pertinent to the outcomes.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Investigation} that may be empty.
     */
    public List<Investigation> getInvestigation() {
        return investigation;
    }

    /**
     * Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides 
     * evidence in support of the diagnosis.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri} that may be empty.
     */
    public List<Uri> getProtocol() {
        return protocol;
    }

    /**
     * A text summary of the investigations and the diagnosis.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Specific findings or diagnoses that were considered likely or relevant to ongoing treatment.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Finding} that may be empty.
     */
    public List<Finding> getFinding() {
        return finding;
    }

    /**
     * Estimate of likely outcome.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getPrognosisCodeableConcept() {
        return prognosisCodeableConcept;
    }

    /**
     * RiskAssessment expressing likely outcome.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPrognosisReference() {
        return prognosisReference;
    }

    /**
     * Information supporting the clinical impression.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * Commentary about the impression, typically recorded after the impression itself was made, though supplemental notes by 
     * the original author could also appear.
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
            !identifier.isEmpty() || 
            (status != null) || 
            (statusReason != null) || 
            (code != null) || 
            (description != null) || 
            (subject != null) || 
            (encounter != null) || 
            (effective != null) || 
            (date != null) || 
            (assessor != null) || 
            (previous != null) || 
            !problem.isEmpty() || 
            !investigation.isEmpty() || 
            !protocol.isEmpty() || 
            (summary != null) || 
            !finding.isEmpty() || 
            !prognosisCodeableConcept.isEmpty() || 
            !prognosisReference.isEmpty() || 
            !supportingInfo.isEmpty() || 
            !note.isEmpty();
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
                accept(statusReason, "statusReason", visitor);
                accept(code, "code", visitor);
                accept(description, "description", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(effective, "effective", visitor);
                accept(date, "date", visitor);
                accept(assessor, "assessor", visitor);
                accept(previous, "previous", visitor);
                accept(problem, "problem", visitor, Reference.class);
                accept(investigation, "investigation", visitor, Investigation.class);
                accept(protocol, "protocol", visitor, Uri.class);
                accept(summary, "summary", visitor);
                accept(finding, "finding", visitor, Finding.class);
                accept(prognosisCodeableConcept, "prognosisCodeableConcept", visitor, CodeableConcept.class);
                accept(prognosisReference, "prognosisReference", visitor, Reference.class);
                accept(supportingInfo, "supportingInfo", visitor, Reference.class);
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
        ClinicalImpression other = (ClinicalImpression) obj;
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
            Objects.equals(statusReason, other.statusReason) && 
            Objects.equals(code, other.code) && 
            Objects.equals(description, other.description) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(effective, other.effective) && 
            Objects.equals(date, other.date) && 
            Objects.equals(assessor, other.assessor) && 
            Objects.equals(previous, other.previous) && 
            Objects.equals(problem, other.problem) && 
            Objects.equals(investigation, other.investigation) && 
            Objects.equals(protocol, other.protocol) && 
            Objects.equals(summary, other.summary) && 
            Objects.equals(finding, other.finding) && 
            Objects.equals(prognosisCodeableConcept, other.prognosisCodeableConcept) && 
            Objects.equals(prognosisReference, other.prognosisReference) && 
            Objects.equals(supportingInfo, other.supportingInfo) && 
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
                status, 
                statusReason, 
                code, 
                description, 
                subject, 
                encounter, 
                effective, 
                date, 
                assessor, 
                previous, 
                problem, 
                investigation, 
                protocol, 
                summary, 
                finding, 
                prognosisCodeableConcept, 
                prognosisReference, 
                supportingInfo, 
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

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private ClinicalImpressionStatus status;
        private CodeableConcept statusReason;
        private CodeableConcept code;
        private String description;
        private Reference subject;
        private Reference encounter;
        private Element effective;
        private DateTime date;
        private Reference assessor;
        private Reference previous;
        private List<Reference> problem = new ArrayList<>();
        private List<Investigation> investigation = new ArrayList<>();
        private List<Uri> protocol = new ArrayList<>();
        private String summary;
        private List<Finding> finding = new ArrayList<>();
        private List<CodeableConcept> prognosisCodeableConcept = new ArrayList<>();
        private List<Reference> prognosisReference = new ArrayList<>();
        private List<Reference> supportingInfo = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();

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
         * Business identifiers assigned to this clinical impression by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
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
         * Business identifiers assigned to this clinical impression by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
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
         * Identifies the workflow status of the assessment.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     in-progress | completed | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ClinicalImpressionStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Captures the reason for the current state of the ClinicalImpression.
         * 
         * @param statusReason
         *     Reason for current status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusReason(CodeableConcept statusReason) {
            this.statusReason = statusReason;
            return this;
        }

        /**
         * Categorizes the type of clinical assessment performed.
         * 
         * @param code
         *     Kind of assessment performed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * Convenience method for setting {@code description}.
         * 
         * @param description
         *     Why/how the assessment was performed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #description(org.linuxforhealth.fhir.model.type.String)
         */
        public Builder description(java.lang.String description) {
            this.description = (description == null) ? null : String.of(description);
            return this;
        }

        /**
         * A summary of the context and/or cause of the assessment - why / where it was performed, and what patient events/status 
         * prompted it.
         * 
         * @param description
         *     Why/how the assessment was performed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * The patient or group of individuals assessed as part of this record.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param subject
         *     Patient or group assessed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The Encounter during which this ClinicalImpression was created or to which the creation of this record is tightly 
         * associated.
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
         * The point in time or period over which the subject was assessed.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link DateTime}</li>
         * <li>{@link Period}</li>
         * </ul>
         * 
         * @param effective
         *     Time of assessment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effective(Element effective) {
            this.effective = effective;
            return this;
        }

        /**
         * Indicates when the documentation of the assessment was complete.
         * 
         * @param date
         *     When the assessment was documented
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        /**
         * The clinician performing the assessment.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param assessor
         *     The clinician performing the assessment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder assessor(Reference assessor) {
            this.assessor = assessor;
            return this;
        }

        /**
         * A reference to the last assessment that was conducted on this patient. Assessments are often/usually ongoing in 
         * nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the 
         * patient's conditions changes.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link ClinicalImpression}</li>
         * </ul>
         * 
         * @param previous
         *     Reference to last assessment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder previous(Reference previous) {
            this.previous = previous;
            return this;
        }

        /**
         * A list of the relevant problems/conditions for a patient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link AllergyIntolerance}</li>
         * </ul>
         * 
         * @param problem
         *     Relevant impressions of patient state
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder problem(Reference... problem) {
            for (Reference value : problem) {
                this.problem.add(value);
            }
            return this;
        }

        /**
         * A list of the relevant problems/conditions for a patient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link AllergyIntolerance}</li>
         * </ul>
         * 
         * @param problem
         *     Relevant impressions of patient state
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder problem(Collection<Reference> problem) {
            this.problem = new ArrayList<>(problem);
            return this;
        }

        /**
         * One or more sets of investigations (signs, symptoms, etc.). The actual grouping of investigations varies greatly 
         * depending on the type and context of the assessment. These investigations may include data generated during the 
         * assessment process, or data previously generated and recorded that is pertinent to the outcomes.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param investigation
         *     One or more sets of investigations (signs, symptoms, etc.)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder investigation(Investigation... investigation) {
            for (Investigation value : investigation) {
                this.investigation.add(value);
            }
            return this;
        }

        /**
         * One or more sets of investigations (signs, symptoms, etc.). The actual grouping of investigations varies greatly 
         * depending on the type and context of the assessment. These investigations may include data generated during the 
         * assessment process, or data previously generated and recorded that is pertinent to the outcomes.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param investigation
         *     One or more sets of investigations (signs, symptoms, etc.)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder investigation(Collection<Investigation> investigation) {
            this.investigation = new ArrayList<>(investigation);
            return this;
        }

        /**
         * Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides 
         * evidence in support of the diagnosis.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param protocol
         *     Clinical Protocol followed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder protocol(Uri... protocol) {
            for (Uri value : protocol) {
                this.protocol.add(value);
            }
            return this;
        }

        /**
         * Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides 
         * evidence in support of the diagnosis.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param protocol
         *     Clinical Protocol followed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder protocol(Collection<Uri> protocol) {
            this.protocol = new ArrayList<>(protocol);
            return this;
        }

        /**
         * Convenience method for setting {@code summary}.
         * 
         * @param summary
         *     Summary of the assessment
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #summary(org.linuxforhealth.fhir.model.type.String)
         */
        public Builder summary(java.lang.String summary) {
            this.summary = (summary == null) ? null : String.of(summary);
            return this;
        }

        /**
         * A text summary of the investigations and the diagnosis.
         * 
         * @param summary
         *     Summary of the assessment
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        /**
         * Specific findings or diagnoses that were considered likely or relevant to ongoing treatment.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param finding
         *     Possible or likely findings and diagnoses
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder finding(Finding... finding) {
            for (Finding value : finding) {
                this.finding.add(value);
            }
            return this;
        }

        /**
         * Specific findings or diagnoses that were considered likely or relevant to ongoing treatment.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param finding
         *     Possible or likely findings and diagnoses
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder finding(Collection<Finding> finding) {
            this.finding = new ArrayList<>(finding);
            return this;
        }

        /**
         * Estimate of likely outcome.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param prognosisCodeableConcept
         *     Estimate of likely outcome
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder prognosisCodeableConcept(CodeableConcept... prognosisCodeableConcept) {
            for (CodeableConcept value : prognosisCodeableConcept) {
                this.prognosisCodeableConcept.add(value);
            }
            return this;
        }

        /**
         * Estimate of likely outcome.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param prognosisCodeableConcept
         *     Estimate of likely outcome
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder prognosisCodeableConcept(Collection<CodeableConcept> prognosisCodeableConcept) {
            this.prognosisCodeableConcept = new ArrayList<>(prognosisCodeableConcept);
            return this;
        }

        /**
         * RiskAssessment expressing likely outcome.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link RiskAssessment}</li>
         * </ul>
         * 
         * @param prognosisReference
         *     RiskAssessment expressing likely outcome
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder prognosisReference(Reference... prognosisReference) {
            for (Reference value : prognosisReference) {
                this.prognosisReference.add(value);
            }
            return this;
        }

        /**
         * RiskAssessment expressing likely outcome.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link RiskAssessment}</li>
         * </ul>
         * 
         * @param prognosisReference
         *     RiskAssessment expressing likely outcome
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder prognosisReference(Collection<Reference> prognosisReference) {
            this.prognosisReference = new ArrayList<>(prognosisReference);
            return this;
        }

        /**
         * Information supporting the clinical impression.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInfo
         *     Information supporting the clinical impression
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInfo(Reference... supportingInfo) {
            for (Reference value : supportingInfo) {
                this.supportingInfo.add(value);
            }
            return this;
        }

        /**
         * Information supporting the clinical impression.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInfo
         *     Information supporting the clinical impression
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder supportingInfo(Collection<Reference> supportingInfo) {
            this.supportingInfo = new ArrayList<>(supportingInfo);
            return this;
        }

        /**
         * Commentary about the impression, typically recorded after the impression itself was made, though supplemental notes by 
         * the original author could also appear.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the ClinicalImpression
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
         * Commentary about the impression, typically recorded after the impression itself was made, though supplemental notes by 
         * the original author could also appear.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the ClinicalImpression
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
         * Build the {@link ClinicalImpression}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>subject</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ClinicalImpression}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ClinicalImpression per the base specification
         */
        @Override
        public ClinicalImpression build() {
            ClinicalImpression clinicalImpression = new ClinicalImpression(this);
            if (validating) {
                validate(clinicalImpression);
            }
            return clinicalImpression;
        }

        protected void validate(ClinicalImpression clinicalImpression) {
            super.validate(clinicalImpression);
            ValidationSupport.checkList(clinicalImpression.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(clinicalImpression.status, "status");
            ValidationSupport.requireNonNull(clinicalImpression.subject, "subject");
            ValidationSupport.choiceElement(clinicalImpression.effective, "effective", DateTime.class, Period.class);
            ValidationSupport.checkList(clinicalImpression.problem, "problem", Reference.class);
            ValidationSupport.checkList(clinicalImpression.investigation, "investigation", Investigation.class);
            ValidationSupport.checkList(clinicalImpression.protocol, "protocol", Uri.class);
            ValidationSupport.checkList(clinicalImpression.finding, "finding", Finding.class);
            ValidationSupport.checkList(clinicalImpression.prognosisCodeableConcept, "prognosisCodeableConcept", CodeableConcept.class);
            ValidationSupport.checkList(clinicalImpression.prognosisReference, "prognosisReference", Reference.class);
            ValidationSupport.checkList(clinicalImpression.supportingInfo, "supportingInfo", Reference.class);
            ValidationSupport.checkList(clinicalImpression.note, "note", Annotation.class);
            ValidationSupport.checkReferenceType(clinicalImpression.subject, "subject", "Patient", "Group");
            ValidationSupport.checkReferenceType(clinicalImpression.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(clinicalImpression.assessor, "assessor", "Practitioner", "PractitionerRole");
            ValidationSupport.checkReferenceType(clinicalImpression.previous, "previous", "ClinicalImpression");
            ValidationSupport.checkReferenceType(clinicalImpression.problem, "problem", "Condition", "AllergyIntolerance");
            ValidationSupport.checkReferenceType(clinicalImpression.prognosisReference, "prognosisReference", "RiskAssessment");
        }

        protected Builder from(ClinicalImpression clinicalImpression) {
            super.from(clinicalImpression);
            identifier.addAll(clinicalImpression.identifier);
            status = clinicalImpression.status;
            statusReason = clinicalImpression.statusReason;
            code = clinicalImpression.code;
            description = clinicalImpression.description;
            subject = clinicalImpression.subject;
            encounter = clinicalImpression.encounter;
            effective = clinicalImpression.effective;
            date = clinicalImpression.date;
            assessor = clinicalImpression.assessor;
            previous = clinicalImpression.previous;
            problem.addAll(clinicalImpression.problem);
            investigation.addAll(clinicalImpression.investigation);
            protocol.addAll(clinicalImpression.protocol);
            summary = clinicalImpression.summary;
            finding.addAll(clinicalImpression.finding);
            prognosisCodeableConcept.addAll(clinicalImpression.prognosisCodeableConcept);
            prognosisReference.addAll(clinicalImpression.prognosisReference);
            supportingInfo.addAll(clinicalImpression.supportingInfo);
            note.addAll(clinicalImpression.note);
            return this;
        }
    }

    /**
     * One or more sets of investigations (signs, symptoms, etc.). The actual grouping of investigations varies greatly 
     * depending on the type and context of the assessment. These investigations may include data generated during the 
     * assessment process, or data previously generated and recorded that is pertinent to the outcomes.
     */
    public static class Investigation extends BackboneElement {
        @Binding(
            bindingName = "InvestigationGroupType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A name/code for a set of investigations.",
            valueSet = "http://hl7.org/fhir/ValueSet/investigation-sets"
        )
        @Required
        private final CodeableConcept code;
        @ReferenceTarget({ "Observation", "QuestionnaireResponse", "FamilyMemberHistory", "DiagnosticReport", "RiskAssessment", "ImagingStudy", "Media" })
        private final List<Reference> item;

        private Investigation(Builder builder) {
            super(builder);
            code = builder.code;
            item = Collections.unmodifiableList(builder.item);
        }

        /**
         * A name/code for the group ("set") of investigations. Typically, this will be something like "signs", "symptoms", 
         * "clinical", "diagnostic", but the list is not constrained, and others such groups such as 
         * (exposure|family|travel|nutritional) history may be used.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * A record of a specific investigation that was undertaken.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getItem() {
            return item;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                !item.isEmpty();
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
                    accept(item, "item", visitor, Reference.class);
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
            Investigation other = (Investigation) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(item, other.item);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
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
            private CodeableConcept code;
            private List<Reference> item = new ArrayList<>();

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
             * A name/code for the group ("set") of investigations. Typically, this will be something like "signs", "symptoms", 
             * "clinical", "diagnostic", but the list is not constrained, and others such groups such as 
             * (exposure|family|travel|nutritional) history may be used.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     A name/code for the set
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * A record of a specific investigation that was undertaken.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Observation}</li>
             * <li>{@link QuestionnaireResponse}</li>
             * <li>{@link FamilyMemberHistory}</li>
             * <li>{@link DiagnosticReport}</li>
             * <li>{@link RiskAssessment}</li>
             * <li>{@link ImagingStudy}</li>
             * <li>{@link Media}</li>
             * </ul>
             * 
             * @param item
             *     Record of a specific investigation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(Reference... item) {
                for (Reference value : item) {
                    this.item.add(value);
                }
                return this;
            }

            /**
             * A record of a specific investigation that was undertaken.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Observation}</li>
             * <li>{@link QuestionnaireResponse}</li>
             * <li>{@link FamilyMemberHistory}</li>
             * <li>{@link DiagnosticReport}</li>
             * <li>{@link RiskAssessment}</li>
             * <li>{@link ImagingStudy}</li>
             * <li>{@link Media}</li>
             * </ul>
             * 
             * @param item
             *     Record of a specific investigation
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder item(Collection<Reference> item) {
                this.item = new ArrayList<>(item);
                return this;
            }

            /**
             * Build the {@link Investigation}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Investigation}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Investigation per the base specification
             */
            @Override
            public Investigation build() {
                Investigation investigation = new Investigation(this);
                if (validating) {
                    validate(investigation);
                }
                return investigation;
            }

            protected void validate(Investigation investigation) {
                super.validate(investigation);
                ValidationSupport.requireNonNull(investigation.code, "code");
                ValidationSupport.checkList(investigation.item, "item", Reference.class);
                ValidationSupport.checkReferenceType(investigation.item, "item", "Observation", "QuestionnaireResponse", "FamilyMemberHistory", "DiagnosticReport", "RiskAssessment", "ImagingStudy", "Media");
                ValidationSupport.requireValueOrChildren(investigation);
            }

            protected Builder from(Investigation investigation) {
                super.from(investigation);
                code = investigation.code;
                item.addAll(investigation.item);
                return this;
            }
        }
    }

    /**
     * Specific findings or diagnoses that were considered likely or relevant to ongoing treatment.
     */
    public static class Finding extends BackboneElement {
        @Binding(
            bindingName = "ConditionKind",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Identification of the Condition or diagnosis.",
            valueSet = "http://hl7.org/fhir/ValueSet/condition-code"
        )
        private final CodeableConcept itemCodeableConcept;
        @ReferenceTarget({ "Condition", "Observation", "Media" })
        private final Reference itemReference;
        private final String basis;

        private Finding(Builder builder) {
            super(builder);
            itemCodeableConcept = builder.itemCodeableConcept;
            itemReference = builder.itemReference;
            basis = builder.basis;
        }

        /**
         * Specific text or code for finding or diagnosis, which may include ruled-out or resolved conditions.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getItemCodeableConcept() {
            return itemCodeableConcept;
        }

        /**
         * Specific reference for finding or diagnosis, which may include ruled-out or resolved conditions.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getItemReference() {
            return itemReference;
        }

        /**
         * Which investigations support finding or diagnosis.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getBasis() {
            return basis;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (itemCodeableConcept != null) || 
                (itemReference != null) || 
                (basis != null);
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
                    accept(itemCodeableConcept, "itemCodeableConcept", visitor);
                    accept(itemReference, "itemReference", visitor);
                    accept(basis, "basis", visitor);
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
            Finding other = (Finding) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(itemCodeableConcept, other.itemCodeableConcept) && 
                Objects.equals(itemReference, other.itemReference) && 
                Objects.equals(basis, other.basis);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    itemCodeableConcept, 
                    itemReference, 
                    basis);
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
            private CodeableConcept itemCodeableConcept;
            private Reference itemReference;
            private String basis;

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
             * Specific text or code for finding or diagnosis, which may include ruled-out or resolved conditions.
             * 
             * @param itemCodeableConcept
             *     What was found
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder itemCodeableConcept(CodeableConcept itemCodeableConcept) {
                this.itemCodeableConcept = itemCodeableConcept;
                return this;
            }

            /**
             * Specific reference for finding or diagnosis, which may include ruled-out or resolved conditions.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Condition}</li>
             * <li>{@link Observation}</li>
             * <li>{@link Media}</li>
             * </ul>
             * 
             * @param itemReference
             *     What was found
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder itemReference(Reference itemReference) {
                this.itemReference = itemReference;
                return this;
            }

            /**
             * Convenience method for setting {@code basis}.
             * 
             * @param basis
             *     Which investigations support finding
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #basis(org.linuxforhealth.fhir.model.type.String)
             */
            public Builder basis(java.lang.String basis) {
                this.basis = (basis == null) ? null : String.of(basis);
                return this;
            }

            /**
             * Which investigations support finding or diagnosis.
             * 
             * @param basis
             *     Which investigations support finding
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder basis(String basis) {
                this.basis = basis;
                return this;
            }

            /**
             * Build the {@link Finding}
             * 
             * @return
             *     An immutable object of type {@link Finding}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Finding per the base specification
             */
            @Override
            public Finding build() {
                Finding finding = new Finding(this);
                if (validating) {
                    validate(finding);
                }
                return finding;
            }

            protected void validate(Finding finding) {
                super.validate(finding);
                ValidationSupport.checkReferenceType(finding.itemReference, "itemReference", "Condition", "Observation", "Media");
                ValidationSupport.requireValueOrChildren(finding);
            }

            protected Builder from(Finding finding) {
                super.from(finding);
                itemCodeableConcept = finding.itemCodeableConcept;
                itemReference = finding.itemReference;
                basis = finding.basis;
                return this;
            }
        }
    }
}
