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
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Dosage;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.MedicationDispenseStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Indicates that a medication product is to be or has been dispensed for a named person/patient. This includes a 
 * description of the medication product (supply) provided and the instructions for administering the medication. The 
 * medication dispense is the result of a pharmacy system responding to a medication order.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "mdd-1",
    level = "Rule",
    location = "(base)",
    description = "whenHandedOver cannot be before whenPrepared",
    expression = "whenHandedOver.empty() or whenPrepared.empty() or whenHandedOver >= whenPrepared",
    source = "http://hl7.org/fhir/StructureDefinition/MedicationDispense"
)
@Constraint(
    id = "medicationDispense-2",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/medicationdispense-category",
    expression = "category.exists() implies (category.memberOf('http://hl7.org/fhir/ValueSet/medicationdispense-category', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/MedicationDispense",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicationDispense extends DomainResource {
    private final List<Identifier> identifier;
    @ReferenceTarget({ "Procedure" })
    private final List<Reference> partOf;
    @Summary
    @Binding(
        bindingName = "MedicationDispenseStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A coded concept specifying the state of the dispense event.",
        valueSet = "http://hl7.org/fhir/ValueSet/medicationdispense-status|4.0.1"
    )
    @Required
    private final MedicationDispenseStatus status;
    @ReferenceTarget({ "DetectedIssue" })
    @Choice({ CodeableConcept.class, Reference.class })
    @Binding(
        bindingName = "MedicationDispenseStatusReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A code describing why a dispense was not performed.",
        valueSet = "http://hl7.org/fhir/ValueSet/medicationdispense-status-reason"
    )
    private final Element statusReason;
    @Binding(
        bindingName = "MedicationDispenseCategory",
        strength = BindingStrength.Value.PREFERRED,
        description = "A code describing where the dispensed medication is expected to be consumed or administered.",
        valueSet = "http://hl7.org/fhir/ValueSet/medicationdispense-category"
    )
    private final CodeableConcept category;
    @Summary
    @ReferenceTarget({ "Medication" })
    @Choice({ CodeableConcept.class, Reference.class })
    @Binding(
        bindingName = "MedicationCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A coded concept identifying which substance or product can be dispensed.",
        valueSet = "http://hl7.org/fhir/ValueSet/medication-codes"
    )
    @Required
    private final Element medication;
    @Summary
    @ReferenceTarget({ "Patient", "Group" })
    private final Reference subject;
    @ReferenceTarget({ "Encounter", "EpisodeOfCare" })
    private final Reference context;
    private final List<Reference> supportingInformation;
    private final List<Performer> performer;
    @ReferenceTarget({ "Location" })
    private final Reference location;
    @ReferenceTarget({ "MedicationRequest" })
    private final List<Reference> authorizingPrescription;
    @Binding(
        bindingName = "MedicationDispenseType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Indicates the type of dispensing event that is performed. For example, Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.",
        valueSet = "http://terminology.hl7.org/ValueSet/v3-ActPharmacySupplyType"
    )
    private final CodeableConcept type;
    private final SimpleQuantity quantity;
    private final SimpleQuantity daysSupply;
    @Summary
    private final DateTime whenPrepared;
    private final DateTime whenHandedOver;
    @ReferenceTarget({ "Location" })
    private final Reference destination;
    @ReferenceTarget({ "Patient", "Practitioner" })
    private final List<Reference> receiver;
    private final List<Annotation> note;
    private final List<Dosage> dosageInstruction;
    private final Substitution substitution;
    @ReferenceTarget({ "DetectedIssue" })
    private final List<Reference> detectedIssue;
    @ReferenceTarget({ "Provenance" })
    private final List<Reference> eventHistory;

    private MedicationDispense(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        partOf = Collections.unmodifiableList(builder.partOf);
        status = builder.status;
        statusReason = builder.statusReason;
        category = builder.category;
        medication = builder.medication;
        subject = builder.subject;
        context = builder.context;
        supportingInformation = Collections.unmodifiableList(builder.supportingInformation);
        performer = Collections.unmodifiableList(builder.performer);
        location = builder.location;
        authorizingPrescription = Collections.unmodifiableList(builder.authorizingPrescription);
        type = builder.type;
        quantity = builder.quantity;
        daysSupply = builder.daysSupply;
        whenPrepared = builder.whenPrepared;
        whenHandedOver = builder.whenHandedOver;
        destination = builder.destination;
        receiver = Collections.unmodifiableList(builder.receiver);
        note = Collections.unmodifiableList(builder.note);
        dosageInstruction = Collections.unmodifiableList(builder.dosageInstruction);
        substitution = builder.substitution;
        detectedIssue = Collections.unmodifiableList(builder.detectedIssue);
        eventHistory = Collections.unmodifiableList(builder.eventHistory);
    }

    /**
     * Identifiers associated with this Medication Dispense that are defined by business processes and/or used to refer to it 
     * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
     * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
     * to server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The procedure that trigger the dispense.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * A code specifying the state of the set of dispense events.
     * 
     * @return
     *     An immutable object of type {@link MedicationDispenseStatus} that is non-null.
     */
    public MedicationDispenseStatus getStatus() {
        return status;
    }

    /**
     * Indicates the reason why a dispense was not performed.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} or {@link Reference} that may be null.
     */
    public Element getStatusReason() {
        return statusReason;
    }

    /**
     * Indicates the type of medication dispense (for example, where the medication is expected to be consumed or 
     * administered (i.e. inpatient or outpatient)).
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
     * A link to a resource representing the person or the group to whom the medication will be given.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The encounter or episode of care that establishes the context for this event.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getContext() {
        return context;
    }

    /**
     * Additional information that supports the medication being dispensed.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSupportingInformation() {
        return supportingInformation;
    }

    /**
     * Indicates who or what performed the event.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Performer} that may be empty.
     */
    public List<Performer> getPerformer() {
        return performer;
    }

    /**
     * The principal physical location where the dispense was performed.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getLocation() {
        return location;
    }

    /**
     * Indicates the medication order that is being dispensed against.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAuthorizingPrescription() {
        return authorizingPrescription;
    }

    /**
     * Indicates the type of dispensing event that is performed. For example, Trial Fill, Completion of Trial, Partial Fill, 
     * Emergency Fill, Samples, etc.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * The amount of medication that has been dispensed. Includes unit of measure.
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that may be null.
     */
    public SimpleQuantity getQuantity() {
        return quantity;
    }

    /**
     * The amount of medication expressed as a timing amount.
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that may be null.
     */
    public SimpleQuantity getDaysSupply() {
        return daysSupply;
    }

    /**
     * The time when the dispensed product was packaged and reviewed.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getWhenPrepared() {
        return whenPrepared;
    }

    /**
     * The time the dispensed product was provided to the patient or their representative.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getWhenHandedOver() {
        return whenHandedOver;
    }

    /**
     * Identification of the facility/location where the medication was shipped to, as part of the dispense event.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getDestination() {
        return destination;
    }

    /**
     * Identifies the person who picked up the medication. This will usually be a patient or their caregiver, but some cases 
     * exist where it can be a healthcare professional.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReceiver() {
        return receiver;
    }

    /**
     * Extra information about the dispense that could not be conveyed in the other attributes.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Indicates how the medication is to be used by the patient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Dosage} that may be empty.
     */
    public List<Dosage> getDosageInstruction() {
        return dosageInstruction;
    }

    /**
     * Indicates whether or not substitution was made as part of the dispense. In some cases, substitution will be expected 
     * but does not happen, in other cases substitution is not expected but does happen. This block explains what 
     * substitution did or did not happen and why. If nothing is specified, substitution was not done.
     * 
     * @return
     *     An immutable object of type {@link Substitution} that may be null.
     */
    public Substitution getSubstitution() {
        return substitution;
    }

    /**
     * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
     * patient; e.g. drug-drug interaction, duplicate therapy, dosage alert etc.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getDetectedIssue() {
        return detectedIssue;
    }

    /**
     * A summary of the events of interest that have occurred, such as when the dispense was verified.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getEventHistory() {
        return eventHistory;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !partOf.isEmpty() || 
            (status != null) || 
            (statusReason != null) || 
            (category != null) || 
            (medication != null) || 
            (subject != null) || 
            (context != null) || 
            !supportingInformation.isEmpty() || 
            !performer.isEmpty() || 
            (location != null) || 
            !authorizingPrescription.isEmpty() || 
            (type != null) || 
            (quantity != null) || 
            (daysSupply != null) || 
            (whenPrepared != null) || 
            (whenHandedOver != null) || 
            (destination != null) || 
            !receiver.isEmpty() || 
            !note.isEmpty() || 
            !dosageInstruction.isEmpty() || 
            (substitution != null) || 
            !detectedIssue.isEmpty() || 
            !eventHistory.isEmpty();
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
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(statusReason, "statusReason", visitor);
                accept(category, "category", visitor);
                accept(medication, "medication", visitor);
                accept(subject, "subject", visitor);
                accept(context, "context", visitor);
                accept(supportingInformation, "supportingInformation", visitor, Reference.class);
                accept(performer, "performer", visitor, Performer.class);
                accept(location, "location", visitor);
                accept(authorizingPrescription, "authorizingPrescription", visitor, Reference.class);
                accept(type, "type", visitor);
                accept(quantity, "quantity", visitor);
                accept(daysSupply, "daysSupply", visitor);
                accept(whenPrepared, "whenPrepared", visitor);
                accept(whenHandedOver, "whenHandedOver", visitor);
                accept(destination, "destination", visitor);
                accept(receiver, "receiver", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(dosageInstruction, "dosageInstruction", visitor, Dosage.class);
                accept(substitution, "substitution", visitor);
                accept(detectedIssue, "detectedIssue", visitor, Reference.class);
                accept(eventHistory, "eventHistory", visitor, Reference.class);
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
        MedicationDispense other = (MedicationDispense) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusReason, other.statusReason) && 
            Objects.equals(category, other.category) && 
            Objects.equals(medication, other.medication) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(context, other.context) && 
            Objects.equals(supportingInformation, other.supportingInformation) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(location, other.location) && 
            Objects.equals(authorizingPrescription, other.authorizingPrescription) && 
            Objects.equals(type, other.type) && 
            Objects.equals(quantity, other.quantity) && 
            Objects.equals(daysSupply, other.daysSupply) && 
            Objects.equals(whenPrepared, other.whenPrepared) && 
            Objects.equals(whenHandedOver, other.whenHandedOver) && 
            Objects.equals(destination, other.destination) && 
            Objects.equals(receiver, other.receiver) && 
            Objects.equals(note, other.note) && 
            Objects.equals(dosageInstruction, other.dosageInstruction) && 
            Objects.equals(substitution, other.substitution) && 
            Objects.equals(detectedIssue, other.detectedIssue) && 
            Objects.equals(eventHistory, other.eventHistory);
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
                partOf, 
                status, 
                statusReason, 
                category, 
                medication, 
                subject, 
                context, 
                supportingInformation, 
                performer, 
                location, 
                authorizingPrescription, 
                type, 
                quantity, 
                daysSupply, 
                whenPrepared, 
                whenHandedOver, 
                destination, 
                receiver, 
                note, 
                dosageInstruction, 
                substitution, 
                detectedIssue, 
                eventHistory);
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
        private List<Reference> partOf = new ArrayList<>();
        private MedicationDispenseStatus status;
        private Element statusReason;
        private CodeableConcept category;
        private Element medication;
        private Reference subject;
        private Reference context;
        private List<Reference> supportingInformation = new ArrayList<>();
        private List<Performer> performer = new ArrayList<>();
        private Reference location;
        private List<Reference> authorizingPrescription = new ArrayList<>();
        private CodeableConcept type;
        private SimpleQuantity quantity;
        private SimpleQuantity daysSupply;
        private DateTime whenPrepared;
        private DateTime whenHandedOver;
        private Reference destination;
        private List<Reference> receiver = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Dosage> dosageInstruction = new ArrayList<>();
        private Substitution substitution;
        private List<Reference> detectedIssue = new ArrayList<>();
        private List<Reference> eventHistory = new ArrayList<>();

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
         * Identifiers associated with this Medication Dispense that are defined by business processes and/or used to refer to it 
         * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
         * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
         * to server.
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
         * Identifiers associated with this Medication Dispense that are defined by business processes and/or used to refer to it 
         * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
         * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
         * to server.
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
         * The procedure that trigger the dispense.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Procedure}</li>
         * </ul>
         * 
         * @param partOf
         *     Event that dispense is part of
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
         * The procedure that trigger the dispense.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Procedure}</li>
         * </ul>
         * 
         * @param partOf
         *     Event that dispense is part of
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
         * A code specifying the state of the set of dispense events.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     preparation | in-progress | cancelled | on-hold | completed | entered-in-error | stopped | declined | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(MedicationDispenseStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Indicates the reason why a dispense was not performed.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link CodeableConcept}</li>
         * <li>{@link Reference}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link DetectedIssue}</li>
         * </ul>
         * 
         * @param statusReason
         *     Why a dispense was not performed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusReason(Element statusReason) {
            this.statusReason = statusReason;
            return this;
        }

        /**
         * Indicates the type of medication dispense (for example, where the medication is expected to be consumed or 
         * administered (i.e. inpatient or outpatient)).
         * 
         * @param category
         *     Type of medication dispense
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
         *     What medication was supplied
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder medication(Element medication) {
            this.medication = medication;
            return this;
        }

        /**
         * A link to a resource representing the person or the group to whom the medication will be given.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param subject
         *     Who the dispense is for
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The encounter or episode of care that establishes the context for this event.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * <li>{@link EpisodeOfCare}</li>
         * </ul>
         * 
         * @param context
         *     Encounter / Episode associated with event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder context(Reference context) {
            this.context = context;
            return this;
        }

        /**
         * Additional information that supports the medication being dispensed.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInformation
         *     Information that supports the dispensing of the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInformation(Reference... supportingInformation) {
            for (Reference value : supportingInformation) {
                this.supportingInformation.add(value);
            }
            return this;
        }

        /**
         * Additional information that supports the medication being dispensed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInformation
         *     Information that supports the dispensing of the medication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder supportingInformation(Collection<Reference> supportingInformation) {
            this.supportingInformation = new ArrayList<>(supportingInformation);
            return this;
        }

        /**
         * Indicates who or what performed the event.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param performer
         *     Who performed event
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
         * Indicates who or what performed the event.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param performer
         *     Who performed event
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder performer(Collection<Performer> performer) {
            this.performer = new ArrayList<>(performer);
            return this;
        }

        /**
         * The principal physical location where the dispense was performed.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param location
         *     Where the dispense occurred
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(Reference location) {
            this.location = location;
            return this;
        }

        /**
         * Indicates the medication order that is being dispensed against.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicationRequest}</li>
         * </ul>
         * 
         * @param authorizingPrescription
         *     Medication order that authorizes the dispense
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder authorizingPrescription(Reference... authorizingPrescription) {
            for (Reference value : authorizingPrescription) {
                this.authorizingPrescription.add(value);
            }
            return this;
        }

        /**
         * Indicates the medication order that is being dispensed against.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicationRequest}</li>
         * </ul>
         * 
         * @param authorizingPrescription
         *     Medication order that authorizes the dispense
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder authorizingPrescription(Collection<Reference> authorizingPrescription) {
            this.authorizingPrescription = new ArrayList<>(authorizingPrescription);
            return this;
        }

        /**
         * Indicates the type of dispensing event that is performed. For example, Trial Fill, Completion of Trial, Partial Fill, 
         * Emergency Fill, Samples, etc.
         * 
         * @param type
         *     Trial fill, partial fill, emergency fill, etc.
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * The amount of medication that has been dispensed. Includes unit of measure.
         * 
         * @param quantity
         *     Amount dispensed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder quantity(SimpleQuantity quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * The amount of medication expressed as a timing amount.
         * 
         * @param daysSupply
         *     Amount of medication expressed as a timing amount
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder daysSupply(SimpleQuantity daysSupply) {
            this.daysSupply = daysSupply;
            return this;
        }

        /**
         * The time when the dispensed product was packaged and reviewed.
         * 
         * @param whenPrepared
         *     When product was packaged and reviewed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder whenPrepared(DateTime whenPrepared) {
            this.whenPrepared = whenPrepared;
            return this;
        }

        /**
         * The time the dispensed product was provided to the patient or their representative.
         * 
         * @param whenHandedOver
         *     When product was given out
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder whenHandedOver(DateTime whenHandedOver) {
            this.whenHandedOver = whenHandedOver;
            return this;
        }

        /**
         * Identification of the facility/location where the medication was shipped to, as part of the dispense event.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param destination
         *     Where the medication was sent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder destination(Reference destination) {
            this.destination = destination;
            return this;
        }

        /**
         * Identifies the person who picked up the medication. This will usually be a patient or their caregiver, but some cases 
         * exist where it can be a healthcare professional.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * </ul>
         * 
         * @param receiver
         *     Who collected the medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder receiver(Reference... receiver) {
            for (Reference value : receiver) {
                this.receiver.add(value);
            }
            return this;
        }

        /**
         * Identifies the person who picked up the medication. This will usually be a patient or their caregiver, but some cases 
         * exist where it can be a healthcare professional.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * </ul>
         * 
         * @param receiver
         *     Who collected the medication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder receiver(Collection<Reference> receiver) {
            this.receiver = new ArrayList<>(receiver);
            return this;
        }

        /**
         * Extra information about the dispense that could not be conveyed in the other attributes.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Information about the dispense
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
         * Extra information about the dispense that could not be conveyed in the other attributes.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Information about the dispense
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
         * Indicates how the medication is to be used by the patient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param dosageInstruction
         *     How the medication is to be used by the patient or administered by the caregiver
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dosageInstruction(Dosage... dosageInstruction) {
            for (Dosage value : dosageInstruction) {
                this.dosageInstruction.add(value);
            }
            return this;
        }

        /**
         * Indicates how the medication is to be used by the patient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param dosageInstruction
         *     How the medication is to be used by the patient or administered by the caregiver
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder dosageInstruction(Collection<Dosage> dosageInstruction) {
            this.dosageInstruction = new ArrayList<>(dosageInstruction);
            return this;
        }

        /**
         * Indicates whether or not substitution was made as part of the dispense. In some cases, substitution will be expected 
         * but does not happen, in other cases substitution is not expected but does happen. This block explains what 
         * substitution did or did not happen and why. If nothing is specified, substitution was not done.
         * 
         * @param substitution
         *     Whether a substitution was performed on the dispense
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder substitution(Substitution substitution) {
            this.substitution = substitution;
            return this;
        }

        /**
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
         * patient; e.g. drug-drug interaction, duplicate therapy, dosage alert etc.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DetectedIssue}</li>
         * </ul>
         * 
         * @param detectedIssue
         *     Clinical issue with action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder detectedIssue(Reference... detectedIssue) {
            for (Reference value : detectedIssue) {
                this.detectedIssue.add(value);
            }
            return this;
        }

        /**
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
         * patient; e.g. drug-drug interaction, duplicate therapy, dosage alert etc.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DetectedIssue}</li>
         * </ul>
         * 
         * @param detectedIssue
         *     Clinical issue with action
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder detectedIssue(Collection<Reference> detectedIssue) {
            this.detectedIssue = new ArrayList<>(detectedIssue);
            return this;
        }

        /**
         * A summary of the events of interest that have occurred, such as when the dispense was verified.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Provenance}</li>
         * </ul>
         * 
         * @param eventHistory
         *     A list of relevant lifecycle events
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder eventHistory(Reference... eventHistory) {
            for (Reference value : eventHistory) {
                this.eventHistory.add(value);
            }
            return this;
        }

        /**
         * A summary of the events of interest that have occurred, such as when the dispense was verified.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Provenance}</li>
         * </ul>
         * 
         * @param eventHistory
         *     A list of relevant lifecycle events
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder eventHistory(Collection<Reference> eventHistory) {
            this.eventHistory = new ArrayList<>(eventHistory);
            return this;
        }

        /**
         * Build the {@link MedicationDispense}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>medication</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MedicationDispense}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicationDispense per the base specification
         */
        @Override
        public MedicationDispense build() {
            MedicationDispense medicationDispense = new MedicationDispense(this);
            if (validating) {
                validate(medicationDispense);
            }
            return medicationDispense;
        }

        protected void validate(MedicationDispense medicationDispense) {
            super.validate(medicationDispense);
            ValidationSupport.checkList(medicationDispense.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(medicationDispense.partOf, "partOf", Reference.class);
            ValidationSupport.requireNonNull(medicationDispense.status, "status");
            ValidationSupport.choiceElement(medicationDispense.statusReason, "statusReason", CodeableConcept.class, Reference.class);
            ValidationSupport.requireChoiceElement(medicationDispense.medication, "medication", CodeableConcept.class, Reference.class);
            ValidationSupport.checkList(medicationDispense.supportingInformation, "supportingInformation", Reference.class);
            ValidationSupport.checkList(medicationDispense.performer, "performer", Performer.class);
            ValidationSupport.checkList(medicationDispense.authorizingPrescription, "authorizingPrescription", Reference.class);
            ValidationSupport.checkList(medicationDispense.receiver, "receiver", Reference.class);
            ValidationSupport.checkList(medicationDispense.note, "note", Annotation.class);
            ValidationSupport.checkList(medicationDispense.dosageInstruction, "dosageInstruction", Dosage.class);
            ValidationSupport.checkList(medicationDispense.detectedIssue, "detectedIssue", Reference.class);
            ValidationSupport.checkList(medicationDispense.eventHistory, "eventHistory", Reference.class);
            ValidationSupport.checkReferenceType(medicationDispense.partOf, "partOf", "Procedure");
            ValidationSupport.checkReferenceType(medicationDispense.statusReason, "statusReason", "DetectedIssue");
            ValidationSupport.checkReferenceType(medicationDispense.medication, "medication", "Medication");
            ValidationSupport.checkReferenceType(medicationDispense.subject, "subject", "Patient", "Group");
            ValidationSupport.checkReferenceType(medicationDispense.context, "context", "Encounter", "EpisodeOfCare");
            ValidationSupport.checkReferenceType(medicationDispense.location, "location", "Location");
            ValidationSupport.checkReferenceType(medicationDispense.authorizingPrescription, "authorizingPrescription", "MedicationRequest");
            ValidationSupport.checkReferenceType(medicationDispense.destination, "destination", "Location");
            ValidationSupport.checkReferenceType(medicationDispense.receiver, "receiver", "Patient", "Practitioner");
            ValidationSupport.checkReferenceType(medicationDispense.detectedIssue, "detectedIssue", "DetectedIssue");
            ValidationSupport.checkReferenceType(medicationDispense.eventHistory, "eventHistory", "Provenance");
        }

        protected Builder from(MedicationDispense medicationDispense) {
            super.from(medicationDispense);
            identifier.addAll(medicationDispense.identifier);
            partOf.addAll(medicationDispense.partOf);
            status = medicationDispense.status;
            statusReason = medicationDispense.statusReason;
            category = medicationDispense.category;
            medication = medicationDispense.medication;
            subject = medicationDispense.subject;
            context = medicationDispense.context;
            supportingInformation.addAll(medicationDispense.supportingInformation);
            performer.addAll(medicationDispense.performer);
            location = medicationDispense.location;
            authorizingPrescription.addAll(medicationDispense.authorizingPrescription);
            type = medicationDispense.type;
            quantity = medicationDispense.quantity;
            daysSupply = medicationDispense.daysSupply;
            whenPrepared = medicationDispense.whenPrepared;
            whenHandedOver = medicationDispense.whenHandedOver;
            destination = medicationDispense.destination;
            receiver.addAll(medicationDispense.receiver);
            note.addAll(medicationDispense.note);
            dosageInstruction.addAll(medicationDispense.dosageInstruction);
            substitution = medicationDispense.substitution;
            detectedIssue.addAll(medicationDispense.detectedIssue);
            eventHistory.addAll(medicationDispense.eventHistory);
            return this;
        }
    }

    /**
     * Indicates who or what performed the event.
     */
    public static class Performer extends BackboneElement {
        @Binding(
            bindingName = "MedicationDispensePerformerFunction",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A code describing the role an individual played in dispensing a medication.",
            valueSet = "http://hl7.org/fhir/ValueSet/medicationdispense-performer-function"
        )
        private final CodeableConcept function;
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization", "Patient", "Device", "RelatedPerson" })
        @Required
        private final Reference actor;

        private Performer(Builder builder) {
            super(builder);
            function = builder.function;
            actor = builder.actor;
        }

        /**
         * Distinguishes the type of performer in the dispense. For example, date enterer, packager, final checker.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getFunction() {
            return function;
        }

        /**
         * The device, practitioner, etc. who performed the action. It should be assumed that the actor is the dispenser of the 
         * medication.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getActor() {
            return actor;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (function != null) || 
                (actor != null);
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
                Objects.equals(actor, other.actor);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    function, 
                    actor);
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
             * Distinguishes the type of performer in the dispense. For example, date enterer, packager, final checker.
             * 
             * @param function
             *     Who performed the dispense and what they did
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder function(CodeableConcept function) {
                this.function = function;
                return this;
            }

            /**
             * The device, practitioner, etc. who performed the action. It should be assumed that the actor is the dispenser of the 
             * medication.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * <li>{@link Patient}</li>
             * <li>{@link Device}</li>
             * <li>{@link RelatedPerson}</li>
             * </ul>
             * 
             * @param actor
             *     Individual who was performing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder actor(Reference actor) {
                this.actor = actor;
                return this;
            }

            /**
             * Build the {@link Performer}
             * 
             * <p>Required elements:
             * <ul>
             * <li>actor</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Performer}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Performer per the base specification
             */
            @Override
            public Performer build() {
                Performer performer = new Performer(this);
                if (validating) {
                    validate(performer);
                }
                return performer;
            }

            protected void validate(Performer performer) {
                super.validate(performer);
                ValidationSupport.requireNonNull(performer.actor, "actor");
                ValidationSupport.checkReferenceType(performer.actor, "actor", "Practitioner", "PractitionerRole", "Organization", "Patient", "Device", "RelatedPerson");
                ValidationSupport.requireValueOrChildren(performer);
            }

            protected Builder from(Performer performer) {
                super.from(performer);
                function = performer.function;
                actor = performer.actor;
                return this;
            }
        }
    }

    /**
     * Indicates whether or not substitution was made as part of the dispense. In some cases, substitution will be expected 
     * but does not happen, in other cases substitution is not expected but does happen. This block explains what 
     * substitution did or did not happen and why. If nothing is specified, substitution was not done.
     */
    public static class Substitution extends BackboneElement {
        @Required
        private final Boolean wasSubstituted;
        @Binding(
            bindingName = "MedicationIntendedSubstitutionType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A coded concept describing whether a different medicinal product may be dispensed other than the product as specified exactly in the prescription.",
            valueSet = "http://terminology.hl7.org/ValueSet/v3-ActSubstanceAdminSubstitutionCode"
        )
        private final CodeableConcept type;
        @Binding(
            bindingName = "MedicationIntendedSubstitutionReason",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A coded concept describing the reason that a different medication should (or should not) be substituted from what was prescribed.",
            valueSet = "http://terminology.hl7.org/ValueSet/v3-SubstanceAdminSubstitutionReason"
        )
        private final List<CodeableConcept> reason;
        @ReferenceTarget({ "Practitioner", "PractitionerRole" })
        private final List<Reference> responsibleParty;

        private Substitution(Builder builder) {
            super(builder);
            wasSubstituted = builder.wasSubstituted;
            type = builder.type;
            reason = Collections.unmodifiableList(builder.reason);
            responsibleParty = Collections.unmodifiableList(builder.responsibleParty);
        }

        /**
         * True if the dispenser dispensed a different drug or product from what was prescribed.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that is non-null.
         */
        public Boolean getWasSubstituted() {
            return wasSubstituted;
        }

        /**
         * A code signifying whether a different drug was dispensed from what was prescribed.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * Indicates the reason for the substitution (or lack of substitution) from what was prescribed.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getReason() {
            return reason;
        }

        /**
         * The person or organization that has primary responsibility for the substitution.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getResponsibleParty() {
            return responsibleParty;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (wasSubstituted != null) || 
                (type != null) || 
                !reason.isEmpty() || 
                !responsibleParty.isEmpty();
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
                    accept(wasSubstituted, "wasSubstituted", visitor);
                    accept(type, "type", visitor);
                    accept(reason, "reason", visitor, CodeableConcept.class);
                    accept(responsibleParty, "responsibleParty", visitor, Reference.class);
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
            Substitution other = (Substitution) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(wasSubstituted, other.wasSubstituted) && 
                Objects.equals(type, other.type) && 
                Objects.equals(reason, other.reason) && 
                Objects.equals(responsibleParty, other.responsibleParty);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    wasSubstituted, 
                    type, 
                    reason, 
                    responsibleParty);
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
            private Boolean wasSubstituted;
            private CodeableConcept type;
            private List<CodeableConcept> reason = new ArrayList<>();
            private List<Reference> responsibleParty = new ArrayList<>();

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
             * Convenience method for setting {@code wasSubstituted}.
             * 
             * <p>This element is required.
             * 
             * @param wasSubstituted
             *     Whether a substitution was or was not performed on the dispense
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #wasSubstituted(com.ibm.fhir.model.type.Boolean)
             */
            public Builder wasSubstituted(java.lang.Boolean wasSubstituted) {
                this.wasSubstituted = (wasSubstituted == null) ? null : Boolean.of(wasSubstituted);
                return this;
            }

            /**
             * True if the dispenser dispensed a different drug or product from what was prescribed.
             * 
             * <p>This element is required.
             * 
             * @param wasSubstituted
             *     Whether a substitution was or was not performed on the dispense
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder wasSubstituted(Boolean wasSubstituted) {
                this.wasSubstituted = wasSubstituted;
                return this;
            }

            /**
             * A code signifying whether a different drug was dispensed from what was prescribed.
             * 
             * @param type
             *     Code signifying whether a different drug was dispensed from what was prescribed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * Indicates the reason for the substitution (or lack of substitution) from what was prescribed.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param reason
             *     Why was substitution made
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reason(CodeableConcept... reason) {
                for (CodeableConcept value : reason) {
                    this.reason.add(value);
                }
                return this;
            }

            /**
             * Indicates the reason for the substitution (or lack of substitution) from what was prescribed.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param reason
             *     Why was substitution made
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder reason(Collection<CodeableConcept> reason) {
                this.reason = new ArrayList<>(reason);
                return this;
            }

            /**
             * The person or organization that has primary responsibility for the substitution.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * </ul>
             * 
             * @param responsibleParty
             *     Who is responsible for the substitution
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder responsibleParty(Reference... responsibleParty) {
                for (Reference value : responsibleParty) {
                    this.responsibleParty.add(value);
                }
                return this;
            }

            /**
             * The person or organization that has primary responsibility for the substitution.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * </ul>
             * 
             * @param responsibleParty
             *     Who is responsible for the substitution
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder responsibleParty(Collection<Reference> responsibleParty) {
                this.responsibleParty = new ArrayList<>(responsibleParty);
                return this;
            }

            /**
             * Build the {@link Substitution}
             * 
             * <p>Required elements:
             * <ul>
             * <li>wasSubstituted</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Substitution}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Substitution per the base specification
             */
            @Override
            public Substitution build() {
                Substitution substitution = new Substitution(this);
                if (validating) {
                    validate(substitution);
                }
                return substitution;
            }

            protected void validate(Substitution substitution) {
                super.validate(substitution);
                ValidationSupport.requireNonNull(substitution.wasSubstituted, "wasSubstituted");
                ValidationSupport.checkList(substitution.reason, "reason", CodeableConcept.class);
                ValidationSupport.checkList(substitution.responsibleParty, "responsibleParty", Reference.class);
                ValidationSupport.checkReferenceType(substitution.responsibleParty, "responsibleParty", "Practitioner", "PractitionerRole");
                ValidationSupport.requireValueOrChildren(substitution);
            }

            protected Builder from(Substitution substitution) {
                super.from(substitution);
                wasSubstituted = substitution.wasSubstituted;
                type = substitution.type;
                reason.addAll(substitution.reason);
                responsibleParty.addAll(substitution.responsibleParty);
                return this;
            }
        }
    }
}
