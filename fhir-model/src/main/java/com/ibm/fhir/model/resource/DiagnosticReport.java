/*
 * (C) Copyright IBM Corp. 2019, 2020
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
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.DiagnosticReportStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The findings and interpretation of diagnostic tests performed on patients, groups of patients, devices, and locations, 
 * and/or specimens derived from these. The report includes clinical context such as requesting and provider information, 
 * and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic 
 * reports.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DiagnosticReport extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    private final List<Reference> basedOn;
    @Summary
    @Binding(
        bindingName = "DiagnosticReportStatus",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "The status of the diagnostic report.",
        valueSet = "http://hl7.org/fhir/ValueSet/diagnostic-report-status|4.0.1"
    )
    @Required
    private final DiagnosticReportStatus status;
    @Summary
    @Binding(
        bindingName = "DiagnosticServiceSection",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "Codes for diagnostic service sections.",
        valueSet = "http://hl7.org/fhir/ValueSet/diagnostic-service-sections"
    )
    private final List<CodeableConcept> category;
    @Summary
    @Binding(
        bindingName = "DiagnosticReportCodes",
        strength = BindingStrength.ValueSet.PREFERRED,
        description = "Codes that describe Diagnostic Reports.",
        valueSet = "http://hl7.org/fhir/ValueSet/report-codes"
    )
    @Required
    private final CodeableConcept code;
    @Summary
    @ReferenceTarget({ "Patient", "Group", "Device", "Location" })
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Summary
    @Choice({ DateTime.class, Period.class })
    private final Element effective;
    @Summary
    private final Instant issued;
    @Summary
    private final List<Reference> performer;
    @Summary
    private final List<Reference> resultsInterpreter;
    private final List<Reference> specimen;
    private final List<Reference> result;
    private final List<Reference> imagingStudy;
    @Summary
    private final List<Media> media;
    private final String conclusion;
    @Binding(
        bindingName = "AdjunctDiagnosis",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "Diagnosis codes provided as adjuncts to the report.",
        valueSet = "http://hl7.org/fhir/ValueSet/clinical-findings"
    )
    private final List<CodeableConcept> conclusionCode;
    private final List<Attachment> presentedForm;

    private volatile int hashCode;

    private DiagnosticReport(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        basedOn = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.basedOn, "basedOn"));
        status = ValidationSupport.requireNonNull(builder.status, "status");
        category = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.category, "category"));
        code = ValidationSupport.requireNonNull(builder.code, "code");
        subject = builder.subject;
        encounter = builder.encounter;
        effective = ValidationSupport.choiceElement(builder.effective, "effective", DateTime.class, Period.class);
        issued = builder.issued;
        performer = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.performer, "performer"));
        resultsInterpreter = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.resultsInterpreter, "resultsInterpreter"));
        specimen = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.specimen, "specimen"));
        result = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.result, "result"));
        imagingStudy = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.imagingStudy, "imagingStudy"));
        media = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.media, "media"));
        conclusion = builder.conclusion;
        conclusionCode = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.conclusionCode, "conclusionCode"));
        presentedForm = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.presentedForm, "presentedForm"));
        ValidationSupport.checkReferenceType(subject, "subject", "Patient", "Group", "Device", "Location");
        ValidationSupport.checkReferenceType(encounter, "encounter", "Encounter");
        ValidationSupport.requireChildren(this);
    }

    /**
     * Identifiers assigned to this report by the performer or other systems.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Details concerning a service requested.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * The status of the diagnostic report.
     * 
     * @return
     *     An immutable object of type {@link DiagnosticReportStatus} that is non-null.
     */
    public DiagnosticReportStatus getStatus() {
        return status;
    }

    /**
     * A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. 
     * cardiology, biochemistry, hematology, MRI). This is used for searching, sorting and display purposes.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * A code or name that describes this diagnostic report.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * The subject of the report. Usually, but not always, this is a patient. However, diagnostic services also perform 
     * analyses on specimens collected from a variety of other sources.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The healthcare event (e.g. a patient and healthcare provider interaction) which this DiagnosticReport is about.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * The time or time-period the observed values are related to. When the subject of the report is a patient, this is 
     * usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is 
     * not known, only the date/time itself.
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getEffective() {
        return effective;
    }

    /**
     * The date and time that this version of the report was made available to providers, typically after the report was 
     * reviewed and verified.
     * 
     * @return
     *     An immutable object of type {@link Instant} that may be null.
     */
    public Instant getIssued() {
        return issued;
    }

    /**
     * The diagnostic service that is responsible for issuing the report.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPerformer() {
        return performer;
    }

    /**
     * The practitioner or organization that is responsible for the report's conclusions and interpretations.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getResultsInterpreter() {
        return resultsInterpreter;
    }

    /**
     * Details about the specimens on which this diagnostic report is based.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSpecimen() {
        return specimen;
    }

    /**
     * [Observations](observation.html) that are part of this diagnostic report.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getResult() {
        return result;
    }

    /**
     * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is 
     * imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this 
     * information to provide views of the source images.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getImagingStudy() {
        return imagingStudy;
    }

    /**
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and 
     * may be directly of the patient, or of treated specimens (i.e. slides of interest).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Media} that may be empty.
     */
    public List<Media> getMedia() {
        return media;
    }

    /**
     * Concise and clinically contextualized summary conclusion (interpretation/impression) of the diagnostic report.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getConclusion() {
        return conclusion;
    }

    /**
     * One or more codes that represent the summary conclusion (interpretation/impression) of the diagnostic report.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getConclusionCode() {
        return conclusionCode;
    }

    /**
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but 
     * they SHALL be semantically equivalent.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Attachment} that may be empty.
     */
    public List<Attachment> getPresentedForm() {
        return presentedForm;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !basedOn.isEmpty() || 
            (status != null) || 
            !category.isEmpty() || 
            (code != null) || 
            (subject != null) || 
            (encounter != null) || 
            (effective != null) || 
            (issued != null) || 
            !performer.isEmpty() || 
            !resultsInterpreter.isEmpty() || 
            !specimen.isEmpty() || 
            !result.isEmpty() || 
            !imagingStudy.isEmpty() || 
            !media.isEmpty() || 
            (conclusion != null) || 
            !conclusionCode.isEmpty() || 
            !presentedForm.isEmpty();
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
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(code, "code", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(effective, "effective", visitor);
                accept(issued, "issued", visitor);
                accept(performer, "performer", visitor, Reference.class);
                accept(resultsInterpreter, "resultsInterpreter", visitor, Reference.class);
                accept(specimen, "specimen", visitor, Reference.class);
                accept(result, "result", visitor, Reference.class);
                accept(imagingStudy, "imagingStudy", visitor, Reference.class);
                accept(media, "media", visitor, Media.class);
                accept(conclusion, "conclusion", visitor);
                accept(conclusionCode, "conclusionCode", visitor, CodeableConcept.class);
                accept(presentedForm, "presentedForm", visitor, Attachment.class);
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
        DiagnosticReport other = (DiagnosticReport) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(basedOn, other.basedOn) && 
            Objects.equals(status, other.status) && 
            Objects.equals(category, other.category) && 
            Objects.equals(code, other.code) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(effective, other.effective) && 
            Objects.equals(issued, other.issued) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(resultsInterpreter, other.resultsInterpreter) && 
            Objects.equals(specimen, other.specimen) && 
            Objects.equals(result, other.result) && 
            Objects.equals(imagingStudy, other.imagingStudy) && 
            Objects.equals(media, other.media) && 
            Objects.equals(conclusion, other.conclusion) && 
            Objects.equals(conclusionCode, other.conclusionCode) && 
            Objects.equals(presentedForm, other.presentedForm);
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
                basedOn, 
                status, 
                category, 
                code, 
                subject, 
                encounter, 
                effective, 
                issued, 
                performer, 
                resultsInterpreter, 
                specimen, 
                this.result, 
                imagingStudy, 
                media, 
                conclusion, 
                conclusionCode, 
                presentedForm);
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
        private List<Reference> basedOn = new ArrayList<>();
        private DiagnosticReportStatus status;
        private List<CodeableConcept> category = new ArrayList<>();
        private CodeableConcept code;
        private Reference subject;
        private Reference encounter;
        private Element effective;
        private Instant issued;
        private List<Reference> performer = new ArrayList<>();
        private List<Reference> resultsInterpreter = new ArrayList<>();
        private List<Reference> specimen = new ArrayList<>();
        private List<Reference> result = new ArrayList<>();
        private List<Reference> imagingStudy = new ArrayList<>();
        private List<Media> media = new ArrayList<>();
        private String conclusion;
        private List<CodeableConcept> conclusionCode = new ArrayList<>();
        private List<Attachment> presentedForm = new ArrayList<>();

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
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * Identifiers assigned to this report by the performer or other systems.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param identifier
         *     Business identifier for report
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
         * Identifiers assigned to this report by the performer or other systems.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param identifier
         *     Business identifier for report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * Details concerning a service requested.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param basedOn
         *     What was requested
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Reference... basedOn) {
            for (Reference value : basedOn) {
                this.basedOn.add(value);
            }
            return this;
        }

        /**
         * Details concerning a service requested.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param basedOn
         *     What was requested
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * The status of the diagnostic report.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     registered | partial | preliminary | final +
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(DiagnosticReportStatus status) {
            this.status = status;
            return this;
        }

        /**
         * A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. 
         * cardiology, biochemistry, hematology, MRI). This is used for searching, sorting and display purposes.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param category
         *     Service category
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
         * A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. 
         * cardiology, biochemistry, hematology, MRI). This is used for searching, sorting and display purposes.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param category
         *     Service category
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * A code or name that describes this diagnostic report.
         * 
         * <p>This element is required.
         * 
         * @param code
         *     Name/Code for this diagnostic report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * The subject of the report. Usually, but not always, this is a patient. However, diagnostic services also perform 
         * analyses on specimens collected from a variety of other sources.
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
         *     The subject of the report - usually, but not always, the patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The healthcare event (e.g. a patient and healthcare provider interaction) which this DiagnosticReport is about.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
         * 
         * @param encounter
         *     Health care event when test ordered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * The time or time-period the observed values are related to. When the subject of the report is a patient, this is 
         * usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is 
         * not known, only the date/time itself.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link DateTime}</li>
         * <li>{@link Period}</li>
         * </ul>
         * 
         * @param effective
         *     Clinically relevant time/time-period for report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effective(Element effective) {
            this.effective = effective;
            return this;
        }

        /**
         * The date and time that this version of the report was made available to providers, typically after the report was 
         * reviewed and verified.
         * 
         * @param issued
         *     DateTime this version was made
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder issued(Instant issued) {
            this.issued = issued;
            return this;
        }

        /**
         * The diagnostic service that is responsible for issuing the report.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param performer
         *     Responsible Diagnostic Service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Reference... performer) {
            for (Reference value : performer) {
                this.performer.add(value);
            }
            return this;
        }

        /**
         * The diagnostic service that is responsible for issuing the report.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param performer
         *     Responsible Diagnostic Service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Collection<Reference> performer) {
            this.performer = new ArrayList<>(performer);
            return this;
        }

        /**
         * The practitioner or organization that is responsible for the report's conclusions and interpretations.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param resultsInterpreter
         *     Primary result interpreter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder resultsInterpreter(Reference... resultsInterpreter) {
            for (Reference value : resultsInterpreter) {
                this.resultsInterpreter.add(value);
            }
            return this;
        }

        /**
         * The practitioner or organization that is responsible for the report's conclusions and interpretations.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param resultsInterpreter
         *     Primary result interpreter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder resultsInterpreter(Collection<Reference> resultsInterpreter) {
            this.resultsInterpreter = new ArrayList<>(resultsInterpreter);
            return this;
        }

        /**
         * Details about the specimens on which this diagnostic report is based.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param specimen
         *     Specimens this report is based on
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specimen(Reference... specimen) {
            for (Reference value : specimen) {
                this.specimen.add(value);
            }
            return this;
        }

        /**
         * Details about the specimens on which this diagnostic report is based.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param specimen
         *     Specimens this report is based on
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specimen(Collection<Reference> specimen) {
            this.specimen = new ArrayList<>(specimen);
            return this;
        }

        /**
         * [Observations](observation.html) that are part of this diagnostic report.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param result
         *     Observations
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder result(Reference... result) {
            for (Reference value : result) {
                this.result.add(value);
            }
            return this;
        }

        /**
         * [Observations](observation.html) that are part of this diagnostic report.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param result
         *     Observations
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder result(Collection<Reference> result) {
            this.result = new ArrayList<>(result);
            return this;
        }

        /**
         * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is 
         * imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this 
         * information to provide views of the source images.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param imagingStudy
         *     Reference to full details of imaging associated with the diagnostic report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder imagingStudy(Reference... imagingStudy) {
            for (Reference value : imagingStudy) {
                this.imagingStudy.add(value);
            }
            return this;
        }

        /**
         * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is 
         * imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this 
         * information to provide views of the source images.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param imagingStudy
         *     Reference to full details of imaging associated with the diagnostic report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder imagingStudy(Collection<Reference> imagingStudy) {
            this.imagingStudy = new ArrayList<>(imagingStudy);
            return this;
        }

        /**
         * A list of key images associated with this report. The images are generally created during the diagnostic process, and 
         * may be directly of the patient, or of treated specimens (i.e. slides of interest).
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param media
         *     Key images associated with this report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder media(Media... media) {
            for (Media value : media) {
                this.media.add(value);
            }
            return this;
        }

        /**
         * A list of key images associated with this report. The images are generally created during the diagnostic process, and 
         * may be directly of the patient, or of treated specimens (i.e. slides of interest).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param media
         *     Key images associated with this report
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder media(Collection<Media> media) {
            this.media = new ArrayList<>(media);
            return this;
        }

        /**
         * Concise and clinically contextualized summary conclusion (interpretation/impression) of the diagnostic report.
         * 
         * @param conclusion
         *     Clinical conclusion (interpretation) of test results
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder conclusion(String conclusion) {
            this.conclusion = conclusion;
            return this;
        }

        /**
         * One or more codes that represent the summary conclusion (interpretation/impression) of the diagnostic report.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param conclusionCode
         *     Codes for the clinical conclusion of test results
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder conclusionCode(CodeableConcept... conclusionCode) {
            for (CodeableConcept value : conclusionCode) {
                this.conclusionCode.add(value);
            }
            return this;
        }

        /**
         * One or more codes that represent the summary conclusion (interpretation/impression) of the diagnostic report.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param conclusionCode
         *     Codes for the clinical conclusion of test results
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder conclusionCode(Collection<CodeableConcept> conclusionCode) {
            this.conclusionCode = new ArrayList<>(conclusionCode);
            return this;
        }

        /**
         * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but 
         * they SHALL be semantically equivalent.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param presentedForm
         *     Entire report as issued
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder presentedForm(Attachment... presentedForm) {
            for (Attachment value : presentedForm) {
                this.presentedForm.add(value);
            }
            return this;
        }

        /**
         * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but 
         * they SHALL be semantically equivalent.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param presentedForm
         *     Entire report as issued
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder presentedForm(Collection<Attachment> presentedForm) {
            this.presentedForm = new ArrayList<>(presentedForm);
            return this;
        }

        /**
         * Build the {@link DiagnosticReport}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>code</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link DiagnosticReport}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid DiagnosticReport per the base specification
         */
        @Override
        public DiagnosticReport build() {
            return new DiagnosticReport(this);
        }

        protected Builder from(DiagnosticReport diagnosticReport) {
            super.from(diagnosticReport);
            identifier.addAll(diagnosticReport.identifier);
            basedOn.addAll(diagnosticReport.basedOn);
            status = diagnosticReport.status;
            category.addAll(diagnosticReport.category);
            code = diagnosticReport.code;
            subject = diagnosticReport.subject;
            encounter = diagnosticReport.encounter;
            effective = diagnosticReport.effective;
            issued = diagnosticReport.issued;
            performer.addAll(diagnosticReport.performer);
            resultsInterpreter.addAll(diagnosticReport.resultsInterpreter);
            specimen.addAll(diagnosticReport.specimen);
            result.addAll(diagnosticReport.result);
            imagingStudy.addAll(diagnosticReport.imagingStudy);
            media.addAll(diagnosticReport.media);
            conclusion = diagnosticReport.conclusion;
            conclusionCode.addAll(diagnosticReport.conclusionCode);
            presentedForm.addAll(diagnosticReport.presentedForm);
            return this;
        }
    }

    /**
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and 
     * may be directly of the patient, or of treated specimens (i.e. slides of interest).
     */
    public static class Media extends BackboneElement {
        private final String comment;
        @Summary
        @ReferenceTarget({ "Media" })
        @Required
        private final Reference link;

        private volatile int hashCode;

        private Media(Builder builder) {
            super(builder);
            comment = builder.comment;
            link = ValidationSupport.requireNonNull(builder.link, "link");
            ValidationSupport.checkReferenceType(link, "link", "Media");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw 
         * the viewer's attention to important features.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getComment() {
            return comment;
        }

        /**
         * Reference to the image source.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getLink() {
            return link;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (comment != null) || 
                (link != null);
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
                    accept(comment, "comment", visitor);
                    accept(link, "link", visitor);
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
            Media other = (Media) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(comment, other.comment) && 
                Objects.equals(link, other.link);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    comment, 
                    link);
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
            private String comment;
            private Reference link;

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw 
             * the viewer's attention to important features.
             * 
             * @param comment
             *     Comment about the image (e.g. explanation)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder comment(String comment) {
                this.comment = comment;
                return this;
            }

            /**
             * Reference to the image source.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Media}</li>
             * </ul>
             * 
             * @param link
             *     Reference to the image source
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder link(Reference link) {
                this.link = link;
                return this;
            }

            /**
             * Build the {@link Media}
             * 
             * <p>Required elements:
             * <ul>
             * <li>link</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Media}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Media per the base specification
             */
            @Override
            public Media build() {
                return new Media(this);
            }

            protected Builder from(Media media) {
                super.from(media);
                comment = media.comment;
                link = media.link;
                return this;
            }
        }
    }
}
