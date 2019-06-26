/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.ClinicalImpressionStatus;
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
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning 
 * the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with 
 * a clinical consultation / encounter, but this varies greatly depending on the clinical workflow. This resource is 
 * called "ClinicalImpression" rather than "ClinicalAssessment" to avoid confusion with the recording of assessment tools 
 * such as Apgar score.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ClinicalImpression extends DomainResource {
    private final List<Identifier> identifier;
    private final ClinicalImpressionStatus status;
    private final CodeableConcept statusReason;
    private final CodeableConcept code;
    private final String description;
    private final Reference subject;
    private final Reference encounter;
    private final Element effective;
    private final DateTime date;
    private final Reference assessor;
    private final Reference previous;
    private final List<Reference> problem;
    private final List<Investigation> investigation;
    private final List<Uri> protocol;
    private final String summary;
    private final List<Finding> finding;
    private final List<CodeableConcept> prognosisCodeableConcept;
    private final List<Reference> prognosisReference;
    private final List<Reference> supportingInfo;
    private final List<Annotation> note;

    private ClinicalImpression(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.statusReason = builder.statusReason;
        this.code = builder.code;
        this.description = builder.description;
        this.subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        this.encounter = builder.encounter;
        this.effective = ValidationSupport.choiceElement(builder.effective, "effective", DateTime.class, Period.class);
        this.date = builder.date;
        this.assessor = builder.assessor;
        this.previous = builder.previous;
        this.problem = builder.problem;
        this.investigation = builder.investigation;
        this.protocol = builder.protocol;
        this.summary = builder.summary;
        this.finding = builder.finding;
        this.prognosisCodeableConcept = builder.prognosisCodeableConcept;
        this.prognosisReference = builder.prognosisReference;
        this.supportingInfo = builder.supportingInfo;
        this.note = builder.note;
    }

    /**
     * <p>
     * Business identifiers assigned to this clinical impression by the performer or other systems which remain constant as 
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
     * Identifies the workflow status of the assessment.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ClinicalImpressionStatus}.
     */
    public ClinicalImpressionStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Captures the reason for the current state of the ClinicalImpression.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getStatusReason() {
        return statusReason;
    }

    /**
     * <p>
     * Categorizes the type of clinical assessment performed.
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
     * A summary of the context and/or cause of the assessment - why / where it was performed, and what patient events/status 
     * prompted it.
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
     * The patient or group of individuals assessed as part of this record.
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
     * The Encounter during which this ClinicalImpression was created or to which the creation of this record is tightly 
     * associated.
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
     * The point in time or period over which the subject was assessed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getEffective() {
        return effective;
    }

    /**
     * <p>
     * Indicates when the documentation of the assessment was complete.
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
     * The clinician performing the assessment.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAssessor() {
        return assessor;
    }

    /**
     * <p>
     * A reference to the last assessment that was conducted on this patient. Assessments are often/usually ongoing in 
     * nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the 
     * patient's conditions changes.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPrevious() {
        return previous;
    }

    /**
     * <p>
     * A list of the relevant problems/conditions for a patient.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getProblem() {
        return problem;
    }

    /**
     * <p>
     * One or more sets of investigations (signs, symptoms, etc.). The actual grouping of investigations varies greatly 
     * depending on the type and context of the assessment. These investigations may include data generated during the 
     * assessment process, or data previously generated and recorded that is pertinent to the outcomes.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Investigation}.
     */
    public List<Investigation> getInvestigation() {
        return investigation;
    }

    /**
     * <p>
     * Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides 
     * evidence in support of the diagnosis.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getProtocol() {
        return protocol;
    }

    /**
     * <p>
     * A text summary of the investigations and the diagnosis.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * <p>
     * Specific findings or diagnoses that were considered likely or relevant to ongoing treatment.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Finding}.
     */
    public List<Finding> getFinding() {
        return finding;
    }

    /**
     * <p>
     * Estimate of likely outcome.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getPrognosisCodeableConcept() {
        return prognosisCodeableConcept;
    }

    /**
     * <p>
     * RiskAssessment expressing likely outcome.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPrognosisReference() {
        return prognosisReference;
    }

    /**
     * <p>
     * Information supporting the clinical impression.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * <p>
     * Commentary about the impression, typically recorded after the impression itself was made, though supplemental notes by 
     * the original author could also appear.
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
                accept(effective, "effective", visitor, true);
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, subject);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.statusReason = statusReason;
        builder.code = code;
        builder.description = description;
        builder.encounter = encounter;
        builder.effective = effective;
        builder.date = date;
        builder.assessor = assessor;
        builder.previous = previous;
        builder.problem.addAll(problem);
        builder.investigation.addAll(investigation);
        builder.protocol.addAll(protocol);
        builder.summary = summary;
        builder.finding.addAll(finding);
        builder.prognosisCodeableConcept.addAll(prognosisCodeableConcept);
        builder.prognosisReference.addAll(prognosisReference);
        builder.supportingInfo.addAll(supportingInfo);
        builder.note.addAll(note);
        return builder;
    }

    public static Builder builder(ClinicalImpressionStatus status, Reference subject) {
        return new Builder(status, subject);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ClinicalImpressionStatus status;
        private final Reference subject;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CodeableConcept statusReason;
        private CodeableConcept code;
        private String description;
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

        private Builder(ClinicalImpressionStatus status, Reference subject) {
            super();
            this.status = status;
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
         * Business identifiers assigned to this clinical impression by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     Business identifier
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
         * Business identifiers assigned to this clinical impression by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     Business identifier
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
         * Captures the reason for the current state of the ClinicalImpression.
         * </p>
         * 
         * @param statusReason
         *     Reason for current status
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder statusReason(CodeableConcept statusReason) {
            this.statusReason = statusReason;
            return this;
        }

        /**
         * <p>
         * Categorizes the type of clinical assessment performed.
         * </p>
         * 
         * @param code
         *     Kind of assessment performed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * <p>
         * A summary of the context and/or cause of the assessment - why / where it was performed, and what patient events/status 
         * prompted it.
         * </p>
         * 
         * @param description
         *     Why/how the assessment was performed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * <p>
         * The Encounter during which this ClinicalImpression was created or to which the creation of this record is tightly 
         * associated.
         * </p>
         * 
         * @param encounter
         *     Encounter created as part of
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * <p>
         * The point in time or period over which the subject was assessed.
         * </p>
         * 
         * @param effective
         *     Time of assessment
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder effective(Element effective) {
            this.effective = effective;
            return this;
        }

        /**
         * <p>
         * Indicates when the documentation of the assessment was complete.
         * </p>
         * 
         * @param date
         *     When the assessment was documented
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
         * The clinician performing the assessment.
         * </p>
         * 
         * @param assessor
         *     The clinician performing the assessment
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder assessor(Reference assessor) {
            this.assessor = assessor;
            return this;
        }

        /**
         * <p>
         * A reference to the last assessment that was conducted on this patient. Assessments are often/usually ongoing in 
         * nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the 
         * patient's conditions changes.
         * </p>
         * 
         * @param previous
         *     Reference to last assessment
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder previous(Reference previous) {
            this.previous = previous;
            return this;
        }

        /**
         * <p>
         * A list of the relevant problems/conditions for a patient.
         * </p>
         * 
         * @param problem
         *     Relevant impressions of patient state
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder problem(Reference... problem) {
            for (Reference value : problem) {
                this.problem.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A list of the relevant problems/conditions for a patient.
         * </p>
         * 
         * @param problem
         *     Relevant impressions of patient state
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder problem(Collection<Reference> problem) {
            this.problem.addAll(problem);
            return this;
        }

        /**
         * <p>
         * One or more sets of investigations (signs, symptoms, etc.). The actual grouping of investigations varies greatly 
         * depending on the type and context of the assessment. These investigations may include data generated during the 
         * assessment process, or data previously generated and recorded that is pertinent to the outcomes.
         * </p>
         * 
         * @param investigation
         *     One or more sets of investigations (signs, symptoms, etc.)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder investigation(Investigation... investigation) {
            for (Investigation value : investigation) {
                this.investigation.add(value);
            }
            return this;
        }

        /**
         * <p>
         * One or more sets of investigations (signs, symptoms, etc.). The actual grouping of investigations varies greatly 
         * depending on the type and context of the assessment. These investigations may include data generated during the 
         * assessment process, or data previously generated and recorded that is pertinent to the outcomes.
         * </p>
         * 
         * @param investigation
         *     One or more sets of investigations (signs, symptoms, etc.)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder investigation(Collection<Investigation> investigation) {
            this.investigation.addAll(investigation);
            return this;
        }

        /**
         * <p>
         * Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides 
         * evidence in support of the diagnosis.
         * </p>
         * 
         * @param protocol
         *     Clinical Protocol followed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder protocol(Uri... protocol) {
            for (Uri value : protocol) {
                this.protocol.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides 
         * evidence in support of the diagnosis.
         * </p>
         * 
         * @param protocol
         *     Clinical Protocol followed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder protocol(Collection<Uri> protocol) {
            this.protocol.addAll(protocol);
            return this;
        }

        /**
         * <p>
         * A text summary of the investigations and the diagnosis.
         * </p>
         * 
         * @param summary
         *     Summary of the assessment
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        /**
         * <p>
         * Specific findings or diagnoses that were considered likely or relevant to ongoing treatment.
         * </p>
         * 
         * @param finding
         *     Possible or likely findings and diagnoses
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder finding(Finding... finding) {
            for (Finding value : finding) {
                this.finding.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Specific findings or diagnoses that were considered likely or relevant to ongoing treatment.
         * </p>
         * 
         * @param finding
         *     Possible or likely findings and diagnoses
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder finding(Collection<Finding> finding) {
            this.finding.addAll(finding);
            return this;
        }

        /**
         * <p>
         * Estimate of likely outcome.
         * </p>
         * 
         * @param prognosisCodeableConcept
         *     Estimate of likely outcome
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder prognosisCodeableConcept(CodeableConcept... prognosisCodeableConcept) {
            for (CodeableConcept value : prognosisCodeableConcept) {
                this.prognosisCodeableConcept.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Estimate of likely outcome.
         * </p>
         * 
         * @param prognosisCodeableConcept
         *     Estimate of likely outcome
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder prognosisCodeableConcept(Collection<CodeableConcept> prognosisCodeableConcept) {
            this.prognosisCodeableConcept.addAll(prognosisCodeableConcept);
            return this;
        }

        /**
         * <p>
         * RiskAssessment expressing likely outcome.
         * </p>
         * 
         * @param prognosisReference
         *     RiskAssessment expressing likely outcome
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder prognosisReference(Reference... prognosisReference) {
            for (Reference value : prognosisReference) {
                this.prognosisReference.add(value);
            }
            return this;
        }

        /**
         * <p>
         * RiskAssessment expressing likely outcome.
         * </p>
         * 
         * @param prognosisReference
         *     RiskAssessment expressing likely outcome
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder prognosisReference(Collection<Reference> prognosisReference) {
            this.prognosisReference.addAll(prognosisReference);
            return this;
        }

        /**
         * <p>
         * Information supporting the clinical impression.
         * </p>
         * 
         * @param supportingInfo
         *     Information supporting the clinical impression
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInfo(Reference... supportingInfo) {
            for (Reference value : supportingInfo) {
                this.supportingInfo.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Information supporting the clinical impression.
         * </p>
         * 
         * @param supportingInfo
         *     Information supporting the clinical impression
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInfo(Collection<Reference> supportingInfo) {
            this.supportingInfo.addAll(supportingInfo);
            return this;
        }

        /**
         * <p>
         * Commentary about the impression, typically recorded after the impression itself was made, though supplemental notes by 
         * the original author could also appear.
         * </p>
         * 
         * @param note
         *     Comments made about the ClinicalImpression
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
         * Commentary about the impression, typically recorded after the impression itself was made, though supplemental notes by 
         * the original author could also appear.
         * </p>
         * 
         * @param note
         *     Comments made about the ClinicalImpression
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        @Override
        public ClinicalImpression build() {
            return new ClinicalImpression(this);
        }
    }

    /**
     * <p>
     * One or more sets of investigations (signs, symptoms, etc.). The actual grouping of investigations varies greatly 
     * depending on the type and context of the assessment. These investigations may include data generated during the 
     * assessment process, or data previously generated and recorded that is pertinent to the outcomes.
     * </p>
     */
    public static class Investigation extends BackboneElement {
        private final CodeableConcept code;
        private final List<Reference> item;

        private Investigation(Builder builder) {
            super(builder);
            this.code = ValidationSupport.requireNonNull(builder.code, "code");
            this.item = builder.item;
        }

        /**
         * <p>
         * A name/code for the group ("set") of investigations. Typically, this will be something like "signs", "symptoms", 
         * "clinical", "diagnostic", but the list is not constrained, and others such groups such as 
         * (exposure|family|travel|nutritional) history may be used.
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
         * A record of a specific investigation that was undertaken.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getItem() {
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
                    accept(code, "code", visitor);
                    accept(item, "item", visitor, Reference.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept code) {
            return new Builder(code);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept code;

            // optional
            private List<Reference> item = new ArrayList<>();

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
             * A record of a specific investigation that was undertaken.
             * </p>
             * 
             * @param item
             *     Record of a specific investigation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder item(Reference... item) {
                for (Reference value : item) {
                    this.item.add(value);
                }
                return this;
            }

            /**
             * <p>
             * A record of a specific investigation that was undertaken.
             * </p>
             * 
             * @param item
             *     Record of a specific investigation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder item(Collection<Reference> item) {
                this.item.addAll(item);
                return this;
            }

            @Override
            public Investigation build() {
                return new Investigation(this);
            }

            private static Builder from(Investigation investigation) {
                Builder builder = new Builder(investigation.code);
                builder.id = investigation.id;
                builder.extension.addAll(investigation.extension);
                builder.modifierExtension.addAll(investigation.modifierExtension);
                builder.item.addAll(investigation.item);
                return builder;
            }
        }
    }

    /**
     * <p>
     * Specific findings or diagnoses that were considered likely or relevant to ongoing treatment.
     * </p>
     */
    public static class Finding extends BackboneElement {
        private final CodeableConcept itemCodeableConcept;
        private final Reference itemReference;
        private final String basis;

        private Finding(Builder builder) {
            super(builder);
            this.itemCodeableConcept = builder.itemCodeableConcept;
            this.itemReference = builder.itemReference;
            this.basis = builder.basis;
        }

        /**
         * <p>
         * Specific text or code for finding or diagnosis, which may include ruled-out or resolved conditions.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getItemCodeableConcept() {
            return itemCodeableConcept;
        }

        /**
         * <p>
         * Specific reference for finding or diagnosis, which may include ruled-out or resolved conditions.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getItemReference() {
            return itemReference;
        }

        /**
         * <p>
         * Which investigations support finding or diagnosis.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getBasis() {
            return basis;
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
                    accept(itemCodeableConcept, "itemCodeableConcept", visitor);
                    accept(itemReference, "itemReference", visitor);
                    accept(basis, "basis", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private CodeableConcept itemCodeableConcept;
            private Reference itemReference;
            private String basis;

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
             * Specific text or code for finding or diagnosis, which may include ruled-out or resolved conditions.
             * </p>
             * 
             * @param itemCodeableConcept
             *     What was found
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder itemCodeableConcept(CodeableConcept itemCodeableConcept) {
                this.itemCodeableConcept = itemCodeableConcept;
                return this;
            }

            /**
             * <p>
             * Specific reference for finding or diagnosis, which may include ruled-out or resolved conditions.
             * </p>
             * 
             * @param itemReference
             *     What was found
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder itemReference(Reference itemReference) {
                this.itemReference = itemReference;
                return this;
            }

            /**
             * <p>
             * Which investigations support finding or diagnosis.
             * </p>
             * 
             * @param basis
             *     Which investigations support finding
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder basis(String basis) {
                this.basis = basis;
                return this;
            }

            @Override
            public Finding build() {
                return new Finding(this);
            }

            private static Builder from(Finding finding) {
                Builder builder = new Builder();
                builder.id = finding.id;
                builder.extension.addAll(finding.extension);
                builder.modifierExtension.addAll(finding.modifierExtension);
                builder.itemCodeableConcept = finding.itemCodeableConcept;
                builder.itemReference = finding.itemReference;
                builder.basis = finding.basis;
                return builder;
            }
        }
    }
}
