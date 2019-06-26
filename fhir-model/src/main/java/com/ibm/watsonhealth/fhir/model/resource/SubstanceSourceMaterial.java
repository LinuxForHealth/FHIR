/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Source material shall capture information on the taxonomic and anatomical origins as well as the fraction of a 
 * material that can result in or can be modified to form a substance. This set of data elements shall be used to define 
 * polymer substances isolated from biological matrices. Taxonomic and anatomical origins shall be described using a 
 * controlled vocabulary as required. This information is captured for naturally derived polymers ( . starch) and 
 * structurally diverse substances. For Organisms belonging to the Kingdom Plantae the Substance level defines the fresh 
 * material of a single species or infraspecies, the Herbal Drug and the Herbal preparation. For Herbal preparations, the 
 * fraction information will be captured at the Substance information level and additional information for herbal 
 * extracts will be captured at the Specified Substance Group 1 information level. See for further explanation the 
 * Substance Class: Structurally Diverse and the herbal annex.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class SubstanceSourceMaterial extends DomainResource {
    private final CodeableConcept sourceMaterialClass;
    private final CodeableConcept sourceMaterialType;
    private final CodeableConcept sourceMaterialState;
    private final Identifier organismId;
    private final String organismName;
    private final List<Identifier> parentSubstanceId;
    private final List<String> parentSubstanceName;
    private final List<CodeableConcept> countryOfOrigin;
    private final List<String> geographicalLocation;
    private final CodeableConcept developmentStage;
    private final List<FractionDescription> fractionDescription;
    private final Organism organism;
    private final List<PartDescription> partDescription;

    private SubstanceSourceMaterial(Builder builder) {
        super(builder);
        this.sourceMaterialClass = builder.sourceMaterialClass;
        this.sourceMaterialType = builder.sourceMaterialType;
        this.sourceMaterialState = builder.sourceMaterialState;
        this.organismId = builder.organismId;
        this.organismName = builder.organismName;
        this.parentSubstanceId = builder.parentSubstanceId;
        this.parentSubstanceName = builder.parentSubstanceName;
        this.countryOfOrigin = builder.countryOfOrigin;
        this.geographicalLocation = builder.geographicalLocation;
        this.developmentStage = builder.developmentStage;
        this.fractionDescription = builder.fractionDescription;
        this.organism = builder.organism;
        this.partDescription = builder.partDescription;
    }

    /**
     * <p>
     * General high level classification of the source material specific to the origin of the material.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getSourceMaterialClass() {
        return sourceMaterialClass;
    }

    /**
     * <p>
     * The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause 
     * refers to the class of infectious agent.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getSourceMaterialType() {
        return sourceMaterialType;
    }

    /**
     * <p>
     * The state of the source material when extracted.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getSourceMaterialState() {
        return sourceMaterialState;
    }

    /**
     * <p>
     * The unique identifier associated with the source material parent organism shall be specified.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getOrganismId() {
        return organismId;
    }

    /**
     * <p>
     * The organism accepted Scientific name shall be provided based on the organism taxonomy.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getOrganismName() {
        return organismName;
    }

    /**
     * <p>
     * The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or 
     * Ginkgo biloba L. (Whole plant).
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getParentSubstanceId() {
        return parentSubstanceId;
    }

    /**
     * <p>
     * The parent substance of the Herbal Drug, or Herbal preparation.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link String}.
     */
    public List<String> getParentSubstanceName() {
        return parentSubstanceName;
    }

    /**
     * <p>
     * The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in 
     * accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides 
     * information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCountryOfOrigin() {
        return countryOfOrigin;
    }

    /**
     * <p>
     * The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link String}.
     */
    public List<String> getGeographicalLocation() {
        return geographicalLocation;
    }

    /**
     * <p>
     * Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the 
     * substance is significantly different in these stages (e.g. foetal bovine serum).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getDevelopmentStage() {
        return developmentStage;
    }

    /**
     * <p>
     * Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary 
     * to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information 
     * will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for 
     * Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information 
     * level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance 
     * Group 1 levels.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link FractionDescription}.
     */
    public List<FractionDescription> getFractionDescription() {
        return fractionDescription;
    }

    /**
     * <p>
     * This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be 
     * specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., 
     * Leaf.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Organism}.
     */
    public Organism getOrganism() {
        return organism;
    }

    /**
     * <p>
     * To do.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link PartDescription}.
     */
    public List<PartDescription> getPartDescription() {
        return partDescription;
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
        builder.sourceMaterialClass = sourceMaterialClass;
        builder.sourceMaterialType = sourceMaterialType;
        builder.sourceMaterialState = sourceMaterialState;
        builder.organismId = organismId;
        builder.organismName = organismName;
        builder.parentSubstanceId.addAll(parentSubstanceId);
        builder.parentSubstanceName.addAll(parentSubstanceName);
        builder.countryOfOrigin.addAll(countryOfOrigin);
        builder.geographicalLocation.addAll(geographicalLocation);
        builder.developmentStage = developmentStage;
        builder.fractionDescription.addAll(fractionDescription);
        builder.organism = organism;
        builder.partDescription.addAll(partDescription);
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        // optional
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
         * General high level classification of the source material specific to the origin of the material.
         * </p>
         * 
         * @param sourceMaterialClass
         *     General high level classification of the source material specific to the origin of the material
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder sourceMaterialClass(CodeableConcept sourceMaterialClass) {
            this.sourceMaterialClass = sourceMaterialClass;
            return this;
        }

        /**
         * <p>
         * The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause 
         * refers to the class of infectious agent.
         * </p>
         * 
         * @param sourceMaterialType
         *     The type of the source material shall be specified based on a controlled vocabulary. For vaccines, this subclause 
         *     refers to the class of infectious agent
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder sourceMaterialType(CodeableConcept sourceMaterialType) {
            this.sourceMaterialType = sourceMaterialType;
            return this;
        }

        /**
         * <p>
         * The state of the source material when extracted.
         * </p>
         * 
         * @param sourceMaterialState
         *     The state of the source material when extracted
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder sourceMaterialState(CodeableConcept sourceMaterialState) {
            this.sourceMaterialState = sourceMaterialState;
            return this;
        }

        /**
         * <p>
         * The unique identifier associated with the source material parent organism shall be specified.
         * </p>
         * 
         * @param organismId
         *     The unique identifier associated with the source material parent organism shall be specified
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder organismId(Identifier organismId) {
            this.organismId = organismId;
            return this;
        }

        /**
         * <p>
         * The organism accepted Scientific name shall be provided based on the organism taxonomy.
         * </p>
         * 
         * @param organismName
         *     The organism accepted Scientific name shall be provided based on the organism taxonomy
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder organismName(String organismName) {
            this.organismName = organismName;
            return this;
        }

        /**
         * <p>
         * The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or 
         * Ginkgo biloba L. (Whole plant).
         * </p>
         * 
         * @param parentSubstanceId
         *     The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or 
         *     Ginkgo biloba L. (Whole plant)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parentSubstanceId(Identifier... parentSubstanceId) {
            for (Identifier value : parentSubstanceId) {
                this.parentSubstanceId.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or 
         * Ginkgo biloba L. (Whole plant).
         * </p>
         * 
         * @param parentSubstanceId
         *     The parent of the herbal drug Ginkgo biloba, Leaf is the substance ID of the substance (fresh) of Ginkgo biloba L. or 
         *     Ginkgo biloba L. (Whole plant)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parentSubstanceId(Collection<Identifier> parentSubstanceId) {
            this.parentSubstanceId.addAll(parentSubstanceId);
            return this;
        }

        /**
         * <p>
         * The parent substance of the Herbal Drug, or Herbal preparation.
         * </p>
         * 
         * @param parentSubstanceName
         *     The parent substance of the Herbal Drug, or Herbal preparation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parentSubstanceName(String... parentSubstanceName) {
            for (String value : parentSubstanceName) {
                this.parentSubstanceName.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The parent substance of the Herbal Drug, or Herbal preparation.
         * </p>
         * 
         * @param parentSubstanceName
         *     The parent substance of the Herbal Drug, or Herbal preparation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parentSubstanceName(Collection<String> parentSubstanceName) {
            this.parentSubstanceName.addAll(parentSubstanceName);
            return this;
        }

        /**
         * <p>
         * The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in 
         * accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides 
         * information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate.
         * </p>
         * 
         * @param countryOfOrigin
         *     The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in 
         *     accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides 
         *     information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder countryOfOrigin(CodeableConcept... countryOfOrigin) {
            for (CodeableConcept value : countryOfOrigin) {
                this.countryOfOrigin.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in 
         * accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides 
         * information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate.
         * </p>
         * 
         * @param countryOfOrigin
         *     The country where the plant material is harvested or the countries where the plasma is sourced from as laid down in 
         *     accordance with the Plasma Master File. For “Plasma-derived substances” the attribute country of origin provides 
         *     information about the countries used for the manufacturing of the Cryopoor plama or Crioprecipitate
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder countryOfOrigin(Collection<CodeableConcept> countryOfOrigin) {
            this.countryOfOrigin.addAll(countryOfOrigin);
            return this;
        }

        /**
         * <p>
         * The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.
         * </p>
         * 
         * @param geographicalLocation
         *     The place/region where the plant is harvested or the places/regions where the animal source material has its habitat
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder geographicalLocation(String... geographicalLocation) {
            for (String value : geographicalLocation) {
                this.geographicalLocation.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The place/region where the plant is harvested or the places/regions where the animal source material has its habitat.
         * </p>
         * 
         * @param geographicalLocation
         *     The place/region where the plant is harvested or the places/regions where the animal source material has its habitat
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder geographicalLocation(Collection<String> geographicalLocation) {
            this.geographicalLocation.addAll(geographicalLocation);
            return this;
        }

        /**
         * <p>
         * Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the 
         * substance is significantly different in these stages (e.g. foetal bovine serum).
         * </p>
         * 
         * @param developmentStage
         *     Stage of life for animals, plants, insects and microorganisms. This information shall be provided only when the 
         *     substance is significantly different in these stages (e.g. foetal bovine serum)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder developmentStage(CodeableConcept developmentStage) {
            this.developmentStage = developmentStage;
            return this;
        }

        /**
         * <p>
         * Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary 
         * to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information 
         * will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for 
         * Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information 
         * level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance 
         * Group 1 levels.
         * </p>
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
         *     A reference to this Builder instance.
         */
        public Builder fractionDescription(FractionDescription... fractionDescription) {
            for (FractionDescription value : fractionDescription) {
                this.fractionDescription.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary 
         * to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information 
         * will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for 
         * Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information 
         * level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance 
         * Group 1 levels.
         * </p>
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
         *     A reference to this Builder instance.
         */
        public Builder fractionDescription(Collection<FractionDescription> fractionDescription) {
            this.fractionDescription.addAll(fractionDescription);
            return this;
        }

        /**
         * <p>
         * This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be 
         * specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., 
         * Leaf.
         * </p>
         * 
         * @param organism
         *     This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be 
         *     specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., 
         *     Leaf
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder organism(Organism organism) {
            this.organism = organism;
            return this;
        }

        /**
         * <p>
         * To do.
         * </p>
         * 
         * @param partDescription
         *     To do
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partDescription(PartDescription... partDescription) {
            for (PartDescription value : partDescription) {
                this.partDescription.add(value);
            }
            return this;
        }

        /**
         * <p>
         * To do.
         * </p>
         * 
         * @param partDescription
         *     To do
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder partDescription(Collection<PartDescription> partDescription) {
            this.partDescription.addAll(partDescription);
            return this;
        }

        @Override
        public SubstanceSourceMaterial build() {
            return new SubstanceSourceMaterial(this);
        }
    }

    /**
     * <p>
     * Many complex materials are fractions of parts of plants, animals, or minerals. Fraction elements are often necessary 
     * to define both Substances and Specified Group 1 Substances. For substances derived from Plants, fraction information 
     * will be captured at the Substance information level ( . Oils, Juices and Exudates). Additional information for 
     * Extracts, such as extraction solvent composition, will be captured at the Specified Substance Group 1 information 
     * level. For plasma-derived products fraction information will be captured at the Substance and the Specified Substance 
     * Group 1 levels.
     * </p>
     */
    public static class FractionDescription extends BackboneElement {
        private final String fraction;
        private final CodeableConcept materialType;

        private FractionDescription(Builder builder) {
            super(builder);
            this.fraction = builder.fraction;
            this.materialType = builder.materialType;
        }

        /**
         * <p>
         * This element is capturing information about the fraction of a plant part, or human plasma for fractionation.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getFraction() {
            return fraction;
        }

        /**
         * <p>
         * The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts 
         * (liquid/dry) is described in Specified Substance Group 1.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getMaterialType() {
            return materialType;
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
                    accept(fraction, "fraction", visitor);
                    accept(materialType, "materialType", visitor);
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
            private String fraction;
            private CodeableConcept materialType;

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
             * This element is capturing information about the fraction of a plant part, or human plasma for fractionation.
             * </p>
             * 
             * @param fraction
             *     This element is capturing information about the fraction of a plant part, or human plasma for fractionation
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder fraction(String fraction) {
                this.fraction = fraction;
                return this;
            }

            /**
             * <p>
             * The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts 
             * (liquid/dry) is described in Specified Substance Group 1.
             * </p>
             * 
             * @param materialType
             *     The specific type of the material constituting the component. For Herbal preparations the particulars of the extracts 
             *     (liquid/dry) is described in Specified Substance Group 1
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder materialType(CodeableConcept materialType) {
                this.materialType = materialType;
                return this;
            }

            @Override
            public FractionDescription build() {
                return new FractionDescription(this);
            }

            private static Builder from(FractionDescription fractionDescription) {
                Builder builder = new Builder();
                builder.id = fractionDescription.id;
                builder.extension.addAll(fractionDescription.extension);
                builder.modifierExtension.addAll(fractionDescription.modifierExtension);
                builder.fraction = fractionDescription.fraction;
                builder.materialType = fractionDescription.materialType;
                return builder;
            }
        }
    }

    /**
     * <p>
     * This subclause describes the organism which the substance is derived from. For vaccines, the parent organism shall be 
     * specified based on these subclause elements. As an example, full taxonomy will be described for the Substance Name: ., 
     * Leaf.
     * </p>
     */
    public static class Organism extends BackboneElement {
        private final CodeableConcept family;
        private final CodeableConcept genus;
        private final CodeableConcept species;
        private final CodeableConcept intraspecificType;
        private final String intraspecificDescription;
        private final List<Author> author;
        private final Hybrid hybrid;
        private final OrganismGeneral organismGeneral;

        private Organism(Builder builder) {
            super(builder);
            this.family = builder.family;
            this.genus = builder.genus;
            this.species = builder.species;
            this.intraspecificType = builder.intraspecificType;
            this.intraspecificDescription = builder.intraspecificDescription;
            this.author = builder.author;
            this.hybrid = builder.hybrid;
            this.organismGeneral = builder.organismGeneral;
        }

        /**
         * <p>
         * The family of an organism shall be specified.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getFamily() {
            return family;
        }

        /**
         * <p>
         * The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal 
         * scientific name; it is present in names for genera, species and infraspecies.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getGenus() {
            return genus;
        }

        /**
         * <p>
         * The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is 
         * present in names for species and infraspecies.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getSpecies() {
            return species;
        }

        /**
         * <p>
         * The Intraspecific type of an organism shall be specified.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getIntraspecificType() {
            return intraspecificType;
        }

        /**
         * <p>
         * The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza 
         * Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getIntraspecificDescription() {
            return intraspecificDescription;
        }

        /**
         * <p>
         * 4.9.13.6.1 Author type (Conditional).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Author}.
         */
        public List<Author> getAuthor() {
            return author;
        }

        /**
         * <p>
         * 4.9.13.8.1 Hybrid species maternal organism ID (Optional).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Hybrid}.
         */
        public Hybrid getHybrid() {
            return hybrid;
        }

        /**
         * <p>
         * 4.9.13.7.1 Kingdom (Conditional).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link OrganismGeneral}.
         */
        public OrganismGeneral getOrganismGeneral() {
            return organismGeneral;
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
                    accept(family, "family", visitor);
                    accept(genus, "genus", visitor);
                    accept(species, "species", visitor);
                    accept(intraspecificType, "intraspecificType", visitor);
                    accept(intraspecificDescription, "intraspecificDescription", visitor);
                    accept(author, "author", visitor, Author.class);
                    accept(hybrid, "hybrid", visitor);
                    accept(organismGeneral, "organismGeneral", visitor);
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
             * The family of an organism shall be specified.
             * </p>
             * 
             * @param family
             *     The family of an organism shall be specified
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder family(CodeableConcept family) {
                this.family = family;
                return this;
            }

            /**
             * <p>
             * The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal 
             * scientific name; it is present in names for genera, species and infraspecies.
             * </p>
             * 
             * @param genus
             *     The genus of an organism shall be specified; refers to the Latin epithet of the genus element of the plant/animal 
             *     scientific name; it is present in names for genera, species and infraspecies
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder genus(CodeableConcept genus) {
                this.genus = genus;
                return this;
            }

            /**
             * <p>
             * The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is 
             * present in names for species and infraspecies.
             * </p>
             * 
             * @param species
             *     The species of an organism shall be specified; refers to the Latin epithet of the species of the plant/animal; it is 
             *     present in names for species and infraspecies
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder species(CodeableConcept species) {
                this.species = species;
                return this;
            }

            /**
             * <p>
             * The Intraspecific type of an organism shall be specified.
             * </p>
             * 
             * @param intraspecificType
             *     The Intraspecific type of an organism shall be specified
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder intraspecificType(CodeableConcept intraspecificType) {
                this.intraspecificType = intraspecificType;
                return this;
            }

            /**
             * <p>
             * The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza 
             * Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention.
             * </p>
             * 
             * @param intraspecificDescription
             *     The intraspecific description of an organism shall be specified based on a controlled vocabulary. For Influenza 
             *     Vaccine, the intraspecific description shall contain the syntax of the antigen in line with the WHO convention
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder intraspecificDescription(String intraspecificDescription) {
                this.intraspecificDescription = intraspecificDescription;
                return this;
            }

            /**
             * <p>
             * 4.9.13.6.1 Author type (Conditional).
             * </p>
             * 
             * @param author
             *     4.9.13.6.1 Author type (Conditional)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder author(Author... author) {
                for (Author value : author) {
                    this.author.add(value);
                }
                return this;
            }

            /**
             * <p>
             * 4.9.13.6.1 Author type (Conditional).
             * </p>
             * 
             * @param author
             *     4.9.13.6.1 Author type (Conditional)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder author(Collection<Author> author) {
                this.author.addAll(author);
                return this;
            }

            /**
             * <p>
             * 4.9.13.8.1 Hybrid species maternal organism ID (Optional).
             * </p>
             * 
             * @param hybrid
             *     4.9.13.8.1 Hybrid species maternal organism ID (Optional)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder hybrid(Hybrid hybrid) {
                this.hybrid = hybrid;
                return this;
            }

            /**
             * <p>
             * 4.9.13.7.1 Kingdom (Conditional).
             * </p>
             * 
             * @param organismGeneral
             *     4.9.13.7.1 Kingdom (Conditional)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder organismGeneral(OrganismGeneral organismGeneral) {
                this.organismGeneral = organismGeneral;
                return this;
            }

            @Override
            public Organism build() {
                return new Organism(this);
            }

            private static Builder from(Organism organism) {
                Builder builder = new Builder();
                builder.id = organism.id;
                builder.extension.addAll(organism.extension);
                builder.modifierExtension.addAll(organism.modifierExtension);
                builder.family = organism.family;
                builder.genus = organism.genus;
                builder.species = organism.species;
                builder.intraspecificType = organism.intraspecificType;
                builder.intraspecificDescription = organism.intraspecificDescription;
                builder.author.addAll(organism.author);
                builder.hybrid = organism.hybrid;
                builder.organismGeneral = organism.organismGeneral;
                return builder;
            }
        }

        /**
         * <p>
         * 4.9.13.6.1 Author type (Conditional).
         * </p>
         */
        public static class Author extends BackboneElement {
            private final CodeableConcept authorType;
            private final String authorDescription;

            private Author(Builder builder) {
                super(builder);
                this.authorType = builder.authorType;
                this.authorDescription = builder.authorDescription;
            }

            /**
             * <p>
             * The type of author of an organism species shall be specified. The parenthetical author of an organism species refers 
             * to the first author who published the plant/animal name (of any rank). The primary author of an organism species 
             * refers to the first author(s), who validly published the plant/animal name.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getAuthorType() {
                return authorType;
            }

            /**
             * <p>
             * The author of an organism species shall be specified. The author year of an organism shall also be specified when 
             * applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any 
             * rank).
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getAuthorDescription() {
                return authorDescription;
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
                        accept(authorType, "authorType", visitor);
                        accept(authorDescription, "authorDescription", visitor);
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
                private CodeableConcept authorType;
                private String authorDescription;

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
                 * The type of author of an organism species shall be specified. The parenthetical author of an organism species refers 
                 * to the first author who published the plant/animal name (of any rank). The primary author of an organism species 
                 * refers to the first author(s), who validly published the plant/animal name.
                 * </p>
                 * 
                 * @param authorType
                 *     The type of author of an organism species shall be specified. The parenthetical author of an organism species refers 
                 *     to the first author who published the plant/animal name (of any rank). The primary author of an organism species 
                 *     refers to the first author(s), who validly published the plant/animal name
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder authorType(CodeableConcept authorType) {
                    this.authorType = authorType;
                    return this;
                }

                /**
                 * <p>
                 * The author of an organism species shall be specified. The author year of an organism shall also be specified when 
                 * applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any 
                 * rank).
                 * </p>
                 * 
                 * @param authorDescription
                 *     The author of an organism species shall be specified. The author year of an organism shall also be specified when 
                 *     applicable; refers to the year in which the first author(s) published the infraspecific plant/animal name (of any rank)
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder authorDescription(String authorDescription) {
                    this.authorDescription = authorDescription;
                    return this;
                }

                @Override
                public Author build() {
                    return new Author(this);
                }

                private static Builder from(Author author) {
                    Builder builder = new Builder();
                    builder.id = author.id;
                    builder.extension.addAll(author.extension);
                    builder.modifierExtension.addAll(author.modifierExtension);
                    builder.authorType = author.authorType;
                    builder.authorDescription = author.authorDescription;
                    return builder;
                }
            }
        }

        /**
         * <p>
         * 4.9.13.8.1 Hybrid species maternal organism ID (Optional).
         * </p>
         */
        public static class Hybrid extends BackboneElement {
            private final String maternalOrganismId;
            private final String maternalOrganismName;
            private final String paternalOrganismId;
            private final String paternalOrganismName;
            private final CodeableConcept hybridType;

            private Hybrid(Builder builder) {
                super(builder);
                this.maternalOrganismId = builder.maternalOrganismId;
                this.maternalOrganismName = builder.maternalOrganismName;
                this.paternalOrganismId = builder.paternalOrganismId;
                this.paternalOrganismName = builder.paternalOrganismName;
                this.hybridType = builder.hybridType;
            }

            /**
             * <p>
             * The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled 
             * vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal 
             * and which is paternal.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getMaternalOrganismId() {
                return maternalOrganismId;
            }

            /**
             * <p>
             * The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t 
             * always known, and it is unlikely that it will be known which is maternal and which is paternal.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getMaternalOrganismName() {
                return maternalOrganismName;
            }

            /**
             * <p>
             * The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled 
             * vocabulary.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getPaternalOrganismId() {
                return paternalOrganismId;
            }

            /**
             * <p>
             * The name of the paternal species constituting the hybrid organism shall be specified.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getPaternalOrganismName() {
                return paternalOrganismName;
            }

            /**
             * <p>
             * The hybrid type of an organism shall be specified.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getHybridType() {
                return hybridType;
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
                        accept(maternalOrganismId, "maternalOrganismId", visitor);
                        accept(maternalOrganismName, "maternalOrganismName", visitor);
                        accept(paternalOrganismId, "paternalOrganismId", visitor);
                        accept(paternalOrganismName, "paternalOrganismName", visitor);
                        accept(hybridType, "hybridType", visitor);
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
                private String maternalOrganismId;
                private String maternalOrganismName;
                private String paternalOrganismId;
                private String paternalOrganismName;
                private CodeableConcept hybridType;

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
                 * The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled 
                 * vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal 
                 * and which is paternal.
                 * </p>
                 * 
                 * @param maternalOrganismId
                 *     The identifier of the maternal species constituting the hybrid organism shall be specified based on a controlled 
                 *     vocabulary. For plants, the parents aren’t always known, and it is unlikely that it will be known which is maternal 
                 *     and which is paternal
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder maternalOrganismId(String maternalOrganismId) {
                    this.maternalOrganismId = maternalOrganismId;
                    return this;
                }

                /**
                 * <p>
                 * The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t 
                 * always known, and it is unlikely that it will be known which is maternal and which is paternal.
                 * </p>
                 * 
                 * @param maternalOrganismName
                 *     The name of the maternal species constituting the hybrid organism shall be specified. For plants, the parents aren’t 
                 *     always known, and it is unlikely that it will be known which is maternal and which is paternal
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder maternalOrganismName(String maternalOrganismName) {
                    this.maternalOrganismName = maternalOrganismName;
                    return this;
                }

                /**
                 * <p>
                 * The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled 
                 * vocabulary.
                 * </p>
                 * 
                 * @param paternalOrganismId
                 *     The identifier of the paternal species constituting the hybrid organism shall be specified based on a controlled 
                 *     vocabulary
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder paternalOrganismId(String paternalOrganismId) {
                    this.paternalOrganismId = paternalOrganismId;
                    return this;
                }

                /**
                 * <p>
                 * The name of the paternal species constituting the hybrid organism shall be specified.
                 * </p>
                 * 
                 * @param paternalOrganismName
                 *     The name of the paternal species constituting the hybrid organism shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder paternalOrganismName(String paternalOrganismName) {
                    this.paternalOrganismName = paternalOrganismName;
                    return this;
                }

                /**
                 * <p>
                 * The hybrid type of an organism shall be specified.
                 * </p>
                 * 
                 * @param hybridType
                 *     The hybrid type of an organism shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder hybridType(CodeableConcept hybridType) {
                    this.hybridType = hybridType;
                    return this;
                }

                @Override
                public Hybrid build() {
                    return new Hybrid(this);
                }

                private static Builder from(Hybrid hybrid) {
                    Builder builder = new Builder();
                    builder.id = hybrid.id;
                    builder.extension.addAll(hybrid.extension);
                    builder.modifierExtension.addAll(hybrid.modifierExtension);
                    builder.maternalOrganismId = hybrid.maternalOrganismId;
                    builder.maternalOrganismName = hybrid.maternalOrganismName;
                    builder.paternalOrganismId = hybrid.paternalOrganismId;
                    builder.paternalOrganismName = hybrid.paternalOrganismName;
                    builder.hybridType = hybrid.hybridType;
                    return builder;
                }
            }
        }

        /**
         * <p>
         * 4.9.13.7.1 Kingdom (Conditional).
         * </p>
         */
        public static class OrganismGeneral extends BackboneElement {
            private final CodeableConcept kingdom;
            private final CodeableConcept phylum;
            private final CodeableConcept clazz;
            private final CodeableConcept order;

            private OrganismGeneral(Builder builder) {
                super(builder);
                this.kingdom = builder.kingdom;
                this.phylum = builder.phylum;
                this.clazz = builder.clazz;
                this.order = builder.order;
            }

            /**
             * <p>
             * The kingdom of an organism shall be specified.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getKingdom() {
                return kingdom;
            }

            /**
             * <p>
             * The phylum of an organism shall be specified.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getPhylum() {
                return phylum;
            }

            /**
             * <p>
             * The class of an organism shall be specified.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getClazz() {
                return clazz;
            }

            /**
             * <p>
             * The order of an organism shall be specified,.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept}.
             */
            public CodeableConcept getOrder() {
                return order;
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
                        accept(kingdom, "kingdom", visitor);
                        accept(phylum, "phylum", visitor);
                        accept(clazz, "class", visitor);
                        accept(order, "order", visitor);
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
                private CodeableConcept kingdom;
                private CodeableConcept phylum;
                private CodeableConcept clazz;
                private CodeableConcept order;

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
                 * The kingdom of an organism shall be specified.
                 * </p>
                 * 
                 * @param kingdom
                 *     The kingdom of an organism shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder kingdom(CodeableConcept kingdom) {
                    this.kingdom = kingdom;
                    return this;
                }

                /**
                 * <p>
                 * The phylum of an organism shall be specified.
                 * </p>
                 * 
                 * @param phylum
                 *     The phylum of an organism shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder phylum(CodeableConcept phylum) {
                    this.phylum = phylum;
                    return this;
                }

                /**
                 * <p>
                 * The class of an organism shall be specified.
                 * </p>
                 * 
                 * @param clazz
                 *     The class of an organism shall be specified
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder clazz(CodeableConcept clazz) {
                    this.clazz = clazz;
                    return this;
                }

                /**
                 * <p>
                 * The order of an organism shall be specified,.
                 * </p>
                 * 
                 * @param order
                 *     The order of an organism shall be specified,
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder order(CodeableConcept order) {
                    this.order = order;
                    return this;
                }

                @Override
                public OrganismGeneral build() {
                    return new OrganismGeneral(this);
                }

                private static Builder from(OrganismGeneral organismGeneral) {
                    Builder builder = new Builder();
                    builder.id = organismGeneral.id;
                    builder.extension.addAll(organismGeneral.extension);
                    builder.modifierExtension.addAll(organismGeneral.modifierExtension);
                    builder.kingdom = organismGeneral.kingdom;
                    builder.phylum = organismGeneral.phylum;
                    builder.clazz = organismGeneral.clazz;
                    builder.order = organismGeneral.order;
                    return builder;
                }
            }
        }
    }

    /**
     * <p>
     * To do.
     * </p>
     */
    public static class PartDescription extends BackboneElement {
        private final CodeableConcept part;
        private final CodeableConcept partLocation;

        private PartDescription(Builder builder) {
            super(builder);
            this.part = builder.part;
            this.partLocation = builder.partLocation;
        }

        /**
         * <p>
         * Entity of anatomical origin of source material within an organism.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getPart() {
            return part;
        }

        /**
         * <p>
         * The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. 
         * Multiple alternative locations may apply.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getPartLocation() {
            return partLocation;
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
                    accept(part, "part", visitor);
                    accept(partLocation, "partLocation", visitor);
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
            private CodeableConcept part;
            private CodeableConcept partLocation;

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
             * Entity of anatomical origin of source material within an organism.
             * </p>
             * 
             * @param part
             *     Entity of anatomical origin of source material within an organism
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder part(CodeableConcept part) {
                this.part = part;
                return this;
            }

            /**
             * <p>
             * The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. 
             * Multiple alternative locations may apply.
             * </p>
             * 
             * @param partLocation
             *     The detailed anatomic location when the part can be extracted from different anatomical locations of the organism. 
             *     Multiple alternative locations may apply
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder partLocation(CodeableConcept partLocation) {
                this.partLocation = partLocation;
                return this;
            }

            @Override
            public PartDescription build() {
                return new PartDescription(this);
            }

            private static Builder from(PartDescription partDescription) {
                Builder builder = new Builder();
                builder.id = partDescription.id;
                builder.extension.addAll(partDescription.extension);
                builder.modifierExtension.addAll(partDescription.modifierExtension);
                builder.part = partDescription.part;
                builder.partLocation = partDescription.partLocation;
                return builder;
            }
        }
    }
}
