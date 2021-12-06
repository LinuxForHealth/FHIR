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
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ResearchSubjectStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A physical entity which is the primary unit of operational and/or administrative interest in a study.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ResearchSubject extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "ResearchSubjectStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "Indicates the progression of a study subject through a study.",
        valueSet = "http://hl7.org/fhir/ValueSet/research-subject-status|4.1.0"
    )
    @Required
    private final ResearchSubjectStatus status;
    @Summary
    private final Period period;
    @Summary
    @ReferenceTarget({ "ResearchStudy" })
    @Required
    private final Reference study;
    @Summary
    @ReferenceTarget({ "Patient" })
    @Required
    private final Reference individual;
    private final String assignedArm;
    private final String actualArm;
    @ReferenceTarget({ "Consent" })
    private final Reference consent;

    private ResearchSubject(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        status = builder.status;
        period = builder.period;
        study = builder.study;
        individual = builder.individual;
        assignedArm = builder.assignedArm;
        actualArm = builder.actualArm;
        consent = builder.consent;
    }

    /**
     * Identifiers assigned to this research subject for a study.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The current state of the subject.
     * 
     * @return
     *     An immutable object of type {@link ResearchSubjectStatus} that is non-null.
     */
    public ResearchSubjectStatus getStatus() {
        return status;
    }

    /**
     * The dates the subject began and ended their participation in the study.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Reference to the study the subject is participating in.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getStudy() {
        return study;
    }

    /**
     * The record of the person or animal who is involved in the study.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getIndividual() {
        return individual;
    }

    /**
     * The name of the arm in the study the subject is expected to follow as part of this study.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getAssignedArm() {
        return assignedArm;
    }

    /**
     * The name of the arm in the study the subject actually followed as part of this study.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getActualArm() {
        return actualArm;
    }

    /**
     * A record of the patient's informed agreement to participate in the study.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getConsent() {
        return consent;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (period != null) || 
            (study != null) || 
            (individual != null) || 
            (assignedArm != null) || 
            (actualArm != null) || 
            (consent != null);
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
                accept(period, "period", visitor);
                accept(study, "study", visitor);
                accept(individual, "individual", visitor);
                accept(assignedArm, "assignedArm", visitor);
                accept(actualArm, "actualArm", visitor);
                accept(consent, "consent", visitor);
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
        ResearchSubject other = (ResearchSubject) obj;
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
            Objects.equals(period, other.period) && 
            Objects.equals(study, other.study) && 
            Objects.equals(individual, other.individual) && 
            Objects.equals(assignedArm, other.assignedArm) && 
            Objects.equals(actualArm, other.actualArm) && 
            Objects.equals(consent, other.consent);
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
                period, 
                study, 
                individual, 
                assignedArm, 
                actualArm, 
                consent);
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
        private ResearchSubjectStatus status;
        private Period period;
        private Reference study;
        private Reference individual;
        private String assignedArm;
        private String actualArm;
        private Reference consent;

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
         * Identifiers assigned to this research subject for a study.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for research subject in a study
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
         * Identifiers assigned to this research subject for a study.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Business Identifier for research subject in a study
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
         * The current state of the subject.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     candidate | eligible | follow-up | ineligible | not-registered | off-study | on-study | on-study-intervention | on-
         *     study-observation | pending-on-study | potential-candidate | screening | withdrawn
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(ResearchSubjectStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The dates the subject began and ended their participation in the study.
         * 
         * @param period
         *     Start and end of participation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * Reference to the study the subject is participating in.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link ResearchStudy}</li>
         * </ul>
         * 
         * @param study
         *     Study subject is part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder study(Reference study) {
            this.study = study;
            return this;
        }

        /**
         * The record of the person or animal who is involved in the study.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param individual
         *     Who is part of study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder individual(Reference individual) {
            this.individual = individual;
            return this;
        }

        /**
         * Convenience method for setting {@code assignedArm}.
         * 
         * @param assignedArm
         *     What path should be followed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #assignedArm(com.ibm.fhir.model.type.String)
         */
        public Builder assignedArm(java.lang.String assignedArm) {
            this.assignedArm = (assignedArm == null) ? null : String.of(assignedArm);
            return this;
        }

        /**
         * The name of the arm in the study the subject is expected to follow as part of this study.
         * 
         * @param assignedArm
         *     What path should be followed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder assignedArm(String assignedArm) {
            this.assignedArm = assignedArm;
            return this;
        }

        /**
         * Convenience method for setting {@code actualArm}.
         * 
         * @param actualArm
         *     What path was followed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #actualArm(com.ibm.fhir.model.type.String)
         */
        public Builder actualArm(java.lang.String actualArm) {
            this.actualArm = (actualArm == null) ? null : String.of(actualArm);
            return this;
        }

        /**
         * The name of the arm in the study the subject actually followed as part of this study.
         * 
         * @param actualArm
         *     What path was followed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder actualArm(String actualArm) {
            this.actualArm = actualArm;
            return this;
        }

        /**
         * A record of the patient's informed agreement to participate in the study.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Consent}</li>
         * </ul>
         * 
         * @param consent
         *     Agreement to participate in study
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder consent(Reference consent) {
            this.consent = consent;
            return this;
        }

        /**
         * Build the {@link ResearchSubject}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>study</li>
         * <li>individual</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ResearchSubject}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ResearchSubject per the base specification
         */
        @Override
        public ResearchSubject build() {
            ResearchSubject researchSubject = new ResearchSubject(this);
            if (validating) {
                validate(researchSubject);
            }
            return researchSubject;
        }

        protected void validate(ResearchSubject researchSubject) {
            super.validate(researchSubject);
            ValidationSupport.checkList(researchSubject.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(researchSubject.status, "status");
            ValidationSupport.requireNonNull(researchSubject.study, "study");
            ValidationSupport.requireNonNull(researchSubject.individual, "individual");
            ValidationSupport.checkReferenceType(researchSubject.study, "study", "ResearchStudy");
            ValidationSupport.checkReferenceType(researchSubject.individual, "individual", "Patient");
            ValidationSupport.checkReferenceType(researchSubject.consent, "consent", "Consent");
        }

        protected Builder from(ResearchSubject researchSubject) {
            super.from(researchSubject);
            identifier.addAll(researchSubject.identifier);
            status = researchSubject.status;
            period = researchSubject.period;
            study = researchSubject.study;
            individual = researchSubject.individual;
            assignedArm = researchSubject.assignedArm;
            actualArm = researchSubject.actualArm;
            consent = researchSubject.consent;
            return this;
        }
    }
}
