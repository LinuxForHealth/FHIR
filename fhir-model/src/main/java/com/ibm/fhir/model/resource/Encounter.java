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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.EncounterLocationStatus;
import com.ibm.fhir.model.type.code.EncounterStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or 
 * assessing the health status of a patient.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "encounter-0",
    level = "Warning",
    location = "(base)",
    description = "SHALL, if possible, contain a code from value set http://terminology.hl7.org/ValueSet/v3-ActEncounterCode",
    expression = "class.exists() and class.memberOf('http://terminology.hl7.org/ValueSet/v3-ActEncounterCode', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Encounter",
    generated = true
)
@Constraint(
    id = "encounter-1",
    level = "Warning",
    location = "classHistory.class",
    description = "SHALL, if possible, contain a code from value set http://terminology.hl7.org/ValueSet/v3-ActEncounterCode",
    expression = "$this.memberOf('http://terminology.hl7.org/ValueSet/v3-ActEncounterCode', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Encounter",
    generated = true
)
@Constraint(
    id = "encounter-2",
    level = "Warning",
    location = "participant.type",
    description = "SHALL, if possible, contain a code from value set http://hl7.org/fhir/ValueSet/encounter-participant-type",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/encounter-participant-type', 'extensible')",
    source = "http://hl7.org/fhir/StructureDefinition/Encounter",
    generated = true
)
@Constraint(
    id = "encounter-3",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/encounter-reason",
    expression = "reasonCode.exists() implies (reasonCode.all(memberOf('http://hl7.org/fhir/ValueSet/encounter-reason', 'preferred')))",
    source = "http://hl7.org/fhir/StructureDefinition/Encounter",
    generated = true
)
@Constraint(
    id = "encounter-4",
    level = "Warning",
    location = "diagnosis.use",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/diagnosis-role",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/diagnosis-role', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/Encounter",
    generated = true
)
@Constraint(
    id = "encounter-5",
    level = "Warning",
    location = "hospitalization.admitSource",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/encounter-admit-source",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/encounter-admit-source', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/Encounter",
    generated = true
)
@Constraint(
    id = "encounter-6",
    level = "Warning",
    location = "hospitalization.specialCourtesy",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/encounter-special-courtesy",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/encounter-special-courtesy', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/Encounter",
    generated = true
)
@Constraint(
    id = "encounter-7",
    level = "Warning",
    location = "hospitalization.specialArrangement",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/encounter-special-arrangements",
    expression = "$this.memberOf('http://hl7.org/fhir/ValueSet/encounter-special-arrangements', 'preferred')",
    source = "http://hl7.org/fhir/StructureDefinition/Encounter",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Encounter extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "EncounterStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Current state of the encounter.",
        valueSet = "http://hl7.org/fhir/ValueSet/encounter-status|4.1.0"
    )
    @Required
    private final EncounterStatus status;
    private final List<StatusHistory> statusHistory;
    @Summary
    @Binding(
        bindingName = "EncounterClass",
        strength = BindingStrength.Value.EXTENSIBLE,
        description = "Classification of the encounter.",
        valueSet = "http://terminology.hl7.org/ValueSet/v3-ActEncounterCode"
    )
    @Required
    private final Coding clazz;
    private final List<ClassHistory> classHistory;
    @Summary
    @Binding(
        bindingName = "EncounterType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "The type of encounter.",
        valueSet = "http://hl7.org/fhir/ValueSet/encounter-type"
    )
    private final List<CodeableConcept> type;
    @Summary
    @Binding(
        bindingName = "EncounterServiceType",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Broad categorization of the service that is to be provided.",
        valueSet = "http://hl7.org/fhir/ValueSet/service-type"
    )
    private final CodeableConcept serviceType;
    @Binding(
        bindingName = "Priority",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Indicates the urgency of the encounter.",
        valueSet = "http://terminology.hl7.org/ValueSet/v3-ActPriority"
    )
    private final CodeableConcept priority;
    @Summary
    @ReferenceTarget({ "Patient", "Group" })
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "EpisodeOfCare" })
    private final List<Reference> episodeOfCare;
    @ReferenceTarget({ "ServiceRequest" })
    private final List<Reference> basedOn;
    @Summary
    private final List<Participant> participant;
    @Summary
    @ReferenceTarget({ "Appointment" })
    private final List<Reference> appointment;
    private final Period period;
    private final Duration length;
    @Summary
    @Binding(
        bindingName = "EncounterReason",
        strength = BindingStrength.Value.PREFERRED,
        description = "Reason why the encounter takes place.",
        valueSet = "http://hl7.org/fhir/ValueSet/encounter-reason"
    )
    private final List<CodeableConcept> reasonCode;
    @Summary
    @ReferenceTarget({ "Condition", "Procedure", "Observation", "ImmunizationRecommendation" })
    private final List<Reference> reasonReference;
    @Summary
    private final List<Diagnosis> diagnosis;
    @ReferenceTarget({ "Account" })
    private final List<Reference> account;
    private final Hospitalization hospitalization;
    private final List<Location> location;
    @ReferenceTarget({ "Organization" })
    private final Reference serviceProvider;
    @ReferenceTarget({ "Encounter" })
    private final Reference partOf;

    private Encounter(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        statusHistory = Collections.unmodifiableList(builder.statusHistory);
        clazz = builder.clazz;
        classHistory = Collections.unmodifiableList(builder.classHistory);
        type = Collections.unmodifiableList(builder.type);
        serviceType = builder.serviceType;
        priority = builder.priority;
        subject = builder.subject;
        episodeOfCare = Collections.unmodifiableList(builder.episodeOfCare);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        participant = Collections.unmodifiableList(builder.participant);
        appointment = Collections.unmodifiableList(builder.appointment);
        period = builder.period;
        length = builder.length;
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        diagnosis = Collections.unmodifiableList(builder.diagnosis);
        account = Collections.unmodifiableList(builder.account);
        hospitalization = builder.hospitalization;
        location = Collections.unmodifiableList(builder.location);
        serviceProvider = builder.serviceProvider;
        partOf = builder.partOf;
    }

    /**
     * Identifier(s) by which this encounter is known.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * planned | arrived | triaged | in-progress | onleave | finished | cancelled +.
     * 
     * @return
     *     An immutable object of type {@link EncounterStatus} that is non-null.
     */
    public EncounterStatus getStatus() {
        return status;
    }

    /**
     * The status history permits the encounter resource to contain the status history without needing to read through the 
     * historical versions of the resource, or even have the server store them.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link StatusHistory} that may be empty.
     */
    public List<StatusHistory> getStatusHistory() {
        return statusHistory;
    }

    /**
     * Concepts representing classification of patient encounter such as ambulatory (outpatient), inpatient, emergency, home 
     * health or others due to local variations.
     * 
     * @return
     *     An immutable object of type {@link Coding} that is non-null.
     */
    public Coding getClazz() {
        return clazz;
    }

    /**
     * The class history permits the tracking of the encounters transitions without needing to go through the resource 
     * history. This would be used for a case where an admission starts of as an emergency encounter, then transitions into 
     * an inpatient scenario. Doing this and not restarting a new encounter ensures that any lab/diagnostic results can more 
     * easily follow the patient and not require re-processing and not get lost or cancelled during a kind of discharge from 
     * emergency to inpatient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ClassHistory} that may be empty.
     */
    public List<ClassHistory> getClassHistory() {
        return classHistory;
    }

    /**
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getType() {
        return type;
    }

    /**
     * Broad categorization of the service that is to be provided (e.g. cardiology).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getServiceType() {
        return serviceType;
    }

    /**
     * Indicates the urgency of the encounter.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPriority() {
        return priority;
    }

    /**
     * The patient or group present at the encounter.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. 
     * This association can facilitate grouping of related encounters together for a specific purpose, such as government 
     * reporting, issue tracking, association via a common problem. The association is recorded on the encounter as these are 
     * typically created after the episode of care and grouped on entry rather than editing the episode of care to append 
     * another encounter to it (the episode of care could span years).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getEpisodeOfCare() {
        return episodeOfCare;
    }

    /**
     * The request this encounter satisfies (e.g. incoming referral or procedure request).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * The list of people responsible for providing the service.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Participant} that may be empty.
     */
    public List<Participant> getParticipant() {
        return participant;
    }

    /**
     * The appointment that scheduled this encounter.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAppointment() {
        return appointment;
    }

    /**
     * The start and end time of the encounter.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Quantity of time the encounter lasted. This excludes the time during leaves of absence.
     * 
     * @return
     *     An immutable object of type {@link Duration} that may be null.
     */
    public Duration getLength() {
        return length;
    }

    /**
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission 
     * diagnosis.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission 
     * diagnosis.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * The list of diagnosis relevant to this encounter.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Diagnosis} that may be empty.
     */
    public List<Diagnosis> getDiagnosis() {
        return diagnosis;
    }

    /**
     * The set of accounts that may be used for billing for this Encounter.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAccount() {
        return account;
    }

    /**
     * Details about the admission to a healthcare service.
     * 
     * @return
     *     An immutable object of type {@link Hospitalization} that may be null.
     */
    public Hospitalization getHospitalization() {
        return hospitalization;
    }

    /**
     * List of locations where the patient has been during this encounter.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Location} that may be empty.
     */
    public List<Location> getLocation() {
        return location;
    }

    /**
     * The organization that is primarily responsible for this Encounter's services. This MAY be the same as the organization 
     * on the Patient record, however it could be different, such as if the actor performing the services was from an 
     * external organization (which may be billed seperately) for an external consultation. Refer to the example bundle 
     * showing an abbreviated set of Encounters for a colonoscopy.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getServiceProvider() {
        return serviceProvider;
    }

    /**
     * Another Encounter of which this encounter is a part of (administratively or in time).
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPartOf() {
        return partOf;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            !statusHistory.isEmpty() || 
            (clazz != null) || 
            !classHistory.isEmpty() || 
            !type.isEmpty() || 
            (serviceType != null) || 
            (priority != null) || 
            (subject != null) || 
            !episodeOfCare.isEmpty() || 
            !basedOn.isEmpty() || 
            !participant.isEmpty() || 
            !appointment.isEmpty() || 
            (period != null) || 
            (length != null) || 
            !reasonCode.isEmpty() || 
            !reasonReference.isEmpty() || 
            !diagnosis.isEmpty() || 
            !account.isEmpty() || 
            (hospitalization != null) || 
            !location.isEmpty() || 
            (serviceProvider != null) || 
            (partOf != null);
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
                accept(status, "status", visitor);
                accept(statusHistory, "statusHistory", visitor, StatusHistory.class);
                accept(clazz, "class", visitor);
                accept(classHistory, "classHistory", visitor, ClassHistory.class);
                accept(type, "type", visitor, CodeableConcept.class);
                accept(serviceType, "serviceType", visitor);
                accept(priority, "priority", visitor);
                accept(subject, "subject", visitor);
                accept(episodeOfCare, "episodeOfCare", visitor, Reference.class);
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(participant, "participant", visitor, Participant.class);
                accept(appointment, "appointment", visitor, Reference.class);
                accept(period, "period", visitor);
                accept(length, "length", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(diagnosis, "diagnosis", visitor, Diagnosis.class);
                accept(account, "account", visitor, Reference.class);
                accept(hospitalization, "hospitalization", visitor);
                accept(location, "location", visitor, Location.class);
                accept(serviceProvider, "serviceProvider", visitor);
                accept(partOf, "partOf", visitor);
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
        Encounter other = (Encounter) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusHistory, other.statusHistory) && 
            Objects.equals(clazz, other.clazz) && 
            Objects.equals(classHistory, other.classHistory) && 
            Objects.equals(type, other.type) && 
            Objects.equals(serviceType, other.serviceType) && 
            Objects.equals(priority, other.priority) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(episodeOfCare, other.episodeOfCare) && 
            Objects.equals(basedOn, other.basedOn) && 
            Objects.equals(participant, other.participant) && 
            Objects.equals(appointment, other.appointment) && 
            Objects.equals(period, other.period) && 
            Objects.equals(length, other.length) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(diagnosis, other.diagnosis) && 
            Objects.equals(account, other.account) && 
            Objects.equals(hospitalization, other.hospitalization) && 
            Objects.equals(location, other.location) && 
            Objects.equals(serviceProvider, other.serviceProvider) && 
            Objects.equals(partOf, other.partOf);
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
                status, 
                statusHistory, 
                clazz, 
                classHistory, 
                type, 
                serviceType, 
                priority, 
                subject, 
                episodeOfCare, 
                basedOn, 
                participant, 
                appointment, 
                period, 
                length, 
                reasonCode, 
                reasonReference, 
                diagnosis, 
                account, 
                hospitalization, 
                location, 
                serviceProvider, 
                partOf);
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
        private EncounterStatus status;
        private List<StatusHistory> statusHistory = new ArrayList<>();
        private Coding clazz;
        private List<ClassHistory> classHistory = new ArrayList<>();
        private List<CodeableConcept> type = new ArrayList<>();
        private CodeableConcept serviceType;
        private CodeableConcept priority;
        private Reference subject;
        private List<Reference> episodeOfCare = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Participant> participant = new ArrayList<>();
        private List<Reference> appointment = new ArrayList<>();
        private Period period;
        private Duration length;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Diagnosis> diagnosis = new ArrayList<>();
        private List<Reference> account = new ArrayList<>();
        private Hospitalization hospitalization;
        private List<Location> location = new ArrayList<>();
        private Reference serviceProvider;
        private Reference partOf;

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
         * Identifier(s) by which this encounter is known.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Identifier(s) by which this encounter is known
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
         * Identifier(s) by which this encounter is known.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Identifier(s) by which this encounter is known
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
         * planned | arrived | triaged | in-progress | onleave | finished | cancelled +.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     planned | arrived | triaged | in-progress | onleave | finished | cancelled +
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(EncounterStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The status history permits the encounter resource to contain the status history without needing to read through the 
         * historical versions of the resource, or even have the server store them.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param statusHistory
         *     List of past encounter statuses
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusHistory(StatusHistory... statusHistory) {
            for (StatusHistory value : statusHistory) {
                this.statusHistory.add(value);
            }
            return this;
        }

        /**
         * The status history permits the encounter resource to contain the status history without needing to read through the 
         * historical versions of the resource, or even have the server store them.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param statusHistory
         *     List of past encounter statuses
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder statusHistory(Collection<StatusHistory> statusHistory) {
            this.statusHistory = new ArrayList<>(statusHistory);
            return this;
        }

        /**
         * Concepts representing classification of patient encounter such as ambulatory (outpatient), inpatient, emergency, home 
         * health or others due to local variations.
         * 
         * <p>This element is required.
         * 
         * @param clazz
         *     Classification of patient encounter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder clazz(Coding clazz) {
            this.clazz = clazz;
            return this;
        }

        /**
         * The class history permits the tracking of the encounters transitions without needing to go through the resource 
         * history. This would be used for a case where an admission starts of as an emergency encounter, then transitions into 
         * an inpatient scenario. Doing this and not restarting a new encounter ensures that any lab/diagnostic results can more 
         * easily follow the patient and not require re-processing and not get lost or cancelled during a kind of discharge from 
         * emergency to inpatient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param classHistory
         *     List of past encounter classes
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder classHistory(ClassHistory... classHistory) {
            for (ClassHistory value : classHistory) {
                this.classHistory.add(value);
            }
            return this;
        }

        /**
         * The class history permits the tracking of the encounters transitions without needing to go through the resource 
         * history. This would be used for a case where an admission starts of as an emergency encounter, then transitions into 
         * an inpatient scenario. Doing this and not restarting a new encounter ensures that any lab/diagnostic results can more 
         * easily follow the patient and not require re-processing and not get lost or cancelled during a kind of discharge from 
         * emergency to inpatient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param classHistory
         *     List of past encounter classes
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder classHistory(Collection<ClassHistory> classHistory) {
            this.classHistory = new ArrayList<>(classHistory);
            return this;
        }

        /**
         * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param type
         *     Specific type of encounter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept... type) {
            for (CodeableConcept value : type) {
                this.type.add(value);
            }
            return this;
        }

        /**
         * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param type
         *     Specific type of encounter
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder type(Collection<CodeableConcept> type) {
            this.type = new ArrayList<>(type);
            return this;
        }

        /**
         * Broad categorization of the service that is to be provided (e.g. cardiology).
         * 
         * @param serviceType
         *     Specific type of service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serviceType(CodeableConcept serviceType) {
            this.serviceType = serviceType;
            return this;
        }

        /**
         * Indicates the urgency of the encounter.
         * 
         * @param priority
         *     Indicates the urgency of the encounter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priority(CodeableConcept priority) {
            this.priority = priority;
            return this;
        }

        /**
         * The patient or group present at the encounter.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param subject
         *     The patient or group present at the encounter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. 
         * This association can facilitate grouping of related encounters together for a specific purpose, such as government 
         * reporting, issue tracking, association via a common problem. The association is recorded on the encounter as these are 
         * typically created after the episode of care and grouped on entry rather than editing the episode of care to append 
         * another encounter to it (the episode of care could span years).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link EpisodeOfCare}</li>
         * </ul>
         * 
         * @param episodeOfCare
         *     Episode(s) of care that this encounter should be recorded against
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder episodeOfCare(Reference... episodeOfCare) {
            for (Reference value : episodeOfCare) {
                this.episodeOfCare.add(value);
            }
            return this;
        }

        /**
         * Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. 
         * This association can facilitate grouping of related encounters together for a specific purpose, such as government 
         * reporting, issue tracking, association via a common problem. The association is recorded on the encounter as these are 
         * typically created after the episode of care and grouped on entry rather than editing the episode of care to append 
         * another encounter to it (the episode of care could span years).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link EpisodeOfCare}</li>
         * </ul>
         * 
         * @param episodeOfCare
         *     Episode(s) of care that this encounter should be recorded against
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder episodeOfCare(Collection<Reference> episodeOfCare) {
            this.episodeOfCare = new ArrayList<>(episodeOfCare);
            return this;
        }

        /**
         * The request this encounter satisfies (e.g. incoming referral or procedure request).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     The ServiceRequest that initiated this encounter
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
         * The request this encounter satisfies (e.g. incoming referral or procedure request).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     The ServiceRequest that initiated this encounter
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
         * The list of people responsible for providing the service.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param participant
         *     List of participants involved in the encounter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder participant(Participant... participant) {
            for (Participant value : participant) {
                this.participant.add(value);
            }
            return this;
        }

        /**
         * The list of people responsible for providing the service.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param participant
         *     List of participants involved in the encounter
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder participant(Collection<Participant> participant) {
            this.participant = new ArrayList<>(participant);
            return this;
        }

        /**
         * The appointment that scheduled this encounter.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Appointment}</li>
         * </ul>
         * 
         * @param appointment
         *     The appointment that scheduled this encounter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder appointment(Reference... appointment) {
            for (Reference value : appointment) {
                this.appointment.add(value);
            }
            return this;
        }

        /**
         * The appointment that scheduled this encounter.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Appointment}</li>
         * </ul>
         * 
         * @param appointment
         *     The appointment that scheduled this encounter
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder appointment(Collection<Reference> appointment) {
            this.appointment = new ArrayList<>(appointment);
            return this;
        }

        /**
         * The start and end time of the encounter.
         * 
         * @param period
         *     The start and end time of the encounter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * Quantity of time the encounter lasted. This excludes the time during leaves of absence.
         * 
         * @param length
         *     Quantity of time the encounter lasted (less time absent)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder length(Duration length) {
            this.length = length;
            return this;
        }

        /**
         * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission 
         * diagnosis.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Coded reason the encounter takes place
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
         * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission 
         * diagnosis.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Coded reason the encounter takes place
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
         * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission 
         * diagnosis.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Observation}</li>
         * <li>{@link ImmunizationRecommendation}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Reason the encounter takes place (reference)
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
         * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission 
         * diagnosis.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Observation}</li>
         * <li>{@link ImmunizationRecommendation}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Reason the encounter takes place (reference)
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
         * The list of diagnosis relevant to this encounter.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param diagnosis
         *     The list of diagnosis relevant to this encounter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder diagnosis(Diagnosis... diagnosis) {
            for (Diagnosis value : diagnosis) {
                this.diagnosis.add(value);
            }
            return this;
        }

        /**
         * The list of diagnosis relevant to this encounter.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param diagnosis
         *     The list of diagnosis relevant to this encounter
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder diagnosis(Collection<Diagnosis> diagnosis) {
            this.diagnosis = new ArrayList<>(diagnosis);
            return this;
        }

        /**
         * The set of accounts that may be used for billing for this Encounter.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Account}</li>
         * </ul>
         * 
         * @param account
         *     The set of accounts that may be used for billing for this Encounter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder account(Reference... account) {
            for (Reference value : account) {
                this.account.add(value);
            }
            return this;
        }

        /**
         * The set of accounts that may be used for billing for this Encounter.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Account}</li>
         * </ul>
         * 
         * @param account
         *     The set of accounts that may be used for billing for this Encounter
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder account(Collection<Reference> account) {
            this.account = new ArrayList<>(account);
            return this;
        }

        /**
         * Details about the admission to a healthcare service.
         * 
         * @param hospitalization
         *     Details about the admission to a healthcare service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder hospitalization(Hospitalization hospitalization) {
            this.hospitalization = hospitalization;
            return this;
        }

        /**
         * List of locations where the patient has been during this encounter.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param location
         *     List of locations where the patient has been
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(Location... location) {
            for (Location value : location) {
                this.location.add(value);
            }
            return this;
        }

        /**
         * List of locations where the patient has been during this encounter.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param location
         *     List of locations where the patient has been
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder location(Collection<Location> location) {
            this.location = new ArrayList<>(location);
            return this;
        }

        /**
         * The organization that is primarily responsible for this Encounter's services. This MAY be the same as the organization 
         * on the Patient record, however it could be different, such as if the actor performing the services was from an 
         * external organization (which may be billed seperately) for an external consultation. Refer to the example bundle 
         * showing an abbreviated set of Encounters for a colonoscopy.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param serviceProvider
         *     The organization (facility) responsible for this encounter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serviceProvider(Reference serviceProvider) {
            this.serviceProvider = serviceProvider;
            return this;
        }

        /**
         * Another Encounter of which this encounter is a part of (administratively or in time).
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
         * 
         * @param partOf
         *     Another Encounter this encounter is part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Reference partOf) {
            this.partOf = partOf;
            return this;
        }

        /**
         * Build the {@link Encounter}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>class</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Encounter}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Encounter per the base specification
         */
        @Override
        public Encounter build() {
            Encounter encounter = new Encounter(this);
            if (validating) {
                validate(encounter);
            }
            return encounter;
        }

        protected void validate(Encounter encounter) {
            super.validate(encounter);
            ValidationSupport.checkList(encounter.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(encounter.status, "status");
            ValidationSupport.checkList(encounter.statusHistory, "statusHistory", StatusHistory.class);
            ValidationSupport.requireNonNull(encounter.clazz, "class");
            ValidationSupport.checkList(encounter.classHistory, "classHistory", ClassHistory.class);
            ValidationSupport.checkList(encounter.type, "type", CodeableConcept.class);
            ValidationSupport.checkList(encounter.episodeOfCare, "episodeOfCare", Reference.class);
            ValidationSupport.checkList(encounter.basedOn, "basedOn", Reference.class);
            ValidationSupport.checkList(encounter.participant, "participant", Participant.class);
            ValidationSupport.checkList(encounter.appointment, "appointment", Reference.class);
            ValidationSupport.checkList(encounter.reasonCode, "reasonCode", CodeableConcept.class);
            ValidationSupport.checkList(encounter.reasonReference, "reasonReference", Reference.class);
            ValidationSupport.checkList(encounter.diagnosis, "diagnosis", Diagnosis.class);
            ValidationSupport.checkList(encounter.account, "account", Reference.class);
            ValidationSupport.checkList(encounter.location, "location", Location.class);
            ValidationSupport.checkReferenceType(encounter.subject, "subject", "Patient", "Group");
            ValidationSupport.checkReferenceType(encounter.episodeOfCare, "episodeOfCare", "EpisodeOfCare");
            ValidationSupport.checkReferenceType(encounter.basedOn, "basedOn", "ServiceRequest");
            ValidationSupport.checkReferenceType(encounter.appointment, "appointment", "Appointment");
            ValidationSupport.checkReferenceType(encounter.reasonReference, "reasonReference", "Condition", "Procedure", "Observation", "ImmunizationRecommendation");
            ValidationSupport.checkReferenceType(encounter.account, "account", "Account");
            ValidationSupport.checkReferenceType(encounter.serviceProvider, "serviceProvider", "Organization");
            ValidationSupport.checkReferenceType(encounter.partOf, "partOf", "Encounter");
        }

        protected Builder from(Encounter encounter) {
            super.from(encounter);
            identifier.addAll(encounter.identifier);
            status = encounter.status;
            statusHistory.addAll(encounter.statusHistory);
            clazz = encounter.clazz;
            classHistory.addAll(encounter.classHistory);
            type.addAll(encounter.type);
            serviceType = encounter.serviceType;
            priority = encounter.priority;
            subject = encounter.subject;
            episodeOfCare.addAll(encounter.episodeOfCare);
            basedOn.addAll(encounter.basedOn);
            participant.addAll(encounter.participant);
            appointment.addAll(encounter.appointment);
            period = encounter.period;
            length = encounter.length;
            reasonCode.addAll(encounter.reasonCode);
            reasonReference.addAll(encounter.reasonReference);
            diagnosis.addAll(encounter.diagnosis);
            account.addAll(encounter.account);
            hospitalization = encounter.hospitalization;
            location.addAll(encounter.location);
            serviceProvider = encounter.serviceProvider;
            partOf = encounter.partOf;
            return this;
        }
    }

    /**
     * The status history permits the encounter resource to contain the status history without needing to read through the 
     * historical versions of the resource, or even have the server store them.
     */
    public static class StatusHistory extends BackboneElement {
        @Binding(
            bindingName = "EncounterStatus",
            strength = BindingStrength.Value.REQUIRED,
            description = "Current state of the encounter.",
            valueSet = "http://hl7.org/fhir/ValueSet/encounter-status|4.1.0"
        )
        @Required
        private final EncounterStatus status;
        @Required
        private final Period period;

        private StatusHistory(Builder builder) {
            super(builder);
            status = builder.status;
            period = builder.period;
        }

        /**
         * planned | arrived | triaged | in-progress | onleave | finished | cancelled +.
         * 
         * @return
         *     An immutable object of type {@link EncounterStatus} that is non-null.
         */
        public EncounterStatus getStatus() {
            return status;
        }

        /**
         * The time that the episode was in the specified status.
         * 
         * @return
         *     An immutable object of type {@link Period} that is non-null.
         */
        public Period getPeriod() {
            return period;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (status != null) || 
                (period != null);
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
                    accept(status, "status", visitor);
                    accept(period, "period", visitor);
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
            StatusHistory other = (StatusHistory) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(status, other.status) && 
                Objects.equals(period, other.period);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    status, 
                    period);
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
            private EncounterStatus status;
            private Period period;

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
             * planned | arrived | triaged | in-progress | onleave | finished | cancelled +.
             * 
             * <p>This element is required.
             * 
             * @param status
             *     planned | arrived | triaged | in-progress | onleave | finished | cancelled +
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder status(EncounterStatus status) {
                this.status = status;
                return this;
            }

            /**
             * The time that the episode was in the specified status.
             * 
             * <p>This element is required.
             * 
             * @param period
             *     The time that the episode was in the specified status
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * Build the {@link StatusHistory}
             * 
             * <p>Required elements:
             * <ul>
             * <li>status</li>
             * <li>period</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link StatusHistory}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid StatusHistory per the base specification
             */
            @Override
            public StatusHistory build() {
                StatusHistory statusHistory = new StatusHistory(this);
                if (validating) {
                    validate(statusHistory);
                }
                return statusHistory;
            }

            protected void validate(StatusHistory statusHistory) {
                super.validate(statusHistory);
                ValidationSupport.requireNonNull(statusHistory.status, "status");
                ValidationSupport.requireNonNull(statusHistory.period, "period");
                ValidationSupport.requireValueOrChildren(statusHistory);
            }

            protected Builder from(StatusHistory statusHistory) {
                super.from(statusHistory);
                status = statusHistory.status;
                period = statusHistory.period;
                return this;
            }
        }
    }

    /**
     * The class history permits the tracking of the encounters transitions without needing to go through the resource 
     * history. This would be used for a case where an admission starts of as an emergency encounter, then transitions into 
     * an inpatient scenario. Doing this and not restarting a new encounter ensures that any lab/diagnostic results can more 
     * easily follow the patient and not require re-processing and not get lost or cancelled during a kind of discharge from 
     * emergency to inpatient.
     */
    public static class ClassHistory extends BackboneElement {
        @Binding(
            bindingName = "EncounterClass",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Classification of the encounter.",
            valueSet = "http://terminology.hl7.org/ValueSet/v3-ActEncounterCode"
        )
        @Required
        private final Coding clazz;
        @Required
        private final Period period;

        private ClassHistory(Builder builder) {
            super(builder);
            clazz = builder.clazz;
            period = builder.period;
        }

        /**
         * inpatient | outpatient | ambulatory | emergency +.
         * 
         * @return
         *     An immutable object of type {@link Coding} that is non-null.
         */
        public Coding getClazz() {
            return clazz;
        }

        /**
         * The time that the episode was in the specified class.
         * 
         * @return
         *     An immutable object of type {@link Period} that is non-null.
         */
        public Period getPeriod() {
            return period;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (clazz != null) || 
                (period != null);
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
                    accept(clazz, "class", visitor);
                    accept(period, "period", visitor);
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
            ClassHistory other = (ClassHistory) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(clazz, other.clazz) && 
                Objects.equals(period, other.period);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    clazz, 
                    period);
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
            private Coding clazz;
            private Period period;

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
             * inpatient | outpatient | ambulatory | emergency +.
             * 
             * <p>This element is required.
             * 
             * @param clazz
             *     inpatient | outpatient | ambulatory | emergency +
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder clazz(Coding clazz) {
                this.clazz = clazz;
                return this;
            }

            /**
             * The time that the episode was in the specified class.
             * 
             * <p>This element is required.
             * 
             * @param period
             *     The time that the episode was in the specified class
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * Build the {@link ClassHistory}
             * 
             * <p>Required elements:
             * <ul>
             * <li>class</li>
             * <li>period</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link ClassHistory}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ClassHistory per the base specification
             */
            @Override
            public ClassHistory build() {
                ClassHistory classHistory = new ClassHistory(this);
                if (validating) {
                    validate(classHistory);
                }
                return classHistory;
            }

            protected void validate(ClassHistory classHistory) {
                super.validate(classHistory);
                ValidationSupport.requireNonNull(classHistory.clazz, "class");
                ValidationSupport.requireNonNull(classHistory.period, "period");
                ValidationSupport.requireValueOrChildren(classHistory);
            }

            protected Builder from(ClassHistory classHistory) {
                super.from(classHistory);
                clazz = classHistory.clazz;
                period = classHistory.period;
                return this;
            }
        }
    }

    /**
     * The list of people responsible for providing the service.
     */
    public static class Participant extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "ParticipantType",
            strength = BindingStrength.Value.EXTENSIBLE,
            description = "Role of participant in encounter.",
            valueSet = "http://hl7.org/fhir/ValueSet/encounter-participant-type"
        )
        private final List<CodeableConcept> type;
        private final Period period;
        @Summary
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "RelatedPerson" })
        private final Reference individual;

        private Participant(Builder builder) {
            super(builder);
            type = Collections.unmodifiableList(builder.type);
            period = builder.period;
            individual = builder.individual;
        }

        /**
         * Role of participant in encounter.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getType() {
            return type;
        }

        /**
         * The period of time that the specified participant participated in the encounter. These can overlap or be sub-sets of 
         * the overall encounter's period.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * Persons involved in the encounter other than the patient.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getIndividual() {
            return individual;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !type.isEmpty() || 
                (period != null) || 
                (individual != null);
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
                    accept(type, "type", visitor, CodeableConcept.class);
                    accept(period, "period", visitor);
                    accept(individual, "individual", visitor);
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
            Participant other = (Participant) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(period, other.period) && 
                Objects.equals(individual, other.individual);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    period, 
                    individual);
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
            private List<CodeableConcept> type = new ArrayList<>();
            private Period period;
            private Reference individual;

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
             * Role of participant in encounter.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param type
             *     Role of participant in encounter
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept... type) {
                for (CodeableConcept value : type) {
                    this.type.add(value);
                }
                return this;
            }

            /**
             * Role of participant in encounter.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param type
             *     Role of participant in encounter
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder type(Collection<CodeableConcept> type) {
                this.type = new ArrayList<>(type);
                return this;
            }

            /**
             * The period of time that the specified participant participated in the encounter. These can overlap or be sub-sets of 
             * the overall encounter's period.
             * 
             * @param period
             *     Period of time during the encounter that the participant participated
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * Persons involved in the encounter other than the patient.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link RelatedPerson}</li>
             * </ul>
             * 
             * @param individual
             *     Persons involved in the encounter other than the patient
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder individual(Reference individual) {
                this.individual = individual;
                return this;
            }

            /**
             * Build the {@link Participant}
             * 
             * @return
             *     An immutable object of type {@link Participant}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Participant per the base specification
             */
            @Override
            public Participant build() {
                Participant participant = new Participant(this);
                if (validating) {
                    validate(participant);
                }
                return participant;
            }

            protected void validate(Participant participant) {
                super.validate(participant);
                ValidationSupport.checkList(participant.type, "type", CodeableConcept.class);
                ValidationSupport.checkReferenceType(participant.individual, "individual", "Practitioner", "PractitionerRole", "RelatedPerson");
                ValidationSupport.requireValueOrChildren(participant);
            }

            protected Builder from(Participant participant) {
                super.from(participant);
                type.addAll(participant.type);
                period = participant.period;
                individual = participant.individual;
                return this;
            }
        }
    }

    /**
     * The list of diagnosis relevant to this encounter.
     */
    public static class Diagnosis extends BackboneElement {
        @Summary
        @ReferenceTarget({ "Condition", "Procedure" })
        @Required
        private final Reference condition;
        @Binding(
            bindingName = "DiagnosisRole",
            strength = BindingStrength.Value.PREFERRED,
            description = "The type of diagnosis this condition represents.",
            valueSet = "http://hl7.org/fhir/ValueSet/diagnosis-role"
        )
        private final CodeableConcept use;
        private final PositiveInt rank;

        private Diagnosis(Builder builder) {
            super(builder);
            condition = builder.condition;
            use = builder.use;
            rank = builder.rank;
        }

        /**
         * Reason the encounter takes place, as specified using information from another resource. For admissions, this is the 
         * admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.
         * detail), or a Procedure.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getCondition() {
            return condition;
        }

        /**
         * Role that this diagnosis has within the encounter (e.g. admission, billing, discharge …).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getUse() {
            return use;
        }

        /**
         * Ranking of the diagnosis (for each role type).
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that may be null.
         */
        public PositiveInt getRank() {
            return rank;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (condition != null) || 
                (use != null) || 
                (rank != null);
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
                    accept(condition, "condition", visitor);
                    accept(use, "use", visitor);
                    accept(rank, "rank", visitor);
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
            Diagnosis other = (Diagnosis) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(condition, other.condition) && 
                Objects.equals(use, other.use) && 
                Objects.equals(rank, other.rank);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    condition, 
                    use, 
                    rank);
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
            private Reference condition;
            private CodeableConcept use;
            private PositiveInt rank;

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
             * Reason the encounter takes place, as specified using information from another resource. For admissions, this is the 
             * admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.
             * detail), or a Procedure.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Condition}</li>
             * <li>{@link Procedure}</li>
             * </ul>
             * 
             * @param condition
             *     The diagnosis or procedure relevant to the encounter
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder condition(Reference condition) {
                this.condition = condition;
                return this;
            }

            /**
             * Role that this diagnosis has within the encounter (e.g. admission, billing, discharge …).
             * 
             * @param use
             *     Role that this diagnosis has within the encounter (e.g. admission, billing, discharge …)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder use(CodeableConcept use) {
                this.use = use;
                return this;
            }

            /**
             * Ranking of the diagnosis (for each role type).
             * 
             * @param rank
             *     Ranking of the diagnosis (for each role type)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder rank(PositiveInt rank) {
                this.rank = rank;
                return this;
            }

            /**
             * Build the {@link Diagnosis}
             * 
             * <p>Required elements:
             * <ul>
             * <li>condition</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Diagnosis}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Diagnosis per the base specification
             */
            @Override
            public Diagnosis build() {
                Diagnosis diagnosis = new Diagnosis(this);
                if (validating) {
                    validate(diagnosis);
                }
                return diagnosis;
            }

            protected void validate(Diagnosis diagnosis) {
                super.validate(diagnosis);
                ValidationSupport.requireNonNull(diagnosis.condition, "condition");
                ValidationSupport.checkReferenceType(diagnosis.condition, "condition", "Condition", "Procedure");
                ValidationSupport.requireValueOrChildren(diagnosis);
            }

            protected Builder from(Diagnosis diagnosis) {
                super.from(diagnosis);
                condition = diagnosis.condition;
                use = diagnosis.use;
                rank = diagnosis.rank;
                return this;
            }
        }
    }

    /**
     * Details about the admission to a healthcare service.
     */
    public static class Hospitalization extends BackboneElement {
        private final Identifier preAdmissionIdentifier;
        @ReferenceTarget({ "Location", "Organization" })
        private final Reference origin;
        @Binding(
            bindingName = "AdmitSource",
            strength = BindingStrength.Value.PREFERRED,
            description = "From where the patient was admitted.",
            valueSet = "http://hl7.org/fhir/ValueSet/encounter-admit-source"
        )
        private final CodeableConcept admitSource;
        @Binding(
            bindingName = "ReAdmissionType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The reason for re-admission of this hospitalization encounter.",
            valueSet = "http://terminology.hl7.org/ValueSet/v2-0092"
        )
        private final CodeableConcept reAdmission;
        @Binding(
            bindingName = "PatientDiet",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Medical, cultural or ethical food preferences to help with catering requirements.",
            valueSet = "http://hl7.org/fhir/ValueSet/encounter-diet"
        )
        private final List<CodeableConcept> dietPreference;
        @Binding(
            bindingName = "Courtesies",
            strength = BindingStrength.Value.PREFERRED,
            description = "Special courtesies.",
            valueSet = "http://hl7.org/fhir/ValueSet/encounter-special-courtesy"
        )
        private final List<CodeableConcept> specialCourtesy;
        @Binding(
            bindingName = "Arrangements",
            strength = BindingStrength.Value.PREFERRED,
            description = "Special arrangements.",
            valueSet = "http://hl7.org/fhir/ValueSet/encounter-special-arrangements"
        )
        private final List<CodeableConcept> specialArrangement;
        @ReferenceTarget({ "Location", "Organization" })
        private final Reference destination;
        @Binding(
            bindingName = "DischargeDisp",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Discharge Disposition.",
            valueSet = "http://hl7.org/fhir/ValueSet/encounter-discharge-disposition"
        )
        private final CodeableConcept dischargeDisposition;

        private Hospitalization(Builder builder) {
            super(builder);
            preAdmissionIdentifier = builder.preAdmissionIdentifier;
            origin = builder.origin;
            admitSource = builder.admitSource;
            reAdmission = builder.reAdmission;
            dietPreference = Collections.unmodifiableList(builder.dietPreference);
            specialCourtesy = Collections.unmodifiableList(builder.specialCourtesy);
            specialArrangement = Collections.unmodifiableList(builder.specialArrangement);
            destination = builder.destination;
            dischargeDisposition = builder.dischargeDisposition;
        }

        /**
         * Pre-admission identifier.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getPreAdmissionIdentifier() {
            return preAdmissionIdentifier;
        }

        /**
         * The location/organization from which the patient came before admission.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getOrigin() {
            return origin;
        }

        /**
         * From where patient was admitted (physician referral, transfer).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getAdmitSource() {
            return admitSource;
        }

        /**
         * Whether this hospitalization is a readmission and why if known.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getReAdmission() {
            return reAdmission;
        }

        /**
         * Diet preferences reported by the patient.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getDietPreference() {
            return dietPreference;
        }

        /**
         * Special courtesies (VIP, board member).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getSpecialCourtesy() {
            return specialCourtesy;
        }

        /**
         * Any special requests that have been made for this hospitalization encounter, such as the provision of specific 
         * equipment or other things.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getSpecialArrangement() {
            return specialArrangement;
        }

        /**
         * Location/organization to which the patient is discharged.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getDestination() {
            return destination;
        }

        /**
         * Category or kind of location after discharge.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getDischargeDisposition() {
            return dischargeDisposition;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (preAdmissionIdentifier != null) || 
                (origin != null) || 
                (admitSource != null) || 
                (reAdmission != null) || 
                !dietPreference.isEmpty() || 
                !specialCourtesy.isEmpty() || 
                !specialArrangement.isEmpty() || 
                (destination != null) || 
                (dischargeDisposition != null);
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
                    accept(preAdmissionIdentifier, "preAdmissionIdentifier", visitor);
                    accept(origin, "origin", visitor);
                    accept(admitSource, "admitSource", visitor);
                    accept(reAdmission, "reAdmission", visitor);
                    accept(dietPreference, "dietPreference", visitor, CodeableConcept.class);
                    accept(specialCourtesy, "specialCourtesy", visitor, CodeableConcept.class);
                    accept(specialArrangement, "specialArrangement", visitor, CodeableConcept.class);
                    accept(destination, "destination", visitor);
                    accept(dischargeDisposition, "dischargeDisposition", visitor);
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
            Hospitalization other = (Hospitalization) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(preAdmissionIdentifier, other.preAdmissionIdentifier) && 
                Objects.equals(origin, other.origin) && 
                Objects.equals(admitSource, other.admitSource) && 
                Objects.equals(reAdmission, other.reAdmission) && 
                Objects.equals(dietPreference, other.dietPreference) && 
                Objects.equals(specialCourtesy, other.specialCourtesy) && 
                Objects.equals(specialArrangement, other.specialArrangement) && 
                Objects.equals(destination, other.destination) && 
                Objects.equals(dischargeDisposition, other.dischargeDisposition);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    preAdmissionIdentifier, 
                    origin, 
                    admitSource, 
                    reAdmission, 
                    dietPreference, 
                    specialCourtesy, 
                    specialArrangement, 
                    destination, 
                    dischargeDisposition);
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
            private Identifier preAdmissionIdentifier;
            private Reference origin;
            private CodeableConcept admitSource;
            private CodeableConcept reAdmission;
            private List<CodeableConcept> dietPreference = new ArrayList<>();
            private List<CodeableConcept> specialCourtesy = new ArrayList<>();
            private List<CodeableConcept> specialArrangement = new ArrayList<>();
            private Reference destination;
            private CodeableConcept dischargeDisposition;

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
             * Pre-admission identifier.
             * 
             * @param preAdmissionIdentifier
             *     Pre-admission identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder preAdmissionIdentifier(Identifier preAdmissionIdentifier) {
                this.preAdmissionIdentifier = preAdmissionIdentifier;
                return this;
            }

            /**
             * The location/organization from which the patient came before admission.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Location}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param origin
             *     The location/organization from which the patient came before admission
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder origin(Reference origin) {
                this.origin = origin;
                return this;
            }

            /**
             * From where patient was admitted (physician referral, transfer).
             * 
             * @param admitSource
             *     From where patient was admitted (physician referral, transfer)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder admitSource(CodeableConcept admitSource) {
                this.admitSource = admitSource;
                return this;
            }

            /**
             * Whether this hospitalization is a readmission and why if known.
             * 
             * @param reAdmission
             *     The type of hospital re-admission that has occurred (if any). If the value is absent, then this is not identified as a 
             *     readmission
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder reAdmission(CodeableConcept reAdmission) {
                this.reAdmission = reAdmission;
                return this;
            }

            /**
             * Diet preferences reported by the patient.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param dietPreference
             *     Diet preferences reported by the patient
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dietPreference(CodeableConcept... dietPreference) {
                for (CodeableConcept value : dietPreference) {
                    this.dietPreference.add(value);
                }
                return this;
            }

            /**
             * Diet preferences reported by the patient.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param dietPreference
             *     Diet preferences reported by the patient
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder dietPreference(Collection<CodeableConcept> dietPreference) {
                this.dietPreference = new ArrayList<>(dietPreference);
                return this;
            }

            /**
             * Special courtesies (VIP, board member).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param specialCourtesy
             *     Special courtesies (VIP, board member)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder specialCourtesy(CodeableConcept... specialCourtesy) {
                for (CodeableConcept value : specialCourtesy) {
                    this.specialCourtesy.add(value);
                }
                return this;
            }

            /**
             * Special courtesies (VIP, board member).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param specialCourtesy
             *     Special courtesies (VIP, board member)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder specialCourtesy(Collection<CodeableConcept> specialCourtesy) {
                this.specialCourtesy = new ArrayList<>(specialCourtesy);
                return this;
            }

            /**
             * Any special requests that have been made for this hospitalization encounter, such as the provision of specific 
             * equipment or other things.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param specialArrangement
             *     Wheelchair, translator, stretcher, etc.
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder specialArrangement(CodeableConcept... specialArrangement) {
                for (CodeableConcept value : specialArrangement) {
                    this.specialArrangement.add(value);
                }
                return this;
            }

            /**
             * Any special requests that have been made for this hospitalization encounter, such as the provision of specific 
             * equipment or other things.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param specialArrangement
             *     Wheelchair, translator, stretcher, etc.
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder specialArrangement(Collection<CodeableConcept> specialArrangement) {
                this.specialArrangement = new ArrayList<>(specialArrangement);
                return this;
            }

            /**
             * Location/organization to which the patient is discharged.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Location}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param destination
             *     Location/organization to which the patient is discharged
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder destination(Reference destination) {
                this.destination = destination;
                return this;
            }

            /**
             * Category or kind of location after discharge.
             * 
             * @param dischargeDisposition
             *     Category or kind of location after discharge
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dischargeDisposition(CodeableConcept dischargeDisposition) {
                this.dischargeDisposition = dischargeDisposition;
                return this;
            }

            /**
             * Build the {@link Hospitalization}
             * 
             * @return
             *     An immutable object of type {@link Hospitalization}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Hospitalization per the base specification
             */
            @Override
            public Hospitalization build() {
                Hospitalization hospitalization = new Hospitalization(this);
                if (validating) {
                    validate(hospitalization);
                }
                return hospitalization;
            }

            protected void validate(Hospitalization hospitalization) {
                super.validate(hospitalization);
                ValidationSupport.checkList(hospitalization.dietPreference, "dietPreference", CodeableConcept.class);
                ValidationSupport.checkList(hospitalization.specialCourtesy, "specialCourtesy", CodeableConcept.class);
                ValidationSupport.checkList(hospitalization.specialArrangement, "specialArrangement", CodeableConcept.class);
                ValidationSupport.checkReferenceType(hospitalization.origin, "origin", "Location", "Organization");
                ValidationSupport.checkReferenceType(hospitalization.destination, "destination", "Location", "Organization");
                ValidationSupport.requireValueOrChildren(hospitalization);
            }

            protected Builder from(Hospitalization hospitalization) {
                super.from(hospitalization);
                preAdmissionIdentifier = hospitalization.preAdmissionIdentifier;
                origin = hospitalization.origin;
                admitSource = hospitalization.admitSource;
                reAdmission = hospitalization.reAdmission;
                dietPreference.addAll(hospitalization.dietPreference);
                specialCourtesy.addAll(hospitalization.specialCourtesy);
                specialArrangement.addAll(hospitalization.specialArrangement);
                destination = hospitalization.destination;
                dischargeDisposition = hospitalization.dischargeDisposition;
                return this;
            }
        }
    }

    /**
     * List of locations where the patient has been during this encounter.
     */
    public static class Location extends BackboneElement {
        @ReferenceTarget({ "Location" })
        @Required
        private final Reference location;
        @Binding(
            bindingName = "EncounterLocationStatus",
            strength = BindingStrength.Value.REQUIRED,
            description = "The status of the location.",
            valueSet = "http://hl7.org/fhir/ValueSet/encounter-location-status|4.1.0"
        )
        private final EncounterLocationStatus status;
        @Binding(
            bindingName = "PhysicalType",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Physical form of the location.",
            valueSet = "http://hl7.org/fhir/ValueSet/location-physical-type"
        )
        private final CodeableConcept physicalType;
        private final Period period;

        private Location(Builder builder) {
            super(builder);
            location = builder.location;
            status = builder.status;
            physicalType = builder.physicalType;
            period = builder.period;
        }

        /**
         * The location where the encounter takes place.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getLocation() {
            return location;
        }

        /**
         * The status of the participants' presence at the specified location during the period specified. If the participant is 
         * no longer at the location, then the period will have an end date/time.
         * 
         * @return
         *     An immutable object of type {@link EncounterLocationStatus} that may be null.
         */
        public EncounterLocationStatus getStatus() {
            return status;
        }

        /**
         * This will be used to specify the required levels (bed/ward/room/etc.) desired to be recorded to simplify either 
         * messaging or query.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getPhysicalType() {
            return physicalType;
        }

        /**
         * Time period during which the patient was present at the location.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getPeriod() {
            return period;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (location != null) || 
                (status != null) || 
                (physicalType != null) || 
                (period != null);
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
                    accept(location, "location", visitor);
                    accept(status, "status", visitor);
                    accept(physicalType, "physicalType", visitor);
                    accept(period, "period", visitor);
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
            Location other = (Location) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(location, other.location) && 
                Objects.equals(status, other.status) && 
                Objects.equals(physicalType, other.physicalType) && 
                Objects.equals(period, other.period);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    location, 
                    status, 
                    physicalType, 
                    period);
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
            private Reference location;
            private EncounterLocationStatus status;
            private CodeableConcept physicalType;
            private Period period;

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
             * The location where the encounter takes place.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Location}</li>
             * </ul>
             * 
             * @param location
             *     Location the encounter takes place
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder location(Reference location) {
                this.location = location;
                return this;
            }

            /**
             * The status of the participants' presence at the specified location during the period specified. If the participant is 
             * no longer at the location, then the period will have an end date/time.
             * 
             * @param status
             *     planned | active | reserved | completed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder status(EncounterLocationStatus status) {
                this.status = status;
                return this;
            }

            /**
             * This will be used to specify the required levels (bed/ward/room/etc.) desired to be recorded to simplify either 
             * messaging or query.
             * 
             * @param physicalType
             *     The physical type of the location (usually the level in the location hierachy - bed room ward etc.)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder physicalType(CodeableConcept physicalType) {
                this.physicalType = physicalType;
                return this;
            }

            /**
             * Time period during which the patient was present at the location.
             * 
             * @param period
             *     Time period during which the patient was present at the location
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * Build the {@link Location}
             * 
             * <p>Required elements:
             * <ul>
             * <li>location</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Location}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Location per the base specification
             */
            @Override
            public Location build() {
                Location location = new Location(this);
                if (validating) {
                    validate(location);
                }
                return location;
            }

            protected void validate(Location location) {
                super.validate(location);
                ValidationSupport.requireNonNull(location.location, "location");
                ValidationSupport.checkReferenceType(location.location, "location", "Location");
                ValidationSupport.requireValueOrChildren(location);
            }

            protected Builder from(Location location) {
                super.from(location);
                this.location = location.location;
                status = location.status;
                physicalType = location.physicalType;
                period = location.period;
                return this;
            }
        }
    }
}
