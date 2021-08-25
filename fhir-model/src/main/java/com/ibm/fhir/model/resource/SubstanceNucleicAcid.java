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
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Nucleic acids are defined by three distinct elements: the base, sugar and linkage. Individual substance/moiety IDs 
 * will be created for each of these elements. The nucleotide sequence will be always entered in the 5’-3’ direction.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubstanceNucleicAcid extends DomainResource {
    @Summary
    private final CodeableConcept sequenceType;
    @Summary
    private final Integer numberOfSubunits;
    @Summary
    private final String areaOfHybridisation;
    @Summary
    private final CodeableConcept oligoNucleotideType;
    @Summary
    private final List<Subunit> subunit;

    private SubstanceNucleicAcid(Builder builder) {
        super(builder);
        sequenceType = builder.sequenceType;
        numberOfSubunits = builder.numberOfSubunits;
        areaOfHybridisation = builder.areaOfHybridisation;
        oligoNucleotideType = builder.oligoNucleotideType;
        subunit = Collections.unmodifiableList(builder.subunit);
    }

    /**
     * The type of the sequence shall be specified based on a controlled vocabulary.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getSequenceType() {
        return sequenceType;
    }

    /**
     * The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would 
     * be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not 
     * specified in the reference source, the assumption is that there is 1 subunit.
     * 
     * @return
     *     An immutable object of type {@link Integer} that may be null.
     */
    public Integer getNumberOfSubunits() {
        return numberOfSubunits;
    }

    /**
     * The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with 
     * the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” 
     * shall be used as separator as follows: “Subunitnumber Residue”.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getAreaOfHybridisation() {
        return areaOfHybridisation;
    }

    /**
     * (TBC).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getOligoNucleotideType() {
        return oligoNucleotideType;
    }

    /**
     * Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
     * subunits that have identical sequences will be repeated multiple times.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Subunit} that may be empty.
     */
    public List<Subunit> getSubunit() {
        return subunit;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (sequenceType != null) || 
            (numberOfSubunits != null) || 
            (areaOfHybridisation != null) || 
            (oligoNucleotideType != null) || 
            !subunit.isEmpty();
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
                accept(sequenceType, "sequenceType", visitor);
                accept(numberOfSubunits, "numberOfSubunits", visitor);
                accept(areaOfHybridisation, "areaOfHybridisation", visitor);
                accept(oligoNucleotideType, "oligoNucleotideType", visitor);
                accept(subunit, "subunit", visitor, Subunit.class);
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
        SubstanceNucleicAcid other = (SubstanceNucleicAcid) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(sequenceType, other.sequenceType) && 
            Objects.equals(numberOfSubunits, other.numberOfSubunits) && 
            Objects.equals(areaOfHybridisation, other.areaOfHybridisation) && 
            Objects.equals(oligoNucleotideType, other.oligoNucleotideType) && 
            Objects.equals(subunit, other.subunit);
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
                sequenceType, 
                numberOfSubunits, 
                areaOfHybridisation, 
                oligoNucleotideType, 
                subunit);
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
        private CodeableConcept sequenceType;
        private Integer numberOfSubunits;
        private String areaOfHybridisation;
        private CodeableConcept oligoNucleotideType;
        private List<Subunit> subunit = new ArrayList<>();

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
         * The type of the sequence shall be specified based on a controlled vocabulary.
         * 
         * @param sequenceType
         *     The type of the sequence shall be specified based on a controlled vocabulary
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sequenceType(CodeableConcept sequenceType) {
            this.sequenceType = sequenceType;
            return this;
        }

        /**
         * Convenience method for setting {@code numberOfSubunits}.
         * 
         * @param numberOfSubunits
         *     The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would 
         *     be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not 
         *     specified in the reference source, the assumption is that there is 1 subunit
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #numberOfSubunits(com.ibm.fhir.model.type.Integer)
         */
        public Builder numberOfSubunits(java.lang.Integer numberOfSubunits) {
            this.numberOfSubunits = (numberOfSubunits == null) ? null : Integer.of(numberOfSubunits);
            return this;
        }

        /**
         * The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would 
         * be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not 
         * specified in the reference source, the assumption is that there is 1 subunit.
         * 
         * @param numberOfSubunits
         *     The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would 
         *     be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not 
         *     specified in the reference source, the assumption is that there is 1 subunit
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder numberOfSubunits(Integer numberOfSubunits) {
            this.numberOfSubunits = numberOfSubunits;
            return this;
        }

        /**
         * Convenience method for setting {@code areaOfHybridisation}.
         * 
         * @param areaOfHybridisation
         *     The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with 
         *     the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” 
         *     shall be used as separator as follows: “Subunitnumber Residue”
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #areaOfHybridisation(com.ibm.fhir.model.type.String)
         */
        public Builder areaOfHybridisation(java.lang.String areaOfHybridisation) {
            this.areaOfHybridisation = (areaOfHybridisation == null) ? null : String.of(areaOfHybridisation);
            return this;
        }

        /**
         * The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with 
         * the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” 
         * shall be used as separator as follows: “Subunitnumber Residue”.
         * 
         * @param areaOfHybridisation
         *     The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with 
         *     the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” 
         *     shall be used as separator as follows: “Subunitnumber Residue”
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder areaOfHybridisation(String areaOfHybridisation) {
            this.areaOfHybridisation = areaOfHybridisation;
            return this;
        }

        /**
         * (TBC).
         * 
         * @param oligoNucleotideType
         *     (TBC)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder oligoNucleotideType(CodeableConcept oligoNucleotideType) {
            this.oligoNucleotideType = oligoNucleotideType;
            return this;
        }

        /**
         * Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
         * subunits that have identical sequences will be repeated multiple times.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param subunit
         *     Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
         *     subunits that have identical sequences will be repeated multiple times
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subunit(Subunit... subunit) {
            for (Subunit value : subunit) {
                this.subunit.add(value);
            }
            return this;
        }

        /**
         * Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
         * subunits that have identical sequences will be repeated multiple times.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param subunit
         *     Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
         *     subunits that have identical sequences will be repeated multiple times
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder subunit(Collection<Subunit> subunit) {
            this.subunit = new ArrayList<>(subunit);
            return this;
        }

        /**
         * Build the {@link SubstanceNucleicAcid}
         * 
         * @return
         *     An immutable object of type {@link SubstanceNucleicAcid}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SubstanceNucleicAcid per the base specification
         */
        @Override
        public SubstanceNucleicAcid build() {
            SubstanceNucleicAcid substanceNucleicAcid = new SubstanceNucleicAcid(this);
            if (validating) {
                validate(substanceNucleicAcid);
            }
            return substanceNucleicAcid;
        }

        protected void validate(SubstanceNucleicAcid substanceNucleicAcid) {
            super.validate(substanceNucleicAcid);
            ValidationSupport.checkList(substanceNucleicAcid.subunit, "subunit", Subunit.class);
        }

        protected Builder from(SubstanceNucleicAcid substanceNucleicAcid) {
            super.from(substanceNucleicAcid);
            sequenceType = substanceNucleicAcid.sequenceType;
            numberOfSubunits = substanceNucleicAcid.numberOfSubunits;
            areaOfHybridisation = substanceNucleicAcid.areaOfHybridisation;
            oligoNucleotideType = substanceNucleicAcid.oligoNucleotideType;
            subunit.addAll(substanceNucleicAcid.subunit);
            return this;
        }
    }

    /**
     * Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
     * subunits that have identical sequences will be repeated multiple times.
     */
    public static class Subunit extends BackboneElement {
        @Summary
        private final Integer subunit;
        @Summary
        private final String sequence;
        @Summary
        private final Integer length;
        @Summary
        private final Attachment sequenceAttachment;
        @Summary
        private final CodeableConcept fivePrime;
        @Summary
        private final CodeableConcept threePrime;
        @Summary
        private final List<Linkage> linkage;
        @Summary
        private final List<Sugar> sugar;

        private Subunit(Builder builder) {
            super(builder);
            subunit = builder.subunit;
            sequence = builder.sequence;
            length = builder.length;
            sequenceAttachment = builder.sequenceAttachment;
            fivePrime = builder.fivePrime;
            threePrime = builder.threePrime;
            linkage = Collections.unmodifiableList(builder.linkage);
            sugar = Collections.unmodifiableList(builder.sugar);
        }

        /**
         * Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered 
         * by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getSubunit() {
            return subunit;
        }

        /**
         * Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base 
         * sequence, sugar and type of phosphate or non-phosphate linkage should also be captured.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getSequence() {
            return sequence;
        }

        /**
         * The length of the sequence shall be captured.
         * 
         * @return
         *     An immutable object of type {@link Integer} that may be null.
         */
        public Integer getLength() {
            return length;
        }

        /**
         * (TBC).
         * 
         * @return
         *     An immutable object of type {@link Attachment} that may be null.
         */
        public Attachment getSequenceAttachment() {
            return sequenceAttachment;
        }

        /**
         * The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
         * represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A 
         * separate representation would be redundant.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getFivePrime() {
            return fivePrime;
        }

        /**
         * The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
         * represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A 
         * separate representation would be redundant.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getThreePrime() {
            return threePrime;
        }

        /**
         * The linkages between sugar residues will also be captured.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Linkage} that may be empty.
         */
        public List<Linkage> getLinkage() {
            return linkage;
        }

        /**
         * 5.3.6.8.1 Sugar ID (Mandatory).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Sugar} that may be empty.
         */
        public List<Sugar> getSugar() {
            return sugar;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (subunit != null) || 
                (sequence != null) || 
                (length != null) || 
                (sequenceAttachment != null) || 
                (fivePrime != null) || 
                (threePrime != null) || 
                !linkage.isEmpty() || 
                !sugar.isEmpty();
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
                    accept(subunit, "subunit", visitor);
                    accept(sequence, "sequence", visitor);
                    accept(length, "length", visitor);
                    accept(sequenceAttachment, "sequenceAttachment", visitor);
                    accept(fivePrime, "fivePrime", visitor);
                    accept(threePrime, "threePrime", visitor);
                    accept(linkage, "linkage", visitor, Linkage.class);
                    accept(sugar, "sugar", visitor, Sugar.class);
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
            Subunit other = (Subunit) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(subunit, other.subunit) && 
                Objects.equals(sequence, other.sequence) && 
                Objects.equals(length, other.length) && 
                Objects.equals(sequenceAttachment, other.sequenceAttachment) && 
                Objects.equals(fivePrime, other.fivePrime) && 
                Objects.equals(threePrime, other.threePrime) && 
                Objects.equals(linkage, other.linkage) && 
                Objects.equals(sugar, other.sugar);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    subunit, 
                    sequence, 
                    length, 
                    sequenceAttachment, 
                    fivePrime, 
                    threePrime, 
                    linkage, 
                    sugar);
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
            private Integer subunit;
            private String sequence;
            private Integer length;
            private Attachment sequenceAttachment;
            private CodeableConcept fivePrime;
            private CodeableConcept threePrime;
            private List<Linkage> linkage = new ArrayList<>();
            private List<Sugar> sugar = new ArrayList<>();

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
             * Convenience method for setting {@code subunit}.
             * 
             * @param subunit
             *     Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered 
             *     by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #subunit(com.ibm.fhir.model.type.Integer)
             */
            public Builder subunit(java.lang.Integer subunit) {
                this.subunit = (subunit == null) ? null : Integer.of(subunit);
                return this;
            }

            /**
             * Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered 
             * by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.
             * 
             * @param subunit
             *     Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered 
             *     by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder subunit(Integer subunit) {
                this.subunit = subunit;
                return this;
            }

            /**
             * Convenience method for setting {@code sequence}.
             * 
             * @param sequence
             *     Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base 
             *     sequence, sugar and type of phosphate or non-phosphate linkage should also be captured
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #sequence(com.ibm.fhir.model.type.String)
             */
            public Builder sequence(java.lang.String sequence) {
                this.sequence = (sequence == null) ? null : String.of(sequence);
                return this;
            }

            /**
             * Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base 
             * sequence, sugar and type of phosphate or non-phosphate linkage should also be captured.
             * 
             * @param sequence
             *     Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base 
             *     sequence, sugar and type of phosphate or non-phosphate linkage should also be captured
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sequence(String sequence) {
                this.sequence = sequence;
                return this;
            }

            /**
             * Convenience method for setting {@code length}.
             * 
             * @param length
             *     The length of the sequence shall be captured
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
             * The length of the sequence shall be captured.
             * 
             * @param length
             *     The length of the sequence shall be captured
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder length(Integer length) {
                this.length = length;
                return this;
            }

            /**
             * (TBC).
             * 
             * @param sequenceAttachment
             *     (TBC)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sequenceAttachment(Attachment sequenceAttachment) {
                this.sequenceAttachment = sequenceAttachment;
                return this;
            }

            /**
             * The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
             * represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A 
             * separate representation would be redundant.
             * 
             * @param fivePrime
             *     The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
             *     represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A 
             *     separate representation would be redundant
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder fivePrime(CodeableConcept fivePrime) {
                this.fivePrime = fivePrime;
                return this;
            }

            /**
             * The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
             * represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A 
             * separate representation would be redundant.
             * 
             * @param threePrime
             *     The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
             *     represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A 
             *     separate representation would be redundant
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder threePrime(CodeableConcept threePrime) {
                this.threePrime = threePrime;
                return this;
            }

            /**
             * The linkages between sugar residues will also be captured.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param linkage
             *     The linkages between sugar residues will also be captured
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder linkage(Linkage... linkage) {
                for (Linkage value : linkage) {
                    this.linkage.add(value);
                }
                return this;
            }

            /**
             * The linkages between sugar residues will also be captured.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param linkage
             *     The linkages between sugar residues will also be captured
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder linkage(Collection<Linkage> linkage) {
                this.linkage = new ArrayList<>(linkage);
                return this;
            }

            /**
             * 5.3.6.8.1 Sugar ID (Mandatory).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param sugar
             *     5.3.6.8.1 Sugar ID (Mandatory)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sugar(Sugar... sugar) {
                for (Sugar value : sugar) {
                    this.sugar.add(value);
                }
                return this;
            }

            /**
             * 5.3.6.8.1 Sugar ID (Mandatory).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param sugar
             *     5.3.6.8.1 Sugar ID (Mandatory)
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder sugar(Collection<Sugar> sugar) {
                this.sugar = new ArrayList<>(sugar);
                return this;
            }

            /**
             * Build the {@link Subunit}
             * 
             * @return
             *     An immutable object of type {@link Subunit}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Subunit per the base specification
             */
            @Override
            public Subunit build() {
                Subunit subunit = new Subunit(this);
                if (validating) {
                    validate(subunit);
                }
                return subunit;
            }

            protected void validate(Subunit subunit) {
                super.validate(subunit);
                ValidationSupport.checkList(subunit.linkage, "linkage", Linkage.class);
                ValidationSupport.checkList(subunit.sugar, "sugar", Sugar.class);
                ValidationSupport.requireValueOrChildren(subunit);
            }

            protected Builder from(Subunit subunit) {
                super.from(subunit);
                this.subunit = subunit.subunit;
                sequence = subunit.sequence;
                length = subunit.length;
                sequenceAttachment = subunit.sequenceAttachment;
                fivePrime = subunit.fivePrime;
                threePrime = subunit.threePrime;
                linkage.addAll(subunit.linkage);
                sugar.addAll(subunit.sugar);
                return this;
            }
        }

        /**
         * The linkages between sugar residues will also be captured.
         */
        public static class Linkage extends BackboneElement {
            @Summary
            private final String connectivity;
            @Summary
            private final Identifier identifier;
            @Summary
            private final String name;
            @Summary
            private final String residueSite;

            private Linkage(Builder builder) {
                super(builder);
                connectivity = builder.connectivity;
                identifier = builder.identifier;
                name = builder.name;
                residueSite = builder.residueSite;
            }

            /**
             * The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic 
             * acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. 
             * Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getConnectivity() {
                return connectivity;
            }

            /**
             * Each linkage will be registered as a fragment and have an ID.
             * 
             * @return
             *     An immutable object of type {@link Identifier} that may be null.
             */
            public Identifier getIdentifier() {
                return identifier;
            }

            /**
             * Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each 
             * linkage.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getName() {
                return name;
            }

            /**
             * Residues shall be captured as described in 5.3.6.8.3.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getResidueSite() {
                return residueSite;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (connectivity != null) || 
                    (identifier != null) || 
                    (name != null) || 
                    (residueSite != null);
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
                        accept(connectivity, "connectivity", visitor);
                        accept(identifier, "identifier", visitor);
                        accept(name, "name", visitor);
                        accept(residueSite, "residueSite", visitor);
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
                Linkage other = (Linkage) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(connectivity, other.connectivity) && 
                    Objects.equals(identifier, other.identifier) && 
                    Objects.equals(name, other.name) && 
                    Objects.equals(residueSite, other.residueSite);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        connectivity, 
                        identifier, 
                        name, 
                        residueSite);
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
                private String connectivity;
                private Identifier identifier;
                private String name;
                private String residueSite;

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
                 * Convenience method for setting {@code connectivity}.
                 * 
                 * @param connectivity
                 *     The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic 
                 *     acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. 
                 *     Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #connectivity(com.ibm.fhir.model.type.String)
                 */
                public Builder connectivity(java.lang.String connectivity) {
                    this.connectivity = (connectivity == null) ? null : String.of(connectivity);
                    return this;
                }

                /**
                 * The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic 
                 * acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. 
                 * Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified.
                 * 
                 * @param connectivity
                 *     The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic 
                 *     acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. 
                 *     Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder connectivity(String connectivity) {
                    this.connectivity = connectivity;
                    return this;
                }

                /**
                 * Each linkage will be registered as a fragment and have an ID.
                 * 
                 * @param identifier
                 *     Each linkage will be registered as a fragment and have an ID
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder identifier(Identifier identifier) {
                    this.identifier = identifier;
                    return this;
                }

                /**
                 * Convenience method for setting {@code name}.
                 * 
                 * @param name
                 *     Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each 
                 *     linkage
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
                 * Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each 
                 * linkage.
                 * 
                 * @param name
                 *     Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each 
                 *     linkage
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder name(String name) {
                    this.name = name;
                    return this;
                }

                /**
                 * Convenience method for setting {@code residueSite}.
                 * 
                 * @param residueSite
                 *     Residues shall be captured as described in 5.3.6.8.3
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #residueSite(com.ibm.fhir.model.type.String)
                 */
                public Builder residueSite(java.lang.String residueSite) {
                    this.residueSite = (residueSite == null) ? null : String.of(residueSite);
                    return this;
                }

                /**
                 * Residues shall be captured as described in 5.3.6.8.3.
                 * 
                 * @param residueSite
                 *     Residues shall be captured as described in 5.3.6.8.3
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder residueSite(String residueSite) {
                    this.residueSite = residueSite;
                    return this;
                }

                /**
                 * Build the {@link Linkage}
                 * 
                 * @return
                 *     An immutable object of type {@link Linkage}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Linkage per the base specification
                 */
                @Override
                public Linkage build() {
                    Linkage linkage = new Linkage(this);
                    if (validating) {
                        validate(linkage);
                    }
                    return linkage;
                }

                protected void validate(Linkage linkage) {
                    super.validate(linkage);
                    ValidationSupport.requireValueOrChildren(linkage);
                }

                protected Builder from(Linkage linkage) {
                    super.from(linkage);
                    connectivity = linkage.connectivity;
                    identifier = linkage.identifier;
                    name = linkage.name;
                    residueSite = linkage.residueSite;
                    return this;
                }
            }
        }

        /**
         * 5.3.6.8.1 Sugar ID (Mandatory).
         */
        public static class Sugar extends BackboneElement {
            @Summary
            private final Identifier identifier;
            @Summary
            private final String name;
            @Summary
            private final String residueSite;

            private Sugar(Builder builder) {
                super(builder);
                identifier = builder.identifier;
                name = builder.name;
                residueSite = builder.residueSite;
            }

            /**
             * The Substance ID of the sugar or sugar-like component that make up the nucleotide.
             * 
             * @return
             *     An immutable object of type {@link Identifier} that may be null.
             */
            public Identifier getIdentifier() {
                return identifier;
            }

            /**
             * The name of the sugar or sugar-like component that make up the nucleotide.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getName() {
                return name;
            }

            /**
             * The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3
             * ‘direction consistent with the base sequences listed above.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getResidueSite() {
                return residueSite;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (identifier != null) || 
                    (name != null) || 
                    (residueSite != null);
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
                        accept(identifier, "identifier", visitor);
                        accept(name, "name", visitor);
                        accept(residueSite, "residueSite", visitor);
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
                Sugar other = (Sugar) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(identifier, other.identifier) && 
                    Objects.equals(name, other.name) && 
                    Objects.equals(residueSite, other.residueSite);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        identifier, 
                        name, 
                        residueSite);
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
                private Identifier identifier;
                private String name;
                private String residueSite;

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
                 * The Substance ID of the sugar or sugar-like component that make up the nucleotide.
                 * 
                 * @param identifier
                 *     The Substance ID of the sugar or sugar-like component that make up the nucleotide
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder identifier(Identifier identifier) {
                    this.identifier = identifier;
                    return this;
                }

                /**
                 * Convenience method for setting {@code name}.
                 * 
                 * @param name
                 *     The name of the sugar or sugar-like component that make up the nucleotide
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
                 * The name of the sugar or sugar-like component that make up the nucleotide.
                 * 
                 * @param name
                 *     The name of the sugar or sugar-like component that make up the nucleotide
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder name(String name) {
                    this.name = name;
                    return this;
                }

                /**
                 * Convenience method for setting {@code residueSite}.
                 * 
                 * @param residueSite
                 *     The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3
                 *     ‘direction consistent with the base sequences listed above
                 * 
                 * @return
                 *     A reference to this Builder instance
                 * 
                 * @see #residueSite(com.ibm.fhir.model.type.String)
                 */
                public Builder residueSite(java.lang.String residueSite) {
                    this.residueSite = (residueSite == null) ? null : String.of(residueSite);
                    return this;
                }

                /**
                 * The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3
                 * ‘direction consistent with the base sequences listed above.
                 * 
                 * @param residueSite
                 *     The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3
                 *     ‘direction consistent with the base sequences listed above
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder residueSite(String residueSite) {
                    this.residueSite = residueSite;
                    return this;
                }

                /**
                 * Build the {@link Sugar}
                 * 
                 * @return
                 *     An immutable object of type {@link Sugar}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Sugar per the base specification
                 */
                @Override
                public Sugar build() {
                    Sugar sugar = new Sugar(this);
                    if (validating) {
                        validate(sugar);
                    }
                    return sugar;
                }

                protected void validate(Sugar sugar) {
                    super.validate(sugar);
                    ValidationSupport.requireValueOrChildren(sugar);
                }

                protected Builder from(Sugar sugar) {
                    super.from(sugar);
                    identifier = sugar.identifier;
                    name = sugar.name;
                    residueSite = sugar.residueSite;
                    return this;
                }
            }
        }
    }
}
