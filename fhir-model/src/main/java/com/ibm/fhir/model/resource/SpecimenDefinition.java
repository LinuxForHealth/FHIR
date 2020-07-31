/*
 * (C) Copyright IBM Corp. 2019, 2020
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
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.SpecimenContainedPreference;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A kind of specimen with associated set of requirements.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SpecimenDefinition extends DomainResource {
    @Summary
    private final Identifier identifier;
    @Summary
    @Binding(
        bindingName = "CollectedSpecimenType",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "The type of the specimen to be collected.",
        valueSet = "http://terminology.hl7.org/ValueSet/v2-0487"
    )
    private final CodeableConcept typeCollected;
    @Summary
    @Binding(
        bindingName = "PreparePatient",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "Checks on the patient prior specimen collection.",
        valueSet = "http://hl7.org/fhir/ValueSet/prepare-patient-prior-specimen-collection"
    )
    private final List<CodeableConcept> patientPreparation;
    @Summary
    private final String timeAspect;
    @Summary
    @Binding(
        bindingName = "SpecimenCollection",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "The action to collect a type of specimen.",
        valueSet = "http://hl7.org/fhir/ValueSet/specimen-collection"
    )
    private final List<CodeableConcept> collection;
    private final List<TypeTested> typeTested;

    private volatile int hashCode;

    private SpecimenDefinition(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        typeCollected = builder.typeCollected;
        patientPreparation = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.patientPreparation, "patientPreparation"));
        timeAspect = builder.timeAspect;
        collection = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.collection, "collection"));
        typeTested = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.typeTested, "typeTested"));
        ValidationSupport.requireChildren(this);
    }

    /**
     * A business identifier associated with the kind of specimen.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * The kind of material to be collected.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getTypeCollected() {
        return typeCollected;
    }

    /**
     * Preparation of the patient for specimen collection.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getPatientPreparation() {
        return patientPreparation;
    }

    /**
     * Time aspect of specimen collection (duration or offset).
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getTimeAspect() {
        return timeAspect;
    }

    /**
     * The action to be performed for collecting the specimen.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCollection() {
        return collection;
    }

    /**
     * Specimen conditioned in a container as expected by the testing laboratory.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link TypeTested} that may be empty.
     */
    public List<TypeTested> getTypeTested() {
        return typeTested;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (identifier != null) || 
            (typeCollected != null) || 
            !patientPreparation.isEmpty() || 
            (timeAspect != null) || 
            !collection.isEmpty() || 
            !typeTested.isEmpty();
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
                accept(identifier, "identifier", visitor);
                accept(typeCollected, "typeCollected", visitor);
                accept(patientPreparation, "patientPreparation", visitor, CodeableConcept.class);
                accept(timeAspect, "timeAspect", visitor);
                accept(collection, "collection", visitor, CodeableConcept.class);
                accept(typeTested, "typeTested", visitor, TypeTested.class);
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
        SpecimenDefinition other = (SpecimenDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(typeCollected, other.typeCollected) && 
            Objects.equals(patientPreparation, other.patientPreparation) && 
            Objects.equals(timeAspect, other.timeAspect) && 
            Objects.equals(collection, other.collection) && 
            Objects.equals(typeTested, other.typeTested);
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
                typeCollected, 
                patientPreparation, 
                timeAspect, 
                collection, 
                typeTested);
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
        private Identifier identifier;
        private CodeableConcept typeCollected;
        private List<CodeableConcept> patientPreparation = new ArrayList<>();
        private String timeAspect;
        private List<CodeableConcept> collection = new ArrayList<>();
        private List<TypeTested> typeTested = new ArrayList<>();

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
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * <p>Adds new element(s) to the existing list
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
         * <p>Replaces the existing list with a new one containing elements from the Collection
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
         * A business identifier associated with the kind of specimen.
         * 
         * @param identifier
         *     Business identifier of a kind of specimen
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * The kind of material to be collected.
         * 
         * @param typeCollected
         *     Kind of material to collect
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder typeCollected(CodeableConcept typeCollected) {
            this.typeCollected = typeCollected;
            return this;
        }

        /**
         * Preparation of the patient for specimen collection.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param patientPreparation
         *     Patient preparation for collection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patientPreparation(CodeableConcept... patientPreparation) {
            for (CodeableConcept value : patientPreparation) {
                this.patientPreparation.add(value);
            }
            return this;
        }

        /**
         * Preparation of the patient for specimen collection.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param patientPreparation
         *     Patient preparation for collection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patientPreparation(Collection<CodeableConcept> patientPreparation) {
            this.patientPreparation = new ArrayList<>(patientPreparation);
            return this;
        }

        /**
         * Time aspect of specimen collection (duration or offset).
         * 
         * @param timeAspect
         *     Time aspect for collection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder timeAspect(String timeAspect) {
            this.timeAspect = timeAspect;
            return this;
        }

        /**
         * The action to be performed for collecting the specimen.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param collection
         *     Specimen collection procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder collection(CodeableConcept... collection) {
            for (CodeableConcept value : collection) {
                this.collection.add(value);
            }
            return this;
        }

        /**
         * The action to be performed for collecting the specimen.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param collection
         *     Specimen collection procedure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder collection(Collection<CodeableConcept> collection) {
            this.collection = new ArrayList<>(collection);
            return this;
        }

        /**
         * Specimen conditioned in a container as expected by the testing laboratory.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param typeTested
         *     Specimen in container intended for testing by lab
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder typeTested(TypeTested... typeTested) {
            for (TypeTested value : typeTested) {
                this.typeTested.add(value);
            }
            return this;
        }

        /**
         * Specimen conditioned in a container as expected by the testing laboratory.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param typeTested
         *     Specimen in container intended for testing by lab
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder typeTested(Collection<TypeTested> typeTested) {
            this.typeTested = new ArrayList<>(typeTested);
            return this;
        }

        /**
         * Build the {@link SpecimenDefinition}
         * 
         * @return
         *     An immutable object of type {@link SpecimenDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SpecimenDefinition per the base specification
         */
        @Override
        public SpecimenDefinition build() {
            return new SpecimenDefinition(this);
        }

        protected Builder from(SpecimenDefinition specimenDefinition) {
            super.from(specimenDefinition);
            identifier = specimenDefinition.identifier;
            typeCollected = specimenDefinition.typeCollected;
            patientPreparation.addAll(specimenDefinition.patientPreparation);
            timeAspect = specimenDefinition.timeAspect;
            collection.addAll(specimenDefinition.collection);
            typeTested.addAll(specimenDefinition.typeTested);
            return this;
        }
    }

    /**
     * Specimen conditioned in a container as expected by the testing laboratory.
     */
    public static class TypeTested extends BackboneElement {
        private final Boolean isDerived;
        @Binding(
            bindingName = "IntendedSpecimenType",
            strength = BindingStrength.ValueSet.EXAMPLE,
            description = "The type of specimen conditioned in a container for lab testing.",
            valueSet = "http://terminology.hl7.org/ValueSet/v2-0487"
        )
        private final CodeableConcept type;
        @Binding(
            bindingName = "SpecimenContainedPreference",
            strength = BindingStrength.ValueSet.REQUIRED,
            description = "Degree of preference of a type of conditioned specimen.",
            valueSet = "http://hl7.org/fhir/ValueSet/specimen-contained-preference|4.0.1"
        )
        @Required
        private final SpecimenContainedPreference preference;
        private final Container container;
        private final String requirement;
        private final Duration retentionTime;
        @Binding(
            bindingName = "RejectionCriterion",
            strength = BindingStrength.ValueSet.EXAMPLE,
            description = "Criterion for rejection of the specimen by laboratory.",
            valueSet = "http://hl7.org/fhir/ValueSet/rejection-criteria"
        )
        private final List<CodeableConcept> rejectionCriterion;
        private final List<Handling> handling;

        private volatile int hashCode;

        private TypeTested(Builder builder) {
            super(builder);
            isDerived = builder.isDerived;
            type = builder.type;
            preference = ValidationSupport.requireNonNull(builder.preference, "preference");
            container = builder.container;
            requirement = builder.requirement;
            retentionTime = builder.retentionTime;
            rejectionCriterion = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.rejectionCriterion, "rejectionCriterion"));
            handling = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.handling, "handling"));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Primary of secondary specimen.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getIsDerived() {
            return isDerived;
        }

        /**
         * The kind of specimen conditioned for testing expected by lab.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The preference for this type of conditioned specimen.
         * 
         * @return
         *     An immutable object of type {@link SpecimenContainedPreference} that is non-null.
         */
        public SpecimenContainedPreference getPreference() {
            return preference;
        }

        /**
         * The specimen's container.
         * 
         * @return
         *     An immutable object of type {@link Container} that may be null.
         */
        public Container getContainer() {
            return container;
        }

        /**
         * Requirements for delivery and special handling of this kind of conditioned specimen.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getRequirement() {
            return requirement;
        }

        /**
         * The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of 
         * additional testing.
         * 
         * @return
         *     An immutable object of type {@link Duration} that may be null.
         */
        public Duration getRetentionTime() {
            return retentionTime;
        }

        /**
         * Criterion for rejection of the specimen in its container by the laboratory.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getRejectionCriterion() {
            return rejectionCriterion;
        }

        /**
         * Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing 
         * process.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Handling} that may be empty.
         */
        public List<Handling> getHandling() {
            return handling;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (isDerived != null) || 
                (type != null) || 
                (preference != null) || 
                (container != null) || 
                (requirement != null) || 
                (retentionTime != null) || 
                !rejectionCriterion.isEmpty() || 
                !handling.isEmpty();
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
                    accept(isDerived, "isDerived", visitor);
                    accept(type, "type", visitor);
                    accept(preference, "preference", visitor);
                    accept(container, "container", visitor);
                    accept(requirement, "requirement", visitor);
                    accept(retentionTime, "retentionTime", visitor);
                    accept(rejectionCriterion, "rejectionCriterion", visitor, CodeableConcept.class);
                    accept(handling, "handling", visitor, Handling.class);
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
            TypeTested other = (TypeTested) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(isDerived, other.isDerived) && 
                Objects.equals(type, other.type) && 
                Objects.equals(preference, other.preference) && 
                Objects.equals(container, other.container) && 
                Objects.equals(requirement, other.requirement) && 
                Objects.equals(retentionTime, other.retentionTime) && 
                Objects.equals(rejectionCriterion, other.rejectionCriterion) && 
                Objects.equals(handling, other.handling);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    isDerived, 
                    type, 
                    preference, 
                    container, 
                    requirement, 
                    retentionTime, 
                    rejectionCriterion, 
                    handling);
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
            private Boolean isDerived;
            private CodeableConcept type;
            private SpecimenContainedPreference preference;
            private Container container;
            private String requirement;
            private Duration retentionTime;
            private List<CodeableConcept> rejectionCriterion = new ArrayList<>();
            private List<Handling> handling = new ArrayList<>();

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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * <p>Adds new element(s) to the existing list
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
             * <p>Replaces the existing list with a new one containing elements from the Collection
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
             * Primary of secondary specimen.
             * 
             * @param isDerived
             *     Primary or secondary specimen
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder isDerived(Boolean isDerived) {
                this.isDerived = isDerived;
                return this;
            }

            /**
             * The kind of specimen conditioned for testing expected by lab.
             * 
             * @param type
             *     Type of intended specimen
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * The preference for this type of conditioned specimen.
             * 
             * <p>This element is required.
             * 
             * @param preference
             *     preferred | alternate
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder preference(SpecimenContainedPreference preference) {
                this.preference = preference;
                return this;
            }

            /**
             * The specimen's container.
             * 
             * @param container
             *     The specimen's container
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder container(Container container) {
                this.container = container;
                return this;
            }

            /**
             * Requirements for delivery and special handling of this kind of conditioned specimen.
             * 
             * @param requirement
             *     Specimen requirements
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder requirement(String requirement) {
                this.requirement = requirement;
                return this;
            }

            /**
             * The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of 
             * additional testing.
             * 
             * @param retentionTime
             *     Specimen retention time
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder retentionTime(Duration retentionTime) {
                this.retentionTime = retentionTime;
                return this;
            }

            /**
             * Criterion for rejection of the specimen in its container by the laboratory.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param rejectionCriterion
             *     Rejection criterion
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder rejectionCriterion(CodeableConcept... rejectionCriterion) {
                for (CodeableConcept value : rejectionCriterion) {
                    this.rejectionCriterion.add(value);
                }
                return this;
            }

            /**
             * Criterion for rejection of the specimen in its container by the laboratory.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param rejectionCriterion
             *     Rejection criterion
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder rejectionCriterion(Collection<CodeableConcept> rejectionCriterion) {
                this.rejectionCriterion = new ArrayList<>(rejectionCriterion);
                return this;
            }

            /**
             * Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing 
             * process.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param handling
             *     Specimen handling before testing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder handling(Handling... handling) {
                for (Handling value : handling) {
                    this.handling.add(value);
                }
                return this;
            }

            /**
             * Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing 
             * process.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param handling
             *     Specimen handling before testing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder handling(Collection<Handling> handling) {
                this.handling = new ArrayList<>(handling);
                return this;
            }

            /**
             * Build the {@link TypeTested}
             * 
             * <p>Required elements:
             * <ul>
             * <li>preference</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link TypeTested}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid TypeTested per the base specification
             */
            @Override
            public TypeTested build() {
                return new TypeTested(this);
            }

            protected Builder from(TypeTested typeTested) {
                super.from(typeTested);
                isDerived = typeTested.isDerived;
                type = typeTested.type;
                preference = typeTested.preference;
                container = typeTested.container;
                requirement = typeTested.requirement;
                retentionTime = typeTested.retentionTime;
                rejectionCriterion.addAll(typeTested.rejectionCriterion);
                handling.addAll(typeTested.handling);
                return this;
            }
        }

        /**
         * The specimen's container.
         */
        public static class Container extends BackboneElement {
            @Binding(
                bindingName = "ContainerMaterial",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Types of material for specimen containers.",
                valueSet = "http://hl7.org/fhir/ValueSet/container-material"
            )
            private final CodeableConcept material;
            @Binding(
                bindingName = "ContainerType",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Type of specimen container.",
                valueSet = "http://hl7.org/fhir/ValueSet/specimen-container-type"
            )
            private final CodeableConcept type;
            @Binding(
                bindingName = "ContainerCap",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Color of the container cap.",
                valueSet = "http://hl7.org/fhir/ValueSet/container-cap"
            )
            private final CodeableConcept cap;
            private final String description;
            private final SimpleQuantity capacity;
            @Choice({ SimpleQuantity.class, String.class })
            private final Element minimumVolume;
            private final List<Additive> additive;
            private final String preparation;

            private volatile int hashCode;

            private Container(Builder builder) {
                super(builder);
                material = builder.material;
                type = builder.type;
                cap = builder.cap;
                description = builder.description;
                capacity = builder.capacity;
                minimumVolume = ValidationSupport.choiceElement(builder.minimumVolume, "minimumVolume", SimpleQuantity.class, String.class);
                additive = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.additive, "additive"));
                preparation = builder.preparation;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * The type of material of the container.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getMaterial() {
                return material;
            }

            /**
             * The type of container used to contain this kind of specimen.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * Color of container cap.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getCap() {
                return cap;
            }

            /**
             * The textual description of the kind of container.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getDescription() {
                return description;
            }

            /**
             * The capacity (volume or other measure) of this kind of container.
             * 
             * @return
             *     An immutable object of type {@link SimpleQuantity} that may be null.
             */
            public SimpleQuantity getCapacity() {
                return capacity;
            }

            /**
             * The minimum volume to be conditioned in the container.
             * 
             * @return
             *     An immutable object of type {@link Element} that may be null.
             */
            public Element getMinimumVolume() {
                return minimumVolume;
            }

            /**
             * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, 
             * Citrate, EDTA.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Additive} that may be empty.
             */
            public List<Additive> getAdditive() {
                return additive;
            }

            /**
             * Special processing that should be applied to the container for this kind of specimen.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getPreparation() {
                return preparation;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (material != null) || 
                    (type != null) || 
                    (cap != null) || 
                    (description != null) || 
                    (capacity != null) || 
                    (minimumVolume != null) || 
                    !additive.isEmpty() || 
                    (preparation != null);
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
                        accept(material, "material", visitor);
                        accept(type, "type", visitor);
                        accept(cap, "cap", visitor);
                        accept(description, "description", visitor);
                        accept(capacity, "capacity", visitor);
                        accept(minimumVolume, "minimumVolume", visitor);
                        accept(additive, "additive", visitor, Additive.class);
                        accept(preparation, "preparation", visitor);
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
                Container other = (Container) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(material, other.material) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(cap, other.cap) && 
                    Objects.equals(description, other.description) && 
                    Objects.equals(capacity, other.capacity) && 
                    Objects.equals(minimumVolume, other.minimumVolume) && 
                    Objects.equals(additive, other.additive) && 
                    Objects.equals(preparation, other.preparation);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        material, 
                        type, 
                        cap, 
                        description, 
                        capacity, 
                        minimumVolume, 
                        additive, 
                        preparation);
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
                private CodeableConcept material;
                private CodeableConcept type;
                private CodeableConcept cap;
                private String description;
                private SimpleQuantity capacity;
                private Element minimumVolume;
                private List<Additive> additive = new ArrayList<>();
                private String preparation;

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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * The type of material of the container.
                 * 
                 * @param material
                 *     Container material
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder material(CodeableConcept material) {
                    this.material = material;
                    return this;
                }

                /**
                 * The type of container used to contain this kind of specimen.
                 * 
                 * @param type
                 *     Kind of container associated with the kind of specimen
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * Color of container cap.
                 * 
                 * @param cap
                 *     Color of container cap
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder cap(CodeableConcept cap) {
                    this.cap = cap;
                    return this;
                }

                /**
                 * The textual description of the kind of container.
                 * 
                 * @param description
                 *     Container description
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder description(String description) {
                    this.description = description;
                    return this;
                }

                /**
                 * The capacity (volume or other measure) of this kind of container.
                 * 
                 * @param capacity
                 *     Container capacity
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder capacity(SimpleQuantity capacity) {
                    this.capacity = capacity;
                    return this;
                }

                /**
                 * The minimum volume to be conditioned in the container.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link SimpleQuantity}</li>
                 * <li>{@link String}</li>
                 * </ul>
                 * 
                 * @param minimumVolume
                 *     Minimum volume
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder minimumVolume(Element minimumVolume) {
                    this.minimumVolume = minimumVolume;
                    return this;
                }

                /**
                 * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, 
                 * Citrate, EDTA.
                 * 
                 * <p>Adds new element(s) to the existing list
                 * 
                 * @param additive
                 *     Additive associated with container
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder additive(Additive... additive) {
                    for (Additive value : additive) {
                        this.additive.add(value);
                    }
                    return this;
                }

                /**
                 * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, 
                 * Citrate, EDTA.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
                 * 
                 * @param additive
                 *     Additive associated with container
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder additive(Collection<Additive> additive) {
                    this.additive = new ArrayList<>(additive);
                    return this;
                }

                /**
                 * Special processing that should be applied to the container for this kind of specimen.
                 * 
                 * @param preparation
                 *     Specimen container preparation
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder preparation(String preparation) {
                    this.preparation = preparation;
                    return this;
                }

                /**
                 * Build the {@link Container}
                 * 
                 * @return
                 *     An immutable object of type {@link Container}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Container per the base specification
                 */
                @Override
                public Container build() {
                    return new Container(this);
                }

                protected Builder from(Container container) {
                    super.from(container);
                    material = container.material;
                    type = container.type;
                    cap = container.cap;
                    description = container.description;
                    capacity = container.capacity;
                    minimumVolume = container.minimumVolume;
                    additive.addAll(container.additive);
                    preparation = container.preparation;
                    return this;
                }
            }

            /**
             * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, 
             * Citrate, EDTA.
             */
            public static class Additive extends BackboneElement {
                @Choice({ CodeableConcept.class, Reference.class })
                @Binding(
                    bindingName = "ContainerAdditive",
                    strength = BindingStrength.ValueSet.EXAMPLE,
                    description = "Substance added to specimen container.",
                    valueSet = "http://terminology.hl7.org/ValueSet/v2-0371"
                )
                @Required
                private final Element additive;

                private volatile int hashCode;

                private Additive(Builder builder) {
                    super(builder);
                    additive = ValidationSupport.requireChoiceElement(builder.additive, "additive", CodeableConcept.class, Reference.class);
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, 
                 * Citrate, EDTA.
                 * 
                 * @return
                 *     An immutable object of type {@link Element} that is non-null.
                 */
                public Element getAdditive() {
                    return additive;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (additive != null);
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
                            accept(additive, "additive", visitor);
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
                    Additive other = (Additive) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(additive, other.additive);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            additive);
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
                    private Element additive;

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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * <p>Adds new element(s) to the existing list
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
                     * <p>Replaces the existing list with a new one containing elements from the Collection
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
                     * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, 
                     * Citrate, EDTA.
                     * 
                     * <p>This element is required.
                     * 
                     * <p>This is a choice element with the following allowed types:
                     * <ul>
                     * <li>{@link CodeableConcept}</li>
                     * <li>{@link Reference}</li>
                     * </ul>
                     * 
                     * @param additive
                     *     Additive associated with container
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder additive(Element additive) {
                        this.additive = additive;
                        return this;
                    }

                    /**
                     * Build the {@link Additive}
                     * 
                     * <p>Required elements:
                     * <ul>
                     * <li>additive</li>
                     * </ul>
                     * 
                     * @return
                     *     An immutable object of type {@link Additive}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid Additive per the base specification
                     */
                    @Override
                    public Additive build() {
                        return new Additive(this);
                    }

                    protected Builder from(Additive additive) {
                        super.from(additive);
                        this.additive = additive.additive;
                        return this;
                    }
                }
            }
        }

        /**
         * Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing 
         * process.
         */
        public static class Handling extends BackboneElement {
            @Binding(
                bindingName = "HandlingConditionSet",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Set of handling instructions prior testing of the specimen.",
                valueSet = "http://hl7.org/fhir/ValueSet/handling-condition"
            )
            private final CodeableConcept temperatureQualifier;
            private final Range temperatureRange;
            private final Duration maxDuration;
            private final String instruction;

            private volatile int hashCode;

            private Handling(Builder builder) {
                super(builder);
                temperatureQualifier = builder.temperatureQualifier;
                temperatureRange = builder.temperatureRange;
                maxDuration = builder.maxDuration;
                instruction = builder.instruction;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * It qualifies the interval of temperature, which characterizes an occurrence of handling. Conditions that are not 
             * related to temperature may be handled in the instruction element.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getTemperatureQualifier() {
                return temperatureQualifier;
            }

            /**
             * The temperature interval for this set of handling instructions.
             * 
             * @return
             *     An immutable object of type {@link Range} that may be null.
             */
            public Range getTemperatureRange() {
                return temperatureRange;
            }

            /**
             * The maximum time interval of preservation of the specimen with these conditions.
             * 
             * @return
             *     An immutable object of type {@link Duration} that may be null.
             */
            public Duration getMaxDuration() {
                return maxDuration;
            }

            /**
             * Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light 
             * exposure'.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getInstruction() {
                return instruction;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (temperatureQualifier != null) || 
                    (temperatureRange != null) || 
                    (maxDuration != null) || 
                    (instruction != null);
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
                        accept(temperatureQualifier, "temperatureQualifier", visitor);
                        accept(temperatureRange, "temperatureRange", visitor);
                        accept(maxDuration, "maxDuration", visitor);
                        accept(instruction, "instruction", visitor);
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
                Handling other = (Handling) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(temperatureQualifier, other.temperatureQualifier) && 
                    Objects.equals(temperatureRange, other.temperatureRange) && 
                    Objects.equals(maxDuration, other.maxDuration) && 
                    Objects.equals(instruction, other.instruction);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        temperatureQualifier, 
                        temperatureRange, 
                        maxDuration, 
                        instruction);
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
                private CodeableConcept temperatureQualifier;
                private Range temperatureRange;
                private Duration maxDuration;
                private String instruction;

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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * <p>Adds new element(s) to the existing list
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
                 * <p>Replaces the existing list with a new one containing elements from the Collection
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
                 * It qualifies the interval of temperature, which characterizes an occurrence of handling. Conditions that are not 
                 * related to temperature may be handled in the instruction element.
                 * 
                 * @param temperatureQualifier
                 *     Temperature qualifier
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder temperatureQualifier(CodeableConcept temperatureQualifier) {
                    this.temperatureQualifier = temperatureQualifier;
                    return this;
                }

                /**
                 * The temperature interval for this set of handling instructions.
                 * 
                 * @param temperatureRange
                 *     Temperature range
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder temperatureRange(Range temperatureRange) {
                    this.temperatureRange = temperatureRange;
                    return this;
                }

                /**
                 * The maximum time interval of preservation of the specimen with these conditions.
                 * 
                 * @param maxDuration
                 *     Maximum preservation time
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder maxDuration(Duration maxDuration) {
                    this.maxDuration = maxDuration;
                    return this;
                }

                /**
                 * Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light 
                 * exposure'.
                 * 
                 * @param instruction
                 *     Preservation instruction
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder instruction(String instruction) {
                    this.instruction = instruction;
                    return this;
                }

                /**
                 * Build the {@link Handling}
                 * 
                 * @return
                 *     An immutable object of type {@link Handling}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Handling per the base specification
                 */
                @Override
                public Handling build() {
                    return new Handling(this);
                }

                protected Builder from(Handling handling) {
                    super.from(handling);
                    temperatureQualifier = handling.temperatureQualifier;
                    temperatureRange = handling.temperatureRange;
                    maxDuration = handling.maxDuration;
                    instruction = handling.instruction;
                    return this;
                }
            }
        }
    }
}
