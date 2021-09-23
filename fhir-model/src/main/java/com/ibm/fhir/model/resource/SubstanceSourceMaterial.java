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
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Source material shall capture information on the taxonomic and anatomical origins as well as the fraction of a 
 * material that can result in or can be modified to form a substance. This set of data elements shall be used to define 
 * polymer substances isolated from biological matrices. Taxonomic and anatomical origins shall be described using a 
 * controlled vocabulary as required. This information is captured for naturally derived polymers ( . starch) and 
 * structurally diverse substances. For Organisms belonging to the Kingdom Plantae the Substance level defines the fresh 
 * material of a single species or infraspecies, the Herbal Drug and the Herbal preparation. For Herbal preparations, the 
 * fraction information will be captured at the Substance information level and additional information for herbal 
 * extracts will be captured at the Specified Substance Group 1 information level. See for further explanation the 
 * Substance Class: Structurally Diverse and the herbal annex.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubstanceSourceMaterial extends DomainResource {
    @Summary
    private final CodeableConcept sourceMaterialClass;
    @Summary
    private final CodeableConcept sourceMaterialType;
    @Summary
    private final CodeableConcept sourceMaterialState;
    @Summary
    private final Identifier organismId;
    @Summary
    private final String organismName;
    @Summary
    private final List<Identifier> parentSubstanceId;
    @Summary
    private final List<String> parentSubstanceName;
    @Summary
    private final List<CodeableConcept> countryOfOrigin;
    @Summary
    private final List<String> geographicalLocation;
    @Summary
    private final CodeableConcept developmentStage;
    @Summary
    private final List<FractionDescription> fractionDescription;
    @Summary
    private final Organism organism;
    @Summary
    private final List<PartDescription> partDescription;

    private SubstanceSourceMaterial(Builder builder) {
        super(builder);
        sourceMaterialClass = builder.sourceMaterialClass;
        sourceMaterialType = builder.sourceMaterialType;
        sourceMaterialState = builder.sourceMaterialState;
        organismId = builder.organismId;
        organismName = builder.organismName;
        parentSubstanceId = Collections.unmodifiableList(builder.parentSubstanceId);
        parentSubstanceName = Collections.unmodifiableList(builder.parentSubstanceName);
        countryOfOrigin = Collections.unmodifiableList(builder.countryOfOrigin);
        geographicalLocation = Collections.unmodifiableList(builder.geographicalLocation);
        developmentStage = builder.developmentStage;
        fractionDescription = Collections.unmodifiableList(builder.fractionDescription);
        organism = builder.organism;
        partDescription = Collections.unmodifiableList(builder.partDescription);
    }

    /**
     * General high level classification of the source material specific to the origin of the material.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSourceMaterialClass() {
        return sourceMaterialClass;
    }

    /**
     * The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause 
     * refers to the class of infectious agent.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSourceMaterialType() {
        return sourceMaterialType;
    }

    /**
     * The state of the source material when extracted.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSourceMaterialState() {
        return sourceMaterialState;
    }

    /**
     * The unique identifier associated with the source material parent organism shall be specified.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getOrganismId() {
        return organismId;
    }

    /**
     * The organism accepted Scientific name shall be provided based on the organism taxonomy.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getOrganismName() {
        return organismName;
    }

    /**
     * The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or 
     * Ginkgo biloba L. (Whole plant).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getParentSubstanceId() {
        return parentSubstanceId;
    }

    /**
     * The parent substance of the Herbal Drug, or Herbal preparation.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getParentSubstanceName() {
        return parentSubstanceName;
    }

    /**
     * The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in 
     * accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides 
     * information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCountryOfOrigin() {
        return countryOfOrigin;
    }

    /**
     * The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getGeographicalLocation() {
        return geographicalLocation;
    }

    /**
     * Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the 
     * substance is significantly different in these stages (e.g. foetal bovine serum).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getDevelopmentStage() {
        return developmentStage;
    }

    /**
     * Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary 
     * to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information 
     * will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for 
     * Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information 
     * level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance 
     * Group 1 levels.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link FractionDescription} that may be empty.
     */
    public List<FractionDescription> getFractionDescription() {
        return fractionDescription;
    }

    /**
     * This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be 
     * specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., 
     * Leaf.
     * 
     * @return
     *     An immutable object of type {@link Organism} that may be null.
     */
    public Organism getOrganism() {
        return organism;
    }

    /**
     * To do.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link PartDescription} that may be empty.
     */
    public List<PartDescription> getPartDescription() {
        return partDescription;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (sourceMaterialClass != null) || 
            (sourceMaterialType != null) || 
            (sourceMaterialState != null) || 
            (organismId != null) || 
            (organismName != null) || 
            !parentSubstanceId.isEmpty() || 
            !parentSubstanceName.isEmpty() || 
            !countryOfOrigin.isEmpty() || 
            !geographicalLocation.isEmpty() || 
            (developmentStage != null) || 
            !fractionDescription.isEmpty() || 
            (organism != null) || 
            !partDescription.isEmpty();
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
                accept(sourceMaterialClass, "sourceMaterialClass", visitor);
                accept(sourceMaterialType, "sourceMaterialType", visitor);
                accept(sourceMaterialState, "sourceMaterialState", visitor);
                accept(organismId, "organismId", visitor);
                accept(organismName, "organismName", visitor);
                accept(parentSubstanceId, "parentSubstanceId", visitor, Identifier.class);
                accept(parentSubstanceName, "parentSubstanceName", visitor, String.class);
                accept(countryOfOrigin, "countryOfOrigin", visitor, CodeableConcept.class);
                accept(geographicalLocation, "geographicalLocation", visitor, String.class);
                accept(developmentStage, "developmentStage", visitor);
                accept(fractionDescription, "fractionDescription", visitor, FractionDescription.class);
                accept(organism, "organism", visitor);
                accept(partDescription, "partDescription", visitor, PartDescription.class);
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
        SubstanceSourceMaterial other = (SubstanceSourceMaterial) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(sourceMaterialClass, other.sourceMaterialClass) && 
            Objects.equals(sourceMaterialType, other.sourceMaterialType) && 
            Objects.equals(sourceMaterialState, other.sourceMaterialState) && 
            Objects.equals(organismId, other.organismId) && 
            Objects.equals(organismName, other.organismName) && 
            Objects.equals(parentSubstanceId, other.parentSubstanceId) && 
            Objects.equals(parentSubstanceName, other.parentSubstanceName) && 
            Objects.equals(countryOfOrigin, other.countryOfOrigin) && 
            Objects.equals(geographicalLocation, other.geographicalLocation) && 
            Objects.equals(developmentStage, other.developmentStage) && 
            Objects.equals(fractionDescription, other.fractionDescription) && 
            Objects.equals(organism, other.organism) && 
            Objects.equals(partDescription, other.partDescription);
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
                sourceMaterialClass, 
                sourceMaterialType, 
                sourceMaterialState, 
                organismId, 
                organismName, 
                parentSubstanceId, 
                parentSubstanceName, 
                countryOfOrigin, 
                geographicalLocation, 
                developmentStage, 
                fractionDescription, 
                organism, 
                partDescription);
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
        private CodeableConcept sourceMaterialClass;
        private CodeableConcept sourceMaterialType;
        private CodeableConcept sourceMaterialState;
        private Identifier organismId;
        private String organismName;
        private List<Identifier> parentSubstanceId = new ArrayList<>();
        private List<String> parentSubstanceName = new ArrayList<>();
        private List<CodeableConcept> countryOfOrigin = new ArrayList<>();
        private List<String> geographicalLocation = new ArrayList<>();
        private CodeableConcept developmentStage;
        private List<FractionDescription> fractionDescription = new ArrayList<>();
        private Organism organism;
        private List<PartDescription> partDescription = new ArrayList<>();

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
         * General high level classification of the source material specific to the origin of the material.
         * 
         * @param sourceMaterialClass
         *     General high level classification of the source material specific to the origin of the material
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sourceMaterialClass(CodeableConcept sourceMaterialClass) {
            this.sourceMaterialClass = sourceMaterialClass;
            return this;
        }

        /**
         * The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause 
         * refers to the class of infectious agent.
         * 
         * @param sourceMaterialType
         *     The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause 
         *     refers to the class of infectious agent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sourceMaterialType(CodeableConcept sourceMaterialType) {
            this.sourceMaterialType = sourceMaterialType;
            return this;
        }

        /**
         * The state of the source material when extracted.
         * 
         * @param sourceMaterialState
         *     The state of the source material when extracted
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sourceMaterialState(CodeableConcept sourceMaterialState) {
            this.sourceMaterialState = sourceMaterialState;
            return this;
        }

        /**
         * The unique identifier associated with the source material parent organism shall be specified.
         * 
         * @param organismId
         *     The unique identifier associated with the source material parent organism shall be specified
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder organismId(Identifier organismId) {
            this.organismId = organismId;
            return this;
        }

        /**
         * Convenience method for setting {@code organismName}.
         * 
         * @param organismName
         *     The organism accepted Scientific name shall be provided based on the organism taxonomy
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #organismName(com.ibm.fhir.model.type.String)
         */
        public Builder organismName(java.lang.String organismName) {
            this.organismName = (organismName == null) ? null : String.of(organismName);
            return this;
        }

        /**
         * The organism accepted Scientific name shall be provided based on the organism taxonomy.
         * 
         * @param organismName
         *     The organism accepted Scientific name shall be provided based on the organism taxonomy
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder organismName(String organismName) {
            this.organismName = organismName;
            return this;
        }

        /**
         * The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or 
         * Ginkgo biloba L. (Whole plant).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param parentSubstanceId
         *     The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or 
         *     Ginkgo biloba L. (Whole plant)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder parentSubstanceId(Identifier... parentSubstanceId) {
            for (Identifier value : parentSubstanceId) {
                this.parentSubstanceId.add(value);
            }
            return this;
        }

        /**
         * The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or 
         * Ginkgo biloba L. (Whole plant).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param parentSubstanceId
         *     The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or 
         *     Ginkgo biloba L. (Whole plant)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder parentSubstanceId(Collection<Identifier> parentSubstanceId) {
            this.parentSubstanceId = new ArrayList<>(parentSubstanceId);
            return this;
        }

        /**
         * Convenience method for setting {@code parentSubstanceName}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param parentSubstanceName
         *     The parent substance of the Herbal Drug, or Herbal preparation
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #parentSubstanceName(com.ibm.fhir.model.type.String)
         */
        public Builder parentSubstanceName(java.lang.String... parentSubstanceName) {
            for (java.lang.String value : parentSubstanceName) {
                this.parentSubstanceName.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * The parent substance of the Herbal Drug, or Herbal preparation.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param parentSubstanceName
         *     The parent substance of the Herbal Drug, or Herbal preparation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder parentSubstanceName(String... parentSubstanceName) {
            for (String value : parentSubstanceName) {
                this.parentSubstanceName.add(value);
            }
            return this;
        }

        /**
         * The parent substance of the Herbal Drug, or Herbal preparation.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param parentSubstanceName
         *     The parent substance of the Herbal Drug, or Herbal preparation
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder parentSubstanceName(Collection<String> parentSubstanceName) {
            this.parentSubstanceName = new ArrayList<>(parentSubstanceName);
            return this;
        }

        /**
         * The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in 
         * accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides 
         * information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param countryOfOrigin
         *     The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in 
         *     accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides 
         *     information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder countryOfOrigin(CodeableConcept... countryOfOrigin) {
            for (CodeableConcept value : countryOfOrigin) {
                this.countryOfOrigin.add(value);
            }
            return this;
        }

        /**
         * The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in 
         * accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides 
         * information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param countryOfOrigin
         *     The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in 
         *     accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides 
         *     information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder countryOfOrigin(Collection<CodeableConcept> countryOfOrigin) {
            this.countryOfOrigin = new ArrayList<>(countryOfOrigin);
            return this;
        }

        /**
         * Convenience method for setting {@code geographicalLocation}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param geographicalLocation
         *     The place/region where the plant is harvested or the places/regions where the animal source material has its habitat
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #geographicalLocation(com.ibm.fhir.model.type.String)
         */
        public Builder geographicalLocation(java.lang.String... geographicalLocation) {
            for (java.lang.String value : geographicalLocation) {
                this.geographicalLocation.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param geographicalLocation
         *     The place/region where the plant is harvested or the places/regions where the animal source material has its habitat
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder geographicalLocation(String... geographicalLocation) {
            for (String value : geographicalLocation) {
                this.geographicalLocation.add(value);
            }
            return this;
        }

        /**
         * The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param geographicalLocation
         *     The place/region where the plant is harvested or the places/regions where the animal source material has its habitat
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder geographicalLocation(Collection<String> geographicalLocation) {
            this.geographicalLocation = new ArrayList<>(geographicalLocation);
            return this;
        }

        /**
         * Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the 
         * substance is significantly different in these stages (e.g. foetal bovine serum).
         * 
         * @param developmentStage
         *     Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the 
         *     substance is significantly different in these stages (e.g. foetal bovine serum)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder developmentStage(CodeableConcept developmentStage) {
            this.developmentStage = developmentStage;
            return this;
        }

        /**
         * Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary 
         * to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information 
         * will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for 
         * Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information 
         * level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance 
         * Group 1 levels.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param fractionDescription
         *     Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary 
         *     to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information 
         *     will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for 
         *     Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information 
         *     level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance 
         *     Group 1 levels
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder fractionDescription(FractionDescription... fractionDescription) {
            for (FractionDescription value : fractionDescription) {
                this.fractionDescription.add(value);
            }
            return this;
        }

        /**
         * Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary 
         * to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information 
         * will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for 
         * Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information 
         * level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance 
         * Group 1 levels.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param fractionDescription
         *     Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary 
         *     to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information 
         *     will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for 
         *     Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information 
         *     level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance 
         *     Group 1 levels
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder fractionDescription(Collection<FractionDescription> fractionDescription) {
            this.fractionDescription = new ArrayList<>(fractionDescription);
            return this;
        }

        /**
         * This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be 
         * specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., 
         * Leaf.
         * 
         * @param organism
         *     This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be 
         *     specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., 
         *     Leaf
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder organism(Organism organism) {
            this.organism = organism;
            return this;
        }

        /**
         * To do.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param partDescription
         *     To do
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partDescription(PartDescription... partDescription) {
            for (PartDescription value : partDescription) {
                this.partDescription.add(value);
            }
            return this;
        }

        /**
         * To do.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param partDescription
         *     To do
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder partDescription(Collection<PartDescription> partDescription) {
            this.partDescription = new ArrayList<>(partDescription);
            return this;
        }

        /**
         * Build the {@link SubstanceSourceMaterial}
         * 
         * @return
         *     An immutable object of type {@link SubstanceSourceMaterial}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SubstanceSourceMaterial per the base specification
         */
        @Override
        public SubstanceSourceMaterial build() {
            SubstanceSourceMaterial substanceSourceMaterial = new SubstanceSourceMaterial(this);
            if (validating) {
                validate(substanceSourceMaterial);
            }
            return substanceSourceMaterial;
        }

        protected void validate(SubstanceSourceMaterial substanceSourceMaterial) {
            super.validate(substanceSourceMaterial);
            ValidationSupport.checkList(substanceSourceMaterial.parentSubstanceId, "parentSubstanceId", Identifier.class);
            ValidationSupport.checkList(substanceSourceMaterial.parentSubstanceName, "parentSubstanceName", String.class);
            ValidationSupport.checkList(substanceSourceMaterial.countryOfOrigin, "countryOfOrigin", CodeableConcept.class);
            ValidationSupport.checkList(substanceSourceMaterial.geographicalLocation, "geographicalLocation", String.class);
            ValidationSupport.checkList(substanceSourceMaterial.fractionDescription, "fractionDescription", FractionDescription.class);
            ValidationSupport.checkList(substanceSourceMaterial.partDescription, "partDescription", PartDescription.class);
        }

        protected Builder from(SubstanceSourceMaterial substanceSourceMaterial) {
            super.from(substanceSourceMaterial);
            sourceMaterialClass = substanceSourceMaterial.sourceMaterialClass;
            sourceMaterialType = substanceSourceMaterial.sourceMaterialType;
            sourceMaterialState = substanceSourceMaterial.sourceMaterialState;
            organismId = substanceSourceMaterial.organismId;
            organismName = substanceSourceMaterial.organismName;
            parentSubstanceId.addAll(substanceSourceMaterial.parentSubstanceId);
            parentSubstanceName.addAll(substanceSourceMaterial.parentSubstanceName);
            countryOfOrigin.addAll(substanceSourceMaterial.countryOfOrigin);
            geographicalLocation.addAll(substanceSourceMaterial.geographicalLocation);
            developmentStage = substanceSourceMaterial.developmentStage;
            fractionDescription.addAll(substanceSourceMaterial.fractionDescription);
            organism = substanceSourceMaterial.organism;
            partDescription.addAll(substanceSourceMaterial.partDescription);
            return this;
        }
    }

    /**
     * Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary 
     * to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information 
     * will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for 
     * Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information 
     * level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance 
     * Group 1 levels.
     */
    public static class FractionDescription extends BackboneElement {
        @Summary
        private final String fraction;
        @Summary
        private final CodeableConcept materialType;

        private FractionDescription(Builder builder) {
            super(builder);
            fraction = builder.fraction;
            materialType = builder.materialType;
        }

        /**
         * This element is capturing information about the fraction of a plant part, or human plasma for fractionation.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getFraction() {
            return fraction;
        }

        /**
         * The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts 
         * (liquid/dry) is described in Specified Substance Group 1.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getMaterialType() {
            return materialType;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (fraction != null) || 
                (materialType != null);
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
                    accept(fraction, "fraction", visitor);
                    accept(materialType, "materialType", visitor);
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
            FractionDescription other = (FractionDescription) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(fraction, other.fraction) && 
                Objects.equals(materialType, other.materialType);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    fraction, 
                    materialType);
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
            private String fraction;
            private CodeableConcept materialType;

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
             * Convenience method for setting {@code fraction}.
             * 
             * @param fraction
             *     This element is capturing information about the fraction of a plant part, or human plasma for fractionation
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #fraction(com.ibm.fhir.model.type.String)
             */
            public Builder fraction(java.lang.String fraction) {
                this.fraction = (fraction == null) ? null : String.of(fraction);
                return this;
            }

            /**
             * This element is capturing information about the fraction of a plant part, or human plasma for fractionation.
             * 
             * @param fraction
             *     This element is capturing information about the fraction of a plant part, or human plasma for fractionation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder fraction(String fraction) {
                this.fraction = fraction;
                return this;
            }

            /**
             * The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts 
             * (liquid/dry) is described in Specified Substance Group 1.
             * 
             * @param materialType
             *     The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts 
             *     (liquid/dry) is described in Specified Substance Group 1
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder materialType(CodeableConcept materialType) {
                this.materialType = materialType;
                return this;
            }

            /**
             * Build the {@link FractionDescription}
             * 
             * @return
             *     An immutable object of type {@link FractionDescription}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid FractionDescription per the base specification
             */
            @Override
            public FractionDescription build() {
                FractionDescription fractionDescription = new FractionDescription(this);
                if (validating) {
                    validate(fractionDescription);
                }
                return fractionDescription;
            }

            protected void validate(FractionDescription fractionDescription) {
                super.validate(fractionDescription);
                ValidationSupport.requireValueOrChildren(fractionDescription);
            }

            protected Builder from(FractionDescription fractionDescription) {
                super.from(fractionDescription);
                fraction = fractionDescription.fraction;
                materialType = fractionDescription.materialType;
                return this;
            }
        }
    }

    /**
     * This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be 
     * specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., 
     * Leaf.
     */
    public static class Organism extends BackboneElement {
        @Summary
        private final CodeableConcept family;
        @Summary
        private final CodeableConcept genus;
        @Summary
        private final CodeableConcept species;
        @Summary
        private final CodeableConcept intraspecificType;
        @Summary
        private final String intraspecificDescription;
        @Summary
        private final List<Author> author;
        @Summary
        private final Hybrid hybrid;
        @Summary
        private final OrganismGeneral organismGeneral;

        private Organism(Builder builder) {
            super(builder);
            family = builder.family;
            genus = builder.genus;
            species = builder.species;
            intraspecificType = builder.intraspecificType;
            intraspecificDescription = builder.intraspecificDescription;
            author = Collections.unmodifiableList(builder.author);
            hybrid = builder.hybrid;
            organismGeneral = builder.organismGeneral;
        }

        /**
         * The family of an organism shall be specified.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getFamily() {
            return family;
        }

        /**
         * The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal 
         * scientific name; it is present in names for genera, species and infraspecies.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getGenus() {
            return genus;
        }

        /**
         * The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is 
         * present in names for species and infraspecies.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getSpecies() {
            return species;
        }

        /**
         * The Intraspecific type of an organism shall be specified.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getIntraspecificType() {
            return intraspecificType;
        }

        /**
         * The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza 
         * Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getIntraspecificDescription() {
            return intraspecificDescription;
        }

        /**
         * 4.9.13.6.1 Author type (Conditional).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Author} that may be empty.
         */
        public List<Author> getAuthor() {
            return author;
        }

        /**
         * 4.9.13.8.1 Hybrid species maternal organism ID (Optional).
         * 
         * @return
         *     An immutable object of type {@link Hybrid} that may be null.
         */
        public Hybrid getHybrid() {
            return hybrid;
        }

        /**
         * 4.9.13.7.1 Kingdom (Conditional).
         * 
         * @return
         *     An immutable object of type {@link OrganismGeneral} that may be null.
         */
        public OrganismGeneral getOrganismGeneral() {
            return organismGeneral;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (family != null) || 
                (genus != null) || 
                (species != null) || 
                (intraspecificType != null) || 
                (intraspecificDescription != null) || 
                !author.isEmpty() || 
                (hybrid != null) || 
                (organismGeneral != null);
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
                    accept(family, "family", visitor);
                    accept(genus, "genus", visitor);
                    accept(species, "species", visitor);
                    accept(intraspecificType, "intraspecificType", visitor);
                    accept(intraspecificDescription, "intraspecificDescription", visitor);
                    accept(author, "author", visitor, Author.class);
                    accept(hybrid, "hybrid", visitor);
                    accept(organismGeneral, "organismGeneral", visitor);
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
            Organism other = (Organism) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(family, other.family) && 
                Objects.equals(genus, other.genus) && 
                Objects.equals(species, other.species) && 
                Objects.equals(intraspecificType, other.intraspecificType) && 
                Objects.equals(intraspecificDescription, other.intraspecificDescription) && 
                Objects.equals(author, other.author) && 
                Objects.equals(hybrid, other.hybrid) && 
                Objects.equals(organismGeneral, other.organismGeneral);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    family, 
                    genus, 
                    species, 
                    intraspecificType, 
                    intraspecificDescription, 
                    author, 
                    hybrid, 
                    organismGeneral);
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
            private CodeableConcept family;
            private CodeableConcept genus;
            private CodeableConcept species;
            private CodeableConcept intraspecificType;
            private String intraspecificDescription;
            private List<Author> author = new ArrayList<>();
            private Hybrid hybrid;
            private OrganismGeneral organismGeneral;

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
             * The family of an organism shall be specified.
             * 
             * @param family
             *     The family of an organism shall be specified
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder family(CodeableConcept family) {
                this.family = family;
                return this;
            }

            /**
             * The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal 
             * scientific name; it is present in names for genera, species and infraspecies.
             * 
             * @param genus
             *     The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal 
             *     scientific name; it is present in names for genera, species and infraspecies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder genus(CodeableConcept genus) {
                this.genus = genus;
                return this;
            }

            /**
             * The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is 
             * present in names for species and infraspecies.
             * 
             * @param species
             *     The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is 
             *     present in names for species and infraspecies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder species(CodeableConcept species) {
                this.species = species;
                return this;
            }

            /**
             * The Intraspecific type of an organism shall be specified.
             * 
             * @param intraspecificType
             *     The Intraspecific type of an organism shall be specified
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder intraspecificType(CodeableConcept intraspecificType) {
                this.intraspecificType = intraspecificType;
                return this;
            }

            /**
             * Convenience method for setting {@code intraspecificDescription}.
             * 
             * @param intraspecificDescription
             *     The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza 
             *     Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #intraspecificDescription(com.ibm.fhir.model.type.String)
             */
            public Builder intraspecificDescription(java.lang.String intraspecificDescription) {
                this.intraspecificDescription = (intraspecificDescription == null) ? null : String.of(intraspecificDescription);
                return this;
            }

            /**
             * The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza 
             * Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention.
             * 
             * @param intraspecificDescription
             *     The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza 
             *     Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder intraspecificDescription(String intraspecificDescription) {
                this.intraspecificDescription = intraspecificDescription;
                return this;
            }

            /**
             * 4.9.13.6.1 Author type (Conditional).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param author
             *     4.9.13.6.1 Author type (Conditional)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder author(Author... author) {
                for (Author value : author) {
                    this.author.add(value);
                }
                return this;
            }

            /**
             * 4.9.13.6.1 Author type (Conditional).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param author
             *     4.9.13.6.1 Author type (Conditional)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder author(Collection<Author> author) {
                this.author = new ArrayList<>(author);
                return this;
            }

            /**
             * 4.9.13.8.1 Hybrid species maternal organism ID (Optional).
             * 
             * @param hybrid
             *     4.9.13.8.1 Hybrid species maternal organism ID (Optional)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder hybrid(Hybrid hybrid) {
                this.hybrid = hybrid;
                return this;
            }

            /**
             * 4.9.13.7.1 Kingdom (Conditional).
             * 
             * @param organismGeneral
             *     4.9.13.7.1 Kingdom (Conditional)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder organismGeneral(OrganismGeneral organismGeneral) {
                this.organismGeneral = organismGeneral;
                return this;
            }

            /**
             * Build the {@link Organism}
             * 
             * @return
             *     An immutable object of type {@link Organism}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Organism per the base specification
             */
            @Override
            public Organism build() {
                Organism organism = new Organism(this);
                if (validating) {
                    validate(organism);
                }
                return organism;
            }

            protected void validate(Organism organism) {
                super.validate(organism);
                ValidationSupport.checkList(organism.author, "author", Author.class);
                ValidationSupport.requireValueOrChildren(organism);
            }

            protected Builder from(Organism organism) {
                super.from(organism);
                family = organism.family;
                genus = organism.genus;
                species = organism.species;
                intraspecificType = organism.intraspecificType;
                intraspecificDescription = organism.intraspecificDescription;
                author.addAll(organism.author);
                hybrid = organism.hybrid;
                organismGeneral = organism.organismGeneral;
                return this;
            }
        }

        /**
         * 4.9.13.6.1 Author type (Conditional).
         */
        public static class Author extends BackboneElement {
            @Summary
            private final CodeableConcept authorType;
            @Summary
            private final String authorDescription;

            private Author(Builder builder) {
                super(builder);
                authorType = builder.authorType;
                authorDescription = builder.authorDescription;
            }

            /**
             * The type of author of an organism species shall be specified. The parenthetical author of an organism species refers 
             * to the first author who published the plant/animal name (of any rank). The primary author of an organism species 
             * refers to the first author(s), who validly published the plant/animal name.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getAuthorType() {
                return authorType;
            }

            /**
             * The author of an organism species shall be specified. The author year of an organism shall also be specified when 
             * applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any 
             * rank).
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getAuthorDescription() {
                return authorDescription;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (authorType != null) || 
                    (authorDescription != null);
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
                        accept(authorType, "authorType", visitor);
                        accept(authorDescription, "authorDescription", visitor);
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
                Author other = (Author) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(authorType, other.authorType) && 
                    Objects.equals(authorDescription, other.authorDescription);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        authorType, 
                        authorDescription);
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
                private CodeableConcept authorType;
                private String authorDescription;

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
                 * The type of author of an organism species shall be specified. The parenthetical author of an organism species refers 
                 * to the first author who published the plant/animal name (of any rank). The primary author of an organism species 
                 * refers to the first author(s), who validly published the plant/animal name.
                 * 
                 * @param authorType
                 *     The type of author of an organism species shall be specified. The parenthetical author of an organism species refers 
                 *     to the first author who published the plant/animal name (of any rank). The primary author of an organism species 
                 *     refers to the first author(s), who validly published the plant/animal name
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder authorType(CodeableConcept authorType) {
                    this.authorType = authorType;
                    return this;
                }

                /**
                 * Convenience method for setting {@code authorDescription}.
                 * 
                 * @param authorDescription
                 *     The author of an organism species shall be specified. The author year of an organism shall also be specified when 
                 *     applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #authorDescription(com.ibm.fhir.model.type.String)
                 */
                public Builder authorDescription(java.lang.String authorDescription) {
                    this.authorDescription = (authorDescription == null) ? null : String.of(authorDescription);
                    return this;
                }

                /**
                 * The author of an organism species shall be specified. The author year of an organism shall also be specified when 
                 * applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any 
                 * rank).
                 * 
                 * @param authorDescription
                 *     The author of an organism species shall be specified. The author year of an organism shall also be specified when 
                 *     applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder authorDescription(String authorDescription) {
                    this.authorDescription = authorDescription;
                    return this;
                }

                /**
                 * Build the {@link Author}
                 * 
                 * @return
                 *     An immutable object of type {@link Author}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Author per the base specification
                 */
                @Override
                public Author build() {
                    Author author = new Author(this);
                    if (validating) {
                        validate(author);
                    }
                    return author;
                }

                protected void validate(Author author) {
                    super.validate(author);
                    ValidationSupport.requireValueOrChildren(author);
                }

                protected Builder from(Author author) {
                    super.from(author);
                    authorType = author.authorType;
                    authorDescription = author.authorDescription;
                    return this;
                }
            }
        }

        /**
         * 4.9.13.8.1 Hybrid species maternal organism ID (Optional).
         */
        public static class Hybrid extends BackboneElement {
            @Summary
            private final String maternalOrganismId;
            @Summary
            private final String maternalOrganismName;
            @Summary
            private final String paternalOrganismId;
            @Summary
            private final String paternalOrganismName;
            @Summary
            private final CodeableConcept hybridType;

            private Hybrid(Builder builder) {
                super(builder);
                maternalOrganismId = builder.maternalOrganismId;
                maternalOrganismName = builder.maternalOrganismName;
                paternalOrganismId = builder.paternalOrganismId;
                paternalOrganismName = builder.paternalOrganismName;
                hybridType = builder.hybridType;
            }

            /**
             * The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled 
             * vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal 
             * and which is paternal.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getMaternalOrganismId() {
                return maternalOrganismId;
            }

            /**
             * The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t 
             * always known, and it is unlikely that it will be known which is maternal and which is paternal.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getMaternalOrganismName() {
                return maternalOrganismName;
            }

            /**
             * The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled 
             * vocabulary.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getPaternalOrganismId() {
                return paternalOrganismId;
            }

            /**
             * The name of the paternal species constituting the hybrid organism shall be specified.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getPaternalOrganismName() {
                return paternalOrganismName;
            }

            /**
             * The hybrid type of an organism shall be specified.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getHybridType() {
                return hybridType;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (maternalOrganismId != null) || 
                    (maternalOrganismName != null) || 
                    (paternalOrganismId != null) || 
                    (paternalOrganismName != null) || 
                    (hybridType != null);
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
                        accept(maternalOrganismId, "maternalOrganismId", visitor);
                        accept(maternalOrganismName, "maternalOrganismName", visitor);
                        accept(paternalOrganismId, "paternalOrganismId", visitor);
                        accept(paternalOrganismName, "paternalOrganismName", visitor);
                        accept(hybridType, "hybridType", visitor);
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
                Hybrid other = (Hybrid) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(maternalOrganismId, other.maternalOrganismId) && 
                    Objects.equals(maternalOrganismName, other.maternalOrganismName) && 
                    Objects.equals(paternalOrganismId, other.paternalOrganismId) && 
                    Objects.equals(paternalOrganismName, other.paternalOrganismName) && 
                    Objects.equals(hybridType, other.hybridType);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        maternalOrganismId, 
                        maternalOrganismName, 
                        paternalOrganismId, 
                        paternalOrganismName, 
                        hybridType);
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
                private String maternalOrganismId;
                private String maternalOrganismName;
                private String paternalOrganismId;
                private String paternalOrganismName;
                private CodeableConcept hybridType;

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
                 * Convenience method for setting {@code maternalOrganismId}.
                 * 
                 * @param maternalOrganismId
                 *     The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled 
                 *     vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal 
                 *     and which is paternal
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #maternalOrganismId(com.ibm.fhir.model.type.String)
                 */
                public Builder maternalOrganismId(java.lang.String maternalOrganismId) {
                    this.maternalOrganismId = (maternalOrganismId == null) ? null : String.of(maternalOrganismId);
                    return this;
                }

                /**
                 * The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled 
                 * vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal 
                 * and which is paternal.
                 * 
                 * @param maternalOrganismId
                 *     The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled 
                 *     vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal 
                 *     and which is paternal
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder maternalOrganismId(String maternalOrganismId) {
                    this.maternalOrganismId = maternalOrganismId;
                    return this;
                }

                /**
                 * Convenience method for setting {@code maternalOrganismName}.
                 * 
                 * @param maternalOrganismName
                 *     The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t 
                 *     always known, and it is unlikely that it will be known which is maternal and which is paternal
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #maternalOrganismName(com.ibm.fhir.model.type.String)
                 */
                public Builder maternalOrganismName(java.lang.String maternalOrganismName) {
                    this.maternalOrganismName = (maternalOrganismName == null) ? null : String.of(maternalOrganismName);
                    return this;
                }

                /**
                 * The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t 
                 * always known, and it is unlikely that it will be known which is maternal and which is paternal.
                 * 
                 * @param maternalOrganismName
                 *     The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t 
                 *     always known, and it is unlikely that it will be known which is maternal and which is paternal
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder maternalOrganismName(String maternalOrganismName) {
                    this.maternalOrganismName = maternalOrganismName;
                    return this;
                }

                /**
                 * Convenience method for setting {@code paternalOrganismId}.
                 * 
                 * @param paternalOrganismId
                 *     The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled 
                 *     vocabulary
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #paternalOrganismId(com.ibm.fhir.model.type.String)
                 */
                public Builder paternalOrganismId(java.lang.String paternalOrganismId) {
                    this.paternalOrganismId = (paternalOrganismId == null) ? null : String.of(paternalOrganismId);
                    return this;
                }

                /**
                 * The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled 
                 * vocabulary.
                 * 
                 * @param paternalOrganismId
                 *     The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled 
                 *     vocabulary
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder paternalOrganismId(String paternalOrganismId) {
                    this.paternalOrganismId = paternalOrganismId;
                    return this;
                }

                /**
                 * Convenience method for setting {@code paternalOrganismName}.
                 * 
                 * @param paternalOrganismName
                 *     The name of the paternal species constituting the hybrid organism shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #paternalOrganismName(com.ibm.fhir.model.type.String)
                 */
                public Builder paternalOrganismName(java.lang.String paternalOrganismName) {
                    this.paternalOrganismName = (paternalOrganismName == null) ? null : String.of(paternalOrganismName);
                    return this;
                }

                /**
                 * The name of the paternal species constituting the hybrid organism shall be specified.
                 * 
                 * @param paternalOrganismName
                 *     The name of the paternal species constituting the hybrid organism shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder paternalOrganismName(String paternalOrganismName) {
                    this.paternalOrganismName = paternalOrganismName;
                    return this;
                }

                /**
                 * The hybrid type of an organism shall be specified.
                 * 
                 * @param hybridType
                 *     The hybrid type of an organism shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder hybridType(CodeableConcept hybridType) {
                    this.hybridType = hybridType;
                    return this;
                }

                /**
                 * Build the {@link Hybrid}
                 * 
                 * @return
                 *     An immutable object of type {@link Hybrid}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Hybrid per the base specification
                 */
                @Override
                public Hybrid build() {
                    Hybrid hybrid = new Hybrid(this);
                    if (validating) {
                        validate(hybrid);
                    }
                    return hybrid;
                }

                protected void validate(Hybrid hybrid) {
                    super.validate(hybrid);
                    ValidationSupport.requireValueOrChildren(hybrid);
                }

                protected Builder from(Hybrid hybrid) {
                    super.from(hybrid);
                    maternalOrganismId = hybrid.maternalOrganismId;
                    maternalOrganismName = hybrid.maternalOrganismName;
                    paternalOrganismId = hybrid.paternalOrganismId;
                    paternalOrganismName = hybrid.paternalOrganismName;
                    hybridType = hybrid.hybridType;
                    return this;
                }
            }
        }

        /**
         * 4.9.13.7.1 Kingdom (Conditional).
         */
        public static class OrganismGeneral extends BackboneElement {
            @Summary
            private final CodeableConcept kingdom;
            @Summary
            private final CodeableConcept phylum;
            @Summary
            private final CodeableConcept clazz;
            @Summary
            private final CodeableConcept order;

            private OrganismGeneral(Builder builder) {
                super(builder);
                kingdom = builder.kingdom;
                phylum = builder.phylum;
                clazz = builder.clazz;
                order = builder.order;
            }

            /**
             * The kingdom of an organism shall be specified.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getKingdom() {
                return kingdom;
            }

            /**
             * The phylum of an organism shall be specified.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getPhylum() {
                return phylum;
            }

            /**
             * The class of an organism shall be specified.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getClazz() {
                return clazz;
            }

            /**
             * The order of an organism shall be specified,.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getOrder() {
                return order;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (kingdom != null) || 
                    (phylum != null) || 
                    (clazz != null) || 
                    (order != null);
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
                        accept(kingdom, "kingdom", visitor);
                        accept(phylum, "phylum", visitor);
                        accept(clazz, "class", visitor);
                        accept(order, "order", visitor);
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
                OrganismGeneral other = (OrganismGeneral) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(kingdom, other.kingdom) && 
                    Objects.equals(phylum, other.phylum) && 
                    Objects.equals(clazz, other.clazz) && 
                    Objects.equals(order, other.order);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        kingdom, 
                        phylum, 
                        clazz, 
                        order);
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
                private CodeableConcept kingdom;
                private CodeableConcept phylum;
                private CodeableConcept clazz;
                private CodeableConcept order;

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
                 * The kingdom of an organism shall be specified.
                 * 
                 * @param kingdom
                 *     The kingdom of an organism shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder kingdom(CodeableConcept kingdom) {
                    this.kingdom = kingdom;
                    return this;
                }

                /**
                 * The phylum of an organism shall be specified.
                 * 
                 * @param phylum
                 *     The phylum of an organism shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder phylum(CodeableConcept phylum) {
                    this.phylum = phylum;
                    return this;
                }

                /**
                 * The class of an organism shall be specified.
                 * 
                 * @param clazz
                 *     The class of an organism shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder clazz(CodeableConcept clazz) {
                    this.clazz = clazz;
                    return this;
                }

                /**
                 * The order of an organism shall be specified,.
                 * 
                 * @param order
                 *     The order of an organism shall be specified,
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder order(CodeableConcept order) {
                    this.order = order;
                    return this;
                }

                /**
                 * Build the {@link OrganismGeneral}
                 * 
                 * @return
                 *     An immutable object of type {@link OrganismGeneral}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid OrganismGeneral per the base specification
                 */
                @Override
                public OrganismGeneral build() {
                    OrganismGeneral organismGeneral = new OrganismGeneral(this);
                    if (validating) {
                        validate(organismGeneral);
                    }
                    return organismGeneral;
                }

                protected void validate(OrganismGeneral organismGeneral) {
                    super.validate(organismGeneral);
                    ValidationSupport.requireValueOrChildren(organismGeneral);
                }

                protected Builder from(OrganismGeneral organismGeneral) {
                    super.from(organismGeneral);
                    kingdom = organismGeneral.kingdom;
                    phylum = organismGeneral.phylum;
                    clazz = organismGeneral.clazz;
                    order = organismGeneral.order;
                    return this;
                }
            }
        }
    }

    /**
     * To do.
     */
    public static class PartDescription extends BackboneElement {
        @Summary
        private final CodeableConcept part;
        @Summary
        private final CodeableConcept partLocation;

        private PartDescription(Builder builder) {
            super(builder);
            part = builder.part;
            partLocation = builder.partLocation;
        }

        /**
         * Entity of anatomical origin of source material within an organism.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getPart() {
            return part;
        }

        /**
         * The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. 
         * Multiple alternative locations may apply.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getPartLocation() {
            return partLocation;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (part != null) || 
                (partLocation != null);
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
                    accept(part, "part", visitor);
                    accept(partLocation, "partLocation", visitor);
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
            PartDescription other = (PartDescription) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(part, other.part) && 
                Objects.equals(partLocation, other.partLocation);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    part, 
                    partLocation);
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
            private CodeableConcept part;
            private CodeableConcept partLocation;

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
             * Entity of anatomical origin of source material within an organism.
             * 
             * @param part
             *     Entity of anatomical origin of source material within an organism
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder part(CodeableConcept part) {
                this.part = part;
                return this;
            }

            /**
             * The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. 
             * Multiple alternative locations may apply.
             * 
             * @param partLocation
             *     The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. 
             *     Multiple alternative locations may apply
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder partLocation(CodeableConcept partLocation) {
                this.partLocation = partLocation;
                return this;
            }

            /**
             * Build the {@link PartDescription}
             * 
             * @return
             *     An immutable object of type {@link PartDescription}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid PartDescription per the base specification
             */
            @Override
            public PartDescription build() {
                PartDescription partDescription = new PartDescription(this);
                if (validating) {
                    validate(partDescription);
                }
                return partDescription;
            }

            protected void validate(PartDescription partDescription) {
                super.validate(partDescription);
                ValidationSupport.requireValueOrChildren(partDescription);
            }

            protected Builder from(PartDescription partDescription) {
                super.from(partDescription);
                part = partDescription.part;
                partLocation = partDescription.partLocation;
                return this;
            }
        }
    }
}
