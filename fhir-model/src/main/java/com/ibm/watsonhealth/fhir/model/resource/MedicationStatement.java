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

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Dosage;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.MedicationStatementStatus;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A record of a medication that is being consumed by a patient. A MedicationStatement may indicate that the patient may 
 * be taking the medication now or has taken the medication in the past or will be taking the medication in the future. 
 * The source of this information can be the patient, significant other (such as a family member or spouse), or a 
 * clinician. A common scenario where this information is captured is during the history taking process during a patient 
 * visit or stay. The medication information may come from sources such as the patient's memory, from a prescription 
 * bottle, or from a list of medications the patient, clinician or other party maintains. 
 * </p>
 * <p>
 * The primary difference between a medication statement and a medication administration is that the medication 
 * administration has complete administration information and is based on actual administration information from the 
 * person who administered the medication. A medication statement is often, if not always, less specific. There is no 
 * required date/time when the medication was administered, in fact we only know that a source has reported the patient 
 * is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete 
 * or missing or less precise. As stated earlier, the medication statement information may come from the patient's 
 * memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains. 
 * Medication administration is more formal and is not missing detailed information.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MedicationStatement extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Reference> basedOn;
    private final List<Reference> partOf;
    private final MedicationStatementStatus status;
    private final List<CodeableConcept> statusReason;
    private final CodeableConcept category;
    private final Element medication;
    private final Reference subject;
    private final Reference context;
    private final Element effective;
    private final DateTime dateAsserted;
    private final Reference informationSource;
    private final List<Reference> derivedFrom;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final List<Annotation> note;
    private final List<Dosage> dosage;

    private volatile int hashCode;

    private MedicationStatement(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        basedOn = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.basedOn, "basedOn"));
        partOf = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.partOf, "partOf"));
        status = ValidationSupport.requireNonNull(builder.status, "status");
        statusReason = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.statusReason, "statusReason"));
        category = builder.category;
        medication = ValidationSupport.requireChoiceElement(builder.medication, "medication", CodeableConcept.class, Reference.class);
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        context = builder.context;
        effective = ValidationSupport.choiceElement(builder.effective, "effective", DateTime.class, Period.class);
        dateAsserted = builder.dateAsserted;
        informationSource = builder.informationSource;
        derivedFrom = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.derivedFrom, "derivedFrom"));
        reasonCode = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonCode, "reasonCode"));
        reasonReference = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonReference, "reasonReference"));
        note = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.note, "note"));
        dosage = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.dosage, "dosage"));
    }

    /**
     * <p>
     * Identifiers associated with this Medication Statement that are defined by business processes and/or used to refer to 
     * it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
     * this resource by the performer or other systems and remain constant as the resource is updated and propagates from 
     * server to server.
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
     * A plan, proposal or order that is fulfilled in whole or in part by this event.
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
     * A larger event of which this particular event is a component or step.
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
     * A code representing the patient or other source's judgment about the state of the medication used that this statement 
     * is about. Generally, this will be active or completed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MedicationStatementStatus}.
     */
    public MedicationStatementStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Captures the reason for the current state of the MedicationStatement.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getStatusReason() {
        return statusReason;
    }

    /**
     * <p>
     * Indicates where the medication is expected to be consumed or administered.
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
     * Identifies the medication being administered. This is either a link to a resource representing the details of the 
     * medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getMedication() {
        return medication;
    }

    /**
     * <p>
     * The person, animal or group who is/was taking the medication.
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
     * The encounter or episode of care that establishes the context for this MedicationStatement.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getContext() {
        return context;
    }

    /**
     * <p>
     * The interval of time during which it is being asserted that the patient is/was/will be taking the medication (or was 
     * not taking, when the MedicationStatement.taken element is No).
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
     * The date when the medication statement was asserted by the information source.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getDateAsserted() {
        return dateAsserted;
    }

    /**
     * <p>
     * The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom 
     * when a MedicationStatement is derived from other resources, e.g. Claim or MedicationRequest.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getInformationSource() {
        return informationSource;
    }

    /**
     * <p>
     * Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or 
     * is used to derive the MedicationStatement.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * <p>
     * A reason for why the medication is being/was taken.
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
     * Condition or observation that supports why the medication is being/was taken.
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
     * Provides extra information about the medication statement that is not conveyed by the other attributes.
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
     * Indicates how the medication is/was or should be taken by the patient.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Dosage}.
     */
    public List<Dosage> getDosage() {
        return dosage;
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
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(statusReason, "statusReason", visitor, CodeableConcept.class);
                accept(category, "category", visitor);
                accept(medication, "medication", visitor);
                accept(subject, "subject", visitor);
                accept(context, "context", visitor);
                accept(effective, "effective", visitor);
                accept(dateAsserted, "dateAsserted", visitor);
                accept(informationSource, "informationSource", visitor);
                accept(derivedFrom, "derivedFrom", visitor, Reference.class);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(dosage, "dosage", visitor, Dosage.class);
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
        MedicationStatement other = (MedicationStatement) obj;
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
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusReason, other.statusReason) && 
            Objects.equals(category, other.category) && 
            Objects.equals(medication, other.medication) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(context, other.context) && 
            Objects.equals(effective, other.effective) && 
            Objects.equals(dateAsserted, other.dateAsserted) && 
            Objects.equals(informationSource, other.informationSource) && 
            Objects.equals(derivedFrom, other.derivedFrom) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(note, other.note) && 
            Objects.equals(dosage, other.dosage);
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
                partOf, 
                status, 
                statusReason, 
                category, 
                medication, 
                subject, 
                context, 
                effective, 
                dateAsserted, 
                informationSource, 
                derivedFrom, 
                reasonCode, 
                reasonReference, 
                note, 
                dosage);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status, medication, subject).from(this);
    }

    public Builder toBuilder(MedicationStatementStatus status, Element medication, Reference subject) {
        return new Builder(status, medication, subject).from(this);
    }

    public static Builder builder(MedicationStatementStatus status, Element medication, Reference subject) {
        return new Builder(status, medication, subject);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final MedicationStatementStatus status;
        private final Element medication;
        private final Reference subject;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private List<CodeableConcept> statusReason = new ArrayList<>();
        private CodeableConcept category;
        private Reference context;
        private Element effective;
        private DateTime dateAsserted;
        private Reference informationSource;
        private List<Reference> derivedFrom = new ArrayList<>();
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Dosage> dosage = new ArrayList<>();

        private Builder(MedicationStatementStatus status, Element medication, Reference subject) {
            super();
            this.status = status;
            this.medication = medication;
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
         * Identifiers associated with this Medication Statement that are defined by business processes and/or used to refer to 
         * it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
         * this resource by the performer or other systems and remain constant as the resource is updated and propagates from 
         * server to server.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     External identifier
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
         * Identifiers associated with this Medication Statement that are defined by business processes and/or used to refer to 
         * it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
         * this resource by the performer or other systems and remain constant as the resource is updated and propagates from 
         * server to server.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     External identifier
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
         * A plan, proposal or order that is fulfilled in whole or in part by this event.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param basedOn
         *     Fulfils plan, proposal or order
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
         * A plan, proposal or order that is fulfilled in whole or in part by this event.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param basedOn
         *     Fulfils plan, proposal or order
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
         * A larger event of which this particular event is a component or step.
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
         * A larger event of which this particular event is a component or step.
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
         * Captures the reason for the current state of the MedicationStatement.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param statusReason
         *     Reason for current status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusReason(CodeableConcept... statusReason) {
            for (CodeableConcept value : statusReason) {
                this.statusReason.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Captures the reason for the current state of the MedicationStatement.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param statusReason
         *     Reason for current status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusReason(Collection<CodeableConcept> statusReason) {
            this.statusReason = new ArrayList<>(statusReason);
            return this;
        }

        /**
         * <p>
         * Indicates where the medication is expected to be consumed or administered.
         * </p>
         * 
         * @param category
         *     Type of medication usage
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
         * The encounter or episode of care that establishes the context for this MedicationStatement.
         * </p>
         * 
         * @param context
         *     Encounter / Episode associated with MedicationStatement
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder context(Reference context) {
            this.context = context;
            return this;
        }

        /**
         * <p>
         * The interval of time during which it is being asserted that the patient is/was/will be taking the medication (or was 
         * not taking, when the MedicationStatement.taken element is No).
         * </p>
         * 
         * @param effective
         *     The date/time or interval when the medication is/was/will be taken
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
         * The date when the medication statement was asserted by the information source.
         * </p>
         * 
         * @param dateAsserted
         *     When the statement was asserted?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dateAsserted(DateTime dateAsserted) {
            this.dateAsserted = dateAsserted;
            return this;
        }

        /**
         * <p>
         * The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom 
         * when a MedicationStatement is derived from other resources, e.g. Claim or MedicationRequest.
         * </p>
         * 
         * @param informationSource
         *     Person or organization that provided the information about the taking of this medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder informationSource(Reference informationSource) {
            this.informationSource = informationSource;
            return this;
        }

        /**
         * <p>
         * Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or 
         * is used to derive the MedicationStatement.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param derivedFrom
         *     Additional supporting information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder derivedFrom(Reference... derivedFrom) {
            for (Reference value : derivedFrom) {
                this.derivedFrom.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or 
         * is used to derive the MedicationStatement.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param derivedFrom
         *     Additional supporting information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder derivedFrom(Collection<Reference> derivedFrom) {
            this.derivedFrom = new ArrayList<>(derivedFrom);
            return this;
        }

        /**
         * <p>
         * A reason for why the medication is being/was taken.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param reasonCode
         *     Reason for why the medication is being/was taken
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
         * A reason for why the medication is being/was taken.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonCode
         *     Reason for why the medication is being/was taken
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
         * Condition or observation that supports why the medication is being/was taken.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param reasonReference
         *     Condition or observation that supports why the medication is being/was taken
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
         * Condition or observation that supports why the medication is being/was taken.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonReference
         *     Condition or observation that supports why the medication is being/was taken
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
         * Provides extra information about the medication statement that is not conveyed by the other attributes.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param note
         *     Further information about the statement
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
         * Provides extra information about the medication statement that is not conveyed by the other attributes.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param note
         *     Further information about the statement
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
         * Indicates how the medication is/was or should be taken by the patient.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param dosage
         *     Details of how medication is/was taken or should be taken
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dosage(Dosage... dosage) {
            for (Dosage value : dosage) {
                this.dosage.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates how the medication is/was or should be taken by the patient.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param dosage
         *     Details of how medication is/was taken or should be taken
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dosage(Collection<Dosage> dosage) {
            this.dosage = new ArrayList<>(dosage);
            return this;
        }

        @Override
        public MedicationStatement build() {
            return new MedicationStatement(this);
        }

        private Builder from(MedicationStatement medicationStatement) {
            id = medicationStatement.id;
            meta = medicationStatement.meta;
            implicitRules = medicationStatement.implicitRules;
            language = medicationStatement.language;
            text = medicationStatement.text;
            contained.addAll(medicationStatement.contained);
            extension.addAll(medicationStatement.extension);
            modifierExtension.addAll(medicationStatement.modifierExtension);
            identifier.addAll(medicationStatement.identifier);
            basedOn.addAll(medicationStatement.basedOn);
            partOf.addAll(medicationStatement.partOf);
            statusReason.addAll(medicationStatement.statusReason);
            category = medicationStatement.category;
            context = medicationStatement.context;
            effective = medicationStatement.effective;
            dateAsserted = medicationStatement.dateAsserted;
            informationSource = medicationStatement.informationSource;
            derivedFrom.addAll(medicationStatement.derivedFrom);
            reasonCode.addAll(medicationStatement.reasonCode);
            reasonReference.addAll(medicationStatement.reasonReference);
            note.addAll(medicationStatement.note);
            dosage.addAll(medicationStatement.dosage);
            return this;
        }
    }
}
