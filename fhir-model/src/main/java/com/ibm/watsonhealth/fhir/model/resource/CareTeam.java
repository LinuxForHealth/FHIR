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
import com.ibm.watsonhealth.fhir.model.type.CareTeamStatus;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of 
 * care for a patient.
 * </p>
 */
@Constraint(
    id = "ctm-1",
    level = "Rule",
    location = "CareTeam.participant",
    description = "CareTeam.participant.onBehalfOf can only be populated when CareTeam.participant.member is a Practitioner",
    expression = "onBehalfOf.exists() implies (member.resolve() is Practitioner)"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class CareTeam extends DomainResource {
    private final List<Identifier> identifier;
    private final CareTeamStatus status;
    private final List<CodeableConcept> category;
    private final String name;
    private final Reference subject;
    private final Reference encounter;
    private final Period period;
    private final List<Participant> participant;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final List<Reference> managingOrganization;
    private final List<ContactPoint> telecom;
    private final List<Annotation> note;

    private CareTeam(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        category = Collections.unmodifiableList(builder.category);
        name = builder.name;
        subject = builder.subject;
        encounter = builder.encounter;
        period = builder.period;
        participant = Collections.unmodifiableList(builder.participant);
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        managingOrganization = Collections.unmodifiableList(builder.managingOrganization);
        telecom = Collections.unmodifiableList(builder.telecom);
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * <p>
     * Business identifiers assigned to this care team by the performer or other systems which remain constant as the 
     * resource is updated and propagates from server to server.
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
     * Indicates the current state of the care team.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CareTeamStatus}.
     */
    public CareTeamStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Identifies what kind of team. This is to support differentiation between multiple co-existing teams, such as care plan 
     * team, episode of care team, longitudinal care team.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * <p>
     * A label for human use intended to distinguish like teams. E.g. the "red" vs. "green" trauma teams.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Identifies the patient or group whose intended care is handled by the team.
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
     * The Encounter during which this CareTeam was created or to which the creation of this record is tightly associated.
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
     * Indicates when the team did (or is intended to) come into effect and end.
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
     * Identifies all people and organizations who are expected to be involved in the care team.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Participant}.
     */
    public List<Participant> getParticipant() {
        return participant;
    }

    /**
     * <p>
     * Describes why the care team exists.
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
     * Condition(s) that this care team addresses.
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
     * The organization responsible for the care team.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getManagingOrganization() {
        return managingOrganization;
    }

    /**
     * <p>
     * A central contact detail for the care team (that applies to all members).
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ContactPoint}.
     */
    public List<ContactPoint> getTelecom() {
        return telecom;
    }

    /**
     * <p>
     * Comments made about the CareTeam.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
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
                accept(category, "category", visitor, CodeableConcept.class);
                accept(name, "name", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(period, "period", visitor);
                accept(participant, "participant", visitor, Participant.class);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(managingOrganization, "managingOrganization", visitor, Reference.class);
                accept(telecom, "telecom", visitor, ContactPoint.class);
                accept(note, "note", visitor, Annotation.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.status = status;
        builder.category.addAll(category);
        builder.name = name;
        builder.subject = subject;
        builder.encounter = encounter;
        builder.period = period;
        builder.participant.addAll(participant);
        builder.reasonCode.addAll(reasonCode);
        builder.reasonReference.addAll(reasonReference);
        builder.managingOrganization.addAll(managingOrganization);
        builder.telecom.addAll(telecom);
        builder.note.addAll(note);
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private CareTeamStatus status;
        private List<CodeableConcept> category = new ArrayList<>();
        private String name;
        private Reference subject;
        private Reference encounter;
        private Period period;
        private List<Participant> participant = new ArrayList<>();
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Reference> managingOrganization = new ArrayList<>();
        private List<ContactPoint> telecom = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();

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
         * Business identifiers assigned to this care team by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     External Ids for this team
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
         * Business identifiers assigned to this care team by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     External Ids for this team
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
         * Indicates the current state of the care team.
         * </p>
         * 
         * @param status
         *     proposed | active | suspended | inactive | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder status(CareTeamStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * Identifies what kind of team. This is to support differentiation between multiple co-existing teams, such as care plan 
         * team, episode of care team, longitudinal care team.
         * </p>
         * 
         * @param category
         *     Type of team
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies what kind of team. This is to support differentiation between multiple co-existing teams, such as care plan 
         * team, episode of care team, longitudinal care team.
         * </p>
         * 
         * @param category
         *     Type of team
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category.addAll(category);
            return this;
        }

        /**
         * <p>
         * A label for human use intended to distinguish like teams. E.g. the "red" vs. "green" trauma teams.
         * </p>
         * 
         * @param name
         *     Name of the team, such as crisis assessment team
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * <p>
         * Identifies the patient or group whose intended care is handled by the team.
         * </p>
         * 
         * @param subject
         *     Who care team is for
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
         * The Encounter during which this CareTeam was created or to which the creation of this record is tightly associated.
         * </p>
         * 
         * @param encounter
         *     Encounter created as part of
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * <p>
         * Indicates when the team did (or is intended to) come into effect and end.
         * </p>
         * 
         * @param period
         *     Time period team covers
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * <p>
         * Identifies all people and organizations who are expected to be involved in the care team.
         * </p>
         * 
         * @param participant
         *     Members of the team
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder participant(Participant... participant) {
            for (Participant value : participant) {
                this.participant.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies all people and organizations who are expected to be involved in the care team.
         * </p>
         * 
         * @param participant
         *     Members of the team
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder participant(Collection<Participant> participant) {
            this.participant.addAll(participant);
            return this;
        }

        /**
         * <p>
         * Describes why the care team exists.
         * </p>
         * 
         * @param reasonCode
         *     Why the care team exists
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
         * Describes why the care team exists.
         * </p>
         * 
         * @param reasonCode
         *     Why the care team exists
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
         * Condition(s) that this care team addresses.
         * </p>
         * 
         * @param reasonReference
         *     Why the care team exists
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
         * Condition(s) that this care team addresses.
         * </p>
         * 
         * @param reasonReference
         *     Why the care team exists
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
         * The organization responsible for the care team.
         * </p>
         * 
         * @param managingOrganization
         *     Organization responsible for the care team
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder managingOrganization(Reference... managingOrganization) {
            for (Reference value : managingOrganization) {
                this.managingOrganization.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The organization responsible for the care team.
         * </p>
         * 
         * @param managingOrganization
         *     Organization responsible for the care team
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder managingOrganization(Collection<Reference> managingOrganization) {
            this.managingOrganization.addAll(managingOrganization);
            return this;
        }

        /**
         * <p>
         * A central contact detail for the care team (that applies to all members).
         * </p>
         * 
         * @param telecom
         *     A contact detail for the care team (that applies to all members)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder telecom(ContactPoint... telecom) {
            for (ContactPoint value : telecom) {
                this.telecom.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A central contact detail for the care team (that applies to all members).
         * </p>
         * 
         * @param telecom
         *     A contact detail for the care team (that applies to all members)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder telecom(Collection<ContactPoint> telecom) {
            this.telecom.addAll(telecom);
            return this;
        }

        /**
         * <p>
         * Comments made about the CareTeam.
         * </p>
         * 
         * @param note
         *     Comments made about the CareTeam
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
         * Comments made about the CareTeam.
         * </p>
         * 
         * @param note
         *     Comments made about the CareTeam
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        @Override
        public CareTeam build() {
            return new CareTeam(this);
        }
    }

    /**
     * <p>
     * Identifies all people and organizations who are expected to be involved in the care team.
     * </p>
     */
    public static class Participant extends BackboneElement {
        private final List<CodeableConcept> role;
        private final Reference member;
        private final Reference onBehalfOf;
        private final Period period;

        private Participant(Builder builder) {
            super(builder);
            role = Collections.unmodifiableList(builder.role);
            member = builder.member;
            onBehalfOf = builder.onBehalfOf;
            period = builder.period;
        }

        /**
         * <p>
         * Indicates specific responsibility of an individual within the care team, such as "Primary care physician", "Trained 
         * social worker counselor", "Caregiver", etc.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getRole() {
            return role;
        }

        /**
         * <p>
         * The specific person or organization who is participating/expected to participate in the care team.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getMember() {
            return member;
        }

        /**
         * <p>
         * The organization of the practitioner.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getOnBehalfOf() {
            return onBehalfOf;
        }

        /**
         * <p>
         * Indicates when the specific member or organization did (or is intended to) come into effect and end.
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
                    accept(role, "role", visitor, CodeableConcept.class);
                    accept(member, "member", visitor);
                    accept(onBehalfOf, "onBehalfOf", visitor);
                    accept(period, "period", visitor);
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
            private List<CodeableConcept> role = new ArrayList<>();
            private Reference member;
            private Reference onBehalfOf;
            private Period period;

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
             * Indicates specific responsibility of an individual within the care team, such as "Primary care physician", "Trained 
             * social worker counselor", "Caregiver", etc.
             * </p>
             * 
             * @param role
             *     Type of involvement
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder role(CodeableConcept... role) {
                for (CodeableConcept value : role) {
                    this.role.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Indicates specific responsibility of an individual within the care team, such as "Primary care physician", "Trained 
             * social worker counselor", "Caregiver", etc.
             * </p>
             * 
             * @param role
             *     Type of involvement
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder role(Collection<CodeableConcept> role) {
                this.role.addAll(role);
                return this;
            }

            /**
             * <p>
             * The specific person or organization who is participating/expected to participate in the care team.
             * </p>
             * 
             * @param member
             *     Who is involved
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder member(Reference member) {
                this.member = member;
                return this;
            }

            /**
             * <p>
             * The organization of the practitioner.
             * </p>
             * 
             * @param onBehalfOf
             *     Organization of the practitioner
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder onBehalfOf(Reference onBehalfOf) {
                this.onBehalfOf = onBehalfOf;
                return this;
            }

            /**
             * <p>
             * Indicates when the specific member or organization did (or is intended to) come into effect and end.
             * </p>
             * 
             * @param period
             *     Time period of participant
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            @Override
            public Participant build() {
                return new Participant(this);
            }

            private static Builder from(Participant participant) {
                Builder builder = new Builder();
                builder.role.addAll(participant.role);
                builder.member = participant.member;
                builder.onBehalfOf = participant.onBehalfOf;
                builder.period = participant.period;
                return builder;
            }
        }
    }
}
