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
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.ContactDetail;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RelatedArtifact;
import com.ibm.watsonhealth.fhir.model.type.ResearchStudyStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A process where a researcher or organization plans and then executes a series of steps intended to increase the field 
 * of healthcare-related knowledge. This includes studies of safety, efficacy, comparative effectiveness and other 
 * information about medications, devices, therapies and other interventional and investigative techniques. A 
 * ResearchStudy involves the gathering of information about human or animal subjects.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ResearchStudy extends DomainResource {
    private final List<Identifier> identifier;
    private final String title;
    private final List<Reference> protocol;
    private final List<Reference> partOf;
    private final ResearchStudyStatus status;
    private final CodeableConcept primaryPurposeType;
    private final CodeableConcept phase;
    private final List<CodeableConcept> category;
    private final List<CodeableConcept> focus;
    private final List<CodeableConcept> condition;
    private final List<ContactDetail> contact;
    private final List<RelatedArtifact> relatedArtifact;
    private final List<CodeableConcept> keyword;
    private final List<CodeableConcept> location;
    private final Markdown description;
    private final List<Reference> enrollment;
    private final Period period;
    private final Reference sponsor;
    private final Reference principalInvestigator;
    private final List<Reference> site;
    private final CodeableConcept reasonStopped;
    private final List<Annotation> note;
    private final List<Arm> arm;
    private final List<Objective> objective;

    private volatile int hashCode;

    private ResearchStudy(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        title = builder.title;
        protocol = Collections.unmodifiableList(builder.protocol);
        partOf = Collections.unmodifiableList(builder.partOf);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        primaryPurposeType = builder.primaryPurposeType;
        phase = builder.phase;
        category = Collections.unmodifiableList(builder.category);
        focus = Collections.unmodifiableList(builder.focus);
        condition = Collections.unmodifiableList(builder.condition);
        contact = Collections.unmodifiableList(builder.contact);
        relatedArtifact = Collections.unmodifiableList(builder.relatedArtifact);
        keyword = Collections.unmodifiableList(builder.keyword);
        location = Collections.unmodifiableList(builder.location);
        description = builder.description;
        enrollment = Collections.unmodifiableList(builder.enrollment);
        period = builder.period;
        sponsor = builder.sponsor;
        principalInvestigator = builder.principalInvestigator;
        site = Collections.unmodifiableList(builder.site);
        reasonStopped = builder.reasonStopped;
        note = Collections.unmodifiableList(builder.note);
        arm = Collections.unmodifiableList(builder.arm);
        objective = Collections.unmodifiableList(builder.objective);
    }

    /**
     * <p>
     * Identifiers assigned to this research study by the sponsor or other systems.
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
     * A short, descriptive user-friendly label for the study.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>
     * The set of steps expected to be performed as part of the execution of the study.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getProtocol() {
        return protocol;
    }

    /**
     * <p>
     * A larger research study of which this particular study is a component or step.
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
     * The current state of the study.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ResearchStudyStatus}.
     */
    public ResearchStudyStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * The type of study based upon the intent of the study's activities. A classification of the intent of the study.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getPrimaryPurposeType() {
        return primaryPurposeType;
    }

    /**
     * <p>
     * The stage in the progression of a therapy from initial experimental use in humans in clinical trials to post-market 
     * evaluation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getPhase() {
        return phase;
    }

    /**
     * <p>
     * Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of 
     * randomization, safety vs. efficacy, etc.
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
     * The medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to 
     * gain more information about.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getFocus() {
        return focus;
    }

    /**
     * <p>
     * The condition that is the focus of the study. For example, In a study to examine risk factors for Lupus, might have as 
     * an inclusion criterion "healthy volunteer", but the target condition code would be a Lupus SNOMED code.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCondition() {
        return condition;
    }

    /**
     * <p>
     * Contact details to assist a user in learning more about or engaging with the study.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ContactDetail}.
     */
    public List<ContactDetail> getContact() {
        return contact;
    }

    /**
     * <p>
     * Citations, references and other related documents.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link RelatedArtifact}.
     */
    public List<RelatedArtifact> getRelatedArtifact() {
        return relatedArtifact;
    }

    /**
     * <p>
     * Key terms to aid in searching for or filtering the study.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getKeyword() {
        return keyword;
    }

    /**
     * <p>
     * Indicates a country, state or other region where the study is taking place.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getLocation() {
        return location;
    }

    /**
     * <p>
     * A full description of how the study is being conducted.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Markdown}.
     */
    public Markdown getDescription() {
        return description;
    }

    /**
     * <p>
     * Reference to a Group that defines the criteria for and quantity of subjects participating in the study. E.g. " 200 
     * female Europeans between the ages of 20 and 45 with early onset diabetes".
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getEnrollment() {
        return enrollment;
    }

    /**
     * <p>
     * Identifies the start date and the expected (or actual, depending on status) end date for the study.
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
     * An organization that initiates the investigation and is legally responsible for the study.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSponsor() {
        return sponsor;
    }

    /**
     * <p>
     * A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, 
     * protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, 
     * interpretation and presentation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPrincipalInvestigator() {
        return principalInvestigator;
    }

    /**
     * <p>
     * A facility in which study activities are conducted.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSite() {
        return site;
    }

    /**
     * <p>
     * A description and/or code explaining the premature termination of the study.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getReasonStopped() {
        return reasonStopped;
    }

    /**
     * <p>
     * Comments made about the study by the performer, subject or other participants.
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
     * Describes an expected sequence of events for one of the participants of a study. E.g. Exposure to drug A, wash-out, 
     * exposure to drug B, wash-out, follow-up.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Arm}.
     */
    public List<Arm> getArm() {
        return arm;
    }

    /**
     * <p>
     * A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data 
     * collected during the study.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Objective}.
     */
    public List<Objective> getObjective() {
        return objective;
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
                accept(title, "title", visitor);
                accept(protocol, "protocol", visitor, Reference.class);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(primaryPurposeType, "primaryPurposeType", visitor);
                accept(phase, "phase", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(focus, "focus", visitor, CodeableConcept.class);
                accept(condition, "condition", visitor, CodeableConcept.class);
                accept(contact, "contact", visitor, ContactDetail.class);
                accept(relatedArtifact, "relatedArtifact", visitor, RelatedArtifact.class);
                accept(keyword, "keyword", visitor, CodeableConcept.class);
                accept(location, "location", visitor, CodeableConcept.class);
                accept(description, "description", visitor);
                accept(enrollment, "enrollment", visitor, Reference.class);
                accept(period, "period", visitor);
                accept(sponsor, "sponsor", visitor);
                accept(principalInvestigator, "principalInvestigator", visitor);
                accept(site, "site", visitor, Reference.class);
                accept(reasonStopped, "reasonStopped", visitor);
                accept(note, "note", visitor, Annotation.class);
                accept(arm, "arm", visitor, Arm.class);
                accept(objective, "objective", visitor, Objective.class);
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
        ResearchStudy other = (ResearchStudy) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(title, other.title) && 
            Objects.equals(protocol, other.protocol) && 
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(status, other.status) && 
            Objects.equals(primaryPurposeType, other.primaryPurposeType) && 
            Objects.equals(phase, other.phase) && 
            Objects.equals(category, other.category) && 
            Objects.equals(focus, other.focus) && 
            Objects.equals(condition, other.condition) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(relatedArtifact, other.relatedArtifact) && 
            Objects.equals(keyword, other.keyword) && 
            Objects.equals(location, other.location) && 
            Objects.equals(description, other.description) && 
            Objects.equals(enrollment, other.enrollment) && 
            Objects.equals(period, other.period) && 
            Objects.equals(sponsor, other.sponsor) && 
            Objects.equals(principalInvestigator, other.principalInvestigator) && 
            Objects.equals(site, other.site) && 
            Objects.equals(reasonStopped, other.reasonStopped) && 
            Objects.equals(note, other.note) && 
            Objects.equals(arm, other.arm) && 
            Objects.equals(objective, other.objective);
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
                title, 
                protocol, 
                partOf, 
                status, 
                primaryPurposeType, 
                phase, 
                category, 
                focus, 
                condition, 
                contact, 
                relatedArtifact, 
                keyword, 
                location, 
                description, 
                enrollment, 
                period, 
                sponsor, 
                principalInvestigator, 
                site, 
                reasonStopped, 
                note, 
                arm, 
                objective);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status).from(this);
    }

    public Builder toBuilder(ResearchStudyStatus status) {
        return new Builder(status).from(this);
    }

    public static Builder builder(ResearchStudyStatus status) {
        return new Builder(status);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ResearchStudyStatus status;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private String title;
        private List<Reference> protocol = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private CodeableConcept primaryPurposeType;
        private CodeableConcept phase;
        private List<CodeableConcept> category = new ArrayList<>();
        private List<CodeableConcept> focus = new ArrayList<>();
        private List<CodeableConcept> condition = new ArrayList<>();
        private List<ContactDetail> contact = new ArrayList<>();
        private List<RelatedArtifact> relatedArtifact = new ArrayList<>();
        private List<CodeableConcept> keyword = new ArrayList<>();
        private List<CodeableConcept> location = new ArrayList<>();
        private Markdown description;
        private List<Reference> enrollment = new ArrayList<>();
        private Period period;
        private Reference sponsor;
        private Reference principalInvestigator;
        private List<Reference> site = new ArrayList<>();
        private CodeableConcept reasonStopped;
        private List<Annotation> note = new ArrayList<>();
        private List<Arm> arm = new ArrayList<>();
        private List<Objective> objective = new ArrayList<>();

        private Builder(ResearchStudyStatus status) {
            super();
            this.status = status;
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
         * Identifiers assigned to this research study by the sponsor or other systems.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for study
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
         * Identifiers assigned to this research study by the sponsor or other systems.
         * </p>
         * 
         * @param identifier
         *     Business Identifier for study
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
         * A short, descriptive user-friendly label for the study.
         * </p>
         * 
         * @param title
         *     Name for this study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * <p>
         * The set of steps expected to be performed as part of the execution of the study.
         * </p>
         * 
         * @param protocol
         *     Steps followed in executing study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder protocol(Reference... protocol) {
            for (Reference value : protocol) {
                this.protocol.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The set of steps expected to be performed as part of the execution of the study.
         * </p>
         * 
         * @param protocol
         *     Steps followed in executing study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder protocol(Collection<Reference> protocol) {
            this.protocol.addAll(protocol);
            return this;
        }

        /**
         * <p>
         * A larger research study of which this particular study is a component or step.
         * </p>
         * 
         * @param partOf
         *     Part of larger study
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
         * A larger research study of which this particular study is a component or step.
         * </p>
         * 
         * @param partOf
         *     Part of larger study
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
         * The type of study based upon the intent of the study's activities. A classification of the intent of the study.
         * </p>
         * 
         * @param primaryPurposeType
         *     treatment | prevention | diagnostic | supportive-care | screening | health-services-research | basic-science | device-
         *     feasibility
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder primaryPurposeType(CodeableConcept primaryPurposeType) {
            this.primaryPurposeType = primaryPurposeType;
            return this;
        }

        /**
         * <p>
         * The stage in the progression of a therapy from initial experimental use in humans in clinical trials to post-market 
         * evaluation.
         * </p>
         * 
         * @param phase
         *     n-a | early-phase-1 | phase-1 | phase-1-phase-2 | phase-2 | phase-2-phase-3 | phase-3 | phase-4
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder phase(CodeableConcept phase) {
            this.phase = phase;
            return this;
        }

        /**
         * <p>
         * Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of 
         * randomization, safety vs. efficacy, etc.
         * </p>
         * 
         * @param category
         *     Classifications for the study
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
         * Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of 
         * randomization, safety vs. efficacy, etc.
         * </p>
         * 
         * @param category
         *     Classifications for the study
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
         * The medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to 
         * gain more information about.
         * </p>
         * 
         * @param focus
         *     Drugs, devices, etc. under study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder focus(CodeableConcept... focus) {
            for (CodeableConcept value : focus) {
                this.focus.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to 
         * gain more information about.
         * </p>
         * 
         * @param focus
         *     Drugs, devices, etc. under study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder focus(Collection<CodeableConcept> focus) {
            this.focus.addAll(focus);
            return this;
        }

        /**
         * <p>
         * The condition that is the focus of the study. For example, In a study to examine risk factors for Lupus, might have as 
         * an inclusion criterion "healthy volunteer", but the target condition code would be a Lupus SNOMED code.
         * </p>
         * 
         * @param condition
         *     Condition being studied
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder condition(CodeableConcept... condition) {
            for (CodeableConcept value : condition) {
                this.condition.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The condition that is the focus of the study. For example, In a study to examine risk factors for Lupus, might have as 
         * an inclusion criterion "healthy volunteer", but the target condition code would be a Lupus SNOMED code.
         * </p>
         * 
         * @param condition
         *     Condition being studied
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder condition(Collection<CodeableConcept> condition) {
            this.condition.addAll(condition);
            return this;
        }

        /**
         * <p>
         * Contact details to assist a user in learning more about or engaging with the study.
         * </p>
         * 
         * @param contact
         *     Contact details for the study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(ContactDetail... contact) {
            for (ContactDetail value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Contact details to assist a user in learning more about or engaging with the study.
         * </p>
         * 
         * @param contact
         *     Contact details for the study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(Collection<ContactDetail> contact) {
            this.contact.addAll(contact);
            return this;
        }

        /**
         * <p>
         * Citations, references and other related documents.
         * </p>
         * 
         * @param relatedArtifact
         *     References and dependencies
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relatedArtifact(RelatedArtifact... relatedArtifact) {
            for (RelatedArtifact value : relatedArtifact) {
                this.relatedArtifact.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Citations, references and other related documents.
         * </p>
         * 
         * @param relatedArtifact
         *     References and dependencies
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relatedArtifact(Collection<RelatedArtifact> relatedArtifact) {
            this.relatedArtifact.addAll(relatedArtifact);
            return this;
        }

        /**
         * <p>
         * Key terms to aid in searching for or filtering the study.
         * </p>
         * 
         * @param keyword
         *     Used to search for the study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder keyword(CodeableConcept... keyword) {
            for (CodeableConcept value : keyword) {
                this.keyword.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Key terms to aid in searching for or filtering the study.
         * </p>
         * 
         * @param keyword
         *     Used to search for the study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder keyword(Collection<CodeableConcept> keyword) {
            this.keyword.addAll(keyword);
            return this;
        }

        /**
         * <p>
         * Indicates a country, state or other region where the study is taking place.
         * </p>
         * 
         * @param location
         *     Geographic region(s) for study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder location(CodeableConcept... location) {
            for (CodeableConcept value : location) {
                this.location.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates a country, state or other region where the study is taking place.
         * </p>
         * 
         * @param location
         *     Geographic region(s) for study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder location(Collection<CodeableConcept> location) {
            this.location.addAll(location);
            return this;
        }

        /**
         * <p>
         * A full description of how the study is being conducted.
         * </p>
         * 
         * @param description
         *     What this is study doing
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder description(Markdown description) {
            this.description = description;
            return this;
        }

        /**
         * <p>
         * Reference to a Group that defines the criteria for and quantity of subjects participating in the study. E.g. " 200 
         * female Europeans between the ages of 20 and 45 with early onset diabetes".
         * </p>
         * 
         * @param enrollment
         *     Inclusion &amp; exclusion criteria
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder enrollment(Reference... enrollment) {
            for (Reference value : enrollment) {
                this.enrollment.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Reference to a Group that defines the criteria for and quantity of subjects participating in the study. E.g. " 200 
         * female Europeans between the ages of 20 and 45 with early onset diabetes".
         * </p>
         * 
         * @param enrollment
         *     Inclusion &amp; exclusion criteria
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder enrollment(Collection<Reference> enrollment) {
            this.enrollment.addAll(enrollment);
            return this;
        }

        /**
         * <p>
         * Identifies the start date and the expected (or actual, depending on status) end date for the study.
         * </p>
         * 
         * @param period
         *     When the study began and ended
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
         * An organization that initiates the investigation and is legally responsible for the study.
         * </p>
         * 
         * @param sponsor
         *     Organization that initiates and is legally responsible for the study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder sponsor(Reference sponsor) {
            this.sponsor = sponsor;
            return this;
        }

        /**
         * <p>
         * A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, 
         * protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, 
         * interpretation and presentation.
         * </p>
         * 
         * @param principalInvestigator
         *     Researcher who oversees multiple aspects of the study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder principalInvestigator(Reference principalInvestigator) {
            this.principalInvestigator = principalInvestigator;
            return this;
        }

        /**
         * <p>
         * A facility in which study activities are conducted.
         * </p>
         * 
         * @param site
         *     Facility where study activities are conducted
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder site(Reference... site) {
            for (Reference value : site) {
                this.site.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A facility in which study activities are conducted.
         * </p>
         * 
         * @param site
         *     Facility where study activities are conducted
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder site(Collection<Reference> site) {
            this.site.addAll(site);
            return this;
        }

        /**
         * <p>
         * A description and/or code explaining the premature termination of the study.
         * </p>
         * 
         * @param reasonStopped
         *     accrual-goal-met | closed-due-to-toxicity | closed-due-to-lack-of-study-progress | temporarily-closed-per-study-design
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonStopped(CodeableConcept reasonStopped) {
            this.reasonStopped = reasonStopped;
            return this;
        }

        /**
         * <p>
         * Comments made about the study by the performer, subject or other participants.
         * </p>
         * 
         * @param note
         *     Comments made about the study
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
         * Comments made about the study by the performer, subject or other participants.
         * </p>
         * 
         * @param note
         *     Comments made about the study
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
         * Describes an expected sequence of events for one of the participants of a study. E.g. Exposure to drug A, wash-out, 
         * exposure to drug B, wash-out, follow-up.
         * </p>
         * 
         * @param arm
         *     Defined path through the study for a subject
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder arm(Arm... arm) {
            for (Arm value : arm) {
                this.arm.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Describes an expected sequence of events for one of the participants of a study. E.g. Exposure to drug A, wash-out, 
         * exposure to drug B, wash-out, follow-up.
         * </p>
         * 
         * @param arm
         *     Defined path through the study for a subject
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder arm(Collection<Arm> arm) {
            this.arm.addAll(arm);
            return this;
        }

        /**
         * <p>
         * A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data 
         * collected during the study.
         * </p>
         * 
         * @param objective
         *     A goal for the study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder objective(Objective... objective) {
            for (Objective value : objective) {
                this.objective.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data 
         * collected during the study.
         * </p>
         * 
         * @param objective
         *     A goal for the study
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder objective(Collection<Objective> objective) {
            this.objective.addAll(objective);
            return this;
        }

        @Override
        public ResearchStudy build() {
            return new ResearchStudy(this);
        }

        private Builder from(ResearchStudy researchStudy) {
            id = researchStudy.id;
            meta = researchStudy.meta;
            implicitRules = researchStudy.implicitRules;
            language = researchStudy.language;
            text = researchStudy.text;
            contained.addAll(researchStudy.contained);
            extension.addAll(researchStudy.extension);
            modifierExtension.addAll(researchStudy.modifierExtension);
            identifier.addAll(researchStudy.identifier);
            title = researchStudy.title;
            protocol.addAll(researchStudy.protocol);
            partOf.addAll(researchStudy.partOf);
            primaryPurposeType = researchStudy.primaryPurposeType;
            phase = researchStudy.phase;
            category.addAll(researchStudy.category);
            focus.addAll(researchStudy.focus);
            condition.addAll(researchStudy.condition);
            contact.addAll(researchStudy.contact);
            relatedArtifact.addAll(researchStudy.relatedArtifact);
            keyword.addAll(researchStudy.keyword);
            location.addAll(researchStudy.location);
            description = researchStudy.description;
            enrollment.addAll(researchStudy.enrollment);
            period = researchStudy.period;
            sponsor = researchStudy.sponsor;
            principalInvestigator = researchStudy.principalInvestigator;
            site.addAll(researchStudy.site);
            reasonStopped = researchStudy.reasonStopped;
            note.addAll(researchStudy.note);
            arm.addAll(researchStudy.arm);
            objective.addAll(researchStudy.objective);
            return this;
        }
    }

    /**
     * <p>
     * Describes an expected sequence of events for one of the participants of a study. E.g. Exposure to drug A, wash-out, 
     * exposure to drug B, wash-out, follow-up.
     * </p>
     */
    public static class Arm extends BackboneElement {
        private final String name;
        private final CodeableConcept type;
        private final String description;

        private volatile int hashCode;

        private Arm(Builder builder) {
            super(builder);
            name = ValidationSupport.requireNonNull(builder.name, "name");
            type = builder.type;
            description = builder.description;
        }

        /**
         * <p>
         * Unique, human-readable label for this arm of the study.
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
         * Categorization of study arm, e.g. experimental, active comparator, placebo comparater.
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
         * A succinct description of the path through the study that would be followed by a subject adhering to this arm.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDescription() {
            return description;
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
                    accept(name, "name", visitor);
                    accept(type, "type", visitor);
                    accept(description, "description", visitor);
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
            Arm other = (Arm) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(type, other.type) && 
                Objects.equals(description, other.description);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    type, 
                    description);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(name).from(this);
        }

        public Builder toBuilder(String name) {
            return new Builder(name).from(this);
        }

        public static Builder builder(String name) {
            return new Builder(name);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final String name;

            // optional
            private CodeableConcept type;
            private String description;

            private Builder(String name) {
                super();
                this.name = name;
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
             * Categorization of study arm, e.g. experimental, active comparator, placebo comparater.
             * </p>
             * 
             * @param type
             *     Categorization of study arm
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
             * A succinct description of the path through the study that would be followed by a subject adhering to this arm.
             * </p>
             * 
             * @param description
             *     Short explanation of study path
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            @Override
            public Arm build() {
                return new Arm(this);
            }

            private Builder from(Arm arm) {
                id = arm.id;
                extension.addAll(arm.extension);
                modifierExtension.addAll(arm.modifierExtension);
                type = arm.type;
                description = arm.description;
                return this;
            }
        }
    }

    /**
     * <p>
     * A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data 
     * collected during the study.
     * </p>
     */
    public static class Objective extends BackboneElement {
        private final String name;
        private final CodeableConcept type;

        private volatile int hashCode;

        private Objective(Builder builder) {
            super(builder);
            name = builder.name;
            type = builder.type;
        }

        /**
         * <p>
         * Unique, human-readable label for this objective of the study.
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
         * The kind of study objective.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getType() {
            return type;
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
                    accept(name, "name", visitor);
                    accept(type, "type", visitor);
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
            Objective other = (Objective) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(type, other.type);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    type);
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
            // optional
            private String name;
            private CodeableConcept type;

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
             * Unique, human-readable label for this objective of the study.
             * </p>
             * 
             * @param name
             *     Label for the objective
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
             * The kind of study objective.
             * </p>
             * 
             * @param type
             *     primary | secondary | exploratory
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            @Override
            public Objective build() {
                return new Objective(this);
            }

            private Builder from(Objective objective) {
                id = objective.id;
                extension.addAll(objective.extension);
                modifierExtension.addAll(objective.modifierExtension);
                name = objective.name;
                type = objective.type;
                return this;
            }
        }
    }
}
