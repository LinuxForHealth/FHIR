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

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Element;
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
 * The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory 
 * purposes.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicinalProductContraindication extends DomainResource {
    @Summary
    @ReferenceTarget({ "MedicinalProduct", "Medication" })
    private final List<Reference> subject;
    @Summary
    private final CodeableConcept disease;
    @Summary
    private final CodeableConcept diseaseStatus;
    @Summary
    private final List<CodeableConcept> comorbidity;
    @Summary
    @ReferenceTarget({ "MedicinalProductIndication" })
    private final List<Reference> therapeuticIndication;
    @Summary
    private final List<OtherTherapy> otherTherapy;
    @Summary
    private final List<Population> population;

    private MedicinalProductContraindication(Builder builder) {
        super(builder);
        subject = Collections.unmodifiableList(builder.subject);
        disease = builder.disease;
        diseaseStatus = builder.diseaseStatus;
        comorbidity = Collections.unmodifiableList(builder.comorbidity);
        therapeuticIndication = Collections.unmodifiableList(builder.therapeuticIndication);
        otherTherapy = Collections.unmodifiableList(builder.otherTherapy);
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
     * The disease, symptom or procedure for the contraindication.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getDisease() {
        return disease;
    }

    /**
     * The status of the disease or symptom for the contraindication.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getDiseaseStatus() {
        return diseaseStatus;
    }

    /**
     * A comorbidity (concurrent condition) or coinfection.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getComorbidity() {
        return comorbidity;
    }

    /**
     * Information about the use of the medicinal product in relation to other therapies as part of the indication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getTherapeuticIndication() {
        return therapeuticIndication;
    }

    /**
     * Information about the use of the medicinal product in relation to other therapies described as part of the indication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link OtherTherapy} that may be empty.
     */
    public List<OtherTherapy> getOtherTherapy() {
        return otherTherapy;
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
            (disease != null) || 
            (diseaseStatus != null) || 
            !comorbidity.isEmpty() || 
            !therapeuticIndication.isEmpty() || 
            !otherTherapy.isEmpty() || 
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
                accept(disease, "disease", visitor);
                accept(diseaseStatus, "diseaseStatus", visitor);
                accept(comorbidity, "comorbidity", visitor, CodeableConcept.class);
                accept(therapeuticIndication, "therapeuticIndication", visitor, Reference.class);
                accept(otherTherapy, "otherTherapy", visitor, OtherTherapy.class);
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
        MedicinalProductContraindication other = (MedicinalProductContraindication) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(disease, other.disease) && 
            Objects.equals(diseaseStatus, other.diseaseStatus) && 
            Objects.equals(comorbidity, other.comorbidity) && 
            Objects.equals(therapeuticIndication, other.therapeuticIndication) && 
            Objects.equals(otherTherapy, other.otherTherapy) && 
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
                disease, 
                diseaseStatus, 
                comorbidity, 
                therapeuticIndication, 
                otherTherapy, 
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
        private CodeableConcept disease;
        private CodeableConcept diseaseStatus;
        private List<CodeableConcept> comorbidity = new ArrayList<>();
        private List<Reference> therapeuticIndication = new ArrayList<>();
        private List<OtherTherapy> otherTherapy = new ArrayList<>();
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
         * The disease, symptom or procedure for the contraindication.
         * 
         * @param disease
         *     The disease, symptom or procedure for the contraindication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder disease(CodeableConcept disease) {
            this.disease = disease;
            return this;
        }

        /**
         * The status of the disease or symptom for the contraindication.
         * 
         * @param diseaseStatus
         *     The status of the disease or symptom for the contraindication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder diseaseStatus(CodeableConcept diseaseStatus) {
            this.diseaseStatus = diseaseStatus;
            return this;
        }

        /**
         * A comorbidity (concurrent condition) or coinfection.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param comorbidity
         *     A comorbidity (concurrent condition) or coinfection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder comorbidity(CodeableConcept... comorbidity) {
            for (CodeableConcept value : comorbidity) {
                this.comorbidity.add(value);
            }
            return this;
        }

        /**
         * A comorbidity (concurrent condition) or coinfection.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param comorbidity
         *     A comorbidity (concurrent condition) or coinfection
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder comorbidity(Collection<CodeableConcept> comorbidity) {
            this.comorbidity = new ArrayList<>(comorbidity);
            return this;
        }

        /**
         * Information about the use of the medicinal product in relation to other therapies as part of the indication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductIndication}</li>
         * </ul>
         * 
         * @param therapeuticIndication
         *     Information about the use of the medicinal product in relation to other therapies as part of the indication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder therapeuticIndication(Reference... therapeuticIndication) {
            for (Reference value : therapeuticIndication) {
                this.therapeuticIndication.add(value);
            }
            return this;
        }

        /**
         * Information about the use of the medicinal product in relation to other therapies as part of the indication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MedicinalProductIndication}</li>
         * </ul>
         * 
         * @param therapeuticIndication
         *     Information about the use of the medicinal product in relation to other therapies as part of the indication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder therapeuticIndication(Collection<Reference> therapeuticIndication) {
            this.therapeuticIndication = new ArrayList<>(therapeuticIndication);
            return this;
        }

        /**
         * Information about the use of the medicinal product in relation to other therapies described as part of the indication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param otherTherapy
         *     Information about the use of the medicinal product in relation to other therapies described as part of the indication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder otherTherapy(OtherTherapy... otherTherapy) {
            for (OtherTherapy value : otherTherapy) {
                this.otherTherapy.add(value);
            }
            return this;
        }

        /**
         * Information about the use of the medicinal product in relation to other therapies described as part of the indication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param otherTherapy
         *     Information about the use of the medicinal product in relation to other therapies described as part of the indication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder otherTherapy(Collection<OtherTherapy> otherTherapy) {
            this.otherTherapy = new ArrayList<>(otherTherapy);
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
         * Build the {@link MedicinalProductContraindication}
         * 
         * @return
         *     An immutable object of type {@link MedicinalProductContraindication}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MedicinalProductContraindication per the base specification
         */
        @Override
        public MedicinalProductContraindication build() {
            MedicinalProductContraindication medicinalProductContraindication = new MedicinalProductContraindication(this);
            if (validating) {
                validate(medicinalProductContraindication);
            }
            return medicinalProductContraindication;
        }

        protected void validate(MedicinalProductContraindication medicinalProductContraindication) {
            super.validate(medicinalProductContraindication);
            ValidationSupport.checkList(medicinalProductContraindication.subject, "subject", Reference.class);
            ValidationSupport.checkList(medicinalProductContraindication.comorbidity, "comorbidity", CodeableConcept.class);
            ValidationSupport.checkList(medicinalProductContraindication.therapeuticIndication, "therapeuticIndication", Reference.class);
            ValidationSupport.checkList(medicinalProductContraindication.otherTherapy, "otherTherapy", OtherTherapy.class);
            ValidationSupport.checkList(medicinalProductContraindication.population, "population", Population.class);
            ValidationSupport.checkReferenceType(medicinalProductContraindication.subject, "subject", "MedicinalProduct", "Medication");
            ValidationSupport.checkReferenceType(medicinalProductContraindication.therapeuticIndication, "therapeuticIndication", "MedicinalProductIndication");
        }

        protected Builder from(MedicinalProductContraindication medicinalProductContraindication) {
            super.from(medicinalProductContraindication);
            subject.addAll(medicinalProductContraindication.subject);
            disease = medicinalProductContraindication.disease;
            diseaseStatus = medicinalProductContraindication.diseaseStatus;
            comorbidity.addAll(medicinalProductContraindication.comorbidity);
            therapeuticIndication.addAll(medicinalProductContraindication.therapeuticIndication);
            otherTherapy.addAll(medicinalProductContraindication.otherTherapy);
            population.addAll(medicinalProductContraindication.population);
            return this;
        }
    }

    /**
     * Information about the use of the medicinal product in relation to other therapies described as part of the indication.
     */
    public static class OtherTherapy extends BackboneElement {
        @Summary
        @Required
        private final CodeableConcept therapyRelationshipType;
        @Summary
        @ReferenceTarget({ "MedicinalProduct", "Medication", "Substance", "SubstanceSpecification" })
        @Choice({ CodeableConcept.class, Reference.class })
        @Required
        private final Element medication;

        private OtherTherapy(Builder builder) {
            super(builder);
            therapyRelationshipType = builder.therapyRelationshipType;
            medication = builder.medication;
        }

        /**
         * The type of relationship between the medicinal product indication or contraindication and another therapy.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getTherapyRelationshipType() {
            return therapyRelationshipType;
        }

        /**
         * Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication 
         * or contraindication.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} or {@link Reference} that is non-null.
         */
        public Element getMedication() {
            return medication;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (therapyRelationshipType != null) || 
                (medication != null);
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
                    accept(therapyRelationshipType, "therapyRelationshipType", visitor);
                    accept(medication, "medication", visitor);
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
            OtherTherapy other = (OtherTherapy) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(therapyRelationshipType, other.therapyRelationshipType) && 
                Objects.equals(medication, other.medication);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    therapyRelationshipType, 
                    medication);
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
            private CodeableConcept therapyRelationshipType;
            private Element medication;

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
             * The type of relationship between the medicinal product indication or contraindication and another therapy.
             * 
             * <p>This element is required.
             * 
             * @param therapyRelationshipType
             *     The type of relationship between the medicinal product indication or contraindication and another therapy
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder therapyRelationshipType(CodeableConcept therapyRelationshipType) {
                this.therapyRelationshipType = therapyRelationshipType;
                return this;
            }

            /**
             * Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication 
             * or contraindication.
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
             * <li>{@link MedicinalProduct}</li>
             * <li>{@link Medication}</li>
             * <li>{@link Substance}</li>
             * <li>{@link SubstanceSpecification}</li>
             * </ul>
             * 
             * @param medication
             *     Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication 
             *     or contraindication
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder medication(Element medication) {
                this.medication = medication;
                return this;
            }

            /**
             * Build the {@link OtherTherapy}
             * 
             * <p>Required elements:
             * <ul>
             * <li>therapyRelationshipType</li>
             * <li>medication</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link OtherTherapy}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid OtherTherapy per the base specification
             */
            @Override
            public OtherTherapy build() {
                OtherTherapy otherTherapy = new OtherTherapy(this);
                if (validating) {
                    validate(otherTherapy);
                }
                return otherTherapy;
            }

            protected void validate(OtherTherapy otherTherapy) {
                super.validate(otherTherapy);
                ValidationSupport.requireNonNull(otherTherapy.therapyRelationshipType, "therapyRelationshipType");
                ValidationSupport.requireChoiceElement(otherTherapy.medication, "medication", CodeableConcept.class, Reference.class);
                ValidationSupport.checkReferenceType(otherTherapy.medication, "medication", "MedicinalProduct", "Medication", "Substance", "SubstanceSpecification");
                ValidationSupport.requireValueOrChildren(otherTherapy);
            }

            protected Builder from(OtherTherapy otherTherapy) {
                super.from(otherTherapy);
                therapyRelationshipType = otherTherapy.therapyRelationshipType;
                medication = otherTherapy.medication;
                return this;
            }
        }
    }
}
