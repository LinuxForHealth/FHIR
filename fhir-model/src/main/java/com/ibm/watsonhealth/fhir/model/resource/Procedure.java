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

import com.ibm.watsonhealth.fhir.model.type.Age;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
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
import com.ibm.watsonhealth.fhir.model.type.ProcedureStatus;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or less 
 * invasive like long term services, counseling, or hypnotherapy.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Procedure extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Canonical> instantiatesCanonical;
    private final List<Uri> instantiatesUri;
    private final List<Reference> basedOn;
    private final List<Reference> partOf;
    private final ProcedureStatus status;
    private final CodeableConcept statusReason;
    private final CodeableConcept category;
    private final CodeableConcept code;
    private final Reference subject;
    private final Reference encounter;
    private final Element performed;
    private final Reference recorder;
    private final Reference asserter;
    private final List<Performer> performer;
    private final Reference location;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final List<CodeableConcept> bodySite;
    private final CodeableConcept outcome;
    private final List<Reference> report;
    private final List<CodeableConcept> complication;
    private final List<Reference> complicationDetail;
    private final List<CodeableConcept> followUp;
    private final List<Annotation> note;
    private final List<FocalDevice> focalDevice;
    private final List<Reference> usedReference;
    private final List<CodeableConcept> usedCode;

    private volatile int hashCode;

    private Procedure(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        instantiatesCanonical = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.instantiatesCanonical, "instantiatesCanonical"));
        instantiatesUri = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.instantiatesUri, "instantiatesUri"));
        basedOn = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.basedOn, "basedOn"));
        partOf = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.partOf, "partOf"));
        status = ValidationSupport.requireNonNull(builder.status, "status");
        statusReason = builder.statusReason;
        category = builder.category;
        code = builder.code;
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        encounter = builder.encounter;
        performed = ValidationSupport.choiceElement(builder.performed, "performed", DateTime.class, Period.class, String.class, Age.class, Range.class);
        recorder = builder.recorder;
        asserter = builder.asserter;
        performer = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.performer, "performer"));
        location = builder.location;
        reasonCode = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonCode, "reasonCode"));
        reasonReference = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonReference, "reasonReference"));
        bodySite = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.bodySite, "bodySite"));
        outcome = builder.outcome;
        report = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.report, "report"));
        complication = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.complication, "complication"));
        complicationDetail = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.complicationDetail, "complicationDetail"));
        followUp = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.followUp, "followUp"));
        note = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.note, "note"));
        focalDevice = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.focalDevice, "focalDevice"));
        usedReference = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.usedReference, "usedReference"));
        usedCode = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.usedCode, "usedCode"));
    }

    /**
     * <p>
     * Business identifiers assigned to this procedure by the performer or other systems which remain constant as the 
     * resource is updated and is propagated from server to server.
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
     * The URL pointing to a FHIR-defined protocol, guideline, order set or other definition that is adhered to in whole or 
     * in part by this Procedure.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * <p>
     * The URL pointing to an externally maintained protocol, guideline, order set or other definition that is adhered to in 
     * whole or in part by this Procedure.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * <p>
     * A reference to a resource that contains details of the request for this procedure.
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
     * A larger event of which this particular procedure is a component or step.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * <p>
     * A code specifying the state of the procedure. Generally, this will be the in-progress or completed state.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ProcedureStatus}.
     */
    public ProcedureStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Captures the reason for the current state of the procedure.
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
     * A code that classifies the procedure for searching, sorting and display purposes (e.g. "Surgical Procedure").
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getCategory() {
        return category;
    }

    /**
     * <p>
     * The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. 
     * "Laparoscopic Appendectomy").
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
     * The person, animal or group on which the procedure was performed.
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
     * The Encounter during which this Procedure was created or performed or to which the creation of this record is tightly 
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
     * Estimated or actual date, date-time, period, or age when the procedure was performed. Allows a period to support 
     * complex procedures that span more than one date, and also allows for the length of the procedure to be captured.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getPerformed() {
        return performed;
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
     * Individual who is making the procedure statement.
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
     * Limited to "real" people rather than equipment.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Performer}.
     */
    public List<Performer> getPerformer() {
        return performer;
    }

    /**
     * <p>
     * The location where the procedure actually happened. E.g. a newborn at home, a tracheostomy at a restaurant.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getLocation() {
        return location;
    }

    /**
     * <p>
     * The coded reason why the procedure was performed. This may be a coded entity of some type, or may simply be present as 
     * text.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * <p>
     * The justification of why the procedure was performed.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * <p>
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies 
     * of a lesion.
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
     * The outcome of the procedure - did it resolve the reasons for the procedure being performed?
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
     * This could be a histology result, pathology report, surgical report, etc.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReport() {
        return report;
    }

    /**
     * <p>
     * Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally 
     * tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' 
     * issues.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getComplication() {
        return complication;
    }

    /**
     * <p>
     * Any complications that occurred during the procedure, or in the immediate post-performance period.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getComplicationDetail() {
        return complicationDetail;
    }

    /**
     * <p>
     * If the procedure required specific follow up - e.g. removal of sutures. The follow up may be represented as a simple 
     * note or could potentially be more complex, in which case the CarePlan resource can be used.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getFollowUp() {
        return followUp;
    }

    /**
     * <p>
     * Any other notes and comments about the procedure.
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
     * A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, 
     * attaching a wound-vac, etc.) as a focal portion of the Procedure.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link FocalDevice}.
     */
    public List<FocalDevice> getFocalDevice() {
        return focalDevice;
    }

    /**
     * <p>
     * Identifies medications, devices and any other substance used as part of the procedure.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getUsedReference() {
        return usedReference;
    }

    /**
     * <p>
     * Identifies coded items that were used as part of the procedure.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getUsedCode() {
        return usedCode;
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
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(statusReason, "statusReason", visitor);
                accept(category, "category", visitor);
                accept(code, "code", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(performed, "performed", visitor);
                accept(recorder, "recorder", visitor);
                accept(asserter, "asserter", visitor);
                accept(performer, "performer", visitor, Performer.class);
                accept(location, "location", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(bodySite, "bodySite", visitor, CodeableConcept.class);
                accept(outcome, "outcome", visitor);
                accept(report, "report", visitor, Reference.class);
                accept(complication, "complication", visitor, CodeableConcept.class);
                accept(complicationDetail, "complicationDetail", visitor, Reference.class);
                accept(followUp, "followUp", visitor, CodeableConcept.class);
                accept(note, "note", visitor, Annotation.class);
                accept(focalDevice, "focalDevice", visitor, FocalDevice.class);
                accept(usedReference, "usedReference", visitor, Reference.class);
                accept(usedCode, "usedCode", visitor, CodeableConcept.class);
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
        Procedure other = (Procedure) obj;
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
            Objects.equals(basedOn, other.basedOn) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusReason, other.statusReason) && 
            Objects.equals(category, other.category) && 
            Objects.equals(code, other.code) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(performed, other.performed) && 
            Objects.equals(recorder, other.recorder) && 
            Objects.equals(asserter, other.asserter) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(location, other.location) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(bodySite, other.bodySite) && 
            Objects.equals(outcome, other.outcome) && 
            Objects.equals(report, other.report) && 
            Objects.equals(complication, other.complication) && 
            Objects.equals(complicationDetail, other.complicationDetail) && 
            Objects.equals(followUp, other.followUp) && 
            Objects.equals(note, other.note) && 
            Objects.equals(focalDevice, other.focalDevice) && 
            Objects.equals(usedReference, other.usedReference) && 
            Objects.equals(usedCode, other.usedCode);
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
                basedOn, 
                partOf, 
                status, 
                statusReason, 
                category, 
                code, 
                subject, 
                encounter, 
                performed, 
                recorder, 
                asserter, 
                performer, 
                location, 
                reasonCode, 
                reasonReference, 
                bodySite, 
                outcome, 
                report, 
                complication, 
                complicationDetail, 
                followUp, 
                note, 
                focalDevice, 
                usedReference, 
                usedCode);
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
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private ProcedureStatus status;
        private CodeableConcept statusReason;
        private CodeableConcept category;
        private CodeableConcept code;
        private Reference subject;
        private Reference encounter;
        private Element performed;
        private Reference recorder;
        private Reference asserter;
        private List<Performer> performer = new ArrayList<>();
        private Reference location;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<CodeableConcept> bodySite = new ArrayList<>();
        private CodeableConcept outcome;
        private List<Reference> report = new ArrayList<>();
        private List<CodeableConcept> complication = new ArrayList<>();
        private List<Reference> complicationDetail = new ArrayList<>();
        private List<CodeableConcept> followUp = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<FocalDevice> focalDevice = new ArrayList<>();
        private List<Reference> usedReference = new ArrayList<>();
        private List<CodeableConcept> usedCode = new ArrayList<>();

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
         * Business identifiers assigned to this procedure by the performer or other systems which remain constant as the 
         * resource is updated and is propagated from server to server.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     External Identifiers for this procedure
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
         * Business identifiers assigned to this procedure by the performer or other systems which remain constant as the 
         * resource is updated and is propagated from server to server.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     External Identifiers for this procedure
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
         * The URL pointing to a FHIR-defined protocol, guideline, order set or other definition that is adhered to in whole or 
         * in part by this Procedure.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The URL pointing to a FHIR-defined protocol, guideline, order set or other definition that is adhered to in whole or 
         * in part by this Procedure.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
            this.instantiatesCanonical = new ArrayList<>(instantiatesCanonical);
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally maintained protocol, guideline, order set or other definition that is adhered to in 
         * whole or in part by this Procedure.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
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
         * <p>
         * The URL pointing to an externally maintained protocol, guideline, order set or other definition that is adhered to in 
         * whole or in part by this Procedure.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
            this.instantiatesUri = new ArrayList<>(instantiatesUri);
            return this;
        }

        /**
         * <p>
         * A reference to a resource that contains details of the request for this procedure.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param basedOn
         *     A request for this procedure
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
         * A reference to a resource that contains details of the request for this procedure.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param basedOn
         *     A request for this procedure
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
         * A larger event of which this particular procedure is a component or step.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param partOf
         *     Part of referenced event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A larger event of which this particular procedure is a component or step.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param partOf
         *     Part of referenced event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * <p>
         * A code specifying the state of the procedure. Generally, this will be the in-progress or completed state.
         * </p>
         * 
         * @param status
         *     preparation | in-progress | not-done | suspended | aborted | completed | entered-in-error | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ProcedureStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * Captures the reason for the current state of the procedure.
         * </p>
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
         * <p>
         * A code that classifies the procedure for searching, sorting and display purposes (e.g. "Surgical Procedure").
         * </p>
         * 
         * @param category
         *     Classification of the procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(CodeableConcept category) {
            this.category = category;
            return this;
        }

        /**
         * <p>
         * The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. 
         * "Laparoscopic Appendectomy").
         * </p>
         * 
         * @param code
         *     Identification of the procedure
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
         * The person, animal or group on which the procedure was performed.
         * </p>
         * 
         * @param subject
         *     Who the procedure was performed on
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
         * The Encounter during which this Procedure was created or performed or to which the creation of this record is tightly 
         * associated.
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
         * Estimated or actual date, date-time, period, or age when the procedure was performed. Allows a period to support 
         * complex procedures that span more than one date, and also allows for the length of the procedure to be captured.
         * </p>
         * 
         * @param performed
         *     When the procedure was performed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performed(Element performed) {
            this.performed = performed;
            return this;
        }

        /**
         * <p>
         * Individual who recorded the record and takes responsibility for its content.
         * </p>
         * 
         * @param recorder
         *     Who recorded the procedure
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
         * Individual who is making the procedure statement.
         * </p>
         * 
         * @param asserter
         *     Person who asserts this procedure
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
         * Limited to "real" people rather than equipment.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param performer
         *     The people who performed the procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Performer... performer) {
            for (Performer value : performer) {
                this.performer.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Limited to "real" people rather than equipment.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param performer
         *     The people who performed the procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Collection<Performer> performer) {
            this.performer = new ArrayList<>(performer);
            return this;
        }

        /**
         * <p>
         * The location where the procedure actually happened. E.g. a newborn at home, a tracheostomy at a restaurant.
         * </p>
         * 
         * @param location
         *     Where the procedure happened
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(Reference location) {
            this.location = location;
            return this;
        }

        /**
         * <p>
         * The coded reason why the procedure was performed. This may be a coded entity of some type, or may simply be present as 
         * text.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param reasonCode
         *     Coded reason procedure performed
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
         * <p>
         * The coded reason why the procedure was performed. This may be a coded entity of some type, or may simply be present as 
         * text.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonCode
         *     Coded reason procedure performed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode = new ArrayList<>(reasonCode);
            return this;
        }

        /**
         * <p>
         * The justification of why the procedure was performed.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param reasonReference
         *     The justification that the procedure was performed
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
         * <p>
         * The justification of why the procedure was performed.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonReference
         *     The justification that the procedure was performed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference = new ArrayList<>(reasonReference);
            return this;
        }

        /**
         * <p>
         * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies 
         * of a lesion.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param bodySite
         *     Target body sites
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
         * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies 
         * of a lesion.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param bodySite
         *     Target body sites
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
         * The outcome of the procedure - did it resolve the reasons for the procedure being performed?
         * </p>
         * 
         * @param outcome
         *     The result of procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder outcome(CodeableConcept outcome) {
            this.outcome = outcome;
            return this;
        }

        /**
         * <p>
         * This could be a histology result, pathology report, surgical report, etc.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param report
         *     Any report resulting from the procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder report(Reference... report) {
            for (Reference value : report) {
                this.report.add(value);
            }
            return this;
        }

        /**
         * <p>
         * This could be a histology result, pathology report, surgical report, etc.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param report
         *     Any report resulting from the procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder report(Collection<Reference> report) {
            this.report = new ArrayList<>(report);
            return this;
        }

        /**
         * <p>
         * Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally 
         * tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' 
         * issues.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param complication
         *     Complication following the procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder complication(CodeableConcept... complication) {
            for (CodeableConcept value : complication) {
                this.complication.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally 
         * tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' 
         * issues.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param complication
         *     Complication following the procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder complication(Collection<CodeableConcept> complication) {
            this.complication = new ArrayList<>(complication);
            return this;
        }

        /**
         * <p>
         * Any complications that occurred during the procedure, or in the immediate post-performance period.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param complicationDetail
         *     A condition that is a result of the procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder complicationDetail(Reference... complicationDetail) {
            for (Reference value : complicationDetail) {
                this.complicationDetail.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Any complications that occurred during the procedure, or in the immediate post-performance period.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param complicationDetail
         *     A condition that is a result of the procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder complicationDetail(Collection<Reference> complicationDetail) {
            this.complicationDetail = new ArrayList<>(complicationDetail);
            return this;
        }

        /**
         * <p>
         * If the procedure required specific follow up - e.g. removal of sutures. The follow up may be represented as a simple 
         * note or could potentially be more complex, in which case the CarePlan resource can be used.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param followUp
         *     Instructions for follow up
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder followUp(CodeableConcept... followUp) {
            for (CodeableConcept value : followUp) {
                this.followUp.add(value);
            }
            return this;
        }

        /**
         * <p>
         * If the procedure required specific follow up - e.g. removal of sutures. The follow up may be represented as a simple 
         * note or could potentially be more complex, in which case the CarePlan resource can be used.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param followUp
         *     Instructions for follow up
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder followUp(Collection<CodeableConcept> followUp) {
            this.followUp = new ArrayList<>(followUp);
            return this;
        }

        /**
         * <p>
         * Any other notes and comments about the procedure.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param note
         *     Additional information about the procedure
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
         * Any other notes and comments about the procedure.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param note
         *     Additional information about the procedure
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
         * A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, 
         * attaching a wound-vac, etc.) as a focal portion of the Procedure.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param focalDevice
         *     Manipulated, implanted, or removed device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder focalDevice(FocalDevice... focalDevice) {
            for (FocalDevice value : focalDevice) {
                this.focalDevice.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, 
         * attaching a wound-vac, etc.) as a focal portion of the Procedure.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param focalDevice
         *     Manipulated, implanted, or removed device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder focalDevice(Collection<FocalDevice> focalDevice) {
            this.focalDevice = new ArrayList<>(focalDevice);
            return this;
        }

        /**
         * <p>
         * Identifies medications, devices and any other substance used as part of the procedure.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param usedReference
         *     Items used during procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder usedReference(Reference... usedReference) {
            for (Reference value : usedReference) {
                this.usedReference.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies medications, devices and any other substance used as part of the procedure.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param usedReference
         *     Items used during procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder usedReference(Collection<Reference> usedReference) {
            this.usedReference = new ArrayList<>(usedReference);
            return this;
        }

        /**
         * <p>
         * Identifies coded items that were used as part of the procedure.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param usedCode
         *     Coded items used during the procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder usedCode(CodeableConcept... usedCode) {
            for (CodeableConcept value : usedCode) {
                this.usedCode.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies coded items that were used as part of the procedure.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param usedCode
         *     Coded items used during the procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder usedCode(Collection<CodeableConcept> usedCode) {
            this.usedCode = new ArrayList<>(usedCode);
            return this;
        }

        @Override
        public Procedure build() {
            return new Procedure(this);
        }

        protected Builder from(Procedure procedure) {
            super.from(procedure);
            identifier.addAll(procedure.identifier);
            instantiatesCanonical.addAll(procedure.instantiatesCanonical);
            instantiatesUri.addAll(procedure.instantiatesUri);
            basedOn.addAll(procedure.basedOn);
            partOf.addAll(procedure.partOf);
            status = procedure.status;
            statusReason = procedure.statusReason;
            category = procedure.category;
            code = procedure.code;
            subject = procedure.subject;
            encounter = procedure.encounter;
            performed = procedure.performed;
            recorder = procedure.recorder;
            asserter = procedure.asserter;
            performer.addAll(procedure.performer);
            location = procedure.location;
            reasonCode.addAll(procedure.reasonCode);
            reasonReference.addAll(procedure.reasonReference);
            bodySite.addAll(procedure.bodySite);
            outcome = procedure.outcome;
            report.addAll(procedure.report);
            complication.addAll(procedure.complication);
            complicationDetail.addAll(procedure.complicationDetail);
            followUp.addAll(procedure.followUp);
            note.addAll(procedure.note);
            focalDevice.addAll(procedure.focalDevice);
            usedReference.addAll(procedure.usedReference);
            usedCode.addAll(procedure.usedCode);
            return this;
        }
    }

    /**
     * <p>
     * Limited to "real" people rather than equipment.
     * </p>
     */
    public static class Performer extends BackboneElement {
        private final CodeableConcept function;
        private final Reference actor;
        private final Reference onBehalfOf;

        private volatile int hashCode;

        private Performer(Builder builder) {
            super(builder);
            function = builder.function;
            actor = ValidationSupport.requireNonNull(builder.actor, "actor");
            onBehalfOf = builder.onBehalfOf;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Distinguishes the type of involvement of the performer in the procedure. For example, surgeon, anaesthetist, 
         * endoscopist.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getFunction() {
            return function;
        }

        /**
         * <p>
         * The practitioner who was involved in the procedure.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getActor() {
            return actor;
        }

        /**
         * <p>
         * The organization the device or practitioner was acting on behalf of.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getOnBehalfOf() {
            return onBehalfOf;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (function != null) || 
                (actor != null) || 
                (onBehalfOf != null);
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
                    accept(function, "function", visitor);
                    accept(actor, "actor", visitor);
                    accept(onBehalfOf, "onBehalfOf", visitor);
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
            Performer other = (Performer) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(function, other.function) && 
                Objects.equals(actor, other.actor) && 
                Objects.equals(onBehalfOf, other.onBehalfOf);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    function, 
                    actor, 
                    onBehalfOf);
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
            private CodeableConcept function;
            private Reference actor;
            private Reference onBehalfOf;

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
             * Distinguishes the type of involvement of the performer in the procedure. For example, surgeon, anaesthetist, 
             * endoscopist.
             * </p>
             * 
             * @param function
             *     Type of performance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder function(CodeableConcept function) {
                this.function = function;
                return this;
            }

            /**
             * <p>
             * The practitioner who was involved in the procedure.
             * </p>
             * 
             * @param actor
             *     The reference to the practitioner
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder actor(Reference actor) {
                this.actor = actor;
                return this;
            }

            /**
             * <p>
             * The organization the device or practitioner was acting on behalf of.
             * </p>
             * 
             * @param onBehalfOf
             *     Organization the device or practitioner was acting for
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder onBehalfOf(Reference onBehalfOf) {
                this.onBehalfOf = onBehalfOf;
                return this;
            }

            @Override
            public Performer build() {
                return new Performer(this);
            }

            protected Builder from(Performer performer) {
                super.from(performer);
                function = performer.function;
                actor = performer.actor;
                onBehalfOf = performer.onBehalfOf;
                return this;
            }
        }
    }

    /**
     * <p>
     * A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, 
     * attaching a wound-vac, etc.) as a focal portion of the Procedure.
     * </p>
     */
    public static class FocalDevice extends BackboneElement {
        private final CodeableConcept action;
        private final Reference manipulated;

        private volatile int hashCode;

        private FocalDevice(Builder builder) {
            super(builder);
            action = builder.action;
            manipulated = ValidationSupport.requireNonNull(builder.manipulated, "manipulated");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * The kind of change that happened to the device during the procedure.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getAction() {
            return action;
        }

        /**
         * <p>
         * The device that was manipulated (changed) during the procedure.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getManipulated() {
            return manipulated;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (action != null) || 
                (manipulated != null);
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
                    accept(action, "action", visitor);
                    accept(manipulated, "manipulated", visitor);
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
            FocalDevice other = (FocalDevice) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(action, other.action) && 
                Objects.equals(manipulated, other.manipulated);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    action, 
                    manipulated);
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
            private CodeableConcept action;
            private Reference manipulated;

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
             * The kind of change that happened to the device during the procedure.
             * </p>
             * 
             * @param action
             *     Kind of change to device
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder action(CodeableConcept action) {
                this.action = action;
                return this;
            }

            /**
             * <p>
             * The device that was manipulated (changed) during the procedure.
             * </p>
             * 
             * @param manipulated
             *     Device that was changed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder manipulated(Reference manipulated) {
                this.manipulated = manipulated;
                return this;
            }

            @Override
            public FocalDevice build() {
                return new FocalDevice(this);
            }

            protected Builder from(FocalDevice focalDevice) {
                super.from(focalDevice);
                action = focalDevice.action;
                manipulated = focalDevice.manipulated;
                return this;
            }
        }
    }
}
