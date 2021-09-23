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

import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Population;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Describe the undesirable effects of the medicinal product.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicinalProductUndesirableEffect extends DomainResource {
    @Summary
    @ReferenceTarget({ "MedicinalProduct", "Medication" })
    private final List<Reference> subject;
    @Summary
    private final CodeableConcept symptomConditionEffect;
    @Summary
    private final CodeableConcept classification;
    @Summary
    private final CodeableConcept frequencyOfOccurrence;
    @Summary
    private final List<Population> population;

    private MedicinalProductUndesirableEffect(Builder builder) {
        super(builder);
        subject = Collections.unmodifiableList(builder.subject);
        symptomConditionEffect = builder.symptomConditionEffect;
        classification = builder.classification;
        frequencyOfOccurrence = builder.frequencyOfOccurrence;
        population = Collections.unmodifiableList(builder.population);
    }

    /**
     * The medication for which this is an indication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSubject() {
        return subject;
    }

    /**
     * The symptom, condition or undesirable effect.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSymptomConditionEffect() {
        return symptomConditionEffect;
    }

    /**
     * Classification of the effect.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getClassification() {
        return classification;
    }

    /**
     * The frequency of occurrence of the effect.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getFrequencyOfOccurrence() {
        return frequencyOfOccurrence;
    }

    /**
     * The population group to which this applies.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Population} that may be empty.
     */
    public List<Population> getPopulation() {
        return population;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !subject.isEmpty() || 
            (symptomConditionEffect != null) || 
            (classification != null) || 
            (frequencyOfOccurrence != null) || 
            !population.isEmpty();
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
                accept(subject, "subject", visitor, Reference.class);
                accept(symptomConditionEffect, "symptomConditionEffect", visitor);
                accept(classification, "classification", visitor);
                accept(frequencyOfOccurrence, "frequencyOfOccurrence", visitor);
                accept(population, "population", visitor, Population.class);
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
        MedicinalProductUndesirableEffect other = (MedicinalProductUndesirableEffect) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(symptomConditionEffect, other.symptomConditionEffect) && 
            Objects.equals(classification, other.classification) && 
            Objects.equals(frequencyOfOccurrence, other.frequencyOfOccurrence) && 
            Objects.equals(population, other.population);
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
                subject, 
                symptomConditionEffect, 
                classification, 
                frequencyOfOccurrence, 
                population);
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
        private List<Reference> subject = new ArrayList<>();
        private CodeableConcept symptomConditionEffect;
        private CodeableConcept classification;
        private CodeableConcept frequencyOfOccurrence;
        private List<Population> population = new ArrayList<>();

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
         * The medication for which this is an indication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProduct}</li>
         * <li>{@link Medication}</li>
         * </ul>
         * 
         * @param subject
         *     The medication for which this is an indication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference... subject) {
            for (Reference value : subject) {
                this.subject.add(value);
            }
            return this;
        }

        /**
         * The medication for which this is an indication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProduct}</li>
         * <li>{@link Medication}</li>
         * </ul>
         * 
         * @param subject
         *     The medication for which this is an indication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder subject(Collection<Reference> subject) {
            this.subject = new ArrayList<>(subject);
            return this;
        }

        /**
         * The symptom, condition or undesirable effect.
         * 
         * @param symptomConditionEffect
         *     The symptom, condition or undesirable effect
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder symptomConditionEffect(CodeableConcept symptomConditionEffect) {
            this.symptomConditionEffect = symptomConditionEffect;
            return this;
        }

        /**
         * Classification of the effect.
         * 
         * @param classification
         *     Classification of the effect
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder classification(CodeableConcept classification) {
            this.classification = classification;
            return this;
        }

        /**
         * The frequency of occurrence of the effect.
         * 
         * @param frequencyOfOccurrence
         *     The frequency of occurrence of the effect
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder frequencyOfOccurrence(CodeableConcept frequencyOfOccurrence) {
            this.frequencyOfOccurrence = frequencyOfOccurrence;
            return this;
        }

        /**
         * The population group to which this applies.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param population
         *     The population group to which this applies
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder population(Population... population) {
            for (Population value : population) {
                this.population.add(value);
            }
            return this;
        }

        /**
         * The population group to which this applies.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param population
         *     The population group to which this applies
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder population(Collection<Population> population) {
            this.population = new ArrayList<>(population);
            return this;
        }

        /**
         * Build the {@link MedicinalProductUndesirableEffect}
         * 
         * @return
         *     An immutable object of type {@link MedicinalProductUndesirableEffect}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicinalProductUndesirableEffect per the base specification
         */
        @Override
        public MedicinalProductUndesirableEffect build() {
            MedicinalProductUndesirableEffect medicinalProductUndesirableEffect = new MedicinalProductUndesirableEffect(this);
            if (validating) {
                validate(medicinalProductUndesirableEffect);
            }
            return medicinalProductUndesirableEffect;
        }

        protected void validate(MedicinalProductUndesirableEffect medicinalProductUndesirableEffect) {
            super.validate(medicinalProductUndesirableEffect);
            ValidationSupport.checkList(medicinalProductUndesirableEffect.subject, "subject", Reference.class);
            ValidationSupport.checkList(medicinalProductUndesirableEffect.population, "population", Population.class);
            ValidationSupport.checkReferenceType(medicinalProductUndesirableEffect.subject, "subject", "MedicinalProduct", "Medication");
        }

        protected Builder from(MedicinalProductUndesirableEffect medicinalProductUndesirableEffect) {
            super.from(medicinalProductUndesirableEffect);
            subject.addAll(medicinalProductUndesirableEffect.subject);
            symptomConditionEffect = medicinalProductUndesirableEffect.symptomConditionEffect;
            classification = medicinalProductUndesirableEffect.classification;
            frequencyOfOccurrence = medicinalProductUndesirableEffect.frequencyOfOccurrence;
            population.addAll(medicinalProductUndesirableEffect.population);
            return this;
        }
    }
}
