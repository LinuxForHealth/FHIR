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

import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.DiagnosticReportStatus;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Instant;
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
 * The findings and interpretation of diagnostic tests performed on patients, groups of patients, devices, and locations, 
 * and/or specimens derived from these. The report includes clinical context such as requesting and provider information, 
 * and some mix of atomic results, images, textual and coded interpretations, and formatted representation of diagnostic 
 * reports.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class DiagnosticReport extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Reference> basedOn;
    private final DiagnosticReportStatus status;
    private final List<CodeableConcept> category;
    private final CodeableConcept code;
    private final Reference subject;
    private final Reference encounter;
    private final Element effective;
    private final Instant issued;
    private final List<Reference> performer;
    private final List<Reference> resultsInterpreter;
    private final List<Reference> specimen;
    private final List<Reference> result;
    private final List<Reference> imagingStudy;
    private final List<Media> media;
    private final String conclusion;
    private final List<CodeableConcept> conclusionCode;
    private final List<Attachment> presentedForm;

    private volatile int hashCode;

    private DiagnosticReport(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        category = Collections.unmodifiableList(builder.category);
        code = ValidationSupport.requireNonNull(builder.code, "code");
        subject = builder.subject;
        encounter = builder.encounter;
        effective = ValidationSupport.choiceElement(builder.effective, "effective", DateTime.class, Period.class);
        issued = builder.issued;
        performer = Collections.unmodifiableList(builder.performer);
        resultsInterpreter = Collections.unmodifiableList(builder.resultsInterpreter);
        specimen = Collections.unmodifiableList(builder.specimen);
        result = Collections.unmodifiableList(builder.result);
        imagingStudy = Collections.unmodifiableList(builder.imagingStudy);
        media = Collections.unmodifiableList(builder.media);
        conclusion = builder.conclusion;
        conclusionCode = Collections.unmodifiableList(builder.conclusionCode);
        presentedForm = Collections.unmodifiableList(builder.presentedForm);
    }

    /**
     * <p>
     * Identifiers assigned to this report by the performer or other systems.
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
     * Details concerning a service requested.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * <p>
     * The status of the diagnostic report.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DiagnosticReportStatus}.
     */
    public DiagnosticReportStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. 
     * cardiology, biochemistry, hematology, MRI). This is used for searching, sorting and display purposes.
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
     * A code or name that describes this diagnostic report.
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
     * The subject of the report. Usually, but not always, this is a patient. However, diagnostic services also perform 
     * analyses on specimens collected from a variety of other sources.
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
     * The healthcare event (e.g. a patient and healthcare provider interaction) which this DiagnosticReport is about.
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
     * The time or time-period the observed values are related to. When the subject of the report is a patient, this is 
     * usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is 
     * not known, only the date/time itself.
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
     * The date and time that this version of the report was made available to providers, typically after the report was 
     * reviewed and verified.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Instant}.
     */
    public Instant getIssued() {
        return issued;
    }

    /**
     * <p>
     * The diagnostic service that is responsible for issuing the report.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPerformer() {
        return performer;
    }

    /**
     * <p>
     * The practitioner or organization that is responsible for the report's conclusions and interpretations.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getResultsInterpreter() {
        return resultsInterpreter;
    }

    /**
     * <p>
     * Details about the specimens on which this diagnostic report is based.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSpecimen() {
        return specimen;
    }

    /**
     * <p>
     * [Observations](observation.html) that are part of this diagnostic report.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getResult() {
        return result;
    }

    /**
     * <p>
     * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is 
     * imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this 
     * information to provide views of the source images.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getImagingStudy() {
        return imagingStudy;
    }

    /**
     * <p>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and 
     * may be directly of the patient, or of treated specimens (i.e. slides of interest).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Media}.
     */
    public List<Media> getMedia() {
        return media;
    }

    /**
     * <p>
     * Concise and clinically contextualized summary conclusion (interpretation/impression) of the diagnostic report.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getConclusion() {
        return conclusion;
    }

    /**
     * <p>
     * One or more codes that represent the summary conclusion (interpretation/impression) of the diagnostic report.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getConclusionCode() {
        return conclusionCode;
    }

    /**
     * <p>
     * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but 
     * they SHALL be semantically equivalent.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Attachment}.
     */
    public List<Attachment> getPresentedForm() {
        return presentedForm;
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
                result, 
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
        return new Builder(status, code).from(this);
    }

    public Builder toBuilder(DiagnosticReportStatus status, CodeableConcept code) {
        return new Builder(status, code).from(this);
    }

    public static Builder builder(DiagnosticReportStatus status, CodeableConcept code) {
        return new Builder(status, code);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final DiagnosticReportStatus status;
        private final CodeableConcept code;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<CodeableConcept> category = new ArrayList<>();
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

        private Builder(DiagnosticReportStatus status, CodeableConcept code) {
            super();
            this.status = status;
            this.code = code;
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
         * Identifiers assigned to this report by the performer or other systems.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Identifiers assigned to this report by the performer or other systems.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * Details concerning a service requested.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Details concerning a service requested.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. 
         * cardiology, biochemistry, hematology, MRI). This is used for searching, sorting and display purposes.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * A code that classifies the clinical discipline, department or diagnostic service that created the report (e.g. 
         * cardiology, biochemistry, hematology, MRI). This is used for searching, sorting and display purposes.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * The subject of the report. Usually, but not always, this is a patient. However, diagnostic services also perform 
         * analyses on specimens collected from a variety of other sources.
         * </p>
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
         * <p>
         * The healthcare event (e.g. a patient and healthcare provider interaction) which this DiagnosticReport is about.
         * </p>
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
         * <p>
         * The time or time-period the observed values are related to. When the subject of the report is a patient, this is 
         * usually either the time of the procedure or of specimen collection(s), but very often the source of the date/time is 
         * not known, only the date/time itself.
         * </p>
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
         * <p>
         * The date and time that this version of the report was made available to providers, typically after the report was 
         * reviewed and verified.
         * </p>
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
         * <p>
         * The diagnostic service that is responsible for issuing the report.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The diagnostic service that is responsible for issuing the report.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * The practitioner or organization that is responsible for the report's conclusions and interpretations.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The practitioner or organization that is responsible for the report's conclusions and interpretations.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * Details about the specimens on which this diagnostic report is based.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Details about the specimens on which this diagnostic report is based.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * [Observations](observation.html) that are part of this diagnostic report.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * [Observations](observation.html) that are part of this diagnostic report.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is 
         * imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this 
         * information to provide views of the source images.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * One or more links to full details of any imaging performed during the diagnostic investigation. Typically, this is 
         * imaging performed by DICOM enabled modalities, but this is not required. A fully enabled PACS viewer can use this 
         * information to provide views of the source images.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * A list of key images associated with this report. The images are generally created during the diagnostic process, and 
         * may be directly of the patient, or of treated specimens (i.e. slides of interest).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * A list of key images associated with this report. The images are generally created during the diagnostic process, and 
         * may be directly of the patient, or of treated specimens (i.e. slides of interest).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * Concise and clinically contextualized summary conclusion (interpretation/impression) of the diagnostic report.
         * </p>
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
         * <p>
         * One or more codes that represent the summary conclusion (interpretation/impression) of the diagnostic report.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * One or more codes that represent the summary conclusion (interpretation/impression) of the diagnostic report.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but 
         * they SHALL be semantically equivalent.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * Rich text representation of the entire result as issued by the diagnostic service. Multiple formats are allowed but 
         * they SHALL be semantically equivalent.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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

        @Override
        public DiagnosticReport build() {
            return new DiagnosticReport(this);
        }

        private Builder from(DiagnosticReport diagnosticReport) {
            id = diagnosticReport.id;
            meta = diagnosticReport.meta;
            implicitRules = diagnosticReport.implicitRules;
            language = diagnosticReport.language;
            text = diagnosticReport.text;
            contained.addAll(diagnosticReport.contained);
            extension.addAll(diagnosticReport.extension);
            modifierExtension.addAll(diagnosticReport.modifierExtension);
            identifier.addAll(diagnosticReport.identifier);
            basedOn.addAll(diagnosticReport.basedOn);
            category.addAll(diagnosticReport.category);
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
     * <p>
     * A list of key images associated with this report. The images are generally created during the diagnostic process, and 
     * may be directly of the patient, or of treated specimens (i.e. slides of interest).
     * </p>
     */
    public static class Media extends BackboneElement {
        private final String comment;
        private final Reference link;

        private volatile int hashCode;

        private Media(Builder builder) {
            super(builder);
            comment = builder.comment;
            link = ValidationSupport.requireNonNull(builder.link, "link");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw 
         * the viewer's attention to important features.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getComment() {
            return comment;
        }

        /**
         * <p>
         * Reference to the image source.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
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
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(comment, "comment", visitor);
                    accept(link, "link", visitor);
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
            return new Builder(link).from(this);
        }

        public Builder toBuilder(Reference link) {
            return new Builder(link).from(this);
        }

        public static Builder builder(Reference link) {
            return new Builder(link);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference link;

            // optional
            private String comment;

            private Builder(Reference link) {
                super();
                this.link = link;
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
             * A comment about the image. Typically, this is used to provide an explanation for why the image is included, or to draw 
             * the viewer's attention to important features.
             * </p>
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

            @Override
            public Media build() {
                return new Media(this);
            }

            private Builder from(Media media) {
                id = media.id;
                extension.addAll(media.extension);
                modifierExtension.addAll(media.modifierExtension);
                comment = media.comment;
                return this;
            }
        }
    }
}
