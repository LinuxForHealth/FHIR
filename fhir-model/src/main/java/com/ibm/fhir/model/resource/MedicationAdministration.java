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
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.MedicationAdministrationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Describes the event of a patient consuming or otherwise being administered a medication. This may be as simple as 
 * swallowing a tablet or it may be a long running infusion. Related resources tie this event to the authorizing 
 * prescription, and the specific encounter between patient and health care practitioner.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "mad-1",
    level = "Rule",
    location = "MedicationAdministration.dosage",
    description = "SHALL have at least one of dosage.dose or dosage.rate[x]",
    expression = "dose.exists() or rate.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/MedicationAdministration"
)
@Constraint(
    id = "medicationAdministration-2",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/medication-admin-category",
    expression = "category.exists() implies (category.memberOf('http://hl7.org/fhir/ValueSet/medication-admin-category', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/MedicationAdministration",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicationAdministration extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    private final List<Uri> instantiates;
    @Summary
    @ReferenceTarget({ "MedicationAdministration", "Procedure" })
    private final List<Reference> partOf;
    @Summary
    @Binding(
        bindingName = "MedicationAdministrationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "A set of codes indicating the current status of a MedicationAdministration.",
        valueSet = "http://hl7.org/fhir/ValueSet/medication-admin-status|4.3.0-cibuild"
    )
    @Required
    private final MedicationAdministrationStatus status;
    @Binding(
        bindingName = "MedicationAdministrationNegationReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A set of codes indicating the reason why the MedicationAdministration is negated.",
        valueSet = "http://hl7.org/fhir/ValueSet/reason-medication-not-given-codes"
    )
    private final List<CodeableConcept> statusReason;
    @Binding(
        bindingName = "MedicationAdministrationCategory",
        strength = BindingStrength.Value.PREFERRED,
        description = "A coded concept describing where the medication administered is expected to occur.",
        valueSet = "http://hl7.org/fhir/ValueSet/medication-admin-category"
    )
    private final CodeableConcept category;
    @Summary
    @ReferenceTarget({ "Medication" })
    @Choice({ CodeableConcept.class, Reference.class })
    @Binding(
        bindingName = "MedicationCode",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes identifying substance or product that can be administered.",
        valueSet = "http://hl7.org/fhir/ValueSet/medication-codes"
    )
    @Required
    private final Element medication;
    @Summary
    @ReferenceTarget({ "Patient", "Group" })
    @Required
    private final Reference subject;
    @ReferenceTarget({ "Encounter", "EpisodeOfCare" })
    private final Reference context;
    private final List<Reference> supportingInformation;
    @Summary
    @Choice({ DateTime.class, Period.class })
    @Required
    private final Element effective;
    @Summary
    private final List<Performer> performer;
    @Binding(
        bindingName = "MedicationAdministrationReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "A set of codes indicating the reason why the MedicationAdministration was made.",
        valueSet = "http://hl7.org/fhir/ValueSet/reason-medication-given-codes"
    )
    private final List<CodeableConcept> reasonCode;
    @ReferenceTarget({ "Condition", "Observation", "DiagnosticReport" })
    private final List<Reference> reasonReference;
    @ReferenceTarget({ "MedicationRequest" })
    private final Reference request;
    @ReferenceTarget({ "Device" })
    private final List<Reference> device;
    private final List<Annotation> note;
    private final Dosage dosage;
    @ReferenceTarget({ "Provenance" })
    private final List<Reference> eventHistory;

    private MedicationAdministration(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiates = Collections.unmodifiableList(builder.instantiates);
        partOf = Collections.unmodifiableList(builder.partOf);
        status = builder.status;
        statusReason = Collections.unmodifiableList(builder.statusReason);
        category = builder.category;
        medication = builder.medication;
        subject = builder.subject;
        context = builder.context;
        supportingInformation = Collections.unmodifiableList(builder.supportingInformation);
        effective = builder.effective;
        performer = Collections.unmodifiableList(builder.performer);
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        request = builder.request;
        device = Collections.unmodifiableList(builder.device);
        note = Collections.unmodifiableList(builder.note);
        dosage = builder.dosage;
        eventHistory = Collections.unmodifiableList(builder.eventHistory);
    }

    /**
     * Identifiers associated with this Medication Administration that are defined by business processes and/or used to refer 
     * to it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
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
     * A protocol, guideline, orderset, or other definition that was adhered to in whole or in part by this event.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri} that may be empty.
     */
    public List<Uri> getInstantiates() {
        return instantiates;
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
     * Will generally be set to show that the administration has been completed. For some long running administrations such 
     * as infusions, it is possible for an administration to be started but not completed or it may be paused while some 
     * other process is under way.
     * 
     * @return
     *     An immutable object of type {@link MedicationAdministrationStatus} that is non-null.
     */
    public MedicationAdministrationStatus getStatus() {
        return status;
    }

    /**
     * A code indicating why the administration was not performed.
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
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the 
     * medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} or {@link Reference} that is non-null.
     */
    public Element getMedication() {
        return medication;
    }

    /**
     * The person or animal or group receiving the medication.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The visit, admission, or other contact between patient and health care provider during which the medication 
     * administration was performed.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getContext() {
        return context;
    }

    /**
     * Additional information (for example, patient height and weight) that supports the administration of the medication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSupportingInformation() {
        return supportingInformation;
    }

    /**
     * A specific date/time or interval of time during which the administration took place (or did not take place, when the 
     * 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more 
     * appropriate.
     * 
     * @return
     *     An immutable object of type {@link DateTime} or {@link Period} that is non-null.
     */
    public Element getEffective() {
        return effective;
    }

    /**
     * Indicates who or what performed the medication administration and how they were involved.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Performer} that may be empty.
     */
    public List<Performer> getPerformer() {
        return performer;
    }

    /**
     * A code indicating why the medication was given.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Condition or observation that supports why the medication was administered.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * The original request, instruction or authority to perform the administration.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getRequest() {
        return request;
    }

    /**
     * The device used in administering the medication to the patient. For example, a particular infusion pump.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getDevice() {
        return device;
    }

    /**
     * Extra information about the medication administration that is not conveyed by the other attributes.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * Describes the medication dosage information details e.g. dose, rate, site, route, etc.
     * 
     * @return
     *     An immutable object of type {@link Dosage} that may be null.
     */
    public Dosage getDosage() {
        return dosage;
    }

    /**
     * A summary of the events of interest that have occurred, such as when the administration was verified.
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
            !instantiates.isEmpty() || 
            !partOf.isEmpty() || 
            (status != null) || 
            !statusReason.isEmpty() || 
            (category != null) || 
            (medication != null) || 
            (subject != null) || 
            (context != null) || 
            !supportingInformation.isEmpty() || 
            (effective != null) || 
            !performer.isEmpty() || 
            !reasonCode.isEmpty() || 
            !reasonReference.isEmpty() || 
            (request != null) || 
            !device.isEmpty() || 
            !note.isEmpty() || 
            (dosage != null) || 
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
                accept(instantiates, "instantiates", visitor, Uri.class);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(statusReason, "statusReason", visitor, CodeableConcept.class);
                accept(category, "category", visitor);
                accept(medication, "medication", visitor);
                accept(subject, "subject", visitor);
                accept(context, "context", visitor);
                accept(supportingInformation, "supportingInformation", visitor, Reference.class);
                accept(effective, "effective", visitor);
                accept(performer, "performer", visitor, Performer.class);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(request, "request", visitor);
                accept(device, "device", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(dosage, "dosage", visitor);
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
        MedicationAdministration other = (MedicationAdministration) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(instantiates, other.instantiates) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusReason, other.statusReason) && 
            Objects.equals(category, other.category) && 
            Objects.equals(medication, other.medication) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(context, other.context) && 
            Objects.equals(supportingInformation, other.supportingInformation) && 
            Objects.equals(effective, other.effective) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(request, other.request) && 
            Objects.equals(device, other.device) && 
            Objects.equals(note, other.note) && 
            Objects.equals(dosage, other.dosage) && 
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
                instantiates, 
                partOf, 
                status, 
                statusReason, 
                category, 
                medication, 
                subject, 
                context, 
                supportingInformation, 
                effective, 
                performer, 
                reasonCode, 
                reasonReference, 
                request, 
                device, 
                note, 
                dosage, 
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
        private List<Uri> instantiates = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private MedicationAdministrationStatus status;
        private List<CodeableConcept> statusReason = new ArrayList<>();
        private CodeableConcept category;
        private Element medication;
        private Reference subject;
        private Reference context;
        private List<Reference> supportingInformation = new ArrayList<>();
        private Element effective;
        private List<Performer> performer = new ArrayList<>();
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private Reference request;
        private List<Reference> device = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private Dosage dosage;
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
         * Identifiers associated with this Medication Administration that are defined by business processes and/or used to refer 
         * to it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
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
         * Identifiers associated with this Medication Administration that are defined by business processes and/or used to refer 
         * to it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
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
         * A protocol, guideline, orderset, or other definition that was adhered to in whole or in part by this event.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiates
         *     Instantiates protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiates(Uri... instantiates) {
            for (Uri value : instantiates) {
                this.instantiates.add(value);
            }
            return this;
        }

        /**
         * A protocol, guideline, orderset, or other definition that was adhered to in whole or in part by this event.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiates
         *     Instantiates protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instantiates(Collection<Uri> instantiates) {
            this.instantiates = new ArrayList<>(instantiates);
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
         * <li>{@link Procedure}</li>
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
         * <li>{@link Procedure}</li>
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
         * Will generally be set to show that the administration has been completed. For some long running administrations such 
         * as infusions, it is possible for an administration to be started but not completed or it may be paused while some 
         * other process is under way.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     in-progress | not-done | on-hold | completed | entered-in-error | stopped | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(MedicationAdministrationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * A code indicating why the administration was not performed.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param statusReason
         *     Reason administration not performed
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
         * A code indicating why the administration was not performed.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param statusReason
         *     Reason administration not performed
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
         * Identifies the medication that was administered. This is either a link to a resource representing the details of the 
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
         *     What was administered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder medication(Element medication) {
            this.medication = medication;
            return this;
        }

        /**
         * The person or animal or group receiving the medication.
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
         *     Who received medication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The visit, admission, or other contact between patient and health care provider during which the medication 
         * administration was performed.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * <li>{@link EpisodeOfCare}</li>
         * </ul>
         * 
         * @param context
         *     Encounter or Episode of Care administered as part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder context(Reference context) {
            this.context = context;
            return this;
        }

        /**
         * Additional information (for example, patient height and weight) that supports the administration of the medication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInformation
         *     Additional information to support administration
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
         * Additional information (for example, patient height and weight) that supports the administration of the medication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param supportingInformation
         *     Additional information to support administration
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
         * A specific date/time or interval of time during which the administration took place (or did not take place, when the 
         * 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more 
         * appropriate.
         * 
         * <p>This element is required.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link DateTime}</li>
         * <li>{@link Period}</li>
         * </ul>
         * 
         * @param effective
         *     Start and end time of administration
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder effective(Element effective) {
            this.effective = effective;
            return this;
        }

        /**
         * Indicates who or what performed the medication administration and how they were involved.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param performer
         *     Who performed the medication administration and what they did
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
         * Indicates who or what performed the medication administration and how they were involved.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param performer
         *     Who performed the medication administration and what they did
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
         * A code indicating why the medication was given.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Reason administration performed
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
         * A code indicating why the medication was given.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Reason administration performed
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
         * Condition or observation that supports why the medication was administered.
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
         *     Condition or observation that supports why the medication was administered
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
         * Condition or observation that supports why the medication was administered.
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
         *     Condition or observation that supports why the medication was administered
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
         * The original request, instruction or authority to perform the administration.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link MedicationRequest}</li>
         * </ul>
         * 
         * @param request
         *     Request administration performed against
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder request(Reference request) {
            this.request = request;
            return this;
        }

        /**
         * The device used in administering the medication to the patient. For example, a particular infusion pump.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param device
         *     Device used to administer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder device(Reference... device) {
            for (Reference value : device) {
                this.device.add(value);
            }
            return this;
        }

        /**
         * The device used in administering the medication to the patient. For example, a particular infusion pump.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param device
         *     Device used to administer
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder device(Collection<Reference> device) {
            this.device = new ArrayList<>(device);
            return this;
        }

        /**
         * Extra information about the medication administration that is not conveyed by the other attributes.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Information about the administration
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
         * Extra information about the medication administration that is not conveyed by the other attributes.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Information about the administration
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
         * Describes the medication dosage information details e.g. dose, rate, site, route, etc.
         * 
         * @param dosage
         *     Details of how medication was taken
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder dosage(Dosage dosage) {
            this.dosage = dosage;
            return this;
        }

        /**
         * A summary of the events of interest that have occurred, such as when the administration was verified.
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
         *     A list of events of interest in the lifecycle
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
         * A summary of the events of interest that have occurred, such as when the administration was verified.
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
         *     A list of events of interest in the lifecycle
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
         * Build the {@link MedicationAdministration}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>medication</li>
         * <li>subject</li>
         * <li>effective</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MedicationAdministration}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicationAdministration per the base specification
         */
        @Override
        public MedicationAdministration build() {
            MedicationAdministration medicationAdministration = new MedicationAdministration(this);
            if (validating) {
                validate(medicationAdministration);
            }
            return medicationAdministration;
        }

        protected void validate(MedicationAdministration medicationAdministration) {
            super.validate(medicationAdministration);
            ValidationSupport.checkList(medicationAdministration.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(medicationAdministration.instantiates, "instantiates", Uri.class);
            ValidationSupport.checkList(medicationAdministration.partOf, "partOf", Reference.class);
            ValidationSupport.requireNonNull(medicationAdministration.status, "status");
            ValidationSupport.checkList(medicationAdministration.statusReason, "statusReason", CodeableConcept.class);
            ValidationSupport.requireChoiceElement(medicationAdministration.medication, "medication", CodeableConcept.class, Reference.class);
            ValidationSupport.requireNonNull(medicationAdministration.subject, "subject");
            ValidationSupport.checkList(medicationAdministration.supportingInformation, "supportingInformation", Reference.class);
            ValidationSupport.requireChoiceElement(medicationAdministration.effective, "effective", DateTime.class, Period.class);
            ValidationSupport.checkList(medicationAdministration.performer, "performer", Performer.class);
            ValidationSupport.checkList(medicationAdministration.reasonCode, "reasonCode", CodeableConcept.class);
            ValidationSupport.checkList(medicationAdministration.reasonReference, "reasonReference", Reference.class);
            ValidationSupport.checkList(medicationAdministration.device, "device", Reference.class);
            ValidationSupport.checkList(medicationAdministration.note, "note", Annotation.class);
            ValidationSupport.checkList(medicationAdministration.eventHistory, "eventHistory", Reference.class);
            ValidationSupport.checkReferenceType(medicationAdministration.partOf, "partOf", "MedicationAdministration", "Procedure");
            ValidationSupport.checkReferenceType(medicationAdministration.medication, "medication", "Medication");
            ValidationSupport.checkReferenceType(medicationAdministration.subject, "subject", "Patient", "Group");
            ValidationSupport.checkReferenceType(medicationAdministration.context, "context", "Encounter", "EpisodeOfCare");
            ValidationSupport.checkReferenceType(medicationAdministration.reasonReference, "reasonReference", "Condition", "Observation", "DiagnosticReport");
            ValidationSupport.checkReferenceType(medicationAdministration.request, "request", "MedicationRequest");
            ValidationSupport.checkReferenceType(medicationAdministration.device, "device", "Device");
            ValidationSupport.checkReferenceType(medicationAdministration.eventHistory, "eventHistory", "Provenance");
        }

        protected Builder from(MedicationAdministration medicationAdministration) {
            super.from(medicationAdministration);
            identifier.addAll(medicationAdministration.identifier);
            instantiates.addAll(medicationAdministration.instantiates);
            partOf.addAll(medicationAdministration.partOf);
            status = medicationAdministration.status;
            statusReason.addAll(medicationAdministration.statusReason);
            category = medicationAdministration.category;
            medication = medicationAdministration.medication;
            subject = medicationAdministration.subject;
            context = medicationAdministration.context;
            supportingInformation.addAll(medicationAdministration.supportingInformation);
            effective = medicationAdministration.effective;
            performer.addAll(medicationAdministration.performer);
            reasonCode.addAll(medicationAdministration.reasonCode);
            reasonReference.addAll(medicationAdministration.reasonReference);
            request = medicationAdministration.request;
            device.addAll(medicationAdministration.device);
            note.addAll(medicationAdministration.note);
            dosage = medicationAdministration.dosage;
            eventHistory.addAll(medicationAdministration.eventHistory);
            return this;
        }
    }

    /**
     * Indicates who or what performed the medication administration and how they were involved.
     */
    public static class Performer extends BackboneElement {
        @Binding(
            bindingName = "MedicationAdministrationPerformerFunction",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A code describing the role an individual played in administering the medication.",
            valueSet = "http://hl7.org/fhir/ValueSet/med-admin-perform-function"
        )
        private final CodeableConcept function;
        @Summary
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Patient", "RelatedPerson", "Device" })
        @Required
        private final Reference actor;

        private Performer(Builder builder) {
            super(builder);
            function = builder.function;
            actor = builder.actor;
        }

        /**
         * Distinguishes the type of involvement of the performer in the medication administration.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getFunction() {
            return function;
        }

        /**
         * Indicates who or what performed the medication administration.
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
             * Distinguishes the type of involvement of the performer in the medication administration.
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
             * Indicates who or what performed the medication administration.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Patient}</li>
             * <li>{@link RelatedPerson}</li>
             * <li>{@link Device}</li>
             * </ul>
             * 
             * @param actor
             *     Who performed the medication administration
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
                ValidationSupport.checkReferenceType(performer.actor, "actor", "Practitioner", "PractitionerRole", "Patient", "RelatedPerson", "Device");
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
     * Describes the medication dosage information details e.g. dose, rate, site, route, etc.
     */
    public static class Dosage extends BackboneElement {
        private final String text;
        @Binding(
            bindingName = "MedicationAdministrationSite",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A coded concept describing the site location the medicine enters into or onto the body.",
            valueSet = "http://hl7.org/fhir/ValueSet/approach-site-codes"
        )
        private final CodeableConcept site;
        @Binding(
            bindingName = "RouteOfAdministration",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A coded concept describing the route or physiological path of administration of a therapeutic agent into or onto the body of a subject.",
            valueSet = "http://hl7.org/fhir/ValueSet/route-codes"
        )
        private final CodeableConcept route;
        @Binding(
            bindingName = "MedicationAdministrationMethod",
            strength = BindingStrength.Value.EXAMPLE,
            description = "A coded concept describing the technique by which the medicine is administered.",
            valueSet = "http://hl7.org/fhir/ValueSet/administration-method-codes"
        )
        private final CodeableConcept method;
        private final SimpleQuantity dose;
        @Choice({ Ratio.class, SimpleQuantity.class })
        private final Element rate;

        private Dosage(Builder builder) {
            super(builder);
            text = builder.text;
            site = builder.site;
            route = builder.route;
            method = builder.method;
            dose = builder.dose;
            rate = builder.rate;
        }

        /**
         * Free text dosage can be used for cases where the dosage administered is too complex to code. When coded dosage is 
         * present, the free text dosage may still be present for display to humans.The dosage instructions should reflect the 
         * dosage of the medication that was administered.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getText() {
            return text;
        }

        /**
         * A coded specification of the anatomic site where the medication first entered the body. For example, "left arm".
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getSite() {
            return site;
        }

        /**
         * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient. 
         * For example, topical, intravenous, etc.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getRoute() {
            return route;
        }

        /**
         * A coded value indicating the method by which the medication is intended to be or was introduced into or on the body. 
         * This attribute will most often NOT be populated. It is most commonly used for injections. For example, Slow Push, Deep 
         * IV.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getMethod() {
            return method;
        }

        /**
         * The amount of the medication given at one administration event. Use this value when the administration is essentially 
         * an instantaneous event such as a swallowing a tablet or giving an injection.
         * 
         * @return
         *     An immutable object of type {@link SimpleQuantity} that may be null.
         */
        public SimpleQuantity getDose() {
            return dose;
        }

        /**
         * Identifies the speed with which the medication was or will be introduced into the patient. Typically, the rate for an 
         * infusion e.g. 100 ml per 1 hour or 100 ml/hr. May also be expressed as a rate per unit of time, e.g. 500 ml per 2 
         * hours. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.
         * 
         * @return
         *     An immutable object of type {@link Ratio} or {@link SimpleQuantity} that may be null.
         */
        public Element getRate() {
            return rate;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (text != null) || 
                (site != null) || 
                (route != null) || 
                (method != null) || 
                (dose != null) || 
                (rate != null);
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
                    accept(text, "text", visitor);
                    accept(site, "site", visitor);
                    accept(route, "route", visitor);
                    accept(method, "method", visitor);
                    accept(dose, "dose", visitor);
                    accept(rate, "rate", visitor);
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
            Dosage other = (Dosage) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(text, other.text) && 
                Objects.equals(site, other.site) && 
                Objects.equals(route, other.route) && 
                Objects.equals(method, other.method) && 
                Objects.equals(dose, other.dose) && 
                Objects.equals(rate, other.rate);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    text, 
                    site, 
                    route, 
                    method, 
                    dose, 
                    rate);
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
            private String text;
            private CodeableConcept site;
            private CodeableConcept route;
            private CodeableConcept method;
            private SimpleQuantity dose;
            private Element rate;

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
             * Convenience method for setting {@code text}.
             * 
             * @param text
             *     Free text dosage instructions e.g. SIG
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #text(com.ibm.fhir.model.type.String)
             */
            public Builder text(java.lang.String text) {
                this.text = (text == null) ? null : String.of(text);
                return this;
            }

            /**
             * Free text dosage can be used for cases where the dosage administered is too complex to code. When coded dosage is 
             * present, the free text dosage may still be present for display to humans.The dosage instructions should reflect the 
             * dosage of the medication that was administered.
             * 
             * @param text
             *     Free text dosage instructions e.g. SIG
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            /**
             * A coded specification of the anatomic site where the medication first entered the body. For example, "left arm".
             * 
             * @param site
             *     Body site administered to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder site(CodeableConcept site) {
                this.site = site;
                return this;
            }

            /**
             * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient. 
             * For example, topical, intravenous, etc.
             * 
             * @param route
             *     Path of substance into body
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder route(CodeableConcept route) {
                this.route = route;
                return this;
            }

            /**
             * A coded value indicating the method by which the medication is intended to be or was introduced into or on the body. 
             * This attribute will most often NOT be populated. It is most commonly used for injections. For example, Slow Push, Deep 
             * IV.
             * 
             * @param method
             *     How drug was administered
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder method(CodeableConcept method) {
                this.method = method;
                return this;
            }

            /**
             * The amount of the medication given at one administration event. Use this value when the administration is essentially 
             * an instantaneous event such as a swallowing a tablet or giving an injection.
             * 
             * @param dose
             *     Amount of medication per dose
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dose(SimpleQuantity dose) {
                this.dose = dose;
                return this;
            }

            /**
             * Identifies the speed with which the medication was or will be introduced into the patient. Typically, the rate for an 
             * infusion e.g. 100 ml per 1 hour or 100 ml/hr. May also be expressed as a rate per unit of time, e.g. 500 ml per 2 
             * hours. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Ratio}</li>
             * <li>{@link SimpleQuantity}</li>
             * </ul>
             * 
             * @param rate
             *     Dose quantity per unit of time
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder rate(Element rate) {
                this.rate = rate;
                return this;
            }

            /**
             * Build the {@link Dosage}
             * 
             * @return
             *     An immutable object of type {@link Dosage}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Dosage per the base specification
             */
            @Override
            public Dosage build() {
                Dosage dosage = new Dosage(this);
                if (validating) {
                    validate(dosage);
                }
                return dosage;
            }

            protected void validate(Dosage dosage) {
                super.validate(dosage);
                ValidationSupport.choiceElement(dosage.rate, "rate", Ratio.class, SimpleQuantity.class);
                ValidationSupport.requireValueOrChildren(dosage);
            }

            protected Builder from(Dosage dosage) {
                super.from(dosage);
                text = dosage.text;
                site = dosage.site;
                route = dosage.route;
                method = dosage.method;
                dose = dosage.dose;
                rate = dosage.rate;
                return this;
            }
        }
    }
}
