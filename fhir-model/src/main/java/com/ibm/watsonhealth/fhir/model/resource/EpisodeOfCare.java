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

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.EpisodeOfCareStatus;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * An association between a patient and an organization / healthcare provider(s) during which time encounters may occur. 
 * The managing organization assumes a level of responsibility for the patient during this time.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class EpisodeOfCare extends DomainResource {
    private final List<Identifier> identifier;
    private final EpisodeOfCareStatus status;
    private final List<StatusHistory> statusHistory;
    private final List<CodeableConcept> type;
    private final List<Diagnosis> diagnosis;
    private final Reference patient;
    private final Reference managingOrganization;
    private final Period period;
    private final List<Reference> referralRequest;
    private final Reference careManager;
    private final List<Reference> team;
    private final List<Reference> account;

    private volatile int hashCode;

    private EpisodeOfCare(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        statusHistory = Collections.unmodifiableList(builder.statusHistory);
        type = Collections.unmodifiableList(builder.type);
        diagnosis = Collections.unmodifiableList(builder.diagnosis);
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        managingOrganization = builder.managingOrganization;
        period = builder.period;
        referralRequest = Collections.unmodifiableList(builder.referralRequest);
        careManager = builder.careManager;
        team = Collections.unmodifiableList(builder.team);
        account = Collections.unmodifiableList(builder.account);
    }

    /**
     * <p>
     * The EpisodeOfCare may be known by different identifiers for different contexts of use, such as when an external agency 
     * is tracking the Episode for funding purposes.
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
     * planned | waitlist | active | onhold | finished | cancelled.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link EpisodeOfCareStatus}.
     */
    public EpisodeOfCareStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The history of statuses that the EpisodeOfCare has been through (without requiring processing the history of the 
     * resource).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link StatusHistory}.
     */
    public List<StatusHistory> getStatusHistory() {
        return statusHistory;
    }

    /**
     * <p>
     * A classification of the type of episode of care; e.g. specialist referral, disease management, type of funded care.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getType() {
        return type;
    }

    /**
     * <p>
     * The list of diagnosis relevant to this episode of care.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Diagnosis}.
     */
    public List<Diagnosis> getDiagnosis() {
        return diagnosis;
    }

    /**
     * <p>
     * The patient who is the focus of this episode of care.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * <p>
     * The organization that has assumed the specific responsibilities for the specified duration.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getManagingOrganization() {
        return managingOrganization;
    }

    /**
     * <p>
     * The interval during which the managing organization assumes the defined responsibility.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Period}.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * <p>
     * Referral Request(s) that are fulfilled by this EpisodeOfCare, incoming referrals.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReferralRequest() {
        return referralRequest;
    }

    /**
     * <p>
     * The practitioner that is the care manager/care coordinator for this patient.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getCareManager() {
        return careManager;
    }

    /**
     * <p>
     * The list of practitioners that may be facilitating this episode of care for specific purposes.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getTeam() {
        return team;
    }

    /**
     * <p>
     * The set of accounts that may be used for billing for this EpisodeOfCare.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAccount() {
        return account;
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
                accept(status, "status", visitor);
                accept(statusHistory, "statusHistory", visitor, StatusHistory.class);
                accept(type, "type", visitor, CodeableConcept.class);
                accept(diagnosis, "diagnosis", visitor, Diagnosis.class);
                accept(patient, "patient", visitor);
                accept(managingOrganization, "managingOrganization", visitor);
                accept(period, "period", visitor);
                accept(referralRequest, "referralRequest", visitor, Reference.class);
                accept(careManager, "careManager", visitor);
                accept(team, "team", visitor, Reference.class);
                accept(account, "account", visitor, Reference.class);
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
        EpisodeOfCare other = (EpisodeOfCare) obj;
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
            Objects.equals(type, other.type) && 
            Objects.equals(diagnosis, other.diagnosis) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(managingOrganization, other.managingOrganization) && 
            Objects.equals(period, other.period) && 
            Objects.equals(referralRequest, other.referralRequest) && 
            Objects.equals(careManager, other.careManager) && 
            Objects.equals(team, other.team) && 
            Objects.equals(account, other.account);
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
                type, 
                diagnosis, 
                patient, 
                managingOrganization, 
                period, 
                referralRequest, 
                careManager, 
                team, 
                account);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status, patient).from(this);
    }

    public Builder toBuilder(EpisodeOfCareStatus status, Reference patient) {
        return new Builder(status, patient).from(this);
    }

    public static Builder builder(EpisodeOfCareStatus status, Reference patient) {
        return new Builder(status, patient);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final EpisodeOfCareStatus status;
        private final Reference patient;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<StatusHistory> statusHistory = new ArrayList<>();
        private List<CodeableConcept> type = new ArrayList<>();
        private List<Diagnosis> diagnosis = new ArrayList<>();
        private Reference managingOrganization;
        private Period period;
        private List<Reference> referralRequest = new ArrayList<>();
        private Reference careManager;
        private List<Reference> team = new ArrayList<>();
        private List<Reference> account = new ArrayList<>();

        private Builder(EpisodeOfCareStatus status, Reference patient) {
            super();
            this.status = status;
            this.patient = patient;
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * Adds new element(s) to the existing list
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
         * The EpisodeOfCare may be known by different identifiers for different contexts of use, such as when an external agency 
         * is tracking the Episode for funding purposes.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param identifier
         *     Business Identifier(s) relevant for this EpisodeOfCare
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
         * The EpisodeOfCare may be known by different identifiers for different contexts of use, such as when an external agency 
         * is tracking the Episode for funding purposes.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Business Identifier(s) relevant for this EpisodeOfCare
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
         * The history of statuses that the EpisodeOfCare has been through (without requiring processing the history of the 
         * resource).
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param statusHistory
         *     Past list of status codes (the current status may be included to cover the start date of the status)
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
         * <p>
         * The history of statuses that the EpisodeOfCare has been through (without requiring processing the history of the 
         * resource).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param statusHistory
         *     Past list of status codes (the current status may be included to cover the start date of the status)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusHistory(Collection<StatusHistory> statusHistory) {
            this.statusHistory = new ArrayList<>(statusHistory);
            return this;
        }

        /**
         * <p>
         * A classification of the type of episode of care; e.g. specialist referral, disease management, type of funded care.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param type
         *     Type/class - e.g. specialist referral, disease management
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
         * <p>
         * A classification of the type of episode of care; e.g. specialist referral, disease management, type of funded care.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param type
         *     Type/class - e.g. specialist referral, disease management
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(Collection<CodeableConcept> type) {
            this.type = new ArrayList<>(type);
            return this;
        }

        /**
         * <p>
         * The list of diagnosis relevant to this episode of care.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param diagnosis
         *     The list of diagnosis relevant to this episode of care
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
         * <p>
         * The list of diagnosis relevant to this episode of care.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param diagnosis
         *     The list of diagnosis relevant to this episode of care
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder diagnosis(Collection<Diagnosis> diagnosis) {
            this.diagnosis = new ArrayList<>(diagnosis);
            return this;
        }

        /**
         * <p>
         * The organization that has assumed the specific responsibilities for the specified duration.
         * </p>
         * 
         * @param managingOrganization
         *     Organization that assumes care
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder managingOrganization(Reference managingOrganization) {
            this.managingOrganization = managingOrganization;
            return this;
        }

        /**
         * <p>
         * The interval during which the managing organization assumes the defined responsibility.
         * </p>
         * 
         * @param period
         *     Interval during responsibility is assumed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * <p>
         * Referral Request(s) that are fulfilled by this EpisodeOfCare, incoming referrals.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param referralRequest
         *     Originating Referral Request(s)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder referralRequest(Reference... referralRequest) {
            for (Reference value : referralRequest) {
                this.referralRequest.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Referral Request(s) that are fulfilled by this EpisodeOfCare, incoming referrals.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param referralRequest
         *     Originating Referral Request(s)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder referralRequest(Collection<Reference> referralRequest) {
            this.referralRequest = new ArrayList<>(referralRequest);
            return this;
        }

        /**
         * <p>
         * The practitioner that is the care manager/care coordinator for this patient.
         * </p>
         * 
         * @param careManager
         *     Care manager/care coordinator for the patient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder careManager(Reference careManager) {
            this.careManager = careManager;
            return this;
        }

        /**
         * <p>
         * The list of practitioners that may be facilitating this episode of care for specific purposes.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param team
         *     Other practitioners facilitating this episode of care
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder team(Reference... team) {
            for (Reference value : team) {
                this.team.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The list of practitioners that may be facilitating this episode of care for specific purposes.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param team
         *     Other practitioners facilitating this episode of care
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder team(Collection<Reference> team) {
            this.team = new ArrayList<>(team);
            return this;
        }

        /**
         * <p>
         * The set of accounts that may be used for billing for this EpisodeOfCare.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
         * 
         * @param account
         *     The set of accounts that may be used for billing for this EpisodeOfCare
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
         * <p>
         * The set of accounts that may be used for billing for this EpisodeOfCare.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param account
         *     The set of accounts that may be used for billing for this EpisodeOfCare
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder account(Collection<Reference> account) {
            this.account = new ArrayList<>(account);
            return this;
        }

        @Override
        public EpisodeOfCare build() {
            return new EpisodeOfCare(this);
        }

        private Builder from(EpisodeOfCare episodeOfCare) {
            id = episodeOfCare.id;
            meta = episodeOfCare.meta;
            implicitRules = episodeOfCare.implicitRules;
            language = episodeOfCare.language;
            text = episodeOfCare.text;
            contained.addAll(episodeOfCare.contained);
            extension.addAll(episodeOfCare.extension);
            modifierExtension.addAll(episodeOfCare.modifierExtension);
            identifier.addAll(episodeOfCare.identifier);
            statusHistory.addAll(episodeOfCare.statusHistory);
            type.addAll(episodeOfCare.type);
            diagnosis.addAll(episodeOfCare.diagnosis);
            managingOrganization = episodeOfCare.managingOrganization;
            period = episodeOfCare.period;
            referralRequest.addAll(episodeOfCare.referralRequest);
            careManager = episodeOfCare.careManager;
            team.addAll(episodeOfCare.team);
            account.addAll(episodeOfCare.account);
            return this;
        }
    }

    /**
     * <p>
     * The history of statuses that the EpisodeOfCare has been through (without requiring processing the history of the 
     * resource).
     * </p>
     */
    public static class StatusHistory extends BackboneElement {
        private final EpisodeOfCareStatus status;
        private final Period period;

        private volatile int hashCode;

        private StatusHistory(Builder builder) {
            super(builder);
            status = ValidationSupport.requireNonNull(builder.status, "status");
            period = ValidationSupport.requireNonNull(builder.period, "period");
        }

        /**
         * <p>
         * planned | waitlist | active | onhold | finished | cancelled.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link EpisodeOfCareStatus}.
         */
        public EpisodeOfCareStatus getStatus() {
            return status;
        }

        /**
         * <p>
         * The period during this EpisodeOfCare that the specific status applied.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getPeriod() {
            return period;
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
                    accept(status, "status", visitor);
                    accept(period, "period", visitor);
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
            return new Builder(status, period).from(this);
        }

        public Builder toBuilder(EpisodeOfCareStatus status, Period period) {
            return new Builder(status, period).from(this);
        }

        public static Builder builder(EpisodeOfCareStatus status, Period period) {
            return new Builder(status, period);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final EpisodeOfCareStatus status;
            private final Period period;

            private Builder(EpisodeOfCareStatus status, Period period) {
                super();
                this.status = status;
                this.period = period;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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

            @Override
            public StatusHistory build() {
                return new StatusHistory(this);
            }

            private Builder from(StatusHistory statusHistory) {
                id = statusHistory.id;
                extension.addAll(statusHistory.extension);
                modifierExtension.addAll(statusHistory.modifierExtension);
                return this;
            }
        }
    }

    /**
     * <p>
     * The list of diagnosis relevant to this episode of care.
     * </p>
     */
    public static class Diagnosis extends BackboneElement {
        private final Reference condition;
        private final CodeableConcept role;
        private final PositiveInt rank;

        private volatile int hashCode;

        private Diagnosis(Builder builder) {
            super(builder);
            condition = ValidationSupport.requireNonNull(builder.condition, "condition");
            role = builder.role;
            rank = builder.rank;
        }

        /**
         * <p>
         * A list of conditions/problems/diagnoses that this episode of care is intended to be providing care for.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getCondition() {
            return condition;
        }

        /**
         * <p>
         * Role that this diagnosis has within the episode of care (e.g. admission, billing, discharge …).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getRole() {
            return role;
        }

        /**
         * <p>
         * Ranking of the diagnosis (for each role type).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getRank() {
            return rank;
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
                    accept(condition, "condition", visitor);
                    accept(role, "role", visitor);
                    accept(rank, "rank", visitor);
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
            Diagnosis other = (Diagnosis) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(condition, other.condition) && 
                Objects.equals(role, other.role) && 
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
                    role, 
                    rank);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(condition).from(this);
        }

        public Builder toBuilder(Reference condition) {
            return new Builder(condition).from(this);
        }

        public static Builder builder(Reference condition) {
            return new Builder(condition);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference condition;

            // optional
            private CodeableConcept role;
            private PositiveInt rank;

            private Builder(Reference condition) {
                super();
                this.condition = condition;
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
             * Adds new element(s) to the existing list
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
             * Adds new element(s) to the existing list
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
             * Role that this diagnosis has within the episode of care (e.g. admission, billing, discharge …).
             * </p>
             * 
             * @param role
             *     Role that this diagnosis has within the episode of care (e.g. admission, billing, discharge …)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder role(CodeableConcept role) {
                this.role = role;
                return this;
            }

            /**
             * <p>
             * Ranking of the diagnosis (for each role type).
             * </p>
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

            @Override
            public Diagnosis build() {
                return new Diagnosis(this);
            }

            private Builder from(Diagnosis diagnosis) {
                id = diagnosis.id;
                extension.addAll(diagnosis.extension);
                modifierExtension.addAll(diagnosis.modifierExtension);
                role = diagnosis.role;
                rank = diagnosis.rank;
                return this;
            }
        }
    }
}
