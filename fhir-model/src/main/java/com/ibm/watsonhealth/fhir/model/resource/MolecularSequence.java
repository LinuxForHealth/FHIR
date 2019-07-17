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

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.OrientationType;
import com.ibm.watsonhealth.fhir.model.type.QualityType;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RepositoryType;
import com.ibm.watsonhealth.fhir.model.type.SequenceType;
import com.ibm.watsonhealth.fhir.model.type.StrandType;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Raw data describing a biological sequence.
 * </p>
 */
@Constraint(
    id = "msq-3",
    level = "Rule",
    location = "(base)",
    description = "Only 0 and 1 are valid for coordinateSystem",
    expression = "coordinateSystem = 1 or coordinateSystem = 0"
)
@Constraint(
    id = "msq-5",
    level = "Rule",
    location = "MolecularSequence.referenceSeq",
    description = "GenomeBuild and chromosome must be both contained if either one of them is contained",
    expression = "(chromosome.empty() and genomeBuild.empty()) or (chromosome.exists() and genomeBuild.exists())"
)
@Constraint(
    id = "msq-6",
    level = "Rule",
    location = "MolecularSequence.referenceSeq",
    description = "Have and only have one of the following elements in referenceSeq : 1. genomeBuild ; 2 referenceSeqId; 3. referenceSeqPointer;  4. referenceSeqString;",
    expression = "(genomeBuild.count()+referenceSeqId.count()+ referenceSeqPointer.count()+ referenceSeqString.count()) = 1"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MolecularSequence extends DomainResource {
    private final List<Identifier> identifier;
    private final SequenceType type;
    private final Integer coordinateSystem;
    private final Reference patient;
    private final Reference specimen;
    private final Reference device;
    private final Reference performer;
    private final Quantity quantity;
    private final ReferenceSeq referenceSeq;
    private final List<Variant> variant;
    private final String observedSeq;
    private final List<Quality> quality;
    private final Integer readCoverage;
    private final List<Repository> repository;
    private final List<Reference> pointer;
    private final List<StructureVariant> structureVariant;

    private volatile int hashCode;

    private MolecularSequence(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        type = builder.type;
        coordinateSystem = ValidationSupport.requireNonNull(builder.coordinateSystem, "coordinateSystem");
        patient = builder.patient;
        specimen = builder.specimen;
        device = builder.device;
        performer = builder.performer;
        quantity = builder.quantity;
        referenceSeq = builder.referenceSeq;
        variant = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.variant, "variant"));
        observedSeq = builder.observedSeq;
        quality = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.quality, "quality"));
        readCoverage = builder.readCoverage;
        repository = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.repository, "repository"));
        pointer = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.pointer, "pointer"));
        structureVariant = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.structureVariant, "structureVariant"));
    }

    /**
     * <p>
     * A unique identifier for this particular sequence instance. This is a FHIR-defined id.
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
     * Amino Acid Sequence/ DNA Sequence / RNA Sequence.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link SequenceType}.
     */
    public SequenceType getType() {
        return type;
    }

    /**
     * <p>
     * Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or 
     * starting at 1 (1-based numbering, inclusive start and inclusive end).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Integer}.
     */
    public Integer getCoordinateSystem() {
        return coordinateSystem;
    }

    /**
     * <p>
     * The patient whose sequencing results are described by this resource.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * <p>
     * Specimen used for sequencing.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSpecimen() {
        return specimen;
    }

    /**
     * <p>
     * The method for sequencing, for example, chip information.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getDevice() {
        return device;
    }

    /**
     * <p>
     * The organization or lab that should be responsible for this result.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPerformer() {
        return performer;
    }

    /**
     * <p>
     * The number of copies of the sequence of interest. (RNASeq).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * <p>
     * A sequence that is used as a reference to describe variants that are present in a sequence analyzed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ReferenceSeq}.
     */
    public ReferenceSeq getReferenceSeq() {
        return referenceSeq;
    }

    /**
     * <p>
     * The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.
     * org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including 
     * insertion,deletion,SNP,etc.) It can represent some complex mutation or segment variation with the assist of CIGAR 
     * string.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Variant}.
     */
    public List<Variant> getVariant() {
        return variant;
    }

    /**
     * <p>
     * Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This 
     * shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getObservedSeq() {
        return observedSeq;
    }

    /**
     * <p>
     * An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred 
     * quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Quality}.
     */
    public List<Quality> getQuality() {
        return quality;
    }

    /**
     * <p>
     * Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed 
     * sequence.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Integer}.
     */
    public Integer getReadCoverage() {
        return readCoverage;
    }

    /**
     * <p>
     * Configurations of the external repository. The repository shall store target's observedSeq or records related with 
     * target's observedSeq.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Repository}.
     */
    public List<Repository> getRepository() {
        return repository;
    }

    /**
     * <p>
     * Pointer to next atomic sequence which at most contains one variant.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPointer() {
        return pointer;
    }

    /**
     * <p>
     * Information about chromosome structure variation.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link StructureVariant}.
     */
    public List<StructureVariant> getStructureVariant() {
        return structureVariant;
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
                accept(type, "type", visitor);
                accept(coordinateSystem, "coordinateSystem", visitor);
                accept(patient, "patient", visitor);
                accept(specimen, "specimen", visitor);
                accept(device, "device", visitor);
                accept(performer, "performer", visitor);
                accept(quantity, "quantity", visitor);
                accept(referenceSeq, "referenceSeq", visitor);
                accept(variant, "variant", visitor, Variant.class);
                accept(observedSeq, "observedSeq", visitor);
                accept(quality, "quality", visitor, Quality.class);
                accept(readCoverage, "readCoverage", visitor);
                accept(repository, "repository", visitor, Repository.class);
                accept(pointer, "pointer", visitor, Reference.class);
                accept(structureVariant, "structureVariant", visitor, StructureVariant.class);
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
        MolecularSequence other = (MolecularSequence) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(type, other.type) && 
            Objects.equals(coordinateSystem, other.coordinateSystem) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(specimen, other.specimen) && 
            Objects.equals(device, other.device) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(quantity, other.quantity) && 
            Objects.equals(referenceSeq, other.referenceSeq) && 
            Objects.equals(variant, other.variant) && 
            Objects.equals(observedSeq, other.observedSeq) && 
            Objects.equals(quality, other.quality) && 
            Objects.equals(readCoverage, other.readCoverage) && 
            Objects.equals(repository, other.repository) && 
            Objects.equals(pointer, other.pointer) && 
            Objects.equals(structureVariant, other.structureVariant);
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
                type, 
                coordinateSystem, 
                patient, 
                specimen, 
                device, 
                performer, 
                quantity, 
                referenceSeq, 
                variant, 
                observedSeq, 
                quality, 
                readCoverage, 
                repository, 
                pointer, 
                structureVariant);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(coordinateSystem).from(this);
    }

    public Builder toBuilder(Integer coordinateSystem) {
        return new Builder(coordinateSystem).from(this);
    }

    public static Builder builder(Integer coordinateSystem) {
        return new Builder(coordinateSystem);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Integer coordinateSystem;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private SequenceType type;
        private Reference patient;
        private Reference specimen;
        private Reference device;
        private Reference performer;
        private Quantity quantity;
        private ReferenceSeq referenceSeq;
        private List<Variant> variant = new ArrayList<>();
        private String observedSeq;
        private List<Quality> quality = new ArrayList<>();
        private Integer readCoverage;
        private List<Repository> repository = new ArrayList<>();
        private List<Reference> pointer = new ArrayList<>();
        private List<StructureVariant> structureVariant = new ArrayList<>();

        private Builder(Integer coordinateSystem) {
            super();
            this.coordinateSystem = coordinateSystem;
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
         * A unique identifier for this particular sequence instance. This is a FHIR-defined id.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     Unique ID for this particular sequence. This is a FHIR-defined id
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
         * A unique identifier for this particular sequence instance. This is a FHIR-defined id.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Unique ID for this particular sequence. This is a FHIR-defined id
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
         * Amino Acid Sequence/ DNA Sequence / RNA Sequence.
         * </p>
         * 
         * @param type
         *     aa | dna | rna
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(SequenceType type) {
            this.type = type;
            return this;
        }

        /**
         * <p>
         * The patient whose sequencing results are described by this resource.
         * </p>
         * 
         * @param patient
         *     Who and/or what this is about
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * <p>
         * Specimen used for sequencing.
         * </p>
         * 
         * @param specimen
         *     Specimen used for sequencing
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specimen(Reference specimen) {
            this.specimen = specimen;
            return this;
        }

        /**
         * <p>
         * The method for sequencing, for example, chip information.
         * </p>
         * 
         * @param device
         *     The method for sequencing
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder device(Reference device) {
            this.device = device;
            return this;
        }

        /**
         * <p>
         * The organization or lab that should be responsible for this result.
         * </p>
         * 
         * @param performer
         *     Who should be responsible for test result
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder performer(Reference performer) {
            this.performer = performer;
            return this;
        }

        /**
         * <p>
         * The number of copies of the sequence of interest. (RNASeq).
         * </p>
         * 
         * @param quantity
         *     The number of copies of the sequence of interest. (RNASeq)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder quantity(Quantity quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * <p>
         * A sequence that is used as a reference to describe variants that are present in a sequence analyzed.
         * </p>
         * 
         * @param referenceSeq
         *     A sequence used as reference
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder referenceSeq(ReferenceSeq referenceSeq) {
            this.referenceSeq = referenceSeq;
            return this;
        }

        /**
         * <p>
         * The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.
         * org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including 
         * insertion,deletion,SNP,etc.) It can represent some complex mutation or segment variation with the assist of CIGAR 
         * string.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param variant
         *     Variant in sequence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder variant(Variant... variant) {
            for (Variant value : variant) {
                this.variant.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.
         * org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including 
         * insertion,deletion,SNP,etc.) It can represent some complex mutation or segment variation with the assist of CIGAR 
         * string.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param variant
         *     Variant in sequence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder variant(Collection<Variant> variant) {
            this.variant = new ArrayList<>(variant);
            return this;
        }

        /**
         * <p>
         * Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This 
         * shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd.
         * </p>
         * 
         * @param observedSeq
         *     Sequence that was observed
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder observedSeq(String observedSeq) {
            this.observedSeq = observedSeq;
            return this;
        }

        /**
         * <p>
         * An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred 
         * quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param quality
         *     An set of value as quality of sequence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder quality(Quality... quality) {
            for (Quality value : quality) {
                this.quality.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred 
         * quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param quality
         *     An set of value as quality of sequence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder quality(Collection<Quality> quality) {
            this.quality = new ArrayList<>(quality);
            return this;
        }

        /**
         * <p>
         * Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed 
         * sequence.
         * </p>
         * 
         * @param readCoverage
         *     Average number of reads representing a given nucleotide in the reconstructed sequence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder readCoverage(Integer readCoverage) {
            this.readCoverage = readCoverage;
            return this;
        }

        /**
         * <p>
         * Configurations of the external repository. The repository shall store target's observedSeq or records related with 
         * target's observedSeq.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param repository
         *     External repository which contains detailed report related with observedSeq in this resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder repository(Repository... repository) {
            for (Repository value : repository) {
                this.repository.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Configurations of the external repository. The repository shall store target's observedSeq or records related with 
         * target's observedSeq.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param repository
         *     External repository which contains detailed report related with observedSeq in this resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder repository(Collection<Repository> repository) {
            this.repository = new ArrayList<>(repository);
            return this;
        }

        /**
         * <p>
         * Pointer to next atomic sequence which at most contains one variant.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param pointer
         *     Pointer to next atomic sequence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder pointer(Reference... pointer) {
            for (Reference value : pointer) {
                this.pointer.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Pointer to next atomic sequence which at most contains one variant.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param pointer
         *     Pointer to next atomic sequence
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder pointer(Collection<Reference> pointer) {
            this.pointer = new ArrayList<>(pointer);
            return this;
        }

        /**
         * <p>
         * Information about chromosome structure variation.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param structureVariant
         *     Structural variant
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder structureVariant(StructureVariant... structureVariant) {
            for (StructureVariant value : structureVariant) {
                this.structureVariant.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Information about chromosome structure variation.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param structureVariant
         *     Structural variant
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder structureVariant(Collection<StructureVariant> structureVariant) {
            this.structureVariant = new ArrayList<>(structureVariant);
            return this;
        }

        @Override
        public MolecularSequence build() {
            return new MolecularSequence(this);
        }

        private Builder from(MolecularSequence molecularSequence) {
            id = molecularSequence.id;
            meta = molecularSequence.meta;
            implicitRules = molecularSequence.implicitRules;
            language = molecularSequence.language;
            text = molecularSequence.text;
            contained.addAll(molecularSequence.contained);
            extension.addAll(molecularSequence.extension);
            modifierExtension.addAll(molecularSequence.modifierExtension);
            identifier.addAll(molecularSequence.identifier);
            type = molecularSequence.type;
            patient = molecularSequence.patient;
            specimen = molecularSequence.specimen;
            device = molecularSequence.device;
            performer = molecularSequence.performer;
            quantity = molecularSequence.quantity;
            referenceSeq = molecularSequence.referenceSeq;
            variant.addAll(molecularSequence.variant);
            observedSeq = molecularSequence.observedSeq;
            quality.addAll(molecularSequence.quality);
            readCoverage = molecularSequence.readCoverage;
            repository.addAll(molecularSequence.repository);
            pointer.addAll(molecularSequence.pointer);
            structureVariant.addAll(molecularSequence.structureVariant);
            return this;
        }
    }

    /**
     * <p>
     * A sequence that is used as a reference to describe variants that are present in a sequence analyzed.
     * </p>
     */
    public static class ReferenceSeq extends BackboneElement {
        private final CodeableConcept chromosome;
        private final String genomeBuild;
        private final OrientationType orientation;
        private final CodeableConcept referenceSeqId;
        private final Reference referenceSeqPointer;
        private final String referenceSeqString;
        private final StrandType strand;
        private final Integer windowStart;
        private final Integer windowEnd;

        private volatile int hashCode;

        private ReferenceSeq(Builder builder) {
            super(builder);
            chromosome = builder.chromosome;
            genomeBuild = builder.genomeBuild;
            orientation = builder.orientation;
            referenceSeqId = builder.referenceSeqId;
            referenceSeqPointer = builder.referenceSeqPointer;
            referenceSeqString = builder.referenceSeqString;
            strand = builder.strand;
            windowStart = builder.windowStart;
            windowEnd = builder.windowEnd;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of 
         * specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.
         * org/browser/current_svn/term/SO:0000340)).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getChromosome() {
            return chromosome;
        }

        /**
         * <p>
         * The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'. Version number must be included if 
         * a versioned release of a primary build was used.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getGenomeBuild() {
            return genomeBuild;
        }

        /**
         * <p>
         * A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the 
         * gene is the "sense" strand, and the opposite complementary strand is the "antisense" strand.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link OrientationType}.
         */
        public OrientationType getOrientation() {
            return orientation;
        }

        /**
         * <p>
         * Reference identifier of reference sequence submitted to NCBI. It must match the type in the MolecularSequence.type 
         * field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, 
         * and “NP_” for amino acid sequences.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getReferenceSeqId() {
            return referenceSeqId;
        }

        /**
         * <p>
         * A pointer to another MolecularSequence entity as reference sequence.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getReferenceSeqPointer() {
            return referenceSeqPointer;
        }

        /**
         * <p>
         * A string like "ACGT".
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getReferenceSeqString() {
            return referenceSeqString;
        }

        /**
         * <p>
         * An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, 
         * and the Crick strand as the one whose 5'-end is on the long arm.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link StrandType}.
         */
        public StrandType getStrand() {
            return strand;
        }

        /**
         * <p>
         * Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then 
         * start position is inclusive.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getWindowStart() {
            return windowStart;
        }

        /**
         * <p>
         * End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and 
         * does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last 
         * position.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getWindowEnd() {
            return windowEnd;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (chromosome != null) || 
                (genomeBuild != null) || 
                (orientation != null) || 
                (referenceSeqId != null) || 
                (referenceSeqPointer != null) || 
                (referenceSeqString != null) || 
                (strand != null) || 
                (windowStart != null) || 
                (windowEnd != null);
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
                    accept(chromosome, "chromosome", visitor);
                    accept(genomeBuild, "genomeBuild", visitor);
                    accept(orientation, "orientation", visitor);
                    accept(referenceSeqId, "referenceSeqId", visitor);
                    accept(referenceSeqPointer, "referenceSeqPointer", visitor);
                    accept(referenceSeqString, "referenceSeqString", visitor);
                    accept(strand, "strand", visitor);
                    accept(windowStart, "windowStart", visitor);
                    accept(windowEnd, "windowEnd", visitor);
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
            ReferenceSeq other = (ReferenceSeq) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(chromosome, other.chromosome) && 
                Objects.equals(genomeBuild, other.genomeBuild) && 
                Objects.equals(orientation, other.orientation) && 
                Objects.equals(referenceSeqId, other.referenceSeqId) && 
                Objects.equals(referenceSeqPointer, other.referenceSeqPointer) && 
                Objects.equals(referenceSeqString, other.referenceSeqString) && 
                Objects.equals(strand, other.strand) && 
                Objects.equals(windowStart, other.windowStart) && 
                Objects.equals(windowEnd, other.windowEnd);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    chromosome, 
                    genomeBuild, 
                    orientation, 
                    referenceSeqId, 
                    referenceSeqPointer, 
                    referenceSeqString, 
                    strand, 
                    windowStart, 
                    windowEnd);
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
            private CodeableConcept chromosome;
            private String genomeBuild;
            private OrientationType orientation;
            private CodeableConcept referenceSeqId;
            private Reference referenceSeqPointer;
            private String referenceSeqString;
            private StrandType strand;
            private Integer windowStart;
            private Integer windowEnd;

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
             * Adds new element(s) to existing list
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
             * Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of 
             * specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.
             * org/browser/current_svn/term/SO:0000340)).
             * </p>
             * 
             * @param chromosome
             *     Chromosome containing genetic finding
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder chromosome(CodeableConcept chromosome) {
                this.chromosome = chromosome;
                return this;
            }

            /**
             * <p>
             * The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'. Version number must be included if 
             * a versioned release of a primary build was used.
             * </p>
             * 
             * @param genomeBuild
             *     The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder genomeBuild(String genomeBuild) {
                this.genomeBuild = genomeBuild;
                return this;
            }

            /**
             * <p>
             * A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the 
             * gene is the "sense" strand, and the opposite complementary strand is the "antisense" strand.
             * </p>
             * 
             * @param orientation
             *     sense | antisense
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder orientation(OrientationType orientation) {
                this.orientation = orientation;
                return this;
            }

            /**
             * <p>
             * Reference identifier of reference sequence submitted to NCBI. It must match the type in the MolecularSequence.type 
             * field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, 
             * and “NP_” for amino acid sequences.
             * </p>
             * 
             * @param referenceSeqId
             *     Reference identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder referenceSeqId(CodeableConcept referenceSeqId) {
                this.referenceSeqId = referenceSeqId;
                return this;
            }

            /**
             * <p>
             * A pointer to another MolecularSequence entity as reference sequence.
             * </p>
             * 
             * @param referenceSeqPointer
             *     A pointer to another MolecularSequence entity as reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder referenceSeqPointer(Reference referenceSeqPointer) {
                this.referenceSeqPointer = referenceSeqPointer;
                return this;
            }

            /**
             * <p>
             * A string like "ACGT".
             * </p>
             * 
             * @param referenceSeqString
             *     A string to represent reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder referenceSeqString(String referenceSeqString) {
                this.referenceSeqString = referenceSeqString;
                return this;
            }

            /**
             * <p>
             * An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, 
             * and the Crick strand as the one whose 5'-end is on the long arm.
             * </p>
             * 
             * @param strand
             *     watson | crick
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder strand(StrandType strand) {
                this.strand = strand;
                return this;
            }

            /**
             * <p>
             * Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then 
             * start position is inclusive.
             * </p>
             * 
             * @param windowStart
             *     Start position of the window on the reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder windowStart(Integer windowStart) {
                this.windowStart = windowStart;
                return this;
            }

            /**
             * <p>
             * End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and 
             * does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last 
             * position.
             * </p>
             * 
             * @param windowEnd
             *     End position of the window on the reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder windowEnd(Integer windowEnd) {
                this.windowEnd = windowEnd;
                return this;
            }

            @Override
            public ReferenceSeq build() {
                return new ReferenceSeq(this);
            }

            private Builder from(ReferenceSeq referenceSeq) {
                id = referenceSeq.id;
                extension.addAll(referenceSeq.extension);
                modifierExtension.addAll(referenceSeq.modifierExtension);
                chromosome = referenceSeq.chromosome;
                genomeBuild = referenceSeq.genomeBuild;
                orientation = referenceSeq.orientation;
                referenceSeqId = referenceSeq.referenceSeqId;
                referenceSeqPointer = referenceSeq.referenceSeqPointer;
                referenceSeqString = referenceSeq.referenceSeqString;
                strand = referenceSeq.strand;
                windowStart = referenceSeq.windowStart;
                windowEnd = referenceSeq.windowEnd;
                return this;
            }
        }
    }

    /**
     * <p>
     * The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.
     * org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including 
     * insertion,deletion,SNP,etc.) It can represent some complex mutation or segment variation with the assist of CIGAR 
     * string.
     * </p>
     */
    public static class Variant extends BackboneElement {
        private final Integer start;
        private final Integer end;
        private final String observedAllele;
        private final String referenceAllele;
        private final String cigar;
        private final Reference variantPointer;

        private volatile int hashCode;

        private Variant(Builder builder) {
            super(builder);
            start = builder.start;
            end = builder.end;
            observedAllele = builder.observedAllele;
            referenceAllele = builder.referenceAllele;
            cigar = builder.cigar;
            variantPointer = builder.variantPointer;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Start position of the variant on the reference sequence. If the coordinate system is either 0-based or 1-based, then 
         * start position is inclusive.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getStart() {
            return start;
        }

        /**
         * <p>
         * End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and 
         * does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last 
         * position.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getEnd() {
            return end;
        }

        /**
         * <p>
         * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.
         * org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position 
         * of sequence on the positive (+) strand of the observed sequence. When the sequence type is DNA, it should be the 
         * sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getObservedAllele() {
            return observedAllele;
        }

        /**
         * <p>
         * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.
         * org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position 
         * of sequence on the positive (+) strand of the reference sequence. When the sequence type is DNA, it should be the 
         * sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getReferenceAllele() {
            return referenceAllele;
        }

        /**
         * <p>
         * Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.
         * illumina.
         * com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.
         * htm).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getCigar() {
            return cigar;
        }

        /**
         * <p>
         * A pointer to an Observation containing variant information.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getVariantPointer() {
            return variantPointer;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (start != null) || 
                (end != null) || 
                (observedAllele != null) || 
                (referenceAllele != null) || 
                (cigar != null) || 
                (variantPointer != null);
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
                    accept(start, "start", visitor);
                    accept(end, "end", visitor);
                    accept(observedAllele, "observedAllele", visitor);
                    accept(referenceAllele, "referenceAllele", visitor);
                    accept(cigar, "cigar", visitor);
                    accept(variantPointer, "variantPointer", visitor);
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
            Variant other = (Variant) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(start, other.start) && 
                Objects.equals(end, other.end) && 
                Objects.equals(observedAllele, other.observedAllele) && 
                Objects.equals(referenceAllele, other.referenceAllele) && 
                Objects.equals(cigar, other.cigar) && 
                Objects.equals(variantPointer, other.variantPointer);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    start, 
                    end, 
                    observedAllele, 
                    referenceAllele, 
                    cigar, 
                    variantPointer);
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
            private Integer start;
            private Integer end;
            private String observedAllele;
            private String referenceAllele;
            private String cigar;
            private Reference variantPointer;

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
             * Adds new element(s) to existing list
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
             * Start position of the variant on the reference sequence. If the coordinate system is either 0-based or 1-based, then 
             * start position is inclusive.
             * </p>
             * 
             * @param start
             *     Start position of the variant on the reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder start(Integer start) {
                this.start = start;
                return this;
            }

            /**
             * <p>
             * End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and 
             * does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last 
             * position.
             * </p>
             * 
             * @param end
             *     End position of the variant on the reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder end(Integer end) {
                this.end = end;
                return this;
            }

            /**
             * <p>
             * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.
             * org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position 
             * of sequence on the positive (+) strand of the observed sequence. When the sequence type is DNA, it should be the 
             * sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
             * </p>
             * 
             * @param observedAllele
             *     Allele that was observed
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder observedAllele(String observedAllele) {
                this.observedAllele = observedAllele;
                return this;
            }

            /**
             * <p>
             * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.
             * org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position 
             * of sequence on the positive (+) strand of the reference sequence. When the sequence type is DNA, it should be the 
             * sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
             * </p>
             * 
             * @param referenceAllele
             *     Allele in the reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder referenceAllele(String referenceAllele) {
                this.referenceAllele = referenceAllele;
                return this;
            }

            /**
             * <p>
             * Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.
             * illumina.
             * com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.
             * htm).
             * </p>
             * 
             * @param cigar
             *     Extended CIGAR string for aligning the sequence with reference bases
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder cigar(String cigar) {
                this.cigar = cigar;
                return this;
            }

            /**
             * <p>
             * A pointer to an Observation containing variant information.
             * </p>
             * 
             * @param variantPointer
             *     Pointer to observed variant information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder variantPointer(Reference variantPointer) {
                this.variantPointer = variantPointer;
                return this;
            }

            @Override
            public Variant build() {
                return new Variant(this);
            }

            private Builder from(Variant variant) {
                id = variant.id;
                extension.addAll(variant.extension);
                modifierExtension.addAll(variant.modifierExtension);
                start = variant.start;
                end = variant.end;
                observedAllele = variant.observedAllele;
                referenceAllele = variant.referenceAllele;
                cigar = variant.cigar;
                variantPointer = variant.variantPointer;
                return this;
            }
        }
    }

    /**
     * <p>
     * An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred 
     * quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).
     * </p>
     */
    public static class Quality extends BackboneElement {
        private final QualityType type;
        private final CodeableConcept standardSequence;
        private final Integer start;
        private final Integer end;
        private final Quantity score;
        private final CodeableConcept method;
        private final Decimal truthTP;
        private final Decimal queryTP;
        private final Decimal truthFN;
        private final Decimal queryFP;
        private final Decimal gtFP;
        private final Decimal precision;
        private final Decimal recall;
        private final Decimal fScore;
        private final Roc roc;

        private volatile int hashCode;

        private Quality(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            standardSequence = builder.standardSequence;
            start = builder.start;
            end = builder.end;
            score = builder.score;
            method = builder.method;
            truthTP = builder.truthTP;
            queryTP = builder.queryTP;
            truthFN = builder.truthFN;
            queryFP = builder.queryFP;
            gtFP = builder.gtFP;
            precision = builder.precision;
            recall = builder.recall;
            fScore = builder.fScore;
            roc = builder.roc;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * INDEL / SNP / Undefined variant.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link QualityType}.
         */
        public QualityType getType() {
            return type;
        }

        /**
         * <p>
         * Gold standard sequence used for comparing against.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getStandardSequence() {
            return standardSequence;
        }

        /**
         * <p>
         * Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is 
         * inclusive.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getStart() {
            return start;
        }

        /**
         * <p>
         * End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last 
         * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getEnd() {
            return end;
        }

        /**
         * <p>
         * The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.
         * org/browser/current_svn/term/SO:0001685)).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Quantity}.
         */
        public Quantity getScore() {
            return score;
        }

        /**
         * <p>
         * Which method is used to get sequence quality.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getMethod() {
            return method;
        }

        /**
         * <p>
         * True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there 
         * are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is 
         * an accurate genotype call for the event.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getTruthTP() {
            return truthTP;
        }

        /**
         * <p>
         * True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there 
         * are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is 
         * an accurate genotype call for the event.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getQueryTP() {
            return queryTP;
        }

        /**
         * <p>
         * False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set 
         * that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for 
         * the event. Sites with correct variant but incorrect genotype are counted here.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getTruthFN() {
            return truthFN;
        }

        /**
         * <p>
         * False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set 
         * that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getQueryFP() {
            return queryFP;
        }

        /**
         * <p>
         * The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the 
         * truth is 1/1 and the query is 0/1 or similar).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getGtFP() {
            return gtFP;
        }

        /**
         * <p>
         * QUERY.TP / (QUERY.TP + QUERY.FP).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getPrecision() {
            return precision;
        }

        /**
         * <p>
         * TRUTH.TP / (TRUTH.TP + TRUTH.FN).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getRecall() {
            return recall;
        }

        /**
         * <p>
         * Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getFScore() {
            return fScore;
        }

        /**
         * <p>
         * Receiver Operator Characteristic (ROC) Curve to give sensitivity/specificity tradeoff.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Roc}.
         */
        public Roc getRoc() {
            return roc;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (standardSequence != null) || 
                (start != null) || 
                (end != null) || 
                (score != null) || 
                (method != null) || 
                (truthTP != null) || 
                (queryTP != null) || 
                (truthFN != null) || 
                (queryFP != null) || 
                (gtFP != null) || 
                (precision != null) || 
                (recall != null) || 
                (fScore != null) || 
                (roc != null);
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
                    accept(type, "type", visitor);
                    accept(standardSequence, "standardSequence", visitor);
                    accept(start, "start", visitor);
                    accept(end, "end", visitor);
                    accept(score, "score", visitor);
                    accept(method, "method", visitor);
                    accept(truthTP, "truthTP", visitor);
                    accept(queryTP, "queryTP", visitor);
                    accept(truthFN, "truthFN", visitor);
                    accept(queryFP, "queryFP", visitor);
                    accept(gtFP, "gtFP", visitor);
                    accept(precision, "precision", visitor);
                    accept(recall, "recall", visitor);
                    accept(fScore, "fScore", visitor);
                    accept(roc, "roc", visitor);
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
            Quality other = (Quality) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(standardSequence, other.standardSequence) && 
                Objects.equals(start, other.start) && 
                Objects.equals(end, other.end) && 
                Objects.equals(score, other.score) && 
                Objects.equals(method, other.method) && 
                Objects.equals(truthTP, other.truthTP) && 
                Objects.equals(queryTP, other.queryTP) && 
                Objects.equals(truthFN, other.truthFN) && 
                Objects.equals(queryFP, other.queryFP) && 
                Objects.equals(gtFP, other.gtFP) && 
                Objects.equals(precision, other.precision) && 
                Objects.equals(recall, other.recall) && 
                Objects.equals(fScore, other.fScore) && 
                Objects.equals(roc, other.roc);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    standardSequence, 
                    start, 
                    end, 
                    score, 
                    method, 
                    truthTP, 
                    queryTP, 
                    truthFN, 
                    queryFP, 
                    gtFP, 
                    precision, 
                    recall, 
                    fScore, 
                    roc);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(type).from(this);
        }

        public Builder toBuilder(QualityType type) {
            return new Builder(type).from(this);
        }

        public static Builder builder(QualityType type) {
            return new Builder(type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final QualityType type;

            // optional
            private CodeableConcept standardSequence;
            private Integer start;
            private Integer end;
            private Quantity score;
            private CodeableConcept method;
            private Decimal truthTP;
            private Decimal queryTP;
            private Decimal truthFN;
            private Decimal queryFP;
            private Decimal gtFP;
            private Decimal precision;
            private Decimal recall;
            private Decimal fScore;
            private Roc roc;

            private Builder(QualityType type) {
                super();
                this.type = type;
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
             * Adds new element(s) to existing list
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
             * Gold standard sequence used for comparing against.
             * </p>
             * 
             * @param standardSequence
             *     Standard sequence for comparison
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder standardSequence(CodeableConcept standardSequence) {
                this.standardSequence = standardSequence;
                return this;
            }

            /**
             * <p>
             * Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is 
             * inclusive.
             * </p>
             * 
             * @param start
             *     Start position of the sequence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder start(Integer start) {
                this.start = start;
                return this;
            }

            /**
             * <p>
             * End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last 
             * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
             * </p>
             * 
             * @param end
             *     End position of the sequence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder end(Integer end) {
                this.end = end;
                return this;
            }

            /**
             * <p>
             * The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.
             * org/browser/current_svn/term/SO:0001685)).
             * </p>
             * 
             * @param score
             *     Quality score for the comparison
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder score(Quantity score) {
                this.score = score;
                return this;
            }

            /**
             * <p>
             * Which method is used to get sequence quality.
             * </p>
             * 
             * @param method
             *     Method to get quality
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder method(CodeableConcept method) {
                this.method = method;
                return this;
            }

            /**
             * <p>
             * True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there 
             * are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is 
             * an accurate genotype call for the event.
             * </p>
             * 
             * @param truthTP
             *     True positives from the perspective of the truth data
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder truthTP(Decimal truthTP) {
                this.truthTP = truthTP;
                return this;
            }

            /**
             * <p>
             * True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there 
             * are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is 
             * an accurate genotype call for the event.
             * </p>
             * 
             * @param queryTP
             *     True positives from the perspective of the query data
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder queryTP(Decimal queryTP) {
                this.queryTP = queryTP;
                return this;
            }

            /**
             * <p>
             * False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set 
             * that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for 
             * the event. Sites with correct variant but incorrect genotype are counted here.
             * </p>
             * 
             * @param truthFN
             *     False negatives
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder truthFN(Decimal truthFN) {
                this.truthFN = truthFN;
                return this;
            }

            /**
             * <p>
             * False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set 
             * that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.
             * </p>
             * 
             * @param queryFP
             *     False positives
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder queryFP(Decimal queryFP) {
                this.queryFP = queryFP;
                return this;
            }

            /**
             * <p>
             * The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the 
             * truth is 1/1 and the query is 0/1 or similar).
             * </p>
             * 
             * @param gtFP
             *     False positives where the non-REF alleles in the Truth and Query Call Sets match
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder gtFP(Decimal gtFP) {
                this.gtFP = gtFP;
                return this;
            }

            /**
             * <p>
             * QUERY.TP / (QUERY.TP + QUERY.FP).
             * </p>
             * 
             * @param precision
             *     Precision of comparison
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder precision(Decimal precision) {
                this.precision = precision;
                return this;
            }

            /**
             * <p>
             * TRUTH.TP / (TRUTH.TP + TRUTH.FN).
             * </p>
             * 
             * @param recall
             *     Recall of comparison
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder recall(Decimal recall) {
                this.recall = recall;
                return this;
            }

            /**
             * <p>
             * Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).
             * </p>
             * 
             * @param fScore
             *     F-score
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder fScore(Decimal fScore) {
                this.fScore = fScore;
                return this;
            }

            /**
             * <p>
             * Receiver Operator Characteristic (ROC) Curve to give sensitivity/specificity tradeoff.
             * </p>
             * 
             * @param roc
             *     Receiver Operator Characteristic (ROC) Curve
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder roc(Roc roc) {
                this.roc = roc;
                return this;
            }

            @Override
            public Quality build() {
                return new Quality(this);
            }

            private Builder from(Quality quality) {
                id = quality.id;
                extension.addAll(quality.extension);
                modifierExtension.addAll(quality.modifierExtension);
                standardSequence = quality.standardSequence;
                start = quality.start;
                end = quality.end;
                score = quality.score;
                method = quality.method;
                truthTP = quality.truthTP;
                queryTP = quality.queryTP;
                truthFN = quality.truthFN;
                queryFP = quality.queryFP;
                gtFP = quality.gtFP;
                precision = quality.precision;
                recall = quality.recall;
                fScore = quality.fScore;
                roc = quality.roc;
                return this;
            }
        }

        /**
         * <p>
         * Receiver Operator Characteristic (ROC) Curve to give sensitivity/specificity tradeoff.
         * </p>
         */
        public static class Roc extends BackboneElement {
            private final List<Integer> score;
            private final List<Integer> numTP;
            private final List<Integer> numFP;
            private final List<Integer> numFN;
            private final List<Decimal> precision;
            private final List<Decimal> sensitivity;
            private final List<Decimal> fMeasure;

            private volatile int hashCode;

            private Roc(Builder builder) {
                super(builder);
                score = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.score, "score"));
                numTP = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.numTP, "numTP"));
                numFP = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.numFP, "numFP"));
                numFN = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.numFN, "numFN"));
                precision = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.precision, "precision"));
                sensitivity = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.sensitivity, "sensitivity"));
                fMeasure = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.fMeasure, "fMeasure"));
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Invidual data point representing the GQ (genotype quality) score threshold.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Integer}.
             */
            public List<Integer> getScore() {
                return score;
            }

            /**
             * <p>
             * The number of true positives if the GQ score threshold was set to "score" field value.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Integer}.
             */
            public List<Integer> getNumTP() {
                return numTP;
            }

            /**
             * <p>
             * The number of false positives if the GQ score threshold was set to "score" field value.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Integer}.
             */
            public List<Integer> getNumFP() {
                return numFP;
            }

            /**
             * <p>
             * The number of false negatives if the GQ score threshold was set to "score" field value.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Integer}.
             */
            public List<Integer> getNumFN() {
                return numFN;
            }

            /**
             * <p>
             * Calculated precision if the GQ score threshold was set to "score" field value.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Decimal}.
             */
            public List<Decimal> getPrecision() {
                return precision;
            }

            /**
             * <p>
             * Calculated sensitivity if the GQ score threshold was set to "score" field value.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Decimal}.
             */
            public List<Decimal> getSensitivity() {
                return sensitivity;
            }

            /**
             * <p>
             * Calculated fScore if the GQ score threshold was set to "score" field value.
             * </p>
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Decimal}.
             */
            public List<Decimal> getFMeasure() {
                return fMeasure;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    !score.isEmpty() || 
                    !numTP.isEmpty() || 
                    !numFP.isEmpty() || 
                    !numFN.isEmpty() || 
                    !precision.isEmpty() || 
                    !sensitivity.isEmpty() || 
                    !fMeasure.isEmpty();
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
                        accept(score, "score", visitor, Integer.class);
                        accept(numTP, "numTP", visitor, Integer.class);
                        accept(numFP, "numFP", visitor, Integer.class);
                        accept(numFN, "numFN", visitor, Integer.class);
                        accept(precision, "precision", visitor, Decimal.class);
                        accept(sensitivity, "sensitivity", visitor, Decimal.class);
                        accept(fMeasure, "fMeasure", visitor, Decimal.class);
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
                Roc other = (Roc) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(score, other.score) && 
                    Objects.equals(numTP, other.numTP) && 
                    Objects.equals(numFP, other.numFP) && 
                    Objects.equals(numFN, other.numFN) && 
                    Objects.equals(precision, other.precision) && 
                    Objects.equals(sensitivity, other.sensitivity) && 
                    Objects.equals(fMeasure, other.fMeasure);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        score, 
                        numTP, 
                        numFP, 
                        numFN, 
                        precision, 
                        sensitivity, 
                        fMeasure);
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
                private List<Integer> score = new ArrayList<>();
                private List<Integer> numTP = new ArrayList<>();
                private List<Integer> numFP = new ArrayList<>();
                private List<Integer> numFN = new ArrayList<>();
                private List<Decimal> precision = new ArrayList<>();
                private List<Decimal> sensitivity = new ArrayList<>();
                private List<Decimal> fMeasure = new ArrayList<>();

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
                 * Adds new element(s) to existing list
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
                 * Invidual data point representing the GQ (genotype quality) score threshold.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param score
                 *     Genotype quality score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder score(Integer... score) {
                    for (Integer value : score) {
                        this.score.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Invidual data point representing the GQ (genotype quality) score threshold.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param score
                 *     Genotype quality score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder score(Collection<Integer> score) {
                    this.score = new ArrayList<>(score);
                    return this;
                }

                /**
                 * <p>
                 * The number of true positives if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param numTP
                 *     Roc score true positive numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder numTP(Integer... numTP) {
                    for (Integer value : numTP) {
                        this.numTP.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The number of true positives if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param numTP
                 *     Roc score true positive numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder numTP(Collection<Integer> numTP) {
                    this.numTP = new ArrayList<>(numTP);
                    return this;
                }

                /**
                 * <p>
                 * The number of false positives if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param numFP
                 *     Roc score false positive numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder numFP(Integer... numFP) {
                    for (Integer value : numFP) {
                        this.numFP.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The number of false positives if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param numFP
                 *     Roc score false positive numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder numFP(Collection<Integer> numFP) {
                    this.numFP = new ArrayList<>(numFP);
                    return this;
                }

                /**
                 * <p>
                 * The number of false negatives if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param numFN
                 *     Roc score false negative numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder numFN(Integer... numFN) {
                    for (Integer value : numFN) {
                        this.numFN.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * The number of false negatives if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param numFN
                 *     Roc score false negative numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder numFN(Collection<Integer> numFN) {
                    this.numFN = new ArrayList<>(numFN);
                    return this;
                }

                /**
                 * <p>
                 * Calculated precision if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param precision
                 *     Precision of the GQ score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder precision(Decimal... precision) {
                    for (Decimal value : precision) {
                        this.precision.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Calculated precision if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param precision
                 *     Precision of the GQ score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder precision(Collection<Decimal> precision) {
                    this.precision = new ArrayList<>(precision);
                    return this;
                }

                /**
                 * <p>
                 * Calculated sensitivity if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param sensitivity
                 *     Sensitivity of the GQ score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder sensitivity(Decimal... sensitivity) {
                    for (Decimal value : sensitivity) {
                        this.sensitivity.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Calculated sensitivity if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param sensitivity
                 *     Sensitivity of the GQ score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder sensitivity(Collection<Decimal> sensitivity) {
                    this.sensitivity = new ArrayList<>(sensitivity);
                    return this;
                }

                /**
                 * <p>
                 * Calculated fScore if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Adds new element(s) to existing list
                 * </p>
                 * 
                 * @param fMeasure
                 *     FScore of the GQ score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder fMeasure(Decimal... fMeasure) {
                    for (Decimal value : fMeasure) {
                        this.fMeasure.add(value);
                    }
                    return this;
                }

                /**
                 * <p>
                 * Calculated fScore if the GQ score threshold was set to "score" field value.
                 * </p>
                 * <p>
                 * Replaces existing list with a new one containing elements from the Collection
                 * </p>
                 * 
                 * @param fMeasure
                 *     FScore of the GQ score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder fMeasure(Collection<Decimal> fMeasure) {
                    this.fMeasure = new ArrayList<>(fMeasure);
                    return this;
                }

                @Override
                public Roc build() {
                    return new Roc(this);
                }

                private Builder from(Roc roc) {
                    id = roc.id;
                    extension.addAll(roc.extension);
                    modifierExtension.addAll(roc.modifierExtension);
                    score.addAll(roc.score);
                    numTP.addAll(roc.numTP);
                    numFP.addAll(roc.numFP);
                    numFN.addAll(roc.numFN);
                    precision.addAll(roc.precision);
                    sensitivity.addAll(roc.sensitivity);
                    fMeasure.addAll(roc.fMeasure);
                    return this;
                }
            }
        }
    }

    /**
     * <p>
     * Configurations of the external repository. The repository shall store target's observedSeq or records related with 
     * target's observedSeq.
     * </p>
     */
    public static class Repository extends BackboneElement {
        private final RepositoryType type;
        private final Uri url;
        private final String name;
        private final String datasetId;
        private final String variantsetId;
        private final String readsetId;

        private volatile int hashCode;

        private Repository(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            url = builder.url;
            name = builder.name;
            datasetId = builder.datasetId;
            variantsetId = builder.variantsetId;
            readsetId = builder.readsetId;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link RepositoryType}.
         */
        public RepositoryType getType() {
            return type;
        }

        /**
         * <p>
         * URI of an external repository which contains further details about the genetics data.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Uri}.
         */
        public Uri getUrl() {
            return url;
        }

        /**
         * <p>
         * URI of an external repository which contains further details about the genetics data.
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
         * Id of the variant in this external repository. The server will understand how to use this id to call for more info 
         * about datasets in external repository.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getDatasetId() {
            return datasetId;
        }

        /**
         * <p>
         * Id of the variantset in this external repository. The server will understand how to use this id to call for more info 
         * about variantsets in external repository.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getVariantsetId() {
            return variantsetId;
        }

        /**
         * <p>
         * Id of the read in this external repository.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getReadsetId() {
            return readsetId;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (url != null) || 
                (name != null) || 
                (datasetId != null) || 
                (variantsetId != null) || 
                (readsetId != null);
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
                    accept(type, "type", visitor);
                    accept(url, "url", visitor);
                    accept(name, "name", visitor);
                    accept(datasetId, "datasetId", visitor);
                    accept(variantsetId, "variantsetId", visitor);
                    accept(readsetId, "readsetId", visitor);
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
            Repository other = (Repository) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(url, other.url) && 
                Objects.equals(name, other.name) && 
                Objects.equals(datasetId, other.datasetId) && 
                Objects.equals(variantsetId, other.variantsetId) && 
                Objects.equals(readsetId, other.readsetId);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    url, 
                    name, 
                    datasetId, 
                    variantsetId, 
                    readsetId);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder(type).from(this);
        }

        public Builder toBuilder(RepositoryType type) {
            return new Builder(type).from(this);
        }

        public static Builder builder(RepositoryType type) {
            return new Builder(type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final RepositoryType type;

            // optional
            private Uri url;
            private String name;
            private String datasetId;
            private String variantsetId;
            private String readsetId;

            private Builder(RepositoryType type) {
                super();
                this.type = type;
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
             * Adds new element(s) to existing list
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
             * URI of an external repository which contains further details about the genetics data.
             * </p>
             * 
             * @param url
             *     URI of the repository
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder url(Uri url) {
                this.url = url;
                return this;
            }

            /**
             * <p>
             * URI of an external repository which contains further details about the genetics data.
             * </p>
             * 
             * @param name
             *     Repository's name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * <p>
             * Id of the variant in this external repository. The server will understand how to use this id to call for more info 
             * about datasets in external repository.
             * </p>
             * 
             * @param datasetId
             *     Id of the dataset that used to call for dataset in repository
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder datasetId(String datasetId) {
                this.datasetId = datasetId;
                return this;
            }

            /**
             * <p>
             * Id of the variantset in this external repository. The server will understand how to use this id to call for more info 
             * about variantsets in external repository.
             * </p>
             * 
             * @param variantsetId
             *     Id of the variantset that used to call for variantset in repository
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder variantsetId(String variantsetId) {
                this.variantsetId = variantsetId;
                return this;
            }

            /**
             * <p>
             * Id of the read in this external repository.
             * </p>
             * 
             * @param readsetId
             *     Id of the read
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder readsetId(String readsetId) {
                this.readsetId = readsetId;
                return this;
            }

            @Override
            public Repository build() {
                return new Repository(this);
            }

            private Builder from(Repository repository) {
                id = repository.id;
                extension.addAll(repository.extension);
                modifierExtension.addAll(repository.modifierExtension);
                url = repository.url;
                name = repository.name;
                datasetId = repository.datasetId;
                variantsetId = repository.variantsetId;
                readsetId = repository.readsetId;
                return this;
            }
        }
    }

    /**
     * <p>
     * Information about chromosome structure variation.
     * </p>
     */
    public static class StructureVariant extends BackboneElement {
        private final CodeableConcept variantType;
        private final Boolean exact;
        private final Integer length;
        private final Outer outer;
        private final Inner inner;

        private volatile int hashCode;

        private StructureVariant(Builder builder) {
            super(builder);
            variantType = builder.variantType;
            exact = builder.exact;
            length = builder.length;
            outer = builder.outer;
            inner = builder.inner;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * Information about chromosome structure variation DNA change type.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getVariantType() {
            return variantType;
        }

        /**
         * <p>
         * Used to indicate if the outer and inner start-end values have the same meaning.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getExact() {
            return exact;
        }

        /**
         * <p>
         * Length of the variant chromosome.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getLength() {
            return length;
        }

        /**
         * <p>
         * Structural variant outer.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Outer}.
         */
        public Outer getOuter() {
            return outer;
        }

        /**
         * <p>
         * Structural variant inner.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Inner}.
         */
        public Inner getInner() {
            return inner;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (variantType != null) || 
                (exact != null) || 
                (length != null) || 
                (outer != null) || 
                (inner != null);
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
                    accept(variantType, "variantType", visitor);
                    accept(exact, "exact", visitor);
                    accept(length, "length", visitor);
                    accept(outer, "outer", visitor);
                    accept(inner, "inner", visitor);
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
            StructureVariant other = (StructureVariant) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(variantType, other.variantType) && 
                Objects.equals(exact, other.exact) && 
                Objects.equals(length, other.length) && 
                Objects.equals(outer, other.outer) && 
                Objects.equals(inner, other.inner);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    variantType, 
                    exact, 
                    length, 
                    outer, 
                    inner);
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
            private CodeableConcept variantType;
            private Boolean exact;
            private Integer length;
            private Outer outer;
            private Inner inner;

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
             * Adds new element(s) to existing list
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
             * Information about chromosome structure variation DNA change type.
             * </p>
             * 
             * @param variantType
             *     Structural variant change type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder variantType(CodeableConcept variantType) {
                this.variantType = variantType;
                return this;
            }

            /**
             * <p>
             * Used to indicate if the outer and inner start-end values have the same meaning.
             * </p>
             * 
             * @param exact
             *     Does the structural variant have base pair resolution breakpoints?
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder exact(Boolean exact) {
                this.exact = exact;
                return this;
            }

            /**
             * <p>
             * Length of the variant chromosome.
             * </p>
             * 
             * @param length
             *     Structural variant length
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder length(Integer length) {
                this.length = length;
                return this;
            }

            /**
             * <p>
             * Structural variant outer.
             * </p>
             * 
             * @param outer
             *     Structural variant outer
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder outer(Outer outer) {
                this.outer = outer;
                return this;
            }

            /**
             * <p>
             * Structural variant inner.
             * </p>
             * 
             * @param inner
             *     Structural variant inner
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder inner(Inner inner) {
                this.inner = inner;
                return this;
            }

            @Override
            public StructureVariant build() {
                return new StructureVariant(this);
            }

            private Builder from(StructureVariant structureVariant) {
                id = structureVariant.id;
                extension.addAll(structureVariant.extension);
                modifierExtension.addAll(structureVariant.modifierExtension);
                variantType = structureVariant.variantType;
                exact = structureVariant.exact;
                length = structureVariant.length;
                outer = structureVariant.outer;
                inner = structureVariant.inner;
                return this;
            }
        }

        /**
         * <p>
         * Structural variant outer.
         * </p>
         */
        public static class Outer extends BackboneElement {
            private final Integer start;
            private final Integer end;

            private volatile int hashCode;

            private Outer(Builder builder) {
                super(builder);
                start = builder.start;
                end = builder.end;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is 
             * inclusive.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Integer}.
             */
            public Integer getStart() {
                return start;
            }

            /**
             * <p>
             * Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last 
             * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Integer}.
             */
            public Integer getEnd() {
                return end;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (start != null) || 
                    (end != null);
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
                        accept(start, "start", visitor);
                        accept(end, "end", visitor);
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
                Outer other = (Outer) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(start, other.start) && 
                    Objects.equals(end, other.end);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        start, 
                        end);
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
                private Integer start;
                private Integer end;

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
                 * Adds new element(s) to existing list
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
                 * Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is 
                 * inclusive.
                 * </p>
                 * 
                 * @param start
                 *     Structural variant outer start
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder start(Integer start) {
                    this.start = start;
                    return this;
                }

                /**
                 * <p>
                 * Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last 
                 * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
                 * </p>
                 * 
                 * @param end
                 *     Structural variant outer end
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder end(Integer end) {
                    this.end = end;
                    return this;
                }

                @Override
                public Outer build() {
                    return new Outer(this);
                }

                private Builder from(Outer outer) {
                    id = outer.id;
                    extension.addAll(outer.extension);
                    modifierExtension.addAll(outer.modifierExtension);
                    start = outer.start;
                    end = outer.end;
                    return this;
                }
            }
        }

        /**
         * <p>
         * Structural variant inner.
         * </p>
         */
        public static class Inner extends BackboneElement {
            private final Integer start;
            private final Integer end;

            private volatile int hashCode;

            private Inner(Builder builder) {
                super(builder);
                start = builder.start;
                end = builder.end;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * <p>
             * Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is 
             * inclusive.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Integer}.
             */
            public Integer getStart() {
                return start;
            }

            /**
             * <p>
             * Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last 
             * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link Integer}.
             */
            public Integer getEnd() {
                return end;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (start != null) || 
                    (end != null);
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
                        accept(start, "start", visitor);
                        accept(end, "end", visitor);
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
                Inner other = (Inner) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(start, other.start) && 
                    Objects.equals(end, other.end);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        start, 
                        end);
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
                private Integer start;
                private Integer end;

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
                 * Adds new element(s) to existing list
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
                 * Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is 
                 * inclusive.
                 * </p>
                 * 
                 * @param start
                 *     Structural variant inner start
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder start(Integer start) {
                    this.start = start;
                    return this;
                }

                /**
                 * <p>
                 * Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last 
                 * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
                 * </p>
                 * 
                 * @param end
                 *     Structural variant inner end
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder end(Integer end) {
                    this.end = end;
                    return this;
                }

                @Override
                public Inner build() {
                    return new Inner(this);
                }

                private Builder from(Inner inner) {
                    id = inner.id;
                    extension.addAll(inner.extension);
                    modifierExtension.addAll(inner.modifierExtension);
                    start = inner.start;
                    end = inner.end;
                    return this;
                }
            }
        }
    }
}
