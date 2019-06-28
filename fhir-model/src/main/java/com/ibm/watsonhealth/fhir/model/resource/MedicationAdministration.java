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

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.MedicationAdministrationStatus;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Ratio;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Describes the event of a patient consuming or otherwise being administered a medication. This may be as simple as 
 * swallowing a tablet or it may be a long running infusion. Related resources tie this event to the authorizing 
 * prescription, and the specific encounter between patient and health care practitioner.
 * </p>
 */
@Constraint(
    id = "mad-1",
    level = "Rule",
    location = "MedicationAdministration.dosage",
    description = "SHALL have at least one of dosage.dose or dosage.rate[x]",
    expression = "dose.exists() or rate.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MedicationAdministration extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Uri> instantiates;
    private final List<Reference> partOf;
    private final MedicationAdministrationStatus status;
    private final List<CodeableConcept> statusReason;
    private final CodeableConcept category;
    private final Element medication;
    private final Reference subject;
    private final Reference context;
    private final List<Reference> supportingInformation;
    private final Element effective;
    private final List<Performer> performer;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final Reference request;
    private final List<Reference> device;
    private final List<Annotation> note;
    private final Dosage dosage;
    private final List<Reference> eventHistory;

    private MedicationAdministration(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiates = Collections.unmodifiableList(builder.instantiates);
        partOf = Collections.unmodifiableList(builder.partOf);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        statusReason = Collections.unmodifiableList(builder.statusReason);
        category = builder.category;
        medication = ValidationSupport.requireChoiceElement(builder.medication, "medication", CodeableConcept.class, Reference.class);
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        context = builder.context;
        supportingInformation = Collections.unmodifiableList(builder.supportingInformation);
        effective = ValidationSupport.requireChoiceElement(builder.effective, "effective", DateTime.class, Period.class);
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
     * <p>
     * Identifiers associated with this Medication Administration that are defined by business processes and/or used to refer 
     * to it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
     * this resource by the performer or other systems and remain constant as the resource is updated and propagates from 
     * server to server.
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
     * A protocol, guideline, orderset, or other definition that was adhered to in whole or in part by this event.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getInstantiates() {
        return instantiates;
    }

    /**
     * <p>
     * A larger event of which this particular event is a component or step.
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
     * Will generally be set to show that the administration has been completed. For some long running administrations such 
     * as infusions, it is possible for an administration to be started but not completed or it may be paused while some 
     * other process is under way.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link MedicationAdministrationStatus}.
     */
    public MedicationAdministrationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * A code indicating why the administration was not performed.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
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
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the 
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
     * The person or animal or group receiving the medication.
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
     * The visit, admission, or other contact between patient and health care provider during which the medication 
     * administration was performed.
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
     * Additional information (for example, patient height and weight) that supports the administration of the medication.
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
     * A specific date/time or interval of time during which the administration took place (or did not take place, when the 
     * 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more 
     * appropriate.
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
     * Indicates who or what performed the medication administration and how they were involved.
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
     * A code indicating why the medication was given.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * <p>
     * Condition or observation that supports why the medication was administered.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * <p>
     * The original request, instruction or authority to perform the administration.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRequest() {
        return request;
    }

    /**
     * <p>
     * The device used in administering the medication to the patient. For example, a particular infusion pump.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getDevice() {
        return device;
    }

    /**
     * <p>
     * Extra information about the medication administration that is not conveyed by the other attributes.
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
     * Describes the medication dosage information details e.g. dose, rate, site, route, etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Dosage}.
     */
    public Dosage getDosage() {
        return dosage;
    }

    /**
     * <p>
     * A summary of the events of interest that have occurred, such as when the administration was verified.
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
                accept(instantiates, "instantiates", visitor, Uri.class);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(statusReason, "statusReason", visitor, CodeableConcept.class);
                accept(category, "category", visitor);
                accept(medication, "medication", visitor, true);
                accept(subject, "subject", visitor);
                accept(context, "context", visitor);
                accept(supportingInformation, "supportingInformation", visitor, Reference.class);
                accept(effective, "effective", visitor, true);
                accept(performer, "performer", visitor, Performer.class);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(request, "request", visitor);
                accept(device, "device", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
                accept(dosage, "dosage", visitor);
                accept(eventHistory, "eventHistory", visitor, Reference.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status, medication, subject, effective);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.instantiates.addAll(instantiates);
        builder.partOf.addAll(partOf);
        builder.statusReason.addAll(statusReason);
        builder.category = category;
        builder.context = context;
        builder.supportingInformation.addAll(supportingInformation);
        builder.performer.addAll(performer);
        builder.reasonCode.addAll(reasonCode);
        builder.reasonReference.addAll(reasonReference);
        builder.request = request;
        builder.device.addAll(device);
        builder.note.addAll(note);
        builder.dosage = dosage;
        builder.eventHistory.addAll(eventHistory);
        return builder;
    }

    public static Builder builder(MedicationAdministrationStatus status, Element medication, Reference subject, Element effective) {
        return new Builder(status, medication, subject, effective);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final MedicationAdministrationStatus status;
        private final Element medication;
        private final Reference subject;
        private final Element effective;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Uri> instantiates = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private List<CodeableConcept> statusReason = new ArrayList<>();
        private CodeableConcept category;
        private Reference context;
        private List<Reference> supportingInformation = new ArrayList<>();
        private List<Performer> performer = new ArrayList<>();
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private Reference request;
        private List<Reference> device = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private Dosage dosage;
        private List<Reference> eventHistory = new ArrayList<>();

        private Builder(MedicationAdministrationStatus status, Element medication, Reference subject, Element effective) {
            super();
            this.status = status;
            this.medication = medication;
            this.subject = subject;
            this.effective = effective;
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
         * Identifiers associated with this Medication Administration that are defined by business processes and/or used to refer 
         * to it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
         * this resource by the performer or other systems and remain constant as the resource is updated and propagates from 
         * server to server.
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
         * Identifiers associated with this Medication Administration that are defined by business processes and/or used to refer 
         * to it when a direct URL reference to the resource itself is not appropriate. They are business identifiers assigned to 
         * this resource by the performer or other systems and remain constant as the resource is updated and propagates from 
         * server to server.
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
         * A protocol, guideline, orderset, or other definition that was adhered to in whole or in part by this event.
         * </p>
         * 
         * @param instantiates
         *     Instantiates protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiates(Uri... instantiates) {
            for (Uri value : instantiates) {
                this.instantiates.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A protocol, guideline, orderset, or other definition that was adhered to in whole or in part by this event.
         * </p>
         * 
         * @param instantiates
         *     Instantiates protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiates(Collection<Uri> instantiates) {
            this.instantiates.addAll(instantiates);
            return this;
        }

        /**
         * <p>
         * A larger event of which this particular event is a component or step.
         * </p>
         * 
         * @param partOf
         *     Part of referenced event
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
         * A larger event of which this particular event is a component or step.
         * </p>
         * 
         * @param partOf
         *     Part of referenced event
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
         * A code indicating why the administration was not performed.
         * </p>
         * 
         * @param statusReason
         *     Reason administration not performed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder statusReason(CodeableConcept... statusReason) {
            for (CodeableConcept value : statusReason) {
                this.statusReason.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A code indicating why the administration was not performed.
         * </p>
         * 
         * @param statusReason
         *     Reason administration not performed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder statusReason(Collection<CodeableConcept> statusReason) {
            this.statusReason.addAll(statusReason);
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
         *     A reference to this Builder instance.
         */
        public Builder category(CodeableConcept category) {
            this.category = category;
            return this;
        }

        /**
         * <p>
         * The visit, admission, or other contact between patient and health care provider during which the medication 
         * administration was performed.
         * </p>
         * 
         * @param context
         *     Encounter or Episode of Care administered as part of
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
         * Additional information (for example, patient height and weight) that supports the administration of the medication.
         * </p>
         * 
         * @param supportingInformation
         *     Additional information to support administration
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
         * Additional information (for example, patient height and weight) that supports the administration of the medication.
         * </p>
         * 
         * @param supportingInformation
         *     Additional information to support administration
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
         * Indicates who or what performed the medication administration and how they were involved.
         * </p>
         * 
         * @param performer
         *     Who performed the medication administration and what they did
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
         * Indicates who or what performed the medication administration and how they were involved.
         * </p>
         * 
         * @param performer
         *     Who performed the medication administration and what they did
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
         * A code indicating why the medication was given.
         * </p>
         * 
         * @param reasonCode
         *     Reason administration performed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonCode(CodeableConcept... reasonCode) {
            for (CodeableConcept value : reasonCode) {
                this.reasonCode.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A code indicating why the medication was given.
         * </p>
         * 
         * @param reasonCode
         *     Reason administration performed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode.addAll(reasonCode);
            return this;
        }

        /**
         * <p>
         * Condition or observation that supports why the medication was administered.
         * </p>
         * 
         * @param reasonReference
         *     Condition or observation that supports why the medication was administered
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonReference(Reference... reasonReference) {
            for (Reference value : reasonReference) {
                this.reasonReference.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Condition or observation that supports why the medication was administered.
         * </p>
         * 
         * @param reasonReference
         *     Condition or observation that supports why the medication was administered
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference.addAll(reasonReference);
            return this;
        }

        /**
         * <p>
         * The original request, instruction or authority to perform the administration.
         * </p>
         * 
         * @param request
         *     Request administration performed against
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder request(Reference request) {
            this.request = request;
            return this;
        }

        /**
         * <p>
         * The device used in administering the medication to the patient. For example, a particular infusion pump.
         * </p>
         * 
         * @param device
         *     Device used to administer
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder device(Reference... device) {
            for (Reference value : device) {
                this.device.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The device used in administering the medication to the patient. For example, a particular infusion pump.
         * </p>
         * 
         * @param device
         *     Device used to administer
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder device(Collection<Reference> device) {
            this.device.addAll(device);
            return this;
        }

        /**
         * <p>
         * Extra information about the medication administration that is not conveyed by the other attributes.
         * </p>
         * 
         * @param note
         *     Information about the administration
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
         * Extra information about the medication administration that is not conveyed by the other attributes.
         * </p>
         * 
         * @param note
         *     Information about the administration
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
         * Describes the medication dosage information details e.g. dose, rate, site, route, etc.
         * </p>
         * 
         * @param dosage
         *     Details of how medication was taken
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder dosage(Dosage dosage) {
            this.dosage = dosage;
            return this;
        }

        /**
         * <p>
         * A summary of the events of interest that have occurred, such as when the administration was verified.
         * </p>
         * 
         * @param eventHistory
         *     A list of events of interest in the lifecycle
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
         * A summary of the events of interest that have occurred, such as when the administration was verified.
         * </p>
         * 
         * @param eventHistory
         *     A list of events of interest in the lifecycle
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder eventHistory(Collection<Reference> eventHistory) {
            this.eventHistory.addAll(eventHistory);
            return this;
        }

        @Override
        public MedicationAdministration build() {
            return new MedicationAdministration(this);
        }
    }

    /**
     * <p>
     * Indicates who or what performed the medication administration and how they were involved.
     * </p>
     */
    public static class Performer extends BackboneElement {
        private final CodeableConcept function;
        private final Reference actor;

        private Performer(Builder builder) {
            super(builder);
            function = builder.function;
            actor = ValidationSupport.requireNonNull(builder.actor, "actor");
        }

        /**
         * <p>
         * Distinguishes the type of involvement of the performer in the medication administration.
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
         * Indicates who or what performed the medication administration.
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
             * Distinguishes the type of involvement of the performer in the medication administration.
             * </p>
             * 
             * @param function
             *     Type of performance
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
                builder.function = performer.function;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Describes the medication dosage information details e.g. dose, rate, site, route, etc.
     * </p>
     */
    public static class Dosage extends BackboneElement {
        private final String text;
        private final CodeableConcept site;
        private final CodeableConcept route;
        private final CodeableConcept method;
        private final Quantity dose;
        private final Element rate;

        private Dosage(Builder builder) {
            super(builder);
            text = builder.text;
            site = builder.site;
            route = builder.route;
            method = builder.method;
            dose = builder.dose;
            rate = ValidationSupport.choiceElement(builder.rate, "rate", Ratio.class, Quantity.class);
        }

        /**
         * <p>
         * Free text dosage can be used for cases where the dosage administered is too complex to code. When coded dosage is 
         * present, the free text dosage may still be present for display to humans.

The dosage instructions should reflect the 
         * dosage of the medication that was administered.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getText() {
            return text;
        }

        /**
         * <p>
         * A coded specification of the anatomic site where the medication first entered the body. For example, "left arm".
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getSite() {
            return site;
        }

        /**
         * <p>
         * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient. 
         * For example, topical, intravenous, etc.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getRoute() {
            return route;
        }

        /**
         * <p>
         * A coded value indicating the method by which the medication is intended to be or was introduced into or on the body. 
         * This attribute will most often NOT be populated. It is most commonly used for injections. For example, Slow Push, Deep 
         * IV.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getMethod() {
            return method;
        }

        /**
         * <p>
         * The amount of the medication given at one administration event. Use this value when the administration is essentially 
         * an instantaneous event such as a swallowing a tablet or giving an injection.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getDose() {
            return dose;
        }

        /**
         * <p>
         * Identifies the speed with which the medication was or will be introduced into the patient. Typically, the rate for an 
         * infusion e.g. 100 ml per 1 hour or 100 ml/hr. May also be expressed as a rate per unit of time, e.g. 500 ml per 2 
         * hours. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getRate() {
            return rate;
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
                    accept(text, "text", visitor);
                    accept(site, "site", visitor);
                    accept(route, "route", visitor);
                    accept(method, "method", visitor);
                    accept(dose, "dose", visitor);
                    accept(rate, "rate", visitor, true);
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
            private String text;
            private CodeableConcept site;
            private CodeableConcept route;
            private CodeableConcept method;
            private Quantity dose;
            private Element rate;

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
             * Free text dosage can be used for cases where the dosage administered is too complex to code. When coded dosage is 
             * present, the free text dosage may still be present for display to humans.

The dosage instructions should reflect the 
             * dosage of the medication that was administered.
             * </p>
             * 
             * @param text
             *     Free text dosage instructions e.g. SIG
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            /**
             * <p>
             * A coded specification of the anatomic site where the medication first entered the body. For example, "left arm".
             * </p>
             * 
             * @param site
             *     Body site administered to
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder site(CodeableConcept site) {
                this.site = site;
                return this;
            }

            /**
             * <p>
             * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient. 
             * For example, topical, intravenous, etc.
             * </p>
             * 
             * @param route
             *     Path of substance into body
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder route(CodeableConcept route) {
                this.route = route;
                return this;
            }

            /**
             * <p>
             * A coded value indicating the method by which the medication is intended to be or was introduced into or on the body. 
             * This attribute will most often NOT be populated. It is most commonly used for injections. For example, Slow Push, Deep 
             * IV.
             * </p>
             * 
             * @param method
             *     How drug was administered
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder method(CodeableConcept method) {
                this.method = method;
                return this;
            }

            /**
             * <p>
             * The amount of the medication given at one administration event. Use this value when the administration is essentially 
             * an instantaneous event such as a swallowing a tablet or giving an injection.
             * </p>
             * 
             * @param dose
             *     Amount of medication per dose
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder dose(Quantity dose) {
                this.dose = dose;
                return this;
            }

            /**
             * <p>
             * Identifies the speed with which the medication was or will be introduced into the patient. Typically, the rate for an 
             * infusion e.g. 100 ml per 1 hour or 100 ml/hr. May also be expressed as a rate per unit of time, e.g. 500 ml per 2 
             * hours. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.
             * </p>
             * 
             * @param rate
             *     Dose quantity per unit of time
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder rate(Element rate) {
                this.rate = rate;
                return this;
            }

            @Override
            public Dosage build() {
                return new Dosage(this);
            }

            private static Builder from(Dosage dosage) {
                Builder builder = new Builder();
                builder.text = dosage.text;
                builder.site = dosage.site;
                builder.route = dosage.route;
                builder.method = dosage.method;
                builder.dose = dosage.dose;
                builder.rate = dosage.rate;
                return builder;
            }
        }
    }
}
