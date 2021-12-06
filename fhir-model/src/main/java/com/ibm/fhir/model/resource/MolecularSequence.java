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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.OrientationType;
import com.ibm.fhir.model.type.code.QualityType;
import com.ibm.fhir.model.type.code.RepositoryType;
import com.ibm.fhir.model.type.code.SequenceType;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.StrandType;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Raw data describing a biological sequence.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "msq-3",
    level = "Rule",
    location = "(base)",
    description = "Only 0 and 1 are valid for coordinateSystem",
    expression = "coordinateSystem = 1 or coordinateSystem = 0",
    source = "http://hl7.org/fhir/StructureDefinition/MolecularSequence"
)
@Constraint(
    id = "msq-5",
    level = "Rule",
    location = "MolecularSequence.referenceSeq",
    description = "GenomeBuild and chromosome must be both contained if either one of them is contained",
    expression = "(chromosome.empty() and genomeBuild.empty()) or (chromosome.exists() and genomeBuild.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/MolecularSequence"
)
@Constraint(
    id = "msq-6",
    level = "Rule",
    location = "MolecularSequence.referenceSeq",
    description = "Have and only have one of the following elements in referenceSeq : 1. genomeBuild ; 2 referenceSeqId; 3. referenceSeqPointer;  4. referenceSeqString;",
    expression = "(genomeBuild.count()+referenceSeqId.count()+ referenceSeqPointer.count()+ referenceSeqString.count()) = 1",
    source = "http://hl7.org/fhir/StructureDefinition/MolecularSequence"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MolecularSequence extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "sequenceType",
        strength = BindingStrength.Value.REQUIRED,
        description = "Type if a sequence -- DNA, RNA, or amino acid sequence.",
        valueSet = "http://hl7.org/fhir/ValueSet/sequence-type|4.1.0"
    )
    private final SequenceType type;
    @Summary
    @Required
    private final Integer coordinateSystem;
    @Summary
    @ReferenceTarget({ "Patient" })
    private final Reference patient;
    @Summary
    @ReferenceTarget({ "Specimen" })
    private final Reference specimen;
    @Summary
    @ReferenceTarget({ "Device" })
    private final Reference device;
    @Summary
    @ReferenceTarget({ "Organization" })
    private final Reference performer;
    @Summary
    private final Quantity quantity;
    @Summary
    private final ReferenceSeq referenceSeq;
    @Summary
    private final List<Variant> variant;
    @Summary
    private final String observedSeq;
    @Summary
    private final List<Quality> quality;
    @Summary
    private final Integer readCoverage;
    @Summary
    private final List<Repository> repository;
    @Summary
    @ReferenceTarget({ "MolecularSequence" })
    private final List<Reference> pointer;
    @Summary
    private final List<StructureVariant> structureVariant;

    private MolecularSequence(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        type = builder.type;
        coordinateSystem = builder.coordinateSystem;
        patient = builder.patient;
        specimen = builder.specimen;
        device = builder.device;
        performer = builder.performer;
        quantity = builder.quantity;
        referenceSeq = builder.referenceSeq;
        variant = Collections.unmodifiableList(builder.variant);
        observedSeq = builder.observedSeq;
        quality = Collections.unmodifiableList(builder.quality);
        readCoverage = builder.readCoverage;
        repository = Collections.unmodifiableList(builder.repository);
        pointer = Collections.unmodifiableList(builder.pointer);
        structureVariant = Collections.unmodifiableList(builder.structureVariant);
    }

    /**
     * A unique identifier for this particular sequence instance. This is a FHIR-defined id.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Amino Acid Sequence/ DNA Sequence / RNA Sequence.
     * 
     * @return
     *     An immutable object of type {@link SequenceType} that may be null.
     */
    public SequenceType getType() {
        return type;
    }

    /**
     * Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or 
     * starting at 1 (1-based numbering, inclusive start and inclusive end).
     * 
     * @return
     *     An immutable object of type {@link Integer} that is non-null.
     */
    public Integer getCoordinateSystem() {
        return coordinateSystem;
    }

    /**
     * The patient whose sequencing results are described by this resource.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * Specimen used for sequencing.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSpecimen() {
        return specimen;
    }

    /**
     * The method for sequencing, for example, chip information.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getDevice() {
        return device;
    }

    /**
     * The organization or lab that should be responsible for this result.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPerformer() {
        return performer;
    }

    /**
     * The number of copies of the sequence of interest. (RNASeq).
     * 
     * @return
     *     An immutable object of type {@link Quantity} that may be null.
     */
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * A sequence that is used as a reference to describe variants that are present in a sequence analyzed.
     * 
     * @return
     *     An immutable object of type {@link ReferenceSeq} that may be null.
     */
    public ReferenceSeq getReferenceSeq() {
        return referenceSeq;
    }

    /**
     * The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.
     * org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including 
     * insertion,deletion,SNP,etc.) It can represent some complex mutation or segment variation with the assist of CIGAR 
     * string.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Variant} that may be empty.
     */
    public List<Variant> getVariant() {
        return variant;
    }

    /**
     * Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This 
     * shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getObservedSeq() {
        return observedSeq;
    }

    /**
     * An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred 
     * quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Quality} that may be empty.
     */
    public List<Quality> getQuality() {
        return quality;
    }

    /**
     * Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed 
     * sequence.
     * 
     * @return
     *     An immutable object of type {@link Integer} that may be null.
     */
    public Integer getReadCoverage() {
        return readCoverage;
    }

    /**
     * Configurations of the external repository. The repository shall store target's observedSeq or records related with 
     * target's observedSeq.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Repository} that may be empty.
     */
    public List<Repository> getRepository() {
        return repository;
    }

    /**
     * Pointer to next atomic sequence which at most contains one variant.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPointer() {
        return pointer;
    }

    /**
     * Information about chromosome structure variation.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link StructureVariant} that may be empty.
     */
    public List<StructureVariant> getStructureVariant() {
        return structureVariant;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (type != null) || 
            (coordinateSystem != null) || 
            (patient != null) || 
            (specimen != null) || 
            (device != null) || 
            (performer != null) || 
            (quantity != null) || 
            (referenceSeq != null) || 
            !variant.isEmpty() || 
            (observedSeq != null) || 
            !quality.isEmpty() || 
            (readCoverage != null) || 
            !repository.isEmpty() || 
            !pointer.isEmpty() || 
            !structureVariant.isEmpty();
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
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private SequenceType type;
        private Integer coordinateSystem;
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
         * A unique identifier for this particular sequence instance. This is a FHIR-defined id.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A unique identifier for this particular sequence instance. This is a FHIR-defined id.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique ID for this particular sequence. This is a FHIR-defined id
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
         * Amino Acid Sequence/ DNA Sequence / RNA Sequence.
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
         * Convenience method for setting {@code coordinateSystem}.
         * 
         * <p>This element is required.
         * 
         * @param coordinateSystem
         *     Base number of coordinate system (0 for 0-based numbering or coordinates, inclusive start, exclusive end, 1 for 1-
         *     based numbering, inclusive start, inclusive end)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #coordinateSystem(com.ibm.fhir.model.type.Integer)
         */
        public Builder coordinateSystem(java.lang.Integer coordinateSystem) {
            this.coordinateSystem = (coordinateSystem == null) ? null : Integer.of(coordinateSystem);
            return this;
        }

        /**
         * Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or 
         * starting at 1 (1-based numbering, inclusive start and inclusive end).
         * 
         * <p>This element is required.
         * 
         * @param coordinateSystem
         *     Base number of coordinate system (0 for 0-based numbering or coordinates, inclusive start, exclusive end, 1 for 1-
         *     based numbering, inclusive start, inclusive end)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder coordinateSystem(Integer coordinateSystem) {
            this.coordinateSystem = coordinateSystem;
            return this;
        }

        /**
         * The patient whose sequencing results are described by this resource.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
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
         * Specimen used for sequencing.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Specimen}</li>
         * </ul>
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
         * The method for sequencing, for example, chip information.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * </ul>
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
         * The organization or lab that should be responsible for this result.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
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
         * The number of copies of the sequence of interest. (RNASeq).
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
         * A sequence that is used as a reference to describe variants that are present in a sequence analyzed.
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
         * The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.
         * org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including 
         * insertion,deletion,SNP,etc.) It can represent some complex mutation or segment variation with the assist of CIGAR 
         * string.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.
         * org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including 
         * insertion,deletion,SNP,etc.) It can represent some complex mutation or segment variation with the assist of CIGAR 
         * string.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param variant
         *     Variant in sequence
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder variant(Collection<Variant> variant) {
            this.variant = new ArrayList<>(variant);
            return this;
        }

        /**
         * Convenience method for setting {@code observedSeq}.
         * 
         * @param observedSeq
         *     Sequence that was observed
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #observedSeq(com.ibm.fhir.model.type.String)
         */
        public Builder observedSeq(java.lang.String observedSeq) {
            this.observedSeq = (observedSeq == null) ? null : String.of(observedSeq);
            return this;
        }

        /**
         * Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This 
         * shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd.
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
         * An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred 
         * quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred 
         * quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param quality
         *     An set of value as quality of sequence
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder quality(Collection<Quality> quality) {
            this.quality = new ArrayList<>(quality);
            return this;
        }

        /**
         * Convenience method for setting {@code readCoverage}.
         * 
         * @param readCoverage
         *     Average number of reads representing a given nucleotide in the reconstructed sequence
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #readCoverage(com.ibm.fhir.model.type.Integer)
         */
        public Builder readCoverage(java.lang.Integer readCoverage) {
            this.readCoverage = (readCoverage == null) ? null : Integer.of(readCoverage);
            return this;
        }

        /**
         * Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed 
         * sequence.
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
         * Configurations of the external repository. The repository shall store target's observedSeq or records related with 
         * target's observedSeq.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Configurations of the external repository. The repository shall store target's observedSeq or records related with 
         * target's observedSeq.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param repository
         *     External repository which contains detailed report related with observedSeq in this resource
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder repository(Collection<Repository> repository) {
            this.repository = new ArrayList<>(repository);
            return this;
        }

        /**
         * Pointer to next atomic sequence which at most contains one variant.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MolecularSequence}</li>
         * </ul>
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
         * Pointer to next atomic sequence which at most contains one variant.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link MolecularSequence}</li>
         * </ul>
         * 
         * @param pointer
         *     Pointer to next atomic sequence
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder pointer(Collection<Reference> pointer) {
            this.pointer = new ArrayList<>(pointer);
            return this;
        }

        /**
         * Information about chromosome structure variation.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Information about chromosome structure variation.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param structureVariant
         *     Structural variant
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder structureVariant(Collection<StructureVariant> structureVariant) {
            this.structureVariant = new ArrayList<>(structureVariant);
            return this;
        }

        /**
         * Build the {@link MolecularSequence}
         * 
         * <p>Required elements:
         * <ul>
         * <li>coordinateSystem</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MolecularSequence}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MolecularSequence per the base specification
         */
        @Override
        public MolecularSequence build() {
            MolecularSequence molecularSequence = new MolecularSequence(this);
            if (validating) {
                validate(molecularSequence);
            }
            return molecularSequence;
        }

        protected void validate(MolecularSequence molecularSequence) {
            super.validate(molecularSequence);
            ValidationSupport.checkList(molecularSequence.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(molecularSequence.coordinateSystem, "coordinateSystem");
            ValidationSupport.checkList(molecularSequence.variant, "variant", Variant.class);
            ValidationSupport.checkList(molecularSequence.quality, "quality", Quality.class);
            ValidationSupport.checkList(molecularSequence.repository, "repository", Repository.class);
            ValidationSupport.checkList(molecularSequence.pointer, "pointer", Reference.class);
            ValidationSupport.checkList(molecularSequence.structureVariant, "structureVariant", StructureVariant.class);
            ValidationSupport.checkReferenceType(molecularSequence.patient, "patient", "Patient");
            ValidationSupport.checkReferenceType(molecularSequence.specimen, "specimen", "Specimen");
            ValidationSupport.checkReferenceType(molecularSequence.device, "device", "Device");
            ValidationSupport.checkReferenceType(molecularSequence.performer, "performer", "Organization");
            ValidationSupport.checkReferenceType(molecularSequence.pointer, "pointer", "MolecularSequence");
        }

        protected Builder from(MolecularSequence molecularSequence) {
            super.from(molecularSequence);
            identifier.addAll(molecularSequence.identifier);
            type = molecularSequence.type;
            coordinateSystem = molecularSequence.coordinateSystem;
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
     * A sequence that is used as a reference to describe variants that are present in a sequence analyzed.
     */
    public static class ReferenceSeq extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "chromosome-human",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Chromosome number for human.",
            valueSet = "http://hl7.org/fhir/ValueSet/chromosome-human"
        )
        private final CodeableConcept chromosome;
        @Summary
        private final String genomeBuild;
        @Summary
        @Binding(
            bindingName = "orientationType",
            strength = BindingStrength.Value.REQUIRED,
            description = "Type for orientation.",
            valueSet = "http://hl7.org/fhir/ValueSet/orientation-type|4.1.0"
        )
        private final OrientationType orientation;
        @Summary
        @Binding(
            bindingName = "sequenceReference",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Reference identifier.",
            valueSet = "http://hl7.org/fhir/ValueSet/sequence-referenceSeq"
        )
        private final CodeableConcept referenceSeqId;
        @Summary
        @ReferenceTarget({ "MolecularSequence" })
        private final Reference referenceSeqPointer;
        @Summary
        private final String referenceSeqString;
        @Summary
        @Binding(
            bindingName = "strandType",
            strength = BindingStrength.Value.REQUIRED,
            description = "Type for strand.",
            valueSet = "http://hl7.org/fhir/ValueSet/strand-type|4.1.0"
        )
        private final StrandType strand;
        @Summary
        private final Integer windowStart;
        @Summary
        private final Integer windowEnd;

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
        }

        /**
         * Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of 
         * specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.
         * org/browser/current_svn/term/SO:0000340)).
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getChromosome() {
            return chromosome;
        }

        /**
         * The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'. Version number must be included if 
         * a versioned release of a primary build was used.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getGenomeBuild() {
            return genomeBuild;
        }

        /**
         * A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the 
         * gene is the "sense" strand, and the opposite complementary strand is the "antisense" strand.
         * 
         * @return
         *     An immutable object of type {@link OrientationType} that may be null.
         */
        public OrientationType getOrientation() {
            return orientation;
        }

        /**
         * Reference identifier of reference sequence submitted to NCBI. It must match the type in the MolecularSequence.type 
         * field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, 
         * and “NP_” for amino acid sequences.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getReferenceSeqId() {
            return referenceSeqId;
        }

        /**
         * A pointer to another MolecularSequence entity as reference sequence.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getReferenceSeqPointer() {
            return referenceSeqPointer;
        }

        /**
         * A string like "ACGT".
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getReferenceSeqString() {
            return referenceSeqString;
        }

        /**
         * An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, 
         * and the Crick strand as the one whose 5'-end is on the long arm.
         * 
         * @return
         *     An immutable object of type {@link StrandType} that may be null.
         */
        public StrandType getStrand() {
            return strand;
        }

        /**
         * Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then 
         * start position is inclusive.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getWindowStart() {
            return windowStart;
        }

        /**
         * End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and 
         * does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last 
         * position.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
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
             * Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of 
             * specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.
             * org/browser/current_svn/term/SO:0000340)).
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
             * Convenience method for setting {@code genomeBuild}.
             * 
             * @param genomeBuild
             *     The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #genomeBuild(com.ibm.fhir.model.type.String)
             */
            public Builder genomeBuild(java.lang.String genomeBuild) {
                this.genomeBuild = (genomeBuild == null) ? null : String.of(genomeBuild);
                return this;
            }

            /**
             * The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'. Version number must be included if 
             * a versioned release of a primary build was used.
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
             * A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the 
             * gene is the "sense" strand, and the opposite complementary strand is the "antisense" strand.
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
             * Reference identifier of reference sequence submitted to NCBI. It must match the type in the MolecularSequence.type 
             * field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, 
             * and “NP_” for amino acid sequences.
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
             * A pointer to another MolecularSequence entity as reference sequence.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link MolecularSequence}</li>
             * </ul>
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
             * Convenience method for setting {@code referenceSeqString}.
             * 
             * @param referenceSeqString
             *     A string to represent reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #referenceSeqString(com.ibm.fhir.model.type.String)
             */
            public Builder referenceSeqString(java.lang.String referenceSeqString) {
                this.referenceSeqString = (referenceSeqString == null) ? null : String.of(referenceSeqString);
                return this;
            }

            /**
             * A string like "ACGT".
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
             * An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, 
             * and the Crick strand as the one whose 5'-end is on the long arm.
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
             * Convenience method for setting {@code windowStart}.
             * 
             * @param windowStart
             *     Start position of the window on the reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #windowStart(com.ibm.fhir.model.type.Integer)
             */
            public Builder windowStart(java.lang.Integer windowStart) {
                this.windowStart = (windowStart == null) ? null : Integer.of(windowStart);
                return this;
            }

            /**
             * Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then 
             * start position is inclusive.
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
             * Convenience method for setting {@code windowEnd}.
             * 
             * @param windowEnd
             *     End position of the window on the reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #windowEnd(com.ibm.fhir.model.type.Integer)
             */
            public Builder windowEnd(java.lang.Integer windowEnd) {
                this.windowEnd = (windowEnd == null) ? null : Integer.of(windowEnd);
                return this;
            }

            /**
             * End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and 
             * does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last 
             * position.
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

            /**
             * Build the {@link ReferenceSeq}
             * 
             * @return
             *     An immutable object of type {@link ReferenceSeq}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ReferenceSeq per the base specification
             */
            @Override
            public ReferenceSeq build() {
                ReferenceSeq referenceSeq = new ReferenceSeq(this);
                if (validating) {
                    validate(referenceSeq);
                }
                return referenceSeq;
            }

            protected void validate(ReferenceSeq referenceSeq) {
                super.validate(referenceSeq);
                ValidationSupport.checkReferenceType(referenceSeq.referenceSeqPointer, "referenceSeqPointer", "MolecularSequence");
                ValidationSupport.requireValueOrChildren(referenceSeq);
            }

            protected Builder from(ReferenceSeq referenceSeq) {
                super.from(referenceSeq);
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
     * The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.
     * org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including 
     * insertion,deletion,SNP,etc.) It can represent some complex mutation or segment variation with the assist of CIGAR 
     * string.
     */
    public static class Variant extends BackboneElement {
        @Summary
        private final Integer start;
        @Summary
        private final Integer end;
        @Summary
        private final String observedAllele;
        @Summary
        private final String referenceAllele;
        @Summary
        private final String cigar;
        @Summary
        @ReferenceTarget({ "Observation" })
        private final Reference variantPointer;

        private Variant(Builder builder) {
            super(builder);
            start = builder.start;
            end = builder.end;
            observedAllele = builder.observedAllele;
            referenceAllele = builder.referenceAllele;
            cigar = builder.cigar;
            variantPointer = builder.variantPointer;
        }

        /**
         * Start position of the variant on the reference sequence. If the coordinate system is either 0-based or 1-based, then 
         * start position is inclusive.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getStart() {
            return start;
        }

        /**
         * End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and 
         * does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last 
         * position.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getEnd() {
            return end;
        }

        /**
         * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.
         * org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position 
         * of sequence on the positive (+) strand of the observed sequence. When the sequence type is DNA, it should be the 
         * sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getObservedAllele() {
            return observedAllele;
        }

        /**
         * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.
         * org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position 
         * of sequence on the positive (+) strand of the reference sequence. When the sequence type is DNA, it should be the 
         * sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getReferenceAllele() {
            return referenceAllele;
        }

        /**
         * Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.
         * illumina.
         * com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.
         * htm).
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getCigar() {
            return cigar;
        }

        /**
         * A pointer to an Observation containing variant information.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
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
             * Convenience method for setting {@code start}.
             * 
             * @param start
             *     Start position of the variant on the reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #start(com.ibm.fhir.model.type.Integer)
             */
            public Builder start(java.lang.Integer start) {
                this.start = (start == null) ? null : Integer.of(start);
                return this;
            }

            /**
             * Start position of the variant on the reference sequence. If the coordinate system is either 0-based or 1-based, then 
             * start position is inclusive.
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
             * Convenience method for setting {@code end}.
             * 
             * @param end
             *     End position of the variant on the reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #end(com.ibm.fhir.model.type.Integer)
             */
            public Builder end(java.lang.Integer end) {
                this.end = (end == null) ? null : Integer.of(end);
                return this;
            }

            /**
             * End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and 
             * does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last 
             * position.
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
             * Convenience method for setting {@code observedAllele}.
             * 
             * @param observedAllele
             *     Allele that was observed
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #observedAllele(com.ibm.fhir.model.type.String)
             */
            public Builder observedAllele(java.lang.String observedAllele) {
                this.observedAllele = (observedAllele == null) ? null : String.of(observedAllele);
                return this;
            }

            /**
             * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.
             * org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position 
             * of sequence on the positive (+) strand of the observed sequence. When the sequence type is DNA, it should be the 
             * sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
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
             * Convenience method for setting {@code referenceAllele}.
             * 
             * @param referenceAllele
             *     Allele in the reference sequence
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #referenceAllele(com.ibm.fhir.model.type.String)
             */
            public Builder referenceAllele(java.lang.String referenceAllele) {
                this.referenceAllele = (referenceAllele == null) ? null : String.of(referenceAllele);
                return this;
            }

            /**
             * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.
             * org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position 
             * of sequence on the positive (+) strand of the reference sequence. When the sequence type is DNA, it should be the 
             * sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
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
             * Convenience method for setting {@code cigar}.
             * 
             * @param cigar
             *     Extended CIGAR string for aligning the sequence with reference bases
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #cigar(com.ibm.fhir.model.type.String)
             */
            public Builder cigar(java.lang.String cigar) {
                this.cigar = (cigar == null) ? null : String.of(cigar);
                return this;
            }

            /**
             * Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.
             * illumina.
             * com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.
             * htm).
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
             * A pointer to an Observation containing variant information.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Observation}</li>
             * </ul>
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

            /**
             * Build the {@link Variant}
             * 
             * @return
             *     An immutable object of type {@link Variant}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Variant per the base specification
             */
            @Override
            public Variant build() {
                Variant variant = new Variant(this);
                if (validating) {
                    validate(variant);
                }
                return variant;
            }

            protected void validate(Variant variant) {
                super.validate(variant);
                ValidationSupport.checkReferenceType(variant.variantPointer, "variantPointer", "Observation");
                ValidationSupport.requireValueOrChildren(variant);
            }

            protected Builder from(Variant variant) {
                super.from(variant);
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
     * An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred 
     * quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).
     */
    public static class Quality extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "qualityType",
            strength = BindingStrength.Value.REQUIRED,
            description = "Type for quality report.",
            valueSet = "http://hl7.org/fhir/ValueSet/quality-type|4.1.0"
        )
        @Required
        private final QualityType type;
        @Summary
        @Binding(
            bindingName = "qualityStandardSequence",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Reference identifier of the sequence that used to mark the quality of tested samples.",
            valueSet = "http://hl7.org/fhir/ValueSet/sequence-quality-standardSequence"
        )
        private final CodeableConcept standardSequence;
        @Summary
        private final Integer start;
        @Summary
        private final Integer end;
        @Summary
        private final Quantity score;
        @Summary
        @Binding(
            bindingName = "qualityMethod",
            strength = BindingStrength.Value.EXAMPLE,
            description = "The method used to evaluate the numerical quality of the observed sequence.",
            valueSet = "http://hl7.org/fhir/ValueSet/sequence-quality-method"
        )
        private final CodeableConcept method;
        @Summary
        private final Decimal truthTP;
        @Summary
        private final Decimal queryTP;
        @Summary
        private final Decimal truthFN;
        @Summary
        private final Decimal queryFP;
        @Summary
        private final Decimal gtFP;
        @Summary
        private final Decimal precision;
        @Summary
        private final Decimal recall;
        @Summary
        private final Decimal fScore;
        @Summary
        private final Roc roc;

        private Quality(Builder builder) {
            super(builder);
            type = builder.type;
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
        }

        /**
         * INDEL / SNP / Undefined variant.
         * 
         * @return
         *     An immutable object of type {@link QualityType} that is non-null.
         */
        public QualityType getType() {
            return type;
        }

        /**
         * Gold standard sequence used for comparing against.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStandardSequence() {
            return standardSequence;
        }

        /**
         * Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is 
         * inclusive.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getStart() {
            return start;
        }

        /**
         * End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last 
         * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getEnd() {
            return end;
        }

        /**
         * The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.
         * org/browser/current_svn/term/SO:0001685)).
         * 
         * @return
         *     An immutable object of type {@link Quantity} that may be null.
         */
        public Quantity getScore() {
            return score;
        }

        /**
         * Which method is used to get sequence quality.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getMethod() {
            return method;
        }

        /**
         * True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there 
         * are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is 
         * an accurate genotype call for the event.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getTruthTP() {
            return truthTP;
        }

        /**
         * True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there 
         * are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is 
         * an accurate genotype call for the event.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getQueryTP() {
            return queryTP;
        }

        /**
         * False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set 
         * that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for 
         * the event. Sites with correct variant but incorrect genotype are counted here.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getTruthFN() {
            return truthFN;
        }

        /**
         * False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set 
         * that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getQueryFP() {
            return queryFP;
        }

        /**
         * The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the 
         * truth is 1/1 and the query is 0/1 or similar).
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getGtFP() {
            return gtFP;
        }

        /**
         * QUERY.TP / (QUERY.TP + QUERY.FP).
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getPrecision() {
            return precision;
        }

        /**
         * TRUTH.TP / (TRUTH.TP + TRUTH.FN).
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getRecall() {
            return recall;
        }

        /**
         * Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getFScore() {
            return fScore;
        }

        /**
         * Receiver Operator Characteristic (ROC) Curve to give sensitivity/specificity tradeoff.
         * 
         * @return
         *     An immutable object of type {@link Roc} that may be null.
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private QualityType type;
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
             * INDEL / SNP / Undefined variant.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     indel | snp | unknown
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(QualityType type) {
                this.type = type;
                return this;
            }

            /**
             * Gold standard sequence used for comparing against.
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
             * Convenience method for setting {@code start}.
             * 
             * @param start
             *     Start position of the sequence
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #start(com.ibm.fhir.model.type.Integer)
             */
            public Builder start(java.lang.Integer start) {
                this.start = (start == null) ? null : Integer.of(start);
                return this;
            }

            /**
             * Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is 
             * inclusive.
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
             * Convenience method for setting {@code end}.
             * 
             * @param end
             *     End position of the sequence
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #end(com.ibm.fhir.model.type.Integer)
             */
            public Builder end(java.lang.Integer end) {
                this.end = (end == null) ? null : Integer.of(end);
                return this;
            }

            /**
             * End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last 
             * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
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
             * The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.
             * org/browser/current_svn/term/SO:0001685)).
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
             * Which method is used to get sequence quality.
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
             * True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there 
             * are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is 
             * an accurate genotype call for the event.
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
             * True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there 
             * are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is 
             * an accurate genotype call for the event.
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
             * False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set 
             * that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for 
             * the event. Sites with correct variant but incorrect genotype are counted here.
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
             * False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set 
             * that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.
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
             * The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the 
             * truth is 1/1 and the query is 0/1 or similar).
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
             * QUERY.TP / (QUERY.TP + QUERY.FP).
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
             * TRUTH.TP / (TRUTH.TP + TRUTH.FN).
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
             * Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).
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
             * Receiver Operator Characteristic (ROC) Curve to give sensitivity/specificity tradeoff.
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

            /**
             * Build the {@link Quality}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Quality}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Quality per the base specification
             */
            @Override
            public Quality build() {
                Quality quality = new Quality(this);
                if (validating) {
                    validate(quality);
                }
                return quality;
            }

            protected void validate(Quality quality) {
                super.validate(quality);
                ValidationSupport.requireNonNull(quality.type, "type");
                ValidationSupport.requireValueOrChildren(quality);
            }

            protected Builder from(Quality quality) {
                super.from(quality);
                type = quality.type;
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
         * Receiver Operator Characteristic (ROC) Curve to give sensitivity/specificity tradeoff.
         */
        public static class Roc extends BackboneElement {
            @Summary
            private final List<Integer> score;
            @Summary
            private final List<Integer> numTP;
            @Summary
            private final List<Integer> numFP;
            @Summary
            private final List<Integer> numFN;
            @Summary
            private final List<Decimal> precision;
            @Summary
            private final List<Decimal> sensitivity;
            @Summary
            private final List<Decimal> fMeasure;

            private Roc(Builder builder) {
                super(builder);
                score = Collections.unmodifiableList(builder.score);
                numTP = Collections.unmodifiableList(builder.numTP);
                numFP = Collections.unmodifiableList(builder.numFP);
                numFN = Collections.unmodifiableList(builder.numFN);
                precision = Collections.unmodifiableList(builder.precision);
                sensitivity = Collections.unmodifiableList(builder.sensitivity);
                fMeasure = Collections.unmodifiableList(builder.fMeasure);
            }

            /**
             * Invidual data point representing the GQ (genotype quality) score threshold.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Integer} that may be empty.
             */
            public List<Integer> getScore() {
                return score;
            }

            /**
             * The number of true positives if the GQ score threshold was set to "score" field value.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Integer} that may be empty.
             */
            public List<Integer> getNumTP() {
                return numTP;
            }

            /**
             * The number of false positives if the GQ score threshold was set to "score" field value.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Integer} that may be empty.
             */
            public List<Integer> getNumFP() {
                return numFP;
            }

            /**
             * The number of false negatives if the GQ score threshold was set to "score" field value.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Integer} that may be empty.
             */
            public List<Integer> getNumFN() {
                return numFN;
            }

            /**
             * Calculated precision if the GQ score threshold was set to "score" field value.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Decimal} that may be empty.
             */
            public List<Decimal> getPrecision() {
                return precision;
            }

            /**
             * Calculated sensitivity if the GQ score threshold was set to "score" field value.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Decimal} that may be empty.
             */
            public List<Decimal> getSensitivity() {
                return sensitivity;
            }

            /**
             * Calculated fScore if the GQ score threshold was set to "score" field value.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Decimal} that may be empty.
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
                 * Convenience method for setting {@code score}.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param score
                 *     Genotype quality score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #score(com.ibm.fhir.model.type.Integer)
                 */
                public Builder score(java.lang.Integer... score) {
                    for (java.lang.Integer value : score) {
                        this.score.add((value == null) ? null : Integer.of(value));
                    }
                    return this;
                }

                /**
                 * Invidual data point representing the GQ (genotype quality) score threshold.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * Invidual data point representing the GQ (genotype quality) score threshold.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param score
                 *     Genotype quality score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder score(Collection<Integer> score) {
                    this.score = new ArrayList<>(score);
                    return this;
                }

                /**
                 * Convenience method for setting {@code numTP}.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param numTP
                 *     Roc score true positive numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #numTP(com.ibm.fhir.model.type.Integer)
                 */
                public Builder numTP(java.lang.Integer... numTP) {
                    for (java.lang.Integer value : numTP) {
                        this.numTP.add((value == null) ? null : Integer.of(value));
                    }
                    return this;
                }

                /**
                 * The number of true positives if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * The number of true positives if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param numTP
                 *     Roc score true positive numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder numTP(Collection<Integer> numTP) {
                    this.numTP = new ArrayList<>(numTP);
                    return this;
                }

                /**
                 * Convenience method for setting {@code numFP}.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param numFP
                 *     Roc score false positive numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #numFP(com.ibm.fhir.model.type.Integer)
                 */
                public Builder numFP(java.lang.Integer... numFP) {
                    for (java.lang.Integer value : numFP) {
                        this.numFP.add((value == null) ? null : Integer.of(value));
                    }
                    return this;
                }

                /**
                 * The number of false positives if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * The number of false positives if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param numFP
                 *     Roc score false positive numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder numFP(Collection<Integer> numFP) {
                    this.numFP = new ArrayList<>(numFP);
                    return this;
                }

                /**
                 * Convenience method for setting {@code numFN}.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param numFN
                 *     Roc score false negative numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #numFN(com.ibm.fhir.model.type.Integer)
                 */
                public Builder numFN(java.lang.Integer... numFN) {
                    for (java.lang.Integer value : numFN) {
                        this.numFN.add((value == null) ? null : Integer.of(value));
                    }
                    return this;
                }

                /**
                 * The number of false negatives if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * The number of false negatives if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param numFN
                 *     Roc score false negative numbers
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder numFN(Collection<Integer> numFN) {
                    this.numFN = new ArrayList<>(numFN);
                    return this;
                }

                /**
                 * Calculated precision if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * Calculated precision if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param precision
                 *     Precision of the GQ score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder precision(Collection<Decimal> precision) {
                    this.precision = new ArrayList<>(precision);
                    return this;
                }

                /**
                 * Calculated sensitivity if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * Calculated sensitivity if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param sensitivity
                 *     Sensitivity of the GQ score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder sensitivity(Collection<Decimal> sensitivity) {
                    this.sensitivity = new ArrayList<>(sensitivity);
                    return this;
                }

                /**
                 * Calculated fScore if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Adds new element(s) to the existing list.
                 * If any of the elements are null, calling {@link #build()} will fail.
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
                 * Calculated fScore if the GQ score threshold was set to "score" field value.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection.
                 * If any of the elements are null, calling {@link #build()} will fail.
                 * 
                 * @param fMeasure
                 *     FScore of the GQ score
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @throws NullPointerException
                 *     If the passed collection is null
                 */
                public Builder fMeasure(Collection<Decimal> fMeasure) {
                    this.fMeasure = new ArrayList<>(fMeasure);
                    return this;
                }

                /**
                 * Build the {@link Roc}
                 * 
                 * @return
                 *     An immutable object of type {@link Roc}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Roc per the base specification
                 */
                @Override
                public Roc build() {
                    Roc roc = new Roc(this);
                    if (validating) {
                        validate(roc);
                    }
                    return roc;
                }

                protected void validate(Roc roc) {
                    super.validate(roc);
                    ValidationSupport.checkList(roc.score, "score", Integer.class);
                    ValidationSupport.checkList(roc.numTP, "numTP", Integer.class);
                    ValidationSupport.checkList(roc.numFP, "numFP", Integer.class);
                    ValidationSupport.checkList(roc.numFN, "numFN", Integer.class);
                    ValidationSupport.checkList(roc.precision, "precision", Decimal.class);
                    ValidationSupport.checkList(roc.sensitivity, "sensitivity", Decimal.class);
                    ValidationSupport.checkList(roc.fMeasure, "fMeasure", Decimal.class);
                    ValidationSupport.requireValueOrChildren(roc);
                }

                protected Builder from(Roc roc) {
                    super.from(roc);
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
     * Configurations of the external repository. The repository shall store target's observedSeq or records related with 
     * target's observedSeq.
     */
    public static class Repository extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "repositoryType",
            strength = BindingStrength.Value.REQUIRED,
            description = "Type for access of external URI.",
            valueSet = "http://hl7.org/fhir/ValueSet/repository-type|4.1.0"
        )
        @Required
        private final RepositoryType type;
        @Summary
        private final Uri url;
        @Summary
        private final String name;
        @Summary
        private final String datasetId;
        @Summary
        private final String variantsetId;
        @Summary
        private final String readsetId;

        private Repository(Builder builder) {
            super(builder);
            type = builder.type;
            url = builder.url;
            name = builder.name;
            datasetId = builder.datasetId;
            variantsetId = builder.variantsetId;
            readsetId = builder.readsetId;
        }

        /**
         * Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.
         * 
         * @return
         *     An immutable object of type {@link RepositoryType} that is non-null.
         */
        public RepositoryType getType() {
            return type;
        }

        /**
         * URI of an external repository which contains further details about the genetics data.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getUrl() {
            return url;
        }

        /**
         * URI of an external repository which contains further details about the genetics data.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * Id of the variant in this external repository. The server will understand how to use this id to call for more info 
         * about datasets in external repository.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDatasetId() {
            return datasetId;
        }

        /**
         * Id of the variantset in this external repository. The server will understand how to use this id to call for more info 
         * about variantsets in external repository.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getVariantsetId() {
            return variantsetId;
        }

        /**
         * Id of the read in this external repository.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
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
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            private RepositoryType type;
            private Uri url;
            private String name;
            private String datasetId;
            private String variantsetId;
            private String readsetId;

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
             * Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     directlink | openapi | login | oauth | other
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(RepositoryType type) {
                this.type = type;
                return this;
            }

            /**
             * URI of an external repository which contains further details about the genetics data.
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
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     Repository's name
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #name(com.ibm.fhir.model.type.String)
             */
            public Builder name(java.lang.String name) {
                this.name = (name == null) ? null : String.of(name);
                return this;
            }

            /**
             * URI of an external repository which contains further details about the genetics data.
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
             * Convenience method for setting {@code datasetId}.
             * 
             * @param datasetId
             *     Id of the dataset that used to call for dataset in repository
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #datasetId(com.ibm.fhir.model.type.String)
             */
            public Builder datasetId(java.lang.String datasetId) {
                this.datasetId = (datasetId == null) ? null : String.of(datasetId);
                return this;
            }

            /**
             * Id of the variant in this external repository. The server will understand how to use this id to call for more info 
             * about datasets in external repository.
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
             * Convenience method for setting {@code variantsetId}.
             * 
             * @param variantsetId
             *     Id of the variantset that used to call for variantset in repository
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #variantsetId(com.ibm.fhir.model.type.String)
             */
            public Builder variantsetId(java.lang.String variantsetId) {
                this.variantsetId = (variantsetId == null) ? null : String.of(variantsetId);
                return this;
            }

            /**
             * Id of the variantset in this external repository. The server will understand how to use this id to call for more info 
             * about variantsets in external repository.
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
             * Convenience method for setting {@code readsetId}.
             * 
             * @param readsetId
             *     Id of the read
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #readsetId(com.ibm.fhir.model.type.String)
             */
            public Builder readsetId(java.lang.String readsetId) {
                this.readsetId = (readsetId == null) ? null : String.of(readsetId);
                return this;
            }

            /**
             * Id of the read in this external repository.
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

            /**
             * Build the {@link Repository}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Repository}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Repository per the base specification
             */
            @Override
            public Repository build() {
                Repository repository = new Repository(this);
                if (validating) {
                    validate(repository);
                }
                return repository;
            }

            protected void validate(Repository repository) {
                super.validate(repository);
                ValidationSupport.requireNonNull(repository.type, "type");
                ValidationSupport.requireValueOrChildren(repository);
            }

            protected Builder from(Repository repository) {
                super.from(repository);
                type = repository.type;
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
     * Information about chromosome structure variation.
     */
    public static class StructureVariant extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "LOINC LL379-9 answerlist",
            strength = BindingStrength.Value.REQUIRED,
            description = "DNA change type.",
            valueSet = "http://loinc.org/vs/LL379-9|4.1.0"
        )
        private final CodeableConcept variantType;
        @Summary
        private final Boolean exact;
        @Summary
        private final Integer length;
        @Summary
        private final Outer outer;
        @Summary
        private final Inner inner;

        private StructureVariant(Builder builder) {
            super(builder);
            variantType = builder.variantType;
            exact = builder.exact;
            length = builder.length;
            outer = builder.outer;
            inner = builder.inner;
        }

        /**
         * Information about chromosome structure variation DNA change type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getVariantType() {
            return variantType;
        }

        /**
         * Used to indicate if the outer and inner start-end values have the same meaning.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getExact() {
            return exact;
        }

        /**
         * Length of the variant chromosome.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getLength() {
            return length;
        }

        /**
         * Structural variant outer.
         * 
         * @return
         *     An immutable object of type {@link Outer} that may be null.
         */
        public Outer getOuter() {
            return outer;
        }

        /**
         * Structural variant inner.
         * 
         * @return
         *     An immutable object of type {@link Inner} that may be null.
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
            private CodeableConcept variantType;
            private Boolean exact;
            private Integer length;
            private Outer outer;
            private Inner inner;

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
             * Information about chromosome structure variation DNA change type.
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
             * Convenience method for setting {@code exact}.
             * 
             * @param exact
             *     Does the structural variant have base pair resolution breakpoints?
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #exact(com.ibm.fhir.model.type.Boolean)
             */
            public Builder exact(java.lang.Boolean exact) {
                this.exact = (exact == null) ? null : Boolean.of(exact);
                return this;
            }

            /**
             * Used to indicate if the outer and inner start-end values have the same meaning.
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
             * Convenience method for setting {@code length}.
             * 
             * @param length
             *     Structural variant length
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #length(com.ibm.fhir.model.type.Integer)
             */
            public Builder length(java.lang.Integer length) {
                this.length = (length == null) ? null : Integer.of(length);
                return this;
            }

            /**
             * Length of the variant chromosome.
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
             * Structural variant outer.
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
             * Structural variant inner.
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

            /**
             * Build the {@link StructureVariant}
             * 
             * @return
             *     An immutable object of type {@link StructureVariant}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid StructureVariant per the base specification
             */
            @Override
            public StructureVariant build() {
                StructureVariant structureVariant = new StructureVariant(this);
                if (validating) {
                    validate(structureVariant);
                }
                return structureVariant;
            }

            protected void validate(StructureVariant structureVariant) {
                super.validate(structureVariant);
                ValidationSupport.requireValueOrChildren(structureVariant);
            }

            protected Builder from(StructureVariant structureVariant) {
                super.from(structureVariant);
                variantType = structureVariant.variantType;
                exact = structureVariant.exact;
                length = structureVariant.length;
                outer = structureVariant.outer;
                inner = structureVariant.inner;
                return this;
            }
        }

        /**
         * Structural variant outer.
         */
        public static class Outer extends BackboneElement {
            @Summary
            private final Integer start;
            @Summary
            private final Integer end;

            private Outer(Builder builder) {
                super(builder);
                start = builder.start;
                end = builder.end;
            }

            /**
             * Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is 
             * inclusive.
             * 
             * @return
             *     An immutable object of type {@link Integer} that may be null.
             */
            public Integer getStart() {
                return start;
            }

            /**
             * Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last 
             * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
             * 
             * @return
             *     An immutable object of type {@link Integer} that may be null.
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
                private Integer start;
                private Integer end;

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
                 * Convenience method for setting {@code start}.
                 * 
                 * @param start
                 *     Structural variant outer start
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #start(com.ibm.fhir.model.type.Integer)
                 */
                public Builder start(java.lang.Integer start) {
                    this.start = (start == null) ? null : Integer.of(start);
                    return this;
                }

                /**
                 * Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is 
                 * inclusive.
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
                 * Convenience method for setting {@code end}.
                 * 
                 * @param end
                 *     Structural variant outer end
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #end(com.ibm.fhir.model.type.Integer)
                 */
                public Builder end(java.lang.Integer end) {
                    this.end = (end == null) ? null : Integer.of(end);
                    return this;
                }

                /**
                 * Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last 
                 * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
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

                /**
                 * Build the {@link Outer}
                 * 
                 * @return
                 *     An immutable object of type {@link Outer}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Outer per the base specification
                 */
                @Override
                public Outer build() {
                    Outer outer = new Outer(this);
                    if (validating) {
                        validate(outer);
                    }
                    return outer;
                }

                protected void validate(Outer outer) {
                    super.validate(outer);
                    ValidationSupport.requireValueOrChildren(outer);
                }

                protected Builder from(Outer outer) {
                    super.from(outer);
                    start = outer.start;
                    end = outer.end;
                    return this;
                }
            }
        }

        /**
         * Structural variant inner.
         */
        public static class Inner extends BackboneElement {
            @Summary
            private final Integer start;
            @Summary
            private final Integer end;

            private Inner(Builder builder) {
                super(builder);
                start = builder.start;
                end = builder.end;
            }

            /**
             * Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is 
             * inclusive.
             * 
             * @return
             *     An immutable object of type {@link Integer} that may be null.
             */
            public Integer getStart() {
                return start;
            }

            /**
             * Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last 
             * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
             * 
             * @return
             *     An immutable object of type {@link Integer} that may be null.
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
                private Integer start;
                private Integer end;

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
                 * Convenience method for setting {@code start}.
                 * 
                 * @param start
                 *     Structural variant inner start
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #start(com.ibm.fhir.model.type.Integer)
                 */
                public Builder start(java.lang.Integer start) {
                    this.start = (start == null) ? null : Integer.of(start);
                    return this;
                }

                /**
                 * Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is 
                 * inclusive.
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
                 * Convenience method for setting {@code end}.
                 * 
                 * @param end
                 *     Structural variant inner end
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #end(com.ibm.fhir.model.type.Integer)
                 */
                public Builder end(java.lang.Integer end) {
                    this.end = (end == null) ? null : Integer.of(end);
                    return this;
                }

                /**
                 * Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last 
                 * position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
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

                /**
                 * Build the {@link Inner}
                 * 
                 * @return
                 *     An immutable object of type {@link Inner}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Inner per the base specification
                 */
                @Override
                public Inner build() {
                    Inner inner = new Inner(this);
                    if (validating) {
                        validate(inner);
                    }
                    return inner;
                }

                protected void validate(Inner inner) {
                    super.validate(inner);
                    ValidationSupport.requireValueOrChildren(inner);
                }

                protected Builder from(Inner inner) {
                    super.from(inner);
                    start = inner.start;
                    end = inner.end;
                    return this;
                }
            }
        }
    }
}
