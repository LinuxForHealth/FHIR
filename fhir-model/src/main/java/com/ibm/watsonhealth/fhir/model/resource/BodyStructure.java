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

import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Record details about an anatomical structure. This resource may be used when a coded concept does not provide the 
 * necessary detail needed for the use case.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class BodyStructure extends DomainResource {
    private final List<Identifier> identifier;
    private final Boolean active;
    private final CodeableConcept morphology;
    private final CodeableConcept location;
    private final List<CodeableConcept> locationQualifier;
    private final String description;
    private final List<Attachment> image;
    private final Reference patient;

    private volatile int hashCode;

    private BodyStructure(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        active = builder.active;
        morphology = builder.morphology;
        location = builder.location;
        locationQualifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.locationQualifier, "locationQualifier"));
        description = builder.description;
        image = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.image, "image"));
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
    }

    /**
     * <p>
     * Identifier for this instance of the anatomical structure.
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
     * Whether this body site is in active use.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * <p>
     * The kind of structure being represented by the body structure at `BodyStructure.location`. This can define both normal 
     * and abnormal morphologies.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getMorphology() {
        return morphology;
    }

    /**
     * <p>
     * The anatomical location or region of the specimen, lesion, or body structure.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getLocation() {
        return location;
    }

    /**
     * <p>
     * Qualifier to refine the anatomical location. These include qualifiers for laterality, relative location, 
     * directionality, number, and plane.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getLocationQualifier() {
        return locationQualifier;
    }

    /**
     * <p>
     * A summary, characterization or explanation of the body structure.
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
     * Image or images used to identify a location.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Attachment}.
     */
    public List<Attachment> getImage() {
        return image;
    }

    /**
     * <p>
     * The person to which the body site belongs.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPatient() {
        return patient;
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
                accept(active, "active", visitor);
                accept(morphology, "morphology", visitor);
                accept(location, "location", visitor);
                accept(locationQualifier, "locationQualifier", visitor, CodeableConcept.class);
                accept(description, "description", visitor);
                accept(image, "image", visitor, Attachment.class);
                accept(patient, "patient", visitor);
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
        BodyStructure other = (BodyStructure) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(active, other.active) && 
            Objects.equals(morphology, other.morphology) && 
            Objects.equals(location, other.location) && 
            Objects.equals(locationQualifier, other.locationQualifier) && 
            Objects.equals(description, other.description) && 
            Objects.equals(image, other.image) && 
            Objects.equals(patient, other.patient);
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
                active, 
                morphology, 
                location, 
                locationQualifier, 
                description, 
                image, 
                patient);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder(Reference patient) {
        Builder builder = new Builder();
        builder.patient(patient);
        return builder;
    }

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private Boolean active;
        private CodeableConcept morphology;
        private CodeableConcept location;
        private List<CodeableConcept> locationQualifier = new ArrayList<>();
        private String description;
        private List<Attachment> image = new ArrayList<>();
        private Reference patient;

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
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * Adds new element(s) to existing list
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
         * Identifier for this instance of the anatomical structure.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     Bodystructure identifier
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
         * Identifier for this instance of the anatomical structure.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Bodystructure identifier
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
         * Whether this body site is in active use.
         * </p>
         * 
         * @param active
         *     Whether this record is in active use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        /**
         * <p>
         * The kind of structure being represented by the body structure at `BodyStructure.location`. This can define both normal 
         * and abnormal morphologies.
         * </p>
         * 
         * @param morphology
         *     Kind of Structure
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder morphology(CodeableConcept morphology) {
            this.morphology = morphology;
            return this;
        }

        /**
         * <p>
         * The anatomical location or region of the specimen, lesion, or body structure.
         * </p>
         * 
         * @param location
         *     Body site
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder location(CodeableConcept location) {
            this.location = location;
            return this;
        }

        /**
         * <p>
         * Qualifier to refine the anatomical location. These include qualifiers for laterality, relative location, 
         * directionality, number, and plane.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param locationQualifier
         *     Body site modifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder locationQualifier(CodeableConcept... locationQualifier) {
            for (CodeableConcept value : locationQualifier) {
                this.locationQualifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Qualifier to refine the anatomical location. These include qualifiers for laterality, relative location, 
         * directionality, number, and plane.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param locationQualifier
         *     Body site modifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder locationQualifier(Collection<CodeableConcept> locationQualifier) {
            this.locationQualifier = new ArrayList<>(locationQualifier);
            return this;
        }

        /**
         * <p>
         * A summary, characterization or explanation of the body structure.
         * </p>
         * 
         * @param description
         *     Text description
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
         * Image or images used to identify a location.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param image
         *     Attached images
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder image(Attachment... image) {
            for (Attachment value : image) {
                this.image.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Image or images used to identify a location.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param image
         *     Attached images
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder image(Collection<Attachment> image) {
            this.image = new ArrayList<>(image);
            return this;
        }

        /**
         * <p>
         * The person to which the body site belongs.
         * </p>
         * 
         * @param patient
         *     Who this is about
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        @Override
        public BodyStructure build() {
            return new BodyStructure(this);
        }

        protected Builder from(BodyStructure bodyStructure) {
            super.from(bodyStructure);
            identifier.addAll(bodyStructure.identifier);
            active = bodyStructure.active;
            morphology = bodyStructure.morphology;
            location = bodyStructure.location;
            locationQualifier.addAll(bodyStructure.locationQualifier);
            description = bodyStructure.description;
            image.addAll(bodyStructure.image);
            patient = bodyStructure.patient;
            return this;
        }
    }
}
