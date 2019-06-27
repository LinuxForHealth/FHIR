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

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Dosage;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.MedicationDispenseStatus;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Indicates that a medication product is to be or has been dispensed for a named person/patient. This includes a 
 * description of the medication product (supply) provided and the instructions for administering the medication. The 
 * medication dispense is the result of a pharmacy system responding to a medication order.
 * </p>
 */
@Constraint(
    key = "mdd-1",
    severity = "error",
    human = "whenHandedOver cannot be before whenPrepared",
    expression = "whenHandedOver.empty() or whenPrepared.empty() or whenHandedOver >= whenPrepared"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MedicationDispense extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Reference> partOf;
    private final MedicationDispenseStatus status;
    private final Element statusReason;
    private final CodeableConcept category;
    private final Element medication;
    private final Reference subject;
    private final Reference context;
    private final List<Reference> supportingInformation;
    private final List<Performer> performer;
    private final Reference location;
    private final List<Reference> authorizingPrescription;
    private final CodeableConcept type;
    private final Quantity quantity;
    private final Quantity daysSupply;
    private final DateTime whenPrepared;
    private final DateTime whenHandedOver;
    private final Reference destination;
    private final List<Reference> receiver;
    private final List<Annotation> note;
    private final List<Dosage> dosageInstruction;
    private final Substitution substitution;
    private final List<Reference> detectedIssue;
    private final List<Reference> eventHistory;

    private MedicationDispense(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.partOf = builder.partOf;
        this.status = ValidationSupport.requireNonNull(builder.status, "status");
        this.statusReason = ValidationSupport.choiceElement(builder.statusReason, "statusReason", CodeableConcept.class, Reference.class);
        this.category = builder.category;
        this.medication = ValidationSupport.requireChoiceElement(builder.medication, "medication", CodeableConcept.class, Reference.class);
        this.subject = builder.subject;
        this.context = builder.context;
        this.supportingInformation = builder.supportingInformation;
        this.performer = builder.performer;
        this.location = builder.location;
        this.authorizingPrescription = builder.authorizingPrescription;
        this.type = builder.type;
        this.quantity = builder.quantity;
        this.daysSupply = builder.daysSupply;
        this.whenPrepared = builder.whenPrepared;
        this.whenHandedOver = builder.whenHandedOver;
        this.destination = builder.destination;
        this.receiver = builder.receiver;
        this.note = builder.note;
        this.dosageInstruction = builder.dosageInstruction;
        this.substitution = builder.substitution;
        this.detectedIssue = builder.detectedIssue;
        this.eventHistory = builder.eventHistory;
    }

    /**
     * <p>
     * Identifiers associated with this Medication Dispense that are defined by business processes and/or used to refer to it 
     * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
     * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
     * to server.
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
     * The procedure that trigger the dispense.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * <p>
     * A code specifying the state of the set of dispense events.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MedicationDispenseStatus}.
     */
    public MedicationDispenseStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Indicates the reason why a dispense was not performed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getStatusReason() {
        return statusReason;
    }

    /**
     * <p>
     * Indicates the type of medication dispense (for example, where the medication is expected to be consumed or 
     * administered (i.e. inpatient or outpatient)).
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
     * A link to a resource representing the person or the group to whom the medication will be given.
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
     * The encounter or episode of care that establishes the context for this event.
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
     * Additional information that supports the medication being dispensed.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSupportingInformation() {
        return supportingInformation;
    }

    /**
     * <p>
     * Indicates who or what performed the event.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Performer}.
     */
    public List<Performer> getPerformer() {
        return performer;
    }

    /**
     * <p>
     * The principal physical location where the dispense was performed.
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
     * Indicates the medication order that is being dispensed against.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAuthorizingPrescription() {
        return authorizingPrescription;
    }

    /**
     * <p>
     * Indicates the type of dispensing event that is performed. For example, Trial Fill, Completion of Trial, Partial Fill, 
     * Emergency Fill, Samples, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * <p>
     * The amount of medication that has been dispensed. Includes unit of measure.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * <p>
     * The amount of medication expressed as a timing amount.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getDaysSupply() {
        return daysSupply;
    }

    /**
     * <p>
     * The time when the dispensed product was packaged and reviewed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getWhenPrepared() {
        return whenPrepared;
    }

    /**
     * <p>
     * The time the dispensed product was provided to the patient or their representative.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getWhenHandedOver() {
        return whenHandedOver;
    }

    /**
     * <p>
     * Identification of the facility/location where the medication was shipped to, as part of the dispense event.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getDestination() {
        return destination;
    }

    /**
     * <p>
     * Identifies the person who picked up the medication. This will usually be a patient or their caregiver, but some cases 
     * exist where it can be a healthcare professional.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReceiver() {
        return receiver;
    }

    /**
     * <p>
     * Extra information about the dispense that could not be conveyed in the other attributes.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * <p>
     * Indicates how the medication is to be used by the patient.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Dosage}.
     */
    public List<Dosage> getDosageInstruction() {
        return dosageInstruction;
    }

    /**
     * <p>
     * Indicates whether or not substitution was made as part of the dispense. In some cases, substitution will be expected 
     * but does not happen, in other cases substitution is not expected but does happen. This block explains what 
     * substitution did or did not happen and why. If nothing is specified, substitution was not done.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Substitution}.
     */
    public Substitution getSubstitution() {
        return substitution;
    }

    /**
     * <p>
     * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
     * patient; e.g. drug-drug interaction, duplicate therapy, dosage alert etc.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getDetectedIssue() {
        return detectedIssue;
    }

    /**
     * <p>
     * A summary of the events of interest that have occurred, such as when the dispense was verified.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getEventHistory() {
        return eventHistory;
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
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(statusReason, "statusReason", visitor, true);
                accept(category, "category", visitor);
                accept(medication, "medication", visitor, true);
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
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, medication);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.partOf.addAll(partOf);
        builder.statusReason = statusReason;
        builder.category = category;
        builder.subject = subject;
        builder.context = context;
        builder.supportingInformation.addAll(supportingInformation);
        builder.performer.addAll(performer);
        builder.location = location;
        builder.authorizingPrescription.addAll(authorizingPrescription);
        builder.type = type;
        builder.quantity = quantity;
        builder.daysSupply = daysSupply;
        builder.whenPrepared = whenPrepared;
        builder.whenHandedOver = whenHandedOver;
        builder.destination = destination;
        builder.receiver.addAll(receiver);
        builder.note.addAll(note);
        builder.dosageInstruction.addAll(dosageInstruction);
        builder.substitution = substitution;
        builder.detectedIssue.addAll(detectedIssue);
        builder.eventHistory.addAll(eventHistory);
        return builder;
    }

    public static Builder builder(MedicationDispenseStatus status, Element medication) {
        return new Builder(status, medication);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final MedicationDispenseStatus status;
        private final Element medication;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private Element statusReason;
        private CodeableConcept category;
        private Reference subject;
        private Reference context;
        private List<Reference> supportingInformation = new ArrayList<>();
        private List<Performer> performer = new ArrayList<>();
        private Reference location;
        private List<Reference> authorizingPrescription = new ArrayList<>();
        private CodeableConcept type;
        private Quantity quantity;
        private Quantity daysSupply;
        private DateTime whenPrepared;
        private DateTime whenHandedOver;
        private Reference destination;
        private List<Reference> receiver = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Dosage> dosageInstruction = new ArrayList<>();
        private Substitution substitution;
        private List<Reference> detectedIssue = new ArrayList<>();
        private List<Reference> eventHistory = new ArrayList<>();

        private Builder(MedicationDispenseStatus status, Element medication) {
            super();
            this.status = status;
            this.medication = medication;
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
         * Identifiers associated with this Medication Dispense that are defined by business processes and/or used to refer to it 
         * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
         * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
         * to server.
         * </p>
         * 
         * @param identifier
         *     External identifier
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
         * Identifiers associated with this Medication Dispense that are defined by business processes and/or used to refer to it 
         * when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to this 
         * resource by the performer or other systems and remain constant as the resource is updated and propagates from server 
         * to server.
         * </p>
         * 
         * @param identifier
         *     External identifier
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
         * The procedure that trigger the dispense.
         * </p>
         * 
         * @param partOf
         *     Event that dispense is part of
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The procedure that trigger the dispense.
         * </p>
         * 
         * @param partOf
         *     Event that dispense is part of
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf.addAll(partOf);
            return this;
        }

        /**
         * <p>
         * Indicates the reason why a dispense was not performed.
         * </p>
         * 
         * @param statusReason
         *     Why a dispense was not performed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder statusReason(Element statusReason) {
            this.statusReason = statusReason;
            return this;
        }

        /**
         * <p>
         * Indicates the type of medication dispense (for example, where the medication is expected to be consumed or 
         * administered (i.e. inpatient or outpatient)).
         * </p>
         * 
         * @param category
         *     Type of medication dispense
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(CodeableConcept category) {
            this.category = category;
            return this;
        }

        /**
         * <p>
         * A link to a resource representing the person or the group to whom the medication will be given.
         * </p>
         * 
         * @param subject
         *     Who the dispense is for
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * The encounter or episode of care that establishes the context for this event.
         * </p>
         * 
         * @param context
         *     Encounter / Episode associated with event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder context(Reference context) {
            this.context = context;
            return this;
        }

        /**
         * <p>
         * Additional information that supports the medication being dispensed.
         * </p>
         * 
         * @param supportingInformation
         *     Information that supports the dispensing of the medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInformation(Reference... supportingInformation) {
            for (Reference value : supportingInformation) {
                this.supportingInformation.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Additional information that supports the medication being dispensed.
         * </p>
         * 
         * @param supportingInformation
         *     Information that supports the dispensing of the medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInformation(Collection<Reference> supportingInformation) {
            this.supportingInformation.addAll(supportingInformation);
            return this;
        }

        /**
         * <p>
         * Indicates who or what performed the event.
         * </p>
         * 
         * @param performer
         *     Who performed event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performer(Performer... performer) {
            for (Performer value : performer) {
                this.performer.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates who or what performed the event.
         * </p>
         * 
         * @param performer
         *     Who performed event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performer(Collection<Performer> performer) {
            this.performer.addAll(performer);
            return this;
        }

        /**
         * <p>
         * The principal physical location where the dispense was performed.
         * </p>
         * 
         * @param location
         *     Where the dispense occurred
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder location(Reference location) {
            this.location = location;
            return this;
        }

        /**
         * <p>
         * Indicates the medication order that is being dispensed against.
         * </p>
         * 
         * @param authorizingPrescription
         *     Medication order that authorizes the dispense
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder authorizingPrescription(Reference... authorizingPrescription) {
            for (Reference value : authorizingPrescription) {
                this.authorizingPrescription.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates the medication order that is being dispensed against.
         * </p>
         * 
         * @param authorizingPrescription
         *     Medication order that authorizes the dispense
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder authorizingPrescription(Collection<Reference> authorizingPrescription) {
            this.authorizingPrescription.addAll(authorizingPrescription);
            return this;
        }

        /**
         * <p>
         * Indicates the type of dispensing event that is performed. For example, Trial Fill, Completion of Trial, Partial Fill, 
         * Emergency Fill, Samples, etc.
         * </p>
         * 
         * @param type
         *     Trial fill, partial fill, emergency fill, etc.
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * <p>
         * The amount of medication that has been dispensed. Includes unit of measure.
         * </p>
         * 
         * @param quantity
         *     Amount dispensed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder quantity(Quantity quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * <p>
         * The amount of medication expressed as a timing amount.
         * </p>
         * 
         * @param daysSupply
         *     Amount of medication expressed as a timing amount
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder daysSupply(Quantity daysSupply) {
            this.daysSupply = daysSupply;
            return this;
        }

        /**
         * <p>
         * The time when the dispensed product was packaged and reviewed.
         * </p>
         * 
         * @param whenPrepared
         *     When product was packaged and reviewed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder whenPrepared(DateTime whenPrepared) {
            this.whenPrepared = whenPrepared;
            return this;
        }

        /**
         * <p>
         * The time the dispensed product was provided to the patient or their representative.
         * </p>
         * 
         * @param whenHandedOver
         *     When product was given out
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder whenHandedOver(DateTime whenHandedOver) {
            this.whenHandedOver = whenHandedOver;
            return this;
        }

        /**
         * <p>
         * Identification of the facility/location where the medication was shipped to, as part of the dispense event.
         * </p>
         * 
         * @param destination
         *     Where the medication was sent
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder destination(Reference destination) {
            this.destination = destination;
            return this;
        }

        /**
         * <p>
         * Identifies the person who picked up the medication. This will usually be a patient or their caregiver, but some cases 
         * exist where it can be a healthcare professional.
         * </p>
         * 
         * @param receiver
         *     Who collected the medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder receiver(Reference... receiver) {
            for (Reference value : receiver) {
                this.receiver.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies the person who picked up the medication. This will usually be a patient or their caregiver, but some cases 
         * exist where it can be a healthcare professional.
         * </p>
         * 
         * @param receiver
         *     Who collected the medication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder receiver(Collection<Reference> receiver) {
            this.receiver.addAll(receiver);
            return this;
        }

        /**
         * <p>
         * Extra information about the dispense that could not be conveyed in the other attributes.
         * </p>
         * 
         * @param note
         *     Information about the dispense
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
         * Extra information about the dispense that could not be conveyed in the other attributes.
         * </p>
         * 
         * @param note
         *     Information about the dispense
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        /**
         * <p>
         * Indicates how the medication is to be used by the patient.
         * </p>
         * 
         * @param dosageInstruction
         *     How the medication is to be used by the patient or administered by the caregiver
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dosageInstruction(Dosage... dosageInstruction) {
            for (Dosage value : dosageInstruction) {
                this.dosageInstruction.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates how the medication is to be used by the patient.
         * </p>
         * 
         * @param dosageInstruction
         *     How the medication is to be used by the patient or administered by the caregiver
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dosageInstruction(Collection<Dosage> dosageInstruction) {
            this.dosageInstruction.addAll(dosageInstruction);
            return this;
        }

        /**
         * <p>
         * Indicates whether or not substitution was made as part of the dispense. In some cases, substitution will be expected 
         * but does not happen, in other cases substitution is not expected but does happen. This block explains what 
         * substitution did or did not happen and why. If nothing is specified, substitution was not done.
         * </p>
         * 
         * @param substitution
         *     Whether a substitution was performed on the dispense
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder substitution(Substitution substitution) {
            this.substitution = substitution;
            return this;
        }

        /**
         * <p>
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
         * patient; e.g. drug-drug interaction, duplicate therapy, dosage alert etc.
         * </p>
         * 
         * @param detectedIssue
         *     Clinical issue with action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder detectedIssue(Reference... detectedIssue) {
            for (Reference value : detectedIssue) {
                this.detectedIssue.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a 
         * patient; e.g. drug-drug interaction, duplicate therapy, dosage alert etc.
         * </p>
         * 
         * @param detectedIssue
         *     Clinical issue with action
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder detectedIssue(Collection<Reference> detectedIssue) {
            this.detectedIssue.addAll(detectedIssue);
            return this;
        }

        /**
         * <p>
         * A summary of the events of interest that have occurred, such as when the dispense was verified.
         * </p>
         * 
         * @param eventHistory
         *     A list of relevant lifecycle events
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder eventHistory(Reference... eventHistory) {
            for (Reference value : eventHistory) {
                this.eventHistory.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A summary of the events of interest that have occurred, such as when the dispense was verified.
         * </p>
         * 
         * @param eventHistory
         *     A list of relevant lifecycle events
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder eventHistory(Collection<Reference> eventHistory) {
            this.eventHistory.addAll(eventHistory);
            return this;
        }

        @Override
        public MedicationDispense build() {
            return new MedicationDispense(this);
        }
    }

    /**
     * <p>
     * Indicates who or what performed the event.
     * </p>
     */
    public static class Performer extends BackboneElement {
        private final CodeableConcept function;
        private final Reference actor;

        private Performer(Builder builder) {
            super(builder);
            this.function = builder.function;
            this.actor = ValidationSupport.requireNonNull(builder.actor, "actor");
        }

        /**
         * <p>
         * Distinguishes the type of performer in the dispense. For example, date enterer, packager, final checker.
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
         * The device, practitioner, etc. who performed the action. It should be assumed that the actor is the dispenser of the 
         * medication.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getActor() {
            return actor;
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
                    accept(function, "function", visitor);
                    accept(actor, "actor", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Reference actor) {
            return new Builder(actor);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference actor;

            // optional
            private CodeableConcept function;

            private Builder(Reference actor) {
                super();
                this.actor = actor;
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
             * Distinguishes the type of performer in the dispense. For example, date enterer, packager, final checker.
             * </p>
             * 
             * @param function
             *     Who performed the dispense and what they did
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder function(CodeableConcept function) {
                this.function = function;
                return this;
            }

            @Override
            public Performer build() {
                return new Performer(this);
            }

            private static Builder from(Performer performer) {
                Builder builder = new Builder(performer.actor);
                builder.id = performer.id;
                builder.extension.addAll(performer.extension);
                builder.modifierExtension.addAll(performer.modifierExtension);
                builder.function = performer.function;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Indicates whether or not substitution was made as part of the dispense. In some cases, substitution will be expected 
     * but does not happen, in other cases substitution is not expected but does happen. This block explains what 
     * substitution did or did not happen and why. If nothing is specified, substitution was not done.
     * </p>
     */
    public static class Substitution extends BackboneElement {
        private final Boolean wasSubstituted;
        private final CodeableConcept type;
        private final List<CodeableConcept> reason;
        private final List<Reference> responsibleParty;

        private Substitution(Builder builder) {
            super(builder);
            this.wasSubstituted = ValidationSupport.requireNonNull(builder.wasSubstituted, "wasSubstituted");
            this.type = builder.type;
            this.reason = builder.reason;
            this.responsibleParty = builder.responsibleParty;
        }

        /**
         * <p>
         * True if the dispenser dispensed a different drug or product from what was prescribed.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getWasSubstituted() {
            return wasSubstituted;
        }

        /**
         * <p>
         * A code signifying whether a different drug was dispensed from what was prescribed.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * <p>
         * Indicates the reason for the substitution (or lack of substitution) from what was prescribed.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getReason() {
            return reason;
        }

        /**
         * <p>
         * The person or organization that has primary responsibility for the substitution.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Reference}.
         */
        public List<Reference> getResponsibleParty() {
            return responsibleParty;
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
                    accept(wasSubstituted, "wasSubstituted", visitor);
                    accept(type, "type", visitor);
                    accept(reason, "reason", visitor, CodeableConcept.class);
                    accept(responsibleParty, "responsibleParty", visitor, Reference.class);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Boolean wasSubstituted) {
            return new Builder(wasSubstituted);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Boolean wasSubstituted;

            // optional
            private CodeableConcept type;
            private List<CodeableConcept> reason = new ArrayList<>();
            private List<Reference> responsibleParty = new ArrayList<>();

            private Builder(Boolean wasSubstituted) {
                super();
                this.wasSubstituted = wasSubstituted;
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
             * A code signifying whether a different drug was dispensed from what was prescribed.
             * </p>
             * 
             * @param type
             *     Code signifying whether a different drug was dispensed from what was prescribed
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * <p>
             * Indicates the reason for the substitution (or lack of substitution) from what was prescribed.
             * </p>
             * 
             * @param reason
             *     Why was substitution made
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder reason(CodeableConcept... reason) {
                for (CodeableConcept value : reason) {
                    this.reason.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Indicates the reason for the substitution (or lack of substitution) from what was prescribed.
             * </p>
             * 
             * @param reason
             *     Why was substitution made
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder reason(Collection<CodeableConcept> reason) {
                this.reason.addAll(reason);
                return this;
            }

            /**
             * <p>
             * The person or organization that has primary responsibility for the substitution.
             * </p>
             * 
             * @param responsibleParty
             *     Who is responsible for the substitution
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder responsibleParty(Reference... responsibleParty) {
                for (Reference value : responsibleParty) {
                    this.responsibleParty.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The person or organization that has primary responsibility for the substitution.
             * </p>
             * 
             * @param responsibleParty
             *     Who is responsible for the substitution
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder responsibleParty(Collection<Reference> responsibleParty) {
                this.responsibleParty.addAll(responsibleParty);
                return this;
            }

            @Override
            public Substitution build() {
                return new Substitution(this);
            }

            private static Builder from(Substitution substitution) {
                Builder builder = new Builder(substitution.wasSubstituted);
                builder.id = substitution.id;
                builder.extension.addAll(substitution.extension);
                builder.modifierExtension.addAll(substitution.modifierExtension);
                builder.type = substitution.type;
                builder.reason.addAll(substitution.reason);
                builder.responsibleParty.addAll(substitution.responsibleParty);
                return builder;
            }
        }
    }
}
