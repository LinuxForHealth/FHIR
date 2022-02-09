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
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Dosage;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.MedicationStatementStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A record of a medication that is being consumed by a patient. A MedicationStatement may indicate that the patient may 
 * be taking the medication now or has taken the medication in the past or will be taking the medication in the future. 
 * The source of this information can be the patient, significant other (such as a family member or spouse), or a 
 * clinician. A common scenario where this information is captured is during the history taking process during a patient 
 * visit or stay. The medication information may come from sources such as the patient's memory, from a prescription 
 * bottle, or from a list of medications the patient, clinician or other party maintains. 
 * 
 * <p>The primary difference between a medication statement and a medication administration is that the medication 
 * administration has complete administration information and is based on actual administration information from the 
 * person who administered the medication. A medication statement is often, if not always, less specific. There is no 
 * required date/time when the medication was administered, in fact we only know that a source has reported the patient 
 * is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete 
 * or missing or less precise. As stated earlier, the medication statement information may come from the patient's 
 * memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains. 
 * Medication administration is more formal and is not missing detailed information.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "medicationStatement-0",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/medication-statement-category",
    expression = "category.exists() implies (category.memberOf('http://hl7.org/fhir/ValueSet/medication-statement-category', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/MedicationStatement",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicationStatement extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @ReferenceTarget({ "MedicationRequest", "CarePlan", "ServiceRequest" })
    private final List<Reference> basedOn;
    @Summary
    @ReferenceTarget({ "MedicationAdministration", "MedicationDispense", "MedicationStatement", "Procedure", "Observation" })
    private final List<Reference> partOf;
    @Summary
    @Binding(
        bindingName = "MedicationStatementStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A coded concept indicating the current status of a MedicationStatement.",
        valueSet = "http://hl7.org/fhir/ValueSet/medication-statement-status|4.3.0-CIBUILD"
    )
    @Required
    private final MedicationStatementStatus status;
    @Binding(
        bindingName = "MedicationStatementStatusReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A coded concept indicating the reason for the status of the statement.",
        valueSet = "http://hl7.org/fhir/ValueSet/reason-medication-status-codes"
    )
    private final List<CodeableConcept> statusReason;
    @Summary
    @Binding(
        bindingName = "MedicationStatementCategory",
        strength = BindingStrength.Value.PREFERRED,
        description = "A coded concept identifying where the medication included in the MedicationStatement is expected to be consumed or administered.",
        valueSet = "http://hl7.org/fhir/ValueSet/medication-statement-category"
    )
    private final CodeableConcept category;
    @Summary
    @ReferenceTarget({ "Medication" })
    @Choice({ CodeableConcept.class, Reference.class })
    @Binding(
        bindingName = "MedicationCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A coded concept identifying the substance or product being taken.",
        valueSet = "http://hl7.org/fhir/ValueSet/medication-codes"
    )
    @Required
    private final Element medication;
    @Summary
    @ReferenceTarget({ "Patient", "Group" })
    @Required
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "Encounter", "EpisodeOfCare" })
    private final Reference context;
    @Summary
    @Choice({ DateTime.class, Period.class })
    private final Element effective;
    @Summary
    private final DateTime dateAsserted;
    @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Organization" })
    private final Reference informationSource;
    private final List<Reference> derivedFrom;
    @Binding(
        bindingName = "MedicationReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A coded concept identifying why the medication is being taken.",
        valueSet = "http://hl7.org/fhir/ValueSet/condition-code"
    )
    private final List<CodeableConcept> reasonCode;
    @ReferenceTarget({ "Condition", "Observation", "DiagnosticReport" })
    private final List<Reference> reasonReference;
    private final List<Annotation> note;
    private final List<Dosage> dosage;

    private MedicationStatement(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        partOf = Collections.unmodifiableList(builder.partOf);
        status = builder.status;
        statusReason = Collections.unmodifiableList(builder.statusReason);
        category = builder.category;
        medication = builder.medication;
        subject = builder.subject;
        context = builder.context;
        effective = builder.effective;
        dateAsserted = builder.dateAsserted;
        informationSource = builder.informationSource;
        derivedFrom = Collections.unmodifiableList(builder.derivedFrom);
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        note = Collections.unmodifiableList(builder.note);
        dosage = Collections.unmodifiableList(builder.dosage);
    }

    /**
     * Identifiers associated with this Medication Statement that are defined by business processes and/or used to refer to 
     * it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
     * this resource by the performer or other systems and remain constant as the resource is updated and propagates from 
     * server to server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A plan, proposal or order that is fulfilled in whole or in part by this event.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * A larger event of which this particular event is a component or step.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * A code representing the patient or other source's judgment about the state of the medication used that this statement 
     * is about. Generally, this will be active or completed.
     * 
     * @return
     *     An immutable object of type {@link MedicationStatementStatus} that is non-null.
     */
    public MedicationStatementStatus getStatus() {
        return status;
    }

    /**
     * Captures the reason for the current state of the MedicationStatement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getStatusReason() {
        return statusReason;
    }

    /**
     * Indicates where the medication is expected to be consumed or administered.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCategory() {
        return category;
    }

    /**
     * Identifies the medication being administered. This is either a link to a resource representing the details of the 
     * medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} or {@link Reference} that is non-null.
     */
    public Element getMedication() {
        return medication;
    }

    /**
     * The person, animal or group who is/was taking the medication.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The encounter or episode of care that establishes the context for this MedicationStatement.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getContext() {
        return context;
    }

    /**
     * The interval of time during which it is being asserted that the patient is/was/will be taking the medication (or was 
     * not taking, when the MedicationStatement.taken element is No).
     * 
     * @return
     *     An immutable object of type {@link DateTime} or {@link Period} that may be null.
     */
    public Element getEffective() {
        return effective;
    }

    /**
     * The date when the medication statement was asserted by the information source.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getDateAsserted() {
        return dateAsserted;
    }

    /**
     * The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom 
     * when a MedicationStatement is derived from other resources, e.g. Claim or MedicationRequest.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getInformationSource() {
        return informationSource;
    }

    /**
     * Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or 
     * is used to derive the MedicationStatement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * A reason for why the medication is being/was taken.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Condition or observation that supports why the medication is being/was taken.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * Provides extra information about the medication statement that is not conveyed by the other attributes.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Indicates how the medication is/was or should be taken by the patient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Dosage} that may be empty.
     */
    public List<Dosage> getDosage() {
        return dosage;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !basedOn.isEmpty() || 
            !partOf.isEmpty() || 
            (status != null) || 
            !statusReason.isEmpty() || 
            (category != null) || 
            (medication != null) || 
            (subject != null) || 
            (context != null) || 
            (effective != null) || 
            (dateAsserted != null) || 
            (informationSource != null) || 
            !derivedFrom.isEmpty() || 
            !reasonCode.isEmpty() || 
            !reasonReference.isEmpty() || 
            !note.isEmpty() || 
            !dosage.isEmpty();
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
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private MedicationStatementStatus status;
        private List<CodeableConcept> statusReason = new ArrayList<>();
        private CodeableConcept category;
        private Element medication;
        private Reference subject;
        private Reference context;
        private Element effective;
        private DateTime dateAsserted;
        private Reference informationSource;
        private List<Reference> derivedFrom = new ArrayList<>();
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Dosage> dosage = new ArrayList<>();

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
         * Identifiers associated with this Medication Statement that are defined by business processes and/or used to refer to 
         * it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
         * this resource by the performer or other systems and remain constant as the resource is updated and propagates from 
         * server to server.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Identifiers associated with this Medication Statement that are defined by business processes and/or used to refer to 
         * it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
         * this resource by the performer or other systems and remain constant as the resource is updated and propagates from 
         * server to server.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     External identifier
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
         * A plan, proposal or order that is fulfilled in whole or in part by this event.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicationRequest}</li>
         * <li>{@link CarePlan}</li>
         * <li>{@link ServiceRequest}</li>
         * </ul>
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
         * A plan, proposal or order that is fulfilled in whole or in part by this event.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicationRequest}</li>
         * <li>{@link CarePlan}</li>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     Fulfils plan, proposal or order
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * A larger event of which this particular event is a component or step.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicationAdministration}</li>
         * <li>{@link MedicationDispense}</li>
         * <li>{@link MedicationStatement}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Observation}</li>
         * </ul>
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
         * A larger event of which this particular event is a component or step.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicationAdministration}</li>
         * <li>{@link MedicationDispense}</li>
         * <li>{@link MedicationStatement}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Observation}</li>
         * </ul>
         * 
         * @param partOf
         *     Part of referenced event
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * A code representing the patient or other source's judgment about the state of the medication used that this statement 
         * is about. Generally, this will be active or completed.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     active | completed | entered-in-error | intended | stopped | on-hold | unknown | not-taken
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(MedicationStatementStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Captures the reason for the current state of the MedicationStatement.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Captures the reason for the current state of the MedicationStatement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param statusReason
         *     Reason for current status
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder statusReason(Collection<CodeableConcept> statusReason) {
            this.statusReason = new ArrayList<>(statusReason);
            return this;
        }

        /**
         * Indicates where the medication is expected to be consumed or administered.
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
         * Identifies the medication being administered. This is either a link to a resource representing the details of the 
         * medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
         * 
         * <p>This element is required.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link Reference}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Medication}</li>
         * </ul>
         * 
         * @param medication
         *     What medication was taken
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder medication(Element medication) {
            this.medication = medication;
            return this;
        }

        /**
         * The person, animal or group who is/was taking the medication.
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
         *     Who is/was taking the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The encounter or episode of care that establishes the context for this MedicationStatement.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * <li>{@link EpisodeOfCare}</li>
         * </ul>
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
         * The interval of time during which it is being asserted that the patient is/was/will be taking the medication (or was 
         * not taking, when the MedicationStatement.taken element is No).
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link DateTime}</li>
         * <li>{@link Period}</li>
         * </ul>
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
         * The date when the medication statement was asserted by the information source.
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
         * The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom 
         * when a MedicationStatement is derived from other resources, e.g. Claim or MedicationRequest.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Organization}</li>
         * </ul>
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
         * Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or 
         * is used to derive the MedicationStatement.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or 
         * is used to derive the MedicationStatement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param derivedFrom
         *     Additional supporting information
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder derivedFrom(Collection<Reference> derivedFrom) {
            this.derivedFrom = new ArrayList<>(derivedFrom);
            return this;
        }

        /**
         * A reason for why the medication is being/was taken.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A reason for why the medication is being/was taken.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Reason for why the medication is being/was taken
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode = new ArrayList<>(reasonCode);
            return this;
        }

        /**
         * Condition or observation that supports why the medication is being/was taken.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * </ul>
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
         * Condition or observation that supports why the medication is being/was taken.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Condition or observation that supports why the medication is being/was taken
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference = new ArrayList<>(reasonReference);
            return this;
        }

        /**
         * Provides extra information about the medication statement that is not conveyed by the other attributes.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Provides extra information about the medication statement that is not conveyed by the other attributes.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Further information about the statement
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
         * Indicates how the medication is/was or should be taken by the patient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Indicates how the medication is/was or should be taken by the patient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param dosage
         *     Details of how medication is/was taken or should be taken
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder dosage(Collection<Dosage> dosage) {
            this.dosage = new ArrayList<>(dosage);
            return this;
        }

        /**
         * Build the {@link MedicationStatement}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>medication</li>
         * <li>subject</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MedicationStatement}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicationStatement per the base specification
         */
        @Override
        public MedicationStatement build() {
            MedicationStatement medicationStatement = new MedicationStatement(this);
            if (validating) {
                validate(medicationStatement);
            }
            return medicationStatement;
        }

        protected void validate(MedicationStatement medicationStatement) {
            super.validate(medicationStatement);
            ValidationSupport.checkList(medicationStatement.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(medicationStatement.basedOn, "basedOn", Reference.class);
            ValidationSupport.checkList(medicationStatement.partOf, "partOf", Reference.class);
            ValidationSupport.requireNonNull(medicationStatement.status, "status");
            ValidationSupport.checkList(medicationStatement.statusReason, "statusReason", CodeableConcept.class);
            ValidationSupport.requireChoiceElement(medicationStatement.medication, "medication", CodeableConcept.class, Reference.class);
            ValidationSupport.requireNonNull(medicationStatement.subject, "subject");
            ValidationSupport.choiceElement(medicationStatement.effective, "effective", DateTime.class, Period.class);
            ValidationSupport.checkList(medicationStatement.derivedFrom, "derivedFrom", Reference.class);
            ValidationSupport.checkList(medicationStatement.reasonCode, "reasonCode", CodeableConcept.class);
            ValidationSupport.checkList(medicationStatement.reasonReference, "reasonReference", Reference.class);
            ValidationSupport.checkList(medicationStatement.note, "note", Annotation.class);
            ValidationSupport.checkList(medicationStatement.dosage, "dosage", Dosage.class);
            ValidationSupport.checkReferenceType(medicationStatement.basedOn, "basedOn", "MedicationRequest", "CarePlan", "ServiceRequest");
            ValidationSupport.checkReferenceType(medicationStatement.partOf, "partOf", "MedicationAdministration", "MedicationDispense", "MedicationStatement", "Procedure", "Observation");
            ValidationSupport.checkReferenceType(medicationStatement.medication, "medication", "Medication");
            ValidationSupport.checkReferenceType(medicationStatement.subject, "subject", "Patient", "Group");
            ValidationSupport.checkReferenceType(medicationStatement.context, "context", "Encounter", "EpisodeOfCare");
            ValidationSupport.checkReferenceType(medicationStatement.informationSource, "informationSource", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Organization");
            ValidationSupport.checkReferenceType(medicationStatement.reasonReference, "reasonReference", "Condition", "Observation", "DiagnosticReport");
        }

        protected Builder from(MedicationStatement medicationStatement) {
            super.from(medicationStatement);
            identifier.addAll(medicationStatement.identifier);
            basedOn.addAll(medicationStatement.basedOn);
            partOf.addAll(medicationStatement.partOf);
            status = medicationStatement.status;
            statusReason.addAll(medicationStatement.statusReason);
            category = medicationStatement.category;
            medication = medicationStatement.medication;
            subject = medicationStatement.subject;
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
