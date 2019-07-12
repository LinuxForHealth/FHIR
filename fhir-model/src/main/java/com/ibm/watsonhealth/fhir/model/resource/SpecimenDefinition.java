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
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Duration;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.SpecimenContainedPreference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A kind of specimen with associated set of requirements.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class SpecimenDefinition extends DomainResource {
    private final Identifier identifier;
    private final CodeableConcept typeCollected;
    private final List<CodeableConcept> patientPreparation;
    private final String timeAspect;
    private final List<CodeableConcept> collection;
    private final List<TypeTested> typeTested;

    private volatile int hashCode;

    private SpecimenDefinition(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        typeCollected = builder.typeCollected;
        patientPreparation = Collections.unmodifiableList(builder.patientPreparation);
        timeAspect = builder.timeAspect;
        collection = Collections.unmodifiableList(builder.collection);
        typeTested = Collections.unmodifiableList(builder.typeTested);
    }

    /**
     * <p>
     * A business identifier associated with the kind of specimen.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The kind of material to be collected.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getTypeCollected() {
        return typeCollected;
    }

    /**
     * <p>
     * Preparation of the patient for specimen collection.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getPatientPreparation() {
        return patientPreparation;
    }

    /**
     * <p>
     * Time aspect of specimen collection (duration or offset).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getTimeAspect() {
        return timeAspect;
    }

    /**
     * <p>
     * The action to be performed for collecting the specimen.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCollection() {
        return collection;
    }

    /**
     * <p>
     * Specimen conditioned in a container as expected by the testing laboratory.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link TypeTested}.
     */
    public List<TypeTested> getTypeTested() {
        return typeTested;
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
                accept(identifier, "identifier", visitor);
                accept(typeCollected, "typeCollected", visitor);
                accept(patientPreparation, "patientPreparation", visitor, CodeableConcept.class);
                accept(timeAspect, "timeAspect", visitor);
                accept(collection, "collection", visitor, CodeableConcept.class);
                accept(typeTested, "typeTested", visitor, TypeTested.class);
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
        // optional
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
         * A business identifier associated with the kind of specimen.
         * </p>
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
         * <p>
         * The kind of material to be collected.
         * </p>
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
         * <p>
         * Preparation of the patient for specimen collection.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Preparation of the patient for specimen collection.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * Time aspect of specimen collection (duration or offset).
         * </p>
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
         * <p>
         * The action to be performed for collecting the specimen.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * The action to be performed for collecting the specimen.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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
         * <p>
         * Specimen conditioned in a container as expected by the testing laboratory.
         * </p>
         * <p>
         * Adds new element(s) to the existing list
         * </p>
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
         * <p>
         * Specimen conditioned in a container as expected by the testing laboratory.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
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

        @Override
        public SpecimenDefinition build() {
            return new SpecimenDefinition(this);
        }

        private Builder from(SpecimenDefinition specimenDefinition) {
            id = specimenDefinition.id;
            meta = specimenDefinition.meta;
            implicitRules = specimenDefinition.implicitRules;
            language = specimenDefinition.language;
            text = specimenDefinition.text;
            contained.addAll(specimenDefinition.contained);
            extension.addAll(specimenDefinition.extension);
            modifierExtension.addAll(specimenDefinition.modifierExtension);
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
     * <p>
     * Specimen conditioned in a container as expected by the testing laboratory.
     * </p>
     */
    public static class TypeTested extends BackboneElement {
        private final Boolean isDerived;
        private final CodeableConcept type;
        private final SpecimenContainedPreference preference;
        private final Container container;
        private final String requirement;
        private final Duration retentionTime;
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
            rejectionCriterion = Collections.unmodifiableList(builder.rejectionCriterion);
            handling = Collections.unmodifiableList(builder.handling);
        }

        /**
         * <p>
         * Primary of secondary specimen.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getIsDerived() {
            return isDerived;
        }

        /**
         * <p>
         * The kind of specimen conditioned for testing expected by lab.
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
         * The preference for this type of conditioned specimen.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link SpecimenContainedPreference}.
         */
        public SpecimenContainedPreference getPreference() {
            return preference;
        }

        /**
         * <p>
         * The specimen's container.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Container}.
         */
        public Container getContainer() {
            return container;
        }

        /**
         * <p>
         * Requirements for delivery and special handling of this kind of conditioned specimen.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getRequirement() {
            return requirement;
        }

        /**
         * <p>
         * The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of 
         * additional testing.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Duration}.
         */
        public Duration getRetentionTime() {
            return retentionTime;
        }

        /**
         * <p>
         * Criterion for rejection of the specimen in its container by the laboratory.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
         */
        public List<CodeableConcept> getRejectionCriterion() {
            return rejectionCriterion;
        }

        /**
         * <p>
         * Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing 
         * process.
         * </p>
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Handling}.
         */
        public List<Handling> getHandling() {
            return handling;
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
                    accept(isDerived, "isDerived", visitor);
                    accept(type, "type", visitor);
                    accept(preference, "preference", visitor);
                    accept(container, "container", visitor);
                    accept(requirement, "requirement", visitor);
                    accept(retentionTime, "retentionTime", visitor);
                    accept(rejectionCriterion, "rejectionCriterion", visitor, CodeableConcept.class);
                    accept(handling, "handling", visitor, Handling.class);
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
            return new Builder(preference).from(this);
        }

        public Builder toBuilder(SpecimenContainedPreference preference) {
            return new Builder(preference).from(this);
        }

        public static Builder builder(SpecimenContainedPreference preference) {
            return new Builder(preference);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final SpecimenContainedPreference preference;

            // optional
            private Boolean isDerived;
            private CodeableConcept type;
            private Container container;
            private String requirement;
            private Duration retentionTime;
            private List<CodeableConcept> rejectionCriterion = new ArrayList<>();
            private List<Handling> handling = new ArrayList<>();

            private Builder(SpecimenContainedPreference preference) {
                super();
                this.preference = preference;
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
             * Primary of secondary specimen.
             * </p>
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
             * <p>
             * The kind of specimen conditioned for testing expected by lab.
             * </p>
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
             * <p>
             * The specimen's container.
             * </p>
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
             * <p>
             * Requirements for delivery and special handling of this kind of conditioned specimen.
             * </p>
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
             * <p>
             * The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of 
             * additional testing.
             * </p>
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
             * <p>
             * Criterion for rejection of the specimen in its container by the laboratory.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Criterion for rejection of the specimen in its container by the laboratory.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
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
             * <p>
             * Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing 
             * process.
             * </p>
             * <p>
             * Adds new element(s) to the existing list
             * </p>
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
             * <p>
             * Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing 
             * process.
             * </p>
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
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

            @Override
            public TypeTested build() {
                return new TypeTested(this);
            }

            private Builder from(TypeTested typeTested) {
                id = typeTested.id;
                extension.addAll(typeTested.extension);
                modifierExtension.addAll(typeTested.modifierExtension);
                isDerived = typeTested.isDerived;
                type = typeTested.type;
                container = typeTested.container;
                requirement = typeTested.requirement;
                retentionTime = typeTested.retentionTime;
                rejectionCriterion.addAll(typeTested.rejectionCriterion);
                handling.addAll(typeTested.handling);
                return this;
            }
        }

        /**
         * <p>
         * The specimen's container.
         * </p>
         */
        public static class Container extends BackboneElement {
            private final CodeableConcept material;
            private final CodeableConcept type;
            private final CodeableConcept cap;
            private final String description;
            private final Quantity capacity;
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
                minimumVolume = ValidationSupport.choiceElement(builder.minimumVolume, "minimumVolume", Quantity.class, String.class);
                additive = Collections.unmodifiableList(builder.additive);
                preparation = builder.preparation;
            }

            /**
             * <p>
             * The type of material of the container.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getMaterial() {
                return material;
            }

            /**
             * <p>
             * The type of container used to contain this kind of specimen.
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
             * Color of container cap.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getCap() {
                return cap;
            }

            /**
             * <p>
             * The textual description of the kind of container.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getDescription() {
                return description;
            }

            /**
             * <p>
             * The capacity (volume or other measure) of this kind of container.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Quantity}.
             */
            public Quantity getCapacity() {
                return capacity;
            }

            /**
             * <p>
             * The minimum volume to be conditioned in the container.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Element}.
             */
            public Element getMinimumVolume() {
                return minimumVolume;
            }

            /**
             * <p>
             * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, 
             * Citrate, EDTA.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Additive}.
             */
            public List<Additive> getAdditive() {
                return additive;
            }

            /**
             * <p>
             * Special processing that should be applied to the container for this kind of specimen.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getPreparation() {
                return preparation;
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
                        accept(material, "material", visitor);
                        accept(type, "type", visitor);
                        accept(cap, "cap", visitor);
                        accept(description, "description", visitor);
                        accept(capacity, "capacity", visitor);
                        accept(minimumVolume, "minimumVolume", visitor);
                        accept(additive, "additive", visitor, Additive.class);
                        accept(preparation, "preparation", visitor);
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
                // optional
                private CodeableConcept material;
                private CodeableConcept type;
                private CodeableConcept cap;
                private String description;
                private Quantity capacity;
                private Element minimumVolume;
                private List<Additive> additive = new ArrayList<>();
                private String preparation;

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
                 * The type of material of the container.
                 * </p>
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
                 * <p>
                 * The type of container used to contain this kind of specimen.
                 * </p>
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
                 * <p>
                 * Color of container cap.
                 * </p>
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
                 * <p>
                 * The textual description of the kind of container.
                 * </p>
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
                 * <p>
                 * The capacity (volume or other measure) of this kind of container.
                 * </p>
                 * 
                 * @param capacity
                 *     Container capacity
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder capacity(Quantity capacity) {
                    this.capacity = capacity;
                    return this;
                }

                /**
                 * <p>
                 * The minimum volume to be conditioned in the container.
                 * </p>
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
                 * <p>
                 * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, 
                 * Citrate, EDTA.
                 * </p>
                 * <p>
                 * Adds new element(s) to the existing list
                 * </p>
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
                 * <p>
                 * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, 
                 * Citrate, EDTA.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
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
                 * <p>
                 * Special processing that should be applied to the container for this kind of specimen.
                 * </p>
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

                @Override
                public Container build() {
                    return new Container(this);
                }

                private Builder from(Container container) {
                    id = container.id;
                    extension.addAll(container.extension);
                    modifierExtension.addAll(container.modifierExtension);
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
             * <p>
             * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, 
             * Citrate, EDTA.
             * </p>
             */
            public static class Additive extends BackboneElement {
                private final Element additive;

                private volatile int hashCode;

                private Additive(Builder builder) {
                    super(builder);
                    additive = ValidationSupport.requireChoiceElement(builder.additive, "additive", CodeableConcept.class, Reference.class);
                }

                /**
                 * <p>
                 * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, 
                 * Citrate, EDTA.
                 * </p>
                 * 
                 * @return
                 *     An immutable object of type {@link Element}.
                 */
                public Element getAdditive() {
                    return additive;
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
                            accept(additive, "additive", visitor);
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
                    return new Builder(additive).from(this);
                }

                public Builder toBuilder(Element additive) {
                    return new Builder(additive).from(this);
                }

                public static Builder builder(Element additive) {
                    return new Builder(additive);
                }

                public static class Builder extends BackboneElement.Builder {
                    // required
                    private final Element additive;

                    private Builder(Element additive) {
                        super();
                        this.additive = additive;
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
                    public Additive build() {
                        return new Additive(this);
                    }

                    private Builder from(Additive additive) {
                        id = additive.id;
                        extension.addAll(additive.extension);
                        modifierExtension.addAll(additive.modifierExtension);
                        return this;
                    }
                }
            }
        }

        /**
         * <p>
         * Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing 
         * process.
         * </p>
         */
        public static class Handling extends BackboneElement {
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
            }

            /**
             * <p>
             * It qualifies the interval of temperature, which characterizes an occurrence of handling. Conditions that are not 
             * related to temperature may be handled in the instruction element.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getTemperatureQualifier() {
                return temperatureQualifier;
            }

            /**
             * <p>
             * The temperature interval for this set of handling instructions.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Range}.
             */
            public Range getTemperatureRange() {
                return temperatureRange;
            }

            /**
             * <p>
             * The maximum time interval of preservation of the specimen with these conditions.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Duration}.
             */
            public Duration getMaxDuration() {
                return maxDuration;
            }

            /**
             * <p>
             * Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light 
             * exposure'.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getInstruction() {
                return instruction;
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
                        accept(temperatureQualifier, "temperatureQualifier", visitor);
                        accept(temperatureRange, "temperatureRange", visitor);
                        accept(maxDuration, "maxDuration", visitor);
                        accept(instruction, "instruction", visitor);
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
                // optional
                private CodeableConcept temperatureQualifier;
                private Range temperatureRange;
                private Duration maxDuration;
                private String instruction;

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
                 * It qualifies the interval of temperature, which characterizes an occurrence of handling. Conditions that are not 
                 * related to temperature may be handled in the instruction element.
                 * </p>
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
                 * <p>
                 * The temperature interval for this set of handling instructions.
                 * </p>
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
                 * <p>
                 * The maximum time interval of preservation of the specimen with these conditions.
                 * </p>
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
                 * <p>
                 * Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light 
                 * exposure'.
                 * </p>
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

                @Override
                public Handling build() {
                    return new Handling(this);
                }

                private Builder from(Handling handling) {
                    id = handling.id;
                    extension.addAll(handling.extension);
                    modifierExtension.addAll(handling.modifierExtension);
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
