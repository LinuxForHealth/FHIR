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

import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Nucleic acids are defined by three distinct elements: the base, sugar and linkage. Individual substance/moiety IDs 
 * will be created for each of these elements. The nucleotide sequence will be always entered in the 5’-3’ direction.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class SubstanceNucleicAcid extends DomainResource {
    private final CodeableConcept sequenceType;
    private final Integer numberOfSubunits;
    private final String areaOfHybridisation;
    private final CodeableConcept oligoNucleotideType;
    private final List<Subunit> subunit;

    private SubstanceNucleicAcid(Builder builder) {
        super(builder);
        this.sequenceType = builder.sequenceType;
        this.numberOfSubunits = builder.numberOfSubunits;
        this.areaOfHybridisation = builder.areaOfHybridisation;
        this.oligoNucleotideType = builder.oligoNucleotideType;
        this.subunit = builder.subunit;
    }

    /**
     * <p>
     * The type of the sequence shall be specified based on a controlled vocabulary.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getSequenceType() {
        return sequenceType;
    }

    /**
     * <p>
     * The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would 
     * be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not 
     * specified in the reference source, the assumption is that there is 1 subunit.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Integer}.
     */
    public Integer getNumberOfSubunits() {
        return numberOfSubunits;
    }

    /**
     * <p>
     * The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with 
     * the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” 
     * shall be used as separator as follows: “Subunitnumber Residue”.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getAreaOfHybridisation() {
        return areaOfHybridisation;
    }

    /**
     * <p>
     * (TBC).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getOligoNucleotideType() {
        return oligoNucleotideType;
    }

    /**
     * <p>
     * Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
     * subunits that have identical sequences will be repeated multiple times.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Subunit}.
     */
    public List<Subunit> getSubunit() {
        return subunit;
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
                accept(sequenceType, "sequenceType", visitor);
                accept(numberOfSubunits, "numberOfSubunits", visitor);
                accept(areaOfHybridisation, "areaOfHybridisation", visitor);
                accept(oligoNucleotideType, "oligoNucleotideType", visitor);
                accept(subunit, "subunit", visitor, Subunit.class);
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
        builder.sequenceType = sequenceType;
        builder.numberOfSubunits = numberOfSubunits;
        builder.areaOfHybridisation = areaOfHybridisation;
        builder.oligoNucleotideType = oligoNucleotideType;
        builder.subunit.addAll(subunit);
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        // optional
        private CodeableConcept sequenceType;
        private Integer numberOfSubunits;
        private String areaOfHybridisation;
        private CodeableConcept oligoNucleotideType;
        private List<Subunit> subunit = new ArrayList<>();

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
         * The type of the sequence shall be specified based on a controlled vocabulary.
         * </p>
         * 
         * @param sequenceType
         *     The type of the sequence shall be specified based on a controlled vocabulary
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder sequenceType(CodeableConcept sequenceType) {
            this.sequenceType = sequenceType;
            return this;
        }

        /**
         * <p>
         * The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would 
         * be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not 
         * specified in the reference source, the assumption is that there is 1 subunit.
         * </p>
         * 
         * @param numberOfSubunits
         *     The number of linear sequences of nucleotides linked through phosphodiester bonds shall be described. Subunits would 
         *     be strands of nucleic acids that are tightly associated typically through Watson-Crick base pairing. NOTE: If not 
         *     specified in the reference source, the assumption is that there is 1 subunit
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder numberOfSubunits(Integer numberOfSubunits) {
            this.numberOfSubunits = numberOfSubunits;
            return this;
        }

        /**
         * <p>
         * The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with 
         * the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” 
         * shall be used as separator as follows: “Subunitnumber Residue”.
         * </p>
         * 
         * @param areaOfHybridisation
         *     The area of hybridisation shall be described if applicable for double stranded RNA or DNA. The number associated with 
         *     the subunit followed by the number associated to the residue shall be specified in increasing order. The underscore “” 
         *     shall be used as separator as follows: “Subunitnumber Residue”
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder areaOfHybridisation(String areaOfHybridisation) {
            this.areaOfHybridisation = areaOfHybridisation;
            return this;
        }

        /**
         * <p>
         * (TBC).
         * </p>
         * 
         * @param oligoNucleotideType
         *     (TBC)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder oligoNucleotideType(CodeableConcept oligoNucleotideType) {
            this.oligoNucleotideType = oligoNucleotideType;
            return this;
        }

        /**
         * <p>
         * Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
         * subunits that have identical sequences will be repeated multiple times.
         * </p>
         * 
         * @param subunit
         *     Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
         *     subunits that have identical sequences will be repeated multiple times
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subunit(Subunit... subunit) {
            for (Subunit value : subunit) {
                this.subunit.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
         * subunits that have identical sequences will be repeated multiple times.
         * </p>
         * 
         * @param subunit
         *     Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
         *     subunits that have identical sequences will be repeated multiple times
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subunit(Collection<Subunit> subunit) {
            this.subunit.addAll(subunit);
            return this;
        }

        @Override
        public SubstanceNucleicAcid build() {
            return new SubstanceNucleicAcid(this);
        }
    }

    /**
     * <p>
     * Subunits are listed in order of decreasing length; sequences of the same length will be ordered by molecular weight; 
     * subunits that have identical sequences will be repeated multiple times.
     * </p>
     */
    public static class Subunit extends BackboneElement {
        private final Integer subunit;
        private final String sequence;
        private final Integer length;
        private final Attachment sequenceAttachment;
        private final CodeableConcept fivePrime;
        private final CodeableConcept threePrime;
        private final List<Linkage> linkage;
        private final List<Sugar> sugar;

        private Subunit(Builder builder) {
            super(builder);
            this.subunit = builder.subunit;
            this.sequence = builder.sequence;
            this.length = builder.length;
            this.sequenceAttachment = builder.sequenceAttachment;
            this.fivePrime = builder.fivePrime;
            this.threePrime = builder.threePrime;
            this.linkage = builder.linkage;
            this.sugar = builder.sugar;
        }

        /**
         * <p>
         * Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered 
         * by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Integer}.
         */
        public Integer getSubunit() {
            return subunit;
        }

        /**
         * <p>
         * Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base 
         * sequence, sugar and type of phosphate or non-phosphate linkage should also be captured.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getSequence() {
            return sequence;
        }

        /**
         * <p>
         * The length of the sequence shall be captured.
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
         * (TBC).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Attachment}.
         */
        public Attachment getSequenceAttachment() {
            return sequenceAttachment;
        }

        /**
         * <p>
         * The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
         * represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A 
         * separate representation would be redundant.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getFivePrime() {
            return fivePrime;
        }

        /**
         * <p>
         * The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
         * represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A 
         * separate representation would be redundant.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getThreePrime() {
            return threePrime;
        }

        /**
         * <p>
         * The linkages between sugar residues will also be captured.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Linkage}.
         */
        public List<Linkage> getLinkage() {
            return linkage;
        }

        /**
         * <p>
         * 5.3.6.8.1 Sugar ID (Mandatory).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Sugar}.
         */
        public List<Sugar> getSugar() {
            return sugar;
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
                    accept(subunit, "subunit", visitor);
                    accept(sequence, "sequence", visitor);
                    accept(length, "length", visitor);
                    accept(sequenceAttachment, "sequenceAttachment", visitor);
                    accept(fivePrime, "fivePrime", visitor);
                    accept(threePrime, "threePrime", visitor);
                    accept(linkage, "linkage", visitor, Linkage.class);
                    accept(sugar, "sugar", visitor, Sugar.class);
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
             * Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered 
             * by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts.
             * </p>
             * 
             * @param subunit
             *     Index of linear sequences of nucleic acids in order of decreasing length. Sequences of the same length will be ordered 
             *     by molecular weight. Subunits that have identical sequences will be repeated and have sequential subscripts
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder subunit(Integer subunit) {
                this.subunit = subunit;
                return this;
            }

            /**
             * <p>
             * Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base 
             * sequence, sugar and type of phosphate or non-phosphate linkage should also be captured.
             * </p>
             * 
             * @param sequence
             *     Actual nucleotide sequence notation from 5' to 3' end using standard single letter codes. In addition to the base 
             *     sequence, sugar and type of phosphate or non-phosphate linkage should also be captured
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder sequence(String sequence) {
                this.sequence = sequence;
                return this;
            }

            /**
             * <p>
             * The length of the sequence shall be captured.
             * </p>
             * 
             * @param length
             *     The length of the sequence shall be captured
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder length(Integer length) {
                this.length = length;
                return this;
            }

            /**
             * <p>
             * (TBC).
             * </p>
             * 
             * @param sequenceAttachment
             *     (TBC)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder sequenceAttachment(Attachment sequenceAttachment) {
                this.sequenceAttachment = sequenceAttachment;
                return this;
            }

            /**
             * <p>
             * The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
             * represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A 
             * separate representation would be redundant.
             * </p>
             * 
             * @param fivePrime
             *     The nucleotide present at the 5’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
             *     represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the first position in the sequence. A 
             *     separate representation would be redundant
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder fivePrime(CodeableConcept fivePrime) {
                this.fivePrime = fivePrime;
                return this;
            }

            /**
             * <p>
             * The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
             * represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A 
             * separate representation would be redundant.
             * </p>
             * 
             * @param threePrime
             *     The nucleotide present at the 3’ terminal shall be specified based on a controlled vocabulary. Since the sequence is 
             *     represented from the 5' to the 3' end, the 5’ prime nucleotide is the letter at the last position in the sequence. A 
             *     separate representation would be redundant
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder threePrime(CodeableConcept threePrime) {
                this.threePrime = threePrime;
                return this;
            }

            /**
             * <p>
             * The linkages between sugar residues will also be captured.
             * </p>
             * 
             * @param linkage
             *     The linkages between sugar residues will also be captured
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder linkage(Linkage... linkage) {
                for (Linkage value : linkage) {
                    this.linkage.add(value);
                }
                return this;
            }

            /**
             * <p>
             * The linkages between sugar residues will also be captured.
             * </p>
             * 
             * @param linkage
             *     The linkages between sugar residues will also be captured
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder linkage(Collection<Linkage> linkage) {
                this.linkage.addAll(linkage);
                return this;
            }

            /**
             * <p>
             * 5.3.6.8.1 Sugar ID (Mandatory).
             * </p>
             * 
             * @param sugar
             *     5.3.6.8.1 Sugar ID (Mandatory)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder sugar(Sugar... sugar) {
                for (Sugar value : sugar) {
                    this.sugar.add(value);
                }
                return this;
            }

            /**
             * <p>
             * 5.3.6.8.1 Sugar ID (Mandatory).
             * </p>
             * 
             * @param sugar
             *     5.3.6.8.1 Sugar ID (Mandatory)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder sugar(Collection<Sugar> sugar) {
                this.sugar.addAll(sugar);
                return this;
            }

            @Override
            public Subunit build() {
                return new Subunit(this);
            }

            private static Builder from(Subunit subunit) {
                Builder builder = new Builder();
                builder.id = subunit.id;
                builder.extension.addAll(subunit.extension);
                builder.modifierExtension.addAll(subunit.modifierExtension);
                builder.subunit = subunit.subunit;
                builder.sequence = subunit.sequence;
                builder.length = subunit.length;
                builder.sequenceAttachment = subunit.sequenceAttachment;
                builder.fivePrime = subunit.fivePrime;
                builder.threePrime = subunit.threePrime;
                builder.linkage.addAll(subunit.linkage);
                builder.sugar.addAll(subunit.sugar);
                return builder;
            }
        }

        /**
         * <p>
         * The linkages between sugar residues will also be captured.
         * </p>
         */
        public static class Linkage extends BackboneElement {
            private final String connectivity;
            private final Identifier identifier;
            private final String name;
            private final String residueSite;

            private Linkage(Builder builder) {
                super(builder);
                this.connectivity = builder.connectivity;
                this.identifier = builder.identifier;
                this.name = builder.name;
                this.residueSite = builder.residueSite;
            }

            /**
             * <p>
             * The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic 
             * acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. 
             * Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getConnectivity() {
                return connectivity;
            }

            /**
             * <p>
             * Each linkage will be registered as a fragment and have an ID.
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
             * Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each 
             * linkage.
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
             * Residues shall be captured as described in 5.3.6.8.3.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getResidueSite() {
                return residueSite;
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
                        accept(connectivity, "connectivity", visitor);
                        accept(identifier, "identifier", visitor);
                        accept(name, "name", visitor);
                        accept(residueSite, "residueSite", visitor);
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
                private String connectivity;
                private Identifier identifier;
                private String name;
                private String residueSite;

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
                 * The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic 
                 * acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. 
                 * Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified.
                 * </p>
                 * 
                 * @param connectivity
                 *     The entity that links the sugar residues together should also be captured for nearly all naturally occurring nucleic 
                 *     acid the linkage is a phosphate group. For many synthetic oligonucleotides phosphorothioate linkages are often seen. 
                 *     Linkage connectivity is assumed to be 3’-5’. If the linkage is either 3’-3’ or 5’-5’ this should be specified
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder connectivity(String connectivity) {
                    this.connectivity = connectivity;
                    return this;
                }

                /**
                 * <p>
                 * Each linkage will be registered as a fragment and have an ID.
                 * </p>
                 * 
                 * @param identifier
                 *     Each linkage will be registered as a fragment and have an ID
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder identifier(Identifier identifier) {
                    this.identifier = identifier;
                    return this;
                }

                /**
                 * <p>
                 * Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each 
                 * linkage.
                 * </p>
                 * 
                 * @param name
                 *     Each linkage will be registered as a fragment and have at least one name. A single name shall be assigned to each 
                 *     linkage
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder name(String name) {
                    this.name = name;
                    return this;
                }

                /**
                 * <p>
                 * Residues shall be captured as described in 5.3.6.8.3.
                 * </p>
                 * 
                 * @param residueSite
                 *     Residues shall be captured as described in 5.3.6.8.3
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder residueSite(String residueSite) {
                    this.residueSite = residueSite;
                    return this;
                }

                @Override
                public Linkage build() {
                    return new Linkage(this);
                }

                private static Builder from(Linkage linkage) {
                    Builder builder = new Builder();
                    builder.id = linkage.id;
                    builder.extension.addAll(linkage.extension);
                    builder.modifierExtension.addAll(linkage.modifierExtension);
                    builder.connectivity = linkage.connectivity;
                    builder.identifier = linkage.identifier;
                    builder.name = linkage.name;
                    builder.residueSite = linkage.residueSite;
                    return builder;
                }
            }
        }

        /**
         * <p>
         * 5.3.6.8.1 Sugar ID (Mandatory).
         * </p>
         */
        public static class Sugar extends BackboneElement {
            private final Identifier identifier;
            private final String name;
            private final String residueSite;

            private Sugar(Builder builder) {
                super(builder);
                this.identifier = builder.identifier;
                this.name = builder.name;
                this.residueSite = builder.residueSite;
            }

            /**
             * <p>
             * The Substance ID of the sugar or sugar-like component that make up the nucleotide.
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
             * The name of the sugar or sugar-like component that make up the nucleotide.
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
             * The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3
             * ‘direction consistent with the base sequences listed above.
             * </p>
             * 
             * @return
             *     An immutable object of type {@link String}.
             */
            public String getResidueSite() {
                return residueSite;
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
                        accept(identifier, "identifier", visitor);
                        accept(name, "name", visitor);
                        accept(residueSite, "residueSite", visitor);
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
                private Identifier identifier;
                private String name;
                private String residueSite;

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
                 * The Substance ID of the sugar or sugar-like component that make up the nucleotide.
                 * </p>
                 * 
                 * @param identifier
                 *     The Substance ID of the sugar or sugar-like component that make up the nucleotide
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder identifier(Identifier identifier) {
                    this.identifier = identifier;
                    return this;
                }

                /**
                 * <p>
                 * The name of the sugar or sugar-like component that make up the nucleotide.
                 * </p>
                 * 
                 * @param name
                 *     The name of the sugar or sugar-like component that make up the nucleotide
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder name(String name) {
                    this.name = name;
                    return this;
                }

                /**
                 * <p>
                 * The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3
                 * ‘direction consistent with the base sequences listed above.
                 * </p>
                 * 
                 * @param residueSite
                 *     The residues that contain a given sugar will be captured. The order of given residues will be captured in the 5‘-3
                 *     ‘direction consistent with the base sequences listed above
                 * 
                 * @return
                 *     A reference to this Builder instance.
                 */
                public Builder residueSite(String residueSite) {
                    this.residueSite = residueSite;
                    return this;
                }

                @Override
                public Sugar build() {
                    return new Sugar(this);
                }

                private static Builder from(Sugar sugar) {
                    Builder builder = new Builder();
                    builder.id = sugar.id;
                    builder.extension.addAll(sugar.extension);
                    builder.modifierExtension.addAll(sugar.modifierExtension);
                    builder.identifier = sugar.identifier;
                    builder.name = sugar.name;
                    builder.residueSite = sugar.residueSite;
                    return builder;
                }
            }
        }
    }
}
